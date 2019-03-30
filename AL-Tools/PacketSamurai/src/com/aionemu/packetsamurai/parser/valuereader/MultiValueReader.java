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


package com.aionemu.packetsamurai.parser.valuereader;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;

import javolution.util.FastMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.LongValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

/**
 * 
 * @author Gilles Duboscq, refactored Rolandas
 *
 */
public class MultiValueReader implements Reader
{
    private Map<Long, String> _cases = new FastMap<Long, String>();
	private String _enumName;
    
    public boolean loadReaderFromXML(Node n)
    {
		NamedNodeMap enumAttrs = n.getAttributes();
		Node nameAttr = enumAttrs.getNamedItem("name");
		if(nameAttr == null)
			return false;

		_enumName = nameAttr.getNodeValue();
		Node startNode = n.getOwnerDocument().getFirstChild().getFirstChild();
		
		for(Node subNode = startNode; subNode != null; subNode = subNode.getNextSibling())
		{
			if("multivalue".equals(subNode.getNodeName()))
			{
				NamedNodeMap attrs = subNode.getAttributes();
				Node atr = attrs.getNamedItem("name");
				if(atr == null || !atr.getNodeValue().equals(_enumName))
					continue;      	
			}
			else
				continue;

			long seq = 0;
			
			for (Node childNode = subNode.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
			{
				if("enum".equals(childNode.getNodeName()))
				{
					NamedNodeMap attrs = childNode.getAttributes();
					
					String display;
					
					Node atr = attrs.getNamedItem("display");
					if(atr == null)
						return false;
					
					display = atr.getNodeValue();
					atr = attrs.getNamedItem("val");
					
					long val;
					if (atr == null)
						val = seq;
					else
					{
						try
						{
							val = Long.decode(atr.getNodeValue());
							seq = val;
						}
						catch (NumberFormatException e)
						{
							return false;
						}						
					}
					seq++;
					_cases.put(val, display);
				}
			}
		}
		return true;
    }

    public String read(ValuePart part)
    {
		long val;
		
		if(part instanceof IntValuePart)
			val = ((IntValuePart)part).getIntValue();
		else if (part instanceof LongValuePart)
			val = ((LongValuePart)part).getLongvalue();
		else
			return "";
		
		if (!_cases.containsKey(val))
			return "";
		
		return _cases.get(val);
    }
    
    public JComponent readToComponent(ValuePart part)
    {
        return new JLabel(this.read(part));
    }

    public boolean saveReaderToXML(Element element, Document doc)
    {
		Element multiElem = doc.createElement("multivalue");
		multiElem.setAttribute("name", _enumName);
		
		for (Entry<Long, String> c : _cases.entrySet())
		{
			Element valueElem = doc.createElement("enum");
			valueElem.setAttribute("val", Long.toString(c.getKey()));
			valueElem.setAttribute("display", c.getValue());
			multiElem.appendChild(valueElem);
		}
		doc.appendChild(multiElem);
		return true;
    }
    
}