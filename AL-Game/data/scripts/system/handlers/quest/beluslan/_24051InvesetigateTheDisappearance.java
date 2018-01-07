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
package quest.beluslan;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 */
public class _24051InvesetigateTheDisappearance extends QuestHandler {

	private final static int questId = 24051;
	private final static int[] npcs = { 204707, 204749, 204800, 700359 };

	public _24051InvesetigateTheDisappearance() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215375, questId);
		qe.registerOnMovieEndQuest(236, questId);
		qe.registerOnEnterZone(ZoneName.get("MINE_PORT_220040000"), questId);
		qe.registerOnEnterWorld(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 24050, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null) {
			return false;
		}
		Npc target = (Npc) env.getVisibleObject();
		int targetId = target.getNpcId();
		int var = qs.getQuestVarById(0);
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204707: { // mani
					if (dialog == DialogAction.QUEST_SELECT && var == 0) {
						return sendQuestDialog(env, 1011);
					}
					if (dialog == DialogAction.QUEST_SELECT && var == 3) {
						return sendQuestDialog(env, 2034);
					}
					if (dialog == DialogAction.SETPRO1) {
						return defaultCloseDialog(env, 0, 1); // 1
					}
					if (dialog == DialogAction.SETPRO4) {
						return defaultCloseDialog(env, 3, 4); // 1
					}
					break;
				}
				case 204749: { // paeru
					if (dialog == DialogAction.QUEST_SELECT && var == 1) {
						return sendQuestDialog(env, 1352);
					}
					if (dialog == DialogAction.SETPRO2) {
						return defaultCloseDialog(env, 1, 2, 182215375, 1, 0, 0);
					}
					break;
				}
				case 204800: { // hammel
					if (dialog == DialogAction.QUEST_SELECT && var == 4) {
						return sendQuestDialog(env, 2375);
					}
					if (dialog == DialogAction.SETPRO5) {
						giveQuestItem(env, 182215376, 1);
						return defaultCloseDialog(env, 4, 5);
					}
					break;
				}
				case 700359: { // Port
					if (dialog == DialogAction.USE_OBJECT && var == 5 && player.getInventory().getItemCountByItemId(182215377) >= 1) {
						TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), 1757.82f, 1392.94f, 401.75f, (byte) 94);
						return true;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204707) { // Mani
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					removeQuestItem(env, 182215376, 1);
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 3, false));
			}
		}
		return HandlerResult.FAILED;
	}

	@Override
	public boolean onEnterZoneEvent(final QuestEnv env, ZoneName name) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (name == ZoneName.get("MINE_PORT_220040000")) {
				if (var == 5) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							playQuestMovie(env, 236);
						}
					}, 10000);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 236) {
			return false;
		}
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			changeQuestStep(env, 5, 5, true); // reward
			removeQuestItem(env, 182215377, 1);
			return true;
		}
		return false;
	}
}
