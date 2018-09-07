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
package quest.highdaeva;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ione542 hckd05
 */
public class _20520LostMemories extends QuestHandler {

    private final static int questId = 20520;

    /**
     * @param questId
     */
    public _20520LostMemories() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerOnEnterWorld(questId);
        qe.registerQuestItem(182215974, questId); //Sealed Letter from Munin
        qe.registerQuestItem(182215954, questId); //Orders to report to Norsvold
        qe.registerQuestNpc(204075).addOnTalkEvent(questId); //Balder
        qe.registerQuestNpc(806077).addOnTalkEvent(questId); //Messenger Edorin
        qe.registerQuestNpc(204191).addOnTalkEvent(questId); //Doman
        qe.registerQuestNpc(806079).addOnTalkEvent(questId); //Peregrine
        qe.registerQuestNpc(806080).addOnTalkEvent(questId); //Peregrine
        qe.registerOnMovieEndQuest(868, questId);
        qe.registerOnMovieEndQuest(867, questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player == null || env == null) {
            return false;
        }

        if (player.getRace() == Race.ASMODIANS && player.getLevel() == 65) {
            QuestService.startQuest(env);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        DialogAction dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs == null) {
            return false;
        }

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 204075) { //Balder
                switch (dialog) {
                    case QUEST_SELECT: {
                        return sendQuestDialog(env, 1011);
                    }
                    case SETPRO1: {
                        giveQuestItem(env, 182215974, 1); //Orders to report to Norsvold
                        changeQuestStep(env, 0, 1, false); //2
                        return closeDialogWindow(env);
                    }
                    default:
                        break;
                }
            } else if (targetId == 806077) { //Messenger Edorin
                switch (env.getDialog()) {
                    case QUEST_SELECT: {
                        return sendQuestDialog(env, 1693);
                    }
                    case SETPRO3: {
                        changeQuestStep(env, 2, 3, false); //2
                        return closeDialogWindow(env);
                    }
                    default:
                    	break;
                }
            } else if (targetId == 204191) { //Doman
                switch (dialog) {
                    case QUEST_SELECT: {
                        return sendQuestDialog(env, 2034);
                    }
                    case SETPRO4: {
                        changeQuestStep(env, 3, 4, false); //3
                        TeleportService2.teleportTo(player, 220110000, 1789f, 1994f, 198, (byte) 52, TeleportAnimation.BEAM_ANIMATION);
                        return closeDialogWindow(env);
                    }
                    default:
                        break;
                }
            } else if (targetId == 806079 || targetId == 806080) { //Peregrine
                switch (dialog) {
                    case USE_OBJECT: {
                        return sendQuestDialog(env, 2716);
                    }
                    case SET_SUCCEED: {
                        qs.setStatus(QuestStatus.REWARD); //REWARD
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 10002);
                    }
                    default:
                        break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806079 || targetId == 806080) { //Peregrine
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null) {
            return HandlerResult.UNKNOWN;
        }

        if (qs.getStatus() == QuestStatus.START) {
            if (item.getItemTemplate().getTemplateId() == 182215974) { //Sealed Letter from Munin
                qs.setQuestVar(2); //1
                updateQuestStatus(env);
                removeQuestItem(env, 182215974, 1); //Orders to report to Norsvold
                return HandlerResult.SUCCESS;
            }
        }
        return HandlerResult.FAILED;
    }

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 4 && player.getWorldId() == 220110000) {
                playQuestMovie(env, 868);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (movieId == 868) {
            playQuestMovie(env, 867);
        } else if (movieId == 867) {
            WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301580000);
            InstanceService.registerPlayerWithInstance(newInstance, player);
            TeleportService2.teleportTo(player, 301580000, newInstance.getInstanceId(), 432.547f, 492.836f, 99.59915f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
            qs.setQuestVar(5); //4
            updateQuestStatus(env);
            removeQuestItem(env, 182215953, 1);
            return true;
        }

        return false;
    }
}
