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
package com.aionemu.gameserver.ai2;

import java.util.Collection;

import com.aionemu.gameserver.controllers.observer.DialogObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Here will be placed some common AI2 actions. These methods have access to AI2's owner
 *
 * @author ATracer
 */
public class AI2Actions {

	/**
	 * Despawn and delete owner
	 */
	public static void deleteOwner(AbstractAI ai2) {
		ai2.getOwner().getController().onDelete();
	}

	/**
	 * Target will die with all notifications using ai's owner as the last attacker
	 */
	public static void killSilently(AbstractAI ai2, Creature target) {
		target.getController().onDie(ai2.getOwner());
	}

	/**
	 * AI's owner will die from specified attacker
	 */
	public static void dieSilently(AbstractAI ai2, Creature attacker) {
		ai2.getOwner().getController().onDie(attacker);
	}

	/**
	 * Use skill or add intention to use (will be implemented later)
	 */
	public static void useSkill(AbstractAI ai2, int skillId) {
		ai2.getOwner().getController().useSkill(skillId);
	}

	/**
	 * Effect will be created and applied to target with 100% success
	 */
	public static void applyEffect(AbstractAI ai2, SkillTemplate template, Creature target) {
		Effect effect = new Effect(ai2.getOwner(), target, template, template.getLvl(), 0);
		effect.setIsForcedEffect(true);
		effect.initialize();
		effect.applyEffect();
	}

	public static void applyEffectSelf(AbstractAI ai2, int skillId) {
		SkillTemplate st = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		Effect effect = new Effect(ai2.getOwner(), ai2.getOwner(), st, 1, st.getEffectsDuration(skillId));
		effect.initialize();
		effect.applyEffect();
	}

	public static void targetSelf(AbstractAI ai2) {
		ai2.getOwner().setTarget(ai2.getOwner());
	}

	public static void targetCreature(AbstractAI ai2, Creature target) {
		ai2.getOwner().setTarget(target);
	}

	public static void handleUseItemFinish(AbstractAI ai2, Player player) {
		ai2.getPosition().getWorldMapInstance().getInstanceHandler().handleUseItemFinish(player, ((Npc) ai2.getOwner()));
	}

	public static void fireIndividualEvent(AbstractAI ai2, Npc target) {
		target.getAi2().onIndividualNpcEvent(ai2.getOwner());
	}

	public static void fireNpcKillInstanceEvent(AbstractAI ai2, Player player) {
		ai2.getPosition().getWorldMapInstance().getInstanceHandler().onDie((Npc) ai2.getOwner());
	}

	public static void registerDrop(AbstractAI ai2, Player player, Collection<Player> registeredPlayers) {
		DropRegistrationService.getInstance().registerDrop((Npc) ai2.getOwner(), player, registeredPlayers);
	}

	public static void scheduleRespawn(NpcAI2 ai2) {
		ai2.getOwner().getController().scheduleRespawn();
	}

	public static SelectDialogResult selectDialog(AbstractAI ai2, Player player, int questId, int dialogId) {
		QuestEnv env = new QuestEnv(ai2.getOwner(), player, questId, dialogId);
		boolean result = QuestEngine.getInstance().onDialog(env);
		return new SelectDialogResult(result, env);
	}

	public static final class SelectDialogResult {

		private final boolean success;
		private final QuestEnv env;

		private SelectDialogResult(boolean success, QuestEnv env) {
			this.success = success;
			this.env = env;
		}

		public boolean isSuccess() {
			return success;
		}

		public QuestEnv getEnv() {
			return env;
		}
	}

	/**
	 * Add RequestResponseHandler to player with senderId equal to objectId of AI owner
	 */
	public static void addRequest(AbstractAI ai2, Player player, int requestId, AI2Request request, Object... requestParams) {
		addRequest(ai2, player, requestId, ai2.getObjectId(), request, requestParams);
	}

	/**
	 * Add RequestResponseHandler to player, which cancels request on movement
	 */
	public static void addRequest(AbstractAI ai2, Player player, int requestId, int senderId, int range, final AI2Request request, Object... requestParams) {

		boolean requested = player.getResponseRequester().putRequest(requestId, new RequestResponseHandler(ai2.getOwner()) {

			@Override
			public void denyRequest(Creature requester, Player responder) {
				request.denyRequest(requester, responder);
			}

			@Override
			public void acceptRequest(Creature requester, Player responder) {
				request.acceptRequest(requester, responder);
			}
		});

		if (requested) {
			if (range > 0) {
				player.getObserveController().addObserver(new DialogObserver(ai2.getOwner(), player, range) {

					@Override
					public void tooFar(Creature requester, Player responder) {
						request.denyRequest(requester, responder);
					}
				});
			}
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(requestId, senderId, range, requestParams));
		}
	}

	/**
	 * Add RequestResponseHandler to player
	 */
	public static void addRequest(AbstractAI ai2, Player player, int requestId, int senderId, final AI2Request request, Object... requestParams) {
		addRequest(ai2, player, requestId, senderId, 0, request, requestParams);
	}
}
