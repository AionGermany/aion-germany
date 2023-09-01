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
package ai.worlds.esterra;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("sky_island_rift_E")
public class Sky_Island_Rift_ElyosAI2 extends NpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 805897: // Sky Island Rift
			case 805898: // Sky Island Rift
			case 805899: // Sky Island Rift
			case 805900: // Sky Island Rift
			case 805901: // Sky Island Rift
			case 805902: // Sky Island Rift
			case 805903: // Sky Island Rift
			case 805904: // Sky Island Rift
			case 805905: // Sky Island Rift
			case 805906: // Sky Island Rift
				startLifeTask();
				break;
		}
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(Sky_Island_Rift_ElyosAI2.this);
			}
		}, 3600000);
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (player.isHighDaeva()) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 805897:
					TeleportService2.teleportTo(player, 210100000, 2317.9f, 2258.29f, 1116.813f, (byte) 108, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805898:
					TeleportService2.teleportTo(player, 210100000, 2642.66f, 2747.09f, 1117.5881f, (byte) 6, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805899:
					TeleportService2.teleportTo(player, 210100000, 2581.79f, 1429.55f, 1116.9115f, (byte) 35, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805900:
					TeleportService2.teleportTo(player, 210100000, 2571.52f, 499.2f, 1116.8452f, (byte) 16, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805901:
					TeleportService2.teleportTo(player, 210100000, 1539.2189f, 311.21225f, 1067.249f, (byte) 8, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805902:
					TeleportService2.teleportTo(player, 210100000, 715.2502f, 966.5452f, 1117.2305f, (byte) 67, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805903:
					TeleportService2.teleportTo(player, 210100000, 305.39f, 435.61f, 1117.0474f, (byte) 4, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805904:
					TeleportService2.teleportTo(player, 210100000, 500.43f, 1683.58f, 1117.03f, (byte) 1, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805905:
					TeleportService2.teleportTo(player, 210100000, 677.18f, 2593.64f, 1116.9187f, (byte) 64, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805906:
					TeleportService2.teleportTo(player, 210100000, 1651.76f, 2645.18f, 1116.8591f, (byte) 117, TeleportAnimation.BEAM_ANIMATION);
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}
