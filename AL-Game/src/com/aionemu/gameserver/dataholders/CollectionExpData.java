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

import com.aionemu.gameserver.model.templates.collection.CollectionExpTemplate;
import com.aionemu.gameserver.model.templates.collection.CollectionType;

import javolution.util.FastList;
import javolution.util.FastMap;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "collection_exp_templates")
public class CollectionExpData {

	@XmlElement(name = "collection_exp_template")
	private List<CollectionExpTemplate> collectionExpTemplates;

	@XmlTransient
	private FastMap<CollectionType, List<CollectionExpTemplate>> expTemplateMap = new FastMap<CollectionType, List<CollectionExpTemplate>>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (CollectionExpTemplate template : collectionExpTemplates) {
            if (expTemplateMap.containsKey(template.getGrade())) {
                expTemplateMap.get(template.getGrade()).add(template);
            } else {
                List<CollectionExpTemplate> exp = (List<CollectionExpTemplate>)new FastList<CollectionExpTemplate>();
                exp.add(template);
                expTemplateMap.put(template.getGrade(), exp);
            }
        }
    }

	public CollectionExpTemplate getTemplate(int level, CollectionType grade) {
        CollectionExpTemplate template = null;

        for (CollectionExpTemplate expTemplate : expTemplateMap.get(grade)) {
			if (expTemplate.getLevel() == level) {
				template = expTemplate;
            }
        }
        return template;
    }

	public int size() {
		return expTemplateMap.size();
	}
}
