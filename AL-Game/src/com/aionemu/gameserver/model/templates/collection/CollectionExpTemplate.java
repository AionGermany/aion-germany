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
package com.aionemu.gameserver.model.templates.collection;

import com.aionemu.gameserver.model.templates.collection.CollectionType;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value=XmlAccessType.FIELD)
@XmlType(name="CollectionExpTemplate")
public class CollectionExpTemplate {

    @XmlElement(name = "modifiers", required = false)
    private ModifiersTemplate modifiers;
    @XmlAttribute(name = "id")
    protected int id;
    @XmlAttribute(name = "level")
    protected int level;
    @XmlAttribute(name = "exp")
    protected int exp;
    @XmlAttribute(name = "grade")
    protected CollectionType grade;
    
    public ModifiersTemplate getModifiers() {
        return modifiers;
    }
    
    public int getId() {
        return id;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getExp() {
        return exp;
    }
    
    public CollectionType getGrade() {
        return grade;
    }
}
