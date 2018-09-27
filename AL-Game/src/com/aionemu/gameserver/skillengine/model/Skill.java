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
package com.aionemu.gameserver.skillengine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.handler.ShoutEventHandler;
import com.aionemu.gameserver.ai2.manager.SkillAttackManager;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.controllers.observer.StartMovingListener;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skinskill.SkillSkin;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CASTSPELL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CASTSPELL_RESULT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.MotionLoggingService;
import com.aionemu.gameserver.services.abyss.AbyssService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.action.Action;
import com.aionemu.gameserver.skillengine.action.Actions;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.properties.FirstTargetAttribute;
import com.aionemu.gameserver.skillengine.properties.Properties;
import com.aionemu.gameserver.skillengine.properties.TargetRangeAttribute;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author ATracer Modified by Wakzashi
 * @Reworked Kill3r
 */
public class Skill {

	private SkillMethod skillMethod = SkillMethod.CAST;
	private List<Creature> effectedList;
	private Creature firstTarget;
	protected Creature effector;
	private int skillLevel;
	private int skillStackLvl;
	protected StartMovingListener conditionChangeListener;
	private SkillTemplate skillTemplate;
	private boolean firstTargetRangeCheck = true;
	private ItemTemplate itemTemplate;
	private int itemObjectId = 0;
	private int targetType;
	private boolean chainSuccess;
	private boolean isCancelled = false;
	private boolean blockedPenaltySkill = false;
	private float x;
	private float y;
	private float z;
	private byte h;
	protected int boostSkillCost;
	private FirstTargetAttribute firstTargetAttribute;
	private TargetRangeAttribute targetRangeAttribute;
	private int skillskinId = 0;
	private int skillskinHitTIme = 0;
	/**
	 * Duration that depends on BOOST_CASTING_TIME
	 */
	private int duration;
	private int hitTime;// from CM_CASTSPELL
	private int serverTime;// time when effect is applied
	private long castStartTime;
	private String chainCategory = null;
	private volatile boolean isMultiCast = false;
	private List<ChargedSkill> chargeSkillList = new ArrayList<ChargedSkill>();

	public enum SkillMethod {

		CAST,
		ITEM,
		PASSIVE,
		PROVOKED,
		CHARGE;
	}

	private Logger log = LoggerFactory.getLogger(Skill.class);

	/**
	 * Each skill is a separate object upon invocation Skill level will be populated from player SkillList
	 *
	 * @param skillTemplate
	 * @param effector
	 * @param world
	 */
	public Skill(SkillTemplate skillTemplate, Player effector, Creature firstTarget) {
		this(skillTemplate, effector, effector.getSkillList().getSkillLevel(skillTemplate.getSkillId()), firstTarget, null);
	}

	public Skill(SkillTemplate skillTemplate, Player effector, Creature firstTarget, int skillLevel) {
		this(skillTemplate, effector, skillLevel, firstTarget, null);
	}

	/**
	 * @param skillTemplate
	 * @param effector
	 * @param skillLvl
	 * @param firstTarget
	 */
	public Skill(SkillTemplate skillTemplate, Creature effector, int skillLvl, Creature firstTarget, ItemTemplate itemTemplate) {
		this.effectedList = new ArrayList<Creature>();
		this.conditionChangeListener = new StartMovingListener();
		this.firstTarget = firstTarget;
		this.skillLevel = skillLvl;
		this.skillStackLvl = skillTemplate.getLvl();
		this.skillTemplate = skillTemplate;
		this.effector = effector;
		this.duration = skillTemplate.getDuration();
		this.itemTemplate = itemTemplate;

		if (itemTemplate != null) {
			skillMethod = SkillMethod.ITEM;
		}
		else if (skillTemplate.isPassive()) {
			skillMethod = SkillMethod.PASSIVE;
		}
		else if (skillTemplate.isProvoked()) {
			skillMethod = SkillMethod.PROVOKED;
		}
		else if (skillTemplate.isCharge()) {
			skillMethod = SkillMethod.CHARGE;
		}
	}

	/**
	 * Check if the skill can be used
	 *
	 * @return True if the skill can be used
	 */
	public boolean canUseSkill() {
		Properties properties = skillTemplate.getProperties();
		if (properties != null && !properties.validate(this)) {
			log.debug("properties failed");
			return false;
		}

		if (!preCastCheck()) {
			return false;
		}

		// check for counter skill
		if (effector instanceof Player) {
			Player player = (Player) effector;
			if (this.skillTemplate.getCounterSkill() != null) {
				long time = player.getLastCounterSkill(skillTemplate.getCounterSkill());
				if ((time + 5000) < System.currentTimeMillis()) {
					log.debug("chain skill failed, too late");
					return false;
				}
			}

			if (skillMethod == SkillMethod.ITEM && duration > 0 && player.getMoveController().isInMove()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(getItemTemplate().getNameId())));
				return false;
			}
		}
		if (!validateEffectedList()) {
			return false;
		}

		return true;
	}

	private boolean validateEffectedList() {
		Iterator<Creature> effectedIter = effectedList.iterator();
		while (effectedIter.hasNext()) {
			Creature effected = effectedIter.next();
			if (effected == null) {
				effected = effector;
			}

			if (effector instanceof Player) {
				if (!RestrictionsManager.canAffectBySkill((Player) effector, effected, this)) {
					effectedIter.remove();
				}
			}
			else {
				if (effector.getEffectController().isAbnormalState(AbnormalState.CANT_ATTACK_STATE)) {
					effectedIter.remove();
				}
			}
		}

		// TODO: Enable non-targeted, non-point AOE skills to trigger.
		if (targetType == 0 && effectedList.size() == 0 && firstTargetAttribute != FirstTargetAttribute.ME && targetRangeAttribute != TargetRangeAttribute.AREA) {
			log.debug("targettype failed");
			return false;
		}

		return true;
	}

	/**
	 * Skill entry point
	 *
	 * @return true if usage is successfull
	 */
	public boolean useSkill() {
		return useSkill(true, true);
	}

	public boolean useNoAnimationSkill() {
		return useSkill(false, true);
	}

	public boolean useWithoutPropSkill() {
		return useSkill(false, false);
	}

	private boolean useSkill(boolean checkAnimation, boolean checkproperties) {
		if (checkproperties && !canUseSkill()) {
			return false;
		}

		if (skillMethod != SkillMethod.CHARGE) {
			calculateSkillDuration();
		}

		if (SecurityConfig.MOTION_TIME) {
			// must be after calculateskillduration
			if (checkAnimation && !checkAnimationTime()) {
				log.debug("check animation time failed");
				return false;
			}
		}

		boostSkillCost = 0;
		getSkillSkinData();

		// notify skill use observers
		if (skillMethod == SkillMethod.CAST || skillMethod == SkillMethod.CHARGE) {
			effector.getObserveController().notifySkilluseObservers(this);
		}

		// start casting
		effector.setCasting(this);

		// log skill time if effector instance of player
		// TODO config
		if (effector instanceof Player) {
			if (((Player) effector).getAccessLevel() > 1) {
				MotionLoggingService.getInstance().logTime((Player) effector, this.getSkillTemplate(), this.getHitTime(), MathUtil.getDistance(effector, firstTarget));
			}
		}

		// send packets to start casting
		if (skillMethod == SkillMethod.CAST || skillMethod == SkillMethod.ITEM || skillMethod == SkillMethod.CHARGE) {
			castStartTime = System.currentTimeMillis();
			startCast();
			if (effector instanceof Npc) {
				((NpcAI2) ((Npc) effector).getAi2()).setSubStateIfNot(AISubState.CAST);
			}
		}

		effector.getObserveController().attach(conditionChangeListener);

		if (this.duration > 0) {
			schedule(this.duration);
		}
		else {
			endCast();
		}
		return true;
	}

	private void setCooldowns() {
		int cooldown = effector.getSkillCooldown(skillTemplate);
		if (cooldown != 0) {
			cooldown = StigmaEnchantCoolDown(this, cooldown);
			effector.setSkillCoolDown(skillTemplate.getCooldownId(), cooldown * 100 + System.currentTimeMillis());
			effector.setSkillCoolDownBase(skillTemplate.getCooldownId(), System.currentTimeMillis());
		}
	}

	public int StigmaEnchantCoolDown(Skill skill, int cooldown) {
		if (skill == null) {
			return 0;
		}
		int SkillLevel = skill.getSkillLevel();
		switch (skill.getSkillId()) {
			case 564: // Dauntless Spirit
			case 565: // Dauntless Spirit
			case 566: // Dauntless Spirit
			case 567: // Dauntless Spirit
			case 568: // Dauntless Spirit
			case 569: // Dauntless Spirit
			case 570: // Dauntless Spirit
			case 571: // Dauntless Spirit
			case 727: // Wind Lance
			case 728: // Wind Lance
			case 729: // Wind Lance
			case 730: // Wind Lance
			case 731: // Wind Lance
			case 732: // Wind Lance
			case 755: // Whirling Strike
			case 756: // Whirling Strike
			case 757: // Whirling Strike
			case 1100: // Trap Of Clairvoyance
			case 1101: // Trap Of Clairvoyance
			case 1324: // Glacial Shard
			case 1325: // Glacial Shard
			case 1326: // Glacial Shard
			case 1640: // Annihilation
			case 1641: // Annihilation
			case 1642: // Annihilation
			case 1643: // Annihilation
			case 1644: // Annihilation
			case 1645: // Annihilation
			case 1646: // Annihilation
			case 1647: // Annihilation
			case 1727: // Word Of Life
			case 1728: // Word Of Life
			case 1729: // Word Of Life
			case 1730: // Word Of Life
			case 1731: // Word Of Life
			case 1732: // Word Of Life
			case 1733: // Word Of Life
			case 1734: // Word Of Life
			case 1863: // Disorienting Blow
			case 1864: // Disorienting Blow
			case 1865: // Disorienting Blow
			case 1866: // Disorienting Blow
			case 1867: // Disorienting Blow
			case 1868: // Disorienting Blow
			case 1883: // Burst
			case 1884: // Burst
			case 1885: // Burst
			case 1886: // Burst
			case 1887: // Burst
			case 1888: // Burst
			case 1889: // Burst
			case 1890: // Burst
			case 1907: // Word of Instigation
			case 1908: // Word of Instigation
			case 1909: // Word of Instigation
			case 2046: // Stopping Power
			case 2054: // Autoload
			case 2268: // Sighting
			case 2269: // Sighting
			case 2270: // Sighting
			case 2271: // Sighting
			case 2272: // Sighting
			case 2273: // Sighting
			case 2391: // Drillbore
			case 2392: // Drillbore
			case 2393: // Drillbore
			case 2394: // Drillbore
			case 2395: // Drillbore
			case 2396: // Drillbore
			case 2397: // Drillbore
			case 2398: // Drillbore
			case 2409: // Debilitating Blade
			case 2410: // Debilitating Blade
			case 2411: // Debilitating Blade
			case 2412: // Debilitating Blade
			case 2413: // Debilitating Blade
			case 2414: // Debilitating Blade
			case 2464: // Aether Recharge
			case 2467: // Aether Recharge
			case 2470: // Aether Recharge
			case 2473: // Aether Recharge
			case 2476: // Aether Recharge
			case 2479: // Aether Recharge
			case 2482: // Aether Recharge
			case 2485: // Aether Recharge
			case 2711: // Convulsion Beam
			case 2712: // Convulsion Beam
			case 2713: // Convulsion Beam
			case 2714: // Convulsion Beam
			case 2715: // Convulsion Beam
			case 2716: // Convulsion Beam
			case 2919: // Invigorating Strike
			case 2920: // Invigorating Strike
			case 2921: // Invigorating Strike
			case 2945: // Incite Rage
			case 2946: // Incite Rage
			case 2947: // Incite Rage
			case 2948: // Incite Rage
			case 2949: // Incite Rage
			case 2950: // Incite Rage
			case 2951: // Incite Rage
			case 2952: // Incite Rage
			case 2961: // Holy Shield
			case 2962: // Holy Shield
			case 2963: // Holy Shield
			case 2964: // Holy Shield
			case 2965: // Holy Shield
			case 2966: // Holy Shield
			case 3147: // Punishing Thrust
			case 3148: // Punishing Thrust
			case 3149: // Punishing Thrust
			case 3150: // Punishing Thrust
			case 3151: // Punishing Thrust
			case 3152: // Punishing Thrust
			case 3153: // Punishing Thrust
			case 3154: // Punishing Thrust
			case 3242: // Explosive Rebranding
			case 3243: // Explosive Rebranding
			case 3244: // Explosive Rebranding
			case 3246: // Quickening Doom
			case 3247: // Quickening Doom
			case 3248: // Quickening Doom
			case 3312: // Eye Of Wrath
			case 3330: // Shadowfall
			case 3731: // Magic's Freedom
			case 3796: // Armor Spirit
			case 3980: // Summon Healing Servant
			case 3981: // Summon Healing Servant
			case 3982: // Summon Healing Servant
			case 3983: // Summon Healing Servant
			case 3984: // Summon Healing Servant
			case 3985: // Summon Healing Servant
			case 3986: // Summon Healing Servant
			case 3987: // Summon Healing Servant
			case 3988: // Summon Healing Servant
			case 3989: // Summon Healing Servant
			case 3990: // Summon Healing Servant
			case 3991: // Summon Healing Servant
			case 3998: // Splendor Of Rebirth
			case 3999: // Splendor Of Rebirth
			case 4000: // Splendor Of Rebirth
			case 4001: // Splendor Of Rebirth
			case 4002: // Splendor Of Rebirth
			case 4003: // Splendor Of Rebirth
			case 4134: // Festering Wound
			case 4164: // Call Lightning
			case 4165: // Call Lightning
			case 4166: // Call Lightning
			case 4384: // Resonant Hymn
			case 4385: // Resonant Hymn
			case 4386: // Resonant Hymn
			case 4387: // Resonant Hymn
			case 4388: // Resonant Hymn
			case 4389: // Resonant Hymn
			case 4390: // Resonant Hymn
			case 4474: // Blazing Requiem
			case 4477: // Blazing Requiem
			case 4480: // Blazing Requiem
			case 4484: // Chorus Of Blessing
			case 4485: // Chorus Of Blessing
			case 4486: // Chorus Of Blessing
			case 4487: // Treble Cleave
			case 4488: // Treble Cleave
			case 4489: // Treble Cleave
			case 4491: // Mvt.2: Summer
			case 4492: // Mvt.2: Summer
			case 4493: // Mvt.2: Summer
			case 4494: // Mvt.2: Summer
			case 4495: // Mvt.2: Summer
			case 4496: // Mvt.2: Summer
			case 4497: // Mvt.3: Autumn
			case 4498: // Mvt.3: Autumn
			case 4499: // Mvt.3: Autumn
			case 4500: // Mvt.3: Autumn
			case 4501: // Mvt.3: Autumn
			case 4502: // Mvt.3: Autumn
			case 4524: // Paean Of Pain
			case 4525: // Paean Of Pain
			case 4526: // Paean Of Pain
			case 4527: // Paean Of Pain
			case 4528: // Paean Of Pain
			case 4529: // Paean Of Pain
			case 4572: // Combustible Cacophony
			case 4573: // Combustible Cacophony
			case 4574: // Combustible Cacophony
			case 4575: // Combustible Cacophony
			case 4576: // Combustible Cacophony
			case 4577: // Combustible Cacophony
			case 4578: // Combustible Cacophony
			case 4579: // Combustible Cacophony
			case 4591: // Shadowfall
			case 4592: // Shadowfall
			case 4593: // Shadowfall
			case 4594: // Shadowfall
			case 4595: // Shadowfall
			case 4596: // Shadowfall
				return cooldown - 6 * SkillLevel;
			case 600: // Magical Defense
			case 641: // Unraveling Assault
			case 642: // Unraveling Assault
			case 643: // Unraveling Assault
			case 1351: // Summon Rock
			case 1352: // Summon Rock
			case 1353: // Summon Rock
			case 1354: // Summon Rock
			case 1355: // Summon Rock
			case 1356: // Summon Rock
			case 1801: // Acceleration Cheer
			case 1802: // Acceleration Cheer
			case 1803: // Acceleration Cheer
			case 1804: // Acceleration Cheer
			case 1805: // Acceleration Cheer
			case 1806: // Acceleration Cheer
			case 1807: // Acceleration Cheer
			case 1808: // Acceleration Cheer
			case 2750: // Aethercharged Steel
			case 2751: // Aethercharged Steel
			case 2752: // Aethercharged Steel
			case 2753: // Aethercharged Steel
			case 2754: // Aethercharged Steel
			case 2755: // Aethercharged Steel
			case 3590: // Healing Spirit
			case 3903: // Power Sprint
			case 4182: // Enfeebling Burst
			case 4183: // Enfeebling Burst
			case 4184: // Enfeebling Burst
			case 4185: // Enfeebling Burst
			case 4186: // Enfeebling Burst
			case 4187: // Enfeebling Burst
				return cooldown - 9 * SkillLevel;
			case 539: // Exhausting Wave
			case 540: // Exhausting Wave
			case 541: // Exhausting Wave
			case 542: // Exhausting Wave
			case 543: // Exhausting Wave
			case 544: // Exhausting Wave
			case 612: // Tendon Slice
			case 613: // Tendon Slice
			case 614: // Tendon Slice
			case 615: // Tendon Slice
			case 616: // Tendon Slice
			case 617: // Tendon Slice
			case 618: // Ankle Snare
			case 698: // Earthquake Wave
			case 699: // Earthquake Wave
			case 700: // Earthquake Wave
			case 701: // Earthquake Wave
			case 702: // Earthquake Wave
			case 703: // Earthquake Wave
			case 704: // Earthquake Wave
			case 705: // Earthquake Wave
			case 749: // Revival Wave
			case 750: // Revival Wave
			case 751: // Revival Wave
			case 752: // Revival Wave
			case 753: // Revival Wave
			case 754: // Revival Wave
			case 849: // Trap Of Slowing
			case 850: // Trap Of Slowing
			case 851: // Trap Of Slowing
			case 852: // Trap Of Slowing
			case 853: // Trap Of Slowing
			case 854: // Trap Of Slowing
			case 855: // Trap Of Slowing
			case 856: // Trap Of Slowing
			case 857: // Trap Of Slowing
			case 858: // Trap Of Slowing
			case 859: // Trap Of Slowing
			case 860: // Trap Of Slowing
			case 861: // Trap Of Slowing
			case 862: // Trap Of Slowing
			case 863: // Trap Of Slowing
			case 864: // Trap Of Slowing
			case 888: // Hunter's Might
			case 962: // Skybound Trap
			case 963: // Skybound Trap
			case 964: // Skybound Trap
			case 965: // Skybound Trap
			case 966: // Skybound Trap
			case 967: // Skybound Trap
			case 968: // Skybound Trap
			case 969: // Skybound Trap
			case 970: // Skybound Trap
			case 971: // Skybound Trap
			case 972: // Skybound Trap
			case 973: // Skybound Trap
			case 974: // Skybound Trap
			case 975: // Skybound Trap
			case 976: // Skybound Trap
			case 977: // Skybound Trap
			case 1006: // Ripthread Shot
			case 1007: // Ripthread Shot
			case 1008: // Ripthread Shot
			case 1486: // Storm Strike
			case 1487: // Storm Strike
			case 1488: // Storm Strike
			case 1489: // Storm Strike
			case 1490: // Storm Strike
			case 1491: // Storm Strike
			case 1492: // Storm Strike
			case 1493: // Storm Strike
			case 1901: // Resonant Strike
			case 1902: // Resonant Strike
			case 1903: // Resonant Strike
			case 2109: // Paralysis Cannon
			case 2110: // Paralysis Cannon
			case 2111: // Paralysis Cannon
			case 2112: // Paralysis Cannon
			case 2113: // Paralysis Cannon
			case 2114: // Paralysis Cannon
			case 2274: // Missile Guide
			case 2277: // Missile Guide
			case 2280: // Missile Guide
			case 2283: // Missile Guide
			case 2286: // Missile Guide
			case 2289: // Missile Guide
			case 2292: // Missile Guide
			case 2295: // Missile Guide
			case 2371: // Sequential Fire
			case 2374: // Sequential Fire
			case 2377: // Sequential Fire
			case 2380: // Pulverizer Cannon
			case 2381: // Pulverizer Cannon
			case 2382: // Pulverizer Cannon
			case 2450: // Life Support Trigger
			case 2451: // Life Support Trigger
			case 2452: // Life Support Trigger
			case 2453: // Life Support Trigger
			case 2454: // Life Support Trigger
			case 2455: // Life Support Trigger
			case 2456: // Life Support Trigger
			case 2457: // Life Support Trigger
			case 2939: // Divine Justice
			case 2940: // Divine Justice
			case 2941: // Divine Justice
			case 2942: // Divine Justice
			case 2943: // Divine Justice
			case 2944: // Divine Justice
			case 3239: // Fangdrop Stab
			case 3240: // Fangdrop Stab
			case 3241: // Fangdrop Stab
			case 3245: // Scoundrel's Bond
			case 3255: // Venomous Strike
			case 3256: // Venomous Strike
			case 3257: // Venomous Strike
			case 3258: // Venomous Strike
			case 3259: // Venomous Strike
			case 3260: // Venomous Strike
			case 3261: // Venomous Strike
			case 3327: // Break Away
			case 3531: // Spirit Wall Of Protection
			case 3562: // Earthen Call
			case 3563: // Earthen Call
			case 3564: // Earthen Call
			case 3565: // Earthen Call
			case 3566: // Earthen Call
			case 3567: // Earthen Call
			case 3568: // Earthen Call
			case 3569: // Earthen Call
			case 3575: // Withering Gloom
			case 3576: // Withering Gloom
			case 3577: // Withering Gloom
			case 3578: // Withering Gloom
			case 3579: // Withering Gloom
			case 3580: // Withering Gloom
			case 3581: // Withering Gloom
			case 3924: // Saving Grace
			case 3925: // Saving Grace
			case 3926: // Saving Grace
			case 3927: // Saving Grace
			case 3928: // Saving Grace
			case 3929: // Saving Grace
			case 3930: // Saving Grace
			case 3931: // Saving Grace
			case 3992: // Ripple Of Purification
			case 3993: // Ripple Of Purification
			case 3994: // Ripple Of Purification
			case 3995: // Ripple Of Purification
			case 3996: // Ripple Of Purification
			case 3997: // Ripple Of Purification
			case 4135: // Blinding Light
			case 4368: // Healing Conduit
			case 4490: // Staggered Rest
			case 4631: // Healing Conduit
			case 4632: // Healing Conduit
			case 4633: // Healing Conduit
			case 4634: // Healing Conduit
			case 4635: // Healing Conduit
			case 4636: // Healing Conduit
			case 4637: // Healing Conduit
			case 4638: // Healing Conduit
				return cooldown - 24 * SkillLevel;
			case 657: // Battle Banner
			case 658: // Battle Banner
			case 659: // Battle Banner
			case 660: // Battle Banner
			case 661: // Battle Banner
			case 662: // Battle Banner
			case 1009: // Nature's Resolve
			case 1057: // Bow Of Blessing
			case 1305: // Wintry Armor
			case 1306: // Wintry Armor
			case 1307: // Wintry Armor
			case 1308: // Ice Sheet
			case 1309: // Ice Sheet
			case 1310: // Ice Sheet
			case 1311: // Ice Sheet
			case 1312: // Ice Sheet
			case 1313: // Ice Sheet
			case 1314: // Ice Sheet
			case 1315: // Ice Sheet
			case 1316: // Ice Sheet
			case 1317: // Ice Sheet
			case 1318: // Ice Sheet
			case 1319: // Ice Sheet
			case 1320: // Ice Sheet
			case 1321: // Ice Sheet
			case 1322: // Ice Sheet
			case 1323: // Ice Sheet
			case 1339: // Sleeping Storm
			case 1402: // Elemental Ward
			case 1460: // Manifest Tornado
			case 1461: // Manifest Tornado
			case 1462: // Manifest Tornado
			case 1463: // Manifest Tornado
			case 1464: // Manifest Tornado
			case 1465: // Manifest Tornado
			case 1466: // Manifest Tornado
			case 1467: // Manifest Tornado
			case 1468: // Manifest Tornado
			case 1469: // Manifest Tornado
			case 1470: // Manifest Tornado
			case 1471: // Manifest Tornado
			case 1472: // Manifest Tornado
			case 1473: // Manifest Tornado
			case 1540: // Aetherblaze
			case 1541: // Aetherblaze
			case 1542: // Aetherblaze
			case 1550: // Illusion Storm
			case 1551: // Illusion Storm
			case 1552: // Illusion Storm
			case 1553: // Illusion Storm
			case 1554: // Illusion Storm
			case 1555: // Illusion Storm
			case 1607: // Rise
			case 1608: // Rise
			case 1609: // Rise
			case 1610: // Rise
			case 1611: // Rise
			case 1612: // Rise
			case 1613: // Rise
			case 1651: // Blessing Of Wind
			case 1652: // Blessing Of Wind
			case 1653: // Blessing Of Wind
			case 1654: // Blessing Of Wind
			case 1655: // Blessing Of Wind
			case 1656: // Blessing Of Wind
			case 1832: // Elemental Screen
			case 1833: // Elemental Screen
			case 1834: // Elemental Screen
			case 1904: // Debilitating Incantation
			case 1905: // Debilitating Incantation
			case 1906: // Debilitating Incantation
			case 2033: // Nature's Favor
			case 2034: // Nature's Favor
			case 2035: // Nature's Favor
			case 2036: // Nature's Favor
			case 2037: // Nature's Favor
			case 2038: // Nature's Favor
			case 2039: // Nature's Favor
			case 2040: // Nature's Favor
			case 2368: // Pursuit Stance
			case 2369: // Pursuit Stance
			case 2370: // Pursuit Stance
			case 2383: // Aimbot Assist
			case 2384: // Aimbot Assist
			case 2385: // Aimbot Assist
			case 2386: // Aimbot Assist
			case 2387: // Aimbot Assist
			case 2388: // Aimbot Assist
			case 2389: // Aimbot Assist
			case 2390: // Aimbot Assist
			case 2458: // Trauma Plate Trigger
			case 2459: // Trauma Plate Trigger
			case 2460: // Trauma Plate Trigger
			case 2461: // Trauma Plate Trigger
			case 2462: // Trauma Plate Trigger
			case 2463: // Trauma Plate Trigger
			case 2825: // Leeching Steel
			case 2826: // Leeching Steel
			case 2827: // Leeching Steel
			case 2828: // Leeching Steel
			case 2829: // Leeching Steel
			case 2830: // Leeching Steel
			case 2831: // Leeching Steel
			case 2832: // Leeching Steel
			case 2849: // Nerve Pulse
			case 2850: // Nerve Pulse
			case 2851: // Nerve Pulse
			case 2852: // Explosive Exhaust
			case 2854: // Explosive Exhaust
			case 2858: // Explosive Exhaust
			case 2861: // Powerspike Trigger
			case 2862: // Powerspike Trigger
			case 2863: // Powerspike Trigger
			case 2915: // Eternal Denial
			case 2916: // Eternal Denial
			case 2917: // Eternal Denial
			case 2918: // Shield of Vengeance
			case 2934: // Aether Armor
			case 2935: // Aether Armor
			case 2936: // Aether Armor
			case 2937: // Aether Armor
			case 2938: // Aether Armor
			case 2968: // Punishing Wave
			case 2969: // Punishing Wave
			case 2970: // Punishing Wave
			case 2971: // Punishing Wave
			case 2972: // Punishing Wave
			case 2973: // Punishing Wave
			case 2974: // Shield Of Faith
			case 3035: // Divine Fury
			case 3155: // Prayer Of Resilience
			case 3156: // Prayer Of Resilience
			case 3157: // Prayer Of Resilience
			case 3158: // Prayer Of Resilience
			case 3159: // Prayer Of Resilience
			case 3160: // Prayer Of Resilience
			case 3236: // Shimmerbomb
			case 3237: // Shimmerbomb
			case 3238: // Shimmerbomb
			case 3319: // Sensory Boost
			case 3321: // Apply Lethal Venom
			case 3322: // Apply Lethal Venom
			case 3323: // Apply Lethal Venom
			case 3324: // Apply Lethal Venom
			case 3325: // Apply Lethal Venom
			case 3326: // Apply Lethal Venom
			case 3329: // Shadow Walk
			case 3332: // Dash And Slash
			case 3333: // Dash And Slash
			case 3334: // Dash And Slash
			case 3335: // Dash And Slash
			case 3336: // Dash And Slash
			case 3337: // Dash And Slash
			case 3480: // Oath Of Accuracy
			case 3541: // Spirit's Empowerment
			case 3542: // Spirit's Empowerment
			case 3543: // Spirit's Empowerment
			case 3544: // Cloaking Word
			case 3545: // Infernal Blight
			case 3546: // Infernal Blight
			case 3547: // Infernal Blight
			case 3836: // Spirit Burn-to-Ashes
			case 3849: // Blood Funnel
			case 3850: // Blood Funnel
			case 3851: // Blood Funnel
			case 3932: // Restoration Relief
			case 3933: // Restoration Relief
			case 3934: // Restoration Relief
			case 4144: // Chain Of Suffering
			case 4145: // Chain Of Suffering
			case 4146: // Chain Of Suffering
			case 4147: // Chain Of Suffering
			case 4148: // Chain Of Suffering
			case 4149: // Chain Of Suffering
			case 4188: // Noble Grace
			case 4189: // Noble Grace
			case 4190: // Noble Grace
			case 4191: // Noble Grace
			case 4192: // Noble Grace
			case 4614: // Sensory Boost
				return cooldown - 36 * SkillLevel;
			case 683: // Howl
			case 684: // Howl
			case 685: // Howl
			case 686: // Howl
			case 687: // Howl
			case 688: // Howl
			case 689: // Howl
			case 690: // Howl
			case 936: // Night Haze
			case 937: // Night Haze
			case 938: // Night Haze
			case 1060: // Staggering Trap
			case 1061: // Staggering Trap
			case 1062: // Staggering Trap
			case 1063: // Staggering Trap
			case 1064: // Staggering Trap
			case 1065: // Staggering Trap
			case 1327: // Exchange Vitality
			case 1329: // Curse Of Weakness
			case 1330: // Curse Of Weakness
			case 1331: // Curse Of Weakness
			case 1332: // Curse Of Weakness
			case 1333: // Curse Of Weakness
			case 1334: // Curse Of Weakness
			case 1335: // Curse Of Weakness
			case 1336: // Curse Of Weakness
			case 1340: // Slumberswept Wind
			case 1341: // Slumberswept Wind
			case 1342: // Slumberswept Wind
			case 1418: // Repulsion Field
			case 1419: // Repulsion Field
			case 1420: // Repulsion Field
			case 2579: // Kinetic Bulwark
			case 2580: // Kinetic Bulwark
			case 2581: // Kinetic Bulwark
			case 2926: // Prayer of Victory
			case 2927: // Prayer of Victory
			case 2928: // Prayer of Victory
			case 2929: // Prayer of Victory
			case 2930: // Prayer of Victory
			case 2931: // Prayer of Victory
			case 3320: // Deadly Abandon
			case 3549: // Command: Absorb Wounds
			case 3906: // Summon Vexing Energy
			case 3907: // Summon Vexing Energy
			case 3908: // Summon Vexing Energy
			case 3909: // Summon Vexing Energy
			case 3910: // Summon Vexing Energy
			case 3911: // Summon Vexing Energy
				return cooldown - 60 * SkillLevel;
			case 2922: // Empyrean Providence
			case 3904: // Reverse Condition
				return cooldown - 120 * SkillLevel;
			// ArchDaeva Transformation 5.1 [Elyos]
			case 4752: // Transformation: Avatar Of Fire.
			case 4757: // Transformation: Avatar Of Water.
			case 4762: // Transformation: Avatar Of Earth.
			case 4768: // Transformation: Avatar Of Wind.
				// ArchDaeva Transformation 5.1 [Asmodians]
			case 4804: // Transformation: Avatar Of Fire.
			case 4805: // Transformation: Avatar Of Water.
			case 4806: // Transformation: Avatar Of Earth.
			case 4807: // Transformation: Avatar Of Wind.
				return cooldown - 300 * SkillLevel;
		}
		return cooldown;
	}

	protected void calculateSkillDuration() {
		// Skills that are not affected by boost casting time
		duration = 0;
		if (isCastTimeFixed()) {
			duration = skillTemplate.getDuration();
			return;
		}
		duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME, skillTemplate.getDuration());
		switch (skillTemplate.getSubType()) {
			case SUMMON:
				duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME_SUMMON, duration);
				break;
			case SUMMONHOMING:
				duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME_SUMMONHOMING, duration);
				break;
			case SUMMONTRAP:
				duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME_TRAP, duration);
				break;
			case HEAL:
				duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME_HEAL, duration);
				break;
			case ATTACK:
				if (skillTemplate.getType() == SkillType.MAGICAL) {
					duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME_ATTACK, duration);
				}
				break;
			default:
				break;
		}

		// fix
		if (skillTemplate.isBoostCastingTime()) {
			duration = effector.getGameStats().getPositiveReverseStat(StatEnum.BOOST_CASTING_TIME, skillTemplate.getDuration());
		}

		// 70% of base skill duration cap
		// No cast speed cap for skill Summoning Alacrity I(skillId: 3779) and Nimble Fingers I(skillId: 913) New Skill Ids
		if (!effector.getEffectController().hasAbnormalEffect(3779) && !effector.getEffectController().hasAbnormalEffect(913)) {
			int baseDurationCap = Math.round(skillTemplate.getDuration() * 0.3f);
			if (duration < baseDurationCap) {
				duration = baseDurationCap;
			}
		}

		if (effector instanceof Player) {
			if (this.isMulticast() && ((Player) effector).getChainSkills().getChainCount((Player) effector, this.getSkillTemplate(), this.chainCategory) != 0) {
				duration = 0;
			}
		}

		if (duration < 0) {
			duration = 0;
		}
	}

	private boolean checkAnimationTime() {
		if (!(effector instanceof Player) || skillMethod != SkillMethod.CAST)// TODO item skills?
		{
			return true;
		}
		Player player = (Player) effector;

		// if player is without weapon, dont check animation time
		if (player.getEquipment().getMainHandWeaponType() == null) {
			return true;
		}

		/**
		 * exceptions for certain skills -herb and mana treatment -traps Updated 4.8
		 */
		// dont check herb , mana treatment and concentration enhancement
		switch (this.getSkillId()) {
			case 245: // Bandage Heal
			case 246: // Herb Treatment I
			case 247: // Herb Treatment II
			case 251: // Herb Treatment III
			case 253: // Herb Treatment IV
			case 297: // Herb Treatment V
			case 308: // Herb Treatment VI
			case 309: // Herb Treatment VII
			case 310: // Herb Treatment VIII
			case 311: // Herb Treatment IX
			case 312: // Herb Treatment X
			case 313: // Herb Treatment XI
			case 314: // Herb Treatment XII
			case 249: // Mana Treatment I
			case 250: // Mana Treatment II
			case 252: // Mana Treatment III
			case 254: // Mana Treatment IV
			case 298: // Mana Treatment V
			case 315: // Mana Treatment VI
			case 316: // Mana Treatment VII
			case 317: // Mana Treatment VIII
			case 318: // Mana Treatment IX
			case 319: // Mana Treatment X
			case 320: // Mana Treatment XI
			case 321: // Mana Treatment XII
			case 3889: // Prayer Of Focus I
			case 3890: // Prayer Of Focus II
			case 3891: // Prayer Of Focus III
			case 3892: // Prayer Of Focus IV
			case 3893: // Prayer Of Focus V
			case 3894: // Prayer Of Focus VI
			case 4783: // [ArchDaeva] Prayer Of Focus 5.1
			case 11580: // Stigma Prayer Of Focus I
				return true;
		}
		if (this.getSkillTemplate().getSubType() == SkillSubType.SUMMONTRAP) {
			return true;
		}

		Motion motion = this.getSkillTemplate().getMotion();

		if (motion == null || motion.getName() == null) {
			log.warn("missing motion for skillId: " + this.getSkillId());
			return true;
		}

		if (motion.getInstantSkill() && hitTime != 0) {
			log.warn("Instant and hitTime not 0! modified client_skills? player objectid: " + player.getObjectId());
			return false;
		}
		else if (!motion.getInstantSkill() && hitTime == 0) {
			log.warn("modified client_skills! player objectid: " + player.getObjectId());
			return false;
		}

		MotionTime motionTime = DataManager.MOTION_DATA.getMotionTime(motion.getName());

		if (motionTime == null) {
			log.warn("missing motiontime for motionName: " + motion.getName() + " skillId: " + this.getSkillId());
			return true;
		}

		WeaponTypeWrapper weapons = new WeaponTypeWrapper(player.getEquipment().getMainHandWeaponType(), player.getEquipment().getOffHandWeaponType());
		float serverTime = motionTime.getTimeForWeapon(player.getRace(), player.getGender(), weapons);
		int clientTime = hitTime;

		if (serverTime == 0) {
			log.warn("missing weapon time for motionName: " + motion.getName() + " weapons: " + weapons.toString() + " skillId: " + this.getSkillId());
			return true;
		}

		// adjust client time with ammotime
		long ammoTime = 0;
		double distance = MathUtil.getDistance(effector, firstTarget);
		if (getSkillTemplate().getAmmoSpeed() != 0) {
			ammoTime = Math.round(distance / getSkillTemplate().getAmmoSpeed() * 1000);// checked with client
		}
		clientTime -= ammoTime;

		// adjust servertime with motion play speed
		if (motion.getSpeed() != 100) {
			serverTime /= 100f;
			serverTime *= motion.getSpeed();
		}

		Stat2 attackSpeed = player.getGameStats().getAttackSpeed();

		// adjust serverTime with attackSpeed
		if (attackSpeed.getBase() != attackSpeed.getCurrent()) {
			serverTime *= ((float) attackSpeed.getCurrent() / (float) attackSpeed.getBase());
		}

		// tolerance
		if (duration == 0) {
			serverTime *= 0.9f;
		}
		else {
			serverTime *= 0.5f;
		}

		int finalTime = Math.round(serverTime);
		if (motion.getInstantSkill() && hitTime == 0) {
			this.serverTime = (int) ammoTime;
		}
		else {
			if (clientTime < finalTime) {
				// check for no animation Hacks
				if (SecurityConfig.NO_ANIMATION) {
					float clientTme = clientTime;
					float serverTme = serverTime;
					float checkTme = clientTme / serverTme;
					// check if values are too low
					if (clientTime < 0 || checkTme < SecurityConfig.NO_ANIMATION_VALUE) {
						if (SecurityConfig.NO_ANIMATION_KICK) {
							player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
							AuditLogger.info(player, "Modified client_skills:" + this.getSkillId() + " (clientTime<finalTime:" + clientTime + "/" + finalTime + ") Kicking Player: " + player.getName());
						}
						else {
							AuditLogger.info(player, "Modified client_skills:" + this.getSkillId() + " (clientTime<finalTime:" + clientTime + "/" + finalTime + ")");
						}
						return false;
					}
				}
				log.warn("Possible modified client_skills:" + this.getSkillId() + " (clientTime<finalTime:" + clientTime + "/" + finalTime + ") player Name: " + player.getName());
			}
			this.serverTime = hitTime;
		}
		player.setNextSkillUse(System.currentTimeMillis() + duration + finalTime);
		return true;
	}

	/**
	 * Penalty success skill
	 */
	private void startPenaltySkill() {
		int penaltySkill = skillTemplate.getPenaltySkillId();
		if (penaltySkill == 0) {
			return;
		}

		SkillEngine.getInstance().applyEffectDirectly(penaltySkill, firstTarget, effector, 0);
	}

	/**
	 * Skin Skill
	 */
	private void getSkillSkinData() {
		if (effector instanceof Player && ((Player) effector).getSkillSkinList() != null) {
			for (SkillSkin skillSkin : ((Player) effector).getSkillSkinList().getSkillSkins()) {
				if (skillSkin.getTemplate() != null) {
					if (skillSkin.getTemplate().getSkillGroup().equalsIgnoreCase(skillTemplate.getSkillGroup()) && skillSkin.getIsActive() == 1) {
						skillskinHitTIme = skillSkin.getTemplate().getAmmoSpeed();
						skillskinId = skillSkin.getId();
						break;
					}
				}
			}
		}
	}

	/**
	 * Start casting of skill
	 */
	protected void startCast() {
		int targetObjId = firstTarget != null ? firstTarget.getObjectId() : 0;

		if (skillMethod == SkillMethod.CAST || skillMethod == SkillMethod.CHARGE) {
			switch (targetType) {
				case 0: // PlayerObjectId as Target
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL(effector.getObjectId(), skillTemplate.getSkillId(), skillLevel, targetType, targetObjId, this.duration, skillTemplate.isCharge(), skillskinId));
					if (firstTarget.getObjectId() != effector.getObjectId()) {
						PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(effector, firstTarget, SM_ATTACK_STATUS.TYPE.ATTACK, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
						// effector.getMoveController().skillMovement();
					}
					if (effector instanceof Npc && firstTarget instanceof Player) {
						NpcAI2 ai = (NpcAI2) effector.getAi2();
						if (ai.poll(AIQuestion.CAN_SHOUT)) {
							ShoutEventHandler.onCast(ai, firstTarget);
						}
					}
					break;

				case 3: // Target not in sight?
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL(effector.getObjectId(), skillTemplate.getSkillId(), skillLevel, targetType, 0, this.duration, skillTemplate.isCharge(), skillskinId));
					if (firstTarget.getObjectId() != effector.getObjectId()) {
						PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(effector, firstTarget, SM_ATTACK_STATUS.TYPE.ATTACK, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
						// effector.getMoveController().skillMovement();
					}
					break;

				case 1: // XYZ as Target
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL(effector.getObjectId(), skillTemplate.getSkillId(), skillLevel, targetType, x, y, z, this.duration, skillskinId));
					if (firstTarget.getObjectId() != effector.getObjectId()) {
						PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(effector, firstTarget, SM_ATTACK_STATUS.TYPE.ATTACK, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
						// effector.getMoveController().skillMovement();
					}
					break;
			}
		}
		else if (skillMethod == SkillMethod.ITEM && duration > 0) {
			PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(), (this.itemObjectId == 0 ? 0 : this.itemObjectId), itemTemplate.getTemplateId(), this.duration, 0)); // For testing
			//PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(), (this.itemObjectId == 0 ? 0 : this.itemObjectId), itemTemplate.getTemplateId(), this.duration, 0, 0));
		}
	}

	/**
	 * Set this skill as canceled
	 */
	public void cancelCast() {
		isCancelled = true;
	}

	/**
	 * Apply effects and perform actions specified in skill template
	 */
	protected void endCast() {
		if (!effector.isCasting() || isCancelled) {
			return;
		}

		// if target out of range
		if (skillTemplate == null) {
			return;
		}

		// Check if target is out of skill range
		Properties properties = skillTemplate.getProperties();
		if (properties != null && !properties.endCastValidate(this)) {
			effector.getController().cancelCurrentSkill();
			return;
		}

		if (!validateEffectedList()) {
			effector.getController().cancelCurrentSkill();
			return;
		}

		if (!preUsageCheck()) {
			return;
		}

		effector.setCasting(null);

		if (this.getSkillTemplate().isDeityAvatar() && effector instanceof Player) {
			AbyssService.rankerSkillAnnounce((Player) effector, this.getSkillTemplate().getNameId());
		}

		/**
		 * try removing item, if its not possible return to prevent exploits
		 */
		if (effector instanceof Player && skillMethod == SkillMethod.ITEM) {
			Item item = ((Player) effector).getInventory().getItemByObjId(this.itemObjectId);
			if (item == null) {
				return;
			}
			if (item.getActivationCount() > 1) {
				item.setActivationCount(item.getActivationCount() - 1);
			}
			else {
				if (!((Player) effector).getInventory().decreaseByObjectId(item.getObjectId(), 1, ItemUpdateType.DEC_ITEM_USE)) {
					return;
				}
			}
		}
		/**
		 * Create effects and precalculate result
		 */
		int spellStatus = 0;
		int dashStatus = 0;
		int resistCount = 0;
		boolean blockedChain = false;
		boolean blockedStance = false;
		final List<Effect> effects = new ArrayList<Effect>();
		if (skillTemplate.getEffects() != null) {
			boolean blockAOESpread = false;
			for (Creature effected : effectedList) {
				Effect effect = new Effect(this, effected, 0, itemTemplate);
				if (effected instanceof Player) {
					if (effect.getEffectResult() == EffectResult.CONFLICT) {
						blockedStance = true;
					}
				}
				// Force RESIST status if AOE spell spread must be blocked
				if (blockAOESpread) {
					effect.setAttackStatus(AttackStatus.RESIST);
				}
				effect.initialize();
				final int worldId = effector.getWorldId();
				final int instanceId = effector.getInstanceId();
				effect.setWorldPosition(worldId, instanceId, x, y, z);

				effects.add(effect);
				spellStatus = effect.getSpellStatus().getId();
				dashStatus = effect.getDashStatus().getId();

				// Block AOE propagation if firstTarget resists the spell
				if ((!blockAOESpread) && (effect.getAttackStatus() == AttackStatus.RESIST) && (isTargetAOE())) {
					blockAOESpread = true;
				}

				if (effect.getAttackStatus() == AttackStatus.RESIST || effect.getAttackStatus() == AttackStatus.DODGE) {
					resistCount++;
				}
			}

			if (resistCount == effectedList.size()) {
				blockedChain = true;
				blockedPenaltySkill = true;
			}

			// exception for point point skills(example Ice Sheet)
			if (effectedList.isEmpty() && this.isPointPointSkill()) {
				Effect effect = new Effect(this, null, 0, itemTemplate);
				effect.initialize();
				final int worldId = effector.getWorldId();
				final int instanceId = effector.getInstanceId();
				effect.setWorldPosition(worldId, instanceId, x, y, z);
				effects.add(effect);
				spellStatus = effect.getSpellStatus().getId();
			}
		}

		if (effector instanceof Player && skillMethod == SkillMethod.CAST) {
			Player playerEffector = (Player) effector;
			if (playerEffector.getController().isUnderStance()) {
				playerEffector.getController().stopStance();
			}
			if (skillTemplate.isStance() && !blockedStance) {
				playerEffector.getController().startStance(skillTemplate.getSkillId());
			}
		}

		boolean setCooldowns = true;
		if (effector instanceof Player) {
			if (this.isMulticast() && ((Player) effector).getChainSkills().getChainCount((Player) effector, this.getSkillTemplate(), this.chainCategory) != 0) {
				setCooldowns = false;
			}
		}

		// Check Chain Skill Trigger Rate
		if (CustomConfig.SKILL_CHAIN_TRIGGERRATE) {
			int chainProb = skillTemplate.getChainSkillProb();
			if (this.chainCategory != null && !blockedChain) {
				this.chainSuccess = Rnd.get(90) < chainProb;
			}
		}
		else {
			this.chainSuccess = true;
		}

		/**
		 * set variables for chaincondition check
		 */
		if (effector instanceof Player && this.chainSuccess && this.chainCategory != null) {
			((Player) effector).getChainSkills().addChainSkill(this.chainCategory, this.isMulticast());
		}

		/**
		 * Perform necessary actions (use mp,dp items etc)
		 */
		Actions skillActions = skillTemplate.getActions();
		if (skillActions != null) {
			for (Action action : skillActions.getActions()) {
				if (!action.act(this)) {
					return;
				}
			}
		}

		if (effector instanceof Player) {
			QuestEnv env = new QuestEnv(effector.getTarget(), (Player) effector, 0, 0);
			QuestEngine.getInstance().onUseSkill(env, skillTemplate.getSkillId());
		}

		if (setCooldowns) {
			this.setCooldowns();
		}

		if (skillMethod == SkillMethod.CAST && getSkillTemplate().getSubType() != SkillSubType.HEAL && hitTime <= 0 || skillMethod == SkillMethod.CHARGE && getSkillTemplate().getSubType() != SkillSubType.HEAL) {
//			double targetDis = MathUtil.getDistance(effector, firstTarget);
			if (skillskinHitTIme > 0) {
				hitTime += (int)(skillskinHitTIme * effector.getDistanceToTarget() * 1.8F);
			} else {
				this.hitTime = ((int)((int)(getSkillTemplate().getAmmoSpeed() * effector.getDistanceToTarget()) * 1.8F));
			}
		}

		if (hitTime == 0) {
			applyEffect(effects);
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					applyEffect(effects);
				}
			}, hitTime);
		}
		if (skillMethod == SkillMethod.CAST || skillMethod == SkillMethod.ITEM || skillMethod == SkillMethod.CHARGE) {
			sendCastspellEnd(spellStatus, dashStatus, effects);
		}

		endCondCheck();

		if (effector instanceof Npc) {
			SkillAttackManager.afterUseSkill((NpcAI2) ((Npc) effector).getAi2());
		}
	}

	public void applyEffect(List<Effect> effects) {
		/**
		 * Apply effects to effected objects
		 */
		for (Effect effect : effects) {
			effect.applyEffect();
		}

		/**
		 * Use penalty skill (now 100% success)
		 */
		if (!blockedPenaltySkill) {
			startPenaltySkill();
		}
	}

	/**
	 * @param spellStatus
	 * @param effects
	 */
	private void sendCastspellEnd(int spellStatus, int dashStatus, List<Effect> effects) {
		getSkillSkinData();
		if (skillMethod == SkillMethod.CAST || skillMethod == SkillMethod.CHARGE) {
			switch (targetType) {
				case 0: // PlayerObjectId as Target
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL_RESULT(this, effects, serverTime, chainSuccess, spellStatus, dashStatus, skillskinId));
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(firstTarget, effector, SM_ATTACK_STATUS.TYPE.REGULAR, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
					break;

				case 3: // Target not in sight?
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL_RESULT(this, effects, serverTime, chainSuccess, spellStatus, dashStatus, skillskinId));
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(firstTarget, effector, SM_ATTACK_STATUS.TYPE.REGULAR, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
					break;

				case 1: // XYZ as Target
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_CASTSPELL_RESULT(this, effects, serverTime, chainSuccess, spellStatus, dashStatus, targetType, skillskinId));
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ATTACK_STATUS(firstTarget, effector, SM_ATTACK_STATUS.TYPE.REGULAR, 0, 0, SM_ATTACK_STATUS.LOG.ATTACK));
					break;
			}
		}
		else if (skillMethod == SkillMethod.ITEM) {
			PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(), (this.itemObjectId == 0 ? 0 : this.itemObjectId), itemTemplate.getTemplateId(), 0, 1)); // TODO TESTING
			//PacketSendUtility.broadcastPacketAndReceive(effector, new SM_ITEM_USAGE_ANIMATION(effector.getObjectId(), firstTarget.getObjectId(), (this.itemObjectId == 0 ? 0 : this.itemObjectId), itemTemplate.getTemplateId(), 0, 1, 0));
			if (effector instanceof Player) {
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(getItemTemplate().getNameId())));
			}
		}
	}

	/**
	 * Schedule actions/effects of skill (channeled skills)
	 */
	private void schedule(int delay) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isCancelled && skillMethod == SkillMethod.CHARGE) {
					cancelCast();
					effector.setCasting(null);
					PacketSendUtility.broadcastPacketAndReceive(effector, new SM_SKILL_CANCEL(effector, skillTemplate.getSkillId()));
					return;
				}
				endCast();
			}
		}, delay);
	}

	/**
	 * Check all conditions before starting cast
	 */
	protected boolean preCastCheck() {
		Conditions skillConditions = skillTemplate.getStartconditions();
		return skillConditions != null ? skillConditions.validate(this) : true;
	}

	/**
	 * Check all conditions before using skill
	 */
	private boolean preUsageCheck() {
		Conditions skillConditions = skillTemplate.getUseconditions();
		return skillConditions != null ? skillConditions.validate(this) : true;
	}

	/**
	 * Check all conditions after using skill
	 */
	private boolean endCondCheck() {
		Conditions skillConditions = skillTemplate.getEndConditions();
		return skillConditions != null ? skillConditions.validate(this) : true;
	}

	/**
	 * @param value
	 *            is the changeMpConsumptionValue to set
	 */
	public void setBoostSkillCost(int value) {
		boostSkillCost = value;
	}

	/**
	 * @return the changeMpConsumptionValue
	 */
	public int getBoostSkillCost() {
		return boostSkillCost;
	}

	/**
	 * @return the effectedList
	 */
	public List<Creature> getEffectedList() {
		return effectedList;
	}

	/**
	 * @return the effector
	 */
	public Creature getEffector() {
		return effector;
	}

	/**
	 * @return the skillLevel
	 */
	public int getSkillLevel() {
		return skillLevel;
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId() {
		return skillTemplate.getSkillId();
	}

	/**
	 * @return the skillStackLvl
	 */
	public int getSkillStackLvl() {
		return skillStackLvl;
	}

	/**
	 * @return the conditionChangeListener
	 */
	public StartMovingListener getConditionChangeListener() {
		return conditionChangeListener;
	}

	/**
	 * @return the skillTemplate
	 */
	public SkillTemplate getSkillTemplate() {
		return skillTemplate;
	}

	/**
	 * @return the firstTarget
	 */
	public Creature getFirstTarget() {
		return firstTarget;
	}

	/**
	 * @param firstTarget
	 *            the firstTarget to set
	 */
	public void setFirstTarget(Creature firstTarget) {
		this.firstTarget = firstTarget;
	}

	/**
	 * @return true or false
	 */
	public boolean isPassive() {
		return skillTemplate.getActivationAttribute() == ActivationAttribute.PASSIVE;
	}

	/**
	 * @return the firstTargetRangeCheck
	 */
	public boolean isFirstTargetRangeCheck() {
		return firstTargetRangeCheck;
	}

	/**
	 * @param FirstTargetAttribute
	 *            the firstTargetAttribute to set
	 */
	public void setFirstTargetAttribute(FirstTargetAttribute firstTargetAttribute) {
		this.firstTargetAttribute = firstTargetAttribute;
	}

	/**
	 * @return true if the present skill is a non-targeted, non-point AOE skill
	 */
	public boolean checkNonTargetAOE() {
		return (firstTargetAttribute == FirstTargetAttribute.ME && targetRangeAttribute == TargetRangeAttribute.AREA);
	}

	/**
	 * @return true if the present skill is a targeted AOE skill
	 */
	public boolean isTargetAOE() {
		return (firstTargetAttribute == FirstTargetAttribute.TARGET && targetRangeAttribute == TargetRangeAttribute.AREA);
	}

	/**
	 * @return true if the present skill is a self buff includes items (such as scroll buffs)
	 */
	public boolean isSelfBuff() {
		return (firstTargetAttribute == FirstTargetAttribute.ME && targetRangeAttribute == TargetRangeAttribute.ONLYONE && skillTemplate.getSubType() == SkillSubType.BUFF && !skillTemplate.isDeityAvatar());
	}

	/**
	 * @return true if the present skill has self as first target
	 */
	public boolean isFirstTargetSelf() {
		return (firstTargetAttribute == FirstTargetAttribute.ME);
	}

	/**
	 * @return true if the present skill is a Point skill
	 */
	public boolean isPointSkill() {
		return (this.firstTargetAttribute == FirstTargetAttribute.POINT);
	}

	/**
	 * @param firstTargetRangeCheck
	 *            the firstTargetRangeCheck to set
	 */
	public void setFirstTargetRangeCheck(boolean firstTargetRangeCheck) {
		this.firstTargetRangeCheck = firstTargetRangeCheck;
	}

	/**
	 * @param itemTemplate
	 *            the itemTemplate to set
	 */
	public void setItemTemplate(ItemTemplate itemTemplate) {
		this.itemTemplate = itemTemplate;
	}

	public ItemTemplate getItemTemplate() {
		return this.itemTemplate;
	}

	public void setItemObjectId(int id) {
		this.itemObjectId = id;
	}

	public int getItemObjectId() {
		return this.itemObjectId;
	}

	/**
	 * @param targetRangeAttribute
	 *            the targetRangeAttribute to set
	 */
	public void setTargetRangeAttribute(TargetRangeAttribute targetRangeAttribute) {
		this.targetRangeAttribute = targetRangeAttribute;
	}

	/**
	 * @param targetType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTargetType(int targetType, float x, float y, float z) {
		this.targetType = targetType;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculated position after skill
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param h
	 */
	public void setTargetPosition(float x, float y, float z, byte h) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}

	public void setDuration(int t) {
		this.duration = t;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public final byte getH() {
		return h;
	}

	/**
	 * @return Returns the time.
	 */
	public int getHitTime() {
		return hitTime;
	}

	/**
	 * @param time
	 *            The time to set.
	 */
	public void setHitTime(int time) {
		this.hitTime = time;
	}

	/**
	 * @return true if skill must not be affected by boost casting time this comes from old 1.5.0.5 patch notes and still applies on 2.5 (confirmed) TODO: maybe another implementation? At the moment
	 *         this doesnt seem to be handled on client infos, so it's hard coded
	 */
	private boolean isCastTimeFixed() {
		if (skillMethod != SkillMethod.CAST) // only casted skills are affected
		{
			return true;
		}
		switch (this.getSkillId()) {
			case 17: // Sleep: Scarecrow
			case 18: // Sleep: Frightcorn
			case 19: // Fear: Porgus
			case 20: // Fear: Ginseng
			case 243: // Return
			case 245: // Bandage Heal
			case 246: // Herb Treatment I
			case 247: // Herb Treatment II
			case 251: // Herb Treatment III
			case 253: // Herb Treatment IV
			case 297: // Herb Treatment V
			case 308: // Herb Treatment VI
			case 309: // Herb Treatment VII
			case 310: // Herb Treatment VIII
			case 311: // Herb Treatment IX
			case 312: // Herb Treatment X
			case 313: // Herb Treatment XI
			case 314: // Herb Treatment XII
			case 249: // Mana Treatment I
			case 250: // Mana Treatment II
			case 252: // Mana Treatment III
			case 254: // Mana Treatment IV
			case 298: // Mana Treatment V
			case 315: // Mana Treatment VI
			case 316: // Mana Treatment VII
			case 317: // Mana Treatment VIII
			case 318: // Mana Treatment IX
			case 319: // Mana Treatment X
			case 320: // Mana Treatment XI
			case 321: // Mana Treatment XII
			case 302: // Escape
			case 1337: // Sleep
			case 1338: // Tranquilizing Cloud
			case 1339: // Sleeping Storm.
			case 1416: // Curse Of Old Roots
			case 1417: // Curse Of Roots
			case 3589: // Fear Shriek
			case 3775: // Fear
				// ArchDaeva Transformation 5.1 [Elyos]
			case 4752: // Transformation: Avatar Of Fire.
			case 4757: // Transformation: Avatar Of Water.
			case 4762: // Transformation: Avatar Of Earth.
			case 4768: // Transformation: Avatar Of Wind.
				// ArchDaeva Transformation 5.1 [Asmodians]
			case 4804: // Transformation: Avatar Of Fire.
			case 4805: // Transformation: Avatar Of Water.
			case 4806: // Transformation: Avatar Of Earth.
			case 4807: // Transformation: Avatar Of Wind.
				// Fissure Of Oblivion 5.1
			case 4808: // Transformation: Avatar Of Fire.
			case 4813: // Transformation: Avatar Of Water.
			case 4818: // Transformation: Avatar Of Earth.
			case 4824: // Transformation: Avatar Of Wind.
				// Elyos [Guardian General]
			case 11885: // Transformation: Guardian General I
			case 11886: // Transformation: Guardian General II
			case 11887: // Transformation: Guardian General III
			case 11888: // Transformation: Guardian General IV
			case 11889: // Transformation: Guardian General V
				// Asmodians [Guardian General]
			case 11890: // Transformation: Guardian General I
			case 11891: // Transformation: Guardian General II
			case 11892: // Transformation: Guardian General III
			case 11893: // Transformation: Guardian General IV
			case 11894: // Transformation: Guardian General V
				return true;
		}

		return false;
	}

	public boolean isGroundSkill() {
		return skillTemplate.isGroundSkill();
	}

	public boolean shouldAffectTarget(VisibleObject object) {
		// If creature is at least 2 meters above the terrain, ground skill cannot be applied
		if (GeoDataConfig.GEO_ENABLE) {
			if (isGroundSkill()) {
				if ((object.getZ() - GeoService.getInstance().getZ(object) > 1.0f) || (object.getZ() - GeoService.getInstance().getZ(object) < -2.0f)) {
					return false;
				}
			}
			return GeoService.getInstance().canSee(getFirstTarget(), object);
		}
		return true;
	}

	public void setChainCategory(String chainCategory) {
		this.chainCategory = chainCategory;
	}

	public String getChainCategory() {
		return this.chainCategory;
	}

	public SkillMethod getSkillMethod() {
		return this.skillMethod;
	}

	public boolean isPointPointSkill() {
		if (this.getSkillTemplate().getProperties().getFirstTarget() == FirstTargetAttribute.POINT && this.getSkillTemplate().getProperties().getTargetType() == TargetRangeAttribute.POINT) {
			return true;
		}

		return false;
	}

	public boolean isMulticast() {
		return this.isMultiCast;
	}

	public void setIsMultiCast(boolean isMultiCast) {
		this.isMultiCast = isMultiCast;
	}

	public long getCastStartTime() {
		return castStartTime;
	}

	public List<ChargedSkill> getChargeSkillList() {
		return chargeSkillList;
	}
}