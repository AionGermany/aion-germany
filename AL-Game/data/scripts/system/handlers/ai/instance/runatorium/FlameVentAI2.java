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
package ai.instance.runatorium;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.ActionItemNpcAI2;
import javolution.util.FastList;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("flame_vent")
// 802548, 802549
public class FlameVentAI2 extends ActionItemNpcAI2 {

	private final FastList<Future<?>> ConcurrentTask = FastList.newInstance();
	private final int FlameLeversSpawnTime = 2 * 60 * 1000;// Every 2-minutes flame levers will be spawned (NA Retail v4.7.5.17)

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getRace() == getOwner().getRace()) {
			super.handleDialogStart(player);
		}
	}

	@Override
	protected void handleUseItemFinish(Player player) {

		int npcId = getOwner().getNpcId();
		switch (npcId) {
			case 802192: // Flame Vent (Elyos).
				analyzeOpening(player);
				sp(702405, 232.19067f, 185.85762f, 80.2f, (byte) 75, 0);// Enchanted Book (IDLDF5_Fortress_Re_Fire_Fxmon_02)
				sp(702405, 238.54938f, 200.84813f, 80.0f, (byte) 15, 0);// Enchanted Book (IDLDF5_Fortress_Re_Fire_Fxmon_02)
				sp(702387, 198.19942f, 190.41037f, 85.757484f, (byte) 13, 143, 0, 0, null);// Enchanted Book (IDLDF5_Fortress_Re_SetFOBJ_01)
				sp(855010, 234.4599f, 194.21619f, 79.23065f, (byte) 0, 0);// Repelling Flame Cannon
				/* The Asmodian Flame Vent has been activated.\nThe Asmodians are trapped! */
				sendMsgByRace(1402368, Race.PC_ALL, 0);
				break;
			case 802549: // Flame Vent (Asmo)
				analyzeOpening(player);
				sp(702405, 290.67102f, 317.26324f, 80.1f, (byte) 75, 0);// Enchanted Book (IDLDF5_Fortress_Re_Fire_Fxmon_02)
				sp(702405, 297.08356f, 332.35382f, 80.1f, (byte) 15, 0);// Enchanted Book (IDLDF5_Fortress_Re_Fire_Fxmon_02)
				sp(702388, 331.23566f, 327.82135f, 85.44032f, (byte) 13, 14, 0, 0, null);// Enchanted Book (IDLDF5_Fortress_Re_SetFOBJ_02)
				sp(855010, 294.62436f, 324.11783f, 79.230644f, (byte) 0, 0);// Repelling Flame Cannon
				/* The Elyos Flame Vent has been activated.\nThe Elyos are trapped! */
				sendMsgByRace(1402369, Race.PC_ALL, 0);
				break;
		}
	}

	private void analyzeOpening(Player player) {
		if (getOwner().isInState(CreatureState.DEAD)) {
			AuditLogger.info(player, "Attempted multiple activating Flame Vent lever on Idgel Dome Instance!");
			return;
		}

		/* Kills the Flame Vent levers and Register to be spawned in 2-minutes */
		AI2Actions.dieSilently(this, player);

		ConcurrentTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (getOwner().getRace() == Race.ELYOS) {
					sp(802192, 199.1874f, 191.76154f, 80.602165f, (byte) 15, 0);// ...Flame Vent Elyos
				}
				else if (getOwner().getRace() == Race.ASMODIANS) {
					sp(802549, 329.79938f, 326.1139f, 80.60217f, (byte) 75, 0);// .....Flame Vent Asmodian
				}

			}
		}, FlameLeversSpawnTime));
	}

	protected void sendMsgByRace(final int msgid, final Race race, int time) {
		ConcurrentTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getOwner().getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msgid));
						}
					}
				});
			}
		}, time));
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
		ConcurrentTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(npcId, x, y, z, h, entityId);
				if (msg > 0) {
					sendMsgByRace(msg, race, 0);
				}
			}
		}, time));
	}
}
