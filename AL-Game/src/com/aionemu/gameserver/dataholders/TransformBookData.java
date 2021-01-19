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
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.transform_book.TransformBookTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;
import javolution.util.FastMap;

@XmlRootElement(name = "transform_book_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransformBookData {

	@XmlElement(name = "transform_book_template")
	private List<TransformBookTemplate> tlist;
	@SuppressWarnings("unused")
	private FastList<TransformBookTemplate> normal;
	@SuppressWarnings("unused")
	private FastList<TransformBookTemplate> greater;
	@SuppressWarnings("unused")
	private FastList<TransformBookTemplate> ancient;
	@SuppressWarnings("unused")
	private FastList<TransformBookTemplate> legendary;
	@SuppressWarnings("unused")
	private FastList<TransformBookTemplate> ultime;
	private List<TransformBookTemplate> rndTransform = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
	private Map<Integer, Integer> transformSkill = (Map<Integer, Integer>) new FastMap<Integer, Integer>();
	@XmlTransient
	private TIntObjectHashMap<TransformBookTemplate> transformBookData = (TIntObjectHashMap<TransformBookTemplate>) new TIntObjectHashMap<TransformBookTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (TransformBookTemplate book : tlist) {
			transformBookData.put(book.getId(), book);
			rndTransform.add(book);
			transformSkill.put(book.getSkillId(), book.getId());
		}
	}

	public int size() {
		return transformBookData.size();
	}

	public TIntObjectHashMap<TransformBookTemplate> getAllBooks() {
		return transformBookData;
	}

	public TransformBookTemplate getTransformBookById(int id) {
		return transformBookData.get(id);
	}

	public List<TransformBookTemplate> getAllTransform() {
		return rndTransform;
	}

	public List<TransformBookTemplate> getAncient() {
		List<TransformBookTemplate> list = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
		for (TransformBookTemplate tp : transformBookData.valueCollection()) {
			if (tp.getGrade() == 1) {
				list.add(tp);
			}
		}
		return list;
	}

	public List<TransformBookTemplate> getGreater() {
		List<TransformBookTemplate> list = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
		for (TransformBookTemplate tp : transformBookData.valueCollection()) {
			if (tp.getGrade() == 2) {
				list.add(tp);
			}
		}
		return list;
	}

	public List<TransformBookTemplate> getLegendary() {
		List<TransformBookTemplate> list = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
		for (TransformBookTemplate tp : transformBookData.valueCollection()) {
			if (tp.getGrade() == 3) {
				list.add(tp);
			}
		}
		return list;
	}

	public List<TransformBookTemplate> getNormal() {
		List<TransformBookTemplate> list = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
		for (TransformBookTemplate tp : transformBookData.valueCollection()) {
			if (tp.getGrade() == 4) {
				list.add(tp);
			}
		}
		return list;
	}

	public List<TransformBookTemplate> getUltime() {
		List<TransformBookTemplate> list = (List<TransformBookTemplate>) new FastList<TransformBookTemplate>();
		for (TransformBookTemplate tp : transformBookData.valueCollection()) {
			if (tp.getGrade() == 5) {
				list.add(tp);
			}
		}
		return list;
	}

	public Integer getTransformId(int skillId) {
		int transformId = 0;
		if (transformSkill.containsKey(skillId)) {
			transformId = transformSkill.get(skillId);
		}
		return transformId;
	}
}
