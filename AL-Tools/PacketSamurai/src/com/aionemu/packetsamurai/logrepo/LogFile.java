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


package com.aionemu.packetsamurai.logrepo;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.logreaders.AbstractLogReader;
import com.aionemu.packetsamurai.session.Session;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class LogFile
{
    private String _name;
    private long _size;
    private String _format;
    private boolean _existsLocally;
    private boolean _existsRemotely;
    private File _file;
    private Session _session;
    private boolean _headersLoaded = false;
    private boolean _fullyLoaded = false;
    private AbstractLogReader _reader;
    private String _comments;
    private long _remoteBitset;
    private String _remoteServerType;
    private String _remoteProtocolName;
    private long _remoteSessionId;
    private int _remoteId;
    private String _remoteUploader;
    private long _uploadedTime;
    
    public LogFile(File file, boolean remote)
    {
        this(file.getName(), file.length(), true, remote);
        _file = file;
    }
    
    public LogFile(String name, long size, boolean local, boolean remote)
    {
        _name = name;
        _size = size;
        _existsLocally = local;
        _existsRemotely = remote;
        _format = _name.substring(_name.lastIndexOf('.')+1);
    }
    
    public boolean ensureLocal()
    {
        if(!_existsLocally && _existsRemotely)
        {
            RemoteLogRepositoryBackend.getInstance().downLoadFile(this);
        }
        else if(!_existsRemotely && !_existsLocally)
        {
            throw new IllegalStateException("This LogFile("+_name+") doesnt exist (neither locally or remotely");
        }
        return _existsLocally;
    }
    
    public boolean ensureRemote()
    {
        if(_existsLocally && !_existsRemotely)
        {
            RemoteLogRepositoryBackend.getInstance().upLoadFile(this);
        }
        else if(!_existsRemotely && !_existsLocally)
        {
            throw new IllegalStateException("This LogFile("+_name+") doesnt exist (neither locally or remotely");
        }
        return _existsRemotely;
    }
    
    public void setFile(File file)
    {
        _file = file;
    }
    
    public void setLocal(boolean val)
    {
        if (_file == null)
            throw new IllegalStateException("In order to make a logFile local one must first associate it with an local file");
        _existsLocally = val;
    }
    
    public boolean isLocal()
    {
        return _existsLocally;
    }
    
    public void setRemote(boolean val)
    {
        _existsRemotely = val;
    }
    
    public boolean isRemote()
    {
        return _existsRemotely;
    }
    
    public File getFile()
    {
        if(!_existsLocally)
        {
            throw new IllegalStateException("This LogFile is not local can not call getFile on it");
        }
        else if(_file == null)
        {
            _file = new File(LogRepository.getInstance().getLogsDir(), _name);
        }
        return _file;
    }
    
    public void loadHeaders()
    {
        if(!this.isLocal())
            throw new IllegalStateException("You can not load the headers of a remote-only file");
        if(this.areHeadersLoaded())
            return;
        _reader = AbstractLogReader.getLogReaderForFile(_file.getAbsolutePath());
        
        if(!_reader.supportsHeaderReading())
        {
            PacketSamurai.getUserInterface().log("We cant read headers from a file that doesnt support header reading");
        }
        try
        {
            _reader.parseHeader();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        Iterator<Session> iter = _reader.getSessions().iterator();
        _session = iter.hasNext() ? iter.next() : null;
        _headersLoaded = true;
    }
    
    public void loadFully()
    {
        if(this.areHeadersLoaded())
            loadHeaders(); // this will throw the appropriate exception if the file is only remote
        try
        {
            _reader.parsePackets(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        _fullyLoaded = true;
    }
    
    public String getFormat()
    {
        return _format;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public long getSize()
    {
        return _size;
    }
    
    public Session getSession()
    {
        return _session;
    }
    
    public boolean areHeadersLoaded()
    {
        return _headersLoaded;
    }
    
    public boolean isFullyLoaded()
    {
        return _fullyLoaded;
    }
    
    public void unLoadSessionPackets()
    {
        if(!this.isFullyLoaded())
            return;
        _fullyLoaded = false;
        _session.unloadPackets();
    }

    public void checkRemote()
    {
        
    }
    
    public String getComments()
    {
        return _comments == null ? "" : _comments;
    }
    
    public long getAnalyserBitSet()
    {
        if(this.isLocal())
        {
            this.loadHeaders();
            return this.getSession().getAnalyserBitSet();
        }
        else if(this.isRemote()) // we never know...
        {
            return _remoteBitset;
        }
        return 0;
    }
    
    public String getServerType()
    {
        if (this.isLocal())
        {
            this.loadHeaders();
            return this.getSession().getServerType();
        }
        else if (this.isRemote()) // we never know...
        {
            return _remoteServerType;
        }
        return null; //ZOMG!
    }
    
    public String getProtocolName()
    {
        if(this.isLocal())
        {
            this.loadHeaders();
            return this.getSession().getProtocol().getName();
        }
        else if(this.isRemote()) // we never know...
        {
            return _remoteProtocolName;
        }
        return null; //ZOMG!
    }
    
    public void setRemoteUploader(String remoteUploader)
    {
        _remoteUploader = remoteUploader;
    }
    
    public String getRemoteUploader()
    {
        if (this.isRemote())
        {
            return _remoteUploader;
        }
        return "";
    }
    
    public void setUploadedTime(long timestamp)
    {
        _uploadedTime = timestamp;
    }
    
    public long getUploadedTime()
    {
        return _uploadedTime;
    }
    
    public long getSessionId()
    {
        if(this.isLocal())
        {
            this.loadHeaders();
            return this.getSession().getSessionId();
        }
        else if(this.isRemote()) // we never know...
        {
            return _remoteSessionId;
        }
        return 0;
    }
    
    public void setComments(String comments)
    {
        _comments = comments;
    }
    
    public void setRemoteId(int id)
    {
        _remoteId = id;
    }
    
    public int getRemoteId()
    {
        return _remoteId;
    }
    
    public void setRemoteAnalyserBits(long bits)
    {
        if(!this.isRemote())
            PacketSamurai.getUserInterface().log("We are setting a remote property on a non-remote file. Things are going  wrong...");
        _remoteBitset = bits;
    }
    
    public void setRemoteServertype(String type)
    {
        if(!this.isRemote())
            PacketSamurai.getUserInterface().log("We are setting a remote property on a non-remote file. Things are going  wrong...");
        _remoteServerType = type;
    }
    
    public void setRemoteProtocolName(String name)
    {
        if(!this.isRemote())
            PacketSamurai.getUserInterface().log("We are setting a remote property on a non-remote file. Things are going  wrong...");
        _remoteProtocolName = name;
    }
    
    public void setRemoteSessionId(long id)
    {
        if(!this.isRemote())
            PacketSamurai.getUserInterface().log("We are setting a remote property on a non-remote file. Things are going  wrong...");
        _remoteSessionId  = id;
    }
}