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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name= "CreatureSpeeds")
public class CreatureSpeeds {

    @XmlAttribute(name= "walk")
    protected Float walk;
    @XmlAttribute(name= "group_walk")
    protected Float groupWalk;
    @XmlAttribute(name= "run")
    protected Float run;
    @XmlAttribute(name= "run_fight")
    protected Float runFight;
    @XmlAttribute(name= "group_run_fight")
    protected Float groupRunFight;
  
    public Float getWalk() {
        return this.walk;
    }
  
    public void setWalk(Float value) {
        this.walk = value;
    }
  
    public Float getGroupWalk() {
        return this.groupWalk;
    }
  
    public void setGroupWalk(Float value) {
        this.groupWalk = value;
    }
  
    public Float getRun() {
        return this.run;
    }
  
    public void setRun(Float value) {
        this.run = value;
    }
  
    public Float getRunFight() {
        return this.runFight;
    }
  
    public void setRunFight(Float value) {
        this.runFight = value;
    }
  
    public Float getGroupRunFight() {
        return this.groupRunFight;
    }
  
    public void setGroupRunFight(Float value) {
        this.groupRunFight = value;
    }
}
