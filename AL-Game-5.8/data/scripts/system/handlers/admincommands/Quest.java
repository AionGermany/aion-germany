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
package admincommands;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.FinishedQuestCond;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author MrPoke
 */
public class Quest extends AdminCommand {

	public Quest() {
		super("quest");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "syntax //quest <start|set|show|delete>");
			return;
		}
		Player target = null;
		VisibleObject creature = admin.getTarget();
		if (admin.getTarget() instanceof Player) {
			target = (Player) creature;
		}

		// <mariella> - target isn't required for the command ".quest id <link>"
		if (target == null && !params[0].equals("id")) {
			PacketSendUtility.sendMessage(admin, "Incorrect target!");
			return;
		}

		if (params[0].equals("start")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "syntax //quest start <questId>");
				return;
			}
			int id;
			try {
				String quest = params[1];
				Pattern questId = Pattern.compile("\\[quest:([^%]+)]");
				Matcher result = questId.matcher(quest);
				if (result.find()) {
					id = Integer.parseInt(result.group(1));
				}
				else {
					id = Integer.parseInt(params[1]);
				}
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "syntax //quest start <questId>");
				return;
			}

			QuestEnv env = new QuestEnv(null, target, id, 0);

			if (QuestService.startQuest(env)) {
				PacketSendUtility.sendMessage(admin, "Quest started.");
			}
			else {
				QuestTemplate template = DataManager.QUEST_DATA.getQuestById(id);
				List<XMLStartCondition> preconditions = template.getXMLStartConditions();
				if (preconditions != null && preconditions.size() > 0) {
					for (XMLStartCondition condition : preconditions) {
						List<FinishedQuestCond> finisheds = condition.getFinishedPreconditions();
						if (finisheds != null && finisheds.size() > 0) {
							for (FinishedQuestCond fcondition : finisheds) {
								QuestState qs1 = admin.getQuestStateList().getQuestState(fcondition.getQuestId());
								if (qs1 == null || qs1.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendMessage(admin, "You have to finish " + fcondition.getQuestId() + " first!");
								}
							}
						}
					}
				}
				PacketSendUtility.sendMessage(admin, "Quest not started. Some preconditions failed");
			}
		}
		else if (params[0].equals("set")) {
			int questId, var;
			int varNum = 0;
			QuestStatus questStatus;
			try {
				String quest = params[1];
				Pattern id = Pattern.compile("\\[quest:([^%]+)]");
				Matcher result = id.matcher(quest);
				if (result.find()) {
					questId = Integer.parseInt(result.group(1));
				}
				else {
					questId = Integer.parseInt(params[1]);
				}

				String statusValue = params[2];
				if ("START".equals(statusValue)) {
					questStatus = QuestStatus.START;
				}
				else if ("NONE".equals(statusValue)) {
					questStatus = QuestStatus.NONE;
				}
				else if ("COMPLETE".equals(statusValue)) {
					questStatus = QuestStatus.COMPLETE;
				}
				else if ("REWARD".equals(statusValue)) {
					questStatus = QuestStatus.REWARD;
				}
				else {
					PacketSendUtility.sendMessage(admin, "<status is one of START, NONE, REWARD, COMPLETE>");
					return;
				}
				var = Integer.valueOf(params[3]);
				if (params.length == 5 && params[4] != null && params[4] != "") {
					varNum = Integer.valueOf(params[4]);
				}
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status var [varNum]>");
				return;
			}
			QuestState qs = target.getQuestStateList().getQuestState(questId);
			if (qs == null) {
				qs = new QuestState(questId, questStatus, 0, 0, new Timestamp(0), 0, new Timestamp(0));
				PacketSendUtility.sendMessage(admin, "<QuestState has been newly initialized.>");
				return;
			}
			qs.setStatus(questStatus);
			if (varNum != 0) {
				qs.setQuestVarById(varNum, var);
			}
			else {
				qs.setQuestVar(var);
			}
			PacketSendUtility.sendPacket(target, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
			
			switch (questStatus) {
				case REWARD: {
					target.getController().updateNearbyQuests();
					break;
				}
				case COMPLETE: {
					qs.setCompleteCount(qs.getCompleteCount() + 1);
					target.getController().updateNearbyQuests();
					break;
				}
				default:
					break;
			}
		}
		if (params[0].equals("delete")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "syntax //quest delete <quest id>");
				return;
			}
			int id;
			try {
				id = Integer.valueOf(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "syntax //quest delete <quest id>");
				return;
			}

			QuestStateList list = admin.getQuestStateList();
			if (list == null || list.getQuestState(id) == null) {
				PacketSendUtility.sendMessage(admin, "Quest not deleted.");
			}
			else {
				QuestState qs = list.getQuestState(id);
				qs.setQuestVar(0);
				qs.setCompleteCount(0);
				qs.setStatus(null);
				if (qs.getPersistentState() != PersistentState.NEW) {
					qs.setPersistentState(PersistentState.DELETED);
				}
				PacketSendUtility.sendPacket(admin, new SM_QUEST_COMPLETED_LIST(admin.getQuestStateList().getAllFinishedQuests()));
				admin.getController().updateNearbyQuests();
			}
		}
		else if (params[0].equals("show")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "syntax //quest show <quest id>");
				return;
			}
			ShowQuestInfo(target, admin, params[1]);
			// <mariella> add sub-command "id" to see the linked questId
		}
		else if (params[0].equals("id")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "syntax //quest id <quest link>");
				return;
			}
			String quest = params[1];
			int questId;
			Pattern id = Pattern.compile("\\[quest:([^%]+)]");
			Matcher result = id.matcher(quest);
			if (result.find()) {
				questId = Integer.parseInt(result.group(1));
			}
			else {
				questId = Integer.parseInt(params[1]);
			}
			PacketSendUtility.sendMessage(admin, "Quest-ID: " + questId);
			// </mariella>
		}
		else {
			PacketSendUtility.sendMessage(admin, "syntax //quest <start|set|show|delete|id>"); // <mariella>
		}
	}

	private void ShowQuestInfo(Player player, Player admin, String param) {
		int id;
		try {
			id = Integer.valueOf(param);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "syntax //quest show <quest id>");
			return;
		}
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if (qs == null) {
			PacketSendUtility.sendMessage(admin, "Quest state: NULL");
		}
		else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 5; i++) {
				sb.append(Integer.toString(qs.getQuestVarById(i)) + " ");
			}
			PacketSendUtility.sendMessage(admin, "Quest state: " + qs.getStatus().toString() + "; vars: " + sb.toString() + qs.getQuestVarById(5));
			sb.setLength(0);
			sb = null;
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //quest <start|set|show|delete|id>");
	}
}
