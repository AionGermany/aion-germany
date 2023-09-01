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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS_APPLY;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CreativityTransfoService {

	public void onTransfoApply(Player player, int type, int size, int id, int point) {
		if (id >= 7 && id <= 14 || id >= 401 && id <= 408) {
			learnTransfo(player, id, point);
		}
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(type, size, id, point));
	}

	public void learnTransfo(Player player, int id, int point) {
		if (point >= 1) {
			switch (id) {
				case 7:
					player.getSkillList().addSkill(player, 4696, 1); // Transformation: Vessel Of Wind.
					player.getSkillList().addSkill(player, 4697, 1); // Mercurial Blast.
					break;
				case 9:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
						player.getSkillList().addSkill(player, 4701, 1); // Detonate (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
						player.getSkillList().addSkill(player, 4704, 1); // Detonate (Asmodians)
					}
					break;
				case 11:
					player.getSkillList().addSkill(player, 4702, 1); // Transformation: Vessel Of Water.
					player.getSkillList().addSkill(player, 4703, 1); // Waterbind.
					break;
				case 13:
					player.getSkillList().addSkill(player, 4698, 1); // Transformation: Vessel Of Earth.
					player.getSkillList().addSkill(player, 4699, 1); // Terraform.
					break;
				case 401:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4768, 1); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4807, 1); // Transformation: Avatar Of Wind (Asmodians)
					}
					break;
				case 403:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4752, 1); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4804, 1); // Transformation: Avatar Of Fire (Asmodians)
					}
					break;
				case 405:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4757, 1); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4805, 1); // Transformation: Avatar Of Water (Asmodians)
					}
					break;
				case 407:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4762, 1); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4806, 1); // Transformation: Avatar Of Earth (Asmodians)
					}
					break;
				case 8:
					player.getSkillList().addSkill(player, 4696, point + 1); // Transformation: Vessel Of Wind.
					break;
				case 10:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4700, point + 1); // Transformation: Vessel Of Fire.
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4700, point + 1); // Transformation: Vessel Of Fire.
					}
					break;
				case 12:
					player.getSkillList().addSkill(player, 4702, point + 1); // Transformation: Vessel Of Water.
					break;
				case 14:
					player.getSkillList().addSkill(player, 4698, point + 1); // Transformation: Vessel Of Earth.
					break;
				case 402:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4768, point + 1); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4807, point + 1); // Transformation: Avatar Of Wind (Asmodians)
					}
					break;
				case 404:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4752, point + 1); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4804, point + 1); // Transformation: Avatar Of Fire (Asmodians)
					}
					break;
				case 406:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4757, point + 1); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4805, point + 1); // Transformation: Avatar Of Water (Asmodians)
					}
					break;
				case 408:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4762, point + 1); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4806, point + 1); // Transformation: Avatar Of Earth (Asmodians)
					}
					break;
			}
			player.getCP().addPoint(player, id, point);
		}
		else if (point == 0) {
			switch (id) {
				case 7:
					SkillLearnService.removeSkill(player, 4696); // Transformation: Vessel Of Wind.
					SkillLearnService.removeSkill(player, 4697); // Mercurial Blast.
					break;
				case 9:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4700); // Transformation: Vessel Of Fire.
						SkillLearnService.removeSkill(player, 4701); // Detonate (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4700); // Transformation: Vessel Of Fire.
						SkillLearnService.removeSkill(player, 4704); // Detonate (Asmodians)
					}
					break;
				case 11:
					SkillLearnService.removeSkill(player, 4702); // Transformation: Vessel Of Water.
					SkillLearnService.removeSkill(player, 4703); // Waterbind.
					break;
				case 13:
					SkillLearnService.removeSkill(player, 4698); // Transformation: Vessel Of Earth.
					SkillLearnService.removeSkill(player, 4699); // Terraform.
					break;
				case 401:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4768); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4807); // Transformation: Avatar Of Wind (Asmodians)
					}
					break;
				case 403:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4752); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4804); // Transformation: Avatar Of Fire (Asmodians)
					}
					break;
				case 405:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4757); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4805); // Transformation: Avatar Of Water (Asmodians)
					}
					break;
				case 407:
					if (player.getRace() == Race.ELYOS) {
						SkillLearnService.removeSkill(player, 4762); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						SkillLearnService.removeSkill(player, 4806); // Transformation: Avatar Of Earth (Asmodians)
					}
					break;
				case 8:
					player.getSkillList().addSkill(player, 4696, 1); // Transformation: Vessel Of Wind.
					break;
				case 10:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4700, 1); // Transformation: Vessel Of Fire.
					}
					break;
				case 12:
					player.getSkillList().addSkill(player, 4702, 1); // Transformation: Vessel Of Water.
					break;
				case 14:
					player.getSkillList().addSkill(player, 4698, 1); // Transformation: Vessel Of Earth.
					break;
				case 402:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4768, 1); // Transformation: Avatar Of Wind (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4807, 1); // Transformation: Avatar Of Wind (Asmodians)
					}
					break;
				case 404:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4752, 1); // Transformation: Avatar Of Fire (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4804, 1); // Transformation: Avatar Of Fire (Asmodians)
					}
					break;
				case 406:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4757, 1); // Transformation: Avatar Of Water (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4805, 1); // Transformation: Avatar Of Water (Asmodians)
					}
					break;
				case 408:
					if (player.getRace() == Race.ELYOS) {
						player.getSkillList().addSkill(player, 4762, 1); // Transformation: Avatar Of Earth (Elyos)
					}
					else if (player.getRace() == Race.ASMODIANS) {
						player.getSkillList().addSkill(player, 4806, 1); // Transformation: Avatar Of Earth (Asmodians)
					}
					break;
			}
			player.getCP().removePoint(player, id);
		}
	}

	public void enchantTransfo(Player player, int id, int point) {
		if (point >= 1) {
			player.getCP().addPoint(player, id, point);
		}
		else if (point == 0) {
			player.getCP().removePoint(player, id);
		}
	}

	public static CreativityTransfoService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final CreativityTransfoService INSTANCE = new CreativityTransfoService();
	}
}
