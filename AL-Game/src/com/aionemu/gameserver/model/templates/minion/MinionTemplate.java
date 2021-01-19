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

import com.aionemu.gameserver.model.templates.stats.*;
import javax.xml.bind.annotation.*;
import com.aionemu.gameserver.model.templates.*;
import java.util.*;
import com.aionemu.gameserver.model.stats.calc.functions.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "", name = "MinionTemplate")
public class MinionTemplate {

	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "nameid")
	private int name_id;
	@XmlAttribute(name = "grade")
	private String grade;
	@XmlAttribute(name = "grade_id")
	private int gradeId;
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
	@XmlElement(name = "nameId")
	private int nameId;
	@XmlElement(name = "physical_attr")
	protected List<MinionAttr> physicalAttr;
	@XmlElement(name = "magical_attr")
	protected List<MinionAttr> magicalAttr;

	public List<MinionAttr> getPhysicalAttr() {
		if (physicalAttr == null) {
			physicalAttr = new ArrayList<MinionAttr>();
		}
		return physicalAttr;
	}

	public List<MinionAttr> getMagicalAttr() {
		if (magicalAttr == null) {
			magicalAttr = new ArrayList<MinionAttr>();
		}
		return magicalAttr;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getGrade() {
		return grade;
	}

	public int getLevel() {
		return level;
	}

	public int getGrowthPoints() {
		return growthPoints;
	}

	public int getGrowthMax() {
		return growthMax;
	}

	public int getGrowthCost() {
		return growthCost;
	}

	public BoundRadius getBoundRadius() {
		return bound;
	}

	public MinionEvolved getEvolved() {
		return evolved;
	}

	public MinionStatsTemplate getStatsTemplate() {
		return statsTemplate;
	}

	public int getNameId() {
		return nameId;
	}

	public int getGradeId() {
		return gradeId;
	}

	public List<StatFunction> getModifiers() {
		if (modifiers != null) {
			return modifiers.getModifiers();
		}
		return null;
	}

	public MinionActions getAction() {
		return actions;
	}
}
