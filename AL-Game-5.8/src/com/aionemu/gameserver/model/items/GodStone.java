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
package com.aionemu.gameserver.model.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.GodstoneInfo;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 * @Reworked Kill3r
 * @Rework Phantom_KNA
 */
public class GodStone extends ItemStone {

	private static final Logger log = LoggerFactory.getLogger(GodStone.class);
	private final GodstoneInfo godstoneInfo;
	private ActionObserver actionListener;
	private final int probability;
	private boolean breakProc;
	private final int probabilityLeft;
	private final ItemTemplate godItem;

	public GodStone(int itemObjId, int itemId, PersistentState persistentState) {
		super(itemObjId, itemId, 0, persistentState);
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		godItem = itemTemplate;
		godstoneInfo = itemTemplate.getGodstoneInfo();

		if (godstoneInfo != null) {
			probability = godstoneInfo.getProbability();
			probabilityLeft = godstoneInfo.getProbabilityleft();
		}
		else {
			probability = 0;
			probabilityLeft = 0;
			log.warn("CHECKPOINT: Godstone info missing for item : " + itemId);
		}

	}

	/**
	 * @param player
	 */
	public void onEquip(final Player player) {
		if (godstoneInfo == null || godItem == null) {
			return;
		}

		final Item equippedItem = player.getEquipment().getEquippedItemByObjId(getItemObjId());
		long equipmentSlot = equippedItem.getEquipmentSlot();
		final int handProbability = equipmentSlot == ItemSlot.MAIN_HAND.getSlotIdMask() ? probability : probabilityLeft;
		actionListener = new ActionObserver(ObserverType.ATTACK) {

			@Override
			public void attack(Creature creature) {
				int chance = 100;
				float breakChance = godstoneInfo.getProbabilityleft() / CustomConfig.ILLUSION_GODSTONE_BREAK_RATE;
				if (handProbability > Rnd.get(0, EnchantsConfig.BASE_GODSTONE)) {
					Skill skill = SkillEngine.getInstance().getSkill(player, godstoneInfo.getSkillid(), godstoneInfo.getSkilllvl(), player.getTarget(), godItem);
					// %effect godstone has been activated.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_PROC_EFFECT_OCCURRED(skill.getSkillTemplate().getNameId()));
					skill.setFirstTargetRangeCheck(false);
					if (skill.canUseSkill()) {
						Effect effect = new Effect(player, creature, skill.getSkillTemplate(), 1, 0, godItem);
						effect.initialize();
						effect.applyEffect();
						effect = null;
					}
					/**
					 * The destruction of a Godstone is not instantaneous, there is a 10min grace period. - When a Illusion Godstone gets damaged a message 'Illusion Godstone has cracked' will appear
					 * and a 10min timer will be activated. - The remaining time is going to be displayed in the item's tooltip. - Regardless of the actions below, Illusion Godstone is going to
					 * disappear. . Mounting, dismounting, switching weapons, armsfusing . Changing characters, logging out, terminating connection
					 */
					if (chance < breakChance && !breakProc) {
						breakProc = true;
						// %1 equipped in %0 was fractured. %1 will be destroyed in 10 minutes even if it is unequipped.
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402536, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId())));
						// The %1 equipped in %0 will be destroyed in %2 minutes
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402537, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId()), 5), 300000);
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402537, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId()), 2), 480000);
						// The %1 equipped in %0 will be destroyed in %2 seconds
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402538, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId()), 60), 540000);
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402538, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId()), 30), 570000);
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402538, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId()), 10), 590000);
						PacketSendUtility.playerSendPacketTime(player, new SM_SYSTEM_MESSAGE(1402237, new DescriptionId(equippedItem.getNameId()), new DescriptionId(godItem.getNameId())), 600000);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								onUnEquip(player);
								equippedItem.setGodStone(null);
								setPersistentState(PersistentState.DELETED);
								ItemPacketService.updateItemAfterInfoChange(player, equippedItem);
								DAOManager.getDAO(InventoryDAO.class).store(equippedItem, player);
								PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, equippedItem));
							}
						}, 600000);
					}
				}
			}
		};

		player.getObserveController().addObserver(actionListener);
	}

	/**
	 * @param player
	 */
	public void onUnEquip(Player player) {
		if (actionListener != null) {
			player.getObserveController().removeObserver(actionListener);
		}

	}
}
