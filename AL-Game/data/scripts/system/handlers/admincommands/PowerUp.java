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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Tago
 */
public class PowerUp extends AdminCommand {

	public PowerUp() {
		super("powerup");
	}

	@Override
	public void execute(Player admin, String... params) {
		// TODO rewrite
		// int index = 2;
		// int i = 0;
		// Player player = null;
		// if (params.length != 0) {
		// if ("help".startsWith(params[i])) {
		// PacketSendUtility.sendMessage(admin, "0 to return to normal state");
		// PacketSendUtility.sendMessage(admin, "//powerup <Multiplier = 2>");
		// PacketSendUtility.sendMessage(admin, "Syntax: //powerup [playerName] [Multiplier = 2]\n"
		// + " This command multiplies your actual power to the number given.\n"
		// + " Using 0 as the Multiplier resets the power to normal.\n"
		// + " Notice: You can ommit parameters between [], especially playerName.\n"
		// + " Target: Named player, then targeted player, only then self.\n" + " Default Value: Multiplier is 2.");
		// return;
		// }
		// player = World.getInstance().findPlayer(Util.convertName(params[i]));
		// if (player == null) {
		// VisibleObject target = admin.getTarget();
		// if (target instanceof Player)
		// player = (Player) target;
		// else
		// player = admin;
		// }
		// else
		// i++;
		// try {
		// index = Integer.parseInt(params[i]);
		// }
		// catch (NumberFormatException ex) {
		// PacketSendUtility.sendMessage(admin, "Wrong input use //powerup help");
		// return;
		// }
		// catch (Exception ex2) {
		// PacketSendUtility.sendMessage(admin, "Occurs an error.");
		// return;
		// }
		// }
		// if (index == 0) {
		// player.getGameStats().recomputeStats();
		// player.getLifeStats().increaseHp(TYPE.HP, admin.getLifeStats().getMaxHp() + 1);
		// player.getLifeStats().increaseMp(TYPE.MP, admin.getLifeStats().getMaxMp() + 1);
		// PacketSendUtility.sendPacket(player, new SM_STATS_INFO(admin));
		// if (player == admin)
		// PacketSendUtility.sendMessage(player, "You are now normal again.");
		// else {
		// PacketSendUtility.sendMessage(admin, "Player " + player.getName() + " is now normal again.");
		// PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " made you normal again.");
		// }
		// return;
		// }
		// player.getGameStats().setStat(StatEnum.MAXMP, admin.getLifeStats().getMaxHp() * index);
		// player.getGameStats().setStat(StatEnum.MAXHP, admin.getLifeStats().getMaxMp() * index);
		//
		// player.getGameStats().setStat(StatEnum.BLOCK, admin.getGameStats().getStatBonus(StatEnum.BLOCK) * index);
		// player.getGameStats().setStat(StatEnum.EVASION, admin.getGameStats().getStatBonus(StatEnum.EVASION) * index);
		// player.getGameStats().setStat(StatEnum.HEALTH, admin.getGameStats().getStatBonus(StatEnum.HEALTH) * index);
		// player.getGameStats().setStat(StatEnum.ACCURACY, admin.getGameStats().getStatBonus(StatEnum.ACCURACY) * index);
		// player.getGameStats().setStat(StatEnum.PARRY, admin.getGameStats().getStatBonus(StatEnum.PARRY) * index);
		//
		// player.getGameStats().setStat(StatEnum.MAIN_HAND_ACCURACY,
		// admin.getGameStats().getStatBonus(StatEnum.MAIN_HAND_ACCURACY) * index);
		// player.getGameStats().setStat(StatEnum.MAIN_HAND_CRITICAL,
		// admin.getGameStats().getStatBonus(StatEnum.MAIN_HAND_CRITICAL) * index);
		// player.getGameStats().setStat(StatEnum.MAIN_HAND_POWER,
		// admin.getGameStats().getStatBonus(StatEnum.MAIN_HAND_POWER) * index);
		// player.getGameStats().setStat(StatEnum.MAIN_HAND_ATTACK_SPEED,
		// admin.getGameStats().getStatBonus(StatEnum.MAIN_HAND_POWER) * index);
		// player.getGameStats().setStat(StatEnum.OFF_HAND_ACCURACY,
		// admin.getGameStats().getStatBonus(StatEnum.OFF_HAND_ACCURACY) * index);
		// player.getGameStats().setStat(StatEnum.OFF_HAND_CRITICAL,
		// admin.getGameStats().getStatBonus(StatEnum.OFF_HAND_CRITICAL) * index);
		// player.getGameStats().setStat(StatEnum.OFF_HAND_POWER,
		// admin.getGameStats().getStatBonus(StatEnum.OFF_HAND_POWER) * index);
		// player.getGameStats().setStat(StatEnum.OFF_HAND_ATTACK_SPEED,
		// admin.getGameStats().getStatBonus(StatEnum.OFF_HAND_ATTACK_SPEED) * index);
		//
		// player.getGameStats().setStat(StatEnum.MAGICAL_ATTACK,
		// admin.getGameStats().getStatBonus(StatEnum.MAGICAL_ATTACK) * index);
		// player.getGameStats().setStat(StatEnum.MAGICAL_ACCURACY,
		// admin.getGameStats().getStatBonus(StatEnum.MAGICAL_ACCURACY) * index);
		// player.getGameStats().setStat(StatEnum.MAGICAL_CRITICAL,
		// admin.getGameStats().getStatBonus(StatEnum.MAGICAL_CRITICAL) * index);
		// player.getGameStats().setStat(StatEnum.MAGICAL_RESIST,
		// admin.getGameStats().getStatBonus(StatEnum.MAGICAL_RESIST) * index);
		// player.getGameStats().setStat(StatEnum.BOOST_MAGICAL_SKILL,
		// admin.getGameStats().getStatBonus(StatEnum.BOOST_MAGICAL_SKILL) * index * 15);
		//
		// player.getGameStats().setStat(StatEnum.REGEN_MP, admin.getGameStats().getStatBonus(StatEnum.REGEN_MP) * index);
		// player.getGameStats().setStat(StatEnum.REGEN_HP, admin.getGameStats().getStatBonus(StatEnum.REGEN_HP) * index);
		//
		// player.getLifeStats().increaseHp(TYPE.HP, admin.getLifeStats().getMaxHp() + 1);
		// player.getLifeStats().increaseMp(TYPE.MP, admin.getLifeStats().getMaxMp() + 1);
		// PacketSendUtility.sendPacket(player, new SM_STATS_INFO(admin));
		// if (player == admin)
		// PacketSendUtility.sendMessage(player, "You are now " + index + " times more powerfull than before.");
		// else {
		// PacketSendUtility.sendMessage(admin, "Player " + player.getName() + " is now " + index +
		// " times more powerfull than before.");
		// PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " made you " + index +
		// " times more powerfull than before.");
		// }
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
