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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author xTz
 */
public class Ring extends AdminCommand {

	public Ring() {
		super("ring");
	}

	@Override
	public void execute(Player admin, String... params) {
		double direction = Math.toRadians(admin.getHeading()) - 0.5f;
		if (direction < 0) {
			direction += 2f;
		}
		float x1 = (float) (Math.cos(Math.PI * direction) * 6);
		float y1 = (float) (Math.sin(Math.PI * direction) * 6);
		PacketSendUtility.sendMessage(admin, "center:" + admin.getX() + " " + admin.getY() + " " + admin.getZ());
		PacketSendUtility.sendMessage(admin, "p1:" + admin.getX() + " " + admin.getY() + " " + (admin.getZ() + 6f));
		PacketSendUtility.sendMessage(admin, "p2:" + (admin.getX() + x1) + " " + (admin.getY() + y1) + " " + admin.getZ());
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax; //ring");
	}
}
