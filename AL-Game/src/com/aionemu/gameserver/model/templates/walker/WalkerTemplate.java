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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.spawnengine.WalkerGroupType;

/**
 * @author KKnD
 */
@XmlRootElement(name = "walker_template")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalkerTemplate {

	@XmlElement(name = "routestep")
	private List<RouteStep> routeStepList;
	@XmlAttribute(name = "route_id", required = true)
	private String routeId;
	@XmlAttribute(name = "pool", required = true)
	private int pool = 1;
	@XmlAttribute(name = "formation")
	private WalkerGroupType formation = WalkerGroupType.POINT;
	@XmlAttribute(name = "rows")
	private String rowValues;
	@XmlAttribute(name = "reversed")
	private Boolean isReversed = false;
	@XmlTransient
	private int[] rows;

	public WalkerTemplate() {
	}

	public WalkerTemplate(String routeId) {
		this.routeId = routeId;
	}

	void beforeMarshal(Marshaller marshaller) {
		if (isReversed == false) {
			isReversed = null;
		}
		if (formation == WalkerGroupType.POINT) {
			formation = null;
		}
	}

	void afterMarshal(Marshaller marshaller) {
		if (isReversed == null) {
			isReversed = false;
		}
		if (formation == null) {
			formation = WalkerGroupType.POINT;
		}
	}

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (isReversed) {
			for (int i = routeStepList.size() - 2; i > 0; i--) {
				RouteStep step = routeStepList.get(i);
				routeStepList.add(new RouteStep(step.getX(), step.getY(), step.getZ(), step.getRestTime()));
			}
		}
		for (int i = 0; i < routeStepList.size() - 1; i++) {
			routeStepList.get(i).setNextStep(routeStepList.get(i + 1));
			routeStepList.get(i).setRouteStep(i + 1);
		}
		routeStepList.get(routeStepList.size() - 1).setRouteStep(routeStepList.size());
		routeStepList.get(routeStepList.size() - 1).setNextStep(routeStepList.get(0));

		if (pool == 2) {
			formation = WalkerGroupType.SQUARE;
			rows = new int[1];
			rows[0] = 2;
		}
		else if (formation == WalkerGroupType.SQUARE) {
			if (rowValues != null) {
				String[] values = rowValues.split(",");
				rows = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					rows[i] = Integer.parseInt(values[i]);
				}
			}
			else {
				formation = WalkerGroupType.POINT;
			}
		}
		rowValues = null;
	}

	public List<RouteStep> getRouteSteps() {
		return routeStepList;
	}

	public RouteStep getRouteStep(int value) {
		return routeStepList.get(value - 1);
	}

	public String getRouteId() {
		return routeId;
	}

	public String getVersionId() {
		return DataManager.WALKER_VERSIONS_DATA.getRouteVersionId(routeId);
	}

	public int getPool() {
		return pool;
	}

	public void setPool(int pool) {
		this.pool = pool;
	}

	public void setRouteSteps(ArrayList<RouteStep> newSteps) {
		routeStepList = newSteps;
	}

	public boolean isReversed() {
		return isReversed;
	}

	public void setIsReversed(boolean value) {
		isReversed = value;
	}

	public WalkerGroupType getType() {
		return formation;
	}

	/**
	 * @return the rows
	 */
	public int[] getRows() {
		return rows;
	}

	public void clear() {
		routeStepList.clear();
	}

	public void addRouteStep(RouteStep step) {
		routeStepList.add(step);
	}
}
