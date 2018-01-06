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


package com.aionemu.chatserver.network.netty.pipeline;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.aionemu.chatserver.network.aion.ClientPacketHandler;
import com.aionemu.chatserver.network.netty.coder.LoginPacketDecoder;
import com.aionemu.chatserver.network.netty.coder.LoginPacketEncoder;
import com.aionemu.chatserver.network.netty.coder.PacketFrameDecoder;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;

/**
 * @author ATracer
 */
public class LoginToClientPipeLineFactory implements ChannelPipelineFactory {

    private static final int THREADS_MAX = 10;
    private static final int MEMORY_PER_CHANNEL = 1048576;
    private static final int TOTAL_MEMORY = 134217728;
    private static final int TIMEOUT = 100;
    private ExecutionHandler executionHandler;
    private final ClientPacketHandler clientPacketHandler;

    public LoginToClientPipeLineFactory(ClientPacketHandler clientPacketHandler) {
        this.clientPacketHandler = clientPacketHandler;
        this.executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(THREADS_MAX, MEMORY_PER_CHANNEL, TOTAL_MEMORY, TIMEOUT, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory()));
    }

    /**
     * Decoding process will include the following handlers: - framedecoder -
     * packetdecoder - handler Encoding process: - packetencoder Please note the
     * sequence of handlers
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("framedecoder", new PacketFrameDecoder());
        pipeline.addLast("packetdecoder", new LoginPacketDecoder());
        pipeline.addLast("packetencoder", new LoginPacketEncoder());
        pipeline.addLast("executor", executionHandler);
        pipeline.addLast("handler", new ClientChannelHandler(clientPacketHandler));

        return pipeline;
    }
}
