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


package com.aionemu.packetsamurai.gui.protocoleditor;

import javax.swing.JComboBox;

import com.aionemu.packetsamurai.gui.IconComboBoxRenderer;


/**
 * 
 * @author Gilles Duboscq
 *
 */
@SuppressWarnings("serial")
public class PartTypeComboBox extends JComboBox<Object>
{
	public int offset;
	
	public PartTypeComboBox()
	{
		super(IconComboBoxRenderer.typesButBlocks);
	}
	
	public void setBounds(int x, int y, int w, int h)
	{
        
		System.out.println("reshaping, offset = "+offset);
	    int newX = Math.max(x, offset);
	    super.setBounds(newX, y, w - (newX - x), h);
	}
}