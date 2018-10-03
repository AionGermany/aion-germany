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
package com.aionemu.gameserver.model.siege;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLocationTemplate;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.SiegeZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.handler.ZoneHandler;

import javolution.util.FastMap;

/**
 * @author Sarynth, Source, Wakizashi
 */
public class SiegeLocation implements ZoneHandler {

	private static final Logger log = LoggerFactory.getLogger(SiegeLocation.class);
	public static final int STATE_INVULNERABLE = 0;
	public static final int STATE_VULNERABLE = 1;
	/**
	 * Unique id, defined by NCSoft
	 */
	protected SiegeLocationTemplate template;
	protected int locationId;
	protected int occupyCount;
	protected SiegeType type;
	protected int worldId;
	protected SiegeRace siegeRace = SiegeRace.BALAUR;
	protected int legionId;
	protected long lastArtifactActivation;
	private boolean vulnerable;
	private int nextState;
	protected List<SiegeZoneInstance> zone;
	private List<SiegeShield> shields;
	protected boolean isUnderShield;
	protected boolean canTeleport;
	protected int siegeDuration;
	protected int influenceValue;
	private FastMap<Integer, Creature> creatures = new FastMap<Integer, Creature>();
	private FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	protected int buffId;
	protected int buffIdA;
	protected int buffIdE;
	protected int baseId;

	public SiegeLocation() {
	}

	public SiegeLocation(SiegeLocationTemplate template) {
		this.template = template;
		this.locationId = template.getId();
		this.worldId = template.getWorldId();
		this.type = template.getType();
		this.siegeDuration = template.getSiegeDuration();
		this.zone = new ArrayList<SiegeZoneInstance>();
		this.influenceValue = template.getInfluenceValue();
		this.occupyCount = template.getOccupyCount();
	}

	public SiegeLocationTemplate getTemplate() {
		return template;
	}

	/**
	 * Returns unique LocationId of Siege Location
	 *
	 * @return Integer LocationId
	 */
	public int getLocationId() {
		return this.locationId;
	}

	public int getWorldId() {
		return this.worldId;
	}

	public SiegeType getType() {
		return this.type;
	}

	public int getSiegeDuration() {
		return this.siegeDuration;
	}

	public SiegeRace getRace() {
		return this.siegeRace;
	}

	public void setRace(SiegeRace siegeRace) {
		this.siegeRace = siegeRace;
	}

	public int getLegionId() {
		return this.legionId;
	}

	public void setLegionId(int legionId) {
		this.legionId = legionId;
	}

	public int getOccupyCount() {
		return this.occupyCount;
	}

	public void setOccupyCount(int occupyCount) {
		this.occupyCount = occupyCount;
	}

	/**
	 * Next State: 0 invulnerable 1 vulnerable
	 *
	 * @return nextState
	 */
	public int getNextState() {
		return nextState;
	}

	public void setNextState(int nextState) {
		this.nextState = nextState;
	}

	/**
	 * @return isVulnerable
	 */
	public boolean isVulnerable() {
		return this.vulnerable;
	}

	/**
	 * @return isUnderShield
	 */
	public boolean isUnderShield() {
		return this.isUnderShield;
	}

	/**
	 * @param value
	 *            new undershield value
	 */
	public void setUnderShield(boolean value) {
		this.isUnderShield = value;
		if (shields != null) {
			for (SiegeShield shield : shields) {
				shield.setEnabled(value);
			}
		}
	}

	public void setShields(List<SiegeShield> shields) {
		this.shields = shields;
		log.debug("Attached shields for locId: " + locationId);
		for (SiegeShield shield : shields) {
			log.debug(shield.toString());
		}
	}

	/**
	 * @return the canTeleport
	 */
	public boolean isCanTeleport(Player player) {
		return canTeleport;
	}

	/**
	 * @param canTeleport
	 *            the canTeleport to set
	 */
	public void setCanTeleport(boolean canTeleport) {
		this.canTeleport = canTeleport;
	}

	/**
	 * @param value
	 *            new vulnerable value
	 */
	public void setVulnerable(boolean value) {
		this.vulnerable = value;
	}

	public int getInfluenceValue() {
		return this.influenceValue;
	}

	/**
	 * @return the zone
	 */
	public List<SiegeZoneInstance> getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void addZone(SiegeZoneInstance zone) {
		this.zone.add(zone);
		zone.addHandler(this);
	}

	public boolean isInsideLocation(Creature creature) {
		if (zone.isEmpty()) {
			return false;
		}
		for (int i = 0; i < zone.size(); i++) {
			if (zone.get(i).isInsideCreature(creature)) {
				return true;
			}
		}
		return false;
	}

	public boolean isInActiveSiegeZone(Player player) {
		if (isVulnerable() && isInsideLocation(player)) {
			return true;
		}

		return false;
	}

	public void clearLocation() {
	}

	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (!creatures.containsKey(creature.getObjectId())) {
			creatures.put(creature.getObjectId(), creature);
			if (creature instanceof Player) {
				players.put(creature.getObjectId(), (Player) creature);
			}
		}
	}

	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		if (!this.isInsideLocation(creature)) {
			creatures.remove(creature.getObjectId());
			players.remove(creature.getObjectId());
		}
	}

	public void doOnAllPlayers(Visitor<Player> visitor) {
		try {
			for (FastMap.Entry<Integer, Player> e = players.head(), mapEnd = players.tail(); (e = e.getNext()) != mapEnd;) {
				Player player = e.getValue();
				if (player != null) {
					visitor.visit(player);
				}
			}
		}
		catch (Exception ex) {
			log.error("Exception when running visitor on all players" + ex);
		}
	}

	/**
	 * @return the creatures
	 */
	public FastMap<Integer, Creature> getCreatures() {
		return creatures;
	}

	/**
	 * @return the players
	 */
	public FastMap<Integer, Player> getPlayers() {
		return players;
	}

	public int getBuffId() {
		return buffId = template.getBuffId();
	}

	public int getBuffIdA() {
		return buffIdA = template.getBuffIdA();
	}

	public int getBuffIdE() {
		return buffIdE = template.getBuffIdE();
	}

	public int getBaseId() {
		return baseId = template.getBaseId();
	}
}
