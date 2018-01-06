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
package com.aionemu.gameserver.services.vortexservice;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.model.vortex.VortexStateType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author Source
 */
public class Invasion extends DimensionalVortex<VortexLocation> {

	PlayerAlliance invAlliance, defAlliance;
	protected FastMap<Integer, Player> invaders = new FastMap<Integer, Player>();
	protected FastMap<Integer, Player> defenders = new FastMap<Integer, Player>();

	public Invasion(VortexLocation vortex) {
		super(vortex);
	}

	@Override
	public void startInvasion() {
		getVortexLocation().setActiveVortex(this);
		despawn();
		spawn(VortexStateType.INVASION);
		initRiftGenerator();
		updateAlliance();
	}

	@Override
	public void stopInvasion() {
		getVortexLocation().setActiveVortex(null);
		unregisterSiegeBossListeners();
		for (Kisk kisk : getVortexLocation().getInvadersKisks().values()) {
			kisk.getController().die();
		}
		for (Player invader : invaders.values()) {
			if (invader.isOnline()) {
				kickPlayer(invader, true);
			}
		}
		despawn();
		spawn(VortexStateType.PEACE);
	}

	@Override
	public void addPlayer(Player player, boolean isInvader) {
		FastMap<Integer, Player> list = isInvader ? invaders : defenders;
		PlayerAlliance alliance = isInvader ? invAlliance : defAlliance;

		if (alliance != null && alliance.size() > 0) {
			PlayerAllianceService.addPlayer(alliance, player);
		}
		else if (!list.isEmpty()) {
			Player first = null;

			for (Player firstOne : list.values()) {
				if (firstOne.isInGroup2()) {
					PlayerGroupService.removePlayer(firstOne);
				}
				else if (firstOne.isInAlliance2()) {
					PlayerAllianceService.removePlayer(firstOne);
				}
				first = firstOne;
			}

			if (first.getObjectId() != player.getObjectId()) {
				if (isInvader) {
					invAlliance = PlayerAllianceService.createAlliance(first, player, TeamType.ALLIANCE_OFFENCE);
				}
				else {
					defAlliance = PlayerAllianceService.createAlliance(first, player, TeamType.ALLIANCE_DEFENCE);
				}
			}
			else {
				kickPlayer(player, isInvader);
			}
		}
		list.putEntry(player.getObjectId(), player);
	}

	@Override
	public void kickPlayer(Player player, boolean isInvader) {
		FastMap<Integer, Player> list = isInvader ? invaders : defenders;
		PlayerAlliance alliance = isInvader ? invAlliance : defAlliance;

		list.remove(player.getObjectId());

		if (alliance != null && alliance.hasMember(player.getObjectId())) {
			if (player.isOnline()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(isInvader ? 1401452 : 1401476));
			}
			PlayerAllianceService.removePlayer(player);
			if (alliance.size() == 0) {
				if (isInvader) {
					invAlliance = null;
				}
				else {
					defAlliance = null;
				}
			}
		}

		if (isInvader && player.isOnline() && player.getWorldId() == getVortexLocation().getInvasionWorldId()) {
			// You will be returned to where you entered.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401474));
			TeleportService2.teleportTo(player, getVortexLocation().getHomePoint());
		}

		getVortexLocation().getVortexController().getPassedPlayers().remove(player.getObjectId());
		getVortexLocation().getVortexController().syncPassed(true);
	}

	@Override
	public void updateDefenders(Player defender) {
		if (defenders.containsKey(defender.getObjectId())) {
			return;
		}

		if (defAlliance == null || !defAlliance.isFull()) {
			RequestResponseHandler responseHandler = new RequestResponseHandler(defender) {

				@Override
				public void acceptRequest(Creature requester, Player responder) {
					if (responder.isInGroup2()) {
						PlayerGroupService.removePlayer(responder);
					}
					else if (responder.isInAlliance2()) {
						PlayerAllianceService.removePlayer(responder);
					}

					if (defAlliance == null || !defAlliance.isFull()) {
						addPlayer(responder, false);
					}
				}

				@Override
				public void denyRequest(Creature requester, Player responder) {
					// do nothing
				}
			};

			boolean requested = defender.getResponseRequester().putRequest(904306, responseHandler);
			if (requested) {
				PacketSendUtility.sendPacket(defender, new SM_QUESTION_WINDOW(904306, 0, 0));
			}
		}
	}

	@Override
	public void updateInvaders(Player invader) {
		if (invaders.containsKey(invader.getObjectId())) {
			return;
		}

		addPlayer(invader, true);
	}

	private void updateAlliance() {
		for (Player player : getVortexLocation().getPlayers().values()) {
			if (player.getRace().equals(getVortexLocation().getDefendersRace())) {
				updateDefenders(player);
			}
		}
	}

	@Override
	public FastMap<Integer, Player> getInvaders() {
		return invaders;
	}

	@Override
	public FastMap<Integer, Player> getDefenders() {
		return defenders;
	}
}
