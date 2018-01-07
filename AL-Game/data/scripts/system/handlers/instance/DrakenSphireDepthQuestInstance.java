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

import java.util.Set;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(301520000)
public class DrakenSphireDepthQuestInstance extends GeneralInstanceHandler {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);

	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		switch (player.getRace()) {
			case ELYOS:
				// Start
				spawn(209823, 351.19638f, 192.30641f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209823, 347.11496f, 192.31288f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209824, 353.4427f, 182.63031f, 1684.2166f, (byte) 60); // Detachment Entry Leader
				spawn(209881, 351.18042f, 173.7679f, 1684.2166f, (byte) 30); // Detachment Demolisher
				spawn(209881, 347.099f, 173.77437f, 1684.2166f, (byte) 30); // Detachment Demolisher
				// Start Attacker
				spawn(209821, 392.2124f, 175.88617f, 1684.216f, (byte) 0); // Kaisinel Elite Mage
				spawn(209825, 423.92587f, 179.96892f, 1682.2166f, (byte) 0); // Kaisinel Elite Combatant
				spawn(209825, 403.4316f, 180.21465f, 1684.216f, (byte) 90); // Kaisinel Elite Combatant
				spawn(209825, 403.47638f, 186.16238f, 1684.216f, (byte) 30); // Kaisinel Elite Combatant
				spawn(209825, 435.78302f, 177.08868f, 1682.2167f, (byte) 0); // Kaisinel Elite Combatant
				spawn(209826, 392.62598f, 190.2562f, 1684.216f, (byte) 0); // Kaisinel Elite Archer
				spawn(209826, 431.28027f, 179.74289f, 1682.2166f, (byte) 45); // Kaisinel Elite Archer
				spawn(209827, 437.78033f, 184.66359f, 1682.2167f, (byte) 45); // Kaisinel Elite Mage
				spawn(209828, 407.54904f, 190.72981f, 1684.216f, (byte) 60); // Kaisinel Elite Priest
				spawn(209828, 407.55682f, 175.88617f, 1684.216f, (byte) 60); // Kaisinel Elite Priest
				spawn(209828, 426.9866f, 186.64618f, 1682.2166f, (byte) 0); // Kaisinel Elite Priest
				spawn(209828, 436.94476f, 194.55673f, 1682.2168f, (byte) 90); // Kaisinel Elite Priest
				break;
			case ASMODIANS:
				// TODO
				break;
			default:
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 237225: // Rapacious Kadena (right)
				instance.getDoors().get(378).setOpen(true);
				break;
			case 237226: // Rapacious Kadena (middle)
				instance.getDoors().get(376).setOpen(true);
				break;
			case 237227: // Rapacious Kadena (left)
				instance.getDoors().get(375).setOpen(true);
				break;
			case 237238: // Beritra (Dragon Form)
				spawn(731550, 132.3337f, 518.45123f, 1749.3623f, (byte) 0); // Drakenspire Depths Exit
				spawn(833580, 152.1312f, 518.49384f, 1749.5945f, (byte) 0); // Sacred Beast of Dragon Lord Beritra
				sendPacket(new SM_PLAY_MOVIE(0, 919));
				break;
		}
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 237224: // Fetid Phantomscorch Chimera
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000244, 1)); // Crossroads Choice Key
				break;
		}
	}

	@Override
	public void onLeaveInstance(Player player) {
	}

	@Override
	public void onInstanceDestroy() {
	}

	@Override
	public void onPlayerLogOut(Player player) {
		switch (player.getRace()) {
			case ELYOS:
				TeleportService2.teleportTo(player, 210070000, 77, 2758, 229);
				break;
			case ASMODIANS:
				TeleportService2.teleportTo(player, 210080000, 2991, 375, 309);
				break;
			default:
				break;
		}
	}

	@Override
	public void onExitInstance(Player player) {
		switch (player.getRace()) {
			case ELYOS:
				TeleportService2.teleportTo(player, 210070000, 77, 2758, 229);
				break;
			case ASMODIANS:
				TeleportService2.teleportTo(player, 210080000, 2991, 375, 309);
				break;
			default:
				break;
		}

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
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
