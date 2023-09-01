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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.MinionController;
import com.aionemu.gameserver.controllers.movement.MinionMoveController;
import com.aionemu.gameserver.controllers.movement.MoveController;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Falke_34
 */
public class Minion extends VisibleObject {

	private final Player master;
	private MoveController moveController;
	private final MinionTemplate minionTemplate;

	public Minion(MinionTemplate minionTemplate, MinionController controller, MinionCommonData commonData, Player master) {
		super(commonData.getObjectId(), controller, null, commonData, new WorldPosition(master.getWorldId()));
		controller.setOwner(this);
		this.master = master;
		this.minionTemplate = minionTemplate;
		this.moveController = new MinionMoveController();
	}

	public Player getMaster() {
		return master;
	}

	public int getMinionId() {
		return objectTemplate.getTemplateId();
	}

	@Override
	public String getName() {
		return objectTemplate.getName();
	}

	public final MinionCommonData getCommonData() {
		return (MinionCommonData) objectTemplate;
	}

	public final MoveController getMoveController() {
		return moveController;
	}

	public final MinionTemplate getMinionTemplate() {
		return minionTemplate;
	}
}