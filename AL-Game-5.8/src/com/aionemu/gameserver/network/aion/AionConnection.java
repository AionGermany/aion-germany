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
package com.aionemu.gameserver.network.aion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.AConnection;
import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.PacketProcessor;
import com.aionemu.commons.utils.concurrent.ExecuteWrapper;
import com.aionemu.commons.utils.concurrent.RunnableStatsManager;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.Crypt;
import com.aionemu.gameserver.network.PacketFloodFilter;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_KEY;
import com.aionemu.gameserver.network.factories.AionPacketHandlerFactory;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_MAC;
import com.aionemu.gameserver.services.player.PlayerLeaveWorldService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.google.common.base.Preconditions;

import javolution.util.FastList;

/**
 * Object representing connection between GameServer and Aion Client.
 *
 * @author -Nemesiss-
 */
public class AionConnection extends AConnection {

	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(AionConnection.class);
	private static final PacketProcessor<AionConnection> packetProcessor = new PacketProcessor<AionConnection>(NetworkConfig.PACKET_PROCESSOR_MIN_THREADS, NetworkConfig.PACKET_PROCESSOR_MAX_THREADS, NetworkConfig.PACKET_PROCESSOR_THREAD_SPAWN_THRESHOLD, NetworkConfig.PACKET_PROCESSOR_THREAD_KILL_THRESHOLD, new ExecuteWrapper());
	private String hdd_serial;
	private String ipv4list;
	private String local_ip;
	private String windows;
	private int countryCode = GSConfig.SERVER_COUNTRY_CODE;
	private String aionBin;
	private int memory;
	private int winEncode;
	private String traceroute;

	public int getCountryCode() {
		if (getIP().equals("109.87.238.83")) {
			return 7;
		}
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	public void setLocalIP(String local_ip) {
		this.local_ip = local_ip;
	}

	public void setIPv4List(String iplist) {
		this.ipv4list = iplist;
	}

	public void setHDDSerial(String hdd_serial) {
		this.hdd_serial = hdd_serial;
	}

	public void setWindows(String windows) {
		this.windows = windows;
	}

	public void setMemoryPC(int memory) {
		this.memory = memory;
	}

	public void setAionBin(String AionBinConnection) {
		this.aionBin = AionBinConnection;
	}

	public String getAionBin() {
		return aionBin;
	}

	public int getMemory() {
		return memory;
	}

	public void setWindowsEncoding(int enc) {
		winEncode = enc;
	}

	public int getWindowsEncoding() {
		return winEncode;
	}

	public String getTracerouteIP() {
		return traceroute;
	}

	public void setTracerouteIP(String ips) {
		traceroute = ips;
	}

	/**
	 * Possible states of AionConnection
	 */
	public static enum State {

		/**
		 * client just connect
		 */
		CONNECTED,
		/**
		 * client is authenticated
		 */
		AUTHED,
		/**
		 * client entered world.
		 */
		IN_GAME
	}

	/**
	 * Server Packet "to send" Queue
	 */
	private final FastList<AionServerPacket> sendMsgQueue = new FastList<AionServerPacket>();
	/**
	 * Current state of this connection
	 */
	private volatile State state;
	/**
	 * AionClient is authenticating by passing to GameServer id of account.
	 */
	private Account account;
	/**
	 * Crypt that will encrypt/decrypt packets.
	 */
	private final Crypt crypt = new Crypt();
	/**
	 * active Player that owner of this connection is playing [entered game]
	 */
	private AtomicReference<Player> activePlayer = new AtomicReference<Player>();
	private String lastPlayerName = "";
	private AionPacketHandler aionPacketHandler;
	private long lastPingTimeMS;
	private int nbInvalidPackets = 0;
	// TODO! why there is no any comments what is this doing? i have no clue what is it for [Nemesiss]
	private final static int MAX_INVALID_PACKETS = 3;
	private String macAddress;
	/**
	 * Ping checker - for detecting hanged up connections *
	 */
	private PingChecker pingChecker;
	/**
	 * packet flood filter *
	 */
	private int[] pff;
	private long[] pffRequests;

	/**
	 * Constructor
	 *
	 * @param sc
	 * @param d
	 * @throws IOException
	 */
	public AionConnection(SocketChannel sc, Dispatcher d) throws IOException {
		super(sc, d, 8192 * 4, 8192 * 4);

		AionPacketHandlerFactory aionPacketHandlerFactory = AionPacketHandlerFactory.getInstance();
		this.aionPacketHandler = aionPacketHandlerFactory.getPacketHandler();

		state = State.CONNECTED;

		String ip = getIP();
		log.info("connection from: " + ip);

		pingChecker = new PingChecker();
		pingChecker.start();

		if (SecurityConfig.PFF_ENABLE) {
			pff = PacketFloodFilter.getInstance().getPackets();
			pffRequests = new long[pff.length];
		}
	}

	@Override
	protected void initialized() {
		/**
		 * Send SM_KEY packet
		 */
		sendPacket(new SM_KEY());
	}

	/**
	 * Enable crypt key - generate random key that will be used to encrypt second server packet [first one is unencrypted] and decrypt client packets. This method is called from SM_KEY server packet,
	 * that packet sends key to aion client.
	 *
	 * @return "false key" that should by used by aion client to encrypt/decrypt packets.
	 */
	public final int enableCryptKey() {
		return crypt.enableKey();
	}

	/**
	 * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	 *
	 * @param data
	 * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	 */
	@Override
	protected final boolean processData(ByteBuffer data) {
		try {
			if (!crypt.decrypt(data)) {
				nbInvalidPackets++;
				log.info("[" + nbInvalidPackets + "/" + MAX_INVALID_PACKETS + "] Decrypt fail, client packet passed...");
				if (nbInvalidPackets >= MAX_INVALID_PACKETS) {
					log.warn("Decrypt fail!");
					return false;
				}
				return true;
			}
		}
		catch (Exception ex) {
			log.error("Exception caught during decrypt!" + ex.getMessage());
			return false;
		}

		if (data.remaining() < 5) {// op + static code + op == 5 bytes
			log.error("Received fake packet from: " + this);
			return false;
		}

		AionClientPacket pck = aionPacketHandler.handle(data, this);

		/**
		 * Execute packet only if packet exist (!= null) and read was ok.
		 */
		if (pck != null) {
			if (SecurityConfig.PFF_ENABLE) {
				int opcode = pck.getOpcode();
				if (pff.length > opcode) {
					if (pff[opcode] > 0) {
						long last = this.pffRequests[opcode];
						if (last == 0) {
							this.pffRequests[opcode] = System.currentTimeMillis();
						}
						else {
							long diff = System.currentTimeMillis() - last;
							if (diff < pff[opcode]) {
								log.warn(this + " has flooding " + pck.getClass().getSimpleName() + " " + diff);
								switch (SecurityConfig.PFF_LEVEL) {
									case 1: // disconnect
										return false;
									case 2:
										break;
								}
							}
							else {
								this.pffRequests[opcode] = System.currentTimeMillis();
							}
						}
					}
				}
			}

			PacketLoggerService.getInstance().logPacketCM(pck.getPacketName());

			if (pck.read()) {
				packetProcessor.executePacket(pck);
			}
		}

		return true;
	}

	/**
	 * This method will be called by Dispatcher, and will be repeated till return false.
	 *
	 * @param data
	 * @return True if data was written to buffer, False indicating that there are not any more data to write.
	 */
	@Override
	protected final boolean writeData(ByteBuffer data) {
		synchronized (guard) {
			final long begin = System.nanoTime();
			if (sendMsgQueue.isEmpty()) {
				return false;
			}
			AionServerPacket packet = sendMsgQueue.removeFirst();
			PacketLoggerService.getInstance().logPacketSM(packet.getPacketName());
			try {
				packet.write(this, data);
				return true;
			}
			finally {
				RunnableStatsManager.handleStats(packet.getClass(), "runImpl()", System.nanoTime() - begin);
			}

		}
	}

	/**
	 * This method is called by Dispatcher when connection is ready to be closed.
	 *
	 * @return time in ms after witch onDisconnect() method will be called. Always return 0.
	 */
	@Override
	protected final long getDisconnectionDelay() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onDisconnect() {
		/**
		 * Client starts authentication procedure
		 */
		pingChecker.stop();
		if (getAccount() != null) {
			LoginServer.getInstance().aionClientDisconnected(getAccount().getId());
			LoginServer.getInstance().sendPacket(new SM_MAC(getAccount().getId(), macAddress, hdd_serial));
		}
		Player player = getActivePlayer();
		if (player != null) {
			PlayerLeaveWorldService.tryLeaveWorld(player);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onServerClose() {
		// TODO mb some packet should be send to client before closing?
		close(/* packet, */true);
	}

	/**
	 * Encrypt packet.
	 *
	 * @param buf
	 */
	public final void encrypt(ByteBuffer buf) {
		crypt.encrypt(buf);
	}

	/**
	 * Sends AionServerPacket to this client.
	 *
	 * @param bp
	 *            AionServerPacket to be sent.
	 */
	public final void sendPacket(AionServerPacket bp) {
		synchronized (guard) {
			/**
			 * Connection is already closed or waiting for last (close packet) to be sent
			 */
			if (isWriteDisabled()) {
				return;
			}

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	/**
	 * Its guaranteed that closePacket will be sent before closing connection, but all past and future packets wont. Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be
	 * called to clear all other things. forced means that server shouldn't wait with removing this connection. closePacket Packet that will be send before closing. forced have no effect in this
	 * implementation.
	 */
	public final void close(AionServerPacket closePacket, boolean forced) {
		synchronized (guard) {
			if (isWriteDisabled()) {
				return;
			}

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
	}

	/**
	 * Current state of this connection
	 *
	 * @return state
	 */
	public final State getState() {
		return state;
	}

	/**
	 * Sets the state of this connection
	 *
	 * @param state
	 *            state of this connection
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Returns account object associated with this connection
	 *
	 * @return account object associated with this connection
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets account object associated with this connection
	 *
	 * @param account
	 *            account object associated with this connection
	 */
	public void setAccount(Account account) {
		Preconditions.checkArgument(account != null, "Account can't be null");
		this.account = account;
	}

	/**
	 * Sets Active player to new value. Update connection state to correct value.
	 *
	 * @param player
	 * @return True if active player was set to new value.
	 */
	public boolean setActivePlayer(Player player) {
		if (player == null) {
			activePlayer.set(player);
			setState(State.AUTHED);
		}
		else if (activePlayer.compareAndSet(null, player)) {
			setState(State.IN_GAME);
			lastPlayerName = player.getName();
		}
		else {
			return false;
		}
		return true;
	}

	/**
	 * Return active player or null.
	 *
	 * @return active player or null.
	 */
	public Player getActivePlayer() {
		return activePlayer.get();
	}

	/**
	 * @return the lastPingTimeMS
	 */
	public long getLastPingTimeMS() {
		return lastPingTimeMS;
	}

	/**
	 * @param lastPingTimeMS
	 *            the lastPingTimeMS to set
	 */
	public void setLastPingTimeMS(long lastPingTimeMS) {
		this.lastPingTimeMS = lastPingTimeMS;
	}

	public void closeNow() {
		this.close(false);
	}

	public void setMacAddress(String mac) {
		this.macAddress = mac;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public String getHddSerial() {
		return hdd_serial;
	}

	public String getIpv4list() {
		return ipv4list;
	}

	public String getLocalIP() {
		return local_ip;
	}

	public String getWindows() {
		return windows;
	}

	@Override
	public String toString() {
		Player player = activePlayer.get();
		if (player != null) {
			return "AionConnection [state=" + state + ", account=" + account + ", getObjectId()=" + player.getObjectId() + ", lastPlayerName=" + lastPlayerName + ", macAddress=" + macAddress + ",hddSerial=" + hdd_serial + ", getIP()=" + getIP() + "]";
		}
		return "";
	}

	private class PingChecker implements Runnable {

		// we don't have to detect hanged connections immediately
		// its rather some very rare case so 10 minutes check should be enough
		private static final int checkTime = 10 * 60 * 1000;
		private ScheduledFuture<?> task;
		private boolean started;

		private void start() {
			Preconditions.checkState(!started, "PingChecker can be started only one time!");
			started = true;
			task = ThreadPoolManager.getInstance().scheduleAtFixedRate(this, checkTime, checkTime);
		}

		private void stop() {
			task.cancel(false);
		}

		@Override
		public void run() {
			if (System.currentTimeMillis() - getLastPingTimeMS() > checkTime) {
				log.info("Found hanged up client: " + AionConnection.this + " - closing now :)");
				closeNow();
			}
		}
	}
}
