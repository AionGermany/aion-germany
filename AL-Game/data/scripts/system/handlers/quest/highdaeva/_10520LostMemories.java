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
public class _10520LostMemories extends QuestHandler {

    private final static int questId = 10520;

    public _10520LostMemories() {
        super(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player == null || env == null) {
            return false;
        }

        if (player.getRace() == Race.ELYOS && player.getLevel() == 65) {
            QuestService.startQuest(env);
            return true;
        }
        return false;
    }

    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerOnEnterWorld(questId);
        qe.registerQuestItem(182215973, questId); //Sealed Letter from Pernos
        qe.registerQuestItem(182215953, questId); //Orders to report to Iluma
        qe.registerQuestNpc(203752).addOnTalkEvent(questId); //Jucleas
        qe.registerQuestNpc(806073).addOnTalkEvent(questId); //Messenger Pellen
        qe.registerQuestNpc(203726).addOnTalkEvent(questId); //Polyidus
        qe.registerQuestNpc(806076).addOnTalkEvent(questId); //Viola
        qe.registerOnMovieEndQuest(996, questId);
        qe.registerOnMovieEndQuest(995, questId);
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
            if (targetId == 203752) { //Jucleas
                switch (dialog) {
                    case QUEST_SELECT: {
                        return sendQuestDialog(env, 1011);
                    }
                    case SETPRO1: {
                        giveQuestItem(env, 182215973, 1); //Orders to report to Iluma
                        changeQuestStep(env, 0, 1, false); //2
                        return closeDialogWindow(env);
                    }
                    default:
                        break;
                }
            } else if (targetId == 806073) { //Messenger Pellen
                switch (dialog) {
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
            } else if (targetId == 203726) { //Polyidus
                switch (dialog) {
                    case QUEST_SELECT: {
                        return sendQuestDialog(env, 2034);
                    }
                    case SETPRO4: {
                        changeQuestStep(env, 3, 4, false); //3
                        TeleportService2.teleportTo(player, 210100000, 1411.0f, 1280.0f, 336.438f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                        return closeDialogWindow(env);
                    }
                    default:
                        break;
                }
            } else if (targetId == 806076) { //Viola
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
            if (targetId == 806076) { //Viola
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
            if (item.getItemTemplate().getTemplateId() == 182215973) { //Sealed Letter from Pernos
                qs.setQuestVar(2); //1
                updateQuestStatus(env);
                removeQuestItem(env, 182215973, 1);
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
            if (var == 4 && player.getWorldId() == 210100000) {
                playQuestMovie(env, 996);
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
        if (movieId == 996) {
            playQuestMovie(env, 995);
        } else if (movieId == 995) {
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
