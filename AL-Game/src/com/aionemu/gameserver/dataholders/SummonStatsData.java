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
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.stats.SummonStatsTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
@XmlRootElement(name = "summon_stats_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class SummonStatsData {

	@XmlElement(name = "summon_stats", required = true)
	private List<SummonStatsType> summonTemplatesList = new ArrayList<SummonStatsType>();
	private final TIntObjectHashMap<SummonStatsTemplate> summonTemplates = new TIntObjectHashMap<SummonStatsTemplate>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (SummonStatsType st : summonTemplatesList) {
			int summonDark = makeHash(st.getNpcIdDark(), st.getRequiredLevel());
			summonTemplates.put(summonDark, st.getTemplate());
			int summonLight = makeHash(st.getNpcIdLight(), st.getRequiredLevel());
			summonTemplates.put(summonLight, st.getTemplate());
		}
	}

	/**
	 * @param npcId
	 * @param level
	 * @return
	 */
	public SummonStatsTemplate getSummonTemplate(int npcId, int level) {
		SummonStatsTemplate template = summonTemplates.get(makeHash(npcId, level));
		if (template == null) {
			// Water Spirit 4.8
			template = summonTemplates.get(makeHash(833305, 19));
			template = summonTemplates.get(makeHash(833306, 19));
			template = summonTemplates.get(makeHash(833307, 24));
			template = summonTemplates.get(makeHash(833308, 24));
			template = summonTemplates.get(makeHash(833309, 29));
			template = summonTemplates.get(makeHash(833310, 29));
			template = summonTemplates.get(makeHash(833311, 34));
			template = summonTemplates.get(makeHash(833312, 34));
			template = summonTemplates.get(makeHash(833313, 39));
			template = summonTemplates.get(makeHash(833314, 39));
			template = summonTemplates.get(makeHash(833315, 44));
			template = summonTemplates.get(makeHash(833316, 44));
			template = summonTemplates.get(makeHash(833317, 49));
			template = summonTemplates.get(makeHash(833318, 49));
			template = summonTemplates.get(makeHash(833319, 54));
			template = summonTemplates.get(makeHash(833320, 54));
			template = summonTemplates.get(makeHash(833321, 59));
			template = summonTemplates.get(makeHash(833322, 59));
			template = summonTemplates.get(makeHash(833255, 64));
			template = summonTemplates.get(makeHash(833256, 64));
			// Fire Spirit 4.8
			template = summonTemplates.get(makeHash(833343, 10));
			template = summonTemplates.get(makeHash(833344, 10));
			template = summonTemplates.get(makeHash(833345, 15));
			template = summonTemplates.get(makeHash(833346, 15));
			template = summonTemplates.get(makeHash(833347, 20));
			template = summonTemplates.get(makeHash(833348, 20));
			template = summonTemplates.get(makeHash(833349, 25));
			template = summonTemplates.get(makeHash(833350, 25));
			template = summonTemplates.get(makeHash(833351, 30));
			template = summonTemplates.get(makeHash(833352, 30));
			template = summonTemplates.get(makeHash(833353, 35));
			template = summonTemplates.get(makeHash(833354, 35));
			template = summonTemplates.get(makeHash(833355, 40));
			template = summonTemplates.get(makeHash(833356, 40));
			template = summonTemplates.get(makeHash(833357, 45));
			template = summonTemplates.get(makeHash(833358, 45));
			template = summonTemplates.get(makeHash(833359, 50));
			template = summonTemplates.get(makeHash(833360, 50));
			template = summonTemplates.get(makeHash(833361, 55));
			template = summonTemplates.get(makeHash(833362, 55));
			template = summonTemplates.get(makeHash(833363, 60));
			template = summonTemplates.get(makeHash(833364, 60));
			template = summonTemplates.get(makeHash(833259, 65));
			template = summonTemplates.get(makeHash(833260, 65));
			// Earth Spirit 4.8
			template = summonTemplates.get(makeHash(833287, 16));
			template = summonTemplates.get(makeHash(833288, 16));
			template = summonTemplates.get(makeHash(833289, 21));
			template = summonTemplates.get(makeHash(833290, 21));
			template = summonTemplates.get(makeHash(833291, 26));
			template = summonTemplates.get(makeHash(833292, 26));
			template = summonTemplates.get(makeHash(833293, 31));
			template = summonTemplates.get(makeHash(833294, 31));
			template = summonTemplates.get(makeHash(833295, 36));
			template = summonTemplates.get(makeHash(833296, 36));
			template = summonTemplates.get(makeHash(833297, 41));
			template = summonTemplates.get(makeHash(833298, 41));
			template = summonTemplates.get(makeHash(833299, 46));
			template = summonTemplates.get(makeHash(833300, 46));
			template = summonTemplates.get(makeHash(833301, 51));
			template = summonTemplates.get(makeHash(833302, 51));
			template = summonTemplates.get(makeHash(833303, 56));
			template = summonTemplates.get(makeHash(833304, 56));
			template = summonTemplates.get(makeHash(833253, 61));
			template = summonTemplates.get(makeHash(833254, 61));
			// Wind Spirit 4.8
			template = summonTemplates.get(makeHash(833323, 13));
			template = summonTemplates.get(makeHash(833324, 13));
			template = summonTemplates.get(makeHash(833325, 18));
			template = summonTemplates.get(makeHash(833326, 18));
			template = summonTemplates.get(makeHash(833327, 23));
			template = summonTemplates.get(makeHash(833328, 23));
			template = summonTemplates.get(makeHash(833329, 28));
			template = summonTemplates.get(makeHash(833330, 28));
			template = summonTemplates.get(makeHash(833331, 33));
			template = summonTemplates.get(makeHash(833332, 33));
			template = summonTemplates.get(makeHash(833333, 38));
			template = summonTemplates.get(makeHash(833334, 38));
			template = summonTemplates.get(makeHash(833335, 43));
			template = summonTemplates.get(makeHash(833336, 43));
			template = summonTemplates.get(makeHash(833337, 48));
			template = summonTemplates.get(makeHash(833338, 48));
			template = summonTemplates.get(makeHash(833339, 53));
			template = summonTemplates.get(makeHash(833340, 53));
			template = summonTemplates.get(makeHash(833341, 58));
			template = summonTemplates.get(makeHash(833342, 58));
			template = summonTemplates.get(makeHash(833257, 63));
			template = summonTemplates.get(makeHash(833258, 63));
			// Magma Spirit 4.8
			template = summonTemplates.get(makeHash(833366, 50));
			template = summonTemplates.get(makeHash(833368, 55));
			template = summonTemplates.get(makeHash(833370, 60));
			template = summonTemplates.get(makeHash(833262, 65));
			// Tempest Spirit 4.8
			template = summonTemplates.get(makeHash(833365, 50));
			template = summonTemplates.get(makeHash(833367, 55));
			template = summonTemplates.get(makeHash(833369, 60));
			template = summonTemplates.get(makeHash(833261, 65));
			// Siege Weapon
			template = summonTemplates.get(makeHash(201054, 40));
			template = summonTemplates.get(makeHash(201055, 40));
			// Quality Siege Weapon
			template = summonTemplates.get(makeHash(201056, 56));
			template = summonTemplates.get(makeHash(201057, 56));
			template = summonTemplates.get(makeHash(201058, 60));
			template = summonTemplates.get(makeHash(201059, 60));
		}
		return template;
	}

	/**
	 * Size of summon templates
	 *
	 * @return
	 */
	public int size() {
		return summonTemplates.size();
	}

	@XmlRootElement(name = "summonStatsTemplateType")
	private static class SummonStatsType {

		@XmlAttribute(name = "npc_id_dark", required = true)
		private int npcIdDark;
		@XmlAttribute(name = "npc_id_light", required = true)
		private int npcIdLight;
		@XmlAttribute(name = "level", required = true)
		private int requiredLevel;
		@XmlElement(name = "stats_template")
		private SummonStatsTemplate template;

		public int getNpcIdDark() {
			return npcIdDark;
		}

		public int getNpcIdLight() {
			return npcIdLight;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public SummonStatsTemplate getTemplate() {
			return template;
		}
	}

	/**
	 * Note:<br>
	 * max level is 255
	 *
	 * @param npcId
	 * @param level
	 * @return
	 */
	private static int makeHash(int npcId, int level) {
		return npcId << 10 | level;
	}
}
