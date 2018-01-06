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
package com.aionemu.gameserver.model.autogroup;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.Collection;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author xTz
 * @author GiGatR00n v4.7.5.x
 */
public class SearchInstance {

	private long registrationTime = System.currentTimeMillis();
	private int instanceMaskId;
	private EntryRequestType ert;
	private List<Integer> members;

	public SearchInstance(int instanceMaskId, EntryRequestType ert, Collection<Player> members) {
		this.instanceMaskId = instanceMaskId;
		this.ert = ert;
		if (members != null) {
			this.members = extract(members, on(Player.class).getObjectId());
		}
	}

	public List<Integer> getMembers() {
		return members;
	}

	public int getInstanceMaskId() {
		return instanceMaskId;
	}

	public int getRemainingTime() {
		return (int) (System.currentTimeMillis() - registrationTime) / 1000 * 256;
	}

	public EntryRequestType getEntryRequestType() {
		return ert;
	}

	public boolean isDredgion() {
		return instanceMaskId == 1 || instanceMaskId == 2 || instanceMaskId == 3 || instanceMaskId == 121;
	}

	public boolean isKamar() {
		return instanceMaskId == 107;
	}

	public boolean isJormungand() {
		return instanceMaskId == 108;
	}

	public boolean isBastion() {
		return instanceMaskId == 109;
	}

	public boolean isRunatorium() {
		return instanceMaskId == 111;
	}

	public boolean isBalaurMarching() {
		return instanceMaskId == 122;
	}

	public boolean isRunatoriumRuins() {
		return instanceMaskId == 123;
	}

	public boolean isGoldenCrusible() {
		return instanceMaskId == 125;
	}

	public boolean isSanctumBattlefield() {
		return instanceMaskId == 416;
	}

	public boolean isPandaemoniumBattlefield() {
		return instanceMaskId == 417;
	}
}
