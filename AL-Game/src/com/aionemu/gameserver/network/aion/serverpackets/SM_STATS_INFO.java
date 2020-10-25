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
		writeD(player.getObjectId()); // <-- OK
		writeD(GameTimeManager.getGameTime().getTime());// Minutes since 1/1/00 00:00:00
		writeH(pgs.getPower().getCurrent()); // <-- OK
		writeH(pgs.getHealth().getCurrent()); // <-- OK
		writeH(pgs.getAccuracy().getCurrent()); // <-- OK
		writeH(pgs.getAgility().getCurrent()); // <-- OK
		writeH(pgs.getKnowledge().getCurrent()); // <-- OK
		writeH(pgs.getWill().getCurrent()); // <-- OK
		writeH(pgs.getStat(StatEnum.WATER_RESISTANCE, 0).getCurrent());// [current water] <-- TODO
		writeH(pgs.getStat(StatEnum.WIND_RESISTANCE, 0).getCurrent());// [current wind] <-- TODO
		writeH(pgs.getStat(StatEnum.EARTH_RESISTANCE, 0).getCurrent());// [current earth] <-- TODO
		writeH(pgs.getStat(StatEnum.FIRE_RESISTANCE, 0).getCurrent());// [current fire] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_LIGHT, 0).getCurrent());// [current light resistance] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_DARK, 0).getCurrent());// [current dark resistance] <-- TODO
		writeH(player.getLevel()); // <-- OK
		writeB(new byte[6]);
		writeQ(pcd.getExpNeed()); // <-- OK
		writeQ(pcd.getExpRecoverable()); // <-- OK
		writeQ(pcd.getExpShown()); // <-- OK
		writeD(0);// [unk 0]
		writeD(pgs.getMaxHp().getCurrent()); // <-- OK
		writeD(pls.getCurrentHp()); // <-- OK
		writeD(pgs.getMaxMp().getCurrent()); // <-- OK
		writeD(pls.getCurrentMp()); // <-- OK
		writeH(pgs.getMaxDp().getCurrent()); // <-- OK
		writeH(pcd.getDp()); // <-- OK
		writeD(pgs.getFlyTime().getCurrent()); // <-- OK
		writeD(pls.getCurrentFp()); // <-- OK
		writeC(player.getFlyState());// [fly state] TODO (Naked Char changes Val)
		writeH(0); // TODO
		writeC(0); // TODO
		writeD(player.getEquipment().getMainHandWeapon() != null ? 0 : 17);
		writeD(0); // No Off Weapon = 0 Attack When Off weapon equiped also 0
		writeD(0); //2326
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
		
		writeD(0); //110
		writeD(pgs.getMResist().getCurrent()); // <-- OK
		writeF(pgs.getAttackRange().getCurrent() / 1000); // <-- OK
		writeD(pgs.getAttackSpeed().getCurrent()); // <-- OK
		writeD(pgs.getEvasion().getCurrent()); // <-- OK
		writeD(pgs.getParry().getCurrent()); // <-- OK
		writeD(pgs.getBlock().getCurrent()); // <-- OK
		writeH(pgs.getMainHandPCritical().getCurrent()); // <-- OK
		writeH(pgs.getOffHandPCritical().getCurrent()); // <-- OK
		writeD(pgs.getMainHandPAccuracy().getCurrent()); // <-- OK
		writeD(pgs.getOffHandPAccuracy().getCurrent()); // <-- OK
		writeC(1);
		writeC(1);
		writeH(0); // Maybe C C
		writeD(pgs.getMAccuracy().getCurrent()); // <-- OK
		writeH(pgs.getMCritical().getCurrent()); // <-- OK
		writeH(pgs.getMCritical().getCurrent()); // <-- OK
		writeF(pgs.getReverseStat(StatEnum.BOOST_CASTING_TIME, 1000).getCurrent() / 1000f); // <-- OK
		writeH(0); // ??
		writeH(17); // Cleri LVL 66 = 7, Asmo Artist LVL 1 = 14, Gunner LVL 69 = 17
		writeD(pgs.getMainHandPAttack().getCurrent()); // <-- OK
		writeD(pgs.getPDef().getCurrent()); // <-- OK
		writeD(pgs.getMAttack().getCurrent()); // <-- OK
		writeD(pgs.getMResist().getCurrent()); // <-- OK
		writeD(pgs.getPVPAttack().getCurrent());// TODO Zus. PVP-Angr. Checked 7.5
		writeD(pgs.getPVPDefense().getCurrent());// TODO Zus. PVP-Abwehr Checked 7.5
		writeD(pgs.getPVEAttack().getCurrent());// TODO Zus. PVE-Angr. Checked 7.5
		writeD(pgs.getPVEDefense().getCurrent());// TODO Zus. PVE-Abwehr Checked 7.5
		writeH(0);// TODO Phys. krit. Schaden Checked 7.5
		writeH(0);// TODO Mag. krit. Schaden Checked 7.5
		writeD(pgs.getMBoost().getCurrent()); // TODO add Magie Abwehr + Magie Ausgleich (Mag. Angriff)
		writeD(pgs.getMDef().getCurrent());// <-- OK
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getCurrent()); // <-- OK
		writeH(0);//294);// ??
		writeH(0);//40);// ??
		writeH(pgs.getStrikeResist().getCurrent()); // <-- OK
		writeH(pgs.getSpellResist().getCurrent()); // <-- OK
		writeD(player.getInventory().getLimit()); // <-- OK
		writeD(player.getInventory().size()); // <-- OK
		writeQ(0);
		writeD(pcd.getPlayerClass().getClassId()); // <-- OK
		writeH(player.getPlayerSettings().getDisplay()); // <-- OK
		writeH(player.getPlayerSettings().getDeny()); // <-- OK
		writeD(0);
		writeQ(pcd.getCurrentReposteEnergy()); // <-- OK
		writeQ(pcd.getMaxReposteEnergy()); // <-- OK
		writeD(0); // TODO
		writeF(1.0f); // <-- OK
		writeQ(0);
		writeQ(pcd.getGoldenStarEnergy()); // Golden Star Energy
		writeQ(pcd.getGrowthEnergy()); // Energy of Growth
		writeQ(pcd.getSilverStarEnergy()); // SilverStarEnergy
		writeB(new byte[20]);
		writeD(0); //Asmo Artist LVL 1 = 0 else sometimes 1000
		
		//Base Stats
		writeH(pgs.getPower().getBase()); // <-- OK
		writeH(pgs.getHealth().getBase()); // <-- OK
		writeH(pgs.getAgility().getBase()); // <-- OK
		writeH(pgs.getAccuracy().getBase()); // <-- OK
		writeH(pgs.getKnowledge().getBase()); // <-- OK
		writeH(pgs.getWill().getBase()); // <-- OK
		writeH(pgs.getStat(StatEnum.WATER_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.WIND_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.EARTH_RESISTANCE, 0).getBase());// [base earth resist] <-- TODO
		writeH(pgs.getStat(StatEnum.FIRE_RESISTANCE, 0).getBase());// [base water res] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_LIGHT, 0).getBase());// [base light resistance] <-- TODO
		writeH(pgs.getStat(StatEnum.ELEMENTAL_RESISTANCE_DARK, 0).getBase());// [base dark resistance] <-- TODO
		writeD(pgs.getMaxHp().getBase()); // <-- OK
		writeD(pgs.getMaxMp().getBase()); // <-- OK
		writeH(pgs.getMaxDp().getBase()); // <-- OK
		writeH(0);
		writeD(pgs.getFlyTime().getBase()); // <-- OK
		writeD(player.getEquipment().getMainHandWeapon() != null ? 0 : 17);
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
		
		writeD(0); // Unknown
		writeD(0); // Unknown
		writeD(pgs.getMResist().getBase()); // <-- OK
		writeF(pgs.getAttackRange().getBase() / 1000); // <-- OK
		writeD(pgs.getEvasion().getBase()); // <-- OK
		writeD(pgs.getParry().getBase()); // <-- OK
		writeD(pgs.getBlock().getBase()); // <-- OK
		writeH(pgs.getMainHandPCritical().getBase()); // <-- OK
		writeH(pgs.getOffHandPCritical().getBase()); // <-- OK
		writeH(pgs.getMCritical().getBase());
		writeH(pgs.getMCritical().getBase());
		writeD(pgs.getMainHandPAccuracy().getBase()); // <-- OK
		writeD(pgs.getOffHandPAccuracy().getBase()); // <-- OK
		writeC(1);
		writeC(1);
		writeH(0); // Unknown
		writeD(pgs.getMAccuracy().getBase()); // <-- OK
		writeH(0); // UNK
		writeH(0); // UNK
		writeD(pgs.getMainHandPAttack().getBase()); // <-- OK
		writeD(pgs.getPDef().getBase());//Phy Def  <-- OK
		writeD(0); // <-- Changes M-Attack
		writeD(0); // <-- Changes M-DEF
		writeH(0); // TODO Phys. krit. Sc....
		writeH(0); // TODO Mag. krit. Sc....
		writeD(pgs.getMAttack().getBase()); // <-- OK
		writeD(pgs.getMDef().getBase()); // <-- OK
		writeD(pgs.getStat(StatEnum.HEAL_BOOST, 0).getBase()); // <-- OK
		writeH(0); // Unknown
		writeH(0); // Unknown
		writeH(pgs.getStrikeResist().getBase()); // <-- OK
		writeH(pgs.getSpellResist().getBase()); // <-- OK
	}
}

