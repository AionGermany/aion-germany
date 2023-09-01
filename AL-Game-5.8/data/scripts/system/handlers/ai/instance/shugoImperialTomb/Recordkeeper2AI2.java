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
package ai.instance.shugoImperialTomb;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */

@AIName("recordkeeper2")
public class Recordkeeper2AI2 extends NpcAI2 {

	private int skillId;

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		if (dialogId == 10000) {
			switch (getNpcId()) {
				// case 831109: //Stage 1.
				// spawn(219468, 334.02423f, 432.39493f, 294.76016f, (byte) 56);
				// spawn(219468, 332.06616f, 423.65768f, 294.7601f, (byte) 55);
				// spawn(219469, 348.7215f, 428.9514f, 294.76022f, (byte) 56);
				// spawn(219469, 346.54214f, 420.23975f, 294.76013f, (byte) 56);
				// PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401586));
				// break;
				case 831110: // Stage 1.
					spawn(219483, 410.8536f, 411.2721f, 376.33743f, (byte) 0);
					skillId = player.getRace() == Race.ASMODIANS ? 21103 : 21094; // Ancient Shugo Warrior Form
					SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0); // Ancient Shugo Warrior Form
					getOwner().getController().delete();

					// spawn tower
					spawn(831130, 170.58615f, 224.70198f, 535.81213f, (byte) 15); // Crown Prince's Tombstone.
					spawn(831250, 186.37216f, 226.88597f, 535.81213f, (byte) 15); // Royal Tomb Stone Tower.
					spawn(831250, 169.97723f, 239.81743f, 535.812133f, (byte) 15); // Royal Tomb Stone Tower.

					// Phase 1
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
						}
					}, 5000);

					// Phase 2
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
						}
					}, 10000);

					// Phase 3
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 15000);

					// Phase 4 Elite
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Center path
							startWalk((Npc) spawn(219924, 218.11357f, 287.34048f, 550.68805f, (byte) 78), "3005600003");
							// TODO: Message "More pillagers will arrive in 5 seconds!"
						}
					}, 20000);

					// Phase 5 more
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
						}
					}, 25000);

					// Phase 6
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 30000);

					// Phase 7
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 35000);

					// Phase 8
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 40000);

					// Phase 9
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
						}
					}, 45000);

					// Phase 10
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219638, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Strong Kobold Worker.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219638, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Strong Kobold Worker.
						}
					}, 50000);

					// Phase 11
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219629, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Tomb Ghost.

							// Center path
							startWalk((Npc) spawn(219924, 218.11357f, 287.34048f, 550.68805f, (byte) 78), "3005600003"); // Awakened Guardian Tomb Ghost.

							// Right path
							startWalk((Npc) spawn(219629, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Tomb Ghost.
						}
					}, 55000);

					// Phase 12
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 60000);

					// Phase 13
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 65000);

					// Phase 14
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 70000);

					// Phase 15
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Left path
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Kobold Foreman.
							startWalk((Npc) spawn(219639, 211.19382f, 294.39218f, 550.68805f, (byte) 78), "3005600001"); // Strong Kobold Foreman. //TODO AI2

							// Right path
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219632, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Diligent Kobold Worker.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
							startWalk((Npc) spawn(219633, 225.52513f, 279.42987f, 550.68805f, (byte) 78), "3005600002"); // Kobold Foreman.
						}
					}, 75000);

					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401607));
					break;
				case 831111: // Stage 3.
					spawn(219487, 101.958855f, 52.92559f, 618.24347f, (byte) 111);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401667));
					break;
				case 831112: // Stage 4.
					spawn(219495, 444.79245f, 103.18407f, 212.20023f, (byte) 67);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401668));
					break;

				case 831113: // Stage 1 End.
					TeleportService2.teleportTo(player, 300560000, instanceId, 413.01855f, 410.42902f, 374.5875f, (byte) 52);
					spawn(831110, 410.8536f, 411.2721f, 376.33743f, (byte) 0);
					break;
				case 831114: // Stage 2 End.
					TeleportService2.teleportTo(player, 300560000, instanceId, 102.74224f, 52.9174f, 619.4042f, (byte) 0);
					spawn(831111, 101.958855f, 52.92559f, 618.24347f, (byte) 111);
					break;
				case 831195: // Stage 3 End.
					TeleportService2.teleportTo(player, 300560000, instanceId, 444.79245f, 103.18407f, 212.20023f, (byte) 67);
					spawn(219407, 468.00766f, 514.5526f, 417.40436f, (byte) 0);
					break;
				case 831116: // Stage 4 End.
					break;
			}
		}
		return true;
	}

	private void startWalk(final Npc npc, final String walkId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				npc.getSpawn().setWalkerId(walkId);
				WalkManager.startWalking((NpcAI2) npc.getAi2());
				npc.setState(1);
				PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
			}
		}, 2000);
	}
}
