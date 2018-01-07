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
package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * Talk with Loriniah (203605). Scout the MuMu Farmland (DF1A_SENSORYAREA_Q2412_206012_1_220030000). Scouting completed! Talk with Loriniah. Burn the MuMu Carts (700096) in the MuMu Farmland (3). Talk
 * with Loriniah. Defeat the Skurvs and Mau and bring the evidence to Loriniah.
 * 
 * @author Mr. Poke
 * @reworked vlog, Gigi, aioncool
 */
public class _24012AnOminousCrop extends QuestHandler {

	private final static int questId = 24012;

	public _24012AnOminousCrop() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203605).addOnTalkEvent(questId);
		qe.registerQuestNpc(700096).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("DF1A_SENSORYAREA_Q2412_206012_1_220030000"), questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null)
			return false;

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203605: { // Loriniah
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 5)
								return sendQuestDialog(env, 2716);
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1, 182215356, 1, 0, 0);
						case CHECK_USER_HAS_QUEST_ITEM:
							return checkQuestItems(env, 5, 6, true, 5, 2120); // reward
						default:
							break;
					}
					break;
				}
				case 700096: { // MuMu Cart
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (var >= 2 && var < 5) {
								return useQuestObject(env, var, var + 1, false, true); // 4,5
							}
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203605) // Loriniah
				removeQuestItem(env, 182215356, 1);
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("DF1A_SENSORYAREA_Q2412_206012_1_220030000")) {
			Player player = env.getPlayer();
			if (player == null)
				return false;
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 1) {
					changeQuestStep(env, 1, 2, false); // 2
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env, 24011);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 24010, true);
	}
}
