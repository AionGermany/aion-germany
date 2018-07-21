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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EventsConfig;
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
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Created by Ghostfur
 */
public class ShugoSweepService {

	private static final Logger log = LoggerFactory.getLogger(ShugoSweepService.class);
	private final int boardId = EventsConfig.EVENT_SHUGOSWEEP_BOARD; 

	public void initShugoSweep() {
		log.info("[ShugoSweepService] is initialized...");
		// TODO
		// String weekly = "0 0 9 ? * WED *";
	}

	public void onLogin(Player player) {
		DAOManager.getDAO(PlayerShugoSweepDAO.class).load(player);
		if (player.getPlayerShugoSweep() == null) {
			PlayerSweep ps = new PlayerSweep(0, EventsConfig.EVENT_SHUGOSWEEP_FREEDICE, boardId);
			ps.setPersistentState(PersistentState.UPDATE_REQUIRED);
			player.setPlayerShugoSweep(ps);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
			DAOManager.getDAO(PlayerShugoSweepDAO.class).add(player.getObjectId(), ps.getFreeDice(), ps.getStep(), ps.getBoardId());
		}
		
		if (player.getPlayerShugoSweep().getBoardId() != boardId) {
			PlayerSweep ps = new PlayerSweep(0, getPlayerSweep(player).getFreeDice(), boardId);
			ps.setPersistentState(PersistentState.UPDATE_REQUIRED);
			player.setPlayerShugoSweep(ps);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
		}
		
		PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
	}

	public void onLogout(Player player) {
		DAOManager.getDAO(PlayerShugoSweepDAO.class).store(player);
		player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
	}

	public void launchDice(final Player player) {
		int move = Rnd.get(1, 6);
		int step = getPlayerSweep(player).getStep();
		int newStep = step + move;
		int dice = getPlayerSweep(player).getFreeDice();
		int goldDice = getCommonData(player).getGoldenDice();
		int diff = newStep - 30;
		if (getPlayerSweep(player).getFreeDice() != 0) {
			getPlayerSweep(player).setFreeDice(dice - 1);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
		} else {
			getCommonData(player).setGoldenDice(goldDice - 1);
			DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		}

		PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(boardId, getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), 0, 0));
		
		if (newStep > 30) {
			System.out.println("Step > 30: " + step + " Move: " + move + " NewStep: " + newStep);
			getPlayerSweep(player).setStep(newStep);
			PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), move));
			getPlayerSweep(player).setStep(diff);	
			rewardPlayer(player, getPlayerSweep(player).getStep(), diff);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
		} else if (newStep == 30) {
			System.out.println("Step = 30: " + step + " Move: " + move + " NewStep: " + newStep);
			getPlayerSweep(player).setStep(newStep);
			PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), move));
			rewardPlayer(player, getPlayerSweep(player).getStep(), newStep);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
		} else {
			System.out.println("Step normal " + step + " Move: " + move + " NewStep: " + newStep);
			getPlayerSweep(player).setStep(newStep);
			player.getPlayerShugoSweep().setShugoSweepByObjId(player.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), move));
			rewardPlayer(player, getPlayerSweep(player).getStep(), move);
		}
	}

	public void resetBoard(Player player) {
		int reset = getCommonData(player).getResetBoard();
		getCommonData(player).setResetBoard(reset - 1);
		getPlayerSweep(player).setStep(0);
		PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), 0, getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
	}

	private void rewardPlayer(final Player player, final int step, final int move) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					ShugoSweepReward reward = getRewardForBoard(boardId, step);
					ItemService.addItem(player, reward.getItemId(), reward.getCount());
				}
			}
		}, move * 1200);
		
	}

	private PlayerCommonData getCommonData(Player player) {
		return player.getCommonData();
	}

	private PlayerSweep getPlayerSweep(Player player) {
		return player.getPlayerShugoSweep();
	}

	private static ShugoSweepReward getRewardForBoard(int boardId, int step) {
		return DataManager.SHUGO_SWEEP_REWARD_DATA.getRewardBoard(boardId, step);
	}

	public static final ShugoSweepService getInstance() {
		return SingletonHolder.instance;
	}
	   
	private static class SingletonHolder {
		protected static final ShugoSweepService instance = new ShugoSweepService();
	}
}
