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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.handlers.models.NpcInfos;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

import javolution.util.FastMap;

/**
 * @author Hilgert
 * @modified vlog
 */
public class ReportToMany extends QuestHandler {

	private final int startItem;
	private final Set<Integer> startNpcs = new HashSet<Integer>();
	private final Set<Integer> endNpcs = new HashSet<Integer>();
	private final int startDialog;
	private final int endDialog;
	private final int maxVar;
	private final FastMap<Integer, NpcInfos> npcInfos;
	private boolean mission;

	/**
	 * @param questId
	 * @param startItem
	 * @param endNpc
	 * @param startDialog
	 * @param endDialog
	 * @param maxVar
	 */
	public ReportToMany(int questId, int startItem, List<Integer> startNpcIds, List<Integer> endNpcIds, FastMap<Integer, NpcInfos> npcInfos, int startDialog, int endDialog, int maxVar, boolean mission) {
		super(questId);
		this.startItem = startItem;
		if (startNpcIds != null) {
			startNpcs.addAll(startNpcIds);
			startNpcs.remove(0);
		}
		if (endNpcIds != null) {
			endNpcs.addAll(endNpcIds);
			endNpcs.remove(0);
		}
		this.npcInfos = npcInfos;
		this.startDialog = startDialog;
		this.endDialog = endDialog;
		this.maxVar = maxVar;
		this.mission = mission;
	}

	@Override
	public void register() {
		if (mission) {
			qe.registerOnLevelUp(getQuestId());
		}
		if (startItem != 0) {
			qe.registerQuestItem(startItem, getQuestId());
		}
		else {
			Iterator<Integer> iterator = startNpcs.iterator();
			while (iterator.hasNext()) {
				int startNpc = iterator.next();
				qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
				qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
			}
		}
		for (int npcId : npcInfos.keySet()) {
			qe.registerQuestNpc(npcId).addOnTalkEvent(getQuestId());
		}
		Iterator<Integer> iterator = endNpcs.iterator();
		while (iterator.hasNext()) {
			int endNpc = iterator.next();
			qe.registerQuestNpc(endNpc).addOnTalkEvent(getQuestId());
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(getQuestId());
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (startItem != 0) {
				if (dialog == DialogAction.QUEST_ACCEPT_1) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
			if (startNpcs.isEmpty() || startNpcs.contains(targetId)) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (startDialog != 0) {
						return sendQuestDialog(env, startDialog);
					}
					else {
						return sendQuestDialog(env, 1011);
					}
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			NpcInfos targetNpcInfo = npcInfos.get(targetId);
			if (var <= maxVar) {
				if (targetNpcInfo != null && var == targetNpcInfo.getVar()) {
					int closeDialog;
					if (targetNpcInfo.getCloseDialog() == 0) {
						closeDialog = 10000 + targetNpcInfo.getVar();
					}
					else {
						closeDialog = targetNpcInfo.getCloseDialog();
					}

					if (dialog == DialogAction.QUEST_SELECT) {
						return sendQuestDialog(env, targetNpcInfo.getQuestDialog());
					}
					else if (dialog.id() == targetNpcInfo.getQuestDialog() + 1 && targetNpcInfo.getMovie() != 0) {
						sendQuestDialog(env, targetNpcInfo.getQuestDialog() + 1);
						return playQuestMovie(env, targetNpcInfo.getMovie());
					}
					else if (dialog.id() == closeDialog) {
						if ((dialog != DialogAction.CHECK_USER_HAS_QUEST_ITEM && dialog != DialogAction.CHECK_USER_HAS_QUEST_ITEM_SIMPLE) || QuestService.collectItemCheck(env, true)) {
							if (var == maxVar) {
								qs.setStatus(QuestStatus.REWARD);
								if (closeDialog == 1009 || closeDialog == 20002 || closeDialog == 34) {
									return sendQuestDialog(env, 5);
								}
							}
							else {
								qs.setQuestVarById(0, var + 1);
							}
							updateQuestStatus(env);
						}
						return sendQuestSelectionDialog(env);
					}
				}
			}
			else if (var > maxVar) {
				if (endNpcs.contains(targetId)) {
					switch(dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, endDialog);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env, true)) {
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
						}
						case SELECT_QUEST_REWARD: {
							if (startItem != 0) {
								if (!removeQuestItem(env, startItem, 1)) {
									return false;
								}
							}
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
					default:
						break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD && endNpcs.contains(targetId)) {
			NpcInfos targetNpcInfo = npcInfos.get(targetId);
			if (dialog == DialogAction.USE_OBJECT && targetNpcInfo != null && targetNpcInfo.getQuestDialog() != 0) {
				return sendQuestDialog(env, targetNpcInfo.getQuestDialog());
			}
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		if (startItem != 0) {
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(getQuestId());
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
			}
		}
		return HandlerResult.UNKNOWN;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv questEnv) {
		return defaultOnLvlUpEvent(questEnv);
	}
}
