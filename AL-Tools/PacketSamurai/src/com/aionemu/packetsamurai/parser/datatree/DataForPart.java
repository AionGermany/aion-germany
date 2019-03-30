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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.packetsamurai.parser.formattree.ForPart;


public class DataForPart extends DataTreeNodeContainer
{
    private List<DataForBlock> _blockList; //list used for type casting
    
    // avoids construction of root ForPart
    @SuppressWarnings("unused")
    private DataForPart()
    {
    }

    public DataForPart(DataTreeNodeContainer container, ForPart part)
    {
        super(container, part);
    }

    @Override
    public void addNode(DataTreeNode node)
    {
        if(node instanceof DataForBlock)
            super.addNode(node);
        else
            throw new IllegalArgumentException("Only DataForBlocks can be added to DataForParts");
        _blockList = null; //invalidate casting list
    }

    @Override
    public List<DataForBlock> getNodes()
    {
        if(_blockList == null)
        {
            // right this is quite slow sompared to just not overriding, plus it kinda duplicates the list (even if it doesnt duplicate the content)
            // but this is the only way i found to make a clean API : getNodes() in DataForPart must have the List<DataForBlock> return type
            // just casting super.getNodes() to List<DataForBlock> will give an unsafe cast warning.
            _blockList = new ArrayList<DataForBlock>();
            for(DataTreeNode node : super.getNodes())
            {
                _blockList.add((DataForBlock) node);
            }
        }
        return _blockList;
    }
    
    @Override
    public String treeString()
    {
        return ((ForPart)this.getModelPart()).treeString();
    }
}