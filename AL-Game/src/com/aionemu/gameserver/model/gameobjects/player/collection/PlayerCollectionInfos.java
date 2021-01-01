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
package com.aionemu.gameserver.model.gameobjects.player.collection;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.collection.CollectionExpTemplate;
import com.aionemu.gameserver.model.templates.collection.CollectionType;

public class PlayerCollectionInfos implements StatOwner {

    private CollectionType type;
    private int level;
    private int exp;
    private List<IStatFunction> functions = new ArrayList<IStatFunction>();

    public PlayerCollectionInfos(CollectionType type, int level, int exp) {
        this.type = type;
        this.level = level;
        this.exp = exp;
    }

    public CollectionType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void apply(Player player) {
        if (type != CollectionType.EVENT && level != 1) {
            CollectionExpTemplate template = DataManager.COLLECTION_EXP_DATA.getTemplate(level, type);
            if (template.getModifiers() != null) {
                for (StatFunction modifiers : template.getModifiers().getModifiers()) {
                    functions.add(new StatAddFunction(modifiers.getName(), modifiers.getValue(), modifiers.isBonus()));
                    player.getGameStats().addEffect(this, functions);
                }
            }
        }
    }

    public void end(Player player) {
        functions.clear();
        player.getGameStats().endEffect(this);
    }

    public void onLevelUp() {
        ++level;
        exp = 0;
    }

    public void addexp() {
        ++exp;
    }
}
