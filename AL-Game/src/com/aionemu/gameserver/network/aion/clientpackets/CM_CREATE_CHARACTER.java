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

import java.sql.Timestamp;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerBonusTimeStatus;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import com.aionemu.gameserver.services.AccountService;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

/**
 * In this packets aion client is requesting creation of character.
 *
 * @author -Nemesiss-
 * @modified cura
 * @rework FrozenKiller
 */
public class CM_CREATE_CHARACTER extends AionClientPacket {

	/**
	 * Character appearance
	 */
	private PlayerAppearance playerAppearance;
	/**
	 * Player base data
	 */
	private PlayerCommonData playerCommonData;
	private String charName; 
	private int raceValue;
	private int genderValue;
	private int classId;
	private int voice;
	private int skinRGB;
	private int hairRGB;
	private int eyeRGB;
	private int lipRGB;
	private int face;
	private int hair;
	private int deco;
	private int tattoo;
	private int faceContour;
	private int expression;
	private int pupilShape;
	private int removeMane;
	private int rightEyeRGB;
	private int eyeLashShape;
	private int jawLine;
	private int forehead;
	private int eyeHeight;
	private int eyeSpace;
	private int eyeWidth;
	private int eyeSize;
	private int eyeShape;
	private int eyeAngle;
	private int browHeight;
	private int browAngle;
	private int browShape;
	private int nose;
	private int noseBridge;
	private int noseWidth;
	private int noseTip;
	private int cheek;
	private int lipHeight;
	private int mouthSize;
	private int lipSize;
	private int smile;
	private int lipShape;
	private int jawHeigh;
	private int chinJut;
	private int earShape;
	private int headSize;
	private int neck;
	private int neckLength;
	private int shoulderSize;
	private int torso;
	private int chest;
	private int waist;
	private int hips;
	private int armThickness;
	private int handSize;
	private int legThickness;
	private int footSize;
	private int facialRate;
	private int armLength;
	private int legLength;
	private int shoulders;
	private int faceShape;
	private int pupilSize;
	private int upperTorso;
	private int foreArmThickness;
	private int handSpan;
	private int calfThickness;
	private float height;
	private boolean isCreate = false;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 *
	 * @param opcode
	 */
	public CM_CREATE_CHARACTER(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		readD(); // AccountID
		readS(); // Account Name
		charName = Util.convertName(readS());
		readB(52 - (charName.length() * 2 + 2)); // some shit? 2.5.x
		genderValue = readD();
		raceValue = readD();
		classId = readD();
		voice = readD();
		skinRGB = readD();
		hairRGB = readD();
		eyeRGB = readD();
		lipRGB = readD();
		face = readC();
		hair = readC();
		deco = readC();
		tattoo = readC();
		faceContour = readC();
		expression = readC();
		pupilShape = readC();
		removeMane = readC();
		rightEyeRGB = readD();
		eyeLashShape = readC();
		readC(); 
		jawLine = readC();
		forehead = readC();
		eyeHeight = readC();
		eyeSpace = readC();
		eyeWidth = readC();
		eyeSize = readC();
		eyeShape = readC();
		eyeAngle = readC();
		browHeight = readC();
		browAngle = readC();
		browShape = readC();
		nose = readC();
		noseBridge = readC();
		noseWidth = readC();
		noseTip = readC();
		cheek = readC();
		lipHeight = readC();
		mouthSize = readC();
		lipSize = readC();
		smile = readC();
		lipShape = readC();
		jawHeigh = readC();
		chinJut = readC();
		earShape = readC();
		headSize = readC();
		neck = readC();
		neckLength = readC();
		shoulderSize = readC();
		torso = readC();
		chest = readC();
		waist = readC();
		hips = readC();
		armThickness = readC();
		handSize = readC();
		legThickness = readC();
		footSize = readC();
		facialRate = readC();
		readC();
		armLength = readC();
		legLength = readC();
		shoulders = readC();
		faceShape = readC();
		pupilSize = readC();
		upperTorso = readC();
		foreArmThickness = readC();
		handSpan = readC();
		calfThickness = readC();
		readC();
		readC();
		readC();
		height = readF();
		isCreate = (readC() == 0 ? false : true);
	}

	/**
	 * Actually does the dirty job
	 */
	@Override
	protected void runImpl() {
		AionConnection client = getConnection();

		Account account = client.getAccount();
		
		playerCommonData = new PlayerCommonData(IDFactory.getInstance().nextId());

		/* Some reasons why player can' be created */
		if (client.getActivePlayer() != null) {
			return;
		}
		if (isCreate) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_CREATE_CHAR));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		playerCommonData.setName(charName);
		playerCommonData.setLevel(GSConfig.STARTING_LEVEL);
		playerCommonData.setPlayerClass(PlayerClass.getPlayerClassById((byte) classId));
		switch (genderValue) {
			case 0: {
				playerCommonData.setGender(Gender.MALE);
				break;
			}
			case 1: {
				playerCommonData.setGender(Gender.FEMALE);
				break;
			}
			case 8: {
				playerCommonData.setGender(Gender.DUMMY);
				break;
			}
		}
		switch (raceValue) {
			case 0: {
				playerCommonData.setRace(Race.ELYOS);
				break;
			}
			case 1: {
				playerCommonData.setRace(Race.ASMODIANS);
				break;
			}
			case 8: {
				playerCommonData.setRace(Race.NAGA);
				break;
			}
		}

		if (getConnection().getAccount().getMembership() >= MembershipConfig.STIGMA_SLOT_QUEST) {
			playerCommonData.setAdvancedStigmaSlotSize(11);
		}
		
		playerAppearance = new PlayerAppearance();
		playerAppearance.setVoice(voice);
		playerAppearance.setSkinRGB(skinRGB);
		playerAppearance.setHairRGB(hairRGB);
		playerAppearance.setEyeRGB(eyeRGB); // TODO LeftEyeRGB
		playerAppearance.setLipRGB(lipRGB);
		playerAppearance.setFace(face);
		playerAppearance.setHair(hair);
		playerAppearance.setDeco(deco);
		playerAppearance.setTattoo(tattoo);
		playerAppearance.setFaceContour(faceContour);
		playerAppearance.setExpression(expression);
		playerAppearance.setPupilShape(pupilShape);
		playerAppearance.setRemoveMane(removeMane);
		playerAppearance.setRightEyeRGB(rightEyeRGB);
		playerAppearance.setEyeLashShape(eyeLashShape);
		playerAppearance.setJawLine(jawLine);
		playerAppearance.setForehead(forehead);
		playerAppearance.setEyeHeight(eyeHeight);
		playerAppearance.setEyeSpace(eyeSpace);
		playerAppearance.setEyeWidth(eyeWidth);
		playerAppearance.setEyeSize(eyeSize);
		playerAppearance.setEyeShape(eyeShape);
		playerAppearance.setEyeAngle(eyeAngle);
		playerAppearance.setBrowHeight(browHeight);
		playerAppearance.setBrowAngle(browAngle);
		playerAppearance.setBrowShape(browShape);
		playerAppearance.setNose(nose);
		playerAppearance.setNoseBridge(noseBridge);
		playerAppearance.setNoseWidth(noseWidth);
		playerAppearance.setNoseTip(noseTip);
		playerAppearance.setCheek(cheek);
		playerAppearance.setLipHeight(lipHeight);
		playerAppearance.setMouthSize(mouthSize);
		playerAppearance.setLipSize(lipSize);
		playerAppearance.setSmile(smile);
		playerAppearance.setLipShape(lipShape);
		playerAppearance.setJawHeigh(jawHeigh);
		playerAppearance.setChinJut(chinJut);
		playerAppearance.setEarShape(earShape);
		playerAppearance.setHeadSize(headSize);
		playerAppearance.setNeck(neck);
		playerAppearance.setNeckLength(neckLength);
		playerAppearance.setShoulderSize(shoulderSize);
		playerAppearance.setTorso(torso);
		playerAppearance.setChest(chest); // only woman
		playerAppearance.setWaist(waist);
		playerAppearance.setHips(hips);
		playerAppearance.setArmThickness(armThickness);
		playerAppearance.setHandSize(handSize);
		playerAppearance.setLegThickness(legThickness);
		playerAppearance.setFootSize(footSize);
		playerAppearance.setFacialRate(facialRate);
		playerAppearance.setArmLength(armLength);
		playerAppearance.setLegLength(legLength);
		playerAppearance.setShoulders(shoulders);
		playerAppearance.setFaceShape(faceShape);
		playerAppearance.setPupilSize(pupilSize);
		playerAppearance.setUpperTorso(upperTorso);
		playerAppearance.setForeArmThickness(foreArmThickness);
		playerAppearance.setHandSpan(handSpan);
		playerAppearance.setCalfThickness(calfThickness);
		playerAppearance.setHeight(height);
		
		if (account.getMembership() >= MembershipConfig.CHARACTER_ADDITIONAL_ENABLE) {
			if (MembershipConfig.CHARACTER_ADDITIONAL_COUNT <= account.size()) {
				client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_SERVER_LIMIT_EXCEEDED));
				IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
				return;
			}
		}
		
		if (GSConfig.CHARACTER_LIMIT_COUNT <= account.size()) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_SERVER_LIMIT_EXCEEDED));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (!PlayerService.isFreeName(charName)) {
			if (GSConfig.CHARACTER_CREATION_MODE == 2) {
				client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_RESERVED));
			}
			else {
				client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
			}
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (PlayerService.isOldName(playerCommonData.getName())) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (!NameRestrictionService.isValidName(charName)) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (NameRestrictionService.isForbiddenWord(charName)) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_FORBIDDEN_CHAR_NAME));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (!playerCommonData.getPlayerClass().isStartingClass()) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		
		if (GSConfig.CHARACTER_CREATION_MODE == 0) {
			for (PlayerAccountData data : account.getSortedAccountsList()) {
				if (data.getPlayerCommonData().getRace() != playerCommonData.getRace()) {
					client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_OTHER_RACE));
					IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
					return;
				}
			}
		}
		AccountService.removeDeletedCharacters(account);
		playerCommonData.setBonusType(PlayerBonusTimeStatus.NEW);
		Player player = PlayerService.newPlayer(playerCommonData, playerAppearance, account);

		if (!PlayerService.storeNewPlayer(player, account.getName(), account.getId())) {
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_DB_ERROR));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
		} else {
			List<Item> equipment = DAOManager.getDAO(InventoryDAO.class).loadEquipment(player.getObjectId());
			PlayerAccountData accPlData = new PlayerAccountData(playerCommonData, null, playerAppearance, equipment, null);
			accPlData.setCreationDate(new Timestamp(System.currentTimeMillis()));
			PlayerService.storeCreationTime(player.getObjectId(), accPlData.getCreationDate());
			playerCommonData.setCreationDate(accPlData.getCreationDate());
			account.addPlayerAccountData(accPlData);
			client.sendPacket(new SM_CREATE_CHARACTER(accPlData, SM_CREATE_CHARACTER.RESPONSE_OK));
		}
	}
}