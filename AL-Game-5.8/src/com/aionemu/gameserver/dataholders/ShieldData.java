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
package com.aionemu.gameserver.dataholders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.shield.ShieldTemplate;

/**
 * @author Wakizashi
 */
@XmlRootElement(name = "shields")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShieldData {

	@XmlElement(name = "shield")
	private List<ShieldTemplate> shieldTemplates;

	public int size() {
		if (shieldTemplates == null) {
			shieldTemplates = new ArrayList<ShieldTemplate>();
			return 0;
		}
		return shieldTemplates.size();
	}

	public List<ShieldTemplate> getShieldTemplates() {
		if (shieldTemplates == null) {
			return new ArrayList<ShieldTemplate>();
		}
		return shieldTemplates;
	}

	public void addAll(Collection<ShieldTemplate> templates) {
		if (shieldTemplates == null) {
			shieldTemplates = new ArrayList<ShieldTemplate>();
		}
		shieldTemplates.addAll(templates);
	}
}
