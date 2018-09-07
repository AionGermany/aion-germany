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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * Created by Kill3r
 */
public class MovePlayerTo extends AdminCommand {

	public MovePlayerTo() {
		super("moveplayerto");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			onFail(player, "Parameter Rule : //moveplayerto <playername> <worldid> <x> <y> <z>  OR //moveplayerto <playername> <map name>");
			return;
		}

		Player player1;
		int worldid;
		float x, y, z;

		try {
			player1 = World.getInstance().findPlayer(Util.convertName(params[0]));
		}
		catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "Parameter Rule : //moveplayerto <playername> <worldid> <x> <y> <z>  OR //moveplayerto <playername> <map name>");
			return;
		}

		if (params[1].contains("0")) {
			try {
				worldid = Integer.parseInt(params[1]);
				x = Float.parseFloat(params[2]);
				y = Float.parseFloat(params[3]);
				z = Float.parseFloat(params[4]);

			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(player, "Parameter Rule : //moveplayerto <playername> <worldid> <x> <y> <z>  OR //moveplayerto <playername> <map name>");
				return;
			}

			goTo(player1, worldid, x, y, z);
			PacketSendUtility.sendMessage(player, "Player : " + player1.getName() + " has teleported to world :" + worldid + " - x : " + x + " - y : " + y + " - z : " + z);
			return;
		}

		StringBuilder sbDestination = new StringBuilder();
		// for (String p : params) {
		// sbDestination.append(p + " ");
		// }
		for (int i = 1; i < params.length - 0; i++) {
			sbDestination.append(params[i] + " ");
		}

		String destination = sbDestination.toString().trim();

		/**
		 * Elysea
		 */
		// Sanctum
		if (destination.equalsIgnoreCase("Sanctum")) {
			goTo(player1, WorldMapType.SANCTUM.getId(), 1322, 1511, 568);
		} // Kaisinel
		else if (destination.equalsIgnoreCase("Kaisinel")) {
			goTo(player1, WorldMapType.KAISINEL.getId(), 2155, 1567, 1205);
		} // Poeta
		else if (destination.equalsIgnoreCase("Poeta")) {
			goTo(player1, WorldMapType.POETA.getId(), 806, 1242, 119);
		}
		else if (destination.equalsIgnoreCase("Melponeh")) {
			goTo(player1, WorldMapType.POETA.getId(), 426, 1740, 119);
		} // Verteron
		else if (destination.equalsIgnoreCase("Verteron")) {
			goTo(player1, WorldMapType.VERTERON.getId(), 1643, 1500, 119);
		}
		else if (destination.equalsIgnoreCase("Cantas") || destination.equalsIgnoreCase("Cantas Coast")) {
			goTo(player1, WorldMapType.VERTERON.getId(), 2384, 788, 102);
		}
		else if (destination.equalsIgnoreCase("Ardus") || destination.equalsIgnoreCase("Ardus Shrine")) {
			goTo(player1, WorldMapType.VERTERON.getId(), 2333, 1817, 193);
		}
		else if (destination.equalsIgnoreCase("Pilgrims") || destination.equalsIgnoreCase("Pilgrims Respite")) {
			goTo(player1, WorldMapType.VERTERON.getId(), 2063, 2412, 274);
		}
		else if (destination.equalsIgnoreCase("Tolbas") || destination.equalsIgnoreCase("Tolbas Village")) {
			goTo(player1, WorldMapType.VERTERON.getId(), 1291, 2206, 142);
		} // Eltnen
		else if (destination.equalsIgnoreCase("Eltnen")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 343, 2724, 264);
		}
		else if (destination.equalsIgnoreCase("Golden") || destination.equalsIgnoreCase("Golden Bough Garrison")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 688, 431, 332);
		}
		else if (destination.equalsIgnoreCase("Eltnen Observatory")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 1779, 883, 422);
		}
		else if (destination.equalsIgnoreCase("Novan")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 947, 2215, 252);
		}
		else if (destination.equalsIgnoreCase("Agairon")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 1921, 2045, 361);
		}
		else if (destination.equalsIgnoreCase("Kuriullu")) {
			goTo(player1, WorldMapType.ELTNEN.getId(), 2411, 2724, 361);
		} // Theobomos
		else if (destination.equalsIgnoreCase("Theobomos")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 1398, 1557, 31);
		}
		else if (destination.equalsIgnoreCase("Jamanok") || destination.equalsIgnoreCase("Jamanok Inn")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 458, 1257, 127);
		}
		else if (destination.equalsIgnoreCase("Meniherk")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 1396, 1560, 31);
		}
		else if (destination.equalsIgnoreCase("obsvillage")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 2234, 2284, 50);
		}
		else if (destination.equalsIgnoreCase("Josnack")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 901, 2774, 62);
		}
		else if (destination.equalsIgnoreCase("Anangke")) {
			goTo(player1, WorldMapType.THEOBOMOS.getId(), 2681, 847, 138);
		} // Heiron
		else if (destination.equalsIgnoreCase("Heiron")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 2540, 343, 411);
		}
		else if (destination.equalsIgnoreCase("Heiron Observatory")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 1423, 1334, 175);
		}
		else if (destination.equalsIgnoreCase("Senea")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 971, 686, 135);
		}
		else if (destination.equalsIgnoreCase("Jeiaparan")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 1635, 2693, 115);
		}
		else if (destination.equalsIgnoreCase("Changarnerk")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 916, 2256, 157);
		}
		else if (destination.equalsIgnoreCase("Kishar")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 1999, 1391, 118);
		}
		else if (destination.equalsIgnoreCase("Arbolu")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 170, 1662, 120);
		}
		else if (destination.equalsIgnoreCase("reaper")) {
			goTo(player1, WorldMapType.HEIRON.getId(), 2767, 1867, 154);
		}
		/**
		 * Asmodae
		 */
		// Pandaemonium
		else if (destination.equalsIgnoreCase("Pandaemonium")) {
			goTo(player1, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195);
		} // Marchutran
		else if (destination.equalsIgnoreCase("Marchutan")) {
			goTo(player1, WorldMapType.MARCHUTAN.getId(), 1557, 1429, 266);
		} // Ishalgen
		else if (destination.equalsIgnoreCase("Ishalgen")) {
			goTo(player1, WorldMapType.ISHALGEN.getId(), 529, 2449, 281);
		}
		else if (destination.equalsIgnoreCase("Anturon")) {
			goTo(player1, WorldMapType.ISHALGEN.getId(), 940, 1707, 259);
		} // Altgard
		else if (destination.equalsIgnoreCase("Altgard")) {
			goTo(player1, WorldMapType.ALTGARD.getId(), 1748, 1807, 254);
		}
		else if (destination.equalsIgnoreCase("Basfelt")) {
			goTo(player1, WorldMapType.ALTGARD.getId(), 1903, 696, 260);
		}
		else if (destination.equalsIgnoreCase("Trader")) {
			goTo(player1, WorldMapType.ALTGARD.getId(), 2680, 1024, 311);
		}
		else if (destination.equalsIgnoreCase("Impetusium")) {
			goTo(player1, WorldMapType.ALTGARD.getId(), 2643, 1658, 324);
		}
		else if (destination.equalsIgnoreCase("Altgard Observatory")) {
			goTo(player1, WorldMapType.ALTGARD.getId(), 1468, 2560, 299);
		} // Morheim
		else if (destination.equalsIgnoreCase("Morheim")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 308, 2274, 449);
		}
		else if (destination.equalsIgnoreCase("Desert")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 634, 900, 360);
		}
		else if (destination.equalsIgnoreCase("Slag")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 1772, 1662, 197);
		}
		else if (destination.equalsIgnoreCase("Kellan")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 1070, 2486, 239);
		}
		else if (destination.equalsIgnoreCase("Alsig")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 2387, 1742, 102);
		}
		else if (destination.equalsIgnoreCase("Morheim Observatory")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 2794, 1122, 171);
		}
		else if (destination.equalsIgnoreCase("Halabana")) {
			goTo(player1, WorldMapType.MORHEIM.getId(), 2346, 2219, 127);
		} // Brusthonin
		else if (destination.equalsIgnoreCase("Brusthonin")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 2917, 2421, 15);
		}
		else if (destination.equalsIgnoreCase("Baltasar")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 1413, 2013, 51);
		}
		else if (destination.equalsIgnoreCase("Bollu")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 840, 2016, 307);
		}
		else if (destination.equalsIgnoreCase("Edge")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 1523, 374, 231);
		}
		else if (destination.equalsIgnoreCase("Bubu")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 526, 848, 76);
		}
		else if (destination.equalsIgnoreCase("Settlers")) {
			goTo(player1, WorldMapType.BRUSTHONIN.getId(), 2917, 2417, 15);
		} // Beluslan
		else if (destination.equalsIgnoreCase("Beluslan")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 398, 400, 222);
		}
		else if (destination.equalsIgnoreCase("Besfer")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 533, 1866, 262);
		}
		else if (destination.equalsIgnoreCase("Kidorun")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 1243, 819, 260);
		}
		else if (destination.equalsIgnoreCase("Red Mane")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 2358, 1241, 470);
		}
		else if (destination.equalsIgnoreCase("Kistenian")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 1942, 513, 412);
		}
		else if (destination.equalsIgnoreCase("Hoarfrost")) {
			goTo(player1, WorldMapType.BELUSLAN.getId(), 2431, 2063, 579);
		}
		/**
		 * Balaurea
		 */
		// Inggison
		else if (destination.equalsIgnoreCase("Inggison")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 1335, 276, 590);
		}
		else if (destination.equalsIgnoreCase("Ufob")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 382, 951, 460);
		}
		else if (destination.equalsIgnoreCase("Soteria")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 2713, 1477, 382);
		}
		else if (destination.equalsIgnoreCase("Hanarkand")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 1892, 1748, 327);
		} // Gelkmaros
		else if (destination.equalsIgnoreCase("Gelkmaros")) {
			goTo(player1, WorldMapType.GELKMAROS.getId(), 1763, 2911, 554);
		}
		else if (destination.equalsIgnoreCase("Subterranea")) {
			goTo(player1, WorldMapType.GELKMAROS.getId(), 2503, 2147, 464);
		}
		else if (destination.equalsIgnoreCase("Rhonnam")) {
			goTo(player1, WorldMapType.GELKMAROS.getId(), 845, 1737, 354);
		} // Silentera
		else if (destination.equalsIgnoreCase("Silentera")) {
			goTo(player1, 600010000, 583, 767, 300);
		}
		/**
		 * Abyss
		 */
		else if (destination.equalsIgnoreCase("Reshanta")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 951, 936, 1667);
		}
		else if (destination.equalsIgnoreCase("Teminon")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2867, 1034, 1528);
		}
		else if (destination.equalsIgnoreCase("Primum")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1078, 2839, 1636);
		}
		else if (destination.equalsIgnoreCase("Tigraki")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 539, 1100, 2843);
		}
		else if (destination.equalsIgnoreCase("Nuage noir") || destination.equalsIgnoreCase("Nuage")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 3429, 2439, 2765);
		}
		else if (destination.equalsIgnoreCase("Leibos")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2136, 1943, 1597);
		}
		else if (destination.equalsIgnoreCase("ORSL") || destination.equalsIgnoreCase("Ori sup Latesran")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1596, 2952, 2943);
		}
		else if (destination.equalsIgnoreCase("OCSL") || destination.equalsIgnoreCase("Occi sup Latesran")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2054, 660, 2843);
		}
		else if (destination.equalsIgnoreCase("ORIL") || destination.equalsIgnoreCase("Ori inf Latesran")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1639, 2968, 1668);
		}
		else if (destination.equalsIgnoreCase("OCIL") || destination.equalsIgnoreCase("Occi inf Latesran")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2095, 679, 1567);
		}
		else if (destination.equalsIgnoreCase("Eye of Reshanta") || destination.equalsIgnoreCase("Eye")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1979, 2114, 2291);
		}
		else if (destination.equalsIgnoreCase("Divine Fortress") || destination.equalsIgnoreCase("Divine") || destination.equalsIgnoreCase("1011")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2130, 1925, 2322);
		}
		/**
		 * Fortos
		 */
		// Abyss
		else if (destination.equalsIgnoreCase("Soufre") || destination.equalsIgnoreCase("1141")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1379, 1187, 1537);
		}
		else if (destination.equalsIgnoreCase("Siel occi") || destination.equalsIgnoreCase("1131")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2792, 2609, 1504);
		}
		else if (destination.equalsIgnoreCase("Siel ori") || destination.equalsIgnoreCase("1132")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2608, 2853, 1530);
		}
		else if (destination.equalsIgnoreCase("Roah") || destination.equalsIgnoreCase("1211")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2735, 801, 2894);
		}
		else if (destination.equalsIgnoreCase("Asteria") || destination.equalsIgnoreCase("1251")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 722, 2961, 2921);
		}
		else if (destination.equalsIgnoreCase("Krotan") || destination.equalsIgnoreCase("1221")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2057, 1275, 2987);
		}
		else if (destination.equalsIgnoreCase("Kysis") || destination.equalsIgnoreCase("1231")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 2506, 2109, 3074);
		}
		else if (destination.equalsIgnoreCase("Miren") || destination.equalsIgnoreCase("1241")) {
			goTo(player1, WorldMapType.RESHANTA.getId(), 1789, 2269, 2951);
		} // Balaurea
		else if (destination.equalsIgnoreCase("Avidite") || destination.equalsIgnoreCase("2011")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 887, 1979, 341);
		}
		else if (destination.equalsIgnoreCase("Dragon") || destination.equalsIgnoreCase("2021")) {
			goTo(player1, WorldMapType.INGGISON.getId(), 1729, 2236, 329);
		}
		else if (destination.equalsIgnoreCase("Vorgaltem") || destination.equalsIgnoreCase("3011")) {
			goTo(player1, WorldMapType.GELKMAROS.getId(), 1198, 806, 314);
		}
		else if (destination.equalsIgnoreCase("Pourpre") || destination.equalsIgnoreCase("3021")) {
			goTo(player1, WorldMapType.GELKMAROS.getId(), 1882, 1042, 331);
		}
		/**
		 * Instances
		 */
		else if (destination.equalsIgnoreCase("Haramel")) {
			goTo(player1, 300200000, 176, 21, 144);
		}
		else if (destination.equalsIgnoreCase("Nochsana") || destination.equalsIgnoreCase("NTC")) {
			goTo(player1, 300030000, 513, 668, 331);
		}
		else if (destination.equalsIgnoreCase("Arcanis") || destination.equalsIgnoreCase("Sky Temple of Arcanis")) {
			goTo(player1, 320050000, 177, 229, 536);
		}
		else if (destination.equalsIgnoreCase("Fire Temple") || destination.equalsIgnoreCase("FT")) {
			goTo(player1, 320100000, 144, 312, 123);
		}
		else if (destination.equalsIgnoreCase("Kromede") || destination.equalsIgnoreCase("Kromede Trial")) {
			goTo(player1, 300230000, 248, 244, 189);
		} // Steel Rake
		else if (destination.equalsIgnoreCase("Steel Rake") || destination.equalsIgnoreCase("SR")) {
			goTo(player1, 300100000, 237, 506, 948);
		}
		else if (destination.equalsIgnoreCase("Steel Rake Lower") || destination.equalsIgnoreCase("SR Low")) {
			goTo(player1, 300100000, 283, 453, 903);
		}
		else if (destination.equalsIgnoreCase("Steel Rake Middle") || destination.equalsIgnoreCase("SR Mid")) {
			goTo(player1, 300100000, 283, 453, 953);
		}
		else if (destination.equalsIgnoreCase("Indratu") || destination.equalsIgnoreCase("Indratu Fortress")) {
			goTo(player1, 310090000, 562, 335, 1015);
		}
		else if (destination.equalsIgnoreCase("Azoturan") || destination.equalsIgnoreCase("Azoturan Fortress")) {
			goTo(player1, 310100000, 458, 428, 1039);
		}
		else if (destination.equalsIgnoreCase("Bio Lab") || destination.equalsIgnoreCase("Aetherogenetics Lab")) {
			goTo(player1, 310050000, 225, 244, 133);
		}
		else if (destination.equalsIgnoreCase("Adma") || destination.equalsIgnoreCase("Adma Stronghold")) {
			goTo(player1, 320130000, 450, 200, 168);
		}
		else if (destination.equalsIgnoreCase("Alquimia") || destination.equalsIgnoreCase("Alquimia Research Center")) {
			goTo(player1, 320110000, 603, 527, 200);
		}
		else if (destination.equalsIgnoreCase("Draupnir") || destination.equalsIgnoreCase("Draupnir Cave")) {
			goTo(player1, 320080000, 491, 373, 622);
		}
		else if (destination.equalsIgnoreCase("Theobomos Lab") || destination.equalsIgnoreCase("Theobomos Research Lab")) {
			goTo(player1, 310110000, 477, 201, 170);
		}
		else if (destination.equalsIgnoreCase("Dark Poeta") || destination.equalsIgnoreCase("DP")) {
			goTo(player1, 300040000, 1214, 412, 140);
		} // Lower Abyss
		else if (destination.equalsIgnoreCase("Sulfur") || destination.equalsIgnoreCase("Sulfur Tree Nest")) {
			goTo(player1, 300060000, 462, 345, 163);
		}
		else if (destination.equalsIgnoreCase("Right Wing") || destination.equalsIgnoreCase("Right Wing Chamber")) {
			goTo(player1, 300090000, 263, 386, 103);
		}
		else if (destination.equalsIgnoreCase("Left Wing") || destination.equalsIgnoreCase("Left Wing Chamber")) {
			goTo(player1, 300080000, 672, 606, 321);
		} // Upper Abyss
		else if (destination.equalsIgnoreCase("Asteria Chamber")) {
			goTo(player1, 300050000, 469, 568, 202);
		}
		else if (destination.equalsIgnoreCase("Miren Chamber")) {
			goTo(player1, 300130000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Chamber")) {
			goTo(player1, 300120000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Chamber")) {
			goTo(player1, 300140000, 528, 109, 176);
		}
		else if (destination.equalsIgnoreCase("Roah Chamber")) {
			goTo(player1, 300070000, 504, 396, 94);
		}
		else if (destination.equalsIgnoreCase("Miren Barrack")) {
			goTo(player1, 301290000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Barrack")) {
			goTo(player1, 301280000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Barrack")) {
			goTo(player1, 301300000, 528, 109, 176);
		}
		else if (destination.equalsIgnoreCase("Miren Barrack Legion")) {
			goTo(player1, 301250000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Barrack Legion")) {
			goTo(player1, 301240000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Barrack Legion")) {
			goTo(player1, 301260000, 528, 109, 176);
		} // Divine
		else if (destination.equalsIgnoreCase("Abyssal Splinter") || destination.equalsIgnoreCase("Core")) {
			goTo(player1, 300220000, 704, 153, 453);
		}
		else if (destination.equalsIgnoreCase("Dredgion")) {
			goTo(player1, 300110000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Chantra") || destination.equalsIgnoreCase("Chantra Dredgion")) {
			goTo(player1, 300210000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Terath") || destination.equalsIgnoreCase("Terath Dredgion")) {
			goTo(player1, 300440000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("Taloc") || destination.equalsIgnoreCase("Taloc's Hollow")) {
			goTo(player1, 300190000, 200, 214, 1099);
		} // Udas
		else if (destination.equalsIgnoreCase("Udas") || destination.equalsIgnoreCase("Udas Temple")) {
			goTo(player1, 300150000, 637, 657, 134);
		}
		else if (destination.equalsIgnoreCase("Udas Lower") || destination.equalsIgnoreCase("Udas Lower Temple")) {
			goTo(player1, 300160000, 1146, 277, 116);
		}
		else if (destination.equalsIgnoreCase("Beshmundir") || destination.equalsIgnoreCase("BT") || destination.equalsIgnoreCase("Beshmundir Temple")) {
			goTo(player1, 300170000, 1477, 237, 243);
		} // Padmaraska Cave
		else if (destination.equalsIgnoreCase("Padmaraska Cave")) {
			goTo(player1, 320150000, 385, 506, 66);
		}
		/**
		 * Quest Instance Maps
		 */
		// TODO : Changer id maps
		else if (destination.equalsIgnoreCase("Karamatis 0")) {
			goTo(player1, 310010000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Karamatis 1")) {
			goTo(player1, 310020000, 312, 274, 206);
		}
		else if (destination.equalsIgnoreCase("Karamatis 2")) {
			goTo(player1, 310120000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Aerdina")) {
			goTo(player1, 310030000, 275, 168, 205);
		}
		else if (destination.equalsIgnoreCase("Geranaia")) {
			goTo(player1, 310040000, 275, 168, 205);
		} // Stigma quest
		else if (destination.equalsIgnoreCase("Sliver") || destination.equalsIgnoreCase("Sliver of Darkness")) {
			goTo(player1, 310070000, 247, 249, 1392);
		}
		else if (destination.equalsIgnoreCase("Space") || destination.equalsIgnoreCase("Space of Destiny")) {
			goTo(player1, 320070000, 246, 246, 125);
		}
		else if (destination.equalsIgnoreCase("Ataxiar 1")) {
			goTo(player1, 320010000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Ataxiar 2")) {
			goTo(player1, 320020000, 221, 250, 206);
		}
		else if (destination.equalsIgnoreCase("Bregirun")) {
			goTo(player1, 320030000, 275, 168, 205);
		}
		else if (destination.equalsIgnoreCase("Nidalber")) {
			goTo(player1, 320040000, 275, 168, 205);
		}
		/**
		 * Arenas
		 */
		else if (destination.equalsIgnoreCase("Sanctum Arena")) {
			goTo(player1, 310080000, 275, 242, 159);
		}
		else if (destination.equalsIgnoreCase("Triniel Arena")) {
			goTo(player1, 320090000, 275, 239, 159);
		} // Empyrean Crucible
		else if (destination.equalsIgnoreCase("Crucible 1-0")) {
			goTo(player1, 300300000, 380, 350, 95);
		}
		else if (destination.equalsIgnoreCase("Crucible 1-1")) {
			goTo(player1, 300300000, 346, 350, 96);
		}
		else if (destination.equalsIgnoreCase("Crucible 5-0")) {
			goTo(player1, 300300000, 1265, 821, 359);
		}
		else if (destination.equalsIgnoreCase("Crucible 5-1")) {
			goTo(player1, 300300000, 1256, 797, 359);
		}
		else if (destination.equalsIgnoreCase("Crucible 6-0")) {
			goTo(player1, 300300000, 1596, 150, 129);
		}
		else if (destination.equalsIgnoreCase("Crucible 6-1")) {
			goTo(player1, 300300000, 1628, 155, 126);
		}
		else if (destination.equalsIgnoreCase("Crucible 7-0")) {
			goTo(player1, 300300000, 1813, 797, 470);
		}
		else if (destination.equalsIgnoreCase("Crucible 7-1")) {
			goTo(player1, 300300000, 1785, 797, 470);
		}
		else if (destination.equalsIgnoreCase("Crucible 8-0")) {
			goTo(player1, 300300000, 1776, 1728, 304);
		}
		else if (destination.equalsIgnoreCase("Crucible 8-1")) {
			goTo(player1, 300300000, 1776, 1760, 304);
		}
		else if (destination.equalsIgnoreCase("Crucible 9-0")) {
			goTo(player1, 300300000, 1357, 1748, 320);
		}
		else if (destination.equalsIgnoreCase("Crucible 9-1")) {
			goTo(player1, 300300000, 1334, 1741, 316);
		}
		else if (destination.equalsIgnoreCase("Crucible 10-0")) {
			goTo(player1, 300300000, 1750, 1255, 395);
		}
		else if (destination.equalsIgnoreCase("Crucible 10-1")) {
			goTo(player1, 300300000, 1761, 1280, 395);
		} // Arena Of Chaos
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 1")) {
			goTo(player1, 300350000, 1332, 1078, 340);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 2")) {
			goTo(player1, 300350000, 599, 1854, 227);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 3")) {
			goTo(player1, 300350000, 663, 265, 512);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 4")) {
			goTo(player1, 300350000, 1840, 1730, 302);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 5")) {
			goTo(player1, 300350000, 1932, 1228, 270);
		}
		else if (destination.equalsIgnoreCase("Arena Of Chaos - 6")) {
			goTo(player1, 300350000, 1949, 946, 224);
		}
		/**
		 * Miscellaneous
		 */
		// Prison
		else if (destination.equalsIgnoreCase("Prison LF") || destination.equalsIgnoreCase("Prison Elyos")) {
			goTo(player1, 510010000, 256, 256, 49);
		}
		else if (destination.equalsIgnoreCase("Prison DF") || destination.equalsIgnoreCase("Prison Asmos")) {
			goTo(player1, 520010000, 256, 256, 49);
		} // Test
		else if (destination.equalsIgnoreCase("Test Dungeon")) {
			goTo(player1, 300020000, 104, 66, 25);
		}
		else if (destination.equalsIgnoreCase("Test Basic")) {
			goTo(player, 900020000, 144, 136, 20);
		}
		else if (destination.equalsIgnoreCase("Test Server")) {
			goTo(player1, 900030000, 228, 171, 49);
		}
		else if (destination.equalsIgnoreCase("Test GiantMonster")) {
			goTo(player1, 900100000, 196, 187, 20);
		} // Unknown
		else if (destination.equalsIgnoreCase("IDAbPro")) {
			goTo(player1, 300010000, 270, 200, 206);
		} // GamezNetwork GM zone
		else if (destination.equalsIgnoreCase("gm")) {
			goTo(player1, 120020000, 1457, 1194, 298);
		}
		/**
		 * 2.5 Maps
		 */
		else if (destination.equalsIgnoreCase("Kaisinel Academy")) {
			goTo(player1, 110070000, 459, 251, 128);
		}
		else if (destination.equalsIgnoreCase("Marchutan Priory")) {
			goTo(player1, 120080000, 577, 250, 94);
		}
		else if (destination.equalsIgnoreCase("Esoterrace")) {
			goTo(player1, 300250000, 333, 437, 326);
		}
		/**
		 * 3.0 Maps
		 */
		else if (destination.equalsIgnoreCase("Pernon")) {
			goTo(player1, 710010000, 1069, 1539, 98);
		}
		else if (destination.equalsIgnoreCase("Pernon Studio")) {
			goTo(player1, 710010000, 1197, 2771, 236);
		}
		else if (destination.equalsIgnoreCase("Oriel")) {
			goTo(player1, 700010000, 1261, 1845, 98);
		}
		else if (destination.equalsIgnoreCase("Oriel Studio")) {
			goTo(player1, 700010000, 2569, 1960, 182);
		}
		else if (destination.equalsIgnoreCase("Protectrice")) {
			goTo(player1, 300330000, 250, 246, 124);
		}
		else if (destination.equalsIgnoreCase("Steel Rake Cabin") || destination.equalsIgnoreCase("Steel Rake Solo")) {
			goTo(player1, 300460000, 248, 244, 189);
		}
		else if (destination.equalsIgnoreCase("Aturam") || destination.equalsIgnoreCase("Aturam Sky Fortress")) {
			goTo(player1, 300240000, 636, 446, 655);
		}
		else if (destination.equalsIgnoreCase("Elementis") || destination.equalsIgnoreCase("Elementis Forest")) {
			goTo(player1, 300260000, 176, 612, 231);
		}
		else if (destination.equalsIgnoreCase("Argent") || destination.equalsIgnoreCase("Argent Manor")) {
			goTo(player1, 300270000, 1005, 1089, 70);
		}
		else if (destination.equalsIgnoreCase("Rentus") || destination.equalsIgnoreCase("Rentus Base")) {
			goTo(player1, 300280000, 579, 606, 153);
		}
		else if (destination.equalsIgnoreCase("Raksang")) {
			goTo(player1, 300310000, 665, 735, 1188);
		}
		else if (destination.equalsIgnoreCase("Muada") || destination.equalsIgnoreCase("Muada's Trencher")) {
			goTo(player1, 300380000, 492, 553, 106);
		}
		else if (destination.equalsIgnoreCase("Satra")) {
			goTo(player1, 300470000, 510, 180, 159);
		}
		else if (destination.equalsIgnoreCase("tract") || destination.equalsIgnoreCase("Israphel Tract")) {
			goTo(player1, 300390000, 510, 180, 159);
		}
		else if (destination.equalsIgnoreCase("griffoen") || destination.equalsIgnoreCase("Griffoen")) {
			goTo(player1, 300410000, 492, 553, 106);
		}
		/**
		 * 3.0 Fortress
		 */
		else if (destination.equalsIgnoreCase("Antre tiamat") || destination.equalsIgnoreCase("tiamat2")) {
			goTo(player1, 300520000, 505, 520, 240);
		}
		else if (destination.equalsIgnoreCase("Forto tiamat") || destination.equalsIgnoreCase("tiamat1")) {
			goTo(player1, 300510000, 1581, 1068, 492);
		}
		else if (destination.equalsIgnoreCase("Unstable Abyssal Splinter") || destination.equalsIgnoreCase("Core2")) {
			goTo(player1, 300600000, 704, 153, 453);
		}
		else if (destination.equalsIgnoreCase("harmonyTraining") || destination.equalsIgnoreCase("Harmony Training Grounds")) {
			goTo(player1, 300570000, 500, 371, 211);
		}
		else if (destination.equalsIgnoreCase("glory") || destination.equalsIgnoreCase("Arena Of Glory")) {
			goTo(player1, 300550000, 500, 371, 211);
		}
		/**
		 * 3.7 Instances
		 */
		else if (destination.equalsIgnoreCase("hexway")) {
			goTo(player1, 300700000, 682, 607, 320);
		}
		else if (destination.equalsIgnoreCase("shugotomb")) {
			goTo(player1, 300560000, 178, 234, 543);
		}
		else if (destination.equalsIgnoreCase("unity") || destination.equalsIgnoreCase("Unity Training Grounds")) {
			goTo(player1, 301100000, 500, 371, 211);
		}
		/**
		 * 4.0 fortress
		 */
		else if (destination.equalsIgnoreCase("Silus") || destination.equalsIgnoreCase("5011")) {
			goTo(player1, 600050000, 2019, 1752, 308);
		}
		else if (destination.equalsIgnoreCase("Bassen") || destination.equalsIgnoreCase("6011")) {
			goTo(player1, 600060000, 1472, 740, 67);
		}
		else if (destination.equalsIgnoreCase("Pradeth") || destination.equalsIgnoreCase("6021")) {
			goTo(player1, 600060000, 2586, 2634, 277);
		}
		/**
		 * 4.0 Maps
		 */
		else if (destination.equalsIgnoreCase("katalamely")) {
			goTo(player1, 600050000, 398, 2718, 142);
		}
		else if (destination.equalsIgnoreCase("katalamasmo")) {
			goTo(player1, 600050000, 361, 383, 281);
		}
		else if (destination.equalsIgnoreCase("krerunerk")) {
			goTo(player1, 600050000, 2787, 2577, 259);
		}
		else if (destination.equalsIgnoreCase("mairinerk")) {
			goTo(player1, 600050000, 2870, 291, 296);
		}
		else if (destination.equalsIgnoreCase("forest shadow")) {
			goTo(player1, 600050000, 1696, 83, 112);
		}
		else if (destination.equalsIgnoreCase("portis")) {
			goTo(player1, 600050000, 1943, 2937, 293);
		}
		else if (destination.equalsIgnoreCase("porte nord eremion")) {
			goTo(player1, 600050000, 2914, 1873, 396);
		}
		else if (destination.equalsIgnoreCase("danaria")) {
			goTo(player1, 600060000, 2545, 1699, 141);
		}
		else if (destination.equalsIgnoreCase("danaria nord ely")) {
			goTo(player1, 600060000, 63, 1927, 519);
		}
		else if (destination.equalsIgnoreCase("pepe")) {
			goTo(player1, 600060000, 1018, 2798, 300);
		}
		else if (destination.equalsIgnoreCase("kaberinrinerk")) {
			goTo(player1, 600060000, 91, 2883, 469);
		}
		else if (destination.equalsIgnoreCase("danaria nord asmo")) {
			goTo(player1, 600060000, 58, 1587, 520);
		}
		else if (destination.equalsIgnoreCase("phon")) {
			goTo(player1, 600060000, 816, 275, 465);
		}
		else if (destination.equalsIgnoreCase("wallinerk")) {
			goTo(player1, 600060000, 146, 345, 625);
		}
		else if (destination.equalsIgnoreCase("idian")) {
			goTo(player1, 600070000, 701, 693, 514);
		}
		else if (destination.equalsIgnoreCase("iu")) {
			goTo(player1, 600080000, 1510, 1511, 565);
		}
		/**
		 * 4.3 Camps
		 */
		else if (destination.equalsIgnoreCase("71")) {
			goTo(player1, 600050000, 237, 840, 210);
		}
		else if (destination.equalsIgnoreCase("72")) {
			goTo(player1, 600050000, 138, 2191, 184);
		}
		else if (destination.equalsIgnoreCase("73")) {
			goTo(player1, 600050000, 1102, 496, 184);
		}
		else if (destination.equalsIgnoreCase("74")) {
			goTo(player1, 600050000, 861, 2752, 182);
		}
		else if (destination.equalsIgnoreCase("75")) {
			goTo(player1, 600050000, 1574, 1485, 129);
		}
		else if (destination.equalsIgnoreCase("76")) {
			goTo(player1, 600050000, 1680, 1098, 181);
		}
		else if (destination.equalsIgnoreCase("77")) {
			goTo(player1, 600050000, 1567, 2265, 174);
		}
		else if (destination.equalsIgnoreCase("78")) {
			goTo(player1, 600050000, 1898, 1189, 259);
		}
		else if (destination.equalsIgnoreCase("79")) {
			goTo(player1, 600050000, 2490, 1838, 325);
		}
		else if (destination.equalsIgnoreCase("80")) {
			goTo(player1, 600060000, 967, 1169, 373);
		}
		else if (destination.equalsIgnoreCase("81")) {
			goTo(player1, 600060000, 1040, 1811, 362);
		}
		else if (destination.equalsIgnoreCase("82")) {
			goTo(player1, 600060000, 1044, 2249, 276);
		}
		else if (destination.equalsIgnoreCase("83")) {
			goTo(player1, 600060000, 2805, 596, 275);
		}
		else if (destination.equalsIgnoreCase("84")) {
			goTo(player1, 600060000, 2660, 489, 234);
		}
		else if (destination.equalsIgnoreCase("85")) {
			goTo(player1, 600060000, 1816, 803, 153);
		}
		else if (destination.equalsIgnoreCase("86")) {
			goTo(player1, 600060000, 1520, 1128, 92);
		}
		else if (destination.equalsIgnoreCase("87")) {
			goTo(player1, 600060000, 1339, 2326, 183);
		}
		else if (destination.equalsIgnoreCase("88")) {
			goTo(player1, 600060000, 2132, 2472, 268);
		}
		else if (destination.equalsIgnoreCase("89")) {
			goTo(player1, 600060000, 2606, 2210, 249);
		}
		/**
		 * 4.3 Instances
		 */
		else if (destination.equalsIgnoreCase("mystic") || destination.equalsIgnoreCase("Danuar Mysticarium")) {
			goTo(player1, 300480000, 179, 122, 231);
		}
		else if (destination.equalsIgnoreCase("idgel") || destination.equalsIgnoreCase("Idgel Research Center")) {
			goTo(player1, 300530000, 571, 472, 102);
		}
		else if (destination.equalsIgnoreCase("eternal") || destination.equalsIgnoreCase("Eternal Bastion")) {
			goTo(player1, 300540000, 763, 268, 233);
		}
		else if (destination.equalsIgnoreCase("cube") || destination.equalsIgnoreCase("Void Cube")) {
			goTo(player1, 300580000, 181, 261, 310);
		}
		else if (destination.equalsIgnoreCase("ophidan") || destination.equalsIgnoreCase("Ophidan Bridge")) {
			goTo(player1, 300590000, 760, 561, 572);
		}
		else if (destination.equalsIgnoreCase("infinity") || destination.equalsIgnoreCase("Infinity Shard")) {
			goTo(player1, 300800000, 118, 115, 131);
		}
		else if (destination.equalsIgnoreCase("runadium") || destination.equalsIgnoreCase("Runadium")) {
			goTo(player1, 300900000, 256, 257, 241);
		}
		else if (destination.equalsIgnoreCase("solo") || destination.equalsIgnoreCase("Solo Q")) {
			goTo(player1, 301000000, 500, 500, 500);
		}
		else if (destination.equalsIgnoreCase("steelsolo1") || destination.equalsIgnoreCase("Steel Rose Solo 1st Deck")) {
			goTo(player1, 301010000, 283, 452, 902);
		}
		else if (destination.equalsIgnoreCase("steelsolo2") || destination.equalsIgnoreCase("Steel Rose Solo 2nd Deck")) {
			goTo(player1, 301020000, 236, 506, 948);
		}
		else if (destination.equalsIgnoreCase("steel1") || destination.equalsIgnoreCase("Steel Rose 1st Deck")) {
			goTo(player1, 301030000, 283, 452, 902);
		}
		else if (destination.equalsIgnoreCase("steel2") || destination.equalsIgnoreCase("Steel Rose 2nd Deck")) {
			goTo(player1, 301040000, 236, 506, 948);
		}
		else if (destination.equalsIgnoreCase("steel3") || destination.equalsIgnoreCase("Steel Rose 3rd Deck")) {
			goTo(player1, 301050000, 713, 462, 1015);
		}
		else if (destination.equalsIgnoreCase("reliquary") || destination.equalsIgnoreCase("Danuar Reliquary")) {
			goTo(player1, 301110000, 256, 257, 241);
		}
		else if (destination.equalsIgnoreCase("kamar") || destination.equalsIgnoreCase("Kamar Battlefield")) {
			goTo(player1, 301120000, 1374, 1455, 600);
		}
		else if (destination.equalsIgnoreCase("sauro") || destination.equalsIgnoreCase("Sauro Supply Base")) {
			goTo(player1, 301130000, 641, 176, 195);
		}
		else if (destination.equalsIgnoreCase("danuar") || destination.equalsIgnoreCase("Danuar Sanctuary")) {
			goTo(player1, 301140000, 388, 1184, 55);
		}
		else if (destination.equalsIgnoreCase("circus") || destination.equalsIgnoreCase("Nightmare Circus")) {
			goTo(player1, 301160000, 464, 567, 201);
		}
		else if (destination.equalsIgnoreCase("idgel2") || destination.equalsIgnoreCase("Idgel Research Center (Legion)")) {
			goTo(player1, 301170000, 571, 472, 102);
		}
		else if (destination.equalsIgnoreCase("cube2") || destination.equalsIgnoreCase("Void Cube (Legion)")) {
			goTo(player1, 301180000, 181, 261, 310);
		}
		else if (destination.equalsIgnoreCase("mystic2") || destination.equalsIgnoreCase("Danuar Mysticarium (Legion)")) {
			goTo(player1, 301190000, 179, 122, 231);
		}
		/**
		 * 4.5 Instances
		 */
		else if (destination.equalsIgnoreCase("ophidanwar") || destination.equalsIgnoreCase("Engulfed Ophidan Bridge")) {
			goTo(player1, 301210000, 773, 553, 576);
		}
		else if (destination.equalsIgnoreCase("ironwall") || destination.equalsIgnoreCase("Iron Wall Warfront")) {
			goTo(player1, 301220000, 449, 449, 270);
		}
		else if (destination.equalsIgnoreCase("Illuminary") || destination.equalsIgnoreCase("Illuminary obelisk")) {
			goTo(player1, 301230000, 321, 323, 405);
		}
		/**
		 * 4.7 Instances
		 */
		else if (destination.equalsIgnoreCase("runatorium")) {
			goTo(player1, 301310000, 528, 109, 176);
		}
		else {
			PacketSendUtility.sendMessage(player, " you wrote : " + destination);
			PacketSendUtility.sendMessage(player, "Could not find the specified destination !");
		}
		PacketSendUtility.sendMessage(player, "Player : " + player1.getName() + " teleported to '" + destination + "'");
		PacketSendUtility.sendMessage(player1, "You've been teleported by an Admin.");
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
}
