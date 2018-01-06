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
package com.aionemu.gameserver.model.team2.group;

import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerFilters {

	public static final Predicate<Player> ONLINE = new Predicate<Player>() {

		@Override
		public boolean apply(Player member) {
			return member.isOnline();
		}
	};

	public static final class MentorSuiteFilter implements Predicate<Player> {

		private final Player player;

		public MentorSuiteFilter(Player player) {
			this.player = player;
		}

		@Override
		public boolean apply(Player member) {
			return member.getLevel() + 9 < player.getLevel();
		}
	}

	public static final class SameInstanceFilter implements Predicate<Player> {

		private final Player player;

		public SameInstanceFilter(Player player) {
			this.player = player;
		}

		@Override
		public boolean apply(Player member) {
			return member.getInstanceId() == player.getInstanceId();
		}
	}

	public static final Predicate<Player> HAS_LOOT_PET = new Predicate<Player>() {

		@Override
		public boolean apply(Player member) {
			Pet pet = member.getPet();
			if (pet == null) {
				return false;
			}
			return pet.getPetTemplate().getPetFunction(PetFunctionType.LOOT) != null;
		}
	};

	public static final class ExcludePlayerFilter implements Predicate<Player> {

		private final Player player;

		public ExcludePlayerFilter(Player player) {
			this.player = player;
		}

		@Override
		public boolean apply(Player member) {
			return !player.getObjectId().equals(member.getObjectId());
		}
	}
}
