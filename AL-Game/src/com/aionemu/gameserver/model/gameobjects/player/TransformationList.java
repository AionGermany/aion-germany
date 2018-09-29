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
import com.aionemu.gameserver.dao.PlayerTransformationsDAO;

import javolution.util.FastMap;

/**
 * @author Falke_34
 */
public class TransformationList {

	private final Player player;
	private FastMap<Integer, TransformationCommonData> transformations = new FastMap<Integer, TransformationCommonData>();

	TransformationList(Player player) {
		this.player = player;
		loadTransformations();
	}

	public void loadTransformations() {
		for (TransformationCommonData transformationCommonData : DAOManager.getDAO(PlayerTransformationsDAO.class).getPlayerTransformations(player)) {
			transformations.put(transformationCommonData.getObjectId(), transformationCommonData);
		}
	}

	public Collection<TransformationCommonData> getTransformations() {
		return transformations.values();
	}

	/**
	 * @param transformationId
	 * @return
	 */
	public TransformationCommonData getTransformation(int transformationId) {
		return transformations.get(transformationId);
	}

	/**
	 * @param transformationId
	 * @return
	 */
	public boolean hasTransformation(int transformationId) {
		return transformations.containsKey(transformationId);
	}
}
