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
package com.aionemu.packetsamurai.utils.collector.data.windstreams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import com.aionemu.packetsamurai.PacketSamurai;

public class WindstreamsTool {

    private static Map<Integer, WindFlight> data = new HashMap<Integer, WindFlight>();
 
    public static void load() {

    	try {
        	JAXBContext jc = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.windstreams");
            Unmarshaller unmarshaller = jc.createUnmarshaller();

			Windstreams collection;
			collection = (Windstreams) unmarshaller.unmarshal(new File("data/windstreams/windstreams.xml"));

            for (WindFlight template : collection.getWindstream()) {
            	data.put(template.mapid, template);
            }
            PacketSamurai.getUserInterface().log("Loaded: " + collection.getWindstream().size() + "Windstreams");
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    public static void save() {
        ObjectFactory objFactory = new ObjectFactory();
        Windstreams collection = objFactory.createWindstreams();
        
        List<WindFlight> templateList = collection.getWindstream();
        
        for (WindFlight wf: data.values()) {
        	List <Location2D> listLoc = wf.getLocations().getLocation();
        	//sort Location by LocationIds
        	Collections.sort(listLoc, new Comparator<Location2D>() {
        		public int compare(Location2D o1, Location2D o2) {
        			return o1.getId() - o2.getId(); 
        		}
        	});

        }
        templateList.addAll(data.values());
        
        //sort windstream by map
        Collections.sort(templateList, new Comparator<WindFlight>() {
			public int compare(WindFlight o1, WindFlight o2) {
				return o1.getMapid() - o2.getMapid(); 
			}
		});

         try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.windstreams");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
            marshaller.marshal(collection, new FileOutputStream("data/windstreams/windstreams.xml"));
        }
        catch (PropertyException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        PacketSamurai.getUserInterface().log("Saved: " + data.size() + " windstreams!");
    }
    
    public static void Update(int mapId, int streamId, int boost, int bidirectional) {
    	if (data.get(mapId) != null){
    		WindFlight wf = data.get(mapId);
    		Location2D location = new Location2D();
    		location.setId(streamId);
    		location.setFlyPath(getFlyPath(bidirectional));
    		location.setState(boost);
    	    List<Location2D> locations = wf.getLocations().getLocation();
    	    boolean include = true;
    	    for (Location2D loc: locations) {
    	    	if (loc.getId() == streamId)
    	    		include = false;
    	    }
    		if (include) {
    			wf.getLocations().getLocation().add(location);
    	   		PacketSamurai.getUserInterface().log("Windstreams - Added 1 windstream location into windstreams");
    	   	}
    	}
    	else {
    		WindFlight wf = new WindFlight();	
    		StreamLocations sl = new StreamLocations();
    		Location2D location = new Location2D();
    		location.setId(streamId);
    		location.setFlyPath(getFlyPath(bidirectional));
    		location.setState(boost);
    		sl.getLocation().add(location);
    		wf.setMapid(mapId);
    		wf.setLocations(sl);
    		data.put(mapId, wf);
    		PacketSamurai.getUserInterface().log("Windstreams - Added 1 windstream location and initialized 1 Map into windstream");
    	}
    }
    
    public static FlyPathType getFlyPath(int bidirectional) {
    	switch (bidirectional){
    	case 0:
    		return FlyPathType.GEYSER;
    	case 1:
       	case 2:
       	  	return FlyPathType.ONE_WAY;
     	default :
    		return FlyPathType.GEYSER;
    	}
    }
}
