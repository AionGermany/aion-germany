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

import com.aionemu.gameserver.model.templates.cosmeticitems.CosmeticItemTemplate;

import javolution.util.FastMap;

/**
 * @author xTz
 */
@XmlRootElement(name = "cosmetic_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class CosmeticItemsData {

	@XmlElement(name = "cosmetic_item", type = CosmeticItemTemplate.class)
	private List<CosmeticItemTemplate> templates;
	private final Map<String, CosmeticItemTemplate> cosmeticItemTemplates = new FastMap<String, CosmeticItemTemplate>().shared();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (CosmeticItemTemplate template : templates) {
			cosmeticItemTemplates.put(template.getCosmeticName(), template);
		}
		templates.clear();
		templates = null;
	}

	public int size() {
		return cosmeticItemTemplates.size();
	}

	public CosmeticItemTemplate getCosmeticItemsTemplate(String str) {
		return cosmeticItemTemplates.get(str);
	}
}
