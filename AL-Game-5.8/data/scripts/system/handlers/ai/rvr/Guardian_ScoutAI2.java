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
package ai.rvr;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.services.NpcShoutsService;

import ai.AggressiveNpcAI2;

@AIName("guardian_scout") // 240971, 240972, 240973, 240974
public class Guardian_ScoutAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		// Victory is a certainty for us Elyos, yet you continue your futile struggles.
		sendMsg(1501537, getObjectId(), false, 5000);
		// You're no fools. Haven't you yet realized that this isn't a battle you can win ?
		sendMsg(1501538, getObjectId(), false, 8000);
		// Are there really this many Asmodians ignorant of their fate… ?
		// Then I'll teach you here and now. That today is your last day alive!
		sendMsg(1501539, getObjectId(), false, 11000);
		// Lord Ariel! Please show your power to that Asmodian!
		sendMsg(1501541, getObjectId(), false, 14000);
	}

	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}
