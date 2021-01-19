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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dao.AccountTransformDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.AccountTransfo;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemTransformList;
import com.aionemu.gameserver.model.templates.transform_book.TransformBookTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORMATION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javolution.util.FastList;

/**
 * @author Falke_34, FrozenKiller
 */
public class TransformationService {

	public void onPlayerLogin(Player player) {
		PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(0, player));
	}

    public void makeTransform(final Player player, final int itemObjId) {
        if (player.getTransformCreated().size() >= 10) {
            player.getTransformCreated().clear();
        }
        final Item item = player.getInventory().getItemByObjId(itemObjId);
        final ItemTemplate it = item.getItemTemplate();
        final ItemTransformList transormList = DataManager.ITEM_TRANSFORM_LIST.getTransformList(it.getTransformList());
        final int transformId = transormList.getTransformId().get(Rnd.get(0, transormList.getTransformId().size() - 1));
        player.getController().cancelTask(TaskId.ITEM_USE);
        final ItemUseObserver moveObserver = new ItemUseObserver() {

            @Override
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.getObserveController().removeObserver(this);
                PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 2));
                PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(1, player, 0, 1, 2));
                PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TRANSFORMATION_CONTRACT_FAIL, 0);
            }
        };
        player.getObserveController().attach(moveObserver);
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.getObserveController().removeObserver(moveObserver);
                if (!player.getInventory().decreaseByObjectId(itemObjId, 1)) {
                    return;
                }
                int transfoRnd = transformId;
                AccountTransfo newTrans = new AccountTransfo(transfoRnd, 1);
                player.getTransformCreated().add(newTrans);
                if (player.getTransformList().hasTransformation(newTrans.getCardId())) {
                    AccountTransfo transfo = player.getTransformList().getTransformation(newTrans.getCardId());
                    transfo.setCount(transfo.getCount() + 1);
                    DAOManager.getDAO(AccountTransformDAO.class).updateTransfo(player.getPlayerAccount(), transfo);
                }
                else {
                    player.getTransformList().addNewTransformation(player, newTrans.getCardId(), 1);
                }
                PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(player.getTransformCreated(), 1, 1));
                PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 1));
                player.getTransformList().updateTransformationsList();
                transfoRnd = 0;
            }
        }, 500));
    }

    public void onPlayerTransform(Player player, int itemObjId, int cardId) {
        TransformBookTemplate book = DataManager.TRANSFORM_BOOK_DATA.getTransformBookById(cardId);
        if (player.isTransformed()) {
            PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TRANSFORMATION_CANT_CURRENT_STATE, 0);
            PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ACT_STATE_POLYMORPH, 3000);
            return;
        }
        if (!player.getInventory().decreaseByObjectId(itemObjId, 1L)) {
            return;
        }
        player.getController().useSkill(book.getSkillId());
        player.setLastUsedTransformation(book.getId());
        PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(0, player));
    }

    public void onCombineTransformation(Player player, ArrayList<Integer> materials) {
        List<Integer> deleteTransform = (List<Integer>)new FastList<Integer>();
        int kinahCost = 0;
        for (final Integer id : materials) {
            if (id != 0) {
                switch (DataManager.TRANSFORM_BOOK_DATA.getTransformBookById(id).getGrade()) {
                    case 1: {
                        kinahCost = 10000;
                        continue;
                    }
                    case 2: {
                        kinahCost = 42000;
                        continue;
                    }
                    case 3: {
                        kinahCost = 166000;
                        continue;
                    }
                    case 4: {
                        kinahCost = 664000;
                        continue;
                    }
                    case 5: {
                        kinahCost = 1280000;
                        continue;
                    }
                }
            }
        }
        if (player.getInventory().getKinah() < kinahCost) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
            return;
        }
        Account account = player.getPlayerAccount();
        int list = 0;
        switch (DataManager.TRANSFORM_BOOK_DATA.getTransformBookById(materials.get(0)).getGrade()) {
            case 1: {
                list = (Rnd.chance(50) ? 1001 : 1000);
                break;
            }
            case 2: {
                list = (Rnd.chance(30) ? 1002 : 1001);
                break;
            }
            case 3: {
                list = (Rnd.chance(20) ? 1003 : 1002);
                break;
            }
            case 4: {
                list = (Rnd.chance(10) ? 1004 : 1003);
                break;
            }
            case 5: {
                list = (Rnd.chance(5) ? 1004 : 1003);
                break;
            }
        }
        int transfoRnd;
        int rnd = transfoRnd = DataManager.ITEM_TRANSFORM_LIST.getTransformList(list).getTransformId().get(Rnd.get(0, DataManager.ITEM_TRANSFORM_LIST.getTransformList(list).getTransformId().size() - 1));
        if (player.getTransformList().hasTransformation(transfoRnd)) {
            AccountTransfo transform = player.getTransformList().getTransformation(transfoRnd);
            transform.setCount(transform.getCount() + 1);
            DAOManager.getDAO(AccountTransformDAO.class).updateTransfo(player.getPlayerAccount(), transform);
            player.getTransformCreated().add(transform);
        }
        else {
            player.getTransformList().addNewTransformation(player, transfoRnd, 1);
        }
        for (Integer id2 : materials) {
            if (id2 != 0) {
                AccountTransfo transfo = player.getTransformList().getTransformation(id2);
                if (transfo.getCount() == 0) {
                    return;
                }
                transfo.setCount(transfo.getCount() - 1);
                DAOManager.getDAO(AccountTransformDAO.class).updateTransfo(account, transfo);
                deleteTransform.add(id2);
            }
        }
        PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(2, deleteTransform));
        PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(1, player, transfoRnd, 3, 1));
        player.getTransformList().updateTransformationsList();
        player.getInventory().decreaseKinah(kinahCost);
        rnd = 0;
    }

    public void addAllToGM(Player player) {
        for (TransformBookTemplate template : DataManager.TRANSFORM_BOOK_DATA.getAllBooks().valueCollection()) {
            AccountTransfo transfo = new AccountTransfo(template.getId(), 4);
            player.getTransformList().addNewTransformation(player, transfo.getCardId(), transfo.getCount());
            PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(1, player, template.getId(), 0, 1));
        }
        player.getTransformList().updateTransformationsList();
    }

	public static TransformationService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final TransformationService instance = new TransformationService();
	}
}
