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
package ai.instance.beshmundirTemple;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Tiger0319
 * @author xTz
 * @author Gigi
 */
@AIName("plegeton")
public class PlegetonAI2 extends NpcAI2 {

	private boolean isStartTimer = false;

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == DialogAction.SETPRO1.id()) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			switch (getNpcId()) {
				case 799517:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 0, 448, 16777216));
					if (!isStartTimer) {
						isStartTimer = true;
						sendTimer();
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc npc = getPosition().getWorldMapInstance().getNpc(216586);
								if (npc != null && !npc.getLifeStats().isAlreadyDead()) {
									npc.getController().onDelete();
									PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
									getPosition().getWorldMapInstance().getDoors().get(467).setOpen(true);
								}
							}
						}, 420000);

					}
					TeleportService2.teleportTo(player, 300170000, 958.45233f, 430.4892f, 219.80301f);
					break;
				case 799518:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 0, 449, 16777216));
					TeleportService2.teleportTo(player, 300170000, 822.0199f, 465.1819f, 220.29918f);
					break;
				case 799519:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 0, 450, 16777216));
					TeleportService2.teleportTo(player, 300170000, 777.1054f, 300.39005f, 219.89926f);
					break;
				case 799520:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 0, 451, 16777216));
					TeleportService2.teleportTo(player, 300170000, 942.3092f, 270.91855f, 219.86185f);
					break;
			}
		}
		return true;
	}

	private void sendTimer() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 420));
			}
		});
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
}
