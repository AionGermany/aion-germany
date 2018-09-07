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
package ai.worlds.nosra;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("sky_island_rift_A")
public class Sky_Island_Rift_AsmodiansAI2 extends NpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 805862: // Sky Island Rift
			case 805863: // Sky Island Rift
			case 805864: // Sky Island Rift
			case 805865: // Sky Island Rift
			case 805866: // Sky Island Rift
			case 805867: // Sky Island Rift
			case 805868: // Sky Island Rift
			case 805869: // Sky Island Rift
			case 805870: // Sky Island Rift
			case 805871: // Sky Island Rift
				startLifeTask();
				break;
		}
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(Sky_Island_Rift_AsmodiansAI2.this);
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
				case 805862:
					TeleportService2.teleportTo(player, 220110000, 1971.852f, 1133.1721f, 876.35187f, (byte) 85, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805863:
					TeleportService2.teleportTo(player, 220110000, 2540.828f, 238.66917f, 884.73773f, (byte) 19, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805864:
					TeleportService2.teleportTo(player, 220110000, 2432.7473f, 2032.2451f, 845.83813f, (byte) 107, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805865:
					TeleportService2.teleportTo(player, 220110000, 2230.0217f, 2939.4053f, 930.6146f, (byte) 34, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805866:
					TeleportService2.teleportTo(player, 220110000, 1574.717f, 2993.7537f, 786.12787f, (byte) 110, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805867:
					TeleportService2.teleportTo(player, 220110000, 803.5246f, 2422.3174f, 806.5194f, (byte) 103, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805868:
					TeleportService2.teleportTo(player, 220110000, 218.0479f, 2862.6848f, 847.5248f, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805869:
					TeleportService2.teleportTo(player, 220110000, 398.20267f, 1621.8885f, 920.9332f, (byte) 114, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805870:
					TeleportService2.teleportTo(player, 220110000, 558.4859f, 557.29987f, 921.48206f, (byte) 50, TeleportAnimation.BEAM_ANIMATION);
					break;
				case 805871:
					TeleportService2.teleportTo(player, 220110000, 1678.5807f, 613.7282f, 955.04645f, (byte) 14, TeleportAnimation.BEAM_ANIMATION);
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}
