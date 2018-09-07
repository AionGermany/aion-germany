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
package com.aionemu.gameserver.model.templates.item.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;
import com.aionemu.gameserver.model.templates.decomposable.itemsets.AccessoriesSets;
import com.aionemu.gameserver.model.templates.decomposable.itemsets.ArmorSets;
import com.aionemu.gameserver.model.templates.decomposable.itemsets.IdianSets;
import com.aionemu.gameserver.model.templates.decomposable.itemsets.WeaponSets;
import com.aionemu.gameserver.model.templates.item.ExtractedItemsCollection;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.RandomItem;
import com.aionemu.gameserver.model.templates.item.RandomType;
import com.aionemu.gameserver.model.templates.item.ResultedItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELECT_ITEM_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemInfoService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

/**
 * @author oslo(a00441234)
 * @author GiGatR00n
 * @rework FrozenKiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DecomposeAction")
public class DecomposeAction extends AbstractItemAction {

	@XmlAttribute(name = "select")
	public boolean isSelect;

	private static final Logger log = LoggerFactory.getLogger(DecomposeAction.class);
	private static final int USAGE_DELAY = 1500;
	private static Map<Race, int[]> chunkEarth = new HashMap<Race, int[]>();

	static {
		chunkEarth.put(Race.ASMODIANS, new int[] { 152000051, 152000052, 152000053, 152000451, 152000453, 152000551, 152000651, 152000751, 152000752, 152000753, 152000851, 152000852, 152000853, 152001051, 152001052, 152000201, 152000102, 152000054, 152000055, 152000455, 152000457, 152000552, 152000652, 152000754, 152000755, 152000854, 152000855, 152000102, 152000202, 152000056, 152000057, 152000459, 152000461, 152000553, 152000653, 152000756, 152000757, 152000856, 152000857, 152000104, 152000204, 152000058, 152000059, 152000463, 152000465, 152000554, 152000654, 152000758, 152000759, 152000760, 152000858, 152001053, 152000107, 152000207, 152003004, 152003005, 152003006, 152000061, 152000062, 152000063, 152000468, 152000470, 152000556, 152000656, 152000657, 152000762, 152000763, 152000860, 152000861, 152000862, 152001055, 152001056, 152000113, 152000117, 152000214, 152000606, 152000713, 152000811 });

		chunkEarth.put(Race.ELYOS, new int[] { 152000001, 152000002, 152000003, 152000401, 152000403, 152000501, 152000601, 152000701, 152000702, 152000703, 152000801, 152000802, 152000803, 152001001, 152001002, 152000101, 152000201, 152000004, 152000005, 152000405, 152000407, 152000502, 152000602, 152000704, 152000705, 152000804, 152000805, 152000102, 152000202, 152000006, 152000007, 152000409, 152000411, 152000503, 152000603, 152000706, 152000707, 152000806, 152000807, 152000104, 152000204, 152000008, 152000009, 152000413, 152000415, 152000504, 152000604, 152000708, 152000709, 152000710, 152000808, 152001003, 152000107, 152000207, 152003004, 152003005, 152003006, 152000010, 152000011, 152000012, 152000417, 152000419, 152000505, 152000605, 152000607, 152000711, 152000712, 152000809, 152000810, 152000812, 152001004, 152001005, 152000113, 152000117, 152000214, 152000606, 152000713, 152000811 });
	}

	private static Map<Race, int[]> chunkSand = new HashMap<Race, int[]>();

	static {

		chunkSand.put(Race.ASMODIANS, new int[] { 152000452, 152000454, 152000301, 152000302, 152000303, 152000456, 152000458, 152000103, 152000203, 152000304, 152000305, 152000306, 152000460, 152000462, 152000105, 152000205, 152000307, 152000309, 152000311, 152000464, 152000466, 152000108, 152000208, 152000313, 152000315, 152000317, 152000469, 152000471, 152000114, 152000215, 152000320, 152000322, 152000324 });

		chunkSand.put(Race.ELYOS, new int[] { 152000402, 152000404, 152000301, 152000302, 152000303, 152000406, 152000408, 152000103, 152000203, 152000304, 152000305, 152000306, 152000410, 152000412, 152000105, 152000205, 152000307, 152000309, 152000311, 152000414, 152000416, 152000108, 152000208, 152000313, 152000315, 152000317, 152000418, 152000420, 152000114, 152000215, 152000320, 152000322, 152000324 });
	}

	private static int[] chunkRock = { 152000106, 152000206, 152000308, 152000310, 152000312, 152000109, 152000209, 152000314, 152000316, 152000318, 152000115, 152000216, 152000219, 152000321, 152000323, 152000325 };

	private static int[] chunkGemstone = { 152000112, 152000213, 152000116, 152000212, 152000217, 152000326, 152000327, 152000328 };

	private static int[] scrolls = { 164002002, 164002058, 164002010, 164002056, 164002057, 164002003, 164002059, 164002011, 164002004, 164002012, 164002012, 164000122, 164000131, 164000118 };

	private static int[] potion = { 162000045, 162000079, 162000016, 162000021, 162000015, 162000027, 162000020, 162000044, 162000043, 162000026, 162000019, 162000014, 162000023, 162000022 };

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (this.isSelect) {
			SelectItems selectitem = DataManager.DECOMPOSABLE_SELECT_ITEM_DATA.getSelectItem(player.getPlayerClass(), player.getRace(), parentItem.getItemId());
			if (selectitem == null) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_INVALID_STANCE(parentItem.getNameId()));
				return false;
			}
		}
		else {
			List<ExtractedItemsCollection> itemsCollections = DataManager.DECOMPOSABLE_ITEMS_DATA.getInfoByItemId(parentItem.getItemId());
			if ((itemsCollections == null) || (itemsCollections.isEmpty())) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_INVALID_STANCE(parentItem.getNameId()));
				return false;
			}
		}
		if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300447));
			return false;
		}

		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		// player.getController().cancelUseItem();

		if (this.isSelect) {
			SelectItems selectItems = DataManager.DECOMPOSABLE_SELECT_ITEM_DATA.getSelectItem(player.getPlayerClass(), player.getRace(), parentItem.getItemId());
			PacketSendUtility.sendPacket(player, new SM_SELECT_ITEM_LIST(selectItems, parentItem.getObjectId().intValue()));
			return;
		}

		// Extracts all Items of the specified ItemId
		List<ExtractedItemsCollection> itemsCollections = DataManager.DECOMPOSABLE_ITEMS_DATA.getInfoByItemId(parentItem.getItemId());

		// Filters only Items that are suitable for Player Level
		Collection<ExtractedItemsCollection> levelSuitableItems = filterItemsByLevel(player, itemsCollections);

		// Select only 1 Item based on Chance Attributes
		final ExtractedItemsCollection selectedCollection = selectItemByChance(levelSuitableItems);
		final ExtractedItemsCollection selectedCollections = selectItemByChance(levelSuitableItems);

		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), USAGE_DELAY, 0));

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				if (parentItem.getItemTemplate().getCategory() == ItemCategory.GATHERABLE) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UNCOMPRESS_COMPRESSED_ITEM_CANCELED(parentItem.getItemTemplate().getNameId()));
				}
				else if ((targetItem != null) && (targetItem.getItemTemplate().isArmor() || targetItem.getItemTemplate().isWeapon())) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_CANCELED(targetItem.getNameId()));
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_CANCELED(parentItem.getNameId()));
				}
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2), true);
				player.getObserveController().removeObserver(this);
			}
		};

		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				boolean validAction = postValidate(player, parentItem);
				int num = 1;
				if (validAction) {
					if (selectedCollection.getItems().size() > 0) {
						for (ResultedItem resultItem : selectedCollection.getItems()) {
							if (canAcquire(player, resultItem)) {
								ItemService.addItem(player, resultItem.getItemId(), resultItem.getResultCount());
								uniqueDropAnnounce(player, resultItem, parentItem);
								num++;
								if (MembershipConfig.ADD_CHEST_DROP) {
									if (num == 2) {
										int rndA = Rnd.get(100);
										if (player.getClientConnection().getAccount().getMembership() == 2) {
											if (rndA > 30) {
												for (ResultedItem resultItems : selectedCollections.getItems()) {
													if (canAcquire(player, resultItems)) {
														ItemService.addItem(player, resultItems.getItemId(), resultItems.getResultCount());
														uniqueDropAnnounces(player, resultItems, parentItem);
													}
												}
											}
										}
										else if (player.getClientConnection().getAccount().getMembership() == 1) {
											if (rndA > 60) {
												for (ResultedItem resultItems : selectedCollections.getItems()) {
													if (canAcquire(player, resultItems)) {
														ItemService.addItem(player, resultItems.getItemId(), resultItems.getResultCount());
														uniqueDropAnnounces(player, resultItems, parentItem);
													}
												}
											}
										}
									}
								}
							}
						}
						if (parentItem.getItemTemplate().getCategory() == ItemCategory.GATHERABLE) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UNCOMPRESS_COMPRESSED_ITEM_SUCCEEDED(parentItem.getNameId()));
						}
						else if ((targetItem != null) && (targetItem.getItemTemplate().isArmor() || targetItem.getItemTemplate().isWeapon())) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_SUCCEED(targetItem.getNameId()));
						}
						else {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(parentItem.getNameId())));
						}
					}

					if (selectedCollection.getRandomItems().size() > 0) {
						for (RandomItem randomItem : selectedCollection.getRandomItems()) {
							if (canAcquire(player, randomItem)) {
								RandomType randomType = randomItem.getType();
								if (randomType != null) {
									int randomId = 0;
									int i = 0;

									switch (randomItem.getType()) {
										case ENCHANTMENT: {
											do {
												randomId = 166000191 + Rnd.get(4);
												i++;
												if (i > 4) {
													randomId = 0;
													log.warn("DecomposeAction random item id not found. " + parentItem.getItemId());
													break;
												}
											}
											while (!ItemService.checkRandomTemplate(randomId));
											break;
										}
										case MANASTONE:
											break;
										case MANASTONE_COMMON_GRADE_10:
											List<Integer> itemsCommon10 = new ArrayList<Integer>();
											itemsCommon10.add(Rnd.get(167000226, 167000233)); // ID 167000234 Unavailable
											itemsCommon10.add(Rnd.get(167000525, 167000526));
											itemsCommon10.add(167000235);
											int randomItemOutCommon10 = itemsCommon10.get(Rnd.get(0, 2));
											randomId = randomItemOutCommon10;
											break;
										case MANASTONE_COMMON_GRADE_20:
											List<Integer> itemsCommon20 = new ArrayList<Integer>();
											itemsCommon20.add(Rnd.get(167000258, 167000261)); // ID 167000262 + 167000266 Unavailable
											itemsCommon20.add(Rnd.get(167000263, 167000265));
											itemsCommon20.add(Rnd.get(167000527, 167000528));
											itemsCommon20.add(167000267);
											int randomItemOutCommon20 = itemsCommon20.get(Rnd.get(0, 3));
											randomId = randomItemOutCommon20;
											break;
										case MANASTONE_COMMON_GRADE_30:
											List<Integer> itemsCommon30 = new ArrayList<Integer>();
											itemsCommon30.add(Rnd.get(167000290, 167000297)); // ID 167000298 Unavailable
											itemsCommon30.add(Rnd.get(167000529, 167000530));
											itemsCommon30.add(167000299);
											int randomItemOutCommon30 = itemsCommon30.get(Rnd.get(0, 2));
											randomId = randomItemOutCommon30;
											break;
										case MANASTONE_COMMON_GRADE_40:
											List<Integer> itemsCommon40 = new ArrayList<Integer>();
											itemsCommon40.add(Rnd.get(167000322, 167000329)); // ID 167000330 Unavailable
											itemsCommon40.add(Rnd.get(167000531, 167000532));
											itemsCommon40.add(167000331);
											int randomItemOutCommon40 = itemsCommon40.get(Rnd.get(0, 2));
											randomId = randomItemOutCommon40;
											break;
										case MANASTONE_COMMON_GRADE_50:
											List<Integer> itemsCommon50 = new ArrayList<Integer>();
											itemsCommon50.add(Rnd.get(167000354, 167000361)); // ID 167000362 Unavailable
											itemsCommon50.add(Rnd.get(167000533, 167000534));
											itemsCommon50.add(167000363);
											int randomItemOutCommon50 = itemsCommon50.get(Rnd.get(0, 2));
											randomId = randomItemOutCommon50;
											break;
										case MANASTONE_COMMON_GRADE_60:
											randomId = Rnd.get(167000543, 167000550);
											break;
										case MANASTONE_COMMON_GRADE_70:
											randomId = Rnd.get(167000758, 167000765);
											break;
										case MANASTONE_RARE_GRADE_10: // Unavailable
											break;
										case MANASTONE_RARE_GRADE_20:
											List<Integer> itemsRare20 = new ArrayList<Integer>();
											itemsRare20.add(Rnd.get(167000418, 167000425)); // ID 167000426 Unavailable
											itemsRare20.add(Rnd.get(167000535, 167000536));
											itemsRare20.add(167000427);
											int randomItemOutRare20 = itemsRare20.get(Rnd.get(0, 2));
											randomId = randomItemOutRare20;
											break;
										case MANASTONE_RARE_GRADE_30:
											List<Integer> itemsRare30 = new ArrayList<Integer>();
											itemsRare30.add(Rnd.get(167000450, 167000457)); // ID 167000458 Unavailable
											itemsRare30.add(Rnd.get(167000537, 167000538));
											itemsRare30.add(167000459);
											itemsRare30.add(167000465);
											int randomItemOutRare30 = itemsRare30.get(Rnd.get(0, 3));
											randomId = randomItemOutRare30;
											break;
										case MANASTONE_RARE_GRADE_40:
											List<Integer> itemsRare40 = new ArrayList<Integer>();
											itemsRare40.add(Rnd.get(167000482, 167000489)); // ID 167000490 + 167000492 - 167000496 Unavailable
											itemsRare40.add(167000491);
											itemsRare40.add(Rnd.get(167000539, 167000540));
											itemsRare40.add(167000497);
											int randomItemOutRare40 = itemsRare40.get(Rnd.get(0, 3));
											randomId = randomItemOutRare40;
											break;
										case MANASTONE_RARE_GRADE_50:
											List<Integer> itemsRare50 = new ArrayList<Integer>();
											itemsRare50.add(Rnd.get(167000514, 167000523));
											itemsRare50.add(Rnd.get(167000541, 167000542));
											int randomItemOutRare50 = itemsRare50.get(Rnd.get(0, 1));
											randomId = randomItemOutRare50;
											break;
										case MANASTONE_RARE_GRADE_60:
											randomId = Rnd.get(167000551, 167000563);
											break;
										case MANASTONE_RARE_GRADE_70:
											List<Integer> itemsRare70 = new ArrayList<Integer>();
											itemsRare70.add(Rnd.get(167000564, 167000576));
											itemsRare70.add(Rnd.get(167000766, 167000773)); // ID 167000774 Unavailable
											itemsRare70.add(Rnd.get(167000775, 167000778));
											int randomItemOutRare70 = itemsRare70.get(Rnd.get(0, 2));
											randomId = randomItemOutRare70;
											break;
										case MANASTONE_LEGEND_GRADE_10: // Unavailable
											break;
										case MANASTONE_LEGEND_GRADE_20: // Unavailable
											break;
										case MANASTONE_LEGEND_GRADE_30:
											randomId = Rnd.get(167000578, 167000613);
											break;
										case MANASTONE_LEGEND_GRADE_40:
											List<Integer> itemsLegend40 = new ArrayList<Integer>();
											itemsLegend40.add(Rnd.get(167000614, 167000645));
											itemsLegend40.add(Rnd.get(167000745, 167000748));
											itemsLegend40.add(Rnd.get(167000779, 167000811));
											int randomItemOutLegend40 = itemsLegend40.get(Rnd.get(0, 2));
											randomId = randomItemOutLegend40;
											break;
										case MANASTONE_LEGEND_GRADE_50:
											List<Integer> itemsLegend50 = new ArrayList<Integer>();
											itemsLegend50.add(Rnd.get(167000646, 167000681));
											itemsLegend50.add(Rnd.get(167000812, 167000844));
											int randomItemOutLegend50 = itemsLegend50.get(Rnd.get(0, 1));
											randomId = randomItemOutLegend50;
											break;
										case MANASTONE_LEGEND_GRADE_60:
											List<Integer> itemsLegend60 = new ArrayList<Integer>();
											itemsLegend60.add(Rnd.get(167000682, 167000714));
											itemsLegend60.add(Rnd.get(167000749, 167000751));
											itemsLegend60.add(Rnd.get(167000845, 167000877));
											int randomItemOutLegend60 = itemsLegend60.get(Rnd.get(0, 2));
											randomId = randomItemOutLegend60;
											break;
										case MANASTONE_LEGEND_GRADE_70:
											List<Integer> itemsLegend70 = new ArrayList<Integer>();
											itemsLegend70.add(Rnd.get(167000715, 167000744));
											itemsLegend70.add(Rnd.get(167000752, 167000757));
											itemsLegend70.add(Rnd.get(167010181, 167010267));
											int randomItemOutLegend70 = itemsLegend70.get(Rnd.get(0, 2));
											randomId = randomItemOutLegend70;
											break;
										case MANASTONE_EPIC_GRADE_50: // Unavailable
											break;
										case MANASTONE_EPIC_GRADE_60: // Unavailable
											break;
										case MANASTONE_EPIC_GRADE_70:
											randomId = Rnd.get(167020072, 167020083);
											break;
										case FINE_SUPERB_BETRAYER_CLOTHSET_65:
											randomId = ArmorSets.Fine_Superb_Betrayer_ClothSet_65[Rnd.get(ArmorSets.Fine_Superb_Betrayer_ClothSet_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_LEATHERSET_65:
											randomId = ArmorSets.Fine_Superb_Betrayer_LeatherSet_65[Rnd.get(ArmorSets.Fine_Superb_Betrayer_LeatherSet_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_CHAINSET_65:
											randomId = ArmorSets.Fine_Superb_Betrayer_ChainSet_65[Rnd.get(ArmorSets.Fine_Superb_Betrayer_ChainSet_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_PLATESET_65:
											randomId = ArmorSets.Fine_Superb_Betrayer_PlateSet_65[Rnd.get(ArmorSets.Fine_Superb_Betrayer_PlateSet_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_GLADIATOR_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Gladiator_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Gladiator_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_TEMPLAR_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Templar_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Templar_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_PRIEST_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Priest_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Priest_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_MAGE_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Mage_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Mage_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_SCOUT_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Scout_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Scout_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_BARD_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Bard_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Bard_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_RIDER_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Rider_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Rider_Weapon_65.length)];
											break;
										case FINE_SUPERB_BETRAYER_AETHERTECH_WEAPON_65:
											randomId = WeaponSets.Fine_Superb_Betrayer_Aethertech_Weapon_65[Rnd.get(WeaponSets.Fine_Superb_Betrayer_Aethertech_Weapon_65.length)];
											break;
										case KUNAX_GLADIATOR_WEAPON_65:
											randomId = WeaponSets.Kunax_Gladiator_Weapon_65[Rnd.get(WeaponSets.Kunax_Gladiator_Weapon_65.length)];
											break;
										case KUNAX_TEMPLAR_WEAPON_65:
											randomId = WeaponSets.Kunax_Templar_Weapon_65[Rnd.get(WeaponSets.Kunax_Templar_Weapon_65.length)];
											break;
										case KUNAX_PRIEST_WEAPON_65:
											randomId = WeaponSets.Kunax_Priest_Weapon_65[Rnd.get(WeaponSets.Kunax_Priest_Weapon_65.length)];
											break;
										case KUNAX_MAGE_WEAPON_65:
											randomId = WeaponSets.Kunax_Mage_Weapon_65[Rnd.get(WeaponSets.Kunax_Mage_Weapon_65.length)];
											break;
										case KUNAX_SCOUT_WEAPON_65:
											randomId = WeaponSets.Kunax_Scout_Weapon_65[Rnd.get(WeaponSets.Kunax_Scout_Weapon_65.length)];
											break;
										case KUNAX_BARD_WEAPON_65:
											randomId = WeaponSets.Kunax_Bard_Weapon_65[Rnd.get(WeaponSets.Kunax_Bard_Weapon_65.length)];
											break;
										case KUNAX_RIDER_WEAPON_65:
											randomId = WeaponSets.Kunax_Rider_Weapon_65[Rnd.get(WeaponSets.Kunax_Rider_Weapon_65.length)];
											break;
										case KUNAX_AETHERTECH_WEAPON_65:
											randomId = WeaponSets.Kunax_Aethertech_Weapon_65[Rnd.get(WeaponSets.Kunax_Aethertech_Weapon_65.length)];
											break;
										case KUNAX_CLOTHSET_65:
											randomId = ArmorSets.Kunax_ClothSet_65[Rnd.get(ArmorSets.Kunax_ClothSet_65.length)];
											break;
										case KUNAX_LEATHERSET_65:
											randomId = ArmorSets.Kunax_LeatherSet_65[Rnd.get(ArmorSets.Kunax_LeatherSet_65.length)];
											break;
										case KUNAX_CHAINSET_65:
											randomId = ArmorSets.Kunax_ChainSet_65[Rnd.get(ArmorSets.Kunax_ChainSet_65.length)];
											break;
										case KUNAX_PLATESET_65:
											randomId = ArmorSets.Kunax_PlateSet_65[Rnd.get(ArmorSets.Kunax_PlateSet_65.length)];
											break;
										case KUNAX_HELMET_CLOTH_65:
											randomId = ArmorSets.Kunax_Helmet_Cloth_65[Rnd.get(ArmorSets.Kunax_Helmet_Cloth_65.length)];
											break;
										case KUNAX_HELMET_LEATHER_65:
											randomId = ArmorSets.Kunax_Helmet_Leather_65[Rnd.get(ArmorSets.Kunax_Helmet_Leather_65.length)];
											break;
										case KUNAX_HELMET_CHAIN_65:
											randomId = ArmorSets.Kunax_Helmet_Chain_65[Rnd.get(ArmorSets.Kunax_Helmet_Chain_65.length)];
											break;
										case KUNAX_HELMET_PLATE_65:
											randomId = ArmorSets.Kunax_Helmet_Plate_65[Rnd.get(ArmorSets.Kunax_Helmet_Plate_65.length)];
											break;
										case KUNAX_ACCESSORY_MAGICAL_65:
											randomId = AccessoriesSets.Kunax_Accessory_Magical_65[Rnd.get(AccessoriesSets.Kunax_Accessory_Magical_65.length)];
											break;
										case KUNAX_ACCESSORY_PHYSICAL_65:
											randomId = AccessoriesSets.Kunax_Accessory_Physical_65[Rnd.get(AccessoriesSets.Kunax_Accessory_Physical_65.length)];
											break;
										case IDIAN_EPIC:
											randomId = IdianSets.Idian_EPIC[Rnd.get(IdianSets.Idian_EPIC.length)];
											break;
										case IDIAN_ICY_LEGEND:
											randomId = IdianSets.Idian_ICY_LEGEND[Rnd.get(IdianSets.Idian_ICY_LEGEND.length)];
											break;
										case IDIAN_CELESTIAL_EPIC:
											randomId = IdianSets.Idian_CELESTIAL_EPIC[Rnd.get(IdianSets.Idian_CELESTIAL_EPIC.length)];
											break;
										case IDIAN_TRIUMPHAL_EPIC:
											randomId = IdianSets.Idian_TRIUMPHAL_EPIC[Rnd.get(IdianSets.Idian_TRIUMPHAL_EPIC.length)];
											break;
										case IDIAN_GOLDEN_EPIC:
											randomId = IdianSets.Idian_GOLDEN_EPIC[Rnd.get(IdianSets.Idian_GOLDEN_EPIC.length)];
											break;
										case IDIAN_HARLOCK_EPIC:
											randomId = IdianSets.Idian_HARLOCK_EPIC[Rnd.get(IdianSets.Idian_HARLOCK_EPIC.length)];
											break;
										case IDIAN_INFUSED_EPIC:
											randomId = IdianSets.Idian_INFUSED_EPIC[Rnd.get(IdianSets.Idian_INFUSED_EPIC.length)];
											break;
										case IDIAN_TIDAL_UNIQUE:
											randomId = IdianSets.Idian_TIDAL_UNIQUE[Rnd.get(IdianSets.Idian_TIDAL_UNIQUE.length)];
											break;
										case IDIAN_NOBLE_TIDAL_EPIC:
											randomId = IdianSets.Idian_NOBLE_TIDAL_EPIC[Rnd.get(IdianSets.Idian_NOBLE_TIDAL_EPIC.length)];
											break;
										case IDIAN_BLAZING_FIGHTER_EPIC:
											randomId = IdianSets.Idian_BLAZING_FIGHTER_EPIC[Rnd.get(IdianSets.Idian_BLAZING_FIGHTER_EPIC.length)];
											break;
										case CHUNK_EARTH: {
											int[] earth = chunkEarth.get(player.getRace());
											randomId = earth[Rnd.get(earth.length)];
											break;
										}
										case CHUNK_SAND: {
											int[] sand = chunkSand.get(player.getRace());
											randomId = sand[Rnd.get(sand.length)];
											break;
										}
										case CHUNK_ROCK: {
											randomId = chunkRock[Rnd.get(chunkRock.length)];
											break;
										}
										case CHUNK_GEMSTONE: {
											randomId = chunkGemstone[Rnd.get(chunkGemstone.length)];
											break;
										}
										case SCROLLS: {
											randomId = scrolls[Rnd.get(scrolls.length)];
											break;
										}
										case POTION: {
											randomId = potion[Rnd.get(potion.length)];
											break;
										}
										case ANCIENTITEMS: {
											do {
												randomId = Rnd.get(186000051, 186000066);
												i++;
												if (i > 50) {
													randomId = 0;
													log.warn("DecomposeAction random item id not found. " + parentItem.getItemId());
													break;
												}
											}
											while (!ItemService.checkRandomTemplate(randomId));
											break;
										}
									}

									if (randomId <= 0 || !isItemExists(randomId)) {
										continue;
									}

									// Finally, Add the selected Decomposable Item to player inventory
									ItemService.addItem(player, randomId, randomItem.getResultCount());
								}
							}
						}
					}
				}
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, validAction ? 1 : 2));
			}

			private boolean isItemExists(int randomId) {
				if (!ItemService.checkRandomTemplate(randomId)) {
					log.warn("DecomposeAction random item id not found. " + randomId);
					return false;
				}
				return true;
			}

			private boolean canAcquire(Player player, ResultedItem resultItem) {
				Race race = resultItem.getRace();
				if (race != Race.PC_ALL && !race.equals(player.getRace())) {
					return false;
				}
				PlayerClass playerClass = resultItem.getPlayerClass();

				if (!playerClass.equals(PlayerClass.ALL) && !playerClass.equals(player.getPlayerClass())) {
					return false;
				}
				return true;
			}

			private boolean canAcquire(Player player, RandomItem randomItem) {
				Race race = randomItem.getRace();
				if (race != Race.PC_ALL && !race.equals(player.getRace())) {
					return false;
				}
				PlayerClass playerClass = randomItem.getPlayerClass();

				if (!playerClass.equals(PlayerClass.ALL) && !playerClass.equals(player.getPlayerClass())) {
					return false;
				}
				return true;
			}

			boolean postValidate(Player player, Item parentItem) {
				if (!canAct(player, parentItem, targetItem)) {
					return false;
				}
				Storage inventory = player.getInventory();
				int slotReq = calcMaxCountOfSlots(selectedCollection, player, false);
				int specialSlotreq = calcMaxCountOfSlots(selectedCollection, player, true);
				if (slotReq > 0 && inventory.getFreeSlots() < slotReq) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
					return false;
				}
				if (specialSlotreq > 0 && inventory.getSpecialCubeFreeSlots() < specialSlotreq) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
					return false;
				}
				if (player.getLifeStats().isAlreadyDead() || !player.isSpawned()) {
					return false;
				}
				if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_NO_TARGET_ITEM);
					return false;
				}
				if (selectedCollection.getItems().isEmpty() && selectedCollection.getRandomItems().isEmpty()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_FAILED(parentItem.getNameId()));
					return false;
				}
				return true;
			}
		}, USAGE_DELAY));
	}

	/**
	 * Add to result collection only items witch suits player's level
	 */
	private Collection<ExtractedItemsCollection> filterItemsByLevel(Player player, List<ExtractedItemsCollection> itemsCollections) {
		int playerLevel = player.getLevel();
		Collection<ExtractedItemsCollection> result = new ArrayList<ExtractedItemsCollection>();
		for (ExtractedItemsCollection collection : itemsCollections) {
			if (collection.getMinLevel() > playerLevel) {
				continue;
			}
			if (collection.getMaxLevel() > 0 && collection.getMaxLevel() < playerLevel) {
				continue;
			}
			result.add(collection);
		}
		return result;
	}

	/**
	 * Select only 1 item based on chance attributes
	 */
	private ExtractedItemsCollection selectItemByChance(Collection<ExtractedItemsCollection> itemsCollections) {
		float sumOfChances = calcSumOfChances(itemsCollections);
		float currentSum = 0f;
		float rnd = (float) Rnd.get(0, (int) (sumOfChances - 1) * 1000) / 1000;
		ExtractedItemsCollection selectedCollection = null;
		for (ExtractedItemsCollection collection : itemsCollections) {
			currentSum += collection.getChance();
			if (rnd < currentSum) {
				selectedCollection = collection;
				break;
			}
		}
		return selectedCollection;
	}

	private int calcMaxCountOfSlots(ExtractedItemsCollection itemsCollections, Player player, boolean special) {
		int maxCount = 0;
		for (ResultedItem item : itemsCollections.getItems()) {
			if (item.getRace().equals(Race.PC_ALL) || player.getRace().equals(item.getRace())) {
				if (item.getPlayerClass().equals(PlayerClass.ALL) || player.getPlayerClass().equals(item.getPlayerClass())) {
					ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(item.getItemId());
					if (special && template.getExtraInventoryId() > 0) {
						maxCount++;
					}
					else if (template.getExtraInventoryId() < 1) {
						maxCount++;
					}
				}
			}
		}
		return maxCount;
	}

	private float calcSumOfChances(Collection<ExtractedItemsCollection> itemsCollections) {
		float sum = 0;
		for (ExtractedItemsCollection collection : itemsCollections) {
			sum += collection.getChance();
		}
		return sum;
	}

	private void uniqueDropAnnounce(final Player player, final ResultedItem resultItem, final Item parentItem) {
		if (DropConfig.ENABLE_UNIQUE_CHEST_DROP_ANNOUNCE) {
			final ItemTemplate itemTemplate = ItemInfoService.getItemTemplate(resultItem.getItemId());
			if (itemTemplate.getItemQuality() == ItemQuality.EPIC || itemTemplate.getItemQuality() == ItemQuality.MYTHIC) {
				final String lastGetName = player.getName();
				String parents = parentItem.getName();
				String results = resultItem.getItemName();
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
				while (iter.hasNext()) {
					Player player2 = iter.next();
					PacketSendUtility.sendYellowMessage(player2, LanguageHandler.translate(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE1, lastGetName, results, parents));
				}

			}
		}
	}

	private void uniqueDropAnnounces(final Player player, final ResultedItem resultItem, final Item parentItem) {
		if (MembershipConfig.ADD_CHEST_DROP_ANNOUNCE) {
			final ItemTemplate itemTemplate = ItemInfoService.getItemTemplate(resultItem.getItemId());
			if (itemTemplate.getItemQuality() == ItemQuality.EPIC || itemTemplate.getItemQuality() == ItemQuality.MYTHIC) {
				final String lastGetName = player.getName();
				String parents = parentItem.getName();
				String results = resultItem.getItemName();
				if (player.getClientConnection().getAccount().getMembership() == 2) {
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						Player player2 = iter.next();
						PacketSendUtility.sendYellowMessage(player2, LanguageHandler.translate(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE3, lastGetName, results, parents));
					}
				}
				else if (player.getClientConnection().getAccount().getMembership() == 1) {
					Iterator<Player> iter = World.getInstance().getPlayersIterator();
					while (iter.hasNext()) {
						Player player2 = iter.next();
						PacketSendUtility.sendYellowMessage(player2, LanguageHandler.translate(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE2, lastGetName, results, parents));
					}
				}
			}
		}
	}
}
