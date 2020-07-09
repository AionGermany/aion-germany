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
package quest.haramel;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 * @rework FrozenKiller
 */
public class _61101InstanceSmugglerMoorilerksStatement extends QuestHandler {

	private final static int questId = 61101;

	public _61101InstanceSmugglerMoorilerksStatement() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799522).addOnQuestStart(questId); // Moofrenerk
		qe.registerQuestNpc(799522).addOnTalkEvent(questId); // Moofrenerk
		qe.registerQuestNpc(799523).addOnTalkEvent(questId); // Moofrenerk
		qe.registerQuestNpc(700951).addOnTalkEvent(questId); // Raw Odium
		qe.registerQuestNpc(700833).addOnTalkEvent(questId); // Fine Odium
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1000, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE ) {
	  		if (targetId == 799522) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE: {
						return sendQuestStartDialog(env);
					}
					case QUEST_REFUSE_SIMPLE: {
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799523: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 0, 0, true, 5, 2716);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				case 700951:
				case 700833: {
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;
					}
				}
					
				default:
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799523) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
}
