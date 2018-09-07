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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Magenik, Antraxx, Alcapwnd
 */
abstract public class AbstractGMHandler {

	protected String params;
	protected Player admin;
	protected Player target;

	public AbstractGMHandler(Player admin, String params) {
		this.admin = admin;
		this.params = params;
		getTarget();
	}

	public void getTarget() {
		VisibleObject t = admin.getTarget();
		if (t instanceof Player) {
			target = (Player) target;
			return;
		}
		target = null;
	}

	public boolean checkTarget() {
		if (target != null) {
			return true;
		}
		PacketSendUtility.sendMessage(admin, "Target not found or target is not an player");
		return false;
	}

}
