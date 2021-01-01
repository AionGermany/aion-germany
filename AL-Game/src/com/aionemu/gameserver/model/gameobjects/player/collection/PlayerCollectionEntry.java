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
import com.aionemu.gameserver.model.templates.collection.CollectionTemplate;

public class PlayerCollectionEntry implements StatOwner {

    private int id;
    private boolean item1;
    private boolean item2;
    private boolean item3;
    private boolean item4;
    private boolean item5;
    private boolean item6;
    private boolean item7;
    private boolean item8;
    private boolean item9;
    private boolean item10;
    private boolean item11;
    private boolean item12;
    private boolean item13;
    private boolean item14;
    private int step;
    private boolean complete = false;
    private CollectionTemplate ct;
    private List<IStatFunction> functions = new ArrayList<IStatFunction>();

    public PlayerCollectionEntry(int id, boolean item1, boolean item2, boolean item3, boolean item4, boolean item5, boolean item6, boolean item7, boolean item8, boolean item9, boolean item10, boolean item11, boolean item12, boolean item13, boolean item14, int step) {
        this.id = id;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.item7 = item7;
        this.item8 = item8;
        this.item9 = item9;
        this.item10 = item10;
        this.item11 = item11;
        this.item12 = item12;
        this.item13 = item13;
        this.item14 = item14;
        this.step = step;
        ct = DataManager.COLLECTION_TEMPLATE_DATA.getTemplate(this.id);
    }

    public int getId() {
        return id;
    }

    public boolean isItem1() {
        return item1;
    }

    public void setItem1(boolean item1) {
        this.item1 = item1;
    }

    public boolean isItem2() {
        return item2;
    }

    public void setItem2(boolean item2) {
        this.item2 = item2;
    }

    public boolean isItem3() {
        return item3;
    }

    public void setItem3(boolean item3) {
        this.item3 = item3;
    }

    public boolean isItem4() {
        return item4;
    }

    public void setItem4(boolean item4) {
        this.item4 = item4;
    }

    public boolean isItem5() {
        return item5;
    }

    public void setItem5(boolean item5) {
        this.item5 = item5;
    }

    public boolean isItem6() {
        return item6;
    }

    public void setItem6(boolean item6) {
        this.item6 = item6;
    }

    public boolean isItem7() {
        return item7;
    }

    public void setItem7(boolean item7) {
        this.item7 = item7;
    }

    public boolean isItem8() {
        return item8;
    }

    public void setItem8(boolean item8) {
        this.item8 = item8;
    }

    public boolean isItem9() {
        return item9;
    }

    public void setItem9(boolean item9) {
        this.item9 = item9;
    }

    public boolean isItem10() {
        return item10;
    }

    public void setItem10(boolean item10) {
        this.item10 = item10;
    }

    public boolean isItem11() {
        return item11;
    }

    public void setItem11(boolean item11) {
        this.item11 = item11;
    }

    public boolean isItem12() {
        return item12;
    }

    public void setItem12(boolean item12) {
        this.item12 = item12;
    }

    public boolean isItem13() {
        return item13;
    }

    public void setItem13(boolean item13) {
        this.item13 = item13;
    }

    public boolean isItem14() {
        return item14;
    }

    public void setItem14(boolean item14) {
        this.item14 = item14;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void update(int index) {
        switch (index) {
            case 0: {
                setItem1(true);
                break;
            }
            case 1: {
                setItem2(true);
                break;
            }
            case 2: {
                setItem3(true);
                break;
            }
            case 3: {
                setItem4(true);
                break;
            }
            case 4: {
                setItem5(true);
                break;
            }
            case 5: {
                setItem6(true);
                break;
            }
            case 6: {
                setItem7(true);
                break;
            }
            case 7: {
                setItem8(true);
                break;
            }
            case 8: {
                setItem9(true);
                break;
            }
            case 9: {
                setItem10(true);
                break;
            }
            case 10: {
                setItem11(true);
                break;
            }
            case 11: {
                setItem12(true);
                break;
            }
            case 12: {
                setItem13(true);
                break;
            }
            case 13: {
                setItem14(true);
            }
        }
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void apply(Player player) {
        if (ct.getModifiers() != null) {
            for (StatFunction modifiers : ct.getModifiers().getModifiers()) {
                this.functions.add(new StatAddFunction(modifiers.getName(), modifiers.getValue(), modifiers.isBonus()));
                player.getGameStats().addEffect(this, functions);
            }
        }
    }

    public void end(Player player) {
        functions.clear();
        player.getGameStats().endEffect(this);
    }
}
