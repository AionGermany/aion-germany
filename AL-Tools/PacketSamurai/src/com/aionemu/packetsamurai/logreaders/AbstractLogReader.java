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


package com.aionemu.packetsamurai.logreaders;

import java.io.IOException;
import java.util.Set;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.ProtocolManager;
import com.aionemu.packetsamurai.session.Session;

import javolution.util.FastSet;

/**
 * 
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 *
 */
public abstract class AbstractLogReader
{
    protected Set<Session> _sessions;
    protected String _fileName;

    protected AbstractLogReader(String filename) throws IOException
    {
        _fileName = filename;
        _sessions = new FastSet<Session>();
    }
    
    public void parse() throws IOException
    {
        long time = System.currentTimeMillis();
        this.parseHeader();
        this.parsePackets(false);
        closeFile();
        if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERBOSE.ordinal())
            System.out.println("Log parsed in "+(System.currentTimeMillis() - time)+"ms");
    }

    public void reloadParse() throws IOException
    {
        long time = System.currentTimeMillis();
        this.parseHeader();
        this.parsePackets(true);
        closeFile();
        if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERBOSE.ordinal())
            System.out.println("Log parsed in "+(System.currentTimeMillis() - time)+"ms");
    }

    public Set<Session> getSessions()
    {
        return _sessions;
    }
    
    /**
     * if this is true, then you can get a partial Session just after loading the headers and getSessions will return a set containing only 1 session
     * @return
     */
    public abstract boolean supportsHeaderReading();
    
    public abstract boolean parsePackets(boolean isReload) throws IOException;
    
    public abstract boolean parseHeader() throws IOException;
    
    protected abstract void closeFile() throws IOException;

    protected abstract String getAditionalName();
    
    protected abstract String getFileExtension();
    
    public boolean hasContinuation()
    {
        return false;
    }
    
    /**
     * Attempts to retrieve the protocol by the specified port, using the following methodology:<BR>
     * <li>If there no protocol loaded for the specified port the user is prompted to choose one from all the loaded ones.</li>
     * <li>If there only one protocol loaded for the specified port its intantly returned.</li>
     * <li>If there more then one protocol loaded for the specified port the user is prompted to choose between them.</li>
     * @param port
     * @return The protocol as specified above
     */
    public static Protocol getLogProtocolByPort(int port)
    {
        Protocol p = null;
        Set<Protocol> protos = ProtocolManager.getInstance().getProtocolForPort(port);
        if (protos == null || protos.isEmpty())
        {
            p = ProtocolManager.promptUserToChoose("Choose the protocol for this log (err?).");
        }
        else if (protos.size() == 1)
        {
            p = protos.iterator().next();
        }
        else
        {
            p = ProtocolManager.promptUserToChoose(protos, "Choose the protocol for this log.");
        }
        return p;
    }
    
    public static AbstractLogReader getLogReaderForFile(String filename)
    {
        //maybe we'd better register the readers and select wth the getExtension
        AbstractLogReader reader = null;
        try
        {
            if (filename.endsWith("pcap") || filename.endsWith("cap"))
            {
                reader = new PCapReader(filename);
            }
            else if(filename.endsWith("psl")){
            	reader = new PSLReader(filename);
            }
            else
            {
                PacketSamurai.getUserInterface().log("ERROR: Failed to open file, unsupported extension.");
                return null;
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reader;
    }

	public String getFileName() {
		return this._fileName;
	}
}
