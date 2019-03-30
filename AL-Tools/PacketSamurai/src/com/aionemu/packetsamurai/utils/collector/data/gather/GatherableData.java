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
package com.aionemu.packetsamurai.utils.collector.data.gather;

import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author ATracer
 */
@XmlRootElement(name = "gatherable_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class GatherableData {

    @XmlElement(name = "gatherable_template")
    public List<GatherableTemplate> gatherables;
    /**
     * A map containing all npc templates
     */
    public FastMap<Integer, GatherableTemplate> gatherableData = new FastMap<Integer, GatherableTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (GatherableTemplate gatherable : gatherables) 
        {
            gatherableData.put(gatherable.id, gatherable);
        }
    }

    public int size() {
        return gatherableData.size();
    }

    /**
     * /** Returns an {@link GatherableTemplate} object with given id.
     *
     * @param id id of GatherableTemplate
     * @return GatherableTemplate object containing data about Gatherable with
     * that id.
     */
    public GatherableTemplate getGatherableTemplate(int id) {
        return gatherableData.get(id);
    }
}
