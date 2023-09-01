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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.recipe.LunaTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;

@XmlRootElement(name = "luna_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class LunaData {

	@XmlElement(name = "luna_template")
	protected List<LunaTemplate> list;

	private TIntObjectHashMap<LunaTemplate> lunaData;

	private FastList<LunaTemplate> elyos, asmos, any;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		lunaData = new TIntObjectHashMap<LunaTemplate>();
		elyos = FastList.newInstance();
		asmos = FastList.newInstance();
		any = FastList.newInstance();
		for (LunaTemplate lt : list) {
			lunaData.put(lt.getId(), lt);
			switch (lt.getRace()) {
				case ASMODIANS:
					asmos.add(lt);
					break;
				case ELYOS:
					elyos.add(lt);
					break;
				case PC_ALL:
					any.add(lt);
					break;
				default:
					break;
			}
		}
		list = null;
	}
	
	public FastList<LunaTemplate> getLunaTemplatesAny() {
		return any;
	}

	public LunaTemplate getLunaTemplateById(int id) {
		return lunaData.get(id);
	}

	public TIntObjectHashMap<LunaTemplate> getLunaTemplates() {
		return lunaData;
	}

	public int size() {
		return lunaData.size();
	}
}
