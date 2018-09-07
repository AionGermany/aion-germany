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
package com.aionemu.gameserver.network.loginserver.serverpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.MacroList;
import com.aionemu.gameserver.model.gameobjects.player.PetCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerSettings;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;
import com.aionemu.gameserver.model.gameobjects.player.RecipeList;
import com.aionemu.gameserver.model.gameobjects.player.emotion.Emotion;
import com.aionemu.gameserver.model.gameobjects.player.emotion.EmotionList;
import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.model.gameobjects.player.motion.MotionList;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFaction;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFactions;
import com.aionemu.gameserver.model.gameobjects.player.title.Title;
import com.aionemu.gameserver.model.gameobjects.player.title.TitleList;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.skill.PlayerSkillList;
import com.aionemu.gameserver.network.loginserver.LoginServerConnection;
import com.aionemu.gameserver.network.loginserver.LsServerPacket;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.transfers.TransferablePlayer;

import javolution.util.FastList;

/**
 * @author KID
 */
public class SM_PTRANSFER_CONTROL extends LsServerPacket {

	private final Logger log = LoggerFactory.getLogger(SM_PTRANSFER_CONTROL.class);
	public static final byte CHARACTER_INFORMATION = 1;
	public static final byte ERROR = 2;
	public static final byte OK = 3;
	public static final byte TASK_STOP = 4;
	private byte type;
	private Player player;
	private String result;
	private int taskId;

	public SM_PTRANSFER_CONTROL(byte type, int taskId) {
		super(14);
		this.type = type;
		this.taskId = taskId;
	}

	public SM_PTRANSFER_CONTROL(byte type, TransferablePlayer tp) {
		super(14);
		this.type = type;
		this.taskId = tp.taskId;
		this.player = tp.player;
	}

	public SM_PTRANSFER_CONTROL(byte type, TransferablePlayer tp, String result) {
		super(14);
		this.type = type;
		this.result = result;
	}

	public SM_PTRANSFER_CONTROL(byte type, int taskId, String result) {
		super(14);
		this.type = type;
		this.taskId = taskId;
		this.result = result;
	}

	@Override
	protected void writeImpl(LoginServerConnection con) {
		writeC(type);
		switch (type) {
			case OK:
				writeD(taskId);
				break;
			case ERROR:
				writeD(taskId);
				writeS(result);
				break;
			case TASK_STOP:
				writeD(taskId);
				writeS(result);
				break;
			case CHARACTER_INFORMATION: {
				writeD(taskId);
				writeS(player.getName());
				writeQ(player.getCommonData().getExp());
				writeD(player.getPlayerClass().getClassId());
				writeD(player.getRace().getRaceId());
				writeD(player.getCommonData().getGender().getGenderId());
				writeD(player.getCommonData().getTitleId());
				writeD(player.getCommonData().getDp());
				writeD(player.getCommonData().getCubeExpands());
				writeD(player.getCommonData().getAdvancedStigmaSlotSize());
				writeD(player.getCommonData().getWarehouseSize());

				PlayerAppearance playerAppearance = player.getPlayerAppearance();
				writeD(playerAppearance.getSkinRGB());
				writeD(playerAppearance.getHairRGB());
				writeD(playerAppearance.getEyeRGB()); // TODO LEFT EYE
				writeD(playerAppearance.getLipRGB());
				writeC(playerAppearance.getFace());
				writeC(playerAppearance.getHair());
				writeC(playerAppearance.getDeco());
				writeC(playerAppearance.getTattoo());
				writeC(playerAppearance.getFaceContour());
				writeC(playerAppearance.getExpression());
				writeC(playerAppearance.getPupilShape());
				writeC(playerAppearance.getRemoveMane());
				writeD(playerAppearance.getRightEyeRGB());
				writeC(playerAppearance.getEyeLashShape());
				if (player.getGender() == Gender.FEMALE) {
					writeC(6);
				}
				else {
					writeC(5);
				}
				writeC(playerAppearance.getJawLine());
				writeC(playerAppearance.getForehead());
				writeC(playerAppearance.getEyeHeight());
				writeC(playerAppearance.getEyeSpace());
				writeC(playerAppearance.getEyeWidth());
				writeC(playerAppearance.getEyeSize());
				writeC(playerAppearance.getEyeShape());
				writeC(playerAppearance.getEyeAngle());
				writeC(playerAppearance.getBrowHeight());
				writeC(playerAppearance.getBrowAngle());
				writeC(playerAppearance.getBrowShape());
				writeC(playerAppearance.getNose());
				writeC(playerAppearance.getNoseBridge());
				writeC(playerAppearance.getNoseWidth());
				writeC(playerAppearance.getNoseTip());
				writeC(playerAppearance.getCheek());
				writeC(playerAppearance.getLipHeight());
				writeC(playerAppearance.getMouthSize());
				writeC(playerAppearance.getLipSize());
				writeC(playerAppearance.getSmile());
				writeC(playerAppearance.getLipShape());
				writeC(playerAppearance.getJawHeigh());
				writeC(playerAppearance.getChinJut());
				writeC(playerAppearance.getEarShape());
				writeC(playerAppearance.getHeadSize());
				// 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize

				writeC(playerAppearance.getNeck());
				writeC(playerAppearance.getNeckLength());
				writeC(playerAppearance.getShoulderSize()); // shoulderSize
				writeC(playerAppearance.getTorso());
				writeC(playerAppearance.getChest());
				writeC(playerAppearance.getWaist());
				writeC(playerAppearance.getHips());
				writeC(playerAppearance.getArmThickness());
				writeC(playerAppearance.getHandSize());
				writeC(playerAppearance.getLegThickness());
				writeC(playerAppearance.getFootSize());
				writeC(playerAppearance.getFacialRate());
				writeC(0);// unk;
				writeC(playerAppearance.getArmLength()); // armLength
				writeC(playerAppearance.getLegLength()); // legLength
				writeC(playerAppearance.getShoulders());
				writeC(playerAppearance.getFaceShape());
				writeC(playerAppearance.getPupilSize());
				writeC(playerAppearance.getUpperTorso());
				writeC(playerAppearance.getForeArmThickness());
				writeC(playerAppearance.getHandSpan());
				writeC(playerAppearance.getCalfThickness());
				writeC(playerAppearance.getVoice());
				writeF(playerAppearance.getHeight());

				writeF(player.getX());
				writeF(player.getY());
				writeF(player.getZ());
				writeC(player.getHeading());
				writeD(player.getWorldId());

				// inventory
				List<Item> inv = DAOManager.getDAO(InventoryDAO.class).loadStorageDirect(player.getObjectId(), StorageType.CUBE);
				writeD(inv.size());
				ItemService.loadItemStones(inv);
				for (Item item : inv) {
					writeD(item.getObjectId());
					writeD(item.getItemId());
					writeQ(item.getItemCount());
					writeD(item.getItemColor());
					writeS(item.getItemCreator());
					writeD(item.getExpireTime());
					writeD(item.getActivationCount());
					writeD(item.isEquipped() ? 1 : 0);
					writeD(item.isSoulBound() ? 1 : 0);
					writeQ(item.getEquipmentSlot());
					writeD(item.getItemLocation());
					writeD(item.getEnchantLevel());
					writeD(item.getItemSkinTemplate().getTemplateId());
					writeD(item.getFusionedItemId());
					writeD(item.getOptionalSocket());
					writeD(item.getOptionalFusionSocket());
					writeD(item.getChargePoints());
					Set<ManaStone> itemStones = item.getItemStones();
					writeD(itemStones.size());
					for (ManaStone stone : itemStones) {
						writeD(stone.getItemId());
						writeD(stone.getSlot());
					}
					itemStones = item.getFusionStones();
					writeD(itemStones.size());
					for (ManaStone stone : itemStones) {
						writeD(stone.getItemId());
						writeD(stone.getSlot());
					}
					GodStone stone = item.getGodStone();
					writeC(stone == null ? 0 : 1);
					if (stone != null) {
						writeD(stone.getItemId());
					}
					writeD(item.getColorExpireTime());
					writeD(item.getBonusNumber());
				}

				inv = DAOManager.getDAO(InventoryDAO.class).loadStorageDirect(player.getObjectId(), StorageType.REGULAR_WAREHOUSE);
				ItemService.loadItemStones(inv);
				writeD(inv.size());
				for (Item item : inv) {
					writeD(item.getObjectId());
					writeD(item.getItemId());
					writeQ(item.getItemCount());
					writeD(item.getItemColor());
					writeS(item.getItemCreator());
					writeD(item.getExpireTime());
					writeD(item.getActivationCount());
					writeD(item.isEquipped() ? 1 : 0);
					writeD(item.isSoulBound() ? 1 : 0);
					writeQ(item.getEquipmentSlot());
					writeD(item.getItemLocation());
					writeD(item.getEnchantLevel());
					writeD(item.getItemSkinTemplate().getTemplateId());
					writeD(item.getFusionedItemId());
					writeD(item.getOptionalSocket());
					writeD(item.getOptionalFusionSocket());
					writeD(item.getChargePoints());
					Set<ManaStone> itemStones = item.getItemStones();
					writeD(itemStones.size());
					for (ManaStone stone : itemStones) {
						writeD(stone.getItemId());
						writeD(stone.getSlot());
					}
					itemStones = item.getFusionStones();
					writeD(itemStones.size());
					for (ManaStone stone : itemStones) {
						writeD(stone.getItemId());
						writeD(stone.getSlot());
					}
					GodStone stone = item.getGodStone();
					writeC(stone == null ? 0 : 1);
					if (stone != null) {
						writeD(stone.getItemId());
					}
				}

				EmotionList emo = player.getEmotions();
				writeD(emo.getEmotions().size());
				for (Emotion e : emo.getEmotions()) {
					writeD(e.getId());
					writeD(e.getRemainingTime());
				}

				MotionList motions = player.getMotions();
				writeD(motions.getMotions().size());
				for (Motion motion : motions.getMotions().values()) {
					writeD(motion.getId());
					writeD(motion.getExpireTime());
					writeC(motion.isActive() ? 1 : 0);
				}

				MacroList macro = player.getMacroList();
				writeD(macro.getMacrosses().size());
				for (Entry<Integer, String> m : macro.getMacrosses().entrySet()) {
					writeD(m.getKey());
					writeS(m.getValue());
				}

				NpcFactions nf = player.getNpcFactions();
				writeD(nf.getNpcFactions().size());
				for (NpcFaction f : nf.getNpcFactions()) {
					writeD(f.getId());
					writeD(f.getTime());
					writeD(f.isActive() ? 1 : 0);
					writeS(f.getState().toString());
					writeD(f.getQuestId());
				}

				Collection<PetCommonData> pets = player.getPetList().getPets();
				writeD(pets.size());
				for (PetCommonData pet : pets) {
					writeD(pet.getPetId());
					writeD(pet.getDecoration());
					long birthday = pet.getBirthdayTimestamp() == null ? 0 : pet.getBirthdayTimestamp().getTime();
					writeQ(birthday);
					writeS(pet.getName());
				}

				RecipeList rec = player.getRecipeList();
				writeD(rec.getRecipeList().size());
				for (int id : rec.getRecipeList()) {
					writeD(id);
				}

				PlayerSkillList skillList = player.getSkillList();

				// discard stigma skills
				List<PlayerSkillEntry> skills = new ArrayList<PlayerSkillEntry>();
				for (PlayerSkillEntry sk : skillList.getAllSkills()) {
					if (!sk.isStigma()) {
						skills.add(sk);
					}
				}

				writeD(skills.size());
				for (PlayerSkillEntry sk : skills) {
					writeD(sk.getSkillId());
					writeD(sk.getSkillLevel());
				}

				TitleList titles = player.getTitleList();
				writeD(titles.getTitles().size());
				for (Title t : titles.getTitles()) {
					writeD(t.getId());
					writeD(t.getRemainingTime());
				}

				PlayerSettings ps = player.getPlayerSettings();
				writeD(ps.getUiSettings() == null ? 0 : ps.getUiSettings().length);
				writeD(ps.getShortcuts() == null ? 0 : ps.getShortcuts().length);
				if (ps.getUiSettings() != null) {
					writeB(ps.getUiSettings());
				}
				if (ps.getShortcuts() != null) {
					writeB(ps.getShortcuts());
				}
				writeD(ps.getDeny());
				writeD(ps.getDisplay());

				QuestStateList qsl = player.getQuestStateList();
				FastList<QuestState> quests = FastList.newInstance();
				for (QuestState qs : qsl.getQuests().values()) {
					if (qs == null) {
						log.warn("there are null quest on player " + player.getName() + ". taskId #" + taskId + ". transfer skip that");
						continue;
					}
					quests.add(qs);
				}

				writeD(quests.size());
				for (QuestState qs : quests) {
					writeD(qs.getQuestId());
					writeS(qs.getStatus().toString());
					writeD(qs.getQuestVars().getQuestVars());
					writeD(qs.getCompleteCount());
					// writeS() next repeat time
					writeD(qs.getReward());
				}

				FastList.recycle(quests);
			}
				break;
		}
	}
}
