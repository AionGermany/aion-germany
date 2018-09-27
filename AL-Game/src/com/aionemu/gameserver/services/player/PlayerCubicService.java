/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.dao.PlayerCubicsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.cubics.PlayerMCEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.cubics.CubicsTemplate;
import com.aionemu.gameserver.model.templates.cubics.StatCoreList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBIC;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Phantom_KNA
 */
public class PlayerCubicService implements StatOwner {

	PlayerMCEntry[] maxMonsterCubic;

	private PlayerCubicService() {
		GameServer.log.info("[PlayerCubic] loaded ...");
	}

	/**
	 *
	 * @param player
	 */
	public void onLogin(Player player) {
		maxMonsterCubic = player.getMonsterCubic().getAllMC();
		Storage bag = player.getInventory();

		for (int i = 1; i < 39; i++) { // Limpiar el visual de cubos en el juego
			PacketSendUtility.sendPacket(player, new SM_CUBIC(i, 0, 0, 0));
		}
		HashMap<Integer, Integer> cubic = new HashMap<>();
		HashMap<Integer, Integer> rank = new HashMap<>();
		HashMap<Integer, Integer> level = new HashMap<>();
		HashMap<Integer, Integer> statValue = new HashMap<>();
		player.getGameStats().endEffect(this);
		for (PlayerMCEntry playerMonsterCubic : maxMonsterCubic) {
			List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
			try {
				modifiers.add(new ExecuteStatAdd(getStatValueByCategory(playerMonsterCubic.getCategory()), playerMonsterCubic.getStatValue()));
				player.getGameStats().addEffect(this, modifiers);
			} catch (Exception ex) {
				GameServer.log.error("Error on add stat.", ex);
			}
			cubic.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getCubeId());
			rank.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getRank());
			level.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getLevel());
			statValue.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getStatValue());
			for (int i = 0; i < 39; i++) {
				if (cubic.containsKey(i)) { // Enviar los cubus obtenidos por el personaje
					CubicsTemplate monsterCubic = DataManager.CUBICS_DATA.getCubicsId(i);
					long itemsCubicInBag = bag.getItemCountByItemId(monsterCubic.getItemIdCubic());
					// Activar el registro de Cubus para aquellos que no hayan sido registrado
					if (itemsCubicInBag > 0 && monsterCubic.getMaxRank() != playerMonsterCubic.getRank()) {
						PacketSendUtility.sendPacket(player, new SM_CUBIC(cubic.get(i), rank.get(cubic.get(i)),level.get(cubic.get(i)), (int) itemsCubicInBag));
					}
				}
			}
		}
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
	}

	/*
	 * Obtener el valor estadisticas en base a la categoria de el Cubus
	 */
	private StatEnum getStatValueByCategory(int category) {
		StatEnum statName = null;
		switch (category) {
		case 100:
			statName = StatEnum.MAXHP;
			break;
		case 101:
			statName = StatEnum.MAXMP;
			break;
		case 102:
			statName = StatEnum.HEAL_BOOST;
			break;
		case 103:
			// statName = StatEnum.PHYSICALPOWERBOOST; //Not Added in Src
			break;
		case 104:
			// statName = StatEnum.MAGICALPOWERBOOST; //Not Added in Src
			break;
		case 105:
			// statName = StatEnum.PHYSICALPOWERBOOSTRESIST; //Not Added in Src
			break;
		case 106:
			// statName = StatEnum.MAGICALPOWERBOOSTRESIST; //Not Added in Src
			break;
		case 107:
			statName = StatEnum.PHYSICAL_ACCURACY;
			break;
		case 108:
			statName = StatEnum.MAGICAL_ACCURACY;
			break;
		case 109:
			statName = StatEnum.EVASION;
			break;
		case 110:
			statName = StatEnum.PARRY;
			break;
		case 111:
			statName = StatEnum.BLOCK;
			break;
		case 112:
			statName = StatEnum.MAGICAL_RESIST;
			break;
		}
		return statName;
	}

	/**
	 *
	 * @param player
	 * @param cubicId
	 */
	public void registerCubic(Player player, int cubicId) {
		CubicsTemplate monsterCubic = DataManager.CUBICS_DATA.getCubicsId(cubicId);
		int rankById = DAOManager.getDAO(PlayerCubicsDAO.class).getRankById(player.getObjectId(), cubicId);
		int level = DAOManager.getDAO(PlayerCubicsDAO.class).getLevelById(player.getObjectId(), cubicId);
		int statValue = DAOManager.getDAO(PlayerCubicsDAO.class).getStatValueById(player.getObjectId(), cubicId);
		player.getMonsterCubic().add(player, cubicId, 0, 0, 0, monsterCubic.getCategory()); // Inicial valores
		for (StatCoreList coreList : monsterCubic.getStatLists()) {
			if (monsterCubic.getMaxRank() != rankById) {
				if (level == coreList.getLevel()) {
					rankById += 1;
					statValue = coreList.getValue();
				}
			}
		}
		level += 1;
		player.getMonsterCubic().add(player, cubicId, rankById, level, statValue, monsterCubic.getCategory());
		maxMonsterCubic = player.getMonsterCubic().getAllMC();
		player.getGameStats().endEffect(this);
		for (PlayerMCEntry playerMonsterCubic : maxMonsterCubic) {
			List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
			try {
				modifiers.add(new ExecuteStatAdd(getStatValueByCategory(playerMonsterCubic.getCategory()),playerMonsterCubic.getStatValue()));
				player.getGameStats().addEffect(this, modifiers);
			} catch (Exception ex) {
				GameServer.log.error("Error on add stat.", ex);
			}
		}
		player.getInventory().decreaseByItemId(monsterCubic.getItemIdCubic(), 1);
		PacketSendUtility.sendPacket(player, new SM_CUBIC(cubicId, rankById, level, 0));
		Storage bag = player.getInventory();
		long itemsCubicInBag = bag.getItemCountByItemId(monsterCubic.getItemIdCubic());
		if (itemsCubicInBag > 0 && monsterCubic.getMaxRank() != rankById) {
			PacketSendUtility.sendPacket(player, new SM_CUBIC(cubicId, rankById, level, (int) itemsCubicInBag));
		}
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
	}

	class ExecuteStatAdd extends StatFunction {

		int modifier = 1;

		ExecuteStatAdd(StatEnum stat, int modifier) {
			this.stat = stat;
			this.modifier = modifier;
		}

		@Override
		public void apply(Stat2 stat) {
			stat.addToBonus(this.modifier);

		}

		@Override
		public int getPriority() {
			return 60;
		}
	}

	public static PlayerCubicService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final PlayerCubicService INSTANCE = new PlayerCubicService();
	}
}
