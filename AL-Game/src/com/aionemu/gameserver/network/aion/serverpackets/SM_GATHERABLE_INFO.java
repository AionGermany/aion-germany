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

import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_GATHERABLE_INFO extends AionServerPacket {

	private VisibleObject visibleObject;

	public SM_GATHERABLE_INFO(VisibleObject visibleObject) {
		super();
		this.visibleObject = visibleObject;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeF(visibleObject.getX());
		writeF(visibleObject.getY());
		writeF(visibleObject.getZ());
		writeD(visibleObject.getObjectId());
		writeD(visibleObject.getSpawn().getStaticId());
		writeD(visibleObject.getObjectTemplate().getTemplateId());
		if (visibleObject instanceof StaticDoor) {
			if (((StaticDoor) visibleObject).isOpen()) {
				writeH(0x09);
			}
			else {
				writeH(0x0A);
			}
		}
		else {
			writeH(1);
		}
		writeC(visibleObject.getSpawn().getHeading());
		writeD(visibleObject.getObjectTemplate().getNameId());
		writeH(0);
		writeH(0);
		writeH(0);
		writeC(100); // unk
	}
}
