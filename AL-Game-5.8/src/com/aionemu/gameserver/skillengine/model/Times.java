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
package com.aionemu.gameserver.skillengine.model;

import java.util.HashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.item.WeaponType;

/**
 * @author kecims
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Times")
public class Times {

	@XmlAttribute(required = true)
	protected String times;
	@XmlTransient
	private HashMap<WeaponTypeWrapper, Integer> timeForWeaponType = new HashMap<WeaponTypeWrapper, Integer>();

	public String getTimes() {
		return times;
	}

	/**
	 * @param times
	 *            the times to set
	 */
	public void setTimes(String times) {
		this.times = times;
	}

	public int getTimeForWeapon(WeaponTypeWrapper weapon) {
		return timeForWeaponType.get(weapon);
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		String[] tokens = times.split(",");
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.BOOK_2H, null), Integer.parseInt(tokens[0]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.BOW, null), Integer.parseInt(tokens[1]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.DAGGER_1H, null), Integer.parseInt(tokens[2]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.MACE_1H, null), Integer.parseInt(tokens[3]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.ORB_2H, null), Integer.parseInt(tokens[4]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.POLEARM_2H, null), Integer.parseInt(tokens[5]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.STAFF_2H, null), Integer.parseInt(tokens[6]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.SWORD_1H, null), Integer.parseInt(tokens[7]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.SWORD_2H, null), Integer.parseInt(tokens[8]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.SWORD_1H, WeaponType.SWORD_1H), Integer.parseInt(tokens[9]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.DAGGER_1H, WeaponType.DAGGER_1H), Integer.parseInt(tokens[10]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.GUN_1H, null), Integer.parseInt(tokens[11]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.GUN_1H, WeaponType.GUN_1H), Integer.parseInt(tokens[12]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.TOOLHOE_1H, null), Integer.parseInt(tokens[13]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.TOOLHOE_1H, WeaponType.TOOLHOE_1H), Integer.parseInt(tokens[14]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.TOOLPICK_2H, null), Integer.parseInt(tokens[15]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.TOOLROD_2H, null), Integer.parseInt(tokens[16]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.CANNON_2H, null), Integer.parseInt(tokens[17]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.HARP_2H, null), Integer.parseInt(tokens[18]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.GUN_2H, null), Integer.parseInt(tokens[19]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.KEYBLADE_2H, null), Integer.parseInt(tokens[20]));
		timeForWeaponType.put(new WeaponTypeWrapper(WeaponType.KEYHAMMER_2H, null), Integer.parseInt(tokens[21]));
	}
}
