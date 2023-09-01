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
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Falke_34
 */
@InstanceID(301700000)
public class TreasureIslandOfCourageInstance extends GeneralInstanceHandler {

    @SuppressWarnings("unused")
	private long startTime;
    private Map<Integer, StaticDoor> doors;
    private Future<?> instanceTimer;
    protected boolean isInstanceDestroyed = false;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openFirstDoors();
				}
			}, 60000);
		}
	}

	protected void openFirstDoors() {
		openDoor(8);
		openDoor(93);
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
			case 835544: // Ancient Hero's Shoe
			case 835592:
				SkillEngine.getInstance().applyEffectDirectly(11277, player, player, 4000 * 1);
				ItemService.addItem(player, 190100295, 1); // Unicorn
				ItemService.addItem(player, 169300017, 1); // Hero`s Might
				break;
			case 835545: // Ancient Hero's Shield
			case 835593:
				SkillEngine.getInstance().applyEffectDirectly(11278, player, player, 4000 * 1);
				ItemService.addItem(player, 190100295, 1); // Unicorn
				ItemService.addItem(player, 169300017, 1); // Hero`s Might
				break;
			case 835546: // Ancient Hero's Trap
			case 835594:
				SkillEngine.getInstance().applyEffectDirectly(11279, player, player, 4000 * 1);
				ItemService.addItem(player, 190100295, 1); // Unicorn
				ItemService.addItem(player, 169300017, 1); // Hero`s Might
				break;
			case 835547: // Ancient Hero's Hook
			case 835794:
				SkillEngine.getInstance().applyEffectDirectly(11280, player, player, 4000 * 1);
				ItemService.addItem(player, 190100295, 1); // Unicorn
				ItemService.addItem(player, 169300017, 1); // Hero`s Might
				break;
		}
	}

	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(190100295, 1);
		storage.decreaseByItemId(169300017, 1);
		player.getEffectController().removeEffect(11277);
		player.getEffectController().removeEffect(11278);
		player.getEffectController().removeEffect(11279);
		player.getEffectController().removeEffect(11280);
	}

	private void stopInstanceTask() {
		if (instanceTimer != null) {
			instanceTimer.cancel(true);
		}
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
		stopInstanceTask();
	}

	@Override
	public void onExitInstance(Player player) {
		removeItems(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
