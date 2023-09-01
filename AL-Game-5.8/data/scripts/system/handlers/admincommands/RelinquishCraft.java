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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.services.craft.CraftSkillUpdateService;
import com.aionemu.gameserver.services.craft.RelinquishCraftStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author synchro2
 */
public class RelinquishCraft extends AdminCommand {

	public RelinquishCraft() {
		super("relinquishcraft");
	}

	@Override
	public void execute(Player admin, String... params) {

		Player player;
		int skillId;
		boolean isExpert = false;
		String skillIdParam = "";
		String isExpertParam = "";

		if (params.length < 2 || params.length > 3) {
			PacketSendUtility.sendMessage(admin, "syntax //relinquishcraft <character_name | target> <skillId> <expert | master>");
			return;
		}

		if (params.length == 2) {
			VisibleObject target = admin.getTarget();
			if (target == null || !(target instanceof Player)) {
				PacketSendUtility.sendMessage(admin, "Select target first.");
				return;
			}

			player = (Player) target;
			skillIdParam = params[0];
			isExpertParam = params[1];

		}
		else {
			player = World.getInstance().findPlayer(Util.convertName(params[0]));
			skillIdParam = params[1];
			isExpertParam = params[2];

			if (player == null) {
				PacketSendUtility.sendMessage(admin, "The specified player is not online.");
				return;
			}
		}

		try {
			skillId = Integer.parseInt(skillIdParam);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "You must using only numbers in skillId.");
			return;
		}

		if (!isExpertParam.equalsIgnoreCase("Expert") && !isExpertParam.equalsIgnoreCase("Master")) {
			PacketSendUtility.sendMessage(admin, "Only master or expert.");
			return;
		}
		if (isExpertParam.equalsIgnoreCase("Expert")) {
			isExpert = true;
		}
		if (isExpertParam.equalsIgnoreCase("Master")) {
			isExpert = false;
		}

		PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
		int minValue = isExpert ? RelinquishCraftStatus.getExpertMinValue() : RelinquishCraftStatus.getMasterMinValue();
		int maxValue = isExpert ? RelinquishCraftStatus.getExpertMaxValue() : RelinquishCraftStatus.getMasterMaxValue();
		int skillMessageId = RelinquishCraftStatus.getSkillMessageId();

		if (!CraftSkillUpdateService.isCraftingSkill(skillId)) {
			PacketSendUtility.sendMessage(admin, "It's not skillId.");
			return;
		}

		if (skill == null || skill.getSkillLevel() < minValue || skill.getSkillLevel() > maxValue) {
			PacketSendUtility.sendMessage(admin, "Wrong skill level.");
			return;
		}

		skill.setSkillLvl(minValue);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, skillMessageId, false));
		RelinquishCraftStatus.removeRecipesAbove(player, skillId, minValue);
		RelinquishCraftStatus.deleteCraftStatusQuests(skillId, player, false);
		PacketSendUtility.sendMessage(admin, "Craft status successfull relinquished.");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //relinquishcraft <character_name | target> <skillId> <expert | master>");
	}
}
