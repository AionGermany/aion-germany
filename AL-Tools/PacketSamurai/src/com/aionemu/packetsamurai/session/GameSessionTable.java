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


package com.aionemu.packetsamurai.session;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import com.aionemu.packetsamurai.Captor;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.protocol.Protocol;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Ulysses R. Ribeiro
 *
 */
public class GameSessionTable 
{
	private Map<Long, PSLogSession> _gameSessionTable;
	private PSLogSession gameSession;
	
	private static class SingletonHolder
	{
		private final static GameSessionTable singleton = new GameSessionTable();
	}
	
	public static GameSessionTable getInstance()
	{
		return SingletonHolder.singleton;
	}
	
	private GameSessionTable()
	{
		_gameSessionTable = new FastMap<Long, PSLogSession>();
	}
	
	public TCPSession newGameSession(long sessionId, int port, InetAddress serverAddr, InetAddress clientAddr) throws IOException
	{
	    Protocol protocol = Captor.getActiveProtocolForPort(port);
	    if (protocol != null)
	    {
			if (port == 2106) {
				gameSession = new PSLogSession(sessionId, protocol, "Login-", true, serverAddr, clientAddr);
				PacketSamurai.getUserInterface().log("Started to log new Login-Session (ID: "+gameSession.getSessionId()+")");
			} else if (port == 7777) {
				gameSession = new PSLogSession(sessionId, protocol, "Game-", true, serverAddr, clientAddr);
				PacketSamurai.getUserInterface().log("Started to log new Game-Session (ID: "+gameSession.getSessionId()+")");
			} else {
				gameSession = new PSLogSession(sessionId, protocol, "Chat-", true, serverAddr, clientAddr);
				PacketSamurai.getUserInterface().log("Started to log new Chat-Session (ID: "+gameSession.getSessionId()+")");
			}
	        _gameSessionTable.put(sessionId, gameSession);
	        if (PacketSamurai.getUserInterface() instanceof Main)
	        {
	            ((Main) PacketSamurai.getUserInterface()).showSession(gameSession, true, false, null);
	        }
	        return gameSession;
	    }
        throw new IllegalStateException("ERROR: Received packet on port ("+port+") but there is no active protocol for this port.");
	}
	
	public void removeGameSession(long sessionId)
	{
		_gameSessionTable.remove(sessionId);
	}
	
	public boolean sessionExists(long sessionId)
	{
		return _gameSessionTable.containsKey(sessionId);
	}
	
	public TCPSession getSession(long sessionId)
	{
		return _gameSessionTable.get(sessionId);
	}
	
	public List<TCPSession> getSessions()
	{
		FastList<TCPSession> sessions = new FastList<TCPSession>();
		sessions.addAll(_gameSessionTable.values());
		return sessions;
	}
}
