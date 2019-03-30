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
package com.aionemu.packetsamurai.utils.collector.data.spawns;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.packetsamurai.Util;

/**
 * @author CoolyT
 */
@XmlType(name = "spot")
public class SpawnSpot {
	
	@XmlAttribute(name = "x")
	public float x;
	
	@XmlAttribute(name = "y")
	public float y;
	
	@XmlAttribute(name = "z")
	public float z;
	
	@XmlAttribute(name = "h")
	public byte h;

	@XmlTransient
	public long objectId;

	@XmlTransient
	public int worldId;
	
	@XmlTransient
	public boolean isNew;
		
	@XmlAttribute(name = "spawn_static_id")
	public int spawnStaticId;

	public SpawnSpot() {
	}
	
	public boolean isInRange(SpawnSpot spot)
	{
		//If the position is already in template
		if (x == spot.x && y == spot.y)
			return true;
		
		return Util.isInRange(this, spot, 5);
	}
	
	public boolean isInRange(SpawnSpot spot, int meter)
	{
		//If the position is already in template
		if (x == spot.x && y == spot.y)
			return true;
		
		return Util.isInRange(this, spot, meter);
	}
	
	public SpawnSpot(float x, float y, float z, byte h) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}
}
