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


package com.aionemu.loginserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.network.IPRange;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.GsConnection.State;

/**
 * This class represents GameServer at LoginServer side. It contain info about
 * id, ip etc.
 *
 * @author -Nemesiss-
 */
public class GameServerInfo {

    /**
     * Id of this GameServer
     */
    private final byte id;
    /**
     * Allowed IP for this GameServer if gs will connect from another ip wont be
     * registered.
     */
    private final String ip;
    /**
     * Password
     */
    private final String password;
    /**
     * Default server address, usually internet address
     */
    private byte[] defaultAddress;
    /**
     * Mapping of ip ranges, usually used for local area connections
     */
    private List<IPRange> ipRanges;
    /**
     * Port on with this GameServer is accepting clients.
     */
    private int port;
    /**
     * gsConnection - if GameServer is connected to LoginServer.
     */
    private GsConnection gscHandler;
    /**
     * Max players count that may play on this GameServer.
     */
    private int maxPlayers;
    /**
     * Map<AccId,Account> of accounts logged in on this GameServer.
     */
    private final Map<Integer, Account> accountsOnGameServer = new HashMap<Integer, Account>();

    /**
     * Constructor.
     *
     * @param id
     * @param ip
     * @param password
     */
    public GameServerInfo(byte id, String ip, String password) {
        this.id = id;
        this.ip = ip;
        this.password = password;
    }

    /**
     * Returns id of this GameServer.
     *
     * @return byte id
     */
    public byte getId() {
        return id;
    }

    /**
     * Returns Password of this GameServer.
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns allowed IP for this GameServer.
     *
     * @return String ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns port of this GameServer.
     *
     * @return in port
     */
    public int getPort() {
        return port;
    }

    /**
     * Set port for this GameServer.
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Retunrs default server address, usually used as internet address
     *
     * @return default server address
     */
    public byte[] getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * Sets default server address
     *
     * @param defaultAddress default server address
     */
    public void setDefaultAddress(byte[] defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    /**
     * Returns IP range mappings
     *
     * @return IPRange mappings
     */
    public List<IPRange> getIpRanges() {
        return ipRanges;
    }

    /**
     * Sets IPRange mappings
     *
     * @param ipRanges ipRangeMappings
     */
    public void setIpRanges(List<IPRange> ipRanges) {
        this.ipRanges = ipRanges;
    }

    /**
     * Returns active GsConnection for this GameServer or null if this
     * GameServer is down.
     *
     * @return GsConnection
     */
    public final GsConnection getConnection() {
        return gscHandler;
    }

    /**
     * Set active GsConnection.
     *
     * @param gsConnection
     */
    public final void setConnection(GsConnection gscHandler) {
        this.gscHandler = gscHandler;
    }

    /**
     * Returns number of max allowed players for this GameServer.
     *
     * @return int maxPlayers
     */
    public final int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set max allowed players for this GameServer.
     *
     * @param maxPlayers
     */
    public final void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Check if GameServer is Online
     *
     * @return true if GameServer is Online.
     */
    public final boolean isOnline() {
        return gscHandler != null && gscHandler.getState() == State.AUTHED;
    }

    /**
     * Check if given account is already on This GameServer
     *
     * @param accountId
     * @return true if account is on this GameServer
     */
    public final boolean isAccountOnGameServer(int accountId) {
        return accountsOnGameServer.containsKey(accountId);
    }

    /**
     * Remove account from this GameServer
     *
     * @param accountId
     * @return removed account.
     */
    public final Account removeAccountFromGameServer(int accountId) {
        return accountsOnGameServer.remove(accountId);
    }

    /**
     * Add account to this GameServer
     *
     * @param acc
     */
    public final void addAccountToGameServer(Account acc) {
        accountsOnGameServer.put(acc.getId(), acc);
    }

    /**
     * Get Account object from account on GameServer list.
     *
     * @param accountId
     * @return Account object if account is on this game server or null.
     */
    public final Account getAccountFromGameServer(int accountId) {
        return accountsOnGameServer.get(accountId);
    }

    /**
     * Clears all accounts on this gameServer
     */
    public void clearAccountsOnGameServer() {
        accountsOnGameServer.clear();
    }

    /**
     * Return number of online players connected to this GameServer.
     *
     * @return number of online players
     */
    public int getCurrentPlayers() {
        return accountsOnGameServer.size();
    }

    /**
     * Return true if server is full.
     *
     * @return true if full.
     */
    public boolean isFull() {
        return getCurrentPlayers() >= getMaxPlayers();
    }

    /**
     * Returns ip address that will be used as server ip for specific
     * player.<br>
     * The problem is that players can access server from various subnetworks so
     * we need to send different ip adresses.<br>
     * If gameserver is not online - it returns 127.0.0.1 as server address.
     *
     * @param playerIp Player address
     * @return ip address that is valid for player
     */
    public byte[] getIPAddressForPlayer(String playerIp) {
        if (!isOnline()) {
            return new byte[]{127, 0, 0, 1};
        }

        for (IPRange ipr : ipRanges) {
            if (ipr.isInRange(playerIp)) {
                return ipr.getAddress();
            }
        }

        return defaultAddress;
    }
}
