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
package admincommands;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ginho1
 */
public class ChangeRace extends AdminCommand {

	public ChangeRace() {
		super("changerace");
	}

	@Override
	public void execute(Player admin, String... params) {

		if (admin.getCommonData().getRace() == Race.ELYOS) {
			admin.getCommonData().setRace(Race.ASMODIANS);
		}
		else {
			admin.getCommonData().setRace(Race.ELYOS);
		}

		admin.clearKnownlist();
		PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
		admin.updateKnownlist();
	}
}
