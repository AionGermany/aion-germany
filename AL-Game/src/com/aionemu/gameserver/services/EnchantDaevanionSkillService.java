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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.skill.PlayerSkillList;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DAEVANION_SKILL_ENCHANT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DAEVANION_SKILL_FUSION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class EnchantDaevanionSkillService {

	public static void enchantDaevanionSkill(final Player player, final int skillId, final int bookObjId, final int materials) {
		final Item parentItem = player.getInventory().getItemByObjId(bookObjId);
		ItemTemplate template = parentItem.getItemTemplate();
		final int nameId = template.getNameId();
		final PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		final boolean isSuccess = Rnd.chance((int) 75);
		final int currentEnchant = skill.getSkillLevel();
		if (player.getInventory().getKinah() < 100000L) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return;
		}
		final ItemUseObserver moveObserver = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(nameId)));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 2, 0), true);
			}
		};
		player.getObserveController().attach(moveObserver);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				int enchantLevel;
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(moveObserver);
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 1), true);
				if (!player.getInventory().decreaseByObjectId(bookObjId, 1)) {
					return;
				}
				player.getInventory().decreaseKinah(100000);
				if (materials != 0) {
					player.getInventory().decreaseByObjectId(materials, 1);
				}
				if (isSuccess) {
					enchantLevel = currentEnchant + 1;
					skill.setSkillLvl(enchantLevel);
					player.getSkillList().addSkill(player, skill.getSkillId(), enchantLevel);
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
					PacketSendUtility.sendPacket(player, new SM_DAEVANION_SKILL_ENCHANT(skillId, skill.getSkillLevel(), currentEnchant));
				} else {
					enchantLevel = currentEnchant - 1;
					skill.setSkillLvl(enchantLevel);
					player.getSkillList().addSkill(player, skill.getSkillId(), enchantLevel);
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
					PacketSendUtility.sendPacket(player, new SM_DAEVANION_SKILL_ENCHANT(skillId, skill.getSkillLevel(), currentEnchant));
				}
				if (currentEnchant >= 15) {
					SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), player.getLevel(), player.getRace());
					PlayerSkillList playerSkillList = player.getSkillList();
					for (SkillLearnTemplate template : skillTemplates) {
						if (template.getRequiredSkill() != skillId)
							continue;
						playerSkillList.addSkill(player, template.getSkillId(), 1);
					}
				}
			}
		}, 100));
	}

    public static void combineDaevanionBook(Player player, ArrayList<Integer> sacrificeBook) {
        for (int sacrifices : sacrificeBook) {
            player.getInventory().decreaseByObjectId(sacrifices, 1);
        }
        int result = 0;
        int chance = Rnd.get((int)0, (int)3);
        switch (player.getPlayerClass()) {
            case GLADIATOR: {
                if (chance == 0) {
                    result = Rnd.get((int)169501640, (int)169501645);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501784, (int)169501785);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501806, (int)169501807);
                }
                if (chance != 3) break;
                result = 169501773;
                break;
            }
            case TEMPLAR: {
                if (chance == 0) {
                    result = Rnd.get((int)169501646, (int)169501651);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501786, (int)169501787);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501808, (int)169501809);
                }
                if (chance != 3) break;
                result = 169501774;
                break;
            }
            case ASSASSIN: {
                if (chance == 0) {
                    result = Rnd.get((int)169501652, (int)169501657);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501788, (int)169501789);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501810, (int)169501811);
                }
                if (chance != 3) break;
                result = 169501775;
                break;
            }
            case RANGER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501658, (int)169501663);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501790, (int)169501791);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501812, (int)169501813);
                }
                if (chance != 3) break;
                result = 169501776;
                break;
            }
            case SORCERER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501676, (int)169501681);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501792, (int)169501793);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501818, (int)169501819);
                }
                if (chance != 3) break;
                result = 169501777;
                break;
            }
            case SPIRIT_MASTER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501682, (int)169501687);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501794, (int)169501795);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501820, (int)169501821);
                }
                if (chance != 3) break;
                result = 169501778;
                break;
            }
            case CLERIC: {
                if (chance == 0) {
                    result = Rnd.get((int)169501664, (int)169501669);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501796, (int)169501797);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501816, (int)169501817);
                }
                if (chance != 3) break;
                result = 169501779;
                break;
            }
            case CHANTER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501670, (int)169501675);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501798, (int)169501799);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501814, (int)169501815);
                }
                if (chance != 3) break;
                result = 169501780;
                break;
            }
            case GUNNER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501688, (int)169501693);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501800, (int)169501801);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501826, (int)169501827);
                }
                if (chance != 3) break;
                result = 169501781;
                break;
            }
            case BARD: {
                if (chance == 0) {
                    result = Rnd.get((int)169501700, (int)169501705);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501802, (int)169501803);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501822, (int)169501823);
                }
                if (chance != 3) break;
                result = 169501782;
                break;
            }
            case RIDER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501694, (int)169501699);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501804, (int)169501805);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501824, (int)169501825);
                }
                if (chance != 3) break;
                result = 169501783;
                break;
            }
            case PAINTER: {
                if (chance == 0) {
                    result = Rnd.get((int)169501872, (int)169501877);
                }
                if (chance == 1) {
                    result = Rnd.get((int)169501878, (int)169501879);
                }
                if (chance == 2) {
                    result = Rnd.get((int)169501880, (int)169501881);
                }
                if (chance != 3) break;
                result = 169501882;
            }
		default:
			break;
        }
        PacketSendUtility.sendPacket(player, new SM_DAEVANION_SKILL_FUSION(1, result));
        ItemService.addItem(player, result, 1);
    }
}
