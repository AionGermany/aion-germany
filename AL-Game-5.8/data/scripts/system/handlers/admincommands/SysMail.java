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
package admincommands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author xTz
 */
public class SysMail extends AdminCommand {

	public SysMail() {
		super("sysmail");
	}

	enum RecipientType {

		ELYOS,
		ASMO,
		ALL,
		PLAYER;

		public boolean isAllowed(Race race) {
			switch (this) {
				case ELYOS:
					return race == Race.ELYOS;
				case ASMO:
					return race == Race.ASMODIANS;
				case ALL:
					return race == Race.ELYOS || race == Race.ASMODIANS;
				default:
					return false;
			}
		}
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 5) {
			onFail(admin, null);
			return;
		}

		String[] paramValues = new String[params.length];
		System.arraycopy(params, 0, paramValues, 0, params.length);

		RecipientType recipientType = null;
		String sender = null;
		String recipient = null;

		if (paramValues[0].startsWith("$$") || paramValues[0].startsWith("%")) {
			if (params.length < 6) {
				onFail(admin, null);
				return;
			}
			sender = paramValues[0];
			paramValues = new String[params.length - 1];
			System.arraycopy(params, 1, paramValues, 0, params.length - 1);
		}
		else {
			sender = "Admin";
		}

		if (paramValues[0].startsWith("@")) {
			if ("@all".startsWith(paramValues[0])) {
				recipientType = RecipientType.ALL;
			}
			else if ("@elyos".startsWith(paramValues[0])) {
				recipientType = RecipientType.ELYOS;
			}
			else if ("@asmodians".startsWith(paramValues[0])) {
				recipientType = RecipientType.ASMO;
			}
			else {
				PacketSendUtility.sendMessage(admin, "Recipient must be Player name, @all, @elyos or @asmodians.");
				return;
			}
		}
		else {
			recipientType = RecipientType.PLAYER;
			recipient = Util.convertName(paramValues[0]);
		}

		int item = 0, count = 0, kinah = 0;
		LetterType letterType;

		try {
			item = Integer.parseInt(paramValues[2]);
			count = Integer.parseInt(paramValues[3]);
			kinah = Integer.parseInt(paramValues[4]);
			letterType = LetterType.getLetterTypeById(Integer.parseInt(paramValues[1]));
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "<Regular|Blackcloud|Express> <Item|Count|Kinah> value must be an integer.");
			return;
		}

		if (letterType == LetterType.BLACKCLOUD) {
			sender = "$$CASH_ITEM_MAIL";
		}

		Boolean express = checkExpress(admin, item, count, kinah, recipient, recipientType, letterType);
		if (express == null) {
			return;
		}

		if (item <= 0) {
			item = 0;
		}

		if (count <= 0) {
			count = -1;
		}

		String title = "System Mail";
		String message = " ";

		if (paramValues.length > 5) {
			String[] words = new String[paramValues.length - 5];
			System.arraycopy(paramValues, 5, words, 0, words.length);
			String[] outText = new String[1];
			int wordCount = extractText(words, outText);
			if (wordCount > 0) {
				title = outText[0];
				String[] msgWords = new String[words.length - wordCount];
				System.arraycopy(words, wordCount, msgWords, 0, msgWords.length);
				wordCount = extractText(msgWords, outText);
				if (wordCount > 0) {
					message = outText[0];
				}
			}
		}

		if (recipientType == RecipientType.PLAYER) {
			if (letterType == LetterType.BLACKCLOUD) {
				MailFormatter.sendBlackCloudMail(recipient, item, count);
			}
			else {
				SystemMailService.getInstance().sendMail(sender, recipient, title, message, item, count, kinah, letterType);
			}
		}
		else {
			for (Player player : World.getInstance().getAllPlayers()) {
				if (recipientType.isAllowed(player.getRace())) {
					if (letterType == LetterType.BLACKCLOUD) {
						MailFormatter.sendBlackCloudMail(player.getName(), item, count);
					}
					else {
						SystemMailService.getInstance().sendMail(sender, player.getName(), title, message, item, count, kinah, letterType);
					}
				}
			}
		}

		if (item != 0) {
			PacketSendUtility.sendMessage(admin, "You send to " + recipientType + (recipientType == RecipientType.PLAYER ? " " + recipient : "") + "\n" + "[item:" + item + "] Count:" + count + " Kinah:" + kinah + "\n" + "Letter send successfully.");
		}
		else if (kinah > 0) {
			PacketSendUtility.sendMessage(admin, "You send to " + recipientType + (recipientType == RecipientType.PLAYER ? " " + recipient : "") + "\n" + " Kinah:" + kinah + "\n" + "Letter send successfully.");
		}
	}

	private int extractText(String[] words, String[] outText) {
		if (words.length == 0 || outText.length == 0) {
			return 0;
		}

		if (!words[0].startsWith("|")) {
			return 0;
		}

		int wordCount = 1;

		String enclosedText = words[0].substring(1);
		if (enclosedText.endsWith("|")) {
			outText[0] = enclosedText.substring(0, enclosedText.length() - 1);
		}
		else {
			List<String> titleWords = new ArrayList<String>();
			titleWords.add(enclosedText);
			for (; wordCount < words.length; wordCount++) {
				String word = words[wordCount];
				if (word.endsWith("|")) {
					word = word.substring(0, word.length() - 1);
					titleWords.add(word);
					wordCount++;
					break;
				}
				else {
					titleWords.add(word);
				}
			}

			outText[0] = StringUtils.join(titleWords.toArray(new String[0]), ' ');
		}

		return wordCount;
	}

	private static Boolean checkExpress(Player admin, int item, int count, int kinah, String recipient, RecipientType recipientType, LetterType letterType) {
		Boolean shouldExpress = null;

		if (recipientType == null) {
			PacketSendUtility.sendMessage(admin, "Please insert Recipient Type.\n" + "Recipient = player, @all, @elyos or @asmodians");
			return null;
		}
		else if (recipientType == RecipientType.PLAYER) {
			if (letterType == LetterType.NORMAL) {
				if (!DAOManager.getDAO(PlayerDAO.class).isNameUsed(recipient)) {
					PacketSendUtility.sendMessage(admin, "Could not find a Recipient by that name.");
					return null;
				}
				shouldExpress = false;
			}
			else if (letterType == LetterType.EXPRESS) {
				if (World.getInstance().findPlayer(recipient) == null) {
					PacketSendUtility.sendMessage(admin, "This Recipient is offline.");
					return null;
				}
				shouldExpress = true;
			}
			else { // Black cloud
				shouldExpress = World.getInstance().findPlayer(recipient) != null;
			}
		}
		else {
			shouldExpress = letterType != LetterType.NORMAL;
		}

		if (item == 0 && count != 0) {
			PacketSendUtility.sendMessage(admin, "Please insert Item Id..");
			return null;
		}

		if (count == 0 && item != 0) {
			PacketSendUtility.sendMessage(admin, "Please insert Item Count.");
			return null;
		}

		if (count <= 0 && item <= 0 && kinah <= 0) {
			PacketSendUtility.sendMessage(admin, "Parameters <Item> <Count> <Kinah> are icorrect.");
			return null;
		}

		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(item);
		if (item != 0) {
			if (itemTemplate == null) {
				PacketSendUtility.sendMessage(admin, "Item id is incorrect: " + item);
				return null;
			}
			long maxStackCount = itemTemplate.getMaxStackCount();
			if (count > maxStackCount && maxStackCount != 0) {
				PacketSendUtility.sendMessage(admin, "Please insert correct Item Count.");
				return null;
			}
		}

		if (kinah < 0) {
			PacketSendUtility.sendMessage(admin, "Kinah value must be >= 0.");
			return null;
		}
		else if (kinah > 0 && letterType == LetterType.BLACKCLOUD) {
			PacketSendUtility.sendMessage(admin, "Kinah attachment are not for black cloud letters!");
			return null;
		}
		return shouldExpress;
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "No parameters detected.\n" + "Please use //sysmail [%|$$<Sender>] <Recipient> <Regular|Blackcloud|Express> <Item> <Count> <Kinah> [|Title|] [|Message|]\n" + "Sender name must start with % or $$. Can be ommitted.\n" + "Regular mail type is 0, Express mail type is 1, Blackcloud type is 2.\n" + "If parameters (Item, Count) = 0 than the item will not be send\n" + "If parameters (Kinah) = 0 not send Kinah\n" + "Recipient = Player name, @all, @elyos or @asmodians\n" + "Optional Title and Message must be enclosed within pipe chars");
	}
}
