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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.PartType;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class PacketId
{
    public static class IdPart
    {
        private PartType _type;
        private long _value;
        
        public IdPart(PartType type, long value)
        {
            _type = type;
            _value = value;
        }
        
        public long getValue()
        {
            return _value;
        }
        
        public PartType getType()
        {
            return _type;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(!(obj instanceof IdPart))
                return false;
            IdPart other = (IdPart)obj;
            return (this.getType() == other.getType() && this.getValue() == other.getValue());
        }
    }
    
    private List<IdPart> _parts;
    private String _toStr;
    
    public PacketId()
    {
        _parts = new ArrayList<IdPart>(2);
    }
    
    public PacketId(PartType[] types, long[] values)
    {
        if(types.length != types.length)
            throw new IllegalArgumentException("Invalid arrays, both must be the same length");
        _parts = new ArrayList<IdPart>(types.length);
        int i = 0;
        for(long val : values)
        {
            this.add(val,types[i]);
            i++;
        }
    }
    
    public void add(long value, PartType type)
    {
        _parts.add(new IdPart(type, value));
        _toStr = null;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(!(obj instanceof PacketId))
            return false;
        PacketId other = (PacketId)obj;
        if(this.size() !=  other.size())
            return false;
        int size = this.size();
        for(int i = 0; i < size; i++)
        {
            IdPart id1 = this.getParts().get(i);
            if(id1 == null || !id1.equals(other.getParts().get(i)))
                return false;
        }
        return true;
    }
    
    public int size()
    {
        return _parts.size();
    }
    
    public List<IdPart> getParts()
    {
        return _parts;
    }
    
    @Override
    public String toString()
    {
        if(_toStr == null)
        {
            StringBuilder sb = new StringBuilder();
            Iterator<IdPart> it = this.getParts().iterator();
            for(IdPart part = it.next(); it.hasNext(); part = it.next())
            {
                if(part == null)
                    break;
                String str = Long.toHexString(part.getValue());
                sb.append(Util.zeropad(str, str.length() + str.length()%2));
                if(it.hasNext())
                    sb.append(":");
            }
            _toStr = sb.toString().toUpperCase();
        }
        return _toStr;
    }
}