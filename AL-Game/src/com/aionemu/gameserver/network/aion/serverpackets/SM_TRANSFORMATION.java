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
 * @author Falke_34, FrozenKiller
 */
public class SM_TRANSFORMATION extends AionServerPacket {

	private int actionId;
	private Collection<TransformationCommonData> transformations;
	private int transformationId;
	private int transformationId1;
	private int transformationId2;
	private int transformationId3;
	private int transformationId4;
	private int transformationId5;
	private int transformationId6;
	private int transformationId7;
	private int transformationId8;
	private int transformationId9;

	public SM_TRANSFORMATION(int actionId, Collection<TransformationCommonData> transformations) {
		this.actionId = actionId;
		this.transformations = transformations;
	}

	public SM_TRANSFORMATION(int actionId, int transformationId) {
		this.actionId = actionId;
		this.transformationId = transformationId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(actionId);
		switch (actionId) {
			case 0: { // List
				writeC(0); // 43?
				writeD(0); // 3836?
				if (transformations != null) {
					writeH(transformations.size());
					for (TransformationCommonData commonData : transformations) {
						writeD(commonData.getTransformationId());
						writeD(commonData.getCount());
					}
				} else {
					writeH(0);
				}
				break;
			}
			case 1: { // Add
				writeH(1);
				writeD(transformationId);
				writeD(transformationId1);
				writeD(transformationId2);
				writeD(transformationId3);
				writeD(transformationId4);
				writeD(transformationId5);
				writeD(transformationId6);
				writeD(transformationId7);
				writeD(transformationId8);
				writeD(transformationId9);
				writeD(1);
				break;
			}
			case 2: { // Delete
				writeH(1);
				writeD(transformationId);
				writeD(1);
				break;
			}
			case 3: { // ??
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				break;
			}
		}
	}
}