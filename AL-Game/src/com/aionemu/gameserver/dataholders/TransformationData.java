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

import com.aionemu.gameserver.model.templates.transformation.TransformationTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "transformations")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransformationData {

	@XmlElement(name = "transformation")
	private List<TransformationTemplate> transformationTemplates;
	@XmlTransient
	private TIntObjectHashMap<TransformationTemplate> TransformationData = new TIntObjectHashMap<>();
	@XmlTransient
	private List<Integer> TransformationDataList = new ArrayList<Integer>();

	void afterUnmarshal(final Unmarshaller unmarshaller, final Object o) {
		for (TransformationTemplate transformationTemplate : transformationTemplates) {
			TransformationData.put(transformationTemplate.getId(), transformationTemplate);
			TransformationDataList.add(transformationTemplate.getId());
		}
		transformationTemplates.clear();
		transformationTemplates = null;
	}

	public int size() {
		return TransformationData.size();
	}

	public TransformationTemplate getTransformationTemplate(int transformationId) {
		return TransformationData.get(transformationId);
	}

	public List<Integer> getAll() {
		return TransformationDataList;
	}

	public TIntObjectHashMap<TransformationTemplate> getTransformationData() {
		return TransformationData;
	}
}
