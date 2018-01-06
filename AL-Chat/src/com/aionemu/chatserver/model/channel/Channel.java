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


package com.aionemu.chatserver.model.channel;

import java.nio.charset.Charset;

import com.aionemu.chatserver.model.ChannelType;
import com.aionemu.chatserver.utils.IdFactory;

/**
 * @author ATracer
 */
public abstract class Channel {

    private final ChannelType channelType;
    private final byte[] identifierBytes;
    private final String identifier;
    private final int channelId;

    public Channel(ChannelType channelType, String identifier) {
        this.channelType = channelType;
        this.identifier = identifier;
        this.channelId = IdFactory.getInstance().nextId();
        this.identifierBytes = identifier.getBytes(Charset.forName("UTF-16le"));
    }

    public String getStringIdentifier() {
        return identifier;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public byte[] getIdentifierBytes() {
        return identifierBytes;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getChannelId() {
        return channelId;
    }
}
