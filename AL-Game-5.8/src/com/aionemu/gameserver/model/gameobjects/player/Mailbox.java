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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.services.mail.MailService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author kosyachok
 * @author Atracer
 * @author Antraxx
 */
public class Mailbox {

	private Map<Integer, Letter> mails = new FastMap<Integer, Letter>().shared();
	private Map<Integer, Letter> reserveMail = new FastMap<Integer, Letter>().shared();
	private Player owner;
	public boolean isMailListUpdateRequired;
	// 0x00 - closed
	// 0x01 - regular
	// 0x02 - express
	public byte mailBoxState = 0;

	public Mailbox(Player player) {
		this.owner = player;
	}

	/**
	 * @param letter
	 */
	public void putLetterToMailbox(Letter letter) {
		if (haveFreeSlots()) {
			mails.put(letter.getObjectId(), letter);
		}
		else {
			reserveMail.put(letter.getObjectId(), letter);
		}
	}

	/**
	 * Get all letters in mailbox (sorted according to time received)
	 *
	 * @return
	 */
	public Collection<Letter> getLetters() {
		SortedSet<Letter> letters = new TreeSet<Letter>(new Comparator<Letter>() {

			@Override
			public int compare(Letter o1, Letter o2) {
				if (o1.getTimeStamp().getTime() > o2.getTimeStamp().getTime()) {
					return -1;
				}
				if (o1.getTimeStamp().getTime() < o2.getTimeStamp().getTime()) {
					return 1;
				}
				return o1.getObjectId() > o2.getObjectId() ? 1 : -1;
			}
		});

		for (Letter letter : mails.values()) {
			letters.add(letter);
		}
		return letters;
	}

	/**
	 * Get system letters which senders start with the string specified and were received since the last player login
	 *
	 * @param substring
	 *            must start with special characters: % or $$
	 * @return new list of letters
	 */
	public List<Letter> getNewSystemLetters(String substring) {
		List<Letter> letters = new ArrayList<Letter>();
		for (Letter letter : mails.values()) {
			if (letter.getSenderName() == null || !letter.isUnread()) {
				continue;
			}
			if (owner.getCommonData().getLastOnline().getTime() > letter.getTimeStamp().getTime()) {
				continue;
			}
			if (letter.getSenderName().startsWith("%") || letter.getSenderName().startsWith("$$")) {
				if (letter.getSenderName().startsWith(substring)) {
					letters.add(letter);
				}
			}
		}
		return letters;
	}

	/**
	 * Get letter with specified letter id
	 *
	 * @param letterObjId
	 * @return
	 */
	public Letter getLetterFromMailbox(int letterObjId) {
		return mails.get(letterObjId);
	}

	/**
	 * Check whether mailbox contains empty letters
	 *
	 * @return
	 */
	public boolean haveUnread() {
		for (Letter letter : mails.values()) {
			if (letter.isUnread()) {
				return true;
			}
		}
		return false;
	}

	public final int getUnreadCount() {
		int unreadCount = 0;
		for (Letter letter : mails.values()) {
			if (letter.isUnread()) {
				unreadCount++;
			}
		}
		return unreadCount;
	}

	public boolean haveUnreadByType(LetterType letterType) {
		for (Letter letter : mails.values()) {
			if (letter.isUnread() && letter.getLetterType() == letterType) {
				return true;
			}
		}
		return false;
	}

	public final int getUnreadCountByType(LetterType letterType) {
		int count = 0;
		for (Letter letter : mails.values()) {
			if (letter.isUnread() && letter.getLetterType() == letterType) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return
	 */
	public boolean haveFreeSlots() {
		return mails.size() < 100;
	}

	/**
	 * @param letterId
	 */
	public void removeLetter(int letterId) {
		mails.remove(letterId);
		uploadReserveLetters();
	}

	/**
	 * Current size of mailbox
	 *
	 * @return
	 */
	public int size() {
		return mails.size();
	}

	public void uploadReserveLetters() {
		if (reserveMail.size() > 0 && haveFreeSlots()) {
			for (Letter letter : reserveMail.values()) {
				if (haveFreeSlots()) {
					mails.put(letter.getObjectId(), letter);
					reserveMail.remove(letter.getObjectId());
				}
				else {
					break;
				}
			}
			MailService.getInstance().refreshMail(getOwner());
		}
	}

	public void sendMailList(boolean expressOnly) {
		PacketSendUtility.sendPacket(owner, new SM_MAIL_SERVICE(owner, getLetters(), expressOnly));
	}

	public Player getOwner() {
		return owner;
	}
}
