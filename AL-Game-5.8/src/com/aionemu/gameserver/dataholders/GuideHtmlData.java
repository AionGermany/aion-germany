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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.Guides.GuideTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author xTz
 */
@XmlRootElement(name = "guides")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuideHtmlData {

	@XmlElement(name = "guide", type = GuideTemplate.class)
	private List<GuideTemplate> guideTemplates;
	private final TIntObjectHashMap<ArrayList<GuideTemplate>> templates = new TIntObjectHashMap<ArrayList<GuideTemplate>>();
	private final int CLASS_ALL = 255;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (GuideTemplate template : guideTemplates) {
			addTemplate(template);
		}
		guideTemplates = null;
	}

	private void addTemplate(GuideTemplate template) {
		Race race = template.getRace();
		if (race == null) {
			race = Race.PC_ALL;
		}
		int classId = template.getPlayerClass() == null ? CLASS_ALL : template.getPlayerClass().ordinal();

		int hash = makeHash(classId, race.ordinal(), template.getLevel());
		ArrayList<GuideTemplate> value = templates.get(hash);
		if (value == null) {
			value = new ArrayList<GuideTemplate>();
			templates.put(hash, value);
		}
		value.add(template);
	}

	public int size() {
		return templates.size();
	}

	public TIntObjectHashMap<ArrayList<GuideTemplate>> getTemplates() {
		return templates;
	}

	public GuideTemplate getTemplateByTitle(String title) {
		for (int templateHash : templates.keys()) {
			for (GuideTemplate template : templates.get(templateHash)) {
				if (template.getTitle().equals(title)) {
					return template;
				}
			}
		}
		return null;
	}

	public GuideTemplate[] getTemplatesFor(PlayerClass playerClass, Race race, int level) {
		List<GuideTemplate> guideTemplate = new ArrayList<GuideTemplate>();

		List<GuideTemplate> classRaceSpecificTemplates = templates.get(makeHash(playerClass.ordinal(), race.ordinal(), level));
		List<GuideTemplate> classSpecificTemplates = templates.get(makeHash(playerClass.ordinal(), Race.PC_ALL.ordinal(), level));
		List<GuideTemplate> raceSpecificTemplates = templates.get(makeHash(CLASS_ALL, race.ordinal(), level));
		List<GuideTemplate> generalTemplates = templates.get(makeHash(CLASS_ALL, Race.PC_ALL.ordinal(), level));

		if (classRaceSpecificTemplates != null) {
			guideTemplate.addAll(classRaceSpecificTemplates);
		}
		if (classSpecificTemplates != null) {
			guideTemplate.addAll(classSpecificTemplates);
		}
		if (raceSpecificTemplates != null) {
			guideTemplate.addAll(raceSpecificTemplates);
		}
		if (generalTemplates != null) {
			guideTemplate.addAll(generalTemplates);
		}

		return guideTemplate.toArray(new GuideTemplate[guideTemplate.size()]);
	}

	private static int makeHash(int classType, int race, int level) {
		int result = classType << 8;
		result = (result | race) << 8;
		return result | level;
	}
}
