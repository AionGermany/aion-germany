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

package instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * Author Rinzler
 */
@InstanceID(301320000)
public class LuckyOphidanBridgeInstance extends GeneralInstanceHandler {

	private Future<?> fireTask;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		Npc npc = instance.getNpc(231050); // Vera Siege Ballista.
		if (npc != null) {
			SkillEngine.getInstance().getSkill(npc, 21438, 10, npc).useNoAnimationSkill(); // Surkana Aetheric Field.
		}
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		switch (player.getRace()) {
			case ELYOS:
				sendMovie(player, 498);
				break;
			case ASMODIANS:
				sendMovie(player, 499);
				break;
			default:
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 230413: // Guard Post Generator.
				despawnNpc(npc);
				sendMsg(1401904);
				deleteNpc(802035);
				spawnDisableGuardPostGenerator();
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							spawn(802033, 667.11389f, 474.22995f, 600.48346f, (byte) 0);
							break;
						case ASMODIANS:
							spawn(802034, 667.11389f, 474.22995f, 600.48346f, (byte) 0);
							break;
						default:
							break;
					}
				}
				break;
			case 230414: // Northern Approach Post Generator.
				despawnNpc(npc);
				sendMsg(1401880);
				deleteNpc(802038);
				spawnDisableNorthernApproachPost();
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							spawn(802036, 524.84589f, 427.63959f, 621.21320f, (byte) 0);
							break;
						case ASMODIANS:
							spawn(802037, 524.84589f, 427.63959f, 621.21320f, (byte) 0);
							break;
						default:
							break;
					}
				}
				break;
			case 230415: // Southern Approach Post Generator.
				despawnNpc(npc);
				sendMsg(1401881);
				deleteNpc(802041);
				spawnDisableSouthernApproachPostGenerator();
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							spawn(802039, 602.73395f, 556.29407f, 591.52533f, (byte) 0);
							break;
						case ASMODIANS:
							spawn(802040, 602.73395f, 556.29407f, 591.52533f, (byte) 0);
							break;
						default:
							break;
					}
				}
				break;
			case 230416: // Defense Post Generator.
				despawnNpc(npc);
				sendMsg(1401882);
				deleteNpc(802044);
				spawnDisableDefensePostGenerator();
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							spawn(802042, 476.99838f, 523.91351f, 598.24933f, (byte) 0);
							break;
						case ASMODIANS:
							spawn(802043, 476.99838f, 523.91351f, 598.24933f, (byte) 0);
							break;
						default:
							break;
					}
				}
				spawn(701644, 435.42862f, 496.41296f, 604.8871f, (byte) 1); // Lucky Ophidan Bridge Control.
				break;
			case 231050: // The Vera Siege Ballista.
				deleteNpc(801956);
				fireTask.cancel(true);
				spawn(730868, 350.18478f, 490.73065f, 606.34015f, (byte) 1); // Lucky Ophidan Bridge Exit.
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
						}
					}
				});
				break;
			case 231052: // Vera Defender Archmage.
				Npc veraSiegeBallista = instance.getNpc(231050); // Vera Siege Ballista.
				veraSiegeBallista.getEffectController().removeEffect(21438); // Surkana Aetheric Field.
				break;
			case 231055: // Balic Defence Wall.
				despawnNpc(npc);
				startFinalTimer();
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 1560)); // 26 Minutes.
						}
					}
				});
				break;
			case 231056: // Surkana Defence Wall.
				despawnNpc(npc);
				break;
		}
	}

	private void startFinalTimer() {
		sendMsg(1401892);
		this.sendMessage(1401875, 15 * 60 * 1000);
		this.sendMessage(1401876, 20 * 60 * 1000);
		this.sendMessage(1401877, 25 * 60 * 1000);
		fireTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1401878);
				final Npc vera = instance.getNpc(231050); // The Vera Siege Ballista.
				if (vera != null) {
					SkillEngine.getInstance().getSkill(vera, 21442, 60, vera).useNoAnimationSkill();
				}
			}
		}, 150000);
	}

	private void spawnDisableGuardPostGenerator() {
		spawn(230417, 667.11389f, 474.22995f, 600.64978f, (byte) 0, 157);
	}

	private void spawnDisableNorthernApproachPost() {
		sendMsg(1401905);
		spawn(230418, 524.84589f, 427.63959f, 621.30267f, (byte) 0, 161);
	}

	private void spawnDisableSouthernApproachPostGenerator() {
		sendMsg(1401906);
		spawn(230419, 602.73395f, 556.29407f, 591.61957f, (byte) 0, 159);
	}

	private void spawnDisableDefensePostGenerator() {
		sendMsg(1401907);
		spawn(230420, 476.99838f, 523.91351f, 598.40454f, (byte) 0, 163);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701644: // Lucky Ophidan Bridge Control.
				despawnNpc(npc);
				doors.get(47).setOpen(true);
				break;
			case 701646: // Mobile Turret Elyos.
				despawnNpc(npc);
				sendMsg(1401879);
				SkillEngine.getInstance().getSkill(npc, 21434, 1, player).useNoAnimationSkill();
				break;
			case 701647: // Mobile Turret Asmodians.
				despawnNpc(npc);
				sendMsg(1401879);
				SkillEngine.getInstance().getSkill(npc, 21435, 1, player).useNoAnimationSkill();
				break;
		}
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21434);
		effectController.removeEffect(21435);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void sendMessage(final int msgId, long delay) {
		if (delay == 0) {
			this.sendMsg(msgId);
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					sendMsg(msgId);
				}
			}, delay);
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
