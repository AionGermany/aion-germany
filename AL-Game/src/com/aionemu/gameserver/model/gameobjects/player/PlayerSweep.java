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
package com.aionemu.gameserver.model.gameobjects.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerShugoSweepDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * Created by Ghostfur
 */
public class PlayerSweep
{
    Logger log = LoggerFactory.getLogger(PlayerSweep.class);
    private PersistentState persistentState;
	
    private int step;
    private int freeDice;
    private int boardId;
	
    public PlayerSweep(int step, int freeDice, int boardId) {
        this.step = step;
        this.freeDice = freeDice;
        this.boardId = boardId;
        this.persistentState = PersistentState.NEW;
    }
	
    public PlayerSweep() {
    }
	
    public int getFreeDice() {
        return freeDice;
    }
	
    public void setFreeDice(int dice) {
        this.freeDice = dice;
    }
	
    public int getStep() {
        return step;
    }
	
    public void setStep(int step) {
        this.step = step;
    }
	
    public int getBoardId() {
        return boardId;
    }
	
    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
	
    public PersistentState getPersistentState() {
        return persistentState;
    }
	
    public void setShugoSweepByObjId(int playerId) {
        DAOManager.getDAO(PlayerShugoSweepDAO.class).setShugoSweepByObjId(playerId, getFreeDice(), getStep(), getBoardId());
    }
	
    public void setPersistentState(PersistentState persistentState) {
        switch (persistentState) {
            case UPDATE_REQUIRED:
                if (this.persistentState == PersistentState.NEW)
				break;
            default:
                this.persistentState = persistentState;
        }
    }
}