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
package com.aionemu.gameserver.services;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.TransformationCommonData;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.transformation.TransformationTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORMATION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author Falke_34, FrozenKiller
 */
public class TransformationService {

	private static Logger log = LoggerFactory.getLogger(TransformationService.class);

	private void addTransformation(Player player, int transformationId, String name, String grade) {
		TransformationCommonData transformationCommonData = player.getTransformationList().addNewTransformation(player, transformationId, name, grade);
		if (transformationCommonData != null) {
			PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(1, transformationId));
		}
		player.getTransformationList().updateTransformationsList();
	}

	private static boolean validateAdoption(Player player, ItemTemplate template, int transformationId) {
		if (player.getAccessLevel() > 3 && template == null) {
			log.warn("Admin adopt transformation. TransformationId: " + transformationId);
			return true;
		}
		if (template == null || template.getActions() == null || template.getActions().getAdoptTransformationAction() == null) {
			return false;
		}
		if (DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformationId) == null) {
			log.warn("Trying adopt transformation without template. TransformationId:" + transformationId);
			return false;
		}
		return true;
	}

	public void onPlayerLogin(Player player) {
		Collection<TransformationCommonData> playerTransformations = player.getTransformationList().getTransformations();
		PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(0, playerTransformations));
		PacketSendUtility.sendPacket(player, new SM_TRANSFORMATION(3, playerTransformations));
	}

	public void adoptTransformation(Player player, Item item, String grade) {
		FastMap<Integer, TransformationTemplate> transformationTemplate =  new FastMap<Integer, TransformationTemplate>();
		int transformationId = 0;
		String transformationName = "";
		String transformationGrade = "";
		for (TransformationTemplate template : DataManager.TRANSFORMATION_DATA.getTransformationData().valueCollection()) {
			if (template.getGrade().equalsIgnoreCase(grade)) {
				transformationTemplate.put(template.getId(), template);
			}
		}
		int rnd = Rnd.get((int) 1, (int) transformationTemplate.size());
		int i = 1;
		for (TransformationTemplate mt : transformationTemplate.values()) {
			if (i == rnd) {
				transformationId = mt.getId();
				transformationName = mt.getName();
				transformationGrade = mt.getGrade();
				break;
			}
			++i;
		}
		if (!validateAdoption(player, item.getItemTemplate(), transformationId)) {
			return;
		}
		addTransformation(player, transformationId, transformationName, transformationGrade);
	}

	public void adoptTransformation(Player player, Item item, int transformationId) {
		String transformationName = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformationId).getName();
		String transformationGrade = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformationId).getGrade();
		if (!TransformationService.validateAdoption(player, item.getItemTemplate(), transformationId)) {
			return;
		}
		addTransformation(player, transformationId, transformationName, transformationGrade);
	}
	
	public void transform(Player player, int transformId, int unk) {
		int skillId = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformId).getSkill();
		SkillEngine.getInstance().getSkill(player, skillId, 1, player).useWithoutPropSkill();
	}

	public static TransformationService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final TransformationService instance = new TransformationService();
	}
}
