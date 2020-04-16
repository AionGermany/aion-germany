/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.atreianpassport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AtreianPassportRewards")
public class AtreianPassportRewards {

	@XmlAttribute(name = "reward_item_num", required = true)
	protected int rewardItemNum;

	@XmlAttribute(name = "reward_item_count", required = true)
	protected int rewardItemCount;

	@XmlAttribute(name = "reward_item", required = true)
	protected int rewardItemId;

	@XmlAttribute(name = "name")
	protected String name;

	public int getRewardItemNum() {
		return rewardItemNum;
	}

	public int getRewardItemCount() {
		return rewardItemCount;
	}

	public int getRewardItemId() {
		return rewardItemId;
	}

	public String getName() {
		return name;
	}
}
