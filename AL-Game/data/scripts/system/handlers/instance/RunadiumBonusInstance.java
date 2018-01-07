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
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Alcapwnd
 */

@InstanceID(301330000)
public class RunadiumBonusInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	private int guardGraendalKilled;
	private int IllusionGraendalKilled;

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		player.getEffectController().removeEffect(218611);
		player.getEffectController().removeEffect(218610);
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 284377: // danuar reliquary novun
			case 284378: // idean lapilima
			case 284379: // idean obscura
				guardGraendalKilled++;
				if (guardGraendalKilled == 1) {
				}
				else if (guardGraendalKilled == 2) {
				}
				else if (guardGraendalKilled == 3) {
					spawn(231304, 256.52f, 257.87f, 241.78f, (byte) 90); // Cursed Queen Modor
				}
				despawnNpc(npc);
				break;
			case 284383:
			case 284384:
			case 284428:
			case 284429:
				IllusionGraendalKilled++;
				if (IllusionGraendalKilled == 4) {
					spawn(284376, 256.5205f, 257.8747f, 241.7870f, (byte) 90); // Enraged Queen Modor
				}
				despawnNpc(npc);
				break;
			case 284380:
			case 284381:
			case 284382:
			case 284659:
			case 284660:
			case 284661:
			case 284662:
			case 284663:
				despawnNpc(npc);
				break;
			case 284376:
				sendMsg(1401893);
				despawnNpc(getNpc(284377));
				despawnNpc(getNpc(284378));
				despawnNpc(getNpc(284379));
				despawnNpc(getNpc(284380));
				despawnNpc(getNpc(284381));
				despawnNpc(getNpc(284382));
				despawnNpc(getNpc(284659));
				despawnNpc(getNpc(284660));
				despawnNpc(getNpc(284661));
				despawnNpc(getNpc(284662));
				despawnNpc(getNpc(284663));

				spawn(730843, 256.2082f, 250.2959f, 241.8779f, (byte) 30); // exit
				return;
		}
	}

	@Override
	public void onInstanceDestroy() {
		doors.clear();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 231304:
			case 284376:
				int index = dropItems.size() + 1;
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052499, 1)); // Danuar Reliquary Expedition's Excavation Box
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000382, 1)); // Quest Item - Damaged Research Diary from Runadium
					}
				}
				break;
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
