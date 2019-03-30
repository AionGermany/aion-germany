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


package com.aionemu.packetsamurai.utils.collector.data.houses;

public class HouseDecorData {

	private static String NULL = "NULL";
	
	public int address;
	public int building;
	
	public int roof;
	public int outwall;
	public int frame;
	public int door;
	public int garden;
	public int fence;
	public int inwall;
	public int infloor;
	public int addon;
	
	public int getUniqueId() {
		return address << 16 | building;
	}
	
	private String formatInt(int value) {
		if (value == 0)
			return NULL;
		return Integer.toString(value);
	}
	
	public String getRoofSql() {
		return formatInt(roof);
	}
	
	public String getOutwallSql() {
		return formatInt(outwall);
	}
	
	public String getFrameSql() {
		return formatInt(frame);
	}
	
	public String getDoorSql() {
		return formatInt(door);
	}
	
	public String getGardenSql() {
		return formatInt(garden);
	}
	
	public String getFenceSql() {
		return formatInt(fence);
	}
	
	public String getInwallSql() {
		return formatInt(inwall);
	}
	
	public String getInfloorSql() {
		return formatInt(infloor);
	}
	
	public String getAddonSql() {
		return formatInt(addon);
	}
}
