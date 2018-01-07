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
package instance.dredgion2;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@InstanceID(300210000)
public class ChantraDredgionInstance2 extends DredgionInstance2 {

	@Override
	public void onEnterInstance(Player player) {
		if (isInstanceStarted.compareAndSet(false, true)) {
			sp(730311, 554.83081f, 173.87158f, 432.52448f, (byte) 0, 9, 720000);
			sp(730312, 397.11661f, 184.29782f, 432.80328f, (byte) 0, 42, 720000);
			if (Rnd.get(1, 100) < 21) {
				sp(216889, 484.1199f, 314.08817f, 403.7213f, (byte) 5, 720000);
			}
			if (Rnd.get(1, 100) < 21) {
				sp(216890, 499.52f, 598.67f, 390.49f, (byte) 59, 720000);
			}
			if (Rnd.get(1, 100) < 21) {
				spawn(216887, 486.26382f, 909.48175f, 405.24463f, (byte) 90);
			}
			if (Rnd.get(1, 100) < 51) {
				switch (Rnd.get(2)) {
					case 0:
						spawn(216888, 416.3429f, 282.32785f, 409.7311f, (byte) 80);
						break;
					default:
						spawn(216888, 552.07446f, 289.058f, 409.7311f, (byte) 80);
						break;
				}
			}

			int spawnTime = Rnd.get(10, 15) * 60 * 1000 + 120000;
			sendMsgByRace(1400633, Race.PC_ALL, spawnTime);
			sp(216941, 485.99f, 299.23f, 402.57f, (byte) 30, spawnTime);
			startInstanceTask();
		}
		super.onEnterInstance(player);
	}

	private void onDieSurkan(Npc npc, Player mostPlayerDamage, int points) {
		Race race = mostPlayerDamage.getRace();
		captureRoom(race, npc.getNpcId() + 14 - 700851);
		for (Player player : instance.getPlayersInside()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400199, new DescriptionId(race.equals(Race.ASMODIANS) ? 1800483 : 1800481), new DescriptionId(npc.getObjectTemplate().getNameId() * 2 + 1)));
		}
		getPlayerReward(mostPlayerDamage).captureZone();
		if (++surkanKills == 5) {
			spawn(216886, 485.33f, 832.26f, 416.64f, (byte) 55);
			sendMsgByRace(1400632, Race.PC_ALL, 0);
		}
		updateScore(mostPlayerDamage, npc, points, false);
		CreatureActions.delete(npc);
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 730350: // Secondary Hatch teleporter
				sendMsgByRace(1400641, Race.PC_ALL, 0);
				spawn(730315, 415.07663f, 173.85265f, 432.53436f, (byte) 0, 34);
				CreatureActions.delete(npc);
				return;
			case 730349: // Escape Hatch teleporter
				sendMsgByRace(1400631, Race.PC_ALL, 0);
				spawn(730314, 396.979f, 184.392f, 433.940f, (byte) 0, 42);
				CreatureActions.delete(npc);
				return;
			case 730351:
				sendMsgByRace(1400226, Race.PC_ALL, 0);
				spawn(730345, 448.391998f, 493.641998f, 394.131989f, (byte) 90, 12);
				CreatureActions.delete(npc);
				return;
			case 730352:
				sendMsgByRace(1400227, Race.PC_ALL, 0);
				spawn(730346, 520.875977f, 493.401001f, 394.433014f, (byte) 90, 133);
				CreatureActions.delete(npc);
				return;
			case 216890:
			case 216889:
				return;
		}
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		Race race = mostPlayerDamage.getRace();
		switch (npc.getNpcId()) {
			case 700838:
			case 700839:
				onDieSurkan(npc, mostPlayerDamage, 400);
				return;
			case 700840:
			case 700848:
			case 700849:
			case 700850:
			case 700851:
				onDieSurkan(npc, mostPlayerDamage, 700);
				return;
			case 700845:
			case 700846:
				onDieSurkan(npc, mostPlayerDamage, 800);
				return;
			case 700847:
				onDieSurkan(npc, mostPlayerDamage, 900);
				return;
			case 700841:
			case 700842:
				onDieSurkan(npc, mostPlayerDamage, 1000);
				return;
			case 700843:
			case 700844:
				onDieSurkan(npc, mostPlayerDamage, 1100);
				return;
			case 216882: // Captain's Cabin teleport
				sendMsgByRace(1400652, Race.PC_ALL, 0);
				if (race.equals(Race.ASMODIANS)) {
					spawn(730358, 496.178f, 761.770f, 390.805f, (byte) 0, 186);
				}
				else {
					spawn(730357, 473.759f, 761.864f, 390.805f, (byte) 0, 33);
				}
				break;
			case 700836:
				updateScore(mostPlayerDamage, npc, 100, false);
				CreatureActions.delete(npc);
				return;
			case 216886:
				if (!dredgionReward.isRewarded()) {
					updateScore(mostPlayerDamage, npc, 1000, false);
					Race winningRace = dredgionReward.getWinningRaceByScore();
					stopInstance(winningRace);
				}
				// if (winningRace.equals(Race.ELYOS)) {
				// sendMsgByRace(1400230, Race.ELYOS, 0);
				// }
				// else {
				// sendMsgByRace(1400231, Race.ASMODIANS, 0);
				// }
				return;
			case 216941:
				updateScore(mostPlayerDamage, npc, 1000, false);
				return;
			case 216885:
				updateScore(mostPlayerDamage, npc, 500, false);
				return;
		}
		super.onDie(npc);
	}

	@Override
	protected void openFirstDoors() {
		openDoor(4);
		openDoor(173);
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
