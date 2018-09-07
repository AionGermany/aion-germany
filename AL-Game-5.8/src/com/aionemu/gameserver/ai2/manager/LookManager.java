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
package com.aionemu.gameserver.ai2.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcUiType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 */
public class LookManager {

	private static byte hBeforeChange = 0;
	private static byte hAfterChange = 0;
	private static byte hFinalChange = 0;
	private static final Logger log = LoggerFactory.getLogger(LookManager.class);

	public static void corrigateHeading(Npc npc, Player player) {
		if (!AIConfig.ACTIVE_NPC_LOOKING)
			return;
		if (npc == null)
			return;

		if (npc.isBoss() || npc.isAttackableNpc()) {
			randomHeading(npc, player);
		}
		else if (npc.isFlag() || npc.isRaidMonster() || isValidRace(npc.getRace()) || isValidTribe(npc.getTribe()) || isValidUiType(npc.getUiType())) {
			return;
		}
		else {
			coordinateHeading(npc, player);
		}
	}

	private static boolean isValidTribe(TribeClass tribe) {
		switch (tribe) {
			case TELEPORTOR_LI:
				return true;
			case TELEPORTOR_DA:
				return true;
			case FIELD_OBJECT_ALL:
				return true;
			case FIELD_OBJECT_LIGHT:
				return true;
			case FIELD_OBJECT_DARK:
				return true;
			default:
				return false;
		}
	}

	private static boolean isValidRace(Race race) {
		switch (race) {
			case DRAGON_CASTLE_DOOR:
				return true;
			case PC_LIGHT_CASTLE_DOOR:
				return true;
			case PC_DARK_CASTLE_DOOR:
				return true;
			default:
				return false;
		}
	}

	private static boolean isValidUiType(NpcUiType uiType) {
		switch (uiType) {
			case CRAFT:
				return true;
			case CRAFT_UI_ALWAYS:
				return true;
			default:
				return false;
		}
	}

	private static void randomHeading(Npc npc, Player player) {
		hBeforeChange = npc.getPosition().getHeading();
		int randomHeading = Rnd.get(1, 100);
		hAfterChange = (byte) randomHeading;

		if (hBeforeChange == hAfterChange) {
			randomHeading = Rnd.get(1, 100);
			hAfterChange = (byte) randomHeading;
		}

		npc.getPosition().setH(hAfterChange);
		updateHeading(npc, player);
		hBeforeChange = 0;
		hAfterChange = 0;

	}

	private static void coordinateHeading(Npc npc, Player player) {
		int randomHeading = 0;
		if (npc.getOldHeading() == 0) {
			randomHeading = Rnd.get(1, 100);
			hAfterChange = (byte) randomHeading;
			npc.getPosition().setH(hAfterChange);
			npc.setOldHeading(hAfterChange);
		}
		else {
			hBeforeChange = npc.getOldHeading();
			randomHeading = Rnd.get(hBeforeChange, hBeforeChange + 5);
			hAfterChange = (byte) randomHeading;
			hFinalChange = (byte) (hBeforeChange + hAfterChange);
			npc.getPosition().setH(hFinalChange);
			npc.setOldHeading(hFinalChange);
		}

		updateHeading(npc, player);
		hBeforeChange = 0;
		hAfterChange = 0;
	}

	private static void updateHeading(Npc npc, Player player) {
		PacketSendUtility.sendPacket(player, new SM_NPC_INFO(npc, player));
	}

	public void onStart() {
		hBeforeChange = 0;
		hAfterChange = 0;
		hFinalChange = 0;
		log.info("LookManager initialized");
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final LookManager instance = new LookManager();
	}

	public static final LookManager getInstance() {
		return SingletonHolder.instance;
	}

}
