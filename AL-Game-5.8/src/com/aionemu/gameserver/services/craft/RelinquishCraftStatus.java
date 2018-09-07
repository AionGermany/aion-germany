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
package com.aionemu.gameserver.services.craft;

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.craft.ExpertQuestsList;
import com.aionemu.gameserver.model.craft.MasterQuestsList;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.templates.CraftLearnTemplate;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author synchro2
 */
public class RelinquishCraftStatus {

	private static final int expertMinValue = 399;
	private static final int expertMaxValue = 499;
	private static final int masterMinValue = 499;
	private static final int masterMaxValue = 549;
	private static final int expertPrice = 120895;
	private static final int masterPrice = 3497448;
	private static final int systemMessageId = 1300388;
	private static final int skillMessageId = 1401127;

	public static final RelinquishCraftStatus getInstance() {
		return SingletonHolder.instance;
	}

	public static void relinquishExpertStatus(Player player, Npc npc) {
		CraftLearnTemplate craftLearnTemplate = CraftSkillUpdateService.npcBySkill.get(npc.getNpcId());
		final int skillId = craftLearnTemplate.getSkillId();
		PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		if (!canRelinquishCraftStatus(player, skill, craftLearnTemplate, expertMinValue, expertMaxValue)) {
			return;
		}
		if (!successDecreaseKinah(player, expertPrice)) {
			return;
		}
		skill.setSkillLvl(expertMinValue);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
		removeRecipesAbove(player, skillId, expertMinValue);
		deleteCraftStatusQuests(skillId, player, true);
	}

	public static void relinquishMasterStatus(Player player, Npc npc) {
		CraftLearnTemplate craftLearnTemplate = CraftSkillUpdateService.npcBySkill.get(npc.getNpcId());
		final int skillId = craftLearnTemplate.getSkillId();
		PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		if (!canRelinquishCraftStatus(player, skill, craftLearnTemplate, masterMinValue, masterMaxValue)) {
			return;
		}
		if (!successDecreaseKinah(player, masterPrice)) {
			return;
		}
		skill.setSkillLvl(masterMinValue);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
		removeRecipesAbove(player, skillId, masterMinValue);
		deleteCraftStatusQuests(skillId, player, false);
	}

	private static boolean canRelinquishCraftStatus(Player player, PlayerSkillEntry skill, CraftLearnTemplate craftLearnTemplate, int minValue, int maxValue) {
		if (craftLearnTemplate == null || !craftLearnTemplate.isCraftSkill()) {
			return false;
		}
		return !(skill == null || skill.getSkillLevel() < minValue || skill.getSkillLevel() > maxValue);
	}

	private static boolean successDecreaseKinah(Player player, int basePrice) {
		if (!player.getInventory().tryDecreaseKinah(PricesService.getPriceForService(basePrice, player.getRace()))) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(systemMessageId));
			return false;
		}
		return true;
	}

	public static void removeRecipesAbove(Player player, int skillId, int level) {
		for (RecipeTemplate recipe : DataManager.RECIPE_DATA.getRecipeTemplates().valueCollection()) {
			if (recipe.getSkillid() != skillId || recipe.getSkillpoint() < level) {
				continue;
			}
			player.getRecipeList().deleteRecipe(player, recipe.getId());
		}
	}

	public static void deleteCraftStatusQuests(int skillId, Player player, boolean isExpert) {
		for (int questId : MasterQuestsList.getSkillsIds(skillId, player.getRace())) {
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null) {
				qs.setQuestVar(0);
				qs.setCompleteCount(0);
				qs.setStatus(null);
				qs.setPersistentState(PersistentState.DELETED);
			}
		}
		if (isExpert) {
			for (int questId : ExpertQuestsList.getSkillsIds(skillId, player.getRace())) {
				final QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs != null) {
					qs.setQuestVar(0);
					qs.setCompleteCount(0);
					qs.setStatus(null);
					qs.setPersistentState(PersistentState.DELETED);
				}
			}
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_COMPLETED_LIST(player.getQuestStateList().getAllFinishedQuests()));
		player.getController().updateNearbyQuests();
	}

	public static void removeExcessCraftStatus(Player player, boolean isExpert) {
		int minValue = isExpert ? expertMinValue : masterMinValue;
		int maxValue = isExpert ? expertMaxValue : masterMaxValue;
		int skillId;
		int skillLevel;
		int maxCraftStatus = isExpert ? CraftConfig.MAX_EXPERT_CRAFTING_SKILLS : CraftConfig.MAX_MASTER_CRAFTING_SKILLS;
		int countCraftStatus;
		for (PlayerSkillEntry skill : player.getSkillList().getBasicSkills()) {
			countCraftStatus = isExpert ? CraftSkillUpdateService.getTotalMasterCraftingSkills(player) + CraftSkillUpdateService.getTotalExpertCraftingSkills(player) : CraftSkillUpdateService.getTotalMasterCraftingSkills(player);
			if (countCraftStatus > maxCraftStatus) {
				skillId = skill.getSkillId();
				skillLevel = skill.getSkillLevel();
				if (CraftSkillUpdateService.isCraftingSkill(skillId) && skillLevel > minValue && skillLevel <= maxValue) {
					skill.setSkillLvl(minValue);
					PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
					removeRecipesAbove(player, skillId, minValue);
					deleteCraftStatusQuests(skillId, player, isExpert);
				}
				continue;
			}
			break;
		}
		if (!isExpert) {
			removeExcessCraftStatus(player, true);
		}
	}

	public static int getExpertMinValue() {
		return expertMinValue;
	}

	public static int getExpertMaxValue() {
		return expertMaxValue;
	}

	public static int getMasterMinValue() {
		return masterMinValue;
	}

	public static int getMasterMaxValue() {
		return masterMaxValue;
	}

	public static int getSkillMessageId() {
		return skillMessageId;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final RelinquishCraftStatus instance = new RelinquishCraftStatus();
	}
}
