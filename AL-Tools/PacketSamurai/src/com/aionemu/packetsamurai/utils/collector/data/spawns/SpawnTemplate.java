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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.packetsamurai.Util;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
@XmlType(name = "spawn")
public class SpawnTemplate {
	
	@XmlAttribute(name = "npc_id")
	public int npc_id;
	
	@XmlAttribute(name = "handler")
	public String handler;
	
	@XmlAttribute(name = "respawn_time")
	public int respawntime;
	
	@XmlElement(name = "spot")
	public FastList<SpawnSpot> spots = new FastList<SpawnSpot>();
	
	@XmlTransient
	public boolean isNew = false;
	
	@XmlTransient
	public FastMap<Long, SpawnSpot> spotMap = new FastMap<Long, SpawnSpot>();
	
	public SpawnTemplate() {
		this.spots = new FastList<SpawnSpot>();
	}
	
	public SpawnTemplate(int npc_id, int respawntime, FastList<SpawnSpot> spots, String handler) {
		this.npc_id = npc_id;
		this.respawntime = respawntime;
		this.spots = spots;
		this.handler = handler;
	}
		
	public boolean addSpot(float x, float y, float z, byte heading)
	{
		SpawnSpot spot = new SpawnSpot(x,y,z,heading);
		if (!containSpot(spot,5))
		{	
			spot.isNew = true;
			spots.add(spot);
			return true;
		}
		return false;			
	}
	
	public boolean addStaticSpot(float x, float y, float z, byte heading)
	{
		SpawnSpot spot = new SpawnSpot(x,y,z,heading);
		if (!containSpot(spot))
		{
			spot.isNew = true;
			spots.add(spot);
			return true;
		}
		return false;	
	}
	
	public boolean containSpot(SpawnSpot spot)
	{
		return containSpot(spot,0);
	}
	
	public boolean containSpot(SpawnSpot spot, int range)
	{
		for (SpawnSpot s : spots)
		{	// if 
			if (s.x == spot.x && s.y == spot.y && s.z == spot.z)
				return true;
			
			if (range > 0 && Util.isInRange(spot, s, range))
				return true;
		}
		return false;
	}
	
	public void addSpot(long objectId, SpawnSpot spot)
	{
		spotMap.put(objectId, spot);
		spots.add(spot);
	}
}
