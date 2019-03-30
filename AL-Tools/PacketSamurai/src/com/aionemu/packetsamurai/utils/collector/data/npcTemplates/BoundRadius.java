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
@XmlType(name= "BoundRadius")
public class BoundRadius {

    @XmlAttribute(name= "front")
    protected Float front;
    @XmlAttribute(name= "side")
    protected Float side;
    @XmlAttribute(name= "upper")
    protected Float upper;
    
    public Float getFront() {
        return this.front;
    }
  
    public void setFront(Float value) {
        this.front = value;
    }
  
    public Float getSide() {
        return this.side;
    }
  
    public void setSide(Float value) {
        this.side = value;
    }
  
    public Float getUpper() {
        return this.upper;
    }
  
    public void setUpper(Float value) {
        this.upper = value;
    }
}
