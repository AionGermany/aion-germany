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
package com.aionemu.packetsamurai.utils.collector.data.towns;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author ViAl
 *
 */
@XmlType(name = "spawn")
public class Spawn {
	
	@XmlAttribute(name = "npc_id")
	public int npc_id;
	
	@XmlAttribute(name = "x")
	public float x;
	
	@XmlAttribute(name = "y")
	public float y;
	
	@XmlAttribute(name = "z")
	public float z;
	
	@XmlAttribute(name = "h")
	public int h;

	public Spawn() {
		super();
	}
	
	/**
	 * @param npc_id
	 * @param x
	 * @param y
	 * @param z
	 * @param h
	 */
	public Spawn(int npc_id, float x, float y, float z, int h) {
		super();
		this.npc_id = npc_id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}
}
