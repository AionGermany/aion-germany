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
package com.aionemu.gameserver.services.player;

import java.util.Iterator;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.dao.PlayerMonsterbookDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.monsterbook.MonsterbookTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MONSTERBOOK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MONSTERBOOK_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 */
public class MonsterbookService {

	private MonsterbookService() {
		GameServer.log.info("[MonsterbookService] loaded ...");
	}	
	
	public void onLogin(Player player) {
		PacketSendUtility.sendPacket(player, new SM_MONSTERBOOK_LIST(player));
	}

    public void onKill(final Player player, int npcId) { // Fixed pls do not change
    	MonsterbookTemplate monsterbookTemplateByNpcId = DataManager.MONSTERBOOK_DATA.getMonsterbookTemplateByNpcId(npcId);
        
        if (monsterbookTemplateByNpcId == null || monsterbookTemplateByNpcId.getNpcIds() == null) {
            return;
        }
        
        int killCountById = DAOManager.getDAO(PlayerMonsterbookDAO.class).getKillCountById(player.getObjectId(), monsterbookTemplateByNpcId.getId());
        byte level = (byte) DAOManager.getDAO(PlayerMonsterbookDAO.class).getLevelById(player.getObjectId(), monsterbookTemplateByNpcId.getId());  
        int rewardId = DAOManager.getDAO(PlayerMonsterbookDAO.class).getClaimRewardById(player.getObjectId(), monsterbookTemplateByNpcId.getId());
        
        if (level != rewardId) { //get reward first
        	return;
        }
        
        Iterator<Integer> iterator = monsterbookTemplateByNpcId.getNpcIds().iterator();
        while (iterator.hasNext()) {
            if (npcId == iterator.next()) {
                ++killCountById;
                for (MonsterbookTemplate.MonsterbookAchievementTemplate monsterbookAchievementTemplate : monsterbookTemplateByNpcId.getMonsterbookAchievementTemplate()) {
                    if (monsterbookAchievementTemplate == null) {
                        return;
                    }
                    if (killCountById != monsterbookAchievementTemplate.getKillCondition()) {
                        continue;
                    } else {
                    	level+= 1;
                    }
                }
                //Should be for ++killCountById
//                if (killCountById == 0) { //TODO Need Template nameId
//                	PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404072, new DescriptionId(monsterbookTemplateByNpcId.getNameId() * 2 + 1)));
//                }
                PacketSendUtility.sendPacket(player, new SM_MONSTERBOOK(monsterbookTemplateByNpcId.getId(), killCountById, level, 0));
                player.getMonsterbook().add(player, monsterbookTemplateByNpcId.getId(), killCountById, level, 0);
            }
        }
    }

	public void onLvlUp(Player player, int npcId) {
        MonsterbookTemplate monsterbookTemplate = DataManager.MONSTERBOOK_DATA.getMonsterbookTemplate(npcId);
        if (monsterbookTemplate == null) {
            return;
        }
        int killCountById = DAOManager.getDAO(PlayerMonsterbookDAO.class).getKillCountById(player.getObjectId(), npcId);
        byte level = (byte)DAOManager.getDAO(PlayerMonsterbookDAO.class).getLevelById(player.getObjectId(), npcId);
        long EXP = 0;        
        for (final MonsterbookTemplate.MonsterbookAchievementTemplate monsterbookAchievementTemplate : monsterbookTemplate.getMonsterbookAchievementTemplate()) {
            if (killCountById == monsterbookAchievementTemplate.getKillCondition()) {
            	EXP = monsterbookAchievementTemplate.getRewardExp();
            }
        }
        player.getCommonData().addExp(EXP, RewardType.MONSTER_BOOK);
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(EXP));
        PacketSendUtility.sendPacket(player, new SM_MONSTERBOOK(npcId, killCountById, level, level));
        player.getMonsterbook().add(player, npcId, killCountById, level, level);
	}

	public static MonsterbookService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final MonsterbookService INSTANCE = new MonsterbookService();
	}
}
