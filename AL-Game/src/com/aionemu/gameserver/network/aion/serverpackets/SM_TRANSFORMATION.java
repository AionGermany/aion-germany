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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.account.AccountTransfo;
import com.aionemu.gameserver.model.account.AccountTransformList;
import com.aionemu.gameserver.model.account.TransformCollection;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_TRANSFORMATION extends AionServerPacket {

	private int action;
	private AccountTransformList transformList;
	private int cardId;
	private int result;
	private Player player;
	private int code;
	List<Integer> deleteList;
	List<AccountTransfo> createdTransform;

	public SM_TRANSFORMATION(int action, Player player) {
		this.deleteList = (List<Integer>) new FastList<Integer>();
		this.createdTransform = (List<AccountTransfo>) new FastList<AccountTransfo>();
		this.action = action;
		this.transformList = player.getTransformList();
		this.player = player;
	}

	public SM_TRANSFORMATION(int action, Player player, int cardId, int result, int code) {
		this.deleteList = (List<Integer>) new FastList<Integer>();
		this.createdTransform = (List<AccountTransfo>) new FastList<AccountTransfo>();
		this.action = action;
		this.transformList = player.getTransformList();
		this.cardId = cardId;
		this.result = result;
		this.player = player;
		this.code = code;
	}

	public SM_TRANSFORMATION(List<AccountTransfo> createdTransform, int result, int code) {
		this.deleteList = (List<Integer>) new FastList<Integer>();
		this.createdTransform = (List<AccountTransfo>) new FastList<AccountTransfo>();
		this.action = 1;
		this.result = result;
		this.code = code;
		this.createdTransform = createdTransform;
	}

	public SM_TRANSFORMATION(int action, int cardId) {
		this.deleteList = (List<Integer>) new FastList<Integer>();
		this.createdTransform = (List<AccountTransfo>) new FastList<AccountTransfo>();
		this.action = action;
		this.cardId = cardId;
	}

	public SM_TRANSFORMATION(int action, List<Integer> deleteList) {
		this.deleteList = (List<Integer>) new FastList<Integer>();
		this.createdTransform = (List<AccountTransfo>) new FastList<AccountTransfo>();
		this.action = action;
		this.deleteList = deleteList;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(action);
		switch (action) {
		case 0: {
			writeD(player.getLastUsedTransformation());
			writeC(0);
			if (transformList != null && transformList.getTransformations().size() != 0) {
				writeH(transformList.getTransformations().size());
				for (AccountTransfo transfo : transformList.getTransformations()) {
					writeD(transfo.getCardId());
					writeD(transfo.getCount());
				}
				break;
			}
			writeH(0);
			break;
		}
		case 1: {
			writeH(result);
			switch (result) {
			case 1: {
				int count = 0;
				for (AccountTransfo transfo2 : createdTransform) {
					writeD(transfo2.getCardId());
					++count;
				}
				for (int i = 0; i < 10 - count; ++i) {
					writeD(0);
				}
				break;
			}
			case 3: {
				writeD(cardId);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				break;
			}
			}
			writeD(code);
			break;
		}
		case 2: {
			writeH(deleteList.size());
			for (Integer trans : deleteList) {
				writeD((int) trans);
				writeD(1);
			}
			break;
		}
		case 3: {
			if (player.getTransformCollections().size() == 0) {
				writeB(new byte[DataManager.TRANSFORM_COLLECTION_DATA.size() * 4]);
				break;
			}
			for (TransformCollection collection : player.getTransformCollections().values()) {
				writeD(collection.getId());
			}
			for (int diff = DataManager.TRANSFORM_COLLECTION_DATA.size() - player.getTransformCollections().size(), i = 0; i < diff; ++i) {
				writeD(0);
			}
			break;
		}
		}
	}
}