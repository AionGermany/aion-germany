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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.stats.container.PlayerGameStats;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;

/**
 * In this packet Server is sending User Info?
 *
 * @author -Nemesiss-
 * @author Luno
 * @author ginho1
 */
public class SM_STATS_INFO extends AionServerPacket {

	/**
	 * Player that stats info will be send
	 */
	private Player player;
	private PlayerGameStats pgs;
	private PlayerLifeStats pls;
	private PlayerCommonData pcd;

	/**
	 * Constructs new <tt>SM_UI</tt> packet
	 *
	 * @param player
	 */
	public SM_STATS_INFO(Player player) {
		this.player = player;
		this.pcd = player.getCommonData();
		this.pgs = player.getGameStats();
		this.pls = player.getLifeStats();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());// Player ObjectId
		writeD(GameTimeManager.getGameTime().getTime());// Minutes since 1/1/00 00:00:00
		writeH(pgs.getPower().getCurrent());// [current power]
		writeH(pgs.getHealth().getCurrent());// [current health]
		writeH(pgs.getAccuracy().getCurrent());// [current accuracy]
		writeH(pgs.getAgility().getCurrent());// [current agility]
		writeH(pgs.getKnowledge().getCurrent());// [current knowledge]
		writeH(pgs.getWill().getCurrent());// [current will]
		writeH(pgs.getStat(StatEnum.WATER_RESISTANCE, 0).getCurrent());// [current water] <-- TODO
		writeH(pgs.getStat(StatEnum.WIND_RESISTANCE, 0).getCurrent());// [current wind] <-- TODO
		writeH(pgs.getStat(StatEnum.EARTH_RESISTANCE, 0).getCurrent());// [current earth] <-- TODO
		writeH(pgs.getStat(StatEnum.FIRE_RESISTANCE, 0).getCurrent());// [current fire] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_LIGHT, 0).getCurrent());// [current light resistance] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_DARK, 0).getCurrent());// [current dark resistance] <-- TODO
		writeH(player.getLevel());// [level]
		writeH(0);// [unk 16386]
		writeH(1);// 1
		writeH(0);// [unk]
		writeQ(pcd.getExpNeed());// [xp till next lv] <-- OK
		writeQ(pcd.getExpRecoverable());// [recoverable exp] <-- OK
		writeQ(pcd.getExpShown());// [current xp] <-- OK
		writeD(0);// [unk 0]
		writeD(pgs.getMaxHp().getCurrent());// [max hp] <-- OK
		writeD(pls.getCurrentHp());// [current hp] <-- OK
		writeD(pgs.getMaxMp().getCurrent());// [max mana] <-- OK
		writeD(pls.getCurrentMp());// [current mana] <-- OK
		writeH(pgs.getMaxDp().getCurrent());// [max dp] <-- OK
		writeH(pcd.getDp());// [current dp] <-- OK
		writeD(pgs.getFlyTime().getCurrent());// [max fly time] <-- OK
		writeD(pls.getCurrentFp());// [current fly time] <-- OK
		writeH(player.getFlyState());// [fly state] TODO (Naked Char changes Val)
		writeH(0); // TODO
		
		if (player.getEquipment().getMainHandWeapon() != null) {
			writeD(0);
		} else {
			writeD(19);
		}
		writeD(0); // No Off Weapon = 0 Attack When Off weapon equiped also 0
		writeD(0);
		if (player.getEquipment().getMainHandWeapon() != null) {
			int mainHandminDMG = player.getEquipment().getMainHandWeapon().getItemTemplate().getWeaponStats().getMinDamage();
			int mainHandmaxDMG = player.getEquipment().getMainHandWeapon().getItemTemplate().getWeaponStats().getMaxDamage();
			int mainHandfinalDamage = mainHandminDMG + mainHandmaxDMG;
			writeD(mainHandfinalDamage / 2); // TODO add Mboost
		} else {
			writeD(0);
		}
		if (player.getEquipment().getOffHandWeapon() != null) {
			int offHandminDMG = player.getEquipment().getOffHandWeapon().getItemTemplate().getWeaponStats().getMinDamage();
			int offHandmaxDMG = player.getEquipment().getOffHandWeapon().getItemTemplate().getWeaponStats().getMaxDamage();
			int offHandfinalDamage = offHandminDMG + offHandmaxDMG;
			writeD(offHandfinalDamage / 2); // TODO add Mboost
		} else {
			writeD(0);
		}
		writeD(0);	
		writeD(pgs.getMResist().getCurrent());// [current mres] Checked 7.2
		writeF(pgs.getAttackRange().getCurrent() / 1000);// attack range Checked 7.2
		writeD(pgs.getAttackSpeed().getCurrent());// attack speed TODO
		writeD(pgs.getEvasion().getCurrent());// [current evasion] Checked 7.2
		writeD(pgs.getParry().getCurrent());// [current parry] Checked 7.2
		writeD(pgs.getBlock().getCurrent());// [current block] Checked 7.2
		
		writeH(pgs.getMainHandPCritical().getCurrent());// [current main hand crit rate] Checked 7.2
		writeH(pgs.getOffHandPCritical().getCurrent());// [current off hand crit rate] ???
		
		writeD(pgs.getMainHandPAccuracy().getCurrent());// [current main_hand_accuracy]
		writeD(pgs.getOffHandPAccuracy().getCurrent());// [current off_hand_accuracy] ??
		
		writeH(257); // <-- TOOD
		writeH(0); // <-- TOOD
		
		writeD(pgs.getMAccuracy().getCurrent()); //Checked 7.2
		writeH(pgs.getMCritical().getCurrent()); //Checked 7.2
		writeH(pgs.getMCritical().getCurrent()); //Checked 7.2
		writeF(pgs.getReverseStat(StatEnum.BOOST_CASTING_TIME, 1000).getCurrent() / 1000f);// [current casting speed] Checked 7.2
		writeH(0); // ??
		writeH(17); // ??
		writeD(pgs.getMainHandPAttack().getCurrent());//Phy Attack realy MainHandpAttack ?
		writeD(pgs.getPDef().getCurrent());//Phy Def Checked 7.2
		writeD(pgs.getMAttack().getCurrent());//Mag Attack Checked 7.2
		writeD(pgs.getMDef().getCurrent());//Mag Def Checked 7.2
		writeD(pgs.getPVPAttack().getCurrent());// TODO Zus. PVP-Angr. Checked 7.2
		writeD(pgs.getPVPDefense().getCurrent());// TODO Zus. PVP-Abwehr Checked 7.2
		writeD(pgs.getPVEAttack().getCurrent());// TODO Zus. PVE-Angr. Checked 7.2
		writeD(pgs.getPVEDefense().getCurrent());// TODO Zus. PVE-Abwehr Checked 7.2
		writeD(0);// TODO Phys. krit. Schaden Checked 7.2
		writeD(pgs.getMBoost().getCurrent()); // TODO add Magie Abwehr + Magie Ausgleich (Mag. Angriff)
		writeD(pgs.getMDef().getCurrent()); // Checked 7.2
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getCurrent());// Checked 7.2
		writeD(90);// ??
		writeD(player.getInventory().getLimit());
		writeD(player.getInventory().size());
		writeD(61); // TODO
		writeQ(0);
		writeD(pcd.getPlayerClass().getClassId());// [Player Class id] Checked 7.2
		writeH(player.getPlayerSettings().getDisplay()); //<-- OK
		writeH(player.getPlayerSettings().getDeny()); //<-- OK
		writeQ(pcd.getCurrentReposteEnergy()); // <-- OK
		writeQ(pcd.getMaxReposteEnergy()); // <-- OK
		writeQ(0); // TODO
		writeF(1.0f);// Default Base Casting Speed
		writeD(0);
		writeD(0);
		writeQ(pcd.getGrowthEnergy());// Energy of Growth (160000000 = 100%)
		writeQ(pcd.getGoldenStarEnergy());// Golden Star Energy (625000000 = 100%)
		writeQ(pcd.getSilverStarEnergy()); //SilverStarEnergy TODO (50000 = 5%, 100000 = 10% etc ) <-- OK
		writeB(new byte[16]);
		writeH(pgs.getPower().getBase());// [base power] <-- ok
		writeH(pgs.getHealth().getBase());// [base health] <-- ok
		writeH(pgs.getAgility().getBase());// [base agility] <-- ok
		writeH(pgs.getAccuracy().getBase());// [base accuracy] <-- ok
		writeH(pgs.getKnowledge().getBase());// [base knowledge] <-- ok
		writeH(pgs.getWill().getBase());// [base will] <-- ok
		writeH(pgs.getStat(StatEnum.WATER_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.WIND_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.EARTH_RESISTANCE, 0).getBase());// [base earth resist] <-- TODO
		writeH(pgs.getStat(StatEnum.FIRE_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_LIGHT, 0).getBase());// [base light resistance] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_DARK, 0).getBase());// [base dark resistance] <-- TODO
		writeD(pgs.getMaxHp().getBase());// [base hp] <-- ok
		writeD(pgs.getMaxMp().getBase());// [base mana] <-- ok
		writeH(pgs.getMaxDp().getBase());// [base dp] <-- ok
		writeH(0);
		writeD(pgs.getFlyTime().getBase());// [fly time] <-- ok
		writeD(0);
		writeD(0);
		
		if (player.getEquipment().getMainHandWeapon() != null) {
			int mainHandminDMG = player.getEquipment().getMainHandWeapon().getItemTemplate().getWeaponStats().getMinDamage();
			int mainHandmaxDMG = player.getEquipment().getMainHandWeapon().getItemTemplate().getWeaponStats().getMaxDamage();
			int mainHandfinalDamage = mainHandminDMG + mainHandmaxDMG;
			writeD(mainHandfinalDamage / 2); // TODO add Mboost
		} else {
			writeD(0);
		}
		if (player.getEquipment().getOffHandWeapon() != null) {
			int offHandminDMG = player.getEquipment().getOffHandWeapon().getItemTemplate().getWeaponStats().getMinDamage();
			int offHandmaxDMG = player.getEquipment().getOffHandWeapon().getItemTemplate().getWeaponStats().getMaxDamage();
			int offHandfinalDamage = offHandminDMG + offHandmaxDMG;
			writeD(offHandfinalDamage / 2); // TODO add Mboost
		} else {
			writeD(0);
		}
		
		writeD(0);//pgs.getPDef().getBase()); ???
		writeD(0);//pgs.getMDef().getBase()); ???
		writeD(pgs.getMResist().getBase()); // <-- OK
		writeF(pgs.getAttackRange().getBase() / 1000);// [base attack range] <-- OK
		writeD(pgs.getEvasion().getBase()); // <-- OK
		writeD(pgs.getParry().getBase()); // <-- OK
		writeD(pgs.getBlock().getBase()); // <-- OK
		writeH(pgs.getMainHandPCritical().getBase());// [current main hand crit rate] <-- OK
		writeH(pgs.getOffHandPCritical().getBase());// [current off hand crit rate] UNK
		writeH(pgs.getMCritical().getBase()); // Magic Crit main <-- OK
		writeH(pgs.getMCritical().getBase());
		writeD(pgs.getMainHandPAccuracy().getBase());// [current main_hand_accuracy] <-- OK
		writeD(pgs.getOffHandPAccuracy().getBase());
		writeD(257);// UNK
		writeD(pgs.getMAccuracy().getBase()); // <-- OK
		writeD(17); // UNK
		writeD(pgs.getMainHandPAttack().getBase()); // <-- OK
		writeD(pgs.getPDef().getBase());//Phy Def  <-- OK
		writeD(0);
		writeD(0);
		writeD(0);// TODO Phys. krit. Schaden <-- OK
		writeD(pgs.getMAttack().getBase()); // UNK
		writeD(0);
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getBase()); // <-- OK
		writeD(90);
		writeD(0);
	}
}

