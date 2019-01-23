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
package quest.event;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator by Mariella
 */
public class _50146EventAttackOnTheLegatesOfTheLakrumGarrisons extends QuestHandler {

	private final static int questId = 50146;
	private final static int[] mobs = { 885651, 885652, 885653, 885654, 885655, 885656, 885657, 885658, 885659, 885660, 885666, 885667, 885668, 885669, 885670, 885671, 885672, 885673, 885674, 885675, 885681, 885682, 885683, 885684, 885685, 885686, 885687, 885688, 885689, 885690, 885696, 885697, 885698, 885699, 885700, 885701, 885702, 885703, 885704, 885705, 885711, 885712, 885713, 885714, 885715, 885716, 885717, 885718, 885719, 885720, 885726, 885727, 885728, 885729, 885730, 885731, 885732, 885733, 885734, 885735, 885741, 885742, 885743, 885744, 885745, 885746, 885747, 885748, 885749, 885750, 885756, 885757, 885758, 885759, 885760, 885761, 885762, 885763, 885764, 885765, 885771, 885772, 885773, 885774, 885775, 885776, 885777, 885778, 885779, 885780 };

	public _50146EventAttackOnTheLegatesOfTheLakrumGarrisons() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(837031).addOnQuestStart(questId); // Lakrunerk
		qe.registerQuestNpc(837031).addOnTalkEvent(questId); // Lakrunerk

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
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

		if (qs == null || qs.getStatus() == QuestStatus.NONE  || qs.canRepeat()) {
	  		if (targetId == 837031) {
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
				case 837031: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		}

		return false;
	}
	/*
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 0, Count: 1, Mobs : 885651, 885652, 885653, 885654, 885655, 885656, 885657, 885658, 885659, 885660, 885666, 885667, 885668, 885669, 885670, 885671, 885672, 885673, 885674, 885675, 885681, 885682, 885683, 885684, 885685, 885686, 885687, 885688, 885689, 885690, 885696, 885697, 885698, 885699, 885700, 885701, 885702, 885703, 885704, 885705, 885711, 885712, 885713, 885714, 885715, 885716, 885717, 885718, 885719, 885720, 885726, 885727, 885728, 885729, 885730, 885731, 885732, 885733, 885734, 885735, 885741, 885742, 885743, 885744, 885745, 885746, 885747, 885748, 885749, 885750, 885756, 885757, 885758, 885759, 885760, 885761, 885762, 885763, 885764, 885765, 885771, 885772, 885773, 885774, 885775, 885776, 885777, 885778, 885779, 885780

			if (var == 0 && var1 < 0) {
			   return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
			} else {
				qs.setQuestVar(1);
				updateQuestStatus(env);
			}
		}
		return false;
	}
	*/
}
