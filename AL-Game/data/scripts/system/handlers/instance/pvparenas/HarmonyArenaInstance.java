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
package instance.pvparenas;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.HarmonyArenaReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author xTz
 */
public class HarmonyArenaInstance extends GeneralInstanceHandler {

	protected int killBonus = 1000;
	protected int deathFine = -150;
	protected HarmonyArenaReward instanceReward;
	protected boolean isInstanceDestroyed;

	@Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}

	@Override
	public void onEnterInstance(final Player player) {
		Integer object = player.getObjectId();
		if (!instanceReward.containPlayer(object)) {
			instanceReward.regPlayerReward(object);
			instanceReward.getPlayerReward(object).applyBoostMoraleEffect(player);
			instanceReward.setRndPosition(object);
		}
		else {
			instanceReward.portToPosition(player);
		}
		sendEnterPacket(player);
	}

	private void sendEnterPacket(final Player player) {
		final Integer object = player.getObjectId();
		final HarmonyGroupReward group = instanceReward.getHarmonyGroupReward(object);
		if (group == null) {
			return;
		}

		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player opponent) {
				if (!group.containPlayer(opponent.getObjectId())) {
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(10, getTime(), getInstanceReward(), object));
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(10, getTime(), getInstanceReward(), opponent.getObjectId()));
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), object));
				}
				else {
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(10, getTime(), getInstanceReward(), opponent.getObjectId()));
					if (object != opponent.getObjectId()) {
						PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), object));
					}
				}
			}
		});
		Integer NullObject = null;
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), getInstanceReward(), NullObject));
		instanceReward.sendPacket(4, object);
	}

	private void updatePoints(Creature victim) {

		if (!instanceReward.isStartProgress()) {
			return;
		}

		int bonus = 0;
		int rank = 0;

		// Decrease victim points
		if (victim instanceof Player) {
			final HarmonyGroupReward victimGroup = instanceReward.getHarmonyGroupReward(victim.getObjectId());
			victimGroup.addPoints(deathFine);
			bonus = killBonus;
			rank = instanceReward.getRank(victimGroup.getPoints());
			instanceReward.sendPacket(10, victim.getObjectId());
		}
		else {
			bonus = getNpcBonus(((Npc) victim).getNpcId());
		}

		if (bonus == 0) {
			return;
		}

		// Reward all damagers
		for (AggroInfo damager : victim.getAggroList().getList()) {
			if (!(damager.getAttacker() instanceof Creature)) {
				continue;
			}
			Creature master = ((Creature) damager.getAttacker()).getMaster();
			if (master == null) {
				continue;
			}
			if (master instanceof Player) {
				Player attaker = (Player) master;
				int rewardPoints = (victim instanceof Player && instanceReward.getRound() == 3 && rank == 0 ? bonus * 3 : bonus) * damager.getDamage() / victim.getAggroList().getTotalDamage();
				instanceReward.getHarmonyGroupReward(attaker.getObjectId()).addPoints(rewardPoints);
				sendSystemMsg(attaker, victim, rewardPoints);
				instanceReward.sendPacket(10, attaker.getObjectId());
			}
		}
		if (instanceReward.hasCapPoints()) {
			instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
			reward();
			instanceReward.sendPacket(5, null);
		}
	}

	@Override
	public void onDie(Npc npc) {
		if (npc.getAggroList().getMostPlayerDamage() == null) {
			return;
		}
		updatePoints(npc);
	}

	protected void sendSystemMsg(Player player, Creature creature, int rewardPoints) {
		int nameId = creature.getObjectTemplate().getNameId();
		DescriptionId name = new DescriptionId(nameId * 2 + 1);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, nameId == 0 ? creature.getName() : name, rewardPoints));
	}

	private int getNpcBonus(int npcId) {
		switch (npcId) {
			case 207102: // Recovery Relics
			case 207116: // 1st Floor Stunner
				return 400;
			case 207099: // 2nd Floor Bomb
				return 200;
			case 219285: // Lurking Fangwing
			case 219376: // Plaza Wall
				return 50;
			default:
				return 0;
		}
	}

	private int getTime() {
		return instanceReward.getTime();
	}

	@Override
	public void onPlayerLogin(Player player) {
		sendEnterPacket(player);
	}

	@Override
	public void onInstanceCreate(final WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new HarmonyArenaReward(mapId, instanceId, instance);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		instanceReward.setInstanceStartTime();
		spawnRings();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// start round 1
				if (!isInstanceDestroyed && !instanceReward.isRewarded() && canStart()) {
					for (StaticDoor door : instance.getDoors().values()) {
						if (door != null) {
							door.setOpen(true);
						}
					}
					sendPacket(new SM_SYSTEM_MESSAGE(1401058));
					instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
					instanceReward.sendPacket(10, null);
					instanceReward.sendPacket(2, null);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// start round 2
							if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
								instanceReward.setRound(2);
								instanceReward.setRndZone();
								instanceReward.sendPacket(10, null);
								instanceReward.sendPacket(2, null);
								changeZone();
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										// start round 3
										if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
											instanceReward.setRound(3);
											instanceReward.setRndZone();
											sendPacket(new SM_SYSTEM_MESSAGE(1401203));
											instanceReward.sendPacket(10, null);
											instanceReward.sendPacket(2, null);
											changeZone();
											ThreadPoolManager.getInstance().schedule(new Runnable() {

												@Override
												public void run() {
													// end
													if (!isInstanceDestroyed && !instanceReward.isRewarded()) {
														instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
														reward();
														instanceReward.sendPacket(5, null);
													}
												}
											}, 180000);

										}
									}
								}, 180000);
							}
						}
					}, 180000);
				}
			}
		}, 120000);
	}

	protected void spawnRings() {
	}

	private boolean canStart() {
		if (instance.getPlayersInside().size() < 2) {
			sendPacket(new SM_SYSTEM_MESSAGE(1401303));
			instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
			reward();
			instanceReward.sendPacket(5, null);
			return false;
		}
		return true;
	}

	private void changeZone() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				for (Player player : instance.getPlayersInside()) {
					instanceReward.portToPosition(player);
					instanceReward.sendPacket(4, player.getObjectId());
				}
			}
		}, 1000);
	}

	private void sendPacket(final AionServerPacket packet) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, packet);
			}
		});
	}

	@Override
	public void onLeaveInstance(Player player) {
		clearDebuffs(player);
		PvPArenaPlayerReward playerReward = instanceReward.getPlayerReward(player.getObjectId());
		if (playerReward != null) {
			playerReward.endBoostMoraleEffect(player);
			instanceReward.clearPosition(playerReward.getPosition(), Boolean.FALSE);
			instanceReward.removePlayerReward(playerReward);
		}
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	private void clearDebuffs(Player player) {
		for (Effect ef : player.getEffectController().getAbnormalEffects()) {
			DispelCategoryType category = ef.getSkillTemplate().getDispelCategory();
			if (category == DispelCategoryType.DEBUFF || category == DispelCategoryType.DEBUFF_MENTAL || category == DispelCategoryType.DEBUFF_PHYSICAL || category == DispelCategoryType.ALL) {
				ef.endEffect();
				player.getEffectController().clearEffect(ef);
			}
		}
	}

	protected void reward() {
		if (instanceReward.canRewarded()) {
			for (Player player : instance.getPlayersInside()) {
				HarmonyGroupReward group = instanceReward.getHarmonyGroupReward(player.getObjectId());
				float playerRate = player.getRates().getGloryRewardRate();
				AbyssPointsService.addAp(player, group.getBasicAP() + group.getRankingAP() + (int) (group.getScoreAP() * playerRate));
				int courage = group.getBasicCourage() + group.getRankingCourage() + (int) (group.getScoreCourage() * playerRate);
				if (courage != 0) {
					ItemService.addItem(player, 186000137, courage);
				}
				int gloryTicket = group.getGloryTicket();
				if (gloryTicket != 0) {
					ItemService.addItem(player, 186000185, gloryTicket);
				}
			}
		}
		for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						if (CreatureActions.isAlreadyDead(player)) {
							PlayerReviveService.duelRevive(player);
						}
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 10000);
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PvPArenaPlayerReward ownerReward = instanceReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
		instanceReward.sendPacket(4, player.getObjectId());
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));

		if (lastAttacker != null && lastAttacker != player) {
			if (lastAttacker instanceof Player) {
				Player winner = (Player) lastAttacker;
				Integer winnerObj = winner.getObjectId();
				instanceReward.getHarmonyGroupReward(winnerObj).addPvPKillToPlayer();
				// notify Kill-Quests
				int worldId = winner.getWorldId();
				QuestEngine.getInstance().onKillInWorld(new QuestEnv(player, winner, 0, 0), worldId);
			}
		}
		updatePoints(player);
		return true;
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		final Integer object = player.getObjectId();
		final HarmonyGroupReward group = instanceReward.getHarmonyGroupReward(object);
		if (!instanceReward.isStartProgress() || group == null) {
			return;
		}
		int rewardetPoints = getNpcBonus(npc.getNpcId());
		int skill = instanceReward.getNpcBonusSkill(npc.getNpcId());
		if (skill != 0) {
			// useSkill(npc, player, skill >> 8, skill & 0xFF);
		}
		group.addPoints(rewardetPoints);
		sendSystemMsg(player, npc, rewardetPoints);
		instanceReward.sendPacket(10, object);
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		if (!isInstanceDestroyed) {
			instanceReward.portToPosition(player);
		}
		return true;
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		instanceReward.clear();
	}
}
