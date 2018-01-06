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
package com.aionemu.gameserver.model.templates.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SysMail", propOrder = { "templates" })
public class SysMail {

	@XmlElement(name = "template", required = true)
	private List<MailTemplate> templates;
	@XmlAttribute(name = "name", required = true)
	private String name;
	@XmlTransient
	private Map<String, List<MailTemplate>> mailCaseTemplates = new HashMap<String, List<MailTemplate>>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (MailTemplate template : templates) {
			String caseName = template.getName().toLowerCase();
			List<MailTemplate> sysTemplates = mailCaseTemplates.get(caseName);
			if (sysTemplates == null) {
				sysTemplates = new ArrayList<MailTemplate>();
				mailCaseTemplates.put(caseName, sysTemplates);
			}
			sysTemplates.add(template);
		}
		templates.clear();
		templates = null;
	}

	public MailTemplate getTemplate(String eventName, Race playerRace) {
		List<MailTemplate> sysTemplates = mailCaseTemplates.get(eventName.toLowerCase());
		if (sysTemplates == null) {
			return null;
		}
		for (MailTemplate template : sysTemplates) {
			if (template.getRace() == playerRace || template.getRace() == Race.PC_ALL) {
				return template;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
}
