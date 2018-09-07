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
package ai.worlds;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Romanz
 * @modified Eloann
 * @rework FrozenKiller
 */
@AIName("fun_ride")
public class FunRideAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == DialogAction.SETPRO1.id()) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			if (player.isInPlayerMode(PlayerMode.RIDE)) {
				player.unsetPlayerMode(PlayerMode.RIDE);
			}
			switch (getNpcId()) {
				case 804811: // Seashell Waterslide
					run(player, 279001);
					break;
				case 804812: // Seashell Elevator
					run(player, 281001);
					break;
				case 804813: // Bubble Down
					run(player, 280001);
					break;
				case 804814: // Bubble Up
					run(player, 282001);
					break;
				case 804822: // Abandoned Bucket
					run(player, 286001);
					break;
				case 804823: // Bobbling Mushroom
					run(player, 284001);
					break;
				case 804824: // Squishy Jellyfish
					run(player, 283001);
					break;
				case 804825: // Tender Jellyfish
					run(player, 285001);
					break;
				case 805790: // Southern Iluman Butterfly
					run(player, 345001);
					break;
				case 805791: // Northern Iluman Butterfly
					run(player, 346001);
					break;
				case 805792: // Ancient Well
					run(player, 343001);
					break;
				case 805793: // Owllau Nest
					run(player, 344001);
					break;
				case 805794: // Northern Starturtle
					run(player, 349001);
					break;
				case 805795: // Southern Starturtle
					run(player, 350001);
					break;
				case 805796: // Western Tarhan Pulley
					run(player, 347001);
					break;
				case 805797: // Eastern Tarhan Pulley
					run(player, 348001);
					break;
				case 805765: // Southern Azured Well
					run(player, 329001);
					break;
				case 805766: // Northern Azured Well
					run(player, 330001);
					break;
				case 805767: // Winch Lever
					run(player, 327001);
					break;
				case 805768: // Rope Traverse
					run(player, 328001);
					break;
				case 805769: // Eastern Springleaf Passage
					run(player, 333001);
					break;
				case 805770: // Western Springleaf Passage
					run(player, 334001);
					break;
				case 805771: // Tranquil Fen Reed Patch
					run(player, 331001);
					break;
				case 805772: // Zenzen Reed Patch
					run(player, 332001);
					break;
			}
		}
		return true;
	}

	// Start Ride
	private void run(Player player, int id) {
		player.setState(CreatureState.FLIGHT_TELEPORT);
		player.unsetState(CreatureState.ACTIVE);
		player.setFlightTeleportId(id);
		PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, id, 0));
	}
}
