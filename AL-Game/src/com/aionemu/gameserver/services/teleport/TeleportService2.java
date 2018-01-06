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
package com.aionemu.gameserver.services.teleport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerTransformationDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.flypath.FlyPathEntry;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.portal.InstanceExit;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.portal.PortalScroll;
import com.aionemu.gameserver.model.templates.robot.RobotInfo;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.teleport.TelelocationTemplate;
import com.aionemu.gameserver.model.templates.teleport.TeleportLocation;
import com.aionemu.gameserver.model.templates.teleport.TeleportType;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BIND_POINT_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FAST_TRACK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FAST_TRACK_MOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RIDE_ROBOT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TELEPORT_LOC;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TELEPORT_MAP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.services.FastTrackService;
import com.aionemu.gameserver.services.PrivateStoreService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author xTz
 */
public class TeleportService2 {

	private static final Logger log = LoggerFactory.getLogger(TeleportService2.class);
	private static final int TELEPORT_DEFAULT_DELAY = 2200;

	/**
	 * Performs flight teleportation
	 *
	 * @param template
	 * @param locId
	 * @param player
	 */
	public static void teleport(TeleporterTemplate template, int locId, Player player, Npc npc, TeleportAnimation animation) {
		TribeClass tribe = npc.getTribe();
		Race race = player.getRace();
		if (tribe.equals(TribeClass.FIELD_OBJECT_LIGHT) && race.equals(Race.ASMODIANS) || (tribe.equals(TribeClass.FIELD_OBJECT_DARK) && race.equals(Race.ELYOS))) {
			return;
		}

		if (template.getTeleLocIdData() == null) {
			log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d", locId));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
			if (player.isGM()) {
				PacketSendUtility.sendMessage(player, "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
			}
			return;
		}

		TeleportLocation location = template.getTeleLocIdData().getTeleportLocation(locId);
		if (location == null) {
			log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d", locId));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
			if (player.isGM()) {
				PacketSendUtility.sendMessage(player, "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
			}
			return;
		}

		TelelocationTemplate locationTemplate = DataManager.TELELOCATION_DATA.getTelelocationTemplate(locId);
		if (locationTemplate == null) {
			log.info(String.format("Missing info at teleport_location.xml with locId: %d", locId));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
			if (player.isGM()) {
				PacketSendUtility.sendMessage(player, "Missing info at teleport_location.xml with locId: " + locId);
			}
			return;
		}

		if (location.getRequiredQuest() > 0) {
			if (player.getRace() == Race.ELYOS) {
				QuestState qs = player.getQuestStateList().getQuestState(location.getRequiredQuest());
				if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) { // Memories Of Eternity.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(10521));
					return;
				}
			}
			else if (player.getRace() == Race.ASMODIANS) {
				QuestState qs = player.getQuestStateList().getQuestState(location.getRequiredQuest());
				if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) { // Recovered Destiny.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(20521));
					return;
				}
			}
		}

		if (!checkKinahForTransportation(location, player)) {
			return;
		}
		if (location.getType().equals(TeleportType.FLIGHT)) {
			if (SecurityConfig.ENABLE_FLYPATH_VALIDATOR) {
				FlyPathEntry flypath = DataManager.FLY_PATH.getPathTemplate((short) location.getLocId());
				if (flypath == null) {
					AuditLogger.info(player, "Try to use null flyPath #" + location.getLocId());
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
					return;
				}

				double dist = MathUtil.getDistance(player, flypath.getStartX(), flypath.getStartY(), flypath.getStartZ());
				if (dist > 7) {
					AuditLogger.info(player, "Try to use flyPath #" + location.getLocId() + " but hes too far " + dist);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
					return;
				}

				if (player.getWorldId() != flypath.getStartWorldId()) {
					AuditLogger.info(player, "Try to use flyPath #" + location.getLocId() + " from not native start world " + player.getWorldId() + ". expected " + flypath.getStartWorldId());
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
					return;
				}

				player.setCurrentFlypath(flypath);
			}
			player.unsetPlayerMode(PlayerMode.RIDE);
			player.setState(CreatureState.FLIGHT_TELEPORT);
			player.unsetState(CreatureState.ACTIVE);
			player.setFlightTeleportId(location.getTeleportId());
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, location.getTeleportId(), 0), true);
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
		}
		else {
			int instanceId = 1;
			int mapId = locationTemplate.getMapId();
			if (player.getWorldId() == mapId) {
				instanceId = player.getInstanceId();
			}
			sendLoc(player, mapId, instanceId, locationTemplate.getX(), locationTemplate.getY(), locationTemplate.getZ(), (byte) locationTemplate.getHeading(), animation);
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
		}
	}

	/**
	 * Check kinah in inventory for teleportation
	 *
	 * @param location
	 * @param player
	 * @return
	 */
	private static boolean checkKinahForTransportation(TeleportLocation location, Player player) {
		Storage inventory = player.getInventory();

		// TODO: Price vary depending on the influence ratio
		int basePrice = location.getPrice();
		// TODO check for location.getPricePvp()

		long transportationPrice = PricesService.getPriceForService(basePrice, player.getRace());

		// If HiPassEffect is active, then all flight/teleport prices are 1 kinah
		if (player.getController().isHiPassInEffect()) {
			transportationPrice = 1;
		}

		if (!inventory.tryDecreaseKinah(transportationPrice, ItemUpdateType.DEC_KINAH_FLY)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(transportationPrice));
			return false;
		}
		return true;
	}

	private static void sendLoc(final Player player, final int mapId, final int instanceId, final float x, final float y, final float z, final byte h, final TeleportAnimation animation) {
		boolean isInstance = DataManager.WORLD_MAPS_DATA.getTemplate(mapId).isInstance();
		int delay = TELEPORT_DEFAULT_DELAY;

		if (animation.equals(TeleportAnimation.BEAM_ANIMATION)) {
			player.setPortAnimation(2);
		}
		else if (animation.equals(TeleportAnimation.JUMP_ANIMATION)) {
			player.setPortAnimation(11);
		}
		if (player.getLevel() >= 66) {
			PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(isInstance, instanceId, mapId, x, y, z, h, 3));// 3 = Archdaeva Animation
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(isInstance, instanceId, mapId, x, y, z, h, animation.getStartAnimationId()));
		}
		player.unsetPlayerMode(PlayerMode.RIDE);
		playerTransformation(player);
		instanceTransformation(player);
		highdaevaTransformation(player);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getLifeStats().isAlreadyDead()) {
					return;
				}
				if (animation.equals(TeleportAnimation.BEAM_ANIMATION)) {
					PacketSendUtility.broadcastPacket(player, new SM_DELETE(player, 2), 50);
				}
				else if (animation.equals(TeleportAnimation.JUMP_ANIMATION)) {
					PacketSendUtility.broadcastPacket(player, new SM_DELETE(player, 11), 50);
				}
				if (!player.isSpawned()) {
					WorldPosition pos = World.getInstance().createPosition(mapId, x, y, z, h, instanceId);
					player.setPosition(pos);
					DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
					return;
				}
				changePosition(player, mapId, instanceId, x, y, z, h, animation);
			}
		}, delay);
	}

	public static void teleportTo(Player player, WorldPosition pos) {
		if (player.getWorldId() == pos.getMapId()) {
			player.getPosition().setXYZH(pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
			// Pet
			Pet pet = player.getPet();
			if (pet != null) {
				World.getInstance().setPosition(pet, pos.getMapId(), player.getInstanceId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
			}
			// Summon
			Summon summon = player.getSummon();
			if (summon != null) {
				World.getInstance().setPosition(summon, pos.getMapId(), player.getInstanceId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
			}
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
			player.setPortAnimation(4); // Beam exit animation
			PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
			PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
			// Pet
			if (pet != null) {
				World.getInstance().spawn(pet);
			}
			// Summon
			if (summon != null) {
				World.getInstance().spawn(summon);
			}
			player.getKnownList().clear();
			player.updateKnownlist();
			player.getController().updateZone();
			player.getController().updateNearbyQuests();
			DisputeLandService.getInstance().onLogin(player);
			player.getEffectController().updatePlayerEffectIcons();
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
		}
		else if (player.getLifeStats().isAlreadyDead()) {
			teleportDeadTo(player, pos.getMapId(), 1, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
		}
		else {
			teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
		}
	}

	public static void teleportDeadTo(Player player, int worldId, int instanceId, float x, float y, float z, byte heading) {
		player.getController().onLeaveWorld();
		World.getInstance().despawn(player);
		World.getInstance().setPosition(player, worldId, instanceId, x, y, z, heading);
		PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
		PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
		player.setPortAnimation(4);
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		if (player.isLegionMember()) {
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
		}
	}

	public static boolean teleportTo(Player player, int worldId, float x, float y, float z) {
		return teleportTo(player, worldId, x, y, z, player.getHeading());
	}

	public static boolean teleportTo(Player player, int worldId, float x, float y, float z, byte h) {
		int instanceId = 1;
		if (player.getWorldId() == worldId) {
			instanceId = player.getInstanceId();
		}
		return teleportTo(player, worldId, instanceId, x, y, z, h, TeleportAnimation.BEAM_ANIMATION);
	}

	public static boolean teleportTo(Player player, int worldId, float x, float y, float z, byte h, TeleportAnimation animation) {
		int instanceId = 1;
		if (player.getWorldId() == worldId) {
			instanceId = player.getInstanceId();
		}
		return teleportTo(player, worldId, instanceId, x, y, z, h, animation);
	}

	public static boolean teleportTo(Player player, int worldId, int instanceId, float x, float y, float z, byte h) {
		return teleportTo(player, worldId, instanceId, x, y, z, h, TeleportAnimation.BEAM_ANIMATION);
	}

	public static boolean teleportTo(Player player, int worldId, int instanceId, float x, float y, float z) {
		return teleportTo(player, worldId, instanceId, x, y, z, player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
	}

	/**
	 * @param player
	 * @param worldId
	 * @param instanceId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param animation
	 * @return
	 */
	public static boolean teleportTo(final Player player, final int worldId, final int instanceId, final float x, final float y, final float z, final byte heading, TeleportAnimation animation) {
		if (player.getLifeStats().isAlreadyDead()) {
			return false;
		}
		else if (DuelService.getInstance().isDueling(player.getObjectId())) {
			DuelService.getInstance().loseDuel(player);
		}
		if (player.getWorldId() != worldId) {
			player.getController().onLeaveWorld();
		}
		if (animation.isNoAnimation()) {
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
			player.unsetPlayerMode(PlayerMode.RIDE);
			changePosition(player, worldId, instanceId, x, y, z, heading, animation);
		}
		else {
			sendLoc(player, worldId, instanceId, x, y, z, heading, animation);
		}
		return true;
	}

	/**
	 * @param worldId
	 * @param instanceId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 */
	private static void changePosition(final Player player, int worldId, int instanceId, float x, float y, float z, byte heading, TeleportAnimation animation) {
		if (player.hasStore()) {
			PrivateStoreService.closePrivateStore(player);
		}
		player.getController().cancelCurrentSkill();
		if (player.getWorldId() != worldId) {
			player.getController().onLeaveWorld();
		}
		player.getFlyController().endFly(true);
		World.getInstance().despawn(player);
		// Send 2x, is normal !!!
		playerTransformation(player);
		instanceTransformation(player);
		highdaevaTransformation(player);
		player.getController().cancelCurrentSkill();
		int currentWorldId = player.getWorldId();
		WorldPosition pos = World.getInstance().createPosition(worldId, x, y, z, heading, instanceId);
		player.setPosition(pos);
		boolean isInstance = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).isInstance();

		// Pet
		Pet pet = player.getPet();
		if (pet != null) {
			World.getInstance().setPosition(pet, worldId, instanceId, x, y, z, heading);
		}
		// Summon
		Summon summon = player.getSummon();
		if (summon != null) {
			World.getInstance().setPosition(summon, worldId, instanceId, x, y, z, heading);
		}
		player.setPortAnimation(animation.getEndAnimationId());
		if (currentWorldId == worldId) {
			PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
			World.getInstance().spawn(player);
			player.getEffectController().updatePlayerEffectIcons();
			player.getController().updateZone();
			player.getController().updateNearbyQuests();
			DisputeLandService.getInstance().onLogin(player);
			// Send 2x, is normal !!!
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
			// Pet
			if (pet != null) {
				World.getInstance().spawn(pet);
				player.setPortAnimation(4);
			}
			// Summon
			if (summon != null) {
				World.getInstance().spawn(summon);
				player.setPortAnimation(4);
			}
			player.getKnownList().clear();
			player.updateKnownlist();
			if (player.isUseRobot() || player.getRobotId() != 0) {
				PacketSendUtility.sendPacket(player, new SM_RIDE_ROBOT(player, getRobotInfo(player).getRobotId()));
			}
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
			PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
			if (player.isUseRobot() || player.getRobotId() != 0) {
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_RIDE_ROBOT(player, getRobotInfo(player).getRobotId()));
					}
				}, 3000);
			}
		}
		if (player.isLegionMember()) {
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
		}
		sendWorldSwitchMessage(player, currentWorldId, worldId, isInstance);
	}

	public static RobotInfo getRobotInfo(Player player) {
		ItemTemplate template = player.getEquipment().getMainHandWeapon().getItemSkinTemplate();
		return DataManager.ROBOT_DATA.getRobotInfo(template.getRobotId());
	}

	private static void sendWorldSwitchMessage(Player player, int oldWorld, int newWorld, boolean enteredInstance) {
		if (enteredInstance && oldWorld != newWorld) {
			if (!WorldMapType.getWorld(newWorld).isPersonal()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_DUNGEON_OPENED_FOR_SELF(newWorld));
			}
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
		}
	}

	/**
	 * @param player
	 * @param targetObjectId
	 */
	public static void showMap(Player player, int targetObjectId, int npcId) {
		if (player.isInFlyingState()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_AIRPORT_WHEN_FLYING);
			return;
		}

		Npc object = (Npc) World.getInstance().findVisibleObject(targetObjectId);
		if (player.isEnemyFrom(object)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_WRONG_NPC);// TODO retail message
			return;
		}

		PacketSendUtility.sendPacket(player, new SM_TELEPORT_MAP(player, targetObjectId, getTeleporterTemplate(npcId)));
	}

	/**
	 * @return the teleporterData
	 */
	public static TeleporterTemplate getTeleporterTemplate(int npcId) {
		return DataManager.TELEPORTER_DATA.getTeleporterTemplateByNpcId(npcId);
	}

	/**
	 * @param player
	 * @param b
	 */
	public static void moveToKiskLocation(Player player, WorldPosition kisk) {
		teleportTo(player, kisk.getMapId(), kisk.getX(), kisk.getY(), kisk.getZ(), kisk.getHeading());
	}

	public static void teleportToPrison(Player player) {
		if (player.getRace() == Race.ELYOS) {
			teleportTo(player, WorldMapType.DE_PRISON.getId(), 275, 239, 49);
		}
		else if (player.getRace() == Race.ASMODIANS) {
			teleportTo(player, WorldMapType.DF_PRISON.getId(), 275, 239, 49);
		}
	}

	public static void teleportToNpc(Player player, int npcId) {
		int worldId = player.getWorldId();
		SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(worldId, npcId);

		if (searchResult == null) {
			log.warn("No npc spawn found for : " + npcId);
			return;
		}

		SpawnSpotTemplate spot = searchResult.getSpot();
		WorldMapTemplate worldTemplate = DataManager.WORLD_MAPS_DATA.getTemplate(searchResult.getWorldId());
		WorldMapInstance newInstance = null;

		if (worldTemplate.isInstance()) {
			newInstance = InstanceService.getNextAvailableInstance(searchResult.getWorldId());
		}

		if (newInstance != null) {
			InstanceService.registerPlayerWithInstance(newInstance, player);
			teleportTo(player, searchResult.getWorldId(), newInstance.getInstanceId(), spot.getX(), spot.getY(), spot.getZ());
		}
		else {
			teleportTo(player, searchResult.getWorldId(), spot.getX(), spot.getY(), spot.getZ());
		}
	}

	/**
	 * This method will send the set bind point packet
	 *
	 * @param player
	 */
	public static void sendSetBindPoint(Player player) {
		int worldId;
		float x, y, z;
		if (player.getBindPoint() != null) {
			BindPointPosition bplist = player.getBindPoint();
			worldId = bplist.getMapId();
			x = bplist.getX();
			y = bplist.getY();
			z = bplist.getZ();
		}
		else {
			PlayerInitialData.LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
			worldId = locationData.getMapId();
			x = locationData.getX();
			y = locationData.getY();
			z = locationData.getZ();
		}
		PacketSendUtility.sendPacket(player, new SM_BIND_POINT_INFO(worldId, x, y, z, player));
	}

	/**
	 * This method will move a player to their bind location
	 *
	 * @param player
	 * @param useTeleport
	 */
	public static void moveToBindLocation(Player player, boolean useTeleport) {
		moveToBindLocation(player, useTeleport, 0);
	}

	public static void moveToBindLocation(Player player, boolean useTeleport, int delay) {
		byte h = 0;
		int worldId;
		float x;
		float y;
		float z;
		if (player.getBindPoint() != null) {
			BindPointPosition bplist = player.getBindPoint();
			worldId = bplist.getMapId();
			x = bplist.getX();
			y = bplist.getY();
			z = bplist.getZ();
			h = bplist.getHeading();
		}
		else {
			PlayerInitialData.LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
			worldId = locationData.getMapId();
			x = locationData.getX();
			y = locationData.getY();
			z = locationData.getZ();
		}

		InstanceService.onLeaveInstance(player);

		if (useTeleport) {
			teleportTo(player, worldId, x, y, z, h);
		}
		else {
			World.getInstance().setPosition(player, worldId, 1, x, y, z, h);
		}
	}

	/**
	 * Move Player concerning object with specific conditions
	 *
	 * @param object
	 * @param player
	 * @param direction
	 * @param distance
	 * @return true or false
	 */
	public static boolean moveToTargetWithDistance(VisibleObject object, Player player, int direction, int distance) {
		double radian = Math.toRadians(object.getHeading() * 3);
		float x0 = object.getX();
		float y0 = object.getY();
		float x1 = (float) (Math.cos(Math.PI * direction + radian) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction + radian) * distance);
		return teleportTo(player, object.getWorldId(), x0 + x1, y0 + y1, object.getZ());
	}

	public static void moveToInstanceExit(Player player, int worldId, Race race) {
		player.getController().cancelCurrentSkill();
		InstanceExit instanceExit = getInstanceExit(worldId, race);
		if (instanceExit == null) {
			log.warn("No instance exit found for race: " + race + " " + worldId);
			moveToBindLocation(player, true);
			return;
		}
		if (InstanceService.isInstanceExist(instanceExit.getExitWorld(), 1)) {
			teleportTo(player, instanceExit.getExitWorld(), instanceExit.getX(), instanceExit.getY(), instanceExit.getZ(), instanceExit.getH());
		}
		else {
			moveToBindLocation(player, true);
		}
	}

	public static void onLogOutOppositeMap(Player player) {
		switch (player.getWorldId()) {
			case 210100000: // Iluma
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					TeleportService2.teleportTo(player, 220110000, 1813.9795f, 1982.6705f, 199.1976f, (byte) 52);
				}
				break;
			case 220110000: // Norsvold
				if (player.getCommonData().getRace() == Race.ELYOS) {
					TeleportService2.teleportTo(player, 210100000, 1417.6694f, 1282.3623f, 336.125f, (byte) 8);
				}
				break;
		}
	}

	public static InstanceExit getInstanceExit(int worldId, Race race) {
		return DataManager.INSTANCE_EXIT_DATA.getInstanceExit(worldId, race);
	}

	/**
	 * @param portalName
	 */
	public static void useTeleportScroll(Player player, String portalName, int worldId) {
		PortalScroll template = DataManager.PORTAL2_DATA.getPortalScroll(portalName);
		if (template == null) {
			log.warn("No portal template found for : " + portalName + " " + worldId);
			return;
		}

		Race playerRace = player.getRace();
		PortalPath portalPath = template.getPortalPath();
		if (portalPath == null) {
			log.warn("No portal scroll for " + playerRace + " on " + portalName + " " + worldId);
			return;
		}
		PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(portalPath.getLocId());
		if (loc == null) {
			log.warn("No portal loc for locId" + portalPath.getLocId());
			return;
		}
		teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
	}

	/**
	 * @param channel
	 */
	public static void changeChannel(Player player, int channel) {
		World.getInstance().despawn(player);
		World.getInstance().setPosition(player, player.getWorldId(), channel + 1, player.getX(), player.getY(), player.getZ(), player.getHeading());
		PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
		PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
		playerTransformation(player);
		instanceTransformation(player);
		highdaevaTransformation(player);
	}

	/**
	 * @category this is only special and its for emulating the fast track server (a real fast track server wont be good for private)
	 */
	public static void moveFastTrack(Player player, int serverId, boolean back) {
		if (back) {
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
			World.getInstance().despawn(player);
			World.getInstance().setPosition(player, player.getWorldId(), player.getX(), player.getY(), player.getZ(), player.getHeading());
			player.FAST_TRACK_TYPE = 0;
			PacketSendUtility.sendPacket(player, new SM_FAST_TRACK_MOVE(NetworkConfig.GAMESERVER_ID, serverId, player.getWorldId()));
			PacketSendUtility.sendPacket(player, new SM_FAST_TRACK(NetworkConfig.GAMESERVER_ID, serverId, false));
			PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
			FastTrackService.getInstance().checkFastTrackMove(player, player.getPlayerAccount().getId(), true);
		}
		else {
			World.getInstance().despawn(player);
			World.getInstance().setPosition(player, player.getWorldId(), player.getX(), player.getY(), player.getZ(), player.getHeading());
			player.FAST_TRACK_TYPE = 1;
			PacketSendUtility.sendPacket(player, new SM_FAST_TRACK_MOVE(serverId, NetworkConfig.GAMESERVER_ID, player.getWorldId()));
			PacketSendUtility.sendPacket(player, new SM_FAST_TRACK(serverId, NetworkConfig.GAMESERVER_ID, false));
			PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
			playerTransformation(player);
			instanceTransformation(player);
			highdaevaTransformation(player);
			FastTrackService.getInstance().checkFastTrackMove(player, player.getPlayerAccount().getId(), false);
		}
	}

	public static void playerTransformation(Player player) {
		DAOManager.getDAO(PlayerTransformationDAO.class).loadPlTransfo(player);
		PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, player.getTransformModel().getPanelId(), true, player.getTransformModel().getItemId()));
	}

	/**
	 * Archdaeva Transformation 5.1 If a player is under one of the following effects, and uses a "Teleport/Fly/Hotspot/Return Scroll" or use admin command "goto/movetoplayer/movetonpc" Then, the
	 * "Skill Panel" linked to this effect, never disappear !!!
	 */
	public static void highdaevaTransformation(Player player) {
		if (!player.isInGroup2() || player != null) {
			if (player.getEffectController().hasAbnormalEffect(4752)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(76);
					player.getTransformModel().setItemId(102301000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 76, true, 102301000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4757)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(77);
					player.getTransformModel().setItemId(102303000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 77, true, 102303000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4762)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(78);
					player.getTransformModel().setItemId(102302000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 78, true, 102302000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4768)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(79);
					player.getTransformModel().setItemId(102304000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 79, true, 102304000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4804)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(76);
					player.getTransformModel().setItemId(102301000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 76, true, 102301000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4805)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(77);
					player.getTransformModel().setItemId(102303000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 77, true, 102303000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4806)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(78);
					player.getTransformModel().setItemId(102302000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 78, true, 102302000));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(4807)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(79);
					player.getTransformModel().setItemId(102304000);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 79, true, 102304000));
				}
			}
		}
	}

	/**
	 * Instance + Event Transformation If a player is under one of the following effects, and uses a "Teleport/Fly/Hotspot/Return Scroll" or use admin command "goto/movetoplayer/movetonpc" Then, the
	 * "Skill Panel" linked to this effect, never disappear !!!
	 */
	public static void instanceTransformation(Player player) {
		if (!player.isInGroup2() || player != null) {
			// [PvP] Arena
			if (player.getEffectController().hasAbnormalEffect(10405)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(15);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 15, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(10406)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(15);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 15, true, 0));
				}
			}
			// Fissure Of Oblivion 5.1
			if (player.getEffectController().hasAbnormalEffect(4829) || player.getEffectController().hasAbnormalEffect(4831) || player.getEffectController().hasAbnormalEffect(4834) || player.getEffectController().hasAbnormalEffect(4835) || player.getEffectController().hasAbnormalEffect(4836)) {
				player.getTransformModel().setPanelId(81);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 81, true, 0));
			}
			if (player.getEffectController().hasAbnormalEffect(4808)) {
				player.getTransformModel().setPanelId(82);
				player.getTransformModel().setItemId(102301000);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 82, true, 102301000));
			}
			if (player.getEffectController().hasAbnormalEffect(4813)) {
				player.getTransformModel().setPanelId(83);
				player.getTransformModel().setItemId(102303000);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 83, true, 102303000));
			}
			if (player.getEffectController().hasAbnormalEffect(4818)) {
				player.getTransformModel().setPanelId(84);
				player.getTransformModel().setItemId(102302000);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 84, true, 102302000));
			}
			if (player.getEffectController().hasAbnormalEffect(4824)) {
				player.getTransformModel().setPanelId(85);
				player.getTransformModel().setItemId(102304000);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 85, true, 102304000));
			}
			// Aturam Sky Fortress 4.8
			if (player.getEffectController().hasAbnormalEffect(21807)) {
				player.getTransformModel().setPanelId(61);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 61, true, 0));
			}
			if (player.getEffectController().hasAbnormalEffect(21808)) {
				player.getTransformModel().setPanelId(62);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 62, true, 0));
			}
			// Cradle Of Eternity 5.1
			if (player.getEffectController().hasAbnormalEffect(21340)) {
				player.getTransformModel().setPanelId(71);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 71, true, 0));
			}
			// Shugo Imperial Tomb 4.3
			if (player.getEffectController().hasAbnormalEffect(21096)) {
				player.getTransformModel().setPanelId(27);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 27, true, 0));
			}
			// Tiamat Stronghold 3.5
			if (player.getEffectController().hasAbnormalEffect(20865)) {
				player.getTransformModel().setPanelId(17);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 17, true, 0));
			}
			// Contaminated Underpath 5.1
			if (player.getEffectController().hasAbnormalEffect(21345)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(68);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 68, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21346)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(68);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 68, true, 0));
				}
			}
			// Secret Munitions Factory 5.1
			if (player.getEffectController().hasAbnormalEffect(21347)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(69);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 69, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21348)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(69);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 69, true, 0));
				}
			}
			// Occupied Rentus Base 4.8 & Fallen Poeta 5.1
			if (player.getEffectController().hasAbnormalEffect(21805)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(63);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 63, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21806)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(63);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 63, true, 0));
				}
			}
			// Smoldering Fire Temple 5.1
			if (player.getEffectController().hasAbnormalEffect(21375)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(72);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 72, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21376)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(74);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 74, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21377)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(75);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 75, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21378)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(72);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 72, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21379)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(74);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 74, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21380)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(75);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 75, true, 0));
				}
			}
			// Ophidan Warpath 5.1
			if (player.getEffectController().hasAbnormalEffect(21336)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(70);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 70, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21337)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(70);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 70, true, 0));
				}
			}
			// Illuminary Obelisk & [Infernal] Illuminary Obelisk 4.7
			if (player.getEffectController().hasAbnormalEffect(21511)) {
				player.getTransformModel().setPanelId(51);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 51, true, 0));
			}
			// The Eternal Bastion 4.3
			if (player.getEffectController().hasAbnormalEffect(21065)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(20);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 20, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21066)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(20);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 20, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21141)) {
				player.getTransformModel().setPanelId(31);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 31, true, 0));
			}
			// Nightmare Circus 4.3
			if (player.getEffectController().hasAbnormalEffect(21469)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(38);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 38, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21470)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(39);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 39, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21471)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(38);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 38, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21472)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(39);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 39, true, 0));
				}
			}
			// Transidium Annex 4.7.5
			if (player.getEffectController().hasAbnormalEffect(21728) || player.getEffectController().hasAbnormalEffect(21729) || player.getEffectController().hasAbnormalEffect(21730) || player.getEffectController().hasAbnormalEffect(21731)) {
				player.getTransformModel().setPanelId(55);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 55, true, 0));
			}
			if (player.getEffectController().hasAbnormalEffect(21579) || player.getEffectController().hasAbnormalEffect(21586) || player.getEffectController().hasAbnormalEffect(21587) || player.getEffectController().hasAbnormalEffect(21588)) {
				player.getTransformModel().setPanelId(56);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 56, true, 0));
			}
			if (player.getEffectController().hasAbnormalEffect(21582) || player.getEffectController().hasAbnormalEffect(21589) || player.getEffectController().hasAbnormalEffect(21590) || player.getEffectController().hasAbnormalEffect(21591)) {
				player.getTransformModel().setPanelId(57);
				PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 57, true, 0));
			}
			// The Shugo Emperor Vault 4.7.5
			// Emperor Trillirunerk Safe 4.9.1
			if (player.getEffectController().hasAbnormalEffect(21829)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(64);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 64, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21830)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(65);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 65, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21831)) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					player.getTransformModel().setPanelId(66);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 66, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21832)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(64);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 64, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21833)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(65);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 65, true, 0));
				}
			}
			if (player.getEffectController().hasAbnormalEffect(21834)) {
				if (player.getCommonData().getRace() == Race.ASMODIANS) {
					player.getTransformModel().setPanelId(66);
					PacketSendUtility.sendPacket(player, new SM_TRANSFORM(player, 66, true, 0));
				}
			}
		}
	}
}
