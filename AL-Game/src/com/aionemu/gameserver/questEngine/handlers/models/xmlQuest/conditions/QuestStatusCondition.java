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
package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.conditions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStatusCondition")
public class QuestStatusCondition extends QuestCondition {

	@XmlAttribute(required = true)
	protected QuestStatus value;
	@XmlAttribute(name = "quest_id")
	protected Integer questId;

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.gameserver.questEngine.handlers.template.xmlQuest.condition.QuestCondition#doCheck(com.aionemu.gameserver .questEngine.model.QuestEnv)
	 */
	@Override
	public boolean doCheck(QuestEnv env) {
		Player player = env.getPlayer();
		int qstatus = 0;
		int id = env.getQuestId();
		if (questId != null) {
			id = questId;
		}
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if (qs != null) {
			qstatus = qs.getStatus().value();
		}

		switch (getOp()) {
			case EQUAL:
				return qstatus == value.value();
			case GREATER:
				return qstatus > value.value();
			case GREATER_EQUAL:
				return qstatus >= value.value();
			case LESSER:
				return qstatus < value.value();
			case LESSER_EQUAL:
				return qstatus <= value.value();
			case NOT_EQUAL:
				return qstatus != value.value();
			default:
				return false;
		}
	}
}
