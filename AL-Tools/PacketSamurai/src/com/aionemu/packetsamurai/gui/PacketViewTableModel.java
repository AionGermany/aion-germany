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


package com.aionemu.packetsamurai.gui;


import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.aionemu.packetsamurai.parser.datatree.DataTreeNode;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

/**
 * @author Ulysses R. Ribeiro
 *
 */
class PacketViewTableModel extends DefaultTreeTableModel
{
	private static final String[] columnNames = {
		"Name",
		"Value",
		"Lookup"
	};
    
    public PacketViewTableModel(TreeTableNode root)
    {
        super(root);
    }
    
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }
    
    public Object getValueAt(Object node, int column)
    {
        DataTreeNode part = ((DataPartNode)node).getPacketNode();
        switch(column)
        {
            case 0:
                return part.treeString();
            case 1:
                if(part instanceof ValuePart)
                {
                    return ((ValuePart)part).getValueAsString(); 
                }
                return "";
            case 2:
                if(part instanceof ValuePart)
                {
                    return ((ValuePart)part).readValueToComponent();
                }
                return "";
            default:
                return "";
        }
    }
}

