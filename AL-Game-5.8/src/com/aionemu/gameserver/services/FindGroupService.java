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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.FindGroup;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.callback.AddPlayerToAllianceCallback;
import com.aionemu.gameserver.model.team2.alliance.callback.PlayerAllianceCreateCallback;
import com.aionemu.gameserver.model.team2.alliance.callback.PlayerAllianceDisbandCallback;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.callback.AddPlayerToGroupCallback;
import com.aionemu.gameserver.model.team2.group.callback.PlayerGroupCreateCallback;
import com.aionemu.gameserver.model.team2.group.callback.PlayerGroupDisbandCallback;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * Find Group Service
 *
 * @author cura, MrPoke
 * @modified teenwolf
 */
public class FindGroupService {

	private FastMap<Integer, FindGroup> elyosRecruitFindGroups = new FastMap<Integer, FindGroup>().shared();
	private FastMap<Integer, FindGroup> elyosApplyFindGroups = new FastMap<Integer, FindGroup>().shared();
	private FastMap<Integer, FindGroup> asmodianRecruitFindGroups = new FastMap<Integer, FindGroup>().shared();
	private FastMap<Integer, FindGroup> asmodianApplyFindGroups = new FastMap<Integer, FindGroup>().shared();

	private FindGroupService() {

		GlobalCallbackHelper.addCallback(new FindGroupOnAddPlayerToGroupListener());
		GlobalCallbackHelper.addCallback(new FindGroupPlayerGroupdDisbandListener());
		GlobalCallbackHelper.addCallback(new FindGroupPlayerGroupdCreateListener());
		GlobalCallbackHelper.addCallback(new FindGroupOnAddPlayerToAllianceListener());
		GlobalCallbackHelper.addCallback(new FindGroupAllianceDisbandListener());
		GlobalCallbackHelper.addCallback(new FindGroupAllianceCreateListener());
	}

	public void addFindGroupList(Player player, int action, String message, int groupType) {
		AionObject object = null;
		if (player.isInTeam()) {
			object = player.getCurrentTeam();
		}
		else {
			object = player;
		}

		FindGroup findGroup = new FindGroup(object, message, groupType);
		int objectId = object.getObjectId();
		switch (player.getRace()) {
			case ELYOS:
				switch (action) {
					case 0x02:
						elyosRecruitFindGroups.put(objectId, findGroup);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400392));
						break;
					case 0x06:
						elyosApplyFindGroups.put(objectId, findGroup);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400393));
						break;
				}
				break;
			case ASMODIANS:
				switch (action) {
					case 0x02:
						asmodianRecruitFindGroups.put(objectId, findGroup);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400392));
						break;
					case 0x06:
						asmodianApplyFindGroups.put(objectId, findGroup);
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400393));
						break;
				}
				break;
			default:
				break;
		}

		Collection<FindGroup> findGroupList = new ArrayList<FindGroup>();
		findGroupList.add(findGroup);

		PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(action, ((int) (System.currentTimeMillis() / 1000)), findGroupList));
	}

	public void updateFindGroupList(Player player, String message, int action, int groupType, int objectId) {
		FindGroup findGroup = null;

		switch (player.getRace()) {
			case ELYOS:
				switch (action) {
					case 0x03:
						findGroup = elyosRecruitFindGroups.get(objectId);
						findGroup.setMessage(message);
						findGroup.setGroupType(groupType);
						break;
					case 0x07:
						findGroup = elyosApplyFindGroups.get(objectId);
						findGroup.setMessage(message);
						findGroup.setGroupType(groupType);
						break;
				}
				break;
			case ASMODIANS:
				switch (action) {
					case 0x03:
						findGroup = asmodianRecruitFindGroups.get(objectId);
						findGroup.setMessage(message);
						findGroup.setGroupType(groupType);
						break;
					case 0x07:
						findGroup = asmodianApplyFindGroups.get(objectId);
						findGroup.setMessage(message);
						findGroup.setGroupType(groupType);
						break;
				}
				break;
			default:
				break;
		}
	}

	public Collection<FindGroup> getFindGroups(Race race, int action) {
		switch (race) {
			case ELYOS:
				switch (action) {
					case 0x00:
						return elyosRecruitFindGroups.values();
					case 0x04:
						return elyosApplyFindGroups.values();
					case 0xA:
						return Collections.emptyList();
				}
				break;
			case ASMODIANS:
				switch (action) {
					case 0x00:
						return asmodianRecruitFindGroups.values();
					case 0x04:
						return asmodianApplyFindGroups.values();
					case 0xA:
						return Collections.emptyList();
				}
				break;
			default:
				break;
		}
		return null;
	}

	public void registerInstanceGroup(Player player, int action, int instanceId, String message, int minMembers, int groupType) {
		AutoGroupType agt = AutoGroupType.getAGTByMaskId(instanceId);
		if (agt != null) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceId, 1, 0, player.getName()));
		}
	}

	public void sendFindGroups(Player player, int action) {
		PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(action, (int) (System.currentTimeMillis() / 1000), getFindGroups(player.getRace(), action)));
	}

	public FindGroup removeFindGroup(final Race race, int action, int playerObjId) {
		FindGroup findGroup = null;
		switch (race) {
			case ELYOS:
				switch (action) {
					case 0x00:
						findGroup = elyosRecruitFindGroups.remove(playerObjId);
						break;
					case 0x04:
						findGroup = elyosApplyFindGroups.remove(playerObjId);
						break;
				}
				break;
			case ASMODIANS:
				switch (action) {
					case 0x00:
						findGroup = asmodianRecruitFindGroups.remove(playerObjId);
						break;
					case 0x04:
						findGroup = asmodianApplyFindGroups.remove(playerObjId);
						break;
				}
				break;
			default:
				break;
		}
		if (findGroup != null) {
			PacketSendUtility.broadcastFilteredPacket(new SM_FIND_GROUP(action + 1, playerObjId, findGroup.getUnk()), new ObjectFilter<Player>() {

					@Override
					public boolean acceptObject(Player object) {
						return race == object.getRace();
					}
				});
		}
		return findGroup;
	}

	public void clean() {
		cleanMap(elyosRecruitFindGroups, Race.ELYOS, 0x00);
		cleanMap(elyosApplyFindGroups, Race.ELYOS, 0x04);
		cleanMap(asmodianRecruitFindGroups, Race.ASMODIANS, 0x00);
		cleanMap(asmodianApplyFindGroups, Race.ASMODIANS, 0x04);
	}

	private void cleanMap(FastMap<Integer, FindGroup> map, Race race, int action) {
		for (FindGroup group : map.values()) {
			if (group.getLastUpdate() + 60 * 60 < System.currentTimeMillis() / 1000) {
				removeFindGroup(race, action, group.getObjectId());
			}
		}
	}

	public static final FindGroupService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final FindGroupService instance = new FindGroupService();
	}

	static class FindGroupOnAddPlayerToGroupListener extends AddPlayerToGroupCallback {

		@Override
		public void onBeforePlayerAddToGroup(PlayerGroup group, Player player) {
			FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
			FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
		}

		@Override
		public void onAfterPlayerAddToGroup(PlayerGroup group, Player player) {
			if (group.isFull()) {
				FindGroupService.getInstance().removeFindGroup(group.getRace(), 0, group.getObjectId());
			}
		}
	}

	static class FindGroupPlayerGroupdDisbandListener extends PlayerGroupDisbandCallback {

		@Override
		public void onBeforeGroupDisband(PlayerGroup group) {
			FindGroupService.getInstance().removeFindGroup(group.getRace(), 0, group.getTeamId());
		}

		@Override
		public void onAfterGroupDisband(PlayerGroup group) {
		}
	}

	static class FindGroupPlayerGroupdCreateListener extends PlayerGroupCreateCallback {

		@Override
		public void onBeforeGroupCreate(Player player) {
		}

		@Override
		public void onAfterGroupCreate(Player player) {
			FindGroup inviterFindGroup = FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
			if (inviterFindGroup == null) {
				inviterFindGroup = FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
			}
			if (inviterFindGroup != null) {
				FindGroupService.getInstance().addFindGroupList(player, 0x02, inviterFindGroup.getMessage(), inviterFindGroup.getGroupType());
			}
		}
	}

	static class FindGroupAllianceDisbandListener extends PlayerAllianceDisbandCallback {

		@Override
		public void onBeforeAllianceDisband(PlayerAlliance alliance) {
			FindGroupService.getInstance().removeFindGroup(alliance.getRace(), 0, alliance.getTeamId());
		}

		@Override
		public void onAfterAllianceDisband(PlayerAlliance alliance) {
		}
	}

	static class FindGroupAllianceCreateListener extends PlayerAllianceCreateCallback {

		@Override
		public void onBeforeAllianceCreate(Player player) {
		}

		@Override
		public void onAfterAllianceCreate(Player player) {
			FindGroup inviterFindGroup = FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
			if (inviterFindGroup == null) {
				inviterFindGroup = FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
			}
			if (inviterFindGroup != null) {
				FindGroupService.getInstance().addFindGroupList(player, 0x02, inviterFindGroup.getMessage(), inviterFindGroup.getGroupType());
			}
		}
	}

	static class FindGroupOnAddPlayerToAllianceListener extends AddPlayerToAllianceCallback {

		@Override
		public void onBeforePlayerAddToAlliance(PlayerAlliance alliance, Player player) {
			FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
			FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
		}

		@Override
		public void onAfterPlayerAddToAlliance(PlayerAlliance alliance, Player player) {
			if (alliance.isFull()) {
				FindGroupService.getInstance().removeFindGroup(alliance.getRace(), 0, alliance.getObjectId());
			}
		}
	}
}
