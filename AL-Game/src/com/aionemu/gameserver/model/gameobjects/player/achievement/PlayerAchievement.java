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
package com.aionemu.gameserver.model.gameobjects.player.achievement;

import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementAction;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementState;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import java.sql.Timestamp;
import java.util.Map;
import javolution.util.FastMap;

public class PlayerAchievement {

    private int objectId;
    private int id;
    private AchievementType type;
    private AchievementState state;
    private int step;
    private Timestamp startDate;
    private Timestamp endateDate;
    private Map<Integer, AchievementAction> actionMap = new FastMap<Integer, AchievementAction>();

    public PlayerAchievement(int id, AchievementType type, AchievementState state, int step, Timestamp startDate, Timestamp endateDate) {
        this.objectId = IDFactory.getInstance().nextId();
        this.id = id;
        this.type = type;
        this.state = state;
        this.step = step;
        this.startDate = startDate;
        this.endateDate = endateDate;
    }

    public PlayerAchievement(int objectId) {
        this.objectId = objectId;
    }

    public Map<Integer, AchievementAction> getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map<Integer, AchievementAction> actionMap) {
        this.actionMap = actionMap;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AchievementType getType() {
        return this.type;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndateDate(Timestamp endateDate) {
        this.endateDate = endateDate;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public Timestamp getEndateDate() {
        return this.endateDate;
    }

    public AchievementState getState() {
        return this.state;
    }

    public void setState(AchievementState state) {
        this.state = state;
    }
}

