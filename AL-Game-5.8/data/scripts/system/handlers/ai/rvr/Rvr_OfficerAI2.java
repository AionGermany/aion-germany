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

@AIName("rvr_officer") // 857733, 857734, 857735, 857740, 857741, 857742
public class Rvr_OfficerAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 857733: // Officer Tarkan
				sendRvrGuide();
				announceGeneralMiltarRescued();
				break;
			case 857734: // Officer Shagad
				sendRvrGuide();
				announceGeneralKuparoRescued();
				break;
			case 857735: // Officer Argan
				sendRvrGuide();
				announceGeneralLanstriRescued();
				break;
			case 857740: // Officer Nars
				sendRvrGuide();
				announceGeneralMagkenRescued();
				break;
			case 857741: // Officer Fasig
				sendRvrGuide();
				announceGeneralHarkRescued();
				break;
			case 857742: // Officer Gadevir
				sendRvrGuide();
				announceGeneralTombolkRescued();
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
	 * Attacker Asmodians.
	 */
	private void announceGeneralMiltarRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_01);
			}
		});
	}

	private void announceGeneralKuparoRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_02);
			}
		});
	}

	private void announceGeneralLanstriRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_03);
			}
		});
	}

	/**
	 * Attacker Elyos.
	 */
	private void announceGeneralMagkenRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_01);
			}
		});
	}

	private void announceGeneralHarkRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_02);
			}
		});
	}

	private void announceGeneralTombolkRescued() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_03);
			}
		});
	}
}
