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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.TransformationsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.TransformationCommonData;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.transformation.TransformationTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
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
		int count = DAOManager.getDAO(TransformationsDAO.class).getCount(player.getObjectId(), transformationId);
		count++;
		TransformationCommonData transformationCommonData = player.getTransformationList().addNewTransformation(player, transformationId, name, grade, count);
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
		for (TransformationTemplate tt : transformationTemplate.values()) {
			if (i == rnd) {
				transformationId = tt.getId();
				transformationName = tt.getName();
				transformationGrade = tt.getGrade();
				break;
			}
			++i;
		}
		if (!validateAdoption(player, item.getItemTemplate(), transformationId)) {
			return;
		}
		addTransformation(player, transformationId, transformationName, transformationGrade);
		player.getTransformationList().updateTransformationsList();
	}

	public void adoptTransformation(Player player, Item item, int transformationId) {
		String transformationName = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformationId).getName();
		String transformationGrade = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformationId).getGrade();
		if (!TransformationService.validateAdoption(player, item.getItemTemplate(), transformationId)) {
			return;
		}
		addTransformation(player, transformationId, transformationName, transformationGrade);
	}

	public void transform(Player player, int transformId, int itemObjId) {
		Item item = player.getInventory().getItemByObjId(itemObjId);
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, item.getObjectId(), item.getItemId(), 0, 1));
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(item.getItemTemplate().getNameId())));
		player.getInventory().decreaseByItemId(item.getItemId(), 1);
		int skillId = DataManager.TRANSFORMATION_DATA.getTransformationTemplate(transformId).getSkill();
		player.setUsingItem(item); //When scroll ID changes, update ID @ TransformEffect if (itemId == 190099001) {
		SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0);
	}


	public void CombinationTransformation(Player player, List<Integer> material) {
		if (player.getInventory().getKinah() < 50000) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404348, new Object[0]));
			return;
		}
	}

	public static TransformationService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final TransformationService instance = new TransformationService();
	}
}
