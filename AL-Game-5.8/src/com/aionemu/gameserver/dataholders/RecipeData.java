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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;

/**
 * @author ATracer, MrPoke, KID
 */
@XmlRootElement(name = "recipe_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeData {

	@XmlElement(name = "recipe_template")
	protected List<RecipeTemplate> list;
	private TIntObjectHashMap<RecipeTemplate> recipeData;
	private FastList<RecipeTemplate> elyos, asmos, any;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		recipeData = new TIntObjectHashMap<RecipeTemplate>();
		elyos = FastList.newInstance();
		asmos = FastList.newInstance();
		any = FastList.newInstance();
		for (RecipeTemplate it : list) {
			recipeData.put(it.getId(), it);
			if (it.getAutoLearn() == 0) {
				continue;
			}

			switch (it.getRace()) {
				case ASMODIANS:
					asmos.add(it);
					break;
				case ELYOS:
					elyos.add(it);
					break;
				case PC_ALL:
					any.add(it);
					break;
				default:
					break;
			}
		}
		list = null;
	}

	public FastList<RecipeTemplate> getAutolearnRecipes(Race race, int skillId, int maxLevel) {
		FastList<RecipeTemplate> list = FastList.newInstance();
		switch (race) {
			case ASMODIANS:
				for (RecipeTemplate recipe : asmos) {
					if (recipe.getSkillid() == skillId && recipe.getSkillpoint() <= maxLevel) {
						list.add(recipe);
					}
				}
				break;
			case ELYOS:
				for (RecipeTemplate recipe : elyos) {
					if (recipe.getSkillid() == skillId && recipe.getSkillpoint() <= maxLevel) {
						list.add(recipe);
					}
				}
				break;
			default:
				break;
		}

		for (RecipeTemplate recipe : any) {
			if (recipe.getSkillid() == skillId && recipe.getSkillpoint() <= maxLevel) {
				list.add(recipe);
			}
		}

		return list;
	}

	public RecipeTemplate getRecipeTemplateById(int id) {
		return recipeData.get(id);
	}

	public TIntObjectHashMap<RecipeTemplate> getRecipeTemplates() {
		return recipeData;
	}

	/**
	 * @return recipeData.size()
	 */
	public int size() {
		return recipeData.size();
	}
}
