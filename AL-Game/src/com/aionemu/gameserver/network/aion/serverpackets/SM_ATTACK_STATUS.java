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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author alexa026
 * @author ATracer
 * @author kecimis
 */
public class SM_ATTACK_STATUS extends AionServerPacket {

	private Creature creature;
	private TYPE type;
	private int skillId;
	private int value;
	private int logId;

	public static enum TYPE {

		NATURAL_HP(3),
		USED_HP(4), // when skill uses hp as cost parameter
		REGULAR(5),
		ABSORBED_HP(6),
		HP(7),
		DAMAGE(7),
		PROTECTDMG(8),
		DELAYDAMAGE(10),
		FALL_DAMAGE(17),
		HEAL_MP(19),
		ABSORBED_MP(20),
		MP(21),
		NATURAL_MP(22),
		ATTACK(23),
		FP_RINGS(24),
		FP(25),
		NATURAL_FP(27); //Old 26 New 5.4 = 27

		private int value;

		private TYPE(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public static enum LOG {

		SPELLATK(1),
		HEAL(3),
		MPHEAL(4),
		SKILLLATKDRAININSTANT(23),
		SPELLATKDRAININSTANT(24),
		POISON(25),
		BLEED(26),
		PROCATKINSTANT(93), // Old 92 New 93
		DELAYEDSPELLATKINSTANT(97), // Old 95 New 97
		SPELLATKDRAIN(130),
		FPHEAL(133),
		REGULARHEAL(170),
		REGULAR(189),
		ATTACK(197); // Old 195 (5.4) 196 (5.6) 197 (5.8)

		private int value;

		private LOG(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value, LOG log) {
		this.creature = creature;
		this.type = type;
		this.skillId = skillId;
		this.value = value;
		this.logId = log.getValue();
	}

	public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value) {
		this(creature, type, skillId, value, LOG.REGULAR);
	}

	public SM_ATTACK_STATUS(Creature creature, int value) {
		this(creature, TYPE.REGULAR, 0, value, LOG.REGULAR);
	}

	/**
	 * {@inheritDoc} ddchcc
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		if (type.getValue() == 5 || type.getValue() == 7 || type.getValue() == 10) {
			writeD(creature.getObjectId());
			writeD(con.getActivePlayer().getObjectId());
		}
		else {
			writeD(con.getActivePlayer().getObjectId());
			writeD(0x00);
		}
		switch (type) {
			case ATTACK:
			case DAMAGE:
			case DELAYDAMAGE:
				writeD(-value);
				break;
			default:
				writeD(value);
		}
		writeC(type.getValue());
		writeC(creature.getLifeStats().getHpPercentage());
		writeH(skillId);
		writeH(0);// unk 5.3
		if (skillId != 0) {
			writeH(logId);
		}
		else {
			writeH(LOG.ATTACK.getValue());
		}
	}
	// logId
	// depends on effecttemplate
	// effecttemplate (TYPE) LOG.getValue()
	// spellattack(hp) 1
	// poison(hp) 25
	// delaydamage(hp) 95
	// bleed(hp) 26
	// mp regen(natural_mp) 171
	// hp regen(natural_hp) 171
	// fp regen(natural_fp) 171
	// fp pot(fp) 171
	// prochp(hp) 171
	// procmp(mp) 171
	// heal_instant (regular) 171
	// SpellAtkDrainInstantEffect(absorbed_mp) 24(refactoring shard)
	// mpheal(mp) 4
	// heal(hp) 3
	// fpheal(fp) 133
	// spellatkdrain(hp) 130
	// falldmg (17) 170
	// mpheal (19) 171
	// hp as cost parameter(4) logId 170
	// procatkinstant - (7) 92
	// protecteffect on protector - (8) 171
	// TODO find rest of logIds
}
