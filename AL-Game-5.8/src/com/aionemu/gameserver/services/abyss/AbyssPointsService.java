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
package com.aionemu.gameserver.services.abyss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_EDIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 * @author ThunderBolt - GloryPoints
 */
public class AbyssPointsService {

	private static final Logger log = LoggerFactory.getLogger(AbyssPointsService.class);

	@GlobalCallback(AddAPGlobalCallback.class)
	public static void addAp(Player player, VisibleObject obj, int value) {
		if (value > 30000) {
			log.warn("WARN BIG COUNT AP: " + value + " name: " + obj.getName() + " obj: " + obj.getObjectId() + " player: " + player.getObjectId());
		}
		addAp(player, value);
	}

	@GlobalCallback(AddGPGlobalCallback.class)
	public static void addGp(Player player, VisibleObject obj, int value) {
		if (value > 10000) {
			log.warn("WARN BIG COUNT GP: " + value + " name: " + obj.getName() + " obj: " + obj.getObjectId() + " player: " + player.getObjectId());
		}
		addGp(player, value);
	}

	public static void addAp(Player player, int value) {
		if (player == null) {
			return;
		}

		// Notify player of AP gained (This should happen before setAp happens.)
		if (value > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_COMBAT_MY_ABYSS_POINT_GAIN(value));
		}
		else // You used %num0 Abyss Points.
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300965, value * -1));
		}

		// Set the new AP value
		setAp(player, value);

		// Add Abyss Points to Legion
		if (player.isLegionMember() && value > 0) {
			player.getLegion().addContributionPoints(value);
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_EDIT(0x03, player.getLegion()));
		}
	}

	public static void addGp(Player player, int value) {
		if (player == null) {
			return;
		}
		if (value > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GLORY_POINT_GAIN(value));
		}
		else if (value < 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GLORY_POINT_LOSE_COMMON(value));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402219, value * -1));
		}
		setGp(player, value);
	}

	/**
	 * @param player
	 * @param value
	 */
	public static void setAp(Player player, int value) {
		if (player == null) {
			return;
		}

		AbyssRank rank = player.getAbyssRank();

		AbyssRankEnum oldAbyssRank = rank.getRank();
		rank.addAp(value);
		AbyssRankEnum newAbyssRank = rank.getRank();

		checkRankChanged(player, oldAbyssRank, newAbyssRank);

		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
	}

	public static void setGp(Player player, int value) {
		if (player == null) {
			return;
		}

		AbyssRank rank = player.getAbyssRank();

		AbyssRankEnum oldAbyssRank = rank.getRank();
		rank.addGp(value);
		AbyssRankEnum newAbyssRank = rank.getRank();

		checkRankGpChanged(player, oldAbyssRank, newAbyssRank);

		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
	}

	/**
	 * @param player
	 * @param oldAbyssRank
	 * @param newAbyssRank
	 */
	public static void checkRankChanged(Player player, AbyssRankEnum oldAbyssRank, AbyssRankEnum newAbyssRank) {
		if (oldAbyssRank == newAbyssRank) {
			return;
		}

		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ABYSS_RANK_UPDATE(0, player));

		player.getEquipment().checkRankLimitItems();
		AbyssSkillService.updateSkills(player);
	}

	/**
	 * @param player
	 * @param oldGloryRank
	 * @param newGloryRank
	 */
	public static void checkRankGpChanged(Player player, AbyssRankEnum oldGloryRank, AbyssRankEnum newGloryRank) {
		if (oldGloryRank == newGloryRank) {
			return;
		}

		Player onlinePlayer = World.getInstance().findPlayer(player.getName());

		if (onlinePlayer != null) {
			AbyssRank abyssRank = onlinePlayer.getAbyssRank();

			// Don't update Rank if Governor, it will become auto governor
			if (abyssRank.getGp() >= AbyssRankEnum.SUPREME_COMMANDER.getRequiredGp()) {
				log.error("Player " + player.getName() + " GP more than GP in abyssRankGP =  " + abyssRank.getGp());
			}
			else {
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ABYSS_RANK_UPDATE(0, player));

				player.getEquipment().checkRankLimitItems();
				AbyssSkillService.updateSkills(player);
			}
		}
	}
    
    // TODO - Please recheck
	public static void AbyssRankCheck (Player player) {
		if (player == null) {
			return;
		} if (player.getAbyssRank().getGp() < AbyssRankEnum.STAR1_OFFICER.getRequiredGp()) {
			if (player.getAbyssRank().getAp() < 1200) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE9_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 1200 && player.getAbyssRank().getAp() < 4220) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE8_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 4220 && player.getAbyssRank().getAp() < 10990) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE7_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 10990 && player.getAbyssRank().getAp() < 23500) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE6_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 23500 && player.getAbyssRank().getAp() < 42780) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE5_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 42780 && player.getAbyssRank().getAp() < 69700) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE4_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 69700 && player.getAbyssRank().getAp() < 105600) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE3_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 105600 && player.getAbyssRank().getAp() < 150800) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE2_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 150800) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE1_SOLDIER);
			}
			PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
		}
	}

	@SuppressWarnings("rawtypes")
	public abstract static class AddAPGlobalCallback implements Callback {

		@Override
		public CallbackResult beforeCall(Object obj, Object[] args) {
			return CallbackResult.newContinue();
		}

		@Override
		public CallbackResult afterCall(Object obj, Object[] args, Object methodResult) {
			Player player = (Player) args[0];
			VisibleObject creature = (VisibleObject) args[1];
			int abyssPoints = (Integer) args[2];

			// Only Players or SiegeNpc(from SiegeModType.SIEGE or .ASSAULT) can add points
			if (creature instanceof Player) {
				onAbyssPointsAdded(player, abyssPoints);
			}
			else if (creature instanceof SiegeNpc) {
				if (!((SiegeNpc) creature).getSpawn().isPeace()) {
					onAbyssPointsAdded(player, abyssPoints);
				}
			}

			return CallbackResult.newContinue();
		}

		@Override
		public Class<? extends Callback> getBaseClass() {
			return AddAPGlobalCallback.class;
		}

		public abstract void onAbyssPointsAdded(Player player, int abyssPoints);
	}

	@SuppressWarnings("rawtypes")
	public abstract static class AddGPGlobalCallback implements Callback {

		@Override
		public CallbackResult beforeCall(Object obj, Object[] args) {
			return CallbackResult.newContinue();
		}

		@Override
		public CallbackResult afterCall(Object obj, Object[] args, Object methodResult) {
			Player player = (Player) args[0];
			VisibleObject creature = (VisibleObject) args[1];
			int gloryPoints = (Integer) args[2];
			if ((creature instanceof Player)) {
				onGloryPointsAdded(player, gloryPoints);
			}
			else if (((creature instanceof SiegeNpc)) && (!((SiegeNpc) creature).getSpawn().isPeace())) {
				onGloryPointsAdded(player, gloryPoints);
			}
			return CallbackResult.newContinue();
		}

		@Override
		public Class<? extends Callback> getBaseClass() {
			return AddGPGlobalCallback.class;
		}

		public abstract void onGloryPointsAdded(Player player, int gloryPoints);
	}
}
