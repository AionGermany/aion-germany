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
package quest.esterra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _15515DefenceoftheWindValley extends QuestHandler {

	public _15515DefenceoftheWindValley() {
		super(questId);
	}

	private final static int questId = 15515;
	int[] mobs = { 240527, 240528, 241725, 241726, 241727, 240529, 240530, 241728, 241729, 241730, 240531, 240532, 241731, 241732, 241733, 240777, 240778, 241734, 241735, 241736, 238984, 238985, 238986, 238987, 238988, 238989, 242724, 242725, 242726, 242727, 243399, 243400, 243401, 243402, 243479, 243480, 243481, 243482, 238990, 238991, 238992, 238993, 238994, 238995, 242744, 242745, 242746, 242747, 243419, 243420, 243421, 243422, 243499, 243500, 243501, 243502, 238996, 238997, 238998, 238999, 239000, 239001, 242764, 242765, 242766, 242767, 243439, 243440, 243441, 243442, 243519, 243520, 243521, 243522, 239002, 239003, 239004, 239005, 239006, 239007, 242784, 242785, 242786, 242787, 243459, 243460, 243461, 243462, 243539, 243540, 243541, 243542, 239459, 239460, 239461, 239462, 239463, 239464, 239465, 239466, 239467, 239468, 239469, 239470, 239471, 239472, 239473, 239474, 239475, 239476, 239477, 239478, 239479, 239480, 239481, 239482, 239483, 239484, 239485, 239486, 239487, 239488, 239489, 239490, 239491, 239492, 239493, 239494, 242728, 242729, 242730, 242731, 242732, 242733, 242734, 242735, 242736, 242737, 242738, 242739, 242740, 242741, 242742, 242743, 243403, 243404, 243405,
		243406, 243407, 243408, 243409, 243410, 243411, 243412, 243413, 243414, 243415, 243416, 243417, 243418, 243483, 243484, 243485, 243486, 243487, 243488, 243489, 243490, 243491, 243492, 243493, 243494, 243495, 243496, 243497, 243498, 239495, 239496, 239497, 239498, 239499, 239500, 239501, 239502, 239503, 239504, 239505, 239506, 239507, 239508, 239509, 239510, 239511, 239512, 239513, 239514, 239515, 239516, 239517, 239518, 239519, 239520, 239521, 239522, 239523, 239524, 239525, 239526, 239527, 239528, 239529, 239530, 242748, 242749, 242750, 242751, 242752, 242753, 242754, 242755, 242756, 242757, 242758, 242759, 242760, 242761, 242762, 242763, 243423, 243424, 243425, 243426, 243427, 243428, 243429, 243430, 243431, 243432, 243433, 243434, 243435, 243436, 243437, 243438, 243503, 243504, 243505, 243506, 243507, 243508, 243509, 243510, 243511, 243512, 243513, 243514, 243515, 243516, 243517, 243518, 239531, 239532, 239533, 239534, 239535, 239536, 239537, 239538, 239539, 239540, 239541, 239542, 239543, 239544, 239545, 239546, 239547, 239548, 239549, 239550, 239551, 239552, 239553, 239554, 239555, 239556, 239557, 239558, 239559, 239560, 239561, 239562, 239563, 239564, 239565, 239566,
		242768, 242769, 242770, 242771, 242772, 242773, 242774, 242775, 242776, 242777, 242778, 242779, 242780, 242781, 242782, 242783, 243443, 243444, 243445, 243446, 243447, 243448, 243449, 243450, 243451, 243452, 243453, 243454, 243455, 243456, 243457, 243458, 243523, 243524, 243525, 243526, 243527, 243528, 243529, 243530, 243531, 243532, 243533, 243534, 243535, 243536, 243537, 243538, 239567, 239568, 239569, 239570, 239571, 239572, 239573, 239574, 239575, 239576, 239577, 239578, 239579, 239580, 239581, 239582, 239583, 239584, 239585, 239586, 239587, 239588, 239589, 239590, 239591, 239592, 239593, 239594, 239595, 239596, 239597, 239598, 239599, 239600, 239601, 239602, 242788, 242789, 242790, 242791, 242792, 242793, 242794, 242795, 242796, 242797, 242798, 242799, 242800, 242801, 242802, 242803, 243463, 243464, 243465, 243466, 243467, 243468, 243469, 243470, 243471, 243472, 243473, 243474, 243475, 243476, 243477, 243478, 243543, 243544, 243545, 243546, 243547, 243548, 243549, 243550, 243551, 243552, 243553, 243554, 243555, 243556, 243557, 243558 };

	@Override
	public void register() {
		qe.registerQuestNpc(806094).addOnQuestStart(questId); // Sernia
		qe.registerQuestNpc(806094).addOnTalkEvent(questId);
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
			if (targetId == 806094) { // Sernia
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806094) { // Sernia
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
			if (targetId == 806094) { // Sernia
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
