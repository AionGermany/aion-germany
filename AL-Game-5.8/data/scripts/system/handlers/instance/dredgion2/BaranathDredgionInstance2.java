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
@InstanceID(300110000)
public class BaranathDredgionInstance2 extends DredgionInstance2 {

	@Override
	public void onEnterInstance(Player player) {
		if (isInstanceStarted.compareAndSet(false, true)) {
			if (Rnd.get(1, 100) < 51) {
				switch (Rnd.get(2)) {
					case 0:
						spawn(215093, 416.3429f, 282.32785f, 409.7311f, (byte) 80);
						break;
					default:
						spawn(215093, 552.07446f, 289.058f, 409.7311f, (byte) 80);
						break;
				}
			}
			startInstanceTask();
		}
		super.onEnterInstance(player);
	}

	private void onDieSurkan(Npc npc, Player mostPlayerDamage, int points) {
		Race race = mostPlayerDamage.getRace();
		captureRoom(race, npc.getNpcId() + 14 - 700498);
		for (Player player : instance.getPlayersInside()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400199, new DescriptionId(race.equals(Race.ASMODIANS) ? 1800483 : 1800481), new DescriptionId(npc.getObjectTemplate().getNameId() * 2 + 1)));
		}
		if (++surkanKills == 5) {
			spawn(214823, 485.423f, 808.826f, 416.868f, (byte) 30);
			sendMsgByRace(1401416, Race.PC_ALL, 0);
		}
		getPlayerReward(mostPlayerDamage).captureZone();
		updateScore(mostPlayerDamage, npc, points, false);
		CreatureActions.delete(npc);
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		Race race = mostPlayerDamage.getRace();
		switch (npc.getNpcId()) {
			case 700485:
			case 700486:
			case 700495:
			case 700496:
				onDieSurkan(npc, mostPlayerDamage, 500);
				return;
			case 700497:
			case 700498:
				onDieSurkan(npc, mostPlayerDamage, 700);
				return;
			case 700493:
			case 700492:
				onDieSurkan(npc, mostPlayerDamage, 800);
				return;
			case 700487:
				onDieSurkan(npc, mostPlayerDamage, 900);
				return;
			case 700490:
			case 700491:
				onDieSurkan(npc, mostPlayerDamage, 600);
				return;
			case 700488:
			case 700489:
				onDieSurkan(npc, mostPlayerDamage, 1080);
				return;
			case 214823:
				updateScore(mostPlayerDamage, npc, 1000, false);
				stopInstance(race);
				if (race == Race.ELYOS) {
					sendMsgByRace(1400230, Race.ELYOS, 0);
				}
				else {
					sendMsgByRace(1400231, Race.ASMODIANS, 0);
				}
				return;
			case 700508:
				switch (race) {
					case ELYOS:
						spawn(700502, 520.88f, 493.40f, 395.34f, (byte) 28, 16);
						sendMsgByRace(1400226, Race.ELYOS, 0);
						break;
					case ASMODIANS:
						spawn(700502, 448.39f, 493.64f, 395.04f, (byte) 108, 12);
						sendMsgByRace(1400227, Race.ASMODIANS, 0);
						break;
					default:
						break;
				}
				return;
			case 700506:
				switch (race) {
					case ELYOS:
						spawn(730214, 567.59f, 175.20f, 432.28f, (byte) 33);
						sendMsgByRace(1400228, Race.ELYOS, 0);
						break;
					case ASMODIANS:
						spawn(730214, 402.33f, 175.12f, 432.28f, (byte) 41);
						sendMsgByRace(1400229, Race.ASMODIANS, 0);
						break;
					default:
						break;
				}
				return;
		}
		super.onDie(npc);
	}

	@Override
	protected void openFirstDoors() {
		openDoor(17);
		openDoor(18);
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
