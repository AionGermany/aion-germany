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

import com.aionemu.packetsamurai.parser.PartType;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class SwitchCaseBlock extends PartContainer
{
	private int _case;
	private boolean _isDefault;
	private AbstractSwitchPart _switch;
	private String _comment;
	
	/**
	 * This constructor creates a new non-default SwitchCase
	 * @param sw the containing SwithBlock
	 * @param id the case of this SwichCase
	 */
	public SwitchCaseBlock(AbstractSwitchPart sw, int id, String analyzerName)
	{
        super(PartType.swicthBlock);
        this.setAnalyzerName(analyzerName);
		_case = id;
		_switch = sw;
	}
	
	/**
	 * This constructor creates a default SwitchCase
	 * @param sw the containing SwithBlock
	 */
	public SwitchCaseBlock(AbstractSwitchPart sw, String analyzerName)
	{
        super(PartType.swicthBlock);
        this.setAnalyzerName(analyzerName);
		_isDefault = true;
		_switch = sw;

	}
	
	/**
	 * Returns the case of this SwitchCase. This is irrelevant if this is a default case.
	 * @return the case of this SwitchCase
	 */
	public int getSwitchCase()
	{
		return _case;
	}
	
	public AbstractSwitchPart getContainingSwitch()
	{
		return _switch;
	}
	
	public boolean isDefault()
	{
		return _isDefault;
	}
	
	public String treeString()
	{
		if(_isDefault)
			return "Default";
		return "Case 0x"+Integer.toHexString(_case)+(_case > 9 ? " ("+_case+")" : "");
	}
	
	public void setDefault(boolean b)
	{
		_isDefault = b;
	}

	public void setSwitchCase(int id)
	{
		_switch.removeCase(_case);
		_case = id;
		_switch.addCase(this);
	}
	
	public String toString()
	{
		return _isDefault ? "default" : _switch.getTestPart().getName()+" = "+Integer.toString(_case);
	}
	
	public void setComment(String com)
	{
		_comment = com;
	}
	
	public String getComment()
	{
		return _comment;
	}
}