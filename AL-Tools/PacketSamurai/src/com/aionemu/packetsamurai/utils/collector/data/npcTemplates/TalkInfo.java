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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name= "TalkInfo")
public class TalkInfo {

    @XmlAttribute(name= "distance")
    protected Integer distance;
    @XmlAttribute(name= "delay")
    protected Integer delay;
    @XmlAttribute(name= "is_dialog")
    protected Boolean isDialog;
    @XmlAttribute(name= "func_dialogs")
    protected List<Integer> funcDialogs;
  
    public Integer getDistance() {
        return this.distance;
    }
  
    public void setDistance(Integer value) {
        this.distance = value;
    }
    
    public Integer getDelay() {
        return this.delay;
    }
  
    public void setDelay(Integer value) {
        this.delay = value;
    }
  
    public boolean isIsDialog() {
        if (this.isDialog == null) {
            return false;
        }
        return this.isDialog.booleanValue();
    }
  
    public void setIsDialog(Boolean value) {
        this.isDialog = value;
    }
  
    public List<Integer> getFuncDialogs() {
        if (this.funcDialogs == null) {
            this.funcDialogs = new ArrayList<Integer>();
        }
        return this.funcDialogs;
    }
}
