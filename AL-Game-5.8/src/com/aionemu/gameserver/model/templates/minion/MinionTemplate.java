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
package com.aionemu.gameserver.model.templates.minion;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MinionTemplate")
public class MinionTemplate {

	@XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "nameid")
	private int name_id;

    @XmlAttribute(name = "grade")
    private String grade;

    @XmlAttribute(name = "level")
    private int level;

	@XmlAttribute(name = "growthPoints")
	private int growthPoints;

	@XmlAttribute(name = "growthMax")
	private int growthMax;

	@XmlAttribute(name = "growthCost")
	private int growthCost;

    @XmlElement(name = "modifiers", required = false)
    private ModifiersTemplate modifiers;

    @XmlElement(name = "actions")
    private MinionActions actions;

    @XmlElement(name = "minionstats")
    private MinionStatsTemplate statsTemplate;

    @XmlElement(name = "bound")
    private BoundRadius bound;

    @XmlElement(name = "evolved")
    private MinionEvolved evolved;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

    public String getGrade() {
        return this.grade;
    }

    public int getLevel() {
        return this.level;
    }

	public int getGrowthPt() {
		return growthPoints;
	}

	public int getMaxGrowthValue() {
		return growthMax;
	}

	public int getGrowthCost() {
		return growthCost;
	}

    public List<StatFunction> getModifiers() {
        if (this.modifiers != null) {
            return this.modifiers.getModifiers();
        }
        return null;
    }

    public MinionActions getAction() {
        return this.actions;
    }

    public MinionStatsTemplate getStatsTemplate() {
        return this.statsTemplate;
    }

    public BoundRadius getBoundRadius() {
        return this.bound;
    }

    public MinionEvolved getEvolved() {
        return this.evolved;
    }
}
