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

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

/**
 * 
 * @author Gilles Duboscq, modified Rolandas
 *
 */
public class ColorReader implements Reader
{
    public boolean loadReaderFromXML(Node n)
    {
        return true;
    }

    public String read(ValuePart part)
    {
        return part.getHexDump();
    }
    
    public JComponent readToComponent(ValuePart part)
    {
        if(!(part instanceof IntValuePart))
            throw new IllegalStateException("A ColorReader must be providen an IntValuePart");
        int color = ((IntValuePart)part).getIntValue();
        int r = (color  & 0x000000ff); //save red
        color = (color  & 0xff00ff00) | ((color & 0x00ff0000) >> 0x10); //swap red and blue
        color  = (color  & 0xff00ffff) | (r << 0x10);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(color));
        return panel;
    }

    public boolean saveReaderToXML(Element element, Document doc)
    {
        return true;
    }
    
}