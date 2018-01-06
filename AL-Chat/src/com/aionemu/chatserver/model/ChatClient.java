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


package com.aionemu.chatserver.model;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

/**
 * @author ATracer
 */
public class ChatClient {

    private final Logger log = LoggerFactory.getLogger(ChatClient.class);
    /**
     * Id of chat client (player id)
     */
    private int clientId;
    /**
     * Identifier used when sending message
     */
    private byte[] identifier;
    /**
     * Token used during auth with GS
     */
    private byte[] token;
    /**
     * Channel handler of chat client
     */
    private ClientChannelHandler channelHandler;
    /**
     * Map with all connected channels<br>
     * Only one channel of specific type can be added
     */
    private Map<ChannelType, Channel> channelsList = PlatformDependent.newConcurrentHashMap();
    // last time message was requested and broadcasted
    private long lastMessage;
    private String realName;
    private long gagTime;

    /**
     * @param clientId
     * @param token
     * @param playerLogin
     * @param nick
     * @param identifier
     */
    public ChatClient(int clientId, byte[] token, String nick) {
        this.clientId = clientId;
        this.token = token;
        this.realName = nick;
    }

    /**
     * @param channel
     */
    public void addChannel(Channel channel) {
        channelsList.put(channel.getChannelType(), channel);
    }

    /**
     * @return the channelHandler
     */
    public ClientChannelHandler getChannelHandler() {
        return channelHandler;
    }

    /**
     * @return the clientId
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @return the identifier
     */
    public byte[] getIdentifier() {
        return identifier;
    }

    public String getRealName() {
        return realName;
    }

    /**
     * @return the token
     */
    public byte[] getToken() {
        return token;
    }

    /**
     * @param channel
     */
    public boolean isInChannel(Channel channel) {
        return channelsList.containsKey(channel.getChannelType());
    }

    /**
     * @param channelHandler the channelHandler to set
     */
    public void setChannelHandler(ClientChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    /**
     * @param identifier the identifier to set
     * @param realAccount
     * @param realName
     */
    public void setIdentifier(byte[] identifier) {
        this.identifier = identifier;
    }

    public boolean verifyLastMessage() {
        if (Config.MESSAGE_DELAY == 0) {
            return true;
        }

        if (this.lastMessage == 0) {
            this.lastMessage = System.currentTimeMillis();
            return true;
        } else {
            long diff = System.currentTimeMillis() - this.lastMessage;
            if (Config.MESSAGE_DELAY * 1000 > diff) {
                log.warn("player " + this.getClientId() + " tried to flood (" + diff + "ms) traffic. skipped");
                return false;
            } else {
                this.lastMessage = System.currentTimeMillis();
                return true;
            }
        }
    }

	public boolean isGagged() {
		if(this.gagTime == 0)
			return false;
		if(System.currentTimeMillis() > this.gagTime)
			return false;
		return true;
	}

    public void setGagTime(long gagTime) {
        this.gagTime = gagTime;
    }

    public long getGagTime() {
        return this.gagTime;
    }

    public boolean same(String nick) {
        if (!this.realName.equals(nick)) {
            log.warn("chat hack! different name " + nick + ". expected " + this.realName);
            return true;
        }
        return true;
    }
}
