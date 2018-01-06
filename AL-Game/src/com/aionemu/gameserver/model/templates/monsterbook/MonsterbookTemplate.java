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
package com.aionemu.gameserver.model.templates.monsterbook;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterbookTemplate")
public class MonsterbookTemplate {

	@XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "level")
	private int level;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "name_id")
	private int name_id;

	@XmlAttribute(name = "npc_ids", required = true)
	private List<Integer> npc_ids;

	@XmlAttribute(name = "type")
	private BookType type;

	@XmlElement(name = "achievement")
	private List<MonsterbookAchievementTemplate> achievement;

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public BookType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getNameId() {
		return name_id;
	}

	public List<Integer> getNpcIds() {
		if (npc_ids == null) {
			npc_ids = new ArrayList<Integer>();
		}
		return npc_ids;
	}

	public List<MonsterbookAchievementTemplate> getMonsterbookAchievementTemplate() {
		return achievement;
	}

	@XmlType(name = "BookType")
	@XmlEnum
	public enum BookType {
		NORMAL, HERO;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "MonsterbookAchievementTemplate")
	public static class MonsterbookAchievementTemplate {
		@XmlAttribute(name = "condition")
		private int condition;
		@XmlAttribute(name = "exp")
		private int exp;

		public int getKillCondition() {
			return condition;
		}

		public int getRewardExp() {
			return exp;
		}
	}
}
