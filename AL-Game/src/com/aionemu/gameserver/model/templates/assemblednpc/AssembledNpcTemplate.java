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
package com.aionemu.gameserver.model.templates.assemblednpc;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssembledNpcTemplate")
public class AssembledNpcTemplate {

	@XmlAttribute(name = "nr")
	private int nr;
	@XmlAttribute(name = "routeId")
	private int routeId;
	@XmlAttribute(name = "mapId")
	private int mapId;
	@XmlAttribute(name = "liveTime")
	private int liveTime;
	@XmlElement(name = "assembled_part")
	private List<AssembledNpcPartTemplate> parts;

	public int getNr() {
		return nr;
	}

	public int getRouteId() {
		return routeId;
	}

	public int getMapId() {
		return mapId;
	}

	public int getLiveTime() {
		return liveTime;
	}

	public List<AssembledNpcPartTemplate> getAssembledNpcPartTemplates() {
		return parts;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AssembledNpcPart")
	public static class AssembledNpcPartTemplate {

		@XmlAttribute(name = "npcId")
		private int npcId;
		@XmlAttribute(name = "staticId")
		private int staticId;

		public int getNpcId() {
			return npcId;
		}

		public int getStaticId() {
			return staticId;
		}
	}
}
