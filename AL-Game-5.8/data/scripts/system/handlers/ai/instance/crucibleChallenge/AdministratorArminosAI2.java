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
package ai.instance.crucibleChallenge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author xTz
 */
@AIName("administratorarminos")
public class AdministratorArminosAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_8_300320000"))) {
				spawn(217827, 1250.1598f, 237.97736f, 405.3968f, (byte) 0);
				spawn(217828, 1250.1598f, 239.97736f, 405.3968f, (byte) 0);
				spawn(217829, 1250.1598f, 235.97736f, 405.3968f, (byte) 0);
			}
			else if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_13_300320000"))) {
				spawn(217827, 1265.9661f, 793.5348f, 436.64008f, (byte) 0);
				spawn(217828, 1265.9661f, 789.5348f, 436.6402f, (byte) 0);
				spawn(217829, 1265.9661f, 791.5348f, 436.64014f, (byte) 0);
			}
			AI2Actions.deleteOwner(this);
		}
		return true;
	}
}
