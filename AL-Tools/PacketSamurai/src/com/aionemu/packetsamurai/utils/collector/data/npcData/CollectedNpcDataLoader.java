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


/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.packetsamurai.utils.collector.data.npcData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import com.aionemu.packetsamurai.PacketSamurai;

/**
 * @author Mr. Poke
 */
public class CollectedNpcDataLoader {

	private static Map<Integer, NpcStat> data = new HashMap<Integer, NpcStat>();

	public static void load() {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcData");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			NpcStats collection;
			collection = (NpcStats) unmarshaller.unmarshal(new File("data/npc_data/npc_data.xml"));
			PacketSamurai.getUserInterface().log("Data [Npcs] - Loaded: " + collection.getNpcStat().size()+" sniffed Npc Data.");
			for (NpcStat c : collection.getNpcStat())
				data.put(c.npcId, c);
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static void add(int npcId, int level, int hp, int titleId, int npcType) {
		NpcStat npcData = data.get(npcId);
		if (npcData == null) {
			npcData = new NpcStat();
			npcData.setNpcId(npcId);
			npcData.setNpcType(npcType);
			data.put(npcId, npcData);
		}

		boolean isNew = false;
		if (npcData.getHp() != hp && (isNew = true))
			npcData.setHp(hp);
		if (npcData.getLvl() != level && (isNew = true))
			npcData.setLvl(level);
		if (titleId != npcData.getTitleId() && (isNew = true))
			npcData.setTitleId(titleId);
		if (npcType != npcData.getNpcType())
			npcData.setNpcType(npcType);

		if (isNew)
			PacketSamurai.getUserInterface().log("Data [Npcs] - Found New NpcData: " + npcData.toString());
	}

	public static void save() {
		ObjectFactory objFactory = new ObjectFactory();
		NpcStats collection = objFactory.createNpcStats();
		List<NpcStat> templateList = collection.getNpcStat();
		templateList.addAll(data.values());
		Collections.sort(templateList);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcData");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(collection, new FileOutputStream("data/npc_data/npc_data.xml"));
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
		PacketSamurai.getUserInterface().log("Data [Npcs] - Saved : " + templateList.size() + " npc data!");
	}

}
