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
package com.aionemu.gameserver.network.aion.serverpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.cp.PlayerCPEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativitySkillService;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativityTransfoService;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Agility;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Health;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Knowledge;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Power;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Precision;
import com.aionemu.gameserver.services.player.CreativityPanel.stats.Will;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_CREATIVITY_POINTS extends AionServerPacket {

	Logger log = LoggerFactory.getLogger(SM_CREATIVITY_POINTS.class);
	private int totalPoint;
	@SuppressWarnings("unused")
	private int dotStep;
	private int size;
	private boolean onLogin;

	public SM_CREATIVITY_POINTS(int totalPoint, int dotStep) {
		this.totalPoint = totalPoint;
		this.dotStep = dotStep;
	}

	public SM_CREATIVITY_POINTS(int dotStep) {
		this.dotStep = dotStep;
	}

	public SM_CREATIVITY_POINTS(int totalPoint, int dotStep, int size, boolean onLogin) {
		this.totalPoint = totalPoint;
		this.dotStep = dotStep;
		this.size = size;
		this.onLogin = onLogin;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player activePlayer = con.getActivePlayer();
		writeD(totalPoint); // Creativity Points Total
		writeD(2);
		writeH(size);
		if (onLogin) {
			for (PlayerCPEntry ce : activePlayer.getCP().getAllCP()) {
				writeD(ce.getSlot());
				writeH(ce.getPoint());
				if (ce.getSlot() == 1) {
					activePlayer.setCPSlot1(ce.getPoint());
					Power.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() == 2) {
					activePlayer.setCPSlot2(ce.getPoint());
					Health.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() == 3) {
					activePlayer.setCPSlot3(ce.getPoint());
					Agility.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() == 4) {
					activePlayer.setCPSlot4(ce.getPoint());
					Precision.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() == 5) {
					activePlayer.setCPSlot5(ce.getPoint());
					Knowledge.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() == 6) {
					activePlayer.setCPSlot6(ce.getPoint());
					Will.getInstance().onChange(activePlayer, ce.getPoint());
				}
				if (ce.getSlot() >= 15 && ce.getSlot() <= 372 && ce.getPoint() != 0) {
					CreativitySkillService.getInstance().enchantSkill(activePlayer, ce.getSlot(), ce.getPoint());
				}
				CreativityTransfoService.getInstance().enchantTransfo(activePlayer, ce.getSlot(), ce.getPoint());
				if (ce.getSlot() >= 373 && ce.getSlot() <= 400) {
					CreativitySkillService.getInstance().loginDaevaSkill(activePlayer, ce.getSlot(), ce.getPoint());
				}
			}
		}
		else {
			for (PlayerCPEntry ce : activePlayer.getCP().getAllCP()) {
				if (ce.getSlot() == 1) {
					activePlayer.setCPSlot1(ce.getPoint());
				}
				if (ce.getSlot() == 2) {
					activePlayer.setCPSlot2(ce.getPoint());
				}
				if (ce.getSlot() == 3) {
					activePlayer.setCPSlot3(ce.getPoint());
				}
				if (ce.getSlot() == 4) {
					activePlayer.setCPSlot4(ce.getPoint());
				}
				if (ce.getSlot() == 5) {
					activePlayer.setCPSlot5(ce.getPoint());
				}
				if (ce.getSlot() == 6) {
					activePlayer.setCPSlot6(ce.getPoint());
				}
			}
		}
	}
}
