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
@XmlType(name= "npcStatsTemplate", propOrder={"speeds"})
public class NpcStatsTemplate {

    protected CreatureSpeeds speeds;
    @XmlAttribute(name= "maxHp", required=true)
    protected int maxHp;
    @XmlAttribute(name= "maxXp")
    protected Integer maxXp;
    @XmlAttribute(name= "main_hand_attack")
    protected Integer mainHandAttack;
    @XmlAttribute(name= "main_hand_accuracy")
    protected Integer mainHandAccuracy;
    @XmlAttribute(name= "pdef")
    protected Integer pdef;
    @XmlAttribute(name= "mresist")
    protected Integer mresist;
    @XmlAttribute(name= "power")
    protected Integer power;
    @XmlAttribute(name= "evasion")
    protected Integer evasion;
    @XmlAttribute(name= "accuracy")
    protected Integer accuracy;
  
    public CreatureSpeeds getSpeeds() {
        return this.speeds;
    }
  
    public void setSpeeds(CreatureSpeeds value) {
        this.speeds = value;
    }
  
    public int getMaxHp() {
        return this.maxHp;
    }
  
    public void setMaxHp(int value) {
        this.maxHp = value;
    }
  
    public Integer getMaxXp() {
        return this.maxXp;
    }
  
    public void setMaxXp(Integer value) {
        this.maxXp = value;
    }
  
    public Integer getMainHandAttack() {
        return this.mainHandAttack;
    }
  
    public void setMainHandAttack(Integer value) {
        this.mainHandAttack = value;
    }
  
    public Integer getMainHandAccuracy() {
        return this.mainHandAccuracy;
    }
  
    public void setMainHandAccuracy(Integer value) {
        this.mainHandAccuracy = value;
    }
  
    public Integer getPdef() {
        return this.pdef;
    }
  
    public void setPdef(Integer value) {
        this.pdef = value;
    }
  
    public Integer getMresist() {
        return this.mresist;
    }
  
    public void setMresist(Integer value) {
        this.mresist = value;
    }
  
    public Integer getPower() {
        return this.power;
    }
  
    public void setPower(Integer value) {
        this.power = value;
    }
  
    public Integer getEvasion() {
        return this.evasion;
    }
  
    public void setEvasion(Integer value) {
        this.evasion = value;
    }
  
    public Integer getAccuracy() {
        return this.accuracy;
    }
  
    public void setAccuracy(Integer value) {
        this.accuracy = value;
    }
}
