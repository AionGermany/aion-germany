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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionMemberEx;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.google.common.base.Predicate;

/**
 * @author lyahim
 * @modified antness
 */
public class PlayerInfo extends AdminCommand {

	public PlayerInfo() {
		super("playerinfo");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "syntax //playerinfo <playername> <loc | item | group | skill | legion | ap | chars | knownlist[info|add|remove] | visual[see|notsee]> ");
			return;
		}

		Player target = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "Selected player is not online!");
			return;
		}

		PacketSendUtility.sendMessage(admin, "\n[Info about " + target.getName() + "]\n-common: lv" + target.getLevel() + "(" + target.getCommonData().getExpShown() + " xp), " + target.getRace() + ", " + target.getPlayerClass() + "\n-IP: " + target.getClientConnection().getIP() + "\n" + "-MAC: " + target.getClientConnection().getMacAddress() + "\n" + "-account name: " + target.getClientConnection().getAccount().getName() + "\n");

		if (params.length < 2) {
			return;
		}

		if (params[1].equals("item")) {
			StringBuilder strbld = new StringBuilder("-items in inventory:\n");

			List<Item> items = target.getInventory().getItemsWithKinah();
			Iterator<Item> it = items.iterator();

			if (items.isEmpty()) {
				strbld.append("none\n");
			}
			else {
				while (it.hasNext()) {

					Item act = it.next();
					strbld.append("    " + act.getItemCount() + "(s) of " + ChatUtil.item(act.getItemTemplate().getTemplateId()) + "\n");
				}
			}
			items.clear();
			items = target.getEquipment().getEquippedItems();
			it = items.iterator();
			strbld.append("-equipped items:\n");
			if (items.isEmpty()) {
				strbld.append("none\n");
			}
			else {
				while (it.hasNext()) {
					Item act = it.next();
					strbld.append("    " + act.getItemCount() + "(s) of " + ChatUtil.item(act.getItemTemplate().getTemplateId()) + "\n");
				}
			}
			showAllLines(admin, strbld.toString());
		}
		else if (params[1].equals("group")) {
			final StringBuilder strbld = new StringBuilder("-group info:\n  Leader: ");

			PlayerGroup group = target.getPlayerGroup2();
			if (group == null) {
				PacketSendUtility.sendMessage(admin, "-group info: no group");
			}
			else {
				strbld.append(group.getLeader().getName() + "\n  Members:\n");
				group.applyOnMembers(new Predicate<Player>() {

					@Override
					public boolean apply(Player player) {
						strbld.append("    " + player.getName() + "\n");
						return true;
					}
				});
				PacketSendUtility.sendMessage(admin, strbld.toString());
			}

		}
		else if (params[1].equals("skill")) {
			StringBuilder strbld = new StringBuilder("-list of skills:\n");

			PlayerSkillEntry sle[] = target.getSkillList().getAllSkills();

			for (int i = 0; i < sle.length; i++) {
				strbld.append("    level " + sle[i].getSkillLevel() + " of " + sle[i].getSkillName() + "\n");
			}
			showAllLines(admin, strbld.toString());
		}
		else if (params[1].equals("loc")) {
			String chatLink = ChatUtil.position(target.getName(), target.getPosition());
			PacketSendUtility.sendMessage(admin, "- " + chatLink + "'s location:\n  mapid: " + target.getWorldId() + "\n  X: " + target.getX() + " Y: " + target.getY() + "Z: " + target.getZ() + "heading: " + target.getHeading());
		}
		else if (params[1].equals("legion")) {
			StringBuilder strbld = new StringBuilder();

			Legion legion = target.getLegion();
			if (legion == null) {
				PacketSendUtility.sendMessage(admin, "-legion info: no legion");
			}
			else {
				ArrayList<LegionMemberEx> legionmemblist = LegionService.getInstance().loadLegionMemberExList(legion, null);
				Iterator<LegionMemberEx> it = legionmemblist.iterator();

				strbld.append("-legion info:\n  name: " + legion.getLegionName() + ", level: " + legion.getLegionLevel() + "\n  members(online):\n");
				while (it.hasNext()) {
					LegionMemberEx act = it.next();
					strbld.append("    " + act.getName() + "(" + ((act.isOnline() == true) ? "online" : "offline") + ")" + act.getRank().toString() + "\n");
				}
			}
			showAllLines(admin, strbld.toString());
		}
		else if (params[1].equals("ap")) {
			PacketSendUtility.sendMessage(admin, "AP info about " + target.getName());
			PacketSendUtility.sendMessage(admin, "Total AP = " + target.getAbyssRank().getAp());
			PacketSendUtility.sendMessage(admin, "Total Kills = " + target.getAbyssRank().getAllKill());
			PacketSendUtility.sendMessage(admin, "Today Kills = " + target.getAbyssRank().getDailyKill());
			PacketSendUtility.sendMessage(admin, "Today AP = " + target.getAbyssRank().getDailyAP());
		}
		else if (params[1].equals("gp")) {
			PacketSendUtility.sendMessage(admin, "GP info about " + target.getName());
			PacketSendUtility.sendMessage(admin, "Total GP = " + target.getAbyssRank().getGp());
			PacketSendUtility.sendMessage(admin, "Total Kills = " + target.getAbyssRank().getAllKill());
			PacketSendUtility.sendMessage(admin, "Today Kills = " + target.getAbyssRank().getDailyKill());
			PacketSendUtility.sendMessage(admin, "Today GP = " + target.getAbyssRank().getDailyGP());
		}
		else if (params[1].equals("chars")) {
			PacketSendUtility.sendMessage(admin, "Others characters of " + target.getName() + " (" + target.getClientConnection().getAccount().size() + ") :");

			Iterator<PlayerAccountData> data = target.getClientConnection().getAccount().iterator();
			while (data.hasNext()) {
				PlayerAccountData d = data.next();
				if (d != null && d.getPlayerCommonData() != null) {
					PacketSendUtility.sendMessage(admin, d.getPlayerCommonData().getName());
				}
			}
		}
		else if (params[1].equals("knownlist")) {
			if (params[2].equals("info")) {
				PacketSendUtility.sendMessage(admin, "KnownList of " + target.getName());

				for (VisibleObject obj : target.getKnownList().getKnownObjects().values()) {
					PacketSendUtility.sendMessage(admin, obj.getName() + " objectId:" + obj.getObjectId());
				}
			}
			else if (params[2].equals("add")) {
				int objId = Integer.parseInt(params[3]);
				VisibleObject obj = World.getInstance().findVisibleObject(objId);
				if (obj != null && !target.getKnownList().getKnownObjects().containsKey(objId)) {
					target.getKnownList().getKnownObjects().put(objId, obj);
				}
			}
			else if (params[2].equals("remove")) {
				int objId = Integer.parseInt(params[3]);
				VisibleObject obj = World.getInstance().findVisibleObject(objId);
				if (obj != null && target.getKnownList().getKnownObjects().containsKey(objId)) {
					target.getKnownList().getKnownObjects().remove(objId);
				}
			}
		}
		else if (params[1].equals("visual")) {
			if (params[2].equals("info")) {
				PacketSendUtility.sendMessage(admin, "VisualList of " + target.getName());

				for (VisibleObject obj : target.getKnownList().getVisibleObjects().values()) {
					PacketSendUtility.sendMessage(admin, obj.getName() + " objectId:" + obj.getObjectId());
				}
			}
			else if (params[2].equals("see")) {
				int objId = Integer.parseInt(params[3]);
				Player player = World.getInstance().findPlayer(objId);
				target.getController().see(player);
			}
			else if (params[2].equals("notsee")) {
				int objId = Integer.parseInt(params[3]);
				Player player = World.getInstance().findPlayer(objId);
				target.getController().notSee(player, true);
			}
		}
		else {
			PacketSendUtility.sendMessage(admin, "bad switch!");
			PacketSendUtility.sendMessage(admin, "syntax //playerinfo <playername> <loc | item | group | skill | legion | ap | chars | knownlist[info|add|remove] | visual[see|notsee]> ");
		}
	}

	private void showAllLines(Player admin, String str) {
		int index = 0;
		String[] strarray = str.split("\n");

		while (index < strarray.length - 20) {
			StringBuilder strbld = new StringBuilder();
			for (int i = 0; i < 20; i++, index++) {
				strbld.append(strarray[index]);
				if (i < 20 - 1) {
					strbld.append("\n");
				}
			}
			PacketSendUtility.sendMessage(admin, strbld.toString());
		}
		int odd = strarray.length - index;
		StringBuilder strbld = new StringBuilder();
		for (int i = 0; i < odd; i++, index++) {
			strbld.append(strarray[index] + "\n");
		}
		PacketSendUtility.sendMessage(admin, strbld.toString());
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //playerinfo <playername> <loc | item | group | skill | legion | ap | chars> ");
	}
}
