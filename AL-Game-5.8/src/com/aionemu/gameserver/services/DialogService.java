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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerHouseOwnerFlags;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionWarehouse;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.teleport.TeleportLocation;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLASTIC_SURGERY;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_REPURCHASE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELL_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRADELIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRADE_IN_LIST;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.craft.CraftSkillUpdateService;
import com.aionemu.gameserver.services.craft.RelinquishCraftStatus;
import com.aionemu.gameserver.services.item.ItemChargeService;
import com.aionemu.gameserver.services.teleport.PortalService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

// FOR TEMP FIX SHOULD BE DELETET LATER

/**
 * @author VladimirZ
 * @author GiGatR00n v4.7.5.x
 */
public class DialogService {

	public static void onCloseDialog(Npc npc, Player player) {
		switch (npc.getObjectTemplate().getTitleId()) {
			case 350409: // Legion Warehouse
			case 358046:
			case 358047:
			case 358048:
			case 358049:
			case 463212:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npc.getObjectId(), 0));
				Legion legion = player.getLegion();
				if (legion != null) {
					LegionWarehouse lwh = player.getLegion().getLegionWarehouse();
					if (lwh.getWhUser() == player.getObjectId()) {
						lwh.setWhUser(0);
					}
				}
				break;
			case 314362: // Stigma Master A
			case 314365: // Stigma Master B
			case 314366: // Stigma Master C
			case 350410: // Stigma Master
			case 350419: // Trade Broker
			case 357007: // Arcadian Fortress Stigma Master
			case 357019: // Umbral Fortress Stigma Master
			case 357031: // Eternum Fortress Stigma Master
			case 357043: // Skyclash Fortress Stigma Master
			case 358063: // Trade Broker 4.8
			case 358493: // Stigma Master 4.9
			case 358523: // Stigma Master 4.9
			case 370111: // Abyss Master Stigma A
			case 370112: // Abyss Master Stigma B
			case 370243: // Aethercraft Technician
			case 370503: // Gogorunerk Solution
			case 462878: // Village Alliance
			case 466226: // Stigma Researcher
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npc.getObjectId(), 0));
				break;
		}
	}

	public static void onDialogSelect(int dialogId, final Player player, Npc npc, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(npc, player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env)) {
			return;
		}
		if (player.getAccessLevel() >= 5 && CustomConfig.ENABLE_SHOW_DIALOGID) {
			PacketSendUtility.sendMessage(player, "questId" + questId);
			PacketSendUtility.sendMessage(player, "dialogId" + dialogId);
		}
		int targetObjectId = npc.getObjectId();
		int titleId = npc.getObjectTemplate().getTitleId();
		int level = player.getLevel();
		switch (dialogId) {
			case 2: {
				// Buy Item's
				TradeListTemplate tradeListTemplate = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
				if (tradeListTemplate == null) {
					PacketSendUtility.sendMessage(player, "Buy list is missing!!");
					break;
				}
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_SELL_WHILE_IN_RIDE);
					return;
				}
				if (player.getInventory().isFull()) {
					// You cannot trade as you are overburdened with items
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EXCHANGE_TOO_HEAVY_TO_TRADE);
					return;
				}
				switch (npc.getNpcId()) {
					// Iron Combat Administration Officer Elyos
					case 203179: // Anontrite
					case 203330: // Laksis
						if (player.getInventory().getItemCountByItemId(186000001) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Iron Combat Administration Officer Asmodians
					case 203659: // Lateni
					case 203689: // Lohaban
						if (player.getInventory().getItemCountByItemId(186000006) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Bronze Combat Administration Officer Elyos
					case 203931: // Ferenna
					case 798111: // Atro
						if (player.getInventory().getItemCountByItemId(186000002) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Bronze Combat Administration Officer Asmodians
					case 204360: // Nott
					case 204368: // Cliessa
					case 204425: // Bevna
					case 204426: // Skataon
						if (player.getInventory().getItemCountByItemId(186000007) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Silver Administration Officer Elyos
					case 203964: // Agrips
					case 204011: // Sandinas
					case 204515: // Belakade
					case 204571: // Lomulias
					case 204637: // Keas
					case 204639: // Serdy
					case 798112: // Metea
					case 798113: // Anobi
					case 801158: // Callias
					case 801161: // Donar
						if (player.getInventory().getItemCountByItemId(186000003) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Silver Administration Officer Asmodians
					case 204359: // Royaa
					case 204427: // Sigen
					case 204781: // Govanon
					case 204789: // Lewin
					case 801170: // Lamachus
					case 801173: // Kellas
						if (player.getInventory().getItemCountByItemId(186000008) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Gold Administration Officer Elyos
					case 204548: // Euripia
					case 204638: // Tolemos
					case 801164: // Alcman
					case 801167: // Dalla
					case 801182: // Dinon
					case 801185: // Gudrun
						if (player.getInventory().getItemCountByItemId(186000004) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Gold Administration Officer Asmodians
					case 204761: // Cinnia
					case 204762: // Coventina
					case 204766: // Maponus
					case 204767: // Art
					case 801179: // Nibelr
					case 801196: // Hippias
					case 801199: // Thorkell
						if (player.getInventory().getItemCountByItemId(186000009) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Platinum Administration Officer Elyos
					case 798172: // Eunomia
					case 798173: // Euterpe
					case 798174: // Guneus
					case 798175: // Charites
					case 798195: // Iphigenia
					case 798196: // Illithyia
					case 798197: // Neoptolemos
					case 798198: // Calliope
					case 801188: // Cariton
					case 801192: // Lithrasir
						if (player.getInventory().getItemCountByItemId(186000005) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Platinum Administration Officer Asmodians
					case 205160: // Nagel
					case 205161: // Huvat
					case 205162: // Bern
					case 205163: // Rumolt
					case 205183: // Signa
					case 205184: // Pogel
					case 205185: // Flazetta
					case 205186: // Forseti
					case 801202: // Bupalus
					case 801206: // Mofrig
						if (player.getInventory().getItemCountByItemId(186000010) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Mithril Administration Officer Elyos
					case 798914: // Eduardo
					case 798915: // Giuseppe
					case 798916: // Silva
					case 798917: // Amauri
						if (player.getInventory().getItemCountByItemId(186000018) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Mithril Administration Officer Asmodians
					case 799213: // Nordin
					case 799214: // Flores
					case 799215: // Godin
					case 799216: // Assulin
						if (player.getInventory().getItemCountByItemId(186000019) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Alabaster Order Steward Elyos
					case 805136: // Andun
					case 805137: // Barett
					case 805138: // Carsemion
					case 805139: // Seoron
						if (player.getInventory().getItemCountByItemId(186000100) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Field Warden Steward Asmodians
					case 805047: // Nielan
					case 805048: // Garpai
					case 805049: // Debuiss
					case 805050: // Lauris
						if (player.getInventory().getItemCountByItemId(186000103) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Fortuneers Order Steward Elyos
					case 805140: // Edell
					case 805141: // Pradon
					case 805142: // Gildox
						if (player.getInventory().getItemCountByItemId(186000102) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Charlirunerk's Daemons Steward Asmodians
					case 805051: // Atarinrinerk
					case 805052: // Moerunerk
					case 805053: // Momorinrinerk
					case 805054: // Yokurunerk
						if (player.getInventory().getItemCountByItemId(186000105) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Radiant Ops Steward Elyos
					case 805144: // Terpander
						if (player.getInventory().getItemCountByItemId(186000101) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Blood Crusade Steward Asmodians
					case 805055: // Ledmar
						if (player.getInventory().getItemCountByItemId(186000104) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					// Master Numismatist
					case 833744: // Cufrunerk
					case 833745: // Irumonerk
					case 833746: // Aimonerk
					case 833747: // Moerunerk
					case 833748: // Ronaminerk
					case 833749: // Grurerk
					case 833750: // Kirarinerk
					case 833751: // Mirunerk
						if (player.getInventory().getItemCountByItemId(186000237) == 0) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
				}
				// Abyss Stigma/Stuff Seller's
				switch (npc.getNpcId()) {
					case 203708: // Iocaste
					case 203710: // Dairos
					case 206331: // Rigdamis
					case 206332: // Gladria
						if (level < 25) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					case 798505: // Salvius
					case 798509: // Papinius
					case 798510: // Ecocia
					case 799224: // Camila
					case 802209: // Temista
					case 802210: // Fujak
					case 805130: // Fibhe
						if (level < 45) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					case 798507: // Gracus
					case 798508: // Tororite
						if (level < 50) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					case 798506: // Elmaia
						if (level < 55) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
				}
				int tradeModifier = tradeListTemplate.getSellPriceRate();
				PacketSendUtility.sendPacket(player, new SM_TRADELIST(player, npc, tradeListTemplate, PricesService.getVendorBuyModifier() * tradeModifier / 100));
				// Abyss Rank
				if (tradeListTemplate.getTradeNpcType() == TradeNpcType.ABYSS) {
					// You may be unable to use or equip some items in your purchase list depending on your Abyss Rank
					// Alternatively: your Abyss Rank may decrease
					// Are you sure you want to buy them ?
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSGBOX_BUY_RANKITEM_WITH_RANKDOWN_CONFIRM, 0, 0));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_TRADELIST(player, npc, tradeListTemplate, PricesService.getVendorBuyModifier() * tradeModifier / 100));
				}
				break;
			}
			case 3: {
				// Sell Item's
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_SELL_WHILE_IN_RIDE);
					return;
				}
				switch (npc.getNpcId()) {
					case 206331: // Rigdamis
					case 206332: // Gladria
						if (level < 25) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
					case 798509: // Papinius
					case 798510: // Ecocia
					case 799224: // Camila
					case 802209: // Temista
						if (level < 45) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
				}
				PacketSendUtility.sendPacket(player, new SM_SELL_ITEM(targetObjectId, PricesService.getVendorSellModifier(player.getRace())));
				break;
			}
			case 4: {
				// Stigma Open
				switch (titleId) {
					case 314362: // Stigma Master A
					case 314365: // Stigma Master B
					case 314366: // Stigma Master C
					case 350410: // Stigma Master
					case 357007: // Arcadian Fortress Stigma Master
					case 357019: // Umbral Fortress Stigma Master
					case 357031: // Eternum Fortress Stigma Master
					case 357043: // Skyclash Fortress Stigma Master
					case 358493: // Redemption Landing 4.9.1
					case 358523: // Harbinger's Landing 4.9.1
					case 370111: // Abyss Master Stigma A
					case 370112: // Abyss Master Stigma B
					case 370503: // Gogorunerk Solution
						if (player.getRace() == Race.ELYOS) {
							QuestState qs = player.getQuestStateList().getQuestState(1929); // Sliver Of Darkness
							if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(1929));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							}
							else {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1));
							}
						}
						else if (player.getRace() == Race.ASMODIANS) {
							QuestState qs = player.getQuestStateList().getQuestState(2900); // No Escaping Destiny
							if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(2900));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							}
							else {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1));
							}
						}
						break;
				}
				break;
			}
			case 5: {
				// Create Legion
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 2));
				break;
			}
			case 6: {
				// Disband Legion
				LegionService.getInstance().requestDisbandLegion(npc, player);
				break;
			}
			case 7: {
				// Recreate Legion
				LegionService.getInstance().recreateLegion(npc, player);
				break;
			}
			case 26: { // 4.3-4.7-4.8-4.9
				// Warehouse
				switch (titleId) {
					case 315008:
					case 350417:
					case 357070:
					case 357074:
					case 357078:
					case 357082:
					case 358039:
					case 358040:
					case 358041:
					case 358042:
					case 358043:
					case 358044:
					case 358045:
						// Warehouse Base Reshanta 4.9
					case 358314:
					case 358317:
					case 358320:
					case 358323:
					case 358326:
					case 358329:
					case 358332:
					case 358335:
					case 358338:
					case 358341:
					case 358344:
					case 358347:
					case 358350:
					case 358353:
					case 358356:
					case 358359:
					case 358362:
					case 358365:
					case 358368:
					case 358371:
					case 358374:
					case 358377:
					case 358380:
					case 358383:
					case 358386:
					case 358389:
					case 358392:
					case 358395:
					case 358398:
					case 358401:
					case 358404:
					case 358407:
						// End Warehouse Base Reshanta 4.9
					case 358485:
					case 358515:
					case 370503:
					case 462878:
						// Panesterra 4.7
						// Belus
					case 357010: // Arcadian Fortress Warehouse Manager
					case 357326: // Belani Warehouse Manager
					case 357330: // Belfarth Warehouse Manager
					case 357334: // Beling Warehouse Manager
					case 357338: // Belsal Warehouse Manager
						// Aspida
					case 357022: // Umbral Fortress Warehouse Manager
					case 357406: // Aspamon Warehouse Manager
					case 357410: // Aspatyr Warehouse Manager
					case 357414: // Aspar Warehouse Manager
					case 357418: // Aspal Warehouse Manager
						// Atanatos
					case 357034: // Eternum Fortress Warehouse Manager
					case 357486: // Atasin Warehouse Manager
					case 357490: // Atair Warehouse Manager
					case 357494: // Atangal Warehouse Manager
					case 357498: // Ataj Warehouse Manager
						// Disillon
					case 357046: // Skyclash Fortress Warehouse Manager
					case 357566: // Disilgot Warehouse Manager
					case 357570: // Disilveir Warehouse Manager
					case 357574: // Disiltroll Warehouse Manager
					case 357578: // Disilheim Warehouse Manager
						if (level < 10) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_WAREHOUSE("10"));
							return;
						}
						if (player.isInPlayerMode(PlayerMode.RIDE)) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PERSONAL_SHOP_RESTRICTION_RIDE);
							return;
						}
						if (!RestrictionsManager.canUseWarehouse(player)) {
							return;
						}
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 26));
						WarehouseService.sendWarehouseInfo(player, true);
						break;
				}
				break;
			}
			case 31: { // 4.3
				// Quest
				if (questId != 0) {
					QuestState qs = player.getQuestStateList().getQuestState(questId);
					if (qs != null) {
						if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD) {
							if (!"useitem".equals(npc.getAi2().getName())) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 10));
							}
							else {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 0));
							}
						}
					}
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 10));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 0));
				}
				break;
			}
			case 33: { // 4.3
				// Trade Broker
				if (level < 10) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_VENDOR("10"));
					return;
				}
				switch (titleId) {
					case 350419:
					case 358062:
					case 358063:
					case 370503:
					case 315066:
					case 358487: // Redemption Landing 4.9.1
					case 358517: // Harbinger's Landing 4.9.1
					case 462878:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 13));
						break;
				}
				break;
			}
			case 35: { // 4.3
				// Soul Healing
				final long expLost = player.getCommonData().getExpRecoverable();
				if (expLost == 0) {
					player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
					player.getCommonData().setDeathCount(0);
				}
				final double factor = (expLost < 1000000 ? 0.25 - (0.00000015 * expLost) : 0.1);
				final int price = (int) (expLost * factor);
				RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {

					@Override
					public void acceptRequest(Creature requester, Player responder) {
						if (player.getInventory().getKinah() >= price) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(expLost));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SUCCESS_RECOVER_EXPERIENCE);
							player.getCommonData().resetRecoverableExp();
							player.getInventory().decreaseKinah(price);
							player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
							player.getCommonData().setDeathCount(0);
						}
						else {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(price));
						}
					}

					@Override
					public void denyRequest(Creature requester, Player responder) {
					}
				};
				if (player.getCommonData().getExpRecoverable() > 0) {
					boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_RECOVER_EXPERIENCE, responseHandler);
					if (result) {
						PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_RECOVER_EXPERIENCE, 0, 0, String.valueOf(price)));
					}
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DONOT_HAVE_RECOVER_EXPERIENCE);
				}
				break;
			}
			case 128: {
				final long expLost = player.getCommonData().getExpRecoverable();
				if (expLost == 0) {
					player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
					player.getCommonData().setDeathCount(0);
				}
				//TODO PRICE !!!
				final int price = (int) ((expLost / 100) * 0.035) ;
				RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {

					@Override
					public void acceptRequest(Creature requester, Player responder) {
						if (player.getInventory().getKinah() >= price) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(expLost));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SUCCESS_RECOVER_EXPERIENCE);
							player.getCommonData().resetRecoverableExp();
							player.getInventory().decreaseKinah(price);
							player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
							player.getCommonData().setDeathCount(0);
							player.getLifeStats().increaseHp(TYPE.HP, player.getLifeStats().getMaxHp() + 1);
							player.getLifeStats().increaseMp(TYPE.MP, player.getLifeStats().getMaxMp() + 1);
							
						}
						else {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(price));
						}
					}

					@Override
					public void denyRequest(Creature requester, Player responder) {
					}
				};
				if (player.getCommonData().getExpRecoverable() > 0) {
					boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_RECOVER_EXPERIENCE2, responseHandler);
					if (result) {
						PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_RECOVER_EXPERIENCE2, 0, 0, price));
					}
				}
				else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DONOT_HAVE_RECOVER_EXPERIENCE);
				}
				break;
			}
			case 36: { // 4.3
				// Arena City Teleporter
				switch (npc.getNpcId()) {
					case 204089: // Garm
						TeleportService2.teleportTo(player, 120010000, 984.000f, 1543.000f, 222.100f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						break;
					case 203764: // Epeios
						TeleportService2.teleportTo(player, 110010000, 1462.500f, 1326.100f, 564.100f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						break;
					case 203981: // Meneus
						if (player.getRace() == Race.ELYOS) {
							QuestState qs = player.getQuestStateList().getQuestState(1346); // Killing For Castor.
							if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(1346));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							}
							else {
								TeleportService2.teleportTo(player, 210020000, 439.300f, 422.200f, 274.300f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
							}
						}
						break;
					case 730048: // Fire Temple Exit
						if (level < 27) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
						}
						if (player.getRace() == Race.ELYOS) {
							TeleportService2.teleportTo(player, 210020000, 1343.200f, 350.900f, 348.600f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						}
						else {
							TeleportService2.teleportTo(player, 220020000, 1592.100f, 977.200f, 140.700f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						}
						break;
					case 730049: // Symbol Of Mau Hero
						if (level < 35) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							return;
						}
						else {
							TeleportService2.teleportTo(player, 220020000, 2434.500f, 553.600f, 334.600f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						}
						break;
					case 730052: // Statue Of Mau Hero [The Hand Behind The IceClaw]
						if (level < 30) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							return;
						}
						else {
							TeleportService2.teleportTo(player, 220020000, 1252.900f, 266.000f, 534.200f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						}
						break;
					case 730053: // Statue Of Mau Hero [The Hand Behind The IceClaw]
						if (level < 30) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
						}
						else {
							TeleportService2.teleportTo(player, 220020000, 1243.500f, 263.900f, 531.300f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						}
						break;
				}
			}
			case 37: { // 4.3
				// Arena City Teleporter
				switch (npc.getNpcId()) {
					case 204087: // Gunnar
						TeleportService2.teleportTo(player, 120010000, 1005.100f, 1528.900f, 222.100f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						break;
					case 203875: // Nepis
						TeleportService2.teleportTo(player, 110010000, 1470.300f, 1343.500f, 563.700f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						break;
					case 203982: // Ipetos
						TeleportService2.teleportTo(player, 210020000, 446.200f, 431.100f, 274.500f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
						break;
				}
				break;
			}
			case 42: { // 4.3
				// Remove Mana Stone
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 20));
				break;
			}
			case 43: { // 4.3
				// Modify Appearance
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 19));
				break;
			}
			case 44: { // 4.3
				// Flight And Teleport
				if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
					if (level < 9) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
					}
					else {
						TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
					}
				}
				else {
					switch (npc.getNpcId()) {
						case 203194: // Daines [Poeta]
							if (player.getRace() == Race.ELYOS) {
								QuestState qs = player.getQuestStateList().getQuestState(1006); // Asension Quest.
								if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(1006));
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								}
								else {
									TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
								}
							}
							break;
						case 203679: // Osmar [Ishalgen]
							if (player.getRace() == Race.ASMODIANS) {
								QuestState qs = player.getQuestStateList().getQuestState(2008); // Asension Quest.
								if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(2008));
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								}
								else {
									TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
								}
							}
							break;
						case 203895: // Lamid [Sanctum]
							if (player.getRace() == Race.ELYOS) {
								QuestState qs = player.getQuestStateList().getQuestState(1926); // Secret Library Access.
								if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(1926));
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								}
								else {
									TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
								}
							}
							break;
						case 204268: // Gaminart [Pandaemonium]
							if (player.getRace() == Race.ASMODIANS) {
								QuestState qs = player.getQuestStateList().getQuestState(2938); // Secret Library Access.
								if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(2938));
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								}
								else {
									TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
								}
							}
							break;
						case 730040: // Secret Passage Of Indratu Legion [Heiron]
							if (level < 45) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							}
							else {
								TeleportService2.teleportTo(player, 210040000, 2599.130f, 2182.7583f, 154.74075f, (byte) 27, TeleportAnimation.JUMP_ANIMATION);
							}
							break;
						case 800245: // Naerty [Inggison]
						case 804561: // Topos [Levinshor]
						case 804562: // Spake [Levinshor]
							if (level < 55) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							}
							else {
								TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
							}
							break;
						case 805748: // Ortiga [Norsvold]
						case 805773: // Mantesia [Iluma]
							if (level < 66) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							}
							else {
								TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
							}
							break;
						case 805750: // Crouzer [Norsvold]
						case 805775: // Tasos [Iluma]
							if (level < 66) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1011));
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
							}
							else {
								TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
							}
							break;
						// Sky Island Rift [Asmodians]
						case 805872:
						case 805873:
						case 805874:
						case 805875:
						case 805876:
						case 805877:
						case 805878:
						case 805879:
						case 805880:
						case 805881:
							TeleportService2.teleportTo(player, 220110000, 1760.1392f, 2007.7195f, 196.34007f, (byte) 110, TeleportAnimation.JUMP_ANIMATION);
							break;
						// Sky Island Rift [Elyos]
						case 805907:
						case 805908:
						case 805909:
						case 805910:
						case 805911:
						case 805912:
						case 805913:
						case 805914:
						case 805915:
						case 805916:
							TeleportService2.teleportTo(player, 210100000, 1451.8922f, 1297.8304f, 335.60767f, (byte) 65, TeleportAnimation.JUMP_ANIMATION);
							break;
						default: {
							TeleportService2.showMap(player, targetObjectId, npc.getNpcId());
							break;
						}
					}
				}
				break;
			}
			case 45: // 4.3
			case 46: { // 4.3
				// Learn Craft
				// Improve Extraction
				if (level < 10) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CRAFT_MSG_CAN_WORK_ONLY_DEVA);
					return;
				}
				CraftSkillUpdateService.getInstance().learnSkill(player, npc);
				break;
			}
			case 47: // 4.3 Expand Cube
			case 48: { // 4.3
				// Expand Warehouse
				WarehouseService.expandWarehouse(player, npc);
				break;
			}
			case 53: { // 4.3
				// Legion Warehouse
				if (level < 10) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_GUILD_WAREHOUSE("10"));
					return;
				}
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PERSONAL_SHOP_RESTRICTION_RIDE);
					return;
				}
				if (player.getLegion() == null) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
					return;
				}
				switch (titleId) {
					case 350409:
					case 358046:
					case 358047:
					case 358048:
					case 358049:
					case 463212:
						LegionService.getInstance().openLegionWarehouse(player, npc);
						break;
				}
				break;
			}
			case 56: { // 4.3
				// Close Legion Warehouse
				break;
			}
			case 58: { // 4.3
				// Work Order
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 28));
				break;
			}
			case 59: { // 4.3
				// Coin's Reward
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 3));
				break;
			}
			case 61: // 4.3
			case 62: { // 4.3
				byte changesex = 0;
				byte check_ticket = 2;
				if (dialogId == 62) {
					// Gender Switch
					changesex = 1;
					if (player.getInventory().getItemCountByItemId(169660000) > 0 || // Gender Switch Ticket
						player.getInventory().getItemCountByItemId(169660001) > 0 || // [Event] Gender Switch Ticket
						player.getInventory().getItemCountByItemId(169660002) > 0 || // Gender Switch Ticket (60 min)
						player.getInventory().getItemCountByItemId(169660003) > 0 || // [Event] Gender Switch Ticket
						player.getInventory().getItemCountByItemId(169660004) > 0 || // [Event] Gender Switch Ticket
						player.getInventory().getItemCountByItemId(169660005) > 0) { // Gender Switch Ticket
						check_ticket = 1;
					}
				}
				else {
					// Plastic Surgery.
					if (player.getInventory().getItemCountByItemId(169650000) > 0 || // Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650001) > 0 || // [Event] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650002) > 0 || // [Special] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650003) > 0 || // [Special] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650004) > 0 || // Plastic Surgery Ticket (60 mins)
						player.getInventory().getItemCountByItemId(169650005) > 0 || // Plastic Surgery Ticket (60 mins)
						player.getInventory().getItemCountByItemId(169650006) > 0 || // [Event] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650007) > 0 || // [Event] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650008) > 0 || // Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650009) > 0 || // Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169650010) > 0 || // Plastic Surgery Ticket (60 mins)
						player.getInventory().getItemCountByItemId(169650011) > 0 || // [Stamp] Plastic Surgery Ticket
						player.getInventory().getItemCountByItemId(169691000) > 0) { // Plastic Surgery Ticket
						check_ticket = 1;
					}
				}
				PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, check_ticket, changesex));
				player.setEditMode(true);
				break;
			}
			case 66: { // 4.3
				// Armsfusion
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 29));
				break;
			}
			case 67: { // 4.3
				// Armsbreaking
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 30));
				break;
			}
			case 68: { // 4.3
				// Join Faction
				player.getNpcFactions().enterGuild(npc);
				break;
			}
			case 69: { // 4.3
				// Leave Faction
				player.getNpcFactions().leaveNpcFaction(npc);
				break;
			}
			case 70: { // 4.3
				// Repurchase
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_SELL_WHILE_IN_RIDE);
					return;
				}
				PacketSendUtility.sendPacket(player, new SM_REPURCHASE(player, npc.getObjectId()));
				break;
			}
			case 71: { // 4.3
				// Adopt Pet
				PacketSendUtility.sendPacket(player, new SM_PET(6));
				break;
			}
			case 72: { // 4.3
				// Surrender Pet
				PacketSendUtility.sendPacket(player, new SM_PET(7));
				break;
			}
			case 73: { // 4.3
				// Housing Build 3.0
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 32));
				break;
			}
			case 74: { // 4.3
				// Housing Destruct 3.0
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 33));
				break;
			}
			case 75: { // 4.3
				// Deep Conditioning Individual Item 2.7
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 35));
				break;
			}
			case 76: { // 4.3
				// Deep Conditioning All Item 2.7
				ItemChargeService.startChargingEquippedItems(player, targetObjectId, 1);
				break;
			}
			case 78: { // 4.3
				// News Mod Buy/Exchange 2.7
				TradeListTemplate tradeListTemplate = DataManager.TRADE_LIST_DATA.getTradeInListTemplate(npc.getNpcId());
				if (tradeListTemplate == null) {
					PacketSendUtility.sendMessage(player, "Buy list is missing!!");
					break;
				}
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_SELL_WHILE_IN_RIDE);
					return;
				}
				PacketSendUtility.sendPacket(player, new SM_TRADE_IN_LIST(npc, tradeListTemplate, 100));
				break;
			}
			case 79: { // 4.3
				RelinquishCraftStatus.getInstance();
				RelinquishCraftStatus.relinquishExpertStatus(player, npc);
				break;
			}
			case 80: { // 4.3
				RelinquishCraftStatus.getInstance();
				RelinquishCraftStatus.relinquishMasterStatus(player, npc);
				break;
			}
			case 84: { // 4.3
				// Sell & Buy House 3.0
				if ((player.getBuildingOwnerStates() & PlayerHouseOwnerFlags.BIDDING_ALLOWED.getId()) == 0) {
					if (player.getRace() == Race.ELYOS) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(18802));
					}
					else {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(28802));
					}
					return;
				}
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 38));
				break;
			}
			case 92: { // 4.3
				// Pet's 3.0 (Great Sidekick)
				PacketSendUtility.sendPacket(player, new SM_PET(16));
				break;
			}
			case 93: { // 4.3
				// Pet's 3.0 (Bannish Sidekick)
				PacketSendUtility.sendPacket(player, new SM_PET(17));
				break;
			}
			case 94: { // 4.3
				// Augmenting Individual Item 3.0
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 42));
				break;
			}
			case 95: { // 4.3
				// Augmenting All Item 3.0
				ItemChargeService.startChargingEquippedItems(player, targetObjectId, 2);
				break;
			}
			case 96: { // 4.3
				// Housing Studio 3.0
				HousingService.getInstance().recreatePlayerStudio(player);
				break;
			}
			case 100: { // 4.3
				// Town 3.9
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 43));
				break;
			}
			case 103: { // 4.3
				// Purchase List 4.3
				TradeListTemplate tradeListTemplate = DataManager.TRADE_LIST_DATA.getPurchaseTemplate(npc.getNpcId());
				if (tradeListTemplate == null) {
					PacketSendUtility.sendMessage(player, "Purchase List is missing !!");
					break;
				}
				if (player.isInPlayerMode(PlayerMode.RIDE)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_SELL_WHILE_IN_RIDE);
					return;
				}
				switch (npc.getNpcId()) {
					case 805976: // Winifred
					case 806012: // Italus
						if (level < 66) {
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							return;
						}
						break;
				}
				PacketSendUtility.sendPacket(player, new SM_SELL_ITEM(targetObjectId, tradeListTemplate));
				break;
			}
			case 106: { // 4.7
				// Move Item Skin 4.7
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 51));
				break;
			}
			case 107: { // 4.7
				// Trade In Upgrade 4.7
				break;
			}
			case 109: { // 4.7
				// Item Upgrade 4.7
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 52));
				break;
			}
			case 125: { // 4.8
				// Stigma Enchant 4.8
				switch (titleId) {
					case 314362: // Stigma Master A
					case 314365: // Stigma Master B
					case 314366: // Stigma Master C
					case 350410: // Stigma Master
					case 357007: // Arcadian Fortress Stigma Master
					case 357019: // Umbral Fortress Stigma Master
					case 357031: // Eternum Fortress Stigma Master
					case 357043: // Skyclash Fortress Stigma Master
					case 358493: // Redemption Landing 4.9.1
					case 358523: // Harbinger's Landing 4.9.1
					case 370111: // Abyss Master Stigma A
					case 370112: // Abyss Master Stigma B
						if (player.getRace() == Race.ELYOS) {
							QuestState qs = player.getQuestStateList().getQuestState(1929); // Sliver Of Darkness
							if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(1929));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							}
							else {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 53));
							}
						}
						else if (player.getRace() == Race.ASMODIANS) {
							QuestState qs = player.getQuestStateList().getQuestState(2900); // No Escaping Destiny
							if (qs == null || qs.getStatus() != QuestStatus.COMPLETE) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(2900));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
							}
							else {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 53));
							}
						}
						break;
				}
				break;
			}
			case 104: {
				if (player.getRace() == Race.ELYOS) {
					switch (npc.getNpcId()) {
						// Walk Of Fame Entrance Manager
						case 802437: // Tisiphone
							if (player.getAbyssRank().getRank().getId() < AbyssRankEnum.GENERAL.getId()) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								return;
							}
							TeleportService2.teleportTo(player, 110070000, 503.80746f, 417.2141f, 126.789635f, (byte) 68, TeleportAnimation.JUMP_ANIMATION);
							break;
						// Legion Area Entry Manager
						case 805162: // Cerio
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 1326.1046f, 640.8399f, 584.82385f, (byte) 26, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805164: // Storia
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 1378.3633f, 655.79205f, 584.82385f, (byte) 54, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805166: // Bersus
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 1228.2413f, 1628.6283f, 470.35565f, (byte) 100, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805168: // Sogia
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 1218.8335f, 1576.2424f, 470.35565f, (byte) 14, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805170: // Rumon
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 2327.8035f, 1535.8595f, 440.83572f, (byte) 17, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805172: // Sanoria
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 210070000, 2381.8564f, 1549.5217f, 440.83572f, (byte) 51, TeleportAnimation.JUMP_ANIMATION);
							break;
					}
				}
				else {
					switch (npc.getNpcId()) {
						// Walk Of Fame Entrance Manager
						case 802439: // Bulundur
							if (player.getAbyssRank().getRank().getId() < AbyssRankEnum.GENERAL.getId()) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 27));
								return;
							}
							TeleportService2.teleportTo(player, 120080000, 385.92166f, 251.25146f, 93.129425f, (byte) 24, TeleportAnimation.JUMP_ANIMATION);
							break;
						// Legion Area Entry Manager
						case 805174: // Sefwoire
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 1769.7422f, 2596.248f, 301.87747f, (byte) 12, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805176: // Portintom
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 1810.1752f, 2559.1487f, 301.87747f, (byte) 25, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805178: // Clarok
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 1435.401f, 1742.898f, 332.56546f, (byte) 38, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805180: // Hamkarden
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 1466.7686f, 1787.5085f, 332.56546f, (byte) 55, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805182: // Edelbourgh
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 768.7018f, 1316.1646f, 254.92737f, (byte) 1, TeleportAnimation.JUMP_ANIMATION);
							break;
						case 805184: // Orportin
							if (player.getLegion() == null) {
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
								return;
							}
							TeleportService2.teleportTo(player, 220080000, 803.8293f, 1274.7317f, 254.92737f, (byte) 18, TeleportAnimation.JUMP_ANIMATION);
							break;
					}
				}
				break;
			}
			case 10000:
			case 10001:
			case 10002:
			case 10003:
			case 10004:
				// 4.7
			case 20006:
			case 20007:
			case 20008:
			case 20009:
			case 20010: {
				if (questId == 0) {
					TeleporterTemplate template = DataManager.TELEPORTER_DATA.getTeleporterTemplateByNpcId(npc.getNpcId());
					PortalPath portalPath = DataManager.PORTAL2_DATA.getPortalDialog(npc.getNpcId(), dialogId, player.getRace());
					if (portalPath != null) {
						PortalService.port(portalPath, player, targetObjectId);
					}
					else if (template != null) {
						TeleportLocation loc = template.getTeleLocIdData().getTelelocations().get(0);
						if (loc != null) {
							TeleportService2.teleport(template, loc.getLocId(), player, npc, npc.getAi2().getName().equals("general") ? TeleportAnimation.JUMP_ANIMATION : TeleportAnimation.BEAM_ANIMATION);
						}
					}
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId, questId));
				}
				break;
			}
			default: {
				if (questId > 0) {
					if (dialogId == 23 && player.getInventory().isFull()) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 0));
					}
					else {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId, questId));
					}
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId));
				}
				break;
			}
		}
	}
}
