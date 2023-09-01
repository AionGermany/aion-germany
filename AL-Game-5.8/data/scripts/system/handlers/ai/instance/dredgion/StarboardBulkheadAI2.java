/*
 * This file is part of Aion-Finish <Aion-Finish.org>
 *
 * Aion-Finish is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Finish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Finish. If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.dredgion;

import com.aionemu.gameserver.ai2.AIName;

import ai.OneDmgPerHitAI2;

/**
 * @author Dr.Nism
 */
@AIName("starboard_bulkhead")
public class StarboardBulkheadAI2 extends OneDmgPerHitAI2 {

	@Override
	protected void handleDied() {
		super.handleDied();
		getOwner().getController().onDespawn();
	}
}
