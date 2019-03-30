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
@XmlType(name= "KiskStats")
public class KiskStats {

    @XmlAttribute(name= "usemask", required=true)
    protected int usemask;
    @XmlAttribute(name= "members", required=true)
    protected int members;
    @XmlAttribute(name= "resurrects", required=true)
    protected int resurrects;
    
    public int getUsemask() {
        return this.usemask;
    }
  
    public void setUsemask(int value) {
        this.usemask = value;
    }
  
    public int getMembers() {
        return this.members;
    }
  
    public void setMembers(int value) {
        this.members = value;
    }
  
    public int getResurrects() {
        return this.resurrects;
    }
  
    public void setResurrects(int value) {
        this.resurrects = value;
    }
}
