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

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

/**
 * @author Falke_34
 */
public abstract class TransformationCommonData extends VisibleObjectTemplate implements IExpirable {

	private final int transformationId;
	private final int transformationObjectId;

	public TransformationCommonData(int transformationId) {
		this.transformationObjectId = IDFactory.getInstance().nextId();
		this.transformationId = transformationId;
	}

	public final int getTransformationId() {
		return transformationId;
	}

	public int getObjectId() {
		return transformationObjectId;
	}

	@Override
	public int getTemplateId() {
		return transformationId;
	}
}
