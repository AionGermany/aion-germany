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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.Race;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailTemplate")
public class MailTemplate {

	@XmlElements({ @XmlElement(name = "sender", type = Sender.class), @XmlElement(name = "title", type = Title.class), @XmlElement(name = "header", type = Header.class), @XmlElement(name = "body", type = Body.class), @XmlElement(name = "tail", type = Tail.class) })
	private List<MailPart> mailParts;
	@XmlAttribute(name = "name", required = true)
	protected String name;
	@XmlAttribute(name = "race", required = true)
	protected Race race;
	@XmlTransient
	private Map<MailPartType, MailPart> mailPartsMap = new HashMap<MailPartType, MailPart>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (MailPart part : mailParts) {
			mailPartsMap.put(((IMailFormatter) part).getType(), part);
		}
		mailParts.clear();
		mailParts = null;
	}

	public MailPart getSender() {
		return mailPartsMap.get(MailPartType.SENDER);
	}

	public MailPart getTitle() {
		return mailPartsMap.get(MailPartType.TITLE);
	}

	public MailPart getHeader() {
		return mailPartsMap.get(MailPartType.HEADER);
	}

	public MailPart getBody() {
		return mailPartsMap.get(MailPartType.BODY);
	}

	public MailPart getTail() {
		return mailPartsMap.get(MailPartType.TAIL);
	}

	public String getName() {
		return name;
	}

	public Race getRace() {
		return race;
	}

	public String getFormattedTitle(IMailFormatter customFormatter) {
		return getTitle().getFormattedString(customFormatter);
	}

	public String getFormattedMessage(IMailFormatter customFormatter) {
		String headerStr = getHeader().getFormattedString(customFormatter);
		String bodyStr = getBody().getFormattedString(customFormatter);
		String tailStr = getTail().getFormattedString(customFormatter);
		String message = headerStr;
		if (StringUtils.isEmpty(message)) {
			message = bodyStr;
		}
		else if (!StringUtils.isEmpty(bodyStr)) {
			message += "," + bodyStr;
		}
		if (StringUtils.isEmpty(message)) {
			message = tailStr;
		}
		else if (!StringUtils.isEmpty(tailStr)) {
			message += "," + tailStr;
		}
		return message;
	}
}
