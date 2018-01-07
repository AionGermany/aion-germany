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
package quest.nosra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ghost_KNA
 */
public class _25528ScoutingtheStormPlateau extends QuestHandler {

	private final static int questId = 25528;
	int[] mobs = { 240555, 240556, 241776, 241777, 241778, 240557, 240558, 241779, 241780, 241781, 240559, 240560, 241782, 241783, 241784, 241238, 241239, 241785, 241786, 241787, 239069, 239070, 239071, 239072, 239073, 243004, 243005, 243006, 243007, 239074, 239075, 239076, 239077, 239078, 243024, 243025, 243026, 243027, 239079, 239080, 239081, 239082, 239083, 243044, 243045, 243046, 243047, 239969, 239970, 239971, 239972, 239973, 243008, 243009, 243010, 243011, 239974, 239975, 239976, 239977, 239978, 239979, 239980, 239981, 239982, 239983, 243012, 243013, 243014, 243015, 239984, 239985, 239986, 239987, 239988, 243016, 243017, 243018, 243019, 239989, 239990, 239991, 239992, 239993, 243020, 243021, 243022, 243023, 239994, 239995, 239996, 239997, 239998, 239999, 240000, 240001, 240002, 240003, 243028, 243029, 243030, 243031, 240004, 240005, 240006, 240007, 240008, 240009, 240010, 240011, 240012, 240013, 243032, 243033, 243034, 243035, 240014, 240015, 240016, 240017, 240018, 243036, 243037, 243038, 243039, 240019, 240020, 240021, 240022, 240023, 243040, 243041, 243042, 243043, 240024, 240025, 240026, 240027, 240028, 240029, 240030, 240031, 240032, 240033, 243048, 243049, 243050,
		243051, 240034, 240035, 240036, 240037, 240038, 240039, 240040, 240041, 240042, 240043, 243052, 243053, 243054, 243055, 240044, 240045, 240046, 240047, 240048, 243056, 243057, 243058, 243059, 240049, 240050, 240051, 240052, 240053, 243060, 243061, 243062, 243063, 240054, 240055, 240056, 240057, 240058 };

	public _25528ScoutingtheStormPlateau() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806110).addOnQuestStart(questId); // Scorvio
		qe.registerQuestNpc(806110).addOnTalkEvent(questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806110) { // Scorvio
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806110) { // Scorvio
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806110) { // Scorvio
				return sendQuestEndDialog(env);
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
			if (var == 0) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 59) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 59) {
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
