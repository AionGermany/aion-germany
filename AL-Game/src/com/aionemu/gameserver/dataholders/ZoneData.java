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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.aionemu.gameserver.model.geometry.Area;
import com.aionemu.gameserver.model.geometry.CylinderArea;
import com.aionemu.gameserver.model.geometry.PolyArea;
import com.aionemu.gameserver.model.geometry.SemisphereArea;
import com.aionemu.gameserver.model.geometry.SphereArea;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "zones")
public class ZoneData {

	private static final Logger log = LoggerFactory.getLogger(ZoneData.class);
	@XmlElement(name = "zone")
	public List<ZoneTemplate> zoneList;
	@XmlTransient
	private TIntObjectHashMap<List<ZoneInfo>> zoneNameMap = new TIntObjectHashMap<List<ZoneInfo>>();
	@XmlTransient
	private HashMap<ZoneTemplate, Integer> weatherZoneIds = new HashMap<ZoneTemplate, Integer>();
	@XmlTransient
	private int count;

	protected void afterUnmarshal(Unmarshaller u, Object parent) {
		int lastMapId = 0;
		int weatherZoneId = 1;
		for (ZoneTemplate zone : zoneList) {
			Area area = null;
			switch (zone.getAreaType()) {
				case POLYGON:
					area = new PolyArea(zone.getName(), zone.getMapid(), zone.getPoints().getPoint(), zone.getPoints().getBottom(), zone.getPoints().getTop());
					break;
				case CYLINDER:
					area = new CylinderArea(zone.getName(), zone.getMapid(), zone.getCylinder().getX(), zone.getCylinder().getY(), zone.getCylinder().getR(), zone.getCylinder().getBottom(), zone.getCylinder().getTop());
					break;
				case SPHERE:
					area = new SphereArea(zone.getName(), zone.getMapid(), zone.getSphere().getX(), zone.getSphere().getY(), zone.getSphere().getZ(), zone.getSphere().getR());
					break;
				case SEMISPHERE:
					area = new SemisphereArea(zone.getName(), zone.getMapid(), zone.getSemisphere().getX(), zone.getSemisphere().getY(), zone.getSemisphere().getZ(), zone.getSemisphere().getR());
			}
			if (area != null) {
				List<ZoneInfo> zones = zoneNameMap.get(zone.getMapid());
				if (zones == null) {
					zones = new ArrayList<ZoneInfo>();
					zoneNameMap.put(zone.getMapid(), zones);
				}
				if (zone.getZoneType() == ZoneClassName.WEATHER) {
					if (lastMapId != zone.getMapid()) {
						lastMapId = zone.getMapid();
						weatherZoneId = 1;
					}
					weatherZoneIds.put(zone, weatherZoneId++);
				}
				zones.add(new ZoneInfo(area, zone));
				count++;
			}
		}
		zoneList.clear();
		zoneList = null;
	}

	public TIntObjectHashMap<List<ZoneInfo>> getZones() {
		return zoneNameMap;
	}

	public int size() {
		return count;
	}

	/**
	 * Weather zone ID it's an order number (starts from 1)
	 */
	public int getWeatherZoneId(ZoneTemplate template) {
		Integer id = weatherZoneIds.get(template);
		if (id == null) {
			return 0;
		}
		return id;
	}

	public void saveData() {
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			schema = sf.newSchema(new File("./data/static_data/zones/zones.xsd"));
		}
		catch (SAXException e1) {
			log.error("Error while saving data: " + e1.getMessage(), e1.getCause());
			return;
		}

		File xml = new File("./data/static_data/zones/generated_zones.xml");
		JAXBContext jc;
		Marshaller marshaller;
		try {
			jc = JAXBContext.newInstance(ZoneData.class);
			marshaller = jc.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(this, xml);
		}
		catch (JAXBException e) {
			log.error("Error while saving data: " + e.getMessage(), e.getCause());
			return;
		}
	}
}
