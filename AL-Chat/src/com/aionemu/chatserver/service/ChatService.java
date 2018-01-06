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


package com.aionemu.chatserver.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.model.ChannelType;
import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.model.channel.ChatChannels;
import com.aionemu.chatserver.network.aion.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler.State;
import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

/**
 * @author ATracer
 */
public class ChatService {

    private static ChatService instance = new ChatService();

    public static ChatService getInstance() {
        return instance;
    }
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private Map<Integer, ChatClient> players = PlatformDependent.newConcurrentHashMap();
    private BroadcastService broadcastService;

    public ChatService() {
        broadcastService = BroadcastService.getInstance();
    }

    /**
     * Player registered from server side
     *
     * @param playerId
     * @param nick
     * @param token
     * @param identifier
     * @return
     * @throws NoSuchAlgorithmException
     */
    public ChatClient registerPlayer(int playerId, String playerLogin, String nick) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(playerLogin.getBytes("UTF-8"), 0, playerLogin.length());
        byte[] accountToken = md.digest();
        byte[] token = generateToken(accountToken);
        ChatClient chatClient = new ChatClient(playerId, token, nick);
        players.put(playerId, chatClient);
        return chatClient;
    }

    /**
     * @param playerId
     * @return
     */
    private byte[] generateToken(byte[] accountToken) {
        byte[] dynamicToken = new byte[16];
        new Random().nextBytes(dynamicToken);
        byte[] token = new byte[48];
        for (int i = 0; i < token.length; i++) {
            if (i < 16) {
                token[i] = dynamicToken[i];
            } else {
                token[i] = accountToken[i - 16];
            }
        }
        return token;
    }

    /**
     * Player registered from client request
     *
     * @param playerId
     * @param token
     * @param identifier
     * @param realAccount
     * @param realName
     * @param clientChannelHandler
     * @throws UnsupportedEncodingException
     */
    public void registerPlayerConnection(int playerId, byte[] token, byte[] identifier, ClientChannelHandler channelHandler, String realName) throws UnsupportedEncodingException {
        ChatClient chatClient = players.get(playerId);
        if (chatClient != null) {
            byte[] regToken = chatClient.getToken();
            chatClient.same(realName);

            if (Arrays.equals(regToken, token)) {
                String sreal = chatClient.getRealName() + "@" + new String(identifier);
                chatClient.setIdentifier(sreal.getBytes("utf-16le"));
                chatClient.setChannelHandler(channelHandler);
                channelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE());
                channelHandler.setState(State.AUTHED);
                channelHandler.setChatClient(chatClient);
                broadcastService.addClient(chatClient);
            }
        }
    }

    /**
     * @param chatClient
     * @param channelIndex
     * @param channelIdentifier
     * @return
     */
    public Channel registerPlayerWithChannel(ChatClient chatClient, int channelIndex, byte[] channelIdentifier) {
        Channel channel = ChatChannels.getChannelByIdentifier(channelIdentifier);
        if (channel != null) {
            ChannelType channelType = channel.getChannelType();
            if (channelType == ChannelType.GROUP /*|| channelType == ChannelType.JOB*/) {
                if (chatClient.isInChannel(channel)) {
                    return null;
                }
            }
            chatClient.addChannel(channel);
        }
        return channel;
    }

    /**
     * @param playerId
     */
    public void playerLogout(int playerId) {
        ChatClient chatClient = players.get(playerId);
        if (chatClient != null) {
            players.remove(playerId);
            broadcastService.removeClient(chatClient);
            if (chatClient.getChannelHandler() != null) {
                chatClient.getChannelHandler().close();
            } else {
                log.warn("Received logout event without client authentication for player " + playerId);
            }
        }
    }

    /**
     *
     * @param playerId
     * @param gagTime
     */
    public void gagPlayer(int playerId, long gagTime) {
        if (players.containsKey(playerId)) {
            ChatClient client = players.get(playerId);
            client.setGagTime(gagTime);
        }
    }
}
