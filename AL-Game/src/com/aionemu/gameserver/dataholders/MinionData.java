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

import com.aionemu.gameserver.model.templates.minion.MinionTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "minions")
public class MinionData {

	@XmlElement(name = "minion")
	private List<MinionTemplate> minionTemplates;
	@XmlTransient
	private TIntObjectHashMap<MinionTemplate> minionData = new TIntObjectHashMap<>();
	@XmlTransient
	private List<Integer> minionDataList = new ArrayList<Integer>();

	void afterUnmarshal(final Unmarshaller unmarshaller, final Object o) {
		for (MinionTemplate minionTemplate : minionTemplates) {
			minionData.put(minionTemplate.getId(), minionTemplate);
			minionDataList.add(minionTemplate.getId());
		}
		minionTemplates.clear();
		minionTemplates = null;
	}

	public int size() {
		return minionData.size();
	}

	public MinionTemplate getMinionTemplate(int minionId) {
		return minionData.get(minionId);
	}

	public List<Integer> getAll() {
		return minionDataList;
	}

    public TIntObjectHashMap<MinionTemplate> getMinionData() {
        return minionData;
    }
}
