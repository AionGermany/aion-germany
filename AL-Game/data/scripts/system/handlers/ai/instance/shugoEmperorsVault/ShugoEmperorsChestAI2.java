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

package ai.instance.shugoEmperorsVault;

import static ch.lambdaj.Lambda.maxFrom;

import java.util.Collection;
import java.util.HashSet;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.drop.DropService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

import ai.ActionItemNpcAI2;

/**
 * @author Falke_34, Lyras
 */
@AIName("shugoEmperorsChest")
// 832929 Kleine Kiste = 1 Key, 832930 Mittlere Kiste = 3 Key, 832931 Gro�e Kiste = 7 Key
public class ShugoEmperorsChestAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		int npcId = getOwner().getNpcId();
		int greyKeys = (int) player.getInventory().getItemCountByItemId(185000222);
		int goldKeys = (int) player.getInventory().getItemCountByItemId(185000221);
		int totalKeys = greyKeys + goldKeys;

		switch (npcId) {
			case 832929:
				if (totalKeys >= 1) {
					if (player.getInventory().decreaseByItemId(185000222, 1) || player.getInventory().decreaseByItemId(185000221, 1))
						analyzeOpening(player);
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1111300));
				}
				break;
			case 832930:
				if (totalKeys >= 3) {
					if (greyKeys - 3 >= 0 && player.getInventory().decreaseByItemId(185000222, 3)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 3 == -1 && player.getInventory().decreaseByItemId(185000222, 2) && player.getInventory().decreaseByItemId(185000221, 1)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 3 == -2 && player.getInventory().decreaseByItemId(185000222, 1) && player.getInventory().decreaseByItemId(185000221, 2)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 3 == -3 && player.getInventory().decreaseByItemId(185000221, 3)) {
						analyzeOpening(player);
					}
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1111300));
				}
				break;
			case 832931:
				if (totalKeys >= 7) {
					if (greyKeys - 7 >= 0 && player.getInventory().decreaseByItemId(185000222, 7)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -1 && player.getInventory().decreaseByItemId(185000222, 6) && player.getInventory().decreaseByItemId(185000221, 1)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -2 && player.getInventory().decreaseByItemId(185000222, 5) && player.getInventory().decreaseByItemId(185000221, 2)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -3 && player.getInventory().decreaseByItemId(185000222, 4) && player.getInventory().decreaseByItemId(185000221, 3)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -4 && player.getInventory().decreaseByItemId(185000222, 3) && player.getInventory().decreaseByItemId(185000221, 4)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -5 && player.getInventory().decreaseByItemId(185000222, 2) && player.getInventory().decreaseByItemId(185000221, 5)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -6 && player.getInventory().decreaseByItemId(185000222, 1) && player.getInventory().decreaseByItemId(185000221, 6)) {
						analyzeOpening(player);
					}
					else if (greyKeys - 7 == -7 && player.getInventory().decreaseByItemId(185000221, 7)) {
						analyzeOpening(player);
					}
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1111300));
				}
				break;
		}
	}

	private void analyzeOpening(Player player) {
		if (getOwner().isInState(CreatureState.DEAD)) {
			AuditLogger.info(player, "Attempted multiple Chest looting!");
			return;
		}

		AI2Actions.dieSilently(this, player);
		Collection<Player> players = new HashSet<Player>();
		if (player.isInGroup2()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
				if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)) {
					players.add(member);
				}
			}
		}
		else if (player.isInAlliance2()) {
			for (Player member : player.getPlayerAlliance2().getOnlineMembers()) {
				if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)) {
					players.add(member);
				}
			}
		}
		else {
			players.add(player);
		}
		DropRegistrationService.getInstance().registerDrop(getOwner(), player, maxFrom(players).getLevel(), players);
		DropService.getInstance().requestDropList(player, getObjectId());
	}
}
