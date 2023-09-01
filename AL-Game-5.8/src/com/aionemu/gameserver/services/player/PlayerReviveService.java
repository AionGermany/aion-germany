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
package com.aionemu.gameserver.services.player;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.common.legacy.GroupEvent;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.WorldType;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Jego, xTz
 */
public class PlayerReviveService {

	public static final void duelRevive(Player player) {
		revive(player, 30, 30, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		player.unsetResPosState();
	}

	public static final void skillRevive(Player player) {
		if (!(player.getResStatus())) {
			cancelRes(player);
			return;
		}
		revive(player, 10, 10, true, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		// if player was flying before res, start flying
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();

		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}

		if (player.isInResPostState()) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		// unset isflyingbeforedeath
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void rebirthRevive(Player player) {
		if (!player.canUseRebirthRevive()) {
			return;
		}
		if (player.getRebirthResurrectPercent() <= 0) {
			PacketSendUtility.sendMessage(player, "Error: Rebirth effect missing percent.");
			player.setRebirthResurrectPercent(5);
		}
		boolean soulSickness = true;
		int rebirthResurrectPercent = player.getRebirthResurrectPercent();
		if (player.getAccessLevel() >= AdminConfig.ADMIN_AUTO_RES) {
			rebirthResurrectPercent = 100;
			soulSickness = false;
		}
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		revive(player, rebirthResurrectPercent, rebirthResurrectPercent, soulSickness, player.getRebirthSkill());
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();

		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		player.unsetResPosState();

		// if player was flying before res, start flying
		// unset isflyingbeforedeath
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void bindRevive(Player player) {
		bindRevive(player, 0);
	}

	public static final void bindRevive(Player player, int skillId) {
		revive(player, 25, 25, true, skillId);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		else {
			boolean isInvadeActiveVortex = false;
			for (VortexLocation loc : VortexService.getInstance().getVortexLocations().values()) {
				isInvadeActiveVortex = loc.isInsideActiveVortex(player) && player.getRace().equals(loc.getInvadersRace());
				if (isInvadeActiveVortex) {
					TeleportService2.teleportTo(player, loc.getResurrectionPoint());
				}
			}

			if (!isInvadeActiveVortex) {
				TeleportService2.moveToBindLocation(player, true);
			}
		}
		player.unsetResPosState();
	}

	public static final void inCurrentWorldRevive(Player player) {
		inCurrentWorldRevive(player, 0);
	}

	public static final void inCurrentWorldRevive(Player player, int skillId) { // TODO
		revive(player, 25, 25, true, skillId);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}

		if (player.getWorldType() == WorldType.ELYSEA || player.getWorldType() == WorldType.ASMODAE || player.getWorldType() == WorldType.BALAUREA) {
			switch (player.getRace().getRaceId()) {
				case 0: { // Elyos
					switch (player.getWorldId()) {
						case 210010000: // Poeta
							TeleportService2.teleportTo(player, 210010000, 853.70f, 1213.26f, 118.15f);
							break;
						case 210020000: // Eltnen
							TeleportService2.teleportTo(player, 210020000, 287.00f, 2752.49f, 270.45f);
							break;
						case 210030000: // Verteron
							TeleportService2.teleportTo(player, 210030000, 1688.16f, 1481.11f, 121.41f);
							break;
						case 210040000: // Heiron
							TeleportService2.teleportTo(player, 210040000, 2471.03f, 305.70f, 415.38f);
							break;
						case 210050000: // Inggison
							TeleportService2.teleportTo(player, 210050000, 1381.71f, 306.35f, 588.70f);
							break;
						case 210060000: // Theobomos
							TeleportService2.teleportTo(player, 210060000, 1422.96f, 1566.10f, 30.87f);
							break;
						case 210070000: // Cygnea
							TeleportService2.teleportTo(player, 210070000, 2944.18f, 852.99f, 571.74f);
							break;
						case 600100000: // Levinshor
							TeleportService2.teleportTo(player, 600100000, 97.54f, 97.22f, 348.62f);
							break;
						case 210100000: // Esterra
							TeleportService2.teleportTo(player, 210100000, 1402.91f, 1304.94f, 336.69f);
							break;
						default:
							System.out.println("Missing Revive Location for Map: " + player.getWorldId());
							TeleportService2.moveToBindLocation(player, true);
							break;
					}
				}
				case 1: { // Asmodians
					switch (player.getWorldId()) {
						case 220010000: // Ishalgen
							TeleportService2.teleportTo(player, 220010000, 590.96f, 2463.37f, 278.68f);
							break;
						case 220020000: // Morheim
							TeleportService2.teleportTo(player, 220020000, 272.66f, 2340.20f, 443.53f);
							break;
						case 220030000: // Altgard
							TeleportService2.teleportTo(player, 220030000, 1662.47f, 1817.08f, 253.69f);
							break;
						case 220040000: // Beluslan
							TeleportService2.teleportTo(player, 220040000, 322.77f, 335.76f, 229.28f);
							break;
						case 220050000: // Brusthonin
							TeleportService2.teleportTo(player, 220050000, 2925.55f, 2431.43f, 16.46f);
							break;
						case 220070000: // Gelkmaros
							TeleportService2.teleportTo(player, 220070000, 1764.03f, 2910.49f, 554.79f);
							break;
						case 220080000: // Enshar
							TeleportService2.teleportTo(player, 220080000, 429.17f, 2235.60f, 219.94f);
							break;
						case 600100000: // Levinshor
							TeleportService2.teleportTo(player, 600100000, 1849.19f, 1782.64f, 305.44f);
							break;
						case 220110000: // Nosra
							TeleportService2.teleportTo(player, 220110000, 1809.81f, 1967.93f, 199.19f);
							break;
						default:
							System.out.println("Missing Revive Location for Map: " + player.getWorldId());
							TeleportService2.moveToBindLocation(player, true);
							break;
					}
				}
				default:
					break;
			}
		}
		player.unsetResPosState();
	}

	public static final void kiskRevive(Player player) {
		kiskRevive(player, 0);
	}

	public static final void kiskRevive(Player player, int skillId) {
		Kisk kisk = player.getKisk();
		if (kisk == null) {
			bindRevive(player);
			return;
		}
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		else if (kisk.isActive()) {
			WorldPosition bind = kisk.getPosition();
			kisk.resurrectionUsed();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
			revive(player, 25, 25, false, skillId);
			player.getController().startProtectionActiveTask();
			player.setPortAnimation(4);
			if (player.getIsFlyingBeforeDeath()) {
				player.getFlyController().startFly();
			}
			player.getGameStats().updateStatsAndSpeedVisually();
			player.unsetResPosState();
			TeleportService2.moveToKiskLocation(player, bind);
		}
	}

	public static final void instanceRevive(Player player) {
		instanceRevive(player, 0);
	}

	public static final void instanceRevive(Player player, int skillId) {
		// Revive in Instances
		if (player.getPosition().getWorldMapInstance().getInstanceHandler().onReviveEvent(player)) {
			return;
		}
		WorldMap map = World.getInstance().getWorldMap(player.getWorldId());
		if (map == null) {
			bindRevive(player);
			return;
		}
		revive(player, 25, 25, true, skillId);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (map.isInstanceType() && (player.getInstanceStartPosX() != 0 && player.getInstanceStartPosY() != 0 && player.getInstanceStartPosZ() != 0)) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceStartPosX(), player.getInstanceStartPosY(), player.getInstanceStartPosZ());
		}
		else {
			bindRevive(player);
		}
		player.unsetResPosState();
	}

	public static final void revive(final Player player, int hpPercent, int mpPercent, boolean setSoulsickness, int resurrectionSkill) {
		player.getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player visitor) {
				VisibleObject target = visitor.getTarget();
				if (target != null && target.getObjectId() == player.getObjectId() && (visitor.getRace() != player.getRace())) {
					visitor.setTarget(null);
					PacketSendUtility.sendPacket(visitor, new SM_TARGET_SELECTED(null));
				}
			}
		});
		boolean isNoResurrectPenalty = player.getController().isNoResurrectPenaltyInEffect();
		player.getMoveController().stopFalling();
		player.setPlayerResActivate(false);
		player.getLifeStats().setCurrentHpPercent(isNoResurrectPenalty ? 100 : hpPercent);
		player.getLifeStats().setCurrentMpPercent(isNoResurrectPenalty ? 100 : mpPercent);
		if (player.getCommonData().getDp() > 0 && !isNoResurrectPenalty) {
			player.getCommonData().setDp(0);
		}
		player.getLifeStats().triggerRestoreOnRevive();
		if (!isNoResurrectPenalty && setSoulsickness) {
			player.getController().updateSoulSickness(resurrectionSkill);
		}
		if (player.getResurrectionSkill() > 0) {
			player.setResurrectionSkill(0);
		}
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		player.getAggroList().clear();
		player.getController().onBeforeSpawn(false);
		if (player.isInGroup2()) {
			PlayerGroupService.updateGroup(player, GroupEvent.MOVEMENT);
		}
		if (player.isInAlliance2()) {
			PlayerAllianceService.updateAlliance(player, PlayerAllianceEvent.MOVEMENT);
		}
	}

	public static final void itemSelfRevive(Player player) {
		Item item = player.getSelfRezStone();
		if (item == null && player.getAccessLevel() == 0) {
			cancelRes(player);
			return;
		}

		// Add Cooldown and use item
		ItemUseLimits useLimits = item.getItemTemplate().getUseLimits();
		int useDelay = useLimits.getDelayTime();
		player.addItemCoolDown(useLimits.getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
		player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
		if (!player.getInventory().decreaseByObjectId(item.getObjectId(), 1)) {
			cancelRes(player);
			return;
		}
		// Tombstone Self-Rez retail verified 15%
		revive(player, 15, 15, true, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void banditRevive(Player player) {
		revive(player, 100, 1000, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInResPostState()) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void ffaRevive(Player player) {
		revive(player, 100, 1000, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInResPostState()) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void bgRevive(Player player) {
		revive(player, 100, 1000, false, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		if (player.isInResPostState()) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}

	public static final void eventRevive(Player player) {
		revive(player, 100, 100, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath()) {
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.getIsFlyingBeforeDeath()) {
			player.getFlyController().startFly();
		}
		if (player.isInPrison()) {
			TeleportService2.teleportToPrison(player);
		}
		if (player.isInResPostState()) {
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		player.setIsFlyingBeforeDeath(false);
	}

	private static final void cancelRes(Player player) {
		AuditLogger.info(player, "Possible selfres hack.");
		player.getController().sendDie();
	}
}
