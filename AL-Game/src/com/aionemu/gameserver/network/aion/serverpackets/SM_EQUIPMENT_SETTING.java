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

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.player.equipmentsetting.EquipmentSetting;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author FrozenKiller
 */
public class SM_EQUIPMENT_SETTING extends AionServerPacket {

	Collection<EquipmentSetting> equipmentSetting;

	public SM_EQUIPMENT_SETTING(final Collection<EquipmentSetting> equipmentSetting) {
		this.equipmentSetting = equipmentSetting;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(equipmentSetting.size());
		if (equipmentSetting != null) {
			for (EquipmentSetting eqSetting : equipmentSetting) {
				writeD(eqSetting.getSlot());
				writeC(1);
				writeC(1);
				writeC(1);
				writeC(1);
				writeD(eqSetting.getmHand());
				writeD(eqSetting.getsHand());
				writeD(eqSetting.getHelmet());
				writeD(eqSetting.getTorso());
				writeD(eqSetting.getGlove());
				writeD(eqSetting.getBoots());
				writeD(eqSetting.getEarringsLeft());
				writeD(eqSetting.getEarringsRight());
				writeD(eqSetting.getRingLeft());
				writeD(eqSetting.getRingRight());
				writeD(eqSetting.getNecklace());
				writeD(eqSetting.getShoulder());
				writeD(eqSetting.getPants());
				writeD(eqSetting.getPowershardLeft());
				writeD(eqSetting.getPowershardRight());
				writeD(eqSetting.getWings());
				writeD(eqSetting.getWaist());
				writeD(eqSetting.getmOffHand());
				writeD(eqSetting.getsOffHand());
				writeD(eqSetting.getPlume());
				writeD(0);
				writeD(eqSetting.getBracelet());
				writeD(0);
			}
		}
	}
}
