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


package com.aionemu.packetsamurai.parser.datatree;


import com.aionemu.packetsamurai.parser.DataStructure.DataPacketMode;
import com.aionemu.packetsamurai.parser.formattree.Part;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public abstract class DataTreeNode
{
    private DataTreeNodeContainer _container;
    private Part _modelPart;
    private DataPacketMode _mode;
    
    
    public DataTreeNode(DataTreeNodeContainer container, Part part)
    {
        this(container, part, container.getMode());
    }
    
    public DataTreeNode(DataTreeNodeContainer container, Part part, DataPacketMode mode)
    {
        _container = container;
        _modelPart = part;
        _container.addNode(this);
        _mode = mode;
    }
    
    public DataTreeNode()
    {
        this(DataPacketMode.PARSING);
    }
    
    public DataTreeNode(DataPacketMode mode)
    {
        _mode = mode;
    }

    public DataPacketMode getMode()
    {
        return _mode;
    }

    public DataTreeNodeContainer getParentContainer()
    {
        return _container;
    }
    
    public Part getModelPart()
    {
        return _modelPart;
    }
    
    public abstract int getBytesSize();

    public String treeString()
    {
        return "DataPacketNode :S";
    }
}