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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.model.trade.RepurchaseList;
import com.aionemu.gameserver.model.trade.TradeList;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.PrivateStoreService;
import com.aionemu.gameserver.services.RepurchaseService;
import com.aionemu.gameserver.services.TradeService;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author orz, ATracer, Simple, xTz
 * @modify pralinka
 * @modify Phantom_KNA
 */
public class CM_BUY_ITEM extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_BUY_ITEM.class);
	private int sellerObjId;
	private int tradeActionId;
	private int amount;
	private int itemId;
	private long count;
	private boolean isAudit;
	private TradeList tradeList;
	private RepurchaseList repurchaseList;

	public CM_BUY_ITEM(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		Player player = getConnection().getActivePlayer();
		sellerObjId = readD();
		tradeActionId = readH();
		amount = readH(); // total no of items

		if (amount < 0 || amount > 36) {
			isAudit = true;
			AuditLogger.info(player, "Player might be abusing CM_BUY_ITEM amount: " + amount);
			return;
		}
		if (tradeActionId == 2) {
			repurchaseList = new RepurchaseList(sellerObjId);
		}
		else if (tradeActionId == 18) {
			tradeList = new TradeList(0);
		}
		else {
			tradeList = new TradeList(sellerObjId);
		}

		for (int i = 0; i < amount; i++) {
			itemId = readD();
			count = readQ();

			// prevent exploit packets
			if (count < 0 || (itemId <= 0 && tradeActionId != 0) || itemId == 190000073 || itemId == 190000074 || count > 20000) {
				isAudit = true;
				AuditLogger.info(player, "Player might be abusing CM_BUY_ITEM item: " + itemId + " count: " + count);
				break;
			}

			System.out.println("TradeAction: " + tradeActionId);
			
			switch (tradeActionId) {
				case 0:// private store
				case 1:// sell to shop
					tradeList.addSellItem(itemId, count);
					break;
				case 2:// repurchase
					repurchaseList.addRepurchaseItem(player, itemId, count);
					break;
				case 13:// buy from shop
				case 14:// buy from abyss shop
				case 15:// buy from reward shop
					tradeList.addBuyItem(itemId, count);
					break;
				case 17:// sell from Miol
					tradeList.addSellItem(itemId, count);
					break;
				case 19:// sell from Inventory
					tradeList.addSellItem(itemId, count);
					break;
				default:
					System.out.println("Unknown TradeAction: " + tradeActionId);
					AuditLogger.info(player, "Unknow TradeAction: " + tradeActionId);
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (isAudit || player == null) {
			return;
		}

		VisibleObject target = player.getKnownList().getKnownObjects().get(sellerObjId);

		if (tradeActionId == 19 && target == null) {
			TradeService.performSellToShop(player, tradeList); // Sell from Inventory
		}
		else if (target == null) {
			return;
		}

		if (target instanceof Player && tradeActionId == 0) {
			Player targetPlayer = (Player) target;
			PrivateStoreService.sellStoreItem(targetPlayer, player, tradeList);
		}
		else if (target instanceof Npc) {
			Npc npc = (Npc) target;
			TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
			TradeListTemplate purchaseTemplate = DataManager.TRADE_LIST_DATA.getPurchaseTemplate(npc.getNpcId());
			switch (tradeActionId) {
				case 1:// sell to shop
					if (npc.getObjectTemplate().getTitleId() == 463495 || // <Ancient Relics Supervisor>
						npc.getObjectTemplate().getTitleId() == 463628 || // <Legion Relics Supervisor>
						npc.getObjectTemplate().getTitleId() == 463230 || // <Battlefield Equipment Vendor>
						npc.getObjectTemplate().getTitleId() == 463224 || // <Abyss Equipment Merchant>
						npc.getObjectTemplate().getTitleId() == 463209 || // <Legion Abyss Equipment Merchant>
						npc.getObjectTemplate().getTitleId() == 463493 || // <Battlefield Equipment Vendor>
						npc.getObjectTemplate().getTitleId() == 463491 || // <Abyss Equipment Merchant>
						npc.getObjectTemplate().getTitleId() == 463222 || // <Ceramium Medal Steward>
						npc.getObjectTemplate().getTitleId() == 463648 || // <Stigma Vendor>
						npc.getObjectTemplate().getTitleId() == 463492 || // <Ancient Coin Reward Officer>
						npc.getObjectTemplate().getTitleId() == 358113 || // <Stigma Vendor 4.8>
						npc.getObjectTemplate().getTitleId() == 358114 || // <Stigma Purchasing Officer>
						npc.getObjectTemplate().getTitleId() == 358081 || // <Abyss Equipment Merchant>
						npc.getObjectTemplate().getTitleId() == 358082 || // <Blood Mark Equipment Officer>
						npc.getObjectTemplate().getTitleId() == 358083 || // <Ancient Relic Collector>
						npc.getObjectTemplate().getTitleId() == 357852 || // <Abyss Equipment Officer>
						npc.getObjectTemplate().getTitleId() == 358096 || // <Abyss Equipment Purchasing Officer>
						npc.getObjectTemplate().getTitleId() == 358100 || // <Ancient Relics Supervisor>
						npc.getObjectTemplate().getTitleId() == 370408 || // <Ancient Icon Custodian>
						npc.getObjectTemplate().getTitleId() == 370409 || // <Ancient Seal Custodian>
						npc.getObjectTemplate().getTitleId() == 370410 || // <Ancient Goblet Custodian>
						npc.getObjectTemplate().getTitleId() == 370411 || // <Ancient Crown Custodian>
						// Reshanta By Phantom_KNA
						npc.getObjectTemplate().getTitleId() == 314357 || // <Ancient Icon Administration Officer>
						npc.getObjectTemplate().getTitleId() == 314358 || // <Ancient Seal Administration Officer>
						npc.getObjectTemplate().getTitleId() == 314359 || // <Ancient Goblet Administration Officer>
						npc.getObjectTemplate().getTitleId() == 314360) { // <Ancient Crown Administration Officer>
						TradeService.performSellForAPToShop(player, tradeList, purchaseTemplate);
					}
					// Sell To Shop [Purchase List Kinah]
					if (npc.getObjectTemplate().getTitleId() == 463203 || // <Special Vendor>
						npc.getObjectTemplate().getTitleId() == 463490 || // <Ancient Coin Equipment Vendor>
						npc.getObjectTemplate().getTitleId() == 463206) { // <Legion Special Vendor>
						TradeService.performSellForKinahToShop(player, tradeList, purchaseTemplate);
					}
					else {
						TradeService.performSellToShop(player, tradeList);
					}
					break;
				case 2:// repurchase
					RepurchaseService.getInstance().repurchaseFromShop(player, repurchaseList);
					break;
				case 13:// buy from shop
					if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.NORMAL) {
						TradeService.performBuyFromShop(npc, player, tradeList);
					}
					break;
				case 14:// buy from abyss shop
					if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.ABYSS) {
						TradeService.performBuyFromAbyssShop(npc, player, tradeList);
					}
					break;
				case 15:// buy from reward shop
					if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.REWARD) {
						TradeService.performBuyFromRewardShop(npc, player, tradeList);
					}
					break;
				case 17:// sell from Miol
					TradeService.performSellForKinahToShop(player, tradeList, purchaseTemplate);
					break;
				default:
					log.info(String.format("Unhandle shop action unk1: %d", tradeActionId));
					break;
			}
		}
	}
}
