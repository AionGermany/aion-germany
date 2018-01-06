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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.npcshout.NpcShout;
import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
import com.aionemu.gameserver.model.templates.npcshout.ShoutGroup;
import com.aionemu.gameserver.model.templates.npcshout.ShoutList;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Rolandas
 */

/**
 * <p/>
 * Java class for anonymous complex type.
 * <p/>
 * The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shout_group" type="{}ShoutGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "shoutGroups" })
@XmlRootElement(name = "npc_shouts")
public class NpcShoutData {

	@XmlElement(name = "shout_group")
	protected List<ShoutGroup> shoutGroups;
	@XmlTransient
	private TIntObjectHashMap<FastMap<Integer, List<NpcShout>>> shoutsByWorldNpcs = new TIntObjectHashMap<FastMap<Integer, List<NpcShout>>>();
	@XmlTransient
	private int count = 0;

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ShoutGroup group : shoutGroups) {
			for (int i = group.getShoutNpcs().size() - 1; i >= 0; i--) {
				ShoutList shoutList = group.getShoutNpcs().get(i);
				int worldId = shoutList.getRestrictWorld();

				FastMap<Integer, List<NpcShout>> worldShouts = shoutsByWorldNpcs.get(worldId);
				if (worldShouts == null) {
					worldShouts = FastMap.newInstance();
					this.shoutsByWorldNpcs.put(worldId, worldShouts);
				}

				this.count += shoutList.getNpcShouts().size();
				for (int j = shoutList.getNpcIds().size() - 1; j >= 0; j--) {
					int npcId = shoutList.getNpcIds().get(j);
					List<NpcShout> shouts = new ArrayList<NpcShout>(shoutList.getNpcShouts());
					if (worldShouts.get(npcId) == null) {
						worldShouts.put(npcId, shouts);
					}
					else {
						worldShouts.get(npcId).addAll(shouts);
					}
					shoutList.getNpcIds().remove(j);
				}
				shoutList.getNpcShouts().clear();
				shoutList.makeNull();
				group.getShoutNpcs().remove(i);
			}
			group.makeNull();
		}
		this.shoutGroups.clear();
		this.shoutGroups = null;
	}

	public int size() {
		return this.count;
	}

	/**
	 * Get global npc shouts plus world specific shouts. Make sure to clean it after the use.
	 *
	 * @return null if not found
	 */
	public List<NpcShout> getNpcShouts(int worldId, int npcId) {
		FastMap<Integer, List<NpcShout>> worldShouts = shoutsByWorldNpcs.get(0);

		if (worldShouts == null || worldShouts.get(npcId) == null) {
			worldShouts = shoutsByWorldNpcs.get(worldId);
			if (worldShouts == null || worldShouts.get(npcId) == null) {
				return null;
			}
			return new ArrayList<NpcShout>(worldShouts.get(npcId));
		}

		List<NpcShout> npcShouts = new ArrayList<NpcShout>(worldShouts.get(npcId));
		worldShouts = shoutsByWorldNpcs.get(worldId);
		if (worldShouts == null || worldShouts.get(npcId) == null) {
			return npcShouts;
		}
		npcShouts.addAll(worldShouts.get(npcId));

		return npcShouts;
	}

	/**
	 * Lightweight check for shouts, doesn't use memory as {@link #getNpcShouts(int worldId, int npcId)})
	 */
	public boolean hasAnyShout(int worldId, int npcId) {
		FastMap<Integer, List<NpcShout>> worldShouts = shoutsByWorldNpcs.get(0);

		if (worldShouts == null || worldShouts.get(npcId) == null) {
			worldShouts = shoutsByWorldNpcs.get(worldId);
			if (worldShouts == null || worldShouts.get(npcId) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Lightweight check for shouts, doesn't use memory as {@link #getNpcShouts(int worldId, int npcId, ShoutEventType type, String pattern, int skillNo)})
	 */
	public boolean hasAnyShout(int worldId, int npcId, ShoutEventType type) {
		List<NpcShout> shouts = getNpcShouts(worldId, npcId);
		if (shouts == null) {
			return false;
		}

		for (NpcShout s : shouts) {
			if (s.getWhen() == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets shouts for npc
	 *
	 * @param worldId
	 *            - npc World Id
	 * @param npcId
	 *            - npc Id
	 * @param type
	 *            - shout event type
	 * @param pattern
	 *            - specific pattern; if null, returns all
	 * @param skillNo
	 *            - specific skill number; if 0, returns all
	 */
	public List<NpcShout> getNpcShouts(int worldId, int npcId, ShoutEventType type, String pattern, int skillNo) {
		List<NpcShout> shouts = getNpcShouts(worldId, npcId);
		if (shouts == null) {
			return null;
		}

		List<NpcShout> result = new ArrayList<NpcShout>();
		for (NpcShout s : shouts) {
			if (s.getWhen() == type) {
				if (pattern != null && !pattern.equals(s.getPattern())) {
					continue;
				}
				if (skillNo != 0 && skillNo != s.getSkillNo()) {
					continue;
				}
				result.add(s);
			}
		}
		shouts.clear();
		return result.size() > 0 ? result : null;
	}
}
