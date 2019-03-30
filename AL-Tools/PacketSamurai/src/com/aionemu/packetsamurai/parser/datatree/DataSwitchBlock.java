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

import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;

public class DataSwitchBlock extends DataTreeNodeContainer
{
    private ValuePart _testValuePart;
    
    //avoids construction of root SwitchBlock
    @SuppressWarnings("unused")
    private DataSwitchBlock()
    {
        
    }
    
    public DataSwitchBlock(DataTreeNodeContainer container, SwitchCaseBlock part, ValuePart vp)
    {
        super(container, part);
        _testValuePart = vp;
    }
    
    @Override
    public String treeString()
    {
        SwitchCaseBlock block = (SwitchCaseBlock) this.getModelPart();
        AbstractSwitchPart part = block.getContainingSwitch();
        if(block.isDefault())
            return "Switch on '"+part.getTestPart().getName()+"' - default case";
        return "Switch on '"+part.getTestPart().getName()+"' - case '"+this.getTestValuePart().readValue()+"'";
    }
    
    public ValuePart getTestValuePart()
    {
        return _testValuePart;
    }
}