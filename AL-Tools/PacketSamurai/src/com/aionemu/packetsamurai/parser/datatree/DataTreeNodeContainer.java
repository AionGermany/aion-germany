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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.DataStructure.DataPacketMode;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.PartContainer;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DataTreeNodeContainer extends DataTreeNode
{
    private Map<Integer, ValuePart> _partIdMap = new HashMap<Integer, ValuePart>();
    private List<DataTreeNode> _nodes = new LinkedList<DataTreeNode>();
    private boolean _isRoot;
    
    // can not intantiate non-root container
    protected DataTreeNodeContainer(DataTreeNodeContainer container, Part part)
    {
        super(container, part);
        if(!(part instanceof PartContainer || part instanceof ForPart))
        {
            System.out.println("Class: "+part.getClass().getName());
            throw new IllegalArgumentException("The model of a packet node container must be a blockpart/switchcase/forpart");
        }
        _isRoot = false;
    }
    
    public DataTreeNodeContainer()
    {
        super();
        _isRoot = true;
    }
    
    public DataTreeNodeContainer(DataPacketMode mode)
    {
        super(mode);
        _isRoot = true;
    }
    
    public boolean isRoot()
    {
        return _isRoot;
    }

    public void addNode(DataTreeNode node)
    {
        _nodes.add(node);
        if(node instanceof ValuePart && node.getModelPart().getId() != -1)
            _partIdMap.put(node.getModelPart().getId(), (ValuePart) node);
    }
    
    /**
     * Searches into parent containers until the part is found or the top level container is reached.
     * @param id The Id of the PacketPart to be retrieved
     * @return The PacketPart with the given Id if found, null otherwise
     */
    public ValuePart getPacketValuePartById(int id)
    {
        ValuePart vp = _partIdMap.get(id);
        if (vp == null && !this.isRoot())
            return this.getParentContainer().getPacketValuePartById(id);
        return vp;
    }
    
    public Map<Integer, ValuePart> getPartIdMap()
    {
        return Collections.unmodifiableMap(_partIdMap);
    }
    
    public List<? extends DataTreeNode> getNodes()
    {
        return _nodes;
    }
    
    public DataTreeNode getPartByName(String name)
    {
        return getPartByName(name, false);
    }

    public DataTreeNode getPartByName(String name, boolean enterSwitch)
    {
        for (DataTreeNode node : this.getNodes())
        {
            if (name.equals(node.getModelPart().getName()))
                return node;
            if(enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
            {
                ((DataTreeNodeContainer)node).getPartByName(name, true);
            }
        }
        return null;
    }
    
    public DataTreeNode getPartById(int id)
    {
        return getPartById(id, false);
    }
    
    public DataTreeNode getPartById(int id, boolean enterSwitch)
    {
        for (DataTreeNode node : this.getNodes())
        {
            if (node.getModelPart().getId() == id)
                return node;
            if(enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
            {
                ((DataTreeNodeContainer)node).getPartById(id, true);
            }
        }
        return null;
    }
    
    public DataTreeNode getPartByAnalyserName(String analyserName)
    {
        return getPartByAnalyserName(analyserName, false);
    }
    
    public DataTreeNode getPartByAnalyserName(String analyserName, boolean enterSwitch)
    {
        for (DataTreeNode node : this.getNodes())
        {
            if (analyserName.equals(node.getModelPart().getAnalyzerName()))
                return node;
            if(enterSwitch && node instanceof DataTreeNodeContainer && node.getModelPart().getType() == PartType.swicthBlock)
            {
                DataTreeNode part = ((DataTreeNodeContainer)node).getPartByAnalyserName(analyserName, true);
                if (part != null)
                {
                    return part;
                }
            }
        }
        return null;
    }

    public String getValueByAnalyzerName(String string)
    {
        DataTreeNode part = getPartByAnalyserName(string);
        if(part != null && part instanceof ValuePart)
        {
            return ((ValuePart)part).getValueAsString();
        }
        return null;
    }
    
    public int getBytesSize()
    {
        int size = 0;
        for (DataTreeNode dpn : this.getNodes())
        {
            size += dpn.getBytesSize();
        }
        return size;
    }
}