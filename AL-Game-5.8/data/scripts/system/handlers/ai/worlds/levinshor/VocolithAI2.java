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

package ai.worlds.levinshor;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("vocolith")
public class VocolithAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getInventory().getFirstItemByItemId(185000216) != null) { // Ancestor's Relic.
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
		else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_ADVANCE_FNAMED_FAIL);
		}
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(185000216, 1)) {
			switch (getNpcId()) {
				// Vocolith
				case 804573:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 155.23886f, 896.0941f, 241.9881f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 155.23886f, 896.0941f, 241.9881f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 155.23886f, 896.0941f, 241.9881f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 155.23886f, 896.0941f, 241.9881f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804574:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 238.54105f, 1581.0961f, 227.63638f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 238.54105f, 1581.0961f, 227.63638f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 238.54105f, 1581.0961f, 227.63638f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 238.54105f, 1581.0961f, 227.63638f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804575:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 605.8056f, 1462.2771f, 276.73804f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 605.8056f, 1462.2771f, 276.73804f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 605.8056f, 1462.2771f, 276.73804f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 605.8056f, 1462.2771f, 276.73804f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804579:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 1106.4177f, 1231.3401f, 308.5724f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 1106.4177f, 1231.3401f, 308.5724f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 1106.4177f, 1231.3401f, 308.5724f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 1106.4177f, 1231.3401f, 308.5724f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804580:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 1663.793f, 612.8768f, 227.10474f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 1663.793f, 612.8768f, 227.10474f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 1663.793f, 612.8768f, 227.10474f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 1663.793f, 612.8768f, 227.10474f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804581:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 1491.7947f, 313.15384f, 253.56389f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 1491.7947f, 313.15384f, 253.56389f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 1491.7947f, 313.15384f, 253.56389f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 1491.7947f, 313.15384f, 253.56389f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804582:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 1211.9669f, 584.015f, 277.96094f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 1211.9669f, 584.015f, 277.96094f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 1211.9669f, 584.015f, 277.96094f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 1211.9669f, 584.015f, 277.96094f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
				// Vocolith
				case 804583:
					switch (Rnd.get(1, 4)) {
						case 1:
							announceLevinshorBoss();
							spawn(287276, 894.8337f, 838.7992f, 313.66245f, (byte) 17); // Lava Arm Cruego.
							break;
						case 2:
							announceLevinshorBoss();
							spawn(287277, 894.8337f, 838.7992f, 313.66245f, (byte) 17); // Cruel Lamia.
							break;
						case 3:
							announceLevinshorBoss();
							spawn(235219, 894.8337f, 838.7992f, 313.66245f, (byte) 17); // Destoyer Feld.
							break;
						case 4:
							announceLevinshorBoss();
							spawn(235220, 894.8337f, 838.7992f, 313.66245f, (byte) 17); // Ruthless Tyranicca.
							break;
					}
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AI2Actions.deleteOwner(this);
		AI2Actions.scheduleRespawn(this);
		return true;
	}

	private void announceLevinshorBoss() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_ADVANCE_FNAMED_SPAWN);
			}
		});
	}
}
