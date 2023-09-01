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
package com.aionemu.gameserver.model.templates.spawns.rvrspawns;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.rvr.RvrStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RvrSpawn")
public class RvrSpawn {

	@XmlAttribute(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	@XmlElement(name = "rvr_type")
	private List<RvrSpawn.RvrStateTemplate> RvrStateTemplate;

	public List<RvrStateTemplate> getSiegeModTemplates() {
		return RvrStateTemplate;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "RvrStateTemplate")
	public static class RvrStateTemplate {

		@XmlElement(name = "spawn")
		private List<Spawn> spawns;

		@XmlAttribute(name = "rstate")
		private RvrStateType rvrType;

		public List<Spawn> getSpawns() {
			return spawns;
		}

		public RvrStateType getRvrType() {
			return rvrType;
		}
	}
}
