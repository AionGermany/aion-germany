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
package instance;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @Author Ever
 */
@InstanceID(301110000)
public class RunadiumInstance extends GeneralInstanceHandler {

	byte clonekill;
	byte clonecount;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		spawn(284375, 255.57027f, 293.0893f, 253.79536f, (byte) 90);
		spawn(284447, 256.5698f, 257.8559f, 241.9354f, (byte) 90);
	}

	@Override
	public void onDie(Npc npc) {
		final Npc npc1 = getNpc(284664);
		final Npc npc2 = getNpc(284380);
		final Npc npc3 = getNpc(284381);
		final Npc npc4 = getNpc(284382);
		final Npc npc5 = getNpc(284663);
		final Npc npc6 = getNpc(284660);
		final Npc npc7 = getNpc(284661);
		final Npc npc8 = getNpc(284662);
		final Npc npc9 = getNpc(284659);
		final Npc novun = getNpc(284377);
		final Npc clone = getNpc(284384);
		final Npc lapilima = getNpc(284378);
		final Npc obscura = getNpc(284379);
		final Npc bosscursed = getNpc(231304);
		final Npc bossnonatk = getNpc(284375);
		switch (npc.getNpcId()) {
			case 284378:
				lapilima.getController().onDelete();
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					bossnonatk.getController().onDelete();
				}
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					spawn(231304, 256.4457f, 257.6867f, 242.30f, (byte) 90);
				}
				break;
			case 284379:
				obscura.getController().onDelete();
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					bossnonatk.getController().onDelete();
				}
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					spawn(231304, 256.4457f, 257.6867f, 242.30f, (byte) 90);
				}
				break;
			case 284377:
				novun.getController().onDelete();
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					bossnonatk.getController().onDelete();
				}
				if (isDead(obscura) && isDead(lapilima) && isDead(novun)) {
					spawn(231304, 256.4457f, 257.6867f, 242.30f, (byte) 90);
				}
				break;
			case 284664:
				npc1.getController().onDelete();
				if (isDead(npc1) && isDead(npc2) && isDead(npc3)) {
					Teleport();
				}
				break;
			case 284380:
				npc2.getController().onDelete();
				if (isDead(npc1) && isDead(npc2) && isDead(npc3)) {
					Teleport();
				}
				break;
			case 284381:
				npc3.getController().onDelete();
				if (isDead(npc1) && isDead(npc2) && isDead(npc3)) {
					Teleport();
				}
				break;
			case 284383:
				bosscursed.getController().onAttack(bosscursed, 125000, true);
				clonekill++;
				clonecount++;
				if (clonecount == 1) {
					spawn(284383, 284.50403f, 262.8162f, 248.77342f, (byte) 63); // clone skill
				}
				if (clonekill % 2 == 0) {
					FakeTeleportToEnrraged();
					deleteNpcs(instance.getNpcs(284384));
				}
				break;
			case 284384:
				bosscursed.getController().onAttack(bosscursed, 125000, true);
				break;
			case 231304:
				clone.getController().onDelete();
				break;
			case 231305:
				spawn(730843, 256.46695f, 263.78525f, 243.71048f, (byte) 90);
				spawn(701795, 256.5923f, 257.96582f, 241.78694f, (byte) 90);
				break;
			case 284382:
				npc4.getController().onDelete();
				if (isDead(npc4) && isDead(npc5) && isDead(npc6)) {
					Teleport2();
				}
				break;
			case 284663:
				npc5.getController().onDelete();
				if (isDead(npc4) && isDead(npc5) && isDead(npc6)) {
					Teleport2();
				}
				break;
			case 284660:
				npc6.getController().onDelete();
				if (isDead(npc4) && isDead(npc5) && isDead(npc6)) {
					Teleport2();
				}
				break;
			case 284661:
				npc7.getController().onDelete();
				if (isDead(npc7) && isDead(npc8) && isDead(npc9)) {
					Teleport2();
				}
				break;
			case 284662:
				npc8.getController().onDelete();
				if (isDead(npc7) && isDead(npc8) && isDead(npc9)) {
					Teleport2();
				}
				break;
			case 284659:
				npc9.getController().onDelete();
				if (isDead(npc7) && isDead(npc8) && isDead(npc9)) {
					Teleport2();
				}
				break;
		}
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	private void Teleport() {
		final Npc bosscursed = getNpc(231304);
		SkillEngine.getInstance().getSkill(bosscursed, 21165, 65, bosscursed).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(bosscursed, 256.4457f, 257.6867f, 242.30f, (byte) 90);
				PacketSendUtility.broadcastPacketAndReceive(bosscursed, new SM_FORCED_MOVE(bosscursed, bosscursed));
			}
		}, 2000);
	}

	private void Teleport2() {
		final Npc bossenrraged = getNpc(231305);
		SkillEngine.getInstance().getSkill(bossenrraged, 21165, 65, bossenrraged).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(bossenrraged, 256.4457f, 257.6867f, 242.30f, (byte) 90);
				PacketSendUtility.broadcastPacketAndReceive(bossenrraged, new SM_FORCED_MOVE(bossenrraged, bossenrraged));
			}
		}, 2000);
	}

	private void FakeTeleportToEnrraged() {
		final Npc bosscursed = getNpc(231304);
		SkillEngine.getInstance().getSkill(bosscursed, 21165, 65, bosscursed).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				bosscursed.getController().onDelete();
				spawn(231305, 256.4457f, 257.6867f, 242.30f, (byte) 90);
			}
		}, 2000);
	}
}
