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


package com.aionemu.packetsamurai.parser.datatree;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JComponent;
import javax.swing.JLabel;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.DataStructure.DataPacketMode;
import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.valuereader.Reader;


/**
 * This class represent a ValuePart (c, h, d...) used to parse data from a raw packet, thus it may contain the data parsed from the packet.
 * 
 * @author Gilles Duboscq
 */
public class ValuePart extends DataTreeNode
{
    protected byte[] _bytes;
    private int _bxSize = -1;

    public ValuePart(DataTreeNodeContainer parent, Part part)
    {
        super(parent,part);
        if(part instanceof ForPart || part instanceof AbstractSwitchPart)
        {
            throw new IllegalArgumentException("The model of a value part must be a basic part not a for/switch or any other container");
        }
    }

    public void parse(ByteBuffer buf)
    {
        if(this.getMode() == DataPacketMode.FORGING)
            throw new IllegalStateException("Can not parse on a Forging mode Data Packet Tree element");
        int pos = buf.position();
        int size = 0;
        size = this.getBSize();
        @SuppressWarnings("unused")
        long maxSize = buf.remaining();
        if (size > 65536)
        {
            if (PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.NORMAL.ordinal())
            {
                System.out.println("ZOMG a huge size!! :"+size+"! defaulting to 1");
            }
            size = 1;
        }
        // sets the raw bytes
        _bytes = new byte[size];
        buf.position(pos);
        buf.get(_bytes);
    }
    
    public void forge(DataOutput stream) throws IOException
    {
        if(this.getMode() == DataPacketMode.PARSING)
            throw new IllegalStateException("Can not call forge on a Parsing mode Data Packet Tree element");
        try
        {
            stream.write(_bytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public PartType getType()
    {
        return this.getModelPart().getType();
    }

    public String getHexDump()
    {
        return Util.hexDump(_bytes);
    }

    public byte[] getBytes()
    {
        if(!this.getType().isReadableType())
            throw new IllegalStateException("Trying to get bytes from a "+this.getType().getName());
        return _bytes;
    }
    
    public int getBytesSize()
    {
        if (_bytes == null)
        {
            return 0;
        }
        return _bytes.length;
    }

    public void setBSize(int size)
    {
        if(size < 0)
        {
            if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.NORMAL.ordinal())
                System.out.println("ZOMG a negative size!! defaulting to 0");
            _bxSize = 0;
        }
        else
        {
            _bxSize = size;
        }
    }

    public int getBSize()
    {
        if (_bxSize < 0)
        {
            if(getModelPart().isDynamicBSize())
            {
                ValuePart vp = this.getParentContainer().getPacketValuePartById(getModelPart().getBSizeId());
                if (vp == null || !(vp instanceof IntValuePart))
                    PacketSamurai.getUserInterface().log("ValuePart: Invalid SizeId for bx: part can not be found or is not an integer");
                else
                    this.setBSize(((IntValuePart)vp).getIntValue());
            }
            else
            {
                this.setBSize(getModelPart().getBSize());
            }
        }
        return _bxSize;
    }

    public String getValueAsString()
    {
        return "";
    }


    /**
     * @param bytes The bytes to set.
     */
    public void setBytes(byte[] bytes)
    {
        if(this.getMode() == DataPacketMode.PARSING)
            throw new IllegalStateException("Can not set value on a Parsing mode Data Packet Tree element");
        _bytes = bytes;
    }
    
    public String readValue()
    {
        Reader r = this.getModelPart().getReader();
        if(r != null)
        {
            return r.read(this);
        }
        return this.getValueAsString();
    }
    
    public JComponent readValueToComponent()
    {
        Reader r = this.getModelPart().getReader();
        if(r != null)
        {
            return r.readToComponent(this);
        }
        return new JLabel(this.getValueAsString());
    }
    
    @Override
    public String treeString()
    {
        return this.getModelPart().getName();
    }
}
