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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.FindGroup;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author cura, MrPoke
 */
public class SM_FIND_GROUP extends AionServerPacket {

	private int action;
	private int lastUpdate;
	private Collection<FindGroup> findGroups;
	private int groupSize;
	private int unk;
	private int instanceId;

	public SM_FIND_GROUP(int action, int lastUpdate, Collection<FindGroup> findGroups) {
		this.lastUpdate = lastUpdate;
		this.action = action;
		this.findGroups = findGroups;
		this.groupSize = findGroups.size();
	}

	public SM_FIND_GROUP(int action, int lastUpdate, int unk) {
		this.action = action;
		this.lastUpdate = lastUpdate;
		this.unk = unk;
	}

	public SM_FIND_GROUP(int action, int instanceId) {
		this.action = action;
		this.instanceId = instanceId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(action);
		switch (action) {
			case 0x00:
				writeH(groupSize); // groupSize
				writeH(groupSize); // groupSize
				writeD(lastUpdate); // objId?
				for (FindGroup findGroup : findGroups) {
					writeD(findGroup.getObjectId()); // player object id
					writeD(findGroup.getUnk()); // unk (0 or 65557)
					writeC(findGroup.getGroupType()); // 0:group, 1:alliance
					writeS(findGroup.getMessage()); // text
					writeS(findGroup.getName()); // writer name
					writeC(findGroup.getSize()); // members count
					writeC(findGroup.getMinLevel()); // members // level
					writeC(findGroup.getMaxLevel()); // members // level
					writeD(findGroup.getLastUpdate()); // objId?
				}
				break;
			case 0x01:
				writeD(lastUpdate); // player object id
				writeD(unk); // unk (0 or 65557)
				break;
			case 0x04:
				writeH(groupSize); // groupSize
				writeH(groupSize); // groupSize
				writeD(lastUpdate); // objId?
				for (FindGroup findGroup : findGroups) {
					writeD(findGroup.getObjectId()); // player object id
					writeC(findGroup.getGroupType()); // 0:group, 1:alliance
					writeS(findGroup.getMessage()); // text
					writeS(findGroup.getName()); // writer name
					writeC(findGroup.getClassId()); // player class id
					writeC(findGroup.getMinLevel()); // player level
					writeD(findGroup.getLastUpdate()); // objId?
				}
				break;
			case 0x05:
				writeD(lastUpdate); // player object id
				break;
			case 0x0A: // registered Groups
				writeH(groupSize);// size
				writeH(groupSize);// size
				writeD(lastUpdate);
				for (FindGroup findGroup : findGroups) {
					writeD(0);// groupregisteredId
					writeD(findGroup.getInstanceId());// instanceId
					writeD(0);// unk
					writeC(findGroup.getSize());// currentMembers
					writeC(findGroup.getMinMembers());// minMembers
					writeH(0);// unk maybe spacer
					writeD(findGroup.getObjectId());// playerObjId
					writeD(1);// unk
					writeD(0);// unk
					writeC(findGroup.getMinLevel());// playerLevel
					writeC(findGroup.getMaxLevel());// playerLevel
					writeH(0);// unk maybe spacer?
					writeD(findGroup.getLastUpdate());// lastUpdate
					writeD(0);// unk
					writeS(findGroup.getName());// writerName
					writeS(findGroup.getMessage());// Message
				}
				break;
			case 0x0E: // register new InstanceGroup
				writeC(1);// packetNumber 0 || 1 || 2
				for (FindGroup findGroup : findGroups) {
					writeD(0);// entryId? counts forwards every entry
					writeD(findGroup.getInstanceId());// instanceId
					writeD(1);// position?
					writeC(findGroup.getSize());// Maybe Members in Group?
					writeC(findGroup.getMinMembers());// min members to enter Instance(writer choose it)
					writeH(0);// unk maybe spacer
					writeH(50); // unk
					writeD(findGroup.getObjectId());// playerObjId leader ID?
					writeC(1);// unk
					writeC(findGroup.getMinLevel());// player level
					writeC(findGroup.getMaxLevel());// player level
					writeC(0);//unk
					writeD(0);// unk
					writeD(findGroup.getLastUpdate());// timestamp
					writeD(0);// unk
					writeD(0);// unk
					writeD(0);// unk
					writeS(findGroup.getName());// writer name TODO RECHECK
					writeS(findGroup.getMessage());// register message TODO RECHECK
				}
				break;
			case 0x10:
				writeH(groupSize);// size
				writeH(groupSize);// size
				writeD(lastUpdate);// systemcurrentimemillis
				for (FindGroup findGroup : findGroups) {
					writeD(0);// groupId?
					writeD(findGroup.getInstanceId());// instanceId
					writeD(findGroup.getObjectId());// playerObjId
					writeD(findGroup.getMinLevel());// playerLevel
					writeD(1);// unk
					writeH(1);// unk
					writeC(findGroup.getGroupType());// groupType?
					writeC(findGroup.getClassId());// classId?
					writeS(findGroup.getName());// writerName
				}
				break;
			case 0x12: // New 5.6 ?
			case 0x16:
				writeD(0);// GroupEntryId
				writeD(0);// instanceId
				break;
			case 0x17:
				writeD(0);// GroupEntryId
				writeD(0);// instanceId
				writeC(3);// TODO
				break;
			case 0x18:
				writeD(0);// GroupObjId
				writeD(0);// instanceId
				writeC(0);// classId?
				for (FindGroup findGroup : findGroups) {
					writeD(0);// GroupRegisteredId
					writeD(findGroup.getInstanceId());// instanceId
					writeD(findGroup.getObjectId());// playerObjId
					writeD(findGroup.getMinLevel());// playerLevel
					writeD(findGroup.getClassId());// classId
					writeH(50);// unk
					writeC(0);// unk
					writeC(1);// unk
					writeH(438);// unk
					writeH(1);// unk
					writeD(0);// unk
					writeS(findGroup.getName());// writerName
					writeC(0);// unk
				}
				break;
			case 0x1A:
				writeH(1);// unk
				writeD(instanceId);
				break;
		}
	}
}
