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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Mrakobes, Loxo
 */
public class Heal extends AdminCommand {

	public Heal() {
		super("heal");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Player player, String... params) {
		VisibleObject target = player.getTarget();
		if (target == null) {
			PacketSendUtility.sendMessage(player, "No target selected");
			return;
		}
		if (!(target instanceof Creature)) {
			PacketSendUtility.sendMessage(player, "Target has to be Creature!");
			return;
		}

		Creature creature = (Creature) target;

		if (params == null || params.length < 1) {
			creature.getLifeStats().increaseHp(TYPE.HP, creature.getLifeStats().getMaxHp() + 1);
			creature.getLifeStats().increaseMp(TYPE.MP, creature.getLifeStats().getMaxMp() + 1);
			creature.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
			PacketSendUtility.sendMessage(player, creature.getName() + " has been refreshed !");
		}
		else if (params[0].equals("dp") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			targetPlayer.getCommonData().setDp(targetPlayer.getGameStats().getMaxDp().getCurrent());
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " is now full of DP !");
		}
		else if (params[0].equals("fp") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			targetPlayer.getLifeStats().setCurrentFp(targetPlayer.getLifeStats().getMaxFp());
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " FP has been fully refreshed !");
		}
		else if (params[0].equals("repose") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			PlayerCommonData pcd = targetPlayer.getCommonData();
			pcd.setCurrentReposteEnergy(pcd.getMaxReposteEnergy());
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " Reposte Energy has been fully refreshed !");
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposteEnergy(), pcd.getMaxReposteEnergy(), pcd.getGoldenStarEnergy(), pcd.getGrowthEnergy()));
		}
		else if (params[0].equals("golden") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			PlayerCommonData pcd = targetPlayer.getCommonData();
			pcd.setGoldenStarEnergy(pcd.getMaxGoldenStarEnergy());
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " Golden Star Energy has been fully refreshed !");
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposteEnergy(), pcd.getMaxReposteEnergy(), pcd.getGoldenStarEnergy(), pcd.getGrowthEnergy()));
		}
		else if (params[0].equals("-golden") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			PlayerCommonData pcd = targetPlayer.getCommonData();
			pcd.setGoldenStarEnergy(0);
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposteEnergy(), pcd.getMaxReposteEnergy(), pcd.getGoldenStarEnergy(), pcd.getGrowthEnergy()));
		}
		else if (params[0].equals("growth") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			PlayerCommonData pcd = targetPlayer.getCommonData();
			pcd.setGrowthEnergy(pcd.getMaxGrowthEnergy());
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " Growth Energy has been fully refreshed !");
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposteEnergy(), pcd.getMaxReposteEnergy(), pcd.getGoldenStarEnergy(), pcd.getGrowthEnergy()));
		}
		else if (params[0].equals("-growth") && creature instanceof Player) {
			Player targetPlayer = (Player) creature;
			PlayerCommonData pcd = targetPlayer.getCommonData();
			pcd.setGrowthEnergy(0);
			PacketSendUtility.sendMessage(player, targetPlayer.getName() + " Growth Energy has been fully refreshed !");
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposteEnergy(), pcd.getMaxReposteEnergy(), pcd.getGoldenStarEnergy(), pcd.getGrowthEnergy()));
		}
		else {
			int hp;
			try {
				String percent = params[0];
				CreatureLifeStats cls = creature.getLifeStats();
				Pattern heal = Pattern.compile("([^%]+)%");
				Matcher result = heal.matcher(percent);
				int value;

				if (result.find()) {
					hp = Integer.parseInt(result.group(1));

					if (hp < 100) {
						value = (int) (hp / 100f * cls.getMaxHp());
					}
					else {
						value = cls.getMaxHp();
					}
				}
				else {
					value = Integer.parseInt(params[0]);
				}
				cls.increaseHp(TYPE.HP, value);
				PacketSendUtility.sendMessage(player, creature.getName() + " has been healed for " + value + " health points!");
			}
			catch (Exception ex) {
				onFail(player, null);
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "//heal : Full HP and MP\n" + "//heal dp : Full DP, must be used on a player !\n" + "//heal fp : Full FP, must be used on a player\n" + "//heal repose : Full repose energy, must be used on a player\n" + "//heal repose : Full Repose Energy, must be used on a player\n" + "//heal golden : Full Golden Star Energy, must be used on a player\n" + "//heal growth : Full Growth Energy, must be used on a player\n";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
