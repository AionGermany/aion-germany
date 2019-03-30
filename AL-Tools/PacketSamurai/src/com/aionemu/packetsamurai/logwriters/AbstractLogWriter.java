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


package com.aionemu.packetsamurai.logwriters;

import java.io.IOException;
import java.io.RandomAccessFile;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.Session;


/**
 * 
 * @author Gilles Duboscq
 * @author Ulysses R. Ribeiro
 */
public abstract class AbstractLogWriter
{
    private String _directory;
    private String _fileName;
    protected Session _session;
    private RandomAccessFile _raFile;
    
    public AbstractLogWriter(Session session) throws IOException
    {
        this(session.getSessionName(),session);
    }
    
    public AbstractLogWriter(String filename, Session session) throws IOException
    {
        this(PacketSamurai.getConfigProperty("lastLogDir",".\\logs"), filename, session);
    }
    
    public AbstractLogWriter(String dir, String filename, Session session) throws IOException
    {
        setDirectory(dir);
        setFileName(filename);
        _session = session;
        if(!getDirectory().endsWith("/") && !getDirectory().endsWith("\\") )
        {
            setDirectory(getDirectory()+"/");
        }
        _raFile = new RandomAccessFile(getDirectory()+getFileName()+"."+getFileExtension(),"rw");
        writeHeader();
    }
    
    public void writeLog() throws IOException
    {
        writePackets();
        this.close();
    }
    
    public void close() throws IOException
    {
        _raFile.close();
    }
    
    protected void setRandomAccessFile(RandomAccessFile raFile)
    {
        _raFile = raFile;
    }
    
    public RandomAccessFile getRandomAccessFile()
    {
        return _raFile;
    }
    
    protected Session getSession()
    {
        return _session;
    }
    
    protected abstract String getFileExtension();

    protected abstract void writeHeader() throws IOException;
    
    protected abstract void writePackets() throws IOException;

    protected abstract void writePacket(DataPacket packet) throws IOException;

    /**
     * @param fileName the fileName to set
     */
    protected void setFileName(String fileName)
    {
        _fileName = fileName;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return _fileName;
    }

    /**
     * @param directory the directory to set
     */
    protected void setDirectory(String directory)
    {
        _directory = directory;
    }

    /**
     * @return the directory
     */
    public String getDirectory()
    {
        return _directory;
    }
}