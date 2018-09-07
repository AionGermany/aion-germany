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
package com.aionemu.gameserver.model.templates.world;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeatherEntry")
public class WeatherEntry {

	public WeatherEntry() {
	}

	public WeatherEntry(int zoneId, int weatherCode) {
		this.weatherCode = weatherCode;
		this.zoneId = zoneId;
	}

	@XmlAttribute(name = "zone_id", required = true)
	private int zoneId;
	@XmlAttribute(name = "code", required = true)
	private int weatherCode;
	@XmlAttribute(name = "rank", required = true)
	private int rank;
	@XmlAttribute(name = "name")
	private String weatherName;
	@XmlAttribute(name = "before")
	private Boolean isBefore;
	@XmlAttribute(name = "after")
	private Boolean isAfter;

	public int getZoneId() {
		return zoneId;
	}

	public int getCode() {
		return weatherCode;
	}

	public int getRank() {
		return rank;
	}

	public Boolean isBefore() {
		if (isBefore == null) {
			return false;
		}
		return isBefore;
	}

	public Boolean isAfter() {
		if (isAfter == null) {
			return false;
		}
		return isAfter;
	}

	public String getWeatherName() {
		return weatherName;
	}
}
