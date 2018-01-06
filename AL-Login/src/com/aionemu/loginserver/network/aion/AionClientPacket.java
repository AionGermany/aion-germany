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


package com.aionemu.loginserver.network.aion;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.packet.BaseClientPacket;
import com.aionemu.loginserver.model.Account;

/**
 * Base class for every Aion -> LS Client Packet
 *
 * @author -Nemesiss-
 */
public abstract class AionClientPacket extends BaseClientPacket<LoginConnection> {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AionClientPacket.class);

    /**
     * Constructs new client packet.
     *
     * @param buf packet data
     * @param client client
     * @param opcode packet id
     */
    protected AionClientPacket(ByteBuffer buf, LoginConnection client, int opcode) {
        super(buf, opcode);
        setConnection(client);
    }

    /**
     * run runImpl catching and logging Throwable.
     */
    @Override
    public final void run() {
        try {
            runImpl();
        } catch (Throwable e) {
            String name;
            Account account = getConnection().getAccount();
            if (account != null) {
                name = account.getName();
            } else {
                name = getConnection().getIP();
            }

            log.error("error handling client (" + name + ") message " + this, e);
        }
    }

    /**
     * Send new AionServerPacket to connection that is owner of this packet.
     * This method is equvalent to: getConnection().sendPacket(msg);
     *
     * @param msg
     */
    protected void sendPacket(AionServerPacket msg) {
        getConnection().sendPacket(msg);
    }
}
