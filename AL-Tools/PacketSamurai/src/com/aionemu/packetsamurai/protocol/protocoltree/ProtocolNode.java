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


package com.aionemu.packetsamurai.protocol.protocoltree;

import java.util.List;

import com.aionemu.packetsamurai.parser.PartType;

import javolution.util.FastList;

/**
 * This class represents a ProtocolNode, either a {@link PacketFormat} or {@link PacketFamilly}
 * @author Gilles Duboscq
 *
 */
public abstract class ProtocolNode
{
	private List<Integer> _ids = new FastList<Integer>();
	private List<PartType> _idParts = new FastList<PartType>();
	
	public ProtocolNode(int id)
	{
		_ids.add(id);
	}
	
	public ProtocolNode()
	{
		//
	}

	public int getID()
	{
		return _ids.get(_ids.size()-1);
	}
	
	public List<Integer> getIDs()
	{
		return _ids;
	}
	
	public List<PartType> getIdParts()
	{
		return _idParts;
	}
	
	public void addIdPartAtEnd(PartType part)
	{
		_idParts.add(part);
	}
	
	public void addIdPartsAtBegining(List<PartType> parts, List<Integer> values)
	{
		_idParts.addAll(0,parts);
		_ids.addAll(0,values);
		if(this instanceof PacketFamilly)
		{
			for(ProtocolNode n :((PacketFamilly)this).getNodes().values())
			{
				n.addIdPartsAtBegining(parts, new FastList<Integer>());
			}
		}
	}
    
    @Override
    public String toString()
    {
        return "ProtocolNode: ID: "+this.getID();
    }
}