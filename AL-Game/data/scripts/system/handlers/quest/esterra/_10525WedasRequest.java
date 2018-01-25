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
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _10525WedasRequest extends QuestHandler {

	public static final int questId = 10525;

	public _10525WedasRequest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZone(ZoneName.get("ARIELS_REFUGE_210100000"), questId);
		qe.registerQuestItem(182216072, questId);
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); // Weda
		qe.registerQuestNpc(806134).addOnTalkEvent(questId); // Ador
		qe.registerQuestNpc(806224).addOnTalkEvent(questId); // Ester
		qe.registerQuestNpc(806225).addOnTalkEvent(questId); // Ovest
		qe.registerQuestNpc(806226).addOnTalkEvent(questId); // Merides
		qe.registerQuestNpc(806227).addOnTalkEvent(questId); // Seber
		qe.registerOnMovieEndQuest(1004, questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (player.getLevel() >= 65 && player.getWorldId() == 400010000) {
				QuestService.startQuest(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 806075: { // Weda
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else {
								return sendQuestStartDialog(env);
							}
						}
						case SETPRO1: {
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 806134: { // Ador
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
							else if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
							else if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						}
						case SETPRO2: {
							changeQuestStep(env, 1, 3, false); // Me falta la variable 2

							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case SETPRO4: {
							changeQuestStep(env, 3, 4, false);
							return closeDialogWindow(env);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 4, 5, false, 10000, 10001); // 4
						}
						case SETPRO6: {
							changeQuestStep(env, 5, 6, false);
							ItemService.addItem(player, 182216072, 1);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 806224: {
					// case 806225: // Ovest
					// case 806226: // Merides
					// case 806227: { // Seber
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 1779);
							}
						case SETPRO3: {
							// changeQuestStep(env, 2, 3, false); // Me falta la variable 2
							// qs.setQuestVarById(0, var + 1);
							qs.setQuestVar(3);
							// qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return false;
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806075) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 1004) {
			removeQuestItem(env, 182216072, 1);
			changeQuestStep(env, 6, 7, true);
			return true;
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("ARIELS_REFUGE_210100000")) {
			Player player = env.getPlayer();
			if (player == null)
				return false;
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;

			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return HandlerResult.UNKNOWN;
		}
		final int var = qs.getQuestVarById(0);
		if (var == 6 && id == 182216072) {
			if (!player.isInsideZone(ZoneName.get("LF6_ITEMUSEAREA_Q10525"))) {
				return HandlerResult.UNKNOWN;
			}
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
					playQuestMovie(env, 1004);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.UNKNOWN;
	}
}
