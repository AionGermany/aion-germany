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
package com.aionemu.gameserver.model.templates.npc;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TalkInfo")
public class TalkInfo {

	@XmlAttribute(name = "distance")
	private int talkDistance = 2;
	@XmlAttribute(name = "delay")
	private int talkDelay;
	@XmlAttribute(name = "is_dialog")
	private boolean hasDialog;
	@XmlAttribute(name = "func_dialogs")
	private List<Integer> funcDialogIds;
	@XmlAttribute(name = "subdialog_type")
	private String subDialogType;

	/**
	 * @return the talkDistance
	 */
	public int getDistance() {
		return talkDistance;
	}

	/**
	 * @return the talk_delay
	 */
	public int getDelay() {
		return talkDelay;
	}

	/**
	 * @return the hasDialog
	 */
	public boolean isDialogNpc() {
		return hasDialog;
	}

	public List<Integer> getFuncDialogIds() {
		return funcDialogIds;
	}

	public String getSubDialogType() {
		return subDialogType;
	}
}
