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
package ai.instance.gardenKnowledge;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.ActionItemNpcAI2;

@AIName("Altar_Of_Earth") // 834006, 834019, 834020, 834021, 834022
public class Altar_Of_EarthAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (!player.getInventory().decreaseByItemId(185000266, 1)) { // Earthen Malachite.
			// You don’t have a Malachite of Earth to place on the altar.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403447));
			return;
		}
		switch (getNpcId()) {
			case 834006: // Altar Of Earth.
				// The Malachite of Earth emits a light and starts to float.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_IDEternity_02_SYSTEM_MSG_37, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(834006);
						AI2Actions.deleteOwner(Altar_Of_EarthAI2.this);
						spawn(834006, 1025.1476f, 774.97748f, 1033.6420f, (byte) 0, 291);
					}
				}, 5000);
				break;
			case 834019: // Altar Of Earth.
				// The Malachite of Earth emits a light and starts to float.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_IDEternity_02_SYSTEM_MSG_37, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(834019);
						AI2Actions.deleteOwner(Altar_Of_EarthAI2.this);
						spawn(834019, 1027.2802f, 771.84601f, 1033.6420f, (byte) 0, 340);
					}
				}, 5000);
				break;
			case 834020: // Altar Of Earth.
				// The Malachite of Earth emits a light and starts to float.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_IDEternity_02_SYSTEM_MSG_37, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(834020);
						AI2Actions.deleteOwner(Altar_Of_EarthAI2.this);
						spawn(834020, 1027.4769f, 777.98260f, 1033.6420f, (byte) 0, 299);
					}
				}, 5000);
				break;
			case 834021: // Altar Of Earth.
				// The Malachite of Earth emits a light and starts to float.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_IDEternity_02_SYSTEM_MSG_37, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(834021);
						AI2Actions.deleteOwner(Altar_Of_EarthAI2.this);
						spawn(834021, 1031.0382f, 776.67932f, 1033.6420f, (byte) 0, 387);
					}
				}, 5000);
				break;
			case 834022: // Altar Of Earth.
				// The Malachite of Earth emits a light and starts to float.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_IDEternity_02_SYSTEM_MSG_37, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(834017);
						despawnNpc(834022);
						AI2Actions.deleteOwner(Altar_Of_EarthAI2.this);
						spawn(834022, 1030.9221f, 772.90582f, 1033.6420f, (byte) 0, 395);
						spawn(834091, 974.25085f, 775.06488f, 1027.0811f, (byte) 0, 322);
					}
				}, 5000);
				break;
		}
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}

	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}
}
