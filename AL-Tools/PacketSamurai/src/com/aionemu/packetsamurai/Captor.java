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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.PacketReceiver;
import jpcap.packet.TCPPacket;


import com.aionemu.packetsamurai.gui.ChoiceDialog;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.ProtocolManager;

/**
 * 
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 *
 */
public class Captor implements Runnable
{

    protected JpcapCaptor _packetCaptor;
    protected NetworkInterfaceAddress[] _networkAddress;
    private boolean _captorEnabled = false;
    private static final int SNAPSHOT_LENGHT = 4*65536;
    private int _deviceId = -1;
    protected PacketReceiver _packetHandler;
    protected static Map<Integer, Protocol> _activeProtocols;
    
    private static class SingletonHolder
	{
		private final static Captor singleton = Captor.initCaptor();
	}
	
	public static Captor getInstance()
	{
		return SingletonHolder.singleton;
	}

    private static Captor initCaptor()
    {
        try
        {
            Captor captor = new Captor();
            captor.initDevice();
            Thread captorThread = new Thread(captor, "CaptorThread");
            captorThread.setPriority(Thread.MAX_PRIORITY);
            captorThread.start();
            return captor;
        }
        catch (Exception e)
        {
            Captor.showSetActiveProtocols();
            
            // try to init again (will ask user to set procotol agains if he canceled this previous one)
            return Captor.initCaptor();
        }
    }
    
    public Captor()
    {
        _packetHandler = new PacketHandler(this);
    }
    
    public void initDevice() throws Exception
    {        
    	this.configureProtocols();
    	Util.drawTitle("Network");
        NetworkInterface[] niList = null;
        try
        {
        	niList = JpcapCaptor.getDeviceList();
        }
        catch(UnsatisfiedLinkError ule)
        {
        	PacketSamurai.getUserInterface().log("System [Network] - ERROR: You are missing the JPcap lib :\n"+ule.getLocalizedMessage());
        	setCaptor(false);
        	return;
        }
        int deviceCount = niList.length;
        // Check if there is a device to sniff
        if (deviceCount <= 0)
        {
            setCaptor(false);
            PacketSamurai.getUserInterface().log("System [Network] - ERROR: No Network Interfaces have been found!");
        }
        //if there is only one we bind it
        else if (deviceCount == 1)
        {
            openDevice(niList[0]);
        }
        else
        {
            if (!PacketSamurai.configExists("NetworkInterface"))
            {
                selectNetWorkInterface();
            }
            else
            {
                int deviceNumber = Integer.parseInt(PacketSamurai.getConfigProperty("NetworkInterface"));
                openDevice(deviceNumber);
            }
        }
    }

    public void selectNetWorkInterface()
    {
        NetworkInterface niList[] = JpcapCaptor.getDeviceList();
        int deviceCount = niList.length;
        String nameList[] = new String[deviceCount];
        for (int i = 0; i < deviceCount; i++)
        {

            PacketSamurai.getUserInterface().log("Found Interface: "+niList[i].description);
            NetworkInterfaceAddress[] addresses = niList[i].addresses;
            NetworkInterfaceAddress address = null;
            for (NetworkInterfaceAddress address1 : addresses) {
                if (address1.subnet instanceof Inet4Address) {
                    address = address1;
                    break;
                }
            }
            if (address == null)
            	address = niList[i].addresses[0];
            
            nameList[i]=(niList[i].addresses.length >= 1 ? address.address.getHostAddress()+": " : "")
            +niList[i].description;
        }
        String[][] choices = new String[1][];
        choices[0] = nameList;
        
        int[] ret = ChoiceDialog.choiceDialog("Select Interface for Sniffing", new String[]{"Interfaces"} , choices);
        if (ret != null)
        {
            this.openDevice(ret[0]);
            PacketSamurai.setConfigProperty("NetworkInterface",Integer.toString(ret[0]));
        }
        else
        {
            PacketSamurai.getUserInterface().log("System [Network] - Error ! No interface selected.");
        }
        // TODO delete its stuff
        //Main.getInstance().showInterfaceSelector(nameList);
    }

    public void openDevice(int deviceNumber)
    {
        openDevice(JpcapCaptor.getDeviceList()[deviceNumber]);
        _deviceId = deviceNumber;
    }
    
    private void configureProtocols() throws Exception
    {
        Captor.setActiveProtocols(PacketSamurai.loadSnifferActiveProtocols());
        if (Captor.getActiveProtocols() == null)
        {
            Captor.setActiveProtocols(new FastMap<Integer, Protocol>());
            
            for (Protocol p : ProtocolManager.getInstance().getProtocols())
            {
                if (Captor.getActiveProtocols().containsKey(p.getPort()))
                {
                    // invalidate the map being built
                    Captor.setActiveProtocols(null);
                    throw new Exception("System [Network] - More then one protocol with same port, only one protocol per port can be active for the sniffer.");
                }
                Captor.getActiveProtocols().put(p.getPort(), p);
            }
        }

        
    }
    
    public void openDevice(NetworkInterface ni)
    {
        try
        {
            if (_packetCaptor != null)
            {
                setCaptor(false);
                _packetCaptor.close();
            }
            _packetCaptor = JpcapCaptor.openDevice(ni,SNAPSHOT_LENGHT,false,10);
            
            setCaptor(true);
            _networkAddress = ni.addresses;
            PacketSamurai.getUserInterface().log("System [Network] - Successfully opened device ("+ni.description+").");
            
            
            Set<Integer> ports = Captor.getActiveProtocols().keySet();
            Iterator<Integer> i = ports.iterator();

            String filter = PacketSamurai.getConfigProperty("filter", "").trim();
            if (filter.length() > 0)
            {
                PacketSamurai.getUserInterface().log("System [Network] - Sniffing with filter: "+filter);
                _packetCaptor.setFilter(filter, false);
            }
            else if (i.hasNext())
            {
                StringBuilder sb = new StringBuilder("(tcp port");
                StringBuilder portsSB = new StringBuilder();
                for(; i.hasNext();)
                {
                    Integer port = i.next();
                    sb.append(" ").append(port).append(")");
                    portsSB.append(port);
                    
                    if(i.hasNext())
                    {
                        portsSB.append(' ');
                        sb.append(" or (tcp port");
                    }
                }
                PacketSamurai.getUserInterface().log("System [Network] - Sniffing with filter: "+sb.toString());
                _packetCaptor.setFilter(sb.toString(),false);
                PacketSamurai.getUserInterface().log("System [Network] - Sniffing on port(s): "+portsSB);
            }
        }
        catch (IOException ioe)
        {
            PacketSamurai.getUserInterface().log("System [Network] - ERROR: Failed to open device ("+ni.description+") for capture "+ioe);
        }
    }


    public void setCaptor(boolean val)
    {
        if (!val && _captorEnabled)
        {
            _packetCaptor.breakLoop();
        }
        _captorEnabled = val;
    }
    
    public boolean isCaptorEnabled()
	{
		return _captorEnabled;
	}

    public void run()
    {
    	if(!this.isCaptorEnabled())
    		return;
        // capture packets indefinitely
        _packetCaptor.loopPacket( -1, _packetHandler);
    }
    
    public JpcapCaptor getPcapCaptor()
    {
        return _packetCaptor;
    }

    public boolean isClientAddress(InetAddress address)
    {
        for (NetworkInterfaceAddress _networkAddres : _networkAddress) {
            if (_networkAddres.address.equals(address))
                return true;
        }
        return false;
    }

    public int getCurrentDeviceId()
    {
        return _deviceId;
    }

    public static void setActiveProtocols(Map<Integer, Protocol> activeProtocols)
    {
        _activeProtocols = activeProtocols;
    }
    
    public static Protocol getActiveProtocolForPort(int port)
    {
        return Captor.getActiveProtocols().get(port);
    }
    
    protected static Map<Integer, Protocol> getActiveProtocols()
    {
        return _activeProtocols;
    }
    
    public String getPacketDump(TCPPacket tcpPacket)
    {

        return "System [Network] - Received a packet: " + PacketSamurai.hexDump(tcpPacket.data) + " - Flags: " + " - ACK: " + (tcpPacket.ack ? "1 : " + tcpPacket.ack_num : "0") + " - Fin: " + (tcpPacket.fin ? "1" : "0") + " - SYN: " + (tcpPacket.syn ? "1" : "0") + " - FIN: " + (tcpPacket.fin ? "1" : "0") + " - RST: " + (tcpPacket.rst ? "1" : "0") + " - Seq:" + tcpPacket.sequence + " - " + (this.isClientAddress(tcpPacket.src_ip) ? "C->S" : "S->C") + " - psh: " + (tcpPacket.psh ? "1" : "0") + " - Delay: " + (tcpPacket.d_flag ? "1" : "0") + " - Dont Fragment: " + (tcpPacket.dont_frag ? "1" : "0") + " - More Fragment: " + (tcpPacket.more_frag ? "1" : "0") + " - Realibility: " + (tcpPacket.more_frag ? "1" : "0") + " - Frag Reservation: " + (tcpPacket.rsv_frag ? "1" : "0");
    }
    
    public static void showSetActiveProtocols()
    {
        int total = ProtocolManager.getInstance().getProtocolsByPort().size();
        String[] titles = new String[total];
        String[][] choices = new String[total][];
        Protocol[][] protocols = new Protocol[total][];
        int i = 0;
        for (int port : ProtocolManager.getInstance().getProtocolsByPort().keySet())
        {
            Set<Protocol> prots = ProtocolManager.getInstance().getProtocolForPort(port);
            titles[i] = "Port "+port;
            int count = prots.size();
            int j = 0;
            choices[i] = new String[count];
            protocols[i] = new Protocol[count];
            for (Protocol prot : prots)
            {
                protocols[i][j] = prot;
                choices[i][j++] = prot.getName();
            }
            i++;
        }
        PacketSamurai.getUserInterface().log("System [Network] - Please select the active protocols for Sniffing, non active protocols are used for opening old logs.");
        int[] ret = ChoiceDialog.choiceDialog("Select Active Protocols for Sniffing", titles, choices);
        
        // u are doomed to properly set it
        if (ret != null)
        {
            Map<Integer,Protocol> activeProtocols = new FastMap<Integer, Protocol>();
            i = 0;
            for (int sel : ret)
            {
                Protocol p = protocols[i++][sel];
                activeProtocols.put(p.getPort(), p);
            }
            Captor.setActiveProtocols(activeProtocols);
            PacketSamurai.saveSnifferActiveProtocols();
        }
    }
}
