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


import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import com.aionemu.packetsamurai.parser.datatree.DataTreeNode;
import com.aionemu.packetsamurai.parser.datatree.DataTreeNodeContainer;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

public class DataPartNode extends DefaultMutableTreeTableNode
{
    private DataTreeNode _node;
    private int _offset;
    private int _length;
    
    public DataPartNode(DataTreeNode node, int offset)
    {
        super();
        _node = node;
        
        if (_node instanceof DataTreeNodeContainer)
        {
            int i = 0;
            for(DataTreeNode n : ((DataTreeNodeContainer)_node).getNodes())
            {
                this.insert(new DataPartNode(n, offset), i++);

                this.setOffset(offset);
                this.setLengtht(_node.getBytesSize()*3 - 1);
                offset += n.getBytesSize()*3;
            }
        }
        else
        {
            this.setOffset(offset);
            this.setLengtht(_node.getBytesSize()*3 - 1);
            offset += this.getLength() + 1;
        }
        
    }
    
    public boolean getAllowsChildren()
    {
        return (_node instanceof DataTreeNodeContainer);
    }
    
    public boolean isLeaf()
    {
        return(_node instanceof ValuePart);
    }
    
    public DataTreeNode getPacketNode()
    {
        return _node;
    }
    
    private void setOffset(int offset)
    {
        _offset = offset;
    }
    
    public int getOffset()
    {
        return _offset;
    }
    
    private void setLengtht(int length)
    {
        _length = length;
    }
    
    public int getLength()
    {
        return _length;
    }
}