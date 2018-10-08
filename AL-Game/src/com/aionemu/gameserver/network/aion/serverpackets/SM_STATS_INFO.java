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
		writeH(16386);// [unk 16386]
		writeH(1);// 1
		writeH(0);// [unk]
		writeQ(pcd.getExpNeed());// [xp till next lv] <-- OK
		writeQ(pcd.getExpRecoverable());// [recoverable exp] <-- OK
		writeQ(pcd.getExpShown());// [current xp] <-- OK
		writeD(0);// [unk 0]
		writeD(pgs.getMaxHp().getCurrent());// [max hp] ok
		writeD(pls.getCurrentHp());// [current hp] ok
		writeD(pgs.getMaxMp().getCurrent());// [max mana] ok
		writeD(pls.getCurrentMp());// [current mana] ok
		writeH(pgs.getMaxDp().getCurrent());// [max dp] ok
		writeH(pcd.getDp());// [current dp]
		writeD(pgs.getFlyTime().getCurrent());// [max fly time] <-- OK
		writeD(pls.getCurrentFp());// [current fly time] <-- OK
		writeH(player.getFlyState());// [fly state] <-- OK
		writeH(0); // TODO
		writeD(0);//pgs.getMainHandPAttack().getCurrent());// // TODO base Attack without weapon(right Hand);
		writeD(0); // No Off Weapon = 0 Attack When Off weapon equiped also 0
		writeD(0);
		writeD(pgs.getMainHandPAttack().getCurrent());
		writeD(0);
		writeD(0);	
//		if (player.getEquipment().getMainHandWeapon() != null && (player.getEquipment().getMainHandWeaponType() == WeaponType.BOOK_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.ORB_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.GUN_1H || player.getEquipment().getMainHandWeaponType() == WeaponType.CANNON_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.HARP_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.KEYBLADE_2H)) {
//			writeD(pgs.getMAttack().getCurrent());
//		}
//		else {
//			writeD(0);//pgs.getMainHandPAttack().getCurrent());
//		}
//		if (player.getEquipment().getOffHandWeaponType() != null && (player.getEquipment().getOffHandWeaponType() == WeaponType.BOOK_2H || player.getEquipment().getOffHandWeaponType() == WeaponType.ORB_2H || player.getEquipment().getOffHandWeaponType() == WeaponType.GUN_1H || player.getEquipment().getOffHandWeaponType() == WeaponType.CANNON_2H || player.getEquipment().getOffHandWeaponType() == WeaponType.HARP_2H || player.getEquipment().getOffHandWeaponType() == WeaponType.KEYBLADE_2H)) {
//			writeD(0);//pgs.getOffHandMAttack().getCurrent());
//		}
//		else {
//			writeD(0);//pgs.getOffHandPAttack().getCurrent());
//		}
		
		writeD(pgs.getMResist().getCurrent());// [current mres]
		writeF(pgs.getAttackRange().getCurrent() / 1000);// attack range
		writeH(pgs.getAttackSpeed().getCurrent());// attack speed
		writeH(97);//unk 97
		writeD(pgs.getEvasion().getCurrent());// [current evasion]
		writeD(pgs.getParry().getCurrent());// [current parry]
		writeD(pgs.getBlock().getCurrent());// [current block]
		
		writeH(pgs.getMainHandPCritical().getCurrent());// [current main hand crit rate]
		writeH(pgs.getOffHandPCritical().getCurrent());// [current off hand crit rate]
		writeD(pgs.getMainHandPAccuracy().getCurrent());// [current main_hand_accuracy]
		writeD(pgs.getOffHandPAccuracy().getCurrent());// [current off_hand_accuracy]
		
		writeH(3);
		writeH(84);
		writeD(pgs.getMAccuracy().getCurrent());
		writeH(pgs.getMCritical().getCurrent());
		writeH(0);//TODO
		writeF(pgs.getReverseStat(StatEnum.BOOST_CASTING_TIME, 1000).getCurrent() / 1000f);// [current casting speed] <-- OK
		writeH(0);
		writeH(7); //Cleri
		writeD(pgs.getMainHandPAttack().getCurrent());//Phy Attack realy MainHandpAttack ?
		writeD(pgs.getPDef().getCurrent());//Phy Def
		writeD(pgs.getMAttack().getCurrent());//Mag Attack
		writeD(pgs.getMDef().getCurrent());// Cleri
		writeB(new byte[16]);
		writeD(pgs.getMBoost().getCurrent());
		writeD(0); // TODO add Magie abwehr + Magie Ausgleich
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getCurrent());
		writeH(96); // Cleri
		writeH(130);// Cleri
		writeH(0);// Cleri
		writeH(0);// Cleri
		
		writeD(player.getInventory().getLimit());
		writeD(player.getInventory().size());
		writeQ(0);
		writeD(pcd.getPlayerClass().getClassId());// [Player Class id] <-- OK
		writeH(player.getPlayerSettings().getDisplay()); //<-- OK
		writeH(player.getPlayerSettings().getDeny()); //<-- OK
		writeQ(pcd.getCurrentReposteEnergy()); // <-- OK
		writeQ(pcd.getMaxReposteEnergy()); // <-- OK
		writeH(0);
		writeH(16385);// Cleri  
		writeF(1.0f);// Default Base Casting Speed
		writeD(8960);
		writeH(0);
		writeH(0);
		writeQ(0);//pcd.getGoldenStarEnergy()); // Golden Star Energy (625000000 = 100%)
		writeQ(0);//pcd.getGrowthEnergy()); // Energy of Growth (160000000 = 100%)
		writeQ(0); //SilverStarEnergy TODO (50000 = 5%, 100000 = 10% etc ) <-- OK
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
		writeD(pgs.getMaxDp().getBase());// [base dp] <-- ok
		writeD(pgs.getFlyTime().getBase());// [fly time] <-- ok

		writeD(0);// TODO base Attack without weapon(right Hand);
		writeD(0);// No Off Weapon = 0 Attack When Off weapon equiped also 0
		writeD(pgs.getMainHandPAttack().getBase());
		writeD(0);
		writeD(0);//pgs.getPDef().getBase());
		writeD(0);//pgs.getMDef().getBase());
//		if (player.getEquipment().getMainHandWeapon() != null && (player.getEquipment().getMainHandWeaponType() == WeaponType.BOOK_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.ORB_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.GUN_1H || player.getEquipment().getMainHandWeaponType() == WeaponType.CANNON_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.HARP_2H || player.getEquipment().getMainHandWeaponType() == WeaponType.KEYBLADE_2H)) {
//			writeD(pgs.getMainHandMAttack().getBase()); // TODO Right Hand
//			writeD(0); // TODO Left Hand
//		}
//		else {
//			writeD(pgs.getMainHandPAttack().getBase()); // TODO Right Hand
//			writeD(0);// TODO Left Hand
//		}
		
		writeD(pgs.getMResist().getBase());
		writeF(pgs.getAttackRange().getBase() / 1000);// [base attack range] <-- OK
		writeD(pgs.getEvasion().getBase());
		writeD(pgs.getParry().getBase());
		writeD(pgs.getBlock().getBase());
		
		writeH(pgs.getMainHandPCritical().getBase());// [current main hand crit rate]
		writeH(pgs.getOffHandPCritical().getBase());// [current off hand crit rate]
		writeH(pgs.getMCritical().getBase()); // Magic Crit main
		writeH(0);// Magic Crit off
		writeD(pgs.getMainHandPAccuracy().getBase());// [current main_hand_accuracy]
		writeD(pgs.getOffHandPAccuracy().getBase());// [current off_hand_accuracy]
		
		writeH(3); //unk value 1 = no Weapon or maybe weaponSlot ?
		writeH(0);
		writeD(pgs.getMAccuracy().getBase());
		writeD(7); // Unk Cleri
		writeD(pgs.getMainHandPAttack().getBase());//Phy Attack realy MainHandpAttack ?
		writeD(pgs.getPDef().getBase());//Phy Def
		writeD(pgs.getMAttack().getBase());//Mag Attack
		writeD(pgs.getMDef().getBase());//
		writeD(pgs.getMBoost().getBase()); 
		writeD(pgs.getMDef().getBase()); // TODO Magic Abwehr (Mdef)+ Ausgleich
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getBase());
		writeH(96); //unk Cleri
		writeH(50); //unk Cleri
		writeH(0); //unk Cleri
		writeH(0); //unk Cleri
	}
}

