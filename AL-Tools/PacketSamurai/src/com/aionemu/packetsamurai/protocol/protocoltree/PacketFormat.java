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


import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.PartTypeManager;
import com.aionemu.packetsamurai.parser.formattree.Format;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.protocol.Protocol;

import javolution.text.TextBuilder;
import javolution.util.FastList;

/**
 * This class represents a packet format in the protocol definition tree.
 * It is a container for {@link PacketParts PacketParts}
 * @author Gilles Duboscq
 * TODO: find a way to ensure part IDs unicity
 */
public class PacketFormat extends ProtocolNode
{
    private Format _format;
	private String _name;
	private String _opStr;
	private String _partStr;
    private Protocol _containingProtocol;
	
	public PacketFormat(int id, String name)
	{
		super(id);
		_name = name;
        _format = new Format(this);
	}
	
	public PacketFormat()
	{
        _format = new Format(this);
	}
    
    public Format getDataFormat()
    {
        return _format;
    }
    
    public void setContainingProtocol(Protocol p)
    {
        _containingProtocol = p;
    }
    
    public Protocol getContainingProtocol()
    {
        return _containingProtocol;
    }
	
	public String getName()
	{
		return _name;
	}

	public String getOpcodeStr()
	{
		if(_opStr == null)
		{
			TextBuilder tb = new TextBuilder();
			boolean first = true;
			for(int id : super.getIDs())
			{
				if(!first)
				{
					tb.append(":");
				}
				tb.append(Util.zeropad(Integer.toHexString(id), 2).toUpperCase());
				first = false;
			}
			_opStr = tb.toString();
		}
		return _opStr;
	}
	
	public String getPartsStr()
	{
		if (_partStr == null)
		{
			_partStr = "";
			TextBuilder tb = new TextBuilder();
			tb.append("(");
			tb.append(Util.makeFormatStringTypes(super.getIdParts()));
			tb.append(") ");
			tb.append(Util.makeFormatStringParts(this.getDataFormat().getMainBlock().getParts()));
			_partStr = tb.toString();
		}
		return _partStr;
	}

	public String toString()
	{
		return getOpcodeStr()+" "+_name;
	}
	
	/**
	 * format : (c) chdfQSsbx[ddd]
	 *           ^id        ^for
	 * id is mandatory, spaces are allowed
	 * @param str
	 * @param ids
	 * @return
	 */
	public static PacketFormat generateFromString(String str, List<Integer> ids)
	{
		PacketFormat pf = new PacketFormat();
		str = str.trim();
		int i = 0;
		if(str.charAt(i) == '(')
		{
			List<PartType> idparts = new FastList<PartType>();
			while(str.charAt(i) != ')')
			{
				if(str.charAt(i) == ' ')
				{
					i++;
					if(i >= str.length())
						return null;
				}
				PartType type = PartTypeManager.getInstance().getType(str.substring(i, i+1));
				if(type != null)
					idparts.add(type);
				else
					return null;
				i++;
				if(i >= str.length())
					return null;
			}
			pf.addIdPartsAtBegining(idparts, ids);
		}
		else
			return null;
		while(i < str.length())
		{
			if(str.charAt(i) != '[')
			{
				i++;
				for(Part part : parseForString(str.substring(i, str.substring(i).indexOf(']'))))
				{
					pf.getDataFormat().getMainBlock().addPart(part);
					while(str.charAt(i) == ' ')
					{
						i++;
						if(i >= str.length())
							return null;
					}
					i++;
					if(i >= str.length())
						return null;
				}
			}
			else if(str.charAt(i) == ' ')
			{
				i++;
				if(i >= str.length())
					return null;
			}
			else
			{
			    PartType type = PartTypeManager.getInstance().getType(str.substring(i, i+1));
				if(type != null)
					pf.getDataFormat().getMainBlock().addPart(new Part(type,-1,"","",""));
				else
					return null;
				i++;
				if(i >= str.length())
					return null;
			}
		}
		return pf;
	}
	
	private static List<Part> parseForString(String str)
	{
		int i = 0;
		List<Part> parts = new FastList<Part>();
		if(str.charAt(i) != '[')
		{
			i++;
			for(Part part : parseForString(str.substring(i, str.substring(i).indexOf(']'))))
			{
				parts.add(part);
				while(str.charAt(i) == ' ')
				{
					i++;
					if(i >= str.length())
						return null;
				}
				i++;
				if(i >= str.length())
					return null;
			}
		}
		else if(str.charAt(i) == ' ')
		{
			i++;
			if(i >= str.length())
				return null;
		}
		else
		{
		    PartType type = PartTypeManager.getInstance().getType(str.substring(i, i+1));
			if(type != null)
				parts.add(new Part(type,-1,"","",""));
			else
				return null;
			i++;
			if(i >= str.length())
				return null;
		}
		return parts;
	}
	
	public static int countIdPartsInString(String str)
	{
		str = str.trim();
		int i = 0;
		int count = 0;
		if(str.charAt(i) == '(')
		{
			i++;
			while(str.charAt(i) != ')')
			{
				if(str.charAt(i) != ' ')
				{
					if(PartTypeManager.getInstance().getType(str.substring(i, i+1)) == null)
					{
						return 0;
					}
					count++;
				}
				i++;
			}
		}
		return count;
	}

	public static List<PartType> getIdPartsInString(String str)
	{
		FastList<PartType> list = new FastList<PartType>();
		str = str.trim();
		int i = 0;
		if(str.charAt(i) == '(')
		{
			i++;
			while(str.charAt(i) != ')')
			{
				if(str.charAt(i) != ' ')
				{
                    PartType type = PartTypeManager.getInstance().getType(str.substring(i, i+1));
					if(type == null)
					{
						return null;
					}
                    list.add(type);
				}
				i++;
			}
		}
		return list;
	}
}