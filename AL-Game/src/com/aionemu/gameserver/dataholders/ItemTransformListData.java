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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.item.ItemTransformList;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "transforms_list")
public class ItemTransformListData {

	@XmlElement(name = "transform_list", required = true)
	protected List<ItemTransformList> transformlist;
	@XmlTransient
	private TIntObjectHashMap<ItemTransformList> custom = new TIntObjectHashMap<ItemTransformList>();

	public ItemTransformList getTransformList(int id) {
		return (ItemTransformList) custom.get(id);
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ItemTransformList it : transformlist) {
			getCustomMap().put(it.getId(), it);
		}
	}

	private TIntObjectHashMap<ItemTransformList> getCustomMap() {
		return custom;
	}

	public int size() {
		return custom.size();
	}
}
