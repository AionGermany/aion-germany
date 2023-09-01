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
package quest.daevation;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author kecimis
 */
public class _2990MakingTheDaevanionWeapon extends QuestHandler {

	private final static int questId = 2990;
	private final static int[] npc_ids = { 204146 };

	public _2990MakingTheDaevanionWeapon() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204146).addOnQuestStart(questId);// Kanensa
		qe.registerQuestNpc(256617).addOnKillEvent(questId);// Strange Lake Spirit
		qe.registerQuestNpc(253720).addOnKillEvent(questId);// Lava Hoverstone
		qe.registerQuestNpc(254513).addOnKillEvent(questId);// Disturbed Resident
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204146)// Kanensa
			{
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					int plate = player.getEquipment().itemSetPartsEquipped(9);
					int chain = player.getEquipment().itemSetPartsEquipped(8);
					int leather = player.getEquipment().itemSetPartsEquipped(7);
					int cloth = player.getEquipment().itemSetPartsEquipped(6);
					int gunner = player.getEquipment().itemSetPartsEquipped(378);

					if (plate != 5 && chain != 5 && leather != 5 && cloth != 5 && gunner != 5) {
						return sendQuestDialog(env, 4848);
					}
					else {
						return sendQuestDialog(env, 4762);
					}
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204146) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						if (var == 2 && var1 == 60) {
							return sendQuestDialog(env, 1693);
						}
						if (var == 3 && player.getInventory().getItemCountByItemId(186000040) > 0) {
							return sendQuestDialog(env, 2034);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						if (var == 0) {
							if (QuestService.collectItemCheck(env, true)) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								return sendQuestDialog(env, 10000);
							}
							else {
								return sendQuestDialog(env, 10001);
							}
						}
						break;
					case SELECT_ACTION_1352:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					case SELECT_ACTION_2035:
						if (var == 3) {
							if (player.getCommonData().getDp() == 4000 && player.getInventory().getItemCountByItemId(186000040) > 0) {
								removeQuestItem(env, 186000040, 1);
								player.getCommonData().setDp(0);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							else {
								return sendQuestDialog(env, 2120);
							}
						}
						break;
					case SETPRO2:
						if (var == 1) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
						break;
					case SETPRO3:
						if (var == 2) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
						break;
					default:
						break;
				}
			}
			return false;
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204146)// Kanensa
			{
				return sendQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int var1 = qs.getQuestVarById(1);
		int var2 = qs.getQuestVarById(2);
		int var3 = qs.getQuestVarById(3);
		int targetId = env.getTargetId();

		if ((targetId == 256617 || targetId == 253720 || targetId == 254513) && qs.getQuestVarById(0) == 2) {
			switch (targetId) {
				case 256617:
					if (var1 >= 0 && var1 < 60) {
						++var1;
						qs.setQuestVarById(1, var1 + 1);
						updateQuestStatus(env);
					}
					break;
				case 253720:
					if (var2 >= 0 && var2 < 120) {
						++var2;
						qs.setQuestVarById(2, var2 + 3);
						updateQuestStatus(env);
					}
					break;
				case 254513:
					if (var3 >= 0 && var3 < 240) {
						++var3;
						qs.setQuestVarById(3, var3 + 7);
						updateQuestStatus(env);
					}
					break;
			}
		}
		if (qs.getQuestVarById(0) == 2 && var1 == 60 && var2 == 120 && var3 == 240) {
			qs.setQuestVarById(1, 60);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
