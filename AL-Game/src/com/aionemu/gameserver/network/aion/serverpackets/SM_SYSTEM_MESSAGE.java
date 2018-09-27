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

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.SystemMessageId;

/**
 * System message packet.
 *
 * @author -Nemesiss-
 * @author EvilSpirit
 * @author Luno :D
 * @author Avol!
 * @author Simple :)
 * @author Sarynth
 */
public final class SM_SYSTEM_MESSAGE extends AionServerPacket {

    private static DescriptionId calculateNameId(int id) {
		return new DescriptionId(id * 2 + 1);
	}


	/**
	 * Skin Animation
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_COSTUME_SKILL_ALREADY_HAS_COSTUME = new SM_SYSTEM_MESSAGE(1403683);


	/**
	 * You inflicted %num1 damage on %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_ATTACK(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1200000, num1, value0);
	}

	/**
	 * Critical Hit! You inflicted %num1 critical damage on %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_CRITICAL(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1200001, num1, value0);
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_REIDENTIFY_CANCELED(final int nameId) {
		return new SM_SYSTEM_MESSAGE(1401638, new Object[] { new DescriptionId(nameId) });
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_IDENTIFY_SUCCEED(final int nameId) {
		return new SM_SYSTEM_MESSAGE(1401626, new Object[] { new DescriptionId(nameId) });
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_REIDENTIFY_SUCCEED(final int nameId) {
		return new SM_SYSTEM_MESSAGE(1401639, new Object[] { new DescriptionId(nameId) });
	}

	/**
	 * %0 inflicted %num2 damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_SUMMONED_ATTACK(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1200002, value0, num2, value1);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_SUMMONED_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1200003, value0, num2, value1);
	}

	/**
	 * %1 received %num2 damage from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_SUMMONED_ENEMY_ATTACK(String value1, int num2, String value0) {
		return new SM_SYSTEM_MESSAGE(1200004, value1, num2, value0);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_SUMMONED_ENEMY_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1200005, value0, num2, value1);
	}

	/**
	 * %0 has been dismissed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_UNSUMMONED(int value0) {
		return new SM_SYSTEM_MESSAGE(1200006, calculateNameId(value0));
	}

	/**
	 * %0 is in Stand-by mode.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_STAY_MODE(String value0) {
		return new SM_SYSTEM_MESSAGE(1200007, value0);
	}

	/**
	 * %0 starts to attack the enemy.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_ATTACK_MODE(int value0) {
		return new SM_SYSTEM_MESSAGE(1200008, calculateNameId(value0));
	}

	/**
	 * %0 is in Guard mode.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_GUARD_MODE(int value0) {
		return new SM_SYSTEM_MESSAGE(1200009, calculateNameId(value0));
	}

	/**
	 * %0 is in Resting mode.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_REST_MODE(int value0) {
		return new SM_SYSTEM_MESSAGE(1200010, calculateNameId(value0));
	}

	/**
	 * You unsummon %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_UNSUMMON_FOLLOWER(int value0) {
		return new SM_SYSTEM_MESSAGE(1200011, calculateNameId(value0));
	}

	/**
	 * You summon %0 Spirit. Cooldown time begins when it is unsummoned, and takes longer when the spirit is killed by an enemy.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_COOLDOWN(String value0) {
		return new SM_SYSTEM_MESSAGE(1200012, value0);
	}

	/**
	 * You are bleeding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BLEED_BEGIN = new SM_SYSTEM_MESSAGE(1200214);
	/**
	 * You are no longer bleeding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BLEED_END = new SM_SYSTEM_MESSAGE(1200215);
	/**
	 * You cannot see.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BLIND_BEGIN = new SM_SYSTEM_MESSAGE(1200216);
	/**
	 * You can see again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BLIND_END = new SM_SYSTEM_MESSAGE(1200217);
	/**
	 * You are charmed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CHARM_BEGIN = new SM_SYSTEM_MESSAGE(1200218);
	/**
	 * You are no longer charmed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CHARM_END = new SM_SYSTEM_MESSAGE(1200219);
	/**
	 * You are confused.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CONFUSE_BEGIN = new SM_SYSTEM_MESSAGE(1200220);
	/**
	 * You are no longer confused.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CONFUSE_END = new SM_SYSTEM_MESSAGE(1200221);
	/**
	 * A defense wall has been created to convert received damage into HP.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CONVERT_HEAL_BEGIN = new SM_SYSTEM_MESSAGE(1200222);
	/**
	 * The defense wall that converts received damage into HP has expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CONVERT_HEAL_END = new SM_SYSTEM_MESSAGE(1200223);
	/**
	 * A defense wall that absorbs damage has been created.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SHIELD_MAGIC_BEGIN = new SM_SYSTEM_MESSAGE(1200224);
	/**
	 * The defense wall that absorbs damage has expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SHIELD_MAGIC_END = new SM_SYSTEM_MESSAGE(1200225);
	/**
	 * You are cursed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CURSE_BEGIN = new SM_SYSTEM_MESSAGE(1200226);
	/**
	 * You are no longer cursed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_CURSE_END = new SM_SYSTEM_MESSAGE(1200227);
	/**
	 * You are diseased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_DISEASE_BEGIN = new SM_SYSTEM_MESSAGE(1200228);
	/**
	 * You recovered from the disease.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_DISEASE_END = new SM_SYSTEM_MESSAGE(1200229);
	/**
	 * You are struck by fear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_FEAR_BEGIN = new SM_SYSTEM_MESSAGE(1200230);
	/**
	 * You recovered from your fear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_FEAR_END = new SM_SYSTEM_MESSAGE(1200231);
	/**
	 * You are invisible.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_INVISIBLE_BEGIN = new SM_SYSTEM_MESSAGE(1200232);
	/**
	 * You are no longer invisible.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_INVISIBLE_END = new SM_SYSTEM_MESSAGE(1200233);
	/**
	 * You are paralyzed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PARALYZE_BEGIN = new SM_SYSTEM_MESSAGE(1200234);
	/**
	 * You are no longer paralyzed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PARALYZE_END = new SM_SYSTEM_MESSAGE(1200235);
	/**
	 * You are petrified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PETRIFICATION_BEGIN = new SM_SYSTEM_MESSAGE(1200236);
	/**
	 * You are no longer petrified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PETRIFICATION_END = new SM_SYSTEM_MESSAGE(1200237);
	/**
	 * You are poisoned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_POISON_BEGIN = new SM_SYSTEM_MESSAGE(1200238);
	/**
	 * You are no longer poisoned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_POISON_END = new SM_SYSTEM_MESSAGE(1200239);
	/**
	 * You are immobilized.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_ROOT_BEGIN = new SM_SYSTEM_MESSAGE(1200240);
	/**
	 * You are no longer immobilized.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_ROOT_END = new SM_SYSTEM_MESSAGE(1200241);
	/**
	 * You fell asleep.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SLEEP_BEGIN = new SM_SYSTEM_MESSAGE(1200242);
	/**
	 * You woke up.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SLEEP_END = new SM_SYSTEM_MESSAGE(1200243);
	/**
	 * You have been stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STUN_BEGIN = new SM_SYSTEM_MESSAGE(1200244);
	/**
	 * You are no longer stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STUN_END = new SM_SYSTEM_MESSAGE(1200245);
	/**
	 * You are silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SILENCE_BEGIN = new SM_SYSTEM_MESSAGE(1200246);
	/**
	 * You are no longer silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SILENCE_END = new SM_SYSTEM_MESSAGE(1200247);
	/**
	 * You are snared in mid-air.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_OPEN_AERIAL_BEGIN = new SM_SYSTEM_MESSAGE(1200248);
	/**
	 * You are released from the Aerial Snare.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_OPEN_AERIAL_END = new SM_SYSTEM_MESSAGE(1200249);
	/**
	 * Your movement speed has decreased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SNARE_BEGIN = new SM_SYSTEM_MESSAGE(1200250);
	/**
	 * You have normal movement speed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SNARE_END = new SM_SYSTEM_MESSAGE(1200251);
	/**
	 * Your attack speed is decreased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SLOW_BEGIN = new SM_SYSTEM_MESSAGE(1200252);
	/**
	 * You have normal attack speed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SLOW_END = new SM_SYSTEM_MESSAGE(1200253);
	/**
	 * You are spinning from shock.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SPIN_BEGIN = new SM_SYSTEM_MESSAGE(1200254);
	/**
	 * You are no longer in shock.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_SPIN_END = new SM_SYSTEM_MESSAGE(1200255);
	/**
	 * You fell down from shock.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STUMBLE_BEGIN = new SM_SYSTEM_MESSAGE(1200256);
	/**
	 * You are no longer in shock.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STUMBLE_END = new SM_SYSTEM_MESSAGE(1200257);
	/**
	 * You are stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STAGGER_BEGIN = new SM_SYSTEM_MESSAGE(1200258);
	/**
	 * You are no longer stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_STAGGER_END = new SM_SYSTEM_MESSAGE(1200259);
	/**
	 * You are bound.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BIND_BEGIN = new SM_SYSTEM_MESSAGE(1200260);
	/**
	 * You are no longer bound.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_BIND_END = new SM_SYSTEM_MESSAGE(1200261);
	/**
	 * You are being pulled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PULLED_BEGIN = new SM_SYSTEM_MESSAGE(1200262);
	/**
	 * You are no longer being pulled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_EFFECT_PULLED_END = new SM_SYSTEM_MESSAGE(1200263);

	/**
	 * You became blinded after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200277, skillname);
	}

	/**
	 * You became confused after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200278, skillname);
	}

	/**
	 * You became diseased after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200279, skillname);
	}

	/**
	 * You are struck with fear after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200280, skillname);
	}

	/**
	 * You became paralyzed after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200281, skillname);
	}

	/**
	 * You became immobilized after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200282, skillname);
	}

	/**
	 * You became silenced after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200283, skillname);
	}

	/**
	 * You fell asleep after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200284, skillname);
	}

	/**
	 * You are spinning after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200285, skillname);
	}

	/**
	 * You were knocked back from a shock after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200286, skillname);
	}

	/**
	 * You fell down from shock after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200287, skillname);
	}

	/**
	 * You became stunned after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200288, skillname);
	}

	/**
	 * You can see again
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200289);
	/**
	 * You are no longer confused.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200290);
	/**
	 * You are no longer diseased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200291);
	/**
	 * You recovered from your fear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200292);
	/**
	 * You are no longer paralyzed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200293);
	/**
	 * You are no longer immobilized.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200294);
	/**
	 * You are no longer silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200295);
	/**
	 * You woke up.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200296);
	/**
	 * You have stopped spinning.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200297);
	/**
	 * You are no longer staggering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200298);
	/**
	 * You are no longer shocked.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200299);
	/**
	 * You are no longer stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200300);

	/**
	 * Your loot rate has increased because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostDropRate_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200301, skillname);
	}

	/**
	 * Your visual range has reduced because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OutofSight_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200302, skillname);
	}

	/**
	 * You exchanged your enmity with the spirit's by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHostile_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200303, skillname);
	}

	/**
	 * You used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ReturnHome_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200304, skillname);
	}

	/**
	 * You began using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200305, skillname);
	}

	/**
	 * You stopped using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_END_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200306, skillname);
	}

	/**
	 * You used [%SkillName] and became bound.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200307, skillname);
	}

	/**
	 * You are bleeding after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200308, skillname);
	}

	/**
	 * You are cursed after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200309, skillname);
	}

	/**
	 * You are unable to fly because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200310, skillname);
	}

	/**
	 * You are snared in mid-air after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200311, skillname);
	}

	/**
	 * You became petrified after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200312, skillname);
	}

	/**
	 * You became poisoned after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200313, skillname);
	}

	/**
	 * Your attack speed has decreased after you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200314, skillname);
	}

	/**
	 * Your movement speed has decreased after you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200315, skillname);
	}

	/**
	 * You are no longer bound
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200316);
	/**
	 * You are no longer bleeding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200317);
	/**
	 * You are released from the cursed state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200318);
	/**
	 * You are able to fly again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200319);
	/**
	 * You are released from the Aerial Snare.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200320);
	/**
	 * You are no longer petrified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200321);
	/**
	 * You are no longer poisoned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200322);
	/**
	 * Your attack speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200323);
	/**
	 * Your movement speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_ME_TO_SELF = new SM_SYSTEM_MESSAGE(1200324);

	/**
	 * You boosted your block by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysBlock_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200325, skillname);
	}

	/**
	 * You boosted your evasion by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysDodge_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200326, skillname);
	}

	/**
	 * You boosted your accuracy by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysHit_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200327, skillname);
	}

	/**
	 * You removed your elemental defense by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysNoResist_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200328, skillname);
	}

	/**
	 * You boosted your parry by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysParry_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200329, skillname);
	}

	/**
	 * You boosted your elemental defense by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysResist_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200330, skillname);
	}

	/**
	 * You boosted your recovery by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHealEffect_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200331, skillname);
	}

	/**
	 * You changed your casting speed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCastingTime_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200332, skillname);
	}

	/**
	 * You changed your MP consumption by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCost_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200333, skillname);
	}

	/**
	 * You reduced your MP consumption for mantra skills by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillToggleCost_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200334, skillname);
	}

	/**
	 * You boosted your spell skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSpellAttackEffect_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200335, skillname);
	}

	/**
	 * You inflicted %num0 damage on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BackDashATK_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200336, num0, skillname);
	}

	/**
	 * You %0d your enmity by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHate_ME_TO_SELF(String value0d, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200337, value0d, skillname);
	}

	/**
	 * You inflicted %num0 damage and the rune carve effect on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CarveSignet_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200338, num0, skillname);
	}

	/**
	 * You received the HP recovery effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200339, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200340, num0, skillname);
	}

	/**
	 * You %0d your enmity by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ChangeHateOnAttacked_ME_TO_SELF(String value0d, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200341, value0d, skillname);
	}

	/**
	 * You are released from the Aerial Snare by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CloseAerial_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200342, skillname);
	}

	/**
	 * You recovered from the transformation by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200343, skillname);
	}

	/**
	 * You recovered HP by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200344, num0, skillname);
	}

	/**
	 * You inflicted %num0 damage on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DashATK_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200345, num0, skillname);
	}

	/**
	 * You inflicted %num0 damage on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeathBlow_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200346, num0, skillname);
	}

	/**
	 * Your recovery amount changed after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeboostHealAmount_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200347, skillname);
	}

	/**
	 * You transformed yourself into a(n) %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Deform_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200348, value0, skillname);
	}

	/**
	 * You decreased your own flight time by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200349, skillname);
	}

	/**
	 * You reduced your flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200350, num0, skillname);
	}

	/**
	 * You will inflict damage on yourself in a moment because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200351, skillname);
	}

	/**
	 * You reduced your flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200352, num0, skillname);
	}

	/**
	 * You dispelled the magic effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Dispel_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200353, skillname);
	}

	/**
	 * You dispelled magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuff_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200354, skillname);
	}

	/**
	 * You suffered %num0 damage and dispelled some of the magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuffCounterATK_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200355, num0, skillname);
	}

	/**
	 * You dispelled magical debuffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuff_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200356, skillname);
	}

	/**
	 * You removed abnormal mental conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffMental_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200357, skillname);
	}

	/**
	 * You removed abnormal physical conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffPhysical_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200358, skillname);
	}

	/**
	 * You transferred %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPTransfer_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200359, num0, skillname);
	}

	/**
	 * You expanded the range of mantra by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ExtendAuraRange_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200360, skillname);
	}

	/**
	 * You were forced to crash by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fall_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200361, skillname);
	}

	/**
	 * You decreased your own flight time by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200362, skillname);
	}

	/**
	 * You reduced your flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200363, num0, skillname);
	}

	/**
	 * Your flight time has increased by %num0 because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200364, num0, skillname);
	}

	/**
	 * Your flight time has been restored by [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200365, skillname);
	}

	/**
	 * You increased your flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200366, num0, skillname);
	}

	/**
	 * You increased the flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200367, num0, skillname);
	}

	/**
	 * You are continuously recovering HP because of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200368, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200369, num0, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200370, num0, skillname);
	}

	/**
	 * You converted damage to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200371, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200372, num0, skillname);
	}

	/**
	 * You converted death to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200373, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200374, num0, skillname);
	}

	/**
	 * You hid yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Hide_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200375, skillname);
	}

	/**
	 * You %0d your enmity by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HostileUp_ME_TO_SELF(String value0d, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200376, value0d, skillname);
	}

	/**
	 * You made a magical counterattack by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200377, skillname);
	}

	/**
	 * You inflicted %num0 damage on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200378, num0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MoveBehindATK_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200379, num0, skillname);
	}

	/**
	 * You recovered %num0 MP after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200380, num0, skillname);
	}

	/**
	 * You recovered MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200381, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200382, num0, skillname);
	}

	/**
	 * You recovered %num0 MP after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200383, num0, skillname);
	}

	/**
	 * You boosted your recovery by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostHealEffect_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200384, skillname);
	}

	/**
	 * You boosted your skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillAttack_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200385, skillname);
	}

	/**
	 * You boosted your critical hit skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillCritical_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200386, skillname);
	}

	/**
	 * You boosted your skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeTypeBoostSkillLevel_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200387, skillname);
	}

	/**
	 * You made the spirit use its skills by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUseUltraSkill_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200388, skillname);
	}

	/**
	 * You have transformed into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Polymorph_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200389, value0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200390, num0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_Ratio_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200391, num0, skillname);
	}

	/**
	 * You increased the flight time by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCFPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200392, num0, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200393, num0, skillname);
	}

	/**
	 * You recovered %num0 MP after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCMPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200394, num0, skillname);
	}

	/**
	 * You protected yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200395, skillname);
	}

	/**
	 * You protected yourself from %num0 damage by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200396, num0, skillname);
	}

	/**
	 * You received the effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Provoker_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200397, skillname);
	}

	/**
	 * You inflicted %num0 damage and the pull effect on yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Pulled_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200398, num0, skillname);
	}

	/**
	 * You teleported yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_RandomMoveLoc_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200399, skillname);
	}

	/**
	 * You ensured resurrection by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Rebirth_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200400, skillname);
	}

	/**
	 * You gave yourself the reflection effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200401, skillname);
	}

	/**
	 * You reflected %num0 damage by the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200402, num0, skillname);
	}

	/**
	 * You resurrected yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Resurrect_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200403, skillname);
	}

	/**
	 * You resurrected and telerported yourself by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectPositional_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200404, skillname);
	}

	/**
	 * You received the see-through effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Search_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200405, skillname);
	}

	/**
	 * You have transformed into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ShapeChange_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200406, value0, skillname);
	}

	/**
	 * You gave yourself a defense shield by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200407, skillname);
	}

	/**
	 * You protected yourself from %num0 damage by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200408, num0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SignetBurst_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200409, num0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATK_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200410, num0, skillname);
	}

	/**
	 * You absorb %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200411, num0, skillname);
	}

	/**
	 * You received continuous damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200412, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200413, num0, skillname);
	}

	/**
	 * You absorb %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200414, num0, skillname);
	}

	/**
	 * You absorb %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200415, num0, skillname);
	}

	/**
	 * Your movement speed has been increased by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sprint_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200416, skillname);
	}

	/**
	 * Your %0 has been weakened by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatDown_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200417, value0, skillname);
	}

	/**
	 * Your %0 has been boosted by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatUp_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200418, value0, skillname);
	}

	/**
	 * Your %0 resistance effects are weakened after using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeBoostResist_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200419, value0, skillname);
	}

	/**
	 * You changed the duration of %0 skills by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeExtendDuration_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200420, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Summon_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200421, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonBindingGroupGate_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200422, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonGroupGate_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200423, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonHoming_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200424, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonServant_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200425, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTotem_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200426, value0, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTrap_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200427, value0, skillname);
	}

	/**
	 * You exchanged your MP with your HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHPMP_Instant_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200428, skillname);
	}

	/**
	 * %0 was changed using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_WeaponStatUp_ME_TO_SELF(String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200429, value0, skillname);
	}

	/**
	 * You blinded [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200430, skilltarget, skillname);
	}

	/**
	 * You confused [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200431, skilltarget, skillname);
	}

	/**
	 * You diseased [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200432, skilltarget, skillname);
	}

	/**
	 * You made [%SkillTarget] afraid by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200433, skilltarget, skillname);
	}

	/**
	 * You paralyzed [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200434, skilltarget, skillname);
	}

	/**
	 * You immobilized [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200435, skilltarget, skillname);
	}

	/**
	 * You silenced [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200436, skilltarget, skillname);
	}

	/**
	 * You put [%SkillTarget] to sleep by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200437, skilltarget, skillname);
	}

	/**
	 * You span [%SkillTarget] around by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200438, skilltarget, skillname);
	}

	/**
	 * You knocked [%SkillTarget] back by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200439, skilltarget, skillname);
	}

	/**
	 * You knocked [%SkillTarget] over by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200440, skilltarget, skillname);
	}

	/**
	 * You stunned [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200441, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is no longer blind.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200442, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer confused.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200443, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer diseased.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200444, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer afraid.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200445, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer paralyzed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200446, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer immobilized.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200447, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer silenced.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200448, skilltarget);
	}

	/**
	 * [%SkillTarget] woke up.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200449, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer spinning.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200450, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer staggering.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200451, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer shocked.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200452, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer stunned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200453, skilltarget);
	}

	/**
	 * [%SkillTarget] was resurrected as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostDropRate_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200454, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s visual range has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OutofSight_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200455, skilltarget, skillcaster, skillname);
	}

	/**
	 * You exchanged [%SkillTarget]'s enmity with the spirit's by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHostile_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200456, skilltarget, skillname);
	}

	/**
	 * You used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ReturnHome_ME_TO_B(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200457, skillname);
	}

	/**
	 * You start using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_ME_TO_B(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200458, skillname);
	}

	/**
	 * You stop using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_END_ME_TO_B(String skillname) {
		return new SM_SYSTEM_MESSAGE(1200459, skillname);
	}

	/**
	 * You used [%SkillName] and [%SkillTarget] became bound.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_ME_TO_B(String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200460, skillname, skilltarget);
	}

	/**
	 * You caused [%SkillTarget] to bleed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200461, skilltarget, skillname);
	}

	/**
	 * You cursed [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200462, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is unable to fly because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200463, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] became snared in mid-air because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200464, skilltarget, skillcaster, skillname);
	}

	/**
	 * You petrified [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200465, skilltarget, skillname);
	}

	/**
	 * You poisoned [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200466, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s attack speed has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200467, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s movement speed decreased as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200468, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is no longer bound
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200469, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer bleeding.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200470, skilltarget);
	}

	/**
	 * [%SkillTarget] recovered from the cursed state.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200471, skilltarget);
	}

	/**
	 * [%SkillTarget] is able to fly again.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200472, skilltarget);
	}

	/**
	 * [%SkillTarget] is released from the Aerial Snare.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200473, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer petrified.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200474, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer poisoned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200475, skilltarget);
	}

	/**
	 * [%SkillTarget]'s attack speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200476, skilltarget);
	}

	/**
	 * [%SkillTarget]'s movement speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_ME_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200477, skilltarget);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s block by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysBlock_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200478, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s evasion by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysDodge_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200479, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s parry by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysHit_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200480, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%Skillcaster] removed [%SkillTarget]'s elemental defense by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysNoResist_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200481, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s parry by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysParry_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200482, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is in the elemental maximum defense state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysResist_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200483, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s recovery skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHealEffect_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200484, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s casting time increased as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCastingTime_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200485, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s movement speed decreased as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCost_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200486, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s movement speed decreased as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillToggleCost_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200487, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s spell skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSpellAttackEffect_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200488, skillcaster, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BackDashATK_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200489, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] %0d [%SkillTarget]'s enmity %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHate_ME_TO_B(String skillcaster, String value0d, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200490, skillcaster, value0d, skilltarget, value0, skillname);
	}

	/**
	 * You inflicted %num0 damage and the rune carve effect on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CarveSignet_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200491, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is recovering HP as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200492, skilltarget, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200493, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] %0d [%SkillTarget]'s enmity %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ChangeHateOnAttacked_ME_TO_B(String skillcaster, String value0d, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200494, skillcaster, value0d, skilltarget, value0, skillname);
	}

	/**
	 * You released [%SkillTarget] from the Aerial Snare by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CloseAerial_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200495, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received the transformation recovery effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200496, skilltarget, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200497, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DashATK_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200498, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeathBlow_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200499, num0, skilltarget, skillname);
	}

	/**
	 * You changed [%SkillTarget]'s recovery amount by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeboostHealAmount_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200500, skilltarget, skillname);
	}

	/**
	 * You transformed [%SkillTarget] into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Deform_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200501, skilltarget, value0, skillname);
	}

	/**
	 * In a moment, [%SkillTarget]'s flight time will decrease because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200502, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200503, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget] received the Delayed Blast effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200504, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200505, skilltarget, num0, skillname);
	}

	/**
	 * You dispelled the magic effect from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Dispel_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200506, skilltarget, skillname);
	}

	/**
	 * You dispelled magical buffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuff_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200507, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] and dispelled some of its magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuffCounterATK_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200508, num0, skilltarget, skillname);
	}

	/**
	 * You dispelled magical debuffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuff_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200509, skilltarget, skillname);
	}

	/**
	 * You removed abnormal mental conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffMental_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200510, skilltarget, skillname);
	}

	/**
	 * You removed abnormal physical conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffPhysical_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200511, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is in the DP recovery state because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPTransfer_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200512, skilltarget, skillname);
	}

	/**
	 * You expanded [%SkillTarget]'s mantra range by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ExtendAuraRange_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200513, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received the forced crash effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fall_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200514, skilltarget, skillname);
	}

	/**
	 * In a moment, [%SkillTarget]'s flight time will decrease because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200515, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200516, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has decreased by %num0 because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_Instant_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200517, skilltarget, num0, skillname);
	}

	/**
	 * You restored [%SkillTarget]'s flight time by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200518, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time increased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200519, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has increased by %num0 because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200520, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillCaster] has caused [%SkillTarget] to recover HP over time by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200521, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] recovered its HP by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200522, skilltarget, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_Instant_HEAL_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200523, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] converted [%SkillTarget]'s damage to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200524, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] has recovered %num0 HP due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200525, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget] converted death to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200526, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] has recovered %num0 HP due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200527, skilltarget, num0, skillname);
	}

	/**
	 * You hid [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Hide_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200528, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] %0d [%SkillTarget]'s enmity %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HostileUp_ME_TO_B(String skillcaster, String value0d, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200529, skillcaster, value0d, skilltarget, value0, skillname);
	}

	/**
	 * You granted [%SkillTarget] a magical counterattack by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200530, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200531, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MoveBehindATK_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200532, num0, skilltarget, skillname);
	}

	/**
	 * You reduced [%SkillTarget]'s MP by %num0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_Instant_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200533, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200534, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200535, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200536, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s recovery skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostHealEffect_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200537, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received the boost skill effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillAttack_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200538, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received the critical hit effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillCritical_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200539, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received the boost skill effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeTypeBoostSkillLevel_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200540, skilltarget, skillname);
	}

	/**
	 * Your spirit uses its skills on [%SkillTarget] as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUseUltraSkill_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200541, skilltarget, skillname);
	}

	/**
	 * You transformed [%SkillTarget] into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Polymorph_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200542, skilltarget, value0, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200543, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_Ratio_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200544, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has increased by %num0 because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCFPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200545, skilltarget, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCHeal_Instant_HEAL_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200546, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] recovered MP by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCMPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200547, skilltarget, num0, skillname);
	}

	/**
	 * You protected [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200548, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200549, skilltarget, num0, skillname);
	}

	/**
	 * You affected [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Provoker_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200550, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage and the pull effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Pulled_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200551, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget] teleported as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_RandomMoveLoc_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200552, skilltarget, skillname);
	}

	/**
	 * You placed [%SkillTarget] in the reserved resurrection state as by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Rebirth_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200553, skilltarget, skillname);
	}

	/**
	 * You gave [%SkillTarget] the reflection effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200554, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] reflected %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200555, skilltarget, num0, skillname);
	}

	/**
	 * [%SkillTarget] has resurrected as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Resurrect_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200556, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is in the resurrection state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectPositional_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200557, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the see-through state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Search_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200558, skilltarget, skillcaster, skillname);
	}

	/**
	 * You transformed [%SkillTarget] into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ShapeChange_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200559, skilltarget, value0, skillname);
	}

	/**
	 * [%SkillTarget] received the defense shield effect as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200560, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200561, skilltarget, num0, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SignetBurst_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200562, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATK_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200563, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200564, num0, skilltarget, skillname);
	}

	/**
	 * You inflicted continuous damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200565, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200566, num0, skilltarget, skillname);
	}

	/**
	 * You start to absorb [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200567, skilltarget, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200568, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is in the movement speed increase state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sprint_ME_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200569, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has weakened [%SkillTarget]'s %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatDown_ME_TO_B(String skillcaster, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200570, skillcaster, skilltarget, value0, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s Physical Def by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatUp_ME_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200571, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s %0 resistance effects were weakened as you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeBoostResist_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200572, skilltarget, value0, skillname);
	}

	/**
	 * You changed [%SkillTarget]'s %0 skill duration by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeExtendDuration_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200573, skilltarget, value0, skillname);
	}

	/**
	 * You summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Summon_ME_TO_B(String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200574, value0, skilltarget, skillname);
	}

	/**
	 * You summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonBindingGroupGate_ME_TO_B(String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200575, value0, skilltarget, skillname);
	}

	/**
	 * You summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonGroupGate_ME_TO_B(String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200576, value0, skilltarget, skillname);
	}

	/**
	 * You summoned %0 by using [%SkillName] to let it attack [%SkillTarget].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonHoming_ME_TO_B(String value0, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200577, value0, skillname, skilltarget);
	}

	/**
	 * You summoned %0 by using [%SkillName] to let it attack [%SkillTarget].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonServant_ME_TO_B(String value0, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200578, value0, skillname, skilltarget);
	}

	/**
	 * You summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTotem_ME_TO_B(String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200579, value0, skilltarget, skillname);
	}

	/**
	 * You summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTrap_ME_TO_B(String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200580, value0, skilltarget, skillname);
	}

	/**
	 * You caused [%SkillTarget] to exchange MP with HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHPMP_Instant_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200581, skilltarget, skillname);
	}

	/**
	 * You changed [%SkillTarget]'s %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_WeaponStatUp_ME_TO_B(String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200582, skilltarget, value0, skillname);
	}

	/**
	 * [%SkillCaster] has blinded you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200583, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has confused you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200584, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has diseased you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200585, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has made you afraid by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200586, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has diseased you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200587, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has immobilized you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200588, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has silenced you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200589, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has put you to sleep by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200590, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has spun you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200591, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has knocked you back by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200592, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has knocked you down by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200593, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has stunned you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200594, skillcaster, skillname);
	}

	/**
	 * You can see again
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200595);
	/**
	 * You are no longer confused.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200596);
	/**
	 * You are no longer diseased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200597);
	/**
	 * You recovered from your fear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200598);
	/**
	 * You are no longer paralyzed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200599);
	/**
	 * You are no longer immobilized.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200600);
	/**
	 * You are no longer silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200601);
	/**
	 * You woke up.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200602);
	/**
	 * You have stopped spinning.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200603);
	/**
	 * You are no longer staggering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200604);
	/**
	 * You are no longer shocked.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200605);
	/**
	 * You are no longer stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200606);

	/**
	 * Your loot rate has increased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostDropRate_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200607, skillcaster, skillname);
	}

	/**
	 * Your visual range has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OutofSight_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200608, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] caused you to exchange your enmity with the spirit's by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHostile_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200609, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ReturnHome_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200610, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] started using [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200611, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] stops using [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_END_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200612, skillcaster, skillname);
	}

	/**
	 * You became bound because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200613, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] caused you to bleed by using [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200614, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has cursed you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200615, skillcaster, skillname);
	}

	/**
	 * You are unable to fly because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200616, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has snared you in mid-air by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200617, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has petrified you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200618, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has poisoned you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200619, skillcaster, skillname);
	}

	/**
	 * Your attack speed has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200620, skillcaster, skillname);
	}

	/**
	 * Your movement speed has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200621, skillcaster, skillname);
	}

	/**
	 * You are no longer bound
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200622);
	/**
	 * You are no longer bleeding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200623);
	/**
	 * You are released from the cursed state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200624);
	/**
	 * You are able to fly again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200625);
	/**
	 * You are released from the Aerial Snare.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200626);
	/**
	 * You are no longer petrified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200627);
	/**
	 * You are no longer poisoned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200628);
	/**
	 * Your attack speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200629);
	/**
	 * Your movement speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_A_TO_ME = new SM_SYSTEM_MESSAGE(1200630);

	/**
	 * [%SkillCaster] has boosted your block by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysBlock_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200631, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your evasion by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysDodge_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200632, skillcaster, skillname);
	}

	/**
	 * You received the boost accuracy effect because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysHit_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200633, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has removed your elemental defense by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysNoResist_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200634, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your parry by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysParry_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200635, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has maximized your elemental defense by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysResist_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200636, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your recovery skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHealEffect_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200637, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has changed your casting speed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCastingTime_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200638, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has changed your MP consumption by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCost_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200639, skillcaster, skillname);
	}

	/**
	 * Your mantra skill MP consumption has changed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillToggleCost_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200640, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your spell skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSpellAttackEffect_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200641, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BackDashATK_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200642, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] inflicted enmity %0 on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHate_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200643, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage and the rune carve effect on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CarveSignet_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200644, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_A_TO_ME(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200645, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200646, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] inflicted enmity %0 on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ChangeHateOnAttacked_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200647, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] released you from the aerial snare by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CloseAerial_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200648, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] restored you from the transformation by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200649, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200650, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DashATK_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200651, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeathBlow_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200652, skillcaster, num0, skillname);
	}

	/**
	 * Your recovery amount has changed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeboostHealAmount_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200653, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] transformed you into a(n) %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Deform_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200654, skillcaster, value0, skillname);
	}

	/**
	 * In a moment, [%SkillCaster] will decrease your flight time because they used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200655, skillcaster, skillname);
	}

	/**
	 * Your flight time has increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200656, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted a Delayed Blast on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200657, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time has decreased by %num0 because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200658, skillcaster, num0, skillname);
	}

	/**
	 * Your magic effect was dispelled because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Dispel_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200659, skillcaster, skillname);
	}

	/**
	 * Your magical buffs were dispelled because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuff_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200660, skillcaster, skillname);
	}

	/**
	 * You suffered %num0 damage and lost some of your magical buffs because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuffCounterATK_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200661, num0, skillcaster, skillname);
	}

	/**
	 * Your magical debuffs were dispelled because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuff_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200662, skillcaster, skillname);
	}

	/**
	 * Your abnormal mental conditions were removed because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffMental_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200663, skillcaster, skillname);
	}

	/**
	 * Your abnormal physical conditions were removed because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffPhysical_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200664, skillcaster, skillname);
	}

	/**
	 * You received %num0 DP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPTransfer_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200665, num0, skillcaster, skillname);
	}

	/**
	 * Your aura range has expanded because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ExtendAuraRange_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200666, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] forced you to crash by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fall_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200667, skillcaster, skillname);
	}

	/**
	 * In a moment, [%SkillCaster] will decrease your flight time because they used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200668, skillcaster, skillname);
	}

	/**
	 * Your flight time decreased by %num0 due to the effect of [%SkillName] used by [%SkillCaster].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_A_TO_ME(int num0, String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1200669, num0, skillname, skillcaster);
	}

	/**
	 * Your flight time decreased by %num0 due to the effect of [%SkillName] used by [%SkillCaster].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_Instant_A_TO_ME(int num0, String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1200670, num0, skillname, skillcaster);
	}

	/**
	 * [%SkillCaster] restored your flight time by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200671, skillcaster, skillname);
	}

	/**
	 * Your flight time has increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200672, num0, skillcaster, skillname);
	}

	/**
	 * Your flight time has increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200673, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is continuously restoring your HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200674, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200675, num0, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200676, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has converted damage dealt to you to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200677, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200678, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has converted death dealt to you to healing by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200679, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200680, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has hidden you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Hide_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200681, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted enmity %0 on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HostileUp_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200682, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has given you a magical counterattack by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200683, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200684, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MoveBehindATK_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200685, skillcaster, num0, skillname);
	}

	/**
	 * Your MP has decreased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_Instant_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200686, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has restored your MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200687, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 MP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200688, num0, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200689, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your recovery skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostHealEffect_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200690, skillcaster, skillname);
	}

	/**
	 * You received the boost skill effect because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillAttack_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200691, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your parry by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillCritical_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200692, skillcaster, skillname);
	}

	/**
	 * You received the boost skill effect because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeTypeBoostSkillLevel_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200693, skillcaster, skillname);
	}

	/**
	 * The spirit uses its skills because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUseUltraSkill_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200694, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has transformed you into a(n) %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Polymorph_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200695, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200696, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_Ratio_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200697, skillcaster, num0, skillname);
	}

	/**
	 * Your flight time has increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCFPHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200698, num0, skillcaster, skillname);
	}

	/**
	 * You recovered %num0 HP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200699, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCMPHeal_Instant_HEAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200700, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has protected you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200701, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] blocked %num0 damage through the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200702, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster]'s [%SkillName] affected you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Provoker_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200703, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage and pulled you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Pulled_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200704, skillcaster, num0, skillname);
	}

	/**
	 * You teleported because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_RandomMoveLoc_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200705, skillcaster, skillname);
	}

	/**
	 * You entered the reserved resurrection state because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Rebirth_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200706, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has cast a reflector on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200707, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] reflected %num0 damage through to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200708, skillcaster, num0, skillname);
	}

	/**
	 * You resurrected as [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Resurrect_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200709, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has decreased your movement speed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectPositional_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200710, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has put a see-through effect on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Search_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200711, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has transformed you into a(n) %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ShapeChange_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200712, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has put a defense shield on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200713, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] blocked %num0 damage through the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200714, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SignetBurst_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200715, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATK_Instant_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200716, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200717, skillcaster, num0, skillname);
	}

	/**
	 * You received continuous damage because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200718, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_Instant_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200719, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has begun draining your HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200720, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has inflicted %num0 damage on you by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_A_TO_ME(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200721, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] has increased your movement speed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sprint_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200722, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has weakened [%SkillTarget]'s %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatDown_A_TO_ME(String skillcaster, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200723, skillcaster, skilltarget, value0, skillname);
	}

	/**
	 * [%SkillCaster] has boosted your %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatUp_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200724, skillcaster, value0, skillname);
	}

	/**
	 * Your %0 resistance effect was weakened because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeBoostResist_A_TO_ME(String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200725, value0, skillcaster, skillname);
	}

	/**
	 * Your %0 skill duration has changed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeExtendDuration_A_TO_ME(String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200726, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Summon_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200727, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonBindingGroupGate_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200728, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonGroupGate_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200729, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonHoming_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200730, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonServant_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200731, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTotem_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200732, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has caused you to summon %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTrap_A_TO_ME(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200733, skillcaster, value0, skillname);
	}

	/**
	 * You exchanged HP with MP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHPMP_Instant_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200734, skillcaster, skillname);
	}

	/**
	 * Your %0 has changed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_WeaponStatUp_A_TO_ME(String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200735, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became blinded because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200736, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became confused because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200737, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became diseased because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200738, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] was put in the fear state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200739, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became paralyzed because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200740, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became immobilized because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200741, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became silenced because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200742, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] fell asleep because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200743, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is spinning because it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200744, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] was knocked back from shock because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200745, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] fell down from shock because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200746, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became stunned because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200747, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is no longer blind.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200748, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer confused.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200749, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer diseased.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200750, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer afraid.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200751, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer paralyzed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200752, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer immobilized.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200753, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer silenced.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200754, skilltarget);
	}

	/**
	 * [%SkillTarget] woke up.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200755, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer spinning.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200756, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer staggering.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200757, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer shocked.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200758, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer stunned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200759, skilltarget);
	}

	/**
	 * [%SkillCaster]'s loot rate has increased because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostDropRate_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200760, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s visual range has reduced because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OutofSight_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200761, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] exchanged its enmity with the spirit's by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHostile_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200762, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ReturnHome_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200763, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] started using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200764, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] stops using [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_END_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200765, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] and became bound.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200766, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is bleeding because it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200767, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is cursed because it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200768, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is unable to fly because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200769, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became snared in mid-air because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200770, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became petrified because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200771, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] became poisoned because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200772, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s attack speed has decreased because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200773, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s movement speed has decreased because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200774, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is no longer bound
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200775, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer bleeding.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200776, skilltarget);
	}

	/**
	 * [%SkillTarget] is released from the cursed state.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200777, skilltarget);
	}

	/**
	 * [%SkillTarget] is able to fly again.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200778, skilltarget);
	}

	/**
	 * [%SkillTarget] is released from the Aerial Snare.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200779, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer petrified.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200780, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer poisoned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200781, skilltarget);
	}

	/**
	 * [%SkillTarget] restored its attack speed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200782, skilltarget);
	}

	/**
	 * [%SkillTarget] restored its movement speed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_A_TO_SELF(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200783, skilltarget);
	}

	/**
	 * [%SkillTarget] is in the boost block state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysBlock_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200784, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost evasion state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysDodge_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200785, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost accuracy state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysHit_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200786, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the no elemental defense state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysNoResist_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200787, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost parry state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysParry_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200788, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the elemental maximum defense state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysResist_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200789, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost recovery skill state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHealEffect_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200790, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s casting speed has changed because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCastingTime_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200791, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s MP consumption has changed because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCost_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200792, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s mantra skill MP consumption has decreased because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillToggleCost_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200793, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost recovery skill state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSpellAttackEffect_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200794, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BackDashATK_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200795, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the enmity %0 state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHate_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200796, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage and the rune carve effect on themselves by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CarveSignet_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200797, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] recovered HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200798, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200799, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the enmity %0 state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ChangeHateOnAttacked_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200800, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] was released from the aerial snare by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CloseAerial_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200801, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered from the transformation by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200802, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200803, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DashATK_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200804, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeathBlow_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200805, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] changed his own recovery amount by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeboostHealAmount_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200806, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has transformed into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Deform_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200807, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] will receive the decrease flight time effect in a moment because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200808, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time decreased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200809, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] will receive damage in a moment because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200810, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time decreased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200811, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] dispelled its magic effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Dispel_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200812, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] dispelled its magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuff_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200813, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] suffered %num0 damage and dispelled some of its magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuffCounterATK_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200814, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] dispelled its magical debuffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuff_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200815, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] removed its abnormal mental conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffMental_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200816, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] removed its abnormal physical conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffPhysical_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200817, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] transferred %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPTransfer_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200818, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] boosted his mantra range by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ExtendAuraRange_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200819, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the forced crash state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fall_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200820, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] will receive the decrease flight time effect in a moment because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200821, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time decreased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200822, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time decreased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200823, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the flight time recovery state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200824, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time increased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200825, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time increased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200826, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the continuous healing state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200827, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200828, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200829, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the convert damage healing state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200830, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200831, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the convert death healing state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200832, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200833, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the hide state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Hide_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200834, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the enmity %0 state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HostileUp_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200835, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] is in the magical counterattack state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200836, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200837, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MoveBehindATK_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200838, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster]'s MP was reduced by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200839, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the Mana Treatment state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200840, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200841, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200842, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost recovery skill state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostHealEffect_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200843, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost skill state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillAttack_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200844, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the critical hit state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillCritical_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200845, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the boost skill state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeTypeBoostSkillLevel_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200846, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] caused the spirit to use its skill by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUseUltraSkill_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200847, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has transformed into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Polymorph_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200848, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200849, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_Ratio_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200850, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster]'s flight time increased by %num0 as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCFPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200851, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200852, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCMPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200853, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the protection state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200854, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] blocked %num0 damage by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200855, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] was affected by its own [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Provoker_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200856, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage and was put in the pull state because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Pulled_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200857, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the teleport state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_RandomMoveLoc_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200858, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the reserved resurrection state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Rebirth_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200859, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the reflection state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200860, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] reflected %num0 damage by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200861, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] is in the resurrection state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Resurrect_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200862, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] is in the summon-resurrection state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectPositional_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200863, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the see-through state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Search_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200864, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] has transformed into %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ShapeChange_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200865, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] is in the defense shield state as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200866, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] blocked %num0 damage by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200867, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SignetBurst_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200868, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATK_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200869, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200870, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] received the continuous damage effect because he used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200871, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] received %num0 damage as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200872, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] absorbed %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200873, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] absorbed %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200874, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillTarget] is in the movement speed increase state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sprint_A_TO_SELF(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200875, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the weaken %0 state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatDown_A_TO_SELF(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200876, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost %0 state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatUp_A_TO_SELF(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200877, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s %0 resistance effects are weakened as it used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeBoostResist_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200878, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] changed his %0 skill duration by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeExtendDuration_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200879, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Summon_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200880, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonBindingGroupGate_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200881, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonGroupGate_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200882, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonHoming_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200883, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonServant_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200884, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTotem_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200885, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTrap_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200886, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] exchanged his HP and MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHPMP_Instant_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200887, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] changed his %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_WeaponStatUp_A_TO_SELF(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200888, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillTarget] became blinded because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200889, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became confused because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200890, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became diseased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200891, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the fear state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200892, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became paralyzed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200893, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is unable to fly because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200894, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became silenced because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200895, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] fell asleep because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200896, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is spinning because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200897, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] was knocked back from shock because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200898, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] fell down from shock because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200899, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became stunned because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200900, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is no longer blind.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200901, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer confused.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200902, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer diseased.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200903, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer afraid.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200904, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer paralyzed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200905, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer immobilized.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200906, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer silenced.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200907, skilltarget);
	}

	/**
	 * [%SkillTarget] woke up.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200908, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer spinning.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200909, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer staggering.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200910, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer shocked.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200911, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer stunned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200912, skilltarget);
	}

	/**
	 * [%SkillTarget]'s loot rate has increased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostDropRate_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200913, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s visual range has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OutofSight_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200914, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] exchanged his enmity toward [%SkillTarget] with his spirit's by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHostile_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200915, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ReturnHome_A_TO_B(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200916, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] started using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_A_TO_B(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200917, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] stopped using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Aura_END_A_TO_B(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200918, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became bound because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200919, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is bleeding because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200920, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is cursed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200921, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is unable to fly because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200922, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became snared in mid-air because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200923, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became petrified because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200924, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] became poisoned because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200925, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s attack speed has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200926, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s attack speed has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200927, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is no longer bound
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200928, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer bleeding.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200929, skilltarget);
	}

	/**
	 * [%SkillTarget] recovered from the cursed state.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200930, skilltarget);
	}

	/**
	 * [%SkillTarget] is able to fly again.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200931, skilltarget);
	}

	/**
	 * [%SkillTarget] is released from the aerial snare.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200932, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer petrified.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200933, skilltarget);
	}

	/**
	 * [%SkillTarget] is no longer poisoned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200934, skilltarget);
	}

	/**
	 * [%SkillTarget]'s attack speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200935, skilltarget);
	}

	/**
	 * [%SkillTarget]'s movement speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_A_TO_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1200936, skilltarget);
	}

	/**
	 * [%SkillTarget] is in the boost block state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysBlock_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200937, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost evasion state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysDodge_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200938, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost accuracy state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysHit_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200939, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the no elemental defense state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysNoResist_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200940, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost parry state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysParry_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200941, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the elemental maximum defense state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_AlwaysResist_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200942, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost recovery skill state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHealEffect_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200943, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] changed [%SkillTarget]'s casting speed by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCastingTime_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200944, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] changed [%SkillTarget]'s MP consumption by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillCost_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200945, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s mantra skill MP consumption has decreased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSkillToggleCost_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200946, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost spell skill state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostSpellAttackEffect_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200947, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BackDashATK_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200948, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is in the spinning state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_BoostHate_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200949, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] and caused the Rune Carve effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CarveSignet_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200950, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is recovering HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200951, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200952, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the spinning state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ChangeHateOnAttacked_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200953, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] was released from the aerial snare because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CloseAerial_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200954, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the transformation recovery state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200955, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200956, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DashATK_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200957, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeathBlow_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200958, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] changed [%SkillTarget]'s recovery amount by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DeboostHealAmount_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200959, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] has transformed into %0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Deform_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200960, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] will receive the decrease flight time effect in a moment because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200961, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has decreased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200962, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] received the Delayed Blast effect because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200963, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has decreased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200964, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] dispelled the magic effect from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Dispel_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200965, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] dispelled the magical buffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuff_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200966, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] and dispelled some of its magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelBuffCounterATK_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200967, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] dispelled the magical debuffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuff_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200968, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] removed abnormal mental conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffMental_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200969, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] removed abnormal physical conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelDeBuffPhysical_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200970, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] transferred %num0 DP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPTransfer_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200971, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] boosted [%SkillTarget]'s mantra range by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ExtendAuraRange_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200972, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] was put in the forced crash state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fall_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200973, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] will receive the decrease flight time effect in a moment because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200974, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has decreased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200975, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time has decreased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_Instant_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200976, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the flight time recovery state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200977, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200978, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200979, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the continuous healing state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200980, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200981, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200982, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the convert damage healing state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200983, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200984, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the convert death healing state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200985, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200986, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the spinning state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Hide_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200987, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the spinning state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HostileUp_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200988, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] was put in the magical counterattack state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200989, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200990, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MoveBehindATK_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200991, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s MP was reduced by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_Instant_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200992, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the Mana Treatment state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200993, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200994, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200995, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost recovery skill state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostHealEffect_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200996, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost skill state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillAttack_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200997, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the critical hit state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeBoostSkillCritical_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200998, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost skill state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OneTimeTypeBoostSkillLevel_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1200999, skilltarget, skillcaster, skillname);
	}

	/**
	 * The spirit used a skill on [%SkillTarget] because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUseUltraSkill_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201000, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] has transformed into %0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Polymorph_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201001, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201002, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_Ratio_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201003, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time increased by %num0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCFPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201004, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 HP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201005, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PROCMPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201006, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the protection state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201007, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201008, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] was affected because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Provoker_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201009, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage and was put in the pull state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Pulled_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201010, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the teleport state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_RandomMoveLoc_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201011, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] entered the reserved resurrection state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Rebirth_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201012, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the reflection state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201013, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] reflected %num0 damage because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201014, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the resurrection state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Resurrect_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201015, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the resurrection summoning state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectPositional_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201016, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the see-through state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Search_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201017, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] has transformed into %0 because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ShapeChange_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201018, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the defense shield state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201019, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201020, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SignetBurst_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201021, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATK_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201022, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201023, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] to inflict the continuous damage effect on [%SkillTarget].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_A_TO_B(String skillcaster, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201024, skillcaster, skillname, skilltarget);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201025, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] absorbs [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201026, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201027, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s movement speed increased because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sprint_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201028, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the weaken %0 state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatDown_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201029, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is in the boost %0 state because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_StatUp_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201030, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s %0 resistance effects were weakened because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeBoostResist_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201031, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget]'s %0 skill durations changed because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SubTypeExtendDuration_A_TO_B(String skilltarget, String value0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201032, skilltarget, value0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Summon_A_TO_B(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201033, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] summoned %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonBindingGroupGate_A_TO_B(String skillcaster, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201034, skillcaster, value0, skillname);
	}

	/**
	 * [%SkillCaster] has summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonGroupGate_A_TO_B(String skillcaster, String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201035, skillcaster, value0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has summoned %0 to attack [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonHoming_A_TO_B(String skillcaster, String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201036, skillcaster, value0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has summoned %0 to attack [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonServant_A_TO_B(String skillcaster, String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201037, skillcaster, value0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTotem_A_TO_B(String skillcaster, String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201038, skillcaster, value0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has summoned %0 to [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonTrap_A_TO_B(String skillcaster, String value0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201039, skillcaster, value0, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] exchanged [%SkillTarget]'s HP and MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SwitchHPMP_Instant_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201040, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] changed [%SkillTarget]'s %0 by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_WeaponStatUp_A_TO_B(String skillcaster, String skilltarget, String value0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201041, skillcaster, skilltarget, value0, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 bleeding damage after you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201042, skilltarget, num0, skillname);
	}

	/**
	 * You received %num0 bleeding damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201043, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201044, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_CaseHeal_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201045, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201046, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201047, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_HEAL_MP_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201048, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ConvertHeal_INTERVAL_HEAL_MP_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201049, num0, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201050, skilltarget, num0, skillname);
	}

	/**
	 * Your flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedFPATK_Instant_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201051, num0, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201052, skilltarget, num0, skillname);
	}

	/**
	 * You received %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSpellATK_Instant_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201053, num0, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201054, skilltarget, num0, skillname);
	}

	/**
	 * Your flight time decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPATK_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201055, num0, skillname);
	}

	/**
	 * [%SkillTarget]'s flight time increased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_HEAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201056, skilltarget, num0, skillname);
	}

	/**
	 * Your flight time increased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_FPHeal_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201057, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201058, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Heal_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201059, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201060, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnAttacked_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201061, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201062, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_HealCastorOnTargetDead_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201063, num0, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201064, skilltarget, num0, skillname);
	}

	/**
	 * You receive %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MagicCounterATK_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201065, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_HEAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201066, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPHeal_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201067, num0, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 poisoning damage after you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201068, skilltarget, num0, skillname);
	}

	/**
	 * You received %num0 poisoning damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201069, num0, skillname);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage through the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201070, skilltarget, num0, skillname);
	}

	/**
	 * You blocked %num0 damage through the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Protect_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201071, num0, skillname);
	}

	/**
	 * [%SkillTarget] reflected %num0 damage.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_TO_B(String skilltarget, int num0) {
		return new SM_SYSTEM_MESSAGE(1201072, skilltarget, num0);
	}

	/**
	 * You reflected %num0 damage.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_INTERVAL_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1201073, num0);
	}

	/**
	 * [%SkillTarget] blocked %num0 damage.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_TO_B(String skilltarget, int num0) {
		return new SM_SYSTEM_MESSAGE(1201074, skilltarget, num0);
	}

	/**
	 * You blocked %num0 damage.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_INTERVAL_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1201075, num0);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201076, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201077, num0, skillname);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201078, skilltarget, skillname);
	}

	/**
	 * [%SkillName] conflicted with [%SkillTarget]'s existing skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CONFLICT_ME_TO_B(String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201079, skillname, skilltarget);
	}

	/**
	 * %0 evaded the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_DODGED_ME_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1201080, value0);
	}

	/**
	 * [%SkillTarget] resisted [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201081, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] is immune to your [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201082, skilltarget, skillname);
	}

	/**
	 * [%SkillName] was cancelled as [%SkillTarget] is under too many effects.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_NO_AVAILABLE_SLOT_ME_TO_B(String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201083, skillname, skilltarget);
	}

	/**
	 * %0 blocked the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_BLOCK_ME_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1201084, value0);
	}

	/**
	 * %0 parried the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARRY_ME_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1201085, value0);
	}

	/**
	 * You absorbed [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201086, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s [%SkillName] conflicted with your existing skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CONFLICT_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201087, skillcaster, skillname);
	}

	/**
	 * You evaded %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_DODGED_A_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1201088, value0);
	}

	/**
	 * You resisted [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201089, skillcaster, skillname);
	}

	/**
	 * You are immune to [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201090, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s [%SkillName] was cancelled as you are under too many effects.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_NO_AVAILABLE_SLOT_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201091, skillcaster, skillname);
	}

	/**
	 * You blocked %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_BLOCK_A_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1201092, value0);
	}

	/**
	 * You parried %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARRY_A_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1201093, value0);
	}

	/**
	 * [%SkillTarget] was affected by [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201094, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s [%SkillName] conflicted with [%SkillTarget]'s existing skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CONFLICT_A_TO_B(String skillcaster, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201095, skillcaster, skillname, skilltarget);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_DODGED_A_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1201096, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201097, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201098, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster]'s [%SkillName] was cancelled as [%SkillTarget] is under too many effects.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_NO_AVAILABLE_SLOT_A_TO_B(String skillcaster, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201099, skillcaster, skillname, skilltarget);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_BLOCK_A_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1201100, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARRY_A_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1201101, value0, value1);
	}

	/**
	 * [%SkillTarget] evaded [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_DODGED_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201102, skilltarget, skillname);
	}

	/**
	 * You evaded [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_DODGED_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201103, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] evaded [%SkillCaster]'s [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_DODGED_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201104, skilltarget, skillcaster, skillname);
	}

	/**
	 * You can see again
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_ME = new SM_SYSTEM_MESSAGE(1201105);
	/**
	 * You are no longer confused.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_ME = new SM_SYSTEM_MESSAGE(1201106);
	/**
	 * You are no longer diseased.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_ME = new SM_SYSTEM_MESSAGE(1201107);
	/**
	 * You recovered from your fear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_ME = new SM_SYSTEM_MESSAGE(1201108);
	/**
	 * You are no longer paralyzed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_ME = new SM_SYSTEM_MESSAGE(1201109);
	/**
	 * You are no longer immobilized.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_ME = new SM_SYSTEM_MESSAGE(1201110);
	/**
	 * You are no longer silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_ME = new SM_SYSTEM_MESSAGE(1201111);
	/**
	 * You woke up.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_ME = new SM_SYSTEM_MESSAGE(1201112);
	/**
	 * You have stopped spinning.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_ME = new SM_SYSTEM_MESSAGE(1201113);
	/**
	 * You are no longer staggering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_ME = new SM_SYSTEM_MESSAGE(1201114);
	/**
	 * You are no longer shocked.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_ME = new SM_SYSTEM_MESSAGE(1201115);
	/**
	 * You are no longer stunned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_ME = new SM_SYSTEM_MESSAGE(1201116);
	/**
	 * You are no longer bound.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_ME = new SM_SYSTEM_MESSAGE(1201117);
	/**
	 * You are no longer bleeding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_ME = new SM_SYSTEM_MESSAGE(1201118);
	/**
	 * You recovered from the cursed state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_ME = new SM_SYSTEM_MESSAGE(1201119);
	/**
	 * You are able to fly again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_ME = new SM_SYSTEM_MESSAGE(1201120);
	/**
	 * You are released from the Aerial Snare.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_ME = new SM_SYSTEM_MESSAGE(1201121);
	/**
	 * You are no longer petrified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_ME = new SM_SYSTEM_MESSAGE(1201122);
	/**
	 * You are no longer poisoned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_ME = new SM_SYSTEM_MESSAGE(1201123);
	/**
	 * Your attack speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_ME = new SM_SYSTEM_MESSAGE(1201124);
	/**
	 * Your movement speed is restored to normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_ME = new SM_SYSTEM_MESSAGE(1201125);

	/**
	 * %0 is no longer blinded.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Blind_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201126, value0);
	}

	/**
	 * %0 is no longer confused.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Confuse_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201127, value0);
	}

	/**
	 * %0 is no longer diseased.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Disease_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201128, value0);
	}

	/**
	 * %0 is no longer afraid.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Fear_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201129, value0);
	}

	/**
	 * %0 is no longer paralyzed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Paralyze_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201130, value0);
	}

	/**
	 * %0 is no longer immobilized.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Root_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201131, value0);
	}

	/**
	 * %0 is no longer silenced.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Silence_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201132, value0);
	}

	/**
	 * %0 woke up.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Sleep_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201133, value0);
	}

	/**
	 * %0 is no longer spinning.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Spin_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201134, value0);
	}

	/**
	 * %0 is no longer staggering.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stagger_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201135, value0);
	}

	/**
	 * %0 is no longer shocked.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stumble_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201136, value0);
	}

	/**
	 * %0 is no longer stunned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Stun_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201137, value0);
	}

	/**
	 * %0 is no longer bound.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bind_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201138, value0);
	}

	/**
	 * %0 is no longer bleeding.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Bleed_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201139, value0);
	}

	/**
	 * %0 recovered from the cursed state.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Curse_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201140, value0);
	}

	/**
	 * %0 is able to fly again.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoFly_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201141, value0);
	}

	/**
	 * %0 is released from the Aerial Snare.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_OpenAerial_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201142, value0);
	}

	/**
	 * %0 recovered from the petrified state.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Petrification_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201143, value0);
	}

	/**
	 * %0 is no longer poisoned.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Poison_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201144, value0);
	}

	/**
	 * %0's attack speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Slow_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201145, value0);
	}

	/**
	 * %0's movement speed is restored to normal.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Snare_END_A(String value0) {
		return new SM_SYSTEM_MESSAGE(1201146, value0);
	}

	/**
	 * You use [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonSkillArea_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201147, skillname);
	}

	/**
	 * [%SkillCaster] uses [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonSkillArea_ME_TO_B(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201148, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] uses [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonSkillArea_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201149, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] uses [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonSkillArea_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201150, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] uses [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SummonSkillArea_A_TO_B(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201151, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201152, skilltarget, num0, skillname);
	}

	/**
	 * You receive %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATK_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201153, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201154, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201155, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_INTERVAL_HEAL_MP_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201156, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_Instant_INTERVAL_HEAL_MP_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201157, num0, skillname);
	}

	/**
	 * [%SkillTarget] received %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201158, skilltarget, num0, skillname);
	}

	/**
	 * You receive %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201159, num0, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_HEAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201160, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 HP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_HEAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201161, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_HEAL_MP_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201162, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SpellATKDrain_INTERVAL_HEAL_MP_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201163, num0, skillname);
	}

	/**
	 * You received %num0 damage as the [%SkillName] you used on [%SkillTarget] was reflected back at you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_SKILL_ME_to_B(int num0, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201164, num0, skillname, skilltarget);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by reflecting [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_SKILL_A_to_ME(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201165, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] inflicted %num0 damage on [%SkillCaster] by reflecting [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_SKILL_A_to_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201166, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * Your attack on [%SkillTarget] was reflected and inflicted %num0 damage on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_ME_to_B(String skilltarget, int num0) {
		return new SM_SYSTEM_MESSAGE(1201167, skilltarget, num0);
	}

	/**
	 * [%SkillTarget] inflicted %num0 damage on [%SkillCaster] by reflecting the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_A_to_ME(String skilltarget, int num0, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201168, skilltarget, num0, skillcaster);
	}

	/**
	 * [%SkillTarget] inflicted %num0 damage on [%SkillCaster] by reflecting the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Reflector_PROTECT_A_to_B(String skilltarget, int num0, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201169, skilltarget, num0, skillcaster);
	}

	/**
	 * [%SkillName] was blocked by the protective shield effect cast on [%SkillTarget].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_SKILL_ME_to_B(String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201170, skillname, skilltarget);
	}

	/**
	 * You blocked the [%SkillName] used by [%SkillCaster] with the protective shield effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_SKILL_A_to_ME(String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201171, skillname, skillcaster);
	}

	/**
	 * [%SkillTarget] blocked the [%SkillName] used by [%SkillCaster] with the protective shield effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_SKILL_A_to_B(String skilltarget, String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201172, skilltarget, skillname, skillcaster);
	}

	/**
	 * The attack was blocked by the protective shield effect cast on [%SkillTarget].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_ME_to_B(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201173, skilltarget);
	}

	/**
	 * You blocked [%SkillCaster]'s attack with the protective shield effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_A_to_ME(String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201174, skillcaster);
	}

	/**
	 * [%SkillTarget] blocked [%SkillCaster]'s attack with the protective shield effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Shield_PROTECT_A_to_B(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201175, skilltarget, skillcaster);
	}

	/**
	 * You received the %num0 damage inflicted on [%SkillTarget] by [%SkillCaster]'s [%SkillName], because of the protection effect you cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_SKILL_A_to_B(int num0, String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201176, num0, skilltarget, skillcaster, skillname);
	}

	/**
	 * You received the %num0 damage inflicted on [%SkillTarget] by [%SkillCaster], because of the protection effect you cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_A_to_B(int num0, String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201177, num0, skilltarget, skillcaster);
	}

	/**
	 * [%Protector] received the %num0 damage inflicted on [%SkillTarget] by a [%SkillName], because of the protection effect cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_SKILL_HEAL_ME_to_B(String protector, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201178, protector, num0, skilltarget, skillname);
	}

	/**
	 * [%Protector] received the %num0 damage inflicted by [%SkillCaster] 's [%SkillName], because of the protection effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_SKILL_HEAL_A_to_ME(String protector, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201179, protector, num0, skillcaster, skillname);
	}

	/**
	 * [%Protector] received the %num0 damage inflicted on [%SkillTarget] by [%SkillCaster]'s [%SkillName], because of the protection effect cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_SKILL_HEAL_A_to_B(String protector, int num0, String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201180, protector, num0, skilltarget, skillcaster, skillname);
	}

	/**
	 * [%Protector] received %num0 damage inflicted on [%SkillTarget], because of the protection effect cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_HEAL_ME_to_B(String protector, int num0, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201181, protector, num0, skilltarget);
	}

	/**
	 * [%Protector] received %num0 damage inflicted on you by [%SkillCaster], because of the protection effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_HEAL_A_to_ME(String protector, int num0, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201182, protector, num0, skillcaster);
	}

	/**
	 * [%Protector] received %num0 damage inflicted on [%SkillTarget] by [%SkillCaster], because of the protection effect cast on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_protect_PROTECT_HEAL_A_to_B(String protector, int num0, String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201183, protector, num0, skilltarget, skillcaster);
	}

	/**
	 * You recovered %num0 MP.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MPHeal_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1201196, num0);
	}

	/**
	 * You restored your flight time by %num0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FPHeal_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1201197, num0);
	}

	/**
	 * You recovered %num0 HP.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Heal_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1201198, num0);
	}

	/**
	 * %0 restored %num1 MP.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MPHeal_TO_OTHER(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1201199, value0, num1);
	}

	/**
	 * %0 restored his flight time by %num1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FPHeal_TO_OTHER(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1201200, value0, num1);
	}

	/**
	 * [%SkillTarget] received %num0 damage due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201201, skilltarget, num0, skillname);
	}

	/**
	 * You receive %num0 damage due to [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcATK_Instant_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201202, num0, skillname);
	}

	/**
	 * %0 restored %num1 HP.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Heal_TO_OTHER(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1201203, value0, num1);
	}

	/**
	 * You released [%SkillTarget]'s spirit by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUnSummon_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201204, skilltarget, skillname);
	}

	/**
	 * Your spirit was unsummoned by the effect of [%SkillName] used by [%SkillCaster].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUnSummon_A_TO_ME(String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201205, skillname, skillcaster);
	}

	/**
	 * [%SkillCaster] released [%SkillTarget]'s spirit by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_PetOrderUnSummon_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201206, skillcaster, skilltarget, skillname);
	}

	/**
	 * You inflicted continuous damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201207, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has reduced your MP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201208, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] gave [%SkillTarget] the continuous MP reduction effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201209, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget]'s MP decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_INTERVAL_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201210, skilltarget, num0, skillname);
	}

	/**
	 * Your MP decreased by %num0 due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_MPAttack_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201211, num0, skillname);
	}

	/**
	 * You gave yourself an XP bonus by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_XPBoost_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201212, skillname);
	}

	/**
	 * You gave [%SkillTarget] an XP bonus by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_XPBoost_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201213, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] gave you an XP bonus by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_XPBoost_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201214, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] gave themselves an XP bonus by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_XPBoost_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201215, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] gave [%SkillTarget] an XP bonus by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_XPBoost_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201216, skillcaster, skilltarget, skillname);
	}

	/**
	 * You made yourself more resistant to crashing and prohibitions on flying by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InvulnerableWing_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201217, skillname);
	}

	/**
	 * You gave [%SkillTarget] the crash and flying prohibition resistance effects by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InvulnerableWing_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201218, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] made you more resistant to crashing and prohibitions on flying by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InvulnerableWing_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201219, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] made themselves more resistant to crashing and prohibitions on flying by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InvulnerableWing_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201220, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] made [%SkillTarget] more resistant to crashing and prohibitions on flying by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InvulnerableWing_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201221, skillcaster, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201222, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 DP because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201223, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 DP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201224, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201225, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 DP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201226, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * You recovered some DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_HEAL_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201227, skillname);
	}

	/**
	 * You restored some of [%SkillTarget]'s DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_HEAL_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201228, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] has boosted [%SkillTarget]'s DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_HEAL_A_TO_ME(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201229, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] restored some DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_HEAL_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201230, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] restored some of [%SkillTarget]'s DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_HEAL_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201231, skillcaster, skilltarget, skillname);
	}

	/**
	 * You restored %num0 of [%SkillTarget]'s DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_INTERVAL_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201232, num0, skilltarget, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DPHeal_INTERVAL_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201233, num0, skillname);
	}

	/**
	 * You recovered %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcDPHeal_Instant_HEAL_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201234, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 DP because you used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcDPHeal_Instant_HEAL_ME_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201235, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 DP because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcDPHeal_Instant_HEAL_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201236, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] recovered %num0 DP by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcDPHeal_Instant_HEAL_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201237, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 DP because [%SkillCaster] used [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ProcDPHeal_Instant_HEAL_A_TO_B(String skilltarget, int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201238, skilltarget, num0, skillcaster, skillname);
	}

	/**
	 * You caused [%SkillTarget] to forcibly resurrect at the bind point by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectBase_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201239, skilltarget, skillname);
	}

	/**
	 * You are forced to resurrect at the bind point because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectBase_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201240, skillcaster, skillname);
	}

	/**
	 * [%SkillTarget] is forced to resurrect at the bind point because [%SkillCaster] used [%SkillName] on it.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_ResurrectBase_A_TO_B(String skilltarget, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201241, skilltarget, skillcaster, skillname);
	}

	/**
	 * You requested [%SkillTarget] to be summoned by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Recall_Instant_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201242, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] requested you to be summoned by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Recall_Instant_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201243, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] requested [%SkillTarget] to be summoned by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_Recall_Instant_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201244, skillcaster, skilltarget, skillname);
	}

	/**
	 * You dispelled magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCBuff_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201245, skillname);
	}

	/**
	 * You dispelled magical debuffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuff_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201246, skillname);
	}

	/**
	 * You removed abnormal mental conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffMental_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201247, skillname);
	}

	/**
	 * You removed abnormal physical conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffPhysical_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201248, skillname);
	}

	/**
	 * You dispelled magical buffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCBuff_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201249, skilltarget, skillname);
	}

	/**
	 * You dispelled magical debuffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuff_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201250, skilltarget, skillname);
	}

	/**
	 * You removed abnormal mental conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffMental_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201251, skilltarget, skillname);
	}

	/**
	 * You removed abnormal physical conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffPhysical_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201252, skilltarget, skillname);
	}

	/**
	 * Your magical buffs were dispelled because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCBuff_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201253, skillcaster, skillname);
	}

	/**
	 * Your magical debuffs were dispelled because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuff_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201254, skillcaster, skillname);
	}

	/**
	 * Your abnormal mental conditions were removed because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffMental_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201255, skillcaster, skillname);
	}

	/**
	 * Your abnormal physical conditions were removed because [%SkillCaster] used [%SkillName] on you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffPhysical_A_TO_ME(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201256, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] dispelled its magical buffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCBuff_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201257, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] dispelled its magical debuffs by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuff_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201258, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] removed its abnormal mental conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffMental_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201259, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] removed its abnormal physical conditions by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffPhysical_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201260, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] dispelled the magical buffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCBuff_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201261, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] dispelled the magical debuffs from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuff_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201262, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] removed abnormal mental conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffMental_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201263, skillcaster, skilltarget, skillname);
	}

	/**
	 * [%SkillCaster] removed abnormal physical conditions from [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DispelNPCDeBuffPhysical_A_TO_B(String skillcaster, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201264, skillcaster, skilltarget, skillname);
	}

	/**
	 * You received a delayed chain effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSkill_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201265, skillname);
	}

	/**
	 * [%SkillTarget] received a delayed chain effect from [%SkillName] used by you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSkill_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201266, skilltarget, skillname);
	}

	/**
	 * You received a delayed chain effect from [%SkillName] used by [%SkillCaster].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSkill_A_TO_ME(String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201267, skillname, skillcaster);
	}

	/**
	 * [%SkillCaster] used [%SkillName] and received a delayed chain effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSkill_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201268, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] to give [%SkillTarget] a delayed chain effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_DelayedSkill_A_TO_B(String skillcaster, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201269, skillcaster, skillname, skilltarget);
	}

	/**
	 * You received a periodic chain effect by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InteralSkill_ME_TO_SELF(String skillname) {
		return new SM_SYSTEM_MESSAGE(1201270, skillname);
	}

	/**
	 * [%SkillTarget] received a periodic chain effect from [%SkillName] used by you.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InteralSkill_ME_TO_B(String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201271, skilltarget, skillname);
	}

	/**
	 * You received a periodic chain effect from [%SkillName] used by [%SkillCaster].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InteralSkill_A_TO_ME(String skillname, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1201272, skillname, skillcaster);
	}

	/**
	 * [%SkillCaster] used [%SkillName] and received a periodic chain effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InteralSkill_A_TO_SELF(String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201273, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] used [%SkillName] to give [%SkillTarget] a periodic chain effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_InteralSkill_A_TO_B(String skillcaster, String skillname, String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1201274, skillcaster, skillname, skilltarget);
	}

	/**
	 * You suffer %num0 damage from [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoReduceSpellATK_Instant_ME_TO_SELF(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201275, num0, skillname);
	}

	/**
	 * You inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoReduceSpellATK_Instant_ME_TO_B(int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201276, num0, skilltarget, skillname);
	}

	/**
	 * You receive %num0 damage from [%SkillCaster]'s [%SkillName] effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoReduceSpellATK_Instant_A_TO_ME(int num0, String skillcaster, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201277, num0, skillcaster, skillname);
	}

	/**
	 * [%SkillCaster] suffers %num0 damage from [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoReduceSpellATK_Instant_A_TO_SELF(String skillcaster, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201278, skillcaster, num0, skillname);
	}

	/**
	 * [%SkillCaster] inflicted %num0 damage on [%SkillTarget] by using [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_NoReduceSpellATK_Instant_A_TO_B(String skillcaster, int num0, String skilltarget, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201279, skillcaster, num0, skilltarget, skillname);
	}

	/**
	 * [%SkillTarget] recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_INTERVAL_HEAL_MP_TO_B(String skilltarget, int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201280, skilltarget, num0, skillname);
	}

	/**
	 * You recovered %num0 MP due to the effect of [%SkillName].
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SUCC_SkillATKDrain_Instant_INTERVAL_HEAL_MP_TO_ME(int num0, String skillname) {
		return new SM_SYSTEM_MESSAGE(1201281, num0, skillname);
	}

	/**
	 * You blocked %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_BLOCK(String value0) {
		return new SM_SYSTEM_MESSAGE(1210000, value0);
	}

	/**
	 * You parried %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_PARRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1210001, value0);
	}

	/**
	 * You evaded %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_DODGE(String value0) {
		return new SM_SYSTEM_MESSAGE(1210002, value0);
	}

	/**
	 * You resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_MY(String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1210003, skillcaster);
	}

	/**
	 * You are immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_MY(String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1210004, skillcaster);
	}

	/**
	 * You absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_MY(String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1210005, skillcaster);
	}

	/**
	 * You received %num1 damage from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_ENEMY_ATTACK(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1210006, num1, value0);
	}

	/**
	 * Critical Hit! You received %num1 damage from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_ENEMY_CRITICAL(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1210007, num1, value0);
	}

	/**
	 * %0 blocked your attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_ENEMY_BLOCK(String value0) {
		return new SM_SYSTEM_MESSAGE(1210224, value0);
	}

	/**
	 * %0 parried your attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_ENEMY_PARRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1210225, value0);
	}

	/**
	 * %0 evaded your attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_ENEMY_DODGE(String value0) {
		return new SM_SYSTEM_MESSAGE(1210226, value0);
	}

	/**
	 * [%SkillTarget] resisted your magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_TARGET(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1210227, skilltarget);
	}

	/**
	 * [%SkillTarget] is immune to your magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_TARGET(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1210228, skilltarget);
	}

	/**
	 * [%SkillTarget] absorbed your magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_TARGET(String skilltarget) {
		return new SM_SYSTEM_MESSAGE(1210229, skilltarget);
	}

	/**
	 * %0 inflicted %num2 damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ATTACK(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1220000, value0, num2, value1);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1220001, value0, num2, value1);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_BLOCK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1220219, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_PARRY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1220220, value0, value1);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_DODGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1220221, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_TO_MAGIC_PARTY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1220222, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_PARTY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1220223, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_PARTY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1220224, skilltarget, skillcaster);
	}

	/**
	 * %1 received %num2 damage from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ENEMY_ATTACK(String value1, int num2, String value0) {
		return new SM_SYSTEM_MESSAGE(1230000, value1, num2, value0);
	}

	/**
	 * Critical Hit! %1 received %num2 critical damage from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ENEMY_CRITICAL(String value1, int num2, String value0) {
		return new SM_SYSTEM_MESSAGE(1230001, value1, num2, value0);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ENEMY_BLOCK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1230218, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ENEMY_PARRY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1230219, value0, value1);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_PARTY_ENEMY_DODGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1230220, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_PARTY_ENEMY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1230221, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_PARTY_ENEMY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1230222, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_PARTY_ENEMY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1230223, skilltarget, skillcaster);
	}

	/**
	 * %0 inflicted %num2 damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_FRIENDLY_ATTACK(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1240000, value0, num2, value1);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_FRIENDLY_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1240001, value0, num2, value1);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_FRIENDLY_BLOCK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1240217, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_FRIENDLY_PARRY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1240218, value0, value1);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_FRIENDLY_DODGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1240219, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_OTHER_FRIENDLY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1240220, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_OTHER_FRIENDLY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1240221, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_OTHER_FRIENDLY(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1240222, skilltarget, skillcaster);
	}

	/**
	 * %0 inflicted %num2 damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_HOSTILE_ATTACK(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1250000, value0, num2, value1);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_HOSTILE_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1250001, value0, num2, value1);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_HOSTILE_BLOCK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1250217, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_HOSTILE_PARRY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1250218, value0, value1);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_HOSTILE_DODGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1250219, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_OTHER_HOSTILE(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1250220, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_OTHER_HOSTILE(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1250221, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_OTHER_HOSTILE(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1250222, skilltarget, skillcaster);
	}

	/**
	 * %0 inflicted %num2 damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_NPC_ATTACK(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1260000, value0, num2, value1);
	}

	/**
	 * Critical Hit! %0 inflicted %num2 critical damage on %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_NPC_CRITICAL(String value0, int num2, String value1) {
		return new SM_SYSTEM_MESSAGE(1260001, value0, num2, value1);
	}

	/**
	 * %0 blocked %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_NPC_BLOCK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1260217, value0, value1);
	}

	/**
	 * %0 parried %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_NPC_PARRY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1260218, value0, value1);
	}

	/**
	 * %0 evaded %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_OTHER_NPC_DODGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1260219, value0, value1);
	}

	/**
	 * [%SkillTarget] resisted [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_RESISTED_MAGIC_OTHER_NPC(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1260220, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] is immune to [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_IMMUNED_MAGIC_OTHER_NPC(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1260221, skilltarget, skillcaster);
	}

	/**
	 * [%SkillTarget] absorbed [%SkillCaster]'s magic.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSORBED_OTHER_NPC(String skilltarget, String skillcaster) {
		return new SM_SYSTEM_MESSAGE(1260222, skilltarget, skillcaster);
	}

	/**
	 * The weapon has been changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHANGE_WEAPON = new SM_SYSTEM_MESSAGE(1300000);
	/**
	 * You can use it after registering it on the Quickbar.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NEED_TO_REGIST_SHORTCUT = new SM_SYSTEM_MESSAGE(1300001);
	/**
	 * You do not have much flight time left. Please land on a secure place.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WARNING_FLY = new SM_SYSTEM_MESSAGE(1300002);
	/**
	 * Warning! You do not have much flight time left.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WARNING_FLY_Notice = new SM_SYSTEM_MESSAGE(1300003);
	/**
	 * You suffered damage as you have submerged deep in the water. Please get out of the water.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WARNING_Swim = new SM_SYSTEM_MESSAGE(1300004);
	/**
	 * Warning! You suffered damage as you have submerged deep in the water.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WARNING_Swim_Notice = new SM_SYSTEM_MESSAGE(1300005);
	/**
	 * No target has been selected.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_TARGET = new SM_SYSTEM_MESSAGE(1300006);
	/**
	 * Invalid target. You can only use this on objects.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_OBJECT_ONLY = new SM_SYSTEM_MESSAGE(1300007);
	/**
	 * Invalid target. You can only use this on NPCs.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_NPC_ONLY = new SM_SYSTEM_MESSAGE(1300008);
	/**
	 * Invalid target. You can only use this only on other players.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_PC_ONLY = new SM_SYSTEM_MESSAGE(1300009);
	/**
	 * Invalid target. You can only use this on spirits.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_PET_ONLY = new SM_SYSTEM_MESSAGE(1300010);
	/**
	 * Invalid target. You can only use this on group members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_PARTY_ONLY = new SM_SYSTEM_MESSAGE(1300011);
	/**
	 * You can only use this on living targets.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TARGET_IS_NOT_ALIVE = new SM_SYSTEM_MESSAGE(1300012);
	/**
	 * Invalid target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TARGET_IS_NOT_VALID = new SM_SYSTEM_MESSAGE(1300013);
	/**
	 * You do not have enough health to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NOT_ENOUGH_HP = new SM_SYSTEM_MESSAGE(1300014);
	/**
	 * You do not have enough mana to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NOT_ENOUGH_MP = new SM_SYSTEM_MESSAGE(1300015);
	/**
	 * You do not have enough DP to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NOT_ENOUGH_DP = new SM_SYSTEM_MESSAGE(1300016);
	/**
	 * You cannot learn the design because your skill level is not high enough.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NOT_ENOUGH_DP_LEVEL = new SM_SYSTEM_MESSAGE(1300017);

	/**
	 * You do not have enough %0 necessary to use the skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_NOT_ENOUGH_ITEM(DescriptionId value0) {
		return new SM_SYSTEM_MESSAGE(1300018, value0);
	}

	/**
	 * You need to equip another weapon to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_WEAPON = new SM_SYSTEM_MESSAGE(1300019);

	/**
	 * You have not learned the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_NOT_LEARNED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300020, value0);
	}

	/**
	 * You are not ready to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NOT_READY = new SM_SYSTEM_MESSAGE(1300021);
	/**
	 * You are too far from the target to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TOO_FAR = new SM_SYSTEM_MESSAGE(1300022);
	/**
	 * The skill was cancelled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANCELED = new SM_SYSTEM_MESSAGE(1300023);
	/**
	 * You have failed to use the skill because the target disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TARGET_LOST = new SM_SYSTEM_MESSAGE(1300024);
	/**
	 * You are using too many skills simultaneously.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TOO_MANY_COOLING = new SM_SYSTEM_MESSAGE(1300025);

	/**
	 * You cannot do that while you are %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300026, value0);
	}

	/**
	 * You can use the skill only during combat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_NOT_IN_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1300027);
	/**
	 * That skill does not exist.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_NO_SUCH_SKILL = new SM_SYSTEM_MESSAGE(1300028);
	/**
	 * The skill has failed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_FAILED = new SM_SYSTEM_MESSAGE(1300029);
	/**
	 * You cannot use that because there is an obstacle in the way.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_OBSTACLE = new SM_SYSTEM_MESSAGE(1300030);
	/**
	 * You do not have a proper target for that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_FIND_VALID_TARGET = new SM_SYSTEM_MESSAGE(1300031);
	/**
	 * The target is too far away.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ATTACK_TOO_FAR_FROM_TARGET = new SM_SYSTEM_MESSAGE(1300032);
	/**
	 * You cannot attack as there is an obstacle in the way.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ATTACK_OBSTACLE_EXIST = new SM_SYSTEM_MESSAGE(1300033);
	/**
	 * You cannot attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ATTACK_CANT_FINT_VALID_TARGET = new SM_SYSTEM_MESSAGE(1300034);

	/**
	 * You acquired the %0 title as a quest reward.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_GET_REWARD_TITLE(int value0) {
		return new SM_SYSTEM_MESSAGE(1300035, new DescriptionId(value0));
	}

	/**
	 * A survey has arrived. Click the icon to open the survey window.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GMPOLL_GOT_POLL = new SM_SYSTEM_MESSAGE(1300036);
	/**
	 * There is no remaining survey to take part in.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GMPOLL_NO_POLL_REMAINED = new SM_SYSTEM_MESSAGE(1300037);

	/**
	 * %0 is running away.
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_COMBAT_NPC_FLEE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300038, value0);
	}

	/**
	 * %0 gives up the pursuit.
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_COMBAT_NPC_RETURN(int nameId) {
		return new SM_SYSTEM_MESSAGE(1300039, calculateNameId(nameId));
	}

	/**
	 * You have discovered [%subzone].
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_DISCOVERY_NEWZONE(String subzone) {
		return new SM_SYSTEM_MESSAGE(1300040, subzone);
	}

	/**
	 * You cannot remove the equipped item because the inventory is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UI_INVENTORY_FULL = new SM_SYSTEM_MESSAGE(1300042);
	/**
	 * You left the group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_SECEDE = new SM_SYSTEM_MESSAGE(1300043);
	/**
	 * A dead person cannot be invited to a group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UI_PARTY_DEAD = new SM_SYSTEM_MESSAGE(1300044);
	/**
	 * You cannot check the information on characters of another race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASK_PCINFO_OTHER_RACE = new SM_SYSTEM_MESSAGE(1300045);
	/**
	 * That person is not logged on.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASK_PCINFO_LOGOFF = new SM_SYSTEM_MESSAGE(1300046);

	/**
	 * You cannot leave the group in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_LEAVE_PARTY_DURING_PATH_FLYING(String value0) {
		return new SM_SYSTEM_MESSAGE(1300047, value0);
	}

	/**
	 * You cannot use an item while %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_USE_ITEM_DURING_PATH_FLYING(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300048, descriptionId);
	}

	/**
	 * %0 gives up the attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_COMBAT_NPC_RETURN_NOMOVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300049, value0);
	}

	/**
	 * You learned %0 (Level %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_LEARNED_NEW_SKILL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300050, value0, value1);
	}

	/**
	 * You stopped using %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_TOGGLE_SKILL_TURNED_OFF(String value0) {
		return new SM_SYSTEM_MESSAGE(1300051, value0);
	}

	/**
	 * That skill is not being used.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TOGGLE_SKILL_ALREADY_TURNED_OFF = new SM_SYSTEM_MESSAGE(1300052);

	/**
	 * You stopped using %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_MAINTAIN_SKILL_TURNED_OFF(String value0) {
		return new SM_SYSTEM_MESSAGE(1300053, value0);
	}

	/**
	 * That skill is not being used.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_MAINTAIN_SKILL_ALREADY_TURNED_OFF = new SM_SYSTEM_MESSAGE(1300054);

	/**
	 * The %0 skill effect has been removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSTATUS_SKILL_TURNED_OFF(String value0) {
		return new SM_SYSTEM_MESSAGE(1300055, value0);
	}

	/**
	 * The %0 skill effect cannot be removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSTATUS_SKILL_CAN_NOT_BE_TURNED_OFF_BY_TARGET(String value0) {
		return new SM_SYSTEM_MESSAGE(1300056, value0);
	}

	/**
	 * The %0 skill effect cannot be removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABSTATUS_SKILL_CAN_NOT_BE_TURNED_OFF_BY_CASTOR(String value0) {
		return new SM_SYSTEM_MESSAGE(1300057, value0);
	}

	/**
	 * You cured the altered state caused by %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_TURN_OFF_ABNORMAL_STATUS(String value0) {
		return new SM_SYSTEM_MESSAGE(1300058, value0);
	}

	/**
	 * The %0 skill was cancelled as %1 is already under a more powerful skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CONFLICT_WITH_OTHER_SKILL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300059, value0, value1);
	}

	/**
	 * You have not learned the skill to equip this weapon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_WEAPON_MASTERY_SKILL = new SM_SYSTEM_MESSAGE(1300060);
	/**
	 * You have not learned the skill to equip this armor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_ARMOR_MASTERY_SKILL = new SM_SYSTEM_MESSAGE(1300061);
	/**
	 * You cannot cast spells while silenced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_MAGIC_SKILL_WHILE_SILENCED = new SM_SYSTEM_MESSAGE(1300062);
	/**
	 * You cannot use physical skills while in a state of fear or restraint.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_PHYSICAL_SKILL_IN_FEAR = new SM_SYSTEM_MESSAGE(1300063);
	/**
	 * You cannot use the skill while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300064);
	/**
	 * Your actions are limited while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_ACT_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300065);
	/**
	 * You cannot attack while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_ATTACK_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300066);
	/**
	 * You cannot gather while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_GATHER_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300067);
	/**
	 * You cannot use the item while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_ITEM_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300068);
	/**
	 * You cannot equip the item while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_EQUIP_ITEM_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1300069);

	/**
	 * The %0 skill failed as there are already too many skills in effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_SLOT_FULL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300070, value0);
	}

	/**
	 * You cannot use the %0 skill in your current stance.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_THIS_SKILL_IN_CURRENT_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300071, value0);
	}

	/**
	 * You already have a spirit following you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_ALREADY_HAVE_A_FOLLOWER = new SM_SYSTEM_MESSAGE(1300072);
	/**
	 * As the spirit is too far, your summon has been forcibly canceled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_UNSUMMON_BY_TOO_DISTANCE = new SM_SYSTEM_MESSAGE(1300073);
	/**
	 * You are too far from the spirit is to issue an order.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_CANT_ORDER_BY_TOO_DISTANCE = new SM_SYSTEM_MESSAGE(1300074);
	/**
	 * You have not learned the Advanced Dual-Wielding skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_WEAPON_DUEL_SKILL = new SM_SYSTEM_MESSAGE(1300075);
	/**
	 * The target cannot be charmed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_ENSLAVE_TARGET_CANT_BE_ENSLAVED = new SM_SYSTEM_MESSAGE(1300076);
	/**
	 * You have failed to charm the target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_ENSLAVE_FAILED_TO_ENSLAVE = new SM_SYSTEM_MESSAGE(1300077);
	/**
	 * You have charmed the target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_ENSLAVE_SUCCEDED_TO_ENSLAVE = new SM_SYSTEM_MESSAGE(1300078);
	/**
	 * You have no dead pets.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_RESURRECT_PET_DONT_HAVE_DEAD_STONE = new SM_SYSTEM_MESSAGE(1300079);
	/**
	 * You do not have enough Kinah to resurrect the Charm Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_RESURRECT_PET_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300080);
	/**
	 * You are too far from the NPC to resurrect it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_RESURRECT_PET_TOO_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300081);
	/**
	 * You have resurrected the pet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_RESURRECT_PET_SUCCEEDED = new SM_SYSTEM_MESSAGE(1300082);
	/**
	 * Please try again after you have closed other dialog boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_RESURRECT_PET_RETRY_WHEN_CLOSE_OTHER_QUESTION_WND = new SM_SYSTEM_MESSAGE(1300083);
	/**
	 * You cannot transfer XP.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_GIVE_EXP_TO_PET_DONT_HAVE_PET = new SM_SYSTEM_MESSAGE(1300084);
	/**
	 * The amount of XP you have transferred to the spirit is not enough.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENSLAVE_GIVE_EXP_TO_PET_NOT_ENOUGH_EXP = new SM_SYSTEM_MESSAGE(1300085);

	/**
	 * %0 has reached level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENSLAVE_PET_LEVEL_CHANGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300086, value0, value1);
	}

	/**
	 * There is no target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_NO_TARGET = new SM_SYSTEM_MESSAGE(1300087);
	/**
	 * Invalid target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_SUMMON_IS_NOT_VALID = new SM_SYSTEM_MESSAGE(1300088);
	/**
	 * You cannot use this on enemies.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_NOTENEMY_ONLY = new SM_SYSTEM_MESSAGE(1300089);
	/**
	 * You can only use this on enemies.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_INVALID_TARGET_ENEMY_ONLY = new SM_SYSTEM_MESSAGE(1300090);

	/**
	 * You cannot duel with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_PARTNER_INVALID(String value0) {
		return new SM_SYSTEM_MESSAGE(1300091, value0);
	}

	/**
	 * %0 is already fighting a duel with another opponent.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_PARTNER_IN_DUEL_ALREADY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300092, value0);
	}

	/**
	 * You are already fighting a duel with another opponent.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_YOU_ARE_IN_DUEL_ALREADY = new SM_SYSTEM_MESSAGE(1300093);

	/**
	 * You challenged %0 to a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_REQUEST_TO_PARTNER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300094, value0);
	}

	/**
	 * %0 is answering another request and cannot respond.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_CANT_REQUEST_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300095, value0);
	}

	/**
	 * There is no one for you to challenge to a duel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_NO_USER_TO_REQUEST = new SM_SYSTEM_MESSAGE(1300096);

	/**
	 * %0 declined your challenge.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_HE_REJECT_DUEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300097, value0);
	}

	/**
	 * You won the duel against %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_YOU_WIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300098, value0);
	}

	/**
	 * You lost the duel against %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_YOU_LOSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300099, value0);
	}

	/**
	 * The duel with %0 ended due to the time limit.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300100, value0);
	}

	/**
	 * You are too far from %0 to start a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_PARTNER_TOO_FAR_FOR_START(String value0) {
		return new SM_SYSTEM_MESSAGE(1300101, value0);
	}

	/**
	 * You cannot find the user you have challenged to a duel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_LOST_REQUEST_DUEL_PARTNER = new SM_SYSTEM_MESSAGE(1300102);
	/**
	 * You are not ready to start a duel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_NOT_READY_TO_START_DUEL = new SM_SYSTEM_MESSAGE(1300103);

	/**
	 * The duel with %0 ended due to the time limit.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_TIMEOUT_WITHOUT_PARTNER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300104, value0);
	}

	/**
	 * You cannot use that on your target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_TO_CURRENT_TARGET = new SM_SYSTEM_MESSAGE(1300105);
	/**
	 * You can use it only when you are in Counterattack mode.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_IN_NONE_COUNTER_STATUS = new SM_SYSTEM_MESSAGE(1300106);
	/**
	 * This skill can only be used as part of a Chain Skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_IN_NONE_CHAINSKILL_STATUS = new SM_SYSTEM_MESSAGE(1300107);
	/**
	 * You can only cast that on a group member who is using a Special Attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_CAST_ONLY_TO_MY_PARTY_CASTING_ULTRASKILL = new SM_SYSTEM_MESSAGE(1300108);
	/**
	 * You interrupted the target's skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TARGET_SKILL_CANCELED = new SM_SYSTEM_MESSAGE(1300109);
	/**
	 * You must be equipped with a shield to use this skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NEED_SHIELD = new SM_SYSTEM_MESSAGE(1300110);
	/**
	 * You must be equipped with an Off-hand Weapon to use the skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NEED_DUAL_WEAPON = new SM_SYSTEM_MESSAGE(1300111);
	/**
	 * This skill can only be used from the rear of your opponent.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_USE_TO_TARGETS_BACK_ONLY = new SM_SYSTEM_MESSAGE(1300112);
	/**
	 * You can use this skill only while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_RESTRICTION_FLY_ONLY = new SM_SYSTEM_MESSAGE(1300113);
	/**
	 * You cannot use a skill while you are flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_RESTRICTION_NO_FLY = new SM_SYSTEM_MESSAGE(1300114);
	/**
	 * The attacker or the target is in a different area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ATTACK_INVALID_POSITION = new SM_SYSTEM_MESSAGE(1300115);
	/**
	 * Invalid target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ATTACK_IMPROPER_TARGET = new SM_SYSTEM_MESSAGE(1300116);
	/**
	 * You cannot equip the shield as you have not learned the Equip Shield skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_SHIELD_MASTERY_SKILL = new SM_SYSTEM_MESSAGE(1300117);
	/**
	 * You cannot attack in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_ATTACK_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300118);
	/**
	 * You cannot gather in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_GATHER_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300119);
	/**
	 * You cannot use that item in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_ITEM_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300120);
	/**
	 * You cannot change your equipment in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_EQUIP_ITEM_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300121);
	/**
	 * You cannot craft in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_COMBINE_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300122);
	/**
	 * You cannot use that skill in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_CAST_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300123);
	/**
	 * You cannot change mode in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_CHANGE_MODE__WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300124);
	/**
	 * You are too close to the target to use that skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_TOO_CLOSE = new SM_SYSTEM_MESSAGE(1300125);
	/**
	 * You cannot use the magic passage while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_GROUPGATE_WHEN_FLYING = new SM_SYSTEM_MESSAGE(1300126);
	/**
	 * You can only use that when you have a spirit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_USE_THIS_SKILL_WITHOUT_A_PET = new SM_SYSTEM_MESSAGE(1300127);
	/**
	 * You can only use it when your spirit is in attack mode.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_USE_THIS_SKILL_WHEN_PET_IS_NOT_ATTACK_MODE = new SM_SYSTEM_MESSAGE(1300128);
	/**
	 * You use the skill on yourself instead of the currently selected target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_AUTO_CHANGE_TARGET_TO_MY = new SM_SYSTEM_MESSAGE(1300129);
	/**
	 * Your spirit has no skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_USE_THIS_SKILL_TO_A_PET_THAT_HAS_NO_ULTRASKILL = new SM_SYSTEM_MESSAGE(1300130);
	/**
	 * You cannot use this skill during combat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_IN_COMBAT_STATE = new SM_SYSTEM_MESSAGE(1300131);

	/**
	 * %0 is running away.
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_COMBAT_NPC_FLEE_ORG(String value0) {
		return new SM_SYSTEM_MESSAGE(1300132, value0);
	}

	/**
	 * %0 gives up the pursuit.
	 */
	public static SM_SYSTEM_MESSAGE STR_UI_COMBAT_NPC_RETURN_ORG(String value0) {
		return new SM_SYSTEM_MESSAGE(1300133, value0);
	}

	/**
	 * %0 has withdrawn the challenge for a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_REQUESTER_WITHDRAW_REQUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300134, value0);
	}

	/**
	 * You have withdrawn the challenge to %0 for a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_WITHDRAW_REQUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300135, value0);
	}

	/**
	 * A duel between %0 and %1 has started.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_START_BROADCAST(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300136, value0, value1);
	}

	/**
	 * %0 defeated %1 in a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_STOP_BROADCAST(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300137, value0, value1);
	}

	/**
	 * The duel between %0 and %1 was a draw.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_TIMEOUT_BROADCAST(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300138, value0, value1);
	}

	/**
	 * The duel ends in %0 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_TIMEOUT_NOTIFY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300139, value0);
	}

	/**
	 * You cannot request a duel to %0 as the player is currently busy.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_START_OTHER_IS_BUSY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300140, value0);
	}

	/**
	 * You cannot learn this skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILLLEARNBOOK_CANT_USE_NO_SKILL = new SM_SYSTEM_MESSAGE(1300141);
	/**
	 * You have already learned this skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILLLEARNBOOK_CANT_USE_ALREADY_HAS_SKILL = new SM_SYSTEM_MESSAGE(1300142);
	/**
	 * You cannot use that item here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_ITEM_IN_CURRENT_POSITION = new SM_SYSTEM_MESSAGE(1300143);

	/**
	 * You have cancelled using the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_CANCELED(DescriptionId id) {
		return new SM_SYSTEM_MESSAGE(1300427, id);
	}

	/**
	 * You cannot craft here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_CAN_NOT_COMBINE_IN_CURRENT_POSITION = new SM_SYSTEM_MESSAGE(1300144);
	/**
	 * You cannot gather here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_GATHER_IN_CURRENT_POSTION = new SM_SYSTEM_MESSAGE(1300145);
	/**
	 * You cannot use the skill here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CANT_CAST_IN_CURRENT_POSTION = new SM_SYSTEM_MESSAGE(1300146);
	/**
	 * You cannot take off in your current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_TAKE_OFF__WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1300147);

	/**
	 * %0 has logged out.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ASK_OTHER_HAS_LOGOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300148, value0);
	}

	/**
	 * You cannot use this skill while transformed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_CAST_IN_SHAPECHANGE = new SM_SYSTEM_MESSAGE(1300149);
	/**
	 * You have no right to use the selected Magic Passage.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_GROUPGATE_NO_RIGHT = new SM_SYSTEM_MESSAGE(1300150);
	/**
	 * This skill can only be used in the Abyss.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_CAST_IN_NOT_ABYSS_WORLD = new SM_SYSTEM_MESSAGE(1300151);
	/**
	 * You cannot invite any more group members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANT_ADD_NEW_MEMBER = new SM_SYSTEM_MESSAGE(1300152);
	/**
	 * Only the group leader can transfer authority to another person.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ONLY_LEADER_CAN_CHANGE_LEADER = new SM_SYSTEM_MESSAGE(1300153);

	/**
	 * %0 has become the new group leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_IS_NEW_LEADER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300154, value0);
	}

	/**
	 * You have become the new group leader.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_YOU_BECOME_NEW_LEADER = new SM_SYSTEM_MESSAGE(1300155);
	/**
	 * Only the group leader can change the item distribution method.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ONLY_LEADER_CAN_CHANGE_LOOTING = new SM_SYSTEM_MESSAGE(1300156);
	/**
	 * The item distribution method of the group has been changed to Manual.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_LOOTING_CHANGED_TO_MANUAL = new SM_SYSTEM_MESSAGE(1300157);
	/**
	 * The item distribution method of the group has been changed to Auto.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_LOOTING_CHANGED_TO_AUTO = new SM_SYSTEM_MESSAGE(1300158);
	/**
	 * The user you invited to the group is currently offline.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_NO_USER_TO_INVITE = new SM_SYSTEM_MESSAGE(1300159);
	/**
	 * Only group leader can invite.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ONLY_LEADER_CAN_INVITE = new SM_SYSTEM_MESSAGE(1300160);

	/**
	 * %0 has declined your invitation.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_REJECT_INVITATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300161, value0);
	}

	/**
	 * You cannot invite yourself to a group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CAN_NOT_INVITE_SELF = new SM_SYSTEM_MESSAGE(1300162);
	/**
	 * You cannot issue an invitation while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANT_INVITE_WHEN_DEAD = new SM_SYSTEM_MESSAGE(1300163);
	/**
	 * The selected group member is currently offline.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_OFFLINE_MEMBER = new SM_SYSTEM_MESSAGE(1300164);
	/**
	 * Only the group leader can kick a member out.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ONLY_LEADER_CAN_BANISH = new SM_SYSTEM_MESSAGE(1300165);
	/**
	 * You have been kicked out of the group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_YOU_ARE_BANISHED = new SM_SYSTEM_MESSAGE(1300166);
	/**
	 * The group has been disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_IS_DISPERSED = new SM_SYSTEM_MESSAGE(1300167);

	/**
	 * %0 has left your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_LEAVE_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300168, value0);
	}

	/**
	 * %0 is already a member of another group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_IS_ALREADY_MEMBER_OF_OTHER_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300169, value0);
	}

	/**
	 * %0 is already a member of your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_IS_ALREADY_MEMBER_OF_OUR_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300170, value0);
	}

	/**
	 * You are not in any group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_YOU_ARE_NOT_PARTY_MEMBER = new SM_SYSTEM_MESSAGE(1300171);
	/**
	 * You are not a group member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_NOT_PARTY_MEMBER = new SM_SYSTEM_MESSAGE(1300172);

	/**
	 * You have invited %0 to join your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_INVITED_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300173, value0);
	}

	/**
	 * Currently, %0 cannot accept your group invitation.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300174, value0);
	}

	/**
	 * %0 has been disconnected.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_BECOME_OFFLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300175, value0);
	}

	/**
	 * %0 has been offline for too long and is automatically excluded from the group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_BECOME_OFFLINE_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300176, value0);
	}

	/**
	 * %0 has been kicked out of your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_IS_BANISHED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300177, value0);
	}

	/**
	 * The rare item distribution method of the group has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_RARE_LOOTING_CHANGED_TO_MANUAL = new SM_SYSTEM_MESSAGE(1300178);
	/**
	 * The rare item distribution method of the group has been changed to Auto.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_RARE_LOOTING_CHANGED_TO_AUTO = new SM_SYSTEM_MESSAGE(1300179);
	/**
	 * The rare item distribution method of the group has been changed to Dice Roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_RARE_LOOTING_CHANGED_TO_DICE = new SM_SYSTEM_MESSAGE(1300180);
	/**
	 * A group member cannot be kicked out before the completion of loot distribution.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANNOT_BANISH_ITEMPOOL_NOT_EMPTY = new SM_SYSTEM_MESSAGE(1300181);

	/**
	 * %0 rolled the dice and got a %num1.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ITEM_DICE(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1300182, value0, num1);
	}

	/**
	 * You can roll the dice once more if the rolled number is less than 100.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ITEM_DICE_AGAIN = new SM_SYSTEM_MESSAGE(1300183);
	/**
	 * The item distribution method of the group has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_LOOTING_CHANGED_TO_FREEFORALL = new SM_SYSTEM_MESSAGE(1300184);
	/**
	 * The item distribution method of the group has been changed to Round-robin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_LOOTING_CHANGED_TO_ROUNDROBIN = new SM_SYSTEM_MESSAGE(1300185);
	/**
	 * The item distribution method of the group has been changed to Group Leader.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_LOOTING_CHANGED_TO_LEADERONLY = new SM_SYSTEM_MESSAGE(1300186);

	/**
	 * %0 rolled the dice and got a %num1 (max. %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ITEM_DICE_CUSTOM(String value0, int num1, int num2) {
		return new SM_SYSTEM_MESSAGE(1300187, value0, num1, num2);
	}

	/**
	 * You cannot invite members of other race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANT_INVITE_OTHER_RACE = new SM_SYSTEM_MESSAGE(1300188);

	/**
	 * You have invited %0's group to the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_INVITED_HIS_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300189, value0);
	}

	/**
	 * %0 has declined your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300190, value0);
	}

	/**
	 * Currently, %0 cannot accept your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300191, value0);
	}

	/**
	 * %0 is already a member of another alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_IS_ALREADY_MEMBER_OF_OTHER_ALLIANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300192, value0);
	}

	/**
	 * %0 is already a member of your alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_IS_ALREADY_MEMBER_OF_OUR_ALLIANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300193, value0);
	}

	/**
	 * You cannot invite %0 to the alliance as he or she is not a group leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CAN_NOT_INVITE_HIM_HE_IS_NOT_PARTY_LEADER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300194, value0);
	}

	/**
	 * You cannot invite %0 to the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CAN_NOT_INVITE_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300195, value0);
	}

	/**
	 * You cannot invite any more as the alliance is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER = new SM_SYSTEM_MESSAGE(1300196);
	/**
	 * Only the group leader can leave the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_ONLY_PARTY_LEADER_CAN_LEAVE_ALLIANCE = new SM_SYSTEM_MESSAGE(1300197);
	/**
	 * Your group is not part of an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_YOUR_PARTY_IS_NOT_ALLIANCE_MEMBER = new SM_SYSTEM_MESSAGE(1300198);

	/**
	 * %0's group has left the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HIS_PARTY_LEAVE_ALLIANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300199, value0);
	}

	/**
	 * Your group has left the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_MY_PARTY_LEAVE_ALLIANCE = new SM_SYSTEM_MESSAGE(1300200);
	/**
	 * The alliance has been disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_DISPERSED = new SM_SYSTEM_MESSAGE(1300201);

	/**
	 * %0 has left the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_LEAVED_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300202, value0);
	}

	/**
	 * %0 has been offline for too long and has been automatically kicked out of the group and the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_LEAVED_PARTY_OFFLINE_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300203, value0);
	}

	/**
	 * %0 has been kicked out of the group and thus the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_IS_BANISHED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300204, value0);
	}

	/**
	 * %0 has become the new group leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_BECOME_PARTY_LEADER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300205, value0);
	}

	/**
	 * The item distribution method of the alliance has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_LOOTING_CHANGED_TO_FREE = new SM_SYSTEM_MESSAGE(1300206);
	/**
	 * The item distribution method of the alliance has been changed to Auto.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_LOOTING_CHANGED_TO_RANDOM = new SM_SYSTEM_MESSAGE(1300207);

	/**
	 * %0 has already requested the item distribution method to be changed.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_PROCESSING_HIS_REQUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300208, value0);
	}

	/**
	 * Your request to change the item distribution method is being processed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_PROCESSING_YOUR_REQUEST = new SM_SYSTEM_MESSAGE(1300209);

	/**
	 * %0 denied %1's request to change the item distribution method.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_HE_DENIED_HIS_ASK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300210, value0, value1);
	}

	/**
	 * %0's request to change the item distribution method has been denied.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_HE_DENIED_MY_ASK(String value0) {
		return new SM_SYSTEM_MESSAGE(1300211, value0);
	}

	/**
	 * You asked the alliance Captain to change the item distribution method.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_ASK_SUBMITTED = new SM_SYSTEM_MESSAGE(1300212);

	/**
	 * %0's request to change the item distribution method timed out.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_CHANGE_LOOT_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300213, value0);
	}

	/**
	 * You asked the alliance Captain for permission to pick up %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_ASK_SUBMITTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300214, value0);
	}

	/**
	 * Your request for permission to pick up %0 is being processed.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_PROCESSING_YOUR_REQUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300215, value0);
	}

	/**
	 * %0's request for permission to pick up %1 was approved.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_ALL_ACCEPT_HIM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300216, value0, value1);
	}

	/**
	 * %0 denied %1's request for permission to pick up %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_HE_DENIED(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1300217, value0, value1, value2);
	}

	/**
	 * %0 denied your request for permission to pick up %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_HE_DENIED_MY_ASK(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300218, value0, value1);
	}

	/**
	 * %0's request for permission to pick up %1 timed out.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_PICKUP_ITEM_TIMEOUT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300219, value0, value1);
	}

	/**
	 * The request cannot be processed, as there are already too many requests pending approval by the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_TOO_MANY_VOTE = new SM_SYSTEM_MESSAGE(1300220);
	/**
	 * You are not a member of an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_YOU_ARE_NOT_PARTY_ALLIANCE_MEMBER = new SM_SYSTEM_MESSAGE(1300221);

	/**
	 * %0 has been disconnected.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_BECOME_OFFLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300222, value0);
	}

	/**
	 * Only the alliance captain can change the item distribution method.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_ONLY_LEADER_CAN_CHANGE_LOOTING = new SM_SYSTEM_MESSAGE(1300223);
	/**
	 * The item distribution method of the alliance has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_LOOTING_CHANGED_TO_FREEFORALL = new SM_SYSTEM_MESSAGE(1300224);
	/**
	 * The item distribution method of the alliance has been changed to Round-robin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_LOOTING_CHANGED_TO_ROUNDROBIN = new SM_SYSTEM_MESSAGE(1300225);
	/**
	 * The item distribution method of the alliance has been changed to Captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_LOOTING_CHANGED_TO_LEADERONLY = new SM_SYSTEM_MESSAGE(1300226);
	/**
	 * You cannot invite the selected player as he or she is too busy.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_INVITE_OTHER_IS_BUSY = new SM_SYSTEM_MESSAGE(1300227);
	/**
	 * That name is invalid. Please try another.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_INVALID_GUILD_NAME = new SM_SYSTEM_MESSAGE(1300228);
	/**
	 * You are too far from the NPC to create a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_TOO_FAR_FROM_CREATOR_NPC = new SM_SYSTEM_MESSAGE(1300229);
	/**
	 * Please try again after you have closed other dialog boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_RETRY_WHEN_CLOSE_OTHER_QUESTION_WND = new SM_SYSTEM_MESSAGE(1300230);
	/**
	 * You do not have enough Kinah to create a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300231);
	/**
	 * You cannot create a Legion as you are already a member of another Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_ALREADY_BELONGS_TO_GUILD = new SM_SYSTEM_MESSAGE(1300232);
	/**
	 * That name is invalid. Please try another..
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_SAME_GUILD_EXIST = new SM_SYSTEM_MESSAGE(1300233);
	/**
	 * You cannot create a new Legion as the grace period between creating Legions has not expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_LAST_DAY_CHECK = new SM_SYSTEM_MESSAGE(1300234);

	/**
	 * The %0 Legion has been created.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CREATED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300235, value0);
	}

	/**
	 * You cannot leave your Legion during a war.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_CANT_LEAVE_WHILE_WAR = new SM_SYSTEM_MESSAGE(1300236);
	/**
	 * You cannot leave your Legion while using the Legion Warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_CANT_LEAVE_GUILD_WHILE_USING_WAREHOUSE = new SM_SYSTEM_MESSAGE(1300237);
	/**
	 * You cannot leave your Legion unless you transfer Brigade General authority to someone else.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_MASTER_CANT_LEAVE_BEFORE_CHANGE_MASTER = new SM_SYSTEM_MESSAGE(1300238);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300239);

	/**
	 * %0 has left the Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_HE_LEFT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300240, value0);
	}

	/**
	 * You have left the %0 Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_DONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300241, value0);
	}

	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300242);
	/**
	 * You cannot kick yourself out from a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_CANT_BANISH_SELF = new SM_SYSTEM_MESSAGE(1300243);
	/**
	 * You do not have the authority to kick out a Legion member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_DONT_HAVE_RIGHT_TO_BANISH = new SM_SYSTEM_MESSAGE(1300244);
	/**
	 * You cannot kick a Legion member out during a war.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_CANT_BAN_MEMBER_WHILE_WAR = new SM_SYSTEM_MESSAGE(1300245);

	/**
	 * You have been kicked out of the %0 Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_BANISHIED_FROM_GUILD_BY_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300246, value0);
	}

	/**
	 * %0 kicked %1 out of the Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_BANSIH_HE_BANISHED_HIM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300247, value0, value1);
	}

	/**
	 * %0 is not a member of your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_BANISH_HE_IS_NOT_MY_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300248, value0);
	}

	/**
	 * You cannot kick out the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_CAN_BANISH_MASTER = new SM_SYSTEM_MESSAGE(1300249);
	/**
	 * You cannot issue a Legion invitation while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CANT_INVITE_WHEN_DEAD = new SM_SYSTEM_MESSAGE(1300250);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300251);
	/**
	 * You have no authority to invite others to the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_DONT_HAVE_RIGHT_TO_INVITE = new SM_SYSTEM_MESSAGE(1300252);
	/**
	 * There is no user to invite to your Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_NO_USER_TO_INVITE = new SM_SYSTEM_MESSAGE(1300253);
	/**
	 * You cannot invite yourself to a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_INVITE_SELF = new SM_SYSTEM_MESSAGE(1300254);

	/**
	 * %0 is already a member of your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_HE_IS_MY_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300255, value0);
	}

	/**
	 * %0 is a member of another Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_HE_IS_OTHER_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300256, value0);
	}

	/**
	 * There is no room in the Legion for more members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_ADD_MEMBER_ANY_MORE = new SM_SYSTEM_MESSAGE(1300257);

	/**
	 * You have sent a Legion invitation to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_SENT_INVITE_MSG_TO_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300258, value0);
	}

	/**
	 * %0 has declined your Legion invitation.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_HE_REJECTED_INVITATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300259, value0);
	}

	/**
	 * %0 has joined your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_HE_JOINED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300260, value0);
	}

	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300261);
	/**
	 * You cannot change the ranks of Legion members because you are not the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300262);
	/**
	 * The Legion Brigade General cannot change its own rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_ERROR_SELF = new SM_SYSTEM_MESSAGE(1300263);
	/**
	 * There is no one to change rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_NO_USER = new SM_SYSTEM_MESSAGE(1300264);

	/**
	 * %0 is not a member of your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_HE_IS_NOT_MY_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300265, value0);
	}

	/**
	 * %0 has become the Legion Brigade General.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONE_1_GUILD_MASTER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300266, value0);
	}

	/**
	 * %0 has become a Legion Centurion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONE_2_GUILD_OFFICER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300267, value0);
	}

	/**
	 * %0 has become a Legionary.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONE_3_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300268, value0);
	}

	/**
	 * You do not have the authority to change the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300269);
	/**
	 * You cannot transfer your Brigade General authority to an offline user.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_NO_SUCH_USER = new SM_SYSTEM_MESSAGE(1300270);
	/**
	 * You are already the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_ERROR_SELF = new SM_SYSTEM_MESSAGE(1300271);

	/**
	 * %0 is not a member of your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_NOT_MY_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300272, value0);
	}

	/**
	 * %0 has become the Legion Brigade General.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_DONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300273, value0);
	}

	/**
	 * You cannot join the alliance because you are not the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_JOIN_CLAN_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300274);
	/**
	 * You cannot leave the alliance because you are not the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEAVE_CLAN_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300275);
	/**
	 * You do not have the authority to modify the Legion Announcement.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WRITE_NOTICE_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300276);
	/**
	 * The Legion Announcement has been modified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WRITE_NOTICE_DONE = new SM_SYSTEM_MESSAGE(1300277);
	/**
	 * You must be a Legion member to use the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NO_GUILD_TO_DEPOSIT = new SM_SYSTEM_MESSAGE(1300278);
	/**
	 * You cannot use the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_USE_GUILD_STORAGE = new SM_SYSTEM_MESSAGE(1300279);
	/**
	 * Another Legion member is using the warehouse. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WAREHOUSE_IN_USE = new SM_SYSTEM_MESSAGE(1300280);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WRITE_INTRO_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300281);
	/**
	 * Your Character Information has been modified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WRITE_INTRO_DONE = new SM_SYSTEM_MESSAGE(1300282);
	/**
	 * You have no authority to change the Legion authority settings.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_RIGHT_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300283);
	/**
	 * The Legion authority has been modified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_RIGHT_DONE = new SM_SYSTEM_MESSAGE(1300284);

	/**
	 * Legion Information: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INTRO(String value0) {
		return new SM_SYSTEM_MESSAGE(1300285, value0);
	}

	/**
	 * You do not have enough Kinah for cancellation.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CREATE_NOT_ENOUGH_MONEY_1 = new SM_SYSTEM_MESSAGE(1300286);
	/**
	 * There is no room in the Legion for more members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_JOIN_TO_GUILD_BY_SIZE_LIMIT = new SM_SYSTEM_MESSAGE(1300287);
	/**
	 * You cannot join the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_JOIN_TO_GUILD = new SM_SYSTEM_MESSAGE(1300288);
	/**
	 * You cannot join the Legion as the player who invited you is dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_JOIN_TO_GUILD_INVITOR_IS_DEAD = new SM_SYSTEM_MESSAGE(1300289);
	/**
	 * Currently, the selected player cannot be invited to join your Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CANT_INVITE_WHEN_HE_IS_QUESTION_ASKED = new SM_SYSTEM_MESSAGE(1300290);
	/**
	 * The target is not valid. Please select a player.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_INCORRECT_TARGET = new SM_SYSTEM_MESSAGE(1300291);
	/**
	 * This authority cannot be granted to the rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_RIGHT_CANT_GIVE_RIGHT = new SM_SYSTEM_MESSAGE(1300292);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300293);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300294);
	/**
	 * Please try again after you have closed other dialog boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_RETRY_WHEN_CLOSE_OTHER_QUESTION_WND = new SM_SYSTEM_MESSAGE(1300295);
	/**
	 * You are too far from the NPC to cancel the Legion disbanding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_TOO_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300296);
	/**
	 * You cannot disband the Legion during a war.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_CANT_DISPERSE_WHILE_WAR = new SM_SYSTEM_MESSAGE(1300297);
	/**
	 * You cannot disband your Legion while you are using the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_CANT_DISPERSE_GUILD_WHILE_USING_WAREHOUSE = new SM_SYSTEM_MESSAGE(1300298);
	/**
	 * You cannot disband a Legion that has a fortress or hideout.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_CANT_DISPERSE_GULILD_HAVING_HOUSE = new SM_SYSTEM_MESSAGE(1300299);
	/**
	 * You have no authority to disband the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_ONLY_MASTER_CAN_DISPERSE = new SM_SYSTEM_MESSAGE(1300300);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300301);

	/**
	 * The %0 Legion has been disbanded.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_DONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300302, value0);
	}

	/**
	 * The Brigade General has requested to disband the Legion. The expected time of disbanding is %DATETIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_REQUESTED(String datetime0) {
		return new SM_SYSTEM_MESSAGE(1300303, datetime0);
	}

	/**
	 * You have already requested to disband the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_ALREADY_REQUESTED = new SM_SYSTEM_MESSAGE(1300304);
	/**
	 * You are too far from the NPC to disband the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_TOO_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300305);
	/**
	 * You cannot delete a character that joined a Legion. Please try again after it has left the Legion or the Legion is disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_1 = new SM_SYSTEM_MESSAGE(1300306);
	/**
	 * The Legion disbanding mode has been cancelled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_CANCEL = new SM_SYSTEM_MESSAGE(1300307);

	/**
	 * Time remaining until disbanding: %DURATIONDAY0
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_TIME(String durationday0) {
		return new SM_SYSTEM_MESSAGE(1300308, durationday0);
	}

	/**
	 * Only the Legion Brigade General can cancel the disbanding mode.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_ONLY_MASTER_CAN_CANCEL = new SM_SYSTEM_MESSAGE(1300309);
	/**
	 * The Legion is not waiting to be disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_STAYMODE_CANCEL_YOUR_GUILD_IS_NOT_DISPERS_REQUESTED = new SM_SYSTEM_MESSAGE(1300310);
	/**
	 * You cannot invite members of other race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_INVITE_OTHER_RACE = new SM_SYSTEM_MESSAGE(1300311);
	/**
	 * You are not a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_NICKNAME_I_AM_NOT_BELONG_TO_GUILD = new SM_SYSTEM_MESSAGE(1300312);
	/**
	 * You have no authority to bestow a title.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_NICKNAME_DONT_HAVE_RIGHT_TO_CHANGE_NICKNAME = new SM_SYSTEM_MESSAGE(1300313);

	/**
	 * %0 is not a member of your Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_NICKNAME_HE_IS_NOT_MY_GUILD_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300314, value0);
	}

	/**
	 * Only the Legion Brigade General can request to raise the level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1300315);

	/**
	 * You need to complete the %0 legion task to level up the legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_LEVEL_UP_CHALLENGE_TASK(int currentLevel) {
		return new SM_SYSTEM_MESSAGE(904452, currentLevel);
	}

	/**
	 * The Legion is already at the highest level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_CANT_LEVEL_UP = new SM_SYSTEM_MESSAGE(1300316);
	/**
	 * You do not have enough Contribution Points.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_NOT_ENOUGH_POINT = new SM_SYSTEM_MESSAGE(1300317);
	/**
	 * Your Legion does not have enough members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_NOT_ENOUGH_MEMBER = new SM_SYSTEM_MESSAGE(1300318);
	/**
	 * You do not have enough Kinah.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300319);

	/**
	 * Your Legion is now at level %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_DONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300320, value0);
	}

	/**
	 * Please try again after you have closed other input boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_RETRY_WHEN_CLOSE_OTHER_QUESTION_WND = new SM_SYSTEM_MESSAGE(1300321);
	/**
	 * You do not have the authority to use the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WAREHOUSE_NO_RIGHT = new SM_SYSTEM_MESSAGE(1300322);
	/**
	 * The Legion warehouse is now loading. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WAREHOUSE_IN_LOADING = new SM_SYSTEM_MESSAGE(1300323);
	/**
	 * Your Legion does not have enough funds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WAREHOUSE_NOT_ENOUGH_FUND = new SM_SYSTEM_MESSAGE(1300324);
	/**
	 * The target is busy and cannot be invited at the moment.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_OTHER_IS_BUSY = new SM_SYSTEM_MESSAGE(1300325);
	/**
	 * You are too far from the NPC to raise the Legion level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_LEVEL_TOO_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300326);
	/**
	 * You are already a member of a Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_YOU_ARE_ALREADY_BELONGS_TO_GUILD = new SM_SYSTEM_MESSAGE(1300327);
	/**
	 * You cannot join another Legion while waiting for your Legion to be created.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_YOU_ARE_WAITING_FOR_GUILD_CREATE = new SM_SYSTEM_MESSAGE(1300328);
	/**
	 * The Legion you were to join no longer exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_JOIN_NOT_EXISTS = new SM_SYSTEM_MESSAGE(1300329);

	/**
	 * You nominated %0 as the next Legion Brigade General.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_SENT_OFFER_MSG_TO_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300330, value0);
	}

	/**
	 * You cannot request the selected player to become the Legion Brigade General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_SENT_CANT_OFFER_WHEN_HE_IS_QUESTION_ASKED = new SM_SYSTEM_MESSAGE(1300331);

	/**
	 * %0 has declined to become the Legion Brigade General.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MASTER_HE_DECLINE_YOUR_OFFER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300332, value0);
	}

	/**
	 * You cannot use the Legion warehouse during the disbandment waiting period.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WAREHOUSE_CANT_USE_WHILE_DISPERSE = new SM_SYSTEM_MESSAGE(1300333);
	/**
	 * Limited edition items are all sold out.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_ITEM_SOLD_OUT = new SM_SYSTEM_MESSAGE(1300334);
	/**
	 * You cannot buy this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_USER_BUY_FAILED = new SM_SYSTEM_MESSAGE(1300335);

	/**
	 * %0 does not sell items.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUY_SELL_HE_DOES_NOT_SELL_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300336, value0);
	}

	/**
	 * %0 does not buy items.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUY_SELL_HE_DOES_NOT_BUY_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300337, value0);
	}

	/**
	 * You are too away to trade.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_TOO_FAR_TO_TRADE = new SM_SYSTEM_MESSAGE(1300338);
	/**
	 * You do not have enough Kinah to buy the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_NOT_ENOUGH_MONEY_TO_BUY_ITEM = new SM_SYSTEM_MESSAGE(1300339);
	/**
	 * You have bought the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_USER_BOUGHT_ITEMS = new SM_SYSTEM_MESSAGE(1300340);
	/**
	 * Sales complete.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_USER_SELL_ITEMS = new SM_SYSTEM_MESSAGE(1300341);
	/**
	 * You cannot sell equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_CAN_NOT_SELL_EQUIPED_ITEM = new SM_SYSTEM_MESSAGE(1300342);
	/**
	 * The price of the item has changed. Please try buying it again after you have checked the changed price.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_PRICE_CHANGED_RETRY_PLEASE = new SM_SYSTEM_MESSAGE(1300343);

	/**
	 * %0 is not an item that can be sold.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUY_SELL_ITEM_CAN_NOT_BE_SELLED_TO_NPC(String value0) {
		return new SM_SYSTEM_MESSAGE(1300344, value0);
	}

	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300345);
	/**
	 * You are too far to have a conversation.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DIALOG_TOO_FAR_TO_TALK = new SM_SYSTEM_MESSAGE(1300346);
	/**
	 * You are already trading with someone else.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_YOU_ARE_ALREADY_EXCHANGING = new SM_SYSTEM_MESSAGE(1300347);
	/**
	 * You cannot trade while you are invisible.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_EXCHANGE_WHILE_INVISIBLE = new SM_SYSTEM_MESSAGE(1300348);
	/**
	 * You cannot trade with an invisible player.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_EXCHANGE_WITH_INVISIBLE_USER = new SM_SYSTEM_MESSAGE(1300349);
	/**
	 * You cannot trade as you are overburdened with items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_TOO_HEAVY_TO_TRADE = new SM_SYSTEM_MESSAGE(1300350);
	/**
	 * You have no one to trade with.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_NO_ONE_TO_EXCHANGE = new SM_SYSTEM_MESSAGE(1300351);
	/**
	 * You are too far from the target to trade.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_TOO_FAR_TO_EXCHANGE = new SM_SYSTEM_MESSAGE(1300352);

	/**
	 * You sent a trade message to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXCHANGE_ASKED_EXCHANGE_TO_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300353, value0);
	}

	/**
	 * %0 declined your trade offer.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXCHANGE_HE_REJECTED_EXCHANGE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300354, value0);
	}

	/**
	 * The target is already trading with someone else.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_PARTNER_IS_EXCHANGING_WITH_OTHER = new SM_SYSTEM_MESSAGE(1300355);

	/**
	 * %0 cannot trade at the moment.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_ASK_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300356, value0);
	}

	/**
	 * You cannot trade with the target as the target is carrying too many items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTNER_TOO_HEAVY_TO_EXCHANGE = new SM_SYSTEM_MESSAGE(1300357);

	/**
	 * %0 is not a tradable item.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXCHANGE_ITEM_CANNOT_BE_EXCHANGED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300358, value0);
	}

	/**
	 * You cannot trade with the target as you are carrying too many items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_EXCHANGE_HEAVY_TO_ADD_EXCHANGE_ITEM = new SM_SYSTEM_MESSAGE(1300359);

	/**
	 * You cannot trade as the target already has the limited possession item %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_EXCHANGE_PARTNER_HAS_LORE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300360, value0);
	}

	/**
	 * The trade is complete.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_COMPLETE = new SM_SYSTEM_MESSAGE(1300361);
	/**
	 * The other player has pressed the Lock List button.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_OTHER_PRESSED_CHECK = new SM_SYSTEM_MESSAGE(1300362);
	/**
	 * The other player has pressed the Final Confirmation button.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_OTHER_PRESSED_OK = new SM_SYSTEM_MESSAGE(1300363);
	/**
	 * The trade has been cancelled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CANCELED = new SM_SYSTEM_MESSAGE(1300364);
	/**
	 * You cannot sell equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CANT_SELL_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300365);
	/**
	 * You cannot trade any more as your inventory is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_FULL_INVENTORY = new SM_SYSTEM_MESSAGE(1300366);
	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300367);
	/**
	 * This item cannot be registered.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_CAN_NOT_REGISTER_ITEM = new SM_SYSTEM_MESSAGE(1300368);
	/**
	 * This is not a tradable item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_ITEM_CAN_NOT_BE_EXCHANGED = new SM_SYSTEM_MESSAGE(1300369);

	/**
	 * You cannot use the selected item until you reach the %0 rank.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_RANK(int value0) {
		return new SM_SYSTEM_MESSAGE(1300370, new DescriptionId(value0));
	}

	/**
	 * Your Class cannot use the selected item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_CLASS = new SM_SYSTEM_MESSAGE(1300371);

	/**
	 * You cannot use %1 until you reach level %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(int value1, int value0) {
		return new SM_SYSTEM_MESSAGE(1300372, value0, new DescriptionId(value1));
	}

	/**
	 * Your race cannot use this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_RACE = new SM_SYSTEM_MESSAGE(1300373);
	/**
	 * Your nationality prevents you from using this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_NATION = new SM_SYSTEM_MESSAGE(1300374);
	/**
	 * This item cannot be used by your gender.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_GENDER = new SM_SYSTEM_MESSAGE(1300375);
	/**
	 * You are too overburdened to pick up any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_HEAVY = new SM_SYSTEM_MESSAGE(1300376);
	/**
	 * Another player has the first chance to pick up this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PICKUP_ITEM_FAILED_NOT_MY_ITEM = new SM_SYSTEM_MESSAGE(1300377);
	/**
	 * You are too far away to pick up the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PICKUP_ITEM_FAILED_TOO_FAR = new SM_SYSTEM_MESSAGE(1300378);
	/**
	 * You cannot put down any more items at this place.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_DROP_THE_LOC = new SM_SYSTEM_MESSAGE(1300379);
	/**
	 * You cannot discard equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_DROP_WORN = new SM_SYSTEM_MESSAGE(1300380);

	/**
	 * You cannot discard %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNBREAKABLE_ITEM(DescriptionId value0) {
		return new SM_SYSTEM_MESSAGE(1300381, value0);
	}

	/**
	 * %0 is currently refusing to accept items.
	 */
	public static SM_SYSTEM_MESSAGE STR_IS_NOT_WILLING_TO_RECEIVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300382, value0);
	}

	/**
	 * You cannot give equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_GIVE_WORN = new SM_SYSTEM_MESSAGE(1300383);
	/**
	 * You are too overburdened to fight.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_HEAVY_TO_ATTACK = new SM_SYSTEM_MESSAGE(1300384);

	/**
	 * You ate %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EAT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300385, value0);
	}

	/**
	 * You cannot equip %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_EQUIP(String value0) {
		return new SM_SYSTEM_MESSAGE(1300386, value0);
	}

	/**
	 * You do not buy %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SELL_TO_NPC_NO_INTEREST_IN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300387, value0);
	}

	/**
	 * You do not have enough Kinah.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300388);

	/**
	 * %0 cannot be discarded.
	 */
	public static SM_SYSTEM_MESSAGE STR_ITEM_CANNOT_BE_DROPPED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300389, value0);
	}

	/**
	 * %0 cannot be given to others.
	 */
	public static SM_SYSTEM_MESSAGE STR_ITEM_CANNOT_BE_GIVEN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300390, value0);
	}

	/**
	 * %0 gave you %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_TO_YOU(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300391, value0, value1);
	}

	/**
	 * That item is limited to one per person, and you already have one in your inventory.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CAN_NOT_BUY_LORE_ITEM = new SM_SYSTEM_MESSAGE(1300392);

	/**
	 * You cannot use %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_USE_ITEM(DescriptionId value0) {
		return new SM_SYSTEM_MESSAGE(1300393, value0);
	}

	/**
	 * You cannot use %0 as you have already used it to its maximum usage count.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_USE_ITEM_OUT_OF_USABLE_COUNT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300394, value0);
	}

	/**
	 * You cannot use %0 to the maximum usage count as the item is currently equipped.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_USE_WORN_ITEM_OUT_OF_USABLE_COUNT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300395, value0);
	}

	/**
	 * You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300396, value0);
	}

	/**
	 * You cannot attack because you have no arrow.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_ATTACK_NO_ARROW = new SM_SYSTEM_MESSAGE(1300397);
	/**
	 * You must board the robot to use this skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_NO_ROBOT = new SM_SYSTEM_MESSAGE(1301067);
	/**
	 * You do not have a weapon to modify the appearance of.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_CHANGE_WEAPON_SKIN__THERE_IS_NO_WEAPON = new SM_SYSTEM_MESSAGE(1300398);
	/**
	 * You cannot modify the appearance of the selected item as it is not a weapon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_CHANGE_WEAPON_SKIN__SELECTED_ITEM_IS_NOT_WEAPON = new SM_SYSTEM_MESSAGE(1300399);
	/**
	 * You can only modify the appearance of the weapon to another of the same type.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_CHANGE_WEAPON_SKIN__DIFFERENT_WEAPON_TYPE = new SM_SYSTEM_MESSAGE(1300400);

	/**
	 * You have equipped the Stigma Stone and acquired the %0 skill (Level %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_YOU_CAN_USE_THIS_SKILL_BY_STIGMA_STONE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300401, value0, value1);
	}

	/**
	 * You have removed the Stigma Stone, but you can still use the %0 skill (Level %1) as you are equipped with another stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_AFTER_UNEQUIP_STONE_YOU_CAN_USE_THIS_SKILL_LEVEL_BY_OTHER_STONE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300402, value0, value1);
	}

	/**
	 * You have removed the Stigma Stone and can no longer use the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_YOU_CANNOT_USE_THIS_SKILL_AFTER_UNEQUIP_STIGMA_STONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300403, value0);
	}

	/**
	 * You need help from a Stigma Master to equip the Stigma Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_TO_EQUIP_STONE_TALK_WITH_STIGMA_NPC = new SM_SYSTEM_MESSAGE(1300404);
	/**
	 * You need help from a Stigma Master to remove the Stigma Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_TO_UNEQUIP_STONE_TALK_WITH_STIGMA_NPC = new SM_SYSTEM_MESSAGE(1300405);

	/**
	 * You need %0 Stigma Shard(s) to equip this Stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_CANNT_EQUIP_STONE_OUT_OF_AVAILABLE_STIGMA_POINT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300406, value0);
	}

	/**
	 * You cannot equip that Stigma Stone because you have not learned the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_CANNT_EQUIP_STONE_YOU_DO_NOT_HAVE_THIS_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300407, value0);
	}

	/**
	 * There is no Stigma slot available.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_SLOT_IS_NOT_OPENED = new SM_SYSTEM_MESSAGE(1300408);

	/**
	 * %0 cannot be equipped as its rank exceeds the maximum rank of the Stigma slot.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_TOO_HIGH_STONE_RANK_FOR_SLOT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300409, value0);
	}

	/**
	 * You cannot remove the Stigma Stone because %1 is a prerequisite for the %0th Stigma Stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_CANNT_UNEQUIP_STONE_OTHER_STONE_NEED_ITS_SKILL(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300410, value1, value0);
	}

	/**
	 * You have spent %num0sp.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_EXHAUST(int num0sp) {
		return new SM_SYSTEM_MESSAGE(1300411, num0sp);
	}

	/**
	 * %num0sp has been returned to you.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_RETURN(int num0sp) {
		return new SM_SYSTEM_MESSAGE(1300412, num0sp);
	}

	/**
	 * You do not have enough Kinah to equip the Stigma Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300413);

	/**
	 * You can no longer use the %0 skill acquired through the Stigma Stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_NOT_USABLE_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300414, value0);
	}

	/**
	 * This Stigma Stone cannot be equipped.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_CANNOT_EQUIP_STONE = new SM_SYSTEM_MESSAGE(1300415);
	/**
	 * You are carrying too many items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_TOO_MANY_ITEMS_INVENTORY = new SM_SYSTEM_MESSAGE(1300416);
	/**
	 * There is no space in the warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_TOO_MANY_ITEMS_WAREHOUSE = new SM_SYSTEM_MESSAGE(1300417);
	/**
	 * You cannot store this in the warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_CANT_DEPOSIT_ITEM = new SM_SYSTEM_MESSAGE(1300418);
	/**
	 * You are too far from the NPC.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_TOO_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300419);
	/**
	 * Equipped items cannot be stored in the warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_DEPOSIT_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300420);
	/**
	 * There is no space in the warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_DEPOSIT_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300421);

	/**
	 * You cannot have this item as you already have the limited possession item %0%.
	 */
	public static SM_SYSTEM_MESSAGE STR_CAN_NOT_GET_LORE_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300422, descriptionId);
	}

	/**
	 * You have used %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_USE_ITEM(DescriptionId value0) {
		return new SM_SYSTEM_MESSAGE(1300423, value0);
	}

	/**
	 * %0 has been destroyed.
	 */
	public static SM_SYSTEM_MESSAGE STR_BREAK_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300424, value0);
	}

	/**
	 * There are no dropped items nearby.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_NO_DROP_ITEM = new SM_SYSTEM_MESSAGE(1300425);
	/**
	 * You cannot use that item here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_LOCATION = new SM_SYSTEM_MESSAGE(1300426);

	/**
	 * You have cancelled using the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_CANCELED = new SM_SYSTEM_MESSAGE(1300427);

	/**
	 * The other player is carrying too many items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_PARTNER_HAS_TOON_MANY_ITEMS_INVENTORY = new SM_SYSTEM_MESSAGE(1300428);
	/**
	 * There are too many items in the target's trade window. The total number of items will exceed the size of your inventory after trading.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_EXCHANGE_RESULT_WILL_BE_OVER_YOUR_INVENTORY_SIZE = new SM_SYSTEM_MESSAGE(1300429);
	/**
	 * Your cube cannot be further expanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTEND_INVENTORY_CANT_EXTEND_MORE = new SM_SYSTEM_MESSAGE(1300430);

	/**
	 * %0 spaces have been added to your cube.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_INVENTORY_SIZE_EXTENDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300431, value0);
	}

	/**
	 * Your private warehouse cannot be further expanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTEND_CHAR_WAREHOUSE_CANT_EXTEND_MORE = new SM_SYSTEM_MESSAGE(1300432);

	/**
	 * %0 spaces have been added to your personal warehouse.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_CHAR_WAREHOUSE_SIZE_EXTENDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300433, value0);
	}

	/**
	 * Your account warehouse cannot be further expanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTEND_ACCOUNT_WAREHOUSE_CANT_EXTEND_MORE = new SM_SYSTEM_MESSAGE(1300434);

	/**
	 * %0 spaces have been added to your account warehouse.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_ACCOUNT_WAREHOUSE_SIZE_EXTENDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300435, value0);
	}

	/**
	 * %0 can only upgrade cubes of level %1 or higher.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_INVENTORY_CANT_EXTEND_DUE_TO_MINIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300436, value0, value1);
	}

	/**
	 * %0 can only upgrade cubes to level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_INVENTORY_CANT_EXTEND_MORE_DUE_TO_MAXIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300437, value0, value1);
	}

	/**
	 * %0 can only upgrade private warehouses of level %1 or higher.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_CHAR_WAREHOUSE_CANT_EXTEND_DUE_TO_MINIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300438, value0, value1);
	}

	/**
	 * %0 can only upgrade warehouses to level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_CHAR_WAREHOUSE_CANT_EXTEND_MORE_DUE_TO_MAXIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300439, value0, value1);
	}

	/**
	 * %0 can only upgrade warehouses of level %1 or higher.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_ACCOUNT_WAREHOUSE_CANT_EXTEND_DUE_TO_MINIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300440, value0, value1);
	}

	/**
	 * %0 can only upgrade warehouses to level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTEND_ACCOUNT_WAREHOUSE_CANT_EXTEND_MORE_DUE_TO_MAXIMUM_EXTEND_LEVEL_BY_THIS_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300441, value0, value1);
	}

	/**
	 * Your cube cannot be upgraded any further through quests.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTEND_INVENTORY_CANT_EXTEND_MORE_BY_QUEST = new SM_SYSTEM_MESSAGE(1300442);
	/**
	 * You are too far away to view the inventory.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VIEW_OTHER_INVENTORY_TOO_FAR_FROM_TARGET = new SM_SYSTEM_MESSAGE(1300443);
	/**
	 * The Stigma Stone cannot be removed: All items currently equipped via the skills acquired through this Stigma Stone must be removed first.
	 */
	public static final SM_SYSTEM_MESSAGE STR_STIGMA_CANNT_UNEQUIP_STONE_FIRST_UNEQUIP_CURRENT_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300444);
	/**
	 * Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300445);

	/**
	 * %0 is not an extractable item.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_IT_CAN_NOT_BE_DECOMPOSED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300446, value0);
	}

	/**
	 * You must have at least one empty space in your cube before you can extract an item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1300447);

	/**
	 * You have failed to extract from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_FAILED(int value0) {
		return new SM_SYSTEM_MESSAGE(1300448, new DescriptionId(value0));
	}

	/**
	 * You have successfully extracted from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_SUCCEED(int value0) {
		return new SM_SYSTEM_MESSAGE(1300449, new DescriptionId(value0));
	}

	/**
	 * You have cancelled the extraction from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_CANCELED(int nameId) {
		return new SM_SYSTEM_MESSAGE(1300450, new DescriptionId(nameId));
	}

	/**
	 * You cannot extract item in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOSE_ITEM_INVALID_STANCE(int value0) {
		return new SM_SYSTEM_MESSAGE(1300451, new DescriptionId(value0));
	}

	/**
	 * Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300452);

	/**
	 * %0 cannot be enchanted.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300453, value0);
	}

	/**
	 * %0 cannot be enchanted any more.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(int nameId) {
		return new SM_SYSTEM_MESSAGE(1300454, new DescriptionId(nameId));
	}

	/**
	 * You have successfully enchanted %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_SUCCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300455, descriptionId);
	}

	/**
	 * You have failed to enchant %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_FAILED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300456, descriptionId);
	}

	/**
	 * You successfully enchanted %0 by +%num1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ENCHANT_ITEM_SUCCEED_NEW(DescriptionId descriptionId, int value0) {
		return new SM_SYSTEM_MESSAGE(1401681, descriptionId, value0);
	}

	/**
	 * You have cancelled the enchanting of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_CANCELED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300457, descriptionId);
	}

	/**
	 * You cannot enchant items in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENCHANT_ITEM_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300458, value0);
	}

	/**
	 * Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300459);

	/**
	 * %0 cannot be socketed with manastones.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_IT_CAN_NOT_BE_GIVEN_OPTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300460, value0);
	}

	/**
	 * %0 cannot be socketed with Manastone.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_IT_CAN_NOT_BE_GIVEN_OPTION_MORE_TIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300461, value0);
	}

	/**
	 * You have succeeded in the manastone socketing of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_SUCCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300462, descriptionId);
	}

	/**
	 * You have failed in the manastone socketing of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_FAILED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300463, descriptionId);
	}

	/**
	 * You have cancelled the manastone socketing of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_CANCELED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300464, value0);
	}

	/**
	 * All manastones that were socketed in %0 have disappeared.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_ALL_OPTION_REMOVED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300465, value0);
	}

	/**
	 * You cannot socket manastones while %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_OPTION_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300466, value0);
	}

	/**
	 * You are too far from the NPC to remove the manastone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300467);
	/**
	 * Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300468);

	/**
	 * Manastone socketing / removal is not possible for the item %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_IT_CAN_NOT_BE_GIVEN_OPTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300469, value0);
	}

	/**
	 * %0 is not socketed with a manastone.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_NO_OPTION_TO_REMOVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300470, value0);
	}

	/**
	 * The target slot on %0 is not socketed with a manastone.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_INVALID_OPTION_SLOT_NUMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300471, value0);
	}

	/**
	 * You do not have enough Kinah to remove the manastone from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_NOT_ENOUGH_GOLD(long price) {
		return new SM_SYSTEM_MESSAGE(1300472, price);
	}

	/**
	 * You have removed the manastone from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_SUCCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300473, descriptionId);
	}

	/**
	 * You cannot remove manastones from items in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMOVE_ITEM_OPTION_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300474, value0);
	}

	/**
	 * You are too far from the NPC to modify the appearance of the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300475);
	/**
	 * You must be at least level 20 before you can modify the appearance of items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_PC_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1300476);
	/**
	 * Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300477);

	/**
	 * The appearance of %0 cannot be modified.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NOT_SKIN_CHANGABLE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300478, value0);
	}

	/**
	 * The appearance of %0 cannot be modified into %1 and vice versa as they are different types of item.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NOT_SAME_EQUIP_SLOT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300479, value0, value1);
	}

	/**
	 * The appearance of %0 cannot be modified into %1 and vice versa as they are different type of items.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NOT_COMPATIBLE(DescriptionId descriptionId, DescriptionId descriptionId2) {
		return new SM_SYSTEM_MESSAGE(1300480, descriptionId, descriptionId2);
	}

	/**
	 * You do not have enough Kinah to modify the appearance of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NOT_ENOUGH_GOLD(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300481, descriptionId);
	}

	/**
	 * You have failed to modify the appearance of the item as you could not remove the skin item %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_CAN_NOT_REMOVE_SKIN_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300482, value0);
	}

	/**
	 * You have modified the appearance of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_SUCCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300483, descriptionId);
	}

	/**
	 * You cannot modify the appearance of items in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300484, value0);
	}

	/**
	 * You have successfully soul-bound %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SOUL_BOUND_ITEM_SUCCEED(int value0) {
		return new SM_SYSTEM_MESSAGE(1300485, new DescriptionId(value0));
	}

	/**
	 * You have failed to soul-bind %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SOUL_BOUND_ITEM_FAILED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300486, value0);
	}

	/**
	 * You cancelled the soul-binding of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SOUL_BOUND_ITEM_CANCELED(int value0) {
		return new SM_SYSTEM_MESSAGE(1300487, new DescriptionId(value0));
	}

	/**
	 * Please try the soul-binding again after you have closed other input boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SOUL_BOUND_CLOSE_OTHER_MSG_BOX_AND_RETRY = new SM_SYSTEM_MESSAGE(1300488);

	/**
	 * You cannot soul-bind an item while %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SOUL_BOUND_INVALID_STANCE(int value0) {
		return new SM_SYSTEM_MESSAGE(1300489, new DescriptionId(value0));
	}

	/**
	 * You do not have a Power Shard equipped.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WEAPON_BOOST_NO_BOOSTER_EQUIPED = new SM_SYSTEM_MESSAGE(1300490);
	/**
	 * You activate the Power Shard.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WEAPON_BOOST_BOOST_MODE_STARTED = new SM_SYSTEM_MESSAGE(1300491);
	/**
	 * You deactivate the Power Shard.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WEAPON_BOOST_BOOST_MODE_ENDED = new SM_SYSTEM_MESSAGE(1300492);
	/**
	 * You cannot use the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_IS_NOT_USABLE = new SM_SYSTEM_MESSAGE(1300493);
	/**
	 * You cannot use the item as its cooldown time has not expired yet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_CANT_USE_UNTIL_DELAY_TIME = new SM_SYSTEM_MESSAGE(1300494);
	/**
	 * You must be next to a postbox and click it to use the post service.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_CLICK_POSTBOX_TO_USE = new SM_SYSTEM_MESSAGE(1300495);
	/**
	 * You cannot mail equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_SEND_CAN_NOT_SEND_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300496);
	/**
	 * You cannot mail items that are not tradable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_SEND_ITEM_CAN_NOT_BE_EXCHANGED = new SM_SYSTEM_MESSAGE(1300497);
	/**
	 * You cannot mail items that you have already used.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_SEND_USED_ITEM = new SM_SYSTEM_MESSAGE(1300498);
	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_SEND_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300499);
	/**
	 * The selected NPC cannot add ability to the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GIVE_ITEM_PROC_CANT_GIVE_PROC_BY_THIS_NPC = new SM_SYSTEM_MESSAGE(1300500);
	/**
	 * You are too far from the NPC to add abilities to the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GIVE_ITEM_PROC_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300501);
	/**
	 * Failed to find the target item to add the ability to.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1300502);
	/**
	 * You cannot add an ability to equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_CANNOT_GIVE_PROC_TO_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300503);

	/**
	 * %0 is not an item you can add ability to.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NOT_PROC_GIVABLE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300504, value0);
	}

	/**
	 * Cannot find the item to add ability to.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NO_PROC_GIVE_ITEM = new SM_SYSTEM_MESSAGE(1300505);

	/**
	 * %0 is not an item you can add ability to.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NOT_PROC_GIVE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300506, value0);
	}

	/**
	 * You do not have enough Kinah to add ability to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NOT_ENOUGH_MONEY(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300507, descriptionId);
	}

	/**
	 * You have successfully added ability to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_ENCHANTED_TARGET_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300508, descriptionId);
	}

	/**
	 * You cannot socket godstones while %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300509, value0);
	}

	/**
	 * You have removed the dye from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ITEM_COLOR_REMOVE_SUCCEED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300510, value0);
	}

	/**
	 * You have dyed %0 %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ITEM_COLOR_CHANGE_SUCCEED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300511, value0, value1);
	}

	/**
	 * %0 cannot be dyed.
	 */
	public static SM_SYSTEM_MESSAGE STR_ITEM_COLOR_CHANGE_ERROR_CANNOTDYE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300512, value0);
	}

	/**
	 * The item has not been dyed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_COLOR_REMOVE_ERROR_CANNOTREMOVE = new SM_SYSTEM_MESSAGE(1300513);
	/**
	 * The item cannot be found.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_COLOR_ERROR = new SM_SYSTEM_MESSAGE(1300514);
	/**
	 * You cannot dye equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_COLOR_CANNOT_CHANGE_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300515);
	/**
	 * The target is busy and cannot trade at the moment.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXCHANGE_START_OHER_IS_BUSY = new SM_SYSTEM_MESSAGE(1300516);

	/**
	 * %0's Reputation has increased by %1 point(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_POINTUP(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300517, value0, value1);
	}

	/**
	 * %0's Reputation has fallen by %1 point(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_POINTDOWN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300518, value0, value1);
	}

	/**
	 * %0's Reputation has become Hostile.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_HOSTIL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300519, value0);
	}

	/**
	 * %0's Reputation has become Confrontational.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_OPPOSITE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300520, value0);
	}

	/**
	 * %0's Reputation has become Neutral.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_NEUTRAL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300521, value0);
	}

	/**
	 * %0's Reputation has become Friendly.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_FREINDSHIP(String value0) {
		return new SM_SYSTEM_MESSAGE(1300522, value0);
	}

	/**
	 * %0's Reputation has become Alliance mode.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_ALLY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300523, value0);
	}

	/**
	 * Congratulations! You have joined %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_JOIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300524, value0);
	}

	/**
	 * You are already a member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_CAN_NOT_JOIN = new SM_SYSTEM_MESSAGE(1300525);

	/**
	 * You have left %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_LEAVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300526, value0);
	}

	/**
	 * The Jeridises
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_ZERIDITH = new SM_SYSTEM_MESSAGE(1300527);
	/**
	 * The Brugons
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_BRUGON = new SM_SYSTEM_MESSAGE(1300528);
	/**
	 * The Timoris
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_TIMORITH = new SM_SYSTEM_MESSAGE(1300529);
	/**
	 * A connection cannot be established with the Petition Server. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1300537);
	/**
	 * The petition is too short. A Support Petition must be at least 5 words in length.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_SHORT_PETITION_MSG = new SM_SYSTEM_MESSAGE(1300538);

	/**
	 * The Support Petition has been received. The receipt number is %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_OK(String value0) {
		return new SM_SYSTEM_MESSAGE(1300539, value0);
	}

	/**
	 * This is your %num0th petition. You may make %num1 more Support Petitions today.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_QUOTA_LEFT(int num0th, int num1) {
		return new SM_SYSTEM_MESSAGE(1300540, num0th, num1);
	}

	/**
	 * There are %num0 users waiting in the queue to lodge Support Petitions.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_WAITING_COUNT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300541, num0);
	}

	/**
	 * %0 has received a request from the GM for consultation.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_OK_TO_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300542, value0);
	}

	/**
	 * %0 has received a proxy petition generated by the GM. The petition number is %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_OK_TO_GM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300543, value0, value1);
	}

	/**
	 * A proxy petition by the GM has been received, but the user is offline. The petition number is %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_OK_TO_GM_USER_OFFLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300544, value0);
	}

	/**
	 * The Support service cannot be used in the hours between %0 and %1 o' clock. Please use the Return skill if you are locked in an impassable area.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_FAILED_NOT_SERVICE_TIME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300545, value0, value1);
	}

	/**
	 * Your Support request has failed. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_FAILED = new SM_SYSTEM_MESSAGE(1300546);

	/**
	 * You have used up your daily quota of %num0 Support Petitions. You cannot make any more inquiries with this account today.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_FAILED_OUT_OF_QUOTA(int num0) {
		return new SM_SYSTEM_MESSAGE(1300547, num0);
	}

	/**
	 * A Support Petition has already been received. Please wait for a reply.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SUBMIT_PETITION_FAILED_ALREADY_SUBMITTED = new SM_SYSTEM_MESSAGE(1300548);

	/**
	 * Your proxy petition request has failed. %0 has already received the Support Petition.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_FAILED_ALREADY_SUBMITTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300549, value0);
	}

	/**
	 * Your proxy petition request for %0 has failed. The error code is %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_FAILED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300550, value0, value1);
	}

	/**
	 * The request for a proxy petition has failed. (The user is currently offline.) The error code is %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SUBMIT_GM_PETITION_FAILED_OFFLINE_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300551, value0);
	}

	/**
	 * Petition No. %0 has been cancelled.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_OK1(String value0) {
		return new SM_SYSTEM_MESSAGE(1300552, value0);
	}

	/**
	 * The petition has been cancelled. You have %num1 Support Petitions left for today.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_OK2(int num1) {
		return new SM_SYSTEM_MESSAGE(1300553, num1);
	}

	/**
	 * You cancelled the proxy petition request for %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_OK_TO_GM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300554, value0);
	}

	/**
	 * Failed to cancel the petition. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_FAIL = new SM_SYSTEM_MESSAGE(1300555);
	/**
	 * The Support Petition is already being processed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_FAIL_FORBIDDEN = new SM_SYSTEM_MESSAGE(1300556);
	/**
	 * Support Petitions cannot be submitted at the moment.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_FAIL_NOT_SERVICE_TIME = new SM_SYSTEM_MESSAGE(1300557);

	/**
	 * Failed to cancel the request for a proxy petition to %0. The error code is %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANCEL_PETITION_FAIL_TO_GM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300558, value0, value1);
	}

	/**
	 * The User (%0) is not in the game server.
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_USER_NOT_FOUND(String value0) {
		return new SM_SYSTEM_MESSAGE(1300559, value0);
	}

	/**
	 * Your consultation with the GM (%0) has started.
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_CHAT_STARTED_TO_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300560, value0);
	}

	/**
	 * The GM (%0)'s response is complete. Please evaluate the Support Petition service in a moment.
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_CHAT_ENDED_TO_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300561, value0);
	}

	/**
	 * You are not in a consultation with the GM.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NOT_IN_PETITION_CHAT = new SM_SYSTEM_MESSAGE(1300562);
	/**
	 * An error has occurred while transmitting the conversation log to the GM. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_CHAT_ERROR = new SM_SYSTEM_MESSAGE(1300563);

	/**
	 * %0 : %1
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_GM_CHAT_MSG(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300564, value0, value1);
	}

	/**
	 * This is a message from the GM: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_GM_LEAVE_MSG(String value0) {
		return new SM_SYSTEM_MESSAGE(1300565, value0);
	}

	/**
	 * Only the group leader can receive this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_PARTY_LEADER_ONLY = new SM_SYSTEM_MESSAGE(1300566);

	/**
	 * You can only receive this quest when your group has %0 or more members.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_PARTY_SIZE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300567, value0);
	}

	/**
	 * Only the Legion Brigade General can receive the quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_GUILD_MASTER_ONLY = new SM_SYSTEM_MESSAGE(1300568);

	/**
	 * You can only receive this quest when the level of your Legion is %0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_GUILD_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300569, value0);
	}

	/**
	 * You can only receive this quest when the Legion Point of your Legion is %num0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_GUILD_EXP(int num0) {
		return new SM_SYSTEM_MESSAGE(1300570, num0);
	}

	/**
	 * You can only receive this quest when your level is %0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MIN_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300571, value0);
	}

	/**
	 * You can only receive this quest when your level is %0 or below.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MAX_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300572, value0);
	}

	/**
	 * You can only receive this quest when your rank is %0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MIN_RANK(int value0) {
		return new SM_SYSTEM_MESSAGE(1300573, new DescriptionId(value0));
	}

	/**
	 * You can only receive this quest when your production job rank is %0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_TS_RANK(String value0) {
		return new SM_SYSTEM_MESSAGE(1300574, value0);
	}

	/**
	 * Your race cannot receive this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_RACE = new SM_SYSTEM_MESSAGE(1300575);
	/**
	 * Your nationality prevents you from receiving this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_NATION = new SM_SYSTEM_MESSAGE(1300576);
	/**
	 * Only males can receive this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MAN_ONLY = new SM_SYSTEM_MESSAGE(1300577);
	/**
	 * Only females can receive this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_WOMAN_ONLY = new SM_SYSTEM_MESSAGE(1300578);
	/**
	 * Your gender prevents you from receiving this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_GENDER = new SM_SYSTEM_MESSAGE(1300579);
	/**
	 * Your class prevents you from receiving this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_CLASS = new SM_SYSTEM_MESSAGE(1300580);

	/**
	 * You must have the %0 voice to receive this quest.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_VOICE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300581, value0);
	}

	/**
	 * You can only receive this quest when you need %num0 or less XP to reach the next level.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_EXP_TO_NEXT_LEVEL(int num0) {
		return new SM_SYSTEM_MESSAGE(1300582, num0);
	}

	/**
	 * You can only receive this quest when your Stigma Point is %num0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_STIGMA_PT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300583, num0);
	}

	/**
	 * You can only receive this quest when your PVP point is %num0 or more.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_PVP_PT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300584, num0);
	}

	/**
	 * You can only receive this quest when %0's favor toward you is %num1 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_FAVOR(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1300585, value0, num1);
	}

	/**
	 * You can only receive this quest when your Faction with %0 is %num1 or higher.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_FACTION(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1300586, value0, num1);
	}

	/**
	 * You can only receive this quest when your National Contribute Point is %num0 or more.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_NATION_PT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300587, num0);
	}

	/**
	 * You can only receive this quest when you have the %0 title.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_TITLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300588, value0);
	}

	/**
	 * You can only receive this quest when you have the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300589, value0);
	}

	/**
	 * You can only accept this quest when you have level %1 %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_SKILL_LEVEL(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300590, value1, value0);
	}

	/**
	 * You are not in the altered state required to receive this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_ABNORMAL_STATUS = new SM_SYSTEM_MESSAGE(1300591);

	/**
	 * You must have played for a total of at least %num0 hours to receive this quest.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_PLAY_TIME(int num0) {
		return new SM_SYSTEM_MESSAGE(1300592, num0);
	}

	/**
	 * You can only receive this quest when you are equipped with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_EQUIP_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300593, descriptionId);
	}

	/**
	 * You can only receive this quest when you have %0 in your inventory.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_INVENTORY_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300594, descriptionId);
	}

	/**
	 * You must be a member of the %0 NPC Legion to receive this quest.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_NPC_GUILD(String value0) {
		return new SM_SYSTEM_MESSAGE(1300595, value0);
	}

	/**
	 * You can only receive this quest when you have completed the %0 quest.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_FINISHED_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300596, value0);
	}

	/**
	 * You cannot receive a quest that you are already working on.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_WORKING_QUEST = new SM_SYSTEM_MESSAGE(1300597);
	/**
	 * You cannot receive quests while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_DIE = new SM_SYSTEM_MESSAGE(1300598);

	/**
	 * You can do the %0 quest only once.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_NONE_REPEATABLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300599, value0);
	}

	/**
	 * You can do the %0 quest only %1 times.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MAX_REPEAT_COUNT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300600, value0, value1);
	}

	/**
	 * You cannot get a quest reward while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_GET_REWARD_ERROR_DEAD = new SM_SYSTEM_MESSAGE(1300601);

	/**
	 * You cannot get the quest reward as you don't have %0 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_GET_REWARD_ERROR_NO_QUEST_ITEM_SINGLE(String value0, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300602, value0, value0s);
	}

	/**
	 * You cannot receive the quest reward as you do not have %1 %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_GET_REWARD_ERROR_NO_QUEST_ITEM_MULTIPLE(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300603, value1, value0);
	}

	/**
	 * You cannot destroy %0 because it is used in the "%1" quest which cannot be abandoned once started.
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM_IMPOSSIBLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300604, value0);
	}

	/**
	 * Please try destroying the quest item again after you have closed other dialog boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM_RETRY = new SM_SYSTEM_MESSAGE(1300605);
	/**
	 * No Quest selected
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_NO_QUEST = new SM_SYSTEM_MESSAGE(1300606);
	/**
	 * Quest Indicator
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUIEST_INDICATOR = new SM_SYSTEM_MESSAGE(1300607);
	/**
	 * Quest
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_QUEST = new SM_SYSTEM_MESSAGE(1300608);

	/**
	 * %1[acquire]%2 %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_QIMSG_ACQUIRE() {
		return new SM_SYSTEM_MESSAGE(1300609);
	}

	/**
	 * %1[fail]%2 %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_QIMSG_GIVEUP() {
		return new SM_SYSTEM_MESSAGE(1300610);
	}

	/**
	 * %1[update]%2 %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_QIMSG_UPDATE() {
		return new SM_SYSTEM_MESSAGE(1300611);
	}

	/**
	 * %1[complete]%2 %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_QIMSG_COMPLETE() {
		return new SM_SYSTEM_MESSAGE(1300612);
	}

	/**
	 * Quest acquired: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_ACQUIRE_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300613, value0);
	}

	/**
	 * Quest failed: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_GIVEUP_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300614, value0);
	}

	/**
	 * Quest updated: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_UPDATE_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300615, value0);
	}

	/**
	 * Quest complete: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_COMPLETE_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300616, value0);
	}

	/**
	 * Quest acquired: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_ACQUIRE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300617, value0);
	}

	/**
	 * Quest failed: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_GIVEUP(String value0) {
		return new SM_SYSTEM_MESSAGE(1300618, value0);
	}

	/**
	 * Quest updated: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_UPDATE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300619, value0);
	}

	/**
	 * Quest complete: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_QUEST_SYSTEMMSG_COMPLETE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300620, value0);
	}

	/**
	 * You cannot learn this design.
	 */
	public static final SM_SYSTEM_MESSAGE STR_RECIPEITEM_CANT_USE_NO_RECIPE = new SM_SYSTEM_MESSAGE(1300621);
	/**
	 * You cannot receive any more quests.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ACQUIRE_ERROR_MAX_NORMAL = new SM_SYSTEM_MESSAGE(1300622);

	/**
	 * %0 has been banned.
	 */
	public static SM_SYSTEM_MESSAGE STR_USER_BANNED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300623, value0);
	}

	/**
	 * %0 has been disconnected from the server.
	 */
	public static SM_SYSTEM_MESSAGE STR_USER_KICKED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300624, value0);
	}

	/**
	 * There is no user named %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_NO_USER_NAMED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300625, value0);
	}

	/**
	 * There is going to be an important announcement from the GM. Please be patient for a while.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_CHAT_DURING_NOTIFICATION = new SM_SYSTEM_MESSAGE(1300626);

	/**
	 * %0 is not playing the game.
	 */
	public static SM_SYSTEM_MESSAGE STR_NO_SUCH_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300627, value0);
	}

	/**
	 * %0 has blocked you.
	 */
	public static SM_SYSTEM_MESSAGE STR_YOU_EXCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300628, value0);
	}

	/**
	 * %0 is currently not accepting any Whispers.
	 */
	public static SM_SYSTEM_MESSAGE STR_WHISPER_REFUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300629, value0);
	}

	/**
	 * Nothing happened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NOTHING_HAPPEN = new SM_SYSTEM_MESSAGE(1300630);
	/**
	 * You cannot use teleport here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NO_TELEPORT = new SM_SYSTEM_MESSAGE(1300631);

	/**
	 * You have unblocked %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ONE_INCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300632, value0);
	}

	/**
	 * You have blocked %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ONE_EXCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300633, value0);
	}

	/**
	 * Blocked users: %num0 users
	 */
	public static SM_SYSTEM_MESSAGE STR_CURRENT_EXCLUDES(int num0) {
		return new SM_SYSTEM_MESSAGE(1300634, num0);
	}

	/**
	 * There are no blocked users.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NO_EXCLUDES = new SM_SYSTEM_MESSAGE(1300635);

	/**
	 * You have excluded %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ONE_NO_LONGER_INCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300636, value0);
	}

	/**
	 * You have listed %0 as a friend.
	 */
	public static SM_SYSTEM_MESSAGE STR_ONE_IS_INCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300637, value0);
	}

	/**
	 * You have too many users listed as friends.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_MANY_INCLUDE = new SM_SYSTEM_MESSAGE(1300638);

	/**
	 * Users listed as friends: %num0 users
	 */
	public static SM_SYSTEM_MESSAGE STR_CURRENT_INCLUDES(int num0) {
		return new SM_SYSTEM_MESSAGE(1300639, num0);
	}

	/**
	 * You have no chat friends.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NO_INCLUDES = new SM_SYSTEM_MESSAGE(1300640);

	/**
	 * Current users: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_LIST_USER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300641, value0);
	}

	/**
	 * The server is due to shut down in %0 seconds. Please quit the game.
	 */
	public static SM_SYSTEM_MESSAGE STR_SERVER_SHUTDOWN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300642, value0);
	}

	/**
	 * Please do not flood chat. Blocked for %0m.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_DISABLED_FOR(String value0m) {
		return new SM_SYSTEM_MESSAGE(1300643, value0m);
	}

	/**
	 * You may now chat again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CAN_CHAT_NOW = new SM_SYSTEM_MESSAGE(1300644);
	/**
	 * Please do not flood chat. Blocked for a short while.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLOBAL_CHAT_DISABLED_FOR = new SM_SYSTEM_MESSAGE(1300645);
	/**
	 * You cannot create a general channel at your discretion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_MAKE_GENERALCHANNEL = new SM_SYSTEM_MESSAGE(1300646);

	/**
	 * %0 has already been sold.
	 */
	public static SM_SYSTEM_MESSAGE STR_VENDOR_SOLD_OUT(int nameId) {
		return new SM_SYSTEM_MESSAGE(1300647, new DescriptionId(nameId));
	}

	/**
	 * You do not have enough Kinah to pay the fee.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300648);
	/**
	 * You cannot register any more items as there is no space available.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_FULL_ITEM = new SM_SYSTEM_MESSAGE(1300649);
	/**
	 * You cannot register items that have already been used.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_REGISTER_USED_ITEM = new SM_SYSTEM_MESSAGE(1300650);
	/**
	 * You cannot register equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_REGISTER_EQUIPPED_ITEM = new SM_SYSTEM_MESSAGE(1300651);
	/**
	 * You cannot list an untradeable item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_REGISTER_CANNOT_BE_EXCHANGED = new SM_SYSTEM_MESSAGE(1300652);
	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_REGISTER_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300653);
	/**
	 * You cannot continue trading as your inventory is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_FULL_INVENTORY = new SM_SYSTEM_MESSAGE(1300654);
	/**
	 * You cannot register this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_CAN_NOT_REGISTER_ITEM = new SM_SYSTEM_MESSAGE(1300655);
	/**
	 * This item is already registered.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_ALREADY_REGISTERED = new SM_SYSTEM_MESSAGE(1300656);
	/**
	 * Items for Sale! The best value around!
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DEFAULT_ADVERTISE_MSG = new SM_SYSTEM_MESSAGE(1300657);
	/**
	 * You start doing business at your private store.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_START = new SM_SYSTEM_MESSAGE(1300658);
	/**
	 * You stop doing business at your private store.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_END = new SM_SYSTEM_MESSAGE(1300659);
	/**
	 * You cannot sell equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CAN_NOT_SELL_EQUIPED_ITEM = new SM_SYSTEM_MESSAGE(1300660);
	/**
	 * You cannot sell items that cannot be traded with other users.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CANNOT_BE_EXCHANGED = new SM_SYSTEM_MESSAGE(1300661);
	/**
	 * You cannot sell used items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CAN_NOT_SELL_USED_ITEM = new SM_SYSTEM_MESSAGE(1300662);
	/**
	 * You cannot open a private store while fighting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DISABLED_IN_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1300663);
	/**
	 * As you cannot open a private store while fighting, it will be closed automatically.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CLOSED_FOR_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1300664);

	/**
	 * %0 has already been sold.
	 */
	public static SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_SOLD_OUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300665, value0);
	}

	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300666);
	/**
	 * You have not opened Quickbar No.2.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUICKBAR_NOT_OPEN_SECONDBAR = new SM_SYSTEM_MESSAGE(1300667);
	/**
	 * You have not opened Quickbar No.3.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUICKBAR_NOT_OPEN_THIRDBAR = new SM_SYSTEM_MESSAGE(1300668);
	/**
	 * As there is no registered bind point, you will resurrect in the city.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DEATH_NOT_REGISTERED_RESURRECT_POINT = new SM_SYSTEM_MESSAGE(1300669);

	/**
	 * You are now bound at [%subzone].
	 */
	public static SM_SYSTEM_MESSAGE STR_DEATH_REGISTER_RESURRECT_POINT(String subzone) {
		return new SM_SYSTEM_MESSAGE(1300670, subzone);
	}

	/**
	 * Please try again after you have closed other dialog boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_ASK_RECOVER_EXPERIENCE_BY_OTHER_QUESTION = new SM_SYSTEM_MESSAGE(1300671);
	/**
	 * You do not have enough Kinah to recover your XP.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_RECOVER_EXPERIENCE_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300672);
	/**
	 * You are too far from a healer to receive Soul Healing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_RECOVER_EXPERIENCE_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300673);
	/**
	 * You received Soul Healing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SUCCESS_RECOVER_EXPERIENCE = new SM_SYSTEM_MESSAGE(1300674);
	/**
	 * You are too far to change the PVP zone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PVPZONE_CANNOT_MOVE_PVPZONE_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300675);
	/**
	 * You cannot change the PVP zone because you have no means to move.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PVPZONE_CANNOT_MOVE_PVPZONE_NPC_NOT_CORRECT = new SM_SYSTEM_MESSAGE(1300676);
	/**
	 * The target is invalid. Please select a player.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ASSISTKEY_INCORRECT_TARGET = new SM_SYSTEM_MESSAGE(1300677);
	/**
	 * The person you want to assist does not have a target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ASSISTKEY_NO_USER = new SM_SYSTEM_MESSAGE(1300678);
	/**
	 * The person you want to assist is too far from the target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ASSISTKEY_TOO_FAR = new SM_SYSTEM_MESSAGE(1300679);

	/**
	 * You are assisting the target %0 has selected.
	 */
	public static SM_SYSTEM_MESSAGE STR_ASSISTKEY_ASSIST_FOR_SOMEONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300680, value0);
	}

	/**
	 * The map is now loading.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WORLDMAP_INFO = new SM_SYSTEM_MESSAGE(1300681);
	/**
	 * You do not have any XP to recover.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DONOT_HAVE_RECOVER_EXPERIENCE = new SM_SYSTEM_MESSAGE(1300682);
	/**
	 * You can respond to the survey only in this server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_POLL_ANSWER_IS_NOT_ORG_SERVER = new SM_SYSTEM_MESSAGE(1300683);
	/**
	 * There is no survey underway.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_FIND_POLL = new SM_SYSTEM_MESSAGE(1300684);
	/**
	 * You have already responded to this survey.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ALREADY_ANSWER_THIS_POLL = new SM_SYSTEM_MESSAGE(1300685);
	/**
	 * You do not have enough Kinah to register this location as a bind point.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_RESURRECT_POINT_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300686);
	/**
	 * You cannot bind from here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_RESURRECT_POINT_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300687);
	/**
	 * You have already bound at this location.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ALREADY_REGISTER_THIS_RESURRECT_POINT = new SM_SYSTEM_MESSAGE(1300688);
	/**
	 * You do not have enough Kinah for teleport.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_MOVE_TO_AIRPORT_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300689);
	/**
	 * You cannot use it as the required quest has not been completed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_MOVE_TO_AIRPORT_NEED_FINISH_QUEST = new SM_SYSTEM_MESSAGE(1300690);
	/**
	 * You cannot move to that destination.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE = new SM_SYSTEM_MESSAGE(1300691);
	/**
	 * The NPC you selected does not have the ability to teleport you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_MOVE_TO_AIRPORT_WRONG_NPC = new SM_SYSTEM_MESSAGE(1300692);
	/**
	 * You are too far from the NPC to teleport.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_MOVE_TO_AIRPORT_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300693);
	/**
	 * You can bind here by clicking the Obelisk.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NOTIFY_RESURRECT_POINT = new SM_SYSTEM_MESSAGE(1300694);
	/**
	 * You are already experiencing the resurrection effect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_OTHER_USER_USE_RESURRECT_SKILL_ALREADY = new SM_SYSTEM_MESSAGE(1300695);
	/**
	 * You cannot teleport to a bind point while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_AIRPORT_WHEN_FLYING = new SM_SYSTEM_MESSAGE(1300696);
	/**
	 * The server is being shut down for an update.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SHUTDOWN_REASON_UPDATE = new SM_SYSTEM_MESSAGE(1300697);

	/**
	 * Leaving Atreia.\n\n Please wait %0 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_WAIT_TO_QUIT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300698, value0);
	}

	/**
	 * The account usage time has expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_TIME_EXPIRED = new SM_SYSTEM_MESSAGE(1300699);
	/**
	 * Another user has tried to log in.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_ANOTHER_USER_TRY_LOGIN = new SM_SYSTEM_MESSAGE(1300700);
	/**
	 * You do not have enough Kinah to use the artifact.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ARTIFACT_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300701);
	/**
	 * The Artifact cannot be used at this time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ARTIFACT_OUT_OF_ORDER = new SM_SYSTEM_MESSAGE(1300702);
	/**
	 * You have no authority to use the Artifact.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ARTIFACT_HAVE_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1300703);
	/**
	 * You cannot use the Artifact from this place.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ARTIFACT_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300704);
	/**
	 * This is not a usable Artifact.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_ARTIFACT_IS_NOT_ARTIFACT = new SM_SYSTEM_MESSAGE(1300705);
	/**
	 * You have no authority to go through the door.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DOOR_HAVE_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1300706);
	/**
	 * You cannot use the door from here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DOOR_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300707);
	/**
	 * Your quest tracker is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_IND_EXCESS = new SM_SYSTEM_MESSAGE(1300708);
	/**
	 * Macro canceled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANCEL = new SM_SYSTEM_MESSAGE(1300709);
	/**
	 * Macro complete.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_END = new SM_SYSTEM_MESSAGE(1300710);
	/**
	 * Mail has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RECEIVE_MAIL = new SM_SYSTEM_MESSAGE(1300711);
	/**
	 * All items are already confirmed. You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EXCHANGE_ALREADY_READY = new SM_SYSTEM_MESSAGE(1300712);
	/**
	 * You cannot split items in the inventory during a trade.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVENTORY_SPLIT_DURING_TRADE = new SM_SYSTEM_MESSAGE(1300713);
	/**
	 * You cannot open the private store on a moving object.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DISABLED_IN_MOVING_OBJECT = new SM_SYSTEM_MESSAGE(1300714);
	/**
	 * This Rift is not usable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_NO_PORTAL = new SM_SYSTEM_MESSAGE(1300715);
	/**
	 * You cannot use a Rift here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300716);
	/**
	 * You cannot use a Rift at your level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1300717);
	/**
	 * The Rift has already had the maximum number of people travel through it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_USE_COUNT_LIMIT = new SM_SYSTEM_MESSAGE(1300718);

	/**
	 * The remaining playing time is %*0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMAIN_PLAYTIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300719, value0);
	}

	/**
	 * Pre-paid credit is being applied. The remaining playing time is %*0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_REMAIN_PLAYTIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300720, value0);
	}

	/**
	 * Please use the key near the door.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_KEY_ITEM = new SM_SYSTEM_MESSAGE(1300721);

	/**
	 * You need %0 to open the door.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_OPEN_DOOR_NEED_NAMED_KEY_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300722, value0);
	}

	/**
	 * You need a key to open the door.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_OPEN_DOOR_NEED_KEY_ITEM = new SM_SYSTEM_MESSAGE(1300723);
	/**
	 * Trade Broker
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_RETURN_MAIL_FROM = new SM_SYSTEM_MESSAGE(1300724);
	/**
	 * Your item has been returned as the sales period has ended.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_RETURN_MAIL_CONTENT = new SM_SYSTEM_MESSAGE(1300725);
	/**
	 * You have no authority to use it as you are not a member of the Conquering Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DOOR_REPAIR_HAVE_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1300726);
	/**
	 * You cannot use that as the cooldown time has not expired yet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DOOR_REPAIR_OUT_OF_COOLTIME = new SM_SYSTEM_MESSAGE(1300727);

	/**
	 * You do not have enough items needed for repair. The fee is %0 (per %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_DOOR_REPAIR_NOT_ENOUGH_FEE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300728, value0, value1);
	}

	/**
	 * %0 has used level %1 %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MONSTER_SKILL(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1300729, value0, value1, value2);
	}

	/**
	 * Moving to Area EE2.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TP_EE2_TP0_L_TOEE2 = new SM_SYSTEM_MESSAGE(1300730);
	/**
	 * Moving to Dungeon D3.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TP_EE2_TP0_L_TOD3_DUN = new SM_SYSTEM_MESSAGE(1300731);
	/**
	 * Moving to the Instanced Dungeon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TP_EE2_TP0_L_TOINSTANT_DUN = new SM_SYSTEM_MESSAGE(1300732);
	/**
	 * Moving to Eltnen.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TP_LF1A_TP0_L_TOLF2 = new SM_SYSTEM_MESSAGE(1300733);
	/**
	 * Moving to Verteron.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TP_LF2_TP0_L_TOLF1A = new SM_SYSTEM_MESSAGE(1300734);
	/**
	 * You cannot use any items while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLYING_DISABLE_1 = new SM_SYSTEM_MESSAGE(1300735);
	/**
	 * You cannot use the skill while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLYING_DISABLE_2 = new SM_SYSTEM_MESSAGE(1300736);
	/**
	 * You have died.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DEATH_MESSAGE_ME = new SM_SYSTEM_MESSAGE(1300737);
	/**
	 * You have resurrected.
	 */
	public static final SM_SYSTEM_MESSAGE STR_REBIRTH_MASSAGE_ME = new SM_SYSTEM_MESSAGE(1300738);

	/**
	 * %0 has defeated %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_KILLMSG(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300739, value0, value1);
	}

	/**
	 * Resurrection wait time: %0 sec
	 */
	public static SM_SYSTEM_MESSAGE STR_WATINGTIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300740, value0);
	}

	/**
	 * Use a skill to resurrect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_RESURRECT_DIALOG__SKILL = new SM_SYSTEM_MESSAGE(1300741);
	/**
	 * Use an item to resurrect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_RESURRECT_DIALOG__ITEM = new SM_SYSTEM_MESSAGE(1300742);
	/**
	 * Press "OK" to resurrect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_RESURRECT_DIALOG__BIND = new SM_SYSTEM_MESSAGE(1300743);

	/**
	 * It will be cancelled if you do not press it in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_RESURRECTOTHER_DIALOG__5MIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300744, value0);
	}

	/**
	 * It will be cancelled if you do not press it in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_RESURRECT_DIALOG__5MIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300745, value0);
	}

	/**
	 * You will be resurrected at the registered bind point if you do not press it in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_RESURRECT_DIALOG__30MIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300746, value0);
	}

	/**
	 * It is at a hard-to-find location.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIND_POS_UNKNOWN_NAME = new SM_SYSTEM_MESSAGE(1300747);

	/**
	 * %0 is at the position indicated on the map.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_SUBZONE_FOUND(String value0) {
		return new SM_SYSTEM_MESSAGE(1300748, value0);
	}

	/**
	 * %0 is where it is indicated on the map, but the path leading to it cannot be found.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_TOO_FAR_FROM_SUBZONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300749, value0);
	}

	/**
	 * %0 is at the position indicated on the map.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_NPC_FOUND(String value0) {
		return new SM_SYSTEM_MESSAGE(1300750, value0);
	}

	/**
	 * %0 is where it is indicated on the map, but the path leading to it cannot be found.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_TOO_FAR_FROM_NPC(String value0) {
		return new SM_SYSTEM_MESSAGE(1300751, value0);
	}

	/**
	 * %0 is at a hard-to-find location.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_NO_NPC_IN_THIS_WORLD(String value0) {
		return new SM_SYSTEM_MESSAGE(1300752, value0);
	}

	/**
	 * Searching for the location. Please wait (max. 30 seconds).
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIND_POS_FINDING_PLEASE_WAIT = new SM_SYSTEM_MESSAGE(1300753);

	/**
	 * %0 is in the %1 region.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_NPC_FOUND_IN_OTHER_WORLD(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300754, value0, value1);
	}

	/**
	 * You cannot quit the game during the battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_QUIT = new SM_SYSTEM_MESSAGE(1300755);
	/**
	 * A one-way Rift into Elysea has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIGHT_SIDE_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1300756);

	/**
	 * You spent %num0 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_USEMONEY(int num0) {
		return new SM_SYSTEM_MESSAGE(1300757, num0);
	}

	/**
	 * Trade Failed
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TITLE_TRADE_FAIL = new SM_SYSTEM_MESSAGE(1300758);
	/**
	 * You do not have enough Kinah.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300759);

	/**
	 * You can list up to %num0 items.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_MAX_ITEM_COUNT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300760, num0);
	}

	/**
	 * This item cannot be traded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_EXCHANGE = new SM_SYSTEM_MESSAGE(1300761);
	/**
	 * You cannot trade as your inventory is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FULL_INVENTORY = new SM_SYSTEM_MESSAGE(1300762);
	/**
	 * You cannot register any more items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FULL_BASKET = new SM_SYSTEM_MESSAGE(1300763);
	/**
	 * You already have this limited possession item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_OWNED_LORE_ITEM = new SM_SYSTEM_MESSAGE(1300764);
	/**
	 * Confirm Registration
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_MSG_TITLE_REGISTER_OK = new SM_SYSTEM_MESSAGE(1300765);
	/**
	 * Registration Failed
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_MSG_TITLE_REGISTER_ERROR = new SM_SYSTEM_MESSAGE(1300766);
	/**
	 * You do not have enough Kinah to pay the fee.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_MSG_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1300767);
	/**
	 * You entered the PvP zone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PVP_ZONE_ENTERED = new SM_SYSTEM_MESSAGE(1300768);
	/**
	 * You left the PvP zone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PVP_ZONE_EXITED = new SM_SYSTEM_MESSAGE(1300769);
	/**
	 * Start Duel!
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_START = new SM_SYSTEM_MESSAGE(1300770);
	/**
	 * Stop Duel
	 */
	public static final SM_SYSTEM_MESSAGE STR_DUEL_STOP = new SM_SYSTEM_MESSAGE(1300771);
	/**
	 * You cannot destroy equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_DESTROY_EQUIP_ITEM = new SM_SYSTEM_MESSAGE(1300772);
	/**
	 * Permanently Acquired Title
	 */
	public static final SM_SYSTEM_MESSAGE STR_TITLE_PERMANENT = new SM_SYSTEM_MESSAGE(1300773);
	/**
	 * Temporarily Acquired Title
	 */
	public static final SM_SYSTEM_MESSAGE STR_TITLE_TEMPORARY = new SM_SYSTEM_MESSAGE(1300774);

	/**
	 * %0 skill (Level %1)
	 */
	public static SM_SYSTEM_MESSAGE STR_TITLE_BONUS_SKILL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300775, value0, value1);
	}

	/**
	 * You are already destroying another item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ALREADY_DESTROY_ITEM = new SM_SYSTEM_MESSAGE(1300776);

	/**
	 * Starting the duel with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DUEL_START(String value0) {
		return new SM_SYSTEM_MESSAGE(1300777, value0);
	}

	/**
	 * The World Map is currently being prepared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WORLDMAP_UNDER_CONSTRUCTION = new SM_SYSTEM_MESSAGE(1300778);

	/**
	 * The current screenshot was saved in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_PRINT_SCREEN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300779, value0);
	}

	/**
	 * You are already chatting with someone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ALREADY_TALKING_TO_SOMEONE = new SM_SYSTEM_MESSAGE(1300780);

	/**
	 * %0 rolled the dice and got a %num1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_ROLLED(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1300781, value0, num1);
	}

	/**
	 * %0 gave up rolling the dice.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_PASSED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300782, value0);
	}

	/**
	 * %0 does not have the right to roll the dice.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_UNAUTHORIZED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300783, value0);
	}

	/**
	 * You have purchased %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_BUY_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300784, value0);
	}

	/**
	 * You have purchased %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_BUY_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300785, value1, value0s);
	}

	/**
	 * You have sold %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SELL_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300786, value0);
	}

	/**
	 * You have sold %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SELL_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300787, value1, value0s);
	}

	/**
	 * You have crafted %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBINE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300788, value0);
	}

	/**
	 * You have crafted %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBINE_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300789, value1, value0s);
	}

	/**
	 * %0 has been sold.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SOLDOUT_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300790, value0);
	}

	/**
	 * %1 %0s have been sold.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SOLDOUT_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300791, value1, value0s);
	}

	/**
	 * You have discarded %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DISCARD_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300792, value0);
	}

	/**
	 * You have discarded %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DISCARD_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1300793, value1, value0s);
	}

	/**
	 * Your builder level is too low to open the selected window.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_OPEN_DIALOG_BY_BUILDER_LEVEL = new SM_SYSTEM_MESSAGE(1300794);

	/**
	 * Currently, %0 cannot receive any friend requests.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDY_CANT_ADD_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1300795, value0);
	}

	/**
	 * Petitions are not accepted right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PETITION_STATUS_NO_IDLE = new SM_SYSTEM_MESSAGE(1300796);
	/**
	 * It is outside the petition submission hours.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PETITION_STATUS_NO_SUBMIT = new SM_SYSTEM_MESSAGE(1300797);
	/**
	 * You cannot open a private store while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DISABLED_IN_FLY_MODE = new SM_SYSTEM_MESSAGE(1300798);
	/**
	 * You cannot use this Kisk.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1300799);
	/**
	 * You cannot use the Kisk here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC = new SM_SYSTEM_MESSAGE(1300800);
	/**
	 * You cannot use the Kisk.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_BINDSTONE_NOT_BINDSTONE = new SM_SYSTEM_MESSAGE(1300801);
	/**
	 * The Kisk has been destroyed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_IS_DESTROYED = new SM_SYSTEM_MESSAGE(1300802);
	/**
	 * The Kisk has been dismantled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_IS_REMOVED = new SM_SYSTEM_MESSAGE(1300803);
	/**
	 * You cannot install the Kisk as it is too close to an Artifact.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_BINDSTONE_ITEM_NOT_PROPER_AREA = new SM_SYSTEM_MESSAGE(1300804);
	/**
	 * You can only use the Kisk when the PvP is On.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_BINDSTONE_ITEM_NOT_PROPER_TIME = new SM_SYSTEM_MESSAGE(1300805);
	/**
	 * You cannot use a Kisk while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_BINDSTONE_ITEM_WHILE_FLYING = new SM_SYSTEM_MESSAGE(1300806);

	/**
	 * You are not allowed to move for %0 minutes for the following reason(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_ENABLE_NO_MOVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300807, value0);
	}

	/**
	 * You are not allowed to chat for %0 minutes for the following reason(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_ENABLE_NO_CHAT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300808, value0);
	}

	/**
	 * You are not allowed to open the private store for %0 minutes for the following reason(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_ENABLE_NO_SHOP(String value0) {
		return new SM_SYSTEM_MESSAGE(1300809, value0);
	}

	/**
	 * You are now allowed to move.
	 */
	public static final SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_DISABLE_NO_MOVE = new SM_SYSTEM_MESSAGE(1300810);
	/**
	 * You are now allowed to chat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_DISABLE_NO_CHAT = new SM_SYSTEM_MESSAGE(1300811);
	/**
	 * You now allowed to open a private store.
	 */
	public static final SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_DISABLE_NO_SHOP = new SM_SYSTEM_MESSAGE(1300812);

	/**
	 * You are currently unable to move. There are %0 minute(s) left in your ban.
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_IN_NO_MOVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300813, value0);
	}

	/**
	 * You are currently unable to chat. There are %0 minute(s) left in your ban.
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_IN_NO_CHAT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300814, value0);
	}

	/**
	 * You are currently unable to open a private store. There are %0 minute(s) left on your ban.
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAME_BLOCK_IN_NO_SHOP(String value0) {
		return new SM_SYSTEM_MESSAGE(1300815, value0);
	}

	/**
	 * Unknown Error
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_UNKNOWN = new SM_SYSTEM_MESSAGE(1300816);
	/**
	 * Database Error
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_DATABASE_FAIL = new SM_SYSTEM_MESSAGE(1300817);
	/**
	 * Please select the type of your petition.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_NO_SELECT = new SM_SYSTEM_MESSAGE(1300818);
	/**
	 * Please enter your petition.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_NO_INPUT = new SM_SYSTEM_MESSAGE(1300819);
	/**
	 * You are not allowed to evaluate the reply of a Support Petition.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_CANT_EVAL = new SM_SYSTEM_MESSAGE(1300820);
	/**
	 * There are unfinished replies.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GM_POLL_ANSWERS_NOT_COMPLETED = new SM_SYSTEM_MESSAGE(1300821);
	/**
	 * Abandon Selected Quest
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ABANDON = new SM_SYSTEM_MESSAGE(1300822);
	/**
	 * Invalid target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_INVALID_TARGET = new SM_SYSTEM_MESSAGE(1300823);
	/**
	 * You cannot use this menu when you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_IN_DEAD_STATE = new SM_SYSTEM_MESSAGE(1300824);
	/**
	 * You cannot destroy items while you are a corpse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DEAD_BODY_CANT_DESTROY_ITEM = new SM_SYSTEM_MESSAGE(1300825);
	/**
	 * You cannot use certain chat functions while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_CHAT_IN_DEAD_STATE = new SM_SYSTEM_MESSAGE(1300826);
	/**
	 * /CreateChannel [ChannelName] [OptionalPassword]: Creates a private channel. /JoinChannel [ChannelName] [Password]: Enters an existing private channel. Password required if one was set.
	 * /LeaveChannel [ChannelNumber]: Leaves a private channel. /ChannelMemberInfo [ChannelNumber]: Shows who's in a channel. /ChannelInfo [ChannelNumber]: Shows information for a channel you are in.
	 * /ChannelBanInfo [ChannelNumber]: Shows who is banned from a channel. /BanFromChannel [ChannelNumber] [CharacterName]: The channel owner can permanently remove a character from the channel.
	 * /UnbanFromChannel [ChannelNumber] [CharacterName]: The channel owner can reinstate a banned character's access to a channel. /ChangeChannelLeader [ChannelNumber] [CharacterName]: The channel
	 * owner makes another character the channel owner. /ChangeChannelPassword [ChannelNumber] [Password]: The channel owner can change the password. /ChannelHelp: Shows the commands available for
	 * channels.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CURRENT_STANCE_DOES_NOT_SUPPORTS = new SM_SYSTEM_MESSAGE(1300827);
	/**
	 * You are too close to attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_CLOSE_TO_ATTACK = new SM_SYSTEM_MESSAGE(1300828);
	/**
	 * Someone is already looting that.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOOT_FAIL_ONLOOTING = new SM_SYSTEM_MESSAGE(1300829);
	/**
	 * You are too far from the target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOOT_FAIL_TOO_FAR = new SM_SYSTEM_MESSAGE(1300830);
	/**
	 * You do not have enough Kinah to expand the cube.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_EXPAND_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1300831);
	/**
	 * Cannot find the emblem.bmp file in the Aion Game folder.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WARN_NO_EMBLEM_FILE = new SM_SYSTEM_MESSAGE(1300832);

	/**
	 * %0 cannot be socketed with Manastone.
	 */
	public static SM_SYSTEM_MESSAGE STR_GIVE_ITEM_PROC_NOT_ADD_PROC(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1300833, descriptionId);
	}

	/**
	 * You must pass the Expert promotion test in order to be promoted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_CANT_EXTEND_MONEY = new SM_SYSTEM_MESSAGE(1300834);
	/**
	 * Upload of the Legion emblem file to the server successful.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WARN_SUCCESS_UPLOAD_EMBLEM = new SM_SYSTEM_MESSAGE(1300835);
	/**
	 * Upload of the Legion emblem file to the server failed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WARN_FAILURE_UPLOAD_EMBLEM = new SM_SYSTEM_MESSAGE(1300836);
	/**
	 * Failed to read the Legion emblem file.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WARN_CORRUPT_EMBLEM_FILE = new SM_SYSTEM_MESSAGE(1300837);
	/**
	 * The size of the Legion emblem file is not 24bit 256X256.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_WARN_IMPROPER_SIZE_EMBLEM_FILE = new SM_SYSTEM_MESSAGE(1300838);
	/**
	 * Skill Penalty
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_PENALTY_TITLE = new SM_SYSTEM_MESSAGE(1300839);
	/**
	 * Reduces Evasion, Parry, and Block
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_PENALTY_ACTIVED_EFEND = new SM_SYSTEM_MESSAGE(1300840);
	/**
	 * Reduces Physical Defense and Magical Resistance
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_PENALTY_DEFEND = new SM_SYSTEM_MESSAGE(1300841);

	/**
	 * You have joined the %0 Channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_JOIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300842, value0);
	}

	/**
	 * You have left the %0 Channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_LEAVE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300843, value0);
	}

	/**
	 * You have created the %0 Channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_CREATE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300844, value0);
	}

	/**
	 * Your private channel "%1"% is open as Channel No. %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CREATE_SUCCESS(String value0) {
		return new SM_SYSTEM_MESSAGE(1300845, value0);
	}

	/**
	 * You cannot open or join any more private channels.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CREATE_FAILED_MAXROOM = new SM_SYSTEM_MESSAGE(1300846);
	/**
	 * Incorrect password.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CREATE_FAILED_WRONG_PASSWORD = new SM_SYSTEM_MESSAGE(1300847);
	/**
	 * You do not have enough DP for conversion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CONVERT_SKILL_NOT_ENOUGH_DP = new SM_SYSTEM_MESSAGE(1300848);
	/**
	 * You must have learned the skill to activate it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_MSG_CANT_WORK = new SM_SYSTEM_MESSAGE(1300849);

	/**
	 * You have joined the private channel %1 at Channel %0. Confirm if you selected the channel in the chatting tab option.
	 */
	public static SM_SYSTEM_MESSAGE STR_CAHT_ROOM_JOIN_SUCCESS(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300850, value1, value0);
	}

	/**
	 * That private channel does not exist.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_FAIL_ROOM_NOT_FOUND = new SM_SYSTEM_MESSAGE(1300851);
	/**
	 * You cannot enter the private channel as it is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_FAIL_TOO_MANY_ROOM = new SM_SYSTEM_MESSAGE(1300852);
	/**
	 * An expelled character cannot enter the same private channel again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_FAIL_BANNED_USER = new SM_SYSTEM_MESSAGE(1300853);

	/**
	 * You need to enter a password to join the private channel %0. Please enter it accurately, in the format of '/JoinChannel [ChannelName] [password]'.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_FAIL_WRONG_PASSWORD(String value0) {
		return new SM_SYSTEM_MESSAGE(1300854, value0);
	}

	/**
	 * You cannot enter the private channel (%0) as it is full.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_FAIL_ROOM_FULL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300855, value0);
	}

	/**
	 * %0 has entered the private channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_NOTIFY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300856, value0);
	}

	/**
	 * You have joined the private channel %1 at Channel %0. Confirm if you selected the channel in the chatting tab option.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_JOIN_NOTIFY_SELF(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300857, value1, value0);
	}

	/**
	 * You are not in the private channel %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_LEAVE_FAIL_NOT_A_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300858, value0);
	}

	/**
	 * %0 has left the private channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_LEAVE_NOTIFY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300859, value0);
	}

	/**
	 * You have left the private channel %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_LEAVE_SUCCESS(String value0) {
		return new SM_SYSTEM_MESSAGE(1300860, value0);
	}

	/**
	 * %0 is the new channel leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_NEW_ADMIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1300861, value0);
	}

	/**
	 * You have no authority.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_ADMIN_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1300862);
	/**
	 * Cannot find the character in this private channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_MEMBER_VOID = new SM_SYSTEM_MESSAGE(1300863);

	/**
	 * %0 has been kicked out of the private channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_BAN_MEMBER_BANNED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300864, value0);
	}

	/**
	 * You have been kicked out of the private channel %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_BAN_SELF_BANNED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300865, value0);
	}

	/**
	 * The password of the private channel has been changed to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_PASSWORD_CHANGED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300866, value0);
	}

	/**
	 * The maximum password length is %0 characters.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_PASSWORD_TOO_LONG(String value0) {
		return new SM_SYSTEM_MESSAGE(1300867, value0);
	}

	/**
	 * The name of that private channel is invalid.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_INVALID_CHANNEL_NAME = new SM_SYSTEM_MESSAGE(1300868);
	/**
	 * You have been kicked out.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_KICKED_OUT = new SM_SYSTEM_MESSAGE(1300869);

	/**
	 * %0 has been deleted from the Ban List.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_UNBAN_SUCCESS(String value0) {
		return new SM_SYSTEM_MESSAGE(1300870, value0);
	}

	/**
	 * The channel has been set as public.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_PROPERTYCHANGE_TOPUBLIC = new SM_SYSTEM_MESSAGE(1300871);
	/**
	 * The channel has been set as private.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_PROPERTYCHANGE_TOPRIVATE = new SM_SYSTEM_MESSAGE(1300872);

	/**
	 * Maximum number of users allowed is now set to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_PROPERTYCHANGE_MAXMEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300873, value0);
	}

	/**
	 * You are not participating in any channels.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_NO_JOINED_CHATROOM = new SM_SYSTEM_MESSAGE(1300874);
	/**
	 * /CreateChannel [ChannelName] [OptionalPassword]: Creates a private channel. /JoinChannel [ChannelName] [Password]: Enters an existing private channel. Password required if one was set.
	 * /LeaveChannel [ChannelNumber]: Leaves a private channel. /ChannelMemberInfo [ChannelNumber]: Shows who's in a channel. /ChannelInfo [ChannelNumber]: Shows information for a channel you are in.
	 * /ChannelBanInfo [ChannelNumber]: Shows who is banned from a channel. /BanFromChannel [ChannelNumber] [CharacterName]: The channel owner can permanently remove a character from the channel.
	 * /UnbanFromChannel [ChannelNumber] [CharacterName]: The channel owner can reinstate a banned character's access to a channel. /ChangeChannelLeader [ChannelNumber] [CharacterName]: The channel
	 * owner makes another character the channel owner. /ChangeChannelPassword [ChannelNumber] [Password]: The channel owner can change the password. /ChannelHelp: Shows the commands available for
	 * channels.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_HELP = new SM_SYSTEM_MESSAGE(1300875);
	/**
	 * You cannot learn a design written in an incomprehensible language.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFTRECIPE_RACE_CHECK = new SM_SYSTEM_MESSAGE(1300876);
	/**
	 * An express courier has already arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_ALREADY_SUMMONED = new SM_SYSTEM_MESSAGE(1300877);
	/**
	 * Please wait for a while before you call for the courier again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_UNABLE_IN_COOLTIME = new SM_SYSTEM_MESSAGE(1300878);
	/**
	 * You cannot call a courierwhile flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_UNABLE_IN_FLIGHT = new SM_SYSTEM_MESSAGE(1300879);
	/**
	 * You cannot call a courier here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_UNABLE_POSITION = new SM_SYSTEM_MESSAGE(1300880);
	/**
	 * That character does not exist.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_CHARACTER_NONEXISIT = new SM_SYSTEM_MESSAGE(1300881);
	/**
	 * That person is not logged on.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_NO_OFFLINE_CHARACTER = new SM_SYSTEM_MESSAGE(1300882);
	/**
	 * The character is already on your Friends List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_ALREADY_IN_LIST = new SM_SYSTEM_MESSAGE(1300883);
	/**
	 * A blocked character cannot also be a Friend.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_NO_BLOCKED_CHARACTER = new SM_SYSTEM_MESSAGE(1300884);

	/**
	 * You have added %0 to your Friends List.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDYLIST_ADD_BUDDY_ACCEPTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300885, value0);
	}

	/**
	 * %0 declined your friend request.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDYLIST_ADD_BUDDY_REJECTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300886, value0);
	}

	/**
	 * Your Friends List is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_LIST_FULL = new SM_SYSTEM_MESSAGE(1300887);

	/**
	 * You have removed %0 from your Friends List.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDYLIST_REMOVE_CHARACTER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300888, value0);
	}

	/**
	 * The character is not on your Friends List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_NOT_IN_LIST = new SM_SYSTEM_MESSAGE(1300889);

	/**
	 * Your friend %0 has logged in.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDYLIST_BUDDY_LOGON(String value0) {
		return new SM_SYSTEM_MESSAGE(1300890, value0);
	}

	/**
	 * You rejected the friend request from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_BUDDYLIST_REQUEST_REJECTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1401517, value0);
	}

	/**
	 * You cannot block a character who is currently on your Friends List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_NO_BUDDY = new SM_SYSTEM_MESSAGE(1300891);

	/**
	 * You have blocked %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_BLOCKLIST_ADD_BLOCKED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300892, value0);
	}

	/**
	 * That character does not exist.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_CHARACTER_NONEXIST = new SM_SYSTEM_MESSAGE(1300893);
	/**
	 * That character is already blocked.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_ALREADY_BLOCKED = new SM_SYSTEM_MESSAGE(1300894);
	/**
	 * Enter the name of the character you want to block.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_ENTER_CHARACTER_NAME = new SM_SYSTEM_MESSAGE(1300895);

	/**
	 * You have unblocked %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_BLOCKLIST_REMOVE_FROM_LIST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300896, value0);
	}

	/**
	 * The character is not blocked.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_NOT_IN_LIST = new SM_SYSTEM_MESSAGE(1300897);
	/**
	 * You must level up to raise your skill level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_INFO_MAXPOINT_UP = new SM_SYSTEM_MESSAGE(1300898);
	/**
	 * Express mail has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_NOTIFY = new SM_SYSTEM_MESSAGE(1300899);

	/**
	 * Channel information: %0, Name: %1, Password: %2, Users: %num3.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_INFO_FORMAT(String value0, String value1, String value2, int num3) {
		return new SM_SYSTEM_MESSAGE(1300900, value0, value1, value2, num3);
	}

	/**
	 * Channel information: %0, Name: %1, Users: %num2.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_INFO_FORMAT_NOPASSWORD(String value0, String value1, int num2) {
		return new SM_SYSTEM_MESSAGE(1300901, value0, value1, num2);
	}

	/**
	 * You have been disconnected from the server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_AUTH_CONNECTION_LOST = new SM_SYSTEM_MESSAGE(1300902);
	/**
	 * A private channel with the same name already exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_ROOM_EXISTS = new SM_SYSTEM_MESSAGE(1300903);
	/**
	 * That private channel already exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CHANNEL_EXISTS = new SM_SYSTEM_MESSAGE(1300904);
	/**
	 * You have already joined the private channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_ALREADY_JOINED_CHANNEL = new SM_SYSTEM_MESSAGE(1300905);
	/**
	 * The character has been banned from this channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_ALREADY_BANNED_MEMBER = new SM_SYSTEM_MESSAGE(1300906);
	/**
	 * You cannot kick yourself out of the channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CANNOT_BAN_SELF = new SM_SYSTEM_MESSAGE(1300907);
	/**
	 * You cannot nominate yourself as a room master.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CANNOT_PROMOTE_SELF = new SM_SYSTEM_MESSAGE(1300908);
	/**
	 * The character is not on the Ban List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_NOT_A_BANNED_CHARACTER = new SM_SYSTEM_MESSAGE(1300909);

	/**
	 * Requires the %0 Equip Skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_TOOLTIP_NEED_MASTERY_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1300910, value0);
	}

	/**
	 * %0 has sent you a friend request.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDY_REQUEST_TO_ADD(String value0) {
		return new SM_SYSTEM_MESSAGE(1300911, value0);
	}

	/**
	 * Your Block List is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_LIST_FULL = new SM_SYSTEM_MESSAGE(1300912);
	/**
	 * You cannot block yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BLOCKLIST_CANNOT_BLOCK_SELF = new SM_SYSTEM_MESSAGE(1300913);
	/**
	 * You cannot use a Macro yet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_READY_TO_USE = new SM_SYSTEM_MESSAGE(1300914);

	/**
	 * %0 has logged in.
	 */
	public static SM_SYSTEM_MESSAGE STR_NOTIFY_LOGIN_BUDDY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300915, value0);
	}

	/**
	 * %0 has logged out.
	 */
	public static SM_SYSTEM_MESSAGE STR_NOTIFY_LOGOFF_BUDDY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300916, value0);
	}

	/**
	 * %0 has deleted you from their Friends List.
	 */
	public static SM_SYSTEM_MESSAGE STR_NOTIFY_DELETE_BUDDY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300917, value0);
	}

	/**
	 * The selected character is already dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_DEAD = new SM_SYSTEM_MESSAGE(1300918);

	/**
	 * Builder Command %0 requires %1 parameters.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_BC_NOT_ENOUGH_PARAMETER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300919, value0, value1);
	}

	/**
	 * You cannot equip or remove items while in action.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_EQUIP_ITEM_IN_ACTION = new SM_SYSTEM_MESSAGE(1300920);

	/**
	 * %0: Level %1 (%2)
	 */
	public static SM_SYSTEM_MESSAGE STR_MACRO_MSG_PROCESS(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1300921, value0, value1, value2);
	}

	/**
	 * Macro: Cannot find the skill.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_FIND_SKILL = new SM_SYSTEM_MESSAGE(1300922);

	/**
	 * Your petition has been received.\nThe receipt number is %0.\nThere are %1 users on the waiting list, and the approximate waiting time is %2.\nYou have submitted %3 petitions today, and there
	 * are %4 more petitions left. Thank you!
	 */
	public static SM_SYSTEM_MESSAGE STR_PETITION_SUBMIT_MESSAGE() {
		return new SM_SYSTEM_MESSAGE(1300923);
	}

	/**
	 * There is a reply to your petition.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOOLTIP_PETITION_MESSAGE_NOTICE = new SM_SYSTEM_MESSAGE(1300924);
	/**
	 * You are in a chat with the GM.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_CHAT_MESSAGE = new SM_SYSTEM_MESSAGE(1300925);
	/**
	 * Macro: Cannot find the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_FIND_ITEM = new SM_SYSTEM_MESSAGE(1300926);
	/**
	 * You do not have enough Abyss Points.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_ABYSSPOINT = new SM_SYSTEM_MESSAGE(1300927);
	/**
	 * You cannot change the channel during a battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_CHANGE_CHANNEL_IN_COMBAT = new SM_SYSTEM_MESSAGE(1300928);
	/**
	 * You cannot change the channel now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_CHANGE_CHANNEL_NOW = new SM_SYSTEM_MESSAGE(1300929);

	/**
	 * Campaign quest acquired: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_MISSION_SYSTEMMSG_ACQUIRE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300930, value0);
	}

	/**
	 * Start Punishment
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAR_PUNISH_START_TIME = new SM_SYSTEM_MESSAGE(1300931);
	/**
	 * End Punishment
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAR_PUNISH_END_TIME = new SM_SYSTEM_MESSAGE(1300932);
	/**
	 * Macro: There is no item registered in the Quickbar.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_FIND_SHORTCUT = new SM_SYSTEM_MESSAGE(1300933);
	/**
	 * Macro: Cannot find the target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_FIND_TARGET = new SM_SYSTEM_MESSAGE(1300934);
	/**
	 * Macro: The sentence cannot be parsed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_PARSE = new SM_SYSTEM_MESSAGE(1300935);

	/**
	 * Please do not flood chat. Blocked for %0m.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_FLOODING_BLOCKED_1(String value0m) {
		return new SM_SYSTEM_MESSAGE(1300936, value0m);
	}

	/**
	 * You can use the Channel: %0 only once every %1 seconds. Time Remaining: %2 seconds
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_FLOODING_BLOCKED_2(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1300937, value0, value1, value2);
	}

	/**
	 * Both local and trade channels were moved to the %0 area.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_LEVEL_CHANGED(String value0) {
		return new SM_SYSTEM_MESSAGE(1300938, value0);
	}

	/**
	 * The channel name must be between 2 and 10 characters.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_CHANNELNAME_SIZE_LIMIT = new SM_SYSTEM_MESSAGE(1300939);
	/**
	 * You cannot resurrect the target due to its insufficient Abyss Points.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_RESURRECT_FAILED = new SM_SYSTEM_MESSAGE(1300940);
	/**
	 * You have too few Abyss points to continue the battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_RESURRECT = new SM_SYSTEM_MESSAGE(1300941);
	/**
	 * The same item is already registered.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_ALREAY_REGIST_ITEM = new SM_SYSTEM_MESSAGE(1300942);
	/**
	 * You cannot register items in the private store while equipped with Stigma.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CANNOT_REGIST_DURING_STIGMA = new SM_SYSTEM_MESSAGE(1300943);
	/**
	 * You cannot register items as you are already selling other items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CANNOT_REGIST_DURING_SELLING = new SM_SYSTEM_MESSAGE(1300944);

	/**
	 * You received %0 item as reward for the survey.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_POLL_REWARD_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300945, value0);
	}

	/**
	 * You received %num1 %0 items as reward for the survey.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_POLL_REWARD_ITEM_MULTI(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1300946, num1, value0);
	}

	/**
	 * You received %num0 Kinah as reward for the survey.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_POLL_REWARD_MONEY(int num0) {
		return new SM_SYSTEM_MESSAGE(1300947, num0);
	}

	/**
	 * Starting the voice chatting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_START_SUCCESS = new SM_SYSTEM_MESSAGE(1300948);
	/**
	 * Failed to start the voice chatting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_START_FAILED = new SM_SYSTEM_MESSAGE(1300949);
	/**
	 * Ending the voice chatting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_FINISH_SUCCESS = new SM_SYSTEM_MESSAGE(1300950);
	/**
	 * Failed to end the voice chatting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_FINISH_FAILED = new SM_SYSTEM_MESSAGE(1300951);
	/**
	 * You cannot use private channels before you change your Class.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_REQUIREMENT_UNFULLFILLED = new SM_SYSTEM_MESSAGE(1300952);

	/**
	 * %0 starts the voice chatting.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_START_NOTIFY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300953, value0);
	}

	/**
	 * %0 ends the voice chatting.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_ROOM_VOICE_FINISH_NOTIFY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300954, value0);
	}

	/**
	 * The password for this private channel has been removed. You can now join the channel without entering the password.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_ROOM_PASSWORD_DELETED = new SM_SYSTEM_MESSAGE(1300955);

	/**
	 * Purchase Item\n%attachItemName you bought has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_CASHITEM_BUY(int itemId) {
		return new SM_SYSTEM_MESSAGE(1300956, "[item:" + itemId + "]");
	}

	/**
	 * Purchase Item\n%attachItemName you bought has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_CASHITEM_GIFT(int itemId) {
		return new SM_SYSTEM_MESSAGE(1300957, "[item:" + itemId + "]");
	}

	/**
	 * You can only send mails to other users of your race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_MSG_DIFFERENT_RACE = new SM_SYSTEM_MESSAGE(1300958);
	/**
	 * You cannot fly in this area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLYING_FORBIDDEN_ZONE = new SM_SYSTEM_MESSAGE(1300959);
	/**
	 * You cannot fly in this area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLYING_FORBIDDEN_HERE = new SM_SYSTEM_MESSAGE(1300960);
	/**
	 * Flight cooldown time has not expired yet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLYING_TIME_NOT_READY = new SM_SYSTEM_MESSAGE(1300961);
	/**
	 * Your petition is being processed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_OPEN_MESSAGE = new SM_SYSTEM_MESSAGE(1300962);
	/**
	 * Some options are applied when the game is restarted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_APPLY_OPTION_WHEN_RESTART = new SM_SYSTEM_MESSAGE(1300963);
	/**
	 * You cannot use special characters in channel name and password.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_INVALID_CHANNEL_NAME_SPECIAL_LETTER = new SM_SYSTEM_MESSAGE(1300964);

	/**
	 * You used %num0 Abyss Points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_USE_ABYSSPOINT(int num0) {
		return new SM_SYSTEM_MESSAGE(1300965, num0);
	}

	/**
	 * You have invited %0 to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITE_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300966, value0);
	}

	/**
	 * You have received an alliance invitation from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITE_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300967, value0);
	}

	/**
	 * You have invited %0's group to the alliance. %0's group has a total of %1 members.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITE_PARTY(String value0, String value2, String value1) {
		return new SM_SYSTEM_MESSAGE(1300968, value0, value2, value1);
	}

	/**
	 * The leader of %0's group is %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITE_PARTY_HIM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300969, value0, value1);
	}

	/**
	 * Your group has received an alliance invitation from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITE_PARTY_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300970, value0);
	}

	/**
	 * You have declined %0's invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_REJECT_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300971, value0);
	}

	/**
	 * %0 has declined your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_REJECT_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300972, value0);
	}

	/**
	 * %0's group has declined your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_REJECT_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1300973, value0);
	}

	/**
	 * %0 is already a member of another alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ALREADY_OTHER_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1300974, value0);
	}

	/**
	 * There is not enough room in the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT = new SM_SYSTEM_MESSAGE(1300975);
	/**
	 * You have no authority in the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_RIGHT_NOT_HAVE = new SM_SYSTEM_MESSAGE(1300976);
	/**
	 * You have left the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LEAVE_ME = new SM_SYSTEM_MESSAGE(1300977);

	/**
	 * %0 has left the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_LEAVE_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1300978, value0);
	}

	/**
	 * %0 has kicked you out of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_BAN_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1300979, value0);
	}

	/**
	 * %0 has kicked out %1 of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_BAN_HIM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1300980, value0, value1);
	}

	/**
	 * %0 has left the alliance due to a prolonged absence.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_LEAVE_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300981, value0);
	}

	/**
	 * %0 is now Captain of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_CHANGE_LEADER_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1300982, value0);
	}

	/**
	 * The alliance has disbanded due to a lack of members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_DISPERSED = new SM_SYSTEM_MESSAGE(1300983);

	/**
	 * %0 is now vice Captain of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_PROMOTE_MANAGER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300984, value0);
	}

	/**
	 * %0 has been demoted to member from vice Captain.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_DEMOTE_MANAGER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300985, value0);
	}

	/**
	 * %0 has promoted %1. From now on, %1 is the alliance captain.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_CHANGE_LEADER(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1300986, value0, value1, value2);
	}

	/**
	 * You have failed to change the alliance group as another person is already trying to change it. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_GROUP_FAILED_ALREADY_CHANGED = new SM_SYSTEM_MESSAGE(1300987);
	/**
	 * You have failed to change the group because there was no group to change.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_GROUP_FAILED_ALREADY_LEAVE = new SM_SYSTEM_MESSAGE(1300988);
	/**
	 * Checking the readiness of the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_START = new SM_SYSTEM_MESSAGE(1300989);

	/**
	 * %0 has requested to check the combat readiness.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_CHECK_REQUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1300990, value0);
	}

	/**
	 * All alliance members are ready.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_COMPLETE = new SM_SYSTEM_MESSAGE(1300991);
	/**
	 * Currently Absent:
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_OUT = new SM_SYSTEM_MESSAGE(1300992);
	/**
	 * Ready:
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_READY = new SM_SYSTEM_MESSAGE(1300993);
	/**
	 * Not Ready:
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_DENIED = new SM_SYSTEM_MESSAGE(1300994);
	/**
	 * You cancelled the request to check the readiness of the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CHECK_CANCEL = new SM_SYSTEM_MESSAGE(1300995);
	/**
	 * You cannot invite any more members to the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_ADD_NEW_MEMBER = new SM_SYSTEM_MESSAGE(1300996);
	/**
	 * Only the alliance Captain can make another person the Captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ONLY_LEADER_CAN_CHANGE_LEADER = new SM_SYSTEM_MESSAGE(1300997);

	/**
	 * %0 is now the alliance captain.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_IS_NEW_LEADER(String value0) {
		return new SM_SYSTEM_MESSAGE(1300998, value0);
	}

	/**
	 * You are now the alliance captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_YOU_BECOME_NEW_LEADER = new SM_SYSTEM_MESSAGE(1300999);
	/**
	 * Only the alliance captain can change the item distribution method.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ONLY_LEADER_CAN_CHANGE_LOOTING = new SM_SYSTEM_MESSAGE(1301000);
	/**
	 * The item distribution method of the alliance has been changed to Manual.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LOOTING_CHANGED_TO_MANUAL = new SM_SYSTEM_MESSAGE(1301001);
	/**
	 * The item distribution method of the alliance has been changed to Auto.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LOOTING_CHANGED_TO_AUTO = new SM_SYSTEM_MESSAGE(1301002);
	/**
	 * There is no target to invite to the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_NO_USER_TO_INVITE = new SM_SYSTEM_MESSAGE(1301003);
	/**
	 * Only the alliance Captain and vice Captain can invite people to the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ONLY_LEADER_CAN_INVITE = new SM_SYSTEM_MESSAGE(1301004);

	/**
	 * %0 has declined your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_REJECT_INVITATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1301005, value0);
	}

	/**
	 * You cannot invite yourself to the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CAN_NOT_INVITE_SELF = new SM_SYSTEM_MESSAGE(1301006);
	/**
	 * You cannot issue invitations while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_INVITE_WHEN_DEAD = new SM_SYSTEM_MESSAGE(1301007);
	/**
	 * The selected alliance member is currently offline.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_OFFLINE_MEMBER = new SM_SYSTEM_MESSAGE(1301008);
	/**
	 * Only the alliance captain can kick out a member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ONLY_LEADER_CAN_BANISH = new SM_SYSTEM_MESSAGE(1301009);
	/**
	 * You have been kicked out of the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_YOU_ARE_BANISHED = new SM_SYSTEM_MESSAGE(1301010);
	/**
	 * The alliance has been disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_IS_DISPERSED = new SM_SYSTEM_MESSAGE(1301011);

	/**
	 * %0 has left the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_LEAVE_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1301012, value0);
	}

	/**
	 * %0 is a member of another alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_IS_ALREADY_MEMBER_OF_OTHER_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1301013, value0);
	}

	/**
	 * %0 is already a member of your alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_IS_ALREADY_MEMBER_OF_OUR_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1301014, value0);
	}

	/**
	 * You are not in an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_YOU_ARE_NOT_FORCE_MEMBER = new SM_SYSTEM_MESSAGE(1301015);
	/**
	 * You are not an alliance member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_NOT_FORCE_MEMBER = new SM_SYSTEM_MESSAGE(1301016);

	/**
	 * You have invited %0 to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_INVITED_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1301017, value0);
	}

	/**
	 * Currently, %0 cannot accept your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1301018, value0);
	}

	/**
	 * %0 has been disconnected.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_BECOME_OFFLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1301019, value0);
	}

	/**
	 * %0 has been offline for too long and had been automatically kicked out of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_BECOME_OFFLINE_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1301020, value0);
	}

	/**
	 * %0 has been kicked out of the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_IS_BANISHED(String value0) {
		return new SM_SYSTEM_MESSAGE(1301021, value0);
	}

	/**
	 * The rare item distribution method of the alliance has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_RARE_LOOTING_CHANGED_TO_MANUAL = new SM_SYSTEM_MESSAGE(1301022);
	/**
	 * The rare item distribution method of the alliance has been changed to Auto.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_RARE_LOOTING_CHANGED_TO_AUTO = new SM_SYSTEM_MESSAGE(1301023);
	/**
	 * The rare item distribution method of the alliance has been changed to Dice Roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_RARE_LOOTING_CHANGED_TO_DICE = new SM_SYSTEM_MESSAGE(1301024);
	/**
	 * An alliance member cannot be kicked out before the items have been distributed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANNOT_BANISH_ITEMPOOL_NOT_EMPTY = new SM_SYSTEM_MESSAGE(1301025);

	/**
	 * %0 rolled the dice and got a %num1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ITEM_DICE(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1301026, value0, num1);
	}

	/**
	 * You can roll the dice once more if the rolled number is less than 100.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ITEM_DICE_AGAIN = new SM_SYSTEM_MESSAGE(1301027);
	/**
	 * The item distribution method of the alliance has been changed to Free-for-All.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LOOTING_CHANGED_TO_FREEFORALL = new SM_SYSTEM_MESSAGE(1301028);
	/**
	 * The item distribution method of the alliance has been changed to Round-robin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LOOTING_CHANGED_TO_ROUNDROBIN = new SM_SYSTEM_MESSAGE(1301029);
	/**
	 * The item distribution method of the alliance has been changed to Captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_LOOTING_CHANGED_TO_LEADERONLY = new SM_SYSTEM_MESSAGE(1301030);

	/**
	 * %0 has been kicked out of the arena.
	 */
	public static SM_SYSTEM_MESSAGE STR_PvPZONE_OUT_MESSAGE(String value0) {
		return new SM_SYSTEM_MESSAGE(1301031, value0);
	}

	/**
	 * You cannot fly while you are banned from flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_FLY_NOW_DUE_TO_NOFLY = new SM_SYSTEM_MESSAGE(1301032);

	/**
	 * The %0 %1 is activating the %2 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_CASTING(DescriptionId race, String value1, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301033, race, value1, descriptionId);
	}

	/**
	 * The %1 Artifact core in %0 possession has been deactivated.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_CORE_CASTING(DescriptionId race, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301034, race, descriptionId);
	}

	/**
	 * The activation of the %1 Artifact in %0 possession has been cancelled.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_CANCELED(DescriptionId race, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301035, race, descriptionId);
	}

	/**
	 * The %0 %1 has succeeded in activating the %2 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_FIRE(DescriptionId race, String value1, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301036, race, value1, descriptionId);
	}

	/**
	 * %0 Legion lost %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_GUILD_CASTLE_TAKEN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1301037, value0, value1);
	}

	/**
	 * %0 has conquered %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_GUILD_WIN_CASTLE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1301038, value0, value1);
	}

	/**
	 * %0 succeeded in conquering %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_WIN_CASTLE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1301039, value0, value1);
	}

	/**
	 * %0 is now vulnerable.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_PVP_ON(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301040, descriptionId);
	}

	/**
	 * %0 is no longer vulnerable.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_PVP_OFF(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1301041, descriptionId);
	}

	/**
	 * The Dredgion has disgorged a horde of Balaur troopers.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_CARRIER_DROP_DRAGON = new SM_SYSTEM_MESSAGE(1301042);
	/**
	 * The Balaur Teleport Raiders appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_WARP_DRAGON = new SM_SYSTEM_MESSAGE(1301043);
	/**
	 * A dredgion has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_CARRIER_SPAWN = new SM_SYSTEM_MESSAGE(1301044);
	/**
	 * Cannot find the target to use the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_CANT_FIND_VALID_TARGET = new SM_SYSTEM_MESSAGE(1301045);

	/**
	 * %0 failed to defend %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_CASTLE_TAKEN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1301046, value0, value1);
	}

	/**
	 * The %0 item has been sold by the broker.
	 */
	public static SM_SYSTEM_MESSAGE STR_VENDOR_REGISTER_SOLD_OUT(int nameId) {
		return new SM_SYSTEM_MESSAGE(1301047, new DescriptionId(nameId));
	}

	/**
	 * %1 of the %0 killed the Aetheric Field Generator.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_SHIELD_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1301048, value1, value0);
	}

	/**
	 * %1 of the %0 destroyed the Castle Gate.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_DOOR_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1301049, value1, value0);
	}

	/**
	 * The Castle Gate is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DOOR_ATTACKED = new SM_SYSTEM_MESSAGE(1301050);
	/**
	 * The Castle Gate is in danger.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DOOR_ATSTAKE = new SM_SYSTEM_MESSAGE(1301051);
	/**
	 * The Aetheric Field Generator is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_SHIELD_ATTACKED = new SM_SYSTEM_MESSAGE(1301052);
	/**
	 * The Gate Guardian Stone is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_REPAIR_ATTACKED = new SM_SYSTEM_MESSAGE(1301053);

	/**
	 * %1 of the %0 destroyed the Gate Guardian Stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_REPAIR_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1301054, value1, value0);
	}

	/**
	 * The Guardian General is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_BOSS_ATTACKED = new SM_SYSTEM_MESSAGE(1301055);
	/**
	 * You cannot start gliding as you are moving too slowly.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLIDE_NOT_ENOUGH_SPEED_FOR_GLIDE = new SM_SYSTEM_MESSAGE(1301056);
	/**
	 * You cannot start gliding while in an Altered State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLIDE_CANNOT_GLIDE_ABNORMAL_STATUS = new SM_SYSTEM_MESSAGE(1301057);
	/**
	 * You cannot change to the combat mode while gliding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLIDE_CANNOT_GLIDE_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1301058);
	/**
	 * You can glide when you become a Daeva.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLIDE_ONLY_DEVA_CAN = new SM_SYSTEM_MESSAGE(1301059);

	/**
	 * You do not have enough mana to continue using %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_INSUFFICIENT_COST_FOR_TOGGLE_SKILLL(String value0) {
		return new SM_SYSTEM_MESSAGE(1301060, value0);
	}

	/**
	 * You cannot appoint any more vice captains. The alliance can have a maximum of 4.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANNOT_PROMOTE_MANAGER = new SM_SYSTEM_MESSAGE(1301061);

	/**
	 * %0 has been activated.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_PROC_EFFECT_OCCURRED(int value0) {
		return new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(value0));
	}

	/**
	 * You are already under the same effect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_SAME_EFFECT_ALREADY_TAKEN = new SM_SYSTEM_MESSAGE(1301063);

	/**
	 * You declined %0's challenge for a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_REJECT_DUEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1301064, value0);
	}

	/**
	 * %0 has challenged you to a duel.
	 */
	public static SM_SYSTEM_MESSAGE STR_DUEL_REQUESTED(String value0) {
		return new SM_SYSTEM_MESSAGE(1301065, value0);
	}

	/**
	 * You are currently unable to chat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_DISABLED = new SM_SYSTEM_MESSAGE(1310000);
	/**
	 * You are unable to chat for 2 minutes as you interrupted the game play through unnecessary chatting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLOODING = new SM_SYSTEM_MESSAGE(1310001);

	/**
	 * Characters under level %0 cannot chat.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_CHAT_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1310002, value0);
	}

	/**
	 * You have too many users blocked from chatting with you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOO_MANY_EXCLUDE = new SM_SYSTEM_MESSAGE(1310003);

	/**
	 * Characters under level %0 cannot send whispers.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_WHISPER_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1310004, value0);
	}

	/**
	 * The NPC server is down. Please restore it soon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_NPC_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1310005);
	/**
	 * The connection with the cache server has been severed. Please restore it soon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CACHE_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1310006);
	/**
	 * The connection with the authorization server has been severed. Please restore it soon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_AUTH_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1310007);
	/**
	 * The connection with the ittem billing server has been severed. Please restore it soon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_BILLING_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1310008);

	/**
	 * You disabled chatting for %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_DISABLED_OTHER_CHAT(String value0) {
		return new SM_SYSTEM_MESSAGE(1310009, value0);
	}

	/**
	 * This is an Assist Target Key. Use it after you have selected a target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ASSISTKEY_THIS_IS_ASSISTKEY = new SM_SYSTEM_MESSAGE(1310010);
	/**
	 * Please use the right NPC for your race to register items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_RACECHECK = new SM_SYSTEM_MESSAGE(1310011);
	/**
	 * You cannot chat while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_CHAT_AT_DIE = new SM_SYSTEM_MESSAGE(1310012);

	/**
	 * You cannot quit during a battle. Canceling in %0 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_QUIT_DURING_BATTLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1310013, value0);
	}

	/**
	 * An error has occurred while restoring the login list on the Billing server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_BILLGATES_ERROR = new SM_SYSTEM_MESSAGE(1310014);
	/**
	 * A dual login error has occurred while trying to enter the world.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_DUAL_LOGIN_ON_ABOUT_TO_PLAY = new SM_SYSTEM_MESSAGE(1310015);
	/**
	 * Your account has been banned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_BANNED = new SM_SYSTEM_MESSAGE(1310016);
	/**
	 * You have been disconnected from the server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_CHARACTER = new SM_SYSTEM_MESSAGE(1310017);
	/**
	 * Your World access time limit has been exceeded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_KICK_ABOUT_TO_PLAY_TIMER_EXPIRED = new SM_SYSTEM_MESSAGE(1310018);
	/**
	 * The requested target no longer exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SEARCH_NOT_EXIST = new SM_SYSTEM_MESSAGE(1310019);
	/**
	 * The user you requested is currently offline.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SEARCH_DISCONNECT = new SM_SYSTEM_MESSAGE(1310020);
	/**
	 * Only Daevas can use that.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_USE_GROUPGATE_BEFORE_CHANGE_CLASS = new SM_SYSTEM_MESSAGE(1310021);
	/**
	 * You do not have enough credit left in the account.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_NOT_PAID = new SM_SYSTEM_MESSAGE(1310022);
	/**
	 * Invalid session info.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INVALID_SESSION = new SM_SYSTEM_MESSAGE(1310023);
	/**
	 * The server list info in the server is incorrect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_SERVERLIST_INCORRECT = new SM_SYSTEM_MESSAGE(1310024);
	/**
	 * Failed to create the character due to a World DB error.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_WORLD_DB_FAIL = new SM_SYSTEM_MESSAGE(1310025);
	/**
	 * You are disconnected from the game server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_CONNECTION_LOST = new SM_SYSTEM_MESSAGE(1310026);
	/**
	 * Failed to connect to the game server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_CONNECTION_FAIL = new SM_SYSTEM_MESSAGE(1310027);
	/**
	 * The client version is not compatible with the game server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_VERSION_FAIL = new SM_SYSTEM_MESSAGE(1310028);
	/**
	 * Characters of different races exist in the same server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_HAS_MULTIPLE_RACE = new SM_SYSTEM_MESSAGE(1310029);
	/**
	 * The NPC script version is not compatible with the game server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_NPC_SCRIPT_VERSION_FAIL = new SM_SYSTEM_MESSAGE(1310030);
	/**
	 * An unknown error has occurred while checking the game server version.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_UNKNOWN_VERSION_FAIL = new SM_SYSTEM_MESSAGE(1310031);
	/**
	 * Failed to delete the character.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_DEL_CHAR_FAIL = new SM_SYSTEM_MESSAGE(1310032);
	/**
	 * Cannot connect to the login server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_CONNECTION_FAIL = new SM_SYSTEM_MESSAGE(1310033);
	/**
	 * That character does not exist.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_CHAR_NOT_EXIST = new SM_SYSTEM_MESSAGE(1310034);
	/**
	 * That character is already set to be deleted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_CHAR_ALREADY_DELETED = new SM_SYSTEM_MESSAGE(1310035);
	/**
	 * Failed to create the character.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_FAILED_TO_CREATE_CHAR = new SM_SYSTEM_MESSAGE(1310036);
	/**
	 * A character with that name already exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_CHARACTER_EXIST = new SM_SYSTEM_MESSAGE(1310037);
	/**
	 * You cannot create any more characters on this server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_NO_AVAILABLE_SLOT = new SM_SYSTEM_MESSAGE(1310038);
	/**
	 * Invalid server ID.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INVALID_SERVERID = new SM_SYSTEM_MESSAGE(1310039);
	/**
	 * Too many users on the game server. You cannot access the game.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_TOO_MANY_USER = new SM_SYSTEM_MESSAGE(1310040);
	/**
	 * The game server memory is full. You cannot access the game.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_OUT_OF_MEMORY = new SM_SYSTEM_MESSAGE(1310041);
	/**
	 * The selected character is already playing on the selected server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_ALREADY_PLAYING = new SM_SYSTEM_MESSAGE(1310042);
	/**
	 * You cannot create any more characters on that account.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_MAX_CHAR_COUNT = new SM_SYSTEM_MESSAGE(1310043);
	/**
	 * Invalid character name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INVALID_NAME = new SM_SYSTEM_MESSAGE(1310044);
	/**
	 * Invalid character gender.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INVALID_GENDER = new SM_SYSTEM_MESSAGE(1310045);
	/**
	 * Invalid character class.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INVALID_CLASS = new SM_SYSTEM_MESSAGE(1310046);
	/**
	 * The game server is down.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_SERVER_DOWN = new SM_SYSTEM_MESSAGE(1310047);
	/**
	 * The Billing server is down.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_BILLGATES_DOWN = new SM_SYSTEM_MESSAGE(1310048);
	/**
	 * Internal game server error
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOGIN_ERROR_INTERNAL_SERVER_ERROR = new SM_SYSTEM_MESSAGE(1310049);
	/**
	 * You have been disconnected from the server by request of the PlayNC Homepage.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_KICKED_BY_WEB = new SM_SYSTEM_MESSAGE(1310050);
	/**
	 * You are not old enough to play the game.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_UNDER_AGE = new SM_SYSTEM_MESSAGE(1310051);
	/**
	 * Double login attempts have been detected.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_KICKED_DOUBLE_LOGIN = new SM_SYSTEM_MESSAGE(1310052);
	/**
	 * You are already logged in.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_ALREADY_PLAY_GAME = new SM_SYSTEM_MESSAGE(1310053);
	/**
	 * Sorry, the queue is full. Please try another server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_LIMIT_EXCEED = new SM_SYSTEM_MESSAGE(1310054);
	/**
	 * The server is currently unavailable. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_SEVER_CHECK = new SM_SYSTEM_MESSAGE(1310055);
	/**
	 * Please login to the game after you have changed your password.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_MODIFY_PASSWORD = new SM_SYSTEM_MESSAGE(1310056);
	/**
	 * Either the usage period has expired or we are experiencing a temporary connection difficulty. For more information, please contact the administrators or our customer center.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_NOT_PAID = new SM_SYSTEM_MESSAGE(1310057);
	/**
	 * You have used up your allocated time and there is no time left on this account.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_NO_SPECIFICTIME = new SM_SYSTEM_MESSAGE(1310058);
	/**
	 * System error.
	 */
	public static final SM_SYSTEM_MESSAGE STR_L2AUTH_S_SYSTEM_ERROR = new SM_SYSTEM_MESSAGE(1310059);
	/**
	 * You cannot open a private store in the arena.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PvPZONE_CANNOT_OPEN_MARKET = new SM_SYSTEM_MESSAGE(1310060);
	/**
	 * You cannot continue unless you stop flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_CANNOT_PROCESS_IN_FLIGHT = new SM_SYSTEM_MESSAGE(1310061);

	/**
	 * You have gained %num0 Abyss Points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_ABYSS_POINT_GAIN(int num0) {
		return new SM_SYSTEM_MESSAGE(1320000, num0);
	}

	/**
	 * A one-way Rift into Asmodae has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DARK_SIDE_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1320001);

	/**
	 * %1 of %0 has captured the %2 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_WIN_ARTIFACT(String value1, String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1320002, value1, value0, value2);
	}

	/**
	 * %0 has conquered %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_WIN_FORT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1320003, value0, value1);
	}

	/**
	 * The %0 Artifact has been lost to %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_LOSE_ARTIFACT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1320004, value0, value1);
	}

	/**
	 * %0 Legion lost %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_LOSE_FORT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1320005, value0, value1);
	}

	/**
	 * Someone is already gathering it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_OUCCPIED_BY_OTHER = new SM_SYSTEM_MESSAGE(1330000);

	/**
	 * Your %0 skill level is not high enough.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_OUT_OF_SKILL_POINT(String value0) {
		return new SM_SYSTEM_MESSAGE(1330001, value0);
	}

	/**
	 * You are too far from the object to gather it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_TOO_FAR_FROM_GATHER_SOURCE = new SM_SYSTEM_MESSAGE(1330002);
	/**
	 * You cannot gather as there are obstacles blocking the way.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_OBSTACLE_EXIST = new SM_SYSTEM_MESSAGE(1330003);

	/**
	 * You have learned the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_LEARNED_NEW_GATHER_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1330004, value0);
	}

	/**
	 * Your %0 skill has been upgraded to %1 points.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SKILL_POINT_UP(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1330005, value0, value1);
	}

	/**
	 * You do not have the basic gathering tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_NO_TOOL_1_BASIC = new SM_SYSTEM_MESSAGE(1330006);
	/**
	 * You do not have the harvesting tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_NO_TOOL_2_GATHER = new SM_SYSTEM_MESSAGE(1330007);
	/**
	 * You do not have the mining tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_NO_TOOL_3_MINING = new SM_SYSTEM_MESSAGE(1330008);
	/**
	 * You do not have the fishing tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_NO_TOOL_4_FISHING = new SM_SYSTEM_MESSAGE(1330009);
	/**
	 * You do not have the forestry tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_NO_TOOL_5_FORESTRY = new SM_SYSTEM_MESSAGE(1330010);

	/**
	 * You are gathering %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_START_1_BASIC(String value0) {
		return new SM_SYSTEM_MESSAGE(1330011, value0);
	}

	/**
	 * You are harvesting %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_START_2_GATHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1330012, value0);
	}

	/**
	 * You are mining %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_START_3_MINING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330013, value0);
	}

	/**
	 * You are fishing %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_START_4_FISHING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330014, value0);
	}

	/**
	 * You are felling %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_START_5_FORESTRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1330015, value0);
	}

	/**
	 * You have gathered %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SUCCESS_1_BASIC(String value0) {
		return new SM_SYSTEM_MESSAGE(1330016, value0);
	}

	/**
	 * You have harvested %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SUCCESS_2_GATHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1330017, value0);
	}

	/**
	 * You have mined %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SUCCESS_3_MINING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330018, value0);
	}

	/**
	 * You have caught %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SUCCESS_4_FISHING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330019, value0);
	}

	/**
	 * You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_SUCCESS_5_FORESTRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1330020, value0);
	}

	/**
	 * You have failed to gather %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_FAIL_1_BASIC(String value0) {
		return new SM_SYSTEM_MESSAGE(1330021, value0);
	}

	/**
	 * You have failed to harvest %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_FAIL_2_GATHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1330022, value0);
	}

	/**
	 * You have failed to mine %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_FAIL_3_MINING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330023, value0);
	}

	/**
	 * You have failed to catch %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_FAIL_4_FISHING(String value0) {
		return new SM_SYSTEM_MESSAGE(1330024, value0);
	}

	/**
	 * You have failed to acquire %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_FAIL_5_FORESTRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1330025, value0);
	}

	/**
	 * You have stopped gathering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANCEL_1_BASIC = new SM_SYSTEM_MESSAGE(1330026);
	/**
	 * You have stopped harvesting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANCEL_2_GATHER = new SM_SYSTEM_MESSAGE(1330027);
	/**
	 * You have stopped mining.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANCEL_3_MINING = new SM_SYSTEM_MESSAGE(1330028);
	/**
	 * You have stopped fishing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANCEL_4_FISHING = new SM_SYSTEM_MESSAGE(1330029);
	/**
	 * You have stopped felling.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANCEL_5_FORESTRY = new SM_SYSTEM_MESSAGE(1330030);
	/**
	 * You must be equipped with the basic gathering tools.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_EQUIP_1_BASIC = new SM_SYSTEM_MESSAGE(1330031);
	/**
	 * You must be equipped with a hoe.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_EQUIP_2_GATHER = new SM_SYSTEM_MESSAGE(1330032);
	/**
	 * You must be equipped with a pick.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_EQUIP_3_MINING = new SM_SYSTEM_MESSAGE(1330033);
	/**
	 * You must be equipped with a fishing rod.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_EQUIP_4_FISHING = new SM_SYSTEM_MESSAGE(1330034);
	/**
	 * You must be equipped with an axe.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_EQUIP_5_FORESTRY = new SM_SYSTEM_MESSAGE(1330035);
	/**
	 * You must have at least one free space in your cube to gather.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1330036);
	/**
	 * You must have at least one free space in your cube to craft.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1330037);
	/**
	 * You cannot craft while in an altered state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_CAN_NOT_COMBINE_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1330038);
	/**
	 * You are already crafting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_ALREADY_COMBINING = new SM_SYSTEM_MESSAGE(1330039);

	/**
	 * You are too far from %0 to craft.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_TOO_FAR_FROM_TOOL(String value0) {
		return new SM_SYSTEM_MESSAGE(1330040, value0);
	}

	/**
	 * You cannot craft as you do not have %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_DO_NOT_HAVE_TOOL(String value0) {
		return new SM_SYSTEM_MESSAGE(1330041, value0);
	}

	/**
	 * You cannot start crafting as you have not learned the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_CANT_USE(String value0) {
		return new SM_SYSTEM_MESSAGE(1330042, value0);
	}

	/**
	 * Cannot find the design.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_CAN_NOT_FIND_RECIPE = new SM_SYSTEM_MESSAGE(1330043);

	/**
	 * Your %0 skill is not good enough yet.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_OUT_OF_SKILL_POINT(String value0) {
		return new SM_SYSTEM_MESSAGE(1330044, value0);
	}

	/**
	 * You cannot craft as you do not have a required item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_NO_COMPONENT_ITEM_IN_RECIPE = new SM_SYSTEM_MESSAGE(1330045);

	/**
	 * You cannot craft as you do not have %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_NO_COMPONENT_ITEM_SINGLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1330046, value0);
	}

	/**
	 * You cannot craft as you do not have %num1 %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_NO_COMPONENT_ITEM_MULTIPLE(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1330047, num1, value0);
	}

	/**
	 * You are crafting %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_START(String value0) {
		return new SM_SYSTEM_MESSAGE(1330048, value0);
	}

	/**
	 * You have crafted %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_SUCCESS(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1330049, descriptionId);
	}

	/**
	 * You have failed to craft %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_FAIL(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1330050, descriptionId);
	}

	/**
	 * You stopped crafting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMBINE_CANCEL = new SM_SYSTEM_MESSAGE(1330051);

	/**
	 * You must have learned the %0 skill to use this tool.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_CANT_USE_TOOL(String value0) {
		return new SM_SYSTEM_MESSAGE(1330052, value0);
	}

	/**
	 * Your %0 skill has been upgraded to %1 points.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_SKILL_POINT_UP(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1330053, value0, value1);
	}

	/**
	 * You must learn the %0 skill to start gathering.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHER_LEARN_SKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1330054, value0);
	}

	/**
	 * You cannot start crafting as there are obstacles blocking the way.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_OBSTACLE_EXIST = new SM_SYSTEM_MESSAGE(1330055);
	/**
	 * You cannot craft while in combat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_DISABLED_IN_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1330056);
	/**
	 * As you cannot craft while in combat mode, it will be closed automatically.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_CLOSED_FOR_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1330057);
	/**
	 * You have gathered successfully.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHERING_SUCCESS_GETEXP = new SM_SYSTEM_MESSAGE(1330058);
	/**
	 * You have crafted successfully.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_SUCCESS_GETEXP = new SM_SYSTEM_MESSAGE(1330059);
	/**
	 * You have already learned this design.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_RECIPE_LEARNED_ALREADY = new SM_SYSTEM_MESSAGE(1330060);

	/**
	 * You have learned %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CRAFT_RECIPE_LEARN(int value0, String name) {
		return new SM_SYSTEM_MESSAGE(1330061, "[recipe_ex:" + value0 + ";" + name + "]");
	}

	/**
	 * You cannot learn the design because you have not learned the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_CRAFT_RECIPE_CANT_LEARN_SKILL(int i) {
		return new SM_SYSTEM_MESSAGE(1330062, new DescriptionId(i));
	}

	/**
	 * You cannot learn the design because your skill level is not high enough.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_RECIPE_CANT_LEARN_SKILLPOINT = new SM_SYSTEM_MESSAGE(1330063);

	/**
	 * Maximum skill level of %0 has been upgraded to Level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_CRAFT_INFO_UPGRADE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1330064, value0, value1);
	}

	/**
	 * Only Daevas can craft it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_MSG_CAN_WORK_ONLY_DEVA = new SM_SYSTEM_MESSAGE(1330065);
	/**
	 * You are a Daeva now. Leave this resource for Humans to use.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_INCORRECT_SKILL = new SM_SYSTEM_MESSAGE(1330066);

	/**
	 * Maximum skill level of %0 has been upgraded to Level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_GATHERING_INFO_UPGRADE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1330067, value0, value1);
	}

	/**
	 * Maximum skill level of %0 has been upgraded to Level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_AERIALGATHERING_INFO_UPGRADE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1330068, value0, value1);
	}

	/**
	 * You cannot be promoted any more.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_COMBINE_CBT_CAP = new SM_SYSTEM_MESSAGE(1330069);
	/**
	 * You cannot be promoted any more.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GATHER_CBT_CAP = new SM_SYSTEM_MESSAGE(1330070);
	/**
	 * You cannot gather while afflicted with an altered state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_EXTRACT_GATHER_WHILE_IN_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1330071);
	/**
	 * You cannot gather while in the current stance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_EXTRACT_GATHER_WHILE_IN_CURRENT_STANCE = new SM_SYSTEM_MESSAGE(1330072);
	/**
	 * You cannot gather while in the current position.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_EXTRACT_GATHER_IN_CURRENT_POSITION = new SM_SYSTEM_MESSAGE(1330073);
	/**
	 * Someone else is gathering that object.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_OCCUPIED_BY_OTHER = new SM_SYSTEM_MESSAGE(1330074);
	/**
	 * You are too far from the target to gather it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_TOO_FAR_FROM_GATHER_SOURCE = new SM_SYSTEM_MESSAGE(1330075);
	/**
	 * You cannot gather because an obstacle is in the way.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_OBSTACLE_EXIST = new SM_SYSTEM_MESSAGE(1330076);

	/**
	 * You have started gathering %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_START_1_BASIC(String value0) {
		return new SM_SYSTEM_MESSAGE(1330077, value0);
	}

	/**
	 * You have gathered %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_SUCCESS_1_BASIC(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1330078, descriptionId);
	}

	/**
	 * You have failed to gather %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_FAIL_1_BASIC(String value0) {
		return new SM_SYSTEM_MESSAGE(1330079, value0);
	}

	/**
	 * You have stopped gathering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_CANCEL_1_BASIC = new SM_SYSTEM_MESSAGE(1330080);
	/**
	 * You cannot gather unless there is at least one free space in your cube.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHER_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1330081);
	/**
	 * You have gained experience from gathering.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHERING_SUCCESS_GETEXP = new SM_SYSTEM_MESSAGE(1330082);
	/**
	 * You cannot use the item until its gathering timer expires.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EXTRACT_GATHERING_CANT_USE_UNTIL_DELAY_TIME = new SM_SYSTEM_MESSAGE(1330083);
	/**
	 * You have died.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_DEATH = new SM_SYSTEM_MESSAGE(1340000);

	/**
	 * You were killed by %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PvPZONE_MY_DEATH_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1340001, value0);
	}

	/**
	 * You were killed by %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_DEATH_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1340002, value0);
	}

	/**
	 * %0 has died.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_FRIENDLY_DEATH(String value0) {
		return new SM_SYSTEM_MESSAGE(1350000, value0);
	}

	/**
	 * %0 was killed by %1's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_FRIENDLY_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1350001, value0, value1);
	}

	/**
	 * %0 has died.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_HOSTILE_DEATH(String value0) {
		return new SM_SYSTEM_MESSAGE(1360000, value0);
	}

	/**
	 * You have defeated %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PvPZONE_HOSTILE_DEATH_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1360001, value0);
	}

	/**
	 * %0 has defeated %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PvPZONE_HOSTILE_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1360002, value0, value1);
	}

	/**
	 * You have defeated %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_HOSTILE_DEATH_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1360003, value0);
	}

	/**
	 * %0 has defeated %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_HOSTILE_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1360004, value0, value1);
	}

	/**
	 * You have gained %num1 XP from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP(String value0, long num1) {
		return new SM_SYSTEM_MESSAGE(1370000, value0, num1);
	}

	public static SM_SYSTEM_MESSAGE STR_GET_EXP_DESC(DescriptionId value0, long num1) {
		return new SM_SYSTEM_MESSAGE(1370000, value0, num1);
	}

	/**
	 * You have earned %0 XP.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_EXP_GAIN(String value0) {
		return new SM_SYSTEM_MESSAGE(1370001, value0);
	}

	/**
	 * You have gained %num0 XP.
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP2(long num0) {
		return new SM_SYSTEM_MESSAGE(1370002, num0);
	}

	/**
	 * %0 has received %num1 XP.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENSLAVE_GIVE_EXP_TO_PET_GET_EXP(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1370003, value0, num1);
	}

	/**
	 * %0 has lost %num1 XP.
	 */
	public static SM_SYSTEM_MESSAGE STR_ENSLAVE_PET_LOSS_EXP(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1370004, value0, num1);
	}

	/**
	 * You distributed %1 Kinah each to %0 members.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_DISTRIBUTE_GOLD(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1380000, value1, value0);
	}

	/**
	 * You have earned %num0 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GETMONEY(int num0) {
		return new SM_SYSTEM_MESSAGE(1380001, num0);
	}

	/**
	 * You received a refund of %num0 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REFUND_MONEY_SYSTEM(int num0) {
		return new SM_SYSTEM_MESSAGE(1380002, num0);
	}

	/**
	 * You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_ITEM1(String value0) {
		return new SM_SYSTEM_MESSAGE(1390000, value0);
	}

	/**
	 * %0 has acquired %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ITEM_WIN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390001, value0, value1);
	}

	/**
	 * %0 has acquired %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE(String value0, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1390002, value0, descriptionId);
	}

	/**
	 * %0 has acquired %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ITEM_WIN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390003, value0, value1);
	}

	/**
	 * You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1390004, value0);
	}

	/**
	 * You have acquired %num1 %0(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_MULTI(int num1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1390005, num1, value0s);
	}

	/**
	 * You cannot close the Craft window while crafting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_CLOSE_MAKING_DIALOG_DURING_COMBINE = new SM_SYSTEM_MESSAGE(1390105);
	/**
	 * You cannot change target while crafting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_SELECT_TARGET_DURING_COMBINE = new SM_SYSTEM_MESSAGE(1390106);
	/**
	 * You cannot open a private store while fighting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DISABLED_IN_EXCHANGE = new SM_SYSTEM_MESSAGE(1390107);
	/**
	 * Group members cannot organize an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_MEMBER_CANT_ORGANIZE_FORCE = new SM_SYSTEM_MESSAGE(1390108);
	/**
	 * You cannot organize an alliance by inviting your own group members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_ORGANIZE_FORCE_INVITED_PARTY_MEMBER = new SM_SYSTEM_MESSAGE(1390109);
	/**
	 * Please select a target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NEED_TARGET = new SM_SYSTEM_MESSAGE(1390110);
	/**
	 * Invalid name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_CORRECT_CHAR_NAME = new SM_SYSTEM_MESSAGE(1390111);
	/**
	 * The character name does not exist. Please check the recipient again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_MSG_RECIPIENT_UNKNOWN = new SM_SYSTEM_MESSAGE(1390112);

	/**
	 * You cannot send a mail to %0 because his/her mailbox is full.
	 */
	public static SM_SYSTEM_MESSAGE STR_MAIL_MSG_RECIPIENT_MAILBOX_FULL(String value0) {
		return new SM_SYSTEM_MESSAGE(1390113, value0);
	}

	/**
	 * %0 is currently refusing the View Detail access.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_WATCH(String value0) {
		return new SM_SYSTEM_MESSAGE(1390114, value0);
	}

	/**
	 * %0 is currently rejecting trade requests.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_TRADE(String value0) {
		return new SM_SYSTEM_MESSAGE(1390115, value0);
	}

	/**
	 * %0 is currently rejecting group invitations.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_INVITE_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1390116, value0);
	}

	/**
	 * %0 is currently rejecting alliance invitations.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_INVITE_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1390117, value0);
	}

	/**
	 * %0 is currently rejecting Legion invitations.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_INVITE_GUILD(String value0) {
		return new SM_SYSTEM_MESSAGE(1390118, value0);
	}

	/**
	 * %0 is not currently accepting friend requests.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_FRIEND(String value0) {
		return new SM_SYSTEM_MESSAGE(1390119, value0);
	}

	/**
	 * %0 is not currently accepting duel requests.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECTED_DUEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1390120, value0);
	}

	/**
	 * You started using the %0 skill.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_TOGGLE_SKILL_TURNED_ON(String value0) {
		return new SM_SYSTEM_MESSAGE(1390121, value0);
	}

	/**
	 * You have entered zone channel %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TELEPORT_ZONECHANNEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1390122, value0);
	}

	/**
	 * A reply to your petition has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PETITION_GOT_MESSAGE = new SM_SYSTEM_MESSAGE(1390123);

	/**
	 * Your Note: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_READ_TODAY_WORDS(String value0) {
		return new SM_SYSTEM_MESSAGE(1390124, value0);
	}

	/**
	 * You did not set Your Note.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOSET_TODAY_WORDS = new SM_SYSTEM_MESSAGE(1390125);
	/**
	 * Your Note has been cleared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CLEAR_TODAY_WORDS = new SM_SYSTEM_MESSAGE(1390126);
	/**
	 * You did not set the Legion Announcement.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOSET_GUILD_NOTICE = new SM_SYSTEM_MESSAGE(1390127);
	/**
	 * Legion Announcement has been cleared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CLEAR_GUILD_NOTICE = new SM_SYSTEM_MESSAGE(1390128);
	/**
	 * You did not set the Self Intro.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOSET_GUILD_MEMBER_INTRO = new SM_SYSTEM_MESSAGE(1390129);
	/**
	 * Your Self Intro has been cleared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CLEAR_GUILD_MEMBER_INTRO = new SM_SYSTEM_MESSAGE(1390130);

	/**
	 * %0 resisted your attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_RESISTED_ME_TO_B(String value0) {
		return new SM_SYSTEM_MESSAGE(1390131, value0);
	}

	/**
	 * You resisted %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_RESISTED_A_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390132, value0);
	}

	/**
	 * %1 resisted %0's attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_RESISTED_A_TO_B(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1390133, value1, value0);
	}

	/**
	 * You changed the connection status to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CONNECTION_STATUS(String value0) {
		return new SM_SYSTEM_MESSAGE(1390134, value0);
	}

	/**
	 * You changed the group to the %0 state.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MY_PARTY_STATE(String value0) {
		return new SM_SYSTEM_MESSAGE(1390135, value0);
	}

	/**
	 * You have no authority to modify the Legion emblem.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_EMBLEM_DONT_HAVE_RIGHT = new SM_SYSTEM_MESSAGE(1390136);
	/**
	 * The Legion emblem has been changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_EMBLEM = new SM_SYSTEM_MESSAGE(1390137);
	/**
	 * Please enter the name of the member to change the rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_NO_NAME = new SM_SYSTEM_MESSAGE(1390138);
	/**
	 * The rank to change is incorrect.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_INCORRECT_RIGHT = new SM_SYSTEM_MESSAGE(1390139);
	/**
	 * You cannot use a Rift while flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_WHILE_FLYING = new SM_SYSTEM_MESSAGE(1390140);

	/**
	 * Your accumulated play time is %0 hour(s) %1 minute(s). Your accumulated rest time is %2 hour(s) %3 minute(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_NORMAL_REMAIN_PLAYTIME(String value0, String value1, String value2, String value3) {
		return new SM_SYSTEM_MESSAGE(1390141, value0, value1, value2, value3);
	}

	/**
	 * Your accumulated rest time is %0 hour(s) %1 minute(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_HEALTH_REMAIN_PLAYTIME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390142, value0, value1);
	}

	/**
	 * You are Tired, and the XP or item rewards gained are reduced to 50% of normal. Please log out and take a break for your health.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TIRED_REMAIN_PLAYTIME = new SM_SYSTEM_MESSAGE(1390143);
	/**
	 * You are Exhausted, and the XP or item rewards gained are reduced to 0%. Please log out and take a break for your health. It will be returned to normal when the accumulated logout time reaches 5
	 * hours.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PENALTY_REMAIN_PLAYTIME = new SM_SYSTEM_MESSAGE(1390144);

	/**
	 * Real Time: %0 %1
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOCAL_TIME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390145, value0, value1);
	}

	/**
	 * Game Time: %0 %1
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GAME_TIME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390146, value0, value1);
	}

	/**
	 * You do not have enough Kinah to pay the fee.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SEND_MAIL_NOT_ENOUGH_FEE = new SM_SYSTEM_MESSAGE(1390147);
	/**
	 * You do not have the authority to use the Alert Chat.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NO_AUTHORITY = new SM_SYSTEM_MESSAGE(1390148);
	/**
	 * You do not have enough space in the inventory.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_FULL_INVENTORY = new SM_SYSTEM_MESSAGE(1390149);
	/**
	 * You cannot use items while crafting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_USE_ITEM_DURING_COMBINE = new SM_SYSTEM_MESSAGE(1390150);
	/**
	 * You cannot use the entrance to the enemy territory.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_TELEPORT_OPPOSITE_RACIAL = new SM_SYSTEM_MESSAGE(1390151);
	/**
	 * You must first complete the Abyss Entry Quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_TELEPORT_TO_ABYSS = new SM_SYSTEM_MESSAGE(1390152);

	/**
	 * The name must be entered in the form of [%0 character name].
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CHAT_CMD_NEED_NAME_FIELD(String value0) {
		return new SM_SYSTEM_MESSAGE(1390153, value0);
	}

	/**
	 * You cannot use the skill in the current form.
	 */
	public static final SM_SYSTEM_MESSAGE STR_SKILL_CAN_NOT_CAST_IN_THIS_FORM = new SM_SYSTEM_MESSAGE(1390154);

	/**
	 * %1 of %0 uses %3 in %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_ABYSS_SKILL_IS_FIRED(Player player, DescriptionId skill) {
		return new SM_SYSTEM_MESSAGE(1390155, player.getRace().getRaceDescriptionId(), player.getName(), "%SubZone:" + player.getPosition().getMapId() + " " + player.getPosition().getX() + " " + player.getPosition().getY() + " " + player.getPosition().getZ(), skill);
	}

	/**
	 * You could not remove the skill effect as your Dispel skill level is too low.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_DISPELLEVEL = new SM_SYSTEM_MESSAGE(1390156);
	/**
	 * You could not remove all the skill effects as you do not have sufficient Dispel skill count.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_DISPELCOUNT = new SM_SYSTEM_MESSAGE(1390157);
	/**
	 * The Kisk you registered as a resurrection bind point has been destroyed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_DESTROYED = new SM_SYSTEM_MESSAGE(1390158);
	/**
	 * You registered the current location as a resurrection bind point.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_REGISTER = new SM_SYSTEM_MESSAGE(1390159);
	/**
	 * You can install only one Kisk at a time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_ALREADY_INSTALLED = new SM_SYSTEM_MESSAGE(1390160);
	/**
	 * You have already bound at this location.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_ALREADY_REGISTERED = new SM_SYSTEM_MESSAGE(1390161);

	/**
	 * You rolled the dice and got %0 (max. %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_RESULT_ME(int value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1390162, value0, num1);
	}

	/**
	 * %0 rolled the dice and got %1 (max. %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_RESULT_OTHER(String value0, int value1, int num2) {
		return new SM_SYSTEM_MESSAGE(1390163, value0, value1, num2);
	}

	/**
	 * You gave up rolling the dice.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DICE_GIVEUP_ME = new SM_SYSTEM_MESSAGE(1390164);

	/**
	 * %0 gave up rolling the dice.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_GIVEUP_OTHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1390165, value0);
	}

	/**
	 * The Kisk you registered is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BINDSTONE_IS_ATTACKED = new SM_SYSTEM_MESSAGE(1390166);
	/**
	 * Items subjected to the group's quality item distribution have been changed to Superior rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_RARE_QUALITY = new SM_SYSTEM_MESSAGE(1390167);
	/**
	 * Items subjected to the alliance's quality item distribution have been changed to Superior rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_RARE_QUALITY = new SM_SYSTEM_MESSAGE(1390168);
	/**
	 * Items subjected to the group's quality item distribution have been changed to Heroic rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_LEGEND_QUALITY = new SM_SYSTEM_MESSAGE(1390169);
	/**
	 * Items subjected to the alliance's quality item distribution have been changed to Heroic rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_LEGEND_QUALITY = new SM_SYSTEM_MESSAGE(1390170);
	/**
	 * Items subjected to the group's quality item distribution have been changed to Fabled rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_UNIQUE_QUALITY = new SM_SYSTEM_MESSAGE(1390171);
	/**
	 * Items subjected to the alliance's quality item distribution have been changed to Fabled rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_UNIQUE_QUALITY = new SM_SYSTEM_MESSAGE(1390172);
	/**
	 * You cannot add any more on the quality item distribution list. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_LIMIT_NUMBER = new SM_SYSTEM_MESSAGE(1390173);
	/**
	 * The group's quality item distribution rules have been changed to Normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_DEFAULT = new SM_SYSTEM_MESSAGE(1390174);
	/**
	 * The alliance's quality item distribution rules have been changed to Normal.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_DEFAULT = new SM_SYSTEM_MESSAGE(1390175);
	/**
	 * The group's quality item distribution rules have been changed to Dice Roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_DICE = new SM_SYSTEM_MESSAGE(1390176);
	/**
	 * The alliance's quality item distribution rules have been changed to Dice Roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_DICE = new SM_SYSTEM_MESSAGE(1390177);
	/**
	 * The group's quality item distribution rules have been changed to Bidding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_PAY = new SM_SYSTEM_MESSAGE(1390178);
	/**
	 * The alliance's quality item distribution rules have been changed to Bidding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_PAY = new SM_SYSTEM_MESSAGE(1390179);

	/**
	 * You are now the owner of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_GET_ITEM_ME(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1390180, descriptionId);
	}

	/**
	 * %0 is now the owner of %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_GET_ITEM_OTHER(String value0, DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1390181, value0, descriptionId);
	}

	/**
	 * You cannot acquire the item because there is no space in the inventory.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DICE_INVEN_ERROR = new SM_SYSTEM_MESSAGE(1390182);
	/**
	 * The account was instantly settled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PAY_RESULT_ME = new SM_SYSTEM_MESSAGE(1390183);

	/**
	 * %0 settled the account instantly.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PAY_RESULT_OTHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1390184, value0);
	}

	/**
	 * Your bid was successful and %num0 Kinah has been deducted.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PAY_ACCOUNT_ME(long highestValue) {
		return new SM_SYSTEM_MESSAGE(1390185, highestValue);
	}

	/**
	 * It was won by %0 for %num1 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PAY_ACCOUNT_OTHER(String value0, long highestValue) {
		return new SM_SYSTEM_MESSAGE(1390186, value0, highestValue);
	}

	/**
	 * %num0 Kinah is distributed %num2 Kinah each to %1 members.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PAY_DISTRIBUTE(long highestValue, int num2, long distributeKinah) {
		return new SM_SYSTEM_MESSAGE(1390187, highestValue, num2, distributeKinah);
	}

	/**
	 * You pause %0 temporarily.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_PAUSE_START_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390188, value0);
	}

	/**
	 * %0 pauses %1 temporarily.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_PAUSE_START_OTHER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390189, value0, value1);
	}

	/**
	 * %0 ended the pause state of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_PAUSE_END_ME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390190, value0, value1);
	}

	/**
	 * %0 unpauses %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_PAUSE_END_OTHER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390191, value0, value1);
	}

	/**
	 * The distribution resumes as the pause time is over.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOT_PAUSE_CALCEL = new SM_SYSTEM_MESSAGE(1390192);

	/**
	 * You rolled the dice and got a %0 (1~%1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_RESULT_EX_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390193, value0);
	}

	/**
	 * %0's rolled the dice and got a %1 (1~%2).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_RESULT_EX_OTHER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390194, value0, value1);
	}

	/**
	 * %1 of the %0 killed the Guardian General.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_BOSS_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1390195, value1, value0);
	}

	/**
	 * %1 of the %0 has destroyed the Balaur Battleship Dredgion.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_CARRIER_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1390196, value1, value0);
	}

	/**
	 * %0 seconds remain until you can cast it again.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_INITIAL_TIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390197, value0);
	}

	/**
	 * You can use it only after the cooldown time is over.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ARTIFACT_COOL_TIME = new SM_SYSTEM_MESSAGE(1390198);
	/**
	 * The Balaur have killed the Guardian General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DRAGON_BOSS_KILLED = new SM_SYSTEM_MESSAGE(1390199);
	/**
	 * The Balaur have destroyed the Castle Gate.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DRAGON_DOOR_BROKEN = new SM_SYSTEM_MESSAGE(1390200);
	/**
	 * The Balaur have destroyed the Gate Guardian Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DRAGON_REPAIR_BROKEN = new SM_SYSTEM_MESSAGE(1390201);
	/**
	 * The Balaur have killed the Aetheric Field Generator.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_DRAGON_SHIELD_BROKEN = new SM_SYSTEM_MESSAGE(1390202);

	/**
	 * %0 captured the %1 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_EVENT_WIN_ARTIFACT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390203, value0, value1);
	}

	/**
	 * %0 lost the %1 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_EVENT_LOSE_ARTIFACT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390204, value0, value1);
	}

	/**
	 * The dredgion has vanished.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ABYSS_CARRIER_DESPAWN = new SM_SYSTEM_MESSAGE(1390205);

	/**
	 * %1 of the %0 has destroyed the Balaur Battleship Dredgion.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_GUILD_CARRIER_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1390206, value1, value0);
	}

	/**
	 * You have captured the %0 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_WIN_FORT_TO_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390207, value0);
	}

	/**
	 * %1 of %0 has captured the %2 Artifact.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_WIN_FORT(String value1, String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1390208, value1, value0, value2);
	}

	/**
	 * The %0 Artifact has been lost to %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_ARTIFACT_LOSE_FORT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390209, value0, value1);
	}

	/**
	 * Starts the auto-distribution of miscellaneous items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_JUNK_DISTRIBUTE_ON = new SM_SYSTEM_MESSAGE(1390210);
	/**
	 * Ends the auto-distribution of miscellaneous items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_JUNK_DISTRIBUTE_OF = new SM_SYSTEM_MESSAGE(1390211);
	/**
	 * You cannot disband your Legion while you have items or money left in the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_DISPERSE_CANT_DISPERSE_GUILD_STORE_ITEM_IN_WAREHOUSE = new SM_SYSTEM_MESSAGE(1390212);

	/**
	 * Playing Time: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PLAYING_TIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390213, value0);
	}

	/**
	 * You have played for %0 hour(s). Please take a break.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_NOTIFY_PLAYING_TIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1390214, value0);
	}

	/**
	 * You have joined the %0 Legion.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_INVITE_I_JOINED(String value0) {
		return new SM_SYSTEM_MESSAGE(1390215, value0);
	}

	/**
	 * You recovered %num0 HP.
	 */
	public static SM_SYSTEM_MESSAGE _STR_MSG_Heal_TO_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1390216, num0);
	}

	/**
	 * You can only buy one %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CAN_BUY_ONLY_ONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1390217, value0);
	}

	/**
	 * Registering %0 on the quality item distribution list.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_LISTING_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1390218, value0);
	}

	/**
	 * %0 is one of the quality items waiting to be distributed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LOOT_ALREADY_DISTRIBUTING_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1390219, descriptionId);
	}

	/**
	 * You do not have the ownership of this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOT_ANOTHER_OWNER_ITEM = new SM_SYSTEM_MESSAGE(1390220);

	/**
	 * The skill level for the %0 skill does not increase as the difficulty is too low.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_GET_PRODUCTION_EXP(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1390221, descriptionId);
	}

	/**
	 * Items subjected to the group's quality item distribution have been changed to Common rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_TO_COMMON_QUALITY = new SM_SYSTEM_MESSAGE(1390222);
	/**
	 * Items subjected to the alliance's quality item distribution have been changed to Common rank or above.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_TO_COMMON_QUALITY = new SM_SYSTEM_MESSAGE(1390223);

	/**
	 * You have sent a friend request to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDY_REQUEST_ADD(String value0) {
		return new SM_SYSTEM_MESSAGE(1390224, value0);
	}

	/**
	 * You cannot connect to the game during the character reservation period.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_CONNECTION_FAIL_BY_CHAR_RES = new SM_SYSTEM_MESSAGE(1390225);
	/**
	 * Everyone gave up the Dice Roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DICE_ALL_GIVEUP = new SM_SYSTEM_MESSAGE(1390226);
	/**
	 * Everyone gave up the Bidding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PAY_ALL_GIVEUP = new SM_SYSTEM_MESSAGE(1390227);
	/**
	 * You gave up the Bidding.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PAY_GIVEUP_ME = new SM_SYSTEM_MESSAGE(1390228);

	/**
	 * %0 gave up the Bidding.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PAY_GIVEUP_OTHER(String value0) {
		return new SM_SYSTEM_MESSAGE(1390229, value0);
	}

	/**
	 * You cannot use this function in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DISABLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1390230, value0);
	}

	/**
	 * The registered Kisk can resurrect %num0 times more.
	 */
	public static SM_SYSTEM_MESSAGE STR_BINDSTONE_CAPACITY_LIMITTED_ALARM(int num0) {
		return new SM_SYSTEM_MESSAGE(1390231, num0);
	}

	/**
	 * Your Abyss Rank has changed to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_CHANGE_RANK(String value0) {
		return new SM_SYSTEM_MESSAGE(1390232, value0);
	}

	/**
	 * You cannot be promoted as your skill level is too low.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_RANK_UP = new SM_SYSTEM_MESSAGE(1390233);
	/**
	 * An Expert cannot take on a Work Order.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_GET_COMBINETASK = new SM_SYSTEM_MESSAGE(1390234);

	/**
	 * Your Abyss Rank has been changed to %0. Check the changed ranking on the Abyss Ranking Window.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_CHANGE_RANK_THIS_WEEK(String value0) {
		return new SM_SYSTEM_MESSAGE(1390235, value0);
	}

	/**
	 * You have learned the skill, %0 (Level - %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_SKILL_LEARNED_ABYSS_SKILL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390236, value0, value1);
	}

	/**
	 * Only available to alliances.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_SPLIT_FORCE = new SM_SYSTEM_MESSAGE(1390237);
	/**
	 * Please enter the amount of Kinah to distribute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ENTER_SPLIT_GOLD = new SM_SYSTEM_MESSAGE(1390238);
	/**
	 * You give up the Bidding as you do not have enough Kinah.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PAY_NOT_ENOUGH_MONEY = new SM_SYSTEM_MESSAGE(1390239);
	/**
	 * You cannot join the Legion as the player who invited you is no longer a member of the Legion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_INVITE_CAN_NOT_JOIN_TO_GUILD_INVITOR_IS_LEFT = new SM_SYSTEM_MESSAGE(1390240);
	/**
	 * You cannot kick out a Legion member of equal or higher rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_BANISH_CAN_NOT_BANISH_SAME_MEMBER_RANK = new SM_SYSTEM_MESSAGE(1390241);

	/**
	 * You have acquired the %0 title.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_CASH_TITLE(int value0) {
		return new SM_SYSTEM_MESSAGE(1390242, new DescriptionId(value0));
	}

	/**
	 * You have acquired the %0 emote.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_CASH_SOCIALACTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1390243, value0);
	}

	/**
	 * The usage time of %0 title has expired.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_TITLE_BY_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1390244, value0);
	}

	/**
	 * Usage time for the %0 emote has expired.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_SOCIALACTION_BY_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1390245, value0);
	}

	/**
	 * Usage time for the [Lodas Amulet] Bonus 20%% XP has expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_XPBOOST_BY_TIMEOUT = new SM_SYSTEM_MESSAGE(1390246);

	/**
	 * You distributed %num0 Kinah to %num1 people, giving each %num2 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SPLIT_ME_TO_B(int num0, int num1, int num2) {
		return new SM_SYSTEM_MESSAGE(1390247, num0, num1, num2);
	}

	/**
	 * %0 distributed %num1 Kinah among %num2 people, giving %num3 Kinah each.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SPLIT_B_TO_ME(String value0, int num1, int num2, int num3) {
		return new SM_SYSTEM_MESSAGE(1390248, value0, num1, num2, num3);
	}

	/**
	 * The search found %num0 characters (max. 110).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_WHO_DIALOG_RESULT(int num0) {
		return new SM_SYSTEM_MESSAGE(1390249, num0);
	}

	/**
	 * Group loot policy is now %0. %1 items will be distributed by %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PARTY_LOOTING_CHANGED_RULE(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1390250, value0, value1, value2);
	}

	/**
	 * Alliance loot policy is now %0. %1 items will be distributed by %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_LOOTING_CHANGED_RULE(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1390251, value0, value1, value2);
	}

	/**
	 * You cannot be promoted anymore as you are an Expert.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_RANK_UP_MASTER = new SM_SYSTEM_MESSAGE(1390252);
	/**
	 * You cannot be promoted anymore as you are at the highest rank.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_RANK_UP_GATHERING = new SM_SYSTEM_MESSAGE(1390253);
	/**
	 * You have not acquired this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_OPEN_QUEST_LINK = new SM_SYSTEM_MESSAGE(1390254);
	/**
	 * Your skill level does not increase with low level crafting as you are an Expert.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_GET_COMBINE_EXP = new SM_SYSTEM_MESSAGE(1390255);
	/**
	 * This area is only accessible to groups.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ENTER_ONLY_PARTY_DON = new SM_SYSTEM_MESSAGE(1390256);
	/**
	 * You do not have enough Medals.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_MEDAL = new SM_SYSTEM_MESSAGE(1390257);

	/**
	 * A report for the character %0 has been received. You have %1 auto hunting reports remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_SUBMIT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1390258, value0, value1);
	}

	/**
	 * Only % minutes have passes since the last report.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANNOT_SUBMIT(String value) {
		return new SM_SYSTEM_MESSAGE(1390259, value);
	}

	/**
	 * There is a charged item issued to the account. Do you want %0 to have the charged item?
	 */
	public static SM_SYSTEM_MESSAGE STR_LOGIN_WARNING_GET_ITEM1(String value0) {
		return new SM_SYSTEM_MESSAGE(1390260, value0);
	}

	/**
	 * Once the item is given, it cannot be used by other characters. Are you sure you want to keep it in %0?
	 */
	public static SM_SYSTEM_MESSAGE STR_LOGIN_WARNING_GET_ITEM2(String value0) {
		return new SM_SYSTEM_MESSAGE(1390261, value0);
	}

	/**
	 * You have joined the group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ENTERED_PARTY = new SM_SYSTEM_MESSAGE(1390262);
	/**
	 * You have joined the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_ENTERED_FORCE = new SM_SYSTEM_MESSAGE(1390263);
	/**
	 * Please complete your current quest first.
	 */
	public static final SM_SYSTEM_MESSAGE STR_QUEST_ANOTHER_SINGLE_STEP_NOT_COMPLETED = new SM_SYSTEM_MESSAGE(1390264);
	/**
	 * You cannot join once it has started.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_LOCKED = new SM_SYSTEM_MESSAGE(1390265);

	/**
	 * %0 rolled the highest (%0 rolled %1, while you rolled %2).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE(String value0, String value3, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1390266, value0, value3, value1, value2);
	}

	/**
	 * %0 rolled the highest (%0 rolled %1, while you passed).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE_GIVEUP_ROLL(String value0, String value2, String value1) {
		return new SM_SYSTEM_MESSAGE(1390267, value0, value2, value1);
	}

	/**
	 * You rolled the highest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE_WIN = new SM_SYSTEM_MESSAGE(1390268);
	/**
	 * Everyone passed on rolling the dice.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE_GIVEUP_ROLL_ALL = new SM_SYSTEM_MESSAGE(1390269);

	/**
	 * A now-disconnected player rolled the highest (they winner rolled %1, while you rolled %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE_OFFLINE_WINNER(String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1390270, value1, value2);
	}

	/**
	 * A now-disconnected player rolled the highest (they winner rolled %1, while you passed).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ITEM_PARTYNOTICE_DICE_GIVEUP_ROLL_OFFLINE_WINNER(String value1) {
		return new SM_SYSTEM_MESSAGE(1390271, value1);
	}

	/**
	 * The selected Instanced Zone's cooldown time can't be reset.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_COOL_TIME_INIT = new SM_SYSTEM_MESSAGE(1390272);

	/**
	 * %0 Shouts:
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_SHOUT_OUTPUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400000, value0);
	}

	/**
	 * %0 is asking for help from %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_SHOUT_OUTPUT1(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400001, value0, value1);
	}

	/**
	 * %0 Shouts:
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_SHOUT_OUTPUT_NPC(String value0) {
		return new SM_SYSTEM_MESSAGE(1400002, value0);
	}

	/**
	 * %0 is asking for help from %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_SHOUT_OUTPUT1_NPC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400003, value0, value1);
	}

	/**
	 * You shout "%0".
	 */
	public static final SM_SYSTEM_MESSAGE STR_CMD_SHOUT_INPUT = new SM_SYSTEM_MESSAGE(1400004);
	/**
	 * You shout for help.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CMD_SHOUT_INPUT1 = new SM_SYSTEM_MESSAGE(1400005);

	/**
	 * %0 Whispers:
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_WHISHPER_OUTPUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400006, value0);
	}

	/**
	 * %0 Whispers:
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_WHISHPER_OUTPUT_NPC(String value0) {
		return new SM_SYSTEM_MESSAGE(1400007, value0);
	}

	/**
	 * You Whisper to %1: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_WHISHPER_INPUT(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400008, value1, value0);
	}

	/**
	 * %0 has joined your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_HE_ENTERED_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400009, value0);
	}

	/**
	 * Your group has joined %0's alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ENTER_WITH_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400010, value0);
	}

	/**
	 * %0 has joined the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ENTER_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400011, value0);
	}

	/**
	 * %0's group has joined the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ENTER_WITH_HIS_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400012, value0);
	}

	/**
	 * %0 has joined the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_HE_ENTERED_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400013, value0);
	}

	/**
	 * %0's group has joined the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HIS_PARTY_ENTERED_ALLIANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400014, value0);
	}

	/**
	 * Your group has joined the alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_MY_PARTY_ENTERED_ALLIANCE = new SM_SYSTEM_MESSAGE(1400015);
	/**
	 * You have joined a group belonging to an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_ENTERY_PARTY_AND_ALLIANCE = new SM_SYSTEM_MESSAGE(1400016);

	/**
	 * %0 has joined the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_HE_ENTERED_ALLIANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400017, value0);
	}

	/**
	 * You have joined %0's allliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_FORCE_ENTER_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1400018, value0);
	}

	/**
	 * Legion Message: %0 %DATETIME1
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_NOTICE(String value0, long i) {
		return new SM_SYSTEM_MESSAGE(1400019, value0, i, 2);
	}

	/**
	 * Please report after you select a character from the same race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DO_NOT_ACCUSE = new SM_SYSTEM_MESSAGE(1400020);

	/**
	 * %0 killed the Guardian General.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_NPC_BOSS_KILLED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400021, value0);
	}

	/**
	 * You cannot use a Rift.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL = new SM_SYSTEM_MESSAGE(1400022);

	/**
	 * %0 %1 %2 has died in %3.
	 */
	public static SM_SYSTEM_MESSAGE STR_ABYSS_ORDER_RANKER_DIE(Player victim, DescriptionId rankDescId) {
		return new SM_SYSTEM_MESSAGE(1400023, victim.getRace().getRaceDescriptionId(), rankDescId, victim.getName(), "%SubZone:" + victim.getPosition().getMapId() + " " + victim.getPosition().getX() + " " + victim.getPosition().getY() + " " + victim.getPosition().getZ());
	}

	/**
	 * You cannot continue the battle as you have insufficient Abyss Points. You will be resurrected at %1 if nothing is entered within %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_RESURRECT_HERE_BY_ABYSS_POINT_ZERO(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400024, value1, value0);
	}

	/**
	 * %0 has used the Gate Guardian Stone to repair the castle gate by %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REPAIR_ABYSS_DOOR(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400025, value0, value1);
	}

	/**
	 * You have obtained %0 from the Internet Cafe Event.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_PCBANG_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400026, value0);
	}

	/**
	 * %WORLDNAME0% region restricts access. You cannot reenter the region for %1 hour(s) if all your group members left the region or if you left the current group.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_COOL_TIME_HOUR(String worldname0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400027, worldname0, value1);
	}

	/**
	 * %WORLDNAME0% region restricts access. You cannot reenter the region for %1 minute(s) if all your group members left the region or if you left the current group.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_COOL_TIME_MIN(String worldname0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400028, worldname0, value1);
	}

	/**
	 * You may enter %WORLDNAME0 again after %1 hour(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR(String worldname0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400029, worldname0, value1);
	}

	/**
	 * You may enter %WORLDNAME0 again after %1 minute(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN(String worldname0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400030, worldname0, value1);
	}

	/**
	 * You can enter %0 area now.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CAN_ENTER_INSTANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400031, value0);
	}

	/**
	 * %0: %1(%2)
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CHECK_INSTANCE_COOL_TIME() {
		return new SM_SYSTEM_MESSAGE(1400032);
	}

	/**
	 * Changing Game Preferences to Pseudo Full Screen mode for convenient access of the website.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TEMP_PSEUDO_FULLSCREEN = new SM_SYSTEM_MESSAGE(1400033);

	/**
	 * Usage time for %0 has expired.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_ITEM_BY_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400034, value0);
	}

	/**
	 * You cannot gain any Abyss Points for a while as you have gained too many Abyss Points in too short a period of time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GET_AP_TIMEBASE_LIMIT = new SM_SYSTEM_MESSAGE(1400035);

	/**
	 * Your trial has ended. %1, We hope you've enjoyed playing Aion! To continue to play, purchase Aion. Go to AionOnline.com to buy now!
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LEVEL_LIMIT_FREE_TIME(String value1) {
		return new SM_SYSTEM_MESSAGE(1400036, value1);
	}

	/**
	 * You may enter %0 again after %1 hour(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR_CLIENT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400037, value0, value1);
	}

	/**
	 * You may enter %0 again after %1 minute(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN_CLIENT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400038, value0, value1);
	}

	/**
	 * The Macro has been registered.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_REGIST = new SM_SYSTEM_MESSAGE(1400039);
	/**
	 * You cannot register any more Macro.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MACRO_MSG_CANNOT_REGIST = new SM_SYSTEM_MESSAGE(1400040);
	/**
	 * You cannot get any Abyss Point from the current target for a while.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GET_AP_TARGET_LIMIT = new SM_SYSTEM_MESSAGE(1400041);
	/**
	 * As you are not currently a member of the group for the Instanced Zone, you will be leaving the zone shortly.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEAVE_INSTANCE_NOT_PARTY = new SM_SYSTEM_MESSAGE(1400042);
	/**
	 * The zone has been reset. Once reset, you cannot enter the zone again until the reentry time expires. You can check the reentry time by typing '/CheckEntry'.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME = new SM_SYSTEM_MESSAGE(1400043);

	/**
	 * You have exited the Instanced Zone. This zone will be reset in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LEAVE_INSTANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400044, value0);
	}

	/**
	 * You have exited the Instanced Zone. This zone will be reset in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LEAVE_INSTANCE_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400045, value0);
	}

	/**
	 * You have exited the Instanced Zone. This zone will be reset in %0 minutes.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LEAVE_INSTANCE_FORCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400046, value0);
	}

	/**
	 * This account has been suspended for not paying the Internet Cafe usage charge.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BLOCK_PC_ROOM_COMPLAIN = new SM_SYSTEM_MESSAGE(1400047);

	/**
	 * The playing time remaining is %*0, and there are %1 items of pre-paid credits left.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_PLAYTIME_WITH_RESERVATION(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400048, value0, value1);
	}

	/**
	 * The playing time will expire in %*0. If you wish to continue using the service, please make additional payments on the Billing Page of the Plaync website.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ALARM_REMAIN_PLAYTIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400049, value0);
	}

	/**
	 * You have %*0 playing time remaining. Pre-paid credit will be applied afterward.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ALARM_REMAIN_PLAYTIME_WITH_RESERVATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400050, value0);
	}

	/**
	 * The playing time has expired, and the game will end automatically in %*0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COUNT_REMAIN_PLAYTIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400051, value0);
	}

	/**
	 * The playing time will expire in %*0. Pre-paid credit will be applied after it expires.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COUNT_REMAIN_PLAYTIME_WITH_RESERVATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400052, value0);
	}

	/**
	 * standing
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_STANDING = new SM_SYSTEM_MESSAGE(1400053);
	/**
	 * flying
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_PATH_FLYING = new SM_SYSTEM_MESSAGE(1400054);
	/**
	 * flying
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_FREE_FLYING = new SM_SYSTEM_MESSAGE(1400055);
	/**
	 * riding
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_RIDING = new SM_SYSTEM_MESSAGE(1400056);
	/**
	 * resting
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_SITTING = new SM_SYSTEM_MESSAGE(1400057);
	/**
	 * sitting
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_SITTING_ON_CHAIR = new SM_SYSTEM_MESSAGE(1400058);
	/**
	 * dead
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_DEAD = new SM_SYSTEM_MESSAGE(1400059);
	/**
	 * dead
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_FLY_DEAD = new SM_SYSTEM_MESSAGE(1400060);
	/**
	 * running a Private Store
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_PERSONAL_SHOP = new SM_SYSTEM_MESSAGE(1400061);
	/**
	 * looting
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_LOOTING = new SM_SYSTEM_MESSAGE(1400062);
	/**
	 * looting
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_FLY_LOOTING = new SM_SYSTEM_MESSAGE(1400063);
	/**
	 * in your current status
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACT_STATE_DEFAULT = new SM_SYSTEM_MESSAGE(1400064);
	/**
	 * You cannot register items of other races.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_OTHER_RACE = new SM_SYSTEM_MESSAGE(1400065);
	/**
	 * This account has been reported for not paying an internet caf? usage charge. If you believe this is an error, please contact customer support.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BLOCK_PC_ROOM_COMPLAIN2 = new SM_SYSTEM_MESSAGE(1400066);
	/**
	 * The Stigma is already equipped.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_STIGMA_ALREADY_EQUIP_STONE = new SM_SYSTEM_MESSAGE(1400067);

	/**
	 * You must wait %DURATIONTIME0 to use the channel change function. Time Remaining: %DURATIONTIME1
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_CHANGE_CHANNEL_COOLTIME(String durationtime0, String durationtime1) {
		return new SM_SYSTEM_MESSAGE(1400068, durationtime0, durationtime1);
	}

	/**
	 * You entered into the Phase %num0 Restriction state because the auto hunting reports have accumulated. You can check the Restriction Phase and the Release Time by typing the '/Restriction'
	 * command.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_UPGRADE_LEVEL(int num0) {
		return new SM_SYSTEM_MESSAGE(1400069, num0);
	}

	/**
	 * Your restriction phase has been lowered to %num0 as you played fair for a certain period of time. Please continue to play the game in a proper manner.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_DEGRADE_LEVEL(int num0) {
		return new SM_SYSTEM_MESSAGE(1400070, num0);
	}

	/**
	 * You are currently at Phase 1 Restriction State, and will be released in %0 minutes. While not affecting your game play in anyway, a continued accumulation of reports will however raise the
	 * Restriction Phase and will limit your gaining of XP and items.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_INFO_1_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400071, value0);
	}

	/**
	 * You are currently in Phase 2 Restriction State, and will be downgraded to Phase 1 in %0 minutes. You now receive less XP, Kinah and Abyss Points, and the chance of successful gathering and
	 * extraction has been decreased. You will face greater restrictions if reports continue to accumulate.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_INFO_2_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400072, value0);
	}

	/**
	 * You are currently in Phase 3 Restriction State and will be downgraded to Phase 2 in %0 minutes. You cannot acquire any loot, and you now receive less XP, Kinah and Abyss Points, and the chance
	 * of successful gathering and extraction has been significantly decreased. You are banned from joining a Group or Alliance. You will face greater restrictions if reports continue to accumulate.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_INFO_3_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400073, value0);
	}

	/**
	 * You are currently at Phase 4 Restriction State and will be downgraded to Phase 3 in %0 minutes. You cannot acquire any loot, XP, Kinah, or Abyss Points, and are unable to gather or extract any
	 * items. You are also banned from joining a Group or Alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_INFO_4_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400074, value0);
	}

	/**
	 * You have consumed all equipped Power Shards.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WEAPON_BOOST_MODE_BURN_OUT = new SM_SYSTEM_MESSAGE(1400075);
	/**
	 * You are in normal state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_INFO_NORMAL = new SM_SYSTEM_MESSAGE(1400076);
	/**
	 * You cannot delete the letter because items or Kinah are attached.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MAIL_ITEM_DEL_DENIED = new SM_SYSTEM_MESSAGE(1400077);
	/**
	 * You cannot open a private store while trading.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_OPEN_STORE_DURING_CRAFTING = new SM_SYSTEM_MESSAGE(1400078);
	/**
	 * in combat
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASF_COMBAT = new SM_SYSTEM_MESSAGE(1400079);
	/**
	 * moving
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASF_MOVE_TYPE_WALK = new SM_SYSTEM_MESSAGE(1400080);
	/**
	 * using a skill
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASF_CASTING_SKILL = new SM_SYSTEM_MESSAGE(1400081);
	/**
	 * gliding
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ASF_GLIDE = new SM_SYSTEM_MESSAGE(1400082);
	/**
	 * You returned to the normal state as you played fair for a certain period of time. Please continue to play the game in a proper manner.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_DEGRADE_NORMAL_LEVEL = new SM_SYSTEM_MESSAGE(1400083);

	/**
	 * %0 is not an appearance-modified item.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_NOT_SKIN_CHANGED_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400084, value0);
	}

	/**
	 * You cannot send auto hunting reports right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ACCUSE = new SM_SYSTEM_MESSAGE(1400085);
	/**
	 * You cannot report auto hunting in the current region.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ACCUSE_CITY = new SM_SYSTEM_MESSAGE(1400086);

	/**
	 * You cannot issue commands in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SUMMON_CANT_ORDER_BY_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400087, value0);
	}

	/**
	 * You have already learned this emote.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SOCIALACTION_ALREADY_HAS_SKILL = new SM_SYSTEM_MESSAGE(1400088);
	/**
	 * This item has not been appearance modified.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHANGE_ITEM_SKIN_CANNOT_INVALID_ITEM = new SM_SYSTEM_MESSAGE(1400089);
	/**
	 * You cannot report as you have exceeded the number of auto hunting reports allowed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANT_SUBMIT_BY_NO_COUNT = new SM_SYSTEM_MESSAGE(1400090);

	/**
	 * You currently have %0 auto hunting reports left.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_COUNT_INFO(String value0) {
		return new SM_SYSTEM_MESSAGE(1400091, value0);
	}

	/**
	 * The selected user cannot be invited to a group or a force.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANT_BE_INVITED = new SM_SYSTEM_MESSAGE(1400092);
	/**
	 * You have been reported too many times, and cannot issue an invitation.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANT_INVITE_OTHER = new SM_SYSTEM_MESSAGE(1400093);
	/**
	 * You cannot join the group as you have been reported too many times for auto hunting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANT_JOIN_PARTY = new SM_SYSTEM_MESSAGE(1400094);
	/**
	 * You cannot join the Alliance as you have been reported too many times for auto hunting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_CANT_JOIN_FORCE = new SM_SYSTEM_MESSAGE(1400095);
	/**
	 * You cannot use the manastone on the selected item as the manastone level is too high.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GIVE_ITEM_OPTION_CANT_FOR_TOO_HIGH_LEVEL = new SM_SYSTEM_MESSAGE(1400096);

	/**
	 * %0 cannot be summoned right now.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_CANNOT_ACCEPT_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400097, value0);
	}

	/**
	 * Summoning of %0 is cancelled as the confirmation stand-by time has been exceeded.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_DONOT_ACCEPT_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400098, value0);
	}

	/**
	 * You declined %0's summoning.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_Reject_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400099, value0);
	}

	/**
	 * %0 declined your summoning.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_Rejected_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400100, value0);
	}

	/**
	 * Summoning of %0 is cancelled.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_CANCEL_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400101, value0);
	}

	/**
	 * You cannot summon %0 as you are already under the same effect.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_Recall_DUPLICATE_EFFECT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400102, value0);
	}

	/**
	 * %0 is currently unable to join a group or a force.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_OTHER_IS_BANISHED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400103, value0);
	}

	/**
	 * The gift has been delivered successfully.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_GIFT_SUCCESS = new SM_SYSTEM_MESSAGE(1400104);
	/**
	 * You have failed to purchase the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_ERROR = new SM_SYSTEM_MESSAGE(1400105);
	/**
	 * You have chosen an invalid target to give the gift.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_NO_USER_TO_GIFT = new SM_SYSTEM_MESSAGE(1400106);
	/**
	 * The item is not on the list.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_INVALID_GOODS = new SM_SYSTEM_MESSAGE(1400107);
	/**
	 * You do not have enough Cash Points.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_NOT_ENOUGH_POINT = new SM_SYSTEM_MESSAGE(1400108);
	/**
	 * Your race cannot purchase the selected item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_INVALID_RACE = new SM_SYSTEM_MESSAGE(1400109);
	/**
	 * Your gender cannot purchase the selected item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_INVALID_GENDER = new SM_SYSTEM_MESSAGE(1400110);
	/**
	 * Your Class cannot purchase the selected item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_INVALID_CLASS = new SM_SYSTEM_MESSAGE(1400111);
	/**
	 * You already have the selected title.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_DUPLICATED_TITLE = new SM_SYSTEM_MESSAGE(1400112);
	/**
	 * You already have the selected emote.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_DUPLICATED_SOCIAL = new SM_SYSTEM_MESSAGE(1400113);
	/**
	 * You have purchased the cube expansion item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_DUPLICATED_CUBE = new SM_SYSTEM_MESSAGE(1400114);

	/**
	 * You cannot register as you are not %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_BINDSTONE_CANNOT_FOR_INVALID_RIGHT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400115, value0);
	}

	/**
	 * You cannot give gifts to yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_CANNOT_GIVE_TO_ME = new SM_SYSTEM_MESSAGE(1400116);

	/**
	 * You cannot send the letter to %0 because you have been blocked by the player.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MAIL_CANT_FOR_YOU_EXCLUDED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400117, value0);
	}

	/**
	 * Network Status: %0 ms
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PING_RESULT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400118, value0);
	}

	/**
	 * You cannot remove a registered item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EXCHANGE_CANNOT_UNREGISTER_ITEM = new SM_SYSTEM_MESSAGE(1400119);
	/**
	 * You cannot register an amount of Kinah that is lower than the registered amount.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EXCHANGE_CANNOT_DECREASE_MONEY = new SM_SYSTEM_MESSAGE(1400120);
	/**
	 * The client's regional code is not compatible with the game server.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ERROR_WORLD_LOCAL_CODE_FAIL = new SM_SYSTEM_MESSAGE(1400121);
	/**
	 * You cannot get any PVP XP from the current target for a while.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GET_PVP_EXP_TARGET_LIMIT = new SM_SYSTEM_MESSAGE(1400122);
	/**
	 * You cannot get any PVP XP for a while as you have gained too many PVP XP in too short a period of time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GET_PVP_EXP_TIMEBASE_LIMIT = new SM_SYSTEM_MESSAGE(1400123);
	/**
	 * You cannot register the target as your Friend as you have been blocked by the player.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_CANNOT_BLOCK_ME = new SM_SYSTEM_MESSAGE(1400124);

	/**
	 * You rolled the dice and got a %num0 (max. %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ITEM_DICE_CUSTOM_ME(int num0, int num1) {
		return new SM_SYSTEM_MESSAGE(1400125, num0, num1);
	}

	/**
	 * You rolled the dice and got a %num0 (max. %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_CUSTOM_ME(int num0, int num1) {
		return new SM_SYSTEM_MESSAGE(1400126, num0, num1);
	}

	/**
	 * %0 rolled the dice and got a %num1 (max. %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DICE_CUSTOM_OTHER(String value0, int num1, int num2) {
		return new SM_SYSTEM_MESSAGE(1400127, value0, num1, num2);
	}

	/**
	 * You cannot invite the player to the force as the group leader of the player is in an Instanced Zone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_INVITE_WHEN_HE_IS_IN_INSTANCE = new SM_SYSTEM_MESSAGE(1400128);
	/**
	 * You cannot use the selected function in the current restriction phase.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_TARGET_IS_NOT_VALID = new SM_SYSTEM_MESSAGE(1400129);
	/**
	 * You cannot preview this item as it can only be used by the opposite sex,
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PREVIEW_INVALID_GENDER = new SM_SYSTEM_MESSAGE(1400130);
	/**
	 * You have item(s) left to settle at the Broker.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_VENDOR_ACCOUNT_IS_NOT_EMPTY = new SM_SYSTEM_MESSAGE(1400131);
	/**
	 * You cannot use a Rift until the curse is removed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_BY_SLAYER = new SM_SYSTEM_MESSAGE(1400132);

	/**
	 * %0 has logged in.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_NOTIFY_LOGIN_GUILD(String value0) {
		return new SM_SYSTEM_MESSAGE(1400133, value0);
	}

	/**
	 * You have sold %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PERSONAL_SHOP_SELL_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400134, value0);
	}

	/**
	 * You have sold %num1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PERSONAL_SHOP_SELL_ITEM_MULTI(int num1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400135, num1, value0s);
	}

	/**
	 * You can now use the chatting functions again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CAN_CHAT_NOW = new SM_SYSTEM_MESSAGE(1400136);

	/**
	 * You are now under level %0 curse of the Empyrean Lords for killing too many lower level targets in the opposition territory.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_UPGRADE_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400137, value0);
	}

	/**
	 * The curse of the Empyrean Lords has been reduced to %0 level because you haven't slaughtered the lower level targets for a certain time.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DEGRADE_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400138, value0);
	}

	/**
	 * The curse of Empyrean Lord has been removed because you haven't slaughtered the lower level targets for a certain time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DEGRADE_TO_NOMAL_LEVEL = new SM_SYSTEM_MESSAGE(1400139);

	/**
	 * Brave %0 has defeated notorious %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400140, value0, value1);
	}

	/**
	 * Hero of Asmodian %0 killed the Divinely Punished Intruder %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_LIGHT_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400141, value0, value1);
	}

	/**
	 * Hero of Elyos %0 killed the Divinely Punished Intruder %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DARK_DEATH_TO_B(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400142, value0, value1);
	}

	/**
	 * You are now in %0 state because you've killed too many lower level targets in the opposition territory.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_UP_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400143, value0);
	}

	/**
	 * %0 has been lowered to %1 because you haven't killed the lower level targets for a certain time.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DOWN_LEVEL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400144, value0, value1);
	}

	/**
	 * %0 is removed because you haven't killed the lower level targets for a certain time.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SLAYER_DOWN_TO_NOMAL_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400145, value0);
	}

	/**
	 * %0 is crafting %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_OTHER_combine_START(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400146, value0, value1);
	}

	/**
	 * %0 successfully crafted %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_OTHER_combine_SUCCESS(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400147, value0, value1);
	}

	/**
	 * %0 failed to craft %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_OTHER_combine_FAIL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400148, value0, value1);
	}

	/**
	 * You cannot use a Rift until the %0 is removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_BY_SLAYER_GRADE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400149, value0);
	}

	/**
	 * Only the Legion Brigade General can change his Legion name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_ERROR_ONLY_MASTER_CAN_CHANGE_NAME = new SM_SYSTEM_MESSAGE(1400150);
	/**
	 * Invalid character name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_CHAR_NAME_ERROR_WRONG_INPUT = new SM_SYSTEM_MESSAGE(1400151);
	/**
	 * Invalid Legion name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_ERROR_WRONG_INPUT = new SM_SYSTEM_MESSAGE(1400152);
	/**
	 * The character name is already in use. Enter another name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_CHAR_NAME_ERROR_SAME_YOUR_NAME = new SM_SYSTEM_MESSAGE(1400153);
	/**
	 * The Legion name is already in use. Enter another name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_ERROR_SAME_YOUR_NAME = new SM_SYSTEM_MESSAGE(1400154);
	/**
	 * A character is using the name. Enter another name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_CHAR_NAME_ALREADY_EXIST = new SM_SYSTEM_MESSAGE(1400155);
	/**
	 * A Legion is using the name. Enter another name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_ALREADY_EXIST = new SM_SYSTEM_MESSAGE(1400156);

	/**
	 * The character name has been changed to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EDIT_CHAR_NAME_SUCCESS(String value0) {
		return new SM_SYSTEM_MESSAGE(1400157, value0);
	}

	/**
	 * The Legion name has been changed to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_SUCCESS(String value0) {
		return new SM_SYSTEM_MESSAGE(1400158, value0);
	}

	/**
	 * Failed to change the name. Error code is %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EDIT_NAME_ERROR_DEFAULT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400159, value0);
	}

	/**
	 * You cannot change the Legion name while occupying the fortress or Artifact.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_CANT_FOR_HAVING_HOUSE = new SM_SYSTEM_MESSAGE(1400160);
	/**
	 * You can neither talk with NPCs nor use any useful functions in your current Restriction Phase.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOT_CANNOT_USE_NPC_UTILITY = new SM_SYSTEM_MESSAGE(1400161);
	/**
	 * You cannot trade with other characters in your current Restriction Phase.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOT_CANNOT_USE_PC_TRADE = new SM_SYSTEM_MESSAGE(1400162);
	/**
	 * You are automatically excluded from the group because the auto hunting reports have accumulated to the limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_BANISHED_FROM_PARTY = new SM_SYSTEM_MESSAGE(1400163);
	/**
	 * You are automatically excluded from the force because the auto hunting reports have accumulated to the limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ACCUSE_BANISHED_FROM_FORCE = new SM_SYSTEM_MESSAGE(1400164);
	/**
	 * The Energy of Repose is ineffective in your current Restriction Phase.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOT_CANNOT_RECEIVE_VITAL_BONUS = new SM_SYSTEM_MESSAGE(1400165);
	/**
	 * The selected user cannot do any trading at the moment.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_USE_PC_TRADE_TO_BOT = new SM_SYSTEM_MESSAGE(1400166);
	/**
	 * You cannot glide in this area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOGLIDE_AREA = new SM_SYSTEM_MESSAGE(1400167);
	/**
	 * You are forced to stop gliding because you've entered the no glide area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOGLIDE_AREA_STOP = new SM_SYSTEM_MESSAGE(1400168);

	/**
	 * The remaining active time of the registered Kisk is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_BINDSTONE_WARNING_REMAIN_TIME(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400169, durationtime0);
	}

	/**
	 * You cannot change the name of the Legion during the disbanding mode.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EDIT_GUILD_NAME_CANT_FOR_DISPERSING_GUILD = new SM_SYSTEM_MESSAGE(1400170);
	/**
	 * You cannot report auto hunting in the current region.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ACCUSE_IN_THIS_ZONE = new SM_SYSTEM_MESSAGE(1400171);
	/**
	 * You have purchased the warehouse expansion item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INGAMESHOP_DUPLICATED_WAREHOUSE = new SM_SYSTEM_MESSAGE(1400172);
	/**
	 * Channel Host
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_HOST = new SM_SYSTEM_MESSAGE(1400173);
	/**
	 * As your character name has changed, you are removed from all joined channels.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_CHAR_NAME_CHANGED1 = new SM_SYSTEM_MESSAGE(1400174);
	/**
	 * As your character name has changed, you are removed from all joined channels. (including any participating private channels)
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_CHAR_NAME_CHANGED2 = new SM_SYSTEM_MESSAGE(1400175);
	/**
	 * Group
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_PARTY = new SM_SYSTEM_MESSAGE(1400176);

	/**
	 * You can no longer use %0 as the number of allowed usage has been reached.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_USE_DUPLICATED_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400177, value0);
	}

	/**
	 * You may reenter %WORLDNAME1 after %DURATIONTIME0 has passed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_COOL_TIME_REMAIN(String worldname1, String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400178, worldname1, durationtime0);
	}

	/**
	 * You cannot enter the selected Instanced Zone at your level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ENTER_LEVEL = new SM_SYSTEM_MESSAGE(1400179);

	/**
	 * The %num0 player limit of %WORLDNAME1 has been exceeded.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_TOO_MANY_MEMBERS(int num0, String worldname1) {
		return new SM_SYSTEM_MESSAGE(1400180, num0, worldname1);
	}

	/**
	 * You have already applied to enter %WORLDNAME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ALREADY_REGISTERED(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400181, worldname0);
	}

	/**
	 * Only the force captain, vice captain or group leader can apply for group entry.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_NOT_LEADER = new SM_SYSTEM_MESSAGE(1400182);

	/**
	 * You aborted entering %0. You can apply again after 10 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REGISTER_CANCELED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400183, value0);
	}

	/**
	 * You aborted entering %0. You can try again after 10 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_ENTER_GIVEUP(String value0) {
		return new SM_SYSTEM_MESSAGE(1400184, value0);
	}

	/**
	 * You are not able to enter the Instanced Zone right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ENTER_STATE = new SM_SYSTEM_MESSAGE(1400185);
	/**
	 * You have failed to make an entry application.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ENTER_NOTICE = new SM_SYSTEM_MESSAGE(1400186);

	/**
	 * %0 is not able to enter the Instanced Zone right now.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ENTER_MEMBER(String value0) {
		return new SM_SYSTEM_MESSAGE(1400187, value0);
	}

	/**
	 * The number of your private channel may have been changed with the deletion of the %0 Channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CHANGE_CHANNEL5(String value0) {
		return new SM_SYSTEM_MESSAGE(1400188, value0);
	}

	/**
	 * You have applied to join %0's group.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_JUST_SENT_APPLY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400189, value0);
	}

	/**
	 * You have invited %0 to join your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_JUST_INVITE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400190, value0);
	}

	/**
	 * You have applied to join %0's alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_JUST_SENT_APPLY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400191, value0);
	}

	/**
	 * You have invited %0 to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_JUST_INVITE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400192, value0);
	}

	/**
	 * That player is already being resurrected.
	 */
	public static final SM_SYSTEM_MESSAGE STR_OTHER_USER_USE_RESURRECTDEBUFF_SKILL_ALREADY = new SM_SYSTEM_MESSAGE(1400193);
	/**
	 * You have successfully made an entry application.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REGISTER_SUCCESS = new SM_SYSTEM_MESSAGE(1400194);

	/**
	 * The attack time remaining is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REMAIN_TIME(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400195, durationtime0);
	}

	/**
	 * Infiltration of Dark Poeta now commences.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_START_IDLF1 = new SM_SYSTEM_MESSAGE(1400196);

	/**
	 * %0 is under attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_BOSS_ATTACKED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400198, value0);
	}

	/**
	 * %0 has destroyed %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_ROOM_DESTROYED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400199, value0, value1);
	}

	/**
	 * The group or force no longer exists.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_NOT_EXIST = new SM_SYSTEM_MESSAGE(1400200);

	/**
	 * The %num0 player limit of %WORLDNAME1 has been exceeded.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_ENTER_INSTANCE_MAX_COUNT(int num0, String worldname1) {
		return new SM_SYSTEM_MESSAGE(1400201, num0, worldname1);
	}

	/**
	 * The attack time remaining is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REMAIN_TIME_60(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400202, durationtime0);
	}

	/**
	 * The attack time remaining is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REMAIN_TIME_30(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400203, durationtime0);
	}

	/**
	 * The attack time remaining is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REMAIN_TIME_10(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400204, durationtime0);
	}

	/**
	 * The attack time remaining is %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REMAIN_TIME_5(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400205, durationtime0);
	}

	/**
	 * The effective time has expired and the link is no longer active.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CMD_LINK_EXPIRED = new SM_SYSTEM_MESSAGE(1400206);

	/**
	 * %0 can't apply to join the selected group as he or she is already a member of an alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_CANT_USE_PARTY_MATCH(String value0) {
		return new SM_SYSTEM_MESSAGE(1400207, value0);
	}

	/**
	 * You cannot preview this item as it can only be used by the other race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PREVIEW_INVALID_RACE = new SM_SYSTEM_MESSAGE(1400208);
	/**
	 * You cannot preview this item as you can't use this appearance modifying item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PREVIEW_INVALID_COSMETIC = new SM_SYSTEM_MESSAGE(1400209);
	/**
	 * You cannot preview this item as there is no appearance image.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PREVIEW_NO_EXIST_COSMETIC_DATA = new SM_SYSTEM_MESSAGE(1400210);
	/**
	 * There is no Greater Stigma slot available.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ENHANCED1_STIGMA_SLOT_IS_NOT_OPENED = new SM_SYSTEM_MESSAGE(1400211);
	/**
	 * You cannot use invite, leave or kick commands related to your group or force in this region.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1400212);

	/**
	 * You must first learn the prerequisite skill to equip %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_TO_EQUIP_STONE_LEARN_PRESKILL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400213, value0);
	}

	/**
	 * You cannot deactivate %0 as it is a prerequisite skill of %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_STIGMA_CANT_UNEQUIP_STONE_FOR_AFTERSKILL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400214, value0, value1);
	}

	/**
	 * You already applied to join %0's group. You may apply for Recruit Group once every 15 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_ALREADY_SENT_APPLY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400215, value0);
	}

	/**
	 * You already applied to join %0's force. You may apply for Recruit Alliance once every 15 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_ALREADY_SENT_APPLY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400216, value0);
	}

	/**
	 * It's a shame, but let's play together next time. Have a good time in Aion!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_DECLINED = new SM_SYSTEM_MESSAGE(1400217);
	/**
	 * It's a shame, but let's play together next time. Have a good time in Aion!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_DECLINED = new SM_SYSTEM_MESSAGE(1400218);
	/**
	 * You cannot enter as you do not have the required item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_ENTER_WITHOUT_ITEM = new SM_SYSTEM_MESSAGE(1400219);

	/**
	 * %DURATIONTIME0 remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_TIME(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400220, durationtime0);
	}

	/**
	 * %DURATIONTIME0 remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_TIME_60(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400221, durationtime0);
	}

	/**
	 * %DURATIONTIME0 remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_TIME_30(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400222, durationtime0);
	}

	/**
	 * %DURATIONTIME0 remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_TIME_10(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400223, durationtime0);
	}

	/**
	 * %DURATIONTIME0 remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_TIME_5(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400224, durationtime0);
	}

	/**
	 * Characters under level %0 cannot use Channel Chat.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_CHANNELCHAT_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400225, value0);
	}

	/**
	 * The Portside Defense Shield has been generated at the Ready Room 1.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEFTWALL_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400226);
	/**
	 * The Starboard Defense Shield has been generated at the Ready Room 2.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RIGHTWALL_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400227);
	/**
	 * A Portside Central Teleporter has been generated at the Escape Hatch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEFTTELEPORTER_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400228);
	/**
	 * A Starboard Central Teleporter has been generated at the Secondary Escape Hatch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RIGHTTELEPORTER_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400229);
	/**
	 * The Portside Door of Captain's Cabin has been destroyed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEFTDOOR_DESTROYED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400230);
	/**
	 * The Starboard Door of Captain's Cabin has been destroyed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RIGHTDOOR_DESTROYED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400231);

	/**
	 * %num1 %0(s) remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_LEFT(int num1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400232, num1, value0s);
	}

	/**
	 * Prepare for Battle!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_PREPARE_TIME = new SM_SYSTEM_MESSAGE(1400233);

	/**
	 * Prepare for combat! Enemies approaching!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Tames_Solo_A_Start = new SM_SYSTEM_MESSAGE(1402780);

	/**
	 * Prepare for combat! More enemies swarming in!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDRaksha_solo_WaveMid = new SM_SYSTEM_MESSAGE(1402832);

	/**
	 * Only a few enemies left!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDRaksha_solo_WaveLast01 = new SM_SYSTEM_MESSAGE(1402834);

	/**
	 * A Captain's Cabin Teleport Device that can be used for 3 minutes has been generated at the end of the Central Passage.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSSTELEPORTER_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400234);

	/**
	 * You cannot enter %WORLDNAME0 as the entry time has expired.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_ENTER_FOR_TIMEOVER(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400235, worldname0);
	}

	/**
	 * Exceeded %num0 points!
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_SCORE_ALARM(int num0) {
		return new SM_SYSTEM_MESSAGE(1400236, num0);
	}

	/**
	 * You have gained %num1 points from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_SCORE(int num1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400237, num1, value0);
	}

	/**
	 * You cannot open a private store in this region.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_OPEN_STORE_IN_THIS_ZONE = new SM_SYSTEM_MESSAGE(1400238);

	/**
	 * You have joined the %0 region channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_JOIN_ZONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400239, value0);
	}

	/**
	 * You have joined the %0 trade channel.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHAT_CHANNEL_JOIN_TRADE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400240, value0);
	}

	/**
	 * You have already sent an Unavailable message to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PARTY_MATCH_ALREADY_SENT_DECLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400241, value0);
	}

	/**
	 * You have already sent a Reject Alliance message to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FORCE_MATCH_ALREADY_SENT_DECLINE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400242, value0);
	}

	/**
	 * The protective magic ward of Balaur has been activated.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_START_IDABRE = new SM_SYSTEM_MESSAGE(1400243);
	/**
	 * All the treasure chests of Balaur have disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TREASUREBOX_DESPAWN_ALL = new SM_SYSTEM_MESSAGE(1400244);
	/**
	 * One treasure chest of Balaur has disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TREASUREBOX_DESPAWN_ONE = new SM_SYSTEM_MESSAGE(1400245);

	/**
	 * %0 is open and you can now access %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_OPEN_DOOR(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400246, value0, value1);
	}

	/**
	 * You cannot register because the limit of characters that can register on the Kisk has been reached.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_REGISTER_BINDSTONE_FULL = new SM_SYSTEM_MESSAGE(1400247);
	/**
	 * Grogget's Safe door is open and you can now access Grogget's Safe.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_OPEN_DOOR_01 = new SM_SYSTEM_MESSAGE(1400248);
	/**
	 * The Brig door is open and you can now access The Brig.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_OPEN_DOOR_02 = new SM_SYSTEM_MESSAGE(1400249);
	/**
	 * The Generator Chamber access door is open and you can now access the Drana Generator Chamber.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_OPEN_DOOR_03 = new SM_SYSTEM_MESSAGE(1400250);
	/**
	 * The Large Gun Deck door is open and you can now access the Large Gun Deck.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_OPEN_DOOR_04 = new SM_SYSTEM_MESSAGE(1400251);
	/**
	 * The infiltration route into Dredgion is open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400252);
	/**
	 * The Abyss Gate will operate for 5 minutes only.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_PORTAL_TIME = new SM_SYSTEM_MESSAGE(1400253);

	/**
	 * You may only battle %0 within the given time limit.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_BATTLE_TIME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400254, value0);
	}

	/**
	 * %0 has left the battle.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_BATTLE_END(String value0) {
		return new SM_SYSTEM_MESSAGE(1400255, value0);
	}

	/**
	 * You cannot gain any more Abyss Points because you reached the maximum Abyss Points you can get for your current level.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_GET_AP_LEVELBASE_LIMIT = new SM_SYSTEM_MESSAGE(1400256);
	/**
	 * You may only battle Tahabata Pyrelord within the given time limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_S_RANK_BATTLE_TIME = new SM_SYSTEM_MESSAGE(1400257);
	/**
	 * Tahabata Pyrelord has left the battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_S_RANK_BATTLE_END = new SM_SYSTEM_MESSAGE(1400258);
	/**
	 * You may only battle Lord of Flame Calindi within the given time limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_A_RANK_BATTLE_TIME = new SM_SYSTEM_MESSAGE(1400259);
	/**
	 * Lord of Flame Calindi has left the battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_A_RANK_BATTLE_END = new SM_SYSTEM_MESSAGE(1400260);

	/**
	 * Connection will time out in %DURATIONTIME0. Please take a break.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_USER_KICKED_BY_TIMEOUT(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400261, durationtime0);
	}

	/**
	 * The Steel Beard Pirates have begun hiding the Key Boxes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_TIMER_START = new SM_SYSTEM_MESSAGE(1400262);
	/**
	 * All the Key Boxes have disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDSHULACKSHIP_TIMER_END = new SM_SYSTEM_MESSAGE(1400263);

	/**
	 * The opposition has withdrawn from the Dredgion infiltration mission. The mission will stop in %DURATIONTIME0% and you will leave the Dredgion.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ALARM_COLD_GAME_IDAB1_DREADGION(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400264, durationtime0);
	}

	/**
	 * A Nuclear Control Room Teleporter has been created at the Emergency Exit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NUCLEARTELEPORTER_CREATED_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400265);

	/**
	 * Characters under level %0 cannot send Alliance invitations.
	 */
	public static SM_SYSTEM_MESSAGE STR_PARTY_ALLIANCE_TOO_LOW_LEVEL_TO_INVITE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400266, value0);
	}

	/**
	 * Only those at or under level %0 can use %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_TOO_HIGH_LEVEL(int value0, int value1) {
		return new SM_SYSTEM_MESSAGE(1400267, value0, value1);
	}

	/**
	 * You were poisoned during extraction and cannot extract for %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_RESTRICTED(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400268, durationtime0);
	}

	/**
	 * You have recovered from poisoning and can extract again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_RECOVERED = new SM_SYSTEM_MESSAGE(1400269);
	/**
	 * You chanted a spell to cleanse the poison from your body. You can now extract again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_UNRESTRICT = new SM_SYSTEM_MESSAGE(1400270);

	/**
	 * Your incantation was incorrect; you failed to purify the poison. You have %0 attempts left.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_UNRESTRICT_FAILED_RETRY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400271, value0);
	}

	/**
	 * Your incantation was incorrect; you failed to purify the poison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_UNRESTRICT_FAILED = new SM_SYSTEM_MESSAGE(1400272);

	/**
	 * You are currently poisoned and unable to extract. (Time remaining: %DURATIONTIME0)
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_REMAIN_RESTRICT_TIME(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400273, durationtime0);
	}

	/**
	 * You are able to extract.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CAPTCHA_NOT_RESTRICTED = new SM_SYSTEM_MESSAGE(1400274);
	/**
	 * A dimensional corridor that leads to the Indratu Fortress has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_PORTAL_OPEN_IDLF3_Castle_Indratoo = new SM_SYSTEM_MESSAGE(1400275);
	/**
	 * A dimensional corridor that leads to the Draupnir Cave has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_PORTAL_OPEN_IDDF3_Dragon = new SM_SYSTEM_MESSAGE(1400276);

	/**
	 * You gained %num0 points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_SCORE_FOR_ENEMY(int num0) {
		return new SM_SYSTEM_MESSAGE(1400277, num0);
	}

	/**
	 * You cannot fly while your pet is banned from flying.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_FLY_NOW_DUE_TO_NOFLY_FROM_PET = new SM_SYSTEM_MESSAGE(1400278);
	/**
	 * You cannot extract from equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DECOMPOSE_EQUIP_ITEM_CAN_NOT_BE_DECOMPOSED = new SM_SYSTEM_MESSAGE(1400279);

	/**
	 * The remaining playing time is %*0.
	 */
	public static SM_SYSTEM_MESSAGE STR_REMAIN_PLAYTIME_CENTER_DISPLAY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400280, value0);
	}

	/**
	 * You do not have enough Jewels of Eternity to buy the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUY_SELL_NOT_ENOUGH_AIONJEWELS_TO_BUY_ITEM = new SM_SYSTEM_MESSAGE(1400281);

	/**
	 * This modification cannot be completed as the gender or race requirements for %0 and %1 are different.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_CHANGE_OPPOSITE_ITEM_SKIN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400282, value0, value1);
	}

	/**
	 * %0 cannot be used for modification.
	 */
	public static SM_SYSTEM_MESSAGE STR_CHANGE_ITEM_SKIN_NOT_SKIN_EXTRACTABLE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400283, value0);
	}

	/**
	 * You must pass the Expert test in order to be promoted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHER_CANT_EXTEND_MASTER = new SM_SYSTEM_MESSAGE(1400284);
	/**
	 * You must pass the Artisan test in order to be promoted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_CANT_EXTEND_HIGH_MASTER = new SM_SYSTEM_MESSAGE(1400285);
	/**
	 * You must pass the Master test in order to be promoted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CRAFT_CANT_EXTEND_GRAND_MASTER = new SM_SYSTEM_MESSAGE(1400286);

	/**
	 * Crafting %0 has used up the recipe.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMBINE_USAGE_OVER(String value0) {
		return new SM_SYSTEM_MESSAGE(1400287, value0);
	}

	/**
	 * The level of the item to be combined must be higher than that of the one to be extracted.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_MAIN_REQUIRE_HIGHER_LEVEL = new SM_SYSTEM_MESSAGE(1400288);

	/**
	 * %0 cannot be combined.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_NOT_AVAILABLE(int i) {
		return new SM_SYSTEM_MESSAGE(1400289, new DescriptionId(i));
	}

	/**
	 * This modification cannot be completed as the equipment requirements for %0 and %1 are different.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_CHANGE_SKIN_OPPOSITE_REQUIREMENT(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400290, value0, value1);
	}

	/**
	 * The appearance maintain time for %0 has expired and the appearance modification effect has been removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SKIN_CHANGE_TIME_EXPIRED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400291, value0);
	}

	/**
	 * All fortresses in Inggison and Gelkmaros have changed to the Capturable State.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_PVP_ON = new SM_SYSTEM_MESSAGE(1400292);

	/**
	 * %0 is no longer vulnerable.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_PVP_OFF(String value0) {
		return new SM_SYSTEM_MESSAGE(1400293, value0);
	}

	/**
	 * The Guardian General is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_BOSS_ATTACKED = new SM_SYSTEM_MESSAGE(1400294);

	/**
	 * %1 of the %0 killed the Guardian General.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_BOSS_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400295, value1, value0);
	}

	/**
	 * The Balaur have killed the Guardian General.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DRAGON_BOSS_KILLED = new SM_SYSTEM_MESSAGE(1400296);

	/**
	 * %0 has conquered %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_GUILD_WIN_CASTLE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400297, value0, value1);
	}

	/**
	 * %0 succeeded in conquering %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_WIN_CASTLE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400298, value0, value1);
	}

	/**
	 * %0 Legion lost %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_GUILD_CASTLE_TAKEN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400299, value0, value1);
	}

	/**
	 * %0 failed to defend %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_CASTLE_TAKEN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400300, value0, value1);
	}

	/**
	 * %1 of %0 obtained the Artifact %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_WIN_FIELDARTIFACT(String value1, String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1400301, value1, value0, value2);
	}

	/**
	 * %1 lost the Artifact %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_LOSE_FIELDARTIFACT(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400302, value1, value0);
	}

	/**
	 * The Castle Gate is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DOOR_ATTACKED = new SM_SYSTEM_MESSAGE(1400303);
	/**
	 * The Castle Gate is in danger.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DOOR_ATSTAKE = new SM_SYSTEM_MESSAGE(1400304);

	/**
	 * %1 of the %0 destroyed the Castle Gate.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_DOOR_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400305, value1, value0);
	}

	/**
	 * The Balaur have destroyed the Castle Gate.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DRAGON_DOOR_BROKEN = new SM_SYSTEM_MESSAGE(1400306);
	/**
	 * The Gate Guardian Stone is under attack.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_REPAIR_ATTACKED = new SM_SYSTEM_MESSAGE(1400307);

	/**
	 * %1 of the %0 destroyed the Gate Guardian Stone.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_REPAIR_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400308, value1, value0);
	}

	/**
	 * The Balaur have destroyed the Gate Guardian Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DRAGON_REPAIR_BROKEN = new SM_SYSTEM_MESSAGE(1400309);
	/**
	 * The Balaur Dredgion has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_CARRIER_SPAWN = new SM_SYSTEM_MESSAGE(1400310);
	/**
	 * The Dredgion has dropped Balaur Troopers.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_CARRIER_DROP_DRAGON = new SM_SYSTEM_MESSAGE(1400311);
	/**
	 * The Balaur Dredgion has disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_CARRIER_DESPAWN = new SM_SYSTEM_MESSAGE(1400312);

	/**
	 * %1 of %0 is activating the Artifact %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDARTIFACT_CASTING(String value1, String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1400313, value1, value0, value2);
	}

	/**
	 * The Artifact %1 core of %0 has been ejected.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDARTIFACT_CORE_CASTING(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400314, value1, value0);
	}

	/**
	 * The activation of the Artifact %1 of %0 was canceled.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDARTIFACT_CANCELED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400315, value1, value0);
	}

	/**
	 * %1 of %0 has activated the Artifact %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDARTIFACT_FIRE(String value1, String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1400316, value1, value0, value2);
	}

	/**
	 * Kaisinel's Agent Veille has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTBOSS_SPAWN = new SM_SYSTEM_MESSAGE(1400317);
	/**
	 * Marchutan's Agent Mastarius has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKBOSS_SPAWN = new SM_SYSTEM_MESSAGE(1400318);
	/**
	 * Kaisinel's Agent Veille has disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTBOSS_DESPAWN = new SM_SYSTEM_MESSAGE(1400319);
	/**
	 * Marchutan's Agent Mastarius has disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKBOSS_DESPAWN = new SM_SYSTEM_MESSAGE(1400320);
	/**
	 * Kaisinel's Agent Veille is under attack!
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTBOSS_ATTACKED = new SM_SYSTEM_MESSAGE(1400321);
	/**
	 * Marchutan's Agent Mastarius is under attack!
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKBOSS_ATTACKED = new SM_SYSTEM_MESSAGE(1400322);

	/**
	 * %1 of %0 has killed Marchutan's Agent Mastarius.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKBOSS_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400323, value1, value0);
	}

	/**
	 * %1 of %0 has killed Kaisinel's Agent Veille.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTBOSS_KILLED(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400324, value1, value0);
	}

	/**
	 * %0 is under attack.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_BARRIER_ATTACKED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400325, value0);
	}

	/**
	 * %0% is in danger!
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_BARRIER_ATSTAKE(String value0value) {
		return new SM_SYSTEM_MESSAGE(1400326, value0value);
	}

	/**
	 * %1 of %0 has destroyed %SUBZONE2.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_BARRIER_BROKEN(String value1, String value0, String subzone2) {
		return new SM_SYSTEM_MESSAGE(1400327, value1, value0, subzone2);
	}

	/**
	 * Silentera Westgate, the entrance from Inggison to Silentera Canyon, has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTUNDERPASS_SPAWN = new SM_SYSTEM_MESSAGE(1400328);
	/**
	 * Silentera Eastgate, the entrance from Gelkmaros to Silentera Canyon, has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKUNDERPASS_SPAWN = new SM_SYSTEM_MESSAGE(1400329);
	/**
	 * Silentera Westgate, the entrance from Inggison to Silentera Canyon, has closed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_LIGHTUNDERPASS_DESPAWN = new SM_SYSTEM_MESSAGE(1400330);
	/**
	 * Silentera Eastgate, the entrance from Gelkmaros to Silentera Canyon, has closed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DARKUNDERPASS_DESPAWN = new SM_SYSTEM_MESSAGE(1400331);
	/**
	 * You cannot extract because you do not have the item required for Essencetapping.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GATHERING_REQUIRE_ITEM = new SM_SYSTEM_MESSAGE(1400332);

	/**
	 * You used %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_USE_CASH_TYPE_ITEM1(String value0) {
		return new SM_SYSTEM_MESSAGE(1400333, value0);
	}

	/**
	 * You used %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_USE_CASH_TYPE_ITEM2(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400334, value1, value0s);
	}

	/**
	 * The ability combined with %0 has been removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMPOUNDED_ITEM_DECOMPOUND_SUCCESS(int i) {
		return new SM_SYSTEM_MESSAGE(1400335, new DescriptionId(i));
	}

	/**
	 * %1 has been combined with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMPOUND_SUCCESS(int i, int j) {
		return new SM_SYSTEM_MESSAGE(1400336, new DescriptionId(i), new DescriptionId(j));
	}

	/**
	 * You do not have enough Kinah to combine %0 and %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_NOT_ENOUGH_MONEY(int i, int j) {
		return new SM_SYSTEM_MESSAGE(1400337, new DescriptionId(i), new DescriptionId(j));
	}

	/**
	 * The target is immune to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_WRONG_TARGET_CLASS(String value0) {
		return new SM_SYSTEM_MESSAGE(1400338, value0);
	}

	/**
	 * The target is immune to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_WRONG_TARGET_RACE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400339, value0);
	}

	/**
	 * Characters under level %0 cannot send letters.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_MAIL_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400340, value0);
	}

	/**
	 * Characters under level %0 cannot use the search function.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_WHO_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400341, value0);
	}

	/**
	 * You have gained %num1 XP from %0 (Energy of Repose %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP_VITAL_BONUS(String value0, long num1, long num2) {
		return new SM_SYSTEM_MESSAGE(1400342, value0, num1, num2);
	}

	public static SM_SYSTEM_MESSAGE STR_GET_EXP_VITAL_BONUS_DESC(DescriptionId value0, long num1, long num2) {
		return new SM_SYSTEM_MESSAGE(1400342, value0, num1, num2);
	}

	/**
	 * You have gained %num1 XP from %0 (Energy of Salvation %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP_MAKEUP_BONUS(String value0, long num1, long num2) {
		return new SM_SYSTEM_MESSAGE(1400343, value0, num1, num2);
	}

	public static SM_SYSTEM_MESSAGE STR_GET_EXP_MAKEUP_BONUS_DESC(DescriptionId value0, long num1, long num2) {
		return new SM_SYSTEM_MESSAGE(1400343, value0, num1, num2);
	}

	/**
	 * You have gained %num1 XP from %0 (Energy of Repose %num2, Energy of Salvation %num3).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP_VITAL_MAKEUP_BONUS(String value0, long num1, long num2, long num3) {
		return new SM_SYSTEM_MESSAGE(1400344, value0, num1, num2, num3);
	}

	public static SM_SYSTEM_MESSAGE STR_GET_EXP_VITAL_MAKEUP_BONUS_DESC(DescriptionId value0, long num1, long num2, long num3) {
		return new SM_SYSTEM_MESSAGE(1400344, value0, num1, num2, num3);
	}

	/**
	 * You have gained %0 (Energy of Repose %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_EXP_GAIN_VITAL_BONUS(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1400345, value0, num1);
	}

	/**
	 * You have gained %0 (Energy of Salvation %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_EXP_GAIN_MAKEUP_BONUS(String value0, int num1) {
		return new SM_SYSTEM_MESSAGE(1400346, value0, num1);
	}

	/**
	 * You have gained %0 (Energy of Repose %num1, Energy of Salvation %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBAT_MY_EXP_GAIN_VITAL_MAKEUP_BONUS(String value0, int num1, int num2) {
		return new SM_SYSTEM_MESSAGE(1400347, value0, num1, num2);
	}

	/**
	 * You have gained %num0 XP (Energy of Repose %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP2_VITAL_BONUS(long num0, long num1) {
		return new SM_SYSTEM_MESSAGE(1400348, num0, num1);
	}

	/**
	 * You have gained %num0 XP (Energy of Salvation %num1).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP2_MAKEUP_BONUS(long num0, long num1) {
		return new SM_SYSTEM_MESSAGE(1400349, num0, num1);
	}

	/**
	 * You have gained %num0 XP (Energy of Repose %num1, Energy of Salvation %num2).
	 */
	public static SM_SYSTEM_MESSAGE STR_GET_EXP2_VITAL_MAKEUP_BONUS(long num0, long num1, long num2) {
		return new SM_SYSTEM_MESSAGE(1400350, num0, num1, num2);
	}

	/**
	 * You have selected more items than there are remaining.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIMITED_SALE_CANT_SELECT_OVER_ITEMS = new SM_SYSTEM_MESSAGE(1400351);
	/**
	 * This item is no longer available.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIMITED_SALE_CANT_SELECT_NO_ITEMS = new SM_SYSTEM_MESSAGE(1400352);
	/**
	 * You cannot purchase the item because you have exceeded the purchase limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIMITED_BUYING_CANT_SELECT_NO_ITEMS = new SM_SYSTEM_MESSAGE(1400353);
	/**
	 * You have selected more than the purchase limit of the item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIMITED_BUYING_CANT_SELECT_OVER_ITEMS = new SM_SYSTEM_MESSAGE(1400354);
	/**
	 * You cannot store this item in the Legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WAREHOUSE_CANT_LEGION_DEPOSIT = new SM_SYSTEM_MESSAGE(1400355);
	/**
	 * You cannot store this item in the account warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WAREHOUSE_CANT_ACCOUNT_DEPOSIT = new SM_SYSTEM_MESSAGE(1400356);

	/**
	 * %WORLDNAME1 (difficulty: %2) with a %num0 player limit has opened.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_WITH_DIFFICULTY_OPENED(String worldname1, String value2, int num0) {
		return new SM_SYSTEM_MESSAGE(1400357, worldname1, value2, num0);
	}

	/**
	 * %WORLDNAME1 with a %num0 player limit has opened.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_OPENED(String worldname1, int num0) {
		return new SM_SYSTEM_MESSAGE(1400358, worldname1, num0);
	}

	/**
	 * %WORLDNAME1 (difficulty: %2) with a %num0 player limit is currently open.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_WITH_DIFFICULTY_OPENED_INFO(String worldname1, String value2, int num0) {
		return new SM_SYSTEM_MESSAGE(1400359, worldname1, value2, num0);
	}

	/**
	 * %WORLDNAME1 with a %num0 player limit is currently open.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_OPENED_INFO(String worldname1, int num0) {
		return new SM_SYSTEM_MESSAGE(1400360, worldname1, num0);
	}

	/**
	 * You can only enter after the Group Leader has created the instance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_CANT_ENTER_NOT_OPENED = new SM_SYSTEM_MESSAGE(1400361);
	/**
	 * You can only use this item in a cube.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_REQUIRE_IN_INVENTORY = new SM_SYSTEM_MESSAGE(1400362);
	/**
	 * Your cube is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DECOMPRESS_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1400363);
	/**
	 * You cannot combine different weapon types.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_DIFFERENT_TYPE = new SM_SYSTEM_MESSAGE(1400364);
	/**
	 * This item cannot be registered for comparison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_NOT_COMPARABLE_ITEM = new SM_SYSTEM_MESSAGE(1400365);
	/**
	 * The Seal of Uniformity has been weakened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDTP_FANATIC_Die_Keynamed = new SM_SYSTEM_MESSAGE(1400366);
	/**
	 * You can now enter the Chamber of Unity.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDTP_FANATIC_DieAll_Keynamed = new SM_SYSTEM_MESSAGE(1400367);

	/**
	 * %0 has blocked all Whispers from characters under level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECT_WHISPER_FROM_LOW_LEVEL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400368, value0, value1);
	}

	/**
	 * %0 has blocked all mail from characters under level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REJECT_MAIL_FROM_LOW_LEVEL(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400369, value0, value1);
	}

	/**
	 * The appearance maintain time for %0 in the warehouse has expired and the appearance modification effect has been removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SKIN_CHANGE_TIME_EXPIRED_IN_WAREHOUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400370, value0);
	}

	/**
	 * This modification cannot be completed as %0 and %1 have the same appearance.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_CHANGE_SAME_ITEM_SKIN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400371, value0, value1);
	}

	/**
	 * The appearance modification effect of %0 has been removed.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNCHANGE_ITEM_SKIN_SUCCEED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400372, value0);
	}

	/**
	 * %0 is not a combined item.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOUND_ERROR_NOT_AVAILABLE(int i) {
		return new SM_SYSTEM_MESSAGE(1400373, new DescriptionId(i));
	}

	/**
	 * %0 is now selling rare items.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_TEST_DESC01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400374, value0);
	}

	/**
	 * You have sent too many mails at once and have been termporarily blocked. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_SEND_OVER_MAILS = new SM_SYSTEM_MESSAGE(1400375);

	/**
	 * You do not have enough %0 to gather.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_GATHERING_B_ITEM_CHECK(String value0) {
		return new SM_SYSTEM_MESSAGE(1400376, value0);
	}

	/**
	 * Characters under level %0 cannot shout.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANT_SHOUT_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400377, value0);
	}

	/**
	 * Optimize Fortress Battle function has been toggled on for smooth game play. Characters in the vicinity are displayed in simplified forms.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHAR_HIDE_AUTO_ON = new SM_SYSTEM_MESSAGE(1400378);
	/**
	 * You are being blown away by the wind!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WindPathIN = new SM_SYSTEM_MESSAGE(1400379);
	/**
	 * Manadar's hidden trap has been tripped!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_BombDrakan_TargetMSG = new SM_SYSTEM_MESSAGE(1400380);
	/**
	 * The Subjugated Souls have been released!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_Spectre_Buff = new SM_SYSTEM_MESSAGE(1400381);
	/**
	 * Captain Lakhara is preparing his final strike!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_TombDrakan = new SM_SYSTEM_MESSAGE(1400382);
	/**
	 * Isbariya the Resolute is tapping into his true power!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest2_01 = new SM_SYSTEM_MESSAGE(1400383);
	/**
	 * The treasure chest vanished because you did not destroy the monsters within the time limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_Oops_Reward_Is_Gone = new SM_SYSTEM_MESSAGE(1400384);
	/**
	 * The wind is too strong--you can't break away!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WindPathNoOUT = new SM_SYSTEM_MESSAGE(1400385);
	/**
	 * Soulcaller's eyes glimmer!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_Summoner_Reflect = new SM_SYSTEM_MESSAGE(1400386);
	/**
	 * Soulcaller casts the Powerful Smite skill!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_Summoner_DeadlyCasting = new SM_SYSTEM_MESSAGE(1400387);
	/**
	 * Flarestorm is unleashing an unknown power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ElementalFire_Buff = new SM_SYSTEM_MESSAGE(1400388);

	/**
	 * %0 is now selling rare items.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_TEST_DESC02(String value0) {
		return new SM_SYSTEM_MESSAGE(1400389, value0);
	}

	/**
	 * Someone in this village is selling unique items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_TEST_DESC03 = new SM_SYSTEM_MESSAGE(1400390);

	/**
	 * This message is for testing %0's limited sale.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_TEST_DESC04(String value0) {
		return new SM_SYSTEM_MESSAGE(1400391, value0);
	}

	/**
	 * Your request has been registered on the Recruit Group Member List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_OFFER_PARTY_POSTED = new SM_SYSTEM_MESSAGE(1400392);
	/**
	 * Your request has been registered on the Apply For Group List.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_SEEK_PARTY_POSTED = new SM_SYSTEM_MESSAGE(1400393);
	/**
	 * Your Find Group request was removed because it has not been updated.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_POST_DELETED_TOO_OLD = new SM_SYSTEM_MESSAGE(1400394);
	/**
	 * Your Find Group request was removed because you have joined a Group or Alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_POST_DELETED_ENTERED_PARTY = new SM_SYSTEM_MESSAGE(1400395);
	/**
	 * Your Find Group request was removed because your Group or Alliance is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_POST_DELETED_PARTY_FULL = new SM_SYSTEM_MESSAGE(1400396);
	/**
	 * Your Find Group request was removed because the Group or Alliance disbanded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_POST_DELETED_PARTY_BROKE = new SM_SYSTEM_MESSAGE(1400397);

	/**
	 * Characters under level %0 who are using a free trial cannot use the Broker.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_VENDOR(String value0) {
		return new SM_SYSTEM_MESSAGE(1400398, value0);
	}

	/**
	 * Characters under level %0 who are using a free trial cannot open a private store.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_OPEN_PERSONAL_SHOP(String value0) {
		return new SM_SYSTEM_MESSAGE(1400399, value0);
	}

	/**
	 * Characters under level %0 who are playing a free trial cannot trade.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_TRADE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400400, value0);
	}

	/**
	 * Characters under level %0 who are using a free trial cannot send mail containing items or money.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_SEND_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400401, value0);
	}

	/**
	 * Characters under level %0 who are using a free trial cannot use the private warehouse.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_WAREHOUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400402, value0);
	}

	/**
	 * Characters under level %0 who are using a free trial cannot use the Legion warehouse.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_GUILD_WAREHOUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400403, value0);
	}

	/**
	 * Characters under level %0 who are using a free trial cannot use the Account warehouse.
	 */
	public static SM_SYSTEM_MESSAGE STR_FREE_EXPERIENCE_CHARACTER_CANT_USE_ACCOUNT_WAREHOUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400404, value0);
	}

	/**
	 * Captain Adhati has appeared in the Captain's Cabin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDAB1_DREADGION = new SM_SYSTEM_MESSAGE(1400405);

	/**
	 * Usage time for %0 in the warehouse has expired.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_ITEM_BY_TIMEOUT_IN_WAREHOUSE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400406, value0);
	}

	/**
	 * Matches meeting your search conditions have been found.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_SEARCH_FOUND = new SM_SYSTEM_MESSAGE(1400407);
	/**
	 * 10 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_10 = new SM_SYSTEM_MESSAGE(1400408);
	/**
	 * 20 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_20 = new SM_SYSTEM_MESSAGE(1400409);
	/**
	 * 30 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_30 = new SM_SYSTEM_MESSAGE(1400410);
	/**
	 * 40 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_40 = new SM_SYSTEM_MESSAGE(1400411);
	/**
	 * 50 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_50 = new SM_SYSTEM_MESSAGE(1400412);
	/**
	 * 60 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_60 = new SM_SYSTEM_MESSAGE(1400413);
	/**
	 * 70 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_70 = new SM_SYSTEM_MESSAGE(1400414);
	/**
	 * 80 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_80 = new SM_SYSTEM_MESSAGE(1400415);
	/**
	 * 90 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_90 = new SM_SYSTEM_MESSAGE(1400416);
	/**
	 * 91 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_91 = new SM_SYSTEM_MESSAGE(1400417);
	/**
	 * 92 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_92 = new SM_SYSTEM_MESSAGE(1400418);
	/**
	 * 93 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_93 = new SM_SYSTEM_MESSAGE(1400419);
	/**
	 * 94 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_94 = new SM_SYSTEM_MESSAGE(1400420);
	/**
	 * 95 persons have gathered their power. The Empyrean Avatar has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_95 = new SM_SYSTEM_MESSAGE(1400421);
	/**
	 * 96 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_96 = new SM_SYSTEM_MESSAGE(1400422);
	/**
	 * 97 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_97 = new SM_SYSTEM_MESSAGE(1400423);
	/**
	 * 98 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_98 = new SM_SYSTEM_MESSAGE(1400424);
	/**
	 * 99 persons have gathered their power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_99 = new SM_SYSTEM_MESSAGE(1400425);
	/**
	 * 100 persons have gathered their power. You can now use the Empyrean Avatar.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_COUNT_100 = new SM_SYSTEM_MESSAGE(1400426);
	/**
	 * You have failed to use the Empyrean Avatar. You will need to gather power and summon it again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_DEATHBLOW_FAIL = new SM_SYSTEM_MESSAGE(1400427);
	/**
	 * The first Sphere of Mirage has been activated.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_FIRST_OBJECT_ON = new SM_SYSTEM_MESSAGE(1400428);
	/**
	 * The second Sphere of Mirage has been activated. Kaisinel's Agent Veille prepares to cast the Empyrean Lord's blessing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_SECOND_OBJECT_ON = new SM_SYSTEM_MESSAGE(1400429);
	/**
	 * You may use the Sphere of Mirage again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_CAN_USE_OBJECT = new SM_SYSTEM_MESSAGE(1400430);
	/**
	 * You need more people to activate the Sphere of Mirage.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_CANT_USE_OBJECT_NOT_ENOUGH_MEMBER = new SM_SYSTEM_MESSAGE(1400431);
	/**
	 * You are marked as Unavailable. Please reset the setting in System Preferences to accept the invitation.
	 */
	public static final SM_SYSTEM_MESSAGE STR_INFORM_INVITE_REJECT_STATE = new SM_SYSTEM_MESSAGE(1400434);

	/**
	 * %0 has succeeded in enchanting %1 to Level 15.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ENCHANT_ITEM_SUCCEEDED_15(String playerName, int nameId) {
		return new SM_SYSTEM_MESSAGE(1400435, playerName, new DescriptionId(nameId));
	}

	/**
	 * %0 has succeeded in enchanting %1 to Level 20.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ENCHANT_ITEM_SUCCEEDED_20(String playerName, int nameId) {
		return new SM_SYSTEM_MESSAGE(1402285, playerName, new DescriptionId(nameId));
	}

	/**
	 * %0 is selling items to extract vitality and Aether.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_GATHERING_DESC01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400436, value0);
	}

	/**
	 * You cannot join this race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_JOIN_ERROR_RACE = new SM_SYSTEM_MESSAGE(1400437);

	/**
	 * You can only join when your level is %0 or above.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_JOIN_ERROR_MIN_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400438, value0);
	}

	/**
	 * %0 is selling materials to create the items of Crafting Masters.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_M_EPIC_SHOP_DESC01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400439, value0);
	}

	/**
	 * %0 is selling materials to create the items of Crafting Masters.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_M_EPIC_SHOP_DESC02(String value0) {
		return new SM_SYSTEM_MESSAGE(1400440, value0);
	}

	/**
	 * %0 is selling special materials for Master Crafting.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_LIMIT_SALE_EPIC_SHOP_MATERIAL_DESC01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400441, value0);
	}

	/**
	 * Devoted Anurati has appeared in the Great Chapel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDTP_FANATIC_DrakanNamed_SpawnMSG = new SM_SYSTEM_MESSAGE(1400442);
	/**
	 * Malicious Obscura exhausts the HP of nearby enemies!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Normal_Stalker_DrainHealth = new SM_SYSTEM_MESSAGE(1400443);
	/**
	 * Misguiding Obscura crouches!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Normal_Stalker_Sanctuary = new SM_SYSTEM_MESSAGE(1400444);
	/**
	 * Grave Slime is splitting in two!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Normal_Slime_Isolation = new SM_SYSTEM_MESSAGE(1400445);
	/**
	 * Thurzon the Undying stops its assault and begins reviving.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_BoneDrake_Sanctuary = new SM_SYSTEM_MESSAGE(1400446);
	/**
	 * Your Apply For Group List request was deleted because you have joined a Group or Alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_MATCH_SEEK_POST_DELETED_ENTERED_PARTY = new SM_SYSTEM_MESSAGE(1400447);

	/**
	 * You used %1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_USE_ITEM_MULTI(String value1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400448, value1, value0s);
	}

	/**
	 * You are in normal state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_INFO_0_LEVEL = new SM_SYSTEM_MESSAGE(1400449);
	/**
	 * A Level 1 Fatigue Penalty has been applied because you have played too long. Please log out and take a break.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_INFO_1_LEVEL = new SM_SYSTEM_MESSAGE(1400450);
	/**
	 * A Level 2 Fatigue Penalty has been applied because you have played too long. Please log out and take a break.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_INFO_2_LEVEL = new SM_SYSTEM_MESSAGE(1400451);

	/**
	 * You have opened the %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNCOMPRESS_COMPRESSED_ITEM_SUCCEEDED(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400452, new DescriptionId(nameId));
	}

	/**
	 * You have stopped opening the %0 bundle.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNCOMPRESS_COMPRESSED_ITEM_CANCELED(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400453, new DescriptionId(nameId));
	}

    /**
	 * Ride Restriction
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ATTACK_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401093);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401094);
	public static final SM_SYSTEM_MESSAGE STR_MSG_PERSONAL_SHOP_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401095);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GATHER_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401096);
	public static final SM_SYSTEM_MESSAGE STR_MSG_COMBINE_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401097);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_RIDE_INVALID_LOCATION = new SM_SYSTEM_MESSAGE(1401099);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SKILL_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1401132);
	public static final SM_SYSTEM_MESSAGE STR_MSG_NORIDE_AREA_STOP = new SM_SYSTEM_MESSAGE(1401170);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_SELL_WHILE_IN_RIDE = new SM_SYSTEM_MESSAGE(1401210);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_RIDE = new SM_SYSTEM_MESSAGE(1401211);
	public static final SM_SYSTEM_MESSAGE STR_MSG_NORIDE_TIME_EXPIRE = new SM_SYSTEM_MESSAGE(1401220);
	public static final SM_SYSTEM_MESSAGE STR_MSG_UNRIDE_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1401254);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_RIDE_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1401255);
	public static final SM_SYSTEM_MESSAGE STR_MSG_UNRIDE_DESTROY_ITEM = new SM_SYSTEM_MESSAGE(1401426);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_RIDE_NEARBY_CEILING = new SM_SYSTEM_MESSAGE(1402950);
	
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_ENCHANT_ITEM = new SM_SYSTEM_MESSAGE(1403363);

	/**
	 * The Divine Artifact has been activated!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_Artifact_Light = new SM_SYSTEM_MESSAGE(1400454);
	/**
	 * The Magic Artifact has been activated!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_Artifact_Dark = new SM_SYSTEM_MESSAGE(1400455);
	/**
	 * Isbariya taps into his power to cause a massive explosion!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_Artifact_LightBoom = new SM_SYSTEM_MESSAGE(1400456);
	/**
	 * Isbariya releases his magical power!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_Artifact_DarkBoom = new SM_SYSTEM_MESSAGE(1400457);

	/**
	 * %0 has given up following because the distance between you is too great.
	 */
	public static SM_SYSTEM_MESSAGE STR_MERCENARY_FOLLOWING_CANCELED_BY_TOO_DISTANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400458, value0);
	}

	/**
	 * Isbariya the Resolute has boosted his attack power!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_2phase = new SM_SYSTEM_MESSAGE(1400459);
	/**
	 * Isbariya the Resolute has boosted his recovery power!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_3phase = new SM_SYSTEM_MESSAGE(1400460);
	/**
	 * Isbariya the Resolute unleashes an intense power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_4phase = new SM_SYSTEM_MESSAGE(1400461);
	/**
	 * Isbariya the Resolute has summoned a Bodyguard Commissioned Officer.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_5phase = new SM_SYSTEM_MESSAGE(1400462);
	/**
	 * Isbariya the Resolute inflicts a devastating curse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_Boss_ArchPriest_6phase = new SM_SYSTEM_MESSAGE(1400463);

	/**
	 * A Level %0 Fatigue Penalty has been applied because you have played too long. Monitor your fatigue level with the '/Fatigue' command.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_UPGRADE_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400464, value0);
	}

	/**
	 * The Warrior Monument has been destroyed. Ahbana the Wicked is on alert.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdSpecter_Spawn = new SM_SYSTEM_MESSAGE(1400465);
	/**
	 * Macunbello's power is weakening.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdLich_weakness1 = new SM_SYSTEM_MESSAGE(1400466);
	/**
	 * Macunbello's power has weakened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdLich_weakness2 = new SM_SYSTEM_MESSAGE(1400467);
	/**
	 * Macunbello has been crippled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdLich_weakness3 = new SM_SYSTEM_MESSAGE(1400468);
	/**
	 * Macunbello has left his sanctuary.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdLich_Leave = new SM_SYSTEM_MESSAGE(1400469);
	/**
	 * Ahbana the Wicked has appeared in the Watcher's Nexus.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdSpecter_Start = new SM_SYSTEM_MESSAGE(1400470);
	/**
	 * Hiding Lupukin has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_NmdShulack_Rufukin = new SM_SYSTEM_MESSAGE(1400471);
	/**
	 * The Aetheric Field Activation Stone is under attack!
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_SHIELD_ATTACKED = new SM_SYSTEM_MESSAGE(1400472);

	/**
	 * %1 of %0 destroyed the castle gate.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIELDABYSS_SHIELD_BROKEN(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400473, value1, value0);
	}

	/**
	 * The Balaur have destroyed the Aetheric Field Activation Stone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIELDABYSS_DRAGON_SHIELD_BROKEN = new SM_SYSTEM_MESSAGE(1400474);
	/**
	 * The cocoons are wriggling--something's inside!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_COCOON_INFO = new SM_SYSTEM_MESSAGE(1400475);
	/**
	 * Cracks appear on the surface of Queen Mosqua's egg.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_EGG_BREAK = new SM_SYSTEM_MESSAGE(1400476);
	/**
	 * An ascending air current is rising from the spot where the egg was. You can fly vertically up by spreading your wings and riding the current.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_WIND_INFO = new SM_SYSTEM_MESSAGE(1400477);
	/**
	 * You are unable to obtain items at the current time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RESTRICTED_STATE_CANT_GET_ITEM = new SM_SYSTEM_MESSAGE(1400478);
	/**
	 * You are unable to obtain items at the current time, and cannot participate in the roll.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RESTRICTED_STATE_CANT_THROW_DICE = new SM_SYSTEM_MESSAGE(1400479);
	/**
	 * The Seal Protector has fallen. The Rift Orb shines while the seal weakens.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCatacombs_BigOrb_Spawn = new SM_SYSTEM_MESSAGE(1400480);

	/**
	 * %1 remains before the usage time for %0 expires.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CASH_ITEM_TIME_LEFT(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400481, value1, value0);
	}

	/**
	 * %1 remains on the appearance change time of %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SKIN_CHANGE_TIME_LEFT(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400482, value1, value0);
	}

	/**
	 * You can only acquire daily quests once per day.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FACTION_CAN_NOT_RECEIVE_QUEST_TWICE_A_DAY = new SM_SYSTEM_MESSAGE(1400483);

	/**
	 * You are too far from %0 to issue an order.
	 */
	public static SM_SYSTEM_MESSAGE STR_MERCENARY_CANT_ORDER_BY_TOO_DISTANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400484, value0);
	}

	/**
	 * Water erupts from the geyser.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_JUMP_TRIGGER_ON_INFO = new SM_SYSTEM_MESSAGE(1400485);
	/**
	 * A gust of air bursts forth.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WINDBOX_TRIGGER_ON_INFO = new SM_SYSTEM_MESSAGE(1400486);
	/**
	 * Sematariux has cast defensive magic. You will be removed from Sematariux's Hideout in 2 hours.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_120M = new SM_SYSTEM_MESSAGE(1400487);
	/**
	 * You will be removed from Sematariux's Hideout in 1 hour and 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_90M = new SM_SYSTEM_MESSAGE(1400488);
	/**
	 * You will be removed from Sematariux's Hideout in 1 hour.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_60M = new SM_SYSTEM_MESSAGE(1400489);
	/**
	 * You will be removed from Sematariux's Hideout in 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_30M = new SM_SYSTEM_MESSAGE(1400490);
	/**
	 * You will be removed from Sematariux's Hideout in 15 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_15M = new SM_SYSTEM_MESSAGE(1400491);
	/**
	 * You will be removed from Sematariux's Hideout in 10 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_10M = new SM_SYSTEM_MESSAGE(1400492);
	/**
	 * You will be removed from Sematariux's Hideout in 5 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_5M = new SM_SYSTEM_MESSAGE(1400493);
	/**
	 * You will be removed from Sematariux's Hideout in 3 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_3M = new SM_SYSTEM_MESSAGE(1400494);
	/**
	 * You will be removed from Sematariux's Hideout in 2 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_2M = new SM_SYSTEM_MESSAGE(1400495);
	/**
	 * You will be removed from Sematariux's Hideout in 1 minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_1M = new SM_SYSTEM_MESSAGE(1400496);
	/**
	 * You will be removed from Sematariux's Hideout in 30 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_30S = new SM_SYSTEM_MESSAGE(1400497);
	/**
	 * You will be removed from Sematariux's Hideout in 15 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_15S = new SM_SYSTEM_MESSAGE(1400498);
	/**
	 * You will be removed from Sematariux's Hideout in 10 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_10S = new SM_SYSTEM_MESSAGE(1400499);
	/**
	 * You will be removed from Sematariux's Hideout in 5 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_5S = new SM_SYSTEM_MESSAGE(1400500);
	/**
	 * You will be removed from Sematariux's Hideout in 4 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_4S = new SM_SYSTEM_MESSAGE(1400501);
	/**
	 * You will be removed from Sematariux's Hideout in 3 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_3S = new SM_SYSTEM_MESSAGE(1400502);
	/**
	 * You will be removed from Sematariux's Hideout in 2 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_2S = new SM_SYSTEM_MESSAGE(1400503);
	/**
	 * You will be removed from Sematariux's Hideout in 1 second.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_1S = new SM_SYSTEM_MESSAGE(1400504);
	/**
	 * You have been forcibly removed from Sematariux's Hideout by Sematariux's defensive magic.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_OUT_TIMER_0S = new SM_SYSTEM_MESSAGE(1400505);
	/**
	 * Padmarashka has cast defensive magic. You will be removed from Padmarashka's Cave in 2 hours.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_120M = new SM_SYSTEM_MESSAGE(1400506);
	/**
	 * You will be removed from Padmarashka's Cave in 1 hour and 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_90M = new SM_SYSTEM_MESSAGE(1400507);
	/**
	 * You will be removed from Padmarashka's Cave in 1 hour.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_60M = new SM_SYSTEM_MESSAGE(1400508);
	/**
	 * You will be removed from Padmarashka's Cave in 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_30M = new SM_SYSTEM_MESSAGE(1400509);
	/**
	 * You will be removed from Padmarashka's Cave in 15 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_15M = new SM_SYSTEM_MESSAGE(1400510);
	/**
	 * You will be removed from Padmarashka's Cave in 10 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_10M = new SM_SYSTEM_MESSAGE(1400511);
	/**
	 * You will be removed from Padmarashka's Cave in 5 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_5M = new SM_SYSTEM_MESSAGE(1400512);
	/**
	 * You will be removed from Padmarashka's Cave in 3 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_3M = new SM_SYSTEM_MESSAGE(1400513);
	/**
	 * You will be removed from Padmarashka's Cave in 2 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_2M = new SM_SYSTEM_MESSAGE(1400514);
	/**
	 * You will be removed from Padmarashka's Cave in 1 minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_1M = new SM_SYSTEM_MESSAGE(1400515);
	/**
	 * You will be removed from Padmarashka's Cave in 30 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_30S = new SM_SYSTEM_MESSAGE(1400516);
	/**
	 * You will be removed from Padmarashka's Cave in 15 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_15S = new SM_SYSTEM_MESSAGE(1400517);
	/**
	 * You will be removed from Padmarashka's Cave in 10 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_10S = new SM_SYSTEM_MESSAGE(1400518);
	/**
	 * You will be removed from Padmarashka's Cave in 5 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_5S = new SM_SYSTEM_MESSAGE(1400519);
	/**
	 * You will be removed from Padmarashka's Cave in 4 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_4S = new SM_SYSTEM_MESSAGE(1400520);
	/**
	 * You will be removed from Padmarashka's Cave in 3 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_3S = new SM_SYSTEM_MESSAGE(1400521);
	/**
	 * You will be removed from Padmarashka's Cave in 2 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_2S = new SM_SYSTEM_MESSAGE(1400522);
	/**
	 * You will be removed from Padmarashka's Cave in 1 second.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_1S = new SM_SYSTEM_MESSAGE(1400523);
	/**
	 * You have been forcibly removed from Padmarashka's Cave by Padmarashka's defensive magic.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_OUT_TIMER_0S = new SM_SYSTEM_MESSAGE(1400524);
	/**
	 * Sematariux is about to lay eggs.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_LAY_EGG = new SM_SYSTEM_MESSAGE(1400525);
	/**
	 * Padmarashka is about to lay eggs.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_LAY_EGG = new SM_SYSTEM_MESSAGE(1400526);
	/**
	 * Lowly Daevas such as you would dare?
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_START_1 = new SM_SYSTEM_MESSAGE(1400527);
	/**
	 * You have leapt into certain death!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_START_1 = new SM_SYSTEM_MESSAGE(1400528);
	/**
	 * Kaisinel's Agent Veille has engaged in battle to defend Inggison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_GODELITE_START_1 = new SM_SYSTEM_MESSAGE(1400529);
	/**
	 * Kaisinel's Agent Veille has engaged in battle to defend Inggison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_GODELITE_START_2 = new SM_SYSTEM_MESSAGE(1400530);
	/**
	 * Kaisinel's Agent Veille has engaged in battle to defend Inggison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_GODELITE_START_3 = new SM_SYSTEM_MESSAGE(1400531);
	/**
	 * Kaisinel's Agent Veille has engaged in battle to defend Inggison.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_GODELITE_START_4 = new SM_SYSTEM_MESSAGE(1400532);
	/**
	 * Marchutan's Agent Mastarius has engaged in battle to defend Gelkmaros.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_GODELITE_START_1 = new SM_SYSTEM_MESSAGE(1400533);
	/**
	 * Marchutan's Agent Mastarius has engaged in battle to defend Gelkmaros.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_GODELITE_START_2 = new SM_SYSTEM_MESSAGE(1400534);
	/**
	 * Marchutan's Agent Mastarius has engaged in battle to defend Gelkmaros.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_GODELITE_START_3 = new SM_SYSTEM_MESSAGE(1400535);
	/**
	 * Marchutan's Agent Mastarius has engaged in battle to defend Gelkmaros.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_GODELITE_START_4 = new SM_SYSTEM_MESSAGE(1400536);
	/**
	 * I grieve for I couldn't become a dragon!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_START_2 = new SM_SYSTEM_MESSAGE(1400537);
	/**
	 * I never cared much for the responsibility of breeding!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_START_3 = new SM_SYSTEM_MESSAGE(1400538);
	/**
	 * I laugh at you pathetic Daevas who think you can defeat me!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_START_4 = new SM_SYSTEM_MESSAGE(1400539);
	/**
	 * The responsibility of breeding is my will!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_START_2 = new SM_SYSTEM_MESSAGE(1400540);
	/**
	 * I must protect the eggs!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_START_3 = new SM_SYSTEM_MESSAGE(1400541);
	/**
	 * You will never see the light of day again!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_START_4 = new SM_SYSTEM_MESSAGE(1400542);

	/**
	 * %0's buddy list is full.
	 */
	public static SM_SYSTEM_MESSAGE STR_BUDDYLIST_BUDDYS_LIST_FULL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400543, value0);
	}

	/**
	 * You must be in an Alliance to access this area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ENTER_ONLY_FORCE_DON = new SM_SYSTEM_MESSAGE(1400544);
	/**
	 * You can advance to level 10 only after you have completed the class change quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LEVEL_LIMIT_QUEST_NOT_FINISHED1 = new SM_SYSTEM_MESSAGE(1400545);

	/**
	 * %0 is located at %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_SUBZONE_FOUND_DEV(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400546, value0, value1);
	}

	/**
	 * Cannot find the path to %0. %0 is located at %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_TOO_FAR_FROM_SUBZONE_DEV(String value0, String value2, String value1) {
		return new SM_SYSTEM_MESSAGE(1400547, value0, value2, value1);
	}

	/**
	 * %0 is located at %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_NPC_FOUND_DEV(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400548, value0, value1);
	}

	/**
	 * Cannot find the path to %0. %0 is located at %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_TOO_FAR_FROM_NPC_DEV(String value0, String value2, String value1) {
		return new SM_SYSTEM_MESSAGE(1400549, value0, value2, value1);
	}

	/**
	 * %0 is located at %2 in %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_FIND_POS_NPC_FOUND_IN_OTHER_WORLD_DEV(String value0, String value2, String value1) {
		return new SM_SYSTEM_MESSAGE(1400550, value0, value2, value1);
	}

	/**
	 * Cannot perform path finding--the cooldown timer has not expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FIND_POS_CANT_USE_UNTIL_DELAYTIME = new SM_SYSTEM_MESSAGE(1400551);

	/**
	 * There is 1 minute left to trade with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ALARM_REMAIN_ONE_MINUTE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400552, value0);
	}

	/**
	 * %0's temporary trade time has expired. %0 can no longer be traded.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_END_OF_EXCHANGE_TIME(int nameId, int timeMin) {
		return new SM_SYSTEM_MESSAGE(1400553, new DescriptionId(nameId), timeMin);
	}

	/**
	 * %0 is not a target you can trade %1 with.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_WRONG_EXCHANGE_TARGET(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400554, value0, value1);
	}

	/**
	 * %0's temporary trade time has expired and can no longer be traded.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EXCHANGE_TIME_OVER(int value0) {
		return new SM_SYSTEM_MESSAGE(1400555, new DescriptionId(value0));
	}

	/**
	 * %0 has acquired %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMPLETE_EXCHANGE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400556, value0, value1);
	}

	/**
	 * You do not have enough %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_TRADE_MONEY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400557, value0);
	}

	/**
	 * You have invited %0's alliance to the Alliance League. %0's alliance has a total of %1 members.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_INVITE_HIM(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400558, value0, value1);
	}

	/**
	 * The alliance captain of the alliance %0 belongs to is %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_INVITE_HIS_LEADER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400559, value0, value1);
	}

	/**
	 * Your alliance has joined %0's Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_ENTER_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400560, value0);
	}

	/**
	 * %0's alliance has joined the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_ENTER_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400561, value0);
	}

	/**
	 * You have declined %0's invitation to join the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_REJECT_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400562, value0);
	}

	/**
	 * %0's alliance has declined your invitation to join the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_REJECT_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400563, value0);
	}

	/**
	 * %0 is already a member of another Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_ALREADY_OTHER_UNION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400564, value0);
	}

	/**
	 * You cannot invite anymore as the Alliance League is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_ADD_NEW_MEMBER = new SM_SYSTEM_MESSAGE(1400565);
	/**
	 * You have don't have permission to invite people to the League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_LEADER_CAN_INVITE = new SM_SYSTEM_MESSAGE(1400566);

	/**
	 * Currently, %0 cannot accept your invitation to join the alliance.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400567, value0);
	}

	/**
	 * You cannot invite your own alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_INVITE_SELF = new SM_SYSTEM_MESSAGE(1400568);
	/**
	 * The player you invited to the Alliance League is currently offline.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_OFFLINE_MEMBER = new SM_SYSTEM_MESSAGE(1400569);
	/**
	 * You cannot use the Alliance League invitation function while you are dead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_INVITE_WHEN_DEAD = new SM_SYSTEM_MESSAGE(1400570);
	/**
	 * You have left the Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_LEAVE_ME = new SM_SYSTEM_MESSAGE(1400571);

	/**
	 * %0's alliance has left the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_LEAVE_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400572, value0);
	}

	/**
	 * Only an alliance captain can leave the Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_LEADER_CAN_LEAVE = new SM_SYSTEM_MESSAGE(1400573);

	/**
	 * You have expelled %0's alliance from the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_BAN_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400574, value0);
	}

	/**
	 * %0 has expelled %1's alliance from the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_BAN_HIS_LEADER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400575, value0, value1);
	}

	/**
	 * %0 has expelled your alliance from the Alliance League.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_BAN_ME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400576, value0);
	}

	/**
	 * Only the league leader can kick out an alliance from the Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_LEADER_CAN_BAN = new SM_SYSTEM_MESSAGE(1400577);
	/**
	 * You cannot remove your own Alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_BAN_SELF = new SM_SYSTEM_MESSAGE(1400578);
	/**
	 * The Alliance League has disbanded due to an insufficient number of alliances.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_DISPERSED = new SM_SYSTEM_MESSAGE(1400579);

	/**
	 * You transferred the league leadership to %0. From now on, %0 is the league leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_CHANGE_LEADER(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400580, value0, value1);
	}

	/**
	 * %0 entrusted %1 with the league leadership authority. From now on, %1 is the league leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_HE_IS_NEW_LEADER(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1400581, value0, value1, value2);
	}

	/**
	 * You are now the League leader.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_YOU_BECOME_NEW_LEADER = new SM_SYSTEM_MESSAGE(1400582);
	/**
	 * Only the league leader can transfer the league leader authority to an alliance captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_LEADER_CAN_CHANGE_LEADER = new SM_SYSTEM_MESSAGE(1400583);
	/**
	 * You cannot transfer the league leadership to someone who isn't an alliance captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_CAN_CHANGE_LEADER_TO_FORCE_LEADER = new SM_SYSTEM_MESSAGE(1400584);
	/**
	 * You cannot transfer leadership to yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_CHANGE_LEADER_SELF = new SM_SYSTEM_MESSAGE(1400585);
	/**
	 * You cannot transfer the leadership to a player outside your League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_CHANGE_LEADER_OTHER_UNION = new SM_SYSTEM_MESSAGE(1400586);
	/**
	 * You are now the League leader.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_YOU_BECOME_NEW_LEADER_TIMEOUT = new SM_SYSTEM_MESSAGE(1400587);

	/**
	 * %0 was automatically entrusted with the league leader authority.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_CHANGE_LEADER_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400588, value0);
	}

	/**
	 * The alliance number has been changed to %num0.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_CHANGE_FORCE_NUMBER_ME(int num0) {
		return new SM_SYSTEM_MESSAGE(1400589, num0);
	}

	/**
	 * The alliance number of %0 has been changed to %num0.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_CHANGE_FORCE_NUMBER_HIM(String value0, int num0) {
		return new SM_SYSTEM_MESSAGE(1400590, value0, num0);
	}

	/**
	 * You cannot change it to an unclaimed alliance number.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_CHANGE_FORCE_NUMBER = new SM_SYSTEM_MESSAGE(1400591);
	/**
	 * You cannot change the league leader's alliance number.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_CANT_CHANGE_LEADER_NUMBER = new SM_SYSTEM_MESSAGE(1400592);
	/**
	 * Only the League leader can change the Alliance number.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_LEADER_CAN_CHANGE_FORCE_NUMBER = new SM_SYSTEM_MESSAGE(1400593);

	/**
	 * Your must be at least level %0 to be promoted.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_LEVELLOW_RANK_UP(String value0) {
		return new SM_SYSTEM_MESSAGE(1400594, value0);
	}

	/**
	 * The bulkhead has been activated and the passage between the First Armory and Gravity Control has been sealed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SHIELD_A_SPAWN_IDAB1_Dreadgion01 = new SM_SYSTEM_MESSAGE(1400595);
	/**
	 * The bulkhead has been activated and the passage between the Second Armory and Gravity Control has been sealed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SHIELD_B_SPAWN_IDAB1_Dreadgion01 = new SM_SYSTEM_MESSAGE(1400596);

	/**
	 * You can use Screen Capture once every %0 seconds. Time Remaining: %1 seconds
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMAIN_PRINT_SCREEN_COOLTIME(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400600, value0, value1);
	}

	/**
	 * You are not in an Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_YOU_ARE_NOT_UNION_MEMBER = new SM_SYSTEM_MESSAGE(1400601);
	/**
	 * The alliance captain is not part of the Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_CAN_BAN_FORCE_LEADER = new SM_SYSTEM_MESSAGE(1400602);
	/**
	 * The selected target is already a member of another force league.
	 */
	public static SM_SYSTEM_MESSAGE STR_UNION_ALREADY_MY_UNION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400603, value0);
	}
	/**
	 * The bulkhead has been activated and the passage between the First Armory and Gravity Control has been sealed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SHIELD_A_SPAWN_IDDreadgion02 = new SM_SYSTEM_MESSAGE(1400604);
	/**
	 * The bulkhead has been activated and the passage between the Second Armory and Gravity Control has been sealed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SHIELD_B_SPAWN_IDDreadgion02 = new SM_SYSTEM_MESSAGE(1400605);
	/**
	 * Omega summons a creature.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_RaidShowTime_Phase1 = new SM_SYSTEM_MESSAGE(1400606);
	/**
	 * Omega summons a powerful creature.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_RaidShowTime_Phase2 = new SM_SYSTEM_MESSAGE(1400607);
	/**
	 * Omega summons a healing creature.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_RaidShowTime_Phase3 = new SM_SYSTEM_MESSAGE(1400608);
	/**
	 * Omega summons a creature that creates barriers.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_RaidShowTime_Phase4 = new SM_SYSTEM_MESSAGE(1400609);
	/**
	 * Attack of poison and paralysis begins.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_RaidShowTime_Phase1 = new SM_SYSTEM_MESSAGE(1400610);
	/**
	 * Attack that restricts physical and magical assaults begins.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_RaidShowTime_Phase2 = new SM_SYSTEM_MESSAGE(1400611);
	/**
	 * Ragnarok's acidic fluid appears.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_RaidShowTime_Phase3 = new SM_SYSTEM_MESSAGE(1400612);
	/**
	 * Powerful continuous attacks and reflections begin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_RaidShowTime_Phase4 = new SM_SYSTEM_MESSAGE(1400613);
	/**
	 * You cannot invite someone who doesn't belong to an alliance to the league.
	 */
	public static final SM_SYSTEM_MESSAGE STR_UNION_ONLY_INVITE_FORCE_MEMBER = new SM_SYSTEM_MESSAGE(1400614);

	/**
	 * %0 Pats its tummy and indicates that its full.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_PET_FULL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400615, value0);
	}

	/**
	 * %0 eats %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_START_EATING(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400616, value0, value1);
	}

	/**
	 * Stop feeding %1 to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_STOP_EATING(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400617, value1, value0);
	}

	/**
	 * %0 spits out %1 and makes a face.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_FOOD_NOT_LOVEFLAVOR(String petName, int nameId) {
		return new SM_SYSTEM_MESSAGE(1400618, petName, new DescriptionId(nameId));
	}

	/**
	 * %0 is grateful and gives you a %1 as a present (Times remaining: %2/%3).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_CASH_REWARD(String value0, String value1, String value2, String value3) {
		return new SM_SYSTEM_MESSAGE(1400619, value0, value1, value2, value3);
	}

	/**
	 * %0 has enjoyed eating %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_ATE_FOOD_1(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400620, value0, value1);
	}

	/**
	 * %0 has enjoyed eating %1 and looks pleased.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_ATE_FOOD_2(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400621, value0, value1);
	}

	/**
	 * %0 has enjoyed eating %1 and looks happy.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_ATE_FOOD_3(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400622, value0, value1);
	}

	/**
	 * %0 has finished eating %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_ATE_ALL_FOOD(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400623, value0, value1);
	}

	/**
	 * %0 is thankful and gives you %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_COMMON_REWARD(String value0, String value2) {
		return new SM_SYSTEM_MESSAGE(1400624, value0, value2);
	}

	/**
	 * Only available while in an Alliance League.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_SPLIT_UNION = new SM_SYSTEM_MESSAGE(1400625);

	/**
	 * The Alliance League's looting method has changed to %0, %1 %2.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_UNION_LOOTING_CHANGED_RULE(String value0, String value1, String value2) {
		return new SM_SYSTEM_MESSAGE(1400626, value0, value1, value2);
	}

	/**
	 * An infiltration passage into the Chantra Dredgion has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDDREADGION_02 = new SM_SYSTEM_MESSAGE(1400628);
	/**
	 * An infiltration passage into the Terath Dredgion has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDDREADGION_03 = new SM_SYSTEM_MESSAGE(1401398);
	/**
	 * An infiltration passage into the Durga Dredgion has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDDREADGION_04 = new SM_SYSTEM_MESSAGE(1401894);
	/**
	 * An infiltration passage into the Kamar Battlefield has opened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDKAMAR = new SM_SYSTEM_MESSAGE(1401730);
	/**
	 * You can now participate in the Ophidan Bridge battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_OPHIDAN_WAR = new SM_SYSTEM_MESSAGE(1401947);

	/**
	 * The returned results have been applied to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_REIDENTIFY_APPLY_YES(String value0) {
		return new SM_SYSTEM_MESSAGE(1401910, value0);
	}

	/**
	 * You aborted the action of applying returned results.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_REIDENTIFY_APPLY_NO = new SM_SYSTEM_MESSAGE(1401911);
	/**
	 * You can take part in the Runatorium Battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDLDF5_Fortress_Re = new SM_SYSTEM_MESSAGE(1402192);
	/**
	 * You may participate in the Iron Wall Warfront.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_BASTION_WAR = new SM_SYSTEM_MESSAGE(1402032);
	/**
	 * The Destroyer Kunax will stop focusing its target and return to its original position.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_FORTRESS_RE_RETURNTOSP = new SM_SYSTEM_MESSAGE(1402578);

	/**
	 * The opposition has abandoned the Chantra Dredgion infiltration mission. You will leave the Chantra Dredgion when the mission ends in %DURATIONTIME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ALARM_COLD_GAME_IDDREADGION_02(String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400629, durationtime0);
	}

	/**
	 * You can save one of the two Reians imprisoned in the cocoon.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_Cocoon_Yell = new SM_SYSTEM_MESSAGE(1400630);
	/**
	 * Supplies Storage teleport device has been created at Escape Hatch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WAREHOUSETELEPORTER_CREATED_IDDREADGION_02_01 = new SM_SYSTEM_MESSAGE(1400631);
	/**
	 * Captain Zanata has appeared in the Captain's Cabin.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDREADGION_02 = new SM_SYSTEM_MESSAGE(1400632);
	/**
	 * Officer Kamanya has appeared in Gravity Control.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BONUSNPC_SPAWN_IDDREADGION_02 = new SM_SYSTEM_MESSAGE(1400633);
	/**
	 * A treasure chest will appear if you defeat Ebonsoul within one minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdC_Light_Die = new SM_SYSTEM_MESSAGE(1400634);
	/**
	 * A treasure chest will appear if you defeat Rukril within one minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdC_Dark_Die = new SM_SYSTEM_MESSAGE(1400635);
	/**
	 * A treasure chest has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdC_BoxSpawn = new SM_SYSTEM_MESSAGE(1400636);
	/**
	 * Yamennes opens the Spawn Gate and begins to summon his minions.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdD_SummonStart = new SM_SYSTEM_MESSAGE(1400637);
	/**
	 * There is no space in the Pet Pouch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_TOO_MANY_ITEMS_TOYPET_WAREHOUSE = new SM_SYSTEM_MESSAGE(1400638);
	/**
	 * Your Favorites list is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_COMBINE_FAVORIT_LIST_FULL = new SM_SYSTEM_MESSAGE(1400639);

	/**
	 * You have entered %WORLDNAME0. Your allies are barred from joining you.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_OPENED_FOR_SELF(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400640, worldname0);
	}

	/**
	 * Supplies Storage teleport device has been created at the Secondary Escape Hatch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WAREHOUSETELEPORTER_CREATED_IDDREADGION_02_02 = new SM_SYSTEM_MESSAGE(1400641);
	/**
	 * This is not a usable pet egg.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PET_NOT_PET_COUPON = new SM_SYSTEM_MESSAGE(1400642);
	/**
	 * That name is invalid. Please try another..
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PET_NOT_AVALIABE_NAME = new SM_SYSTEM_MESSAGE(1400643);

	/**
	 * You abandoned %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_ABANDON_PET_COMPLETE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400644, value0);
	}

	/**
	 * You summoned %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_SUMMONED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400645, value0);
	}

	/**
	 * %0 has been dismissed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_UNSUMMONED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400646, value0);
	}

	/**
	 * You cannot put this item in the Pet Pouch.
	 */
	public static final SM_SYSTEM_MESSAGE STR_WAREHOUSE_CANT_DEPOSIT_ITEM_TOYPET_WAREHOUSE = new SM_SYSTEM_MESSAGE(1400647);
	/**
	 * You cannot combine equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_EQUIPED_ITEM = new SM_SYSTEM_MESSAGE(1400648);
	/**
	 * Life energy begins to course through your body.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BARD_BUFF_LIFE = new SM_SYSTEM_MESSAGE(1400649);
	/**
	 * You feel all your muscles becoming harder.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BARD_BUFF_PROTECTION = new SM_SYSTEM_MESSAGE(1400650);
	/**
	 * You already have the selected pet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_ALREADY_TAMED_PET = new SM_SYSTEM_MESSAGE(1400651);
	/**
	 * Captain's Cabin teleport device has been created at the end of the Central Passage.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSSTELEPORTER_CREATED_IDDREDAGION_02 = new SM_SYSTEM_MESSAGE(1400652);
	/**
	 * There is an object of great power nearby.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_SKILL = new SM_SYSTEM_MESSAGE(1400653);
	/**
	 * You can use a Silver Blade Rotan to destroy the rock door leading to the Temple Vault.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_DOOR = new SM_SYSTEM_MESSAGE(1400654);
	/**
	 * You have acquired the 'Cool Water' effect from the garden fountain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_BUFF_01 = new SM_SYSTEM_MESSAGE(1400655);
	/**
	 * You have acquired the 'Sweet Fruit' effect from the fruit basket.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_BUFF_02 = new SM_SYSTEM_MESSAGE(1400656);
	/**
	 * You have acquired the 'Tasty Meat' effect from the Porgus Barbecue.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_BUFF_03 = new SM_SYSTEM_MESSAGE(1400657);
	/**
	 * You have acquired the 'Prophet's Blessing' effect from the Prophet's Tower.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_BUFF_04 = new SM_SYSTEM_MESSAGE(1400658);
	/**
	 * You sense a movement in Taloc's Roots. You won't be able to meet him unless you hurry.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_HYAS_SPAWN_INFO = new SM_SYSTEM_MESSAGE(1400659);
	/**
	 * Smoke is being discharged. Exposure to smoke will destroy Kinquid's Barrier.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDELIM_GAS_INFO = new SM_SYSTEM_MESSAGE(1400660);
	/**
	 * Sematariux has died. You will be removed from Sematariux's Hideout in 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_30M = new SM_SYSTEM_MESSAGE(1400661);
	/**
	 * You will be removed from Sematariux's Hideout in 25 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_25M = new SM_SYSTEM_MESSAGE(1400662);
	/**
	 * You will be removed from Sematariux's Hideout in 20 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_20M = new SM_SYSTEM_MESSAGE(1400663);
	/**
	 * You will be removed from Sematariux's Hideout in 15 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_15M = new SM_SYSTEM_MESSAGE(1400664);
	/**
	 * You will be removed from Sematariux's Hideout in 10 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_10M = new SM_SYSTEM_MESSAGE(1400665);
	/**
	 * You will be removed from Sematariux's Hideout in 5 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_5M = new SM_SYSTEM_MESSAGE(1400666);
	/**
	 * You will be removed from Sematariux's Hideout in 4 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_4M = new SM_SYSTEM_MESSAGE(1400667);
	/**
	 * You will be removed from Sematariux's Hideout in 3 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_3M = new SM_SYSTEM_MESSAGE(1400668);
	/**
	 * You will be removed from Sematariux's Hideout in 2 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_2M = new SM_SYSTEM_MESSAGE(1400669);
	/**
	 * You will be removed from Sematariux's Hideout in 1 minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_1M = new SM_SYSTEM_MESSAGE(1400670);
	/**
	 * You will be removed from Sematariux's Hideout in 30 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_30S = new SM_SYSTEM_MESSAGE(1400671);
	/**
	 * You will be removed from Sematariux's Hideout in 15 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_15S = new SM_SYSTEM_MESSAGE(1400672);
	/**
	 * You will be removed from Sematariux's Hideout in 10 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_10S = new SM_SYSTEM_MESSAGE(1400673);
	/**
	 * You will be removed from Sematariux's Hideout in 5 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_KILLED_OUT_TIMER_5S = new SM_SYSTEM_MESSAGE(1400674);
	/**
	 * Padmarashka has died. You will be removed from Padmarashka's Cave in 30 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_30M = new SM_SYSTEM_MESSAGE(1400675);
	/**
	 * You will be removed from Padmarashka's Cave in 25 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_25M = new SM_SYSTEM_MESSAGE(1400676);
	/**
	 * You will be removed from Padmarashka's Cave in 20 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_20M = new SM_SYSTEM_MESSAGE(1400677);
	/**
	 * You will be removed from Padmarashka's Cave in 15 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_15M = new SM_SYSTEM_MESSAGE(1400678);
	/**
	 * You will be removed from Padmarashka's Cave in 10 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_10M = new SM_SYSTEM_MESSAGE(1400679);
	/**
	 * You will be removed from Padmarashka's Cave in 5 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_5M = new SM_SYSTEM_MESSAGE(1400680);
	/**
	 * You will be removed from Padmarashka's Cave in 4 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_4M = new SM_SYSTEM_MESSAGE(1400681);
	/**
	 * You will be removed from Padmarashka's Cave in 3 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_3M = new SM_SYSTEM_MESSAGE(1400682);
	/**
	 * You will be removed from Padmarashka's Cave in 2 minutes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_2M = new SM_SYSTEM_MESSAGE(1400683);
	/**
	 * You will be removed from Padmarashka's Cave in 1 minute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_1M = new SM_SYSTEM_MESSAGE(1400684);
	/**
	 * You will be removed from Padmarashka's Cave in 30 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_30S = new SM_SYSTEM_MESSAGE(1400685);
	/**
	 * You will be removed from Padmarashka's Cave in 15 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_15S = new SM_SYSTEM_MESSAGE(1400686);
	/**
	 * You will be removed from Padmarashka's Cave in 10 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_10S = new SM_SYSTEM_MESSAGE(1400687);
	/**
	 * You will be removed from Padmarashka's Cave in 5 seconds.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_KILLED_OUT_TIMER_5S = new SM_SYSTEM_MESSAGE(1400688);
	/**
	 * The destruction of the Huge Aether Fragment has destabilized the artifact!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_Artifact_Die_01 = new SM_SYSTEM_MESSAGE(1400689);
	/**
	 * The destruction of the Huge Aether Fragment has put the artifact protector on alert!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_Artifact_Die_02 = new SM_SYSTEM_MESSAGE(1400690);
	/**
	 * The destruction of the Huge Aether Fragment has caused abnormality on the artifact. The artifact protector is furious!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_Artifact_Die_03 = new SM_SYSTEM_MESSAGE(1400691);

	/**
	 * You may change a pet's name once every %DURATIONTIME1 (Time remaining: %DURATIONTIME0).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_NAME_CHANGE_DELAY(String durationtime1, String durationtime0) {
		return new SM_SYSTEM_MESSAGE(1400692, durationtime1, durationtime0);
	}

	/**
	 * The pet has been renamed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_NAME_CHANGED = new SM_SYSTEM_MESSAGE(1400693);
	/**
	 * You already have a pet of the same name. Please choose another name.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_EXISTING_NAME = new SM_SYSTEM_MESSAGE(1400694);
	/**
	 * You cannot feed it right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_CANT_NOW = new SM_SYSTEM_MESSAGE(1400695);

	/**
	 * %0 indicates that it is not hungry.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_LIMIT_LOVE_COUNT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400696, value0);
	}

	/**
	 * You received %0: %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_PET_TAME_COMPLETE(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400697, value0, value1);
	}

	/**
	 * You cannot feed your pet %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSGBOX_TOYPET_FEED_CANT_FEED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400698, value0);
	}

	/**
	 * You are too far from your pet to feed it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSGBOX_TOYPET_FEED_CANT_FEED_TOO_FAR = new SM_SYSTEM_MESSAGE(1400699);
	/**
	 * You cannot feed your pet while moving.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSGBOX_TOYPET_FEED_CANT_FEED_WHEN_MOVING = new SM_SYSTEM_MESSAGE(1400700);
	/**
	 * You have obtained an object with great power. For quick access, drag the item from your Cube to your Quickbar.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDCROMEDE_SKILL_01 = new SM_SYSTEM_MESSAGE(1400701);

	/**
	 * %0 cannot eat any food.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_PET_CANT_EAT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400702, value0);
	}

	/**
	 * You have obtained a new item from the selected target.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GET_QUEST_ITEM = new SM_SYSTEM_MESSAGE(1400703);
	/**
	 * You must destroy the enemies of Taloc. It allows you to acquire objects with great power.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NOTICE_LOOT_SKILL_ITEM = new SM_SYSTEM_MESSAGE(1400704);
	/**
	 * You cannot kick yourself out of the channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANT_BAN_SELF = new SM_SYSTEM_MESSAGE(1400705);
	/**
	 * You cannot kick yourself out of the channel.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_BAN_SELF = new SM_SYSTEM_MESSAGE(1400706);
	/**
	 * You cannot transfer leadership to yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PARTY_CANT_CHANGE_LEADER_SELF = new SM_SYSTEM_MESSAGE(1400707);
	/**
	 * You cannot transfer leadership to yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_CHANGE_LEADER_SELF = new SM_SYSTEM_MESSAGE(1400708);
	/**
	 * You cannot use it because the version of your package is too low.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NO_RIGHT_PACKAGE_VERSION = new SM_SYSTEM_MESSAGE(1400709);
	/**
	 * Items stored in the surrendered pet's bag have been returned to your cube.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_RETURN_MASTER_ITEM = new SM_SYSTEM_MESSAGE(1400710);

	/**
	 * Your pet's time is up. %0 has gone.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_ABANDON_EXPIRE_TIME_COMPLETE(String value0) {
		return new SM_SYSTEM_MESSAGE(1401194, value0);
	}

	/**
	 * You must defeat the protector within the time limit to wake Padmarashka from the Protective Slumber.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_GUARDIAN_START = new SM_SYSTEM_MESSAGE(1400711);
	/**
	 * Padmarashka has summoned the protector once again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_GUARDIAN_FAIL = new SM_SYSTEM_MESSAGE(1400712);
	/**
	 * Hamerun has dropped a treasure chest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDNOVICE_HAMEROON_TREASUREBOX_SPAWN = new SM_SYSTEM_MESSAGE(1400713);

	/**
	 * You have failed to reclaim %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_RETURN_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400714, value0);
	}

	/**
	 * You have failed to reclaim %num1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_RETURN_ITEM_MULTI(int num1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400715, num1, value0s);
	}

	/**
	 * %0 is grateful and gives you a %1 as a present.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_FEED_CASH_REWARD_CASH_UNLIMITED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400716, value0, value1);
	}

	/**
	 * Cannot find the item to combine.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ITEM_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1400717);
	/**
	 * Cannot find the item to remove the combination from.
	 */
	public static final SM_SYSTEM_MESSAGE STR_DECOMPOUND_ITEM_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1400718);
	/**
	 * You cannot expel the alliance captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_BANISH_LEADER = new SM_SYSTEM_MESSAGE(1400719);
	/**
	 * You cannot appoint yourself as an alliance vice captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_PROMOTE_MANAGER_SELF = new SM_SYSTEM_MESSAGE(1400720);
	/**
	 * You are already appointed as an alliance vice captain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_PROMOTE_MANAGER_AGAIN = new SM_SYSTEM_MESSAGE(1400721);
	/**
	 * You cannot demote yourself to an alliance member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_DEMOTE_MANAGER_SELF = new SM_SYSTEM_MESSAGE(1400722);
	/**
	 * You cannot demote an alliance member.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FORCE_CANT_DEMOTE_MANAGER_AGAIN = new SM_SYSTEM_MESSAGE(1400723);
	/**
	 * Thunder Storm has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_SUMMON_THUNDER = new SM_SYSTEM_MESSAGE(1400724);
	/**
	 * Terra Blast has appeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_SUMMON_EARTH = new SM_SYSTEM_MESSAGE(1400725);
	/**
	 * Acid mist has covered some areas.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_SUMMON_POISON = new SM_SYSTEM_MESSAGE(1400726);
	/**
	 * Sematariux has awoken from the Protective Slumber.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF4_DRAMATA_AWAKENING = new SM_SYSTEM_MESSAGE(1400727);
	/**
	 * Padmarashka has awoken from the Protective Slumber.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF4_DRAMATA_AWAKENING = new SM_SYSTEM_MESSAGE(1400728);
	/**
	 * Yamennes's threat level has been reset!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdD_ResetAggro = new SM_SYSTEM_MESSAGE(1400729);
	/**
	 * A summoned Lapilima is healing Yamennes!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdD_Heal = new SM_SYSTEM_MESSAGE(1400730);
	/**
	 * Yamennes Blindsight has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdD_Wakeup = new SM_SYSTEM_MESSAGE(1400731);
	/**
	 * Yamennes Painflare has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDAbRe_Core_NmdDH_Wakeup = new SM_SYSTEM_MESSAGE(1400732);

	/**
	 * You cannot summon a pet in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_SUMMON_STATE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400733, value0);
	}

	/**
	 * You are in an altered state and cannot summon a pet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_SUMMON_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1400734);

	/**
	 * You cannot dismiss a pet in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_UNSUMMON_STATE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400735, value0);
	}

	/**
	 * You are in an altered state and cannot dismiss a pet.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_UNSUMMON_ABNORMAL_STATE = new SM_SYSTEM_MESSAGE(1400736);

	/**
	 * You must be at least level %0 to perform extraction.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_GATHERING_B_LEVEL_CHECK(String value0) {
		return new SM_SYSTEM_MESSAGE(1400737, value0);
	}

	/**
	 * Item couldn't be registered due to a change in the fees.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_CAN_NOT_REGISTER_ITEM_FEE_CHANGED = new SM_SYSTEM_MESSAGE(1400738);
	/**
	 * The first Sphere of Destiny has been activated.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_FIRST_OBJECT_ON_DF = new SM_SYSTEM_MESSAGE(1400739);
	/**
	 * The second Sphere of Destiny has been activated. Marchutan's Agent Mastarius prepares to cast the Empyrean Lord's blessing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_SECOND_OBJECT_ON_DF = new SM_SYSTEM_MESSAGE(1400740);
	/**
	 * You may use the Sphere of Destiny again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_CAN_USE_OBJECT_DF = new SM_SYSTEM_MESSAGE(1400741);
	/**
	 * You need more people to activate the Sphere of Destiny.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GODELITE_BUFF_CANT_USE_OBJECT_NOT_ENOUGH_MEMBER_DF = new SM_SYSTEM_MESSAGE(1400742);
	/**
	 * Such basic crafting doesn't affect your skill level, Master.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_GET_COMBINE_EXP_GRAND_MASTER = new SM_SYSTEM_MESSAGE(1400743);
	/**
	 * You cannot modify equipped items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHANGE_ITEM_SKIN_CANT_CHANGE_EQUIPED_ITEM_SKIN = new SM_SYSTEM_MESSAGE(1400744);
	/**
	 * You cannot preview an item that you cannot equip.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHANGE_ITEM_SKIN_PREVIEW_INVALID_COSMETIC = new SM_SYSTEM_MESSAGE(1400745);
	/**
	 * High Elder Roamim's threat level has reset!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Underpass_Nephilim_Raid_ResetAggro = new SM_SYSTEM_MESSAGE(1400746);
	/**
	 * High Elder Roamim is furious!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Underpass_Nephilim_Raid_Rage = new SM_SYSTEM_MESSAGE(1400747);
	/**
	 * High Elder Roamim has summoned players.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Underpass_Nephilim_Raid_Recall = new SM_SYSTEM_MESSAGE(1400748);
	/**
	 * You do not have the authority to make this decision.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PARTY_FORCE_NO_RIGHT_TO_DECIDE = new SM_SYSTEM_MESSAGE(1400749);
	/**
	 * You cannot buy an item you have registered yourself.
	 */
	public static final SM_SYSTEM_MESSAGE STR_VENDOR_CAN_NOT_BUY_MY_REGISTER_ITEM = new SM_SYSTEM_MESSAGE(1400750);
	/**
	 * Commander Bakarma has appeared at Beritra's Oracle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDF3_DRAGON = new SM_SYSTEM_MESSAGE(1400751);
	/**
	 * An object of great power waits in your cube. Transform into a mighty being with Taloc's Fruit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_KASPAFRUIT_INFO = new SM_SYSTEM_MESSAGE(1400752);
	/**
	 * An object of great power waits in your cube. Launch a powerful aerial attack with Taloc's Tears.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_KASPATEAR_INFO = new SM_SYSTEM_MESSAGE(1400753);
	/**
	 * An object of great power waits in Shishir's carcass. Obtain it, then register it in the skill window.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_SHISHIR_INFO = new SM_SYSTEM_MESSAGE(1400754);
	/**
	 * An object of great power waits in Gellmar's carcass. Obtain it, then register it in the skill window.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GELMAR_INFO = new SM_SYSTEM_MESSAGE(1400755);
	/**
	 * An object of great power waits in Neith's carcass. Obtain it, then register it in the skill window.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RAGOS_INFO = new SM_SYSTEM_MESSAGE(1400756);
	/**
	 * You must kill Afrane, Saraswati, Lakshmi, and Nimbarka to make Commander Bakarma appear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDF3_DRAGON_1 = new SM_SYSTEM_MESSAGE(1400757);
	/**
	 * You must kill 3 more Adjutants to make Commander Bakarma appear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDF3_DRAGON_2 = new SM_SYSTEM_MESSAGE(1400758);
	/**
	 * You must kill 2 more Adjutants to make Commander Bakarma appear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDF3_DRAGON_3 = new SM_SYSTEM_MESSAGE(1400759);
	/**
	 * You must kill 1 more Adjutant to make Commander Bakarma appear.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BOSS_SPAWN_IDDF3_DRAGON_4 = new SM_SYSTEM_MESSAGE(1400760);

	/**
	 * The %0 sealed by the heat of high summer has been accumulated.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_HCOIN_01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400761, value0);
	}

	/**
	 * You are now a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_START = new SM_SYSTEM_MESSAGE(1400762);

	/**
	 * %0 is now a Mentor.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MENTOR_START_PARTYMSG(String value0) {
		return new SM_SYSTEM_MESSAGE(1400763, value0);
	}

	/**
	 * You are no longer a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_END = new SM_SYSTEM_MESSAGE(1400764);

	/**
	 * %0 is no longer a Mentor.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MENTOR_END_PARTYMSG(String value0) {
		return new SM_SYSTEM_MESSAGE(1400765, value0);
	}

	/**
	 * You cannot enter %WORLDNAME0 with a Mentor in your group.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MENTOR_CANT_ENTER(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400766, worldname0);
	}

	/**
	 * You were forced to leave %WORLDNAME0 because you stopped Mentoring.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MENTOR_END_BANISH(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400767, worldname0);
	}

	/**
	 * %0 was forced to leave %WORLDNAME1 because he or she stopped Mentoring.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MENTOR_END_BANISH_PARTYMSG(String value0, String worldname1) {
		return new SM_SYSTEM_MESSAGE(1400768, value0, worldname1);
	}

	/**
	 * You cannot become a Mentor in %WORLDNAME0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_BE_MENTOR(String worldname0) {
		return new SM_SYSTEM_MESSAGE(1400769, worldname0);
	}

	/**
	 * You have left %0 because of the level limit.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_LEAVE_BY_LEVEL_LIMIT(int value0) {
		return new SM_SYSTEM_MESSAGE(1400770, new DescriptionId(value0));
	}

	/**
	 * None of your group members meet the level requirement for %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_KILL_COUNT_BY_WRONG_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400771, value0);
	}

	/**
	 * None of your group members meet the level requirement for %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_DROP_ITEM_BY_WRONG_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400772, value0);
	}

	/**
	 * You cannot use %1 while Mentoring.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_USE_ITEM_BY_NOT_MENTOR(String value1) {
		return new SM_SYSTEM_MESSAGE(1400773, value1);
	}

	/**
	 * You cannot use %1 while Mentoring.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DONT_USE_ITEM_BY_NOT_MENTEE(String value1) {
		return new SM_SYSTEM_MESSAGE(1400774, value1);
	}

	/**
	 * The Tainted Inina is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_01 = new SM_SYSTEM_MESSAGE(1400775);
	/**
	 * You cannot open it because there are no users of levels 10 - 19.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_02 = new SM_SYSTEM_MESSAGE(1400776);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_03 = new SM_SYSTEM_MESSAGE(1400777);
	/**
	 * Speak to the Kaidan Head Priest while disguised as a Draconute Guard to receive a Tribute Chest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_04 = new SM_SYSTEM_MESSAGE(1400778);
	/**
	 * The Tribute Chest can only be opened by someone disguised as a Drakan Envoy.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_05 = new SM_SYSTEM_MESSAGE(1400779);
	/**
	 * The Lepharist Revolutionary hideout is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_06 = new SM_SYSTEM_MESSAGE(1400780);
	/**
	 * You cannot open it because there are no users of levels 20 - 29.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_07 = new SM_SYSTEM_MESSAGE(1400781);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_08 = new SM_SYSTEM_MESSAGE(1400782);
	/**
	 * The Asmodian hideout is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_09 = new SM_SYSTEM_MESSAGE(1400783);
	/**
	 * You cannot open it because there are no users of levels 30 - 39.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_10 = new SM_SYSTEM_MESSAGE(1400784);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_11 = new SM_SYSTEM_MESSAGE(1400785);
	/**
	 * The Ward Orb is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_01 = new SM_SYSTEM_MESSAGE(1400786);
	/**
	 * You cannot open it because there are no users of levels 10 - 19.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_02 = new SM_SYSTEM_MESSAGE(1400787);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_03 = new SM_SYSTEM_MESSAGE(1400788);
	/**
	 * Speak to the Mau High Priest while disguised as a Draconute Guard to receive a Tribute.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_04 = new SM_SYSTEM_MESSAGE(1400789);
	/**
	 * The Tribute Chest can only be opened by someone disguised as a High Rank Drakan Envoy.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_05 = new SM_SYSTEM_MESSAGE(1400790);
	/**
	 * The Ward Globe is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_06 = new SM_SYSTEM_MESSAGE(1400791);
	/**
	 * You cannot open it because there are no users of levels 20 - 29.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_07 = new SM_SYSTEM_MESSAGE(1400792);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_08 = new SM_SYSTEM_MESSAGE(1400793);
	/**
	 * The Morheim Observatory Auxiliary Device is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_09 = new SM_SYSTEM_MESSAGE(1400794);
	/**
	 * You cannot open it because there are no users of levels 30 - 39.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_10 = new SM_SYSTEM_MESSAGE(1400795);
	/**
	 * You cannot open that without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_11 = new SM_SYSTEM_MESSAGE(1400796);

	/**
	 * %0 has been sealed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_DONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400797, value0);
	}

	/**
	 * Canceled sealing %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_CANCEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400798, value0);
	}

	/**
	 * Are you sure you want to seal it?
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_CONFIRM_START = new SM_SYSTEM_MESSAGE(1400799);
	/**
	 * Are you sure you want to unseal it?
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_CONFIRM_UNSEAL = new SM_SYSTEM_MESSAGE(1400800);
	/**
	 * The seal will be removed immediately.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_CONFIRM_UNSEALINSTANT = new SM_SYSTEM_MESSAGE(1400801);
	/**
	 * Sealed Item
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS = new SM_SYSTEM_MESSAGE(1400802);
	/**
	 * Unseal Pending
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALWAIT = new SM_SYSTEM_MESSAGE(1400803);

	/**
	 * It takes %0 days to remove the seal completely.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_DURATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400804, value0);
	}

	/**
	 * You can have only %0 items pending unsealing at a time.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALMAX(String value0) {
		return new SM_SYSTEM_MESSAGE(1400805, value0);
	}

	/**
	 * %0 is now pending unsealing. This will take 7 days.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALWAIT_START(String value0) {
		return new SM_SYSTEM_MESSAGE(1400806, value0);
	}

	/**
	 * %0 is now pending unsealing. This will take %DURATIONDAY1 days.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALPROGRESS(String value0, String durationday1) {
		return new SM_SYSTEM_MESSAGE(1400807, value0, durationday1);
	}

	/**
	 * %0 is unsealed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALDONE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400808, value0);
	}

	/**
	 * You cannot seal an item that is already sealed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_RESEAL = new SM_SYSTEM_MESSAGE(1400810);
	/**
	 * You cannot trade sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_TRADE = new SM_SYSTEM_MESSAGE(1400811);
	/**
	 * You cannot sell sealed items in a private store.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_SHOP = new SM_SYSTEM_MESSAGE(1400812);
	/**
	 * You cannot sell sealed items at the Broker.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_AUCTION = new SM_SYSTEM_MESSAGE(1400813);
	/**
	 * You cannot mail sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_MAIL = new SM_SYSTEM_MESSAGE(1400814);
	/**
	 * You cannot store sealed items in the account warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_ACCOUNT = new SM_SYSTEM_MESSAGE(1400815);
	/**
	 * You cannot store sealed items in the legion warehouse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_GUILD = new SM_SYSTEM_MESSAGE(1400816);
	/**
	 * You cannot enhance sealed items with enchantment stones.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UP = new SM_SYSTEM_MESSAGE(1400817);
	/**
	 * You cannot enhance sealed items with manastones.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UP_MSTONE = new SM_SYSTEM_MESSAGE(1400818);
	/**
	 * You cannot enhance sealed items with godstones.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UP_GSTONE = new SM_SYSTEM_MESSAGE(1400819);
	/**
	 * You cannot modify the appearance of sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_LOOKCHANGE = new SM_SYSTEM_MESSAGE(1400820);
	/**
	 * You cannot combine sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_MERGE = new SM_SYSTEM_MESSAGE(1400821);
	/**
	 * You cannot destroy sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_DESTROY = new SM_SYSTEM_MESSAGE(1400822);
	/**
	 * You cannot sell sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_SELL = new SM_SYSTEM_MESSAGE(1400823);
	/**
	 * You cannot extract sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_EXTRACT = new SM_SYSTEM_MESSAGE(1400824);
	/**
	 * You cannot remove manastones from sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_REMOVE = new SM_SYSTEM_MESSAGE(1400825);
	/**
	 * You must unseal your items to complete that quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_QUESTFINISH = new SM_SYSTEM_MESSAGE(1400826);
	/**
	 * You cannot stack sealed items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_OVERLAP = new SM_SYSTEM_MESSAGE(1400827);

	/**
	 * You cannot use %0 while you are waiting for more than 3 items to be unsealed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_MAXWAIT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400828, value0);
	}

	/**
	 * Canceled unsealing %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UNSEALCANCEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400829, value0);
	}

	/**
	 * You cannot use %0 on an item that is pending unsealing.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_ALREADYUNSEAL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400830, value0);
	}

	/**
	 * %0 is not a sealed item.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_NOTSEALED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400831, value0);
	}

	/**
	 * Cancel the pending unsealing of another item, and then try again.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_TRYAGAIN = new SM_SYSTEM_MESSAGE(1400832);

	/**
	 * %0 Premium Seal Breaking Scrolls have arrived. They will vanish in 60 minutes or if you log out.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_SCROLLGET(String value0) {
		return new SM_SYSTEM_MESSAGE(1400833, value0);
	}

	/**
	 * The Seal Obliterator has vanished.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_SCROLLDESTROY = new SM_SYSTEM_MESSAGE(1400834);
	/**
	 * You cannot use sealed consumable items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_CANTUSE = new SM_SYSTEM_MESSAGE(1400835);

	/**
	 * Promotion Item Test: You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_ALL_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400836, value0);
	}

	/**
	 * Group Leader Loot is not available when the group leader is a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_SELECT_LEADER_LOOTING_BY_MENTOR = new SM_SYSTEM_MESSAGE(1400837);
	/**
	 * You cannot be a Mentor because all the other Group Members are Mentors.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_BE_MENTOR_BY_LAST_MENTEE = new SM_SYSTEM_MESSAGE(1400838);
	/**
	 * Not available when the Group Leader is a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEADER_LOOTING_IS_UNAVAILABLE = new SM_SYSTEM_MESSAGE(1400839);
	/**
	 * You have stopped Mentoring because the lowest level group member must be at least 10 levels lower than you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_BE_MENTOR_BY_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1400840);
	/**
	 * You have stopped Mentoring because no group members need your help.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_PARTY_END_BY_LEAVE_ALL_MENTEE = new SM_SYSTEM_MESSAGE(1400841);
	/**
	 * The Mentor group has been converted to an alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_PARTY_END_BY_CONVERT_BY_FORCE = new SM_SYSTEM_MESSAGE(1400842);
	/**
	 * You cannot join the group/alliance because your character name is invalid.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_JOIN_PARTY_FORCE_NOT_NORMAL_CHAR_NAME = new SM_SYSTEM_MESSAGE(1400843);

	/**
	 * You cannot seal %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_FAIL1(String value0) {
		return new SM_SYSTEM_MESSAGE(1400844, value0);
	}

	/**
	 * You cannot seal %0 while it is equipped.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_FAIL2(String value0) {
		return new SM_SYSTEM_MESSAGE(1400845, value0);
	}

	/**
	 * You cannot seal a quest item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_FAIL3 = new SM_SYSTEM_MESSAGE(1400846);

	/**
	 * %0 cannot break the combination.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_DICOMPOSITION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400847, value0);
	}

	/**
	 * You can only Mentor a group member at least 10 levels below you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_CANT_START_WITHOUT_MENTOR_TARGET = new SM_SYSTEM_MESSAGE(1400848);
	/**
	 * You can only be a Mentor when you're in a group.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_MENTOR_CANT_START_WHEN_NOT_IN_PARTY = new SM_SYSTEM_MESSAGE(1400849);
	/**
	 * You belong to a Mentor Group. Use the [/Recruit Mentor [your text here]] command to post a message to <Recruit Mentor Group>.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_REGISTER_NORMAL_PARTY_IN_MENTOR_PARTY = new SM_SYSTEM_MESSAGE(1400850);
	/**
	 * Because you belong to an Alliance, you cannot post a <Recruit Group> message. Use /RecruitAllianceMember and the <Recruit Alliance> window instead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_REGISTER_NORMAL_PARTY_IN_FORCE = new SM_SYSTEM_MESSAGE(1400851);
	/**
	 * Your posting to the Find Group window was canceled because you already belong to a Group or Alliance.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_REGISTER_APPLY_IN_PARTY_OR_FORCE = new SM_SYSTEM_MESSAGE(1400852);
	/**
	 * You cannot combine items that are temporarily tradable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_COMPOUND_ERROR_TEMPORARY_EXCHANGE_ITEM = new SM_SYSTEM_MESSAGE(1400853);
	/**
	 * You can receive the daily quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_QUEST_LIMIT_RESET_DAILY = new SM_SYSTEM_MESSAGE(1400854);

	/**
	 * You can receive the daily quest again at %0 in the morning.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_QUEST_LIMIT_START_DAILY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400855, value0);
	}

	/**
	 * You can receive the weekly quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_QUEST_LIMIT_RESET_WEEK = new SM_SYSTEM_MESSAGE(1400856);

	/**
	 * You can receive the weekly quest again at %1 in the morning on %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_QUEST_LIMIT_START_WEEK(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400857, value1, value0);
	}

	/**
	 * Ihr könnt diese Quest in %DURATIONDAY0 ein weiteres Mal annehmen.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_QUEST_COOLTIME_START_LIMITED_DURATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1402676, value0);
	}

	/**
	 * You cannot soul bind a sealed item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_CANT_SOUL_BIND = new SM_SYSTEM_MESSAGE(1400858);

	/**
	 * %0 cannot break the combination.
	 */
	public static SM_SYSTEM_MESSAGE STR_DECOMPOUND_ERROR_NOT_DECOMPOUNDABLE_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400859, value0);
	}

	/**
	 * You cannot continue the quest with %0 equipped.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_QUEST_ERROR_UNEQUIP_QUEST_ITEM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400860, value0);
	}

	/**
	 * That doesn't work without a user of level 35-45.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_12 = new SM_SYSTEM_MESSAGE(1400861);
	/**
	 * That doesn't work without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Li_13 = new SM_SYSTEM_MESSAGE(1400862);
	/**
	 * That doesn't work without a user of level 25 - 35.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_12 = new SM_SYSTEM_MESSAGE(1400863);
	/**
	 * That doesn't work without a Mentor.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Da_13 = new SM_SYSTEM_MESSAGE(1400864);

	/**
	 * You check how %0 feels.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_CARE_01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400865, value0);
	}

	/**
	 * %0 feels a little better.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_UP_01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400866, value0);
	}

	/**
	 * %0 feels much better.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_UP_02(String value0) {
		return new SM_SYSTEM_MESSAGE(1400867, value0);
	}

	/**
	 * %0 feels a lot better.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_UP_02_01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400868, value0);
	}

	/**
	 * You ask %0 to search the area.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_SEARCH_01(String value0) {
		return new SM_SYSTEM_MESSAGE(1400869, value0);
	}

	/**
	 * You cannot issue a command when your cube is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_PET_CONDITION_REWARD_FULL_INVEN = new SM_SYSTEM_MESSAGE(1400870);

	/**
	 * %0 unearthed a buried %1 for you.
	 */
	public static SM_SYSTEM_MESSAGE STR_PET_CONDITION_REWARD_GET(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400871, value0, value1);
	}

	/**
	 * You cannot glide while you are transformed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GLIDE_CANNOT_GLIDE_POLYMORPH_STATUS = new SM_SYSTEM_MESSAGE(1400872);
	/**
	 * You cannot fly while you are transformed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_FLY_CANNOT_FLY_POLYMORPH_STATUS = new SM_SYSTEM_MESSAGE(1400873);
	/**
	 * Pets cannot use this item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_CANNOT_USE = new SM_SYSTEM_MESSAGE(1400874);
	/**
	 * You cannot list an unusable item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_MESSAGE02 = new SM_SYSTEM_MESSAGE(1400875);
	/**
	 * The pet will now automatically loot items on your behalf, except for items that require confirmation (such as Dice Roll items.)
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_MESSAGE01 = new SM_SYSTEM_MESSAGE(1400876);
	/**
	 * The pet can't pick up items that can be shared with other group members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_MESSAGE02 = new SM_SYSTEM_MESSAGE(1400877);
	/**
	 * You cannot use the Pet Loot function when the group is using the Free-for-All loot setting.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_MESSAGE03 = new SM_SYSTEM_MESSAGE(1400878);
	/**
	 * Pet Auto-Buffing activated. Your pet automatically uses Buff Bag items to buff you as old buffs expire.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_USE_START_MESSAGE = new SM_SYSTEM_MESSAGE(1400879);
	/**
	 * Stop Pet Auto-Buffing.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_USE_STOP_MESSAGE = new SM_SYSTEM_MESSAGE(1400880);
	/**
	 * Deactivates the Pet Loot function.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_USE_STOP_MESSAGE = new SM_SYSTEM_MESSAGE(1400881);
	/**
	 * The pet can only eat food.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_USE_CATEGORY_FOOD = new SM_SYSTEM_MESSAGE(1400882);
	/**
	 * The pet can only drink beverages.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_USE_CATEGORY_DRINK = new SM_SYSTEM_MESSAGE(1400883);
	/**
	 * The pet can only use scrolls.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_USE_CATEGORY_SCROLL = new SM_SYSTEM_MESSAGE(1400884);
	/**
	 * Your pet cannot pick up items that require your confirmation.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_ITEM_REMAIN = new SM_SYSTEM_MESSAGE(1400885);
	/**
	 * Your pet cannot pick up items that you must share with other Group Members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LOOTING_PET_ITEM_REMAIN02 = new SM_SYSTEM_MESSAGE(1400886);

	/**
	 * %0 has been conditioned to level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_SUCCESS(DescriptionId descriptionId, int level) {
		return new SM_SYSTEM_MESSAGE(1400887, descriptionId, level);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE2_SUCCESS(DescriptionId descriptionId, int level) {
		return new SM_SYSTEM_MESSAGE(1401335, descriptionId, level);
	}

	/**
	 * You cannot condition %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_FAIL_NOT_CHARGEABLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400888, value0);
	}

	/**
	 * %0 has been conditioned as much as it can be. You cannot condition it to Level %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_FAIL_ALREADY_CHARGED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400889, value0, value1);
	}

	/**
	 * All equipped items are already conditioned. You cannot condition them further.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_ALL_FAIL_ALREADY_CHARGED = new SM_SYSTEM_MESSAGE(1400890);
	/**
	 * The trade has been cancelled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_CANCELED = new SM_SYSTEM_MESSAGE(1400891);
	/**
	 * Successfully conditioned equipped item(s).
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_ALL_COMPLETE = new SM_SYSTEM_MESSAGE(1400892);

	/**
	 * %0 has been conditioned to level %1, and the item's stats have changed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_LEVEL_DOWN(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400893, value0, value1);
	}

	/**
	 * All equipped items have been conditioned to their maximum level, and cannot be conditioned to level %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_ALL_FAIL_EQUIPED_ALREADY_CHARGED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400894, value0);
	}

	/**
	 * None of the equipped items are conditionable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_ALL_FAIL_NO_CHARGEABLE_EQUIPMENT = new SM_SYSTEM_MESSAGE(1400895);
	/**
	 * While the Pet Auto-Buffing is active, you cannot change the contents of the Buff Bag.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DOPING_PET_MESSAGE03 = new SM_SYSTEM_MESSAGE(1400896);
	/**
	 * Please log out of the game and take a break.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TIRED_REMAIN_PLAYTIME_CHINA_1 = new SM_SYSTEM_MESSAGE(1400897);
	/**
	 * Your in-game gains have been reduced to 50% of normal values. Please log out and taking a break.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TIRED_REMAIN_PLAYTIME_CHINA_2 = new SM_SYSTEM_MESSAGE(1400898);
	/**
	 * You can't acquire any quest while fatigued. Please take a break until your fatigue level decreases, and then resume play.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_ACQUIRE_QUEST_FATIGUE = new SM_SYSTEM_MESSAGE(1400899);
	/**
	 * Quests can't be continued or completed while you are fatigued. Please log out and take a break until your fatigue level decreases.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CANT_PROCEED_QUEST_FATIGUE = new SM_SYSTEM_MESSAGE(1400900);

	/**
	 * %0 has been sealed by a GM. You cannot remove this seal.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_GMSEAL_UNSEAL_IMPOSSIBLE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400901, value0);
	}

	/**
	 * %0 has become the Legion Deputy.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONE_GUILD_SUBMASTER(String value0) {
		return new SM_SYSTEM_MESSAGE(1400902, value0);
	}

	/**
	 * %0 has become a Legion Member.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_CHANGE_MEMBER_RANK_DONE_GUILD_NEWBIE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400903, value0);
	}

	/**
	 * You may be unable to use certain skills or items in this area.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ENTERED_SKILL_ITEM_RESTRICTED_AREA = new SM_SYSTEM_MESSAGE(1400904);

	/**
	 * You cannot use %1 in %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_SKILL_ITEM_RESTRICTED_AREA(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400905, value1, value0);
	}

	/**
	 * You cannot use %1 until you reach level %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_TOO_LOW_GUILD_LEVEL(String value1, String value0) {
		return new SM_SYSTEM_MESSAGE(1400906, value1, value0);
	}

	/**
	 * You have already learned this motion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CUSTOMANIMATION_ALREADY_HAS_MOTION = new SM_SYSTEM_MESSAGE(1400907);

	/**
	 * %0 has been conditioned. You cannot condition it further.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_FAIL_ALREADY_FULLY_CHARGED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400908, value0);
	}

	/**
	 * The Outer Protective Wall is gone, and Weapon H is waking from its dormant state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_HugenNM_00 = new SM_SYSTEM_MESSAGE(1400909);
	/**
	 * The Energy Generator is becoming unstable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_HugenNM_01 = new SM_SYSTEM_MESSAGE(1400910);
	/**
	 * The Energy Generator has been destroyed and the power of the Protective Shield has been reduced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_HugenNM_02 = new SM_SYSTEM_MESSAGE(1400911);
	/**
	 * The Energy Generator has been destroyed and the power of the Protective Shield has been greatly reduced.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_HugenNM_03 = new SM_SYSTEM_MESSAGE(1400912);
	/**
	 * The Energy Generator has been destroyed and the Protective Shield has disappeared.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_HugenNM_04 = new SM_SYSTEM_MESSAGE(1400913);
	/**
	 * You pet is sulky and can't feel better until you interact with it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PET_CONDITION_CARE_END = new SM_SYSTEM_MESSAGE(1400914);
	/**
	 * You need the aid of a Mentor to open it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Ask_Mentor = new SM_SYSTEM_MESSAGE(1400915);
	/**
	 * You need the aid of a lower level Group Member to open it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DailyQuest_Ask_Mentee = new SM_SYSTEM_MESSAGE(1400916);

	/**
	 * The %0 motion has expired and can no longer be used.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_CASH_CUSTOMANIMATION_BY_TIMEOUT(String value0) {
		return new SM_SYSTEM_MESSAGE(1400917, value0);
	}

	/**
	 * The Bridge to the Drana Production Lab has been raised.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_01 = new SM_SYSTEM_MESSAGE(1400918);
	/**
	 * Defeat all Drana Production Lab Section Managers to open the Laboratory Yard door.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_02 = new SM_SYSTEM_MESSAGE(1400919);
	/**
	 * The door to the Laboratory Yard is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_03 = new SM_SYSTEM_MESSAGE(1400920);
	/**
	 * The door to the Laboratory Air Conditioning Room is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_04 = new SM_SYSTEM_MESSAGE(1400921);
	/**
	 * The Laboratory Air Conditioning Room Ventilator is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_05 = new SM_SYSTEM_MESSAGE(1400922);
	/**
	 * The Drana Production Lab walkway is now open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_06 = new SM_SYSTEM_MESSAGE(1400923);
	/**
	 * The outer wall of the Bio Lab has collapsed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_07 = new SM_SYSTEM_MESSAGE(1400924);
	/**
	 * The Airship Weapon has appeared in your cube. Register it to the Skill Window to use it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Zone3_Morph_01 = new SM_SYSTEM_MESSAGE(1400925);
	/**
	 * The Recharger is filling your whole body with energy. It seems to be increasing!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Doping_01 = new SM_SYSTEM_MESSAGE(1400926);
	/**
	 * The Shulack Drink is energizing you!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Doping_02 = new SM_SYSTEM_MESSAGE(1400927);

	/**
	 * Round %0 begins!
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_START_ROUND_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400928, value0);
	}

	/**
	 * You have eliminated all enemies in Round %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_COMPLETE_ROUND_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400929, value0);
	}

	/**
	 * You have passed Stage %0!
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_COMPLETE_STAGE_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400930, value0);
	}

	/**
	 * You join Stage %0 Round %1!
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_JOIN_ROUND_IDARENA(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400931, value0, value1);
	}

	/**
	 * You failed the training and have been sent to the Ready Room.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_MOVE_BIRTHAREA_ME_IDARENA = new SM_SYSTEM_MESSAGE(1400932);

	/**
	 * %0 failed the training and has been sent to the Ready Room.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_MOVE_BIRTHAREA_FRIENDLY_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400933, value0);
	}

	/**
	 * You have acquired %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_GET_COIN_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400934, value0);
	}

	/**
	 * You cannot use that because the wind has weakened.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WindPathOff = new SM_SYSTEM_MESSAGE(1400935);
	/**
	 * You cannot re-enter the Crucible until the cooldown time has expired.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_REENTER_INSTANCE_IDARENA = new SM_SYSTEM_MESSAGE(1400936);

	/**
	 * You have acquired %num1 %0s.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_GET_COIN_MULTI_IDARENA(int num1, String value0s) {
		return new SM_SYSTEM_MESSAGE(1400937, num1, value0s);
	}

	/**
	 * You have %0 kinah left in your daily sell limit, and this transaction would exceed that.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_SELL_NPC(long value0) {
		return new SM_SYSTEM_MESSAGE(1400938, value0);
	}

	/**
	 * The %0 has crystallized in your cube.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_HCOIN_02(String value0) {
		return new SM_SYSTEM_MESSAGE(1400939, value0);
	}

	/**
	 * The item you bought has arrived.
	 */
	public static final SM_SYSTEM_MESSAGE STR_POSTMAN_NOTIFY_CASH = new SM_SYSTEM_MESSAGE(1400940);
	/**
	 * Your trading partner has reached the daily Private Store trading limit, so the trade cannot be completed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_PARTNER_SHOP = new SM_SYSTEM_MESSAGE(1400941);

	/**
	 * You have reached the daily Private Store trading limit of %0 Kinah, so the trade cannot be completed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_OWN_SHOP(String value0) {
		return new SM_SYSTEM_MESSAGE(1400942, value0);
	}

	/**
	 * Your trading partner has reached the daily Trading limit, so the trade cannot be completed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_PARTNER_TRADE = new SM_SYSTEM_MESSAGE(1400943);

	/**
	 * You have reached the daily Trading limit of %0 Kinah, so the trade cannot be completed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_OWN_TRADE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400944, value0);
	}

	/**
	 * You have reached the daily Broker limit of %0 Kinah, so the item cannot be listed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_SELL_AUCTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400945, value0);
	}

	/**
	 * You have reached the daily Mail attachment limit of %0 Kinah, so the kinah cannot be sent.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_SEND_MAIL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400946, value0);
	}

	/**
	 * You have reached the daily Mail attachment limit of %0 Kinah, so you cannot receive this kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_RECEIVE_MAIL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400947, value0);
	}

	/**
	 * %0 has declined your invitation to the Mini Fortress Battle.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_REJECT_INVITATION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400948, value0);
	}

	/**
	 * %0 has joined the Mini Fortress Battle.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_ENTERED_PARTY(String value0) {
		return new SM_SYSTEM_MESSAGE(1400949, value0);
	}

	/**
	 * Your trading partner has reached the daily Trading limit, so the trade has been canceled.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_PARTNER_TRADE_LIMIT = new SM_SYSTEM_MESSAGE(1400950);
	/**
	 * You have reached the daily Trading limit, so the trade cannot be completed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_OWN_TRADE_LIMIT = new SM_SYSTEM_MESSAGE(1400951);

	/**
	 * You have invited %0 to join the Mini Game.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_INVITED_HIM(String value0) {
		return new SM_SYSTEM_MESSAGE(1400952, value0);
	}

	/**
	 * %0 cannot accept your Mini Game invitation right now.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_CANT_INVITE_WHEN_HE_IS_ASKED_QUESTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1400953, value0);
	}

	/**
	 * %0 is participating in another Mini Fortress Battle.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_ALREADY_MEMBER_OF_OTHER_GAME(String value0) {
		return new SM_SYSTEM_MESSAGE(1400954, value0);
	}

	/**
	 * The power binding the soul of %0 has weakened, and %1 has vanished.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_DELETE_ITEM_CHANGE_TO_PUBLIC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400955, value0, value1);
	}

	/**
	 * The power binding the soul of %0 with %1 has weakened.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_WILL_DELETE_ITEM_CHANGE_TO_PUBLIC(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400956, value0, value1);
	}

	/**
	 * The treasure chest has disappeared because you failed to destroy the monsters within the time limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDABRECORE_OOPS_REWARD_IS_GONE = new SM_SYSTEM_MESSAGE(1400957);
	/**
	 * The Dredgion Generator has been destroyed. Its Protector is coming soon!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Zone2_Tower_01 = new SM_SYSTEM_MESSAGE(1400958);
	/**
	 * Cannot find the Supplements.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_ENCHANT_ASSISTANT_CANNOT_FIND = new SM_SYSTEM_MESSAGE(1400959);
	/**
	 * You cannot use sealed Supplements.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_ENCHANT_ASSISTANT_SEALED = new SM_SYSTEM_MESSAGE(1400960);
	/**
	 * You cannot use those Supplements.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_ENCHANT_ASSISTANT_NO_RIGHT_ITEM = new SM_SYSTEM_MESSAGE(1400961);

	/**
	 * %0 dropped out of training and left the Empyrean Crucible.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FRIENDLY_LEAVE_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400962, value0);
	}

	/**
	 * Training is in progress. You must stay in the Ready Room until you can join.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ENTERED_BIRTHAREA_IDARENA = new SM_SYSTEM_MESSAGE(1400963);

	/**
	 * %0 has reentered the Illusion Stadium.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FRIENDLY_MOVE_COMBATAREA_IDARENA(String value0) {
		return new SM_SYSTEM_MESSAGE(1400964, value0);
	}

	/**
	 * You do not have enough kinah to condition that item.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_CHARGE_NOT_ENOUGH_GOLD = new SM_SYSTEM_MESSAGE(1400965);

	/**
	 * All fatigue is gone. You have %0 Fatigue Recovery remaining.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_STATE_RECOVERED(String value0) {
		return new SM_SYSTEM_MESSAGE(1400966, value0);
	}

	/**
	 * You have reached maximum Fatigue, and so can obtain only limited XP, AP, and items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_STATE_INFO_STATE_CHANGE = new SM_SYSTEM_MESSAGE(1400967);
	/**
	 * You are very Fatigued, and so can obtain only limited XP, AP, and items.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_STATE_INFO = new SM_SYSTEM_MESSAGE(1400968);
	/**
	 * You feel refreshed, and your Fatigue is gone.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_FATIGUE_RESET = new SM_SYSTEM_MESSAGE(1400969);

	/**
	 * You cannot extract items while %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_CHARGE_INVALID_STANCE(String value0) {
		return new SM_SYSTEM_MESSAGE(1400970, value0);
	}

	/**
	 * %0 quit the Mini Fortress Battle, and %1 became the leader.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_TOWER_DEFENCE_HOST_MOVED(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400971, value0, value1);
	}

	/**
	 * Looted!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PET_LOOTING_DIALOG = new SM_SYSTEM_MESSAGE(1400972);
	/**
	 * Buff the Master!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_PET_DOPING_DIALOG = new SM_SYSTEM_MESSAGE(1400973);

	/**
	 * %0 succeeded in crafting %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_COMBINE_BROADCAST_COMBINE_SUCCESS(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1400974, value0, value1);
	}

	/**
	 * A Worthiness Ticket Box has appeared in the Illusion Stadium.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S1_ResurBox1_01 = new SM_SYSTEM_MESSAGE(1400975);
	/**
	 * A Worthiness Ticket Box has appeared in the Ready Room.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S3_ResurBox1_01 = new SM_SYSTEM_MESSAGE(1400976);
	/**
	 * A Worthiness Ticket Box has appeared in the Ready Room.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S6_ResurBox1_01 = new SM_SYSTEM_MESSAGE(1400977);
	/**
	 * You can earn an additional reward if you catch the Saam King.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S2_SAAM_CTRL_01 = new SM_SYSTEM_MESSAGE(1400978);
	/**
	 * King Saam will disappear in 30 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S2_Saam1_01 = new SM_SYSTEM_MESSAGE(1400979);
	/**
	 * King Saam will disappear in 10 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S2_Saam1_02 = new SM_SYSTEM_MESSAGE(1400980);
	/**
	 * King Saam will disappear in 5 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S2_Saam1_03 = new SM_SYSTEM_MESSAGE(1400981);
	/**
	 * The Drakies will appear soon!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S4_Draky_CTRL_01 = new SM_SYSTEM_MESSAGE(1400982);
	/**
	 * The Ornate Treasure Chest has appeared in the Illusion Stadium!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S7_BookBox_01 = new SM_SYSTEM_MESSAGE(1400983);
	/**
	 * Lightning Drakie has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S9_DuskDraky_55_Ah_01 = new SM_SYSTEM_MESSAGE(1400984);

	/**
	 * You must be under level %0 to join.
	 */
	public static SM_SYSTEM_MESSAGE STR_FACTION_JOIN_ERROR_MAX_LEVEL(String value0) {
		return new SM_SYSTEM_MESSAGE(1400985, value0);
	}

	/**
	 * Administrator Arminos has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S3_Elemeltal_CTRL_01 = new SM_SYSTEM_MESSAGE(1400986);
	/**
	 * Administrator Arminos has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S4_Draky_CTRL_00 = new SM_SYSTEM_MESSAGE(1400987);
	/**
	 * 3...
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S4_Draky_CTRL_02 = new SM_SYSTEM_MESSAGE(1400988);
	/**
	 * 2...
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S4_Draky_CTRL_03 = new SM_SYSTEM_MESSAGE(1400989);
	/**
	 * 1...
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S4_Draky_CTRL_04 = new SM_SYSTEM_MESSAGE(1400990);
	/**
	 * Administrator Arminos has appeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S6_Ghost_55_Ah_01 = new SM_SYSTEM_MESSAGE(1400991);
	/**
	 * Lightning Drakie has disappeared!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_S9_DuskDraky_55_Ah_02 = new SM_SYSTEM_MESSAGE(1400992);
	/**
	 * You must have a Mentor with you in order to complete this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_KILL_COUNT_WITHOUT_MENTOR = new SM_SYSTEM_MESSAGE(1400993);
	/**
	 * You must be Mentoring someone in order to complete this quest.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_KILL_COUNT_WITHOUT_MENTEE = new SM_SYSTEM_MESSAGE(1400994);
	/**
	 * Cannot find the location for the selected quest step.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NEW_MAP_QUEST_CANT_FIND_NPC = new SM_SYSTEM_MESSAGE(1400995);
	/**
	 * The Surkana Supplier has overloaded.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_08 = new SM_SYSTEM_MESSAGE(1400996);
	/**
	 * The Surkana Steam Jet has generated an updraft.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_09 = new SM_SYSTEM_MESSAGE(1400997);
	/**
	 * Management Director Surama uses Collapsing Earth.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_10 = new SM_SYSTEM_MESSAGE(1400998);
	/**
	 * A large number of Balaur Troopers descend from the Dredgion.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_01 = new SM_SYSTEM_MESSAGE(1400999);
	/**
	 * Kamara explodes.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_02 = new SM_SYSTEM_MESSAGE(1401000);
	/**
	 * Norris's eyes turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_03 = new SM_SYSTEM_MESSAGE(1401001);
	/**
	 * The eyes of King Consierd turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_04 = new SM_SYSTEM_MESSAGE(1401002);
	/**
	 * The eyes of Takun the Terrible turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_05 = new SM_SYSTEM_MESSAGE(1401003);
	/**
	 * The eyes of Gojira turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_06 = new SM_SYSTEM_MESSAGE(1401004);
	/**
	 * The eyes of Andre turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_07 = new SM_SYSTEM_MESSAGE(1401005);
	/**
	 * The eyes of Kamara turn red.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_08 = new SM_SYSTEM_MESSAGE(1401006);
	/**
	 * Unlimited Battle Temporary System Message
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_09 = new SM_SYSTEM_MESSAGE(1401007);
	/**
	 * Unlimited Battle Temporary System Message
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_10 = new SM_SYSTEM_MESSAGE(1401008);
	/**
	 * A Worthiness Ticket has appeared in your cube.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ResurBox = new SM_SYSTEM_MESSAGE(1401009);
	/**
	 * Spirits will disappear in 30 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S3_Bonus_01 = new SM_SYSTEM_MESSAGE(1401010);
	/**
	 * Spirits will disappear in 10 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S3_Bonus_02 = new SM_SYSTEM_MESSAGE(1401011);
	/**
	 * Spirits will disappear in 5 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S3_Bonus_03 = new SM_SYSTEM_MESSAGE(1401012);
	/**
	 * Drakies will disappear in 30 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S4_Bonus_01 = new SM_SYSTEM_MESSAGE(1401013);
	/**
	 * Drakies will disappear in 10 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S4_Bonus_02 = new SM_SYSTEM_MESSAGE(1401014);
	/**
	 * Drakies will disappear in 5 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S4_Bonus_03 = new SM_SYSTEM_MESSAGE(1401015);
	/**
	 * Administrator Arminos will disappear in 30 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S6_Bonus_01 = new SM_SYSTEM_MESSAGE(1401016);
	/**
	 * Administrator Arminos will disappear in 10 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S6_Bonus_02 = new SM_SYSTEM_MESSAGE(1401017);
	/**
	 * Administrator Arminos will disappear in 5 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S6_Bonus_03 = new SM_SYSTEM_MESSAGE(1401018);
	/**
	 * Lightning Drakie will disappear in 30 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S9_Bonus_01 = new SM_SYSTEM_MESSAGE(1401019);
	/**
	 * Lightning Drakie will disappear in 10 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S9_Bonus_02 = new SM_SYSTEM_MESSAGE(1401020);
	/**
	 * Lightning Drakie will disappear in 5 seconds!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_S9_Bonus_03 = new SM_SYSTEM_MESSAGE(1401021);
	/**
	 * Because you belong to an Alliance, you cannot post a <Recruit Mentor Group> message. Use /RecruitAllianceMember and the <Recruit Alliance> window instead.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_REGISTER_MENTOR_PARTY_IN_FORCE = new SM_SYSTEM_MESSAGE(1401022);
	/**
	 * You can see Omega's Recharger. Certainly there would be no harm in trying it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Doping_01_AD = new SM_SYSTEM_MESSAGE(1401023);
	/**
	 * You can see the Energy Drink Can that Shulacks often drink. Certainly there would be no harm in trying it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDStation_Doping_02_AD = new SM_SYSTEM_MESSAGE(1401024);
	/**
	 * You failed to purchase the item. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CASH_PURCHASE_ERROR_FAILED_RECEIVING_PRODUCT = new SM_SYSTEM_MESSAGE(1401025);
	/**
	 * That item is invalid.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CASH_PURCHASE_ERROR_INVALID_PRODUCT = new SM_SYSTEM_MESSAGE(1401026);

	/**
	 * You have acquired the %0 motion.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_CASH_CUSTOMIZE_MOTION(String value0) {
		return new SM_SYSTEM_MESSAGE(1401029, value0);
	}

	/**
	 * One of the Distribution Targets has reached the daily Trading limit, so the trade cannot be completed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DAY_CANNOT_SHARE_TRADE_LIMIT = new SM_SYSTEM_MESSAGE(1401034);
	/**
	 * Dalia Charlands has vanished.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_11 = new SM_SYSTEM_MESSAGE(1401036);
	/**
	 * The Surkana Supplier has been broken.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF4Re_Drana_12 = new SM_SYSTEM_MESSAGE(1401037);
	/**
	 * System error. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CASH_PURCHASE_ERROR_SYSTEM_ERROR = new SM_SYSTEM_MESSAGE(1401038);
	/**
	 * Your cube is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CASH_PURCHASE_ERROR_NOT_ENOUGH_SPACE = new SM_SYSTEM_MESSAGE(1401039);
	/**
	 * You cannot summon a pet here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_SUMMON_MOVING_STATE = new SM_SYSTEM_MESSAGE(1401040);
	/**
	 * You cannot fight a duel here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DUEL_CANT_IN_THIS_ZONE = new SM_SYSTEM_MESSAGE(1401047);
	/**
	 * You have been disconnected from the Bid Withdrawal Server. Please try again later.
	 */
	public static final SM_SYSTEM_MESSAGE STR_ITEM_BILLING_SERVER_DOWN_SA2 = new SM_SYSTEM_MESSAGE(1401051);
	/**
	 * You can not verify Pet Status in the current state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_SHOPOPEN_STATE = new SM_SYSTEM_MESSAGE(1401052);

	/**
	 * You have already requested entry into %WORLDNAME0 (Difficulty: %1).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_ALREADY_REGISTERED_WITH_DIFFICULTY(String worldname0, String value1) {
		return new SM_SYSTEM_MESSAGE(1401053, worldname0, value1);
	}

	/**
	 * You cannot make more entry requests.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_INSTANCE_NO_MORE_REGISTER = new SM_SYSTEM_MESSAGE(1401054);

	/**
	 * You have aborted entering %0 (Difficulty: %1). You may apply again in 10 seconds.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_REGISTER_CANCELED_WTH_DIFFICULTY(String value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1401055, value0, value1);
	}

	/**
	 * NEW
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_NOTICE_BLANK = new SM_SYSTEM_MESSAGE(1401057);

	/**
	 * You have entered %WORLDNAME0. Your allies are barred from joining you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_OPENED_FOR_SELF(int worldId) {
		return new SM_SYSTEM_MESSAGE(1400640, worldId);
	}

	/**
	 * The time for group member recruitment has expired. You cannot recruit more group members.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_REINFORCE_MEMBER = new SM_SYSTEM_MESSAGE(1401058);
	/**
	 * You cannot use the commands Invite to Group or Invite to Legion right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_INVITE_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401059);
	/**
	 * You cannot use the commands Assign Group Leader and Make Alliance Captain right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_CHANGE_LEADER_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401060);
	/**
	 * You cannot use the commands Leave Group or Leave Alliance right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_QUIT_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401061);
	/**
	 * You cannot use the commands Ban Group or Ban from Alliance right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_BANISH_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401062);
	/**
	 * You cannot use the commands Distribute Cash to Group or Distribute Cash to Alliance settings right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_LOOT_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401063);
	/**
	 * You cannot change group or alliance members right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_ARRANGE_MEMBER_FORCE_COMMAND = new SM_SYSTEM_MESSAGE(1401064);
	/**
	 * Poppy is running from the Dukaki Cooks. Eliminate them and help Poppy to reach the refuge.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_START_BROADCAST = new SM_SYSTEM_MESSAGE(1401067);
	/**
	 * There are 5 Dukaki Cooks remaining.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_5Dead_BROADCAST = new SM_SYSTEM_MESSAGE(1401068);
	/**
	 * Careful! Poppy's health is very low.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_AllDead_BROADCAST = new SM_SYSTEM_MESSAGE(1401069);
	/**
	 * Poppy has almost reached the refuge. Just a little bit further!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_HideNear_BROADCAST = new SM_SYSTEM_MESSAGE(1401070);
	/**
	 * Poppy has reached the refuge safely. A successful rescue!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_HideSucc_BROADCAST = new SM_SYSTEM_MESSAGE(1401071);
	/**
	 * You have eliminated all of the Dukaki Cooks and successfully rescued Poppy!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_Succ_BROADCAST = new SM_SYSTEM_MESSAGE(1401072);
	/**
	 * You already have a pet of this type.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_CANT_USE_ALREADY_HAS_PET = new SM_SYSTEM_MESSAGE(1401073);
	/**
	 * You already have a pack pet with this functionality.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_TOYPET_ALREADY_SAME_WAREHOUSE_PET = new SM_SYSTEM_MESSAGE(1401074);
	/**
	 * Poppy was captured by the Dukaki Cooks... and roasted whole!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_Failed_BROADCAST = new SM_SYSTEM_MESSAGE(1401075);
	/**
	 * The Dukaki Cooks attacked and wounded Poppy!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_LowHP_BROADCAST = new SM_SYSTEM_MESSAGE(1401082);
	/**
	 * Poppy was attacked by the Dukaki Cooks. They're planning to roast Poppy for dinner!
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDArena_Solo_SB1_LowHP2_BROADCAST = new SM_SYSTEM_MESSAGE(1401083);
	/**
	 * Smash the Meat Barrel to lure and destroy the Starved Karnifs.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_IDArena_Solo_S4_System1 = new SM_SYSTEM_MESSAGE(1401084);
	/**
	 * Smash the Aether Barrel to lure and destroy the Thirsty Spirits.
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_IDArena_Solo_S4_System2 = new SM_SYSTEM_MESSAGE(1401085);
	/**
	 * Stop Gomju from perpetrating a senseless massacre!
	 */
	public static final SM_SYSTEM_MESSAGE STR_CHAT_IDArena_Solo_S4_System5 = new SM_SYSTEM_MESSAGE(1401086);
	/**
	 * You cannot use the commands /RecruitGroupMember or /RecruitAllianceMember right now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_CANT_POST_PARTY_COMMAND = new SM_SYSTEM_MESSAGE(1401098);
	/**
	 * This emblem is already registered.
	 */
	public static final SM_SYSTEM_MESSAGE STR_GUILD_ALREADY_POSTED_THIS_EMBLEM = new SM_SYSTEM_MESSAGE(1401142);
	/**
	 * A Master cannot take Work Orders.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DONT_GET_COMBINETASK_MASTER = new SM_SYSTEM_MESSAGE(1401182);
	/**
	 * UI Zone messages
	 */
	/**
	 * You have already learned this title.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOOLTIP_LEARNED_TITLE = new SM_SYSTEM_MESSAGE(901714);

	/**
	 * You do not have enough %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_INGAMESHOP_NOT_ENOUGH_CASH(String value0) {
		return new SM_SYSTEM_MESSAGE(901706, value0);
	}

	/**
	 * You have already learned this emote.
	 */
	public static final SM_SYSTEM_MESSAGE STR_TOOLTIP_LEARNED_EMOTION = new SM_SYSTEM_MESSAGE(901713);
	/**
	 * You can only use this when you have a Plastic Surgery Ticket.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EDIT_CHAR_ALL_CANT_NO_ITEM = new SM_SYSTEM_MESSAGE(901752);
	/**
	 * You can only use this when you have a Gender Switch Ticket.
	 */
	public static final SM_SYSTEM_MESSAGE STR_EDIT_CHAR_GENDER_CANT_NO_ITEM = new SM_SYSTEM_MESSAGE(901754);

	/**
	 * Coordinates of current location: %WORLDNAME0 Region, X=%1 Y=%2 Z=%3
	 */
	public static SM_SYSTEM_MESSAGE STR_CMD_LOCATION_DESC(int worldId, float x, float y, float z) {
		return new SM_SYSTEM_MESSAGE(230038, worldId, x, y, z);
	}

	/**
	 * Busy in game
	 */
	public static final SM_SYSTEM_MESSAGE STR_BUDDYLIST_BUSY = new SM_SYSTEM_MESSAGE(900847);

	/**
	 * You don't have enough Kinah. It costs %num0 Kinah.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_NOT_ENOUGH_KINA(long num0) {
		return new SM_SYSTEM_MESSAGE(901285, num0);
	}

	/**
	 * You cannot use it because you belong to a different race.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MOVE_PORTAL_ERROR_INVALID_RACE = new SM_SYSTEM_MESSAGE(901354);
	/**
	 * Only the conquering Legion may enter.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MOVE_PORTAL_ERROR_INVALID_LEGION = new SM_SYSTEM_MESSAGE(904641);
	/**
	 * You are not authorized to examine the corpse.
	 */
	public static final SM_SYSTEM_MESSAGE STR_LOOT_NO_RIGHT = new SM_SYSTEM_MESSAGE(901338);

	/**
	 * The Legion was leveled up to %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_GUILD_EVENT_LEVELUP(int newLevel) {
		return new SM_SYSTEM_MESSAGE(900700, newLevel);
	}

	/**
	 * %0 disappears in 10 minutes because your Abyss Rank changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_UNEQUIP_RANKITEM_TIMER_10M(int value0) {
		return new SM_SYSTEM_MESSAGE(1401327, new DescriptionId(value0));
	}

	/**
	 * %0 disappears in 1 minute because your Abyss Rank changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_UNEQUIP_RANKITEM_TIMER_1M(int value0) {
		return new SM_SYSTEM_MESSAGE(1401328, new DescriptionId(value0));
	}

	/**
	 * %0 disappeared because your Abyss Rank changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_UNEQUIP_RANKITEM(int value0) {
		return new SM_SYSTEM_MESSAGE(1401329, new DescriptionId(value0));
	}

	// Housing messages
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_INS_OWN_SUCCESS = new SM_SYSTEM_MESSAGE(1401275);
	/**
	 * You are not authorized to enter.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_ENTER_NO_RIGHT2 = new SM_SYSTEM_MESSAGE(1401364);
	/**
	 * You cannot enter the house until it sells.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_ENTER_HAVE_TO_RECREATE = new SM_SYSTEM_MESSAGE(1401365);

	/**
	 * You must equip %0 to use it.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_USE_HOUSE_OBJECT_ITEM_EQUIP(DescriptionId id) {
		return new SM_SYSTEM_MESSAGE(1401294, id);
	}

	/**
	 * You cannot use it as you don't have enough %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANT_USE_HOUSE_OBJECT_ITEM_CHECK(DescriptionId id) {
		return new SM_SYSTEM_MESSAGE(1401199, id);
	}

	/**
	 * You are too far away.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_TOO_FAR_TO_USE = new SM_SYSTEM_MESSAGE(1401297);
	/**
	 * Only the owner can use it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_IS_ONLY_FOR_OWNER_VALID = new SM_SYSTEM_MESSAGE(1401298);
	/**
	 * It is unavailable.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_ALL_CANT_USE = new SM_SYSTEM_MESSAGE(1401296);
	/**
	 * You have reached the maximum usage count.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_ACHIEVE_USE_COUNT = new SM_SYSTEM_MESSAGE(1401295);
	/**
	 * The item's cooldown time has yet to expire.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANNOT_USE_FLOWERPOT_COOLTIME = new SM_SYSTEM_MESSAGE(1401280);
	/**
	 * Another cabinet is already open.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ALREADY_OPEN_ANOTHER_STORAGE = new SM_SYSTEM_MESSAGE(1401282);
	/**
	 * You can use it only once a day.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_CANT_USE_PER_DAY = new SM_SYSTEM_MESSAGE(1401260);

	/**
	 * You already have the %0% and cannot reuse %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_USE_ALREADY_HAVE_REWARD_ITEM(int rewardNameId, int objectNameId) {
		return new SM_SYSTEM_MESSAGE(1401396, new DescriptionId(rewardNameId), calculateNameId(objectNameId));
	}

	/**
	 * It is already occupied.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_OCCUPIED_BY_OTHER = new SM_SYSTEM_MESSAGE(1401256);

	/**
	 * Using %0%.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_USE(int objectNameId) {
		return new SM_SYSTEM_MESSAGE(1401257, calculateNameId(objectNameId));
	}

	/**
	 * %0 is worn out and useless.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_DELETE_EXPIRE_TIME(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401261, calculateNameId(nameId));
	}

	/**
	 * %0 is is no longer available.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_DELETE_USE_COUNT(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401262, calculateNameId(nameId));
	}

	/**
	 * You have acquired %1% from %0%.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_REWARD_ITEM(int objectNameId, int rewardItemNameId) {
		return new SM_SYSTEM_MESSAGE(1401263, calculateNameId(objectNameId), new DescriptionId(rewardItemNameId));
	}

	/**
	 * You have removed the paint from %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_PAINT_REMOVE_SUCCEED(int objectNameId) {
		return new SM_SYSTEM_MESSAGE(1401435, calculateNameId(objectNameId));
	}

	/**
	 * You can only paint decor that you own.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_PAINT_ERROR_NOTOWNER = new SM_SYSTEM_MESSAGE(1401438);
	/**
	 * You cannot paint this decor.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_PAINT_ERROR_CANNOTPAINT = new SM_SYSTEM_MESSAGE(1401439);
	/**
	 * This decor is yet to be painted.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_PAINT_ERROR_CANNOTREMOVE = new SM_SYSTEM_MESSAGE(1401440);

	/**
	 * You have painted %0 with %1.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_PAINT_SUCCEED(int objectNameId, int colorItemNameId) {
		return new SM_SYSTEM_MESSAGE(1401436, calculateNameId(objectNameId), new DescriptionId(colorItemNameId));
	}

	/**
	 * Your cube is full.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_USE_HOUSE_OBJECT_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1401200);
	/**
	 * You freed the object.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_CANCEL_USE = new SM_SYSTEM_MESSAGE(1401258);

	/**
	 * You reached your %0 usage goal.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_FLOWERPOT_GOAL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401333, calculateNameId(nameId));
	}

	/**
	 * Changed House Settings.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_ORDER_OPEN_DOOR = new SM_SYSTEM_MESSAGE(1401379);
	/**
	 * Changed House Settings.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_ORDER_CLOSE_DOOR_WITHOUT_FRIENDS = new SM_SYSTEM_MESSAGE(1401380);
	/**
	 * Changed House Settings.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_ORDER_CLOSE_DOOR_ALL = new SM_SYSTEM_MESSAGE(1401381);
	/**
	 * Only Friends and Legion Members remain.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_ORDER_OUT_WITHOUT_FRIENDS = new SM_SYSTEM_MESSAGE(1401382);
	/**
	 * All were kicked out.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_ORDER_OUT_ALL = new SM_SYSTEM_MESSAGE(1401383);
	/**
	 * You were evicted by the house's owner.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_REQUEST_OUT = new SM_SYSTEM_MESSAGE(1401384);
	/**
	 * The house's owner has changed.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CHANGE_OWNER = new SM_SYSTEM_MESSAGE(1401385);
	/**
	 * You need a house for that.
	 */
	public static final SM_SYSTEM_MESSAGE STR_HOUSING_TELEPORT_NEED_HOUSE = new SM_SYSTEM_MESSAGE(1401357);
	/**
	 * You need a studio to enter.
	 */
	public static final SM_SYSTEM_MESSAGE STR_HOUSING_ENTER_NEED_HOUSE = new SM_SYSTEM_MESSAGE(1401359);
	/**
	 * You cannot decorate in the current state.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_MODE_CANNOT_START = new SM_SYSTEM_MESSAGE(1401358);
	/**
	 * Only house owners and their friends may enter.
	 */
	public static final SM_SYSTEM_MESSAGE STR_HOUSING_TELEPORT_CANT_USE = new SM_SYSTEM_MESSAGE(1401244);
	/**
	 * You haven't had any interactions recently.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NO_RELATIONSHIP_RECENTLY = new SM_SYSTEM_MESSAGE(1401504);

	/**
	 * %0 is gone.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OBJECT_DELETE_USE_COUNT_FINAL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401470, calculateNameId(nameId));
	}

	// House Bidding messages

	/**
	 * You made a bid for %addr0.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_BID_SUCCESS(int address) {
		return new SM_SYSTEM_MESSAGE(1401265, address);
	}

	/**
	 * You have been passed over in favor of a higher bid.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_BID_CANCEL = new SM_SYSTEM_MESSAGE(1401266);

	/**
	 * %addr0 is sold to you.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_BID_WIN(int address) {
		return new SM_SYSTEM_MESSAGE(1401267, address);
	}

	/**
	 * You listed %addr0 for auction.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_AUCTION_MY_HOUSE(int address) {
		return new SM_SYSTEM_MESSAGE(1401268, address);
	}

	/**
	 * You successfully auctioned %addr0.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_AUCTION_SUCCESS(int address) {
		return new SM_SYSTEM_MESSAGE(1401269, address);
	}

	/**
	 * Listed %addr0 was not auctioned.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_AUCTION_FAIL(int address) {
		return new SM_SYSTEM_MESSAGE(1401270, address);
	}

	/**
	 * Already listed. Please refresh your list.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_AUCTION_FAIL_ALREADY_REGISTED = new SM_SYSTEM_MESSAGE(1401372);
	/**
	 * You cannot make a bid now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_TIMEOUT = new SM_SYSTEM_MESSAGE(1401274);
	/**
	 * You cannot register now.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_AUCTION_TIMEOUT = new SM_SYSTEM_MESSAGE(1401308);

	/**
	 * You must complete %quest0 first.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(int questId) {
		return new SM_SYSTEM_MESSAGE(1401277, questId);
	}

	/**
	 * You already have a house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_INS_CANT_OWN_MORE_HOUSE = new SM_SYSTEM_MESSAGE(1401276);
	/**
	 * You cannot place this bid because the amount exceeds the bid limit.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_EXCESS_ACCOUNT = new SM_SYSTEM_MESSAGE(1401497);

	/**
	 * The home you have made an offer for has a new high bid of %num0 Kinah.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_PRICE_CHANGE(long kinah) {
		return new SM_SYSTEM_MESSAGE(1401324, kinah);
	}

	/**
	 * You cannot make a bid for your own house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_MY_HOUSE = new SM_SYSTEM_MESSAGE(1401221);
	/**
	 * You can only bid on a house one time.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_SUCC_BID_HOUSE = new SM_SYSTEM_MESSAGE(1401222);
	/**
	 * You are currently the highest bidder for another house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_OTHER_HOUSE = new SM_SYSTEM_MESSAGE(1401223);
	/**
	 * You may bid after the grace period ends on your other house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_GRACE_HOUSE = new SM_SYSTEM_MESSAGE(1401224);

	/**
	 * You must be Level %0 or higher to bid on the house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_LOW_LEVEL(int minLevel) {
		return new SM_SYSTEM_MESSAGE(1401225, minLevel);
	}

	/**
	 * Your housing payment is due. Please pay your maintenance costs.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_OVERDUE = new SM_SYSTEM_MESSAGE(1401226);
	/**
	 * Your house has been seized against your unpaid maintenance fees.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_SEQUESTRATE = new SM_SYSTEM_MESSAGE(1401227);
	/**
	 * You cannot bid until you pay the maintenance fees on your house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_BID_OVERDUE = new SM_SYSTEM_MESSAGE(1401349);
	/**
	 * There is no need to pay a maintenance fee for this house.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_F2P_CASH_HOUSE_FEE_FREE = new SM_SYSTEM_MESSAGE(1401445);
	/**
	 * You need to pay the maintenance fee to list it.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_CANT_AUCTION_OVERDUE = new SM_SYSTEM_MESSAGE(1401317);
	/**
	 * The script is too long to apply here.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOUSING_SCRIPT_OVERFLOW = new SM_SYSTEM_MESSAGE(1401399);
	/**
	 * Danaria 4.3
	 */
	public static final SM_SYSTEM_MESSAGE STR_LDF5B_6021_OUT_DOOR_01_DESPAWN = new SM_SYSTEM_MESSAGE(1401690);
	public static final SM_SYSTEM_MESSAGE STR_LDF5B_6021_OUT_DOOR_02_DESPAWN = new SM_SYSTEM_MESSAGE(1401691);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_DUNGEON_COUNT_USE = new SM_SYSTEM_MESSAGE(1402084);

	/**
	 * %0's Idian is fully charged.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_POLISH_SUCCEED(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401650, new DescriptionId(nameId));
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_REIDENTIFY_CANNOT_REIDENTIFY(final int nameId) {
		return new SM_SYSTEM_MESSAGE(1401636, new Object[] { new DescriptionId(nameId) });
	}

	/**
	 * You cannot socket %0 with Idian.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_CANNOT_POLISH_ITEM(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1401648, descriptionId);
	}

	/**
	 * You have gained %num0 Glory Points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GLORY_POINT_GAIN(int num0) {
		return new SM_SYSTEM_MESSAGE(1402081, num0);
	}

	/**
	 * You lost personal %num0 Glory Points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GLORY_POINT_LOSE_PERSONAL(int num0) {
		return new SM_SYSTEM_MESSAGE(1402209, num0);
	}

	/**
	 * You loose %num0 Glory Points.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GLORY_POINT_LOSE_COMMON(int num0) {
		return new SM_SYSTEM_MESSAGE(1402219, num0);
	}

	/**
	 * Authorize System
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_AUTHORIZE_CANCEL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1402147, new Object[] { new DescriptionId(nameId) });
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_AUTHORIZE_SUCCEEDED(int nameId, int level) {
		return new SM_SYSTEM_MESSAGE(1402148, new Object[] { new DescriptionId(nameId), Integer.valueOf(level) });
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_AUTHORIZE_FAILED(int nameId) {
		return new SM_SYSTEM_MESSAGE(1402149, new Object[] { new DescriptionId(nameId) });
	}

	public static SM_SYSTEM_MESSAGE STR_STR_MSG_ITEM_AUTHORIZE_FAILED_TSHIRT(int nameId) {
		return new SM_SYSTEM_MESSAGE(1402447, new Object[] { new DescriptionId(nameId) });
	}

	/**
	 * Purification System
	 */
	public static SM_SYSTEM_MESSAGE STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT(DescriptionId nameId) {
		return new SM_SYSTEM_MESSAGE(1402397, new Object[] { nameId });
	}

	public static SM_SYSTEM_MESSAGE STR_ITEM_UPGRADE_MSG_UPGRADE_SUCCESS(DescriptionId nameId, DescriptionId nameId2) {
		return new SM_SYSTEM_MESSAGE(1402579, new Object[] { nameId, nameId2 });
	}

	public static final SM_SYSTEM_MESSAGE STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT_NEED_AP = new SM_SYSTEM_MESSAGE(1402571);

	public static final SM_SYSTEM_MESSAGE STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT_NEED_QINA = new SM_SYSTEM_MESSAGE(1402572);

	/**
	 * Atreian Passport
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_NEW_PASSPORT_AVAIBLE = new SM_SYSTEM_MESSAGE(1402601);

	/**
	 * Linkgate Foundry
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_01 = new SM_SYSTEM_MESSAGE(1402453);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_02 = new SM_SYSTEM_MESSAGE(1402454);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_03 = new SM_SYSTEM_MESSAGE(1402455);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_04 = new SM_SYSTEM_MESSAGE(1402456);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_05 = new SM_SYSTEM_MESSAGE(1402457);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_06 = new SM_SYSTEM_MESSAGE(1402458);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_Time_07 = new SM_SYSTEM_MESSAGE(1402461);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4Re_01_DoorOpen_01 = new SM_SYSTEM_MESSAGE(1402440);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4Re_01_DoorOpen_02 = new SM_SYSTEM_MESSAGE(1402441);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4Re_01_DoorOpen_03 = new SM_SYSTEM_MESSAGE(1402442);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF4_Re_01_secret_room_03 = new SM_SYSTEM_MESSAGE(1402603);

	// new 4.8 stigma system start
	/**
	 * You acquired %2 %0 skill (Level %1) through your Linked Stigma combination.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_GET_HIDDEN_SKILL(DescriptionId descriptionId, int skilllvl) {
		return new SM_SYSTEM_MESSAGE(1402891, descriptionId, skilllvl);
	}

	/**
	 * You can no longer use the %2 %0 skill (Level %1) because your Linked Stigma combination has changed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_DELETE_HIDDEN_SKILL(DescriptionId descriptionId, int skilllvl) {
		return new SM_SYSTEM_MESSAGE(1402895, descriptionId, skilllvl);
	}

	/**
	 * You have successfully enchanted %0 and the Stigma's enchantment level has increased by 1 level
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_ENCHANT_SUCCESS(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402930, descriptionId);
	}
	
	/**
	 * The enchantment of %0 was successful and the boost level increased by %1 level(s).
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_MATTER_ENCHANT_SUCCESS(DescriptionId descriptionId, int level) {
		return new SM_SYSTEM_MESSAGE(1404544, descriptionId, level);
	}

	/**
	 * You have failed to enchant %0 and the Stigma has been destroyed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_ENCHANT_FAIL(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402931, descriptionId);
	}

	/**
	 * %0%'s hidden enchantment level has increased by 1 level
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_ENCHANT_HIDDEN_SKILL(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402944, descriptionId);
	}
	// new 4.8 stigma system end

	/**
	 * Agent Fight
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_GodElite = new SM_SYSTEM_MESSAGE(1402543);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_GodElite_time_01 = new SM_SYSTEM_MESSAGE(1402544);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_GodElite_time_02 = new SM_SYSTEM_MESSAGE(1402545);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_GodElite_time_03 = new SM_SYSTEM_MESSAGE(1402546);

	/**
	 * Amplification System
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_SUCCEED(DescriptionId nameId) {
		return new SM_SYSTEM_MESSAGE(1402657, new Object[] { nameId });
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_ENCHANT_CANNOT_01(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402658, descriptionId);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_ENCHANT_ITEM_SUCCEEDED_EXCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402659, descriptionId);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_CAN_EXCEED_ENCHANT_LEVEL(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402660, descriptionId);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_ENCHANT_CANNOT_02(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402661, descriptionId);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_SKILL_ENCHANT(DescriptionId num0, int num1, int num2) {
		return new SM_SYSTEM_MESSAGE(1402662, num0, num2);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EXCEED_SKILL_DELETE(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402663, descriptionId);
	}

	/**
	 * %1 cannot be wrapped with %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_PACK_ITEM_WRONG_TARGET_ITEM_CATEGORY(int parentNameId, int targetNameId) {
		return new SM_SYSTEM_MESSAGE(1402018, new DescriptionId(parentNameId), new DescriptionId(targetNameId));
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_PACK_ITEM_NEED_IDENTIFY = new SM_SYSTEM_MESSAGE(1402030);

	/**
	 * Upgrade Arcade Game
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GACHA_ITEM_CHECK = new SM_SYSTEM_MESSAGE(1402581, "[item:" + 186000389 + "]"); // frenzy_coins

	public static SM_SYSTEM_MESSAGE STR_MSG_GACHA_ITEM_REWARD(int itemId) {
		return new SM_SYSTEM_MESSAGE(1402582, "[item:" + itemId + "]");
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_GACHA_ITEM_REWARD_MULTI(int itemId, int count) {
		return new SM_SYSTEM_MESSAGE(1402583, "[item:" + itemId + "]", count);
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_GACHA_FEVERTIME_START = new SM_SYSTEM_MESSAGE(1402608);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GACHA_TIME_END = new SM_SYSTEM_MESSAGE(14025858);

	public static SM_SYSTEM_MESSAGE STR_MSG_GACHA_FEVER_ITEM_REWARD(int itemId) {
		return new SM_SYSTEM_MESSAGE(1403137, "[item:" + itemId + "]");
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_GACHA_FEVER_ITEM_REWARD_MULTI(int itemId, int count) {
		return new SM_SYSTEM_MESSAGE(1403138, "[item:" + itemId + "]", count);
	}

	/**
	 * [Abyss Logon] 4.9
	 */

	/**
	 * Elyos Governor %0% has graced Atreia.
	 */
	public static SM_SYSTEM_MESSAGE STR_RANK1_LIGHT_LOGON(String name) {
		return new SM_SYSTEM_MESSAGE(1403134, name);
	}

	/**
	 * Asmodian Governor %0% has graced Atreia.
	 */
	public static SM_SYSTEM_MESSAGE STR_RANK1_DARK_LOGON(String name) {
		return new SM_SYSTEM_MESSAGE(1403135, name);
	}

	/**
	 * You can advance to level %0 only after you have completed the Transcendence quest.
	 */
	public static SM_SYSTEM_MESSAGE STR_LEVEL_LIMIT_QUEST_NOT_FINISHED2(int level) {
		return new SM_SYSTEM_MESSAGE(1403187, level);
	}

	/**
	 * Gp Delete Daily
	 */
	/**
	 * You loose %num0 Daily Glory Points.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GLORY_POINT_LOSE_COMMON = new SM_SYSTEM_MESSAGE(1402082);

	public static SM_SYSTEM_MESSAGE STR_MSG_GLORY_POINT_LOSE_PERSONAL(String name, int num0) {
		return new SM_SYSTEM_MESSAGE(1402209, name, num0);
	}

	/**
	 * Illuminary Obelisk
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF5_U3_Hard_Door_Open = new SM_SYSTEM_MESSAGE(1402423);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF5_U3_DEFENCE_01_ATTACKED = new SM_SYSTEM_MESSAGE(1402220);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF5_U3_DEFENCE_02_ATTACKED = new SM_SYSTEM_MESSAGE(1402221);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF5_U3_DEFENCE_03_ATTACKED = new SM_SYSTEM_MESSAGE(1402222);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF5_U3_DEFENCE_04_ATTACKED = new SM_SYSTEM_MESSAGE(1402223);

	/**
	 * Dimensional Vortex 3.5
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DARK_SIDE_INVADE_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1401446);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LIGHT_SIDE_INVADE_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1401447);
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_INVADE_DIRECT_PORTAL_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1401448);
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_INVADE_DIRECT_PORTAL_USE_COUNT_LIMIT = new SM_SYSTEM_MESSAGE(1401449);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER = new SM_SYSTEM_MESSAGE(1401450);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_COMPULSION_TELEPORT = new SM_SYSTEM_MESSAGE(1401451);
	public static final SM_SYSTEM_MESSAGE STR_MSG_COMPULSION_TELEPORT_NOT_FORCE = new SM_SYSTEM_MESSAGE(1401452);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE = new SM_SYSTEM_MESSAGE(1401453);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_OPEN_NOTICE = new SM_SYSTEM_MESSAGE(1401454);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_OUT_NOTICE = new SM_SYSTEM_MESSAGE(1401455);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_OUT_COMPULSION = new SM_SYSTEM_MESSAGE(1401474);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_DEFENSE_FORCE_OUT = new SM_SYSTEM_MESSAGE(1401476);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_90M = new SM_SYSTEM_MESSAGE(1401478);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_60M = new SM_SYSTEM_MESSAGE(1401479);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_30M = new SM_SYSTEM_MESSAGE(1401480);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_15M = new SM_SYSTEM_MESSAGE(1401481);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_10M = new SM_SYSTEM_MESSAGE(1401482);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_5M = new SM_SYSTEM_MESSAGE(1401483);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_3M = new SM_SYSTEM_MESSAGE(1401484);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_2M = new SM_SYSTEM_MESSAGE(1401485);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_1M = new SM_SYSTEM_MESSAGE(1401486);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_30S = new SM_SYSTEM_MESSAGE(1401487);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_15S = new SM_SYSTEM_MESSAGE(1401488);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_10S = new SM_SYSTEM_MESSAGE(1401489);
	public static final SM_SYSTEM_MESSAGE STR_MSG_INVADE_DIRECT_PORTAL_CLOSE_TIMER_5S = new SM_SYSTEM_MESSAGE(1401490);
	public static final SM_SYSTEM_MESSAGE STR_CHAT_INVADEPORTL_KEEPER_SYSTEM_MSG01 = new SM_SYSTEM_MESSAGE(1401493);
	public static final SM_SYSTEM_MESSAGE STR_CHAT_INVADEPORTL_KEEPER_SYSTEM_MSG02 = new SM_SYSTEM_MESSAGE(1401494);
	public static final SM_SYSTEM_MESSAGE STR_CHAT_INVADEPORTL_KEEPER_SYSTEM_MSG03 = new SM_SYSTEM_MESSAGE(1401495);

	/**
	 * You cannot open a private store while hiding
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_DISABLED_IN_HIDDEN_MODE = new SM_SYSTEM_MESSAGE(1401969);

	/**
	 * Your private store closed automatically because you are currently hiding
	 */
	public static final SM_SYSTEM_MESSAGE STR_PERSONAL_SHOP_CLOSED_FOR_HIDDEN_MODE = new SM_SYSTEM_MESSAGE(1401970);

	/**
	 * Pets Buff 4.8
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_BUFF_PET_USE_START_MESSAGE = new SM_SYSTEM_MESSAGE(1402606);
	public static final SM_SYSTEM_MESSAGE STR_MSG_BUFF_PET_USE_STOP_MESSAGE = new SM_SYSTEM_MESSAGE(1402607);
	public static final SM_SYSTEM_MESSAGE STR_MSG_BUFF_PET_USE_STOP_MESSAGE_03 = new SM_SYSTEM_MESSAGE(1402672);

	/**
	 * Beritra Invasion 4.7
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_01 = new SM_SYSTEM_MESSAGE(1402383);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_02 = new SM_SYSTEM_MESSAGE(1402384);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_03 = new SM_SYSTEM_MESSAGE(1402385);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_04 = new SM_SYSTEM_MESSAGE(1402386);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_01 = new SM_SYSTEM_MESSAGE(1402387);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_02 = new SM_SYSTEM_MESSAGE(1402388);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_03 = new SM_SYSTEM_MESSAGE(1402389);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_04 = new SM_SYSTEM_MESSAGE(1402390);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_05 = new SM_SYSTEM_MESSAGE(1402391);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_MESSAGE_DIE_06 = new SM_SYSTEM_MESSAGE(1402392);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_INVADE_VRITRA = new SM_SYSTEM_MESSAGE(1402459);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_INVADE_VRITRA_SPECIAL = new SM_SYSTEM_MESSAGE(1402460);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_ADVANCE_FNAMED_FAIL = new SM_SYSTEM_MESSAGE(1402539);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_ADVANCE_FNAMED_SPAWN = new SM_SYSTEM_MESSAGE(1402540);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_ADVANCE_FNAMED_SPAWN_ITEM = new SM_SYSTEM_MESSAGE(1402541);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_ADVANCE_SUMMON_NAMED_TIME = new SM_SYSTEM_MESSAGE(1402602);
	private final int code;
	private final Object[] params;
	private boolean npcShout = false;
	private int npcObjId = 0;
	private int textColorId = 0x19;

	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_Under_User = new SM_SYSTEM_MESSAGE(1402945);

	public static final SM_SYSTEM_MESSAGE STR_MSG_LIGHT_SIDE_LEGION_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1402877);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DARK_SIDE_LEGION_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1402878);

	/**
	 * Hotspot Location 4.7
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOTSPOT_NOT_ENOUGH_COST = new SM_SYSTEM_MESSAGE(1402443);
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOTSPOT_CANT_MOVE_THIS_STANCE = new SM_SYSTEM_MESSAGE(1402444);
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOTSPOT_CANT_MOVE_NOW = new SM_SYSTEM_MESSAGE(1402445);
	public static final SM_SYSTEM_MESSAGE STR_MSG_HOTSPOT_CANCEL_MOVE = new SM_SYSTEM_MESSAGE(1402446);

	/**
	 * Mentor
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_BE_MENTEE_BY_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1402835);

	/**
	 * Bases
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v01 = new SM_SYSTEM_MESSAGE(1402506);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v02 = new SM_SYSTEM_MESSAGE(1402507);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v03 = new SM_SYSTEM_MESSAGE(1402508);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v04 = new SM_SYSTEM_MESSAGE(1402509);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v05 = new SM_SYSTEM_MESSAGE(1402510);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v06 = new SM_SYSTEM_MESSAGE(1402511);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v07 = new SM_SYSTEM_MESSAGE(1402512);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v08 = new SM_SYSTEM_MESSAGE(1402513);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v09 = new SM_SYSTEM_MESSAGE(1402514);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v10 = new SM_SYSTEM_MESSAGE(1402515);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v11 = new SM_SYSTEM_MESSAGE(1402516);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v12 = new SM_SYSTEM_MESSAGE(1402517);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_chief_v13 = new SM_SYSTEM_MESSAGE(1402518);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v01 = new SM_SYSTEM_MESSAGE(1402519);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v02 = new SM_SYSTEM_MESSAGE(1402520);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v03 = new SM_SYSTEM_MESSAGE(1402521);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v04 = new SM_SYSTEM_MESSAGE(1402522);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v05 = new SM_SYSTEM_MESSAGE(1402523);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v06 = new SM_SYSTEM_MESSAGE(1402524);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v07 = new SM_SYSTEM_MESSAGE(1402525);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v08 = new SM_SYSTEM_MESSAGE(1402526);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v09 = new SM_SYSTEM_MESSAGE(1402527);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v10 = new SM_SYSTEM_MESSAGE(1402528);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v11 = new SM_SYSTEM_MESSAGE(1402529);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v12 = new SM_SYSTEM_MESSAGE(1402530);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF4_Advance_killer_v13 = new SM_SYSTEM_MESSAGE(1402531);

	public static final SM_SYSTEM_MESSAGE STR_MSG_WEAK_RACE_BUFF_LIGHT_GAIN = new SM_SYSTEM_MESSAGE(1402588);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WEAK_RACE_BUFF_DARK_GAIN = new SM_SYSTEM_MESSAGE(1402592);

	/**
	 * Panesterra 4.7
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Gab1_START01 = new SM_SYSTEM_MESSAGE(1402609);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Gab1_START02 = new SM_SYSTEM_MESSAGE(1402610);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Gab1_START03 = new SM_SYSTEM_MESSAGE(1402611);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Gab1_START04 = new SM_SYSTEM_MESSAGE(1402612);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Gab1_START05 = new SM_SYSTEM_MESSAGE(1402613);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User = new SM_SYSTEM_MESSAGE(1402283);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User02 = new SM_SYSTEM_MESSAGE(1402284);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User03 = new SM_SYSTEM_MESSAGE(1402291);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User04 = new SM_SYSTEM_MESSAGE(1402364);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User05 = new SM_SYSTEM_MESSAGE(1402370);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User06 = new SM_SYSTEM_MESSAGE(1402382);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Telepoter_GAb1_User07 = new SM_SYSTEM_MESSAGE(1402420);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End01 = new SM_SYSTEM_MESSAGE(1402620);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End02 = new SM_SYSTEM_MESSAGE(1402621);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End03 = new SM_SYSTEM_MESSAGE(1402622);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End04 = new SM_SYSTEM_MESSAGE(1402623);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End05 = new SM_SYSTEM_MESSAGE(1402624);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End06 = new SM_SYSTEM_MESSAGE(1402982);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End07 = new SM_SYSTEM_MESSAGE(1402983);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End08 = new SM_SYSTEM_MESSAGE(1402984);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End09 = new SM_SYSTEM_MESSAGE(1402985);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End10 = new SM_SYSTEM_MESSAGE(1402986);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LDF5_Gab1_End11 = new SM_SYSTEM_MESSAGE(1403013);
	public static final SM_SYSTEM_MESSAGE STR_Gab1_ARTIFACT_CASTING = new SM_SYSTEM_MESSAGE(1402547);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_INVADE_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1402399);
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_SVS_DIRECT_PORTAL_LEVEL_LIMIT = new SM_SYSTEM_MESSAGE(1402400);
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_SVS_DIRECT_PORTAL_USE_COUNT_LIMIT = new SM_SYSTEM_MESSAGE(1402401);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER = new SM_SYSTEM_MESSAGE(1402402);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_COMPULSION = new SM_SYSTEM_MESSAGE(1402403);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_OPEN_NOTICE = new SM_SYSTEM_MESSAGE(1402404);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_COMPULSION_TELEPORT = new SM_SYSTEM_MESSAGE(1402405);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_90M = new SM_SYSTEM_MESSAGE(1402406);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_60M = new SM_SYSTEM_MESSAGE(1402407);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_30M = new SM_SYSTEM_MESSAGE(1402408);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_15M = new SM_SYSTEM_MESSAGE(1402409);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_10M = new SM_SYSTEM_MESSAGE(1402410);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_5M = new SM_SYSTEM_MESSAGE(1402411);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_3M = new SM_SYSTEM_MESSAGE(1402412);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_2M = new SM_SYSTEM_MESSAGE(1402413);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_1M = new SM_SYSTEM_MESSAGE(1402414);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_30S = new SM_SYSTEM_MESSAGE(1402415);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_15S = new SM_SYSTEM_MESSAGE(1402416);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_10S = new SM_SYSTEM_MESSAGE(1402417);
	public static final SM_SYSTEM_MESSAGE STR_MSG_SVS_DIRECT_PORTAL_CLOSE_TIMER_5S = new SM_SYSTEM_MESSAGE(1402418);

	/**
	 * Transidium Annex
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_TANK_A_ATTACKED = new SM_SYSTEM_MESSAGE(1402258);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_TANK_B_ATTACKED = new SM_SYSTEM_MESSAGE(1402259);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_TANK_C_ATTACKED = new SM_SYSTEM_MESSAGE(1402260);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_TANK_D_ATTACKED = new SM_SYSTEM_MESSAGE(1402261);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_PORTAL_DEST_69_ATTACKED = new SM_SYSTEM_MESSAGE(1402266);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_PORTAL_DEST_70_ATTACKED = new SM_SYSTEM_MESSAGE(1402267);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_PORTAL_DEST_71_ATTACKED = new SM_SYSTEM_MESSAGE(1402268);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_PORTAL_DEST_72_ATTACKED = new SM_SYSTEM_MESSAGE(1402269);
	public static final SM_SYSTEM_MESSAGE STR_CANNOT_USE_DIRECT_PORTAL_NOT_TITLE = new SM_SYSTEM_MESSAGE(1402286);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_01 = new SM_SYSTEM_MESSAGE(1402252);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_02 = new SM_SYSTEM_MESSAGE(1402253);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_03 = new SM_SYSTEM_MESSAGE(1402254);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_04 = new SM_SYSTEM_MESSAGE(1402255);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_05 = new SM_SYSTEM_MESSAGE(1402256);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_06 = new SM_SYSTEM_MESSAGE(1402257);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_07 = new SM_SYSTEM_MESSAGE(1402586);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_08 = new SM_SYSTEM_MESSAGE(1402587);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_11 = new SM_SYSTEM_MESSAGE(1402639);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GAB1_SUB_ALARM_12 = new SM_SYSTEM_MESSAGE(1402640);

	/**
	 * Upper Abyss
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Crotan_Named_Spawn = new SM_SYSTEM_MESSAGE(1403049);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Crotan_Named_Spawn_In = new SM_SYSTEM_MESSAGE(1403142);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Crotan_Named_Spawn_System = new SM_SYSTEM_MESSAGE(1403050);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Crotan_Named_Spawn_Item = new SM_SYSTEM_MESSAGE(1403051);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Spawn_Crotan = new SM_SYSTEM_MESSAGE(1403112);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Despawn_Crotan = new SM_SYSTEM_MESSAGE(1403113);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Die_Crotan = new SM_SYSTEM_MESSAGE(1403114);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Lamiren_Named_Spawn = new SM_SYSTEM_MESSAGE(1403052);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Lamiren_Named_Spawn_In = new SM_SYSTEM_MESSAGE(1403143);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Lamiren_Named_Spawn_System = new SM_SYSTEM_MESSAGE(1403053);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Lamiren_Named_Spawn_Item = new SM_SYSTEM_MESSAGE(1403054);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Spawn_lamiren = new SM_SYSTEM_MESSAGE(1403115);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Despawn_lamiren = new SM_SYSTEM_MESSAGE(1403116);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Die_lamiren = new SM_SYSTEM_MESSAGE(1403117);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Dkisas_Named_Spawn = new SM_SYSTEM_MESSAGE(1403055);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Dkisas_Named_Spawn_In = new SM_SYSTEM_MESSAGE(1403144);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Dkisas_Named_Spawn_System = new SM_SYSTEM_MESSAGE(1403056);
	public static final SM_SYSTEM_MESSAGE STR_MSG_Ab1_Dkisas_Named_Spawn_Item = new SM_SYSTEM_MESSAGE(1403057);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Spawn_dkisas = new SM_SYSTEM_MESSAGE(1403118);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Despawn_dkisas = new SM_SYSTEM_MESSAGE(1403119);
	public static final SM_SYSTEM_MESSAGE STR_Ab1_BossNamed_65_Al_Die_dkisas = new SM_SYSTEM_MESSAGE(1403120);

	/**
	 * Ereshkigal Invasion
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_Ere_MESSAGE_01 = new SM_SYSTEM_MESSAGE(1403069);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_Ere_MESSAGE_02 = new SM_SYSTEM_MESSAGE(1403070);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_Ere_MESSAGE_03 = new SM_SYSTEM_MESSAGE(1403071);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_Ere_MESSAGE_04 = new SM_SYSTEM_MESSAGE(1403072);
	public static final SM_SYSTEM_MESSAGE STR_MSG_WORLDRAID_Ere_MESSAGE_DIE_01 = new SM_SYSTEM_MESSAGE(1403073);

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_POINT_QUEST_GAIN(int value0) {
		return new SM_SYSTEM_MESSAGE(1403172, value0);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_QUEST(String value0) {
		return new SM_SYSTEM_MESSAGE(1403173, value0);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_OCCUPY(DescriptionId value0, DescriptionId value1) {
		return new SM_SYSTEM_MESSAGE(1403174, value0, value1);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_KILL_NPC(DescriptionId value0, DescriptionId value1) {
		return new SM_SYSTEM_MESSAGE(1403175, value0, value1);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_DESTROY(DescriptionId value0, DescriptionId value1) {
		return new SM_SYSTEM_MESSAGE(1403176, value0, value1);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_OCCUPY_BASECAMP(DescriptionId value0, String value1) {
		return new SM_SYSTEM_MESSAGE(1403186, value0, value1);
	}

	/**
	 * Stigma
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_STIGMA_OPEN_NORMAL_SLOT = new SM_SYSTEM_MESSAGE(1402933);
	public static final SM_SYSTEM_MESSAGE STR_MSG_STIGMA_OPEN_ENHANCED1_SLOT = new SM_SYSTEM_MESSAGE(1402934);
	public static final SM_SYSTEM_MESSAGE STR_MSG_STIGMA_OPEN_ENHANCED2_SLOT = new SM_SYSTEM_MESSAGE(1402935);
	public static final SM_SYSTEM_MESSAGE STR_MSG_STIGMA_OPEN_SLOT_BY_QUEST = new SM_SYSTEM_MESSAGE(1402942);

	public static SM_SYSTEM_MESSAGE STR_MSG_STIGMA_CANT_ENCHANT(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402896, descriptionId);
	}

	/**
	 * Remove Soulbind
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_CANNOT = new SM_SYSTEM_MESSAGE(1403324);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_WRONG_COMPOSITION = new SM_SYSTEM_MESSAGE(1403325);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_WRONG_EQUIPED = new SM_SYSTEM_MESSAGE(1403326);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_WRONG_SEAL = new SM_SYSTEM_MESSAGE(1403327);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_WRONG_NOT_SOULBIND = new SM_SYSTEM_MESSAGE(1403328);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_INVALID_STANCE = new SM_SYSTEM_MESSAGE(1403329);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1403330);
	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_NEED_IDENTIFY = new SM_SYSTEM_MESSAGE(1403331);

	public static final SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_SUCCEED(final int nameId) {
		return new SM_SYSTEM_MESSAGE(1403332, new Object[] { new DescriptionId(nameId) });
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_CHARGE_ABSOLUTEEXPPOINT_50(int num0) {
		return new SM_SYSTEM_MESSAGE(1403336, num0);
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ABSOLUTEEXP_BONUS_50_BOOST_ON(int num0) {
		return new SM_SYSTEM_MESSAGE(1403399, num0);
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ABSOLUTEEXP_BONUS_50_BOOST_OFF(int num0) {
		return new SM_SYSTEM_MESSAGE(1403400, num0);
	}

	public static final SM_SYSTEM_MESSAGE STR_MSG_ABSOLUTEEXP_BONUS_50_END = new SM_SYSTEM_MESSAGE(1403401);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_PLUS_ABSOLUTEEXP_BONUS = new SM_SYSTEM_MESSAGE(1402615);

	public static final SM_SYSTEM_MESSAGE STR_GET_EXP_ABSOLUTEEXP_BONUS(DescriptionId value0, long var1, long var2) {
		return new SM_SYSTEM_MESSAGE(1402617, value0, var1, var2);
	}

	public static final SM_SYSTEM_MESSAGE STR_GET_EXP_VITAL_ABSOLUTEEXP_BONUS(DescriptionId value0, long var1, long var2, long var3) {
		return new SM_SYSTEM_MESSAGE(1402617, value0, var1, var2, var3);
	}

	/**
	 * Unbinding Item 5.0
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_REMOVE_SOULBIND_SUCCEED(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1403332, descriptionId);
	}

	/**
	 * You can take part in the battle on the Balaur Marching Route.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDLDF5_Under_02_War = new SM_SYSTEM_MESSAGE(1403403);

	/**
	 * You can take part in the Runatorium Ruins Battle.
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDLDF5_Fortress_War = new SM_SYSTEM_MESSAGE(1403404);

	/**
	 * Reduction Level 5.1
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_WRONG_SEAL = new SM_SYSTEM_MESSAGE(1403593);
	public static final SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_WRONG_PACK = new SM_SYSTEM_MESSAGE(1403594);
	public static final SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_INVALID_STANCE = new SM_SYSTEM_MESSAGE(1403595);
	public static final SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_NO_TARGET_ITEM = new SM_SYSTEM_MESSAGE(1403596);
	public static final SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_NEED_IDENTIFY = new SM_SYSTEM_MESSAGE(1403597);

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_CANNOT(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403591, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_WRONG_MAX(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403592, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_SUCCEED(int nameId, int count) {
		return new SM_SYSTEM_MESSAGE(1403598, new DescriptionId(nameId), count);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_FAIL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403599, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_CANCEL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403600, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_EQUIPLEVEL_ADJ_SUCCEED_MAX(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403603, new DescriptionId(nameId));
	}

	/**
	 * %0 has been sealed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_DONE(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400797, new DescriptionId(nameId));
	}

	/**
	 * Canceled sealing %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_START_CANCEL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400798, new DescriptionId(nameId));
	}

	/**
	 * %0 is unsealed.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_STATUS_UNSEALDONE(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400808, new DescriptionId(nameId));
	}

	/**
	 * Canceled unsealing %0.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UNSEALCANCEL(int nameId) {
		return new SM_SYSTEM_MESSAGE(1400829, new DescriptionId(nameId));
	}

	/**
	 * You cannot trade, enhance, destroy, sell, extract, or soulbind sealed items, and it takes %0 days to unseal them completely.
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_START(String value0) {
		return new SM_SYSTEM_MESSAGE(1400809, value0);
	}

	/**
	 * Seal/Unseal
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_POLISH = new SM_SYSTEM_MESSAGE(1401659);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_IDENTIFY = new SM_SYSTEM_MESSAGE(1401660);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_AP_EXTRACTION = new SM_SYSTEM_MESSAGE(1401662);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_AUTHORIZE = new SM_SYSTEM_MESSAGE(1402164);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_AUTHORIZE_RESET = new SM_SYSTEM_MESSAGE(1402167);
	public static final SM_SYSTEM_MESSAGE STR_MSG_ITEM_SEAL_WARNING_UP_TYPE_OPTION = new SM_SYSTEM_MESSAGE(1402246);

	/**
	 * Golden Arena/Crusible
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_INSTANCE_OPEN_IDTM_Lobby01 = new SM_SYSTEM_MESSAGE(1403979);

	/**
	 * F2P
	 */
	public static SM_SYSTEM_MESSAGE STR_F2P_MSG_NOTICE_PACK_EXPIRE_TIME(String durationtime0, String value1) {
		return new SM_SYSTEM_MESSAGE(1700040, durationtime0, value1);
	}

	public static SM_SYSTEM_MESSAGE STR_F2P_MSG_WARNING_PACK_EXPIRE_TIME(String durationtime0, String value1) {
		return new SM_SYSTEM_MESSAGE(1700041, durationtime0, value1);
	}

	public static final SM_SYSTEM_MESSAGE STR_F2P_MSG_PACK_EXPIRED = new SM_SYSTEM_MESSAGE(1700042);

	/**
	 * Warship Invasion
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_B_G2_Spawn = new SM_SYSTEM_MESSAGE(1403150);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_B2_G2_Spawn = new SM_SYSTEM_MESSAGE(1403151);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_C_G2_Spawn = new SM_SYSTEM_MESSAGE(1403152);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_D_G2_Spawn = new SM_SYSTEM_MESSAGE(1403153);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_E_G2_Spawn = new SM_SYSTEM_MESSAGE(1403154);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_F_G2_Spawn = new SM_SYSTEM_MESSAGE(1403155);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_F2_G2_Spawn = new SM_SYSTEM_MESSAGE(1403156);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G_G2_Spawn = new SM_SYSTEM_MESSAGE(1403157);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_H_G2_Spawn = new SM_SYSTEM_MESSAGE(1403158);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_I_G2_Spawn = new SM_SYSTEM_MESSAGE(1403159);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_B_G2_Spawn = new SM_SYSTEM_MESSAGE(1403160);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_B2_G2_Spawn = new SM_SYSTEM_MESSAGE(1403161);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_C_G2_Spawn = new SM_SYSTEM_MESSAGE(1403162);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_D_G2_Spawn = new SM_SYSTEM_MESSAGE(1403163);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_E_G2_Spawn = new SM_SYSTEM_MESSAGE(1403164);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_F_G2_Spawn = new SM_SYSTEM_MESSAGE(1403165);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_F2_G2_Spawn = new SM_SYSTEM_MESSAGE(1403166);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G_G2_Spawn = new SM_SYSTEM_MESSAGE(1403167);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_H_G2_Spawn = new SM_SYSTEM_MESSAGE(1403168);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_I_G2_Spawn = new SM_SYSTEM_MESSAGE(1403169);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Spawn_01 = new SM_SYSTEM_MESSAGE(1403178);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Spawn_02 = new SM_SYSTEM_MESSAGE(1403179);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Spawn_03 = new SM_SYSTEM_MESSAGE(1403180);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Spawn_04 = new SM_SYSTEM_MESSAGE(1403181);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Spawn_05 = new SM_SYSTEM_MESSAGE(1403170);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Spawn_01 = new SM_SYSTEM_MESSAGE(1403182);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Spawn_02 = new SM_SYSTEM_MESSAGE(1403183);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Spawn_03 = new SM_SYSTEM_MESSAGE(1403184);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Spawn_04 = new SM_SYSTEM_MESSAGE(1403185);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Spawn_05 = new SM_SYSTEM_MESSAGE(1403171);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G2_Event_Start = new SM_SYSTEM_MESSAGE(1403190);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G2_Event_Start = new SM_SYSTEM_MESSAGE(1403191);
	public static final SM_SYSTEM_MESSAGE STR_DF6_Event_G1_Defence_Failed = new SM_SYSTEM_MESSAGE(1403204);
	public static final SM_SYSTEM_MESSAGE STR_LF6_Event_G1_Defence_Failed = new SM_SYSTEM_MESSAGE(1403205);
	public static final SM_SYSTEM_MESSAGE STR_DF6_Event_G2_Start_01 = new SM_SYSTEM_MESSAGE(1403216);
	public static final SM_SYSTEM_MESSAGE STR_DF6_Event_G2_Start_02 = new SM_SYSTEM_MESSAGE(1403217);
	public static final SM_SYSTEM_MESSAGE STR_DF6_Event_G2_Start_03 = new SM_SYSTEM_MESSAGE(1403218);
	public static final SM_SYSTEM_MESSAGE STR_LF6_Event_G2_Start_01 = new SM_SYSTEM_MESSAGE(1403219);
	public static final SM_SYSTEM_MESSAGE STR_LF6_Event_G2_Start_02 = new SM_SYSTEM_MESSAGE(1403220);
	public static final SM_SYSTEM_MESSAGE STR_LF6_Event_G2_Start_03 = new SM_SYSTEM_MESSAGE(1403221);
	public static final SM_SYSTEM_MESSAGE STR_DF6_Event_G1_Wave_Start_01 = new SM_SYSTEM_MESSAGE(1403226);
	public static final SM_SYSTEM_MESSAGE STR_LF6_Event_G1_Wave_Start_01 = new SM_SYSTEM_MESSAGE(1403227);
	public static final SM_SYSTEM_MESSAGE STR_MSG_F6_Invasion_3rd_Bonus_01 = new SM_SYSTEM_MESSAGE(1403271);
	public static final SM_SYSTEM_MESSAGE STR_MSG_F6_Invasion_3rd_Bonus_02 = new SM_SYSTEM_MESSAGE(1403272);
	public static final SM_SYSTEM_MESSAGE STR_MSG_F6_Invasion_3rd_Bonus_03 = new SM_SYSTEM_MESSAGE(1403273);
	public static final SM_SYSTEM_MESSAGE STR_MSG_F6_Invasion_3rd_Bonus_04 = new SM_SYSTEM_MESSAGE(1403274);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Boss_Spawn_01 = new SM_SYSTEM_MESSAGE(1403290);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Boss_Spawn_01 = new SM_SYSTEM_MESSAGE(1403292);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_Evett_G1_Time_End_01 = new SM_SYSTEM_MESSAGE(1403367);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_Evett_G1_Time_End_02 = new SM_SYSTEM_MESSAGE(1403368);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_Evett_G1_Time_End_03 = new SM_SYSTEM_MESSAGE(1403369);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_Evett_G1_Time_End_01 = new SM_SYSTEM_MESSAGE(1403370);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_Evett_G1_Time_End_02 = new SM_SYSTEM_MESSAGE(1403371);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_Evett_G1_Time_End_03 = new SM_SYSTEM_MESSAGE(1403372);
	public static final SM_SYSTEM_MESSAGE STR_MSG_F6_Event_G1_Po_Time_Start_01 = new SM_SYSTEM_MESSAGE(1403373);
	public static final SM_SYSTEM_MESSAGE STR_MSG_DF6_G1_Windpath_Off_01 = new SM_SYSTEM_MESSAGE(1403293);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LF6_G1_Windpath_Off_01 = new SM_SYSTEM_MESSAGE(1403291);

	/**
	 * R.v.R
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DIRECT_PORTAL_OPEN = new SM_SYSTEM_MESSAGE(1403215);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DIRECT_PORTAL_OPEN_NOTICE = new SM_SYSTEM_MESSAGE(1403222);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DIRECT_PORTAL = new SM_SYSTEM_MESSAGE(1403223);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DIRECT_PORTAL_OUT_NOTICE = new SM_SYSTEM_MESSAGE(1403228);
	public static final SM_SYSTEM_MESSAGE STR_CONFIRM_RVR_DIRECT_PORTAL_OUT = new SM_SYSTEM_MESSAGE(1403229);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403248);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403249);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_HEAL_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403250);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403251);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403252);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_LF3_BOSS_KILL_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403253);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403254);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403255);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_HEAL_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403256);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403257);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403258);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DF3_BOSS_KILL_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403259);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403275);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403276);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403277);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_04 = new SM_SYSTEM_MESSAGE(1403278);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_05 = new SM_SYSTEM_MESSAGE(1403279);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_TIMER_NOTICE_06 = new SM_SYSTEM_MESSAGE(1403280);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_CLOSING_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403281);
	public static final SM_SYSTEM_MESSAGE STR_MSG_RVR_DIRECT_PORTAL_CLOSE_COMPULSION_TELEPORT = new SM_SYSTEM_MESSAGE(1403282);

	public static final SM_SYSTEM_MESSAGE STR_MSG_LEGION_APPLICATION_DENIED = new SM_SYSTEM_MESSAGE(1403109);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEGION_APPLICATION_ACCEPTED = new SM_SYSTEM_MESSAGE(1403110);
	public static final SM_SYSTEM_MESSAGE STR_MSG_LEGION_JOIN_SERVER_CHANGE = new SM_SYSTEM_MESSAGE(1403111);

	/**
	 * Drakenseer's Lair
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_Dragon_Attack_Tower = new SM_SYSTEM_MESSAGE(1403378);
	public static final SM_SYSTEM_MESSAGE STR_MSG_NoticeFlyingArea = new SM_SYSTEM_MESSAGE(1401526);

	/**
	 * Ophidan Bridge
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Wi_Start = new SM_SYSTEM_MESSAGE(1402848);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Wi_Point_01 = new SM_SYSTEM_MESSAGE(1402849);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Wi_Point_02 = new SM_SYSTEM_MESSAGE(1402850);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Wi_Point_03 = new SM_SYSTEM_MESSAGE(1402851);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Pr_Start = new SM_SYSTEM_MESSAGE(1402852);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Pr_Point_01 = new SM_SYSTEM_MESSAGE(1402853);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Pr_Point_02 = new SM_SYSTEM_MESSAGE(1402854);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_Pr_Point_03 = new SM_SYSTEM_MESSAGE(1402855);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_As_Start = new SM_SYSTEM_MESSAGE(1402856);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_As_Point_01 = new SM_SYSTEM_MESSAGE(1402857);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_As_Point_02 = new SM_SYSTEM_MESSAGE(1402858);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDLDF5_U_01_RA_As_Point_03 = new SM_SYSTEM_MESSAGE(1402859);

	/**
	 * Archives Of Eternity
	 */
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Shield = new SM_SYSTEM_MESSAGE(1403210);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Shield_Remove = new SM_SYSTEM_MESSAGE(1403211);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Artifact_Turn = new SM_SYSTEM_MESSAGE(1403212);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Artifact_Turn_On = new SM_SYSTEM_MESSAGE(1403213);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Artifact_Turn_Off = new SM_SYSTEM_MESSAGE(1403214);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Road_Set = new SM_SYSTEM_MESSAGE(1403235);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Artifact_Countdown = new SM_SYSTEM_MESSAGE(1403236);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Cannon = new SM_SYSTEM_MESSAGE(1403237);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Magic_Die = new SM_SYSTEM_MESSAGE(1403269);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Phy_Die = new SM_SYSTEM_MESSAGE(1403270);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_01_Cannon_Die = new SM_SYSTEM_MESSAGE(1403285);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_Warning = new SM_SYSTEM_MESSAGE(1403314);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Cannon_Warning = new SM_SYSTEM_MESSAGE(1403315);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_01_Turn_On = new SM_SYSTEM_MESSAGE(1403316);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_01_Turn_Off = new SM_SYSTEM_MESSAGE(1403317);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_02_Turn_On = new SM_SYSTEM_MESSAGE(1403318);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_02_Turn_Off = new SM_SYSTEM_MESSAGE(1403319);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_03_Turn_On = new SM_SYSTEM_MESSAGE(1403320);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_03_Turn_Off = new SM_SYSTEM_MESSAGE(1403321);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_04_Turn_On = new SM_SYSTEM_MESSAGE(1403322);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Artifact_04_Turn_Off = new SM_SYSTEM_MESSAGE(1403323);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Start = new SM_SYSTEM_MESSAGE(1403340);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_01_On = new SM_SYSTEM_MESSAGE(1403341);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_01_Off = new SM_SYSTEM_MESSAGE(1403342);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_02_On = new SM_SYSTEM_MESSAGE(1403343);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_02_Off = new SM_SYSTEM_MESSAGE(1403344);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_03_On = new SM_SYSTEM_MESSAGE(1403345);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_03_Off = new SM_SYSTEM_MESSAGE(1403346);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_04_On = new SM_SYSTEM_MESSAGE(1403347);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_01_Summon_Ctrl_04_Off = new SM_SYSTEM_MESSAGE(1403348);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_11 = new SM_SYSTEM_MESSAGE(1403511);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_12 = new SM_SYSTEM_MESSAGE(1403512);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_13 = new SM_SYSTEM_MESSAGE(1403513);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_14 = new SM_SYSTEM_MESSAGE(1403514);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_37 = new SM_SYSTEM_MESSAGE(1403589);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_SYSTEM_MSG_41 = new SM_SYSTEM_MESSAGE(1403606);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_Nepilim_Summon_MSG_01 = new SM_SYSTEM_MESSAGE(1403582);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_Nepilim_Summon_MSG_02 = new SM_SYSTEM_MESSAGE(1403583);
	public static final SM_SYSTEM_MESSAGE STR_IDEternity_02_Nepilim_Summon_MSG_03 = new SM_SYSTEM_MESSAGE(1403584);

	/**
	 * HighDeva 5.0
	 */
	public static SM_SYSTEM_MESSAGE STR_CANNOT_USE_ITEM_INVALID_HIGHDEVA(String value0) {
		return new SM_SYSTEM_MESSAGE(1300896, value0);
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_ENCHANT_TYPE1_ENCHANT_FAIL(DescriptionId descriptionId) {
		return new SM_SYSTEM_MESSAGE(1402674, descriptionId);
	}

	/**
	 * Estima 5.1
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_WARNING_EQUIP_ITEM_ENCHANT_CPSTONE = new SM_SYSTEM_MESSAGE(1403639);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CPSTONE_OPEN_SLOT = new SM_SYSTEM_MESSAGE(1403684);

	/**
	 * Creativity Panel 5.1
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_GIVE_CP_ENCHANT = new SM_SYSTEM_MESSAGE(1403230);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GIVE_CP_ENCHANT_CANNOT = new SM_SYSTEM_MESSAGE(1403231);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GIVE_CP_ENCHANT_NOT_ENOUGH = new SM_SYSTEM_MESSAGE(1403232);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GET_CP_LINK = new SM_SYSTEM_MESSAGE(1403233);
	public static final SM_SYSTEM_MESSAGE STR_MSG_CANNOT_USE_CP_UI_START_MENU = new SM_SYSTEM_MESSAGE(1403234);
	public static final SM_SYSTEM_MESSAGE STR_MSG_GET_CP = new SM_SYSTEM_MESSAGE(1403350);

	/**
	 * Aura Of Growth
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_CHARGE_EXP_POINT = new SM_SYSTEM_MESSAGE(1403189);

	/**
	 * Luna Shop
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_LUNA_REWARD_RESTRICTION_RIDE = new SM_SYSTEM_MESSAGE(1403487);
	public static final SM_SYSTEM_MESSAGE STR_LUNA_CRAFT_MATERIAL_INVENTORY_IS_FULL = new SM_SYSTEM_MESSAGE(1403585);

	public static SM_SYSTEM_MESSAGE STR_MSG_LUNA_REWARD_GOTCHA_ITEM_MULTI(long value1, int nameId) {
		return new SM_SYSTEM_MESSAGE(1403486, value1, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_LUNA_REWARD_GOTCHA_ITEM(int nameId) {
		return new SM_SYSTEM_MESSAGE(1403485, new DescriptionId(nameId));
	}

	public static SM_SYSTEM_MESSAGE STR_MSG_GETLUNA(String playerName, int count) {
		return new SM_SYSTEM_MESSAGE(1403973, playerName, count);
	}

	/**
	 * Kroban Base 5.1
	 */
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Start_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403414);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Start_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403415);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Start_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403416);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Start_NOTICE_04 = new SM_SYSTEM_MESSAGE(1403417);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Play_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403418);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_End_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403419);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_End_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403420);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_End_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403421);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Tension_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403422);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Tension_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403423);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Tension_NOTICE_03 = new SM_SYSTEM_MESSAGE(1403424);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Retry_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403442);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Retry_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403443);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Success_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403444);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_Success_NOTICE_02 = new SM_SYSTEM_MESSAGE(1403445);
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDF6_LF1_AnuHelp_NOTICE_01 = new SM_SYSTEM_MESSAGE(1403456);

	/**
	 * Tower of Challenge
	 */	
	public static final SM_SYSTEM_MESSAGE STR_MSG_IDInfinity_02 = new SM_SYSTEM_MESSAGE(1404398);
	
	/**
	 * MonsterBook
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_MONSTER_ACHIEVEMENT_GET(int nameId) {
		return new SM_SYSTEM_MESSAGE(1404072, new Object[] { new DescriptionId(nameId)});
	}
	
	public static SM_SYSTEM_MESSAGE STR_MSG_MONSTER_ACHIEVEMENT_COMPLETION(int level, int nameId) {
		return new SM_SYSTEM_MESSAGE(1404073, level, new Object[] { new DescriptionId(nameId)});
	}
	
	/**
	 * Through Aion's power you have received the following item: %0
	 */
	public static SM_SYSTEM_MESSAGE STR_MSG_GET_HCOIN_07(int nameId) {
		return new SM_SYSTEM_MESSAGE(1401496, new DescriptionId(nameId));
	}

	/**
	 * Museum of Knowledge
	 */
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone1_book_01 = new SM_SYSTEM_MESSAGE(1404076);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone2_book_01 = new SM_SYSTEM_MESSAGE(1404077);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone3_book_01 = new SM_SYSTEM_MESSAGE(1404078);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone4_book_01 = new SM_SYSTEM_MESSAGE(1404079);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone1_book_02 = new SM_SYSTEM_MESSAGE(1404100);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone2_book_02 = new SM_SYSTEM_MESSAGE(1404101);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone3_book_02 = new SM_SYSTEM_MESSAGE(1404102);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone4_book_02 = new SM_SYSTEM_MESSAGE(1404103);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone1_book_03 = new SM_SYSTEM_MESSAGE(1404104);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone2_book_03 = new SM_SYSTEM_MESSAGE(1404105);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone3_book_03 = new SM_SYSTEM_MESSAGE(1404106);
    public static final SM_SYSTEM_MESSAGE STR_MSG_junk_ideternity03_zone4_book_03 = new SM_SYSTEM_MESSAGE(1404107);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_teleporter_1 = new SM_SYSTEM_MESSAGE(1404156);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_teleporter_2 = new SM_SYSTEM_MESSAGE(1404157);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_teleporter_3 = new SM_SYSTEM_MESSAGE(1404158);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_teleporter_4 = new SM_SYSTEM_MESSAGE(1404159);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_teleporter_5 = new SM_SYSTEM_MESSAGE(1404160);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_Dimension_01 = new SM_SYSTEM_MESSAGE(1404259);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_Dimension_02 = new SM_SYSTEM_MESSAGE(1404260);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_Dimension_03 = new SM_SYSTEM_MESSAGE(1404300);
    public static final SM_SYSTEM_MESSAGE STR_MSG_IDEternity_03_Dimension_04 = new SM_SYSTEM_MESSAGE(1404379);

	/**
	 * Minions
	 */
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_GET_FAMILIAR = new SM_SYSTEM_MESSAGE(1404316);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_COMBAT_MODE = new SM_SYSTEM_MESSAGE(1404317);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_RIDE_MODE = new SM_SYSTEM_MESSAGE(1404318);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_CURRENT_STATE = new SM_SYSTEM_MESSAGE(1404319);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_CURRENT_POSITION = new SM_SYSTEM_MESSAGE(1404320);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_CURRENT_MOTION = new SM_SYSTEM_MESSAGE(1404321);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_CONTRACT_BY_MAXUNIT = new SM_SYSTEM_MESSAGE(1404322);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANCEL_CONTRACT = new SM_SYSTEM_MESSAGE(1404323);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CALL_FAMILIAR = new SM_SYSTEM_MESSAGE(1404324);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_DISMISS_FAMILIAR = new SM_SYSTEM_MESSAGE(1404325);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_CANNOT_USE_FSKILL_BY_LACK_FENERGY = new SM_SYSTEM_MESSAGE(1404326);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FENERGY_CHARGE = new SM_SYSTEM_MESSAGE(1404327);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FENERGY_AUTOCHARGING = new SM_SYSTEM_MESSAGE(1404328);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FENERGY_AUTOCHARGING_NOTICE = new SM_SYSTEM_MESSAGE(1404329);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FENERGY_AUTOCHARGING_FAIL_BY_GOLD = new SM_SYSTEM_MESSAGE(1404330);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FENERGY_CHARGE_FAIL_BY_AUTOCHARGE = new SM_SYSTEM_MESSAGE(1404331);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FFUNCTION_END = new SM_SYSTEM_MESSAGE(1404332);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FFUNCTION_AUTOCONTINUE_NOTICE = new SM_SYSTEM_MESSAGE(1404333);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FFUNCTION_AUTOCONTINUE_FAIL_BY_GOLD = new SM_SYSTEM_MESSAGE(1404334);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_OVERLAB = new SM_SYSTEM_MESSAGE(1404335);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_OVERLENGTH = new SM_SYSTEM_MESSAGE(1404336);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_CALLING = new SM_SYSTEM_MESSAGE(1404337);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FAIL_CHANGE_NAME_CONFIRM = new SM_SYSTEM_MESSAGE(1404338);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_UNLOCK_CONFIRM = new SM_SYSTEM_MESSAGE(1404339);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FAIL_DELETE_CALLING = new SM_SYSTEM_MESSAGE(1404340);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_DELETE_CONFIRM = new SM_SYSTEM_MESSAGE(1404341);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_MSG_FFUNCTION_USE_FAIL_BY_GOLD = new SM_SYSTEM_MESSAGE(1404342);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_GROWTH_MSG_OVERSELECT = new SM_SYSTEM_MESSAGE(1404343);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_GROWTH_MSG_OVERGROWTH = new SM_SYSTEM_MESSAGE(1404344);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_GROWTH_MSG_NOTSELECT = new SM_SYSTEM_MESSAGE(1404345);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_GROWTH_MSG_NOGOLD = new SM_SYSTEM_MESSAGE(1404346);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_EVOLVE_MSG_LACK_ITEM = new SM_SYSTEM_MESSAGE(1404347);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_EVOLVE_MSG_NOGOLD = new SM_SYSTEM_MESSAGE(1404348);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_EVOLVE_MSG_NOEVOLVE = new SM_SYSTEM_MESSAGE(1404349);
    public static final SM_SYSTEM_MESSAGE STR_FAMILIAR_EVOLVE_MSG_EVOLVE_RESULT = new SM_SYSTEM_MESSAGE(1404350);
    public static final SM_SYSTEM_MESSAGE STR_MSG_CANT_ENCHANT_EQUIPED = new SM_SYSTEM_MESSAGE(1403358);

	/**
	 * Constructs new <tt>SM_SYSTEM_MESSAGE </tt> packet
	 *
	 * @param code
	 *            operation code, take it from SM_SYSTEM_MESSAGE public static values
	 * @param params
	 */
	public SM_SYSTEM_MESSAGE(int code, Object... params) {
		this.code = code;
		this.params = params;
	}

	public SM_SYSTEM_MESSAGE(boolean npcShout, int code, int npcObjId, int textColorId, Object... params) {
		this.npcShout = npcShout;
		this.code = code;
		this.npcObjId = npcObjId;
		this.textColorId = textColorId;
		this.params = params;
	}

	public SM_SYSTEM_MESSAGE(int code, Creature creature, Object... params) {
		this.code = code;
		this.npcObjId = creature.getObjectId();
		this.params = params;
	}

	public SM_SYSTEM_MESSAGE(SystemMessageId sm, Object... params) {
		this.code = sm.getId();
		this.params = params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeC(textColorId);
		writeC(0x00); // to do for shoots text encoding (unk dialect)
		writeD(npcObjId);

		writeD(code); // msg id
		writeC(params.length); // count

		for (Object param : params) {
			if (param instanceof DescriptionId) {
				writeH(0x24);
				writeD(((DescriptionId) param).getValue());
				writeH(0x00); // unk
			}
			else {
				writeS(String.valueOf(param));
			}
		}

		if (npcShout) {
			writeC(0x01);
		}
		else {
			writeC(0x00);
		}
	}
}
