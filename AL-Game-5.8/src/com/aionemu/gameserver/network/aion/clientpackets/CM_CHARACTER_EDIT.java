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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerAppearanceDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerEnterWorldService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * In this packets aion client is requesting edit of character.
 *
 * @author IlBuono
 */
public class CM_CHARACTER_EDIT extends AionClientPacket {

	private int objectId;
	private boolean gender_change;
	private boolean check_ticket = true;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 *
	 * @param opcode
	 */
	public CM_CHARACTER_EDIT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		AionConnection client = getConnection();
		Account account = client.getAccount();
		objectId = readD();
		readB(52);
		if (account.getPlayerAccountData(objectId) == null) {
			return;
		}
		Player player = PlayerService.getPlayer(objectId, account);
		if (player == null) {
			return;
		}
		PlayerCommonData playerCommonData = player.getCommonData();
		PlayerAppearance playerAppearance = player.getPlayerAppearance();
		// Before modify appearance, we do a check of ticket
		int gender = readD();
		gender_change = playerCommonData.getGender().getGenderId() == gender ? false : true;
		if (!gender_change) {
			if (player.getInventory().getItemCountByItemId(169650000) == 0 && // Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650001) == 0 && // [Event] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650002) == 0 && // [Special] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650003) == 0 && // [Special] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650004) == 0 && // Plastic Surgery Ticket (60 mins)
				player.getInventory().getItemCountByItemId(169650005) == 0 && // [Event] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650006) == 0 && // [Event] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650007) == 0 && // [Event] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650008) == 0 && // Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650009) == 0 && // Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169650010) == 0 && // Plastic Surgery Ticket (60 mins)
				player.getInventory().getItemCountByItemId(169650011) == 0 && // [Stamp] Plastic Surgery Ticket
				player.getInventory().getItemCountByItemId(169691000) == 0) { // Plastic Surgery Ticket
				check_ticket = false;
				return;
			}
		}
		else {
			if (player.getInventory().getItemCountByItemId(169660000) == 0 && // Gender Switch Ticket
				player.getInventory().getItemCountByItemId(169660001) == 0 && // [Event] Gender Switch Ticket
				player.getInventory().getItemCountByItemId(169660002) == 0 && // Gender Switch Ticket (60 min)
				player.getInventory().getItemCountByItemId(169660003) == 0 && // [Event] Gender Switch Ticket
				player.getInventory().getItemCountByItemId(169660004) == 0 && // Gender Switch Ticket
				player.getInventory().getItemCountByItemId(169660005) == 0) { // Gender Switch Ticket
				check_ticket = false;
				return;
			}
		}
		playerCommonData.setGender(gender == 0 ? Gender.MALE : Gender.FEMALE);
		readD(); // race
		readD(); // player class
		playerAppearance.setVoice(readD());
		playerAppearance.setSkinRGB(readD());
		playerAppearance.setHairRGB(readD());
		playerAppearance.setEyeRGB(readD()); // TODO LEFT EYE
		playerAppearance.setLipRGB(readD());
		playerAppearance.setFace(readC());
		playerAppearance.setHair(readC());
		playerAppearance.setDeco(readC());
		playerAppearance.setTattoo(readC());
		playerAppearance.setFaceContour(readC());
		playerAppearance.setExpression(readC());
		playerAppearance.setPupilShape(readC());
		playerAppearance.setRemoveMane(readC());
		playerAppearance.setRightEyeRGB(readD());
		playerAppearance.setEyeLashShape(readC());
		readC();// UNK 6
		playerAppearance.setJawLine(readC());
		playerAppearance.setForehead(readC());
		playerAppearance.setEyeHeight(readC());
		playerAppearance.setEyeSpace(readC());
		playerAppearance.setEyeWidth(readC());
		playerAppearance.setEyeSize(readC());
		playerAppearance.setEyeShape(readC());
		playerAppearance.setEyeAngle(readC());
		playerAppearance.setBrowHeight(readC());
		playerAppearance.setBrowAngle(readC());
		playerAppearance.setBrowShape(readC());
		playerAppearance.setNose(readC());
		playerAppearance.setNoseBridge(readC());
		playerAppearance.setNoseWidth(readC());
		playerAppearance.setNoseTip(readC());
		playerAppearance.setCheek(readC());
		playerAppearance.setLipHeight(readC());
		playerAppearance.setMouthSize(readC());
		playerAppearance.setLipSize(readC());
		playerAppearance.setSmile(readC());
		playerAppearance.setLipShape(readC());
		playerAppearance.setJawHeigh(readC());
		playerAppearance.setChinJut(readC());
		playerAppearance.setEarShape(readC());
		playerAppearance.setHeadSize(readC());
		playerAppearance.setNeck(readC());
		playerAppearance.setNeckLength(readC());
		playerAppearance.setShoulderSize(readC());
		playerAppearance.setTorso(readC());
		playerAppearance.setChest(readC()); // only woman
		playerAppearance.setWaist(readC());
		playerAppearance.setHips(readC());
		playerAppearance.setArmThickness(readC());
		playerAppearance.setHandSize(readC());
		playerAppearance.setLegThickness(readC());
		playerAppearance.setFootSize(readC());
		playerAppearance.setFacialRate(readC());
		readC(); // Unk
		playerAppearance.setArmLength(readC());
		playerAppearance.setLegLength(readC());
		playerAppearance.setShoulders(readC());
		playerAppearance.setFaceShape(readC());
		playerAppearance.setPupilSize(readC());
		playerAppearance.setUpperTorso(readC());
		playerAppearance.setForeArmThickness(readC());
		playerAppearance.setHandSpan(readC());
		playerAppearance.setCalfThickness(readC());
		readC();// always 0 may be acessLevel
		readC();// always 0
		readC();// always 0
		playerAppearance.setHeight(readF());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		AionConnection client = getConnection();
		PlayerEnterWorldService.enterWorld(client, objectId);
		Player player = client.getActivePlayer();
		if (!check_ticket) {
			if (!gender_change) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EDIT_CHAR_ALL_CANT_NO_ITEM);
			}
			else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EDIT_CHAR_GENDER_CANT_NO_ITEM);
			}
		}
		else {
			// Remove ticket and save appearance
			if (!gender_change) {
				if (player.getInventory().getItemCountByItemId(169650000) > 0) { // Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650000, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650001) > 0) { // [Event] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650001, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650002) > 0) { // [Special] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650002, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650003) > 0) { // [Special] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650003, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650004) > 0) { // Plastic Surgery Ticket (60 mins)
					player.getInventory().decreaseByItemId(169650004, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650005) > 0) { // Plastic Surgery Ticket (60 mins)
					player.getInventory().decreaseByItemId(169650005, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650006) > 0) { // [Event] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650006, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650007) > 0) { // [Event] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650007, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169650008) > 0) { // Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650008, 1);
				} 
				else if (player.getInventory().getItemCountByItemId(169650009) > 0) { // Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650009, 1);
				} 
				else if (player.getInventory().getItemCountByItemId(169650010) > 0) { // Plastic Surgery Ticket (60 mins)
					player.getInventory().decreaseByItemId(169650010, 1);
				} 
				else if (player.getInventory().getItemCountByItemId(169650011) > 0) { // [Stamp] Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169650011, 1);
				} 
				else if (player.getInventory().getItemCountByItemId(169691000) > 0) { // Plastic Surgery Ticket
					player.getInventory().decreaseByItemId(169691000, 1);
				}
			}
			else {
				if (player.getInventory().getItemCountByItemId(169660000) > 0) { // Gender Switch Ticket
					player.getInventory().decreaseByItemId(169660000, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169660001) > 0) { // [Event] Gender Switch Ticket.
					player.getInventory().decreaseByItemId(169660001, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169660002) > 0) { // Gender Switch Ticket (60 min)
					player.getInventory().decreaseByItemId(169660002, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169660003) > 0) { // [Event] Gender Switch Ticket
					player.getInventory().decreaseByItemId(169660003, 1);
				}
				else if (player.getInventory().getItemCountByItemId(169660004) > 0) { // Gender Switch Ticket
					player.getInventory().decreaseByItemId(169660004, 1);
				} 
				else if (player.getInventory().getItemCountByItemId(169660005) > 0) { // Gender Switch Ticket
					player.getInventory().decreaseByItemId(169660005, 1);
				}
				DAOManager.getDAO(PlayerDAO.class).storePlayer(player); // save new gender
			}
			DAOManager.getDAO(PlayerAppearanceDAO.class).store(player); // save new appearance
		}
	}
}
