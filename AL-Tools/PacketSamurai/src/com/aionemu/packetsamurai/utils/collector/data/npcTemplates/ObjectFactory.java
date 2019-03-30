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
package com.aionemu.packetsamurai.utils.collector.data.npcTemplates;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public NpcTemplates createNpcTemplates() {
        return new NpcTemplates();
    }
  
    public BoundRadius createBoundRadius() {
        return new BoundRadius();
    }
  
    public NpcTemplate createNpcTemplate() {
        return new NpcTemplate();
    }
  
    public TalkInfo createTalkInfo() {
        return new TalkInfo();
    }

	public MassiveLooting createMassiveLooting() {
    return new MassiveLooting();
  }
	
    public CreatureSpeeds createCreatureSpeeds() {
        return new CreatureSpeeds();
    }
  
    public NpcStatsTemplate createNpcStatsTemplate() {
        return new NpcStatsTemplate();
    }
  
    public NpcEquipmentList createNpcEquipmentList() {
        return new NpcEquipmentList();
    }
  
    public KiskStats createKiskStats() {
        return new KiskStats();
    }
}
