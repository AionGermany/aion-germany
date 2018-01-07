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
package instance.pvparenas;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.world.WorldMapInstance;

@InstanceID(301100000)
public class UnityTrainingGroundInstance extends HarmonyArenaInstance {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		killBonus = 1000;
		deathFine = -150;
		super.onInstanceCreate(instance);
	}

	@Override
	protected void reward() {
		float totalScoreAP = (1.0f * 3) * 100;
		float totalScoreGP = (1.0f * 3) * 100;
		float totalScoreCourage = (1.0f * 3) * 100;
		float totalScoreInfinity = (1.0f * 3) * 100;
		int totalPoints = instanceReward.getTotalPoints();
		for (HarmonyGroupReward group : instanceReward.getGroups()) {
			int score = group.getPoints();
			int rank = instanceReward.getRank(score);
			float percent = group.getParticipation();
			float scoreRate = ((float) score / (float) totalPoints);
			int basicAP = 100;
			int rankingAP = 0;
			basicAP *= percent;
			int basicGP = 100;
			int rankingGP = 0;
			basicGP *= percent;
			int basicCoI = 0;
			int rankingCoI = 0;
			basicCoI *= percent;
			int basicCiI = 0;
			int rankingCiI = 0;
			basicCiI *= percent;
			int scoreAP = (int) (totalScoreAP * scoreRate);
			int scoreGP = (int) (totalScoreGP * scoreRate);
			switch (rank) {
			case 0:
				rankingAP = 681;
				rankingGP = 481;
				rankingCoI = 49;
				rankingCiI = 49;
				group.setGloryTicket(1);
				break;
			case 1:
				rankingAP = 487;
				rankingGP = 287;
				rankingCoI = 20;
				rankingCiI = 20;
				break;
			case 2:
				rankingAP = 251;
				rankingGP = 151;
				rankingCoI = 1;
				rankingCiI = 1;
				break;
			}
			rankingAP *= percent;
			rankingGP *= percent;
			rankingCoI *= percent;
			rankingCiI *= percent;
			int scoreCoI = (int) (totalScoreCourage * scoreRate);
			int scoreCiI = (int) (totalScoreInfinity * scoreRate);
			group.setBasicAP(basicAP);
			group.setRankingAP(rankingAP);
			group.setScoreAP(scoreAP);
			group.setBasicGP(basicGP);
			group.setRankingGP(rankingGP);
			group.setScoreGP(scoreGP);
			group.setBasicCourage(basicCoI);
			group.setRankingCourage(rankingCoI);
			group.setScoreCourage(scoreCoI);
			group.setBasicInfinity(basicCiI);
			group.setRankingInfinity(rankingCiI);
			group.setScoreInfinity(scoreCiI);
		}
		super.reward();
	}

	@Override
	protected void spawnRings() {
		FlyRing f1 = new FlyRing(new FlyRingTemplate("PVP_ARENA_1", mapId,
				new Point3D(526.3772, 1800.0292, 176.74919), new Point3D(
						526.3772, 1800.0292, 179.74919), new Point3D(525.0156,
						1797.356, 176.74919), 3), instanceId);
		f1.spawn();
		FlyRing f2 = new FlyRing(new FlyRingTemplate("PVP_ARENA_2", mapId,
				new Point3D(506.68332, 1801.1233, 176.29509), new Point3D(
						506.68332, 1801.1233, 179.29509), new Point3D(
						506.68332, 1798.1233, 176.29509), 3), instanceId);
		f2.spawn();
		FlyRing f3 = new FlyRing(new FlyRingTemplate("PVP_ARENA_3", mapId,
				new Point3D(537.7089, 1772.1154, 176.39908), new Point3D(
						537.7089, 1772.1154, 179.39908), new Point3D(534.8185,
						1772.9186, 176.39908), 3), instanceId);
		f3.spawn();
		FlyRing f4 = new FlyRing(new FlyRingTemplate("PVP_ARENA_4", mapId,
				new Point3D(506.82697, 1761.2292, 176.5923), new Point3D(
						506.82697, 1761.2292, 179.5923), new Point3D(506.54703,
						1764.2162, 176.5923), 3), instanceId);
		f4.spawn();
		FlyRing f5 = new FlyRing(new FlyRingTemplate("PVP_ARENA_5", mapId,
				new Point3D(526.93854, 1761.6971, 176.46439), new Point3D(
						526.93854, 1761.6971, 179.46439), new Point3D(525.2771,
						1764.1951, 176.46439), 3), instanceId);
		f5.spawn();
		FlyRing f6 = new FlyRing(new FlyRingTemplate("PVP_ARENA_6", mapId,
				new Point3D(486.66653, 1761.2954, 176.52344), new Point3D(
						486.66653, 1761.2954, 179.52344), new Point3D(
						486.22333, 1764.2625, 176.52344), 3), instanceId);
		f6.spawn();
		FlyRing f7 = new FlyRing(new FlyRingTemplate("PVP_ARENA_7", mapId,
				new Point3D(463.7616, 1760.8594, 176.37796), new Point3D(
						463.7616, 1760.8594, 179.37796), new Point3D(465.9929,
						1762.8647, 176.37796), 3), instanceId);
		f7.spawn();
		FlyRing f8 = new FlyRing(new FlyRingTemplate("PVP_ARENA_8", mapId,
				new Point3D(453.18936, 1775.0388, 176.37965), new Point3D(
						453.18936, 1775.0388, 179.37965), new Point3D(
						451.03714, 1772.9489, 176.37965), 3), instanceId);
		f8.spawn();
		FlyRing f9 = new FlyRing(new FlyRingTemplate("PVP_ARENA_9", mapId,
				new Point3D(453.36063, 1792.1831, 176.28424), new Point3D(
						453.36063, 1792.1831, 179.28424), new Point3D(450.8542,
						1792.6652, 176.28424), 3), instanceId);
		f9.spawn();
		FlyRing f10 = new FlyRing(new FlyRingTemplate("PVP_ARENA_10", mapId,
				new Point3D(464.15717, 1801.1119, 176.49224), new Point3D(
						464.15717, 1801.1119, 179.4922), new Point3D(462.10913,
						1803.3041, 176.49224), 3), instanceId);
		f10.spawn();
		FlyRing f11 = new FlyRing(new FlyRingTemplate("PVP_ARENA_11", mapId,
				new Point3D(486.1619, 1801.2965, 176.45914), new Point3D(
						486.1619, 1801.2965, 179.45914), new Point3D(485.39664,
						1804.1973, 176.45914), 3), instanceId);
		f11.spawn();
		FlyRing f12 = new FlyRing(new FlyRingTemplate("PVP_ARENA_12", mapId,
				new Point3D(537.2858, 1789.3806, 176.13591), new Point3D(
						537.2858, 1789.3806, 179.13591), new Point3D(534.82043,
						1787.6713, 176.13591), 3), instanceId);
		f12.spawn();
		FlyRing f13 = new FlyRing(new FlyRingTemplate("PVP_ARENA_13", mapId,
				new Point3D(519.96678, 1767.7379, 164.51219), new Point3D(
						519.96678, 1767.7379, 167.51219), new Point3D(
						518.04193, 1770.0391, 164.51219), 3), instanceId);
		f13.spawn();
		FlyRing f14 = new FlyRing(new FlyRingTemplate("PVP_ARENA_14", mapId,
				new Point3D(529.45605, 1780.3438, 152.91333), new Point3D(
						529.45605, 1780.3438, 155.91333), new Point3D(
						526.61993, 1779.3657, 152.91333), 3), instanceId);
		f14.spawn();
		FlyRing f15 = new FlyRing(new FlyRingTemplate("PVP_ARENA_15", mapId,
				new Point3D(520.3683, 1792.2515, 138.297), new Point3D(
						520.3683, 1792.2515, 141.297), new Point3D(520.271,
						1789.253, 138.297), 3), instanceId);
		f15.spawn();
		FlyRing f16 = new FlyRing(new FlyRingTemplate("PVP_ARENA_16", mapId,
				new Point3D(469.53625, 1792.6782, 163.97906), new Point3D(
						469.53625, 1792.6782, 166.97906), new Point3D(
						467.26062, 1794.633, 163.97906), 3), instanceId);
		f16.spawn();
		FlyRing f17 = new FlyRing(new FlyRingTemplate("PVP_ARENA_17", mapId,
				new Point3D(459.51752, 1783.4421, 152.58247), new Point3D(
						459.51752, 1783.4421, 155.58247), new Point3D(
						456.87985, 1782.013, 152.58247), 3), instanceId);
		f17.spawn();
		FlyRing f18 = new FlyRing(new FlyRingTemplate("PVP_ARENA_18", mapId,
				new Point3D(469.3592, 1769.092, 137.91689), new Point3D(
						469.3592, 1769.092, 140.91689), new Point3D(470.37436,
						1771.915, 137.91689), 3), instanceId);
		f18.spawn();
		FlyRing f19 = new FlyRing(new FlyRingTemplate("PVP_ARENA_19", mapId,
				new Point3D(494.79196, 1759.603, 151.13585), new Point3D(
						494.79196, 1759.603, 154.13585), new Point3D(494.51202,
						1762.59, 151.13585), 3), instanceId);
		f19.spawn();
		FlyRing f20 = new FlyRing(new FlyRingTemplate("PVP_ARENA_20", mapId,
				new Point3D(503.06744, 1759.9893, 157.17598), new Point3D(
						503.06744, 1759.9893, 160.17598), new Point3D(502.7875,
						1762.9762, 157.17598), 3), instanceId);
		f20.spawn();
		FlyRing f21 = new FlyRing(new FlyRingTemplate("PVP_ARENA_21", mapId,
				new Point3D(495.112, 1802.11895, 151.642), new Point3D(495.112,
						1802.11895, 154.642), new Point3D(494.50687, 1805.1278,
						151.642), 3), instanceId);
		f21.spawn();
		FlyRing f22 = new FlyRing(new FlyRingTemplate("PVP_ARENA_22", mapId,
				new Point3D(503.28552, 1801.8163, 142.87953), new Point3D(
						503.28552, 1801.8163, 145.87953), new Point3D(
						502.52026, 1804.717, 142.87953), 3), instanceId);
		f22.spawn();
		FlyRing f23 = new FlyRing(new FlyRingTemplate("PVP_ARENA_23", mapId,
				new Point3D(487.70947, 1802.1512, 157.30313), new Point3D(
						487.70947, 1802.1512, 160.30313), new Point3D(
						487.10434, 1805.0896, 157.30313), 3), instanceId);
		f23.spawn();
		FlyRing f24 = new FlyRing(new FlyRingTemplate("PVP_ARENA_24", mapId,
				new Point3D(486.5052, 1759.7152, 143.67566), new Point3D(
						486.5052, 1759.7152, 146.67566), new Point3D(485.90005,
						1762.6536, 143.67566), 3), instanceId);
		f24.spawn();
		FlyRing f25 = new FlyRing(new FlyRingTemplate("PVP_ARENA_25", mapId,
				new Point3D(495.1142, 1769.1791, 151.02248), new Point3D(
						495.1142, 1769.1791, 154.02248), new Point3D(492.2781,
						1768.201, 151.02248), 3), instanceId);
		f25.spawn();
		FlyRing f26 = new FlyRing(new FlyRingTemplate("PVP_ARENA_26", mapId,
				new Point3D(517.1142, 1780.5552, 151.54797), new Point3D(
						517.1142, 1780.5552, 154.54797), new Point3D(517.248,
						1783.5222, 151.54797), 3), instanceId);
		f26.spawn();
		FlyRing f27 = new FlyRing(new FlyRingTemplate("PVP_ARENA_27", mapId,
				new Point3D(539.5928, 1768.1559, 153.20947), new Point3D(
						539.5928, 1768.1559, 156.20947), new Point3D(536.7507,
						1769.1163, 153.20947), 3), instanceId);
		f27.spawn();
		FlyRing f28 = new FlyRing(new FlyRingTemplate("PVP_ARENA_28", mapId,
				new Point3D(494.78378, 1791.6841, 151.01425), new Point3D(
						494.78378, 1791.6841, 154.01425), new Point3D(
						497.77554, 1791.9062, 151.01425), 3), instanceId);
		f28.spawn();
		FlyRing f29 = new FlyRing(new FlyRingTemplate("PVP_ARENA_29", mapId,
				new Point3D(496.37292, 1781.1427, 151.12042), new Point3D(
						496.37292, 1781.1427, 154.12042), new Point3D(496.093,
						1784.1296, 151.12042), 3), instanceId);
		f29.spawn();
		FlyRing f30 = new FlyRing(new FlyRingTemplate("PVP_ARENA_30", mapId,
				new Point3D(504.7736, 1781.0814, 151.68193), new Point3D(
						504.7736, 1781.0814, 154.68193), new Point3D(505.331,
						1778.1337, 151.68193), 3), instanceId);
		f30.spawn();
		FlyRing f31 = new FlyRing(new FlyRingTemplate("PVP_ARENA_31", mapId,
				new Point3D(538.9759, 1791.9437, 153.94568), new Point3D(
						538.9759, 1791.9437, 156.94568), new Point3D(537.32996,
						1789.4355, 153.94568), 3), instanceId);
		f31.spawn();
		FlyRing f32 = new FlyRing(new FlyRingTemplate("PVP_ARENA_32", mapId,
				new Point3D(486.21588, 1781.1268, 150.98273), new Point3D(
						486.21588, 1781.1268, 153.98273), new Point3D(486.6109,
						1778, 150.98273), 3), instanceId);
		f32.spawn();
		FlyRing f33 = new FlyRing(new FlyRingTemplate("PVP_ARENA_33", mapId,
				new Point3D(472.12088, 1781.8315, 150.49611), new Point3D(
						472.12088, 1781.8315, 153.49611), new Point3D(472.839,
						1778.9187, 150.49611), 3), instanceId);
		f33.spawn();
		FlyRing f34 = new FlyRing(new FlyRingTemplate("PVP_ARENA_34", mapId,
				new Point3D(496.78357, 1781.3253, 151.18472), new Point3D(
						496.78357, 1781.3253, 154.18472), new Point3D(
						496.64036, 1784.2924, 151.18472), 3), instanceId);
		f34.spawn();
		FlyRing f35 = new FlyRing(new FlyRingTemplate("PVP_ARENA_35", mapId,
				new Point3D(450.68466, 1771.1696, 153.57443), new Point3D(
						450.68466, 1771.1696, 156.57443), new Point3D(
						452.91595, 1771.1696, 153.57443), 3), instanceId);
		f35.spawn();
		FlyRing f36 = new FlyRing(new FlyRingTemplate("PVP_ARENA_36", mapId,
				new Point3D(452.1979, 1790.3647, 153.43126), new Point3D(
						452.1979, 1790.3647, 156.43126), new Point3D(449.41272,
						1791.4795, 153.43126), 3), instanceId);
		f36.spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (!instanceReward.isStartProgress()) {
			return false;
		}
		Npc npc;
		if (flyingRing.equals("PVP_ARENA_1")) {
			npc = getNpc(526.5524f, 1799.9530f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_2")) {
			npc = getNpc(506.4008f, 1801.0159f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_3")) {
			npc = getNpc(537.6169f, 1772.0968f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_4")) {
			npc = getNpc(506.2996f, 1761.2419f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_5")) {
			npc = getNpc(526.5186f, 1761.3792f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_6")) {
			npc = getNpc(485.9503f, 1761.1323f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_7")) {
			npc = getNpc(463.6774f, 1761.2948f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_8")) {
			npc = getNpc(453.2310f, 1774.9258f, 177.3975f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_9")) {
			npc = getNpc(453.3799f, 1791.6423f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_10")) {
			npc = getNpc(464.1622f, 1801.0581f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_11")) {
			npc = getNpc(485.8056f, 1801.1987f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_12")) {
			npc = getNpc(537.3194f, 1789.4381f, 177.3270f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_13")) {
			npc = getNpc(520.1588f, 1767.9170f, 165.3259f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_14")) {
			npc = getNpc(529.5792f, 1780.8058f, 153.6571f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_15")) {
			npc = getNpc(519.9453f, 1792.4106f, 139.4744f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_16")) {
			npc = getNpc(469.7408f, 1792.6573f, 165.4409f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_17")) {
			npc = getNpc(459.6954f, 1783.1649f, 153.1804f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_18")) {
			npc = getNpc(469.6530f, 1769.2192f, 138.8079f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_19")) {
			npc = getNpc(494.7642f, 1759.5282f, 152.5068f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_20")) {
			npc = getNpc(503.3351f, 1759.6985f, 158.4491f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_21")) {
			npc = getNpc(494.9348f, 1802.1798f, 152.5857f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_22")) {
			npc = getNpc(503.2661f, 1801.7520f, 143.9769f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_23")) {
			npc = getNpc(486.9509f, 1801.9956f, 158.4124f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_24")) {
			npc = getNpc(486.7321f, 1759.7345f, 144.8943f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_25")) {
			npc = getNpc(495.0289f, 1769.2734f, 152.1635f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_26")) {
			npc = getNpc(518.0588f, 1780.5404f, 152.5605f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_27")) {
			npc = getNpc(539.5499f, 1767.9496f, 154.2043f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_28")) {
			npc = getNpc(494.9674f, 1791.8362f, 152.1635f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_29")) {
			npc = getNpc(495.0894f, 1781.0751f, 152.1635f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_30")) {
			npc = getNpc(505.1635f, 1781.0885f, 152.3523f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_31")) {
			npc = getNpc(539.3393f, 1791.3956f, 154.9766f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_32")) {
			npc = getNpc(486.1324f, 1781.1040f, 152.0575f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_33")) {
			npc = getNpc(472.1582f, 1781.9821f, 151.6493f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_34")) {
			npc = getNpc(496.5575f, 1781.0718f, 152.1614f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_35")) {
			npc = getNpc(450.1526f, 1771.6342f, 154.4948f);
			checkRing(npc, player);
		} else if (flyingRing.equals("PVP_ARENA_36")) {
			npc = getNpc(451.9798f, 1789.6945f, 154.4945f);
			checkRing(npc, player);
		}
		return false;
	}

	private void checkRing(Npc npc, Player player) {
		final Integer object = player.getObjectId();
		final HarmonyGroupReward group = instanceReward
				.getHarmonyGroupReward(object);
		if (npc != null && npc.isSpawned()) {
			npc.getController().scheduleRespawn();
			npc.getController().onDelete();
			if (group == null) {
				return;
			}
			group.addPoints(100);
			sendSystemMsg(player, npc, 250);
			instanceReward.sendPacket(10, object);
		}
	}

	protected Npc getNpc(float x, float y, float z) {
		if (!isInstanceDestroyed) {
			for (Npc npc : instance.getNpcs()) {
				SpawnTemplate st = npc.getSpawn();
				if (st.getX() == x && st.getY() == y && st.getZ() == z) {
					return npc;
				}
			}
		}
		return null;
	}
}
