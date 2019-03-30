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

import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import java.sql.Timestamp;

public class TimeReader implements Reader 
{
    @Override
    public boolean loadReaderFromXML(Node n) 
    {
        return true;
    }

    @Override
    public boolean saveReaderToXML(Element element, Document doc) 
    {
        return true;
    }

    public JComponent readToComponent(ValuePart part) 
    {
        return new JLabel(read(part));
    }

    @Override
    public String read(ValuePart part) 
    {
        if (!(part instanceof IntValuePart))
            return "";

        long result = ((IntValuePart) part).getIntValue();

        Timestamp tt = new Timestamp(result);

        return String.format("%tH:%tM:%tS", tt);
    }

}
