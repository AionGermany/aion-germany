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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import javolution.util.FastMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.LongValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

/**
 * 
 * @author Gilles Duboscq, modified Rolandas
 *
 */
public class BitSetReader implements Reader
{
	private Map<Long,Bit> _bits = new FastMap<Long, Bit>();
	private String _bitsetName;
	private boolean _isDefaultList = false;

	public boolean loadReaderFromXML(Node n)
	{
		NamedNodeMap bitsetAttrs = n.getAttributes();
		Node nameAttr = bitsetAttrs.getNamedItem("name");
		if(nameAttr == null)
			return true; // we will set default Hex values

		_bitsetName = nameAttr.getNodeValue();
		Node startNode = n.getOwnerDocument().getFirstChild().getFirstChild();
				
		for(Node subNode = startNode; subNode != null; subNode = subNode.getNextSibling())
		{
			if("bitset".equals(subNode.getNodeName()))
			{
				NamedNodeMap attrs = subNode.getAttributes();
				Node atr = attrs.getNamedItem("name");
				if(atr == null || !atr.getNodeValue().equals(_bitsetName))
					continue;      	
			}
			else
				continue;

			for (Node childNode = subNode.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
			{
				if("bit".equals(childNode.getNodeName()))
				{
					NamedNodeMap attrs = childNode.getAttributes();
					Node atr = attrs.getNamedItem("num");
					if(atr == null)
						return false;

					long bitId = 0;
					boolean inverted = false;
					try
					{
						bitId = Long.decode(atr.getNodeValue());
					}
					catch (NumberFormatException e)
					{
						//wrong
						return false;
					}
					atr = attrs.getNamedItem("inverted");
					if(atr != null)
					{
						inverted = Boolean.parseBoolean(atr.getNodeValue());
					}
					atr = attrs.getNamedItem("display");
					_bits.put(bitId, new Bit(bitId, atr.getNodeValue(),inverted));
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

		StringBuilder sb = new StringBuilder();
		int size = part.getType().getTypeByteNumber() * 8;
		
		if (_bits.isEmpty())
		{
			_isDefaultList = true;
			_bits.put(0L, new Bit(0, "0x0"));
			for (int s = 0; s < size; s++)
			{
				long n = 1 << s;
				_bits.put(n, new Bit(n, "0x" + Long.toHexString(n)));
			}
		}
		
		long maxValue = 1;
		while (--size > 0)
		{
			maxValue <<= 1;
			maxValue |= 1;
		}
		
		for(Entry<Long, Bit> entry : _bits.entrySet())
		{
			Bit b = entry.getValue();
			if (Long.bitCount(entry.getKey()) > Long.bitCount(maxValue))
				continue;
			
			long i = maxValue & entry.getKey();
			
			if ((val & i) != 0)
			{
				if (_isDefaultList)
					sb.append(b.name).append(",\n");
				else
					sb.append(b.name).append(" = 0x").append(Long.toHexString(b.number).toUpperCase()).append(",\n");
			}
		}
		
		if (sb.lastIndexOf(",\n") == sb.length() - 2)
			sb.setLength(sb.length() - 2);
		
		return sb.toString();
	}

	public JComponent readToComponent(ValuePart part)
	{
		String value = this.read(part);
		if ("".equals(value))
			return new JLabel(part.getValueAsString());
		
		JButton view = new JButton("View");
		view.addActionListener(new ButtonActionListener(this.read(part)));
		view.setActionCommand("clicked");
		return view;
	}

	class ButtonActionListener implements ActionListener
	{
		private String _values;

		public ButtonActionListener(String values)
		{
			_values = values;
		}

		public void actionPerformed(ActionEvent e)
		{
			JDialog dlg = new JDialog(((Main) PacketSamurai.getUserInterface()).getMainFrame(), "BitSet Values");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(200, 210);
			dlg.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getMainFrame());

			JTabbedPane tabPane = new JTabbedPane();

			JEditorPane sourceDisplay = new JEditorPane();
			sourceDisplay.setEditable(false);
			sourceDisplay.setContentType("text/plain");
			sourceDisplay.setText(_values);

			tabPane.add(new JScrollPane(sourceDisplay), "Masks");

			dlg.add(tabPane);
			dlg.setVisible(true);
		}
	}

	private class Bit
	{
		long number;
		String name;
		boolean inverted; // not used

		public Bit(long num, String n)
		{
			this(num, n, false);
		}		
		
		public Bit(long num, String n, boolean i)
		{
			number = num;
			name = n;
			inverted = i;
		}
	}

	public boolean saveReaderToXML(Element element, Document doc)
	{
		Element bitsetElem = doc.createElement("bitset");
		bitsetElem.setAttribute("name", _bitsetName);
		
		for (Bit b : _bits.values())
		{
			Element bit = doc.createElement("bit");
			bit.setAttribute("num", Long.toString(b.number));
			bit.setAttribute("display", b.name);
			if(b.inverted)
				bit.setAttribute("inverted", "true");
			bitsetElem.appendChild(bit);
		}
		doc.appendChild(bitsetElem);
		return true;
	}

}