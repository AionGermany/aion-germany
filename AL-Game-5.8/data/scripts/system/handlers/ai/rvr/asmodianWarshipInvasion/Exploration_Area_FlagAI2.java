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
package ai.rvr.asmodianWarshipInvasion;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.ActionItemNpcAI2;

@AIName("battlefield_trigger_asmodians") // 805854, 805856, 805858, 805860, 805924, 805926, 805928, 805930, 805932, 805934, 805938, 805936
public class Exploration_Area_FlagAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 805854: // DF6_A1_BattleField_1_1
				announceF6Invasion();
				explorationAreaStart1_1();
				spawn(805855, 1279.7623f, 2054.3877f, 182.72609f, (byte) 0, 215);
				break;
			case 805924: // DF6_A1_BattleField_1_2
				announceF6Invasion();
				explorationAreaStart1_2();
				spawn(805925, 1265.1541f, 2072.4131f, 184.76331f, (byte) 0, 211);
				break;
			case 805932: // DF6_A1_BattleField_1_3
				announceF6Invasion();
				explorationAreaStart1_3();
				spawn(805933, 1291.6405f, 2098.4895f, 185.14610f, (byte) 0, 131);
				break;
			case 805856: // DF6_A1_BattleField_2_1
				announceF6Invasion();
				explorationAreaStart2_1();
				spawn(805857, 1767.1818f, 2499.2625f, 212.46266f, (byte) 0, 881);
				break;
			case 805926: // DF6_A1_BattleField_2_2
				announceF6Invasion();
				explorationAreaStart2_2();
				spawn(805927, 1789.3649f, 2473.3726f, 213.40018f, (byte) 0, 860);
				break;
			case 805934: // DF6_A1_BattleField_2_3
				announceF6Invasion();
				explorationAreaStart2_3();
				spawn(805935, 1764.2432f, 2447.4143f, 213.45287f, (byte) 0, 874);
				break;
			case 805858: // DF6_A1_BattleField_3_1
				announceF6Invasion();
				explorationAreaStart3_1();
				spawn(805859, 1525.2533f, 1594.7480f, 202.64267f, (byte) 0, 1143);
				break;
			case 805928: // DF6_A1_BattleField_3_2
				announceF6Invasion();
				explorationAreaStart3_2();
				spawn(805929, 1550.8925f, 1570.4790f, 201.36314f, (byte) 0, 1145);
				break;
			case 805936: // DF6_A1_BattleField_3_3
				announceF6Invasion();
				explorationAreaStart3_3();
				spawn(805937, 1553.7285f, 1615.7953f, 203.65607f, (byte) 0, 890);
				break;
			case 805860: // DF6_A1_BattleField_4_1
				announceF6Invasion();
				explorationAreaStart4_1();
				spawn(805861, 1971.6879f, 1701.5045f, 224.99850f, (byte) 0, 220);
				break;
			case 805930: // DF6_A1_BattleField_4_2
				announceF6Invasion();
				explorationAreaStart4_2();
				spawn(805931, 2026.8665f, 1712.7850f, 227.54637f, (byte) 0, 107);
				break;
			case 805938: // DF6_A1_BattleField_4_3
				announceF6Invasion();
				explorationAreaStart4_3();
				spawn(805939, 1962.8475f, 1737.5066f, 224.86259f, (byte) 0, 216);
				break;
		}
		AI2Actions.deleteOwner(this);
	}

	private void announceF6Invasion() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Vanguard Commanders are preparing exploration area defenses. It will be established in 10 minutes
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_01, 0);
				// Strike Commanders are preparing exploration area defenses. It will be established in 5 minutes
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_02, 300000);
				// Strike Commanders are preparing exploration area defenses. It will be established in one minutes
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_03, 540000);
				// Strike Commissioned Officers are defending the exploration area
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Invasion_3rd_Bonus_04, 600000);
			}
		});
	}

	private void explorationAreaStart1_1() {
		// spawn(240672,
		// spawn(240673,
		// spawn(240675,
	}

	private void explorationAreaStart1_2() {

	}

	private void explorationAreaStart1_3() {

	}

	private void explorationAreaStart2_1() {

	}

	private void explorationAreaStart2_2() {

	}

	private void explorationAreaStart2_3() {

	}

	private void explorationAreaStart3_1() {

	}

	private void explorationAreaStart3_2() {

	}

	private void explorationAreaStart3_3() {

	}

	private void explorationAreaStart4_1() {

	}

	private void explorationAreaStart4_2() {

	}

	private void explorationAreaStart4_3() {

	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
