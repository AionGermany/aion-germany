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


package com.aionemu.chatserver.network.netty;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.aion.ClientPacketHandler;
import com.aionemu.chatserver.network.gameserver.GsConnectionFactoryImpl;
import com.aionemu.chatserver.network.netty.pipeline.LoginToClientPipeLineFactory;
import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;

/**
 * @author ATracer
 */
public class NettyServer {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final ChannelGroup channelGroup = new DefaultChannelGroup(NettyServer.class.getName());
    private final LoginToClientPipeLineFactory loginToClientPipeLineFactory;
    private ChannelFactory loginToClientChannelFactory;
    private NioServer nioServer;
    private static NettyServer instance = new NettyServer();

    public static NettyServer getInstance() {
        return instance;
    }

    public NettyServer() {
        this.loginToClientPipeLineFactory = new LoginToClientPipeLineFactory(new ClientPacketHandler());
        initialize();
    }

    /**
     * Initialize listening on login port
     */
    public void initialize() {
        loginToClientChannelFactory = initChannelFactory();

        Channel loginToClientChannel = initChannel(loginToClientChannelFactory, Config.CHAT_ADDRESS, loginToClientPipeLineFactory);

        channelGroup.add(loginToClientChannel);

        ServerCfg gs = new ServerCfg(Config.GAME_ADDRESS.getAddress().getHostAddress(), Config.GAME_ADDRESS.getPort(), "Gs Connections", new GsConnectionFactoryImpl());
        nioServer = new NioServer(5, gs);
        nioServer.connect();
    }

    /**
     * @return NioServerSocketChannelFactory
     */
    private NioServerSocketChannelFactory initChannelFactory() {
        return new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), Runtime.getRuntime().availableProcessors() * 2 + 1);
    }

    /**
     * @param channelFactory
     * @param listenAddress
     * @param port
     * @param channelPipelineFactory
     * @return Channel
     */
    private Channel initChannel(ChannelFactory channelFactory, InetSocketAddress address, ChannelPipelineFactory channelPipelineFactory) {
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
        bootstrap.setPipelineFactory(channelPipelineFactory);
        bootstrap.setOption("child.bufferFactory", HeapChannelBufferFactory.getInstance(ByteOrder.LITTLE_ENDIAN));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("child.reuseAddress", true);
        bootstrap.setOption("child.connectTimeoutMillis", 100);
        bootstrap.setOption("readWriteFair", true);

        return bootstrap.bind(address);
    }

    /**
     * Shutdown server
     */
    public void shutdownAll() {
        ChannelGroupFuture future = channelGroup.close();
        future.awaitUninterruptibly();
        loginToClientChannelFactory.releaseExternalResources();
        nioServer.shutdown();
    }
}
