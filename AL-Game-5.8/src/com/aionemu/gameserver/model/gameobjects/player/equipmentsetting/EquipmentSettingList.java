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
package com.aionemu.gameserver.model.gameobjects.player.equipmentsetting;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerEquipmentSettingDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Falke_34
 */
public class EquipmentSettingList {

	private Map<Integer, EquipmentSetting> equipmentSetting;
	private Player owner;

	/**
	 * @param owner
	 */
	public EquipmentSettingList(final Player owner) {
		this.owner = owner;
	}

	public void add(int slot, int display, int mHand, int sHand, int helmet, int torso, int glove, int boots, int earringsLeft,
		int earringsRight, int ringLeft, int ringRight, int necklace, int shoulder, int pants, int powershardLeft, int powershardRight,
		int wings, int waist, int mOffHand, int sOffHand, int plume, int bracelet, boolean isNew) {
		if (equipmentSetting == null) {
			equipmentSetting = new HashMap<Integer, EquipmentSetting>();
		}
		EquipmentSetting equipmentSettings = new EquipmentSetting(slot, display, mHand, sHand, helmet, torso, glove, boots, earringsLeft,
			earringsRight, ringLeft, ringRight, necklace, shoulder, pants, powershardLeft, powershardRight, wings, waist, mOffHand,
			sOffHand, plume, bracelet);
		equipmentSetting.put(slot, equipmentSettings);
		DAOManager.getDAO(PlayerEquipmentSettingDAO.class).insertEquipmentSetting(owner, equipmentSettings);
	}

	public Collection<EquipmentSetting> getEquipmentSetting() {
		if (equipmentSetting == null) {
			return Collections.emptyList();
		}
		return equipmentSetting.values();
	}
}