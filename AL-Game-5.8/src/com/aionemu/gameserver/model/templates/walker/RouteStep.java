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
package com.aionemu.gameserver.model.templates.walker;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author KKnD, Rolandas
 */
@XmlRootElement(name = "routestep")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteStep {

	@XmlAttribute(name = "step", required = true)
	private int routeStep;
	@XmlAttribute(name = "x", required = true)
	private float locX;
	@XmlAttribute(name = "y", required = true)
	private float locY;
	@XmlAttribute(name = "z", required = true)
	private float locZ;
	@XmlAttribute(name = "rest_time", required = true)
	private Integer time = 0;
	@XmlTransient
	private RouteStep nextStep;

	void beforeMarshal(Marshaller marshaller) {
		if (time == 0) {
			time = null;
		}
	}

	void afterMarshal(Marshaller marshaller) {
		if (time == null) {
			time = 0;
		}
	}

	public RouteStep() {
	}

	public RouteStep(float x, float y, float z, int restTime) {
		locX = x;
		locY = y;
		locZ = z;
		time = restTime;
	}

	public float getX() {
		return locX;
	}

	public float getY() {
		return locY;
	}

	public float getZ() {
		return locZ;
	}

	public void setZ(float z) {
		locZ = z;
	}

	public int getRestTime() {
		return time;
	}

	public RouteStep getNextStep() {
		return nextStep;
	}

	public void setNextStep(RouteStep nextStep) {
		this.nextStep = nextStep;
	}

	public int getRouteStep() {
		return routeStep;
	}

	public void setRouteStep(int routeStep) {
		this.routeStep = routeStep;
	}
}
