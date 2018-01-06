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


package com.aionemu.loginserver.network.gameserver;

/**
 * This class contains possible response that LoginServer may send to gameserver
 * if authentication fail etc.
 *
 * @author -Nemesiss-
 */
public enum GsAuthResponse {

    /**
     * Everything is OK
     */
    AUTHED(0),
    /**
     * Password/IP etc does not match.
     */
    NOT_AUTHED(1),
    /**
     * Requested id is not free
     */
    ALREADY_REGISTERED(2);
    /**
     * id of this enum that may be sent to client
     */
    private byte responseId;

    /**
     * Constructor.
     *
     * @param responseId id of the message
     */
    private GsAuthResponse(int responseId) {
        this.responseId = (byte) responseId;
    }

    /**
     * Message Id that may be sent to client.
     *
     * @return message id
     */
    public byte getResponseId() {
        return responseId;
    }
}
