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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

@InstanceID(400030000)
public class TransidiumAnnexInstance extends GeneralInstanceHandler {

	private Future<?> instanceTimer;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 277224: // Ahserion.
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						switch (Rnd.get(1, 3)) {
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053106, 1)); // Ahserion's Weapon Box.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053107, 1)); // Ahserion's Armor Box.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053108, 1)); // Ahserion's Accessory Box.
								break;
						}
					}
				}
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		// Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 277224: // Ahserion.
				sendMsg("[Congratulation]: you finish <Transidium Annex>");
				break;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openFirstDoors();
					sendMsg(1401838);
				}
			}, 60000);
		}
	}

	protected void openFirstDoors() {
		openDoor(176);
		openDoor(177);
		openDoor(178);
		openDoor(179);
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 277225: // Belus Camp Defense Cannon.
				SkillEngine.getInstance().getSkill(npc, 21731, 60, player).useNoAnimationSkill();
				despawnNpc(npc);
				break;
			case 277226: // Aspida Camp Defense Cannon.
				SkillEngine.getInstance().getSkill(npc, 21728, 60, player).useNoAnimationSkill();
				despawnNpc(npc);
				break;
			case 277227: // Atanatos Camp Defense Cannon.
				SkillEngine.getInstance().getSkill(npc, 21729, 60, player).useNoAnimationSkill();
				despawnNpc(npc);
				break;
			case 277228: // Disilon Camp Defense Cannon.
				SkillEngine.getInstance().getSkill(npc, 21730, 60, player).useNoAnimationSkill();
				despawnNpc(npc);
				break;
			case 297331: // Belus Chariot.
				SkillEngine.getInstance().getSkill(npc, 21582, 60, player).useNoAnimationSkill(); // Board The Chariot.
				despawnNpc(npc);
				break;
			case 297332: // Aspida Chariot.
				SkillEngine.getInstance().getSkill(npc, 21589, 60, player).useNoAnimationSkill(); // Board The Chariot.
				despawnNpc(npc);
				break;
			case 297333: // Atanatos Chariot.
				SkillEngine.getInstance().getSkill(npc, 21590, 60, player).useNoAnimationSkill(); // Board The Chariot.
				despawnNpc(npc);
				break;
			case 297334: // Disilon Chariot.
				SkillEngine.getInstance().getSkill(npc, 21591, 60, player).useNoAnimationSkill(); // Board The Chariot.
				despawnNpc(npc);
				break;
			case 297472: // Belus Chariot.
				SkillEngine.getInstance().getSkill(npc, 21579, 60, player).useNoAnimationSkill(); // Board The Ignus Engine.
				despawnNpc(npc);
				break;
			case 297473: // Aspida Chariot.
				SkillEngine.getInstance().getSkill(npc, 21586, 60, player).useNoAnimationSkill(); // Board The Ignus Engine.
				despawnNpc(npc);
				break;
			case 297474: // Atanatos Chariot.
				SkillEngine.getInstance().getSkill(npc, 21587, 60, player).useNoAnimationSkill(); // Board The Ignus Engine.
				despawnNpc(npc);
				break;
			case 297475: // Disilon Chariot.
				SkillEngine.getInstance().getSkill(npc, 21588, 60, player).useNoAnimationSkill(); // Board The Ignus Engine.
				despawnNpc(npc);
				break;
		}
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21728);
		effectController.removeEffect(21729);
		effectController.removeEffect(21730);
		effectController.removeEffect(21731);
		effectController.removeEffect(21579);
		effectController.removeEffect(21582);
		effectController.removeEffect(21586);
		effectController.removeEffect(21587);
		effectController.removeEffect(21588);
		effectController.removeEffect(21589);
		effectController.removeEffect(21590);
		effectController.removeEffect(21591);
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
