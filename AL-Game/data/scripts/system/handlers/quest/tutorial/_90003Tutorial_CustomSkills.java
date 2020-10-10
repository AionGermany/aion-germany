package quest.tutorial;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Falke_34
 */
public class _90003Tutorial_CustomSkills extends QuestHandler {

	private final static int questId = 90003;

	public _90003Tutorial_CustomSkills() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
			switch (action) {
			case QUEST_AUTO_REWARD:
				QuestService.finishQuest(env);
				return closeDialogWindow(env);
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
