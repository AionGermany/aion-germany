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
 * @author ATracer
 */
public class AddDrop extends AdminCommand {

	public AddDrop() {
		super("adddrop");
	}

	@Override
	public void execute(Player player, String... params) {
		PacketSendUtility.sendMessage(player, "Now this is not implemented.");
		/*
		 * if (params.length != 5) { onFail(player, null); return; } try { final int mobId = Integer.parseInt(params[0]); final int itemId = Integer.parseInt(params[1]); final int min =
		 * Integer.parseInt(params[2]); final int max = Integer.parseInt(params[3]); final float chance = Float.parseFloat(params[4]); DropList dropList = DropRegistration.getInstance().getDropList();
		 * DropTemplate dropTemplate = new DropTemplate(mobId, itemId, min, max, chance, false); dropList.addDropTemplate(mobId, dropTemplate); DB.insertUpdate("INSERT INTO droplist (" +
		 * "`mob_id`, `item_id`, `min`, `max`, `chance`)" + " VALUES " + "(?, ?, ?, ?, ?)", new IUStH() {
		 * @Override public void handleInsertUpdate(PreparedStatement ps) throws SQLException { ps.setInt(1, mobId); ps.setInt(2, itemId); ps.setInt(3, min); ps.setInt(4, max); ps.setFloat(5, chance);
		 * ps.execute(); } }); } catch (Exception ex) { PacketSendUtility.sendMessage(player, "Only numbers are allowed"); return; }
		 */
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //adddrop <mobid> <itemid> <min> <max> <chance>");
	}
}
