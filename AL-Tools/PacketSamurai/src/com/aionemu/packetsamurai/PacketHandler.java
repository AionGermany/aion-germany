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


package com.aionemu.packetsamurai;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.logreaders.AbstractLogReader;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.session.GameSessionTable;
import com.aionemu.packetsamurai.session.TCPSession;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

/**
 * 
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 *
 */
public class PacketHandler implements PacketReceiver
{
    private final Captor _captor;
    private final Executor _asyncPacketProcessor;
    
    public PacketHandler(Captor captor)
    {
        _captor = captor;
        _asyncPacketProcessor = Executors.newSingleThreadExecutor();
    }
    
    public void receivePacket(final Packet p)
    {
    	if(!((Main)PacketSamurai.getUserInterface()).isRecording())
    		return;

        // do processing async
        _asyncPacketProcessor.execute(new Runnable()
        {
           public void run()
           {
              PacketHandler.this.processReceivedPacket(p);
           }
        });
    }

    public void processReceivedPacket(Packet p)
    {
        try
        {
            if (!(p instanceof TCPPacket))
                return;

            TCPPacket tcpPacket = (TCPPacket) p;
            long sessionId = tcpPacket.src_port * tcpPacket.dst_port;
            int port;
            InetAddress client;
            InetAddress server;

            if (_captor.isClientAddress(tcpPacket.src_ip))
            {
                port = tcpPacket.dst_port;
                client = tcpPacket.src_ip;
                server = tcpPacket.dst_ip;
            }
            else
            {
                port = tcpPacket.src_port;
                client = tcpPacket.dst_ip;
                server = tcpPacket.src_ip;
            }
            
            if (_captor instanceof FileCaptor)
            {
                FileCaptor fCaptor = (FileCaptor)_captor;
                TCPSession tcpSession = fCaptor.getSessionByID(sessionId);
                long time = (p.sec * 1000) + (p.usec / 1000);

                if (tcpSession == null)
                {
                    System.err.println("PacketHandler : receivePacket (from FileCaptor) : New TCPSession ("+sessionId+") - Port: "+port);
                    Protocol proto = AbstractLogReader.getLogProtocolByPort(port);
                    if (proto != null)
                    {
                        tcpSession = new TCPSession(sessionId, proto, "pcap", true);
                        tcpSession.setClientIp((Inet4Address) client);
                        tcpSession.setServerIp((Inet4Address) server);
                        fCaptor.addFileTCPSession(tcpSession);
                        System.err.println("CAPTOR: "+fCaptor);
                        tcpSession.receivePacket(tcpPacket, !_captor.isClientAddress(tcpPacket.src_ip), time);
                    }
                    else
                    {
                        //XXX future packets of this sessionid, will once again try to create the session
                        //we should somehow "ban" the session
                        PacketSamurai.getUserInterface().log("PCapReader: ERROR: No protocol specified.");
                    }
                }
                else
                {
                    tcpSession.receivePacket(tcpPacket, !_captor.isClientAddress(tcpPacket.src_ip), time);
                }
            }
            else
            {
                if ((tcpPacket.fin || tcpPacket.rst) && !GameSessionTable.getInstance().sessionExists(sessionId))
                    return;

                if (!GameSessionTable.getInstance().sessionExists(sessionId))
                {
                    try
                    {
                        TCPSession session = GameSessionTable.getInstance().newGameSession(sessionId, port, server, client);
                        session.receivePacket(tcpPacket, !_captor.isClientAddress(tcpPacket.src_ip));
                    }
                    catch (IOException e)
                    {
                        PacketSamurai.getUserInterface().log("ERROR: Failed to create new Log Session, an I/O error occurred when creating the respective log file.");
                    }
                    catch (IllegalStateException e)
                    {
                        PacketSamurai.getUserInterface().log(e.getMessage());
                    }
                }
                else
                {
                    GameSessionTable.getInstance().getSession(sessionId).receivePacket(tcpPacket, !_captor.isClientAddress(tcpPacket.src_ip));
                }
            }
        }
        catch (Throwable t)
        {
            /*
             * If this methods throws an exception when called by a native jpcap method (ie loopPacket)
             * i do get a full JVM crash (EXCEPTION_ACCESS_VIOLATION)
             * thus im enclosing it on a try as a way to increase the logger stability
             */
            PacketSamurai.getUserInterface().log("SEVERE: ERROR: Please report the exception trace, the logger will attempt to proceed through. ("+t.toString()+")");
            t.printStackTrace();
        }
    }
}