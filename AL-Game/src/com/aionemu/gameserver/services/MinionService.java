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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.controllers.MinionController;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.minion.MinionBuff;
import com.aionemu.gameserver.model.team2.common.legacy.LootRuleType;
import com.aionemu.gameserver.model.templates.item.ItemMinionList;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.model.templates.minion.MinionSkill;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

public class MinionService {

	private List<Integer> minions;
	private MinionBuff mb;
	Logger log = LoggerFactory.getLogger(MinionService.class);
	private ScheduledFuture<?> conbine = null;

	public void init() {
		this.minions = DataManager.MINION_DATA.getAll();
		this.mb = new MinionBuff();
		log.info("[MinionService] Loaded " + minions.size() + " Minions");
	}

	public void makeMinion(final Player player, final int itemObjId) {
		final Item item = player.getInventory().getItemByObjId(itemObjId);
		final ItemTemplate it = item.getItemTemplate();
		ItemMinionList minionList = DataManager.ITEM_MINION_LIST.getMinionList(it.getMinionList());
		final int minionRnd = RndArray.get(minionList.getMinionId());
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 1500, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		final ItemUseObserver moveObserver = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 2));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_MSG_CANCEL_CONTRACT);
			}
		};
		player.getObserveController().attach(moveObserver);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(moveObserver);
				int minionId = 0;
				String grade = "";
				String name = "";
				if (!it.getMinionTicket()) {
					return;
				}
				if (it.isMinionCashContract()) {
					minionId = minionRnd;
					final MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(minionId);
					grade = mt.getGrade();
					name = mt.getName();
				} else {
					for (Integer asd : MinionService.this.minions) {
						MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(asd);
						if (mt2.getLevel() == 1) {
							minionId = minionRnd;
							MinionTemplate mt3 = DataManager.MINION_DATA.getMinionTemplate(minionId);
							grade = mt3.getGrade();
							name = mt3.getName();
						}
					}
				}
				if (!player.getInventory().decreaseByObjectId(itemObjId, 1)) {
					return;
				}
				if (player.getRace() == Race.ELYOS) {
					QuestState qs15545 = player.getQuestStateList().getQuestState(15545);
					if (qs15545 != null && qs15545.getStatus() == QuestStatus.START && qs15545.getQuestVarById(0) == 0) {
						ClassChangeService.onUpdateQuest15545(player);
					}
				} else {
					QuestState qs15546 = player.getQuestStateList().getQuestState(25545);
					if (qs15546 != null && qs15546.getStatus() == QuestStatus.START && qs15546.getQuestVarById(0) == 0) {
						ClassChangeService.onUpdateQuest25545(player);
					}
				}
				MinionCommonData mcd = player.getMinionList().addNewMinion(player, minionId, name, grade, 1, 0);
				if (mcd != null) {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd, 0));
				}
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 1));
			}
		}, 1500));
	}

	public void spawnMinion(Player player, int minionObjId) {
		MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjId);
		MinionTemplate minionTemplate = DataManager.MINION_DATA.getMinionTemplate(minionCommonData.getMinionId());
		MinionController controller = new MinionController();
		Minion minion = new Minion(minionTemplate, controller, minionCommonData, player);
		Iterator<MinionSkill> iterator = minionTemplate.getAction().getSkillsCollections().iterator();
		while (iterator.hasNext()) {
			player.getSkillList().addSkill(player, iterator.next().getSkillId(), 1);
		}
		if (player.getPet() != null) {
			PetSpawnService.dismissPet(player, true);
		}
		if (player.getMinion() != null) {
			despawnMinion(player, player.getMinionList().getLastUsed());
		}
		minion.setKnownlist(new PlayerAwareKnownList(minion));
		player.setMinion(minion);
		player.getMinionList().setLastUsed(minionObjId);
		player.getCommonData().setLastMinion(minionObjId);
		player.getCommonData().setMinionEnergy(0);
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		mb.apply(player, minionCommonData.getMinionId());
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(6, minionCommonData));
	}

	public void despawnMinion(Player player, int minionObjId) {
		MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjId);
		Iterator<MinionSkill> iterator = DataManager.MINION_DATA.getMinionTemplate(minionCommonData.getMinionId()).getAction().getSkillsCollections().iterator();
		while (iterator.hasNext()) {
			SkillLearnService.removeSkill(player, iterator.next().getSkillId());
		}
		minionCommonData.setIsLooting(false);
		minionCommonData.setIsBuffing(false);
		player.getMinion().getController().delete();
		player.setMinion(null);
		player.getMinionList().setLastUsed(0);
		player.getCommonData().setLastMinion(0);
		player.getCommonData().setMinionEnergy(0);
		player.getMinionList().updateMinionsList();
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		mb.end(player);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(7, minionCommonData));
	}

	public void onTeleportPlayer(Player player) {
		PlayerCommonData pcd = player.getCommonData();
		int minionObjId = pcd.getLastMinion();
		if (minionObjId != 0) {
			MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjId);
			Iterator<MinionSkill> iterator = DataManager.MINION_DATA.getMinionTemplate(minionCommonData.getMinionId()).getAction().getSkillsCollections().iterator();
			while (iterator.hasNext()) {
				SkillLearnService.removeSkill(player, iterator.next().getSkillId());
			}
			minionCommonData.setIsLooting(false);
			minionCommonData.setIsBuffing(false);
			player.getMinion().getController().delete();
			player.setMinion(null);
			player.getMinionList().setLastUsed(0);
			player.getCommonData().setLastMinion(0);
			player.getCommonData().setMinionEnergy(0);
			player.getMinionList().updateMinionsList();
			DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
			mb.end(player);
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(7, minionCommonData));
		}
	}

	public void onLoggedIn(Player player) {
		PacketSendUtility.sendPacket(player, new SM_MINIONS(0));
		PacketSendUtility.sendPacket(player, new SM_MINIONS(1, player.getMinionList().getMinions()));
		PacketSendUtility.sendPacket(player, new SM_MINIONS(11, player));
		PacketSendUtility.sendPacket(player, new SM_MINIONS(13));
		PacketSendUtility.sendPacket(player, new SM_MINIONS(14));
		if (player.getCommonData().getLastMinion() != 0) {
			player.getCommonData().setMinionEnergy(0);
			DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
			spawnMinion(player, player.getCommonData().getLastMinion());
		}
		if (player.getCommonData().getMinionFunctionTime() != null) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(11, -1702967296));
		}
		player.getMinionList().updateMinionsList();
	}

	public void onUpdateEnergy(Player player, int count) {
		int energy = player.getCommonData().getMinionEnergy() + count;
		if (energy >= 5000) {
			player.getCommonData().setMinionEnergy(5000);
		} else {
			player.getCommonData().setMinionEnergy(player.getCommonData().getMinionEnergy() + count);
		}
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(13));
	}

	public void addMinionGrowth(Player player, int exp) {
		int finalExp = 0;
		final MinionCommonData mcd = player.getMinionList().getMinion(player.getMinion().getObjectId());
		final MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(mcd.getMinionId());
		final int growth = mcd.getGrowthPoints();
		finalExp = growth + exp;
		if (finalExp >= mt.getGrowthMax()) {
			mcd.setGrowthPoints(mt.getGrowthMax());
		} else {
			mcd.setGrowthPoints(growth + finalExp);
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0));
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionGrowth(mcd);
		player.getMinionList().updateMinionsList();
	}

	public void onLogout(Player player) {
		if (player.getMinion() != null) {
			despawnMinion(player, player.getMinionList().getLastUsed());
		}
		player.getCommonData().setMinionEnergy(0);
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
	}

	public void growthUpMinion(Player player, int objId, List<Integer> sacrificeMinions) {
		final MinionCommonData mcd = player.getMinionList().getMinion(objId);
		final MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(mcd.getMinionId());
		int finalExp = 0;
		int kinahCost = 0;
		final int growth = mcd.getGrowthPoints();
		for (final MinionCommonData list : player.getMinionList().getMinions()) {
			for (final int sacrifices : sacrificeMinions) {
				if (list.getObjectId() == sacrifices) {
					final MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(list.getMinionId());
					kinahCost += mt2.getGrowthCost();
					if (player.getInventory().getKinah() < kinahCost) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_GROWTH_MSG_NOGOLD);
						return;
					}
					finalExp += mt2.getGrowthPoints();
					player.getMinionList().disMissMinion(list.getMinionId());
					PacketSendUtility.sendPacket(player, new SM_MINIONS(3, list, 0));
					this.dismissMinion(player, list.getObjectId());
				}
			}
		}
		if (finalExp <= 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_GROWTH_MSG_NOTSELECT);
			return;
		}
		if (player.getInventory().getKinah() < kinahCost) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_GROWTH_MSG_NOGOLD);
			return;
		}
		if (growth + finalExp > mt.getGrowthMax()) {
			mcd.setGrowthPoints(mt.getGrowthMax());
		} else {
			mcd.setGrowthPoints(growth + finalExp);
		}
		player.getInventory().decreaseKinah(kinahCost);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0));
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionGrowth(mcd);
		player.getMinionList().updateMinionsList();
	}

	public void EnergyRecharge(Player player, int isAuto) {
		boolean auto = false;
		if (player.getCommonData().getMinionEnergy() >= 5000) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_MSG_FENERGY_CHARGE);
			return;
		}
		int rechargeCount = 5000 - player.getCommonData().getMinionEnergy();
		int price = 20 * rechargeCount;
		if (player.getInventory().getKinah() < price) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_MSG_FENERGY_CHARGE_FAIL_BY_GOLD);
			return;
		}
		if (isAuto == 0) {
			auto = false;
		} else {
			auto = true;
		}
		player.getCommonData().setMinionEnergy(player.getCommonData().getMinionEnergy() + rechargeCount);
		player.getInventory().decreaseKinah(price);
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(13));
	}

	public void lockMinion(final Player player, int objId, int lock) {
		MinionCommonData mcd = player.getMinionList().getMinion(objId);
		if (mcd.isLocked()) {
			mcd.setLocked(false);
		} else {
			mcd.setLocked(true);
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(5, mcd, 0));
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionLock(mcd);
	}

	public void EvolveMinion(Player player, int objId) {
		String grade = "";
		MinionCommonData mcd = player.getMinionList().getMinion(objId);
		MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(mcd.getMinionId());
		if (player.getInventory().getItemCountByItemId(mt.getEvolved().getItemId()) < mt.getEvolved().getEvolvedNum()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_EVOLVE_MSG_LACK_ITEM);
			return;
		}
		if (player.getInventory().getKinah() < mt.getEvolved().getEvolvedCost()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_EVOLVE_MSG_NOGOLD);
			return;
		}
		String name = mcd.getName();
		int minionId = mt.getId() + 1;
		player.getInventory().decreaseKinah(mt.getEvolved().getEvolvedCost());
		player.getInventory().decreaseByItemId(mt.getEvolved().getItemId(), mt.getEvolved().getEvolvedNum());
		player.getMinionList().disMissMinion(mt.getId());
		PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd, 0));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404350, new Object[] { mcd.getName(), mt.getLevel() + 1 }));
		dismissMinion(player, mcd.getObjectId());
		MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId);
		grade = mt2.getGrade();
		MinionCommonData mcd2 = player.getMinionList().addNewMinion(player, minionId, name, grade, 1, 0);
		if (mcd != null) {
			player.getMinionList().updateMinionsList();
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd2, 0));
		}
	}

	public void dismissMinion(Player player, int objId) {
		MinionCommonData mcd = player.getMinionList().getMinion(objId);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd, 0));
		player.getMinionList().disMissMinion(mcd.getMinionId());
		DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinion(player, objId);
		player.getMinionList().getMinions().remove(mcd);
		player.getCommonData().setMinionEnergy(0);
		player.getMinionList().updateMinionsList();
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
	}

	public void CombineMinion(Player player, int conbine1, int conbine2, int conbine3, int conbine4) {
		boolean combineSuccess = Rnd.chance(CustomConfig.COMBINE_MINION);
		MinionCommonData mcd1 = player.getMinionList().getMinion(conbine1);
		MinionCommonData mcd2 = player.getMinionList().getMinion(conbine2);
		MinionCommonData mcd3 = player.getMinionList().getMinion(conbine3);
		MinionCommonData mcd4 = player.getMinionList().getMinion(conbine4);
		MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(mcd1.getMinionId());
		if (combineSuccess) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd1, 3));
			player.getMinionList().disMissMinion(mcd1.getMinionId());
			dismissMinion(player, mcd1.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd2, 3));
			player.getMinionList().disMissMinion(mcd2.getMinionId());
			dismissMinion(player, mcd2.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd3, 3));
			player.getMinionList().disMissMinion(mcd3.getMinionId());
			dismissMinion(player, mcd3.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd4, 3));
			player.getMinionList().disMissMinion(mcd4.getMinionId());
			dismissMinion(player, mcd4.getObjectId());
		} else {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd1, 2));
			player.getMinionList().disMissMinion(mcd1.getMinionId());
			dismissMinion(player, mcd1.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd2, 2));
			player.getMinionList().disMissMinion(mcd2.getMinionId());
			dismissMinion(player, mcd2.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd3, 2));
			player.getMinionList().disMissMinion(mcd3.getMinionId());
			dismissMinion(player, mcd3.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_MINIONS(3, mcd4, 2));
			player.getMinionList().disMissMinion(mcd4.getMinionId());
			dismissMinion(player, mcd4.getObjectId());
		}
		if (combineSuccess && !this.isKerub(mt.getId()) && !this.isRankA(mt.getId())) {
			if (mt.getGradeId() == 3) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(31);
				int minionId;
				int minionRnd = minionId = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId);
				String grade = mt2.getGrade();
				String name = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId, name, grade, 1, 0);
				if (mcd5 != null) {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 2));
				}
			} else if (mt.getGradeId() == 2) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(32);
				int minionId2;
				int minionRnd = minionId2 = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId2);
				String grade2 = mt2.getGrade();
				String name2 = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId2, name2, grade2, 1,
						0);
				if (mcd5 != null) {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 2));
				}
			}
		} else if (mt.getGradeId() == 3) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(30);
			int minionId;
			int minionRnd = minionId = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId);
			String grade = mt2.getGrade();
			String name = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId, name, grade, 1, 0);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
		} else if (mt.getGradeId() == 2) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(31);
			int minionId2;
			int minionRnd = minionId2 = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId2);
			String grade2 = mt2.getGrade();
			String name2 = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId2, name2, grade2, 1, 0);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
		}
		if (combineSuccess && this.isKerub(mt.getId())) {
			if (mt.getGradeId() == 4) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(34);
				int minionId3;
				int minionRnd = minionId3 = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId3);
				String grade3 = mt2.getGrade();
				String name3 = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId3, name3, grade3, 1, 0);
				PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
			} else if (mt.getGradeId() == 3) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(35);
				int minionId4;
				int minionRnd = minionId4 = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId4);
				String grade4 = mt2.getGrade();
				String name4 = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId4, name4, grade4, 1, 0);
				PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
			} else if (mt.getGradeId() == 2) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(36);
				int minionId5;
				int minionRnd = minionId5 = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId5);
				String grade5 = mt2.getGrade();
				String name5 = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId5, name5, grade5, 1, 0);
				PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
			}
		} else if (mt.getGradeId() == 4) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(33);
			int minionId3;
			int minionRnd = minionId3 = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId3);
			String grade3 = mt2.getGrade();
			String name3 = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId3, name3, grade3, 1, 0);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
		} else if (mt.getGradeId() == 3) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(34);
			int minionId4;
			int minionRnd = minionId4 = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId4);
			String grade4 = mt2.getGrade();
			String name4 = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId4, name4, grade4, 1, 0);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
		} else if (mt.getGradeId() == 2) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(35);
			int minionId5;
			int minionRnd = minionId5 = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId5);
			String grade5 = mt2.getGrade();
			String name5 = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId5, name5, grade5, 1, 0);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
		}
		if (combineSuccess && this.isRankA(mt.getId())) {
			if (mt.getGradeId() == 4) {
				ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(37);
				int minionId6;
				int minionRnd = minionId6 = RndArray.get(minionList1.getMinionId());
				MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId6);
				String grade6 = mt2.getGrade();
				String name6 = mt2.getName();
				MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId6, name6, grade6, 1, 0);
				if (mcd5 != null) {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 2));
				}
			}
		} else if (mt.getGradeId() == 4) {
			ItemMinionList minionList1 = DataManager.ITEM_MINION_LIST.getMinionList(32);
			int minionId6;
			int minionRnd = minionId6 = RndArray.get(minionList1.getMinionId());
			MinionTemplate mt2 = DataManager.MINION_DATA.getMinionTemplate(minionId6);
			String grade6 = mt2.getGrade();
			String name6 = mt2.getName();
			MinionCommonData mcd5 = player.getMinionList().addNewMinion(player, minionId6, name6, grade6, 1, 0);
			if (mcd5 != null) {
				PacketSendUtility.sendPacket(player, new SM_MINIONS(2, mcd5, 3));
			}
		}
	}

	public boolean isKerub(int minionId) {
		return minionId == 980010 || minionId == 980011 || minionId == 980012 || minionId == 980013;
	}

	public boolean isRankA(int minionId) {
		return minionId == 980063 || minionId == 980073 || minionId == 980085 || minionId == 980089 || minionId == 980093;
	}

	public void renameMinion(Player player, int objId, String name) {
		MinionCommonData mcd = player.getMinionList().getMinion(objId);
		if (name.length() > 9) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_OVERLENGTH);
			return;
		}
		mcd.setName(name);
		DAOManager.getDAO(PlayerMinionsDAO.class).updateName(mcd);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(4, mcd, 0));
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_CONFIRM);
	}

	public void activateMinionFunction(Player player) {
		long leftTime = System.currentTimeMillis() - 1702967296;
		if (player.getInventory().decreaseByObjectId(190200100, 1)) {
			player.getCommonData().setMinionFunctionTime(new Timestamp(leftTime));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(11, player));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(14));
		}
	}

	public void activateLoot(Player player, int minionObj, boolean activate) {
		if (activate) {
			if (player.isInTeam()) {
				LootRuleType lootType = player.getLootGroupRules().getLootRule();
				if (lootType == LootRuleType.FREEFORALL) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE03);
					return;
				}
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE01);
		}
		MinionCommonData mcd = player.getMinionList().getMinion(minionObj);
		mcd.setIsLooting(activate);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(activate));
	}

	public void relocateDoping(Player player, int minionObjectId, int targetSlot, int destinationSlot) {
		MinionCommonData minions = player.getMinionList().getMinion(minionObjectId);
		if (minions == null || minions.getDopingBag() == null) {
			return;
		}
		int[] scrollBag = minions.getDopingBag().getScrollsUsed();
		int targetItem = scrollBag[targetSlot - 2];
		if (destinationSlot - 2 > scrollBag.length - 1) {
			minions.getDopingBag().setItem(targetItem, destinationSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 2, targetItem, destinationSlot));
			minions.getDopingBag().setItem(0, targetSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 2, targetItem, targetSlot));
		} else {
			minions.getDopingBag().setItem(scrollBag[destinationSlot - 2], targetSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 2, scrollBag[destinationSlot - 2], targetSlot));
			minions.getDopingBag().setItem(targetItem, destinationSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 2, targetItem, destinationSlot));
		}
	}

	public void addItemToDopingBag(Player player, int action, int minionObjectId, int itemId, int targetSlot) {
		if (player.getMinion() == null) {
			return;
		}
		Minion minions = player.getMinion();
		minions.getCommonData().getDopingBag().setItem(itemId, targetSlot);
		if (minions.getCommonData().getDopingBag().getFoodItem() != 0) {
		}
		if (minions.getCommonData().getDopingBag().getDrinkItem() != 0) {
		}
		for (int n : minions.getCommonData().getDopingBag().getScrollsUsed()) {
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 0, itemId, targetSlot));
	}

	public void buffPlayer(final Player player, int minionObjectId, int itemId, final int slot) {
		Minion minion = player.getMinion();
		if (minion == null || minion.getCommonData().getDopingBag() == null) {
			return;
		}
		List<Item> items = player.getInventory().getItemsByItemId(itemId);
		Item useItem = items.get(0);
		ItemActions itemActions = useItem.getItemTemplate().getActions();
		ItemUseLimits limit = new ItemUseLimits();
		int useDelay = player.getItemCooldown(useItem.getItemTemplate()) / 3;
		if (useDelay < 3000) {
			useDelay = 3000;
		}
		limit.setDelayId(useItem.getItemTemplate().getUseLimits().getDelayId());
		limit.setDelayTime(useDelay);
		if (player.isItemUseDisabled(limit)) {
			final int useItemId = itemId;
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 3, useItemId, slot));
				}
			}, useDelay);
			return;
		}
		if (!RestrictionsManager.canUseItem(player, useItem) || player.isProtectionActive()) {
			player.addItemCoolDown(limit.getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
		} else {
			player.getController().cancelCurrentSkill();
			for (AbstractItemAction itemAction : itemActions.getItemActions()) {
				if (itemAction.canAct(player, useItem, null)) {
					itemAction.act(player, useItem, null);
				}
			}
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 3, itemId, slot));
		itemId = minion.getCommonData().getDopingBag().getFoodItem();
		long totalDopes = player.getInventory().getItemCountByItemId(itemId);
		itemId = minion.getCommonData().getDopingBag().getDrinkItem();
		totalDopes += player.getInventory().getItemCountByItemId(itemId);
		int[] scrollBag = minion.getCommonData().getDopingBag().getScrollsUsed();
		for (int i = 0; i < scrollBag.length; ++i) {
			if (scrollBag[i] != 0) {
				totalDopes += player.getInventory().getItemCountByItemId(scrollBag[i]);
			}
		}
		if (totalDopes == 0) {
			minion.getCommonData().setIsBuffing(false);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, 3, itemId, slot));
		}
	}

	public static MinionService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final MinionService instance = new MinionService();
	}
}
