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

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.CreatureType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.NpcEquippedGear;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.TownService;

/**
 * This packet is displaying visible npc/monsters.
 *
 * @author -Nemesiss-
 */
public class SM_NPC_INFO extends AionServerPacket {

	/**
	 * Visible npc
	 */
	private Creature _npc;
	private NpcTemplate npcTemplate;
	private int npcId;
	private int creatorId;
	private String masterName = StringUtils.EMPTY;
	@SuppressWarnings("unused")
	private float speed = 0.3f;
	private int npcTypeId;

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 *
	 * @param player
	 * @param kisk
	 *            - the visible npc.
	 */
	public SM_NPC_INFO(Npc npc, Player player) {
		this._npc = npc;
		npcTemplate = npc.getObjectTemplate();
		npcTypeId = npc.getType(player);
		npcId = npc.getNpcId();
		creatorId = npc.getCreatorId();
		masterName = npc.getMasterName();
	}

	/**
	 * @param summon
	 */
	public SM_NPC_INFO(Summon summon, Player player) {
		this._npc = summon;
		npcTemplate = summon.getObjectTemplate();
		npcId = summon.getNpcId();
		Player owner = summon.getMaster();
		npcTypeId = !player.isEnemy(owner) ? CreatureType.SUPPORT.getId() : CreatureType.ATTACKABLE.getId();
		if (owner != null) {
			creatorId = owner.getObjectId();
			masterName = owner.getName();
			speed = owner.getGameStats().getMovementSpeedFloat();
		}
		else {
			masterName = "LOST";
		}
	}

	/**
	 * @param add
	 *            mob
	 */
	public SM_NPC_INFO(Npc npc, String master) {
		this._npc = npc;
		npcTemplate = npc.getObjectTemplate();
		npcTypeId = CreatureType.ATTACKABLE.getId();
		npcId = npc.getNpcId();
		masterName = master;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeF(_npc.getX());// x
		writeF(_npc.getY());// y
		writeF(_npc.getZ());// z
		writeD(_npc.getObjectId());
		writeD(npcId);
		writeD(npcId);

		writeC(npcTypeId);

		writeH(_npc.getState());// unk 65=normal,0x47 (71)= [dead npc ?]no drop,0x21(33)=fight state,0x07=[dead
		// monster?]
		// no drop
		// 3,19 - wings spread (NPCs)
		// 5,6,11,21 - sitting (NPC)
		// 7,23 - dead (no drop)
		// 8,24 - [dead][NPC only] - looks like some orb of light (no normal mesh)
		// 32,33 - fight mode

		writeC(_npc.getHeading());
		writeD(npcTemplate.getNameId());
		writeD(npcTemplate.getTitleId());// TODO: implement fortress titles

		writeH(0x00);// unk
		writeC(0x00);// unk
		writeD(0x00);// unk

		/*
		 * Creator/Master Info (Summon, Kisk, Etc)
		 */
		writeD(creatorId);// creatorId - playerObjectId or House address
		writeS(masterName);// masterName

		int maxHp = _npc.getLifeStats().getMaxHp();
		int currHp = _npc.getLifeStats().getCurrentHp();

		writeC((int) (100f * currHp / maxHp));// %hp
		writeD(_npc.getGameStats().getMaxHp().getCurrent());
		writeC(_npc.getLevel());// lvl

		NpcEquippedGear gear = npcTemplate.getEquipment();
		boolean hasWeapon = false;
		BoundRadius boundRadius = npcTemplate.getBoundRadius();

		if (gear == null) {
			writeD(0x00);// unk 4.6
			writeF(boundRadius.getFront());
		}
		else {
			writeD(gear.getItemsMask());
			for (Entry<ItemSlot, ItemTemplate> item : gear) // getting it from template ( later if we make sure that npcs
			// actually use items, we'll make Item from it )
			{
				if (item.getValue().getWeaponType() != null) {
					hasWeapon = true;
				}
				writeD(item.getValue().getTemplateId());
				writeD(0x00);
				writeD(0x00);
				writeH(0x00);
				writeH(0x00);
			}
			// we don't know weapon dimensions, just add 0.1
			writeF(boundRadius.getFront() + 0.125f + (hasWeapon ? 0.1f : 0f));
		}

		writeF(npcTemplate.getHeight());
		writeF(_npc.getGameStats().getMovementSpeedFloat());// speed

		writeH(npcTemplate.getAttackDelay());
		writeH(npcTemplate.getAttackDelay());

		if (npcTemplate.getNpcTemplateType() == NpcTemplateType.FLAG) {
			writeC(0x13);
		} 
		else {
			writeC(_npc.isNewSpawn() ? 0x01 : 0x00);
		}

		/**
		 * Movement
		 */
		writeF(_npc.getMoveController().getTargetX2());// x
		writeF(_npc.getMoveController().getTargetY2());// y
		writeF(_npc.getMoveController().getTargetZ2());// z
		writeC(_npc.getMoveController().getMovementMask()); // move type

		SpawnTemplate spawn = _npc.getSpawn();
		if (spawn == null) {
			writeH(0);
		}
		else {
			writeH(spawn.getStaticId());
		}
		writeQ(0);
		writeC(_npc.getVisualState()); // visualState

		/**
		 * 1 : normal (kisk too) 2 : summon 32 : trap 64 : skill area 1024 : holy servant, noble energy
		 */
		if (npcTemplate.getNpcTemplateType() == NpcTemplateType.FLAG) {
			writeH(0x13);
		} 
		else {
			writeH(_npc.getNpcObjectType().getId());
		}
		writeC(0x00); // unk
		writeD(_npc.getTarget() == null ? 0 : _npc.getTarget().getObjectId());
		writeD(TownService.getInstance().getTownIdByPosition(_npc));
		writeD(0);// unk 4.7.5
	}
}
