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


package com.aionemu.loginserver.network.gameserver.clientpackets;

import com.aionemu.loginserver.controller.BannedMacManager;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;

/**
 *
 * @author KID
 *
 */
public class CM_MACBAN_CONTROL extends GsClientPacket {

    private byte type;
    private String address;
    private String details;
    private long time;

    @Override
    protected void readImpl() {
        type = (byte) readC();
        address = readS();
        details = readS();
        time = readQ();
    }

    @Override
    protected void runImpl() {
        BannedMacManager bmm = BannedMacManager.getInstance();
        switch (type) {
            case 0://unban
                bmm.unban(address, details);
                break;
            case 1://ban
                bmm.ban(address, time, details);
                break;
        }
    }
}
