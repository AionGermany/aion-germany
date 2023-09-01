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
package ai;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.DialogPage;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.PlayerScript;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_SCRIPTS;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas
 */
@AIName("butler")
public class ButlerAI2 extends GeneralNpcAI2 {

	private static final Logger log = LoggerFactory.getLogger(ButlerAI2.class);

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		return kickDialog(player, DialogPage.getPageByAction(dialogId));
	}

	private boolean kickDialog(Player player, DialogPage page) {
		if (page == DialogPage.NULL) {
			return false;
		}

		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), page.id()));
		return true;
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		if (creature instanceof Player) {
			Player player = (Player) creature;
			House house = (House) getCreator();
			if (player.getObjectId() == house.getOwnerId()) {
				// DO SOMETHING SPECIAL
			}

			Map<Integer, PlayerScript> scriptMap = house.getPlayerScripts().getScripts();
			try {
				// protect against writing
				for (int position = 0; position < 8; position++) {
					scriptMap.get(position).writeLock();
				}
				int totalSize = 0;
				int position = 0;
				int from = 0;
				while (position != 7) {
					for (; position < 8; position++) {
						PlayerScript script = scriptMap.get(position);
						byte[] bytes = script.getCompressedBytes();
						if (bytes == null) {
							continue;
						}
						if (bytes.length > 8141) {
							log.warn("Player " + player.getObjectId() + " has too big script at position " + position);
							return;
						}
						if (totalSize + bytes.length > 8141) {
							position--;
							PacketSendUtility.sendPacket(player, new SM_HOUSE_SCRIPTS(house.getAddress().getId(), house.getPlayerScripts(), from, position));
							from = position + 1;
							totalSize = 0;
							continue;
						}
						totalSize += bytes.length + 8;
					}
					position--;
					if (totalSize > 0 || from == 0 && position == 7) {
						PacketSendUtility.sendPacket(player, new SM_HOUSE_SCRIPTS(house.getAddress().getId(), house.getPlayerScripts(), from, position));
					}
				}
			}
			finally {
				// remove write locks finally
				for (int position = 0; position < 8; position++) {
					scriptMap.get(position).writeUnlock();
				}
			}
		}
	}
}
