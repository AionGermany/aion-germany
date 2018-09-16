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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * Goto command
 *
 * @author Dwarfpicker
 * @rework Imaginary
 * @modified GiGatR00n
 */
public class GoTo extends AdminCommand {

	public GoTo() {
		super("goto");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax //goto <location> or //goto <?location>");
			return;
		}

		StringBuilder sbDestination = new StringBuilder();
		for (String p : params) {
			sbDestination.append(p + " ");
		}

		String destination = sbDestination.toString().trim();

		/**
		 * show some help
		 **/
		if (destination.equalsIgnoreCase("?elysea")) {
			PacketSendUtility.sendMessage(player, "?poeta | ?heiron | ?inggison | ?reshanta");
		}
		else if (destination.equalsIgnoreCase("?asmodae")) {
			PacketSendUtility.sendMessage(player, "?ishalgen | ?beluslan | ?gelkmaros | ?reshanta");
		}
		/**
		 * Elysea
		 */
		else if (destination.equalsIgnoreCase("Sanctum")) {
			goTo(player, WorldMapType.SANCTUM.getId(), 1322, 1511, 568);
		}
		else if (destination.equalsIgnoreCase("Kaisinel")) {
			goTo(player, WorldMapType.KAISINEL.getId(), 2155, 1567, 1205);
		}
		else if (destination.equalsIgnoreCase("?poeta")) {
			PacketSendUtility.sendMessage(player, "Poeta | Melponeh");
		}
		else if (destination.equalsIgnoreCase("Poeta")) {
			goTo(player, WorldMapType.POETA.getId(), 806, 1242, 119);
		}
		else if (destination.equalsIgnoreCase("Melponeh")) {
			goTo(player, WorldMapType.POETA.getId(), 426, 1740, 119);
		}
		else if (destination.equalsIgnoreCase("?heiron")) {
			PacketSendUtility.sendMessage(player, "Heiron | Heiron Observatory | Senea | Jeiaparan | Changarnerk | Kishar | Arbolu | Reaper");
		}
		else if (destination.equalsIgnoreCase("Heiron")) {
			goTo(player, WorldMapType.HEIRON.getId(), 2540, 343, 411);
		}
		else if (destination.equalsIgnoreCase("Heiron Observatory")) {
			goTo(player, WorldMapType.HEIRON.getId(), 1423, 1334, 175);
		}
		else if (destination.equalsIgnoreCase("Senea")) {
			goTo(player, WorldMapType.HEIRON.getId(), 971, 686, 135);
		}
		else if (destination.equalsIgnoreCase("Jeiaparan")) {
			goTo(player, WorldMapType.HEIRON.getId(), 1635, 2693, 115);
		}
		else if (destination.equalsIgnoreCase("Changarnerk")) {
			goTo(player, WorldMapType.HEIRON.getId(), 916, 2256, 157);
		}
		else if (destination.equalsIgnoreCase("Kishar")) {
			goTo(player, WorldMapType.HEIRON.getId(), 1999, 1391, 118);
		}
		else if (destination.equalsIgnoreCase("Arbolu")) {
			goTo(player, WorldMapType.HEIRON.getId(), 170, 1662, 120);
		}
		else if (destination.equalsIgnoreCase("reaper")) {
			goTo(player, WorldMapType.HEIRON.getId(), 2767, 1867, 154);
		}
		/**
		 * Asmodae
		 */
		else if (destination.equalsIgnoreCase("Pandaemonium")) {
			goTo(player, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195);
		}
		else if (destination.equalsIgnoreCase("Marchutan")) {
			goTo(player, WorldMapType.MARCHUTAN.getId(), 1557, 1429, 266);
		}
		else if (destination.equalsIgnoreCase("?ishalgen")) {
			PacketSendUtility.sendMessage(player, "Ishalgen | Anturon");
		}
		else if (destination.equalsIgnoreCase("Ishalgen")) {
			goTo(player, WorldMapType.ISHALGEN.getId(), 529, 2449, 281);
		}
		else if (destination.equalsIgnoreCase("Anturon")) {
			goTo(player, WorldMapType.ISHALGEN.getId(), 940, 1707, 259);
		}
		else if (destination.equalsIgnoreCase("?beluslan")) {
			PacketSendUtility.sendMessage(player, "Beluslan | Besfer | Kidorun | Red Mane | Kistenian | Hoarfrost");
		}
		else if (destination.equalsIgnoreCase("Beluslan")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 398, 400, 222);
		}
		else if (destination.equalsIgnoreCase("Besfer")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 533, 1866, 262);
		}
		else if (destination.equalsIgnoreCase("Kidorun")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 1243, 819, 260);
		}
		else if (destination.equalsIgnoreCase("Red Mane")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 2358, 1241, 470);
		}
		else if (destination.equalsIgnoreCase("Kistenian")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 1942, 513, 412);
		}
		else if (destination.equalsIgnoreCase("Hoarfrost")) {
			goTo(player, WorldMapType.BELUSLAN.getId(), 2431, 2063, 579);
		}
		/**
		 * Balaurea
		 */
		else if (destination.equalsIgnoreCase("?Inggison")) {
			PacketSendUtility.sendMessage(player, "Inggison | Ufob | Soteria | Hanarkand");
		}
		else if (destination.equalsIgnoreCase("Inggison")) {
			goTo(player, WorldMapType.INGGISON.getId(), 1335, 276, 590);
		}
		else if (destination.equalsIgnoreCase("Ufob")) {
			goTo(player, WorldMapType.INGGISON.getId(), 382, 951, 460);
		}
		else if (destination.equalsIgnoreCase("Soteria")) {
			goTo(player, WorldMapType.INGGISON.getId(), 2713, 1477, 382);
		}
		else if (destination.equalsIgnoreCase("Hanarkand")) {
			goTo(player, WorldMapType.INGGISON.getId(), 1892, 1748, 327);
		}
		else if (destination.equalsIgnoreCase("?gelkmaros")) {
			PacketSendUtility.sendMessage(player, "Gelkmaros | Subterranea | Rhonnam");
		}
		else if (destination.equalsIgnoreCase("Gelkmaros")) {
			goTo(player, WorldMapType.GELKMAROS.getId(), 1763, 2911, 554);
		}
		else if (destination.equalsIgnoreCase("Subterranea")) {
			goTo(player, WorldMapType.GELKMAROS.getId(), 2503, 2147, 464);
		}
		else if (destination.equalsIgnoreCase("Rhonnam")) {
			goTo(player, WorldMapType.GELKMAROS.getId(), 845, 1737, 354);
		}
		else if (destination.equalsIgnoreCase("Silentera")) {
			goTo(player, 600010000, 583, 767, 300);
		}
		else if (destination.equalsIgnoreCase("Kaisinel Academy")) {
			goTo(player, 110070000, 459, 251, 128);
		}
		else if (destination.equalsIgnoreCase("Marchutan Priory")) {
			goTo(player, 120080000, 577, 250, 94);
		}
		else if (destination.equalsIgnoreCase("cygnea") || destination.equalsIgnoreCase("Cygnea")) { // Elyos
			goTo(player, 210070000, 2916.1519f, 836.4192f, 569.375f, (byte) 72);
		}
		else if (destination.equalsIgnoreCase("cygnea1") || destination.equalsIgnoreCase("Aequis Advance Post")) {
			goTo(player, 210070000, 508.38f, 1902.79f, 467.20273f, (byte) 83);
		}
		else if (destination.equalsIgnoreCase("cygnea2") || destination.equalsIgnoreCase("Aequis Outpost")) {
			goTo(player, 210070000, 2171.52f, 2905.67f, 325.25f, (byte) 44);
		}
		else if (destination.equalsIgnoreCase("enshar") || destination.equalsIgnoreCase("Enshar")) { // Asmodians
			goTo(player, 220080000, 448.34335f, 2242.4497f, 220.04858f, (byte) 24);
		}
		else if (destination.equalsIgnoreCase("enshar1") || destination.equalsIgnoreCase("Dawnbreak Temple")) {
			goTo(player, 220080000, 2688.6538f, 1422.5023f, 339.0829f, (byte) 23);
		}
		else if (destination.equalsIgnoreCase("enshar2") || destination.equalsIgnoreCase("Whirlpool Temple")) {
			goTo(player, 220080000, 1578.95f, 151.19f, 186.81342f, (byte) 55);
		}
		else if (destination.equalsIgnoreCase("lakrum") || destination.equalsIgnoreCase("Lakrum")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 600200000, 2688.5857f, 487.36588f, 323.3768f, (byte) 110);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 600200000, 2925.3015f, 2505.124f, 313.80035f, (byte) 90);
			}
		}
		/**
		 * Quest Instance Maps
		 */
		// TODO : Changer id maps
		else if (destination.equalsIgnoreCase("Karamatis 0")) {
			goTo(player, 310010000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Karamatis 1")) {
			goTo(player, 310020000, 312, 274, 206);
		}
		else if (destination.equalsIgnoreCase("Karamatis 2")) {
			goTo(player, 310120000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Aerdina")) {
			goTo(player, 310030000, 275, 168, 205);
		}
		else if (destination.equalsIgnoreCase("Geranaia")) {
			goTo(player, 310040000, 275, 168, 205);
		} // Stigma quest
		else if (destination.equalsIgnoreCase("Sliver") || destination.equalsIgnoreCase("Sliver of Darkness")) {
			goTo(player, 310070000, 247, 249, 1392);
		}
		else if (destination.equalsIgnoreCase("Space") || destination.equalsIgnoreCase("Space of Destiny")) {
			goTo(player, 320070000, 246, 246, 125);
		}
		else if (destination.equalsIgnoreCase("Ataxiar 1")) {
			goTo(player, 320010000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Ataxiar 2")) {
			goTo(player, 320020000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Bregirun")) {
			goTo(player, 320030000, 275, 168, 205);
		}
		else if (destination.equalsIgnoreCase("Nidalber")) {
			goTo(player, 320040000, 275, 168, 205);
		}
		/**
		 * Arenas
		 */
		else if (destination.equalsIgnoreCase("Sanctum Arena")) {
			goTo(player, 310080000, 275, 242, 159);
		}
		else if (destination.equalsIgnoreCase("Triniel Arena")) {
			goTo(player, 320090000, 275, 239, 159);
		} // Empyrean Crucible
		else if (destination.equalsIgnoreCase("Crucible 1-0")) {
			goTo(player, 300300000, 380, 350, 95);
		}
		else if (destination.equalsIgnoreCase("Crucible 1-1")) {
			goTo(player, 300300000, 346, 350, 96);
		}
		else if (destination.equalsIgnoreCase("Crucible 5-0")) {
			goTo(player, 300300000, 1265, 821, 359);
		}
		else if (destination.equalsIgnoreCase("Crucible 5-1")) {
			goTo(player, 300300000, 1256, 797, 359);
		}
		else if (destination.equalsIgnoreCase("Crucible 6-0")) {
			goTo(player, 300300000, 1596, 150, 129);
		}
		else if (destination.equalsIgnoreCase("Crucible 6-1")) {
			goTo(player, 300300000, 1628, 155, 126);
		}
		else if (destination.equalsIgnoreCase("Crucible 7-0")) {
			goTo(player, 300300000, 1813, 797, 470);
		}
		else if (destination.equalsIgnoreCase("Crucible 7-1")) {
			goTo(player, 300300000, 1785, 797, 470);
		}
		else if (destination.equalsIgnoreCase("Crucible 8-0")) {
			goTo(player, 300300000, 1776, 1728, 304);
		}
		else if (destination.equalsIgnoreCase("Crucible 8-1")) {
			goTo(player, 300300000, 1776, 1760, 304);
		}
		else if (destination.equalsIgnoreCase("Crucible 9-0")) {
			goTo(player, 300300000, 1357, 1748, 320);
		}
		else if (destination.equalsIgnoreCase("Crucible 9-1")) {
			goTo(player, 300300000, 1334, 1741, 316);
		}
		else if (destination.equalsIgnoreCase("Crucible 10-0")) {
			goTo(player, 300300000, 1750, 1255, 395);
		}
		else if (destination.equalsIgnoreCase("Crucible 10-1")) {
			goTo(player, 300300000, 1761, 1280, 395);
		} // Arena Of Chaos
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 1")) {
			goTo(player, 300350000, 1332, 1078, 340);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 2")) {
			goTo(player, 300350000, 599, 1854, 227);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 3")) {
			goTo(player, 300350000, 663, 265, 512);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 4")) {
			goTo(player, 300350000, 1840, 1730, 302);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 5")) {
			goTo(player, 300350000, 1932, 1228, 270);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 6")) {
			goTo(player, 300350000, 1949, 946, 224);
		}
		/**
		 * Prison
		 */
		else if (destination.equalsIgnoreCase("Prison LF") || destination.equalsIgnoreCase("Prison Elyos")) {
			goTo(player, 510010000, 256, 256, 49);
		}
		else if (destination.equalsIgnoreCase("Prison DF") || destination.equalsIgnoreCase("Prison Asmos")) {
			goTo(player, 520010000, 256, 256, 49);
		}
		/**
		 * Test
		 */
		else if (destination.equalsIgnoreCase("Test Dungeon")) {
			goTo(player, 300020000, 104, 66, 25);
		}
		else if (destination.equalsIgnoreCase("Test Basic")) {
			goTo(player, 900020000, 144, 136, 20);
		}
		else if (destination.equalsIgnoreCase("Test Server")) {
			goTo(player, 900030000, 300, 200, 79);
		}
		else if (destination.equalsIgnoreCase("Test GiantMonster")) {
			goTo(player, 900100000, 196, 187, 20);
		}
		/**
		 * Unknown
		 */
		else if (destination.equalsIgnoreCase("IDAbPro")) {
			goTo(player, 300010000, 270, 200, 206);
		}
		/**
		 * GM Zone
		 */
		else if (destination.equalsIgnoreCase("gm")) { // 4.9 New Place ?! :)
			goTo(player, 210010000, 1703, 1472, 118);
		}
		/**
		 * 3.0 Maps
		 */
		else if (destination.equalsIgnoreCase("Pernon")) {
			goTo(player, 710010000, 1069, 1539, 98);
		}
		else if (destination.equalsIgnoreCase("Pernon Studio")) {
			goTo(player, 710010000, 1197, 2771, 236);
		}
		else if (destination.equalsIgnoreCase("Oriel")) {
			goTo(player, 700010000, 1261, 1845, 98);
		}
		else if (destination.equalsIgnoreCase("Oriel Studio")) {
			goTo(player, 700010000, 2569, 1960, 182);
		}
		else if (destination.equalsIgnoreCase("Protectrice")) {
			goTo(player, 300330000, 250, 246, 124);
		}
		/**
		 * Instances
		 */
		else if (destination.equalsIgnoreCase("Haramel")) {
			goTo(player, 300200000, 178, 20, 144);
		}
		else if (destination.equalsIgnoreCase("Arcanis") || destination.equalsIgnoreCase("Sky Temple of Arcanis")) {
			goTo(player, 320050000, 177, 229, 536);
		}
		else if (destination.equalsIgnoreCase("Fire Temple") || destination.equalsIgnoreCase("FT")) {
			goTo(player, 320100000, 148, 456, 141);
		}
		else if (destination.equalsIgnoreCase("Kromede") || destination.equalsIgnoreCase("Kromede Trial")) {
			goTo(player, 300230000, 248, 244, 189);
		}
		else if (destination.equalsIgnoreCase("Indratu") || destination.equalsIgnoreCase("Indratu Fortress")) {
			goTo(player, 310090000, 562, 335, 1015);
		}
		else if (destination.equalsIgnoreCase("Azoturan") || destination.equalsIgnoreCase("Azoturan Fortress")) {
			goTo(player, 310100000, 458, 428, 1039);
		}
		else if (destination.equalsIgnoreCase("Bio Lab") || destination.equalsIgnoreCase("Aetherogenetics Lab")) {
			goTo(player, 310050000, 225, 244, 133);
		}
		else if (destination.equalsIgnoreCase("Dredgion")) {
			goTo(player, 300110000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Chantra") || destination.equalsIgnoreCase("Chantra Dredgion")) {
			goTo(player, 300210000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Terath") || destination.equalsIgnoreCase("Terath Dredgion")) {
			goTo(player, 300440000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Bakarma") || destination.equalsIgnoreCase("Bakarma Fortress")) {
			goTo(player, 320170000, 562, 334, 1015);
		}
		else if (destination.equalsIgnoreCase("Taloc") || destination.equalsIgnoreCase("Taloc's Hollow")) {
			goTo(player, 300190000, 192, 219, 1098);
		}
		else if (destination.equalsIgnoreCase("Udas Lower") || destination.equalsIgnoreCase("Udas Lower Temple")) {
			goTo(player, 300160000, 1335, 779, 111);
		}
		else if (destination.equalsIgnoreCase("Esoterrace")) {
			goTo(player, 300250000, 340, 452, 326);
		}
		else if (destination.equalsIgnoreCase("Beshmundir") || destination.equalsIgnoreCase("BT") || destination.equalsIgnoreCase("Beshmundir Temple")) {
			goTo(player, 300170000, 1477, 249, 243);
		}
		else if (destination.equalsIgnoreCase("refuge") || destination.equalsIgnoreCase("Dragon Lord Refuge")) {
			goTo(player, 300520000, 505, 526, 240);
		}
		else if (destination.equalsIgnoreCase("stronghold") || destination.equalsIgnoreCase("Tiamat Stronghold")) {
			goTo(player, 300510000, 1581, 1068, 492);
		}
		else if (destination.equalsIgnoreCase("harmonyTraining") || destination.equalsIgnoreCase("Harmony Training Grounds")) {
			goTo(player, 300570000, 500, 371, 211);
		}
		else if (destination.equalsIgnoreCase("glory") || destination.equalsIgnoreCase("Arena Of Glory")) {
			goTo(player, 300550000, 500, 371, 211);
		}
		else if (destination.equalsIgnoreCase("hell_pass") || destination.equalsIgnoreCase("Hell Pass")) {
			goTo(player, 301630000, 229.00113f, 171.8445f, 164.60031f, (byte) 20);
		}
		else if (destination.equalsIgnoreCase("weapon_factory") || destination.equalsIgnoreCase("Mechanerks Weapons Factory")) {
			goTo(player, 301640000, 400.3279f, 290.5061f, 198.64015f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("ashunatal") || destination.equalsIgnoreCase("Ashunatal Dredgion")) {
			goTo(player, 301650000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("tower") || destination.equalsIgnoreCase("Tower of Challenge")) {
			goTo(player, 302400000, 223.03874f, 249.47852f, 241.08308f, (byte) 0);
		}
		else if (destination.equalsIgnoreCase("narakkalli") || destination.equalsIgnoreCase("Narakkalli")) {
			goTo(player, 302340000, 1317.6039f, 1360.2423f, 494.76074f, (byte) 75);
		}
		else if (destination.equalsIgnoreCase("neviwind") || destination.equalsIgnoreCase("Neviwind Canyon")) {
			goTo(player, 302350000, 1108.7395f, 746.5648f, 336.33923f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("holy") || destination.equalsIgnoreCase("Holy Tower")) {
			goTo(player, 310160000, 76.61565f, 204.50403f, 419.9462f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("treasure") || destination.equalsIgnoreCase("Treasure Island of Courage")) {
			goTo(player, 301700000, 1370.3208f, 1381.8193f, 375.19562f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("mirash") || destination.equalsIgnoreCase("Mirash Sanctum")) {
			goTo(player, 301720000, 770.9657f, 831.978f, 520.6439f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("workshop") || destination.equalsIgnoreCase("Prometuns Workshop")) {
			goTo(player, 302430000, 456.6875f, 805.88556f, 811.7392f, (byte) 0);
		}
		else if (destination.equalsIgnoreCase("rentus") || destination.equalsIgnoreCase("Rentus Base")) {
			goTo(player, 300280000, 557.61523f, 593.1484f, 154.125f, (byte) 73);
		}
		else if (destination.equalsIgnoreCase("markana") || destination.equalsIgnoreCase("Markana")) {
			goTo(player, 301520000, 324.51382f, 183.24435f, 1687.2552f, (byte) 0);
		}
		else if (destination.equalsIgnoreCase("garden") || destination.equalsIgnoreCase("Garden of Knowledge")) {
			goTo(player, 301550000, 1484.535f, 780.51636f, 1035.2672f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("garden_bonus") || destination.equalsIgnoreCase("Garden of Knowledge (Bonus)")) {
			goTo(player, 302490000, 1484.535f, 780.51636f, 1035.2672f, (byte) 60);
		}
		else {
			PacketSendUtility.sendMessage(player, "Could not find the specified destination !");
		}
	}

	private static void goTo(final Player player, int worldId, float x, float y, float z) {
		WorldMap destinationMap = World.getInstance().getWorldMap(worldId);
		if (destinationMap.isInstanceType()) {
			TeleportService2.teleportTo(player, worldId, getInstanceId(worldId, player), x, y, z);
		}
		else {
			TeleportService2.teleportTo(player, worldId, x, y, z);
		}
	}

	private static void goTo(final Player player, int worldId, float x, float y, float z, byte h) {
		WorldMap destinationMap = World.getInstance().getWorldMap(worldId);
		if (destinationMap.isInstanceType()) {
			TeleportService2.teleportTo(player, worldId, getInstanceId(worldId, player), x, y, z, h);
		}
		else {
			TeleportService2.teleportTo(player, worldId, x, y, z, h);
		}
	}

	private static int getInstanceId(int worldId, Player player) {
		if (player.getWorldId() == worldId) {
			WorldMapInstance registeredInstance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
			if (registeredInstance != null) {
				return registeredInstance.getInstanceId();
			}
		}
		WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(worldId);
		InstanceService.registerPlayerWithInstance(newInstance, player);
		return newInstance.getInstanceId();
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax : //goto <location> or //goto <?location>");
	}
}
