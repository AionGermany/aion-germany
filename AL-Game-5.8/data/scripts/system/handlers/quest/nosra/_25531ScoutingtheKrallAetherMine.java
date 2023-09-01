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
public class _25531ScoutingtheKrallAetherMine extends QuestHandler {

	private final static int questId = 25531;
	int[] mobs = { 241791, 241792, 241793, 240781, 240782, 241800, 241801, 241802, 239084, 239085, 239086, 239087, 239088, 239089, 243064, 243065, 243066, 243067, 240797, 240798, 240799, 240800, 240801, 240802, 243084, 243085, 243086, 243087, 240803, 240804, 240805, 240806, 240807, 240808, 243104, 243105, 243106, 243107, 239096, 239097, 239098, 239099, 239100, 239101, 243124, 243125, 243126, 243127, 240059, 240060, 240061, 240062, 240063, 240064, 240065, 240066, 240067, 240068, 240069, 240070, 240071, 240072, 240073, 240074, 240075, 240076, 240077, 240078, 240079, 240080, 240081, 240082, 240083, 240084, 240085, 240086, 240087, 240088, 240089, 240090, 240091, 240092, 240093, 240094, 243068, 243069, 243070, 243071, 243072, 243073, 243074, 243075, 243076, 243077, 243078, 243079, 243080, 243081, 243082, 243083, 240131, 240132, 240133, 240134, 240135, 240136, 240137, 240138, 240139, 240140, 240141, 240142, 240143, 240144, 240145, 240146, 240147, 240148, 240149, 240150, 240151, 240152, 240153, 240154, 240155, 240156, 240157, 240158, 240159, 240160, 240161, 240162, 240163, 240164, 240165, 240166, 243128, 243129, 243130, 243131, 243132, 243133, 243134, 243135, 243136, 243137, 243138,
		243139, 243140, 243141, 243142, 243143, 240809, 240810, 240811, 240812, 240813, 240814, 240815, 240816, 240817, 240818, 240819, 240820, 240821, 240822, 240823, 240824, 240825, 240826, 240827, 240828, 240829, 240830, 240831, 240832, 240833, 240834, 240835, 240836, 240837, 240838, 240839, 240840, 240841, 240842, 240843, 240844, 243088, 243089, 243090, 243091, 243092, 243093, 243094, 243095, 243096, 243097, 243098, 243099, 243100, 243101, 243102, 243103, 240845, 240846, 240847, 240848, 240849, 240850, 240851, 240852, 240853, 240854, 240855, 240856, 240857, 240858, 240859, 240860, 240861, 240862, 240863, 240864, 240865, 240866, 240867, 240868, 240869, 240870, 240871, 240872, 240873, 240874, 240875, 240876, 240877, 240878, 240879, 240880, 243108, 243109, 243110, 243111, 243112, 243113, 243114, 243115, 243116, 243117, 243118, 243119, 243120, 243121, 243122, 243123, 239102, 239103, 239104, 239105, 239106, 239107, 243144, 243145, 243146, 243147, 239108, 239109, 239110, 239111, 239112, 239113, 243164, 243165, 243166, 243167, 239114, 239115, 239116, 239117, 239118, 239119, 243184, 243185, 243186, 243187, 240239, 240240, 240241, 240242, 240243, 240244, 240245, 240246, 240247, 240248,
		240249, 240250, 240251, 240252, 240253, 240254, 240255, 240256, 240257, 240258, 240259, 240260, 240261, 240262, 240263, 240264, 240265, 240266, 240267, 240268, 240269, 240270, 240271, 240272, 240273, 240274, 243188, 243189, 243190, 243191, 243192, 243193, 243194, 243195, 243196, 243197, 243198, 243199, 243200, 243201, 243202, 243203, 240167, 240168, 240169, 240170, 240171, 240172, 240173, 240174, 240175, 240176, 240177, 240178, 240179, 240180, 240181, 240182, 240183, 240184, 240185, 240186, 240187, 240188, 240189, 240190, 240191, 240192, 240193, 240194, 240195, 240196, 240197, 240198, 240199, 240200, 240201, 240202, 243148, 243149, 243150, 243151, 243152, 243153, 243154, 243155, 243156, 243157, 243158, 243159, 243160, 243161, 243162, 243163, 240203, 240204, 240205, 240206, 240207, 240208, 240209, 240210, 240211, 240212, 240213, 240214, 240215, 240216, 240217, 240218, 240219, 240220, 240221, 240222, 240223, 240224, 240225, 240226, 240227, 240228, 240229, 240230, 240231, 240232, 240233, 240234, 240235, 240236, 240237, 240238, 243168, 243169, 243170, 243171, 243172, 243173, 243174, 243175, 243176, 243177, 243178, 243179, 243180, 243181, 243182, 243183 };

	public _25531ScoutingtheKrallAetherMine() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806111).addOnQuestStart(questId); // Shazel
		qe.registerQuestNpc(806111).addOnTalkEvent(questId);
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
			if (targetId == 806111) { // Shazel
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806111) { // Shazel
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
			if (targetId == 806111) { // Shazel
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
