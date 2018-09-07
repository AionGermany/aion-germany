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

import java.util.Iterator;

import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.event.AIEventLog;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 */
public class Ai2Command extends AdminCommand {

	public Ai2Command() {
		super("ai2");
	}

	@Override
	public void execute(Player player, String... params) {
		/**
		 * Non target commands
		 */
		String param0 = params[0];

		if (param0.equals("createlog")) {
			boolean oldValue = AIConfig.ONCREATE_DEBUG;
			AIConfig.ONCREATE_DEBUG = !oldValue;
			PacketSendUtility.sendMessage(player, "New createlog value: " + !oldValue);
			return;
		}

		if (param0.equals("eventlog")) {
			boolean oldValue = AIConfig.EVENT_DEBUG;
			AIConfig.EVENT_DEBUG = !oldValue;
			PacketSendUtility.sendMessage(player, "New eventlog value: " + !oldValue);
			return;
		}

		if (param0.equals("movelog")) {
			boolean oldValue = AIConfig.MOVE_DEBUG;
			AIConfig.MOVE_DEBUG = !oldValue;
			PacketSendUtility.sendMessage(player, "New movelog value: " + !oldValue);
			return;
		}

		if (param0.equals("say")) {
			LoggerFactory.getLogger(Ai2Command.class).info("[AI2] marker: " + params[1]);
		}

		/**
		 * Target commands
		 */
		VisibleObject target = player.getTarget();

		if (target == null || !(target instanceof Npc)) {
			PacketSendUtility.sendMessage(player, "Select target first (Npc only)");
			return;
		}
		Npc npc = (Npc) target;

		if (param0.equals("info")) {
			PacketSendUtility.sendMessage(player, "Ai name: " + npc.getAi2().getName());
			PacketSendUtility.sendMessage(player, "Ai state: " + npc.getAi2().getState());
			PacketSendUtility.sendMessage(player, "Ai substate: " + npc.getAi2().getSubState());
			return;
		}

		if (param0.equals("log")) {
			boolean oldValue = npc.getAi2().isLogging();
			((AbstractAI) npc.getAi2()).setLogging(!oldValue);
			PacketSendUtility.sendMessage(player, "New log value: " + !oldValue);
			return;
		}

		if (param0.equals("print")) {
			AIEventLog eventLog = ((AbstractAI) npc.getAi2()).getEventLog();
			Iterator<AIEventType> iterator = eventLog.iterator();
			while (iterator.hasNext()) {
				PacketSendUtility.sendMessage(player, "EVENT: " + iterator.next().name());
			}
			return;
		}

		String param1 = params[1];
		if (param0.equals("set")) {
			String aiName = param1;
			AI2Engine.getInstance().setupAI(aiName, npc);
		}
		else if (param0.equals("event")) {
			AIEventType eventType = AIEventType.valueOf(param1.toUpperCase());
			if (eventType != null) {
				npc.getAi2().onGeneralEvent(eventType);
			}
		}
		else if (param0.equals("event2")) {
			AIEventType eventType = AIEventType.valueOf(param1.toUpperCase());
			Creature creature = (Creature) World.getInstance().findVisibleObject(Integer.valueOf(params[2]));
			if (eventType != null) {
				npc.getAi2().onCreatureEvent(eventType, creature);
			}
		}
		else if (param0.equals("state")) {
			AIState state = AIState.valueOf(param1.toUpperCase());
			((NpcAI2) npc.getAi2()).setStateIfNot(state);
			if (params.length > 2) {
				AISubState substate = AISubState.valueOf(params[2]);
				((NpcAI2) npc.getAi2()).setSubStateIfNot(substate);
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //ai2 <set|event|event2|info|log|print|createlog|eventlog|movelog>");
	}
}
