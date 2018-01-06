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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Rolandas
 */
public class AdoptPetAction extends AbstractItemAction {

	@XmlAttribute(name = "petId")
	private int petId;
	@XmlAttribute(name = "minutes")
	private int expireMinutes;
	@XmlAttribute(name = "sidekick")
	private Boolean isSideKick = false;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
	}

	public int getPetId() {
		return petId;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	public Boolean isSideKick() {
		return isSideKick;
	}
}
