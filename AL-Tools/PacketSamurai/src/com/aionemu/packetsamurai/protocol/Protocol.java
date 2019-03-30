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


package com.aionemu.packetsamurai.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.PartTypeManager;
import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.BitwiseSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.PartContainer;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;
import com.aionemu.packetsamurai.parser.formattree.SwitchPart;
import com.aionemu.packetsamurai.parser.formattree.WhilePart;
import com.aionemu.packetsamurai.parser.parttypes.RawPartType;
import com.aionemu.packetsamurai.parser.valuereader.Reader;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFormat;
import com.aionemu.packetsamurai.protocol.protocoltree.ProtocolNode;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;
import com.sun.org.apache.xerces.internal.impl.Constants;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class Protocol
{
	private PacketFamilly _serverPackets;
	private PacketFamilly _clientPackets;
    private int _port; // this can be used to specify a tcp or udp port
    private int _sizeBytes; // this is the number of bytes used to specify the size of packets (2 is here as a default value that will be changed if specified)
    private int _checksumSize = 0;
    private boolean _loaded = false;
    
    private boolean _strictLength = false;
    private boolean _analyzerEnabled = true;
    private String _encryption;
    private String _name;
    private String _filename;
    private String _packetBufferType = "DefaultPacketBuffer";
    
    private static final Comparator<ProtocolNode> PROTOCOL_NODE_COMPARATOR = new Comparator<ProtocolNode>()
    {
        public int compare(ProtocolNode o1, ProtocolNode o2)
        {
            return o1.getID() - o2.getID();
        }
    };
	
	public Protocol(String pFile)
	{
        _loaded = false;
        _filename = pFile;
		loadProtocol(pFile);
	}
    
    public Protocol(int sizeBytes, String name, int port, String encryption)
    {
        _sizeBytes = sizeBytes;
        _name = name;
        _port = port;
        _encryption = encryption;
        _clientPackets = new PacketFamilly("ClientPackets");
        _clientPackets.setDirection(packetDirection.clientPacket);
        _serverPackets = new PacketFamilly("ServerPackets");
        _serverPackets.setDirection(packetDirection.serverPacket);
    }
    
    public Protocol(int sizeBytes, String name, int port)
    {
        this(sizeBytes, name, port, "Null");
    }

	public void saveProtocol(String fileName)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
			factory.setIgnoringComments(true);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			DOMImplementationLS implLS = (DOMImplementationLS) docBuilder.getDOMImplementation();
			LSSerializer domWriter = implLS.createLSSerializer();
			LSOutput output = implLS.createLSOutput();
            domWriter.getDomConfig().setParameter(Constants.DOM_FORMAT_PRETTY_PRINT, Boolean.TRUE);
            
			Element root = doc.createElement("protocol");
            
            root.setAttribute("port", Integer.toString(_port));
            root.setAttribute("sizebytes", Integer.toString(_sizeBytes));
            root.setAttribute("crypt", _encryption);
            root.setAttribute("strictlength", Boolean.toString(_strictLength));
            root.setAttribute("name", _name);
            root.setAttribute("packetbuffer", _packetBufferType);
            root.setAttribute("checksumSize", Integer.toString(_checksumSize));
			
			if(_serverPackets != null)
			{
				Element serverPacketFamilly = doc.createElement("packetfamilly");
				serverPacketFamilly.setAttribute("way","ServerPackets");
				serverPacketFamilly.setAttribute("switchtype",_serverPackets.getSwitchType().getName());
//               sort elements in family
                FastList<ProtocolNode> nodes = new FastList<ProtocolNode>(_serverPackets.getNodes().values());
                Collections.sort(nodes, PROTOCOL_NODE_COMPARATOR);
				for(ProtocolNode node : nodes)
				{
					buildDOM(node, serverPacketFamilly, doc);
				}
				root.appendChild(serverPacketFamilly);
			}
            else
            {
                PacketSamurai.getUserInterface().log("Server Packets were not loaded, skipped while saving");
            }
			
			if(_clientPackets != null)
			{
				Element clientPacketFamilly = doc.createElement("packetfamilly");
				clientPacketFamilly.setAttribute("way","ClientPackets");
				clientPacketFamilly.setAttribute("switchtype",_clientPackets.getSwitchType().getName());
//              sort elements in family
                FastList<ProtocolNode> nodes = new FastList<ProtocolNode>(_clientPackets.getNodes().values());
                Collections.sort(nodes, PROTOCOL_NODE_COMPARATOR);
                for(ProtocolNode node : nodes)
				{
					buildDOM(node, clientPacketFamilly, doc);
				}
				root.appendChild(clientPacketFamilly);
			} else { PacketSamurai.getUserInterface().log("Client Packets were not loaded, skipped while saving"); }
			
			doc.appendChild(root);
			OutputStream outputStream = new FileOutputStream(new File(fileName));
			output.setByteStream(outputStream);
			output.setEncoding("UTF-8");
			domWriter.write(doc, output);
            outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public PacketFormat getFormat(DataPacket packet, PacketId _packetid)
	{
		if(packet.getDirection() == packetDirection.serverPacket)
		{
			if (_serverPackets == null)
				return null;
            int i = packet.readIntType(_serverPackets.getSwitchType());
            if(_checksumSize != 0)
            	packet.getByteBuffer().position(packet.getByteBuffer().position()+_checksumSize);
            _packetid.add(i,_serverPackets.getSwitchType());
			ProtocolNode node = _serverPackets.getNodes().get(i);
			while(node instanceof PacketFamilly)
			{
				PacketFamilly familly = (PacketFamilly)node;
                i = packet.readIntType(familly.getSwitchType());
                _packetid.add(i,familly.getSwitchType());
				node = familly.getNodes().get(i);
			}
			return (PacketFormat)node;
		}
        else if(packet.getDirection() == packetDirection.clientPacket)
		{
            int i = packet.readIntType(_clientPackets.getSwitchType());
            if(_checksumSize != 0)
            	packet.getByteBuffer().position(packet.getByteBuffer().position()+_checksumSize);
            _packetid.add(i,_clientPackets.getSwitchType());
			ProtocolNode node = _clientPackets.getNodes().get(i);
			while(node instanceof PacketFamilly)
			{
				PacketFamilly familly = (PacketFamilly)node;
                i = packet.readIntType(familly.getSwitchType());
                _packetid.add(i,familly.getSwitchType());
				node = familly.getNodes().get(i);
			}
			return (PacketFormat)node;
		}
		return null;
	}
	
	public PacketFamilly getServerPacketsFamilly()
	{
		return _serverPackets;
	}
	
	public PacketFamilly getClientPacketsFamilly()
	{
		return _clientPackets;
	}
	
	public int getPort()
    {
        return _port;
    }

    public int getSizeBytes()
    {
        return _sizeBytes;
    }

    public int getChecksumSize()
    {
        return _checksumSize;
    }

    public void setPort(int port)
    {
        _port = port;
    }

    public void setSizeBytes(int size)
    {
        _sizeBytes = size;
    }
    
    public void setEncryption(String enc)
    {
        _encryption = enc;
    }
    
    public String getEncryption()
    {
        return _encryption;
    }
    
    public boolean isLoaded()
    {
        return _loaded;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    public String getFileName()
    {
        return _filename;
    }

    public void saveProtocol()
    {
        if(_filename == null)
            saveProtocol(ProtocolManager.getInstance().getProtocolsDirectory()+"/protocol"+_name+".xml");
        else
            saveProtocol(_filename);
    }

    public long getMaxPacketLength()
    {
        return (long)Math.pow(256, _sizeBytes);
    }
    
    public String toString()
    {
        return "Protocol "+this.getName()+" ("+this.getPort()+")";
    }
    
    public String getPacketBufferType()
    {
        return _packetBufferType;        
    }

    public boolean isStrictLength()
    {
        return _strictLength;
    }

    public boolean isAnalyzerEnabled()
    {
        return _analyzerEnabled;
    }

	/*
    @Deprecated
    public List<DataStructure> parseFile(String fileName) throws IOException
	{
		FileInputStream fis = new FileInputStream(new File(fileName));
		List<DataStructure> list = new FastList<DataStructure>();
		
		while(fis.available() > 0)
		{
			packetDirection dir;
			if(fis.read() == 0)
			{
				dir = packetDirection.clientPacket;
			}
			else
			{
				dir = packetDirection.serverPacket;
			}
			byte[] timeByte = new byte[8];
			fis.read(timeByte,0,8);
			long time = new BigInteger(timeByte).longValue();
			int lengthHi = 0;
			int lengthLo = 0;
			lengthLo = fis.read();
			lengthHi = fis.read();
			int length = lengthHi*256 + lengthLo;
			byte[] data = new byte [length];
			fis.read(data,0,length);
			
			list.add(new DataPacket(data,dir,time, this));
			
		}
		return list;
	}
    */

	private void loadProtocol(String pFile)
	{
		
		Document doc = null;
		try
		{
			File file = new File(pFile);
			if(!file.exists())
			{
				_clientPackets = new PacketFamilly("ClientPackets");
				_clientPackets.setDirection(packetDirection.clientPacket);
				_serverPackets = new PacketFamilly("ServerPackets");
                _serverPackets.setDirection(packetDirection.serverPacket);
				PacketSamurai.getUserInterface().log("Protocol file didnt exist ("+pFile+"), creating new protocol.");
                _filename = null;
				return;
			}
			FileInputStream fis = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setValidating(false);
			factory.setIgnoringComments(false);
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            docBuilder.setErrorHandler(new XMLErrorHandler());
            try
            {
                doc = docBuilder.parse(fis);
            }
            catch (SAXParseException e)
            {
                Protocol.report("ERROR", e);
                return;
            }
			Node root = doc.getDocumentElement();
			if(!root.getNodeName().equals("protocol"))
			{
				PacketSamurai.getUserInterface().log("Error malformed protocol : root node should be called 'protocol'.");
				return;
			}
			
            NamedNodeMap attr = root.getAttributes();
            Node node = attr.getNamedItem("sizebytes");
            if(node != null)
            {
                try
                {
                    _sizeBytes = Integer.parseInt(node.getNodeValue());
                }
                catch(NumberFormatException nfe)
                {
                    PacketSamurai.getUserInterface().log("Warning: The sizebyte attribute of protocol must be an integer. Couldnt read the attribute provided, falling back to the default value");
                    _sizeBytes = 2 ;
                }
            }
            else
            {
                _sizeBytes = 2 ;
            }
            
            node = attr.getNamedItem("port");
            if(node != null)
            {
                try
                {
                    _port = Integer.parseInt(node.getNodeValue());
                }
                catch(NumberFormatException nfe)
                {
                    PacketSamurai.getUserInterface().log("Warning: The port attribute of protocol must be an integer. Couldnt read the attribute provided");
                }
            }
            
            node = attr.getNamedItem("crypt");
            if(node != null)
            {
                _encryption = node.getNodeValue();
            }
            else
            {
                _encryption = "Null";
            }
            
            node = attr.getNamedItem("strictlength");
            if(node != null)
            {
                _strictLength = Boolean.parseBoolean(node.getNodeValue());
            }
            
            node = attr.getNamedItem("analyzerEnabled");
            if(node != null)
            {
                _analyzerEnabled = Boolean.parseBoolean(node.getNodeValue());
            }
            
            node = attr.getNamedItem("packetbuffer");
            if(node != null)
            {
                _packetBufferType = node.getNodeValue();
            }
            
            node = attr.getNamedItem("checksumSize");
            if(node != null)
            {
                _checksumSize = Integer.parseInt(node.getNodeValue());
            }

            node = attr.getNamedItem("name");
            if(node != null)
            {
                _name = node.getNodeValue();
            }
            else
            {
                PacketSamurai.getUserInterface().log("* Protocol [Error] - while loading "+pFile+" : protocol has no 'name' attribute");
                return;
            }

			
			for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling())
	        {
	            if ("packetfamilly".equalsIgnoreCase(n.getNodeName()))
	            {
	            	PacketFamilly familly = parseFamilly(n, true);
	            	if(familly != null)
	            	{
	            		switch(familly.getDirection())
	            		{
	            		case clientPacket:
	            			_clientPackets = familly;
	            			//set a default name if necessary
	            			if(_clientPackets.getName() == null)
	            				_clientPackets.setName("ClientPackets");
	            			//Main.getInstance().log("Client Familly Ok");
	            			break;
	            		case serverPacket:
	            			_serverPackets = familly;
	            			// set a default name if necessary
	            			if(_serverPackets.getName() == null)
	            				_serverPackets.setName("ServerPackets");
	            			//Main.getInstance().log("Server Familly Ok");
	            			break;
	            		default:
	            			PacketSamurai.getUserInterface().log("* Protocol [Error] - Packetfamilly doesnt have a valid way");
	            		}
	            	}
	            	else
	            	{
	            		PacketSamurai.getUserInterface().log("* Protocol [Error] - Packetfamilly returned is null, there was an error");
	            	}
	            }
	        }
            _loaded = true;
		}
		catch (Exception e)
		{
			PacketSamurai.getUserInterface().log("* Protocol [Error] - while parsing protocol "+pFile);
			e.printStackTrace();
		}
	}

	private PacketFamilly parseFamilly(Node n, boolean isRoot)
	{
		PacketFamilly newFamilly;
		NamedNodeMap attrs = n.getAttributes();
		Node atr;
		if(isRoot)
		{
			newFamilly =  new PacketFamilly();
		}
		else
		{
			atr = attrs.getNamedItem("id");
			if(atr == null)
			{
				PacketSamurai.getUserInterface().log("* Protocol [Error] - Packetfamilly is not root and has no id. skipping it:");
				return null;
			}
			newFamilly =  new PacketFamilly(Integer.decode(atr.getNodeValue()));
		}
		//-------
		atr = attrs.getNamedItem("way");
		if(atr == null)
		{
			if(isRoot)
			{
				PacketSamurai.getUserInterface().log("* Protocol [Error] - Root Packetfamilly doesnt have 'way'. skipping it");
				return null;
			}
		}
		else
		{
			String way = atr.getNodeValue();
			if(way.equals("ServerPackets"))
			{
				newFamilly.setDirection(packetDirection.serverPacket);
			}
			else if(way.equals("ClientPackets"))
			{
				newFamilly.setDirection(packetDirection.clientPacket);
			}
		}
		//--------
		atr = attrs.getNamedItem("switchtype");
		if(atr == null)
		{
			PacketSamurai.getUserInterface().log("* Protocol [Error] - Packetfamilly doesnt have 'switchtype'. skipping it");
			return null;
		}
		PartType idPart = PartTypeManager.getInstance().getType(atr.getNodeValue());
		newFamilly.setSwitchType(idPart);
		newFamilly.addIdPartAtEnd(idPart);
		
		atr = attrs.getNamedItem("name");
		if(atr != null)
		{
			newFamilly.setName(atr.getNodeValue());
		}
		
		for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling())
	    {
			if ("packet".equalsIgnoreCase(o.getNodeName()))
	        {
				PacketFormat format = parseFormat(o);
	        	if(format != null)
	        	{
	        		format.addIdPartsAtBegining(newFamilly.getIdParts(),newFamilly.getIDs());
	        		newFamilly.addNode(format);
	        	}
	        }
			else if ("packetfamilly".equalsIgnoreCase(o.getNodeName()))
	        {
				PacketFamilly familly = parseFamilly(o, false);
	        	if(familly != null)
	        	{
	        		familly.addIdPartsAtBegining(newFamilly.getIdParts(),newFamilly.getIDs());
	        		newFamilly.addNode(familly);
	        	}
	        }
	    }
		
		return newFamilly;
	}

	private PacketFormat parseFormat(Node n)
	{		
		PacketFormat newFormat;
		NamedNodeMap attrs = n.getAttributes();
		Node atr;
		
		atr = attrs.getNamedItem("id");
		if(atr == null)
		{
			PacketSamurai.getUserInterface().log("* Protocol [Error] - Packet doesnt have 'id'. skipping it");
			return null;
		}
		int id = Integer.decode(atr.getNodeValue());
		
		String name;
		atr = attrs.getNamedItem("name");
		if(atr == null)
		{
			PacketSamurai.getUserInterface().log("* Protocol [Warning] - Packet doesnt have 'name'");
			name = "";
		}
		else
		{
			name = atr.getNodeValue();
		}
		
		newFormat = new PacketFormat(id, name);
        newFormat.setContainingProtocol(this);
		
        boolean parseRet = false;
        try
        {
            parseRet = parseParts(n, newFormat.getDataFormat().getMainBlock());
        }
        catch (IllegalStateException e)
        {
             PacketSamurai.getUserInterface().log("* Protocol [Error] - parsing format: "+newFormat.toString()+" - Details:  "+e.getMessage());
        }
        
		if (!parseRet)
		{
			return null;
		}
		
		return newFormat;
	}
    
    private boolean parseParts(Node n, PartContainer pc)
    {
        
    	NamedNodeMap attrs;
        Node atr;
        for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling())
        {
            if ("part".equalsIgnoreCase(o.getNodeName()))
            {
                Part pp = parsePart(o, pc);
                if(pp == null)
                    return false;
                pc.addPart(pp);
            }
            else if ("for".equalsIgnoreCase(o.getNodeName()) || "while".equalsIgnoreCase(o.getNodeName()))
            {
                attrs = o.getAttributes();
                int forId;
                atr = attrs.getNamedItem("id");
                if(atr == null)
                {
                    PacketSamurai.getUserInterface().log("* Protocol [Error] - for doesnt have 'id'. skipping packet");
                    return false;
                }
                forId = Integer.parseInt(atr.getNodeValue());
                
                atr = attrs.getNamedItem("analyzername");
                String analyzerName = "";
                if (atr != null)
                {
                    analyzerName = atr.getNodeValue();
                }
                int size = 0;
				if (attrs.getNamedItem("size") != null) {
					size = Integer.parseInt(attrs.getNamedItem("size").getNodeValue());
				}
                ForPart newForPart;
                if("for".equalsIgnoreCase(o.getNodeName()))
                	newForPart = new ForPart(forId, size, analyzerName);
                else
                	newForPart = new WhilePart(forId, analyzerName);

                newForPart.setParentContainer(pc);
                newForPart.setContainingFormat(pc.getContainingFormat());
                if(parseParts(o, newForPart.getModelBlock()))
                {
                    pc.addPart(newForPart);
                }
                else
                {
                    return false;
                }
            }
            else if("switch".equalsIgnoreCase(o.getNodeName()) || "bitwise_switch".equalsIgnoreCase(o.getNodeName()))
            {
                attrs = o.getAttributes();
                int switchId;
                atr = attrs.getNamedItem("id");
                if(atr == null)
                {
                    PacketSamurai.getUserInterface().log("* Protocol [Error] - (switch) doesnt have 'id'. skipping packet");
                    return false;
                }
                switchId = Integer.parseInt(atr.getNodeValue());
                
                atr = attrs.getNamedItem("analyzername");
                String analyzerName = "";
                if (atr != null)
                {
                    analyzerName = atr.getNodeValue();
                }
                
                AbstractSwitchPart newSwitchBlock = null;
                if("switch".equalsIgnoreCase(o.getNodeName()))
                	newSwitchBlock = new SwitchPart(switchId, analyzerName);
                else
                	newSwitchBlock = new BitwiseSwitchPart(switchId, analyzerName);

                newSwitchBlock.setParentContainer(pc);
                newSwitchBlock.setContainingFormat(pc.getContainingFormat());
                for(Node caseNode = o.getFirstChild(); caseNode != null; caseNode = caseNode.getNextSibling())
                {
                    if("case".equalsIgnoreCase(caseNode.getNodeName()))
                    {
                        attrs = caseNode.getAttributes();

                        String comment = null;
                        atr = attrs.getNamedItem("comment");
                        if(atr != null)
                        {
                            comment = atr.getNodeValue();
                        }
                        
                        atr = attrs.getNamedItem("analyzername");
                        analyzerName = "";
                        if (atr != null)
                        {
                            analyzerName = atr.getNodeValue();
                        }
                        
                        atr = attrs.getNamedItem("id");
                        if(atr == null)
                        {
                            PacketSamurai.getUserInterface().log("* Protocol [Error] - (case) doesnt have 'id'. skipping packet");
                            return false;
                        }
                        SwitchCaseBlock newSwitchCase = null;
                        if(atr.getNodeValue().equalsIgnoreCase("default"))
                        {
                            newSwitchCase = new SwitchCaseBlock(newSwitchBlock, analyzerName);
                        }
                        else
                        {
                            int caseId;
                            try
                            {
                                caseId = Integer.decode(atr.getNodeValue());
                                newSwitchCase = new SwitchCaseBlock(newSwitchBlock, caseId, analyzerName);
                            }
                            catch (NumberFormatException e)
                            {
                                PacketSamurai.getUserInterface().log("* Protocol [Warning] - case doesnt have a valid 'id'. making it default");
                                newSwitchCase = new SwitchCaseBlock(newSwitchBlock, analyzerName);
                            }
                        }
                        if(comment != null)
                            newSwitchCase.setComment(comment);
                        newSwitchCase.setParentContainer(pc);
                        newSwitchCase.setContainingFormat(pc.getContainingFormat());
                        if(parseParts(caseNode, newSwitchCase))
                        {
                            newSwitchBlock.addCase(newSwitchCase);
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
                pc.addPart(newSwitchBlock);
            }
        }
        return true;
    }
    
    private Part parsePart(Node n, PartContainer pc)
    {
        String partName;
        NamedNodeMap attrs = n.getAttributes();
        Node atr = attrs.getNamedItem("name");
        if(atr == null)
        {
            PacketSamurai.getUserInterface().log("* Protocol [Warning] - Part doesnt have 'name'");
            partName = "";
        }
        else
        {
            partName = atr.getNodeValue();
        }
        
        int partId;
        atr = attrs.getNamedItem("id");
        if(atr == null)
        {
            partId = -1;
        }
        else
        {
            try
            {
                partId = Integer.parseInt(atr.getNodeValue());
            }
            catch(NumberFormatException nfe)
            {
                PacketSamurai.getUserInterface().log("* Protocol [Warning] - Parts id must be an integer");
                partId = -1;
            }
        }
        
        atr = attrs.getNamedItem("type");
        if(atr == null)
        {
            PacketSamurai.getUserInterface().log("* Protocol [Error] - Part doesnt have 'type'. skipping packet");
            return null;
        }
        String type = atr.getNodeValue();
        
        int size = 0;
        int sizeid = -1;
        boolean dynBSize = false;
        atr = attrs.getNamedItem("size");
        if(atr != null)
        {
            size = Integer.decode(atr.getNodeValue());
        }
        else
        {
            atr = attrs.getNamedItem("sizeid");
            if(atr != null)
            {
                sizeid = Integer.parseInt(atr.getNodeValue());
                dynBSize = true;
            }
        }
        Reader r = null;
        String partComment = null;
        for (Node subNode = n.getFirstChild(); subNode != null; subNode = subNode.getNextSibling())
        {
            if ("comment".equals(subNode.getNodeName()))
            {
                if(partComment != null)
                {
                    PacketSamurai.getUserInterface().log("* Protocol [Warning] - Part '"+(partName)+"' has mutiple comments");
                }
                partComment = subNode.getTextContent();
            }
            
            if ("reader".equals(subNode.getNodeName()))
            {
                if(r != null)
                    PacketSamurai.getUserInterface().log("* Protocol [Warning] - Part '"+(partName)+"' has mutiple readers");
                NamedNodeMap attrs2 = subNode.getAttributes();
                atr = attrs2.getNamedItem("type");
                if(atr == null)
                {
                    PacketSamurai.getUserInterface().log("* Protocol [Warning] - Part '"+(partName)+"' has a reader with no type");
                    continue;
                }
                
                //try default package
                Class<?> clazz = null;
                try
                {
                    clazz = Class.forName("com.aionemu.packetsamurai.parser.valuereader."+atr.getNodeValue()+"Reader");
                }
                catch (DOMException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                }
                
                if (clazz == null)
                {
                    PacketSamurai.getUserInterface().log("* Protocol [Warning] - Part '"+(partName)+"' reader's could not be found in either parser or custom packages");
                    continue;
                }
                try
                {
                    r = (Reader) clazz.newInstance();
                    if(!r.loadReaderFromXML(subNode)) //drop reader if loading went wrong
                        r = null;
                    
                }
                catch (InstantiationException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if(partComment == null)
            partComment = "";
        
        String partLookUpType;
        atr = attrs.getNamedItem("lookuptype");
        if(atr == null)
        {
            partLookUpType = "";
        }
        else
        {
            partLookUpType = atr.getNodeValue();
        }
        
        String partAnalyzerName;
        atr = attrs.getNamedItem("analyzername");
        if (atr == null)
        {
            partAnalyzerName = "";
        }
        else
        {
            partAnalyzerName = atr.getNodeValue();
        }
        
        Part pp = new Part(PartTypeManager.getInstance().getType(type), partId, partName, partComment, partLookUpType, partAnalyzerName);
        if (dynBSize)
        {
            pp.setBSizeId(sizeid);
            pp.setDynamicBSize(true);
        }
        else
        {
            pp.setBSize(size);
            pp.setDynamicBSize(false);
        }
        pp.setReader(r);
        return pp;
    }

	private void processParts(List<Part> parts, Element newNode, Document document)
	{
		for(Part part : parts)
		{
			Element partNode;
			if(part instanceof ForPart)
			{
				ForPart block = (ForPart)part;
				partNode = document.createElement("for");
				partNode.setAttribute("id",String.valueOf(block.getForId()));
				processParts(block.getModelBlock().getParts(), partNode, document);
			}
			else if(part instanceof AbstractSwitchPart)
			{
				AbstractSwitchPart block = (AbstractSwitchPart) part;
				
				if(block instanceof SwitchPart)
					partNode = document.createElement("switch");
				else
					partNode = document.createElement("bitwise_switch");

				partNode.setAttribute("id",String.valueOf(block.getSwitchId()));
				for(SwitchCaseBlock sCase : block.getCases(true))
				{
					Element caseNode = document.createElement("case");
					if(sCase.isDefault())
						caseNode.setAttribute("id", "default");
					else
						caseNode.setAttribute("id", Integer.toString(sCase.getSwitchCase()));
					processParts(sCase.getParts(), caseNode, document);
					partNode.appendChild(caseNode);
				}
			}
			else
			{
				partNode = document.createElement("part");
				partNode.setAttribute("type",part.getType().getName());
				if(part.getId() != -1)
					partNode.setAttribute("id",String.valueOf(part.getId()));
				if(part.getName() != null && part.getName().length() > 0)
					partNode.setAttribute("name",part.getName());
				if(part.getComment() != null && part.getComment().length() > 0)
					partNode.setAttribute("comment",part.getComment());
				if(part.getAnalyzerName() != null && part.getAnalyzerName().length() > 0)
					partNode.setAttribute("analyzername", part.getLookUpType());
				if(part.getType() instanceof RawPartType)
				{
					if(part.isDynamicBSize())
						partNode.setAttribute("sizeid", String.valueOf(part.getBSizeId()));
					else if(part.getBSize() != 0)
						partNode.setAttribute("size", String.valueOf(part.getBSize()));
				}
                Reader reader = part.getReader();
                if(reader !=null)
                {
                    Element readerNode = document.createElement("reader");
                    readerNode.setAttribute("type", Util.getReaderName(reader.getClass()) );
                    reader.saveReaderToXML(readerNode, document);
                    partNode.appendChild(readerNode);
                }
			}
			newNode.appendChild(partNode);
		}		
	}

	private void buildDOM(ProtocolNode node, Element packetFamilly, Document document)
	{
		if(node instanceof PacketFormat)
		{
			PacketFormat format = (PacketFormat)node;
			Element newNode = document.createElement("packet");
			newNode.setAttribute("id","0x"+Integer.toHexString(format.getID()));
			newNode.setAttribute("name",format.getName());
			processParts(format.getDataFormat().getMainBlock().getParts(), newNode, document);
			packetFamilly.appendChild(newNode);
		}
		else if(node instanceof PacketFamilly)
		{
			PacketFamilly familly = (PacketFamilly)node;
			Element newNode = document.createElement("packetfamilly");
			newNode.setAttribute("switchtype",familly.getSwitchType().getName());
			newNode.setAttribute("id","0x"+Integer.toHexString(familly.getID()));
            // sort elements in family
            FastList<ProtocolNode> nodes = new FastList<ProtocolNode>(familly.getNodes().values());
            Collections.sort(nodes, PROTOCOL_NODE_COMPARATOR);
			for(ProtocolNode pnode : nodes)
			{
				buildDOM(pnode, newNode, document);
			}
			packetFamilly.appendChild(newNode);
		}
	}
    
    public static void report(String severity, SAXParseException e)
    {
        PacketSamurai.getUserInterface().log(severity+": "+e.getMessage()+" (Line "+e.getLineNumber()+", Column: "+e.getColumnNumber()+")");
    }

	class XMLErrorHandler implements ErrorHandler
    {

        public void error(SAXParseException e) throws SAXException
        {
            Protocol.report("SAXParseException [Error]: ", e);
        }

        public void fatalError(SAXParseException e) throws SAXException
        {
            Protocol.report("SAXParseException [Fatal]: ", e);
            
        }

        public void warning(SAXParseException e) throws SAXException
        {
            Protocol.report("SAXParseException[Warning]: ", e);
        }
    }
}