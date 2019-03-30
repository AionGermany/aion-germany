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


package com.aionemu.packetsamurai.utils.collector.data.npcskills;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.math.IntRange;

import com.aionemu.packetsamurai.PacketSamurai;

public class NpcSkillsTool {

	static final int MAX_HITS_PER_NPC = 5;

	private static Map<Integer, NpcSkillList> skillsByNpcId = new HashMap<Integer, NpcSkillList>();

	public static void load() {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcskills");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			NpcSkillTemplates collection;
			collection = (NpcSkillTemplates) unmarshaller.unmarshal(new File("data/npc_skills/npc_skills.xml"));

			PacketSamurai.getUserInterface().log("Skills [Npcs] - Loaded " + collection.getNpcskills().size()+" Npc Skills " );
			for (NpcSkillList npc : collection.getNpcskills()) {
				skillsByNpcId.put(npc.getNpcid(), npc);
			}

		} catch (JAXBException e) {
			PacketSamurai.getUserInterface().log("Skills [Npcs] - Error on loading NpcSkills Template: " + e);
		}
	}

	public static boolean addSkill(int npcId, NpcSkillTemplate template) {
		NpcSkillList npcSkills = skillsByNpcId.get(npcId);
		if (npcSkills == null) {
			npcSkills = new NpcSkillList();
			npcSkills.setNpcid(npcId);
			skillsByNpcId.put(npcId, npcSkills);
		}
		// add distribution 5% error
		if (template.getStats().maxHp + 5 > 100)
			template.setMaxhp(100);

		if (!npcSkills.getNpcskill().contains(template)) {
			if (template.getSkillid() != null && template.getSkillid() == 0) {
				npcSkills.getNpcskill().add(template);
				return true;
			}
			if (template.getStats().maxHp - 5 < 0) {
				template.setMinhp(0);
				template.getStats().minHp = 0;
			}
			else
				template.getStats().minHp = template.getStats().maxHp - 5;
			if (template.getMaxhp() != null && template.getMaxhp() == 100)
				template.getStats().maxHp = 100;
			template.getStats().useCount = 1;
			npcSkills.getNpcskill().add(template);
			return true;
		} else {
			int index = npcSkills.getNpcskill().indexOf(template);
			NpcSkillTemplate oldTemplate = npcSkills.getNpcskill().get(index);
			if (template.getSkillid() != null && template.getSkillid() == 0) {
				oldTemplate.getStats().useCount = Math.max(template.getStats().useCount, oldTemplate.getStats().useCount);
				return false;
			}
			oldTemplate.getStats().useCount++;
			// Fix skill lvl
			oldTemplate.setSkilllevel(template.getSkilllevel());
			if (oldTemplate.getStats().maxHp < template.getStats().maxHp)
				oldTemplate.getStats().maxHp = template.getStats().maxHp;
			if (oldTemplate.getStats().minHp > template.getStats().minHp)
				oldTemplate.getStats().minHp = template.getStats().minHp;
		}
		return false;
	}

	public static void save() {
		ObjectFactory objFactory = new ObjectFactory();
		NpcSkillTemplates collection = objFactory.createNpcSkillTemplates();
		List<NpcSkillList> templateList = collection.getNpcskills();
		templateList.addAll(skillsByNpcId.values());
		Collections.sort(templateList);

		NpcSkillTemplate total = new NpcSkillTemplate();
		total.setSkillid(0);

		List<NpcSkillList> toRemove = new ArrayList<NpcSkillList>();

		for (NpcSkillList skillList : templateList) {
			HashMap<IntRange, Integer> useCounts = new HashMap<IntRange, Integer>();
			HashMap<NpcSkillTemplate, Integer> skillCounts = new HashMap<NpcSkillTemplate, Integer>();
			NpcSkillTemplate totalAttacks = null;
			int index = 0;

			if (skillList.getNpcskill().contains(total)) {
				totalAttacks = skillList.getNpcskill().get(index);
				index = skillList.getNpcskill().indexOf(total);
				skillList.getNpcskill().remove(index);
			}

			int useTotal = 0;
			int skillUseTotal = 0;
			for (NpcSkillTemplate template : skillList.getNpcskill())
				skillUseTotal += template.getStats().useCount;

			if (totalAttacks != null) {
				useTotal = totalAttacks.getStats().useCount;
				if (skillUseTotal > useTotal)
					useTotal = skillUseTotal;
			} else {
				useTotal = skillUseTotal;
			}

			if (skillList.getNpcskill().size() == 0) {
				toRemove.add(skillList);
				continue;
			} else if (useTotal == 0) {
				// old data from XML
				continue;
			}

			for (NpcSkillTemplate template : skillList.getNpcskill()) {
				if (useTotal < MAX_HITS_PER_NPC) {
					if (template.getStats().maxHp > 90)
						template.setMaxhp(100);
					else if (template.getMaxhp() == null || template.getMaxhp() < template.getStats().maxHp)
						template.setMaxhp(template.getStats().maxHp);
					if (template.getStats().minHp < 10)
						template.setMinhp(0);
					else if (template.getMinhp() == null || template.getMinhp() > template.getStats().minHp)
						template.setMinhp(template.getStats().minHp);
				}
				else {
					IntRange hpRange = new IntRange(template.getStats().minHp, template.getStats().maxHp);
					if (useCounts.containsKey(hpRange)) {
						int oldCounts = useCounts.get(hpRange);
						useCounts.put(hpRange, oldCounts + template.getStats().useCount);
					}
					else
						useCounts.put(hpRange, template.getStats().useCount);
					skillCounts.put(template, template.getStats().useCount);
				}

				if (useTotal >= MAX_HITS_PER_NPC)
					template.setProbability(Math.round((float) template.getStats().useCount * 100 / useTotal));
				else {
					template.setProbability(25);
				}
			}
		}

		templateList.removeAll(toRemove);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("com.aionemu.packetsamurai.utils.collector.data.npcskills");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(collection, new FileOutputStream("data/npc_skills/npc_skills.xml"));
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("Skills [Npcs] - Saved : " + templateList.size() + " Npc Skills!");
	}
}
