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
package com.aionemu.gameserver.model.account;

import java.util.Collection;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AccountTransformDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORMATION;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class AccountTransformList {

	private final Player player;
	private Map<Integer, AccountTransfo> transformations = (Map<Integer, AccountTransfo>) new FastMap<Integer, AccountTransfo>();
	private Map<Integer, AccountTransfo> transformationsCreated = (Map<Integer, AccountTransfo>) new FastMap<Integer, AccountTransfo>();

	public AccountTransformList(Player player) {
		this.player = player;
		this.loadAccountTransformList();
	}

	public void loadAccountTransformList() {
		for (AccountTransfo cmd : DAOManager.getDAO(AccountTransformDAO.class).loadAccountTransfo(player.getPlayerAccount()).values()) {
			transformations.put(cmd.getCardId(), cmd);
		}
	}

	public Collection<AccountTransfo> getTransformations() {
		return transformations.values();
	}

	public AccountTransfo getTransformation(int transformationId) {
		return transformations.get(transformationId);
	}

	public AccountTransfo addNewTransformation(Player player, int transformationId, int count) {
		AccountTransfo transformationCommonData = new AccountTransfo(transformationId, count);
		transformationCommonData.setCount(count);
		DAOManager.getDAO(AccountTransformDAO.class).addTransfo(player.getPlayerAccount(), transformationCommonData);
		transformations.put(transformationCommonData.getCardId(), transformationCommonData);
		player.getTransformCreated().add(transformationCommonData);
		return transformationCommonData;
	}

	public boolean hasTransformation(int transformationId) {
		return transformations.containsKey(transformationId);
	}

	public void deleteTransformation(Account account, int transformationId) {
		if (hasTransformation(transformationId)) {
			DAOManager.getDAO(AccountTransformDAO.class).deleteTransfo(account, transformationId);
			transformations.remove(transformationId);
		}
	}

	public void updateTransformationsList() {
		transformations.clear();
		for (AccountTransfo transformationCommonData : DAOManager.getDAO(AccountTransformDAO.class).loadAccountTransfo(player.getPlayerAccount()).values()) {
			transformations.put(transformationCommonData.getCardId(), transformationCommonData);
		}
		if (transformations != null) {
			PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(0, player));
		}
	}

	public Map<Integer, AccountTransfo> getTransformationsCreated() {
		return transformationsCreated;
	}
}
