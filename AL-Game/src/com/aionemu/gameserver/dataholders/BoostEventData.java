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

import com.aionemu.gameserver.model.templates.event.BoostEvents;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "boost_events")
public class BoostEventData {

	@XmlElement(name = "boost_event")
	protected List<BoostEvents> bonusServiceBonusattr;
	@XmlTransient
	private TIntObjectHashMap<BoostEvents> templates = new TIntObjectHashMap<BoostEvents>();
	@XmlTransient
	private Map<Integer, BoostEvents> templatesMap = new HashMap<Integer, BoostEvents>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (BoostEvents boostEvents : bonusServiceBonusattr) {
			templates.put(boostEvents.getId(), (boostEvents));
			templatesMap.put(boostEvents.getId(), boostEvents);
		}
		bonusServiceBonusattr.clear();
		bonusServiceBonusattr = null;
	}

	public int size() {
		return templates.size();
	}

	public BoostEvents getInstanceBonusattr(int buffId) {
		return templates.get(buffId);
	}

	public Map<Integer, BoostEvents> getAll() {
		return templatesMap;
	}
}