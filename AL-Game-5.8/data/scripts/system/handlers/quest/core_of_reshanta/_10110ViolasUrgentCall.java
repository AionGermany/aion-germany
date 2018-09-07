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
package quest.core_of_reshanta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author FrozenKiller (TODO Spawn missing mobs and Npc's)
 */

public class _10110ViolasUrgentCall extends QuestHandler {

	private final static int questId = 10110;
	int[] mobs = {885140, 885141};

	public _10110ViolasUrgentCall() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(806075).addOnQuestStart(questId);
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); //Weda
		qe.registerQuestNpc(805351).addOnTalkEvent(questId); //Jiskur
		qe.registerQuestNpc(703463).addOnTalkEvent(questId); //Burning Brimstone
		qe.registerQuestNpc(703464).addOnTalkEvent(questId); //Lava
		qe.registerOnEnterWorld(questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (player.getLevel() >= 66 && player.getWorldId() == 210100000) {
				QuestService.startQuest(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return false;
		} 
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch(targetId) {
				case 806075: //Weda
					switch(dialog) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else if (var == 1){
								return sendQuestDialog(env, 1352);
							}
						case SETPRO1:
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						case CHECK_USER_HAS_QUEST_ITEM: 
								return checkQuestItems(env, 1, 2, false, 10000, 10001); // 2
						case SETPRO3:
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						default:
							return sendQuestStartDialog(env);
					}
				case 703463:
					switch(dialog) {
						case USE_OBJECT:
							if (var == 1) {
								return true;
							} else {
								return false;
							}
						default:
							break;
					}
				case 703464:
					switch(dialog) {
						case USE_OBJECT:
							if (var == 1) {
								return true;
							} else {
								return false;
							}
						default:
							break;
					}
				case 805351: //Jiskur
					switch(dialog) {
						case QUEST_SELECT:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							} else if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						case SETPRO4:
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						case SET_SUCCEED:
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						default:
							sendQuestStartDialog(env);
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805351) { //Jiskur
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
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
			int var = qs.getQuestVarById(0);
			if (var == 4) {
				return defaultOnKillEvent(env, mobs, var, var + 1, 0);
			}
		}
		return false;
	}
}