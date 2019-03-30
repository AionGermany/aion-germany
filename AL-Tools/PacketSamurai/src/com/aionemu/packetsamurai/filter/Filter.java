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


package com.aionemu.packetsamurai.filter;

import java.util.List;

import com.aionemu.packetsamurai.parser.DataStructure;

import javolution.util.FastList;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class Filter
{
    private Condition _cond;
    
    
    public Filter(String str)
    {
        _cond = new Condition(str);
    }
    
    public Condition getFilterCondition()
    {
        return _cond;
    }
    
    public List<DataStructure> filterPacketList(List<DataStructure> packets)
    {
        List<DataStructure> list = new FastList<DataStructure>();
        // :)
        for(DataStructure dp : packets)
            if(this.matches(dp))
                list.add(dp);
        // i dont like when there's no { } tho, this one is beautiful :)
        return list;
    }
    
    public boolean matches(DataStructure dp)
    {
        return this.getFilterCondition().evaluate(dp);
    }
}