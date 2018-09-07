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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Source, kecimis
 */
public class UseSkill extends AdminCommand {

	private final String syntax = "Syntax: //skill <skillId> <skillLevel> [true|target] <duration>";

	public UseSkill() {
		super("useskill");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length > 4 || params.length <= 0) {
			onFail(admin, null);
			return;
		}

		if (params[0].equalsIgnoreCase("help")) {
			PacketSendUtility.sendMessage(admin, syntax + " \n" + "TARGET - targetted creature will use skill on its target.\n" + "TRUE - effect of skill is applied without any checks.\n" + "If you want to add duration, you have to use TRUE!\n" + "Example: //useskill 1968 1 or //useskill 1968 1 true 1\n" + "Duration is in seconds, 0 means its taken from skill_template.");
			return;
		}

		VisibleObject target = admin.getTarget();

		int skillId = 0;
		int skillLevel = 0;

		try {
			skillId = Integer.parseInt(params[0]);
			skillLevel = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "SkillId and skillLevel need to be an integer.");
			return;
		}

		if (target == null || !(target instanceof Creature)) {
			PacketSendUtility.sendMessage(admin, "You must select a target!");
			return;
		}

		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);

		if (skillTemplate == null) {
			PacketSendUtility.sendMessage(admin, "No skill template id:" + skillId);
			return;
		}

		if (params.length >= 3) {
			if (params[2].equalsIgnoreCase("true")) {
				int time = 0;
				if (params.length == 4) {
					try {
						time = Integer.parseInt(params[3]);
					}
					catch (NumberFormatException e) {
						PacketSendUtility.sendMessage(admin, "Time has to be integer!");
						return;
					}
				}
				if (time < 0 || time > 86400) {
					PacketSendUtility.sendMessage(admin, "Time has to be in range 0 - 86400!");
					return;
				}

				SkillEngine.getInstance().applyEffectDirectly(skillId, admin, (Creature) target, (time * 1000));

				PacketSendUtility.sendMessage(admin, "SkillId:" + skillId + " was applied on target " + target.getName());
			}
			else if (params[2].equalsIgnoreCase("target")) {
				if (target.getTarget() == null || !(target.getTarget() instanceof Creature)) {
					PacketSendUtility.sendMessage(admin, "Target must select some creature!");
					return;
				}

				this.useSkill(admin, (Creature) target, (Creature) target.getTarget(), skillId, skillLevel);
				PacketSendUtility.sendMessage(admin, "Target: " + target.getName() + " used skillId:" + skillId + " on target " + target.getTarget().getName());
			}
			else {
				onFail(admin, null);
				return;
			}
		}
		else {
			this.useSkill(admin, admin, (Creature) target, skillId, skillLevel);
			PacketSendUtility.sendMessage(admin, "SkillId:" + skillId + " was used on target " + target.getName());
		}
	}

	private void useSkill(Player admin, Creature effector, Creature target, int skillId, int skillLevel) {
		Skill skill = SkillEngine.getInstance().getSkill(effector, skillId, skillLevel, target);
		if (skill != null) {
			skill.useNoAnimationSkill();
		}
		else {
			onFail(admin, null);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, syntax + " \n" + "or use //useskill help.");
	}
}
