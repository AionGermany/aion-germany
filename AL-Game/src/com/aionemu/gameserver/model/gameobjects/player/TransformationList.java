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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.TransformationsDAO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORMATION;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author Falke_34, FrozenKiller
 */
public class TransformationList {

	private final Player player;
	private FastMap<Integer, TransformationCommonData> transformations = new FastMap<Integer, TransformationCommonData>();

	public TransformationList(final Player player) {
		this.player = player;
		loadTransformations();
	}

	public void loadTransformations() {
		for (TransformationCommonData transformationCommonData : DAOManager.getDAO(TransformationsDAO.class).getTransformations(player)) {
			transformations.put(transformationCommonData.getObjectId(), transformationCommonData);
		}
	}

	public Collection<TransformationCommonData> getTransformations() {
		return (Collection<TransformationCommonData>) transformations.values();
	}

	public TransformationCommonData getTransformation(int transformationId) {
		return transformations.get(transformationId);
	}

	public TransformationCommonData addNewTransformation(Player player, int transformationId, String name, String grade, int count) {
		TransformationCommonData transformationCommonData = new TransformationCommonData(transformationId, player.getObjectId(), name, grade, count);
		transformationCommonData.setName(name);
		transformationCommonData.setGrade(grade);
		transformationCommonData.setCount(count);
		DAOManager.getDAO(TransformationsDAO.class).insertTransformation(transformationCommonData);
		transformations.put(transformationCommonData.getObjectId(), transformationCommonData);
		return transformationCommonData;
	}

	public boolean hasTransformation(int transformationId) {
		return transformations.containsKey(transformationId);
	}

	public void deleteTransformation(int transformationId) {
		if (hasTransformation(transformationId)) {
			DAOManager.getDAO(TransformationsDAO.class).removeTransformation(player, transformationId);
			transformations.remove(transformationId);
		}
	}

	public void updateTransformationsList() {
		transformations.clear();
		for (TransformationCommonData transformationCommonData : DAOManager.getDAO(TransformationsDAO.class).getTransformations(player)) {
			transformations.put(transformationCommonData.getObjectId(), transformationCommonData);
		}
		if (this.transformations != null) {
			PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(0, player.getTransformationList().getTransformations()));
		}
	}
}
