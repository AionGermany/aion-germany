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
public class _49982TheDestructionOfUlagusStrigik extends QuestHandler {

	private final static int questId = 49982;
	private final static int[] mobs = { 650189, 650190, 650326, 650330, 650332, 650338, 650339, 650187, 650188, 650325, 650329, 650331, 650337, 650183, 650184, 650323, 650327, 650333, 650334, 650185, 650186, 650324, 650328, 650335, 650336 };

	public _49982TheDestructionOfUlagusStrigik() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820187).addOnTalkEvent(questId);  // Fektor

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 49900, false);
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
				case 820187: {
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

			// (0) Step: 0, Count: 35, Mobs : 650189, 650190, 650326, 650330, 650332, 650338, 650339
			// (1) Step: 0, Count: 35, Mobs : 650187, 650188, 650325, 650329, 650331, 650337
			// (2) Step: 0, Count: 35, Mobs : 650183, 650184, 650323, 650327, 650333, 650334
			// (3) Step: 0, Count: 35, Mobs : 650185, 650186, 650324, 650328, 650335, 650336

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
