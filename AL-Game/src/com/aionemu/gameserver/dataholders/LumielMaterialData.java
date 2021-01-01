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

import com.aionemu.gameserver.model.templates.lumiel_transform.LumielMaterialTemplate;

import javolution.util.FastMap;

@XmlAccessorType(value=XmlAccessType.FIELD)
@XmlRootElement(name="lumiel_material_templates")
public class LumielMaterialData {

    @XmlElement(name="lumiel_material_template")
    private List<LumielMaterialTemplate> materialTemplate;
    @XmlTransient
    private FastMap<Integer, LumielMaterialTemplate> templates = new FastMap<Integer, LumielMaterialTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (LumielMaterialTemplate template : materialTemplate) {
            templates.put(template.getId(), template);
        }
    }

    public int size() {
        return templates.size();
    }

    public LumielMaterialTemplate getTemplate(int lumielId, int itemId) {
        LumielMaterialTemplate lumiel = null;
        for (LumielMaterialTemplate template : templates.values()) {
            if (template.getLumielId() != lumielId || template.getItemId() != itemId) continue;
            lumiel = template;
        }
        return lumiel;
    }
}

