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
package com.aionemu.gameserver.model.house;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.controllers.HouseController;
import com.aionemu.gameserver.dao.HouseScriptsDAO;
import com.aionemu.gameserver.dao.HousesDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.SummonedHouseNpc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.PlayerHouseOwnerFlags;
import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;
import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.model.templates.housing.HouseAddress;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.model.templates.housing.Sale;
import com.aionemu.gameserver.model.templates.spawns.HouseSpawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnType;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneService;

/**
 * @author Rolandas
 */
public class House extends VisibleObject {

	private static final Logger log = LoggerFactory.getLogger(House.class);
	private HousingLand land;
	private HouseAddress address;
	private Building building;
	private String name;
	private int playerObjectId;
	private Timestamp acquiredTime;
	private int permissions;
	private HouseStatus status;
	private boolean feePaid = true;
	private Timestamp nextPay;
	private Timestamp sellStarted;
	private Map<SpawnType, Npc> spawns = new HashMap<SpawnType, Npc>(3);
	private HouseRegistry houseRegistry;
	private byte houseOwnerInfoFlags = PlayerHouseOwnerFlags.SINGLE_HOUSE.getId();
	private PlayerScripts playerScripts;
	private PersistentState persistentState;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
	private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
	private ByteArrayOutputStream signNoticeStream;
	public static final int NOTICE_LENGTH = 130;

	public House(Building building, HouseAddress address, int instanceId) {
		this(IDFactory.getInstance().nextId(), building, address, instanceId);
	}

	/**
	 * @param instanceId
	 */
	public House(int objectId, Building building, HouseAddress address, int instanceId) {
		super(objectId, new HouseController(), null, null, null);
		getController().setOwner(this);
		this.address = address;
		this.building = building;
		this.name = "HOUSE_" + address.getId();
		setKnownlist(new PlayerAwareKnownList(this));
		setPersistentState(PersistentState.UPDATED);
		getRegistry();
	}

	@Override
	public HouseController getController() {
		return (HouseController) super.getController();
	}

	private void putDefaultParts() {
		for (PartType partType : PartType.values()) {
			Integer partId = building.getDefaultPartId(partType);
			if (partId == null) {
				continue;
			}
			for (int line = partType.getStartLineNr(); line <= partType.getEndLineNr(); line++) {
				int floor = partType.getEndLineNr() - line;
				HouseDecoration decor = new HouseDecoration(0, partId, floor);
				getRegistry().putDefaultPart(decor, floor);
			}
		}
	}

	/**
	 * TODO: improve this, now it's inefficient during startup
	 */
	public HousingLand getLand() {
		if (land == null) {
			for (HousingLand housingland : DataManager.HOUSE_DATA.getLands()) {
				for (HouseAddress houseAddress : housingland.getAddresses()) {
					if (this.getAddress().getId() == houseAddress.getId()) {
						this.land = housingland;
						break;
					}
				}
			}
		}
		return this.land;
	}

	@Override
	public String getName() {
		return name;
	}

	public HouseAddress getAddress() {
		return address;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public synchronized void spawn(int instanceId) {
		playerScripts = DAOManager.getDAO(HouseScriptsDAO.class).getPlayerScripts(getObjectId());

		if (playerObjectId > 0 && status == HouseStatus.ACTIVE || status == HouseStatus.SELL_WAIT) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).loadRegistry(playerObjectId);
		}

		fixBuildingStates();

		// Studios are brought into world already, skip them
		if (getPosition() == null || !getPosition().isSpawned()) {
			WorldPosition position = World.getInstance().createPosition(address.getMapId(), address.getX(), address.getY(), address.getZ(), (byte) 0, instanceId);
			this.setPosition(position);
			SpawnEngine.bringIntoWorld(this);
		}

		List<HouseSpawn> templates = DataManager.HOUSE_NPCS_DATA.getSpawnsByAddress(getAddress().getId());
		if (templates == null) {
			Collection<ZoneInstance> zones = ZoneService.getInstance().getZoneInstancesByWorldId(getAddress().getMapId()).values();
			String msg = null;
			for (ZoneInstance zone : zones) {
				if (zone.getZoneTemplate().getZoneType() != ZoneClassName.SUB || zone.getZoneTemplate().getPriority() > 20) {
					continue;
				}
				if (zone.isInsideCordinate(getAddress().getX(), getAddress().getY(), getAddress().getZ())) {
					msg = "zone=" + zone.getZoneTemplate().getXmlName();
					break;
				}
			}
			if (msg == null) {
				msg = "address=" + this.getAddress().getId() + "; map=" + this.getAddress().getMapId();
			}
			msg += "; x=" + getAddress().getX() + ", y=" + getAddress().getY() + ", z=" + getAddress().getZ();
			log.warn("Missing npcs for house: " + msg);
			return;
		}

		int creatorId = getAddress().getId();
		String masterName = StringUtils.EMPTY;
		if (playerObjectId != 0) {
			ArrayList<Integer> players = new ArrayList<Integer>(1);
			players.add(playerObjectId);
			Map<Integer, String> playerNames = DAOManager.getDAO(PlayerDAO.class).getPlayerNames(players);
			if (playerNames.containsKey(playerObjectId)) {
				masterName = playerNames.get(playerObjectId);
			}
			else {
				this.revokeOwner(); // Something bad happened :P
			}
		}

		for (HouseSpawn spawn : templates) {
			SpawnTemplate t = null;
			if (spawn.getType() == SpawnType.MANAGER && spawns.get(SpawnType.MANAGER) == null) {
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getLand().getManagerNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH());
				SummonedHouseNpc npc = VisibleObjectSpawner.spawnHouseNpc(t, getPosition().getInstanceId(), this, masterName);
				spawns.put(SpawnType.MANAGER, npc);
			}
			else if (spawn.getType() == SpawnType.TELEPORT && spawns.get(SpawnType.TELEPORT) == null) {
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getLand().getTeleportNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH());
				SummonedHouseNpc npc = VisibleObjectSpawner.spawnHouseNpc(t, getPosition().getInstanceId(), this, masterName);
				spawns.put(SpawnType.TELEPORT, npc);
			}
			else if (spawn.getType() == SpawnType.SIGN && spawns.get(SpawnType.SIGN) == null) {
				// Signs do not have master name displayed, but have creatorId
				t = SpawnEngine.addNewSingleTimeSpawn(getAddress().getMapId(), getCurrentSignNpcId(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getH(), creatorId, StringUtils.EMPTY);
				spawns.put(SpawnType.SIGN, (Npc) SpawnEngine.spawnObject(t, getPosition().getInstanceId()));
			}
		}
	}

	@Override
	public float getVisibilityDistance() {
		return HousingConfig.VISIBILITY_DISTANCE;
	}

	@Override
	public float getMaxZVisibleDistance() {
		return HousingConfig.VISIBILITY_DISTANCE;
	}

	public int getOwnerId() {
		return playerObjectId;
	}

	public void setOwnerId(int playerObjectId) {
		if (this.playerObjectId != playerObjectId) {
			writeLock.lock();
			try {
				if (playerObjectId == 0) {
					signNoticeStream = null;
				}
				else {
					if (signNoticeStream == null) {
						signNoticeStream = new ByteArrayOutputStream(NOTICE_LENGTH);
					}
					signNoticeStream.reset();
					signNoticeStream.write(new byte[] { 0, 0 }, 0, 2);
				}
				this.playerObjectId = playerObjectId;
			}
			finally {
				writeLock.unlock();
			}
		}
		fixBuildingStates();
	}

	public Timestamp getAcquiredTime() {
		return acquiredTime;
	}

	public void setAcquiredTime(Timestamp acquiredTime) {
		this.acquiredTime = acquiredTime;
	}

	public int getPermissions() {
		if (playerObjectId == 0) {
			setDoorState(status == HouseStatus.SELL_WAIT ? HousePermissions.DOOR_OPENED_ALL : HousePermissions.DOOR_CLOSED);
			setNoticeState(HousePermissions.NOT_SET);
		}
		else {
			if (permissions == 0) {
				setNoticeState(HousePermissions.SHOW_OWNER);
				if (getBuilding().getType() == BuildingType.PERSONAL_FIELD) {
					setDoorState(HousePermissions.DOOR_CLOSED);
				}
			}
		}
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	public HousePermissions getDoorState() {
		return HousePermissions.getDoorState(getPermissions());
	}

	public void setDoorState(HousePermissions doorState) {
		permissions = HousePermissions.setDoorState(permissions, doorState);
	}

	public HousePermissions getNoticeState() {
		return HousePermissions.getNoticeState(getPermissions());
	}

	public void setNoticeState(HousePermissions noticeState) {
		permissions = HousePermissions.setNoticeState(permissions, noticeState);
	}

	public HouseStatus getStatus() {
		return status;
	}

	public synchronized void setStatus(HouseStatus status) {
		if (this.status != status) {
			// fix invalid status from DB, or automatically remove sign from not auctioned houses
			if (this.playerObjectId == 0 && status == HouseStatus.ACTIVE) {
				status = HouseStatus.NOSALE;
			}
			this.status = status;
			fixBuildingStates();

			if ((status != HouseStatus.INACTIVE || getSellStarted() != null) && spawns.get(SpawnType.SIGN) != null) {
				Npc sign = spawns.get(SpawnType.SIGN);
				int oldNpcId = sign.getNpcId();
				int newNpcId = getCurrentSignNpcId();

				if (newNpcId != oldNpcId) {
					SpawnTemplate t = sign.getSpawn();
					sign.setSpawn(null);
					sign.getController().onDelete();
					t = SpawnEngine.addNewSingleTimeSpawn(t.getWorldId(), newNpcId, t.getX(), t.getY(), t.getZ(), t.getHeading());
					sign = (Npc) SpawnEngine.spawnObject(t, this.getPosition().getInstanceId());
					spawns.put(SpawnType.SIGN, sign);
				}
			}
		}
	}

	public boolean isFeePaid() {
		return feePaid;
	}

	public void setFeePaid(boolean feePaid) {
		this.feePaid = feePaid;
	}

	public Timestamp getNextPay() {
		return nextPay;
	}

	public void setNextPay(Timestamp nextPay) {
		this.nextPay = nextPay;
	}

	public Timestamp getSellStarted() {
		return sellStarted;
	}

	public void setSellStarted(Timestamp sellStarted) {
		this.sellStarted = sellStarted;
	}

	public boolean isInGracePeriod() {
		return playerObjectId > 0 && HousingService.getInstance().searchPlayerHouses(playerObjectId).size() == 2 && (status == HouseStatus.ACTIVE || status == HouseStatus.SELL_WAIT) && sellStarted != null && sellStarted.getTime() <= HousingBidService.getInstance().getAuctionStartTime();
	}

	public synchronized Npc getButler() {
		return spawns.get(SpawnType.MANAGER);
	}

	public Race getPlayerRace() {
		if (getButler() == null) {
			return Race.NONE;
		}
		if (getButler().getTribe() == TribeClass.GENERAL) {
			return Race.ELYOS;
		}
		return Race.ASMODIANS;
	}

	public synchronized Npc getTeleport() {
		return spawns.get(SpawnType.TELEPORT);
	}

	public synchronized Npc getCurrentSign() {
		return spawns.get(SpawnType.SIGN);
	}

	/**
	 * Do not use directly !!! It's for housespawn command only
	 */
	public synchronized void setSpawn(SpawnType type, Npc npc) {
		if (npc == null) {
			npc = spawns.remove(type);
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
		else {
			spawns.put(type, npc);
		}
	}

	public int getCurrentSignNpcId() {
		int npcId = getLand().getWaitingSignNpcId(); // bidding closed
		if (status == HouseStatus.NOSALE) {
			npcId = getLand().getNosaleSignNpcId(); // invisible npc
		}
		else if (status == HouseStatus.SELL_WAIT) {
			if (HousingBidService.getInstance().isBiddingAllowed()) {
				npcId = getLand().getSaleSignNpcId(); // bidding open
			}
		}
		else if (playerObjectId != 0) {
			if (status == HouseStatus.ACTIVE) {
				npcId = getLand().getHomeSignNpcId(); // resident information
			}
		}
		return npcId;
	}

	public synchronized boolean revokeOwner() {
		if (playerObjectId == 0) {
			return false;
		}
		getRegistry().despawnObjects();
		if (this.getBuilding().getType() == BuildingType.PERSONAL_INS) {
			HousingService.getInstance().removeStudio(playerObjectId);
			DAOManager.getDAO(HousesDAO.class).deleteHouse(playerObjectId);
			return true;
		}
		houseRegistry = null;
		acquiredTime = null;
		sellStarted = null;
		nextPay = null;
		feePaid = true;

		Building defaultBuilding = getLand().getDefaultBuilding();
		setOwnerId(0);
		if (defaultBuilding != building) {
			HousingService.getInstance().switchHouseBuilding(this, defaultBuilding.getId());
		}
		setStatus(HouseStatus.NOSALE);
		save();
		return true;
	}

	public HouseRegistry getRegistry() {
		if (houseRegistry == null) {
			houseRegistry = new HouseRegistry(this);
			putDefaultParts();
		}
		return houseRegistry;
	}

	public synchronized void reloadHouseRegistry() {
		houseRegistry = null;
		getRegistry();
		if (playerObjectId != 0) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).loadRegistry(playerObjectId);
		}
	}

	public HouseDecoration getRenderPart(PartType partType, int floor) {
		return getRegistry().getRenderPart(partType, floor);
	}

	public HouseDecoration getDefaultPart(PartType partType, int floor) {
		return getRegistry().getDefaultPartByType(partType, floor);
	}

	public PlayerScripts getPlayerScripts() {
		return playerScripts;
	}

	public HouseType getHouseType() {
		return HouseType.fromValue(getBuilding().getSize());
	}

	public synchronized void save() {
		DAOManager.getDAO(HousesDAO.class).storeHouse(this);
		// save registry if needed
		if (houseRegistry != null) {
			this.houseRegistry.save();
		}
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public byte getHouseOwnerInfoFlags() {
		return houseOwnerInfoFlags;
	}

	public boolean isInHousingStatus(PlayerHouseOwnerFlags status) {
		return (houseOwnerInfoFlags & status.getId()) != 0;
	}

	public void fixBuildingStates() {
		houseOwnerInfoFlags = PlayerHouseOwnerFlags.SINGLE_HOUSE.getId();
		if (playerObjectId != 0) {
			houseOwnerInfoFlags |= PlayerHouseOwnerFlags.HAS_OWNER.getId();
			if (status == HouseStatus.ACTIVE) {
				houseOwnerInfoFlags |= PlayerHouseOwnerFlags.BIDDING_ALLOWED.getId();
				houseOwnerInfoFlags &= ~PlayerHouseOwnerFlags.SINGLE_HOUSE.getId();
			}
		}
		else if (status == HouseStatus.SELL_WAIT) {
			houseOwnerInfoFlags = PlayerHouseOwnerFlags.SELLING_HOUSE.getId();
		}
	}

	public byte[] getSignNotice() {
		byte[] notice;
		readLock.lock();
		if (signNoticeStream == null) {
			notice = new byte[0];
		}
		else {
			notice = signNoticeStream.toByteArray();
		}
		readLock.unlock();
		return notice;
	}

	public void setSignNotice(byte[] noticeStream) {
		writeLock.lock();
		if (signNoticeStream == null) {
			signNoticeStream = new ByteArrayOutputStream(NOTICE_LENGTH);
		}
		signNoticeStream.reset();
		try {
			signNoticeStream.write(noticeStream, 0, Math.min(noticeStream.length, NOTICE_LENGTH));
		}
		finally {
			writeLock.unlock();
		}
	}

	public int getLevelRestrict() {
		return land != null ? land.getSaleOptions().getMinLevel() : 10;
	}

	public final long getDefaultAuctionPrice() {
		Sale saleOptions = getLand().getSaleOptions();
		switch (getHouseType()) {
			case HOUSE:
				if (HousingConfig.HOUSE_MIN_BID > 0) {
					return HousingConfig.HOUSE_MIN_BID;
				}
				break;
			case MANSION:
				if (HousingConfig.MANSION_MIN_BID > 0) {
					return HousingConfig.MANSION_MIN_BID;
				}
				break;
			case ESTATE:
				if (HousingConfig.ESTATE_MIN_BID > 0) {
					return HousingConfig.ESTATE_MIN_BID;
				}
				break;
			case PALACE:
				if (HousingConfig.PALACE_MIN_BID > 0) {
					return HousingConfig.PALACE_MIN_BID;
				}
				break;
			default:
				break;
		}
		return saleOptions.getGoldPrice();
	}

	@Override
	public String toString() {
		return name;
	}
}
