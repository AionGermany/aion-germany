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
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.services.MotionLoggingService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author kecimis
 */
public class Motion extends AdminCommand implements StatOwner {

	public Motion() {
		super("motion");
	}

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.gameserver.utils.chathandlers.AdminCommand#execute(com.aionemu.gameserver.model.gameobjects.player.Player, java.lang.String[])
	 */
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			onFail(player, "");
			return;
		}
		if (params[0].equalsIgnoreCase("help")) {
			onFail(player, "");
			PacketSendUtility.sendMessage(player, "//motion start - starts MotionLoggingService, plus loads data from db");
			PacketSendUtility.sendMessage(player, "//motion advanced - turns on/of advanced logging info");
			PacketSendUtility.sendMessage(player, "//motion as (value) - adds attack speed");
			PacketSendUtility.sendMessage(player, "//motion analyze - creats .txt files in SERVER_DIR/motions with detailed info about motions");
			PacketSendUtility.sendMessage(player, "//motion savetosql - saves content of MotionLoggingService to database");
			PacketSendUtility.sendMessage(player, "//motion createxml - create new_motion_times.xml in static_data/skills");
		}
		else if (params[0].equalsIgnoreCase("start")) {
			MotionLoggingService.getInstance().start();
			PacketSendUtility.sendMessage(player, "MotionLogginService was started!\nData loaded from DB.");
		}
		else if (params[0].equalsIgnoreCase("analyze")) {
			MotionLoggingService.getInstance().createAnalyzeFiles();
			PacketSendUtility.sendMessage(player, "Created testing files!");
		}
		else if (params[0].equalsIgnoreCase("createxml")) {
			MotionLoggingService.getInstance().createFinalFile();
			PacketSendUtility.sendMessage(player, "Created new_motion_times.xml in data/static_data/skills!");
		}
		else if (params[0].equalsIgnoreCase("savetosql")) {
			MotionLoggingService.getInstance().saveToSql();
			PacketSendUtility.sendMessage(player, "MotionLog data saved to sql!");
		}
		else if (params[0].equalsIgnoreCase("advanced")) {
			MotionLoggingService.getInstance().setAdvancedLog((!MotionLoggingService.getInstance().getAdvancedLog()));
			PacketSendUtility.sendMessage(player, "AdvancedLog set to: " + MotionLoggingService.getInstance().getAdvancedLog());
		}
		else if (params[0].equalsIgnoreCase("as")) {
			int parameter = 10000;
			if (params.length == 2) {
				try {
					parameter = Integer.parseInt(params[1]);
				}
				catch (NumberFormatException e) {
					PacketSendUtility.sendMessage(player, "Parameter should number");
					return;
				}
			}
			this.addAttackSpeed(player, -parameter);
			PacketSendUtility.sendMessage(player, "Attack Speed updated");
		}
		else {
			onFail(player, "");
		}
	}

	private void addAttackSpeed(Player player, int i) {
		if (i == 0) {
			player.getGameStats().endEffect(this);
		}
		else {
			List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
			modifiers.add(new StatAddFunction(StatEnum.ATTACK_SPEED, i, true));
			player.getGameStats().endEffect(this);
			player.getGameStats().addEffect(this, modifiers);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax: //motion <HELP|analyze|savetosql|advanced|as>");
	}
}
