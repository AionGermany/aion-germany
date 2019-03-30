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


package com.aionemu.packetsamurai.logrepo.communication;

import java.util.List;

import javolution.util.FastList;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class Request
{
    private String _type;
    private int _id;
    private List<RequestPart> _parts = new FastList<RequestPart>();
    
    protected Request(int id, String type)
    {
        _id = id;
        _type = type;
    }
    
    public int getId()
    {
        return _id;
    }
    
    public String getType()
    {
        return _type;
    }
    
    public void setType(String type)
    {
        _type = type;
    }
    
    public List<RequestPart> getParts()
    {
        return _parts;
    }
    
    public boolean addPart(RequestPart part)
    {
        return _parts.add(part);
    }
    
    public boolean addPart(String name, String value)
    {
        return this.addPart(new RequestPart(name, value));
    }
    
    public boolean removePart(RequestPart part)
    {
        return _parts.remove(part);
    }
}