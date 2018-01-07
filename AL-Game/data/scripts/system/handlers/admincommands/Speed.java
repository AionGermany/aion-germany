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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ATracer
 */
public class Speed extends AdminCommand implements StatOwner {

	public Speed() {
		super("speed");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "Syntax //speed <percent>");
			return;
		}

		int parameter = 0;
		try {
			parameter = Integer.parseInt(params[0]);
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "Parameter should number");
			return;
		}

		if (parameter < 0 || parameter > 1000) {
			PacketSendUtility.sendMessage(admin, "Valid values are in 0-1000 range");
			return;
		}

		admin.getGameStats().endEffect(this);
		List<IStatFunction> functions = new ArrayList<IStatFunction>();
		functions.add(new SpeedFunction(StatEnum.SPEED, parameter));
		functions.add(new SpeedFunction(StatEnum.FLY_SPEED, parameter));
		admin.getGameStats().addEffect(this, functions);

		PacketSendUtility.broadcastPacket(admin, new SM_EMOTION(admin, EmotionType.START_EMOTE2, 0, 0), true);
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax //speed <percent>");
	}

	class SpeedFunction extends StatFunction {

		static final int speed = 6000;
		static final int flyspeed = 9000;
		int modifier = 1;

		SpeedFunction(StatEnum stat, int modifier) {
			this.stat = stat;
			this.modifier = modifier;
		}

		@Override
		public void apply(Stat2 stat) {
			switch (this.stat) {
				case SPEED:
					stat.setBase(speed + (speed * modifier) / 100);
					break;
				case FLY_SPEED:
					stat.setBase(flyspeed + (flyspeed * modifier) / 100);
					break;
				default:
					break;
			}
		}

		@Override
		public int getPriority() {
			return 60;
		}
	}
}
