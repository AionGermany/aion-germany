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
package com.aionemu.gameserver.model.templates.quest;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author antness
 */
@XmlType(name = "QuestCategory")
@XmlEnum
public enum QuestCategory {

	QUEST(0),
	EVENT(1),
	MISSION(0),
	SIGNIFICANT(0),
	IMPORTANT(0),
	NON_COUNT(0),
	SEEN_MARKER(0),
	TASK(0),
	FACTION(0),
	CHALLENGE_TASK(0),
	PUBLIC(0),
	LEGION(0),
	PRIMARY(0);

	private int id;

	private QuestCategory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
