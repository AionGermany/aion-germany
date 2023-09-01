/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects;

import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.KiskStatsTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_KISK_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;
import javolution.util.FastSet;

/**
 * @author Sarynth, nrg
 */
public class Kisk extends SummonedObject<Player> {

	private final Legion ownerLegion;
	private final Race ownerRace;
	private KiskStatsTemplate kiskStatsTemplate;
	private int remainingResurrections;
	private long kiskSpawnTime;
	public final int KISK_LIFETIME_IN_SEC = 2 * 60 * 60; // 2 hours
	private final Set<Integer> kiskMemberIds;

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Kisk(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate npcTemplate, Player owner) {
		super(objId, controller, spawnTemplate, npcTemplate, npcTemplate.getLevel());

		this.kiskStatsTemplate = npcTemplate.getKiskStatsTemplate();

		if (this.kiskStatsTemplate == null) {
			this.kiskStatsTemplate = new KiskStatsTemplate();
		}

		this.kiskMemberIds = new FastSet<Integer>(kiskStatsTemplate.getMaxMembers());
		this.remainingResurrections = this.kiskStatsTemplate.getMaxResurrects();
		this.kiskSpawnTime = System.currentTimeMillis() / 1000;
		this.ownerLegion = owner.getLegion();
		this.ownerRace = owner.getRace();
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return creature.isEnemyFrom(this);
	}

	/**
	 * Required so that the enemy race can attack the Kisk!
	 */
	@Override
	public boolean isEnemyFrom(Player player) {
		// int worldId = getPosition().getMapId();
		// if (worldId == 600020000 || worldId == 600030000) {
		// if (!isInsideZoneType(ZoneType.PVP)) {
		// return false;
		// }
		// }
		return player.getRace() != this.ownerRace;
	}

	/**
	 * @return NpcObjectType.NORMAL
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.NORMAL;
	}

	/**
	 * 1 ~ race 2 ~ legion 3 ~ solo 4 ~ group 5 ~ alliance
	 *
	 * @return useMask
	 */
	public int getUseMask() {
		return this.kiskStatsTemplate.getUseMask();
	}

	public List<Player> getCurrentMemberList() {
		List<Player> currentMemberList = new FastList<Player>();

		for (int memberId : this.kiskMemberIds) {
			Player member = World.getInstance().findPlayer(memberId);
			if (member != null) {
				currentMemberList.add(member);
			}
		}

		return currentMemberList;
	}

	/**
	 * @return
	 */
	public int getCurrentMemberCount() {
		return this.kiskMemberIds.size();
	}

	public Set<Integer> getCurrentMemberIds() {
		return this.kiskMemberIds;
	}

	/**
	 * @return
	 */
	public int getMaxMembers() {
		return this.kiskStatsTemplate.getMaxMembers();
	}

	/**
	 * @return
	 */
	public int getRemainingResurrects() {
		return this.remainingResurrections;
	}

	/**
	 * @return
	 */
	public int getMaxRessurects() {
		return this.kiskStatsTemplate.getMaxResurrects();
	}

	/**
	 * @return
	 */
	public int getRemainingLifetime() {
		long timeElapsed = (System.currentTimeMillis() / 1000) - kiskSpawnTime;
		int timeRemaining = (int) (KISK_LIFETIME_IN_SEC - timeElapsed);
		return (timeRemaining > 0 ? timeRemaining : 0);
	}

	/**
	 * @param player
	 * @return
	 */
	public boolean canBind(Player player) {
		if (!player.getName().equals(getMasterName())) {
			// Check if they fit the usemask
			switch (this.getUseMask()) {
				case 0:
				case 1: // Race
					if (this.ownerRace != player.getRace()) {
						return false;
					}
					break;

				case 2: // Legion
					if (ownerLegion == null || !ownerLegion.isMember(player.getObjectId())) {
						return false;
					}
					break;
				case 3: // Solo
					return false; // Already Checked Name

				case 4: // Group (PlayerGroup or PlayerAllianceGroup)
					if (!player.isInTeam() || !player.getCurrentGroup().hasMember(getCreatorId())) {
						return false;
					}
					break;
				case 5: // Alliance
					if (!player.isInTeam() || (player.isInAlliance2() && !player.getPlayerAlliance2().hasMember(getCreatorId())) || (player.isInGroup2() && !player.getPlayerGroup2().hasMember(getCreatorId()))) {
						return false;
					}
					break;

				default:
					return false;
			}
		}

		if (this.getCurrentMemberCount() >= getMaxMembers()) {
			return false;
		}

		return true;
	}

	/**
	 * @param player
	 */
	public synchronized void addPlayer(Player player) {
		if (kiskMemberIds.add(player.getObjectId())) {
			this.broadcastKiskUpdate();
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_KISK_UPDATE(this));
		}
		player.setKisk(this);
	}

	/**
	 * @param player
	 */
	public synchronized void removePlayer(Player player) {
		player.setKisk(null);
		if (kiskMemberIds.remove(player.getObjectId())) {
			this.broadcastKiskUpdate();
		}
	}

	/**
	 * Sends SM_KISK_UPDATE to each member
	 */
	private void broadcastKiskUpdate() {
		// on all members, but not the ones in knownlist, they will receive the update in the next step
		for (Player member : this.getCurrentMemberList()) {
			if (!this.getKnownList().knowns(member)) {
				PacketSendUtility.sendPacket(member, new SM_KISK_UPDATE(this));
			}
		}

		final Kisk kisk = this;
		// all players having the same race in knownlist
		getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				// Logic to prevent enemy race from knowing kisk information.
				if (object.getRace() == ownerRace) {
					PacketSendUtility.sendPacket(object, new SM_KISK_UPDATE(kisk));
				}
			}
		});
	}

	/**
	 * @param message
	 */
	public void broadcastPacket(SM_SYSTEM_MESSAGE message) {
		for (Player member : this.getCurrentMemberList()) {
			if (member != null) {
				PacketSendUtility.sendPacket(member, message);
			}
		}
	}

	/**
	 * @param player
	 */
	public void resurrectionUsed() {
		remainingResurrections--;
		broadcastKiskUpdate();
		if (remainingResurrections <= 0) {
			this.getController().onDelete();
		}
	}

	/**
	 * @return ownerRace
	 */
	public Race getOwnerRace() {
		return this.ownerRace;
	}

	public boolean isActive() {
		return !this.getLifeStats().isAlreadyDead() && this.getRemainingResurrects() > 0;
	}
}
