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
package quest.black_clouds_in_pandaemonium;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _20530TheCallofHighPriestBalder extends QuestHandler {

	private final static int questId = 20530;

	public _20530TheCallofHighPriestBalder() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(204075).addOnTalkEvent(questId); // Balder
		qe.registerQuestNpc(204182).addOnTalkEvent(questId); // Heimdall
		qe.registerQuestNpc(703388).addOnTalkEvent(questId); // Frosted Pandeanan
		qe.registerQuestNpc(798443).addOnTalkEvent(questId); // Slike
		qe.registerQuestNpc(204173).addOnTalkEvent(questId); // Jeckrow
		qe.registerQuestNpc(806556).addOnTalkEvent(questId); // Jiharimaan
		qe.registerQuestNpc(703395).addOnTalkEvent(questId); // Cherry Brandy
		qe.registerQuestNpc(703391).addOnTalkEvent(questId); // Magic Ward of Diabolic Energy
		qe.registerQuestNpc(703389).addOnTalkEvent(questId); // Magic Ward of Diabolic Energy
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		DialogAction dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204075: { // Balder
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							if (var == 7) {
								return sendQuestDialog(env, 3398);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1);
						case CHECK_USER_HAS_QUEST_ITEM:
							return checkQuestItems(env, 7, 8, true, 10000, 10001);
						default:
							break;
					}
				}
				case 204182: { // Heimdall
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2);
						default:
							break;
					}
				}
				case 703388: { // Frosted Pandeanan
					switch (dialog) {
						case USE_OBJECT:
							if (var == 2) {
								return defaultCloseDialog(env, 2, 3);
							}
						default:
							break;
					}
				}
				case 798443: { // Slike
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						case SETPRO4:
							return defaultCloseDialog(env, 3, 4);
						default:
							break;
					}
				}
				case 204173: { // Jeckrow
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						case SETPRO5:
							return defaultCloseDialog(env, 4, 5);
						default:
							break;
					}
				}
				case 806556: { // Jiharimaan
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						case SETPRO6:
							return defaultCloseDialog(env, 5, 6);
						default:
							break;
					}
				}
				case 703395: { // Frosted Pandeanan
					switch (dialog) {
						case USE_OBJECT:
							if (var == 6) {
								return defaultCloseDialog(env, 6, 7);
							}
						default:
							break;
					}
				}
				case 703391: { // Magic Ward of Diabolic Energy
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;

					}
				}
				case 703389: { // Greatmage Tiny
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;

					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204075) { // Balder
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null) {
			return HandlerResult.UNKNOWN;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (item.getItemTemplate().getTemplateId() == 182215973) { // Sealed Letter from Pernos
				if (var == 0) {
					qs.setQuestVar(1); // 1
					updateQuestStatus(env);
					removeQuestItem(env, 182215973, 1); // Orders to report to Iluma
					return HandlerResult.SUCCESS;
				}
			}
		}
		return HandlerResult.FAILED;
	}
}
