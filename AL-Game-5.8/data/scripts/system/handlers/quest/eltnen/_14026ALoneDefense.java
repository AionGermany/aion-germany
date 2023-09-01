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
package quest.eltnen;

import java.util.Collection;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _14026ALoneDefense extends QuestHandler {

	private final static int questId = 14026;
	private int waveCount;
	private int time = 0;

	public _14026ALoneDefense() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnQuestTimerEnd(questId);
		qe.registerQuestNpc(203901).addOnTalkEvent(questId);
		qe.registerQuestNpc(204020).addOnTalkEvent(questId);
		qe.registerQuestNpc(204044).addOnTalkEvent(questId);
		qe.registerQuestNpc(700141).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203901: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, 210020000, 1596.1948f, 1529.9152f, 317, (byte) 120, TeleportAnimation.BEAM_ANIMATION);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 204020: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO2: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							giveQuestItem(env, 182215324, 1);
							TeleportService2.teleportTo(player, 210020000, 2500.15f, 780.9f, 409, (byte) 15, TeleportAnimation.BEAM_ANIMATION);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 204044: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							switch (qs.getQuestVarById(0)) {
								case 2: {
									return sendQuestDialog(env, 1693);
								}
								case 4: {
									return sendQuestDialog(env, 2034);
								}
							}
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							QuestService.questTimerStart(env, 240);// 4min
							time = 5000;
							wave(player);
							return closeDialogWindow(env);
						}
						case SETPRO4: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, 210020000, 271.69f, 2787.04f, 272.47f, (byte) 50, TeleportAnimation.BEAM_ANIMATION);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203901) {
				switch (env.getDialog()) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 3739);
					}
					case SELECT_QUEST_REWARD: {
						removeQuestItem(env, 182201013, 1);
						return sendQuestDialog(env, 6);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] quests = { 14021, 14022, 14023, 14024, 14025 };
		return defaultOnZoneMissionEndEvent(env, quests);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] quests = { 14021, 14022, 14023, 14024, 14025 };
		return defaultOnLvlUpEvent(env, quests, true);
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				waveCount = 0;
				playQuestMovie(env, 157);
				Collection<Npc> allNpcs = World.getInstance().getNpcs();
				for (Npc npc : allNpcs) {
					if (npc.getWorldId() == 310040000) {
						if (npc.getNpcId() == 204044) {
							continue;
						}
						else {
							npc.getController().delete();
						}
					}
				}
				qs.setQuestVar(4);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				waveCount = 0;
				qs.setQuestVar(2);
				updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				waveCount = 0;
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}

	private void wave(final Player player) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (waveCount == 0) {
					spawn(player, 1);
					waveCount++;
					time = 55000;
					wave(player);
				}
				else if (waveCount == 1) {
					spawn(player, 2);
					waveCount++;
					time = 59000;
					wave(player);
				}
				else if (waveCount == 2) {
					spawn(player, 3);
					waveCount++;
					time = 59000;
					wave(player);
				}
				else if (waveCount == 3) {
					spawn(player, 4);
					waveCount++;
					time = 49000;
					wave(player);
				}
				else if (waveCount == 4) {
					spawn(player, 1);
					spawn(player, 2);
					threadSleep(1000);
					spawn(player, 3);
					spawn(player, 4);
				}
			}
		}, time);
	}

	private void spawn(Player player, int wave) {
		switch (wave) {
			case 1: {
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213575, 248.186f, 266.738f, 229.569f, (byte) 95);
				threadSleep(500);
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213575, 252.065f, 266.866f, 229.569f, (byte) 95);
				break;
			}
			case 2: {
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213575, 248.186f, 266.738f, 229.569f, (byte) 95);
				threadSleep(500);
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213578, 252.065f, 266.866f, 229.569f, (byte) 95);
				break;
			}
			case 3: {
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213578, 248.186f, 266.738f, 229.569f, (byte) 95);
				threadSleep(500);
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213576, 252.065f, 266.866f, 229.569f, (byte) 95);
				break;
			}
			case 4: {
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213575, 248.186f, 266.738f, 229.569f, (byte) 95);
				threadSleep(500);
				QuestService.spawnQuestNpc(310040000, player.getInstanceId(), 213577, 252.065f, 266.866f, 229.569f, (byte) 95);
				break;
			}
			default:
				break;
		}
	}

	private void threadSleep(int time) {
		try {
			Thread.sleep(time);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
