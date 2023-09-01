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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.monsterbook.MonsterbookTemplate;

import javolution.util.FastMap;

@XmlRootElement(name = "monster_books")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonsterbookData {

	@XmlElement(name = "monster_book")
	private List<MonsterbookTemplate> monsterbookTemplates;
	private Map<Integer, MonsterbookTemplate> idsHolder;
	private Map<Integer, MonsterbookTemplate> npcIdsHolder;

	public MonsterbookData() {
		idsHolder = new FastMap<Integer, MonsterbookTemplate>().shared();
		npcIdsHolder = new FastMap<Integer, MonsterbookTemplate>().shared();
	}

	void afterUnmarshal(Unmarshaller unmarshaller, Object o) {
		for (MonsterbookTemplate monsterbookTemplate : monsterbookTemplates) {
			idsHolder.put(monsterbookTemplate.getId(), monsterbookTemplate);
			if (!monsterbookTemplate.getNpcIds().isEmpty()) {
				Iterator<Integer> iterator2 = monsterbookTemplate.getNpcIds().iterator();
				while (iterator2.hasNext()) {
					npcIdsHolder.put((int) iterator2.next(), monsterbookTemplate);
				}
			}
		}
		monsterbookTemplates.clear();
		monsterbookTemplates = null;
	}

	public int size() {
		return idsHolder.size();
	}

	public MonsterbookTemplate getMonsterbookTemplate(int id) {
		return idsHolder.get(id);
	}

	public int sizeByNpcId() {
		return npcIdsHolder.size();
	}

	public MonsterbookTemplate getMonsterbookTemplateByNpcId(int id) {
		return npcIdsHolder.get(id);
	}
}
