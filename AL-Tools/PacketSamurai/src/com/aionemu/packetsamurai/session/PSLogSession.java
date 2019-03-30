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


/**
 * 
 */
package com.aionemu.packetsamurai.session;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.logwriters.PSLogWriter;
import com.aionemu.packetsamurai.protocol.Protocol;



/**
 * Basically a TCPSession that autmatically logs the packets on the fly.
 * 
 * @author Ulysses R. Ribeiro
 *
 */
public class PSLogSession extends TCPSession
{
    private PSLogWriter _logWriter;
    
    public PSLogSession(long sessionId, Protocol protocol, String prefix, boolean crypted, InetAddress serverAddr, InetAddress clientAddr) throws IOException
    {
        super(sessionId, protocol, prefix, crypted, PacketSamurai.DECRYPT_ACTIVE);
        this.setClientIp((Inet4Address) clientAddr);
        this.setServerIp((Inet4Address) serverAddr);
        _logWriter = new PSLogWriter(this, true);
    }
    
    @Override
    public void addPacket(byte[] data, boolean fromServer, long time)
    {
        // let it be added normally
        super.addPacket(data, fromServer, time);
        //notify writer
        try
        {
            this.getLogWriter().writePacket(this.getPackets().getLast());
        }
        catch (IOException e)
        {
            PacketSamurai.getUserInterface().log("ERROR: While writing packet data on Session ID: "+this.getSessionId()+", attempting to continue.");
        }
    }
    
    @Override
    public void saveSession()
    {
        // we are already saving the session on the fly
        // prevent the super method from doing it again
    }
    
    @Override
    public void close()
    {
        super.close();
        try
        {
            this.getLogWriter().close();
        }
        catch (IOException e)
        {
            PacketSamurai.getUserInterface().log("ERROR: While closing log of Session ID: "+this.getSessionId()+".");
        }
    }
    
    protected PSLogWriter getLogWriter()
    {
        return _logWriter;
    }

}
