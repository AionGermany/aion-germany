/**
 * 
 */
package com.aionemu.gameserver.model.team.legion;

/**
 * @author CoolyT
 */
public class LegionTerritory {

	int territoryId = 0;
	int legionId = 0;
	String legionName = "";

	public LegionTerritory(int id) {
		this.territoryId = id;
	}

	public LegionTerritory() {

	}

	public int getId() {
		return territoryId;
	}

	public void setTerritoryId(int terretoryId) {
		this.territoryId = terretoryId;
	}

	public int getLegionId() {
		return legionId;
	}

	public void setLegionId(int legionId) {
		this.legionId = legionId;
	}

	public String getLegionName() {
		return legionName;
	}

	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}
}
