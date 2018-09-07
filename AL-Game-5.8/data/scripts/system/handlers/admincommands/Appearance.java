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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerAppearanceDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayersAppearanceData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.appearances.PlayerApp;
import com.aionemu.gameserver.model.templates.appearances.PlayerAppearanceTemplate;
import com.aionemu.gameserver.model.templates.appearances.PlayerItem;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

import javolution.util.FastList;

/**
 * @author Divinity / CoolyT
 */
public class Appearance extends AdminCommand {

	public Appearance() {
		super("appearance");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			onFail(admin, null);
			return;
		}
		PlayersAppearanceData appData = DataManager.PLAYER_APPEARANCE_DATA;
		FastList<PlayerApp> apps = PlayersAppearanceData.getApp();
		VisibleObject target = admin.getTarget();
		Player player;

		if (target == null) {
			player = admin;
		}
		else {
			player = (Player) target;
		}

		if (params[0].equals("list")) {
			for (int i = 0; i <= apps.size() - 1; i++) {
				PlayerApp a = apps.get(i);
				PacketSendUtility.sendMessage(player, "[" + i + ".] " + a.name + " (" + a.gender + "-" + a.race + "-" + a.playerClass + "-Lv. " + a.level + ")");
			}
			PacketSendUtility.sendMessage(player, "-----------------------------------------------------------");

			return;
		}

		if (params[0].equals("save")) {
			if (DAOManager.getDAO(PlayerAppearanceDAO.class).store(player))
				PacketSendUtility.sendMessage(player, "Sucessfully saved new Appearance Data");
			else
				PacketSendUtility.sendMessage(player, "Something went wrong on saving Appearance Data");

			return;
		}

		else if (params[0].equals("get")) {
			// Get the current player's appearance
			PlayerAppearance playerAppearance = player.getPlayerAppearance();
			PlayerApp a = new PlayerApp();
			// Save a clean player's appearance
			if (player.getSavedPlayerAppearance() == null) {
				player.setSavedPlayerAppearance((PlayerAppearance) playerAppearance.clone());
			}

			int index = (Integer) Integer.parseInt(params[1]) != null ? Integer.parseInt(params[1]) : 0;
			if (index <= 0)
				a = appData.getAppearanceByName(params[1].toLowerCase());
			else
				a = apps.get(index);
			PlayerAppearanceTemplate app = a.appearance;
			FastList<PlayerItem> items = a.items;
			Equipment equip = player.getEquipment();
			for (PlayerItem item : items) {
				ItemTemplate it = DataManager.ITEM_DATA.getItemTemplate(item.itemTemplateId);
				Storage inv = player.getInventory();
				if (it.getItemSlot() > 0 && inv.getFreeSlots() >= 1) {
					ItemService.addItem(player, item.itemTemplateId, 1);

					Item _item = inv.getFirstItemByItemId(item.itemTemplateId);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_ITEM_SUCCEED(_item.getNameId()));
					_item.setSoulBound(true);
					ItemPacketService.updateItemAfterInfoChange(player, _item);
					equip.equipItem(_item.getObjectId(), it.getItemSlot());

				}
			}

			playerAppearance.setSkinRGB(app.skinRGB);
			playerAppearance.setHairRGB(app.hairRGB);
			playerAppearance.setLipRGB(app.lipRGB);
			playerAppearance.setEyeRGB(app.eyeRGB);
			playerAppearance.setFace(app.face);
			playerAppearance.setHair(app.hairShape);
			playerAppearance.setDeco(app.deco);
			playerAppearance.setTattoo(app.tattoo);
			playerAppearance.setFaceContour(app.faceContour);
			playerAppearance.setExpression(app.expresion);
			// playerAppearance.setunk_0x06(app.unk1); //0x06
			playerAppearance.setJawLine(app.jawLine);
			playerAppearance.setForehead(app.foreHead);
			playerAppearance.setEyeHeight(app.eyeHeight);
			playerAppearance.setEyeSpace(app.eyeSpace);
			playerAppearance.setEyeWidth(app.eyeWidth);
			playerAppearance.setEyeSize(app.eyeSize);
			playerAppearance.setEyeShape(app.eyeShape);
			playerAppearance.setEyeAngle(app.eyeAngle);
			playerAppearance.setBrowHeight(app.browHeight);
			playerAppearance.setBrowAngle(app.browAngle);
			playerAppearance.setBrowAngle(app.browShape);
			playerAppearance.setNose(app.nose);
			playerAppearance.setNoseBridge(app.noseBridge);
			playerAppearance.setNoseWidth(app.noseWidth);
			playerAppearance.setNoseTip(app.noseTip);
			playerAppearance.setCheek(app.cheek);
			playerAppearance.setLipHeight(app.lipHeight);
			playerAppearance.setMouthSize(app.mouthSize);
			playerAppearance.setLipSize(app.lipSize);
			playerAppearance.setSmile(app.smile);
			playerAppearance.setLipShape(app.lipShape);
			playerAppearance.setJawHeigh(app.ChinHeight);
			playerAppearance.setChinJut(app.CheckBones);
			playerAppearance.setEarShape(app.earShape);
			playerAppearance.setHeadSize(app.headSize);
			playerAppearance.setNeck(app.neck);
			playerAppearance.setNeckLength(app.neckLength);
			playerAppearance.setShoulderSize(app.shoulderSize);
			playerAppearance.setTorso(app.torso);
			playerAppearance.setChest(app.chest);
			playerAppearance.setWaist(app.waist);
			playerAppearance.setHips(app.hips);
			playerAppearance.setArmThickness(app.armThickness);
			playerAppearance.setHandSize(app.handSize);
			playerAppearance.setLegThickness(app.legThickness);
			playerAppearance.setFootSize(app.footSize);
			playerAppearance.setFacialRate(app.facialRatio);
			// playerAppearance.setunk_0x00(app.unk2);
			playerAppearance.setArmLength(app.armLength);
			playerAppearance.setLegLength(app.legLength);
			playerAppearance.setShoulders(app.shoulders);
			playerAppearance.setFaceShape(app.faceShape);
			playerAppearance.setHeight(app.height);

			player.setPlayerAppearance(playerAppearance);
		}

		else if (params[0].equals("reset")) {
			PlayerAppearance savedPlayerAppearance = player.getSavedPlayerAppearance();

			if (savedPlayerAppearance == null) {
				PacketSendUtility.sendMessage(admin, "The target has already the normal appearance.");
				return;
			}

			// Edit the current player's appearance with the saved player's appearance
			player.setPlayerAppearance(savedPlayerAppearance);

			// See line 44
			player.setSavedPlayerAppearance(null);

			// Warn the player
			PacketSendUtility.sendMessage(player, "An admin has resetted your appearance.");

			// Send update packets
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading());

			return;
		}
		else

		if (params.length < 1) {
			onFail(player, null);
			return;
		}

		// Warn the player
		PacketSendUtility.sendMessage(player, "An admin has changed your appearance.");

		// Send update packets
		TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading());
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //appearance <list | get <Number> | reset | save>";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
