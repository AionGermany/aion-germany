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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TELEPORT_BACK;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.SummonEffect;
import com.aionemu.gameserver.skillengine.effect.TransformEffect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillUseAction")
public class SkillUseAction extends AbstractItemAction {

	@XmlAttribute
	protected int skillid;
	@XmlAttribute
	protected int level;
	@XmlAttribute(required = false)
	private Integer mapid;
	boolean teleportBack = false;

	/**
	 * Gets the value of the skillid property.
	 */
	public int getSkillid() {
		return skillid;
	}

	/**
	 * Gets the value of the level property.
	 */
	public int getLevel() {
		return level;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget(), parentItem.getItemTemplate());
		if (skill == null) {
			return false;
		}
		int nameId = parentItem.getItemTemplate().getNameId();
		byte levelRestrict = parentItem.getItemTemplate().getMaxLevelRestrict(player);
		if (levelRestrict != 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(levelRestrict, nameId));
			return false;
		}
		// Cant use transform items while already transformed
		if (player.isTransformed()) {
			for (EffectTemplate template : skill.getSkillTemplate().getEffects().getEffects()) {
				if (template instanceof TransformEffect) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANT_USE_ITEM(new DescriptionId(nameId)));
					return false;
				}
			}
		}
		if (player.getSummon() != null) {
			for (EffectTemplate template : skill.getSkillTemplate().getEffects().getEffects()) {
				if (template instanceof SummonEffect) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300072));
					return false;
				}
			}
		}
		if (skill.getSkillId() == 11072) {// Add GoldenStar Energy
			PlayerCommonData pcd = player.getCommonData();
			if (pcd.getGoldenStarEnergy() == pcd.getMaxGoldenStarEnergy()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402615));
				return false;
			}

		}
		return skill.canUseSkill();
	}

	@Override
	public void act(final Player player, final Item parentItem, Item targetItem) {
		final Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget(), parentItem.getItemTemplate());
		if (skill != null) {
			if (skill.getSkillId() == 8198) {
				RequestResponseHandler responseHandler = new RequestResponseHandler(player) {

					@Override
					public void acceptRequest(Creature requester, Player responder) {
						final ActionObserver moveObserver = new ActionObserver(ObserverType.MOVE) {

							@Override
							public void moved() {
								player.getController().cancelTask(TaskId.ITEM_USE);
							}
						};
						player.getObserveController().attach(moveObserver);
						player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								player.getObserveController().removeObserver(moveObserver);
								sendTeleportBack(player);
							}
						}, 6000));

						skill.setItemObjectId(parentItem.getObjectId());
						skill.useSkill();

					}

					@Override
					public void denyRequest(Creature requester, Player responder) {
						player.getController().cancelUseItem();
						player.getController().cancelTask(TaskId.ITEM_USE);
						player.addItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId(), 0, 0);
						PacketSendUtility.sendPacket(player, new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));
					}
				};

				if (player.getRace() == Race.ELYOS && player.getWorldId() == 110010000 || player.getRace() == Race.ASMODIANS && player.getWorldId() == 120010000) {
					teleportBack = true;
					player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_ROUND_RETURN_ITEM_DO_YOU_ACCEPT_MOVE, responseHandler);
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_ROUND_RETURN_ITEM_DO_YOU_ACCEPT_MOVE, parentItem.getObjectId(), 0, new DescriptionId(parentItem.getNameId())));
				}
				else {
					teleportBack = false;
					player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_ROUND_RETURN_ITEM_ACCEPT_MOVE_DONT_RETURN, responseHandler);
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_ROUND_RETURN_ITEM_ACCEPT_MOVE_DONT_RETURN, parentItem.getObjectId(), 0, new DescriptionId(parentItem.getNameId())));
				}

			}
			else {
				player.getController().cancelUseItem();
				player.setUsingItem(parentItem);
				skill.setItemObjectId(parentItem.getObjectId());
				skill.useSkill();
			}
		}
	}

	private void sendTeleportBack(Player player) {
		if (teleportBack) {
			player.setBattleReturnCoords(player.getWorldId(), new float[] { player.getX(), player.getY(), player.getZ() });
			PacketSendUtility.sendPacket(player, new SM_TELEPORT_BACK());
			teleportBack = false;
		}
	}

	public int getMapid() {
		if (mapid == null) {
			return 0;
		}
		return mapid;
	}
}
