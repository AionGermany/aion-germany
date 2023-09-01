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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author loleron pieced together from DyeAction.java, Set.java
 */
public class Dye extends AdminCommand {

	public Dye() {
		super("dye");
	}

	@Override
	public void execute(Player admin, String... params) {
		Player target;

		// Add a check to prevent players to dye other people
		if (admin.getAccessLevel() > 0 && admin.getTarget() instanceof Player) {
			target = (Player) admin.getTarget();
		}
		else {
			target = admin;
		}

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "You should select a target first!");
			return;
		}

		if (params.length == 0 || params.length > 2) {
			PacketSendUtility.sendMessage(admin, "syntax //dye <dye color|hex color|no>");
			return;
		}

		long price = CustomConfig.DYE_PRICE;
		if (admin.getInventory().getKinah() < price && !admin.isGM()) {
			PacketSendUtility.sendMessage(admin, "You need " + CustomConfig.DYE_PRICE + " kinah to dye yourself.");
			return;
		}

		String color = "";
		if (params.length == 2) {
			if (params[1].equalsIgnoreCase("petal")) {
				color = params[0];
			}
			else {
				color = params[0] + " " + params[1];
			}
		}
		else {
			color = params[0];
		}

		int rgb = 0;
		int bgra = 0;

		if (color.equalsIgnoreCase("turquoise")) {
			color = "198d94";
		} // 169200001, 169201001
		else if (color.equalsIgnoreCase("blue")) {
			color = "1f87f5";
		} // 169200002, 169201002
		else if (color.equalsIgnoreCase("brown")) {
			color = "66250e";
		} // 169200003, 169201003
		else if (color.equalsIgnoreCase("purple")) {
			color = "c38df5";
		} // 169200004, 169201004
		else if (color.equalsIgnoreCase("true red")) {
			color = "c22626";
		} // 169200005, 169201005, 169220001, 169230001, 169231001
		else if (color.equalsIgnoreCase("true white") || color.equalsIgnoreCase("white")) {
			color = "ffffff";
		} // 169200006, 169201006, 169220002, 169231002
		else if (color.equalsIgnoreCase("black") || color.equalsIgnoreCase("true black")) {
			color = "000000";
		} // 169200007, 169201007, 169230008, 169231008
		else if (color.equalsIgnoreCase("hot orange")) {
			color = "e36b00";
		} // 169201008, 169220004, 169230009, 169231009
		else if (color.equalsIgnoreCase("rich purple")) {
			color = "440b9a";
		} // 169201009, 169220005, 169230007, 169231003
		else if (color.equalsIgnoreCase("hot pink")) {
			color = "d60b7e";
		} // 169201010, 169220006, 169230010, 169231010
		else if (color.equalsIgnoreCase("mustard")) {
			color = "fcd251";
		} // 169201011, 169220007, 169230004, 169231004
		else if (color.equalsIgnoreCase("green tea")) {
			color = "61bb4f";
		} // 169201012, 169220008, 169230003, 169231005
		else if (color.equalsIgnoreCase("olive green")) {
			color = "5f730e";
		} // 169201013, 169220009, 169230005, 169231006
		else if (color.equalsIgnoreCase("deep blue")) {
			color = "14398b";
		} // 169201014, 169220010, 169230006, 169231007
		else if (color.equalsIgnoreCase("romantic purple")) {
			color = "80185d";
		} // 169230011
		else if (color.equalsIgnoreCase("wiki")) {
			color = "85e831";
		} // 169240001
		else if (color.equalsIgnoreCase("omblic")) {
			color = "ff5151";
		} // 169240002
		else if (color.equalsIgnoreCase("meon")) {
			color = "afaf26";
		} // 169240003
		else if (color.equalsIgnoreCase("ormea")) {
			color = "ffaa11";
		} // 169240004
		else if (color.equalsIgnoreCase("tange")) {
			color = "bd5fff";
		} // 169240005
		else if (color.equalsIgnoreCase("ervio")) {
			color = "3bb7fe";
		} // 169240006
		else if (color.equalsIgnoreCase("lunime")) {
			color = "c7af27";
		} // 169240007
		else if (color.equalsIgnoreCase("vinna")) {
			color = "052775";
		} // 169240008
		else if (color.equalsIgnoreCase("kirka")) {
			color = "ca84ff";
		} // 169240009
		else if (color.equalsIgnoreCase("brommel")) {
			color = "c7af27";
		} // 169240010
		else if (color.equalsIgnoreCase("pressa")) {
			color = "ff9d29";
		} // 169240011
		else if (color.equalsIgnoreCase("merone")) {
			color = "8df598";
		} // 169240012
		else if (color.equalsIgnoreCase("kukar")) {
			color = "ffff96";
		} // 169240013
		else if (color.equalsIgnoreCase("leopis")) {
			color = "31dfff";
		} // 169240014

		try {
			rgb = Integer.parseInt(color, 16);
			bgra = 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
		}

		catch (NumberFormatException e) {
			if (!color.equalsIgnoreCase("no")) {
				PacketSendUtility.sendMessage(admin, "[Dye] Can't understand: " + color);
				return;
			}
		}

		if (!admin.isGM())
			admin.getInventory().decreaseKinah(price);

		for (Item targetItem : target.getEquipment().getEquippedItemsWithoutStigma()) {
			if (color.equals("no")) {
				targetItem.setItemColor(0);
			}
			else {
				targetItem.setItemColor(bgra);
			}
			ItemPacketService.updateItemAfterInfoChange(target, targetItem);
		}
		PacketSendUtility.broadcastPacket(target, new SM_UPDATE_PLAYER_APPEARANCE(target.getObjectId(), target.getEquipment().getEquippedForApparence()), true);
		target.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		if (target.getObjectId() != admin.getObjectId()) {
			PacketSendUtility.sendMessage(target, "You have been dyed by " + admin.getName() + "!");
		}
		PacketSendUtility.sendMessage(admin, "Dyed " + target.getName() + " successfully!");
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
