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


package com.aionemu.chatserver.network.aion.clientpackets;

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.model.channel.ChatChannels;
import com.aionemu.chatserver.model.message.Message;
import com.aionemu.chatserver.network.aion.AbstractClientPacket;
import com.aionemu.chatserver.network.aion.serverpackets.SM_CHANNEL_MESSAGE;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.chatserver.service.BroadcastService;

/**
 * @author ATracer
 */
public class CM_CHANNEL_MESSAGE extends AbstractClientPacket {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(CM_CHANNEL_MESSAGE.class);
    private int channelId;
    private byte[] content;
    private BroadcastService broadcastService;

    /**
     * @param channelBuffer
     * @param gameChannelHandler
     * @param opCode
     */
    public CM_CHANNEL_MESSAGE(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler, BroadcastService broadcastService) {
        super(channelBuffer, gameChannelHandler, 0x18);
        this.broadcastService = broadcastService;
    }

    @Override
    protected void readImpl() {
        readH();
        readC();
        readD();
        readD();
        readD();
        readD();
        channelId = readD();
        readC();
        int lenght = readH() * 2;
        content = readB(lenght);
    }

    @Override
    protected void runImpl() {
        Channel channel = ChatChannels.getChannelById(channelId);
        Message message = new Message(channel, content, clientChannelHandler.getChatClient());
        if (!clientChannelHandler.getChatClient().verifyLastMessage()) {
            message.setText("You can use chat only once every 30 second.");
            clientChannelHandler.sendPacket(new SM_CHANNEL_MESSAGE(message));
            return;
        }
        if (clientChannelHandler.getChatClient().isGagged()) {
            long endTime = (clientChannelHandler.getChatClient().getGagTime() - System.currentTimeMillis()) / 1000 / 60;
            message.setText("You have been gagged for " + endTime + " minutes");
            clientChannelHandler.sendPacket(new SM_CHANNEL_MESSAGE(message));
            return;
        }
        broadcastService.broadcastMessage(message);

        if (Config.LOG_CHAT) {
            LoggerFactory.getLogger("CHAT_LOG").info(String.format("[MESSAGE] <%s>: [%s]> %s", message.getChannelString(), message.getSenderString(), message.getTextString()));
        }
    }

    @Override
    public String toString() {
        return "CM_CHANNEL_MESSAGE [channelId=" + channelId + ", content=" + Arrays.toString(content) + "]";
    }
}
