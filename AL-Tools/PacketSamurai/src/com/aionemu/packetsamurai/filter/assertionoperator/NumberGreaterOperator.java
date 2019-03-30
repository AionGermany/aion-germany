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


package com.aionemu.packetsamurai.filter.assertionoperator;


import com.aionemu.packetsamurai.filter.value.number.FloatNumberValue;
import com.aionemu.packetsamurai.filter.value.number.IntegerNumberValue;
import com.aionemu.packetsamurai.filter.value.number.NumberValue;
import com.aionemu.packetsamurai.parser.DataStructure;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class NumberGreaterOperator implements NumberAssertionOperator
{

    public boolean execute(NumberValue value1, NumberValue value2, DataStructure dp)
    {
        if(value1 instanceof IntegerNumberValue && value2 instanceof IntegerNumberValue)
        {
            return ((IntegerNumberValue)value1).getIntegerValue(dp) > ((IntegerNumberValue)value2).getIntegerValue(dp);
        }
        if(value1 instanceof FloatNumberValue && value2 instanceof IntegerNumberValue)
        {
            return ((FloatNumberValue)value1).getFloatValue(dp) > ((IntegerNumberValue)value2).getIntegerValue(dp);
        }
        if(value1 instanceof IntegerNumberValue && value2 instanceof FloatNumberValue)
        {
            return ((IntegerNumberValue)value1).getIntegerValue(dp) > ((FloatNumberValue)value2).getFloatValue(dp);
        }
        if(value1 instanceof FloatNumberValue && value2 instanceof FloatNumberValue)
        {
            return ((FloatNumberValue)value1).getFloatValue(dp) > ((FloatNumberValue)value2).getFloatValue(dp);
        }
        return false;
    }

}