package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class MinionList {

	private Player player;
	private int lastUsedMinionId;
	private FastMap<Integer, MinionCommonData> minions = new FastMap<Integer, MinionCommonData>();
	private int lastUsedObjId;

	MinionList(Player player) {
		this.player = player;
		loadMinions();
	}

	public void loadMinions() {
		List<MinionCommonData> playerMinions = DAOManager.getDAO(PlayerMinionsDAO.class).getPlayerMinions(player);
		MinionCommonData lastUsedMinion = null;
		for (MinionCommonData minion : playerMinions) {
			if (minion.getExpireTime() > 0) {
				ExpireTimerTask.getInstance().addTask(minion, player);
			}
			minions.put(minion.getObjectId(), minion);
			if (lastUsedMinion == null || minion.getDespawnTime().after(lastUsedMinion.getDespawnTime())) {
				lastUsedMinion = minion;
			}
		}

		if (lastUsedMinion != null) {
			lastUsedMinionId = lastUsedMinion.getMinionId();
		}
	}

	public Collection<MinionCommonData> getMinions() {
		return minions.values();
	}

	public MinionCommonData getMinion(int minionId) {
		return minions.get(minionId);
	}

	public MinionCommonData getLastUsedMinion() {
		return getMinion(lastUsedMinionId);
	}

	public void setLastUsedMinionId(int lastUsedMinionId) {
		this.lastUsedMinionId = lastUsedMinionId;
	}

	public MinionCommonData addNewMinion(Player player, int minionId, String name, String grade, int level, int growth_points) {
		return addNewMinion(player, minionId, name, grade, level, System.currentTimeMillis(), growth_points);
	}

	public MinionCommonData addNewMinion(Player player, int minionId, String name, String minionGrade, int level, long birthday, int growth_points) {
		MinionCommonData minionCommonData = new MinionCommonData(minionId, player.getObjectId(), name, minionGrade, level, growth_points, true);
		minionCommonData.setBirthday(new Timestamp(birthday));
		minionCommonData.setDespawnTime(new Timestamp(System.currentTimeMillis()));
		DAOManager.getDAO(PlayerMinionsDAO.class).insertPlayerMinion(minionCommonData);
		minions.put(minionCommonData.getObjectId(), minionCommonData);
		return minionCommonData;
	}

	public boolean hasMinion(int minionId) {
		return minions.containsKey(minionId);
	}

	public void deleteMinion(int minionId) {
		if (hasMinion(minionId)) {
			minions.remove(minionId);
			DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinion(player, minionId);
		}
	}

	public void disMissMinion(int minionId) {
		if (minionId != 0) {
			minions.remove(minionId);
		}
	}

	public void updateMinionsList() {
		minions.clear();
		for (MinionCommonData minionCommonData : DAOManager.getDAO(PlayerMinionsDAO.class).getPlayerMinions(player)) {
			minions.put(minionCommonData.getObjectId(), minionCommonData);
		}
		if (minions != null) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(1, player.getMinionList().getMinions()));
		}
	}

	public void setLastUsed(int objId) {
		this.lastUsedObjId = objId;
	}

	public int getLastUsed() {
		return lastUsedObjId;
	}
}
