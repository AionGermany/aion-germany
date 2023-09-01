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
package com.aionemu.gameserver.model.templates.luna;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "luna_consume_reward")
@XmlAccessorType(XmlAccessType.NONE)
public class LunaConsumeRewardsTemplate {

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "luna_sum_count", required = true)
	protected int luna_sum_count;

	@XmlAttribute(name = "gacha_cost")
	protected int gacha_cost;

	@XmlAttribute(name = "create_1")
	protected int create_1;

	@XmlAttribute(name = "num_1")
	protected int num_1;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public int getSumCount() {
		return luna_sum_count;
	}

	public int getGachaCost() {
		return gacha_cost;
	}

	public int getCreateItemId() {
		return create_1;
	}

	public int getCreateItemCount() {
		return num_1;
	}
}
