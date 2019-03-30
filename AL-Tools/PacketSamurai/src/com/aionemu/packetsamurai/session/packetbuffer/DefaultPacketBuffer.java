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


package com.aionemu.packetsamurai.session.packetbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.aionemu.packetsamurai.protocol.Protocol;



/**
 * @author Ulysses R. Ribeiro
 *
 */
public class DefaultPacketBuffer implements PacketBuffer
{
    public static final int BUFFER_SIZE = 65536;
    private ByteBuffer _buffer;
    
    public DefaultPacketBuffer()
    {
        _buffer = ByteBuffer.allocate(BUFFER_SIZE);
        _buffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public int nextAvaliablePacket()
    {
        //System.out.println("PacketBuffer : availablePacket : avaiableBytes: "+_availableBytes);
        //if there isnt bytes enough for a header
        if (_buffer.position() < 2)
        {
            return 0;
        }
        
        int size = (_buffer.getShort(0) & 0xFFFF);
        //System.out.println("PacketBuffer : availablePacket : size: "+size);
        
        //header but no data ? consume the header
        if (size == 2)
        {
        	System.out.println("PacketBuffer : availablePacket : consumed 2 bytes of useless header");
            // just reset the pos to get it overwritten
            _buffer.position(0);
            return 0;
        }
        // if size is greater then avaliable bytes
        else if (size > _buffer.position())
        {
            //System.out.println("PacketBuffer : availablePacket : not enough data available");
            return 0;
        }
        //System.out.println("PacketBuffer : availablePacket : available :"+size);
        return size-2;
    }
    
    public void getNextPacket(byte[] header, byte[] data)
    {
        _buffer.limit(_buffer.position());
        
        //rewind it so we can get the very first packet in buffer
        _buffer.position(0);
        
        //get the packet header
        _buffer.get(header);
        
        //Get packet data
        _buffer.get(data);
        
        //System.out.println("PacketBuffer : newtPacket : returned one packet size="+(data.length + header.length));
        //System.out.println("PacketBuffer : newtPacket : mark:"+mark);
        //compact the buffer
        _buffer.compact();
        //System.out.println("PacketBuffer : newtPacket : wants to position to :"+(mark - (data.length + header.length))+" and limit : "+_buffer.limit());
    }
    
    public void addData(byte[] data)
    {
        _buffer.put(data);
    }

    public void setProtocol(Protocol p)
    {
        // no use here
        
    }
}
