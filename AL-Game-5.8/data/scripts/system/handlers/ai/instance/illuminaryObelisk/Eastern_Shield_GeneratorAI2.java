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

import ai.ActionItemNpcAI2;

/**
 * @author Rinzler
 * @rework Ever
 * @rework Blackfire
 */
@AIName("eastern_shield_generator")
public class Eastern_Shield_GeneratorAI2 extends ActionItemNpcAI2 {

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
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402194));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						startWaveEasternShieldGenerator1();
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402198));
						wave1 = false;
						wave2 = true;
						restrict = false;

					}
				}, 8000);

				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702014, 255.7926f, 338.22058f, 325.56473f, (byte) 0, 60);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402224));
					}
				}, 4000);
			}
			if (wave2) {
				restrict = true;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402194));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402198));
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 0, 0));
						startWaveEasternShieldGenerator2();
						wave2 = false;
						wave3 = true;
						restrict = false;
					}
				}, 8000);

			}
			if (wave3) {
				restrict = true;
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402194));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402198));
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 0, 0));
						startWaveEasternShieldGenerator3();
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
	 * monsters. *************************** Eastern Shield Generator * **************************
	 */

	private void startWaveEasternShieldGenerator1() {
		sp(233720, 252.68709f, 333.483f, 325.59268f, (byte) 90, 1000, "EasternShieldGenerator1");
		sp(233721, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 1000, "EasternShieldGenerator2");
		sp(233881, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233728, 252.68709f, 333.483f, 325.59268f, (byte) 90, 6000, "EasternShieldGenerator1");
				sp(233729, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 6000, "EasternShieldGenerator2");
				sp(233730, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702218, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
			}
		}, 30000);
	}

	private void startWaveEasternShieldGenerator2() {
		sp(233731, 252.68709f, 333.483f, 325.59268f, (byte) 90, 1000, "EasternShieldGenerator1");
		sp(233732, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 1000, "EasternShieldGenerator2");
		sp(233733, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 1000, "EasternShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233734, 252.68709f, 333.483f, 325.59268f, (byte) 90, 23000, "EasternShieldGenerator1");
				sp(233735, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 23000, "EasternShieldGenerator2");
				sp(233736, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 23000, "EasternShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702219, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
			}
		}, 30000);

	}

	private void startWaveEasternShieldGenerator3() {
		sp(233737, 252.68709f, 333.483f, 325.59268f, (byte) 90, 6000, "EasternShieldGenerator1");
		sp(233738, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 6000, "EasternShieldGenerator2");
		sp(233728, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");
		sp(233735, 252.68709f, 333.483f, 325.59268f, (byte) 90, 23000, "EasternShieldGenerator1");
		sp(233734, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 23000, "EasternShieldGenerator2");
		sp(233733, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 23000, "EasternShieldGenerator3");

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sp(233729, 252.68709f, 333.483f, 325.59268f, (byte) 90, 6000, "EasternShieldGenerator1");
				sp(233730, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 6000, "EasternShieldGenerator2");
				sp(233731, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");
				sp(233732, 252.68709f, 333.483f, 325.59268f, (byte) 90, 23000, "EasternShieldGenerator1");
				sp(233739, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 23000, "EasternShieldGenerator2");
				sp(233735, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 23000, "EasternShieldGenerator3");
			}
		}, 15000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702220, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
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
