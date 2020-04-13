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
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.cubics.CubicsTemplate;
import com.aionemu.gameserver.model.templates.cubics.StatCoreList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBIC;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBIC_INFO;
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
	private List<IStatFunction> modifiers = new ArrayList<IStatFunction>();
	private HashMap<Integer, Integer> cubic = new HashMap<>();
	private HashMap<Integer, Integer> rank = new HashMap<>();
	private HashMap<Integer, Integer> level = new HashMap<>();
	private HashMap<Integer, Integer> statValue = new HashMap<>();

	/**
	 *
	 * @param player
	 */
	public void onLogin(Player player) {
		player.getGameStats().endEffect(this);
		player.setBonus(false);
		modifiers.clear();
		
		player.setMonsterCubic(null);
		player.setMonsterCubic(DAOManager.getDAO(PlayerCubicsDAO.class).load(player));
		maxMonsterCubic = player.getMonsterCubic().getAllMC();
		
		PacketSendUtility.sendPacket(player, new SM_CUBIC_INFO(94));
		
		for (PlayerMCEntry playerMonsterCubic : maxMonsterCubic) {
			try {
				modifiers.add(new StatAddFunction(getStatValueByCategory(playerMonsterCubic.getCategory()), playerMonsterCubic.getStatValue(), true));
			} catch (Exception ex) {
				GameServer.log.error("Error on add stat.", ex);
			}
			cubic.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getCubeId());
			rank.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getRank());
			level.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getLevel());
			statValue.put(playerMonsterCubic.getCubeId(), playerMonsterCubic.getStatValue());
			
			for (int cubicId = 1; cubicId <= 94; cubicId++) {
				if (cubic.containsKey(cubicId)) { // Enviar los cubus obtenidos por el personaje
					CubicsTemplate monsterCubic = DataManager.CUBICS_DATA.getCubicsId(cubicId);
					long itemsCubicInBag = player.getInventory().getItemCountByItemId(monsterCubic.getItemIdCubic());
					PacketSendUtility.sendPacket(player, new SM_CUBIC(cubic.get(cubicId), rank.get(cubic.get(cubicId)),level.get(cubic.get(cubicId)), (int) itemsCubicInBag));
				}
			}
		}
		player.setBonus(true);
		player.getGameStats().addEffect(this, modifiers);
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		cubic.clear();
		rank.clear();
		level.clear();
		statValue.clear();
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
			statName = StatEnum.PHYSICAL_ATTACK; // TODO .PHYSICALPOWERBOOST; //Not Added in Src
			break;
		case 104:
			statName = StatEnum.BOOST_MAGICAL_SKILL; // TODO .MAGICALPOWERBOOST; //Not Added in Src
			break;
		case 105:
			statName = StatEnum.PHYSICAL_DEFENSE; // TODO .PHYSICALPOWERBOOSTRESIST; //Not Added in Src
			break;
		case 106:
			statName = StatEnum.MAGICAL_RESIST; // TODO .MAGICALPOWERBOOSTRESIST; //Not Added in Src
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
		level++;
		for (StatCoreList coreList : monsterCubic.getStatLists()) {
			if (rankById <= monsterCubic.getMaxRank()) {
				if (level == coreList.getLevel()) {
					rankById++;
					statValue = coreList.getValue();
					break;
				}
			}
		}
		player.getMonsterCubic().add(player, cubicId, rankById, level, statValue, monsterCubic.getCategory());
		player.getGameStats().endEffect(this);
		player.setBonus(false);
		modifiers.clear();
		player.setMonsterCubic(null);
		player.setMonsterCubic(DAOManager.getDAO(PlayerCubicsDAO.class).load(player));
		for (PlayerMCEntry playerMonsterCubic : player.getMonsterCubic().getAllMC()) {
			try {
				modifiers.add(new StatAddFunction(getStatValueByCategory(playerMonsterCubic.getCategory()), playerMonsterCubic.getStatValue(), true));
			} catch (Exception ex) {
				GameServer.log.error("Error on add stat.", ex);
			}
		}
		player.getInventory().decreaseByItemId(monsterCubic.getItemIdCubic(), 1);
		PacketSendUtility.sendPacket(player, new SM_CUBIC(cubicId, rankById, level, 0));
		long itemsCubicInBag = player.getInventory().getItemCountByItemId(monsterCubic.getItemIdCubic());
		if (itemsCubicInBag > 0 && monsterCubic.getMaxRank() != rankById) {
			PacketSendUtility.sendPacket(player, new SM_CUBIC(cubicId, rankById, level, (int) itemsCubicInBag));
		}
		player.setBonus(true);
		player.getGameStats().addEffect(this, modifiers);
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
	}

	public static PlayerCubicService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final PlayerCubicService INSTANCE = new PlayerCubicService();
	}
}
