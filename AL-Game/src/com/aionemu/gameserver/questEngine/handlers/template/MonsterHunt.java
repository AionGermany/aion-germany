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
package com.aionemu.gameserver.questEngine.handlers.template;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.handlers.models.Monster;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.services.VortexService;

import javolution.util.FastMap;

/**
 * @author MrPoke
 * @reworked vlog, Bobobear
 */
public class MonsterHunt extends QuestHandler {

	private final int questId;
	private final Set<Integer> startNpcs = new HashSet<Integer>();
	private final Set<Integer> endNpcs = new HashSet<Integer>();
	private final FastMap<Monster, Set<Integer>> monsters;
	private final int startDialog;
	private final int endDialog;
	private final Set<Integer> aggroNpcs = new HashSet<Integer>();
	private final int invasionWorldId;

	public MonsterHunt(int questId, List<Integer> startNpcIds, List<Integer> endNpcIds, FastMap<Monster, Set<Integer>> monsters, int startDialog, int endDialog, List<Integer> aggroNpcs, int invasionWorld) {
		super(questId);
		this.questId = questId;
		this.startNpcs.addAll(startNpcIds);
		this.startNpcs.remove(0);
		if (endNpcIds == null) {
			this.endNpcs.addAll(startNpcs);
		}
		else {
			this.endNpcs.addAll(endNpcIds);
			this.endNpcs.remove(0);
		}
		this.monsters = monsters;
		this.startDialog = startDialog;
		this.endDialog = endDialog;
		if (aggroNpcs != null) {
			this.aggroNpcs.addAll(aggroNpcs);
			this.aggroNpcs.remove(0);
		}
		this.invasionWorldId = invasionWorld;
	}

	@Override
	public void register() {
		for (Integer startNpc : startNpcs) {
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}

		for (Set<Integer> monsterIds : monsters.values()) {
			for (Integer monsterId : monsterIds) {
				qe.registerQuestNpc(monsterId).addOnKillEvent(questId);
			}
		}

		for (Integer endNpc : endNpcs) {
			qe.registerQuestNpc(endNpc).addOnTalkEvent(getQuestId());
		}

		for (Integer aggroNpc : aggroNpcs) {
			qe.registerQuestNpc(aggroNpc).addOnAddAggroListEvent(getQuestId());
		}

		if (invasionWorldId != 0) {
			qe.registerOnEnterWorld(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (startNpcs.isEmpty() || startNpcs.contains(targetId)) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, startDialog != 0 ? startDialog : 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			for (Monster mi : monsters.keySet()) {
				int endVar = mi.getEndVar();
				int varId = mi.getVar();
				int total = 0;
				do {
					int currentVar = qs.getQuestVarById(varId);
					total += currentVar << ((varId - mi.getVar()) * 6);
					endVar >>= 6;
					varId++;
				}
				while (endVar > 0);
				if (mi.getEndVar() > total) {
					return false;
				}
			}
			if (endNpcs.contains(targetId)) {
				if (endDialog != 0) { // Workaround for Quest's with select_success for this Quests you have to add end_dialog_id="10002" after end_npc_ids="" (only for Monster Hunt)
					switch (dialog) {
						case USE_OBJECT: {
							return sendQuestDialog(env, endDialog);
						}
						case SELECT_QUEST_REWARD: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						default:
							break;
					}
				}
				else {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SELECT_QUEST_REWARD: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						default:
							break;
					}

				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (endNpcs.contains(targetId)) {
				if (!aggroNpcs.isEmpty()) {
					return sendQuestStartDialog(env);
				} else {
					if (endDialog != 0) {
						switch (dialog) {
							case USE_OBJECT:
								return sendQuestDialog(env, 10002);
							case SELECT_QUEST_REWARD:
								return sendQuestDialog(env, 5);
							default:
								return sendQuestEndDialog(env);
						}
					} else {
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			for (Monster m : monsters.keySet()) {
				if (m.getNpcIds().contains(env.getTargetId())) {
					int endVar = m.getEndVar();
					int varId = m.getVar();
					int total = 0;
					do {
						int currentVar = qs.getQuestVarById(varId);
						total += currentVar << ((varId - m.getVar()) * 6);
						endVar >>= 6;
						varId++;
					}
					while (endVar > 0);
					total += 1;
					if (total <= m.getEndVar()) {
						if (!aggroNpcs.isEmpty()) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
						}
						else {
							for (int varsUsed = m.getVar(); varsUsed < varId; varsUsed++) {
								int value = total & 0x3F;
								total >>= 6;
								qs.setQuestVarById(varsUsed, value);
							}
							int var = qs.getQuestVarById(0);
							int var1 = qs.getQuestVarById(1);
							if (var == 0 && m.getVar() == 1 && varId == 2 && var1 == m.getEndVar()) {
								qs.setQuestVarById(0, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
							}
							else {
								updateQuestStatus(env);
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onAddAggroListEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		VortexLocation vortexLoc = VortexService.getInstance().getLocationByWorld(invasionWorldId);
		if (player.getWorldId() == invasionWorldId) {
			if ((qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat())) {
				if ((vortexLoc != null && vortexLoc.isActive()) || (searchOpenRift())) {
					return QuestService.startQuest(env);
				}
			}
		}
		return false;
	}

	private boolean searchOpenRift() {
		for (RiftLocation loc : RiftService.getInstance().getRiftLocations().values()) {
			if (loc.getWorldId() == invasionWorldId && loc.isOpened()) {
				return true;
			}
		}
		return false;
	}
}
