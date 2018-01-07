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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Source
 */
public class Damage extends AdminCommand {

	public Damage() {
		super("damage");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length > 1) {
			onFail(admin, null);
		}

		VisibleObject target = admin.getTarget();
		if (target == null) {
			PacketSendUtility.sendMessage(admin, "No target selected");
		}
		else if (target instanceof Creature) {
			Creature creature = (Creature) target;
			int dmg;
			try {
				String percent = params[0];
				Pattern damage = Pattern.compile("([^%]+)%");
				Matcher result = damage.matcher(percent);

				if (result.find()) {
					dmg = Integer.parseInt(result.group(1));

					if (dmg < 100) {
						creature.getController().onAttack(admin, (int) (dmg / 100f * creature.getLifeStats().getMaxHp()), true);
					}
					else {
						creature.getController().onAttack(admin, creature.getLifeStats().getMaxHp() + 1, true);
					}
				}
				else {
					creature.getController().onAttack(admin, Integer.parseInt(params[0]), true);
				}
			}
			catch (Exception ex) {
				onFail(admin, null);
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //damage <dmg | dmg%>" + "\n<dmg> must be a number.");
	}
}
