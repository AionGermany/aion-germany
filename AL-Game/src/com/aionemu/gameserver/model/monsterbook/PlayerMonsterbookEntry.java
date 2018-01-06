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
package com.aionemu.gameserver.model.monsterbook;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

public class PlayerMonsterbookEntry extends MonsterbookEntry {

	private PersistentState persistentState;

	public PlayerMonsterbookEntry(int id, int killCount, int level,	int claimReward, PersistentState persistentState) {
		super(id, killCount, level, claimReward);
		this.persistentState = persistentState;
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

    public void setPersistentState(final PersistentState persistentState) {
        switch (persistentState) {
            case DELETED: {
                if (this.persistentState == PersistentState.NEW) {
                    this.persistentState = PersistentState.NOACTION;
                    break;
                }
                this.persistentState = PersistentState.DELETED;
                break;
            }
            case UPDATE_REQUIRED: {
                if (this.persistentState != PersistentState.NEW) {
                    this.persistentState = PersistentState.UPDATE_REQUIRED;
                    break;
                }
                break;
            }
            case NOACTION: {
                break;
            }
            default: {
                this.persistentState = persistentState;
                break;
            }
        }
    }
}
