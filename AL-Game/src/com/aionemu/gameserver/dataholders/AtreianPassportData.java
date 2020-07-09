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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.atreianpassport.AtreianPassportTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "atreian_passports")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtreianPassportData {

	@XmlElement(name = "atreian_passport")
	private List<AtreianPassportTemplate> tlist;

	@XmlTransient
	private final TIntObjectHashMap<AtreianPassportTemplate> passportData = new TIntObjectHashMap<>();

	@XmlTransient
	private final Map<Integer, AtreianPassportTemplate> passportDataMap = new HashMap<>(1);

	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
		for (AtreianPassportTemplate id : tlist) {
			passportData.put(id.getId(), id);
			passportDataMap.put(id.getId(), id);
		}
	}

	public int size() {
		return passportData.size();
	}

	public AtreianPassportTemplate getAtreianPassportId(int id) {
		return passportData.get(id);
	}

	public Map<Integer, AtreianPassportTemplate> getAll() {
		return passportDataMap;
	}
}
