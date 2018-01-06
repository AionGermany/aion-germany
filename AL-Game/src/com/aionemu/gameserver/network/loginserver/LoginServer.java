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
package com.aionemu.gameserver.network.loginserver;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.NioServer;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.AccountTime;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_L2AUTH_LOGIN_CHECK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECONNECT_KEY;
import com.aionemu.gameserver.network.loginserver.LoginServerConnection.State;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_AUTH;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_DISCONNECTED;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_BAN;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_LS_CONTROL;
import com.aionemu.gameserver.services.AccountService;
import com.aionemu.gameserver.services.player.PlayerLeaveWorldService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * Utill class for connecting GameServer to LoginServer.
 *
 * @author -Nemesiss-
 */
public class LoginServer {

	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(LoginServer.class);
	/**
	 * Map<accountId,Connection> for waiting request. This request is send to LoginServer and GameServer is waiting for response.
	 */
	private Map<Integer, AionConnection> loginRequests = new HashMap<Integer, AionConnection>();
	/**
	 * Map<accountId,Connection> for all logged in accounts.
	 */
	private Map<Integer, AionConnection> loggedInAccounts = new HashMap<Integer, AionConnection>();
	/**
	 * Connection to LoginServer.
	 */
	private LoginServerConnection loginServer;
	private NioServer nioServer;
	private boolean serverShutdown = false;

	public static final LoginServer getInstance() {
		return SingletonHolder.instance;
	}

	private LoginServer() {
	}

	public void setNioServer(NioServer nioServer) {
		this.nioServer = nioServer;
	}

	/**
	 * Connect to LoginServer and return object representing this connection. This method is blocking and may block till connect successful.
	 *
	 * @return LoginServerConnection
	 */
	public LoginServerConnection connect() {
		SocketChannel sc;
		for (;;) {
			loginServer = null;
			log.info("Connecting to LoginServer: " + NetworkConfig.LOGIN_ADDRESS);
			try {
				sc = SocketChannel.open(NetworkConfig.LOGIN_ADDRESS);
				sc.configureBlocking(false);
				Dispatcher d = nioServer.getReadWriteDispatcher();
				loginServer = new LoginServerConnection(sc, d);

				// register
				d.register(sc, SelectionKey.OP_READ, loginServer);

				// initialized
				loginServer.initialized();

				return loginServer;
			}
			catch (Exception e) {
				log.info("Cant connect to LoginServer: " + e.getMessage());
				System.out.println("");
			}
			try {
				/**
				 * 10s sleep
				 */
				Thread.sleep(10 * 1000);
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * This method is called when we lost connection to LoginServer. We will disconnects all aionClients waiting for LoginServer response and also try reconnect to LoginServer.
	 */
	public void loginServerDown() {
		log.warn("Connection with LoginServer lost...");

		loginServer = null;
		synchronized (this) {
			/**
			 * We lost connection for LoginServer so client pending authentication should be disconnected [cuz authentication will never ends]
			 */
			for (AionConnection client : loginRequests.values()) {
				// TODO! somme error packet!
				client.close(/* closePacket, */true);
			}
			loginRequests.clear();
		}

		/**
		 * Reconnect after 5s if not server shutdown sequence
		 */
		if (!serverShutdown) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					connect();
				}
			}, 5000);
		}
	}

	/**
	 * Notify that client is disconnected - we must clear waiting request to LoginServer if any to prevent leaks. Also notify LoginServer that this account is no longer on GameServer side.
	 *
	 * @param client
	 */
	public void aionClientDisconnected(int accountId) {
		synchronized (this) {
			loginRequests.remove(accountId);
			loggedInAccounts.remove(accountId);
		}
		sendAccountDisconnected(accountId);
	}

	/**
	 * @param accountId
	 */
	private void sendAccountDisconnected(int accountId) {
		log.info("Sending account disconnected " + accountId);
		if (loginServer != null && loginServer.getState() == State.AUTHED) {
			loginServer.sendPacket(new SM_ACCOUNT_DISCONNECTED(accountId));
		}
	}

	/**
	 * Starts authentication procedure of this client - LoginServer will sends response with information about account name if authentication is ok.
	 *
	 * @param accountId
	 * @param client
	 * @param loginOk
	 * @param playOk1
	 * @param playOk2
	 */
	public void requestAuthenticationOfClient(int accountId, AionConnection client, int loginOk, int playOk1, int playOk2) {
		/**
		 * There are no connection to LoginServer. We should disconnect this client since authentication is not possible.
		 */
		if (loginServer == null || loginServer.getState() != State.AUTHED) {
			log.debug("LS !!! " + (loginServer == null ? "NULL" : loginServer.getState()));
			// TODO! some error packet!
			client.close(/* closePacket, */true);
			return;
		}

		synchronized (this) {
			if (loginRequests.containsKey(accountId)) {
				return;
			}
			loginRequests.put(accountId, client);
		}
		loginServer.sendPacket(new SM_ACCOUNT_AUTH(accountId, loginOk, playOk1, playOk2));
	}

	/**
	 * This method is called by CM_ACCOUNT_AUTH_RESPONSE LoginServer packets to notify GameServer about results of client authentication.
	 *
	 * @param accountId
	 * @param accountName
	 * @param result
	 * @param accountTime
	 */
	public void accountAuthenticationResponse(int accountId, String accountName, boolean result, AccountTime accountTime, byte accessLevel, byte membership, long toll, long luna, byte isReturn) {
		AionConnection client = loginRequests.remove(accountId);

		if (client == null) {
			return;
		}

		Account account = AccountService.getAccount(accountId, accountName, accountTime, accessLevel, membership, toll, luna, isReturn);
		if (!validateAccount(account)) {
			log.info("[LoginServer] Illegal account auth detected: " + accountId);
			client.close(new SM_L2AUTH_LOGIN_CHECK(false, accountName), true);
			return;
		}

		if (result) {
			client.setAccount(account);
			client.setState(AionConnection.State.AUTHED);
			loggedInAccounts.put(accountId, client);
			log.info("[LoginServer] Account authed: " + accountId + " = " + accountName);
			client.sendPacket(new SM_L2AUTH_LOGIN_CHECK(true, accountName));
		}
		else {
			log.info("[LoginServer] Account not authed: " + accountId);
			client.close(new SM_L2AUTH_LOGIN_CHECK(false, accountName), true);
		}
	}

	/**
	 * @param account
	 * @return
	 */
	private boolean validateAccount(Account account) {
		for (PlayerAccountData accountData : account) {
			if (accountData.getPlayerCommonData().isOnline()) {
				log.warn("[LoginServer] [AUDIT] Possible dupe hack account: " + account.getId());
				Player player = World.getInstance().findPlayer(accountData.getPlayerCommonData().getPlayerObjId());
				if (player != null) {
					// kick
					PlayerLeaveWorldService.startLeaveWorld(player);
				}
				else {
					// db update offline
					DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, false);
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * Starts reconnection to LoginServer procedure. LoginServer in response will send reconnection key.
	 *
	 * @param client
	 */
	public void requestAuthReconnection(AionConnection client) {
		/**
		 * There are no connection to LoginServer. We should disconnect this client since authentication is not possible.
		 */
		if (loginServer == null || loginServer.getState() != State.AUTHED) {
			// TODO! somme error packet!
			client.close(/* closePacket, */false);
			return;
		}

		synchronized (this) {
			if (loginRequests.containsKey(client.getAccount().getId())) {
				return;
			}
			loginRequests.put(client.getAccount().getId(), client);

		}
		loginServer.sendPacket(new SM_ACCOUNT_RECONNECT_KEY(client.getAccount().getId()));
	}

	/**
	 * This method is called by CM_ACCOUNT_RECONNECT_KEY LoginServer packets to give GameServer reconnection key for client that was requesting reconnection.
	 *
	 * @param accountId
	 * @param reconnectKey
	 */
	public void authReconnectionResponse(int accountId, int reconnectKey) {
		AionConnection client = loginRequests.remove(accountId);

		if (client == null) {
			return;
		}

		log.info("[LoginServer] Account reconnecting: " + accountId + " = " + client.getAccount().getName());
		client.close(new SM_RECONNECT_KEY(reconnectKey), false);
	}

	/**
	 * This method is called by CM_REQUEST_KICK_ACCOUNT LoginServer packets to request GameServer to disconnect client with given account id.
	 *
	 * @param accountId
	 */
	public void kickAccount(int accountId) {
		synchronized (this) {
			AionConnection client = loggedInAccounts.get(accountId);
			if (client != null) {
				closeClientWithCheck(client, accountId);
			} // This account is not logged in on this GameServer but LS thinks different...
			else {
				sendAccountDisconnected(accountId);
			}
		}
	}

	private void closeClientWithCheck(AionConnection client, final int accountId) {
		log.info("[LoginServer] Closing client connection " + accountId);
		client.close(/* closePacket, */false);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AionConnection client = loggedInAccounts.get(accountId);
				if (client != null) {
					log.warn("[LoginServer] Removing client from server because of stalled connection");
					client.close(false);
					loggedInAccounts.remove(accountId);
					sendAccountDisconnected(accountId);
				}
			}
		}, 5000);
	}

	/**
	 * Returns unmodifiable map with accounts that are logged in to current GS Map Key: Account ID Map Value: AionConnectionObject
	 *
	 * @return unmodifiable map wwith accounts
	 */
	public Map<Integer, AionConnection> getLoggedInAccounts() {
		return Collections.unmodifiableMap(loggedInAccounts);
	}

	/**
	 * When Game Server shutdown, have to close all pending client connection
	 */
	public void gameServerDisconnected() {
		synchronized (this) {
			serverShutdown = true;
			/**
			 * GameServer shutting down, must close all pending login requests
			 */
			for (AionConnection client : loginRequests.values()) {
				// TODO! some error packet!
				client.close(/* closePacket, */true);
			}
			loginRequests.clear();

			if (loginServer != null) {
				loginServer.close(false);
			}
		}

		log.info("[LoginServer] GameServer disconnected from the Login Server...");
	}

	public void sendLsControlPacket(String accountName, String playerName, String adminName, int param, int type) {
		if (loginServer != null && loginServer.getState() == State.AUTHED) {
			loginServer.sendPacket(new SM_LS_CONTROL(accountName, playerName, adminName, param, type));
		}
	}

	public void accountUpdate(int accountId, byte param, int type) {
		synchronized (this) {
			AionConnection client = loggedInAccounts.get(accountId);
			if (client != null) {
				Account account = client.getAccount();
				if (type == 1) {
					account.setAccessLevel(param);
				}
				if (type == 2) {
					account.setMembership(param);
				}
			}
		}
	}

	public void sendBanPacket(byte type, int accountId, String ip, int time, int adminObjId) {
		if (loginServer != null && loginServer.getState() == State.AUTHED) {
			loginServer.sendPacket(new SM_BAN(type, accountId, ip, time, adminObjId));
		}
	}

	public boolean sendPacket(LsServerPacket pk) {
		if (loginServer != null && loginServer.getState() == State.AUTHED) {
			loginServer.sendPacket(pk);
			return true;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final LoginServer instance = new LoginServer();
	}
}
