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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.MinionController;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
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
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

import javolution.util.FastMap;

public class MinionService {

//	private MinionBuff minionbuff;
	private static Logger log = LoggerFactory.getLogger(MinionService.class);

	public void addMinion(Player player, int minionId, String name, String grade, int level, int growthPoints) {
		MinionCommonData minionCommonData = player.getMinionList().addNewMinion(player, minionId, name, grade, level);
		if (minionCommonData != null) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, minionCommonData));
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
			log.warn("Trying adopt minion without template. MinionId:" + minionId);
			return false;
		}
		return true;
	}

	public void onPlayerLogin(Player player) {
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(1, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(10));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(13));
		}
	}

	public void adoptMinion(Player player, Item item, String grade) {
		FastMap<Integer, MinionTemplate> minionTemplate =  new FastMap<Integer, MinionTemplate>();
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
	
	public void spawnMinion(Player player, int minionObjId) {
		MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjId);
		MinionTemplate minionTemplate = DataManager.MINION_DATA.getMinionTemplate(minionCommonData.getMinionId());
		MinionController controller = new MinionController();
		Minion minion = new Minion(minionTemplate, controller, minionCommonData, player);
//		Iterator<MinionSkill> iterator = minionTemplate.getAction().getSkillsCollections().iterator();
//		while (iterator.hasNext()) {
//			player.getSkillList().addSkill(player, iterator.next().getSkillId(), 1);
//		}
		if (player.getMinion() != null) {
			despawnMinion(player, player.getMinionList().getLastUsed());
		}
		minion.setKnownlist(new PlayerAwareKnownList(minion));
		player.setMinion(minion);
		player.getMinionList().setLastUsed(minionObjId);
//		minionbuff.apply(player, minionCommonData.getMinionId());
		PacketSendUtility.broadcastPacketAndReceive(player,	new SM_MINIONS(6, minionCommonData));
	}

	public void despawnMinion(Player player, int minionObjId) {
		MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjId);
//		Iterator<MinionSkill> iterator = DataManager.MINION_DATA.getMinionTemplate(minionCommonData.getMinionId()).getAction().getSkillsCollections().iterator();
//		while (iterator.hasNext()) {
//			SkillLearnService.removeSkill(player, iterator.next().getSkillId());
//		}
		minionCommonData.setIsLooting(false);
		minionCommonData.setIsBuffing(false);
		player.getMinion().getController().delete();
		player.setMinion(null);
//		minionbuff.end(player);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(7, minionCommonData));
	}
	
	public void lockMinion(Player player, int minionObjId, int lock) {
		MinionCommonData minion = player.getMinionList().getMinion(minionObjId);
		if (lock == 1) {
			minion.setLock(true);
			DAOManager.getDAO(PlayerMinionsDAO.class).lockMinions(player, minionObjId, 1);
	        PacketSendUtility.broadcastPacket(player, new SM_MINIONS(4, minion));
		} else {
			minion.setLock(false);
			DAOManager.getDAO(PlayerMinionsDAO.class).lockMinions(player, minionObjId, 0);
	        PacketSendUtility.broadcastPacket(player, new SM_MINIONS(4, minion));
		}
		player.getMinionList().updateMinionsList();
	}
	
	public void renameMinion(Player player, int minionObjId, String name) {
		MinionCommonData minion = player.getMinionList().getMinion(minionObjId);
		minion.setName(name);
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionName(minion);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(3, minion));
		player.getMinionList().updateMinionsList();
	}
	
//	public void activateLoot(Player player, boolean activate) {
//		Minion minion = player.getMinion();
//		if(minion == null) {
//			return;
//		}
//		if(!minion.getCommonData().isLooting()) {
//			if (activate) {
//				if (player.isInTeam()) {
//					LootRuleType lootType = player.getLootGroupRules().getLootRule();
//					if (lootType == LootRuleType.FREEFORALL) {
//						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE03);
//						return;
//					}
//				}
//				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE01);
//			}
//			minion.getCommonData().setIsLooting(true);
//			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, 1, 0, true));
//		} else {
//			minion.getCommonData().setIsLooting(false);
//			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, 1, 0, false));
//		}
//	}
	
	public static MinionService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final MinionService instance = new MinionService();
	}
}
