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


package com.aionemu.chatserver.network.gameserver;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.packet.BaseClientPacket;

/**
 *
 * @author KID
 *
 */
public abstract class GsClientPacket extends BaseClientPacket<GsConnection> {

    public GsClientPacket(ByteBuffer buffer, GsConnection connection, int opCode) {
        super(opCode);
    }
    private static final Logger log = LoggerFactory.getLogger(GsClientPacket.class);

    @Override
    public final void run() {
        try {
            runImpl();
        } catch (Throwable e) {
            log.warn("error handling gs (" + getConnection().getIP() + ") message " + this, e);
        }
    }

    protected void sendPacket(GsServerPacket msg) {
        getConnection().sendPacket(msg);
    }
}
