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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.configs.main.GSConfig;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalReq")
public class PortalReq {

	@XmlElement(name = "quest_req")
	protected List<QuestReq> questReq;
	@XmlElement(name = "item_req")
	protected List<ItemReq> itemReq;
	@XmlAttribute(name = "min_level")
	protected int minLevel;
	@XmlAttribute(name = "max_level")
	protected int maxLevel = GSConfig.PLAYER_MAX_LEVEL;
	@XmlAttribute(name = "kinah_req")
	protected int kinahReq;
	@XmlAttribute(name = "title_id")
	protected int titleId;
	@XmlAttribute(name = "err_level")
	protected int errLevel;
	@XmlAttribute(name = "legion_req")
	protected boolean legionReq;

	public List<QuestReq> getQuestReq() {
		return this.questReq;
	}

	public List<ItemReq> getItemReq() {
		return this.itemReq;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int value) {
		this.minLevel = value;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int value) {
		this.maxLevel = value;
	}

	public int getKinahReq() {
		return kinahReq;
	}

	public void setKinahReq(int value) {
		this.kinahReq = value;
	}

	public int getTitleId() {
		return titleId;
	}

	public int getErrLevel() {
		return errLevel;
	}

	public boolean getLegionReq() {
		return legionReq;
	}
}
