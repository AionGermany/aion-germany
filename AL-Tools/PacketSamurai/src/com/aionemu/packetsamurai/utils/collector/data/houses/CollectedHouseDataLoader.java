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


package com.aionemu.packetsamurai.utils.collector.data.houses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.aionemu.packetsamurai.PacketSamurai;

public class CollectedHouseDataLoader {

	private static Set<Integer> addresses = new HashSet<Integer>();
	private static Map<Integer, List<Spawn>> addressSpawns = new HashMap<Integer, List<Spawn>>();
	private static Map<Integer, HouseDecorData> decorData = new HashMap<Integer, HouseDecorData>();

	private static String SQL_FORMAT = "UPDATE `houses` SET `outwall` = %s, `inwall` = %s, `infloor` = %s, `frame` = %s, `garden` = %s, `fence` = %s, `roof` = %s, `door` = %s, `addon` = %s WHERE `address` = %d AND building_id = %d;";

	public static void load() {
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			schema = sf.newSchema(new File("./data/houses.xsd"));
			File xml = new File("./data/houses.xml");

			JAXBContext jc;
			Unmarshaller unmarshaller;
			jc = JAXBContext.newInstance(HouseLands.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			HouseLands lands = (HouseLands) unmarshaller.unmarshal(xml);
			for (Land land : lands.getLand()) {
				for (Address address : land.getAddresses().getAddress()) {
					addresses.add(address.getId());
				}
			}
			addresses.remove(0);
		}
		catch (SAXException se) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load houses.xsd: " + se.toString());
		}
		catch (JAXBException je) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load houses.xml: " + je.toString());
		}

		try {
			schema = sf.newSchema(new File("./data/house_data/house_npcs.xsd"));
			File xml = new File("./data/house_data/house_npcs.xml");

			JAXBContext jc;
			Unmarshaller unmarshaller;
			jc = JAXBContext.newInstance(HouseNpcs.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			HouseNpcs objects = (HouseNpcs) unmarshaller.unmarshal(xml);
			for (Address addressData : objects.getAddress()) {
				addressSpawns.put(addressData.getId(), addressData.getSpawn());
			}
		}
		catch (SAXException se) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load house_npcs.xsd: " + se.toString());
		}
		catch (JAXBException je) {
			PacketSamurai.getUserInterface().log("ERROR: Failed to load house_npcs.xml: " + je.toString());
		}

		File sqlFile = new File("data/house_data/house_appearances.sql");
		if (!sqlFile.exists())
			return;

		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(sqlFile);
			reader = new BufferedReader(new InputStreamReader(fis));
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				int[] values = new int[11];
				int index = 0;
				String[] tokens = strLine.split("=");
				for (String token : tokens) {
					String[] substrs = token.split(",");
					if (substrs.length == 2) {
						try {
							values[index] = Integer.parseInt(substrs[0].trim());
						}
						catch (NumberFormatException ex) {
							values[index] = 0;
						}
					}
					else if (substrs.length == 1) {
						substrs = token.split("AND");
						if (substrs.length != 2)
							substrs = token.split("WHERE");
						try {
							if ("NULL".equals(substrs[0].trim()))
								values[index] = 0;
							else
								values[index] = Integer.parseInt(substrs[0].replace(";", "").trim());
						}
						catch (NumberFormatException ex) {
							continue;
						}
					}
					index++;
				}
				HouseDecorData data = new HouseDecorData();
				data.outwall = values[0];
				data.inwall = values[1];
				data.infloor = values[2];
				data.frame = values[3];
				data.garden = values[4];
				data.fence = values[5];
				data.roof = values[6];
				data.door = values[7];
				data.addon = values[8];
				data.address = values[9];
				data.building = values[10];
				decorData.put(data.getUniqueId(), data);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				try {
					if (reader != null) reader.close();
					if (fis != null) fis.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static boolean isHouseAddress(int addressId) {
		return addresses.contains(addressId);
	}

	public static void add(HouseSpawnInfo objectInfo) {
		List<Spawn> addressObjects = addressSpawns.get(objectInfo.creatorId);
		if (addressObjects == null) {
			addressObjects = new ArrayList<Spawn>();
			addressSpawns.put(objectInfo.creatorId, addressObjects);
		}

		for (Spawn spawn : addressObjects) {
			if (spawn.getType() == objectInfo.getSpawnType())
				return;
		}

		Spawn sp = new Spawn();
		sp.setType(objectInfo.getSpawnType());
		sp.setX(objectInfo.X);
		sp.setY(objectInfo.Y);
		sp.setZ(objectInfo.Z);
		if (objectInfo.getH() != 0)
			sp.setH(objectInfo.getH());
		addressObjects.add(sp);
	}

	public static void save() {
		HouseNpcs collection = new HouseNpcs();
		List<Address> templateList = collection.getAddress();
		for (Entry<Integer, List<Spawn>> pair : addressSpawns.entrySet()) {
			if (pair.getValue().size() != 3) {
				PacketSamurai.getUserInterface().log("Invalid count for house spawns; address=" + pair.getKey());
				continue;
			}
			Address address = new Address();
			address.setId(pair.getKey());
			address.setSpawn(pair.getValue());
			templateList.add(address);
		}

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.houses");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(collection, new FileOutputStream("data/house_data/house_npcs.xml"));
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
		PacketSamurai.getUserInterface().log("Saved : " + templateList.size() + " house npcs!");

		File sqlFile = new File("data/house_data/house_appearances.sql");
		FileOutputStream fos = null;
		PrintWriter writer = null;
		try {
			fos = new FileOutputStream(sqlFile);
			writer = new PrintWriter(fos);
			for (HouseDecorData decor : decorData.values()) {
				writer.println(String.format(SQL_FORMAT, decor.getOutwallSql(), decor.getInwallSql(), decor.getInfloorSql(),
					decor.getFrameSql(), decor.getGardenSql(), decor.getFenceSql(), decor.getRoofSql(), decor.getDoorSql(),
					decor.getAddonSql(), decor.address, decor.building));
				writer.flush();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				try {
					if (writer != null) writer.close();
					if (fos != null) fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}

		PacketSamurai.getUserInterface().log("Saved : " + decorData.size() + " house appearances!");
	}

	public static void add(HouseDecorData data) {
		if (decorData.containsKey(data.getUniqueId()))
			return;
		decorData.put(data.getUniqueId(), data);
	}

}
