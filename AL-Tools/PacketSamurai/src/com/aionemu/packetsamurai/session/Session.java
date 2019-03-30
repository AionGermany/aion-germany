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
import java.net.Inet4Address;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.crypt.NullCrypter;
import com.aionemu.packetsamurai.crypt.ProtocolCrypter;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.logwriters.PSLogWriter;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.utils.collector.Collector;
import com.aionemu.packetsamurai.utils.collector.DataManager;

import javolution.util.FastList;
/**
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 *
 */
public class Session 
{
	private long _sessionId;
	
    private ProtocolCrypter _crypt;
	
	private Protocol _protocol;
	
	private String _sessionName;
	
	private FastList<DataPacket> _packets;
	
	private Runnable _newPacketNotification;

	private boolean _shown;

    private Inet4Address _clientIP;

    private Inet4Address _serverIP;
    
    private String _serverType;
    
    private long _analyserBits;

    private String _comments;
    
    private boolean _decrypt;

    private boolean _parse;
    
    private long _firstTimeStamp;

    private Collector _collector;

	private AtomicInteger _packetNumberId;
    
    public Session(long sessionId, Protocol protocol, String prefix, boolean crypted, boolean decrypt)
    {
        _sessionName = prefix+sessionId;
        _packets = new FastList<DataPacket>();
        _sessionId = sessionId;
        _protocol = protocol;
        _decrypt = decrypt || !crypted;
        _parse = PacketSamurai.PARSER_ACTIVE;
        _collector = new Collector();
	    _packetNumberId = new AtomicInteger(0);
        init(crypted);
    }
    
    public Collector getCollector() {
    	return _collector;
    }
	
	public Session(long sessionId, Protocol protocol, String prefix, boolean crypted)
	{
        this(sessionId, protocol, prefix, crypted, true);
	}
    
    private void init(boolean crypted)
    {
        if (crypted && _decrypt)
        {
            try
            {
                Class<?> clazz = Class.forName("com.aionemu.packetsamurai.crypt."+_protocol.getEncryption()+"Crypter");
                _crypt = (ProtocolCrypter) clazz.newInstance();
            }
            catch (SecurityException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ClassCastException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            _crypt = new NullCrypter();
        }
        _crypt.setProtocol(_protocol);
    }

	public int getNewPacketNumber()
	{
		return _packetNumberId.incrementAndGet();
	}

    /**
     * <li>Decrypts the raw data received</li>
     * <li>Adds a DataPacket to the packetList, instantiated from the decrypted data</li>
     * <li>Runs the packet notification, if any</li>
     * 
     * @param data the raw data WITHOUT header
     * @param fromServer should be true if packet came from server, false otherwise
     * @param time (miliseconds)
     */
	public void addPacket(byte[] data, boolean fromServer, long time)
	{
        packetDirection direction = (fromServer ? packetDirection.serverPacket : packetDirection.clientPacket);
        
        if (_decrypt)
        {
            _crypt.decrypt(data, direction);
        }

        DataPacket dp = new DataPacket(_packetNumberId.incrementAndGet(), data, direction, time, _protocol,_parse);
        if (Collector.isEnabled()){
        	_collector.parse(dp, direction);
        }

    	if(((Main)PacketSamurai.getUserInterface()).isSkippingPackets())
    		return;
        
		_packets.add(dp);
		
		if (_firstTimeStamp == 0) {
			_firstTimeStamp = time;
		}
		
		if (dp.hasError() || dp.hasWarning()) {
			String id = Integer.toHexString(0x10000 | data[0]).substring(1).toUpperCase();
			if (id.contains("FFF")) {
				id = "00" + id.substring(id.length()-2);
			}
			
			long myTime = time - _firstTimeStamp;
			
			PacketSamurai.getUserInterface().log(((fromServer) ? "Server " : "Client") + "Packet id: " + id + " of timestamp + " + myTime + "ms has this error msg:" +  dp.getErrorMessage(), false);
		}
        
		
        if (_newPacketNotification != null)
        {
            _newPacketNotification.run();
        }
	}

	public synchronized void flush()
	{
		_packetNumberId.set(0);
		_packets.clear();
	}

    public long getSessionId()
    {
        return _sessionId;
    }
    
    public String getSessionName()
    {
        return _sessionName;
    }
    
	public FastList<DataPacket> getPackets()
	{
		return _packets;
	}
	
	public void setPackets(FastList<DataPacket> packets){
		_packets=packets;
	}
	
	public void setNewPacketNotification(Runnable r)
	{
		_newPacketNotification = r;
	}

	public void setShown(boolean b)
	{
		_shown = b;
	}
	
	public boolean isShown()
	{
		return _shown;
	}

    public void setClientIp(Inet4Address ip)
    {
        _clientIP = ip;
    }
    
    public Inet4Address getClientIp()
    {
        return _clientIP;
    }

    public void setServerIp(Inet4Address ip)
    {
        _serverIP = ip;        
    }
    
    public Inet4Address getServerIp()
    {
        return _serverIP;
    }
    
    public Protocol getProtocol()
    {
        return _protocol;
    }
    
    public String getServerType()
    {
        return _serverType;
    }
    
    public void setServerType(String type)
    {
        _serverType = type;
    }
    
    public long getAnalyserBitSet()
    {
        return _analyserBits;
    }
    
    public void setAnalyserBitSet(long bits)
    {
        _analyserBits = bits;
    }
    
    
    public String getComments()
    {
        return _comments;
    }
    
    public void setComments(String com)
    {
        _comments = com;
    }

    public void saveSession()
    {
        try
        {
            PSLogWriter writer = new PSLogWriter(this);
            writer.writeLog();
            DataManager.save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void unloadPackets()
    {
        _packets.clear();        
    }
    
    public boolean isDecrypted()
    {
        return _decrypt;
    }
}

