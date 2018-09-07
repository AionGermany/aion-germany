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
package ai.instance.illuminaryObelisk;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler
 * @rework Ever
 * @rework Blackfire
 */
@AIName("northern_shield_generator")
public class Northern_Shield_GeneratorAI2 extends NpcAI2 {

	private boolean isInstanceDestroyed;
	private boolean wave1 = true;
	private boolean wave2;
	private boolean wave3;
	private boolean restrict;
	private boolean isFull;

	@Override
	protected void handleDialogStart(Player player) {
		if (!restrict) {
			if (isFull) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402203));
			}
			else {
				if (player.getInventory().getFirstItemByItemId(164000289) != null) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
				}
			}
		}
		else {
			String str = "You can start charging this again after 8 seconds";
			PacketSendUtility.sendMessage(player, str);
		}
	}

	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(164000289, 3)) {
			if (wave1) {
				restrict = true;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402197));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						startWaveNorthernShieldGenerator1();
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402201));
						wave1 = false;
						wave2 = true;
						restrict = false;

					}
				}, 8000);

				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702017, 169.55626f, 254.52907f, 293.04276f, (byte) 0, 17);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402227));
					}
				}, 4000);
			}
			if (wave2) {
				restrict = true;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402197));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402201));
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 0, 0));
						startWaveNorthernShieldGenerator2();
						wave2 = false;
						wave3 = true;
						restrict = false;
					}
				}, 8000);

			}
			if (wave3) {
				restrict = true;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402197));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402201));
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 0, 0));
						startWaveNorthernShieldGenerator3();
						wave3 = false;
						restrict = false;
						isFull = true;
					}
				}, 8000);

			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}

	/**
	 * The higher the phase of the charge will spawn more difficult monsters, in the 3rd phase elite monsters will spawn. Charging a shield to the 3rd phase continuously can be hard because of all the
	 * mobs you will have to handle. A few easy monsters will spawn after a certain time if you leave the shield unit alone. After all units have been charged to the 3rd phase, defeat the remaining
	 * monsters. *************************** Northern Shield Generator * **************************
	 */

	private void startWaveNorthernShieldGenerator1() {
		sp(233726, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
		sp(233727, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
		sp(233857, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233729, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
				sp(233734, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
				sp(233738, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702227, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
			}
		}, 30000);
	}

	private void startWaveNorthernShieldGenerator2() {
		sp(233736, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
		sp(233737, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
		sp(233730, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233728, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
				sp(233731, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
				sp(233732, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702228, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
			}
		}, 30000);

	}

	private void startWaveNorthernShieldGenerator3() {
		sp(233736, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 6000, "NorthernShieldGenerator1");
		sp(233737, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 6000, "NorthernShieldGenerator2");
		sp(233730, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 6000, "NorthernShieldGenerator3");
		sp(233729, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 23000, "NorthernShieldGenerator1");
		sp(233734, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 23000, "NorthernShieldGenerator2");
		sp(233738, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 23000, "NorthernShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233728, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 6000, "NorthernShieldGenerator1");
				sp(233731, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 6000, "NorthernShieldGenerator2");
				sp(233732, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 6000, "NorthernShieldGenerator3");
				sp(233739, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 23000, "NorthernShieldGenerator1");
				sp(233735, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 23000, "NorthernShieldGenerator2");
				sp(233733, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 23000, "NorthernShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702229, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
			}
		}, 30000);

	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time);
	}

	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
}
