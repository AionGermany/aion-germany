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
package com.aionemu.gameserver.network.aion.gmhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CmdAttrBonus extends AbstractGMHandler implements StatOwner {

	public CmdAttrBonus(Player admin, String params) {
		super(admin, params);
		run();
	}

	private void run() {
		Player tgtPlayer = target != null ? target : admin;

		// get values from params with regex
		Pattern pattern = Pattern.compile("((.*)\\s(\\d+))");
		Matcher matcher = pattern.matcher(params);
		if (!matcher.find()) {
			System.out.println("no matches found");
			return;
		}

		String func = matcher.group(2).toUpperCase();
		Integer value = Integer.parseInt(matcher.group(3));

		System.out.println("func: " + func);
		System.out.println("value: " + value);

		AttrBonusAction a = null;
		try {
			a = AttrBonusAction.valueOf(func);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// special value handling
		if (a.equals(AttrBonusAction.SPEED) && value >= 1000) {
			return;
		}
		else if (a.equals(AttrBonusAction.FLYSPEED) && value >= 1000) {
			return;
		}

		// standard value handling
		if (value >= 99999999) {
			return;
		}

		List<IStatFunction> functions = new ArrayList<IStatFunction>();
		functions.add(new StatChangeFunction(StatEnum.SPEED, value));
		tgtPlayer.getGameStats().addEffect(this, functions);
		PacketSendUtility.broadcastPacket(tgtPlayer, new SM_EMOTION(tgtPlayer, EmotionType.START_EMOTE2, 0, 0), true);
	}

	enum AttrBonusAction {
		AGI("AGI", StatEnum.ACCURACY),
		AGIDEX("AGIDEX", StatEnum.AGIDEX),
		ALLPARA("ALLPARA", StatEnum.ALLRESIST),
		ARALL("ARALL", StatEnum.ABNORMAL_RESISTANCE_ALL),
		ARBLEED("ARBLEED", StatEnum.BLEED_RESISTANCE),
		ARBLIND("ARBLIND", StatEnum.BLIND_RESISTANCE),
		ARCHARM("ARCHARM", StatEnum.CHARM_RESISTANCE),
		ARCONFUSE("ARCONFUSE", StatEnum.CONFUSE_RESISTANCE),
		ARDISEASE("ARDISEASE", StatEnum.DISEASE_RESISTANCE),
		ARFEAR("ARFEAR", StatEnum.FEAR_RESISTANCE),
		AROPENAREIAL("AROPENAREIAL", StatEnum.OPENAREIAL_RESISTANCE),
		ARPARALYZE("ARPARALYZE", StatEnum.PARALYZE_RESISTANCE),
		ARPERIFICATION("ARPERIFICATION", StatEnum.PERIFICATION_RESISTANCE),
		ARPOISON("ARPOISON", StatEnum.POISON_RESISTANCE),
		ARROOT("ARROOT", StatEnum.ROOT_RESISTANCE),
		ARSILENCE("ARSILENCE", StatEnum.SILENCE_RESISTANCE),
		ARSLEEP("ARSLEEP", StatEnum.SLEEP_RESISTANCE),
		ARSLOW("ARSLOW", StatEnum.SLOW_RESISTANCE),
		ARSNARE("ARSNARE", StatEnum.SNARE_RESISTANCE),
		ARSPIN("ARSPIN", StatEnum.SPIN_RESISTANCE),
		ARSTAGGER("ARSTAGGER", StatEnum.STAGGER_RESISTANCE),
		ARSTUMBLE("ARSTUMBLE", StatEnum.STUMBLE_RESISTANCE),
		ARSTUN("ARSTUN", StatEnum.STUN_RESISTANCE),
		ATTACKDELAY("ATTACKDELAY", StatEnum.ATTACK_SPEED),
		ATTACKRANGE("ATTACKRANGE", StatEnum.ATTACK_RANGE),
		BLEED_ARP("BLEED ARP", StatEnum.BLEED_RESISTANCE_PENETRATION),
		BLIND_ARP("BLIND ARP", StatEnum.BLIND_RESISTANCE_PENETRATION),
		BLOCK("BLOCK", StatEnum.BLOCK),
		BOOSTCASTINGTIME("BOOSTCASTINGTIME", StatEnum.BOOST_CASTING_TIME),
		CHARM_ARP("CHARM ARP", StatEnum.CHARM_RESISTANCE_PENETRATION),
		CONCENTRATION("CONCENTRATION", StatEnum.CONCENTRATION),
		CONFUSE_ARP("CONFUSE ARP", StatEnum.CONFUSE_RESISTANCE_PENETRATION),
		CRITICAL("CRITICAL", StatEnum.PHYSICAL_CRITICAL),
		CURSE_ARP("CURSE ARP", StatEnum.CURSE_RESISTANCE_PENETRATION),
		DEX("DEX", StatEnum.AGILITY),
		DISEASE_ARP("DISEASE ARP", StatEnum.DISEASE_RESISTANCE_PENETRATION),
		DODGE("DODGE", StatEnum.EVASION),
		ELEMENTALDEFENDAIR("ELEMENTALDEFENDAIR", StatEnum.WIND_RESISTANCE),
		ELEMENTALDEFENDEARTH("ELEMENTALDEFENDEARTH", StatEnum.EARTH_RESISTANCE),
		ELEMENTALDEFENDFIRE("ELEMENTALDEFENDFIRE", StatEnum.FIRE_RESISTANCE),
		ELEMENTALDEFENDWATER("ELEMENTALDEFENDWATER", StatEnum.WATER_RESISTANCE),
		FEAR_ARP("FEAR ARP", StatEnum.FEAR_RESISTANCE_PENETRATION),
		FLYSPEED("FLYSPEED", StatEnum.FLY_SPEED),
		FPREGEN("FPREGEN", StatEnum.REGEN_FP),
		HEALSKILLBOOST("HEALSKILLBOOST", StatEnum.HEAL_BOOST),
		HITCOUNT("HITCOUNT", StatEnum.HIT_COUNT),
		HP("HP", StatEnum.MAXHP),
		HPREGEN("HPREGEN", StatEnum.REGEN_HP),
		KNO("KNO", StatEnum.KNOWLEDGE),
		KNOWIL("KNOWIL", StatEnum.KNOWIL),
		MAGICALATTACK("MAGICALATTACK", StatEnum.MAGICAL_ATTACK),
		MAGICALCRITICALDAMAGEREDUCE("MAGICALCRITICALDAMAGEREDUCE", StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE),
		MAGICALCRITICAL("MAGICALCRITICAL", StatEnum.MAGICAL_CRITICAL),
		MAGICALCRITICALREDUCERATE("MAGICALCRITICALREDUCERATE", StatEnum.MAGICAL_CRITICAL_RESIST),
		MAGICALDEFEND("MAGICALDEFEND", StatEnum.MAGICAL_DEFEND),
		MAGICALHITACCURACY("MAGICALHITACCURACY", StatEnum.MAGICAL_ACCURACY),
		MAGICALRESIST("MAGICALRESIST", StatEnum.MAGICAL_RESIST),
		MAGICALSKILLBOOST("MAGICALSKILLBOOST", StatEnum.BOOST_MAGICAL_SKILL),
		MAGICALSKILLBOOSTRESIST("MAGICALSKILLBOOSTRESIST", StatEnum.MAGIC_SKILL_BOOST_RESIST),
		MAXDP("MAXDP", StatEnum.MAXDP),
		MAXHAP("MAXHP", StatEnum.MAXHP),
		MAXHP("MAXMP", StatEnum.MAXMP),
		MP("MP", StatEnum.MAXMP),
		MPREGEN("MPREGEN", StatEnum.REGEN_MP),
		OPENAREIAL_ARP("OPENAREIAL ARP", StatEnum.OPENAREIAL_RESISTANCE_PENETRATION),
		PARALYZE_ARP("PARALYZE ARP", StatEnum.PARALYZE_RESISTANCE_PENETRATION),
		PARRY("PARRY", StatEnum.PARRY),
		PERIFICATION_ARP("PERIFICATION ARP", StatEnum.PERIFICATION_RESISTANCE_PENETRATION),
		PHYSICALCRITICALDAMAGEREDUCE("PHYSICALCRITICALDAMAGEREDUCE", StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE),
		PHYSICALCRITICALREDUCERATE("PHYSICALCRITICALREDUCERATE", StatEnum.PHYSICAL_CRITICAL_RESIST),
		PHYSICALDEFEND("PHYSICALDEFEND", StatEnum.PHYSICAL_DEFENSE),
		POISON_ARP("POISON ARP", StatEnum.POISON_RESISTANCE_PENETRATION),
		ROOT_ARP("ROOT ARP", StatEnum.ROOT_RESISTANCE_PENETRATION),
		SILENCE_ARP("SILENCE ARP", StatEnum.SILENCE_RESISTANCE_PENETRATION),
		SLEEP_ARP("SLEEP ARP", StatEnum.SLEEP_RESISTANCE_PENETRATION),
		SLOW_ARP("SLOW ARP", StatEnum.SLOW_RESISTANCE_PENETRATION),
		SNARE_ARP("SNARE ARP", StatEnum.SNARE_RESISTANCE_PENETRATION),
		SPEED("SPEED", StatEnum.SPEED),
		SPIN_ARP("SPIN ARP", StatEnum.SPIN_RESISTANCE_PENETRATION),
		STAGGER_ARP("STAGGER ARP", StatEnum.STAGGER_RESISTANCE_PENETRATION),
		STR("STR", StatEnum.POWER),
		STRVIT("STRVIT", StatEnum.STRVIT),
		STUMBLE_ARP("STUMBLE ARP", StatEnum.STUMBLE_RESISTANCE_PENETRATION),
		STUN_ARP("STUN ARP", StatEnum.STUN_RESISTANCE_PENETRATION),
		VIT("VIT", StatEnum.HEALTH),
		WIL("WIL", StatEnum.WILL);

		private String value;
		private StatEnum statEnum;

		public String getValue() {
			return value;
		}

		public StatEnum getStatEnum() {
			return statEnum;
		}

		private AttrBonusAction(String value, StatEnum statEnum) {
			this.value = value;
			this.statEnum = statEnum;
		}

		AttrBonusAction(String value) throws IllegalArgumentException {
			for (AttrBonusAction t : AttrBonusAction.values()) {
				if (t.getValue() == value) {
					return;
				}
			}
			throw new IllegalArgumentException("Unsupported type: " + value);
		}

	}

	class StatChangeFunction extends StatFunction {

		static final int speed = 6000;
		static final int flyspeed = 9000;
		static final int maxDp = 4000;
		int modifier = 1;

		StatChangeFunction(StatEnum stat, int modifier) {
			this.stat = stat;
			this.modifier = modifier;
		}

		@Override
		public void apply(Stat2 stat) {
			switch (this.stat) {
				case SPEED:
					stat.setBase(speed + (speed * modifier) / 100);
					break;
				case FLY_SPEED:
					stat.setBase(flyspeed + (flyspeed * modifier) / 100);
					break;
				case POWER:
					short modifierPower = (short) modifier;
					stat.setBase(Math.round(modifierPower));
					break;
				case MAXHP:
					float modifierHp = modifier;
					stat.setBase(Math.round(modifierHp));
					break;
				case MAXMP:
					float modifierMp = modifier;
					stat.setBase(Math.round(modifierMp));
					break;
				case REGEN_HP:
					int baseHp = stat.getOwner().getLevel() + 3;
					stat.setBase(baseHp *= modifier / 100f);
					break;
				case REGEN_MP:
					int baseMp = stat.getOwner().getLevel() + 8;
					stat.setBase(baseMp *= modifier / 100f);
					break;
				case MAXDP:
					stat.setBase(maxDp + (maxDp * modifier) / 100);
					break;
				case ALLRESIST:
					short modifierALLR = (short) modifier;
					stat.setBase(Math.round(modifierALLR));
					break;
				case ABNORMAL_RESISTANCE_ALL:
					short modifierAbnormalALLR = (short) modifier;
					stat.setBase(Math.round(modifierAbnormalALLR));
					break;
				case STRVIT:
					short modifierStrvit = (short) modifier;
					stat.setBase(Math.round(modifierStrvit));
					break;
				case KNOWIL:
					short modifierKnowil = (short) modifier;
					stat.setBase(Math.round(modifierKnowil));
					break;
				case AGIDEX:
					short modifierAgidex = (short) modifier;
					stat.setBase(Math.round(modifierAgidex));
					break;
				case HEALTH:
					short modifierHealth = (short) modifier;
					stat.setBase(Math.round(modifierHealth));
					break;
				case ACCURACY:
					short modifierAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierAccuracy));
					break;
				case AGILITY:
					short modifierAgility = (short) modifier;
					stat.setBase(Math.round(modifierAgility));
					break;
				case KNOWLEDGE:
					short modifierKnow = (short) modifier;
					stat.setBase(Math.round(modifierKnow));
					break;
				case WILL:
					short modifierWill = (short) modifier;
					stat.setBase(Math.round(modifierWill));
					break;
				case WATER_RESISTANCE:
					short modifierWaterRes = (short) modifier;
					stat.setBase(Math.round(modifierWaterRes));
					break;
				case WIND_RESISTANCE:
					short modifierWindRes = (short) modifier;
					stat.setBase(Math.round(modifierWindRes));
					break;
				case EARTH_RESISTANCE:
					short modifierEarthRes = (short) modifier;
					stat.setBase(Math.round(modifierEarthRes));
					break;
				case FIRE_RESISTANCE:
					short modifierFireRes = (short) modifier;
					stat.setBase(Math.round(modifierFireRes));
					break;
				case REGEN_FP:
					stat.setBase(Math.round(modifier));
					break;
				case PHYSICAL_DEFENSE:
					stat.setBase(Math.round(modifier));
					break;
				case MAGICAL_ATTACK:
					short modifierMAttack = (short) modifier;
					stat.setBase(Math.round(modifierMAttack));
					break;
				case MAGICAL_RESIST:
					short modifierMResist = (short) modifier;
					stat.setBase(Math.round(modifierMResist));
					break;
				case ATTACK_SPEED:
					short modifierASpeed = (short) modifier;
					stat.setBase(Math.round(modifierASpeed / 2));
					break;
				case EVASION:
					short modifierEvasion = (short) modifier;
					stat.setBase(Math.round(modifierEvasion));
					break;
				case PARRY:
					short modifierParry = (short) modifier;
					stat.setBase(Math.round(modifierParry));
					break;
				case BLOCK:
					short modifierBlock = (short) modifier;
					stat.setBase(Math.round(modifierBlock));
					break;
				case PHYSICAL_CRITICAL:
					short modifierPCrit = (short) modifier;
					stat.setBase(Math.round(modifierPCrit));
					break;
				case HIT_COUNT:
					short modifierHCount = (short) modifier;
					stat.setBase(Math.round(modifierHCount));
					break;
				case ATTACK_RANGE:
					float modifierARange = modifier;
					stat.setBase(Math.round(modifierARange));
					break;
				case MAGICAL_CRITICAL:
					short modifierMCrit = (short) modifier;
					stat.setBase(Math.round(modifierMCrit));
					break;
				case CONCENTRATION:
					short modifierConcentration = (short) modifier;
					stat.setBase(Math.round(modifierConcentration));
					break;
				case POISON_RESISTANCE:
					short modifierPResist = (short) modifier;
					stat.setBase(Math.round(modifierPResist));
					break;
				case BLEED_RESISTANCE:
					short modifierBResist = (short) modifier;
					stat.setBase(Math.round(modifierBResist));
					break;
				case PARALYZE_RESISTANCE:
					short modifierPAResist = (short) modifier;
					stat.setBase(Math.round(modifierPAResist));
					break;
				case SLEEP_RESISTANCE:
					short modifierSResist = (short) modifier;
					stat.setBase(Math.round(modifierSResist));
					break;
				case ROOT_RESISTANCE:
					short modifierRResist = (short) modifier;
					stat.setBase(Math.round(modifierRResist));
					break;
				case BLIND_RESISTANCE:
					short modifierBLResist = (short) modifier;
					stat.setBase(Math.round(modifierBLResist));
					break;
				case CHARM_RESISTANCE:
					short modifierCResist = (short) modifier;
					stat.setBase(Math.round(modifierCResist));
					break;
				case DISEASE_RESISTANCE:
					short modifierDResist = (short) modifier;
					stat.setBase(Math.round(modifierDResist));
					break;
				case SILENCE_RESISTANCE:
					short modifierSIResist = (short) modifier;
					stat.setBase(Math.round(modifierSIResist));
					break;
				case FEAR_RESISTANCE:
					short modifierFResist = (short) modifier;
					stat.setBase(Math.round(modifierFResist));
					break;
				case CURSE_RESISTANCE:
					short modifierCUResist = (short) modifier;
					stat.setBase(Math.round(modifierCUResist));
					break;
				case CONFUSE_RESISTANCE:
					short modifierCOResist = (short) modifier;
					stat.setBase(Math.round(modifierCOResist));
					break;
				case STUN_RESISTANCE:
					short modifierSTResist = (short) modifier;
					stat.setBase(Math.round(modifierSTResist));
					break;
				case PERIFICATION_RESISTANCE:
					short modifierPEResist = (short) modifier;
					stat.setBase(Math.round(modifierPEResist));
					break;
				case STUMBLE_RESISTANCE:
					short modifierSTUResist = (short) modifier;
					stat.setBase(Math.round(modifierSTUResist));
					break;
				case STAGGER_RESISTANCE:
					short modifierSTAResist = (short) modifier;
					stat.setBase(Math.round(modifierSTAResist));
					break;
				case OPENAREIAL_RESISTANCE:
					short modifierOResist = (short) modifier;
					stat.setBase(Math.round(modifierOResist));
					break;
				case SNARE_RESISTANCE:
					short modifierSNResist = (short) modifier;
					stat.setBase(Math.round(modifierSNResist));
					break;
				case SLOW_RESISTANCE:
					short modifierSLResist = (short) modifier;
					stat.setBase(Math.round(modifierSLResist));
					break;
				case SPIN_RESISTANCE:
					short modifierSPResist = (short) modifier;
					stat.setBase(Math.round(modifierSPResist));
					break;
				case POISON_RESISTANCE_PENETRATION:
					short modifierPRP = (short) modifier;
					stat.setBase(Math.round(modifierPRP));
					break;
				case BLEED_RESISTANCE_PENETRATION:
					short modifierBRP = (short) modifier;
					stat.setBase(Math.round(modifierBRP));
					break;
				case PARALYZE_RESISTANCE_PENETRATION:
					short modifierPARP = (short) modifier;
					stat.setBase(Math.round(modifierPARP));
					break;
				case SLEEP_RESISTANCE_PENETRATION:
					short modifierSRP = (short) modifier;
					stat.setBase(Math.round(modifierSRP));
					break;
				case ROOT_RESISTANCE_PENETRATION:
					short modifierRRP = (short) modifier;
					stat.setBase(Math.round(modifierRRP));
					break;
				case BLIND_RESISTANCE_PENETRATION:
					short modifierBLRP = (short) modifier;
					stat.setBase(Math.round(modifierBLRP));
					break;
				case CHARM_RESISTANCE_PENETRATION:
					short modifierCRP = (short) modifier;
					stat.setBase(Math.round(modifierCRP));
					break;
				case DISEASE_RESISTANCE_PENETRATION:
					short modifierDRP = (short) modifier;
					stat.setBase(Math.round(modifierDRP));
					break;
				case SILENCE_RESISTANCE_PENETRATION:
					short modifierSIRP = (short) modifier;
					stat.setBase(Math.round(modifierSIRP));
					break;
				case FEAR_RESISTANCE_PENETRATION:
					short modifierFRP = (short) modifier;
					stat.setBase(Math.round(modifierFRP));
					break;
				case CURSE_RESISTANCE_PENETRATION:
					short modifierCURP = (short) modifier;
					stat.setBase(Math.round(modifierCURP));
					break;
				case CONFUSE_RESISTANCE_PENETRATION:
					short modifierCORP = (short) modifier;
					stat.setBase(Math.round(modifierCORP));
					break;
				case STUN_RESISTANCE_PENETRATION:
					short modifierSTRP = (short) modifier;
					stat.setBase(Math.round(modifierSTRP));
					break;
				case PERIFICATION_RESISTANCE_PENETRATION:
					short modifierPERP = (short) modifier;
					stat.setBase(Math.round(modifierPERP));
					break;
				case STUMBLE_RESISTANCE_PENETRATION:
					short modifierSTURP = (short) modifier;
					stat.setBase(Math.round(modifierSTURP));
					break;
				case STAGGER_RESISTANCE_PENETRATION:
					short modifierSTARP = (short) modifier;
					stat.setBase(Math.round(modifierSTARP));
					break;
				case OPENAREIAL_RESISTANCE_PENETRATION:
					short modifierORP = (short) modifier;
					stat.setBase(Math.round(modifierORP));
					break;
				case SNARE_RESISTANCE_PENETRATION:
					short modifierSNRP = (short) modifier;
					stat.setBase(Math.round(modifierSNRP));
					break;
				case SLOW_RESISTANCE_PENETRATION:
					short modifierSLRP = (short) modifier;
					stat.setBase(Math.round(modifierSLRP));
					break;
				case SPIN_RESISTANCE_PENETRATION:
					short modifierSPRP = (short) modifier;
					stat.setBase(Math.round(modifierSPRP));
					break;
				case BOOST_MAGICAL_SKILL:
					short modifierBMSkill = (short) modifier;
					stat.setBase(Math.round(modifierBMSkill));
					break;
				case MAGICAL_ACCURACY:
					short modifierMAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierMAccuracy));
					break;
				case BOOST_CASTING_TIME:
					short modifierBCTime = (short) modifier;
					stat.setBase(Math.round(modifierBCTime));
					break;
				case HEAL_BOOST:
					short modifierHBoost = (short) modifier;
					stat.setBase(Math.round(modifierHBoost));
					break;
				case PHYSICAL_CRITICAL_RESIST:
					short modifierPCResist = (short) modifier;
					stat.setBase(Math.round(modifierPCResist));
					break;
				case MAGICAL_CRITICAL_RESIST:
					short modifierMCResist = (short) modifier;
					stat.setBase(Math.round(modifierMCResist));
					break;
				case PHYSICAL_CRITICAL_DAMAGE_REDUCE:
					short modifierPCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierPCDReduce));
					break;
				case MAGICAL_CRITICAL_DAMAGE_REDUCE:
					short modifierMCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierMCDReduce));
					break;
				case MAGICAL_DEFEND:
					stat.setBase(Math.round(modifier));
					break;
				case MAGIC_SKILL_BOOST_RESIST:
					short modifierMSBResist = (short) modifier;
					stat.setBase(Math.round(modifierMSBResist));
					break;
				default:
					break;
			}
		}

		@Override
		public int getPriority() {
			return 60;
		}
	}

}
