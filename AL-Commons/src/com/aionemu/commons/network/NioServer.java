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

package com.aionemu.commons.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.options.Assertion;

/**
 * NioServer instance that handle connections on specified addresses.
 * 
 * @author -Nemesiss-
 */
public class NioServer {

	/**
	 * Logger for NioServer
	 */
	private static final Logger log = LoggerFactory.getLogger(NioServer.class.getName());

	/**
	 * The channels on which we'll accept connections
	 */
	private final List<SelectionKey> serverChannelKeys = new ArrayList<SelectionKey>();

	/**
	 * Dispatcher that will accept connections
	 */
	private Dispatcher acceptDispatcher;
	/**
	 * Useful int to load balance connections between Dispatchers
	 */
	private int currentReadWriteDispatcher;
	/**
	 * Read Write Dispatchers
	 */
	private Dispatcher[] readWriteDispatchers;

	/**
	 * DisconnectionThreadPool that will be used to execute DisconnectionTask.
	 */
	private final Executor dcPool;

	/**
	 * 
	 */
	private int readWriteThreads;
	/**
	 * 
	 */
	private ServerCfg[] cfgs;

	/**
	 * Constructor.
	 * 
	 * @param readWriteThreads
	 *            - number of threads that will be used for handling read and
	 *            write.
	 * @param dcPool
	 *            - ThreadPool on witch Disconnection tasks will be executed.
	 * @param cfgs
	 *            - Server Configurations
	 */
	public NioServer(int readWriteThreads, ServerCfg... cfgs) {
		/**
		 * Test if this build should use assertion and enforce it. If
		 * NetworkAssertion == false javac will remove this code block
		 */
		if (Assertion.NetworkAssertion) {
			boolean assertionEnabled = false;
			assert assertionEnabled = true;
			if (!assertionEnabled)
				throw new RuntimeException(
						"This is unstable build. Assertion must be enabled! Add -ea to your start script or consider using stable build instead.");
		}
		this.dcPool = ThreadPoolManager.getInstance();
		this.readWriteThreads = readWriteThreads;
		this.cfgs = cfgs;
	}

	public void connect() {
		try {
			this.initDispatchers(readWriteThreads, dcPool);

			/** Create a new non-blocking server socket channel for clients */
			for (ServerCfg cfg : cfgs) {
				ServerSocketChannel serverChannel = ServerSocketChannel.open();
				serverChannel.configureBlocking(false);

				/** Bind the server socket to the specified address and port */
				InetSocketAddress isa;
				if ("*".equals(cfg.hostName)) {
					isa = new InetSocketAddress(cfg.port);
					log.info(
							"Server listening on all available IPs on Port " + cfg.port + " for " + cfg.connectionName);
				} else {
					isa = new InetSocketAddress(cfg.hostName, cfg.port);
					log.info("Server listening on IP: " + cfg.hostName + " Port " + cfg.port + " for "
							+ cfg.connectionName);
				}
				serverChannel.socket().bind(isa);

				/**
				 * Register the server socket channel, indicating an interest in
				 * accepting new connections
				 */
				SelectionKey acceptKey = getAcceptDispatcher().register(serverChannel, SelectionKey.OP_ACCEPT,
						new Acceptor(cfg.factory, this));
				serverChannelKeys.add(acceptKey);
			}
		} catch (Exception e) {
			log.error("NioServer Initialization Error: " + e, e);
			throw new Error("NioServer Initialization Error!");
		}
	}

	/**
	 * @return Accept Dispatcher.
	 */
	public final Dispatcher getAcceptDispatcher() {
		return acceptDispatcher;
	}

	/**
	 * @return one of ReadWrite Dispatcher or Accept Dispatcher if
	 *         readWriteThreads was set to 0.
	 */
	public final Dispatcher getReadWriteDispatcher() {
		if (readWriteDispatchers == null)
			return acceptDispatcher;

		if (readWriteDispatchers.length == 1)
			return readWriteDispatchers[0];

		if (currentReadWriteDispatcher >= readWriteDispatchers.length)
			currentReadWriteDispatcher = 0;
		return readWriteDispatchers[currentReadWriteDispatcher++];
	}

	/**
	 * Initialize Dispatchers.
	 * 
	 * @param readWriteThreads
	 * @param dcPool
	 * @throws IOException
	 */
	private void initDispatchers(int readWriteThreads, Executor dcPool) throws IOException {
		if (readWriteThreads < 1) {
			acceptDispatcher = new AcceptReadWriteDispatcherImpl("AcceptReadWrite Dispatcher", dcPool);
			acceptDispatcher.start();
		} else {
			acceptDispatcher = new AcceptDispatcherImpl("Accept Dispatcher");
			acceptDispatcher.start();

			readWriteDispatchers = new Dispatcher[readWriteThreads];
			for (int i = 0; i < readWriteDispatchers.length; i++) {
				readWriteDispatchers[i] = new AcceptReadWriteDispatcherImpl("ReadWrite-" + i + " Dispatcher", dcPool);
				readWriteDispatchers[i].start();
			}
		}
	}

	/**
	 * @return Number of active connections.
	 */
	public final int getActiveConnections() {
		int count = 0;
		if (readWriteDispatchers != null) {
			for (Dispatcher d : readWriteDispatchers)
				count += d.selector().keys().size();
		} else {
			count = acceptDispatcher.selector().keys().size() - serverChannelKeys.size();
		}
		return count;
	}

	/**
	 * Shutdown.
	 */
	public final void shutdown() {
		log.info("Closing ServerChannels...");
		try {
			for (SelectionKey key : serverChannelKeys)
				key.cancel();
			log.info("ServerChannel closed.");
		} catch (Exception e) {
			log.error("Error during closing ServerChannel, " + e, e);
		}

		notifyServerClose();
		/** Wait 5s */
		try {
			Thread.sleep(1000);
		} catch (Throwable t) {
			log.warn("Nio thread was interrupted during shutdown", t);
		}

		log.info(" Active connections: " + getActiveConnections());

		/** DC all */
		log.info("Forced Disconnecting all connections...");
		closeAll();
		log.info(" Active connections: " + getActiveConnections());

		// dcPool.waitForDisconnectionTasks();

		/** Wait 5s */
		try {
			Thread.sleep(1000);
		} catch (Throwable t) {
			log.warn("Nio thread was interrupted during shutdown", t);
		}
	}

	/**
	 * Calls onServerClose method for all active connections.
	 */
	private void notifyServerClose() {
		if (readWriteDispatchers != null) {
			for (Dispatcher d : readWriteDispatchers)
				for (SelectionKey key : d.selector().keys()) {
					if (key.attachment() instanceof AConnection) {
						((AConnection) key.attachment()).onServerClose();
					}
				}
		} else {
			for (SelectionKey key : acceptDispatcher.selector().keys()) {
				if (key.attachment() instanceof AConnection) {
					((AConnection) key.attachment()).onServerClose();
				}
			}
		}
	}

	/**
	 * Close all active connections.
	 */
	private void closeAll() {
		if (readWriteDispatchers != null) {
			for (Dispatcher d : readWriteDispatchers)
				for (SelectionKey key : d.selector().keys()) {
					if (key.attachment() instanceof AConnection) {
						((AConnection) key.attachment()).close(true);
					}
				}
		} else {
			for (SelectionKey key : acceptDispatcher.selector().keys()) {
				if (key.attachment() instanceof AConnection) {
					((AConnection) key.attachment()).close(true);
				}
			}
		}
	}
}
