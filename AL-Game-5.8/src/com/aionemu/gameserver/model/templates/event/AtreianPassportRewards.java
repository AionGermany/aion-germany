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
package com.aionemu.gameserver.model.templates.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "atreian_passport_reward")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtreianPassportRewards {

	@XmlAttribute(name = "name")
	private String name = "";
	@XmlAttribute(name = "reward_item", required = true)
	private int rewardItem;
	@XmlAttribute(name = "reward_item_count", required = true)
	private int rewardItemCount;
	@XmlAttribute(name = "reward_item_num", required = true)
	private int rewardItemNum;
	@XmlAttribute(name = "reward_permit_level")
	private int rewardPermitLevel;

	public String getName() {
		return name;
	}

	public int getRewardItem() {
		return rewardItem;
	}

	public int getRewardItemCount() {
		return rewardItemCount;
	}

	public int getRewardItemNum() {
		return rewardItemNum;
	}

	public int getRewardPermitLevel() {
		return rewardPermitLevel;
	}
}
