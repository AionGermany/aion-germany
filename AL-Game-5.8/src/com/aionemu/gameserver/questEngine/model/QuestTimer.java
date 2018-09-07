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
package com.aionemu.gameserver.questEngine.model;

import java.util.Timer;
import java.util.TimerTask;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Hilgert
 */
public class QuestTimer {

	private Timer timer;
	private int Time = 0;
	@SuppressWarnings("unused")
	private int questId;
	private boolean isTicking = false;
	private Player player;

	/**
	 * @param questId
	 */
	public QuestTimer(int questId, int seconds, Player player) {
		this.questId = questId;
		this.Time = seconds * 1000;
		this.player = player;
	}

	/**
	 * @param seconds
	 * @param player
	 * @return
	 */
	public void Start() {
		PacketSendUtility.sendMessage(player, "Timer started");
		timer = new Timer();
		isTicking = true;
		// TODO Send Packet that timer start
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				PacketSendUtility.sendMessage(player, "Timer is over");
				onEnd();
			}
		};

		timer.schedule(task, Time);
	}

	public void Stop() {
		timer.cancel();
		onEnd();
	}

	public void onEnd() {
		// TODO Send Packet that timer end
		isTicking = false;
	}

	/**
	 * @return false - if Timer not started or stoped.
	 */
	public boolean isTicking() {
		return this.isTicking;
	}

	/**
	 * @return
	 */
	public int getTimeSeconds() {
		return this.Time / 1000;
	}
}
