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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aionemu.gameserver.model.templates.event.AtreianPassport;

/**
 * @author Alcapwnd
 */
public class PlayerPassports {

	private final SortedMap<Integer, AtreianPassport> passports;

	public PlayerPassports() {
		passports = new TreeMap<Integer, AtreianPassport>();
	}

	public synchronized boolean addPassport(int passportid, AtreianPassport atp) {
		if (passports.containsKey(passportid)) {
			return false;
		}
		passports.put(passportid, atp);
		return true;
	}

	public synchronized boolean removePassport(int passportid) {
		if (passports.containsKey(passportid)) {
			passports.remove(passportid);
			return true;
		}
		return false;
	}

	public Collection<AtreianPassport> getAllPassports() {
		return passports.values();
	}
}
