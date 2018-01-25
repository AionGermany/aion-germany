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
package com.aionemu.gameserver.services.player.CreativityPanel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HighDaevaConfig;
import com.aionemu.gameserver.dao.PlayerCreativityPointsDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.cp.PlayerCPEntry;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.panel_cp.PanelCp;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS_APPLY;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Agility;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Health;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Knowledge;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Power;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Precision;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Will;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CreativityEssenceService {

	Logger log = LoggerFactory.getLogger(CreativityEssenceService.class);
	PlayerCreativityPointsDAO cpDAO = DAOManager.getDAO(PlayerCreativityPointsDAO.class);
	private int point;
	public static int currentCp;
	public static int estimaCp;
	public static List<Item> estima;

	public void onLogin(Player player) {
		if (player.isHighDaeva()) {
			int totalPoint = player.getCreativityPoint();
			int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
			for (PlayerCPEntry ce : player.getCP().getAllCP()) {
				if (ce.getSlot() <= 6 && ce.getPoint() >= 255) {
					player.getCP().addPoint(player, ce.getSlot(), 0);
				}
				if (ce.getSlot() >= 7 && ce.getSlot() <= 14 || ce.getSlot() >= 401 && ce.getSlot() <= 408) {
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(0, size, ce.getSlot(), ce.getPoint()));
				}
				if (ce.getSlot() >= 15 && ce.getSlot() <= 400 || ce.getSlot() >= 409 && ce.getSlot() <= 456) {
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(0, size, ce.getSlot(), ce.getPoint()));
				}
			}
			PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, player.getCPStep(), size, true));
		}
	}

	/**
	 * http://aionpowerbook.com/powerbook/Creativity
	 */
	public void pointPerExp(Player player) {
		float current = player.getCommonData().getExpShown();
		float total = player.getCommonData().getExpNeed();
		float percent = getPercentage(current, total);
		int step = player.getCPStep();
		if (player.isHighDaeva() && player.getCreativityPoint() < HighDaevaConfig.CP_LIMIT_MAX) {
			if (percent >= 16.66f && percent < 33.33f) {
				if (step == 1) {
					player.setCPStep(step + 1);
					point = player.getCreativityPoint() + 1;
					player.setCreativityPoint(point);
					int totalPoint = player.getCreativityPoint();
					int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, 2, size, false));
					PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					// You have gained Essence.
					// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
					// Add Essence to open the Allocate Essence window.Open Allocate Essence.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK, 0);
					// Essence has increased by 1.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP, 15000);
				}
			}
			else if (percent >= 33.33f && percent < 50.00f) {
				if (step == 2) {
					player.setCPStep(step + 1);
					point = player.getCreativityPoint() + 1;
					player.setCreativityPoint(point);
					int totalPoint = player.getCreativityPoint();
					int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, 3, size, false));
					PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					// You have gained Essence.
					// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
					// Add Essence to open the Allocate Essence window.Open Allocate Essence.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK, 0);
					// Essence has increased by 1.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP, 15000);
				}
			}
			else if (percent >= 50.00f && percent < 66.66f) {
				if (step == 3) {
					player.setCPStep(step + 1);
					point = player.getCreativityPoint() + 1;
					player.setCreativityPoint(point);
					int totalPoint = player.getCreativityPoint();
					int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, 4, size, false));
					PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					// You have gained Essence.
					// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
					// Add Essence to open the Allocate Essence window.Open Allocate Essence.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK, 0);
					// Essence has increased by 1.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP, 15000);
				}
			}
			else if (percent >= 66.66f && percent < 83.33f) {
				if (step == 4) {
					player.setCPStep(step + 1);
					point = player.getCreativityPoint() + 1;
					player.setCreativityPoint(point);
					int totalPoint = player.getCreativityPoint();
					int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, 5, size, false));
					PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					// You have gained Essence.
					// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
					// Add Essence to open the Allocate Essence window.Open Allocate Essence.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK, 0);
					// Essence has increased by 1.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP, 15000);
				}
			}
			else if (percent >= 83.33f && percent < 100.00f) {
				if (step == 5) {
					player.setCPStep(step + 1);
					point = player.getCreativityPoint() + 1;
					player.setCreativityPoint(point);
					int totalPoint = player.getCreativityPoint();
					int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalPoint, 6, size, false));
					PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					// You have gained Essence.
					// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
					// Add Essence to open the Allocate Essence window.Open Allocate Essence.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK, 0);
					// Essence has increased by 1.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP, 15000);
				}
			}
		}
	}

	/**
	 * KR - Update December 30th 2015 http://aionpowerbook.com/powerbook/KR_-_Update_December_30th_2015 - The amount of Creativity a "ArchDaeva" can acquire with each level has been increased. - If
	 * you have already leveled up the difference will be paid the next time you reach the Creativity point. - For a certain amount of time reseting Creativity will only require 1 Kinah, the price
	 * will not increase with each reset.
	 */
	public void pointPerLevel(Player player) {
		if (player.isHighDaeva()) {
			// player.setCPStep(1);
			if (player.getLevel() == 66) {
				point = player.getCreativityPoint() + 1;
			}
			else if (player.getLevel() == 67) {
				point = player.getCreativityPoint() + 9;
			}
			else if (player.getLevel() == 68) {
				point = player.getCreativityPoint() + 9;
			}
			else if (player.getLevel() == 69) {
				point = player.getCreativityPoint() + 11;
			}
			else if (player.getLevel() == 70) {
				point = player.getCreativityPoint() + 12;
			}
			else if (player.getLevel() == 71) {
				point = player.getCreativityPoint() + 13;
			}
			else if (player.getLevel() == 72) {
				point = player.getCreativityPoint() + 15;
			}
			else if (player.getLevel() == 73) {
				point = player.getCreativityPoint() + 17;
			}
			else if (player.getLevel() == 74) {
				point = player.getCreativityPoint() + 20;
			}
			else if (player.getLevel() == 75) {
				point = player.getCreativityPoint() + 23;
			}
			else if (player.getLevel() == 76) {
				point = player.getCreativityPoint() + 25;
			}
			else if (player.getLevel() == 77) {
				point = player.getCreativityPoint() + 35;
			}
			else if (player.getLevel() == 78) {
				point = player.getCreativityPoint() + 41;
			}
			else if (player.getLevel() == 79) {
				point = player.getCreativityPoint() + 48;
			}
			else if (player.getLevel() == 80) {
				point = player.getCreativityPoint() + 53;
			}
			else if (player.getLevel() == 81) {
				point = player.getCreativityPoint() + 49;
			}
			else if (player.getLevel() == 82) {
				point = player.getCreativityPoint() + 65;
			}
			else if (player.getLevel() == 83) {
				point = player.getCreativityPoint() + 71;
			}

			if (player.getCPStep() < 6) // to make sure all steps are granted
				point = point + 6 - player.getCPStep();

			player.setCPStep(1);
			if (point > HighDaevaConfig.CP_LIMIT_MAX)
				point = HighDaevaConfig.CP_LIMIT_MAX;

			player.setCreativityPoint(point);
			int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(point, player.getCPStep()));
			PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(player.getCreativityPoint(), player.getCPStep(), size, false));
			int vesselCount = 0;
			int avatarCount = 0;
			for (PlayerCPEntry ce : player.getCP().getAllCP()) {
				if (ce.getSlot() >= 7 && ce.getSlot() <= 14) {
					vesselCount++;
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(0, vesselCount, ce.getSlot(), ce.getPoint()));
				}
				else if (ce.getSlot() >= 401 && ce.getSlot() <= 408) {
					avatarCount++;
					PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(0, avatarCount, ce.getSlot(), ce.getPoint()));
				}
			}
			player.getCommonData().addGrowthEnergy(1060000 * 10);
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			// You have gained Essence.
			// Click the Essence icon displayed on the Character XP meter, SHIFT+U, or select Start Menu.
			// Add Essence to open the Allocate Essence window.Open Allocate Essence.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_CP_LINK);
			DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		}
	}

	private float getPercentage(float current, float total) {
		float percentCounter;
		percentCounter = (current * 100.0f) / total;
		percentCounter = percentCounter * 100;
		percentCounter = Math.round(percentCounter);
		percentCounter = percentCounter / 100;
		return percentCounter;
	}

	public void onEssenceApply(Player player, int type, int size, int id, int point) {
		if (player.isHighDaeva()) {
			player.getCP().addPoint(player, id, point);
			switch (id) {
				case 1:
					player.setCPSlot1(point);
					Power.getInstance().onChange(player, point);
					break;
				case 2:
					player.setCPSlot2(point);
					Health.getInstance().onChange(player, point);
					break;
				case 3:
					player.setCPSlot3(point);
					Agility.getInstance().onChange(player, point);
					break;
				case 4:
					player.setCPSlot4(point);
					Precision.getInstance().onChange(player, point);
					break;
				case 5:
					player.setCPSlot5(point);
					Knowledge.getInstance().onChange(player, point);
					break;
				case 6:
					player.setCPSlot6(point);
					Will.getInstance().onChange(player, point);
					break;
			}
			PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(type, size, id, point));
		}
	}

	public void onResetEssence(Player player, int plusSize) {
		for (PlayerCPEntry ce : player.getCP().getAllCP()) {
			PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(ce.getSlot());
			player.getCP().removePoint(player, ce.getSlot());
			if (pcp.getSkillId() > 0) {
				SkillLearnService.removeSkill(player, pcp.getSkillId());
			} else {
				SkillLearnService.removeSkill(player, pcp.getLearnSkill());
			}
		}
		player.setCPSlot1(0);
		player.setCPSlot2(0);
		player.setCPSlot3(0);
		player.setCPSlot4(0);
		player.setCPSlot5(0);
		player.setCPSlot6(0);
		Power.getInstance().onChange(player, 0);
		Health.getInstance().onChange(player, 0);
		Agility.getInstance().onChange(player, 0);
		Precision.getInstance().onChange(player, 0);
		Knowledge.getInstance().onChange(player, 0);
		Will.getInstance().onChange(player, 0);
		List<Integer> a = new ArrayList<Integer>();
		a.add(8);
		a.add(10);
		a.add(12);
		a.add(14);
		a.add(402);
		a.add(404);
		a.add(406);
		a.add(408);
		for (int i = 0; i < a.size(); i++) {
			onSkillsApply(player, 0, 0, a.get(i), 0);
		}
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(player.getCreativityPoint(), player.getCPStep(), 0, false));
	}

	public void onSkillsApply(Player player, int type, int size, int id, int point) {
		if (id >= 7 && id <= 14 || id >= 401 && id <= 408) {
			archDaevaSkills(player, id, point);
		}
		if (id >= 15 && id <= 400) {
			classSkills(player, id, point);

		}
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(type, size, id, point));
	}

	public void classSkills(Player player, int id, int point) {
		PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point >= 1) {
			player.getSkillList().addSkill(player, pcp.getSkillId(), point + 1);
			player.getCP().addPoint(player, id, point);
		}
		else {
			player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
		}
	}

	public void archDaevaSkills(Player player, int id, int point) {
		if (point >= 5) {
			switch (id) {
				case 8:
					player.getSkillList().addSkill(player, 4696, 1); // Transformation: Vessel Of Wind.
					player.getSkillList().addSkill(player, 4697, 1); // Mercurial Blast.
					player.getCP().addPoint(player, 7, 1);
					break;
				case 10:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
						player.getSkillList().addSkill(player, 4701, 1); // Detonate (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
						player.getSkillList().addSkill(player, 4704, 1); // Detonate (Asmodians)
					}
					player.getCP().addPoint(player, 9, 1);
					break;
				case 12:
					player.getSkillList().addSkill(player, 4702, 1); // Transformation: Vessel Of Water.
					player.getSkillList().addSkill(player, 4703, 1); // Waterbind.
					player.getCP().addPoint(player, 11, 1);
					break;
				case 14:
					player.getSkillList().addSkill(player, 4698, 1); // Transformation: Vessel Of Earth.
					player.getSkillList().addSkill(player, 4699, 1); // Terraform.
					player.getCP().addPoint(player, 13, 1);
					break;
				// Ver: 5.1
				case 402:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4768, 1); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4807, 1); // Transformation: Avatar Of Wind (Asmodians)
					}
					player.getCP().addPoint(player, 401, 1);
					break;
				case 404:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4752, 1); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4804, 1); // Transformation: Avatar Of Fire (Asmodians)
					}
					player.getCP().addPoint(player, 403, 1);
					break;
				case 406:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4757, 1); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4805, 1); // Transformation: Avatar Of Water (Asmodians)
					}
					player.getCP().addPoint(player, 405, 1);
					break;
				case 408:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4762, 1); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4806, 1); // Transformation: Avatar Of Earth (Asmodians)
					}
					player.getCP().addPoint(player, 407, 1);
					break;
			}
			player.getCP().addPoint(player, id, point);
		}
		else if (point == 0) {
			switch (id) {
				case 8:
					SkillLearnService.removeSkill(player, 4696); // Transformation: Vessel Of Wind.
					SkillLearnService.removeSkill(player, 4697); // Mercurial Blast.
					player.getCP().removePoint(player, 7);
					break;
				case 10:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4700); // Transformation: Vessel Of Fire.
						SkillLearnService.removeSkill(player, 4701); // Detonate (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4700); // Transformation: Vessel Of Fire.
						SkillLearnService.removeSkill(player, 4704); // Detonate (Asmodians)
					}
					player.getCP().removePoint(player, 9);
					break;
				case 12:
					SkillLearnService.removeSkill(player, 4702); // Transformation: Vessel Of Water.
					SkillLearnService.removeSkill(player, 4703); // Waterbind.
					player.getCP().removePoint(player, 11);
					break;
				case 14:
					SkillLearnService.removeSkill(player, 4698); // Transformation: Vessel Of Earth.
					SkillLearnService.removeSkill(player, 4699); // Terraform.
					player.getCP().removePoint(player, 13);
					break;
				// Ver: 5.1
				case 402:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4768); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4807); // Transformation: Avatar Of Wind (Asmodians)
					}
					player.getCP().removePoint(player, 401);
					break;
				case 404:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4752); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4804); // Transformation: Avatar Of Fire (Asmodians)
					}
					player.getCP().removePoint(player, 403);
					break;
				case 406:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4757); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4805); // Transformation: Avatar Of Water (Asmodians)
					}
					player.getCP().removePoint(player, 405);
					break;
				case 408:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4762); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4806); // Transformation: Avatar Of Earth (Asmodians)
					}
					player.getCP().removePoint(player, 407);
					break;
			}
			player.getCP().removePoint(player, id);
		}
	}
	
	public void addEstimaCp(Player player, int objId) {
		estimaCp = 0;
		Item addEstima = player.getEquipment().getEquippedItemByObjId(objId);
		if (addEstima != null) {
			switch (addEstima.getEnchantLevel()) {
				case 6:
					estimaCp = 8;
					break;
				case 7:
					estimaCp = 10;
					break;
				case 8:
					estimaCp = 12;
					break;
				case 9:
					estimaCp = 14;
					break;
				case 10:
					estimaCp = 17;
					break;
				default:
					estimaCp = (addEstima.getEnchantLevel() + 1);
					break;
			}
		}
		currentCp = player.getCreativityPoint();
		int totalCp = (currentCp + estimaCp);
		player.setCreativityPoint(totalCp);
		int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalCp, player.getCPStep()));
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(player.getCreativityPoint(), player.getCPStep(), size, false));
	}
	
	public void removeEstimaCp(Player player, int objId) {
		estimaCp = 0;
		Item removedEstima = player.getInventory().getItemByObjId(objId);
		if (removedEstima != null) {
			switch (removedEstima.getEnchantLevel()) {
				case 6:
					estimaCp = 8;
					break;
				case 7:
					estimaCp = 10;
					break;
				case 8:
					estimaCp = 12;
					break;
				case 9:
					estimaCp = 14;
					break;
				case 10:
					estimaCp = 17;
					break;
				default:
					estimaCp = (removedEstima.getEnchantLevel() + 1);
					break;
			}
		}
		currentCp = player.getCreativityPoint();
		int totalCp = (currentCp - estimaCp);
		player.setCreativityPoint(totalCp);
		int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(totalCp, player.getCPStep()));
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(player.getCreativityPoint(), player.getCPStep(), size, false));
	}

	public static CreativityEssenceService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final CreativityEssenceService INSTANCE = new CreativityEssenceService();
	}
}
