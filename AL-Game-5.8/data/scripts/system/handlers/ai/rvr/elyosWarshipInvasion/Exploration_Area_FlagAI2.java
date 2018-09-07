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
package ai.rvr.elyosWarshipInvasion;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.ActionItemNpcAI2;

@AIName("battlefield_trigger_elyos") // 805889, 805891, 805893, 805895, 805940, 805942, 805944, 805946, 805948, 805950, 805952, 805954
public class Exploration_Area_FlagAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			/**
			 * Othia Fragment Redoubt
			 */
			case 805889: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart1_1();
				spawn(805890, 1308.3436f, 1031.3064f, 320.10107f, (byte) 0, 625);
				break;
			case 805940: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart1_2();
				spawn(805941, 1256.1188f, 980.66156f, 322.22174f, (byte) 0, 500);
				break;
			case 805948: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart1_3();
				spawn(805949, 1264.2919f, 1046.5237f, 323.42804f, (byte) 0, 579);
				break;
			/**
			 * Candellus Fragment Redoubt
			 */
			case 805891: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart2_1();
				spawn(805892, 1706.7684f, 875.67474f, 305.81323f, (byte) 0, 1846);
				break;
			case 805942: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart2_2();
				spawn(805943, 1736.994f, 792.06250f, 309.298130f, (byte) 0, 2276);
				break;
			case 805950: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart2_3();
				spawn(805951, 1755.5957f, 872.9082f, 307.162750f, (byte) 0, 494);
				break;
			/**
			 * Philos Redoubt
			 */
			case 805893: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart3_1();
				spawn(805894, 1477.2379f, 1783.2937f, 315.10504f, (byte) 0, 696);
				break;
			case 805944: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart3_2();
				spawn(805945, 1533.2758f, 1782.7814f, 313.42178f, (byte) 0, 353);
				break;
			case 805952: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart3_3();
				spawn(805953, 1542.8401f, 1835.7413f, 316.32877f, (byte) 0, 47);
				break;
			/**
			 * Anemos Fragment Redoubt
			 */
			case 805895: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart4_1();
				spawn(805896, 1887.7408f, 1454.496f, 291.563420f, (byte) 0, 425);
				break;
			case 805946: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart4_2();
				spawn(805947, 1934.3413f, 1449.7533f, 291.42267f, (byte) 0, 427);
				break;
			case 805954: // Exploration Area Flag.
				announceF6Invasion();
				explorationAreaStart4_3();
				spawn(805955, 1913.5474f, 1388.1427f, 295.92883f, (byte) 0, 466);
				break;
		}
		AI2Actions.deleteOwner(this);
	}

	private void announceF6Invasion() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Vanguard Commanders are preparing exploration area defenses. It will be established in 10 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_01, 0);
				// Strike Commanders are preparing exploration area defenses. It will be established in 5 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_02, 300000);
				// Strike Commanders are preparing exploration area defenses. It will be established in one minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_03, 540000);
				// Strike Commissioned Officers are defending the exploration area.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_04, 600000);
			}
		});
	}

	/**
	 * Othia Fragment Redoubt
	 */
	private void explorationAreaStart1_1() {
		spawn(240579, 1306.3539f, 1028.4309f, 319.90192f, (byte) 73);
		spawn(240580, 1303.2139f, 1032.4003f, 320.0f, (byte) 72);
		spawn(240582, 1303.6835f, 1029.6238f, 319.875f, (byte) 72);
	}

	private void explorationAreaStart1_2() {
		spawn(240579, 1256.9261f, 983.4426f, 322.21515f, (byte) 13);
		spawn(240580, 1259.6881f, 979.1361f, 322.125f, (byte) 11);
		spawn(240582, 1260.0139f, 982.2795f, 322.12415f, (byte) 11);
	}

	private void explorationAreaStart1_3() {
		spawn(240579, 1267.1093f, 1046.0477f, 323.378f, (byte) 101);
		spawn(240580, 1262.8469f, 1042.9991f, 323.375f, (byte) 101);
		spawn(240582, 1266.0925f, 1042.848f, 323.375f, (byte) 101);
	}

	/**
	 * Candellus Fragment Redoubt
	 */
	private void explorationAreaStart2_1() {
		spawn(240579, 1709.2223f, 875.2744f, 305.75f, (byte) 104);
		spawn(240580, 1705.511f, 871.7817f, 305.75f, (byte) 104);
		spawn(240582, 1708.9626f, 871.6489f, 305.75f, (byte) 104);
	}

	private void explorationAreaStart2_2() {
		spawn(240579, 1734.1124f, 795.29175f, 309.25f, (byte) 33);
		spawn(240580, 1739.11f, 796.21246f, 309.25f, (byte) 33);
		spawn(240582, 1736.1868f, 797.2706f, 309.25f, (byte) 33);
	}

	private void explorationAreaStart2_3() {
		spawn(240579, 1755.028f, 870.1109f, 307.125f, (byte) 79);
		spawn(240580, 1750.5299f, 873.45874f, 307.125f, (byte) 77);
		spawn(240582, 1751.6447f, 870.323f, 307.125f, (byte) 77);
	}

	/**
	 * Philos Redoubt
	 */
	private void explorationAreaStart3_1() {
		spawn(240579, 1478.3773f, 1786.3193f, 315.16492f, (byte) 12);
		spawn(240580, 1481.5193f, 1781.7296f, 315.125f, (byte) 11);
		spawn(240582, 1481.911f, 1785.4368f, 315.125f, (byte) 11);
	}

	private void explorationAreaStart3_2() {
		spawn(240579, 1530.3748f, 1783.4668f, 313.375f, (byte) 36);
		spawn(240580, 1535.2054f, 1785.2559f, 313.375f, (byte) 36);
		spawn(240582, 1532.2056f, 1786.3596f, 313.375f, (byte) 36);
	}

	private void explorationAreaStart3_3() {
		spawn(240579, 1543.5227f, 1832.787f, 316.31534f, (byte) 77);
		spawn(240580, 1539.5873f, 1835.9158f, 316.48947f, (byte) 77);
		spawn(240582, 1540.3894f, 1833.0126f, 316.25f, (byte) 77);
	}

	/**
	 * Anemos Fragment Redoubt
	 */
	private void explorationAreaStart4_1() {
		spawn(240579, 1890.5361f, 1453.3826f, 291.5f, (byte) 100);
		spawn(240580, 1886.4353f, 1450.7627f, 291.5f, (byte) 100);
		spawn(240582, 1889.5486f, 1450.518f, 291.5f, (byte) 100);
	}

	private void explorationAreaStart4_2() {
		spawn(240579, 1934.3427f, 1447.1375f, 291.25f, (byte) 73);
		spawn(240580, 1930.9955f, 1450.9803f, 291.375f, (byte) 73);
		spawn(240582, 1931.2223f, 1447.8662f, 291.375f, (byte) 73);
	}

	private void explorationAreaStart4_3() {
		spawn(240579, 1912.2007f, 1386.4188f, 295.875f, (byte) 67);
		spawn(240580, 1910.654f, 1390.4042f, 295.875f, (byte) 67);
		spawn(240582, 1909.7693f, 1387.8267f, 295.875f, (byte) 67);
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
