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
package com.aionemu.gameserver.services.mail;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.templates.mail.MailPart;
import com.aionemu.gameserver.model.templates.mail.MailTemplate;

/**
 * @author Rolandas
 */
public final class MailFormatter {

	public static void sendBlackCloudMail(String recipientName, final int itemObjectId, final int itemCount) {
		final MailTemplate template = DataManager.SYSTEM_MAIL_TEMPLATES.getMailTemplate("$$CASH_ITEM_MAIL", "", Race.PC_ALL);

		MailPart formatter = new MailPart() {

			@Override
			public String getParamValue(String name) {
				if ("itemid".equals(name)) {
					return Integer.toString(itemObjectId);
				}
				else if ("count".equals(name)) {
					return Integer.toString(itemCount);
				}
				else if ("unk1".equals(name)) {
					return "0";
				}
				else if ("purchasedate".equals(name)) {
					return Long.toString(System.currentTimeMillis() / 1000);
				}
				return "";
			}
		};

		String title = template.getFormattedTitle(formatter);
		String body = template.getFormattedMessage(formatter);

		SystemMailService.getInstance().sendMail("$$CASH_ITEM_MAIL", recipientName, title, body, itemObjectId, itemCount, 0, LetterType.BLACKCLOUD);
	}

	public static void sendHouseMaintenanceMail(final House ownedHouse, int warnCount, final long impoundTime) {
		String templateName = "";
		switch (warnCount) {
			case 1:
				templateName = "$$HS_OVERDUE_FIRST";
				break;
			case 2:
				templateName = "$$HS_OVERDUE_SECOND";
				break;
			case 3:
				templateName = "$$HS_OVERDUE_FINAL";
				break;
			default:
				return;
		}

		final MailTemplate template = DataManager.SYSTEM_MAIL_TEMPLATES.getMailTemplate(templateName, "", ownedHouse.getPlayerRace());

		MailPart formatter = new MailPart() {

			@Override
			public String getParamValue(String name) {
				if ("address".equals(name)) {
					return Integer.toString(ownedHouse.getAddress().getId());
				}
				else if ("datetime".equals(name)) {
					return Long.toString(impoundTime / 1000);
				}
				return "";
			}
		};

		String title = template.getFormattedTitle(null);
		String message = template.getFormattedMessage(formatter);

		SystemMailService.getInstance().sendMail(templateName, ownedHouse.getButler().getMasterName(), title, message, 0, 0, 0, LetterType.NORMAL);
	}

	public static void sendHouseAuctionMail(final House ownedHouse, final PlayerCommonData playerData, final AuctionResult result, final long time, long returnKinah) {
		final MailTemplate template = DataManager.SYSTEM_MAIL_TEMPLATES.getMailTemplate("$$HS_AUCTION_MAIL", "", playerData.getRace());
		if (ownedHouse == null || playerData == null || result == null) {
			return;
		}

		MailPart formatter = new MailPart() {

			@Override
			public String getParamValue(String name) {
				if ("address".equals(name)) {
					return Integer.toString(ownedHouse.getAddress().getId());
				}
				else if ("datetime".equals(name)) {
					return Long.toString(time / 1000);
				}
				else if ("resultid".equals(name)) {
					return Integer.toString(result.getId());
				}
				else if ("raceid".equals(name)) {
					return Integer.toString(playerData.getRace().getRaceId());
				}
				return "";
			}
		};

		String title = template.getFormattedTitle(formatter);
		String message = template.getFormattedMessage(formatter);

		SystemMailService.getInstance().sendMail("$$HS_AUCTION_MAIL", playerData.getName(), title, message, 0, 0, returnKinah, LetterType.NORMAL);
	}

	public static void sendAbyssRewardMail(final SiegeLocation siegeLocation, final PlayerCommonData playerData, final AbyssSiegeLevel level, final SiegeResult result, final long time, int attachedItemObjId, long attachedItemCount, long attachedKinahCount) {

		final MailTemplate template = DataManager.SYSTEM_MAIL_TEMPLATES.getMailTemplate("$$ABYSS_REWARD_MAIL", "", playerData.getRace());

		MailPart formatter = new MailPart() {

			@Override
			public String getParamValue(String name) {
				if ("siegelocid".equals(name)) {
					return Integer.toString(siegeLocation.getTemplate().getId());
				}
				else if ("datetime".equals(name)) {
					return Long.toString(time / 1000);
				}
				else if ("rankid".equals(name)) {
					return Integer.toString(level.getId());
				}
				else if ("raceid".equals(name)) {
					return Integer.toString(playerData.getRace().getRaceId());
				}
				else if ("resultid".equals(name)) {
					return Integer.toString(result.getId());
				}
				return "";
			}
		};

		String title = template.getFormattedTitle(formatter);
		String message = template.getFormattedMessage(formatter);

		SystemMailService.getInstance().sendMail("$$ABYSS_REWARD_MAIL", playerData.getName(), title, message, attachedItemObjId, attachedItemCount, attachedKinahCount, LetterType.NORMAL);
	}
}
