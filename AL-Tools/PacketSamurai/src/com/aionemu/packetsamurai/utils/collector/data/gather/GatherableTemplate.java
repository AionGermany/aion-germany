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
import javax.xml.bind.annotation.*;

/**
 * @author ATracer, KID
 */
@XmlRootElement(name = "gatherable_template")
@XmlAccessorType(XmlAccessType.FIELD)
public class GatherableTemplate {

    @XmlElement(required = true)
    public Materials materials;
    @XmlElement(required = true)
    public ExMaterials exmaterials;
    @XmlAttribute
    public int id;
    @XmlAttribute
    public String name;
    @XmlAttribute
    public int nameId;
    @XmlAttribute
    public String sourceType;
    @XmlAttribute
    public int harvestCount;
    @XmlAttribute
    public int skillLevel;
    @XmlAttribute
    public int harvestSkill;
    @XmlAttribute
    public int successAdj;
    @XmlAttribute
    public int failureAdj;
    @XmlAttribute
    public int aerialAdj;
    @XmlAttribute
    public int captcha;
    @XmlAttribute
    public int lvlLimit;
    @XmlAttribute
    public int reqItem;
    @XmlAttribute
    public int reqItemNameId;
    @XmlAttribute
    public int checkType;
    @XmlAttribute
    public int eraseValue;
}

   