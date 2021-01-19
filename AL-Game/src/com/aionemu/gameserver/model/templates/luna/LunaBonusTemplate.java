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
package com.aionemu.gameserver.model.templates.luna;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlType(name = "luna_bonusattr")
@XmlAccessorType(XmlAccessType.NONE)
public class LunaBonusTemplate {

    @XmlElement(name = "bonus_attr")
    protected List<LunaBonusAttr> bonusAttr;
    @XmlAttribute(name = "buff_id", required = true)
    protected int buffId;
    
    public List<LunaBonusAttr> getPenaltyAttr() {
        if (bonusAttr == null) {
            this.bonusAttr = new ArrayList<LunaBonusAttr>();
        }
        return bonusAttr;
    }
    
    public int getBuffId() {
        return buffId;
    }
}
