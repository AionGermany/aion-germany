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
package com.aionemu.gameserver.dataholders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.event.EventTemplate;

import gnu.trove.map.hash.THashMap;

/**
 * <p/>
 * Java class for EventData complex type.
 * <p/>
 * The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * 
 * <pre>
 * &lt;complexType name="EventData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="event" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{}EventTemplate">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventData", propOrder = { "active", "events" })
@XmlRootElement(name = "events_config")
public class EventData {

	@XmlElement(required = true)
	protected String active;
	@XmlElementWrapper(name = "events")
	@XmlElement(name = "event")
	protected List<EventTemplate> events;
	@XmlTransient
	private THashMap<String, EventTemplate> activeEvents = new THashMap<String, EventTemplate>();
	@XmlTransient
	private THashMap<String, EventTemplate> allEvents = new THashMap<String, EventTemplate>();
	@XmlTransient
	private int counter = 0;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (active == null || events == null) {
			return;
		}

		counter = 0;
		allEvents.clear();
		activeEvents.clear();

		Set<String> ae = new HashSet<String>();
		Collections.addAll(ae, active.split(";"));

		for (EventTemplate ev : events) {
			if (ae.contains(ev.getName()) && ev.isActive()) {
				activeEvents.put(ev.getName(), ev);
				counter++;
			}
			allEvents.put(ev.getName(), ev);
		}

		events.clear();
		events = null;
		active = null;
	}

	public int size() {
		return counter;
	}

	public String getActiveText() {
		return active;
	}

	public List<EventTemplate> getAllEvents() {
		List<EventTemplate> result = new ArrayList<EventTemplate>();
		synchronized (allEvents) {
			result.addAll(allEvents.values());
		}

		return result;
	}

	public void setAllEvents(List<EventTemplate> events, String active) {
		if (events == null) {
			events = new ArrayList<EventTemplate>();
		}
		this.events = events;
		this.active = active;

		for (EventTemplate et : this.events) {
			if (allEvents.containsKey(et.getName())) {
				EventTemplate oldEvent = allEvents.get(et.getName());
				if (oldEvent.isActive() && oldEvent.isStarted()) {
					et.setStarted();
				}
			}
		}
		afterUnmarshal(null, null);
	}

	public List<EventTemplate> getActiveEvents() {
		List<EventTemplate> result = new ArrayList<EventTemplate>();
		synchronized (activeEvents) {
			result.addAll(activeEvents.values());
		}

		return result;
	}

	public boolean Contains(String eventName) {
		return activeEvents.containsKey(eventName);
	}
}
