/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 * <p/>
 * Aion-Lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * Aion-Lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.model.pets;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.pet.PetBonusAttr;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.model.templates.pet.PetPenaltyAttr;
import com.aionemu.gameserver.model.templates.pet.PetTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.change.Func;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Created by Ace on 01/08/2016.
 */

public class PetBuff implements StatOwner {

	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private PetBonusAttr petBonusAttr;
	private long startTime;
	private ScheduledFuture<?> task = null;

	public PetBuff(int buffId) {
		petBonusAttr = DataManager.PET_BUFF_DATA.getPetBonusattr(buffId);
	}

	public void applyEffect(Player player, int time) {
		if (hasPetBuff() || petBonusAttr == null)
			return;
		if (time != 0)
			task = ThreadPoolManager.getInstance().schedule(new PetBuffTask(player), time);
		startTime = System.currentTimeMillis();
		for (PetPenaltyAttr petPenaltyAttr : petBonusAttr.getPenaltyAttr()) {
			StatEnum stat = petPenaltyAttr.getStat();
			int statToModified = player.getGameStats().getStat(stat, 0).getBase();
			int value = petPenaltyAttr.getValue();
			int valueModified = petPenaltyAttr.getFunc().equals(Func.PERCENT) ? (statToModified * value / 100) : (value);
			functions.add(new StatAddFunction(stat, valueModified, true));
		}
		player.getGameStats().addEffect(this, functions);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_BUFF_PET_USE_START_MESSAGE);
		PacketSendUtility.sendPacket(player, new SM_PET(true, 0, 0));// start buffing
	}

	private void loopEffect(Player player, int time) {
		if (time != 0)
			task = ThreadPoolManager.getInstance().schedule(new PetBuffTask(player), time);
		PacketSendUtility.sendPacket(player, new SM_PET(true, 0, 0));// start buffing
	}

	public void endEffect(Player player) { // Test for http://falke34.bplaced.net/forums/showthread.php?tid=2977
		functions.clear();
		if (task != null) {
			task.cancel(false);
			task = null;
			player.getGameStats().endEffect(this);
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_BUFF_PET_USE_STOP_MESSAGE);
			PacketSendUtility.sendPacket(player, new SM_PET(false, 0, 0));// stop buffing
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		} else {
			return;
		}
	}

	public int getBuffRemaningTime() {
		return (int) ((System.currentTimeMillis() - startTime) / 1000);
	}

	private class PetBuffTask implements Runnable {

		private Player player;

		public PetBuffTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			Pet pet = player.getPet();
			PetTemplate petTemp = DataManager.PET_DATA.getPetTemplate(pet.getPetId());
			PetBonusAttr petBuff = DataManager.PET_BUFF_DATA.getPetBonusattr(petTemp.getPetFunction(PetFunctionType.BUFF).getId());

			if (task != null && player.getInventory().getItemCountByItemId(182007162) >= petBuff.getFoodCount()) {
				player.getInventory().decreaseByItemId(182007162, petBuff.getFoodCount());
				loopEffect(player, 300000);
			}
			else
				endEffect(player);
		}
	}

	public boolean hasPetBuff() {
		return task != null && !task.isDone();
	}

}
