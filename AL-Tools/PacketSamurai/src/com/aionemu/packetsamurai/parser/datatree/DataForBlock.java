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

import com.aionemu.packetsamurai.parser.formattree.PartContainer;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DataForBlock extends DataTreeNodeContainer
{
    private int _iteration;
    private int _size;
    
    // avoids construction of root ForBlock
    @SuppressWarnings("unused")
    private DataForBlock()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public DataForBlock(DataTreeNodeContainer container, PartContainer part, int iteration, int size)
    {
        super(container, part);
        _iteration = iteration;
        _size = size;
    }
    
    @Override
    public String treeString()
    {
        return "Iteration "+(_iteration+1)+"/"+_size;
        
    }
}