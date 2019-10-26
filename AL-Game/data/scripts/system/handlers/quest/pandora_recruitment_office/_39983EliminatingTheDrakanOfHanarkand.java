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
package quest.pandora_recruitment_office;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator 1.15 by Mariella
 */
public class _39983EliminatingTheDrakanOfHanarkand extends QuestHandler {

	private final static int questId = 39983;
	private final static int[] mobs = { 650902, 650907, 650924, 650925, 650934, 650935, 650904, 650909, 650928, 650929, 650937, 650938, 650903, 650908, 650926, 650927, 650936, 650901, 650906, 650922, 650923, 650932, 650933 };

	public _39983EliminatingTheDrakanOfHanarkand() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820181).addOnTalkEvent(questId);  // Berio

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 39900, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 820181: {
					switch (dialog) {
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

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var  = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 0, Count: 35, Mobs : 650902, 650907, 650924, 650925, 650934, 650935
			// (1) Step: 0, Count: 35, Mobs : 650904, 650909, 650928, 650929, 650937, 650938
			// (2) Step: 0, Count: 35, Mobs : 650903, 650908, 650926, 650927, 650936
			// (3) Step: 0, Count: 35, Mobs : 650901, 650906, 650922, 650923, 650932, 650933

			switch (var) {
				case 0: {
				// ???
				}
				case 1: {
				// ???
				}
				case 2: {
				// ???
				}
				case 3: {
				// ???
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
}
