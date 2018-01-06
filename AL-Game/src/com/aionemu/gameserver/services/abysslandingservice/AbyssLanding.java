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
package com.aionemu.gameserver.services.abysslandingservice;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AbyssLandingDAO;
import com.aionemu.gameserver.model.landing.LandingLocation;
import com.aionemu.gameserver.model.landing.LandingStateType;

public class AbyssLanding extends Landing<LandingLocation> {

	public AbyssLanding(LandingLocation landing) {
		super(landing);
	}

	@Override
	public void startLanding(int level) {
		getLandingLocation().setActiveLanding(this);
		if (!getLandingLocation().getSpawned().isEmpty()) {
			despawn();
		}
		switch (level) {
			case 1:
				spawn(LandingStateType.LVL1);
				break;
			case 2:
				spawn(LandingStateType.LVL2);
				break;
			case 3:
				spawn(LandingStateType.LVL3);
				break;
			case 4:
				spawn(LandingStateType.LVL4);
				break;
			case 5:
				spawn(LandingStateType.LVL5);
				break;
			case 6:
				spawn(LandingStateType.LVL6);
				break;
			case 7:
				spawn(LandingStateType.LVL7);
				break;
			case 8:
				spawn(LandingStateType.LVL8);
				break;
			default:
				spawn(LandingStateType.LVL1);
				break;
		}
	}

	@Override
	public void saveLanding() {
		DAOManager.getDAO(AbyssLandingDAO.class).updateLocation(getLandingLocation());
	}

	@Override
	public void stopLanding() {
		getLandingLocation().setActiveLanding(null);
		despawn();
		spawn(LandingStateType.NONE);
	}
}
