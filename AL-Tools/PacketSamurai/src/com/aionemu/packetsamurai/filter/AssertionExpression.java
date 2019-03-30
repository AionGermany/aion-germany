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


package com.aionemu.packetsamurai.filter;


import com.aionemu.packetsamurai.filter.assertionoperator.AssertionOperator;
import com.aionemu.packetsamurai.filter.assertionoperator.NumberAssertionOperator;
import com.aionemu.packetsamurai.filter.assertionoperator.StringAssertionOperator;
import com.aionemu.packetsamurai.filter.value.Value;
import com.aionemu.packetsamurai.filter.value.number.NumberValue;
import com.aionemu.packetsamurai.filter.value.string.StringValue;
import com.aionemu.packetsamurai.parser.DataStructure;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class AssertionExpression extends Expression
{
    private AssertionOperator _operator;
    private Value _value1;
    private Value _value2;
    
    public AssertionExpression(AssertionOperator op)
    {
        _operator = op;
    }
    
    @Override
    public boolean evaluate(DataStructure dp)
    {
        if(this.getValue1() == null || this.getValue2() == null)
            throw new IllegalStateException("Must set values before evaluation");
        if(this.getAssertionOperator() instanceof StringAssertionOperator)
        {
            return ((StringAssertionOperator) this.getAssertionOperator()).execute((StringValue)this.getValue1(), (StringValue)this.getValue2(), dp);
        }
        else if(this.getAssertionOperator() instanceof NumberAssertionOperator)
        {
            return ((NumberAssertionOperator) this.getAssertionOperator()).execute((NumberValue)this.getValue1(), (NumberValue)this.getValue2(), dp);
        }
        return false;
    }
    
    public AssertionOperator getAssertionOperator()
    {
        return _operator;
    }
    
    public Value getValue1()
    {
        return _value1;
    }
    
    public void setValue1(Value val)
    {
        if(getAssertionOperator() instanceof StringAssertionOperator && !(val instanceof StringValue))
            throw new IllegalArgumentException("This Expression is made to have String operands");
        else if (getAssertionOperator() instanceof NumberAssertionOperator && !(val instanceof NumberValue))
            throw new IllegalArgumentException("This Expression is made to have Number operands");
        _value1 = val;
    }
    
    public Value getValue2()
    {
        return _value2;
    }
    
    public void setValue2(Value val)
    {
        if(getAssertionOperator() instanceof StringAssertionOperator && !(val instanceof StringValue))
            throw new IllegalArgumentException("This Expression is made to have String operands");
        else if (getAssertionOperator() instanceof NumberAssertionOperator && !(val instanceof NumberValue))
            throw new IllegalArgumentException("This Expression is made to have Number operands");
        _value2 = val;
    }
}