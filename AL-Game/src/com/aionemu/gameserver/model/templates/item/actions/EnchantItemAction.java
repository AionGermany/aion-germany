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

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Nemiroff, Wakizashi, vlog
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction {

	// Count of required supplements
	@XmlAttribute(name = "count")
	private int count;
	// Min level of enchantable item
	@XmlAttribute(name = "min_level")
	private Integer min_level;
	// Max level of enchantable item
	@XmlAttribute(name = "max_level")
	private Integer max_level;
	@XmlAttribute(name = "manastone_only")
	private boolean manastone_only;
	@XmlAttribute(name = "chance")
	private float chance;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		int EnchantKinah = EnchantService.EnchantKinah(targetItem);

		if (isSupplementAction()) {
			return false;
		}
		if (targetItem == null) { // no item selected.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (parentItem == null) {
			return false;
		}
		if (targetItem.isEquipped() && targetItem.getItemTemplate().getCategory() == ItemCategory.STIGMA) {
			String message = "Can't Enchant equiped Stigma";
			PacketSendUtility.sendMessage(player, message);
			return false;
		}
		if (targetItem.isAmplified() && parentItem.getItemTemplate().isAmplificationStone() && player.getInventory().getKinah() < EnchantKinah) {
			return false;
		}
		int msID = parentItem.getItemTemplate().getTemplateId() / 1000000;
		int tID = targetItem.getItemTemplate().getTemplateId() / 1000000;

		if (msID == tID && tID == 140) {// Stigma Enchant //805230 sId == && sLvl == ||
			int sId = targetItem.getItemId();
			int sLvl = targetItem.getEnchantLevel();
			if (sId == 140000001 && sLvl == 0 || sId == 140000002 && sLvl == 0 || sId == 140000003 && sLvl == 0 || sId == 140000004 && sLvl == 0 || sId == 140001103 && sLvl == 5 || sId == 140001104 && sLvl == 5 || sId == 140001105 && sLvl == 5 || sId == 140001106 && sLvl == 5 || sId == 140001107 && sLvl == 5 || sId == 140001108 && sLvl == 5 || sId == 140001109 && sLvl == 7 || sId == 140001110 && sLvl == 7 || sId == 140001111 && sLvl == 7 || sId == 140001112 && sLvl == 0 || sId == 140001113 && sLvl == 0 || sId == 140001114 && sLvl == 7 || sId == 140001115 && sLvl == 7 || sId == 140001116 && sLvl == 7 || sId == 140001117 && sLvl == 7 || sId == 140001118 && sLvl == 2 || sId == 140001119 && sLvl == 2 ||
			// Gladiator
				sId == 140001120 && sLvl == 5 || sId == 140001121 && sLvl == 5 || sId == 140001122 && sLvl == 5 || sId == 140001123 && sLvl == 5 || sId == 140001124 && sLvl == 5 || sId == 140001125 && sLvl == 0 || sId == 140001126 && sLvl == 6 || sId == 140001127 && sLvl == 7 || sId == 140001128 && sLvl == 0 || sId == 140001129 && sLvl == 5 || sId == 140001130 && sLvl == 7 || sId == 140001131 && sLvl == 7 || sId == 140001132 && sLvl == 4 || sId == 140001133 && sLvl == 7 || sId == 140001134 && sLvl == 0 || sId == 140001135 && sLvl == 2 ||
				// Templar
				sId == 140001136 && sLvl == 5 || sId == 140001137 && sLvl == 5 || sId == 140001138 && sLvl == 1 || sId == 140001139 && sLvl == 0 || sId == 140001140 && sLvl == 5 || sId == 140001141 && sLvl == 5 || sId == 140001142 && sLvl == 0 || sId == 140001143 && sLvl == 0 || sId == 140001144 && sLvl == 0 || sId == 140001145 && sLvl == 6 || sId == 140001146 && sLvl == 7 || sId == 140001147 && sLvl == 6 || sId == 140001148 && sLvl == 0 || sId == 140001149 && sLvl == 7 || sId == 140001150 && sLvl == 9 || sId == 140001151 && sLvl == 2 || sId == 140001152 && sLvl == 0 ||
				// Assasin
				sId == 140001153 && sLvl == 5 || sId == 140001154 && sLvl == 5 || sId == 140001155 && sLvl == 0 || sId == 140001156 && sLvl == 5 || sId == 140001157 && sLvl == 5 || sId == 140001158 && sLvl == 0 || sId == 140001159 && sLvl == 0 || sId == 140001160 && sLvl == 7 || sId == 140001162 && sLvl == 7 || sId == 140001163 && sLvl == 7 || sId == 140001165 && sLvl == 7 || sId == 140001167 && sLvl == 0 || sId == 140001168 && sLvl == 0 || sId == 140001169 && sLvl == 0 || sId == 140001171 && sLvl == 7 || sId == 140001172 && sLvl == 2 || sId == 140001173 && sLvl == 2 ||
				// Ranger
				sId == 140001174 && sLvl == 0 || sId == 140001175 && sLvl == 7 || sId == 140001176 && sLvl == 5 || sId == 140001177 && sLvl == 5 || sId == 140001178 && sLvl == 5 || sId == 140001179 && sLvl == 0 || sId == 140001180 && sLvl == 7 || sId == 140001181 && sLvl == 5 || sId == 140001182 && sLvl == 7 || sId == 140001184 && sLvl == 5 || sId == 140001186 && sLvl == 0 || sId == 140001187 && sLvl == 0 || sId == 140001188 && sLvl == 7 || sId == 140001189 && sLvl == 7 || sId == 140001190 && sLvl == 0 || sId == 140001191 && sLvl == 2 || sId == 140001192 && sLvl == 2 ||
				// Sorcerer
				sId == 140001193 && sLvl == 0 || sId == 140001194 && sLvl == 5 || sId == 140001195 && sLvl == 5 || sId == 140001196 && sLvl == 0 || sId == 140001197 && sLvl == 5 || sId == 140001199 && sLvl == 0 || sId == 140001200 && sLvl == 7 || sId == 140001201 && sLvl == 0 || sId == 140001202 && sLvl == 7 || sId == 140001203 && sLvl == 0 || sId == 140001204 && sLvl == 7 || sId == 140001205 && sLvl == 6 || sId == 140001206 && sLvl == 0 || sId == 140001207 && sLvl == 0 || sId == 140001208 && sLvl == 0 || sId == 140001209 && sLvl == 2 || sId == 140001210 && sLvl == 0 ||
				// Spiritmaster
				sId == 140001228 && sLvl == 5 || sId == 140001229 && sLvl == 5 || sId == 140001230 && sLvl == 5 || sId == 140001232 && sLvl == 5 || sId == 140001233 && sLvl == 5 || sId == 140001234 && sLvl == 5 || sId == 140001236 && sLvl == 7 || sId == 140001237 && sLvl == 6 || sId == 140001238 && sLvl == 7 || sId == 140001239 && sLvl == 0 || sId == 140001240 && sLvl == 7 || sId == 140001241 && sLvl == 0 || sId == 140001242 && sLvl == 0 || sId == 140001243 && sLvl == 0 || sId == 140001244 && sLvl == 4 || sId == 140001245 && sLvl == 2 || sId == 140001246 && sLvl == 0 ||
				// Cleric
				sId == 140001211 && sLvl == 0 || sId == 140001212 && sLvl == 5 || sId == 140001213 && sLvl == 0 || sId == 140001214 && sLvl == 5 || sId == 140001215 && sLvl == 5 || sId == 140001216 && sLvl == 5 || sId == 140001217 && sLvl == 3 || sId == 140001218 && sLvl == 7 || sId == 140001219 && sLvl == 7 || sId == 140001220 && sLvl == 7 || sId == 140001221 && sLvl == 7 || sId == 140001222 && sLvl == 7 || sId == 140001223 && sLvl == 7 || sId == 140001224 && sLvl == 7 || sId == 140001225 && sLvl == 6 || sId == 140001226 && sLvl == 2 || sId == 140001227 && sLvl == 2 ||
				// Chanter
				sId == 140001264 && sLvl == 5 || sId == 140001265 && sLvl == 5 || sId == 140001266 && sLvl == 5 || sId == 140001267 && sLvl == 1 || sId == 140001268 && sLvl == 5 || sId == 140001269 && sLvl == 5 || sId == 140001270 && sLvl == 7 || sId == 140001271 && sLvl == 7 || sId == 140001272 && sLvl == 7 || sId == 140001273 && sLvl == 7 || sId == 140001274 && sLvl == 7 || sId == 140001275 && sLvl == 7 || sId == 140001276 && sLvl == 7 || sId == 140001277 && sLvl == 7 || sId == 140001278 && sLvl == 7 || sId == 140001279 && sLvl == 2 || sId == 140001280 && sLvl == 2 ||
				// Rider
				sId == 140001247 && sLvl == 5 || sId == 140001248 && sLvl == 5 || sId == 140001249 && sLvl == 0 || sId == 140001250 && sLvl == 5 || sId == 140001251 && sLvl == 5 || sId == 140001252 && sLvl == 0 || sId == 140001253 && sLvl == 7 || sId == 140001254 && sLvl == 7 || sId == 140001255 && sLvl == 7 || sId == 140001256 && sLvl == 5 || sId == 140001257 && sLvl == 7 || sId == 140001258 && sLvl == 7 || sId == 140001259 && sLvl == 7 || sId == 140001260 && sLvl == 7 || sId == 140001261 && sLvl == 7 || sId == 140001262 && sLvl == 2 || sId == 140001263 && sLvl == 2 ||
				// Gunner
				sId == 140001281 && sLvl == 0 || sId == 140001282 && sLvl == 5 || sId == 140001283 && sLvl == 0 || sId == 140001284 && sLvl == 0 || sId == 140001285 && sLvl == 5 || sId == 140001286 && sLvl == 5 || sId == 140001287 && sLvl == 5 || sId == 140001288 && sLvl == 7 || sId == 140001289 && sLvl == 7 || sId == 140001290 && sLvl == 0 || sId == 140001291 && sLvl == 1 || sId == 140001292 && sLvl == 0 || sId == 140001293 && sLvl == 5 || sId == 140001294 && sLvl == 7 || sId == 140001295 && sLvl == 7 || sId == 140001296 && sLvl == 2 || sId == 140001297 && sLvl == 2) {
				// Bard
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300454, new DescriptionId(targetItem.getNameId())));
				return false;
			}
			else {
				return true;
			}
		}

		if (targetItem.getItemTemplate().getArmorType() == ArmorType.WING) {
			return true;
		}

		if ((msID != 167 && msID != 166) || tID >= 120) {
			if (targetItem.getItemTemplate().isBracelet()) { // Allows Socketing for Bracelet
				return true;
			}
			else if (targetItem.getItemTemplate().isEstima()) { // Allows Enchant for Estima
				return true;
			}
			else {
				System.out.println("MSID: " + msID);
				System.out.println("TID: " + tID);
				return false;
			}
		}
		if ((targetItem.canAmplify()) && parentItem.getItemTemplate().isAmplificationStone() && targetItem.getEnchantLevel() == targetItem.getItemTemplate().getMaxEnchantLevel() && !targetItem.isAmplified()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_ENCHANT_CANNOT_01(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		act(player, parentItem, targetItem, null, 1);
	}

	// necessary overloading to not change AbstractItemAction
	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon) {

		if (supplementItem != null && !checkSupplementLevel(player, supplementItem.getItemTemplate(), targetItem.getItemTemplate())) {
			return;
		}
		// Current enchant level
		final int currentEnchant = targetItem.getEnchantLevel();
		final boolean isSuccess = isSuccess(player, parentItem, targetItem, supplementItem, targetWeapon);
		// player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), EnchantsConfig.ENCHANT_CAST_DELAY, 0));

		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300457, new DescriptionId(targetItem.getNameId()))); // Enchant Item canceled
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), targetItem.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2), true);

				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);

		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				// Item template
				ItemTemplate itemTemplate = parentItem.getItemTemplate();
				// Enchantment stone
				if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT || itemTemplate.getCategory() == ItemCategory.AMPLIFICATION) {
					EnchantService.enchantItemAct(player, parentItem, targetItem, currentEnchant, isSuccess);
				} // Stigma
				else if (itemTemplate.getCategory() == ItemCategory.STIGMA && parentItem.getItemTemplate().getCategory() == targetItem.getItemTemplate().getCategory()) {
					EnchantService.enchantStigmaAct(player, parentItem, targetItem, currentEnchant, isSuccess);
				} // Manastone
				else {
					EnchantService.socketManastoneAct(player, parentItem, targetItem, targetWeapon, isSuccess);
				}

				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 384, isSuccess ? 1 : 2));
				if (CustomConfig.ENABLE_ENCHANT_ANNOUNCE) {
					if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT || itemTemplate.getCategory() == ItemCategory.AMPLIFICATION) {
						Iterator<Player> iter = World.getInstance().getPlayersIterator();
						while (iter.hasNext()) {
							Player player2 = iter.next();
							if (targetItem.getEnchantLevel() == 15 && isSuccess) {
								if (player2.getRace() == player.getRace()) {
									PacketSendUtility.sendPacket(player2, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_15(player.getName(), targetItem.getItemTemplate().getNameId()));
								}
							}
							if (targetItem.getEnchantLevel() == 20 && isSuccess) {
								if (player2.getRace() == player.getRace()) {
									PacketSendUtility.sendPacket(player2, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_20(player.getName(), targetItem.getItemTemplate().getNameId()));
								}
							}
						}
					}
				}
			}
		}, EnchantsConfig.ENCHANT_CAST_DELAY));
	}

	/**
	 * Check, if the item enchant will be successful
	 *
	 * @param player
	 * @param parentItem
	 *            the enchantment-/manastone to insert
	 * @param targetItem
	 *            the current item to enchant
	 * @param supplementItem
	 *            the item to increase the enchant chance (if exists)
	 * @param targetWeapon
	 *            the fused weapon (if exists)
	 * @param currentEnchant
	 *            current enchant level
	 * @return true if successful
	 */
	private boolean isSuccess(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon) {
		if (parentItem.getItemTemplate() != null) {
			// Item template
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			// Enchantment stone // stigma
			if (itemTemplate.getCategory() == ItemCategory.ENCHANTMENT || itemTemplate.getCategory() == ItemCategory.AMPLIFICATION || (parentItem.getItemTemplate().getCategory() == targetItem.getItemTemplate().getCategory() && itemTemplate.getCategory() == ItemCategory.STIGMA)) {
				return EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
			}
			// Manastone
			return EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
		}
		return false;
	}

	public int getCount() {
		return count;
	}

	public int getMaxLevel() {
		return max_level != null ? max_level : 0;
	}

	public int getMinLevel() {
		return min_level != null ? min_level : 0;
	}

	public boolean isManastoneOnly() {
		return manastone_only;
	}

	public float getChance() {
		return chance;
	}

	boolean isSupplementAction() {
		return getMinLevel() > 0 || getMaxLevel() > 0 || getChance() > 0 || isManastoneOnly();
	}

	private boolean checkSupplementLevel(final Player player, final ItemTemplate supplementTemplate, final ItemTemplate targetItemTemplate) {
		// Is item manastone? True - check if player can use supplement
		if (supplementTemplate.getCategory() != ItemCategory.ENCHANTMENT) {
			// Check if max item level is ok for the enchant
			int minEnchantLevel = targetItemTemplate.getLevel();
			int maxEnchantLevel = targetItemTemplate.getLevel();

			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				if (action.getMinLevel() != 0) {
					minEnchantLevel = action.getMinLevel();
				}
				if (action.getMaxLevel() != 0) {
					maxEnchantLevel = action.getMaxLevel();
				}
			}

			if (minEnchantLevel <= targetItemTemplate.getLevel() && maxEnchantLevel >= targetItemTemplate.getLevel()) {
				return true;
			}

			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ENCHANT_ASSISTANT_NO_RIGHT_ITEM);
			return false;
		}
		return true;
	}
}
