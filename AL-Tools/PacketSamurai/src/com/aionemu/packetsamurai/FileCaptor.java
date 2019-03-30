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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javolution.util.FastList;
import javolution.util.FastSet;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.protocol.ProtocolManager;
import com.aionemu.packetsamurai.session.TCPSession;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class FileCaptor extends Captor
{
    private Set<TCPSession> _fileCaptorSessions;
    
    public FileCaptor(String pcapFile)
    {
        _fileCaptorSessions = new FastSet<TCPSession>();
        try
        {
            _packetCaptor = JpcapCaptor.openFile(pcapFile);
            Set<Integer> ports = ProtocolManager.getInstance().getAvailablePorts();
            Iterator<Integer> i = ports.iterator();
            StringBuilder portsSB = new StringBuilder();
            if(i.hasNext())
            {
                StringBuilder sb = new StringBuilder("tcp port");
                for(; i.hasNext();)
                {
                    Integer port = i.next();
                    sb.append(" ").append(port);
                    portsSB.append(port);
                    if(i.hasNext())
                    {
                        sb.append(" or tcp port");
                        portsSB.append(" ");
                    }
                }
                _packetCaptor.setFilter(sb.toString(),true);
            }
            setCaptor(true);
            PacketSamurai.getUserInterface().log("Successfully opened File "+pcapFile+".");
            PacketSamurai.getUserInterface().log("Filtering for port(s): "+portsSB);
        }
        catch (IOException ioe)
        {
            PacketSamurai.getUserInterface().log("ERROR: Failed to open file ("+pcapFile+") for capture"+ioe);
        }
    }
    
    public void proccesCaptureFile()
    {
        // TODO move this dialog stuff from here or at least redo the threading stuff correctly
        final JDialog dialog = new JDialog(((Main) PacketSamurai.getUserInterface()).getMainFrame(),"Please Wait");
        JPanel panel = new JPanel();
        dialog.setContentPane(panel);
        panel.add(new JLabel("Please wait, Getting TCP packets and resolving host names..."));
        dialog.setSize(400, 75);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getMainFrame());
        dialog.setVisible(true);
        
        final CountDownLatch mainlatch = new CountDownLatch(1);
        
        Thread pthread = new Thread(new Runnable()
        {

            public void run()
            {
                final FastList<Packet> packets = new FastList<Packet>();
                Vector<InetAddress> addresses = new Vector<InetAddress>();
                Set<InetAddress> ipAddresses = new FastSet<InetAddress>(); // just used for teh contains() call
                while (true)
                {
                    Packet p = _packetCaptor.getPacket();
                    if (p == null || p == Packet.EOF)
                    {
                        break;
                    }
                    packets.add(p);
                    InetAddress addr = ((IPPacket)p).src_ip;
                    if(!addresses.contains(addr))
                    {
                        try
                        {
                            Resolver res = new ExtendedResolver();
                            Name name = ReverseMap.fromAddress(addr.getAddress());
                            int type = Type.PTR;
                            int dclass = DClass.IN;
                            Record rec = Record.newRecord(name, type, dclass);
                            Message query = Message.newQuery(rec);
                            Message response = res.send(query);

                            Record[] answers = response.getSectionArray(Section.ANSWER);
                            if (answers.length > 0)
                                addr = InetAddress.getByAddress(answers[0].rdataToString(), addr.getAddress());
                        } catch (UnknownHostException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        addresses.add(addr);
                    }
                    ipAddresses.add(((IPPacket)p).src_ip);
                }
                System.err.println("asd");
                final CountDownLatch latch = new CountDownLatch(1);
                final JDialog choseDialog = new JDialog(((Main) PacketSamurai.getUserInterface()).getMainFrame(),"Chose client inet address");
                JPanel chosePanel = new JPanel();
                JButton okButton = new JButton("Ok");
                okButton.setActionCommand("ok");
                final JComboBox<InetAddress> combo = new JComboBox<InetAddress>(addresses);
                okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent a)
                    {
                        if(a.getActionCommand().equals("ok"))
                        {
                            _networkAddress = new NetworkInterfaceAddress[1];
                            _networkAddress[0] = new NetworkInterfaceAddress(null, null, null, null);
                            _networkAddress[0].address = (InetAddress) combo.getSelectedItem();
                            choseDialog.setVisible(false);
                            choseDialog.dispose();
                            latch.countDown();
                        }
                    }
                });

                JLabel lbl = new JLabel("Please choose the client IP InetAddress");
                chosePanel.add(lbl);
                chosePanel.add(combo);
                chosePanel.add(okButton);
                choseDialog.setSize(450, 150);
                choseDialog.setResizable(false);
                choseDialog.setContentPane(chosePanel);
                choseDialog.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getMainFrame());
                dialog.setVisible(false);
                choseDialog.setVisible(true);
                Thread t = new Thread
                (
                        new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    latch.await();
                                } 
                                catch (InterruptedException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                System.err.println("We have "+packets.size()+" tcp packets");
                                PacketHandler pHandler = (PacketHandler)_packetHandler;
                                if(_networkAddress != null)
                                {
                                	int packetCount = 0;
                                	long nextTimeAnnounce = System.currentTimeMillis();
                                	PacketSamurai.logWriting(false);
                                    for(Packet p : packets)
                                    {
                                        packetCount++;
        
                                        if (nextTimeAnnounce <= System.currentTimeMillis()) {
                                        	nextTimeAnnounce = System.currentTimeMillis() + 15000;
                                        	PacketSamurai.getUserInterface().log("Current packets processed " + packetCount + "/" + packets.size());
                                        }
                                        pHandler.processReceivedPacket(p);
                                    }
                                    PacketSamurai.logWriting(true);
                                    
                                }
                                else
                                {
                                    System.err.println("Null network address");
                                }
                                _packetCaptor.close();
                                mainlatch.countDown();
                            }
                        }
                );
                t.start();
                try
                {
                    t.join();
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        pthread.start();
        try
        {
            pthread.join();
        }
        catch (InterruptedException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        try
        {
            mainlatch.await();
            dialog.dispose();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return A TCPSession if this is a file captor, null otherwise
     */
    public Set<TCPSession> getFileTCPSessions()
    {
        return _fileCaptorSessions;
    }
    
    public void run()
    {
        throw new IllegalStateException("Can not call run on a FileCaptor");
    }
    
    public TCPSession getSessionByID(long sessionId)
    {
        for(TCPSession s : _fileCaptorSessions)
        {
            if(s.getSessionId() == sessionId)
                return s;
        }
        return null;
    }

    public void addFileTCPSession(TCPSession tcpSession)
    {
        System.err.println("ADICIONEI SESSION");
        System.err.flush();
        _fileCaptorSessions.add(tcpSession);        
    }
}