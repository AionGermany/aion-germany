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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.controllers.FlyController;
import com.aionemu.gameserver.controllers.PlayerController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.controllers.attack.PlayerAggroList;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.controllers.movement.PlayerMoveController;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerVarsDAO;
import com.aionemu.gameserver.dao.PlayerWorldBanDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.WorldBuff;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.cubics.PlayerMCList;
import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeList;
import com.aionemu.gameserver.model.event_window.PlayerEventWindowList;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.CreatureType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.SummonedObject;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank.AbyssRankUpdateType;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.model.gameobjects.player.emotion.EmotionList;
import com.aionemu.gameserver.model.gameobjects.player.equipmentsetting.EquipmentSettingList;
import com.aionemu.gameserver.model.gameobjects.player.f2p.F2p;
import com.aionemu.gameserver.model.gameobjects.player.motion.MotionList;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFactions;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfCooperationRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfDisciplineRank;
import com.aionemu.gameserver.model.gameobjects.player.title.TitleList;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseRegistry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.items.ItemCooldown;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.LegionStorageProxy;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillList;
import com.aionemu.gameserver.model.skinskill.SkillSkinList;
import com.aionemu.gameserver.model.stats.container.PlayerGameStats;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;
import com.aionemu.gameserver.model.team.legion.LegionMember;
import com.aionemu.gameserver.model.team2.TeamMember;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceGroup;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.event.MaxCountOfDay;
import com.aionemu.gameserver.model.templates.flypath.FlyPathEntry;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.ride.RideInfo;
import com.aionemu.gameserver.model.templates.stats.PlayerStatsTemplate;
import com.aionemu.gameserver.model.templates.windstreams.WindstreamPath;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_TOLL_INFO;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.skillengine.condition.ChainCondition;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.RebirthEffect;
import com.aionemu.gameserver.skillengine.effect.ResurrectBaseEffect;
import com.aionemu.gameserver.skillengine.model.ChainSkills;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.task.CraftingTask;
import com.aionemu.gameserver.utils.HumanTime;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.utils.rates.RegularRates;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.zone.ZoneInstance;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * This class is representing Player object, it contains all needed data.
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author alexa026
 * @author cura
 * @author GiGatR00n v4.7.5.x
 */
public class Player extends Creature {

	public RideInfo ride;
	public InRoll inRoll;
	public WindstreamPath windstreamPath;
	private PlayerAppearance playerAppearance;
	private PlayerAppearance savedPlayerAppearance;
	private PlayerCommonData playerCommonData;
	private Account playerAccount;
	private LegionMember legionMember;
	private MacroList macroList;
	private PlayerSkillList skillList;
	private FriendList friendList;
	private BlockList blockList;
	private PetList toyPetList;
	private MinionList minionList;
	private Mailbox mailbox;
	private PrivateStore store;
	private TitleList titleList;
	private QuestStateList questStateList;
	private RecipeList recipeList;
	private List<House> houses;
	private ResponseRequester requester;
	private boolean lookingForGroup = false;
	private boolean lookingForEvent = false;
	private Storage inventory;
	private Storage[] petBag = new Storage[StorageType.PET_BAG_MAX - StorageType.PET_BAG_MIN + 1];
	private Storage[] cabinets = new Storage[StorageType.HOUSE_WH_MAX - StorageType.HOUSE_WH_MIN + 1];
	private Storage regularWarehouse;
	private Storage accountWarehouse;
	private Equipment equipment;
	private HouseRegistry houseRegistry;
	private PlayerStatsTemplate playerStatsTemplate;
	private PlayerSettings playerSettings;
	private com.aionemu.gameserver.model.team2.group.PlayerGroup playerGroup2;
	private PlayerAllianceGroup playerAllianceGroup;
	private final AbsoluteStatOwner absStatsHolder;
	private AbyssRank abyssRank;
	private NpcFactions npcFactions;
	private Rates rates;
	private int flyState = 0;
	private boolean isTrading;
	private long prisonTimer = 0;
	private long startPrison;
	private boolean invul;
	private FlyController flyController;
	private CraftingTask craftingTask;
	private int flightTeleportId;
	private int flightDistance;
	private Summon summon;
	private SummonedObject<?> summonedObj;
	private Pet toyPet;
	private Minion minion;
	private Kisk kisk;
	private boolean isResByPlayer = false;
	private int resurrectionSkill = 0;
	private boolean isFlyingBeforeDeath = false;
	private boolean isGagged = false;
	private boolean edit_mode = false;
	private Npc postman = null;
	private boolean isInResurrectPosState = false;
	private float resPosX = 0;
	private float resPosY = 0;
	private float resPosZ = 0;
	private boolean underNoFPConsum = false;
	private boolean isAdminTeleportation = false;
	private boolean cooldownZero = false;
	private boolean isUnderInvulnerableWing = false;
	private boolean isFlying = false;
	private boolean isWispable = true;
	private boolean isCommandUsed = false;
	private int abyssRankListUpdateMask = 0;
	private BindPointPosition bindPoint;
	private Map<Integer, ItemCooldown> itemCoolDowns;
	private PortalCooldownList portalCooldownList;
	private CraftCooldownList craftCooldownList;
	private HouseObjectCooldownList houseObjectCooldownList;
	private long nextSkillUse;
	private long nextSummonSkillUse;
	private ChainSkills chainSkills;
	private Map<AttackStatus, Long> lastCounterSkill = new HashMap<AttackStatus, Long>();
	private int dualEffectValue = 0;
	private int wordBanTime = 0;
	private boolean bannedFromWorld = false;
	private String bannedFromWorldBy = "";
	private long bannedFromWorldDuring = 0;
	private Date bannedFromWorldDate = null;
	private String bannedFromWorldReason = "";
	private ScheduledFuture<?> taskToUnbanFromWorld = null;
	private Map<Integer, MaxCountOfDay> maxCountEvent;
	private int LunaDiceGame;
	private int LunaDiceGameTry = 0;
	/**
	 * Static information for players
	 */
	private static final int CUBE_SPACE = 9;
	private static final int WAREHOUSE_SPACE = 8;
	private boolean isAttackMode = false;
	private long gatherableTimer = 0;
	private long stopGatherable;
	private String captchaWord;
	private byte[] captchaImage;
	private float instanceStartPosX, instanceStartPosY, instanceStartPosZ;
	private int rebirthResurrectPercent = 1;
	private int rebirthSkill = 0;

	/**
	 * Connection of this Player.
	 */
	private AionConnection clientConnection;
	private FlyPathEntry flyLocationId;
	private long flyStartTime;
	private EmotionList emotions;
	private MotionList motions;
	private int partnerId;
	private long flyReuseTime;
	private boolean isMentor;
	private long lastMsgTime = 0;
	private int floodMsgCount = 0;
	private long onlineTime = 0;
	private long onlineBonusTime = 0;
	private int lootingNpcOid;
	private boolean rebirthRevive;
	// Needed to remove supplements queue
	private int subtractedSupplementsCount;
	private int subtractedSupplementId;
	private int portAnimation;
	private boolean isInSprintMode;
	private List<ActionObserver> rideObservers;
	byte housingStatus = HousingFlags.BUY_STUDIO_ALLOWED.getId();
	private int battleReturnMap;
	private float[] battleReturnCoords;
	private FastList<WorldBuff> worldBuff;
	// This variables are for the FFA system
	// This variables are for the custom RP and GM system
	private boolean isGmMode = false;
	// This variables are for the battleground system
	private int timer = 0;
	private boolean addedStatus;
	private boolean afkmode;
	// This variables are for the custom PvE and PK system
	byte buildingOwnerStates = PlayerHouseOwnerFlags.BUY_STUDIO_ALLOWED.getId();
	public static final int CHAT_NOT_FIXED = 0;
	public static final int CHAT_FIXED_ON_WORLD = 1;
	public static final int CHAT_FIXED_ON_ELYOS = 2;
	public static final int CHAT_FIXED_ON_ASMOS = 4;
	public static final int CHAT_FIXED_ON_BOTH = CHAT_FIXED_ON_ELYOS | CHAT_FIXED_ON_ASMOS;
	public int CHAT_FIX_WORLD_CHANNEL = CHAT_NOT_FIXED;
	private int useAutoGroup = 0;
	private boolean robot = false;
	private int robotId = 0;
	public int FAST_TRACK_TYPE = 0;// 0 = nothing,1 = moved exact current,2 = already moved
	private boolean isOnFastTrack = false;
	private boolean isInLiveParty = false;
	// private int linkedSkill;
	private PlayerConquererProtectorData conquerorProtectorData;

	private PlayerBonusTime bonusTime;
	private boolean newPlayer = false;
	private long creationDay;

	private int playersBonusId = 0;
	@SuppressWarnings("unused")
	private boolean hasBonus;
	private int bonusId = 0;
	private F2p f2p;
	private boolean hasAbyssBonus;
	private int abyssId = 0;
	private List<ActionObserver> hotTeleObservers;
	private int transformModelId;
	private int transformItemId;
	private int transformPanelId;
	private PlayerWardrobeList wardrobe;
	private PlayerLunaShop lunaShop;
	private PlayerSweep shugoSweep;
	private boolean setMinionSpawned;
	private EquipmentSettingList equipmentSettingList;
	private PlayerMCList mc;

	/**
	 * Player Skill Skin List
	 */
	private SkillSkinList skillSkinList;

	/**
	 * Player Event Window List
	 */
	private PlayerEventWindowList eventWindowList;

	/**
	 * Player Event Window List
	 */
	public PlayerEventWindowList getEventWindow() {
		return eventWindowList;
	}

	public void setEventWindow(PlayerEventWindowList eventWindowList) {
		this.eventWindowList = eventWindowList;
	}

	/**
	 * Used for JUnit tests
	 */
	private Player(PlayerCommonData plCommonData) {
		super(plCommonData.getPlayerObjId(), new PlayerController(), null, plCommonData, null);
		this.playerCommonData = plCommonData;
		this.playerAccount = new Account(0);
		this.absStatsHolder = new AbsoluteStatOwner(this, 0);
	}

	public Player(PlayerController controller, PlayerCommonData plCommonData, PlayerAppearance appereance, Account account) {
		super(plCommonData.getPlayerObjId(), controller, null, plCommonData, plCommonData.getPosition());
		this.daoVars = DAOManager.getDAO(PlayerVarsDAO.class);
		this.playerCommonData = plCommonData;
		this.playerAppearance = appereance;
		this.playerAccount = account;

		this.requester = new ResponseRequester(this);
		this.questStateList = new QuestStateList();
		this.titleList = new TitleList();
		this.portalCooldownList = new PortalCooldownList(this);
		this.craftCooldownList = new CraftCooldownList(this);
		this.houseObjectCooldownList = new HouseObjectCooldownList(this);
		this.toyPetList = new PetList(this);
		this.minionList = new MinionList(this);
		controller.setOwner(this);
		moveController = new PlayerMoveController(this);
		plCommonData.setBoundingRadius(new BoundRadius(0.5f, 0.5f, getPlayerAppearance().getHeight()));

		setPlayerStatsTemplate(DataManager.PLAYER_STATS_DATA.getTemplate(this));
		setGameStats(new PlayerGameStats(this));
		setLifeStats(new PlayerLifeStats(this));
		absStatsHolder = new AbsoluteStatOwner(this, 0);
		this.setMinionSpawned = false;
	}

	public boolean isInPlayerMode(PlayerMode mode) {
		return PlayerActions.isInPlayerMode(this, mode);
	}

	public void setPlayerMode(PlayerMode mode, Object obj) {
		PlayerActions.setPlayerMode(this, mode, obj);
	}

	public void unsetPlayerMode(PlayerMode mode) {
		PlayerActions.unsetPlayerMode(this, mode);
	}

	@Override
	public PlayerMoveController getMoveController() {
		return (PlayerMoveController) super.getMoveController();
	}

	@Override
	protected final AggroList createAggroList() {
		return new PlayerAggroList(this);
	}

	public PlayerCommonData getCommonData() {
		return playerCommonData;
	}

	@Override
	public String getName() {
		return playerCommonData.getName();
	}

	public PlayerAppearance getPlayerAppearance() {
		return playerAppearance;
	}

	public void setPlayerAppearance(PlayerAppearance playerAppearance) {
		this.playerAppearance = playerAppearance;
	}

	/**
	 * Only use for the Size admin command
	 *
	 * @return PlayerAppearance : The saved player's appearance, to rollback his appearance
	 */
	public PlayerAppearance getSavedPlayerAppearance() {
		return savedPlayerAppearance;
	}

	/**
	 * Only use for the Size admin command
	 *
	 * @param playerAppearance
	 *            PlayerAppearance : The saved player's appearance, to rollback his appearance
	 */
	public void setSavedPlayerAppearance(PlayerAppearance savedPlayerAppearance) {
		this.savedPlayerAppearance = savedPlayerAppearance;
	}

	/**
	 * Set connection of this player.
	 *
	 * @param clientConnection
	 */
	public void setClientConnection(AionConnection clientConnection) {
		this.clientConnection = clientConnection;
	}

	/**
	 * Get connection of this player.
	 *
	 * @return AionConnection of this player.
	 */
	public AionConnection getClientConnection() {
		return this.clientConnection;
	}

	public MacroList getMacroList() {
		return macroList;
	}

	public void setMacroList(MacroList macroList) {
		this.macroList = macroList;
	}

	public PlayerSkillList getSkillList() {
		return skillList;
	}

	public void setSkillList(PlayerSkillList skillList) {
		this.skillList = skillList;
	}

	/**
	 * @return the toyPet
	 */
	public Pet getPet() {
		return toyPet;
	}

	/**
	 * @param toyPet
	 *            the toyPet to set
	 */
	public void setToyPet(Pet toyPet) {
		this.toyPet = toyPet;
	}

 	/**
	 * @return the minions
	 */
	public Minion getMinion() {
		return minion;
	}

	public void setMinion(Minion minion) {
		this.minion = minion;
	}

	public void setMinionSpawned(boolean setMinionSpawned) {
		this.setMinionSpawned = setMinionSpawned;
	}

	public boolean isMinionSpawned() {
		return setMinionSpawned;
	}

	/**
	 * Gets this players Friend List
	 *
	 * @return FriendList
	 */
	public FriendList getFriendList() {
		return friendList;
	}

	/**
	 * Is this player looking for a group
	 *
	 * @return true or false
	 */
	public boolean isLookingForGroup() {
		return lookingForGroup;
	}

	/**
	 * Sets whether or not this player is looking for a group
	 *
	 * @param lookingForGroup
	 */
	public void setLookingForGroup(boolean lookingForGroup) {
		this.lookingForGroup = lookingForGroup;
	}

	/**
	 * Is this player looking for an event
	 *
	 * @return true or false
	 */
	public boolean isLookingForEvent() {
		return lookingForEvent;
	}

	/**
	 * Sets whether or not this player is looking for an event
	 *
	 * @param lookingForEvent
	 */
	public void setLookingForEvent(boolean lookingForEvent) {
		this.lookingForEvent = lookingForEvent;
	}

	public boolean isAttackMode() {
		return isAttackMode;
	}

	public void setAttackMode(boolean isAttackMode) {
		this.isAttackMode = isAttackMode;
	}

	public boolean isNotGatherable() {
		return gatherableTimer != 0;
	}

	public void setGatherableTimer(long gatherableTimer) {
		if (gatherableTimer < 0) {
			gatherableTimer = 0;
		}

		this.gatherableTimer = gatherableTimer;
	}

	public long getGatherableTimer() {
		return gatherableTimer;
	}

	public long getStopGatherable() {
		return stopGatherable;
	}

	public void setStopGatherable(long stopGatherable) {
		this.stopGatherable = stopGatherable;
	}

	public String getCaptchaWord() {
		return captchaWord;
	}

	public void setCaptchaWord(String captchaWord) {
		this.captchaWord = captchaWord;
	}

	public byte[] getCaptchaImage() {
		return captchaImage;
	}

	public void setCaptchaImage(byte[] captchaImage) {
		this.captchaImage = captchaImage;
	}

	/**
	 * Sets this players friend list. <br />
	 * Remember to send the player the <tt>SM_FRIEND_LIST</tt> packet.
	 *
	 * @param list
	 */
	public void setFriendList(FriendList list) {
		this.friendList = list;
	}

	public BlockList getBlockList() {
		return blockList;
	}

	public void setBlockList(BlockList list) {
		this.blockList = list;
	}

	public final PetList getPetList() {
		return toyPetList;
	}

	public final MinionList getMinionList() {
		return minionList;
	}

	@Override
	public PlayerLifeStats getLifeStats() {
		return (PlayerLifeStats) super.getLifeStats();
	}

	@Override
	public PlayerGameStats getGameStats() {
		return (PlayerGameStats) super.getGameStats();
	}

	/**
	 * Gets the ResponseRequester for this player
	 *
	 * @return ResponseRequester
	 */
	public ResponseRequester getResponseRequester() {
		return requester;
	}

	public boolean isOnline() {
		return getClientConnection() != null;
	}

	public void setCubeExpands(int cubeExpands) {
		this.playerCommonData.setCubeExpands(cubeExpands);
		getInventory().setLimit(getInventory().getLimit() + (cubeExpands) * CUBE_SPACE);
	}

	public int getCubeExpands() {
		return this.playerCommonData.getCubeExpands();
	}

	public PlayerClass getPlayerClass() {
		return playerCommonData.getPlayerClass();
	}

	public Gender getGender() {
		return playerCommonData.getGender();
	}

	/**
	 * Return PlayerController of this Player Object.
	 *
	 * @return PlayerController.
	 */
	@Override
	public PlayerController getController() {
		return (PlayerController) super.getController();
	}

	@Override
	public byte getLevel() {
		return (byte) playerCommonData.getLevel();
	}

	/**
	 * @return the inventory
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	/**
	 * @return the player private store
	 */
	public PrivateStore getStore() {
		return store;
	}

	/**
	 * @param store
	 *            the store that needs to be set
	 */
	public void setStore(PrivateStore store) {
		this.store = store;
	}

	/**
	 * @return the questStatesList
	 */
	public QuestStateList getQuestStateList() {
		return questStateList;
	}

	/**
	 * @param questStateList
	 *            the QuestStateList to set
	 */
	public void setQuestStateList(QuestStateList questStateList) {
		this.questStateList = questStateList;
	}

	/**
	 * @return the playerStatsTemplate
	 */
	public PlayerStatsTemplate getPlayerStatsTemplate() {
		return playerStatsTemplate;
	}

	/**
	 * @param playerStatsTemplate
	 *            the playerStatsTemplate to set
	 */
	public void setPlayerStatsTemplate(PlayerStatsTemplate playerStatsTemplate) {
		this.playerStatsTemplate = playerStatsTemplate;
	}

	public RecipeList getRecipeList() {
		return recipeList;
	}

	public void setRecipeList(RecipeList recipeList) {
		this.recipeList = recipeList;
	}

	/**
	 * @param inventory
	 *            the inventory to set Inventory should be set right after player object is created
	 */
	public void setStorage(Storage storage, StorageType storageType) {
		if (storageType == StorageType.CUBE) {
			this.inventory = storage;
		}
		if (storageType.getId() >= StorageType.PET_BAG_MIN && storageType.getId() <= StorageType.PET_BAG_MAX) {
			this.petBag[storageType.getId() - StorageType.PET_BAG_MIN] = storage;
		}
		if (storageType.getId() >= StorageType.HOUSE_WH_MIN && storageType.getId() <= StorageType.HOUSE_WH_MAX) {
			this.cabinets[storageType.getId() - StorageType.HOUSE_WH_MIN] = storage;
		}
		if (storageType == StorageType.REGULAR_WAREHOUSE) {
			this.regularWarehouse = storage;
		}
		if (storageType == StorageType.ACCOUNT_WAREHOUSE) {
			this.accountWarehouse = storage;
		}
		storage.setOwner(this);
	}

	/**
	 * @param storageType
	 * @return
	 */
	public IStorage getStorage(int storageType) {
		if (storageType == StorageType.REGULAR_WAREHOUSE.getId()) {
			return regularWarehouse;
		}

		if (storageType == StorageType.ACCOUNT_WAREHOUSE.getId()) {
			return accountWarehouse;
		}

		if (storageType == StorageType.LEGION_WAREHOUSE.getId() && getLegion() != null) {
			return new LegionStorageProxy(getLegion().getLegionWarehouse(), this);
		}

		if (storageType >= StorageType.PET_BAG_MIN && storageType <= StorageType.PET_BAG_MAX) {
			return petBag[storageType - StorageType.PET_BAG_MIN];
		}

		if (storageType >= StorageType.HOUSE_WH_MIN && storageType <= StorageType.HOUSE_WH_MAX) {
			return cabinets[storageType - StorageType.HOUSE_WH_MIN];
		}

		if (storageType == StorageType.CUBE.getId()) {
			return inventory;
		}
		return null;
	}

	/**
	 * Items from UPDATE_REQUIRED storages and equipment
	 *
	 * @return
	 */
	public List<Item> getDirtyItemsToUpdate() {
		List<Item> dirtyItems = new ArrayList<Item>();

		IStorage cubeStorage = getStorage(StorageType.CUBE.getId());
		if (cubeStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
			dirtyItems.addAll(cubeStorage.getItemsWithKinah());
			dirtyItems.addAll(cubeStorage.getDeletedItems());
			cubeStorage.setPersistentState(PersistentState.UPDATED);
		}

		IStorage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
		if (regularWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
			dirtyItems.addAll(regularWhStorage.getItemsWithKinah());
			dirtyItems.addAll(regularWhStorage.getDeletedItems());
			regularWhStorage.setPersistentState(PersistentState.UPDATED);
		}

		IStorage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
		if (accountWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
			dirtyItems.addAll(accountWhStorage.getItemsWithKinah());
			dirtyItems.addAll(accountWhStorage.getDeletedItems());
			accountWhStorage.setPersistentState(PersistentState.UPDATED);
		}

		IStorage legionWhStorage = getStorage(StorageType.LEGION_WAREHOUSE.getId());
		if (legionWhStorage != null) {
			if (legionWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
				dirtyItems.addAll(legionWhStorage.getItemsWithKinah());
				dirtyItems.addAll(legionWhStorage.getDeletedItems());
				legionWhStorage.setPersistentState(PersistentState.UPDATED);
			}
		}

		for (int petBagId = StorageType.PET_BAG_MIN; petBagId <= StorageType.PET_BAG_MAX; petBagId++) {
			IStorage petBag = getStorage(petBagId);
			if (petBag != null && petBag.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
				dirtyItems.addAll(petBag.getItemsWithKinah());
				dirtyItems.addAll(petBag.getDeletedItems());
				petBag.setPersistentState(PersistentState.UPDATED);
			}
		}

		for (int houseWhId = StorageType.HOUSE_WH_MIN; houseWhId <= StorageType.HOUSE_WH_MAX; houseWhId++) {
			IStorage cabinet = getStorage(houseWhId);
			if (cabinet != null && cabinet.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
				dirtyItems.addAll(cabinet.getItemsWithKinah());
				dirtyItems.addAll(cabinet.getDeletedItems());
				cabinet.setPersistentState(PersistentState.UPDATED);
			}
		}

		Equipment equipment = getEquipment();
		if (equipment.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
			dirtyItems.addAll(equipment.getEquippedItems());
			equipment.setPersistentState(PersistentState.UPDATED);
		}

		return dirtyItems;
	}

	/**
	 * //TODO probably need to optimize here
	 *
	 * @return
	 */
	public FastList<Item> getAllItems() {
		FastList<Item> items = FastList.newInstance();
		items.addAll(this.inventory.getItemsWithKinah());
		if (this.regularWarehouse != null) {
			items.addAll(this.regularWarehouse.getItemsWithKinah());
		}
		if (this.accountWarehouse != null) {
			items.addAll(this.accountWarehouse.getItemsWithKinah());
		}

		for (int petBagId = StorageType.PET_BAG_MIN; petBagId <= StorageType.PET_BAG_MAX; petBagId++) {
			IStorage petBag = getStorage(petBagId);
			if (petBag != null) {
				items.addAll(petBag.getItemsWithKinah());
			}
		}

		for (int houseWhId = StorageType.HOUSE_WH_MIN; houseWhId <= StorageType.HOUSE_WH_MAX; houseWhId++) {
			IStorage cabinet = getStorage(houseWhId);
			if (cabinet != null) {
				items.addAll(cabinet.getItemsWithKinah());
			}
		}

		items.addAll(getEquipment().getEquippedItems());
		return items;
	}

	public Storage getInventory() {
		return inventory;
	}

	/**
	 * @return the playerSettings
	 */
	public PlayerSettings getPlayerSettings() {
		return playerSettings;
	}

	/**
	 * @param playerSettings
	 *            the playerSettings to set
	 */
	public void setPlayerSettings(PlayerSettings playerSettings) {
		this.playerSettings = playerSettings;
	}

	public TitleList getTitleList() {
		return titleList;
	}

	public void setTitleList(TitleList titleList) {
		if (havePermission(MembershipConfig.TITLES_ADDITIONAL_ENABLE)) {
			titleList.addEntry(102, 0);
			titleList.addEntry(103, 0);
			titleList.addEntry(104, 0);
			titleList.addEntry(105, 0);
			titleList.addEntry(106, 0);
			titleList.addEntry(146, 0);
			titleList.addEntry(151, 0);
			titleList.addEntry(152, 0);
			titleList.addEntry(160, 0);
			titleList.addEntry(161, 0);
		}
		this.titleList = titleList;
		titleList.setOwner(this);
	}

	public com.aionemu.gameserver.model.team2.group.PlayerGroup getPlayerGroup2() {
		return playerGroup2;
	}

	public void setPlayerGroup2(com.aionemu.gameserver.model.team2.group.PlayerGroup playerGroup) {
		this.playerGroup2 = playerGroup;
	}

	/**
	 * @return the abyssRank
	 */
	public AbyssRank getAbyssRank() {
		return abyssRank;
	}

	/**
	 * @param abyssRank
	 *            the abyssRank to set
	 */
	public void setAbyssRank(AbyssRank abyssRank) {
		this.abyssRank = abyssRank;
	}

	@Override
	public PlayerEffectController getEffectController() {
		return (PlayerEffectController) super.getEffectController();
	}

	public void onLoggedIn() {
		friendList.setStatus(Status.ONLINE, getCommonData());
	}

	public void onLoggedOut() {
		requester.denyAll();
		friendList.setStatus(FriendList.Status.OFFLINE, getCommonData());
	}

	/**
	 * Returns true if has valid LegionMember
	 */
	public boolean isLegionMember() {
		return legionMember != null;
	}

	/**
	 * @param legionMember
	 *            the legionMember to set
	 */
	public void setLegionMember(LegionMember legionMember) {
		this.legionMember = legionMember;
	}

	/**
	 * @return the legionMember
	 */
	public LegionMember getLegionMember() {
		return legionMember;
	}

	/**
	 * @return the legion
	 */
	public Legion getLegion() {
		return legionMember != null ? legionMember.getLegion() : null;
	}

	/**
	 * Checks if object id's are the same
	 *
	 * @return true if the object id is the same
	 */
	public boolean sameObjectId(int objectId) {
		return this.getObjectId() == objectId;
	}

	/**
	 * @return true if a player has a store opened
	 */
	public boolean hasStore() {
		if (getStore() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Removes legion from player
	 */
	public void resetLegionMember() {
		setLegionMember(null);
	}

	public boolean isInGroup2() {
		return playerGroup2 != null;
	}

	/**
	 * Access level of this player
	 *
	 * @return byte
	 */
	public byte getAccessLevel() {
		return playerAccount.getAccessLevel();
	}

	/**
	 * accountName of this player
	 *
	 * @return int
	 */
	public String getAcountName() {
		return playerAccount.getName();
	}

	/**
	 * @return the rates
	 */
	public Rates getRates() {
		if (rates == null) {
			rates = new RegularRates();
		}
		return rates;
	}

	/**
	 * @param rates
	 *            the rates to set
	 */
	public void setRates(Rates rates) {
		this.rates = rates;
	}

	/**
	 * @return warehouse size
	 */
	public int getWarehouseSize() {
		return this.playerCommonData.getWarehouseSize();
	}

	/**
	 * @param warehouseSize
	 */
	public void setWarehouseSize(int warehouseSize) {
		this.playerCommonData.setWarehouseSize(warehouseSize);
		getWarehouse().setLimit(getWarehouse().getLimit() + (warehouseSize * WAREHOUSE_SPACE));
	}

	/**
	 * @return regularWarehouse
	 */
	public Storage getWarehouse() {
		return regularWarehouse;
	}

	/**
	 * 0: regular, 1: fly, 2: glide
	 */
	public int getFlyState() {
		return this.flyState;
	}

	public void setFlyState(int flyState) {
		this.flyState = flyState;
		if (flyState == 1) {
			setFlyingMode(true);
		}
		else if (flyState == 0) {
			setFlyingMode(false);
		}
	}

	/**
	 * @return the isTrading
	 */
	public boolean isTrading() {
		return isTrading;
	}

	/**
	 * @param isTrading
	 *            the isTrading to set
	 */
	public void setTrading(boolean isTrading) {
		this.isTrading = isTrading;
	}

	/**
	 * @return the isInPrison
	 */
	public boolean isInPrison() {
		return prisonTimer != 0;
	}

	/**
	 * @param prisonTimer
	 *            the prisonTimer to set
	 */
	public void setPrisonTimer(long prisonTimer) {
		if (prisonTimer < 0) {
			prisonTimer = 0;
		}

		this.prisonTimer = prisonTimer;
	}

	/**
	 * @return the prisonTimer
	 */
	public long getPrisonTimer() {
		return prisonTimer;
	}

	/**
	 * @return the time in ms of start prison
	 */
	public long getStartPrison() {
		return startPrison;
	}

	/**
	 * @param start The time in ms of start prison
	 */
	public void setStartPrison(long start) {
		this.startPrison = start;
	}

	/**
	 * @return
	 */
	public boolean isProtectionActive() {
		return isInVisualState(CreatureVisualState.BLINKING);
	}

	/**
	 * Check is player is invul
	 *
	 * @return boolean
	 */
	public boolean isInvul() {
		return invul;
	}

	/**
	 * Sets invul on player
	 *
	 * @param invul
	 *            - boolean
	 */
	public void setInvul(boolean invul) {
		this.invul = invul;
	}

	public void setMailbox(Mailbox mailbox) {
		this.mailbox = mailbox;
	}

	public Mailbox getMailbox() {
		return mailbox;
	}

	/**
	 * @return the flyController
	 */
	public FlyController getFlyController() {
		return flyController;
	}

	/**
	 * @param flyController
	 *            the flyController to set
	 */
	public void setFlyController(FlyController flyController) {
		this.flyController = flyController;
	}

	public int getLastOnline() {
		Timestamp lastOnline = playerCommonData.getLastOnline();
		if (lastOnline == null || isOnline()) {
			return 0;
		}

		return (int) (lastOnline.getTime() / 1000);
	}

	/**
	 * @param craftingTask
	 */
	public void setCraftingTask(CraftingTask craftingTask) {
		this.craftingTask = craftingTask;
	}

	/**
	 * @return
	 */
	public CraftingTask getCraftingTask() {
		return craftingTask;
	}

	/**
	 * @param flightTeleportId
	 */
	public void setFlightTeleportId(int flightTeleportId) {
		this.flightTeleportId = flightTeleportId;
	}

	/**
	 * @return flightTeleportId
	 */
	public int getFlightTeleportId() {
		return flightTeleportId;
	}

	/**
	 * @param flightDistance
	 */
	public void setFlightDistance(int flightDistance) {
		this.flightDistance = flightDistance;

	}

	/**
	 * @param path
	 */
	public void setCurrentFlypath(FlyPathEntry path) {
		this.flyLocationId = path;
		if (path != null) {
			this.flyStartTime = System.currentTimeMillis();
		}
		else {
			this.flyStartTime = 0;
		}
	}

	/**
	 * @return flightDistance
	 */
	public int getFlightDistance() {
		return flightDistance;
	}

	/**
	 * @return
	 */
	public boolean isUsingFlyTeleport() {
		return isInState(CreatureState.FLIGHT_TELEPORT) && flightTeleportId != 0;
	}

	public boolean isGM() {
		return getAccessLevel() >= AdminConfig.GM_LEVEL;
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return creature.isEnemyFrom(this) || this.isEnemyFrom(creature);
	}

	@Override
	public boolean isEnemyFrom(Npc enemy) {
		switch (CreatureType.getCreatureType(enemy.getType(this))) {
			case AGGRESSIVE:
			case ATTACKABLE:
			case INVULNERABLE:
				return true;
			default:
				break;
		}
		return false;
	}

	/**
	 * Player enemies:<br>
	 * - different race<br>
	 * - duel partner<br>
	 * - in pvp zone - in ffa zone
	 *
	 * @param enemy
	 * @return
	 */
	@Override
	public boolean isEnemyFrom(Player enemy) {
		if (this.getObjectId() == enemy.getObjectId()) {
			return false;
		}
		else if ((this.getAdminEnmity() > 1 || enemy.getAdminEnmity() > 1)) {
			return false;
		}
		else if (canPvP(enemy) || this.getController().isDueling(enemy)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isAggroIconTo(Player player) {
		if (getAdminEnmity() > 1 || player.getAdminEnmity() > 1) {
			return true;
		}
		return !player.getRace().equals(getRace());
	}

	private boolean canPvP(Player enemy) {
		int worldId = enemy.getWorldId();
		if (!enemy.getRace().equals(getRace())) {
			if (World.getInstance().getWorldMap(getWorldId()).isPvpAllowed()) {
				return (!this.isInDisablePvPZone() && !enemy.isInDisablePvPZone());
			}
			else {
				return (this.isInPvPZone() && enemy.isInPvPZone());
			}
		}
		else {
			if (worldId != 210020000 && // Elten.
				worldId != 210040000 && // Heiron.
				worldId != 210050000 && // Inggison.
				worldId != 210060000 && // Theobomos.
				worldId != 210070000 && // Cygnea.
				worldId != 210100000 && // Iluma.
				worldId != 220020000 && // Morheim.
				worldId != 220040000 && // Beluslan.
				worldId != 220050000 && // Brusthonin.
				worldId != 220070000 && // Gelkmaros.
				worldId != 220080000 && // Enshar.
				worldId != 220110000 && // Norsvold.
				// \\//\\//\\//\\//\\//
				worldId != 400010000 && // Reshanta.
				// \\//Panesterra//\\//
				worldId != 400020000 && // Belus.
				worldId != 400040000 && // Aspida.
				worldId != 400050000 && // Atanatos.
				worldId != 400060000 && // Disillon.
				// \\//\\//\\//\\//\\//
				worldId != 600010000 && // Silentera Canyon.
				worldId != 600090000 && // Kaldor.
				worldId != 600100000) { // Levinshor.
				return (this.isInsideZoneType(ZoneType.PVP) && enemy.isInsideZoneType(ZoneType.PVP) && !isInSameTeam(enemy));
			}
		}
		return false;
	}

	private boolean isInDisablePvPZone() {
		List<ZoneInstance> zones = this.getPosition().getMapRegion().getZones(this);
		for (ZoneInstance zone : zones) {
			if (!zone.isPvpAllowed()) {
				return true;
			}
		}
		return false;
	}

	private boolean isInPvPZone() {
		List<ZoneInstance> zones = this.getPosition().getMapRegion().getZones(this);
		for (ZoneInstance zone : zones) {
			if (zone.isPvpAllowed()) {
				return true;
			}
		}
		return false;
	}

	public boolean isInSameTeam(Player player) {
		if (isInGroup2() && player.isInGroup2()) {
			return getPlayerGroup2().getTeamId().equals(player.getPlayerGroup2().getTeamId());
		}
		else if (isInAlliance2() && player.isInAlliance2()) {
			return getPlayerAlliance2().getObjectId().equals(player.getPlayerAlliance2().getObjectId());
		}
		else if (isInLeague() && player.isInLeague()) {
			return getPlayerAllianceGroup2().getObjectId().equals(player.getPlayerAllianceGroup2().getObjectId());
		}
		return false;
	}

	@Override
	public boolean canSee(Creature creature) {
		if (creature.isInVisualState(CreatureVisualState.BLINKING)) {
			return true;
		}

		if (creature instanceof Player && isInSameTeam((Player) creature)) {
			return true;
		}

		if (creature instanceof Trap && ((Trap) creature).getCreator().getObjectId() == this.getObjectId()) {
			return true;
		}

		return creature.getVisualState() <= getSeeState();
	}

	@Override
	public TribeClass getTribe() {
		TribeClass transformTribe = getTransformModel().getTribe();
		if (transformTribe != null) {
			return transformTribe;
		}
		return getRace() == Race.ELYOS ? TribeClass.PC : TribeClass.PC_DARK;
	}

	@Override
	public TribeClass getBaseTribe() {
		TribeClass transformTribe = getTransformModel().getTribe();
		if (transformTribe != null) {
			return DataManager.TRIBE_RELATIONS_DATA.getBaseTribe(transformTribe);
		}
		return getTribe();
	}

	/**
	 * @return the summon
	 */
	public Summon getSummon() {
		return summon;
	}

	/**
	 * @param summon
	 *            the summon to set
	 */
	public void setSummon(Summon summon) {
		this.summon = summon;
	}

	/**
	 * @return the summoned object
	 */
	public SummonedObject<?> getSummonedObj() {
		return summonedObj;
	}

	/**
	 * @param summonedObj
	 *            the summoned object to set
	 */
	public void setSummonedObj(SummonedObject<?> summonedObj) {
		this.summonedObj = summonedObj;
	}

	/**
	 * @param new
	 *            kisk to bind to (null if unbinding)
	 */
	public void setKisk(Kisk newKisk) {
		this.kisk = newKisk;
	}

	/**
	 * @return
	 */
	public Kisk getKisk() {
		return this.kisk;
	}

	/**
	 * @param delayId
	 * @return
	 */
	public boolean isItemUseDisabled(ItemUseLimits limits) {
		if (limits == null) {
			return false;
		}

		if (itemCoolDowns == null || !itemCoolDowns.containsKey(limits.getDelayId())) {
			return false;
		}

		Long coolDown = itemCoolDowns.get(limits.getDelayId()).getReuseTime();
		if (coolDown <= 0) {
			return false;
		}

		if (coolDown < System.currentTimeMillis()) {
			itemCoolDowns.remove(limits.getDelayId());
			return false;
		}

		return true;
	}

	/**
	 * @param delayId
	 * @return
	 */
	public long getItemCoolDown(int delayId) {
		if (itemCoolDowns == null || !itemCoolDowns.containsKey(delayId)) {
			return 0;
		}

		return itemCoolDowns.get(delayId).getReuseTime();
	}

	/**
	 * @return the itemCoolDowns
	 */
	public Map<Integer, ItemCooldown> getItemCoolDowns() {
		return itemCoolDowns;
	}

	/**
	 * @param delayId
	 * @param time
	 * @param useDelay
	 */
	public void addItemCoolDown(int delayId, long time, int useDelay) {
		if (itemCoolDowns == null) {
			itemCoolDowns = new FastMap<Integer, ItemCooldown>().shared();
		}

		itemCoolDowns.put(delayId, new ItemCooldown(time, useDelay));
	}

	/**
	 * @param itemMask
	 */
	public void removeItemCoolDown(int itemMask) {
		if (itemCoolDowns == null) {
			return;
		}
		itemCoolDowns.remove(itemMask);
	}

	/**
	 * @param isGagged
	 *            the isGagged to set
	 */
	public void setGagged(boolean isGagged) {
		this.isGagged = isGagged;
	}

	/**
	 * @return the isGagged
	 */
	public boolean isGagged() {
		return isGagged;
	}

	/**
	 * @return isAdminTeleportation
	 */
	public boolean getAdminTeleportation() {
		return isAdminTeleportation;
	}

	/**
	 * @param isAdminTeleportation
	 */
	public void setAdminTeleportation(boolean isAdminTeleportation) {
		this.isAdminTeleportation = isAdminTeleportation;
	}

	public final boolean isCoolDownZero() {
		return cooldownZero;
	}

	public final void setCoolDownZero(boolean cooldownZero) {
		this.cooldownZero = cooldownZero;
	}

	public void setPlayerResActivate(boolean isActivated) {
		this.isResByPlayer = isActivated;
	}

	public boolean getResStatus() {
		return isResByPlayer;
	}

	public int getResurrectionSkill() {
		return resurrectionSkill;
	}

	public void setResurrectionSkill(int resurrectionSkill) {
		this.resurrectionSkill = resurrectionSkill;
	}

	public void setIsFlyingBeforeDeath(boolean isActivated) {
		this.isFlyingBeforeDeath = isActivated;
	}

	public boolean getIsFlyingBeforeDeath() {
		return isFlyingBeforeDeath;
	}

	public com.aionemu.gameserver.model.team2.alliance.PlayerAlliance getPlayerAlliance2() {
		return playerAllianceGroup != null ? playerAllianceGroup.getAlliance() : null;
	}

	public PlayerAllianceGroup getPlayerAllianceGroup2() {
		return playerAllianceGroup;
	}

	public boolean isInAlliance2() {
		return playerAllianceGroup != null;
	}

	public void setPlayerAllianceGroup2(PlayerAllianceGroup playerAllianceGroup) {
		this.playerAllianceGroup = playerAllianceGroup;
	}

	public final boolean isInLeague() {
		return isInAlliance2() && getPlayerAlliance2().isInLeague();
	}

	public final boolean isInTeam() {
		return isInGroup2() || isInAlliance2();
	}

	/**
	 * @return current {@link PlayerGroup}, {@link PlayerAlliance} or null
	 */
	public final TemporaryPlayerTeam<? extends TeamMember<Player>> getCurrentTeam() {
		return isInGroup2() ? getPlayerGroup2() : getPlayerAlliance2();
	}

	/**
	 * @return current {@link PlayerGroup}, {@link PlayerAllianceGroup} or null
	 */
	public final TemporaryPlayerTeam<? extends TeamMember<Player>> getCurrentGroup() {
		return isInGroup2() ? getPlayerGroup2() : getPlayerAllianceGroup2();
	}

	/**
	 * @return current team id
	 */
	public final int getCurrentTeamId() {
		return isInTeam() ? getCurrentTeam().getTeamId() : 0;
	}

	/**
	 * @param worldId
	 * @return
	 */
	public PortalCooldownList getPortalCooldownList() {
		return portalCooldownList;
	}

	public CraftCooldownList getCraftCooldownList() {
		return craftCooldownList;
	}

	public HouseObjectCooldownList getHouseObjectCooldownList() {
		return houseObjectCooldownList;
	}

	/**
	 * @author IlBuono
	 */
	public void setEditMode(boolean edit_mode) {
		this.edit_mode = edit_mode;
	}

	/**
	 * @author IlBuono
	 */
	public boolean isInEditMode() {
		return edit_mode;
	}

	public Npc getPostman() {
		return postman;
	}

	public void setPostman(Npc postman) {
		this.postman = postman;
	}

	public Account getPlayerAccount() {
		return playerAccount;
	}

	/**
	 * Quest completion
	 *
	 * @param questId
	 * @return
	 */
	public boolean isCompleteQuest(int questId) {
		QuestState qs = getQuestStateList().getQuestState(questId);

		if (qs == null) {
			return false;
		}

		return qs.getStatus() == QuestStatus.COMPLETE;
	}

	public long getNextSkillUse() {
		return nextSkillUse;
	}

	public void setNextSkillUse(long nextSkillUse) {
		this.nextSkillUse = nextSkillUse;
	}

	public long getNextSummonSkillUse() {
		return nextSummonSkillUse;
	}

	public void setNextSummonSkillUse(long nextSummonSkillUse) {
		this.nextSummonSkillUse = nextSummonSkillUse;
	}

	/**
	 * chain skills
	 */
	public ChainSkills getChainSkills() {
		if (this.chainSkills == null) {
			this.chainSkills = new ChainSkills();
		}
		return this.chainSkills;
	}

	public void setLastCounterSkill(AttackStatus status) {
		long time = System.currentTimeMillis();
		// Dodge
		if (AttackStatus.getBaseStatus(status) == AttackStatus.DODGE && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.WARRIOR || PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.SCOUT || PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.ENGINEER) {
			this.lastCounterSkill.put(AttackStatus.DODGE, time);
		}
		// Parry
		else if (AttackStatus.getBaseStatus(status) == AttackStatus.PARRY && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.WARRIOR || PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.PRIEST || PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.ENGINEER) {
			this.lastCounterSkill.put(AttackStatus.PARRY, time);
		}
		// Block
		else if (AttackStatus.getBaseStatus(status) == AttackStatus.BLOCK && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.WARRIOR) {
			this.lastCounterSkill.put(AttackStatus.BLOCK, time);
		}
		// Resist
		else if (AttackStatus.getBaseStatus(status) == AttackStatus.RESIST && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.WARRIOR || PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.ENGINEER) {
			this.lastCounterSkill.put(AttackStatus.RESIST, time);
		}
	}

	public long getLastCounterSkill(AttackStatus status) {
		if (this.lastCounterSkill.get(status) == null) {
			return 0;
		}

		return this.lastCounterSkill.get(status);
	}

	/**
	 * @return the dualEffectValue
	 */
	public int getDualEffectValue() {
		return dualEffectValue;
	}

	/**
	 * @param dualEffectValue
	 *            the dualEffectValue to set
	 */
	public void setDualEffectValue(int dualEffectValue) {
		this.dualEffectValue = dualEffectValue;
	}

	/**
	 * @return the Resurrection Positional State
	 */
	public boolean isInResPostState() {
		return this.isInResurrectPosState;
	}

	/**
	 * @param the
	 *            Resurrection Positional State to set
	 */
	public void setResPosState(boolean value) {
		this.isInResurrectPosState = value;
	}

	/**
	 * @param the
	 *            Resurrection Positional X value to set
	 */
	public void setResPosX(float value) {
		this.resPosX = value;
	}

	/**
	 * @return the Resurrection Positional X value
	 */
	public float getResPosX() {
		return this.resPosX;
	}

	/**
	 * @param the
	 *            Resurrection Positional Y value to set
	 */
	public void setResPosY(float value) {
		this.resPosY = value;
	}

	/**
	 * @return the Resurrection Positional Y value
	 */
	public float getResPosY() {
		return this.resPosY;
	}

	/**
	 * @param the
	 *            Resurrection Positional Z value to set
	 */
	public void setResPosZ(float value) {
		this.resPosZ = value;
	}

	/**
	 * @return the Resurrection Positional Z value
	 */
	public float getResPosZ() {
		return this.resPosZ;
	}

	public boolean isInSiegeWorld() {
		switch (getWorldId()) {
			case 210050000:
			case 220070000:
			case 400010000:
				// case 600030000: Tiamaranta
			case 600050000:
			case 600060000:
				return true;
			default:
				return false;
		}
	}

	public boolean isInPvPArena() {
		switch (getWorldId()) {
			case 300350000:
			case 300360000:
			case 300420000:
			case 300430000:
			case 300450000:
			case 300550000:
			case 300570000:
				return true;
		}
		return false;
	}

	/**
	 * @return true if player is under NoFly Effect
	 */
	public boolean isUnderNoFly() {
		return this.getEffectController().isAbnormalSet(AbnormalState.NOFLY);
	}

	/**
	 * @param the
	 *            status of NoFpConsum Effect
	 */
	public void setUnderNoFPConsum(boolean value) {
		this.underNoFPConsum = value;
	}

	/**
	 * @return true if player is under NoFpConsumEffect
	 */
	public boolean isUnderNoFPConsum() {
		return this.underNoFPConsum;
	}

	public void setInstanceStartPos(float instanceStartPosX, float instanceStartPosY, float instanceStartPosZ) {
		this.instanceStartPosX = instanceStartPosX;
		this.instanceStartPosY = instanceStartPosY;
		this.instanceStartPosZ = instanceStartPosZ;
	}

	public float getInstanceStartPosX() {
		return instanceStartPosX;
	}

	public float getInstanceStartPosY() {
		return instanceStartPosY;
	}

	public float getInstanceStartPosZ() {
		return instanceStartPosZ;
	}

	public boolean havePermission(byte perm) {
		return playerAccount.getMembership() >= perm;
	}

	/**
	 * @return Returns the emotions.
	 */
	public EmotionList getEmotions() {
		return emotions;
	}

	/**
	 * @param emotions
	 *            The emotions to set.
	 */
	public void setEmotions(EmotionList emotions) {
		this.emotions = emotions;
	}

	public int getRebirthResurrectPercent() {
		return rebirthResurrectPercent;
	}

	public void setRebirthResurrectPercent(int rebirthResurrectPercent) {
		this.rebirthResurrectPercent = rebirthResurrectPercent;
	}

	public int getRebirthSkill() {
		return rebirthSkill;
	}

	public void setRebirthSkill(int rebirthSkill) {
		this.rebirthSkill = rebirthSkill;
	}

	public BindPointPosition getBindPoint() {
		return bindPoint;
	}

	public void setBindPoint(BindPointPosition bindPoint) {
		this.bindPoint = bindPoint;
	}

	public int speedHackCounter;
	public int abnormalHackCounter;
	public long prevPosUT;
	public byte prevMoveType;
	private WorldPosition prevPos;

	@Override
	public ItemAttackType getAttackType() {
		Item weapon = getEquipment().getMainHandWeapon();
		if (weapon != null) {
			return weapon.getItemTemplate().getAttackType();
		}
		return ItemAttackType.PHYSICAL;
	}

	public long getFlyStartTime() {
		return this.flyStartTime;
	}

	public FlyPathEntry getCurrentFlyPath() {
		return flyLocationId;
	}

	public void setUnWispable() {
		this.isWispable = false;
	}

	public void setWispable() {
		this.isWispable = true;
	}

	public boolean isWispable() {
		return isWispable;
	}

	public boolean isInvulnerableWing() {
		return this.isUnderInvulnerableWing;
	}

	public void setInvulnerableWing(boolean value) {
		this.isUnderInvulnerableWing = value;
	}

	public void resetAbyssRankListUpdated() {
		this.abyssRankListUpdateMask = 0;
	}

	public void setAbyssRankListUpdated(AbyssRankUpdateType type) {
		this.abyssRankListUpdateMask |= type.value();
	}

	public boolean isAbyssRankListUpdated(AbyssRankUpdateType type) {
		return (this.abyssRankListUpdateMask & type.value()) == type.value();
	}

	public void addSalvationPoints(long points) {
		this.playerCommonData.addSalvationPoints(points);
		PacketSendUtility.sendPacket(this, new SM_STATS_INFO(this));
	}

	@Override
	public byte isPlayer() {
		if (this.isGM()) {
			return 2;
		}
		else {
			return 1;
		}
	}

	/**
	 * @return the motions
	 */
	public MotionList getMotions() {
		return motions;
	}

	/**
	 * @param motions
	 *            the motions to set
	 */
	public void setMotions(MotionList motions) {
		this.motions = motions;
	}

	public void setTransformed(boolean value) {
		this.getTransformModel().setActive(value);
	}

	public boolean isTransformed() {
		return this.getTransformModel().isActive();
	}

	/**
	 * @return the npcFactions
	 */
	public NpcFactions getNpcFactions() {
		return npcFactions;
	}

	/**
	 * @param npcFactions
	 *            the npcFactions to set
	 */
	public void setNpcFactions(NpcFactions npcFactions) {
		this.npcFactions = npcFactions;
	}

	/**
	 * @return the flyReuseTime
	 */
	public long getFlyReuseTime() {
		return flyReuseTime;
	}

	/**
	 * @param flyReuseTime
	 *            the flyReuseTime to set
	 */
	public void setFlyReuseTime(long flyReuseTime) {
		this.flyReuseTime = flyReuseTime;
	}

	/**
	 * @param the
	 *            flying mode flag to set
	 */
	public void setFlyingMode(boolean value) {
		this.isFlying = value;
	}

	/**
	 * @return true if player is in Flying mode
	 */
	public boolean isInFlyingMode() {
		return this.isFlying;
	}

	/**
	 * Stone Use Order determined by highest inventory slot. :( If player has two types, wrong one might be used.
	 *
	 * @param player
	 * @return selfRezItem
	 */
	public Item getSelfRezStone() {
		Item item = null;
		item = getReviveStone(161001001);
		item = getReviveStone(161001004);
		item = getReviveStone(161001005);
		if (item == null) {
			item = getReviveStone(161000003); // Reviving Elemental Stone
		}
		if (item == null) {
			item = getReviveStone(161000004); // Tombstone Of Revival
		}
		if (item == null) {
			item = getReviveStone(161000005); // Reviving Elemental Stone
		}
		return item;
	}

	/**
	 * @param stoneId
	 * @return stoneItem or null
	 */
	private Item getReviveStone(int stoneId) {
		Item item = getInventory().getFirstItemByItemId(stoneId);
		if (item != null && isItemUseDisabled(item.getItemTemplate().getUseLimits())) {
			item = null;
		}
		return item;
	}

	/**
	 * Need to find how an item is determined as able to self-rez.
	 *
	 * @return boolean can self rez with item
	 */
	public boolean haveSelfRezItem() {
		return (getSelfRezStone() != null);
	}

	/**
	 * Rebirth Effect is id 160.
	 *
	 * @return
	 */
	public boolean haveSelfRezEffect() {
		if (getAccessLevel() >= AdminConfig.ADMIN_AUTO_RES) {
			return true;
		}

		// Store the effect info.
		List<Effect> effects = getEffectController().getAbnormalEffects();
		for (Effect effect : effects) {
			for (EffectTemplate template : effect.getEffectTemplates()) {
				if (template.getEffectid() == 160 && template instanceof RebirthEffect) {
					RebirthEffect rebirthEffect = (RebirthEffect) template;
					setRebirthResurrectPercent(rebirthEffect.getResurrectPercent());
					setRebirthSkill(rebirthEffect.getSkillId());
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasResurrectBase() {
		List<Effect> effects = getEffectController().getAbnormalEffects();
		for (Effect effect : effects) {
			for (EffectTemplate template : effect.getEffectTemplates()) {
				if (template.getEffectid() == 160 && template instanceof ResurrectBaseEffect) {
					return true;
				}
			}
		}
		return false;
	}

	public void unsetResPosState() {
		if (isInResPostState()) {
			setResPosState(false);
			setResPosX(0);
			setResPosY(0);
			setResPosZ(0);
		}
	}

	public LootGroupRules getLootGroupRules() {
		if (isInGroup2()) {
			return getPlayerGroup2().getLootGroupRules();
		}
		if (isInAlliance2()) {
			return getPlayerAlliance2().getLootGroupRules();
		}
		return null;
	}

	public boolean isLooting() {
		return lootingNpcOid != 0;
	}

	public void setLootingNpcOid(int lootingNpcOid) {
		this.lootingNpcOid = lootingNpcOid;
	}

	public int getLootingNpcOid() {
		return lootingNpcOid;
	}

	public final boolean isMentor() {
		return isMentor;
	}

	public final void setMentor(boolean isMentor) {
		this.isMentor = isMentor;
	}

	@Override
	public Race getRace() {
		return playerCommonData.getRace();
	}

	public Player findPartner() {
		return World.getInstance().findPlayer(partnerId);

	}

	private PlayerVarsDAO daoVars = DAOManager.getDAO(PlayerVarsDAO.class);
	private Map<String, Object> vars = FastMap.newInstance();

	public boolean hasVar(String key) {
		return vars.containsKey(key);
	}

	public void setVar(String key, Object value, boolean sql) {
		vars.put(key, value);
		if (sql) {
			daoVars.set(this.getObjectId(), key, value);
		}
	}

	public Object getVar(String key) {
		return this.vars.get(key);
	}

	public int getVarInt(String key) {
		Object o = this.vars.get(key);
		if (o != null) {
			return Integer.parseInt(o.toString());
		}
		return 0;
	}

	public String getVarStr(String key) {
		Object o = this.vars.get(key);
		if (o != null) {
			return o.toString();
		}
		return null;
	}

	public void setVars(Map<String, Object> map) {
		this.vars = map;
	}

	public boolean isMarried() {
		return partnerId != 0;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	@Override
	public int getSkillCooldown(SkillTemplate template) {
		return isCoolDownZero() ? 0 : template.getCooldown();
	}

	@Override
	public int getItemCooldown(ItemTemplate template) {
		return isCoolDownZero() ? 0 : template.getUseLimits().getDelayTime();
	}

	public void setLastMessageTime() {
		if ((System.currentTimeMillis() - lastMsgTime) / 1000 < SecurityConfig.FLOOD_DELAY) {
			floodMsgCount++;
		}
		else {
			floodMsgCount = 0;
		}
		lastMsgTime = System.currentTimeMillis();
	}

	public int floodMsgCount() {
		return floodMsgCount;
	}

	public void setOnlineTime() {
		onlineTime = System.currentTimeMillis();
	}

	/*
	 * return online time in sec
	 */
	public long getOnlineTime() {
		return (System.currentTimeMillis() - onlineTime) / 1000;
	}

	/**
	 * Set Player command Used
	 */
	public void setCommandUsed(boolean value) {
		this.isCommandUsed = value;
	}

	/**
	 * @return true if player has used command
	 */
	public boolean isCommandInUse() {
		return this.isCommandUsed;
	}

	public void setRebirthRevive(boolean result) {
		rebirthRevive = result;
	}

	public boolean canUseRebirthRevive() {
		return rebirthRevive;
	}

	/**
	 * Put up supplements to subtraction queue, so that when moving they would not decrease, need update as confirmation To update use updateSupplements()
	 */
	public void subtractSupplements(int count, int supplementId) {
		subtractedSupplementsCount = count;
		subtractedSupplementId = supplementId;
	}

	/**
	 * Update supplements in queue and clear the queue
	 */
	public void updateSupplements() {
		if (subtractedSupplementId == 0 || subtractedSupplementsCount == 0) {
			return;
		}
		this.getInventory().decreaseByItemId(subtractedSupplementId, subtractedSupplementsCount);
		subtractedSupplementsCount = 0;
		subtractedSupplementId = 0;
	}

	public int getPortAnimation() {
		return portAnimation;
	}

	public void setPortAnimation(int portAnimation) {
		this.portAnimation = portAnimation;
	}

	@Override
	public boolean isSkillDisabled(SkillTemplate template) {
		ChainCondition cond = template.getChainCondition();
		if (cond != null && cond.getSelfCount() > 0) {// exception for multicast
			int chainCount = this.getChainSkills().getChainCount(this, template, cond.getCategory());
			if (chainCount > 0 && chainCount < cond.getSelfCount() && this.getChainSkills().chainSkillEnabled(cond.getCategory(), cond.getTime())) {
				return false;
			}
		}
		return super.isSkillDisabled(template);
	}

	/**
	 * @param itemId
	 * @param thisCount
	 */
	public void addItemMaxCountOfDay(int itemId, int thisCount) {
		if (maxCountEvent == null) {
			maxCountEvent = new FastMap<Integer, MaxCountOfDay>().shared();
		}

		if (maxCountEvent.get(itemId) != null) {
			maxCountEvent.get(itemId).setThisCount(thisCount);
		}
		else {
			maxCountEvent.put(itemId, new MaxCountOfDay(thisCount));
		}
	}

	/**
	 * @param itemId
	 * @return maxCountEvent.get(itemId).getThisCount()
	 */
	public int getItemMaxThisCount(int itemId) {
		if (maxCountEvent == null || !maxCountEvent.containsKey(itemId)) {
			return 0;
		}
		return maxCountEvent.get(itemId).getThisCount();
	}

	/**
	 * @param itemId
	 */
	public void removeItemMaxThisCount(int itemId) {
		if (maxCountEvent == null) {
			return;
		}
		maxCountEvent.remove(itemId);
	}

	public void clearItemMaxThisCount() {
		if (maxCountEvent == null) {
			return;
		}
		maxCountEvent.clear();
	}

	/**
	 * @return the maxCountEvent
	 */
	public Map<Integer, MaxCountOfDay> getItemMaxThisCounts() {
		return maxCountEvent;
	}

	/**
	 * @return the houses
	 */
	public List<House> getHouses() {
		if (houses == null) {
			List<House> found = HousingService.getInstance().searchPlayerHouses(this.getObjectId());
			if (found.size() > 0) {
				houses = found;
			}
			else {
				return found;
			}
		}
		return houses;
	}

	public void resetHouses() {
		if (houses != null) {
			houses.clear();
			houses = null;
		}
	}

	public House getActiveHouse() {
		for (House house : getHouses()) {
			if (house.getStatus() == HouseStatus.ACTIVE || house.getStatus() == HouseStatus.SELL_WAIT) {
				return house;
			}
		}

		return null;
	}

	public int getHouseOwnerId() {
		House house = getActiveHouse();
		if (house != null) {
			return house.getAddress().getId();
		}

		return 0;
	}

	public HouseRegistry getHouseRegistry() {
		return houseRegistry;
	}

	public void setHouseRegistry(HouseRegistry houseRegistry) {
		this.houseRegistry = houseRegistry;
	}

	public byte getBuildingOwnerStates() {
		return buildingOwnerStates;
	}

	public boolean isBuildingInState(PlayerHouseOwnerFlags state) {
		return (buildingOwnerStates & state.getId()) != 0;
	}

	public void setBuildingOwnerState(byte state) {
		buildingOwnerStates |= state;
		House house = getActiveHouse();
		if (house != null) {
			house.fixBuildingStates();
		}
	}

	public void unsetBuildingOwnerState(byte state) {
		buildingOwnerStates &= ~state;
		House house = getActiveHouse();
		if (house != null) {
			house.fixBuildingStates();
		}
	}

	public float[] getBattleReturnCoords() {
		return this.battleReturnCoords;
	}

	public void setBattleReturnCoords(int mapId, float[] coords) {
		this.battleReturnMap = mapId;
		this.battleReturnCoords = coords;
	}

	public int getBattleReturnMap() {
		return battleReturnMap;
	}

	public boolean isInSprintMode() {
		return isInSprintMode;
	}

	public void setSprintMode(boolean isInSprintMode) {
		this.isInSprintMode = isInSprintMode;
	}

	public void setRideObservers(ActionObserver observer) {
		if (rideObservers == null) {
			rideObservers = new ArrayList<ActionObserver>(3);
		}

		rideObservers.add(observer);
	}

	public List<ActionObserver> getRideObservers() {
		return rideObservers;
	}

	public boolean isGmMode() {
		return isGmMode;
	}

	public void setGmMode(boolean isGmMode) {
		this.isGmMode = isGmMode;
	}

	public boolean isAFKMode() {
		return afkmode;
	}

	public void setAFKMode(boolean afkmode) {
		this.afkmode = afkmode;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public long getOnlineBonusTime() {
		return this.onlineBonusTime;
	}

	public void setOnlineBonusTime(long value) {
		this.onlineBonusTime = value;
	}

	public boolean getStatus() {
		return addedStatus;
	}

	public void addStatus(boolean addedStatus) {
		this.addedStatus = addedStatus;
	}

	public boolean banFromWorld(String by, String reason, long duration) {
		if (isBannedFromWorld()) {
			return false;
		}
		else {
			bannedFromWorld = true;
			bannedFromWorldDate = Calendar.getInstance().getTime();
			bannedFromWorldDuring = duration;
			bannedFromWorldBy = by;
			bannedFromWorldReason = reason;
			PlayerWorldBanDAO dao = DAOManager.getDAO(PlayerWorldBanDAO.class);
			if (!dao.addWorldBan(getObjectId(), by, bannedFromWorldDuring, bannedFromWorldDate, bannedFromWorldReason)) {
				return false;
			}

			if (bannedFromWorldDuring > 0) {
				scheduleUnbanFromWorld();
			}
		}
		return true;
	}

	public static String getChanCommand(int chanId) {
		switch (chanId) {
			case CHAT_FIXED_ON_ASMOS:
				return "." + "asmo";
			case CHAT_FIXED_ON_ELYOS:
				return "." + "ely";
			case CHAT_FIXED_ON_WORLD:
				return "." + "tg";
			case CHAT_FIXED_ON_BOTH:
				return "." + "2race";
		}
		return "";
	}

	public boolean isBannedFromWorld() {
		return bannedFromWorld;
	}

	public boolean unbanFromWorld() {
		bannedFromWorld = false;
		PlayerWorldBanDAO dao = DAOManager.getDAO(PlayerWorldBanDAO.class);
		cancelUnbanFromWorld();
		dao.removeWorldBan(getObjectId());
		return true;
	}

	public void scheduleUnbanFromWorld() {
		if (!isBannedFromWorld()) {
			throw new RuntimeException("scheduling unban task when not banned from " + getChanCommand(CHAT_FIX_WORLD_CHANNEL));
		}
		cancelUnbanFromWorld();
		final int playerObjId = getObjectId();
		final String playerName = getName();
		final String adminName = bannedFromWorldBy;
		final long time = bannedFromWorldDuring;
		if (time > 0) {
			final Date endDate = new Date(bannedFromWorldDate.getTime() + bannedFromWorldDuring);
			taskToUnbanFromWorld = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					World world = World.getInstance();
					Player player = world.findPlayer(playerName);
					Player admin = world.findPlayer(adminName);
					if (endDate.getTime() <= Calendar.getInstance().getTimeInMillis()) {
						DAOManager.getDAO(PlayerWorldBanDAO.class).removeWorldBan(playerObjId);
					}
					if (player != null) {
						player.bannedFromWorld = false;
						PacketSendUtility.sendBrightYellowMessageOnCenter(player, "You're not anymore banned from chat channels");
					}
					if (admin != null) {
						PacketSendUtility.sendBrightYellowMessageOnCenter(admin, "The player " + playerName + " is no more banned from chat channels");
					}
				}
			}, time);
		}
	}

	private void cancelUnbanFromWorld() {
		if (taskToUnbanFromWorld != null) {
			taskToUnbanFromWorld.cancel(false);
			taskToUnbanFromWorld = null;
		}
	}

	public String getBannedFromWorldBy() {
		return bannedFromWorldBy;
	}

	public String getBannedFromWorldReason() {
		return bannedFromWorldReason;
	}

	public String getBannedFromWorldRemainingTime() {
		long elapsed = 0;
		if (bannedFromWorldDuring == 0) {
			return "indetermin?";
		}
		else {
			elapsed = bannedFromWorldDuring - (Calendar.getInstance().getTimeInMillis() - bannedFromWorldDate.getTime());
			return HumanTime.approximately(elapsed - (elapsed % 1000));
		}
	}

	public void setBannedFromWorld(String by, String reason, long duration, Date date) {
		bannedFromWorld = true;
		bannedFromWorldBy = by;
		bannedFromWorldDate = date;
		bannedFromWorldDuring = duration;
		bannedFromWorldReason = reason;
	}

	public void increaseWordBanTime() {
		this.wordBanTime += wordBanTime;
	}

	public int getWordBanTime() {
		return this.wordBanTime;
	}

	public int getUseAutoGroup() {
		return useAutoGroup;
	}

	public void setUseAutoGroup(int useAutoGroup) {
		this.useAutoGroup = useAutoGroup;
	}

	public boolean isUseRobot() {
		return robot;
	}

	public void setUseRobot(boolean robot) {
		this.robot = robot;
	}

	public int getRobotId() {
		return robotId;
	}

	public void setRobotId(int robotId) {
		this.robotId = robotId;
	}

	public WorldPosition getPrevPos() {
		if (getPosition() == null || !getPosition().isSpawned()) {
			return null;
		}
		if (prevPos == null || prevPos.getMapId() != getPosition().getMapId()) {
			prevPos = new WorldPosition(getPosition().getMapId());
			prevPos.setXYZH(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getPosition().getHeading());
		}
		return prevPos;
	}

	@Override
	public WorldPosition getPosition() {
		return playerCommonData.getPosition();
	}

	@Override
	public void setPosition(WorldPosition position) {
		playerCommonData.setPosition(position);
	}

	public AbsoluteStatOwner getAbsoluteStats() {
		return absStatsHolder;
	}

	public boolean isOnFastTrack() {
		return isOnFastTrack;
	}

	public void setOnFastTrack(boolean isOnFastTrack) {
		this.isOnFastTrack = isOnFastTrack;
	}

	public boolean isInLiveParty() {
		return isInLiveParty;
	}

	public void setInLiveParty(boolean isInLiveParty) {
		this.isInLiveParty = isInLiveParty;
	}
	/*
	 * public int getLinkedSkill() { return linkedSkill; } public void setLinkedSkill(int skillId) { this.linkedSkill = skillId; }
	 */

	public FastList<WorldBuff> getWorldBuffList() {
		return worldBuff;
	}

	public void addWorldBuff(WorldBuff buff) {
		if (worldBuff == null) {
			worldBuff = FastList.newInstance();
		}
		worldBuff.add(buff);
	}

	public void clearJoinRequest() {
		playerCommonData.setJoinRequestLegionId(0);
		playerCommonData.setJoinRequestState(LegionJoinRequestState.NONE);
		DAOManager.getDAO(PlayerDAO.class).clearJoinRequest(getObjectId());
	}

	public PlayerUpgradeArcade getUpgradeArcade() {
		return playerCommonData.getUpgradeArcade();
	}

	public PlayerConquererProtectorData getConquerorProtectorData() {
		if (conquerorProtectorData == null)
			this.conquerorProtectorData = new PlayerConquererProtectorData();

		return conquerorProtectorData;
	}

	public void setConquerorDefenderData(PlayerConquererProtectorData conquerorDefenderData) {
		this.conquerorProtectorData = conquerorDefenderData;
	}

	public boolean hasAbyssBonus() {
		return hasAbyssBonus;
	}

	public void setAbyssBonus(boolean hasAbyssBonus) {
		this.hasAbyssBonus = hasAbyssBonus;
	}

	public int getAbyssId() {
		return abyssId;
	}

	public void setAbyssId(int id) {
		abyssId = id;
	}

	/**
	 * 4.7 Buff System
	 */
	public void setBonus(boolean hasBonus) {
		this.hasBonus = hasBonus;
	}

	public int getBonusId() {
		return bonusId;
	}

	public void setBonusId(int id) {
		bonusId = id;
	}

	public int getPlayersBonusId() {
		return playersBonusId;
	}

	public void setPlayersBonusId(int id) {
		playersBonusId = id;
	}

	/**
	 * @return the New User Bonus Time
	 */
	public void setNew(boolean b) {
		this.newPlayer = b;
	}

	public boolean isNewPlayer() {
		return newPlayer;
	}

	public PlayerBonusTime getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(PlayerBonusTime bonusTime) {
		this.bonusTime = bonusTime;
	}

	public long getCreationDate() {
		Timestamp creationDate = playerCommonData.getCreationDate();
		if (creationDate == null) {
			return 0;
		}

		return creationDate.getTime();
	}

	public void setCreationDataDay(long i) {
		this.creationDay = i;
	}

	public long getCreationDataDay() {
		return creationDay;
	}

	public void setBonusTimeStatus() {
//		Timestamp tm = getClientConnection().getAccount().getPlayerAccountData(getObjectId()).getPlayerCommonData().getLastOnline();
//		long lastOnlineTimeDay = (System.currentTimeMillis() - tm.getTime()) / 24 / 60 / 60 / 1000;
		long t = (System.currentTimeMillis() - getCommonData().getCreationDate().getTime()) / 24 / 60 / 60 / 1000;
		long bonus_time = getBonusTime().getTime() != null ? System.currentTimeMillis() - getBonusTime().getTime().getTime() : 0;
		boolean bonus_comeback = System.currentTimeMillis() < bonus_time;
		setCreationDataDay(t);
		if (t <= 3L) {
			setNew(true);
		}
		else {
			setNew(false);
		}
		if (getBonusTime().getStatus() == PlayerBonusTimeStatus.RETURN && bonus_comeback || getBonusTime().getStatus() == PlayerBonusTimeStatus.NEW && t <= 30) {
			return;
		}

		if (t <= 30L) {
			getBonusTime().setStatus(PlayerBonusTimeStatus.NEW);
		}
		else {
			getBonusTime().setStatus(PlayerBonusTimeStatus.NORMAL);
		}

		if (getClientConnection().getAccount().getIsReturn() == 1) {
			getBonusTime().setStatus(PlayerBonusTimeStatus.RETURN);
			getBonusTime().setTime(new Timestamp(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
		}
	}

	/**
	 * Membership of this player
	 * 
	 * @return
	 */
	public byte getMembership() {
		if (playerAccount == null) {
			return 0x00;
		}
		return playerAccount.getMembership();
	}

	/**
	 * F2p
	 */
	public F2p getF2p() {
		return f2p;
	}

	public void setF2p(F2p f2p) {
		this.f2p = f2p;
	}

	/**
	 * Hotspot Teleport
	 */
	public void setHotTeleObservers(ActionObserver observer) {
		if (hotTeleObservers == null) {
			hotTeleObservers = new ArrayList<ActionObserver>(3);
		}
		hotTeleObservers.add(observer);
	}

	public List<ActionObserver> getHotTeleObservers() {
		return hotTeleObservers;
	}

	/**
	 * Transformation
	 */
	public int getTransformedModelId() {
		return transformModelId;
	}

	public void setTransformedModelId(int id) {
		transformModelId = id;
	}

	public int getTransformedItemId() {
		return transformItemId;
	}

	public void setTransformedItemId(int id) {
		transformItemId = id;
	}

	public int getTransformedPanelId() {
		return transformPanelId;
	}

	public void setTransformedPanelId(int id) {
		transformPanelId = id;
	}

	/**
	 * Luna System
	 */
	public PlayerWardrobeList getWardrobe() {
		return wardrobe;
	}

	public void setWardrobe(PlayerWardrobeList wardrobe) {
		this.wardrobe = wardrobe;
	}

	public void setPlayerLunaShop(PlayerLunaShop pls) {
		this.lunaShop = pls;
	}

	public PlayerLunaShop getPlayerLunaShop() {
		return lunaShop;
	}

	public void setLunaConsumePoint(int point) {
		this.playerCommonData.setLunaConsumePoint(point);
	}

	public int getLunaConsumePoint() {
		return this.playerCommonData.getLunaConsumePoint();
	}

	public PlayerSweep getPlayerShugoSweep() { return shugoSweep; }

	public void setPlayerShugoSweep(PlayerSweep ps) { this.shugoSweep = ps; }

	public void setMuniKeys(int keys) {
		this.playerCommonData.setMuniKeys(keys);
	}

	public int getMuniKeys() {
		return this.playerCommonData.getMuniKeys();
	}

	public void setLunaConsumeCount(int count) {
		this.playerCommonData.setLunaConsumeCount(count);
	}

	public int getLunaConsumeCount() {
		return this.playerCommonData.getLunaConsumeCount();
	}

	public void setLunaAccount(long luna) {
		if (LoginServer.getInstance().sendPacket(new SM_ACCOUNT_TOLL_INFO(1, this.getClientConnection().getAccount().getToll(), luna, this.getAcountName()))) {
			this.getClientConnection().getAccount().setLuna(luna);
		}
		else
			PacketSendUtility.sendMessage(this, "ls communication error.");
	}

	public long getLunaAccount() {
		return this.getClientConnection().getAccount().getLuna();
	}

	public void setWardrobeSlot(int slot) {
		this.playerCommonData.setWardrobeSlot(slot);
	}

	public int getWardrobeSlot() {
		return this.playerCommonData.getWardrobeSlot();
	}

	/**
	 * Tower of Challenge
	 */
    public void setFloor(final int floor) {
        this.getCommonData().setFloor(floor);
    }
    
    public int getFloor() {
        return this.getCommonData().getFloor();
    }

	public int getMinionSkillPoints() {
		return this.getCommonData().getMinionSkillPoints();
	}
	
	public void setMinionSkillPoints(int minionSkillPoints) {
		this.getCommonData().setMinionSkillPoints(minionSkillPoints);
	}
	
	/**
	 * Luna Dice Game
	 */
	public int getLunaDiceGame() {
		return this.LunaDiceGame;
	}
	
	public void setLunaDiceGame(int dice, boolean reset) {
		if (!reset) {
			if (dice > this.LunaDiceGame) {
				this.LunaDiceGame = dice;
			} else {
				return;
			}
		} else {
			this.LunaDiceGame = dice;
		}
	}
	
	public int getLunaDiceGameTry() {
		return this.LunaDiceGameTry;
	}
	
	public void setLunaDiceGameTry(int dice) {
		this.LunaDiceGameTry = dice;
	}

	/**
	 * Equipment Setting
	 */
    public EquipmentSettingList getEquipmentSettingList() {
        return equipmentSettingList;
    }
    
    public void setEquipmentSettingList(EquipmentSettingList equipmentSettingList) {
        this.equipmentSettingList = equipmentSettingList;
    }

	/**
	 * Skill Skin List
	 */
	public SkillSkinList getSkillSkinList() {
		return skillSkinList;
	}

	public void setSkillSkinList(SkillSkinList skillSkinList) {
		this.skillSkinList = skillSkinList;
		skillSkinList.setOwner(this);
	}
	
	/**
	 * Add Tag Wedding
	 */
	public int getPartnerId() { 
		return this.partnerId;
	}

	/**
	 * Cubics System
	 */
	public PlayerMCList getMonsterCubic() {
		return this.mc;
	}

	public void setMonsterCubic(PlayerMCList playerABList) {
		this.mc = playerABList;
	}

	/**
	 * Player Ranking System
	 */
	private ArenaOfDisciplineRank disciplineRank;
	private ArenaOfCooperationRank cooperationRank;

	public ArenaOfDisciplineRank getDisciplineRank() {
		return disciplineRank;
	}

	public void setDisciplineRank(ArenaOfDisciplineRank dr) {
		this.disciplineRank = dr;
	}

	public ArenaOfCooperationRank getCooperationRank() {
		return cooperationRank;
	}

	public void setCooperationRank(ArenaOfCooperationRank cr) {
		this.cooperationRank = cr;
	}	
}