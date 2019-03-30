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


package com.aionemu.packetsamurai.utils.collector.data.npcMoves;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import com.aionemu.packetsamurai.PacketSamurai;

public class NpcMoveDataSaver {

	private static Map<Integer, Npc> data = new HashMap<Integer, Npc>();

	public static void add(int npcId, String name, float x, float y, float z, byte mask, long time) {
		Npc npcData = data.get(npcId);
		if (npcData == null) {
			npcData = new Npc();
			npcData.setId(npcId);
			npcData.setName(name);
			data.put(npcId, npcData);
		}
		NpcMove move = new NpcMove(x, y, z, mask, time);
		npcData.getStep().add(move);
		if (mask != 0 && npcData.getStep().size() > 1) {
			NpcMove prevMove = npcData.getStep().get(npcData.getStep().size() - 2);
			double distance = getDistance(prevMove.getX(), prevMove.getY(), prevMove.getZ(),
				move.getX(), move.getY(), move.getZ());
			if (distance > 0 && move.getTime() > prevMove.getTime()) {
				prevMove.setSpeed((float) (distance * 1000 / (move.getTime() - prevMove.getTime())));
				if (prevMove.getSpeed() != null && prevMove.getSpeed() < 0.5)
					prevMove.setSpeed(0);
			}
		}
	}
	
	private static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static void save() {
		ObjectFactory objFactory = new ObjectFactory();
		NpcMoveList collection = objFactory.createNpcMoveList();
		List<Npc> templateList = collection.getNpc();
		templateList.addAll(data.values());
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcMoves");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(collection, new FileOutputStream("data/npc_moves/npc_moves.xml"));
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
		PacketSamurai.getUserInterface().log("Saved : " + templateList.size() + " npc moves!");
	}

}
