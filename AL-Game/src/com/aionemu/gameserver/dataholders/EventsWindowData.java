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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.event.EventsWindow;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Ghostfur (Aion-Unique)
 */
@XmlRootElement(name = "events_window")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EventsWindowData {

	@XmlElement(name = "event_window")
	private List<EventsWindow> events_window;

	@XmlTransient
	private TIntObjectHashMap<EventsWindow> eventData = new TIntObjectHashMap<EventsWindow>();

	@XmlTransient
	private Map<Integer, EventsWindow> eventDataMap = new HashMap<>(1);

	void afterUnmarshal(Unmarshaller unmarshaller, Object object) {
		for (EventsWindow eventsWindow : events_window) {
			eventData.put(eventsWindow.getId(), eventsWindow);
			eventDataMap.put(eventsWindow.getId(), eventsWindow);
		}
	}

	public int size() {
		return eventData.size();
	}

	public EventsWindow getEventWindowId(int EventData) {
		return eventData.get(EventData);
	}

	public Map<Integer, EventsWindow> getAllEvents() {
		return eventDataMap;
	}
}
