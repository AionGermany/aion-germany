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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerCreativityPointsDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Falke_34, Frozenkiller
 */
public class AddCp extends AdminCommand {

	public AddCp() {
		super("addcp");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length != 1) {
			onFail(player, null);
			return;
		}

		Player target = null;

		if (player.getTarget() == null) {
			onFail(player, null);
		}
		else if (!(player.getTarget() instanceof Player)) {
			onFail(player, null);
		}
		else {
			target = (Player) player.getTarget();
		}

		String paramValue = params[0];
		int cp;
		try {
			cp = Integer.parseInt(paramValue);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "<cp> must be an Integer");
			return;
		}

		cp += target.getCommonData().getCreativityPoint();
		target.getCommonData().setCreativityPoint(cp);
		int size = DAOManager.getDAO(PlayerCreativityPointsDAO.class).getSlotSize(player.getObjectId());
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(cp, player.getCPStep()));
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS(player.getCreativityPoint(), player.getCPStep(), size, false));
		PacketSendUtility.sendMessage(player, "You added " + params[0] + " Creativity Points to the target.");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Select a target and use command this way:");
		PacketSendUtility.sendMessage(player, "syntax //AddCp <cp>");
	}
}
