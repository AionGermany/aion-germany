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
package com.aionemu.gameserver.model;

/**
 * @author Rolandas Some emotions are for NPCs (like ANGRY, THANK, THINK, SURPRISE)
 */
public enum EmotionId {

	NONE(0),
	LAUGH(1), // Q1337 (1009), Q1665 (1009), Q2127 (1012), Q2307 (10002),
	// Q2344 (1009), Q2352 (1009), Q2655 (1009), Q4318 (1009)

	ANGRY(2), // Q1307 (1012), Q1372 (1003), Q1376 (1012), Q1501 (1012), Q1609 (1009),
	// Q1621 (1012), Q1672 (1012), Q2305 (1003), Q2401 (1003), Q11014 (1012)

	SAD(3), // Q1345 (1013,1008), Q1364 (1009), Q1468 (1009), Q1474 (1007), Q1491 (1012),
	// Q1634 (4763,1694,2035,1009), Q2113 (1012), Q2239 (1012), Q2375 (1012), Q2535 (1007)

	POINT(5), // Q1041 (3059), Q2919 (10005)

	YES(6), // Q1004 (1012), Q1346 (1009), Q2124 (1012)

	NO(7), // Q1471 (1694), Q1604 (1009), Q2430 (2376), Q3088 (2376),
	// Q80020 (1694), Q80021 (1694), Q80070 (1694), Q80071 (1694)

	VICTORY(8), // Q1311 (1009)

	CLAP(11), // Q1305 (1009), Q1306 (1009), Q1340 (1009), Q1601 (1009),
	// Q2220 (1009), Q2231 (1009), Q2326 (1009), Q2738 (1009)

	SIGH(12), // Q1361 (1012), Q1469 (1009), Q1541 (1012), Q2002 (1012), Q2007 (1012), Q2109 (1007),
	// Q2232 (1012), Q2310 (1009), Q2389 (1012), Q4329 (1009), Q4330 (1009)

	SURPRISE(13), // Q1016 (2718), Q1336 (1009), Q1355 (1009), Q1416 (1008), Q1466 (1009), Q1469 (1353),
	// Q1528 (1009), Q1643 (1009), Q2008 (1013), Q2040 (1009), Q2055 (1694), Q2239 (1353),
	// Q2409 (1353), Q2422 (1353), Q2426 (1009), Q2515 (1353)

	COMFORT(14), // Q1126 (1002), Q2911 (10000,10001)

	THANK(15), // Q1302 (1002), Q1361 (1009), Q2228 (1009), Q11465 (1009),
	// Q11466 (1009), Q21465 (1009), Q28708 (1009), Q29708 (1009)

	BEG(16), // Q2135 (1012), Q2239 (1007)

	BLUSH(17), // Q2107 (1353)

	SMILE(28), // Q1464 (4763)

	SALUTE(29), // Q2036 (34), Q2201 (1009)

	PANIC(30), // Q1471 (2035), Q2053 (3058), Q80020 (2035), Q80021 (2035), Q80070 (2035), Q80071 (2035)

	SORRY(31), // Q1430 (1009)

	THINK(33), // Q1309 (1353), Q1363 (1007), Q1586 (1009), Q2002 (1013), Q2007 (1353), Q2137 (1012), Q2325 (1012),
	// Q2692 (1353), Q80008 (1009), Q80009 (1009), Q80135 (1009), Q80137 (1009), Q80139 (1009),
	// Q80141 (1009), Q80143 (1009), Q80145, Q80147 (1009), Q80149 (1009)

	DISLIKE(34), // Q2323 (1012), Q2676 (1007)

	STAND(128), // All action NPCs having animation quest_actstanding
	CASH_GOOD_DAY_FULL(133),
	CASH_U_AND_ME_FULL(134);

	private int id;

	private EmotionId(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}
}
