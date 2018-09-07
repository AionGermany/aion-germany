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
package com.aionemu.gameserver.network.loginserver.serverpackets;

import com.aionemu.gameserver.network.loginserver.LoginServerConnection;
import com.aionemu.gameserver.network.loginserver.LsServerPacket;

/**
 * @author KID
 */
public class SM_MACBAN_CONTROL extends LsServerPacket {

	private byte type;
	private String address;
	private String details;
	private long time;

	public SM_MACBAN_CONTROL(byte type, String address, long time, String details) {
		super(10);
		this.type = type;
		this.address = address;
		this.time = time;
		this.details = details;
	}

	@Override
	protected void writeImpl(LoginServerConnection con) {
		writeC(type);
		writeS(address);
		writeS(details);
		writeQ(time);
	}
}
