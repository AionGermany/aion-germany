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
package com.aionemu.gameserver.model;

/**
 * @author Rolandas
 */
public enum DialogPage {

	NULL(DialogAction.NULL, 0),
	STIGMA(DialogAction.OPEN_STIGMA_WINDOW, 1),
	CREATE_LEGION(DialogAction.CREATE_LEGION, 2),
	ASK_QUEST_ACCEPT_WINDOW(4),
	SELECT_QUEST_REWARD_WINDOW1(5),
	SELECT_QUEST_REWARD_WINDOW2(6),
	SELECT_QUEST_REWARD_WINDOW3(7),
	SELECT_QUEST_REWARD_WINDOW4(8),
	SELECT_QUEST_REWARD_WINDOW5(45),
	SELECT_QUEST_REWARD_WINDOW6(46),
	SELECT_QUEST_REWARD_WINDOW7(47),
	SELECT_QUEST_REWARD_WINDOW8(48),
	SELECT_QUEST_REWARD_WINDOW9(49),
	SELECT_QUEST_REWARD_WINDOW10(50),
	VENDOR(DialogAction.OPEN_VENDOR, 13),
	RETRIEVE_CHAR_WAREHOUSE(DialogAction.RETRIEVE_CHAR_WAREHOUSE, 14),
	DEPOSIT_CHAR_WAREHOUSE(DialogAction.DEPOSIT_CHAR_WAREHOUSE, 15),
	RETRIEVE_ACCOUNT_WAREHOUSE(DialogAction.RETRIEVE_ACCOUNT_WAREHOUSE, 16),
	DEPOSIT_ACCOUNT_WAREHOUSE(DialogAction.DEPOSIT_ACCOUNT_WAREHOUSE, 17),
	MAIL(DialogAction.OPEN_POSTBOX, 18),
	CHANGE_ITEM_SKIN(DialogAction.CHANGE_ITEM_SKIN, 19),
	REMOVE_MANASTONE(DialogAction.REMOVE_MANASTONE, 20),
	GIVE_ITEM_PROC(DialogAction.GIVE_ITEM_PROC, 21),
	GATHER_SKILL_LEVELUP(DialogAction.GATHER_SKILL_LEVELUP, 23),
	LOOT(DialogAction.NULL, 24),
	LEGION_WAREHOUSE(DialogAction.OPEN_LEGION_WAREHOUSE, 25),
	PERSONAL_WAREHOUSE(DialogAction.OPEN_PERSONAL_WAREHOUSE, 26),
	NO_RIGHT(27),
	COMBINETASK_WINDOW(DialogAction.CRAFT, 28),
	COMPOUND_WEAPON(DialogAction.COMPOUND_WEAPON, 29),
	DECOMPOUND_WEAPON(DialogAction.DECOMPOUND_WEAPON, 30),
	HOUSING_MARKER(DialogAction.NULL, 32), // Unknown
	HOUSING_LIFETIME(DialogAction.NULL, 33), // Unknown
	CHARGE_ITEM(DialogAction.NULL, 35), // Actually, two choices
	HOUSING_FRIENDLIST(DialogAction.HOUSING_FRIENDLIST, 36),
	HOUSING_POST(DialogAction.NULL, 37), // Unknown
	HOUSING_AUCTION(DialogAction.HOUSING_PERSONAL_AUCTION, 38),
	HOUSING_PAY_RENT(DialogAction.HOUSING_PAY_RENT, 39),
	HOUSING_KICK(DialogAction.HOUSING_KICK, 40),
	HOUSING_CONFIG(DialogAction.HOUSING_CONFIG, 41),
	TOWN_CHALLENGE_TASK(43);

	private int id;
	private DialogAction action;

	private DialogPage(int id) {
		this.id = id;
	}

	private DialogPage(DialogAction action, int id) {
		this.id = id;
		this.action = action;
	}

	public int id() {
		return id;
	}

	public int actionId() {
		return action.id();
	}

	public static DialogPage getPageByAction(int dialogId) {
		for (DialogPage page : DialogPage.values()) {
			if (page.action == null) {
				continue;
			}
			if (page.actionId() == dialogId) {
				return page;
			}
		}
		return DialogPage.NULL;
	}
}
