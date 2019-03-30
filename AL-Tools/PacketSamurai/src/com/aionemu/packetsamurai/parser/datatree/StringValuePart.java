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
import java.nio.ByteOrder;

import com.aionemu.packetsamurai.parser.DataStructure.DataPacketMode;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.parttypes.StringPartType.stringType;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class StringValuePart extends ValuePart
{
    protected String _string;
    protected stringType _type;
    
    public StringValuePart(DataTreeNodeContainer parent, Part part, stringType type)
    {
        super(parent, part);
        _type = type;
    }
    
    @Override
    public void parse(ByteBuffer buf)
    {
        if(this.getMode() == DataPacketMode.FORGING)
            throw new IllegalStateException("Can not parse on a Forging mode Data Packet Tree element");
        int pos = buf.position();
        int size = 0;
        switch(_type)
        {
            case s:
                StringBuffer sb = new StringBuffer();
                byte b;
                while ((b = buf.get()) != 0)
                    sb.append((char)b);
                _string = sb.toString();
                size = sb.length()+1;
                break;
            case S:
                StringBuffer sb2 = new StringBuffer();
                char ch;
                char first = buf.getChar();
                if (first == '$')
                {
                	boolean stringFallback = false;
                	while ((ch = buf.getChar()) != 0) {
                		if (ch == '$') {
                			stringFallback = true;
                		}
                		sb2.append(ch);
                	}
                	if (stringFallback) {
                		 _string = "$" + sb2.toString();
   	                	size = _string.length() * 2 + 2;
   	                	break;
                	}
                	size = sb2.length() * 2;
                	
                	buf.position(pos + 2);
                	byte[] bytes = new byte[size];
                	for (int i = 0; i < size; i++)
                		bytes[i] = buf.get();
                	
            		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            		buffer.order(ByteOrder.LITTLE_ENDIAN);
            		buffer.put(bytes);
            		int value = 0;
            		if (bytes.length == 1)
            			value = buffer.get(0);
            		else if (bytes.length == 2)
            			value = buffer.getShort(0);
            		else if (bytes.length == 4)
            			value = buffer.getInt(0);
            		
            		_string = Long.toString(value);                 	
                	size += 4;
                }
                else
                {
                	buf.position(pos);
	                while ((ch = buf.getChar()) != 0)
	                    sb2.append(ch);
	                _string = sb2.toString();
	                size = sb2.length()*2+2;
                }
                break;
            case Ss:
                StringBuffer sb3 = new StringBuffer();
				char ch1;
				while ((ch1 = buf.getChar()) != 0)
				{
					sb3.append(ch1);
				}
				_string = sb3.toString();
				size = getBSize();
				break;
            case Sn:
                StringBuffer sb4 = new StringBuffer();
				for (int i = 0; i < super.getBSize(); i = i + 2)
				{
					sb4.append(buf.getChar());
				}
				_string = sb4.toString();
				size = getBSize();          	
            	break;
        }
        // sets the raw bytes
        _bytes = new byte[size];
        buf.position(pos);
        buf.get(_bytes);
    }
    
    @Override
    public void forge(DataOutput stream) throws IOException
    {
        if(this.getMode() == DataPacketMode.PARSING)
            throw new IllegalStateException("Can not call forge on a Parsing mode Data Packet Tree element");
        switch(_type)
        {
            case S:
                if (_string == null)
                {
                    stream.writeChar('\000');
                }
                else
                {
                    final int len = _string.length();
                    for (int i=0; i < len; i++)
                        stream.writeChar(_string.charAt(i));
                    stream.writeChar('\000');
                }
                break;
            case s:
                if (_string == null)
                {
                    stream.write((byte)0x00);
                }
                else
                {
                    final int len = _string.length();
                    for (int i=0; i < len; i++)
                        stream.write((byte) _string.charAt(i));
                    stream.write((byte)0x00);
                }
                break;
           case Ss:
				for (int i = 0; i < (_string.getBytes().length /2); i++)
				{
					stream.writeChar(_string.charAt(i));
				}
				break;
			default:
				break;
        }
    }
    
    public String getStringValue()
    {
        return _string;
    }
    
    public void setStringValue(String s)
    {
        if(this.getMode() == DataPacketMode.PARSING)
            throw new IllegalStateException("Can not set value on a Parsing mode Data Packet Tree element");
        _string = s;
    }
    
    
    @Override
    public String getValueAsString()
    {
        return _string;
    }
}