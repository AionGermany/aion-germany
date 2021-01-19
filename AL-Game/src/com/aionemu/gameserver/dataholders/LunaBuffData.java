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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.luna.*;
import javax.xml.bind.annotation.*;
import gnu.trove.map.hash.*;
import javax.xml.bind.*;
import java.util.*;

@XmlRootElement(name = "luna_bonusattrs")
@XmlAccessorType(XmlAccessType.FIELD)
public class LunaBuffData {

    @XmlElement(name = "luna_bonusattr")
    private List<LunaBonusTemplate> tlist;
    private TIntObjectHashMap<LunaBonusTemplate> mcData = (TIntObjectHashMap<LunaBonusTemplate>)new TIntObjectHashMap<LunaBonusTemplate>();
    private Map<Integer, LunaBonusTemplate> mcDataMap = new HashMap<Integer, LunaBonusTemplate>(1);
    
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (LunaBonusTemplate id : tlist) {
            mcData.put(id.getBuffId(), id);
            mcDataMap.put(id.getBuffId(), id);
        }
    }
    
    public LunaBonusTemplate getLunaBuffId(int id) {
        return mcData.get(id);
    }
    
    public Map<Integer, LunaBonusTemplate> getAll() {
        return mcDataMap;
    }
    
    public int size() {
        return mcData.size();
    }
}
