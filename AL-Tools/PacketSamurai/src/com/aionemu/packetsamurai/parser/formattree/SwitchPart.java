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


package com.aionemu.packetsamurai.parser.formattree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class SwitchPart extends AbstractSwitchPart
{
    public SwitchPart(int id, String analyzerName) 
    {
    	super(id, analyzerName);
    }

    @Override
    public List<SwitchCaseBlock> getCases(int switchCase)
    {
        SwitchCaseBlock c = casesMap.get(switchCase);
        if(c == null)
        {
            if(_default == null)
            	return Collections.emptyList();
            c = _default;
        }
        return Arrays.asList(c);
    }
}