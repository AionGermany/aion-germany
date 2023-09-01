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
package com.aionemu.gameserver.model.team2.alliance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.alliance.callback.AddPlayerToAllianceCallback;
import com.aionemu.gameserver.model.team2.alliance.callback.PlayerAllianceCreateCallback;
import com.aionemu.gameserver.model.team2.alliance.callback.PlayerAllianceDisbandCallback;
import com.aionemu.gameserver.model.team2.alliance.events.AllianceDisbandEvent;
import com.aionemu.gameserver.model.team2.alliance.events.AssignViceCaptainEvent;
import com.aionemu.gameserver.model.team2.alliance.events.AssignViceCaptainEvent.AssignType;
import com.aionemu.gameserver.model.team2.alliance.events.ChangeAllianceLeaderEvent;
import com.aionemu.gameserver.model.team2.alliance.events.ChangeAllianceLootRulesEvent;
import com.aionemu.gameserver.model.team2.alliance.events.ChangeMemberGroupEvent;
import com.aionemu.gameserver.model.team2.alliance.events.CheckAllianceReadyEvent;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerAllianceInvite;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerAllianceLeavedEvent;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerAllianceUpdateEvent;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerConnectedEvent;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerDisconnectedEvent;
import com.aionemu.gameserver.model.team2.alliance.events.PlayerEnteredEvent;
import com.aionemu.gameserver.model.team2.common.events.PlayerLeavedEvent.LeaveReson;
import com.aionemu.gameserver.model.team2.common.events.ShowBrandEvent;
import com.aionemu.gameserver.model.team2.common.events.TeamCommand;
import com.aionemu.gameserver.model.team2.common.events.TeamKinahDistributionEvent;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.TimeUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerAllianceService {

	private static final Logger log = LoggerFactory.getLogger(PlayerAllianceService.class);
	private static final Map<Integer, PlayerAlliance> alliances = new ConcurrentHashMap<Integer, PlayerAlliance>();
	private static final AtomicBoolean offlineCheckStarted = new AtomicBoolean();

	public static final void inviteToAlliance(final Player inviter, final Player invited) {
		if (canInvite(inviter, invited)) {
			PlayerAllianceInvite invite = new PlayerAllianceInvite(inviter, invited);
			if (invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_MSGBOX_FORCE_INVITE_PARTY, invite)) {
				if (invited.isInGroup2()) {
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_INVITED_HIS_PARTY(invited.getName()));
				}
				else {
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITED_HIM(invited.getName()));
				}
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSGBOX_FORCE_INVITE_PARTY, 0, 0, inviter.getName()));
			}
		}
	}

	public static final boolean canInvite(Player inviter, Player invited) {
		if (inviter.isInInstance()) {
			if (AutoGroupService.getInstance().isAutoInstance(inviter.getInstanceId())) {
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
				return false;
			}
		}
		if (invited.isInInstance()) {
			if (AutoGroupService.getInstance().isAutoInstance(invited.getInstanceId())) {
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
				return false;
			}
		}
		PlayerAlliance alliance = inviter.getPlayerAlliance2();
		if (alliance != null && alliance.getTeamType().isDefence()) {
			if (invited.isInTeam()) {
				for (Player tm : invited.getCurrentTeam().getMembers()) {
					if (tm.isInInstance()) {
						// You cannot invite the player to the force as the group leader of the player is in an Instanced Zone.
						PacketSendUtility.sendPacket(inviter, new SM_SYSTEM_MESSAGE(1400128));
						return false;
					}
					else if (!VortexService.getInstance().isInsideVortexZone(tm)) {
						// TODO: chk on retail
						PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(tm.getName()));
						return false;
					}
				}
			}
			else if (!VortexService.getInstance().isInsideVortexZone(invited)) {
				// You cannot invite someone in a different area.
				PacketSendUtility.sendPacket(inviter, new SM_SYSTEM_MESSAGE(1401527));
				return false;
			}
		}
		return RestrictionsManager.canInviteToAlliance(inviter, invited);
	}

	@GlobalCallback(PlayerAllianceCreateCallback.class)
	public static final PlayerAlliance createAlliance(Player leader, Player invited, TeamType type) {
		PlayerAlliance newAlliance = new PlayerAlliance(new PlayerAllianceMember(leader), type);
		alliances.put(newAlliance.getTeamId(), newAlliance);
		addPlayer(newAlliance, leader);
		addPlayer(newAlliance, invited);
		if (offlineCheckStarted.compareAndSet(false, true)) {
			initializeOfflineCheck();
		}
		return newAlliance;
	}

	private static void initializeOfflineCheck() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new OfflinePlayerAllianceChecker(), 1000, 30 * 1000);
	}

	@GlobalCallback(AddPlayerToAllianceCallback.class)
	public static final void addPlayerToAlliance(PlayerAlliance alliance, Player invited) {
		// TODO leader member is already set
		alliance.addMember(new PlayerAllianceMember(invited));
	}

	/**
	 * Change alliance's loot rules and notify team members
	 */
	public static final void changeGroupRules(PlayerAlliance alliance, LootGroupRules lootRules) {
		alliance.onEvent(new ChangeAllianceLootRulesEvent(alliance, lootRules));
	}

	/**
	 * Player entered world - search for non expired alliance
	 */
	public static final void onPlayerLogin(Player player) {
		for (PlayerAlliance alliance : alliances.values()) {
			PlayerAllianceMember member = alliance.getMember(player.getObjectId());
			if (member != null) {
				alliance.onEvent(new PlayerConnectedEvent(alliance, player));
			}
		}
	}

	/**
	 * Player leaved world - set last online on member
	 */
	public static final void onPlayerLogout(Player player) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			PlayerAllianceMember member = alliance.getMember(player.getObjectId());
			member.updateLastOnlineTime();
			alliance.onEvent(new PlayerDisconnectedEvent(alliance, player));
		}
	}

	/**
	 * Update alliance members to some event of player
	 */
	public static final void updateAlliance(Player player, PlayerAllianceEvent allianceEvent) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new PlayerAllianceUpdateEvent(alliance, player, allianceEvent));
		}
	}

	/**
	 * Add player to alliance
	 */
	public static final void addPlayer(PlayerAlliance alliance, Player player) {
		Preconditions.checkNotNull(alliance, "Alliance should not be null");
		alliance.onEvent(new PlayerEnteredEvent(alliance, player));
	}

	/**
	 * Remove player from alliance (normal leave, or kick offline player)
	 */
	public static final void removePlayer(Player player) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			if (alliance.getTeamType().isDefence()) {
				VortexService.getInstance().removeDefenderPlayer(player);
			}
			alliance.onEvent(new PlayerAllianceLeavedEvent(alliance, player));
		}
	}

	/**
	 * Remove player from alliance (ban)
	 */
	public static final void banPlayer(Player bannedPlayer, Player banGiver) {
		Preconditions.checkNotNull(bannedPlayer, "Banned player should not be null");
		Preconditions.checkNotNull(banGiver, "Bangiver player should not be null");
		PlayerAlliance alliance = banGiver.getPlayerAlliance2();
		if (alliance != null) {
			if (alliance.getTeamType().isDefence()) {
				VortexService.getInstance().removeDefenderPlayer(bannedPlayer);
			}
			PlayerAllianceMember bannedMember = alliance.getMember(bannedPlayer.getObjectId());
			if (bannedMember != null) {
				alliance.onEvent(new PlayerAllianceLeavedEvent(alliance, bannedMember.getObject(), LeaveReson.BAN, banGiver.getName()));
			}
			else {
				log.warn("TEAM2: banning player not in alliance {}", alliance.onlineMembers());
			}
		}
	}

	/**
	 * Disband alliance after minimum of members has been reached
	 */
	@GlobalCallback(PlayerAllianceDisbandCallback.class)
	public static void disband(PlayerAlliance alliance) {
		Preconditions.checkState(alliance.onlineMembers() <= 1, "Can't disband alliance with more than one online member");
		alliances.remove(alliance.getTeamId());
		alliance.onEvent(new AllianceDisbandEvent(alliance));
	}

	public static void changeLeader(Player player) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new ChangeAllianceLeaderEvent(alliance, player));
		}
	}

	/**
	 * Change vice captain position of player (promote, demote)
	 */
	public static void changeViceCaptain(Player player, AssignType assignType) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new AssignViceCaptainEvent(alliance, player, assignType));
		}
	}

	public static final PlayerAlliance searchAlliance(Integer playerObjId) {
		for (PlayerAlliance alliance : alliances.values()) {
			if (alliance.hasMember(playerObjId)) {
				return alliance;
			}
		}
		return null;
	}

	/**
	 * Move members between alliance groups
	 */
	public static void changeMemberGroup(Player player, int firstPlayer, int secondPlayer, int allianceGroupId) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		Preconditions.checkNotNull(alliance, "Alliance should not be null for group change");
		if (alliance.isLeader(player) || alliance.isViceCaptain(player)) {
			alliance.onEvent(new ChangeMemberGroupEvent(alliance, firstPlayer, secondPlayer, allianceGroupId));
		}
		else {
			PacketSendUtility.sendMessage(player, "You do not have the authority for that.");
		}
	}

	/**
	 * Check that alliance is ready
	 */
	public static void checkReady(Player player, TeamCommand eventCode) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new CheckAllianceReadyEvent(alliance, player, eventCode));
		}
	}

	/**
	 * Share specific amount of kinah between alliance members
	 */
	public static void distributeKinah(Player player, long amount) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new TeamKinahDistributionEvent<PlayerAlliance>(alliance, player, amount));
		}
	}

	public static void distributeKinahInGroup(Player player, long amount) {
		PlayerAllianceGroup allianceGroup = player.getPlayerAllianceGroup2();
		if (allianceGroup != null) {
			allianceGroup.onEvent(new TeamKinahDistributionEvent<PlayerAllianceGroup>(allianceGroup, player, amount));
		}
	}

	/**
	 * Show specific mark on top of player
	 */
	public static void showBrand(Player player, int targetObjId, int brandId) {
		PlayerAlliance alliance = player.getPlayerAlliance2();
		if (alliance != null) {
			alliance.onEvent(new ShowBrandEvent<PlayerAlliance>(alliance, targetObjId, brandId));
		}
	}

	public static final String getServiceStatus() {
		return "Number of alliances: " + alliances.size();
	}

	public static class OfflinePlayerAllianceChecker implements Runnable, Predicate<PlayerAllianceMember> {

		private PlayerAlliance currentAlliance;

		@Override
		public void run() {
			for (PlayerAlliance alliance : alliances.values()) {
				currentAlliance = alliance;
				alliance.apply(this);
			}
			currentAlliance = null;
		}

		@Override
		public boolean apply(PlayerAllianceMember member) {
			int kickDelay = currentAlliance.getTeamType().isAutoTeam() ? 60 : GroupConfig.ALLIANCE_REMOVE_TIME;
			if (!member.isOnline() && TimeUtil.isExpired(member.getLastOnlineTime() + kickDelay * 1000)) {
				if (currentAlliance.getTeamType().isOffence()) {
					VortexService.getInstance().removeInvaderPlayer(member.getObject());
				}
				currentAlliance.onEvent(new PlayerAllianceLeavedEvent(currentAlliance, member.getObject(), LeaveReson.LEAVE_TIMEOUT));
			}
			return true;
		}
	}
}
