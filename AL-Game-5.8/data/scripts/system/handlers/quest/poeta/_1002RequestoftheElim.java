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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author MrPoke
 * @reworked vlog
 * @modified apozema
 */
public class _1002RequestoftheElim extends QuestHandler {

	private final static int questId = 1002;

	public _1002RequestoftheElim() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 203076, 730007, 730010, 730008, 205000, 203067 };
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203076: { // Ampeis
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
					break;
				}
				case 730007: { // Forest Protector Noah
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 5) {
								return sendQuestDialog(env, 1693);
							}
							else if (var == 6) {
								return sendQuestDialog(env, 2034);
							}
							else if (var == 12) {
								return sendQuestDialog(env, 2120);
							}
						}
						case SELECT_ACTION_1353: {
							if (var == 1) {
								playQuestMovie(env, 20);
								return sendQuestDialog(env, 1353);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2, 182200002, 1, 0, 0);
						}
						case SETPRO3: {
							return defaultCloseDialog(env, 5, 6, 0, 0, 182200002, 1);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (var == 6) {
								return checkQuestItems(env, 6, 12, false, 2120, 2205);
							}
							else if (var == 12) {
								return sendQuestDialog(env, 2120);
							}
						}
						case SETPRO4: {
							return defaultCloseDialog(env, 12, 13);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default:
							break;
					}
					break;
				}
				case 730010: { // Sleeping Elder
					if (dialog == DialogAction.USE_OBJECT) {
						if (player.getInventory().getItemCountByItemId(182200002) == 1) {
							if (var == 2) {
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								useQuestObject(env, 2, 4, false, false);
							}
							else if (var == 4) {
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								return useQuestObject(env, 4, 5, false, false);
							}
						}
					}
					break;
				}
				case 730008: { // Daminu
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 13) {
								return sendQuestDialog(env, 2375);
							}
							else if (var == 14) {
								return sendQuestDialog(env, 2461);
							}
						}
						case SETPRO5: {
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310010000);
							TeleportService2.teleportTo(player, 310010000, newInstance.getInstanceId(), 53.002262f, 175.88441f, 229.1948f, (byte) 9, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 13, 20, false);
							return closeDialogWindow(env);
						}
						case SETPRO6: {
							return defaultCloseDialog(env, 14, 14, true, false);
						}
						default:
							break;
					}
					break;
				}
				case 205000: { // Belpartan
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 20) {
								player.setState(CreatureState.FLIGHT_TELEPORT);
								player.unsetState(CreatureState.ACTIVE);
								player.setFlightTeleportId(1001);
								PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										changeQuestStep(env, 20, 14, false);
										TeleportService2.teleportTo(player, 210010000, 601.8938f, 1537.4147f, 115.93347f, (byte) 36, TeleportAnimation.BEAM_ANIMATION);
									}
								}, 43000);
								return true;
							}
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203067) { // Kalio
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 2716);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() == 310010000) {
				PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
				return true;
			}
			else {
				int var = qs.getQuestVarById(0);
				if (var == 20) {
					changeQuestStep(env, 20, 13, false);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		int targetId = env.getTargetId();
		if (targetId == 730010) { // Sleeping Elder
			if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVarById(0) != 2 && qs.getQuestVarById(0) != 4) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1100, true);
	}
}
