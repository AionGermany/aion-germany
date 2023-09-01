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
package ai.rvr;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

@AIName("rvr_general") // 857737, 857738, 857739, 857744, 857745, 857746
public class Rvr_GeneralAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 857737: // General Miltar
				sendRvrGuide();
				announceGeneralMiltarDie();
				break;
			case 857738: // General Kuparo
				sendRvrGuide();
				announceGeneralKuparoDie();
				break;
			case 857739: // General Lanstri
				sendRvrGuide();
				announceGeneralLanstriDie();
				break;
			case 857744: // General Magken
				sendRvrGuide();
				announceGeneralMagkenDie();
				break;
			case 857745: // General Hark
				sendRvrGuide();
				announceGeneralHarkDie();
				break;
			case 857746: // General Tombolk
				sendRvrGuide();
				announceGeneralTombolkDie();
				break;
		}
		super.handleDied();
	}

	private void sendRvrGuide() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(player, getOwner(), 15)) {
					HTMLService.sendGuideHtml(player, "Rvr_Guide");
				}
			}
		});
	}

	/**
	 * Defender Elyos.
	 */
	private void announceGeneralMiltarDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_01);
			}
		});
	}

	private void announceGeneralKuparoDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_02);
			}
		});
	}

	private void announceGeneralLanstriDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_03);
			}
		});
	}

	/**
	 * Defender Asmodians.
	 */
	private void announceGeneralMagkenDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_01);
			}
		});
	}

	private void announceGeneralHarkDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_02);
			}
		});
	}

	private void announceGeneralTombolkDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_03);
			}
		});
	}
}
