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
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Lilith
 */
public class AddCraft extends AdminCommand {

	private static final String LIST = "list";
	private static final String ESSENCETAPPING = "essencetapping";
	private static final String AETHERTAPPING = "aethertapping";
	private static final String COOKING = "cooking";
	private static final String WEAPONSMITHING = "weaponsmithing";
	private static final String ARMORSMITHING = "armorsmithing";
	private static final String TAILORING = "tailoring";
	private static final String ALCHEMY = "alchemy";
	private static final String HANDICRAFTING = "handicrafting";
	private static final String CONSTRUCTION = "construction";

	public AddCraft() {
		super("addcraft");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		}
		if (LIST.equalsIgnoreCase(params[0])) {
			showList(player);
		}
		else if (ESSENCETAPPING.equalsIgnoreCase(params[0])) {
			addEssencetapping(player, params);
		}
		else if (AETHERTAPPING.equalsIgnoreCase(params[0])) {
			addAethertapping(player, params);
		}
		else if (COOKING.equalsIgnoreCase(params[0])) {
			addCooking(player, params);
		}
		else if (WEAPONSMITHING.equalsIgnoreCase(params[0])) {
			addWeaponsmithing(player, params);
		}
		else if (ARMORSMITHING.equalsIgnoreCase(params[0])) {
			addArmorsmithing(player, params);
		}
		else if (TAILORING.equalsIgnoreCase(params[0])) {
			addTailoring(player, params);
		}
		else if (ALCHEMY.equalsIgnoreCase(params[0])) {
			addAlchemy(player, params);
		}
		else if (HANDICRAFTING.equalsIgnoreCase(params[0])) {
			addHandicrafting(player, params);
		}
		else if (CONSTRUCTION.equalsIgnoreCase(params[0])) {
			addConstruction(player, params);
		}
	}

	protected void showList(Player player) {
		PacketSendUtility.sendMessage(player, "\n" + "AddCraft Name List:\n" + "- Essencetapping\n" + "- Aethertapping\n" + "- Cooking\n" + "- Weaponsmithing\n" + "- Armorsmithing\n" + "- Tailoring\n" + "- Alchemy\n" + "- Handicrafting\n" + "- Construction");
	}

	protected void addEssencetapping(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 30002, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Essencetapping.");
			PacketSendUtility.sendMessage(targetpl, "Your Essencetapping is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addAethertapping(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 30003, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Aethertapping.");
			PacketSendUtility.sendMessage(targetpl, "Your Aethertapping is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addCooking(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40001, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Cooking.");
			PacketSendUtility.sendMessage(targetpl, "Your Cooking is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addWeaponsmithing(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40002, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Weaponsmithing.");
			PacketSendUtility.sendMessage(targetpl, "Your Weaponsmithing is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addArmorsmithing(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40003, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Armorsmithing.");
			PacketSendUtility.sendMessage(targetpl, "Your Armorsmithing is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addTailoring(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40004, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Tailoring.");
			PacketSendUtility.sendMessage(targetpl, "Your Tailoring is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addAlchemy(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40007, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Alchemy.");
			PacketSendUtility.sendMessage(targetpl, "Your Alchemy is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addHandicrafting(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40008, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Handicrafting.");
			PacketSendUtility.sendMessage(targetpl, "Your Handicrafting is now on Skill Level " + params[1] + ".");
		}
	}

	protected void addConstruction(Player player, String[] params) {

		VisibleObject target = player.getTarget();

		int skillLevel = 0;

		try {
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameters need to be an integer.");
			return;
		}

		if (target instanceof Player) {
			Player targetpl = (Player) target;
			targetpl.getSkillList().addSkill(targetpl, 40010, skillLevel);
			PacketSendUtility.sendMessage(player, "You acquired the skill Construction.");
			PacketSendUtility.sendMessage(targetpl, "Your Construction is now on Skill Level " + params[1] + ".");
		}
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "\n" + "AddCraft Help:\n" + "Please target yourself and type\n" + "//addcraft <CraftName> <SkillLevel>\n" + "or if you need a list of possible craft names type\n" + "//addcraft list");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "\n" + "AddCraft Help:\n" + "Please target yourself and type\n" + "//addcraft <CraftName> <SkillLevel>\n" + "or if you need a list of possible craft names type\n" + "//addcraft list");
	}

}
