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
package com.aionemu.gameserver.services.toypet;

import java.sql.Timestamp;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class MinionService {

	private static Logger log = LoggerFactory.getLogger(MinionService.class);

	public void addMinion(Player player, int minionId, String name, String grade, int level, int growthPoints) {
		MinionCommonData minionCommonData = player.getMinionList().addNewMinion(player, minionId, name, grade, level);
		if (minionCommonData != null) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(1, minionCommonData));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404316, name));
		}
		int questId = player.getRace() == Race.ASMODIANS ? 25545 : 15545;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getQuestVars().getQuestVars() == 0) {
			qs.setStatus(QuestStatus.REWARD);
			qs.setQuestVar(1);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
			if (qs.getStatus() == QuestStatus.COMPLETE || qs.getStatus() == QuestStatus.REWARD) {
				player.getController().updateNearbyQuests();
			}
		}
	}

	private static boolean validateAdoption(Player player, ItemTemplate template, int minionId) {
		if (template == null || template.getActions() == null || template.getActions().getAdoptMinionAction() == null) {
			return false;
		}
		if (DataManager.MINION_DATA.getMinionTemplate(minionId) == null) {
			log.warn("Trying adopt minion without template. PetId:" + minionId);
			return false;
		}
		return true;
	}

	public void onPlayerLogin(Player player) {
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(player, 11));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12, playerMinions));
		}
	}

	public void adoptMinion(Player player, Item item, String grade) {
		FastMap<Integer, MinionTemplate> minionTemplate = (FastMap<Integer, MinionTemplate>) new FastMap();
		int minionId = 0;
		int minionLvl = 0;
		int minionGrowthPoints = 0;
		String minionName = "";
		String minionGrade = "";
		for (MinionTemplate template : DataManager.MINION_DATA.getMinionData().valueCollection()) {
			if (template.getGrade().equalsIgnoreCase(grade) && template.getLevel() == 1) {
				minionTemplate.put(template.getId(), template);
			}
		}
		int rnd = Rnd.get((int) 1, (int) minionTemplate.size());
		int i = 1;
		for (MinionTemplate mt : minionTemplate.values()) {
			if (i == rnd) {
				minionId = mt.getId();
				minionName = mt.getName();
				minionGrade = mt.getGrade();
				minionLvl = mt.getLevel();
				minionGrowthPoints = mt.getGrowthPt();
				break;
			}
			++i;
		}
		if (!validateAdoption(player, item.getItemTemplate(), minionId)) {
			return;
		}
		addMinion(player, minionId, minionName, minionGrade, minionLvl, minionGrowthPoints);
		player.getMinionList().updateMinionsList();
	}

	public void adoptMinion(Player player, Item item, int minionId) {
		String minionName = DataManager.MINION_DATA.getMinionTemplate(minionId).getName();
		String minionGrade = DataManager.MINION_DATA.getMinionTemplate(minionId).getGrade();
		int minionLvl = DataManager.MINION_DATA.getMinionTemplate(minionId).getLevel();
		int minionGrowthPoints = DataManager.MINION_DATA.getMinionTemplate(minionId).getGrowthPt();
		if (!MinionService.validateAdoption(player, item.getItemTemplate(), minionId)) {
			return;
		}
		addMinion(player, minionId, minionName, minionGrade, minionLvl, minionGrowthPoints);
	}

	public void despawnMinion(Player player, boolean isManualDespawn) {
		Minion minions = player.getMinion();
		if (minions != null) {
			player.getController().cancelTask(TaskId.MINION_UPDATE);
			if (isManualDespawn) {
				minions.getCommonData().setDespawnTime(new Timestamp(System.currentTimeMillis()));
			}
			player.setMinion(null);
			minions.getController().delete();
		}
	}

	private static int minionId(int rnd) {
		if (rnd <= 35) {
			return 980010;
		}
		if (rnd >= 36 && rnd <= 70) {
			return 980011;
		}
		if (rnd >= 71 && rnd <= 105) {
			return 980012;
		}
		if (rnd >= 106 && rnd <= 140) {
			return 980013;
		}
		if (rnd >= 141 && rnd <= 175) {
			return 980020;
		}
		if (rnd >= 176 && rnd <= 210) {
			return 980021;
		}
		if (rnd >= 211 && rnd <= 245) {
			return 980022;
		}
		if (rnd >= 246 && rnd <= 280) {
			return 980023;
		}
		if (rnd >= 281 && rnd <= 315) {
			return 980030;
		}
		if (rnd >= 316 && rnd <= 350) {
			return 980031;
		}
		if (rnd >= 351 && rnd <= 385) {
			return 980032;
		}
		if (rnd >= 386 && rnd <= 420) {
			return 980033;
		}
		if (rnd >= 421 && rnd <= 455) {
			return 980040;
		}
		if (rnd >= 456 && rnd <= 490) {
			return 980041;
		}
		if (rnd >= 491 && rnd <= 525) {
			return 980042;
		}
		if (rnd >= 526 && rnd <= 560) {
			return 980043;
		}
		if (rnd >= 561 && rnd <= 595) {
			return 980050;
		}
		if (rnd >= 596 && rnd <= 630) {
			return 980051;
		}
		if (rnd >= 631 && rnd <= 665) {
			return 980052;
		}
		if (rnd >= 666 && rnd <= 700) {
			return 980053;
		}
		if (rnd >= 701 && rnd <= 735) {
			return 980060;
		}
		if (rnd >= 736 && rnd <= 770) {
			return 980061;
		}
		if (rnd >= 771 && rnd <= 805) {
			return 980062;
		}
		if (rnd >= 806 && rnd <= 840) {
			return 980063;
		}
		if (rnd >= 841 && rnd <= 875) {
			return 980070;
		}
		if (rnd >= 876 && rnd <= 910) {
			return 980071;
		}
		if (rnd >= 911 && rnd <= 945) {
			return 980072;
		}
		if (rnd >= 946 && rnd <= 980) {
			return 980073;
		}
		return 0;
	}

	public static MinionService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final MinionService instance = new MinionService();
	}
}
