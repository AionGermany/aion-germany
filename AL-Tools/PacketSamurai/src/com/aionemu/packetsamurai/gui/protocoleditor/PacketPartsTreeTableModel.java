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


package com.aionemu.packetsamurai.gui.protocoleditor;


import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

import com.aionemu.packetsamurai.parser.PartTypeManager;
import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFormat;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class PacketPartsTreeTableModel extends AbstractTreeTableModel
{
    public PacketPartsTreeTableModel(PacketFormat format)
    {
        super(format);
    }

    protected static String[] columnsNames = {"Type", "Name", "Id", "LookUp"};
    protected static Class<?>[] columnsClasses = { TreeTableModel.class, String.class, Integer.class, String.class };

    public int getColumnCount()
    {
        return columnsNames.length;
    }

    public String getColumnName(int column)
    {
        return columnsNames[column];
    }

    public Class<?> getColumnClass(int column)
    {
        return columnsClasses[column];
    }

    public boolean isCellEditable(Object node, int column)
    {
        if(column != 0 && column != 1)
            return true;
        if(node instanceof ForPart || node instanceof AbstractSwitchPart || node instanceof SwitchCaseBlock)
            return false;
        return true;
    }

    public Object getValueAt(Object node, int column)
    {
        if(node instanceof ForPart)
        {
            switch(column)
            {
                case 1:
                    return ((ForPart)node).treeString();
                case 2:
                    return ((ForPart)node).getForId();
                default:
                    return "";
            }
        }
        else if(node instanceof AbstractSwitchPart)
        {
            switch(column)
            {
                case 1:
                    return ((AbstractSwitchPart)node).treeString();
                case 2:
                    return ((AbstractSwitchPart)node).getSwitchId();
                default:
                    return "";
            }
        }
        else if(node instanceof SwitchCaseBlock)
        {
            switch(column)
            {
                case 0:
                    return ((SwitchCaseBlock)node).isDefault() ? "default" : Integer.toString(((SwitchCaseBlock)node).getSwitchCase());
                case 1:
                    return ((SwitchCaseBlock)node).treeString();
                default:
                    return "";
            }
        }
        else if (node instanceof Part)
        {
            Part part = (Part) node;
            switch(column)
            {
                case 0:
                    return part.getType().getName();
                case 1:
                    return part.getName();
                case 2:
                    return part.getId();
                case 3:
                    return part.getLookUpType();
            }
        }
        return null;
    }

    public Object getChild(Object parent, int index)
    {
        if (parent instanceof ForPart)
        {
            ForPart forBlock = (ForPart) parent;
            return forBlock.getModelBlock().getPart(index);
        }
        else if(parent instanceof AbstractSwitchPart)
        {
        	AbstractSwitchPart switchBlock = (AbstractSwitchPart) parent;
            return switchBlock.getCases(true).get(index);
        }
        else if(parent instanceof SwitchCaseBlock)
        {
            SwitchCaseBlock switchCase = (SwitchCaseBlock) parent;
            return switchCase.getParts().get(index);
        }
        else if(parent instanceof PacketFormat)
        {
            return ((PacketFormat)parent).getDataFormat().getMainBlock().getParts().get(index);
        }
        return null;
    }

    public int getChildCount(Object parent)
    {
        if (parent instanceof ForPart)
        {
            ForPart forBlock = (ForPart) parent;
            return forBlock.getModelBlock().size();
        }
        else if(parent instanceof AbstractSwitchPart)
        {
        	AbstractSwitchPart switchBlock = (AbstractSwitchPart) parent;
            return switchBlock.getCases(true).size();
        }
        else if(parent instanceof SwitchCaseBlock)
        {
            SwitchCaseBlock switchCase = (SwitchCaseBlock) parent;
            return switchCase.getParts().size();
        }
        else if(parent instanceof PacketFormat)
        {
            return ((PacketFormat)parent).getDataFormat().getMainBlock().getParts().size();
        }
        return 0;
    }

    public void setValueAt(Object aValue, Object node, int column)
    {
        switch(column)
        {
            case 0:
            {
                Part part = (Part) node;
                part.setType(PartTypeManager.getInstance().getType((String) aValue));
                break;
            }
            case 1:
            {
                Part part = (Part) node;
                if(!(part instanceof ForPart))
                    part.setName((String) aValue);
                break;
            }
            case 2:
                if(node instanceof ForPart)
                {
                    Part part = (Part) node;
                    try
                    {
                        int id = Integer.valueOf((String) aValue);
                        ((ForPart)part).setForId(id);
                    }
                    catch (NumberFormatException nfe)
                    {
                        nfe.printStackTrace();
                    }
                    break;
                }
                else if (node instanceof SwitchCaseBlock)
                {
                    try
                    {
                        int id = Integer.valueOf((String) aValue);
                        ((SwitchCaseBlock)node).setSwitchCase(id);
                    }
                    catch (NumberFormatException nfe)
                    {
                        nfe.printStackTrace();
                    }
                    break;
                }
                else if (node instanceof AbstractSwitchPart)
                {
                    try
                    {
                        int id = Integer.valueOf((String) aValue);
                        ((AbstractSwitchPart)node).setId(id);
                    }
                    catch (NumberFormatException nfe)
                    {
                        nfe.printStackTrace();
                    }
                    break;
                }
                try
                {
                    Part part = (Part) node;
                    int id = Integer.valueOf((String) aValue);
                    part.setId(id);
                }
                catch (NumberFormatException nfe)
                {
                    nfe.printStackTrace();
                }
                break;
            case 3:
            {
                Part part = (Part) node;
                if(!(part instanceof ForPart))
                    part.setLookUpType((String) aValue);
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child)
    {
        throw new UnsupportedOperationException("Mehod not implemented yet.");
    }

}