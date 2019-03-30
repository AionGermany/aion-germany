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


package com.aionemu.packetsamurai.session;

import java.nio.BufferUnderflowException;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.DataStructure;
import com.aionemu.packetsamurai.protocol.PacketId;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFormat;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DataPacket extends DataStructure
{
	private packetDirection _direction;
    private Protocol _protocol;
    private long _timeStamp;
    private PacketFormat _packetFormat;
    private PacketId _packetID;
    private byte[] _IdData;
    private int _size;
    
    public DataPacket(int packetNumberId, byte[] data, packetDirection dir, long timeStamp, Protocol proto)
    {
        this(packetNumberId, data,dir,timeStamp,proto,true);
    }
    
    public DataPacket(int packetNumberId, byte[] data, packetDirection direction, long timeStamp, Protocol protocol, boolean parse)
    {
        super(data,null);
	    _direction = direction;
        _protocol = protocol;
        _timeStamp = timeStamp;
        _packetID = new PacketId();
        _packetFormat = _protocol.getFormat(this,_packetID);
        if (_packetFormat == null)
        {
            this.getByteBuffer().rewind();
            _packetID = null;
        }
        _size = data.length;
        _IdData = new byte[this.getByteBuffer().position()];
        System.arraycopy(data, 0, _IdData, 0, this.getByteBuffer().position());
        this.getByteBuffer().compact();
        this.getByteBuffer().flip();
        if (_packetFormat != null)
        {
            this.setFormat(_packetFormat.getDataFormat());
        }
        if (parse)
        {
            try
            {
                this.parse();
                if (this.getProtocol() != null && this.getProtocol().isStrictLength() && this.getUnparsedData().length > 0)
                {
                    _warning = "Incomplete Format";
                }
                else if (this.getFormat() == null)
                {
                    _warning = "Missing Format";
                }
            }
            catch (BufferUnderflowException e)
            {
                _error = "Insuficient data for the specified format";
                if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERBOSE.ordinal())
                {
                    if(this.getFormat() != null)
                        System.out.println(this.getFormat().toString());
                    dumpParts();
                }
                PacketSamurai.getUserInterface().log("ERROR: Parsing packet ("+this.getName()+"), insuficient data for the specified format. Please verify the format."); 
            }
        }
    }

	public Protocol getProtocol()
    {
        return _protocol;
    }
    
    public boolean fromServer()
    {
        return (_direction == packetDirection.serverPacket);
    }
    
    public long getTimeStamp()
    {
        return _timeStamp;
    }

    public packetDirection getDirection()
    {
        return _direction;
    }
    
    public PacketFormat getPacketFormat()
    {
        return _packetFormat;
    }
    
    public String getName()
    {
        if (this.getPacketFormat() == null)
        {
            return null;
        }
        return this.getPacketFormat().getName();
    }
    
    public PacketId getPacketId()
    {
        return _packetID;
    }
    
    public int getRawSize()
    {
        return _size;
    }
    
    public byte[] getIdData()
    {
        return _IdData;
    }
}