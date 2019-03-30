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
package com.aionemu.packetsamurai.logreaders;

import java.io.IOException;


import com.aionemu.packetsamurai.FileCaptor;
import com.aionemu.packetsamurai.session.Session;


/**
 * @author Ulysses R. Ribeiro
 *
 */
public class PCapReader extends AbstractLogReader
{
    private FileCaptor _fCaptor;
    
    public PCapReader(String filename) throws IOException
    {
        super(filename);
        _fCaptor = new FileCaptor(filename);
    }
    
    @Override
    protected String getAditionalName()
    {
        return "pcap";
    }

    @Override
    public boolean parseHeader()
    {
        return true;
        // reading those logs is delegated to the Captor
        
    }

    @Override
    public boolean parsePackets(boolean isReload) throws IOException
    {
//      reading those logs is delegated to the Captor
        _fCaptor.proccesCaptureFile();
        for (Session s : _fCaptor.getFileTCPSessions())
        {
            _sessions.add(s);
          
            s.saveSession(); //XXX Temp Hack this should be triggered by some button in the UI or something
        }
        return false;
    }

    @Override
    protected void closeFile() throws IOException
    {
        // FileCaptor will take care of that
    }

    @Override
    protected String getFileExtension()
    {
        return "pcap";
    }

    @Override
    public boolean supportsHeaderReading()
    {
        return false;
    }

}
