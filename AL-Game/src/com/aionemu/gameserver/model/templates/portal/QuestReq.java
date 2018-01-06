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
package com.aionemu.gameserver.model.templates.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestReq")
public class QuestReq {

	@XmlAttribute(name = "quest_id")
	protected int questId;
	@XmlAttribute(name = "quest_step")
	protected int questStep;
	@XmlAttribute(name = "err_quest")
	protected int errQuest;

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int value) {
		this.questId = value;
	}

	public int getQuestStep() {
		return questStep;
	}

	public void setQuestStep(int value) {
		this.questStep = value;
	}

	public int getErrQuest() {
		return errQuest;
	}
}
