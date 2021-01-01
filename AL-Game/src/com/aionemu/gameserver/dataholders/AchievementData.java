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

import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;
import com.aionemu.gameserver.model.templates.achievement.AchievementTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javolution.util.FastList;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "achievement_templates")
public class AchievementData {

	@XmlElement(name = "achievement_template", required = true)
	protected List<AchievementTemplate> achievements;
	List<AchievementTemplate> daily = new FastList<AchievementTemplate>();
	List<AchievementTemplate> weekly = new FastList<AchievementTemplate>();
	@XmlTransient
	private TIntObjectHashMap<AchievementTemplate> custom = new TIntObjectHashMap<AchievementTemplate>();

	public AchievementTemplate getAchievementId(int id) {
		return custom.get(id);
	}

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (AchievementTemplate it : achievements) {
        	getCustomMap().put(it.getId(), it);
            if (it.getType() == AchievementType.DAILY) {
                daily.add(it);
            }
            else {
                weekly.add(it);
            }
        }
    }

	public List<AchievementTemplate> getDaily() {
		return daily;
	}

	public List<AchievementTemplate> getWeekly() {
		return weekly;
	}

	private TIntObjectHashMap<AchievementTemplate> getCustomMap() {
		return custom;
	}

	public int size() {
		return custom.size();
	}
}
