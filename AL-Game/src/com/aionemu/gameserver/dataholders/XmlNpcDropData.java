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
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropGroup;
import com.aionemu.gameserver.model.npcdrops.XmlDrop;
import com.aionemu.gameserver.model.npcdrops.XmlDropGroup;
import com.aionemu.gameserver.model.npcdrops.XmlNpcDrops;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "npc_drops")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNpcDropData {

	static Logger log = LoggerFactory.getLogger(XmlNpcDropData.class);
	@XmlElement(name = "npc_drop")
	private List<XmlNpcDrops> nds;
	private HashMap<Integer, ArrayList<DropGroup>> drops;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		this.drops = new HashMap<Integer, ArrayList<DropGroup>>();
		for (XmlNpcDrops nd : this.nds) {
			List<DropGroup> newDg = new ArrayList<DropGroup>();
			for (XmlDropGroup dg : nd.getDropGroup()) {
				List<Drop> dr = new ArrayList<Drop>();
				for (XmlDrop xd : dg.getDrop()) {
					Drop datDg = new Drop(xd.getItemId(), xd.getMinAmount(), xd.getMaxAmount(), xd.getChance(), xd.isNoReduction(), xd.isEachMember());
					dr.add(datDg);
				}
				DropGroup datDg = new DropGroup(dr, dg.getRace(), dg.isUseCategory(), dg.getGroupName());
				newDg.add(datDg);
			}
			if (this.drops.containsKey(Integer.valueOf(nd.getNpcId()))) {
				log.warn("Drop NPC duplicate List ID: " + nd.getNpcId());
			}
			else {
				this.drops.put(nd.getNpcId(), new ArrayList<DropGroup>());
			}
			this.drops.get(nd.getNpcId()).addAll(newDg);
		}
	}

	public int size() {
		return this.nds.size();
	}

	public HashMap<Integer, ArrayList<DropGroup>> getDrops() {
		return this.drops;
	}

	public void clear() {
		this.drops.clear();
		this.drops = null;
	}
}
