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


package com.aionemu.chatserver.network.netty.handler;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.chatserver.network.aion.AbstractClientPacket;
import com.aionemu.chatserver.network.aion.AbstractServerPacket;
import com.aionemu.chatserver.network.aion.ClientPacketHandler;
import com.google.inject.Inject;

/**
 * @author ATracer
 */
public class ClientChannelHandler extends AbstractChannelHandler {

    private static final Logger log = LoggerFactory.getLogger(ClientChannelHandler.class);
    private final ClientPacketHandler clientPacketHandler;
    private State state;
    private ChatClient chatClient;

    @Inject
    public ClientChannelHandler(ClientPacketHandler clientPacketHandler) {
        this.clientPacketHandler = clientPacketHandler;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);

        state = State.CONNECTED;
        inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
        channel = ctx.getChannel();
        log.info("Channel connected Ip:" + inetAddress.getHostAddress());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);

        AbstractClientPacket clientPacket = clientPacketHandler.handle((ChannelBuffer) e.getMessage(), this);
        if (clientPacket != null && clientPacket.read()) {
            clientPacket.run();
        }
        if (clientPacket != null) {
            if (log.isDebugEnabled()) {
                log.debug("Received packet: " + clientPacket);
            }
        }
    }

    /**
     * @param packet
     */
    public void sendPacket(AbstractServerPacket packet) {
        ChannelBuffer cb = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192);
        packet.write(this, cb);
        channel.write(cb);
        if (log.isDebugEnabled()) {
            log.debug("Sent packet: " + packet);
        }
    }

    /**
     * Possible states of channel handler
     */
    public static enum State {

        /**
         * client just connected
         */
        CONNECTED,
        /**
         * client is authenticated
         */
        AUTHED,
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return the chatClient
     */
    public ChatClient getChatClient() {
        return chatClient;
    }

    /**
     * @param chatClient the chatClient to set
     */
    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
}
