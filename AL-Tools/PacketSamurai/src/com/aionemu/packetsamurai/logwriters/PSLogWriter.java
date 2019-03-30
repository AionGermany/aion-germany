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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.Session;


/**
 * 
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 *
 */
public class PSLogWriter extends AbstractLogWriter
{
    private static final byte LOG_FORMAT_VERSION = 0x07;
    private static final long MAX_LOG_SIZE = Long.decode(PacketSamurai.getConfigProperty("MaxLogSize", ""+4*1024*1024));
    
    private int _part = 0;
    private boolean _wroteOnTheFly; // used to ensure only one method is used onthe fly or bulk writing (avoid the bulk method beeing called after having written everything on the fly)
    private int _packetsWrote;
    
    public PSLogWriter(Session session) throws IOException
    {
        super(session);
    }
    
    public PSLogWriter(Session session, boolean wroteOnTheFly) throws IOException
    {
        super(session);
        _wroteOnTheFly= wroteOnTheFly;
    }
    
    public PSLogWriter(String filename, Session session) throws IOException
    {
        super(filename, session);
    }
    
    public PSLogWriter(String filename, Session session, boolean wroteOnTheFly) throws IOException
    {
        super(filename, session);
        _wroteOnTheFly= wroteOnTheFly;
    }
    
    public PSLogWriter(String dir, String filename, Session session) throws IOException
    {
        super(dir, filename, session);
    }
    
    public PSLogWriter(String dir, String filename, Session session, boolean wroteOnTheFly) throws IOException
    {
        super(dir, filename, session);
        _wroteOnTheFly= wroteOnTheFly;
    }

    @Override
    protected void writeHeader() throws IOException
    {
        
        int bufSize = 1+4+1+2+2+4+4+2*_session.getProtocol().getName().length()+2+2+2+8+8+1;
        if(this.getSession().getComments() != null)
            bufSize += this.getSession().getComments().length()*2;
        if(this.getSession().getServerType() != null)
            bufSize += this.getSession().getServerType().length()*2;
        System.out.println("Computed Header size : "+bufSize);
        ByteBuffer buf = ByteBuffer.allocate(bufSize);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        
        buf.put(LOG_FORMAT_VERSION);
        // write dummy packet count
        buf.putInt(this.getSession().getPackets().size());
        // write dummy has Continuation (split log)
        buf.put((byte) 0x00);
        // write dummy part
        buf.putShort((short) 0x00);
        buf.putShort((short) this.getSession().getProtocol().getPort());
        if(this.getSession().getClientIp() == null)
            buf.put(new byte[4]);
        else
            buf.put(this.getSession().getClientIp().getAddress());
        if(this.getSession().getServerIp() == null)
            buf.put(new byte[4]);
        else
            buf.put(this.getSession().getServerIp().getAddress());
        writeS(this.getSession().getProtocol().getName(),buf);
        writeS(this.getSession().getComments(), buf);
        writeS(this.getSession().getServerType(), buf);
        buf.putLong(this.getSession().getAnalyserBitSet());//bitset
        buf.putLong(this.getSession().getSessionId());
        buf.put((byte) (this.getSession().isDecrypted() ? 0x00 : 0x01));
        
        this.getRandomAccessFile().write(buf.array());
    }

    @Override
    protected void writePackets() throws IOException
    {
        if(_wroteOnTheFly)
            return;
        for (DataPacket packet : this.getSession().getPackets())
        {
            this.writePacket(packet, false);
        }
    }

    @Override
    public void writePacket(DataPacket packet) throws IOException
    {
        if(!_wroteOnTheFly)
            throw new IllegalStateException("Write on the fly methods can not be called on a writer that was not iniated with this property");
        this.writePacket(packet, true);
    }
    
    public void writePacket(DataPacket packet, boolean isWriteOnTheFly) throws IOException
    {
        if(!_wroteOnTheFly && isWriteOnTheFly)
            throw new IllegalStateException("Write on the fly methods can not be called on a writer that was not iniated with this property");
        int size = packet.getRawSize();
        // uglyish
        ByteBuffer buffer = ByteBuffer.allocate(3+8+size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        // if packet came from server
        if (packet.getDirection() == packetDirection.serverPacket)
            buffer.put((byte) 0x01);
        else
            buffer.put((byte) 0x00);
        
        buffer.putShort((short) (size + 2));
        buffer.putLong(packet.getTimeStamp());
        buffer.put(packet.getIdData());
        buffer.put(packet.getData(),0,packet.getSize()); //must use the size of the data in the strcuture coz the data array might contain some useless bytes that are here because of the compact/flip that removes the id bytes
        this.getRandomAccessFile().write(buffer.array());
        
        if (isWriteOnTheFly)
        {
            long pos = this.getRandomAccessFile().getFilePointer();
            this.getRandomAccessFile().seek(1);
            // right... this is lame.. but that daln RandomAccess doesnt write with little endianess alone...
            ByteBuffer buf = ByteBuffer.allocate(4);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            buf.putInt(this.getSession().getPackets().size() - _packetsWrote);
            this.getRandomAccessFile().write(buf.array());
            this.getRandomAccessFile().seek(pos);
        }
        
        if (this.getRandomAccessFile().getFilePointer() >= PSLogWriter.MAX_LOG_SIZE)
        {
            // mark log as continued
            long pos = this.getRandomAccessFile().getFilePointer();
            this.getRandomAccessFile().seek(5);
            this.getRandomAccessFile().write(0x01);
            // write short
            this.getRandomAccessFile().write(_part & 0xFF);
            this.getRandomAccessFile().write((_part >> 8) & 0xFF);
            this.getRandomAccessFile().seek(pos);
            
            // close current file
            this.close();
            _packetsWrote = this.getSession().getPackets().size();
            _part++;
            String file = this.getDirectory()+this.getFileName()+"-"+_part+"."+this.getFileExtension();
            this.setRandomAccessFile(new RandomAccessFile(file,"rw"));
            this.writeHeader();
        }
    }
    
    @Override
    protected String getFileExtension()
    {
        return "psl";
    }
    
    private void writeS(CharSequence text, ByteBuffer buf)
    {
        if (text == null)
        {
            buf.putChar('\000');
        }
        else
        {
            final int len = text.length();
            for (int i=0; i < len; i++)
                buf.putChar(text.charAt(i));
            buf.putChar('\000');
        }
    }
    
}