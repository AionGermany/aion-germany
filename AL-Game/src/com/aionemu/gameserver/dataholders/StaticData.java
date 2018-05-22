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
package com.aionemu.gameserver.dataholders;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.mail.Mails;

/**
 * An instance of this class is the result of data loading.
 *
 * @author Luno, orz Modified by Wakizashi
 */
@XmlRootElement(name = "ae_static_data")
@XmlAccessorType(XmlAccessType.NONE)
public class StaticData {

	@XmlElement(name = "npc_drops")
	public XmlNpcDropData xmlNpcDropData;
	@XmlElement(name = "world_maps")
	public WorldMapsData worldMapsData;
	@XmlElement(name = "weather")
	public MapWeatherData mapWeatherData;
	@XmlElement(name = "npc_trade_list")
	public TradeListData tradeListData;
	@XmlElement(name = "npc_teleporter")
	public TeleporterData teleporterData;
	@XmlElement(name = "teleport_location")
	public TeleLocationData teleLocationData;
	@XmlElement(name = "bind_points")
	public BindPointData bindPointData;
	@XmlElement(name = "quests")
	public QuestsData questData;
	@XmlElement(name = "quest_scripts")
	public XMLQuests questsScriptData;
	@XmlElement(name = "player_experience_table")
	public PlayerExperienceTable playerExperienceTable;
	@XmlElement(name = "player_stats_templates")
	public PlayerStatsData playerStatsData;
	@XmlElement(name = "summon_stats_templates")
	public SummonStatsData summonStatsData;
	@XmlElement(name = "item_templates")
	public ItemData itemData;
	@XmlElement(name = "item_presettings")
	public ItemPresettingData itemPresettingData;
	@XmlElement(name = "random_bonuses")
	public ItemRandomBonusData itemRandomBonuses;
	@XmlElement(name = "npc_templates")
	public NpcData npcData;
	@XmlElement(name = "npc_shouts")
	public NpcShoutData npcShoutData;
	@XmlElement(name = "player_initial_data")
	public PlayerInitialData playerInitialData;
	@XmlElement(name = "skill_data")
	public SkillData skillData;
	@XmlElement(name = "motion_times")
	public MotionData motionData;
	@XmlElement(name = "skill_tree")
	public SkillTreeData skillTreeData;
	@XmlElement(name = "warehouse_expander")
	public WarehouseExpandData warehouseExpandData;
	@XmlElement(name = "player_titles")
	public TitleData titleData;
	@XmlElement(name = "gatherable_templates")
	public GatherableData gatherableData;
	@XmlElement(name = "npc_walker")
	public WalkerData walkerData;
	@XmlElement(name = "zones")
	public ZoneData zoneData;
	@XmlElement(name = "goodslists")
	public GoodsListData goodsListData;
	@XmlElement(name = "tribe_relations")
	public TribeRelationsData tribeRelationsData;
	@XmlElement(name = "recipe_templates")
	public RecipeData recipeData;
	@XmlElement(name = "luna_templates")
	public LunaData lunaData;
	@XmlElement(name = "chest_templates")
	public ChestData chestData;
	@XmlElement(name = "staticdoor_templates")
	public StaticDoorData staticDoorData;
	@XmlElement(name = "item_sets")
	public ItemSetData itemSetData;
	@XmlElement(name = "npc_factions")
	public NpcFactionsData npcFactionsData;
	@XmlElement(name = "npc_skill_templates")
	public NpcSkillData npcSkillData;
	@XmlElement(name = "pet_skill_templates")
	public PetSkillData petSkillData;
	@XmlElement(name = "siege_locations")
	public SiegeLocationData siegeLocationData;
	@XmlElement(name = "dimensional_vortex")
	public VortexData vortexData;
	@XmlElement(name = "rift_locations")
	public RiftData riftData;
	@XmlElement(name = "fly_rings")
	public FlyRingData flyRingData;
	@XmlElement(name = "shields")
	public ShieldData shieldData;
	@XmlElement(name = "pets")
	public PetData petData;
	@XmlElement(name = "pet_feed")
	public PetFeedData petFeedData;
	@XmlElement(name = "dopings")
	public PetDopingData petDopingData;
	@XmlElement(name = "guides")
	public GuideHtmlData guideData;
	@XmlElement(name = "roads")
	public RoadData roadData;
	@XmlElement(name = "instance_cooltimes")
	public InstanceCooltimeData instanceCooltimeData;
	@XmlElement(name = "decomposable_items")
	public DecomposableItemsData decomposableItemsData;
	@XmlElement(name = "ai_templates")
	public AIData aiData;
	@XmlElement(name = "flypath_template")
	public FlyPathData flyPath;
	@XmlElement(name = "windstreams")
	public WindstreamData windstreamsData;
	@XmlElement(name = "item_restriction_cleanups")
	public ItemRestrictionCleanupData itemCleanup;
	@XmlElement(name = "assembled_npcs")
	public AssembledNpcsData assembledNpcData;
	@XmlElement(name = "cosmetic_items")
	public CosmeticItemsData cosmeticItemsData;
	@XmlElement(name = "auto_groups")
	public AutoGroupData autoGroupData;
	@XmlElement(name = "events_config")
	public EventData eventData;
	@XmlElement(name = "spawns")
	public SpawnsData2 spawnsData2;
	@XmlElement(name = "item_groups")
	public ItemGroupsData itemGroupsData;
	@XmlElement(name = "polymorph_panels")
	public PanelSkillsData panelSkillsData;
	@XmlElement(name = "instance_bonusattrs")
	public InstanceBuffData instanceBuffData;
	@XmlElement(name = "housing_objects")
	public HousingObjectData housingObjectData;
	@XmlElement(name = "rides")
	public RideData rideData;
	@XmlElement(name = "instance_exits")
	public InstanceExitData instanceExitData;
	@XmlElement(name = "portal_locs")
	PortalLocData portalLocData;
	@XmlElement(name = "portal_templates2")
	Portal2Data portalTemplate2;
	@XmlElement(name = "portals_conquest")
	public ConquestPortalData conquestPortalData;
	@XmlElement(name = "house_lands")
	public HouseData houseData;
	@XmlElement(name = "buildings")
	public HouseBuildingData houseBuildingData;
	@XmlElement(name = "house_parts")
	public HousePartsData housePartsData;
	@XmlElement(name = "house_npcs")
	public HouseNpcsData houseNpcsData;
	@XmlElement(name = "assembly_items")
	public AssemblyItemsData assemblyItemData;
	@XmlElement(name = "lboxes")
	public HouseScriptData houseScriptData;
	@XmlElement(name = "mails")
	public Mails systemMailTemplates;
	@XmlElement(name = "material_templates")
	public MaterialData materiaData;
	@XmlElement(name = "challenge_tasks")
	public ChallengeData challengeData;
	@XmlElement(name = "town_spawns_data")
	public TownSpawnsData townSpawnsData;
	@XmlElement(name = "skill_charge")
	public SkillChargeData skillChargeData;
	@XmlElement(name = "item_purifications")
	public ItemPurificationData itemPurificationData;
	@XmlElement(name = "decomposable_selectitems")
	public DecomposableSelectItemsData decomposableSelectItemsData;
	@XmlElement(name = "enchant_templates")
	public ItemEnchantData itemEnchantData;
	@XmlElement(name = "enchant_tables")
	public ItemEnchantTableData itemEnchantTableData;
	@XmlElement(name = "item_multi_returns")
	public MultiReturnItemData multiReturnItemData;
	@XmlElement(name = "hotspot_teleport")
	public HotspotTeleporterData hotspotTeleporterData;
	@XmlElement(name = "atreian_passports")
	public AtreianPassportData atreianPassportData;
	@XmlElement(name = "base_locations")
	public BaseData baseData;
	@XmlElement(name = "beritra_invasion")
	public BeritraData beritraData;
	@XmlElement(name = "svs")
	public SvsData svsData;
	@XmlElement(name = "rvr")
	public RvrData rvrData;
	@XmlElement(name = "abyss_bonusattrs")
	public AbyssBuffData abyssBuffData;
	@XmlElement(name = "abyss_groupattrs")
	public AbyssGroupData abyssGroupData;
	@XmlElement(name = "absolute_stats")
	public AbsoluteStatsData absoluteStatsData;
	@XmlElement(name = "walker_versions")
	public WalkerVersionsData walkerVersionsData;
	@XmlElement(name = "robots")
	public RobotData robotData;
	@XmlElement(name = "arcadelist")
	public ArcadeUpgradeData arcadeUpgradeData;
	@XmlElement(name = "hidden_stigma_tree")
	public HiddenStigmaTreeData hiddenStigmas;
	@XmlElement(name = "world_buffs")
	public WorldBuffData buffData;
	@XmlElement(name = "players")
	public PlayersAppearanceData playerAppearance;
	@XmlElement(name = "service_bonusattrs")
	public ServiceBuffData serviceBuffData;
	@XmlElement(name = "players_service_bonusattrs")
	public PlayersBonusData playersBonusData;
	@XmlElement(name = "pet_bonusattrs")
	public PetBuffData petBuffData;
	@XmlElement(name = "merchants")
	public PetMerchandData petMerchandData;
	@XmlElement(name = "panel_cps")
	public PanelCpData panelCpData;
	@XmlElement(name = "f2p_bonus")
	public F2PBonusData f2pBonus;
	@XmlElement(name = "abyss_ops")
	public AbyssOpData abyssOpData;
	@XmlElement(name = "landing")
	public LandingData landingLocationData;
	@XmlElement(name = "landing_special")
	public LandingSpecialData landingSpecialLocationData;
	@XmlElement(name = "luna_consume_rewards")
	public LunaConsumeRewardsData lunaConsumeRewardsData;
	@XmlElement(name = "minions")
	public MinionData minionData;
    @XmlElement(name = "monster_books")
    public MonsterbookData monsterbookData;
	@XmlElement(name = "boost_events")
	public BoostEventData boostEvents;

	// JAXB callback
	@SuppressWarnings("unused")
	private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		DataManager.log.info("[DataManager] Loaded " + worldMapsData.size() + " world maps");
		DataManager.log.info("[DataManager] Loaded " + materiaData.size() + " material ids");
		DataManager.log.info("[DataManager] Loaded " + mapWeatherData.size() + " weather maps");
		DataManager.log.info("[DataManager] Loaded " + playerExperienceTable.getMaxLevel() + " levels");
		DataManager.log.info("[DataManager] Loaded " + playerStatsData.size() + " player stat templates");
		DataManager.log.info("[DataManager] Loaded " + summonStatsData.size() + " summon stat templates");
		DataManager.log.info("[DataManager] Loaded " + itemCleanup.size() + " item cleanup entries");
		DataManager.log.info("[DataManager] Loaded " + itemData.size() + " item templates");
		DataManager.log.info("[DataManager] Loaded " + itemRandomBonuses.size() + " item bonus templates");
		DataManager.log.info("[DataManager] Loaded " + itemGroupsData.bonusSize() + " bonus item group templates");
		DataManager.log.info("[DataManager] Loaded " + itemGroupsData.petFoodSize() + " pet food items");
		DataManager.log.info("[DataManager] Loaded " + npcData.size() + " npc templates");
		DataManager.log.info("[DataManager] Loaded " + systemMailTemplates.size() + " system mail templates");
		DataManager.log.info("[DataManager] Loaded " + npcShoutData.size() + " npc shout templates");
		DataManager.log.info("[DataManager] Loaded " + petData.size() + " pet templates");
		DataManager.log.info("[DataManager] Loaded " + petFeedData.size() + " food flavours");
		DataManager.log.info("[DataManager] Loaded " + petDopingData.size() + " pet doping templates");
		DataManager.log.info("[DataManager] Loaded " + playerInitialData.size() + " initial player templates");
		DataManager.log.info("[DataManager] Loaded " + tradeListData.size() + " trade lists");
		DataManager.log.info("[DataManager] Loaded " + teleporterData.size() + " npc teleporter templates");
		DataManager.log.info("[DataManager] Loaded " + teleLocationData.size() + " teleport locations");
		DataManager.log.info("[DataManager] Loaded " + skillData.size() + " skill templates");
		DataManager.log.info("[DataManager] Loaded " + motionData.size() + " motion times");
		DataManager.log.info("[DataManager] Loaded " + skillTreeData.size() + " skill learn entries");
		DataManager.log.info("[DataManager] Loaded " + warehouseExpandData.size() + " warehouse expand entries");
		DataManager.log.info("[DataManager] Loaded " + bindPointData.size() + " bind point entries");
		DataManager.log.info("[DataManager] Loaded " + questData.size() + " quest data entries");
		DataManager.log.info("[DataManager] Loaded " + gatherableData.size() + " gatherable entries");
		DataManager.log.info("[DataManager] Loaded " + titleData.size() + " title entries");
		DataManager.log.info("[DataManager] Loaded " + walkerData.size() + " walker routes");
		DataManager.log.info("[DataManager] Loaded " + walkerVersionsData.size() + " walker group variants");
		DataManager.log.info("[DataManager] Loaded " + zoneData.size() + " zone entries");
		DataManager.log.info("[DataManager] Loaded " + goodsListData.size() + " goodslist entries");
		DataManager.log.info("[DataManager] Loaded " + tribeRelationsData.size() + " tribe relation entries");
		DataManager.log.info("[DataManager] Loaded " + recipeData.size() + " recipe entries");
		DataManager.log.info("[DataManager] Loaded " + lunaData.size() + " luna recipe entries");
		DataManager.log.info("[DataManager] Loaded " + chestData.size() + " chest locations");
		DataManager.log.info("[DataManager] Loaded " + staticDoorData.size() + " static door locations");
		DataManager.log.info("[DataManager] Loaded " + itemSetData.size() + " item set entries");
		DataManager.log.info("[DataManager] Loaded " + npcFactionsData.size() + " npc factions");
		DataManager.log.info("[DataManager] Loaded " + npcSkillData.size() + " npc skill list entries");
		DataManager.log.info("[DataManager] Loaded " + petSkillData.size() + " pet skill list entries");
		DataManager.log.info("[DataManager] Loaded " + siegeLocationData.size() + " siege location entries");
		DataManager.log.info("[DataManager] Loaded " + vortexData.size() + " vortex entries");
		DataManager.log.info("[DataManager] Loaded " + riftData.size() + " rift entries");
		DataManager.log.info("[DataManager] Loaded " + flyRingData.size() + " fly ring entries");
		DataManager.log.info("[DataManager] Loaded " + shieldData.size() + " shield entries");
		DataManager.log.info("[DataManager] Loaded " + petData.size() + " pet entries");
		DataManager.log.info("[DataManager] Loaded " + guideData.size() + " guide entries");
		DataManager.log.info("[DataManager] Loaded " + roadData.size() + " road entries");
		DataManager.log.info("[DataManager] Loaded " + instanceCooltimeData.size() + " instance cooltime entries");
		DataManager.log.info("[DataManager] Loaded " + decomposableItemsData.size() + " decomposable items entries");
		DataManager.log.info("[DataManager] Loaded " + aiData.size() + " ai templates");
		DataManager.log.info("[DataManager] Loaded " + flyPath.size() + " flypath templates");
		DataManager.log.info("[DataManager] Loaded " + windstreamsData.size() + " windstream entries");
		DataManager.log.info("[DataManager] Loaded " + assembledNpcData.size() + " assembled npcs entries");
		DataManager.log.info("[DataManager] Loaded " + cosmeticItemsData.size() + " cosmetic items entries");
		DataManager.log.info("[DataManager] Loaded " + autoGroupData.size() + " auto group entries");
		DataManager.log.info("[DataManager] Loaded " + spawnsData2.size() + " spawn maps entries");
		DataManager.log.info("[DataManager] Loaded " + eventData.size() + " active events");
		DataManager.log.info("[DataManager] Loaded " + panelSkillsData.size() + " skill panel entries");
		DataManager.log.info("[DataManager] Loaded " + instanceBuffData.size() + " instance Buffs entries");
		DataManager.log.info("[DataManager] Loaded " + housingObjectData.size() + " housing object entries");
		DataManager.log.info("[DataManager] Loaded " + rideData.size() + " ride info entries");
		DataManager.log.info("[DataManager] Loaded " + instanceExitData.size() + " instance exit entries");
		DataManager.log.info("[DataManager] Loaded " + portalLocData.size() + " portal loc entries");
		DataManager.log.info("[DataManager] Loaded " + portalTemplate2.size() + " portal templates2 entries");
		DataManager.log.info("[DataManager] Loaded " + conquestPortalData.size() + " conquest portal entries");
		DataManager.log.info("[DataManager] Loaded " + houseData.size() + " housing lands");
		DataManager.log.info("[DataManager] Loaded " + houseBuildingData.size() + " house building styles");
		DataManager.log.info("[DataManager] Loaded " + housePartsData.size() + " house parts");
		DataManager.log.info("[DataManager] Loaded " + houseNpcsData.size() + " house spawns");
		DataManager.log.info("[DataManager] Loaded " + houseScriptData.size() + " house default scripts");
		DataManager.log.info("[DataManager] Loaded " + assemblyItemData.size() + " assembly items entries");
		DataManager.log.info("[DataManager] Loaded " + challengeData.size() + " challenge tasks entries");
		DataManager.log.info("[DataManager] Loaded " + townSpawnsData.getSpawnsCount() + " town spawns");
		DataManager.log.info("[DataManager] Loaded " + skillChargeData.size() + " skill charge entries");
		DataManager.log.info("[DataManager] Loaded " + itemPurificationData.size() + " item purifications entries");
		DataManager.log.info("[DataManager] Loaded " + decomposableSelectItemsData.size() + " decomposable select data");
		DataManager.log.info("[DataManager] Loaded " + itemEnchantData.size() + " item enchant data");
		DataManager.log.info("[DataManager] Loaded " + itemEnchantTableData.size() + " item enchant tables");
		DataManager.log.info("[DataManager] Loaded " + multiReturnItemData.size() + " multi returns items");
		DataManager.log.info("[DataManager] Loaded " + hotspotTeleporterData.size() + " hotspot templates");
		DataManager.log.info("[DataManager] Loaded " + arcadeUpgradeData.size() + " arcade upgrade entries");
		DataManager.log.info("[DataManager] Loaded " + atreianPassportData.size() + " atreian passports");
		DataManager.log.info("[DataManager] Loaded " + baseData.size() + " base entries");
		DataManager.log.info("[DataManager] Loaded " + beritraData.size() + " beritra invasion entries");
		DataManager.log.info("[DataManager] Loaded " + svsData.size() + " s.v.s entries");
		DataManager.log.info("[DataManager] Loaded " + rvrData.size() + " r.v.r entries");
		DataManager.log.info("[DataManager] Loaded " + abyssBuffData.size() + " abyss bonus entries");
		DataManager.log.info("[DataManager] Loaded " + abyssGroupData.size() + " abyss bonus group entries");
		DataManager.log.info("[DataManager] Loaded " + absoluteStatsData.size() + " absolute stat templates");
		DataManager.log.info("[DataManager] Loaded " + robotData.size() + " robot armor entries");
		DataManager.log.info("[DataManager] Loaded " + xmlNpcDropData.size() + " custom npc drop data");
		DataManager.log.info("[DataManager] Loaded " + hiddenStigmas.size() + " hidden stigmas entries");
		DataManager.log.info("[DataManager] Loaded " + buffData.size() + " world buff map entries");
		// DataManager.log.info("[DataManager] Loaded " + playerAppearance.size() + " Player Appearances");
		DataManager.log.info("[DataManager] Loaded " + serviceBuffData.size() + " service bonus entries");
		DataManager.log.info("[DataManager] Loaded " + playersBonusData.size() + " player bonus entries");
		DataManager.log.info("[DataManager] Loaded " + petBuffData.size() + " Pets Buff entries");
		DataManager.log.info("[DataManager] Loaded " + petMerchandData.size() + " Pets Merchant entries");
		DataManager.log.info("[DataManager] Loaded " + panelCpData.size() + " Creativity Template entries");
		DataManager.log.info("[DataManager] Loaded " + f2pBonus.size() + " F2P Bonus entries");
		DataManager.log.info("[DataManager] Loaded " + abyssOpData.size() + " Abyss Landing entries");
		DataManager.log.info("[DataManager] Loaded " + landingLocationData.size() + " Abyss Landing entries");
		DataManager.log.info("[DataManager] Loaded " + landingSpecialLocationData.size() + " Abyss Monument entries");
		DataManager.log.info("[DataManager] Loaded " + lunaConsumeRewardsData.size() + " Luna Consume entries");
		DataManager.log.info("[DataManager] Loaded " + minionData.size() + " Minion templates");
		DataManager.log.info("[DataManager] Loaded " + monsterbookData.size() + " Monsterbook templates");
		DataManager.log.info("[DataManager] Loaded " + boostEvents.size() + " Boost Event templates");
		DataManager.log.info("[DataManager] Loaded " + itemPresettingData.size() + " Item Presetting templates");

	}
}
