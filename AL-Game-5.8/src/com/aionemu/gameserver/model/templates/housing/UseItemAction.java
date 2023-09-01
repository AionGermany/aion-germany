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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UseItemAction")
public class UseItemAction {

	@XmlAttribute(name = "final_reward_id")
	protected Integer finalRewardId;
	@XmlAttribute(name = "reward_id")
	protected Integer rewardId;
	@XmlAttribute(name = "remove_count")
	protected Integer removeCount;
	@XmlAttribute(name = "check_type")
	protected Integer checkType;

	public Integer getFinalRewardId() {
		return finalRewardId;
	}

	public Integer getRewardId() {
		return rewardId;
	}

	public Integer getRemoveCount() {
		return removeCount;
	}

	public Integer getCheckType() {
		return checkType;
	}
}
