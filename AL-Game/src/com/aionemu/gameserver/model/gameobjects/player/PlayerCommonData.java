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

import java.sql.Timestamp;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.StaticData;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DP_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_DP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.XPLossEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * This class is holding base information about player, that may be used even when player itself is not online.
 *
 * @author Luno
 * @modified cura
 */
public class PlayerCommonData extends VisibleObjectTemplate {

	/**
	 * Logger used by this class and {@link StaticData} class
	 */
	static Logger log = LoggerFactory.getLogger(PlayerCommonData.class);
	private final int playerObjId;
	private Race race;
	private String name;
	private PlayerClass playerClass;
	/**
	 * Should be changed right after character creation *
	 */
	private int level = 0;
	private long exp = 0;
	private long expRecoverable = 0;
	private Gender gender;
	private Timestamp lastOnline = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private Timestamp lastStamp = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private boolean online;
	private String note;
	private WorldPosition position;
	private int cubeExpands = 0;
	private int warehouseSize = 0;
	private int AdvancedStigmaSlotSize = 0;
	private int titleId = -1;
	private int bonusTitleId = -1;
	private int dp = 0;
	private int mailboxLetters;
	private int soulSickness = 0;
	private boolean noExp = false;
	private long reposteCurrent;
	private long reposteMax;
	private long goldenStarEnergy;
	private long goldenStarEnergyMax = 625000000;
	private long growthEnergy;
	private long growthEnergyMax;
	private long salvationPoint;
	private int mentorFlagTime;
	private int worldOwnerId;
	private BoundRadius boundRadius;
	private long lastTransferTime;
	public int battleGroundPoints = 0;
	private int initialGameStatsDatabase = 0;
	private int fatigue = 0;
	private int fatigueRecover = 0;
	private int fatigueReset = 0;
	private int joinRequestLegionId = 0;
	private LegionJoinRequestState joinRequestState = LegionJoinRequestState.NONE;
	private PlayerUpgradeArcade upgradeArcade;

	private PlayerBonusTime bonusTime = new PlayerBonusTime();
	private Timestamp creationDate;

	private int lunaCoins = 0;
	private int wardrobeSize = 256;
	private boolean GoldenStarBoost = false;
	private int lunaConsumePoint;
	private int muni_keys;
	private int consumeCount = 0;
	private int wardrobeSlot;
	private int floor;
    private int minionSkillPoints;
    private Timestamp minionFunctionTime;

	//Shugo Sweep 5.1
	private int goldenDice;
	private int resetBoard;

	// TODO: Move all function to playerService or Player class.
	public PlayerCommonData(int objId) {
		this.playerObjId = objId;
	}

	public int getPlayerObjId() {
		return playerObjId;
	}

	public long getExp() {
		return this.exp;
	}

	public int getCubeExpands() {
		return this.cubeExpands;
	}

	public void setCubeExpands(int cubeExpands) {
		this.cubeExpands = cubeExpands;
	}

	/**
	 * @return the AdvancedStigmaSlotSize
	 */
	public int getAdvancedStigmaSlotSize() {
		return AdvancedStigmaSlotSize;
	}

	/**
	 * @param AdvancedStigmaSlotSize
	 *            the AdvancedStigmaSlotSize to set
	 */
	public void setAdvancedStigmaSlotSize(int AdvancedStigmaSlotSize) {
		this.AdvancedStigmaSlotSize = AdvancedStigmaSlotSize;
	}

	public long getExpShown() {
		return this.exp - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	public long getExpNeed() {
		if (this.level == DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel()) {
			return 0;
		}
		return DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level + 1) - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	/**
	 * calculate the lost experience must be called before setexp
	 *
	 * @author Jangan
	 */
	public void calculateExpLoss() {
		long expLost = XPLossEnum.getExpLoss(this.level, this.getExpNeed());

		int unrecoverable = (int) (expLost * 0.33333333);
		int recoverable = (int) expLost - unrecoverable;
		long allExpLost = recoverable + this.expRecoverable;

		if (this.getExpShown() > unrecoverable) {
			this.exp = this.exp - unrecoverable;
		}
		else {
			this.exp = this.exp - this.getExpShown();
		}
		if (this.getExpShown() > recoverable) {
			this.expRecoverable = allExpLost;
			this.exp = this.exp - recoverable;
		}
		else {
			this.expRecoverable = this.expRecoverable + this.getExpShown();
			this.exp = this.exp - this.getExpShown();
		}
		if (this.expRecoverable > getExpNeed() * 0.25) {
			this.expRecoverable = Math.round(getExpNeed() * 0.25);
		}
		if (this.getPlayer() != null) {
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), this.getCurrentReposteEnergy(), this.getMaxReposteEnergy(), this.getGoldenStarEnergy(), this.getGrowthEnergy()));
		}
	}

	public void setRecoverableExp(long expRecoverable) {
		this.expRecoverable = expRecoverable;
	}

	public void resetRecoverableExp() {
		long el = this.expRecoverable;
		this.expRecoverable = 0;
		this.setExp(this.exp + el);
	}

	public long getExpRecoverable() {
		return this.expRecoverable;
	}

	/**
	 * @param value
	 */
	public void addExp(long value, int npcNameId) {
		this.addExp(value, null, npcNameId, "", 0);
	}

	public void addExp(long value, RewardType rewardType) {
		this.addExp(value, rewardType, 0, "", 0);
	}

	public void addExp(long value, RewardType rewardType, int npcNameId) {
		this.addExp(value, rewardType, npcNameId, "", 0);
	}
	
	public void addExp(long value, RewardType rewardType, int npcNameId, int questId) {
		this.addExp(value, rewardType, npcNameId, "", questId);
	}


	public void addExp(long value, RewardType rewardType, String name) {
		this.addExp(value, rewardType, 0, name, 0);
	}

	public void addExp(long value, RewardType rewardType, int npcNameId, String name, int questId) {
		if (this.noExp) {
			return;
		}

		if (CustomConfig.ENABLE_EXP_CAP) {
			value = value > CustomConfig.EXP_CAP_VALUE ? CustomConfig.EXP_CAP_VALUE : value;
		}

		long reward = value;
		if (this.getPlayer() != null && rewardType != null) {
			reward = rewardType.calcReward(this.getPlayer(), value);
		}

		long repose = 0;
		if (this.isReadyForReposteEnergy() && this.getCurrentReposteEnergy() > 0) {
			repose = (long) ((reward / 100f) * 40); // 40% bonus
			this.addReposteEnergy(-repose);
		}

		long salvation = 0;
		if (this.isReadyForSalvationPoints() && this.getCurrentSalvationPercent() > 0) {
			salvation = (long) ((reward / 100f) * this.getCurrentSalvationPercent());
			// TODO! remove salvation points?
		}

		long goldenstar = 0;
		long goldenstarboost = 0;
		if (this.isReadyForGoldenStarEnergy() && this.getGoldenStarEnergy() > 0) {
			goldenstar = (reward);
			this.addGoldenStarEnergy(-goldenstar);
			if (GoldenStarBoost) {
				goldenstarboost = (long) ((reward / 100f) * 20);
			}
		}

		long growth = 0;
		if (this.isReadyForGrowthEnergy() && this.getGrowthEnergy() > 0) {
			growth = (long) ((reward / 100f) * 60);
			this.addGrowthEnergy(-growth * 5);// reduce
		}

		if (this.getPlayer() != null) {
			if (rewardType != null) {
				if (this.getPlayer().getPosition().getMapId() != 302400000) { //TowerOfChallenge
					if (rewardType == RewardType.HUNTING || rewardType == RewardType.GROUP_HUNTING || rewardType == RewardType.CRAFTING || rewardType == RewardType.GATHERING || rewardType == RewardType.MONSTER_BOOK) {
						reward += repose + goldenstar + goldenstarboost + growth;
					}
					else {
						reward += repose;
					}
				} else {
					reward += 0;
				}
			}
		}
		
		setExp(exp + reward);
		if (this.getPlayer() != null) {
			if (rewardType != null) {
				switch (rewardType) {
					case HUNTING:
					case GROUP_HUNTING:
					case CRAFTING:
					case GATHERING:
					case MONSTER_BOOK:
						if (this.getPlayer().getPosition().getMapId() != 302400000) { // TowerOfChallenge
							if (npcNameId == 0) {// Exeption quest w/o reward npc
								// You have gained %num1 XP.
								PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
							}
							else { // You have gained %num1 XP from %0.
								PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(npcNameId * 2 + 1), reward));
								if (repose > 0) {
									PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2805577), repose));
								}
								if (growth > 0) {
									PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806377), growth));
								}
								if (goldenstar > 0) {
									PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806671), goldenstar));
								}
							}
						}
						break;
					case QUEST:
						if (npcNameId == 0) // Exeption quest w/o reward npc
						// You have gained %num1 XP.
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
						}
						else if (repose > 0 && salvation > 0) // You have gained %num1 XP from %0 (Energy of Repose %num2, Energy of Salvation %num3).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, repose, salvation));
						}
						else if (repose > 0 && salvation == 0) // You have gained %num1 XP from %0 (Energy of Repose %num2).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, repose));
						}
						else if (repose == 0 && salvation > 0) // You have gained %num1 XP from %0 (Energy of Salvation %num2).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, salvation));
						}
						else // You have gained %num1 XP from %0.
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(npcNameId * 2 + 1), reward));
						}
						break;
					case PVP_KILL:
						if (repose > 0 && salvation > 0) // You have gained %num1 XP from %0 (Energy of Repose %num2, Energy of Salvation %num3).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS(name, reward, repose, salvation));
						}
						else if (repose > 0 && salvation == 0) // You have gained %num1 XP from %0 (Energy of Repose %num2).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS(name, reward, repose));
						}
						else if (repose == 0 && salvation > 0) // You have gained %num1 XP from %0 (Energy of Salvation %num2).
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS(name, reward, salvation));
						}
						else // You have gained %num1 XP from %0.
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP(name, reward));
						}
						break;
					case TOWER_OF_CHALLENGE_REWARD:
							// You have gained %num1 XP.
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
						break;
					default:
						break;
				}
			}
		}
	}

	public boolean isReadyForSalvationPoints() {
		return level >= 15 && level < GSConfig.PLAYER_MAX_LEVEL + 1;
	}

	public boolean isReadyForReposteEnergy() {
		return level >= 10;
	}

	public void addReposteEnergy(long add) {
		if (!this.isReadyForReposteEnergy()) {
			return;
		}

		reposteCurrent += add;
		if (reposteCurrent < 0) {
			reposteCurrent = 0;
		}
		else if (reposteCurrent > getMaxReposteEnergy()) {
			reposteCurrent = getMaxReposteEnergy();
		}
	}

	public void updateMaxReposte() {
		if (!isReadyForReposteEnergy()) {
			reposteCurrent = 0;
			reposteMax = 0;
		}
		else {
			reposteMax = (long) (getExpNeed() * 0.25f); // Retail 99%
		}
	}

	public void setCurrentReposteEnergy(long value) {
		reposteCurrent = value;
	}

	public long getCurrentReposteEnergy() {
		return isReadyForReposteEnergy() ? this.reposteCurrent : 0;
	}

	public long getMaxReposteEnergy() {
		return isReadyForReposteEnergy() ? this.reposteMax : 0;
	}

	/**
	 * @Golden Star Energy
	 */
	public boolean isReadyForGoldenStarEnergy() {
		return level >= 10;
	}

	public void addGoldenStarEnergy(long add) {
		if (!this.isReadyForGoldenStarEnergy()) {
			return;
		}

		goldenStarEnergy += add;
		if (goldenStarEnergy < 0) {
			goldenStarEnergy = 0;
		}
		else if (goldenStarEnergy > getMaxGoldenStarEnergy()) {
			goldenStarEnergy = getMaxGoldenStarEnergy();
		}
		checkGoldenStarPercent();
	}

	public void setGoldenStarEnergy(long value) {
		goldenStarEnergy = value;
		if (goldenStarEnergy < 0) {
			goldenStarEnergy = 0;
		}
		checkGoldenStarPercent();
	}

	public long getGoldenStarEnergy() {
		return isReadyForGoldenStarEnergy() ? this.goldenStarEnergy : 0;
	}

	public long getMaxGoldenStarEnergy() {
		return isReadyForGoldenStarEnergy() ? this.goldenStarEnergyMax : 0;
	}

	public void checkGoldenStarPercent() {
		if (getPlayer() != null) {
			if (this.isReadyForGoldenStarEnergy()) {
				int percent = (int) (goldenStarEnergy * 100f / getMaxGoldenStarEnergy());
				if (!GoldenStarBoost && percent > 50) {
					GoldenStarBoost = true;
					PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403399, 50));
				}
				else if (GoldenStarBoost && percent < 50) {
					GoldenStarBoost = false;
					PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403400, 50));
				}
				else if (goldenStarEnergy <= 0) {
					PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403401));
				}
			}
		}
	}

	/**
	 * @Growth Energy
	 */
	public boolean isReadyForGrowthEnergy() {
		return level >= 66 && level < GSConfig.PLAYER_MAX_LEVEL + 1;
	}

	public void addGrowthEnergy(long add) {
		if (!this.isReadyForGrowthEnergy()) {
			return;
		}

		growthEnergy += add;
		if (growthEnergy < 0) {
			growthEnergy = 0;
		}
		else if (growthEnergy > getMaxGrowthEnergy()) {
			growthEnergy = getMaxGrowthEnergy();
		}
	}

	public void updateMaxGrowthEnergy() {
		if (!isReadyForGrowthEnergy()) {
			growthEnergy = 0;
			growthEnergyMax = 0;
		}
		else {
			if (this.level < 70) {
				growthEnergyMax = (77000000 + (7000000 * (this.level - 66)));
			}
			else if (this.level == 70) {
				growthEnergyMax = 106000000;
			}
			else if (this.level == 71) {
				growthEnergyMax = 127000000;
			}
			else if (this.level < 75) {
				growthEnergyMax = (127000000 + (11000000 * (this.level - 71)));
			}
			else {
				growthEnergyMax = 175000000;
			}
		}
	}

	public void setGrowthEnergy(long value) {
		growthEnergy = value;
	}

	public long getGrowthEnergy() {
		return isReadyForGrowthEnergy() ? this.growthEnergy : 0;
	}

	public long getMaxGrowthEnergy() {
		return isReadyForGrowthEnergy() ? this.growthEnergyMax : 0;
	}

	/**
	 * sets the exp value
	 *
	 * @param exp
	 */
	public void setExp(long exp) {
		int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();
		long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
		if (getPlayerClass() != null && getPlayerClass().isStartingClass()) {
			maxLevel = GSConfig.STARTING_LEVEL > GSConfig.STARTCLASS_MAXLEVEL ? GSConfig.STARTING_LEVEL : GSConfig.STARTCLASS_MAXLEVEL;;
			if (this.getLevel() == 9 && this.getExp() >= 74059) {
				// You can advance to level 10 only after you have completed the class change quest.
				PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.STR_LEVEL_LIMIT_QUEST_NOT_FINISHED1);
			}
		}
		if (exp > maxExp) {
			exp = maxExp;
		}
		int oldLvl = this.level;
		this.exp = exp;
		// make sure level is never larger than maxLevel-1
		boolean up = false;
		while ((this.level + 1) < maxLevel && (up = exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level + 1)) || (this.level - 1) >= 0 && exp < DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level)) {
			if (up) {
				this.level++;
			}
			else {
				this.level--;
			}
			upgradePlayerData();
		}
		if (this.getPlayer() != null) {
			if (oldLvl != level) {
				updateMaxReposte();
				updateMaxGrowthEnergy();
			}
			PacketSendUtility.sendPacket(this.getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), this.getCurrentReposteEnergy(), this.getMaxReposteEnergy(), this.getGoldenStarEnergy(), this.getGrowthEnergy()));
		}
	}

	private void upgradePlayerData() {
		Player player = getPlayer();
		if (player != null) {
			player.getController().upgradePlayer();
			resetSalvationPoints();
		}
	}

	public void setNoExp(boolean value) {
		this.noExp = value;
	}

	public boolean getNoExp() {
		return noExp;
	}

	/**
	 * @return Race as from template
	 */
	public final Race getRace() {
		return race;
	}

	public Race getOppositeRace() {
		return race == Race.ELYOS ? Race.ASMODIANS : Race.ELYOS;
	}

	/**
	 * @return the mentorFlagTime
	 */
	public int getMentorFlagTime() {
		return mentorFlagTime;
	}

	public boolean isHaveMentorFlag() {
		return mentorFlagTime > System.currentTimeMillis() / 1000;
	}

	/**
	 * @param mentorFlagTime
	 *            the mentorFlagTime to set
	 */
	public void setMentorFlagTime(int mentorFlagTime) {
		this.mentorFlagTime = mentorFlagTime;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public WorldPosition getPosition() {
		return position;
	}

	public Timestamp getLastOnline() {
		return lastOnline;
	}

	public void setLastOnline(Timestamp timestamp) {
		lastOnline = timestamp;
	}

	public Timestamp getLastStamp() {
		return lastStamp;
	}

	public void setLastStamp(Timestamp timestamp) {
		this.lastStamp = timestamp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level <= DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel()) {
			this.setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level));
		}
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public int getBonusTitleId() {
		return bonusTitleId;
	}

	public void setBonusTitleId(int bonusTitleId) {
		this.bonusTitleId = bonusTitleId;
	}

	/**
	 * This method should be called exactly once after creating object of this class
	 *
	 * @param position
	 */
	public void setPosition(WorldPosition position) {
		this.position = position;
	}

	/**
	 * Gets the cooresponding Player for this common data. Returns null if the player is not online
	 *
	 * @return Player or null
	 */
	public Player getPlayer() {
		if (online && getPosition() != null) {
			return World.getInstance().findPlayer(playerObjId);
		}
		return null;
	}

	public void addDp(int dp) {
		setDp(this.dp + dp);
	}

	/**
	 * //TODO move to lifestats -> db save?
	 *
	 * @param dp
	 */
	public void setDp(int dp) {
		if (getPlayer() != null) {
			if (playerClass.isStartingClass()) {
				return;
			}

			int maxDp = getPlayer().getGameStats().getMaxDp().getCurrent();
			this.dp = dp > maxDp ? maxDp : dp;

			PacketSendUtility.broadcastPacket(getPlayer(), new SM_DP_INFO(playerObjId, this.dp), true);
			getPlayer().getGameStats().updateStatsAndSpeedVisually();
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_DP(this.dp));
		}
		else {
			log.debug("CHECKPOINT : getPlayer in PCD return null for setDP " + isOnline() + " " + getPosition());
		}
	}

	public int getDp() {
		return this.dp;
	}

	@Override
	public int getTemplateId() {
		return 100000 + race.getRaceId() * 2 + gender.getGenderId();
	}

	@Override
	public int getNameId() {
		return 0;
	}

	/**
	 * @param warehouseSize
	 *            the warehouseSize to set
	 */
	public void setWarehouseSize(int warehouseSize) {
		this.warehouseSize = warehouseSize;
	}

	/**
	 * @return the warehouseSize
	 */
	public int getWarehouseSize() {
		return warehouseSize;
	}

	public void setMailboxLetters(int count) {
		this.mailboxLetters = count;
	}

	public int getMailboxLetters() {
		return mailboxLetters;
	}

	/**
	 * @param boundRadius
	 */
	public void setBoundingRadius(BoundRadius boundRadius) {
		this.boundRadius = boundRadius;
	}

	@Override
	public BoundRadius getBoundRadius() {
		return boundRadius;
	}

	public void setDeathCount(int count) {
		this.soulSickness = count;
	}

	public int getDeathCount() {
		return this.soulSickness;
	}

	/**
	 * Value returned here means % of exp bonus.
	 *
	 * @return
	 */
	public byte getCurrentSalvationPercent() {
		if (salvationPoint <= 0) {
			return 0;
		}

		long per = salvationPoint / 1000;
		if (per > 30) {
			return 30;
		}

		return (byte) per;
	}

	public void addSalvationPoints(long points) {
		salvationPoint += points;
	}

	public void resetSalvationPoints() {
		salvationPoint = 0;
	}

	public void setLastTransferTime(long value) {
		this.lastTransferTime = value;
	}

	public long getLastTransferTime() {
		return this.lastTransferTime;
	}

	public int getWorldOwnerId() {
		return worldOwnerId;
	}

	public void setWorldOwnerId(int worldOwnerId) {
		this.worldOwnerId = worldOwnerId;
	}

	/**
	 * @return the battleGroundPoints
	 */
	public int getBattleGroundPoints() {
		return battleGroundPoints;
	}

	/**
	 * @param battleGroundPoints
	 *            the battleGroundPoints to set
	 */
	public void setBattleGroundPoints(int battleGroundPoints) {
		this.battleGroundPoints = battleGroundPoints;
	}

	public int isInitialGameStats() {
		return initialGameStatsDatabase;
	}

	public void setInitialGameStats(int initialGameStats) {
		this.initialGameStatsDatabase = initialGameStats;
	}

	public void setFatigue(int value) {
		this.fatigue = value;
	}

	public void setFatigueRecover(int count) {
		this.fatigueRecover = count;
	}

	public int getFatigue() {
		return fatigue;
	}

	public int getFatigueRecover() {
		return fatigueRecover;
	}

	public void setFatigueReset(int value) {
		this.fatigueReset = value;
	}

	public int getFatigueReset() {
		return fatigueReset;
	}

	public int getJoinRequestLegionId() {
		return joinRequestLegionId;
	}

	public void setJoinRequestLegionId(int joinRequestLegionId) {
		this.joinRequestLegionId = joinRequestLegionId;
	}

	public LegionJoinRequestState getJoinRequestState() {
		return joinRequestState;
	}

	public void setJoinRequestState(LegionJoinRequestState joinRequestState) {
		this.joinRequestState = joinRequestState;
	}

	public PlayerUpgradeArcade getUpgradeArcade() {
		if (upgradeArcade == null)
			this.upgradeArcade = new PlayerUpgradeArcade();

		return upgradeArcade;
	}

	public void setUpgradeArcade(PlayerUpgradeArcade upgradeArcade) {
		this.upgradeArcade = upgradeArcade;
	}

	/**
	 * @New User Bonus Time
	 */
	public PlayerBonusTime getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(Timestamp time) {
		this.bonusTime.setTime(time);
	}

	public void setBonusType(PlayerBonusTimeStatus status) {
		this.bonusTime.setStatus(status);
	}

	public void setCreationDate(Timestamp date) {
		creationDate = date;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @Luna System
	 */
	public int getLunaCoins() {
		return lunaCoins;
	}

	public void setLunaCoins(int lunaCoins) {
		this.lunaCoins = lunaCoins;
	}

	public int getWardrobeSize() {
		return wardrobeSize;
	}

	public void setWardrobeSize(int wardrobeSize) {
		this.wardrobeSize = wardrobeSize;
	}

	/**
	 * @Luna System
	 */
	public void setLunaConsumePoint(int point) {
		this.lunaConsumePoint = point;
	}

	public int getLunaConsumePoint() {
		return lunaConsumePoint;
	}

	public void setMuniKeys(int keys) {
		this.muni_keys = keys;
	}

	public int getMuniKeys() {
		return muni_keys;
	}

	public void setLunaConsumeCount(int count) {
		this.consumeCount = count;
	}

	public int getLunaConsumeCount() {
		return consumeCount;
	}

	public void setWardrobeSlot(int slot) {
		this.wardrobeSlot = slot;
	}

	public int getWardrobeSlot() {
		return wardrobeSlot;
	}

	/**
	 * @Golden Dice / Lucky Dice
	 */
	public int getGoldenDice() {
		return goldenDice;
	}

	public void setGoldenDice(int dice) {
		this.goldenDice = dice;
	}

	public int getResetBoard() { 
		return resetBoard;
	}

	public void setResetBoard(int reset) {
		this.resetBoard = reset;
	}
	
	/**
	 * @Tower of Challenge
	 */
    public void setFloor(final int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return this.floor;
    }

	/**
	 * @Minions
	 */
    public int getMinionSkillPoints() {
        return minionSkillPoints;
    }
    
    public void setMinionSkillPoints(int minionSkillPoints) {
        this.minionSkillPoints = minionSkillPoints;
    }
    
    public Timestamp getMinionFunctionTime() {
        return minionFunctionTime;
    }
    
    public void setMinionFunctionTime(Timestamp minionFunctionTime) {
        this.minionFunctionTime = minionFunctionTime;
    }
}
