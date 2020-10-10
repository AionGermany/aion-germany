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
package quest.ishalgen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Falke_34
 * @author FrozenKiller
 */
public class _70000MuninsReputation extends QuestHandler {

	private final static int questId = 70000;

	public _70000MuninsReputation() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806810).addOnTalkEvent(questId); // Old Friend Cheska
		qe.registerQuestNpc(203550).addOnTalkEvent(questId); // Munin
        qe.registerOnEnterWorld(questId);
        qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env);
	}

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            env.setQuestId(questId);
            QuestService.startQuest(env);
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }

        int targetId = env.getTargetId();
        DialogAction action = env.getDialog();

        if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
            if (targetId == 806810) {
                switch (action) {
                    case QUEST_SELECT:
                        return sendQuestDialog(env, 1011);
                    case SELECT_ACTION_1012:
                        return sendQuestDialog(env, 1012);
                    case SET_SUCCEED:
                        qs.setQuestVar(1);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return closeDialogWindow(env);
				default:
					break;
                }
            }
        } else if ( qs.getStatus() == QuestStatus.REWARD ) {
            if (targetId == 203550) {
                switch (action) {
                    case USE_OBJECT:
                        return sendQuestDialog(env, 10002);
                    case SELECT_QUEST_REWARD:
                        return sendQuestDialog(env, 5);
                    case SELECTED_QUEST_NOREWARD:
                        return sendQuestEndDialog(env);
				default:
					break;
                }
            }

        }
        return false;
    }


}
