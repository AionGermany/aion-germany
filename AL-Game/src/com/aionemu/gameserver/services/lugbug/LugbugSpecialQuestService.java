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
package com.aionemu.gameserver.services.lugbug;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.lugbug.LugbugSpecialQuestTemplate;

/**
 * @author Falke_34
 */
public class LugbugSpecialQuestService {

	private static final Logger log = LoggerFactory.getLogger("LUGBUG_QUEST_LOG"); 
	private Map<Integer, LugbugSpecialQuestTemplate> allSpecialQuests = DataManager.LUGBUG_SPECIAL_QUEST_DATA.getAll();

	/**
	 * initialize all quests
	 */
	public void initialize() {
		if (allSpecialQuests.size() != 0) {
			for (LugbugSpecialQuestTemplate lugbugSpecialQuest : allSpecialQuests.values()) {
				log.info("[LugbugSpecialQuestService] Loaded Quest " + lugbugSpecialQuest.getId());
			}
		}
	}

	private static class SingletonHolder {
		protected static final LugbugSpecialQuestService instance = new LugbugSpecialQuestService();
	}

	public static final LugbugSpecialQuestService getInstance() {
		return SingletonHolder.instance;
	}
}
