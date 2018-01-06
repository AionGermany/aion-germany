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
package com.aionemu.gameserver.services.rvrservice;

import java.util.Map;

import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class RvrStartRunnable implements Runnable {

	private final int id;

	public RvrStartRunnable(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		// An Elyos warship will invade in 10 minutes.
		RvrService.getInstance().DF6G1Spawn01Msg(id);
		// An Asmodian warship will invade in 10 minutes.
		RvrService.getInstance().LF6G1Spawn01Msg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// An Elyos warship will invade in 5 minutes.
				RvrService.getInstance().DF6G1Spawn02Msg(id);
				// An Asmodian warship will invade in 5 minutes.
				RvrService.getInstance().LF6G1Spawn02Msg(id);
			}
		}, 300000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// An Elyos warship will invade in 3 minutes.
				RvrService.getInstance().DF6G1Spawn03Msg(id);
				// An Asmodian warship will invade in 3 minutes.
				RvrService.getInstance().LF6G1Spawn03Msg(id);
			}
		}, 480000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// An Elyos warship will invade in 1 minute.
				RvrService.getInstance().DF6G1Spawn04Msg(id);
				// An Asmodian warship will invade in 1 minute.
				RvrService.getInstance().LF6G1Spawn04Msg(id);
			}
		}, 540000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Map<Integer, RvrLocation> locations = RvrService.getInstance().getRvrLocations();
				for (final RvrLocation loc : locations.values()) {
					if (loc.getId() == id) {
						// Elyos warship Invasion.
						RvrService.getInstance().DF6G1Spawn05Msg(id);
						// Asmodian warship Invasion.
						RvrService.getInstance().LF6G1Spawn05Msg(id);
						RvrService.getInstance().startRvr(loc.getId());
						// An Asmodian Troopers scout ship will arrive at the Sky Island in one minute.
						// An Asmodian Troopers scout ship will soon arrive at the Sky Island.
						RvrService.getInstance().LF6EventG2Start02Msg(id);
						// An Aetos ship will arrive at the Sky Island in one minute.
						// An Aetos ship will soon arrive at the Sky Island.
						RvrService.getInstance().DF6EventG2Start02Msg(id);
					}
				}
			}
		}, 600000);
	}
}
