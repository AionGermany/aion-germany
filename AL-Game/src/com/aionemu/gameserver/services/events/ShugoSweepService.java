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
package com.aionemu.gameserver.services.events;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerShugoSweepDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerSweep;
import com.aionemu.gameserver.model.templates.shugosweep.ShugoSweepReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHUGO_SWEEP;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ghostfur
 */
public class ShugoSweepService
{
    private static final Logger log = LoggerFactory.getLogger(ShugoSweepService.class);
	

	public void initShugoSweep() {
        log.info("[ShugoSweepService] is initialized...");
        //TODO
        //String weekly = "0 0 9 ? * WED *";
    }
	
    public void onLogin(Player player) {
        DAOManager.getDAO(PlayerShugoSweepDAO.class).load(player);
        if (player.getPlayerShugoSweep() == null) {
            int boardId = Rnd.get(1, 30);
            PlayerSweep ps = new PlayerSweep(0, 7, boardId);
            ps.setPersistentState(PersistentState.UPDATE_REQUIRED);
            player.setPlayerShugoSweep(ps);
            player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
            DAOManager.getDAO(PlayerShugoSweepDAO.class).add(player.getObjectId(), ps.getFreeDice(), ps.getStep(), ps.getBoardId());
        }
        PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
    }
	
    public void onLogout(Player player) {
        DAOManager.getDAO(PlayerShugoSweepDAO.class).store(player);
        player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
    }
	
    public void launchDice(Player player) {
        int move = Rnd.get(1, 6);
        int step = getPlayerSweep(player).getStep();
        int newStep = step + move;
        int dice = getPlayerSweep(player).getFreeDice();
        int goldDice = getCommonData(player).getGoldenDice();
        if (getPlayerSweep(player).getFreeDice() != 0) {
            getPlayerSweep(player).setFreeDice(dice - 1);
            player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
        } else  {
            getCommonData(player).setGoldenDice(goldDice - 1);
            DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
        } if (step == 30) {
            restartBoard(player);
        } if (newStep > 30) {
            int diff = 30 - step;
            getPlayerSweep(player).setStep(step + diff);
            PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), diff));
            restartBoard(player);
            int realMove = move - diff; //32 = case 2
            getPlayerSweep(player).setStep(realMove);
            player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
            PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), realMove));
            rewardPlayer(player, getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep());
        } else {
            getPlayerSweep(player).setStep(newStep);
            player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
            PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), move));
            rewardPlayer(player, getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep());
        }
    }
	
    public void resetBoard(Player player) {
        int reset = getCommonData(player).getResetBoard();
        getCommonData(player).setResetBoard(reset - 1);
        int boardId = 0;
        boardId = Rnd.get(1, 30);
        if (getPlayerSweep(player).getBoardId() == boardId) {
            boardId = Rnd.get(1, 30);
        }
        getPlayerSweep(player).setBoardId(boardId);
        getPlayerSweep(player).setStep(0);
        PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), 0, getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
    }
	
    public void rewardPlayer(Player player, int boardid, int step) {
        ShugoSweepReward reward = getRewardForBoard(boardid, step);
        ItemService.addItem(player, reward.getItemId(), reward.getCount());
    }
	
    public void restartBoard(Player player){
        if (getPlayerSweep(player).getStep() !=0) {
            player.getPlayerShugoSweep().setStep(0);
        }
        int boardId = 0;
        boardId = Rnd.get(1, 30);
        if (getPlayerSweep(player).getBoardId() == boardId) {
            boardId = Rnd.get(1, 30);
        }
        getPlayerSweep(player).setBoardId(boardId);
        PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
    }
	
    public PlayerCommonData getCommonData(Player player) {
        return player.getCommonData();
    }
	
    public PlayerSweep getPlayerSweep(Player player) {
        return player.getPlayerShugoSweep();
    }
	
    public static ShugoSweepReward getRewardForBoard(int boardId, int step) {
        return DataManager.SHUGO_SWEEP_REWARD_DATA.getRewardBoard(boardId, step);
    }
	
    public static final ShugoSweepService getInstance() {
        return SingletonHolder.instance;
    }
	
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final ShugoSweepService instance = new ShugoSweepService();
    }
}