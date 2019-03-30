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


package com.aionemu.packetsamurai.utils.collector.objects;

public class VisibleObject {
	protected int objectId;
	protected int templateId;
	protected int staticId;

	/**
	 * @param objectId
	 * @param templateId
	 * @param staticId
	 */
	public VisibleObject(int objectId, int templateId, int staticId) {
		this.objectId = objectId;
		this.templateId = templateId;
		this.staticId = staticId;
	}

	/**
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}
	/**
	 * @return the templateId
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * @return the staticId
	 */
	public int getStaticId() {
		return staticId;
	}
	
}
