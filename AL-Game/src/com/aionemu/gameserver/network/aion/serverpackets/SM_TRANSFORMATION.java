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

import com.aionemu.gameserver.model.gameobjects.player.TransformationCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 */
public class SM_TRANSFORMATION extends AionServerPacket {

	private int actionId;
	private Collection<TransformationCommonData> transformations;
	private TransformationCommonData transformationsCommonData;

	public SM_TRANSFORMATION(int actionId) {
		this.actionId = actionId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(actionId);
		switch (actionId) {
		case 0: //List
			writeC(0);
			if (transformations == null) {
				writeH(0);
				break;
			}
			writeH(transformations.size());
			for (TransformationCommonData commonData : transformations) {
				writeD(commonData.getTransformationId());
				writeD(0); // 0/1
			}
			break;
		case 1: //Add
			writeH(1);
			writeD(transformationsCommonData.getTransformationId());
		case 2: //Delete
			writeH(1);
			writeD(transformationsCommonData.getTransformationId());
			writeD(1);
		}
	}
}