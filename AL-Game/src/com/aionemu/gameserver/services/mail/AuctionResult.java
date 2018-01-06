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
package com.aionemu.gameserver.services.mail;

/**
 * @author Rolandas
 */
public enum AuctionResult {

	FAILED_BID(0),
	CANCELED_BID(1),
	FAILED_SALE(2),
	SUCCESS_SALE(3),
	WIN_BID(4),
	GRACE_START(5),
	GRACE_FAIL(6),
	GRACE_SUCCESS(7);

	private int value;

	AuctionResult(int value) {
		this.value = value;
	}

	public int getId() {
		return this.value;
	}

	public static AuctionResult getResultFromId(int resultId) {
		for (AuctionResult result : AuctionResult.values()) {
			if (result.getId() == resultId) {
				return result;
			}
		}
		return null;
	}
}
