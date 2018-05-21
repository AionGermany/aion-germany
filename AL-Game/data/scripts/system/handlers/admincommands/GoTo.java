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
			PacketSendUtility.sendMessage(player, "?poeta | ?verteron | ?eltnen | ?theobomos | ?heiron | ?inggison | ?reshanta");
		}
		else if (destination.equalsIgnoreCase("?asmodae")) {
			PacketSendUtility.sendMessage(player, "?ishalgen | ?altgard | ?morheim | ?brusthonin | ?beluslan | ?gelkmaros | ?reshanta");
		}
		/**
		 * Elysea
		 */
		// Sanctum
		else if (destination.equalsIgnoreCase("Sanctum")) {
			goTo(player, WorldMapType.SANCTUM.getId(), 1322, 1511, 568);
		} // Kaisinel
		else if (destination.equalsIgnoreCase("Kaisinel")) {
			goTo(player, WorldMapType.KAISINEL.getId(), 2155, 1567, 1205);
		} // Poeta
		else if (destination.equalsIgnoreCase("?poeta")) {
			PacketSendUtility.sendMessage(player, "Poeta | Melponeh");
		}
		else if (destination.equalsIgnoreCase("Poeta")) {
			goTo(player, WorldMapType.POETA.getId(), 806, 1242, 119);
		}
		else if (destination.equalsIgnoreCase("Melponeh")) {
			goTo(player, WorldMapType.POETA.getId(), 426, 1740, 119);
		} // Verteron
		else if (destination.equalsIgnoreCase("?verteron")) {
			PacketSendUtility.sendMessage(player, "Verteron | Cantas | Ardus | Pilgrims | Tolbas");
		}
		else if (destination.equalsIgnoreCase("Verteron")) {
			goTo(player, WorldMapType.VERTERON.getId(), 1643, 1500, 119);
		}
		else if (destination.equalsIgnoreCase("Cantas") || destination.equalsIgnoreCase("Cantas Coast")) {
			goTo(player, WorldMapType.VERTERON.getId(), 2384, 788, 102);
		}
		else if (destination.equalsIgnoreCase("Ardus") || destination.equalsIgnoreCase("Ardus Shrine")) {
			goTo(player, WorldMapType.VERTERON.getId(), 2333, 1817, 193);
		}
		else if (destination.equalsIgnoreCase("Pilgrims") || destination.equalsIgnoreCase("Pilgrims Respite")) {
			goTo(player, WorldMapType.VERTERON.getId(), 2063, 2412, 274);
		}
		else if (destination.equalsIgnoreCase("Tolbas") || destination.equalsIgnoreCase("Tolbas Village")) {
			goTo(player, WorldMapType.VERTERON.getId(), 1291, 2206, 142);
		} // Eltnen
		else if (destination.equalsIgnoreCase("?eltnen")) {
			PacketSendUtility.sendMessage(player, "Eltnen | Golden | Eltnen Observatory | Novan | Agairon | Kuriullu | Kyola | Sataloc");
		}
		else if (destination.equalsIgnoreCase("Eltnen")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 343, 2724, 264);
		}
		else if (destination.equalsIgnoreCase("Golden") || destination.equalsIgnoreCase("Golden Bough Garrison")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 688, 431, 332);
		}
		else if (destination.equalsIgnoreCase("Eltnen Observatory")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 1779, 883, 422);
		}
		else if (destination.equalsIgnoreCase("Novan")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 947, 2215, 252);
		}
		else if (destination.equalsIgnoreCase("Agairon")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 1921, 2045, 361);
		}
		else if (destination.equalsIgnoreCase("Kuriullu")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 2411, 2724, 361);
		}
		else if (destination.equalsIgnoreCase("Kyola")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 1523, 526, 358);
		}
		else if (destination.equalsIgnoreCase("Sataloc")) {
			goTo(player, WorldMapType.ELTNEN.getId(), 201, 1214, 286);
		} // Theobomos
		else if (destination.equalsIgnoreCase("?theobomos")) {
			PacketSendUtility.sendMessage(player, "Theobomos | Jamanok | Meniherk | obsvillage | Josnack | Anangke");
		}
		else if (destination.equalsIgnoreCase("Theobomos")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 1398, 1557, 31);
		}
		else if (destination.equalsIgnoreCase("Jamanok") || destination.equalsIgnoreCase("Jamanok Inn")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 458, 1257, 127);
		}
		else if (destination.equalsIgnoreCase("Meniherk")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 1396, 1560, 31);
		}
		else if (destination.equalsIgnoreCase("obsvillage")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 2234, 2284, 50);
		}
		else if (destination.equalsIgnoreCase("Josnack")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 901, 2774, 62);
		}
		else if (destination.equalsIgnoreCase("Anangke")) {
			goTo(player, WorldMapType.THEOBOMOS.getId(), 2681, 847, 138);
		} // Heiron
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
		// Pandaemonium
		else if (destination.equalsIgnoreCase("Pandaemonium")) {
			goTo(player, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195);
		} // Marchutran
		else if (destination.equalsIgnoreCase("Marchutan")) {
			goTo(player, WorldMapType.MARCHUTAN.getId(), 1557, 1429, 266);
		} // Ishalgen
		else if (destination.equalsIgnoreCase("?ishalgen")) {
			PacketSendUtility.sendMessage(player, "Ishalgen | Anturon");
		}
		else if (destination.equalsIgnoreCase("Ishalgen")) {
			goTo(player, WorldMapType.ISHALGEN.getId(), 529, 2449, 281);
		}
		else if (destination.equalsIgnoreCase("Anturon")) {
			goTo(player, WorldMapType.ISHALGEN.getId(), 940, 1707, 259);
		} // Altgard
		else if (destination.equalsIgnoreCase("?altgard")) {
			PacketSendUtility.sendMessage(player, "Altgard | Basfelt | Trader | Impetusium | Altgard Observatory");
		}
		else if (destination.equalsIgnoreCase("Altgard")) {
			goTo(player, WorldMapType.ALTGARD.getId(), 1748, 1807, 254);
		}
		else if (destination.equalsIgnoreCase("Basfelt")) {
			goTo(player, WorldMapType.ALTGARD.getId(), 1903, 696, 260);
		}
		else if (destination.equalsIgnoreCase("Trader")) {
			goTo(player, WorldMapType.ALTGARD.getId(), 2680, 1024, 311);
		}
		else if (destination.equalsIgnoreCase("Impetusium")) {
			goTo(player, WorldMapType.ALTGARD.getId(), 2643, 1658, 324);
		}
		else if (destination.equalsIgnoreCase("Altgard Observatory")) {
			goTo(player, WorldMapType.ALTGARD.getId(), 1468, 2560, 299);
		} // Morheim
		else if (destination.equalsIgnoreCase("?morheim")) {
			PacketSendUtility.sendMessage(player, "Morheim | Desert | Slag | Kellan | Alsig | Morheim Observatory | Halabana");
		}
		else if (destination.equalsIgnoreCase("Morheim")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 308, 2274, 449);
		}
		else if (destination.equalsIgnoreCase("Desert")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 634, 900, 360);
		}
		else if (destination.equalsIgnoreCase("Slag")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 1772, 1662, 197);
		}
		else if (destination.equalsIgnoreCase("Kellan")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 1070, 2486, 239);
		}
		else if (destination.equalsIgnoreCase("Alsig")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 2387, 1742, 102);
		}
		else if (destination.equalsIgnoreCase("Morheim Observatory")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 2794, 1122, 171);
		}
		else if (destination.equalsIgnoreCase("Halabana")) {
			goTo(player, WorldMapType.MORHEIM.getId(), 2346, 2219, 127);
		} // Brusthonin
		else if (destination.equalsIgnoreCase("?brusthonin")) {
			PacketSendUtility.sendMessage(player, "Brusthonin | Baltasar | Bollu | Edge | Bubu | Settlers");
		}
		else if (destination.equalsIgnoreCase("Brusthonin")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 2917, 2421, 15);
		}
		else if (destination.equalsIgnoreCase("Baltasar")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 1413, 2013, 51);
		}
		else if (destination.equalsIgnoreCase("Bollu")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 840, 2016, 307);
		}
		else if (destination.equalsIgnoreCase("Edge")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 1523, 374, 231);
		}
		else if (destination.equalsIgnoreCase("Bubu")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 526, 848, 76);
		}
		else if (destination.equalsIgnoreCase("Settlers")) {
			goTo(player, WorldMapType.BRUSTHONIN.getId(), 2917, 2417, 15);
		} // Beluslan
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
		// Inggison
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
		} // Gelkmaros
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
		} // Silentera
		else if (destination.equalsIgnoreCase("Silentera")) {
			goTo(player, 600010000, 583, 767, 300);
		}
		/**
		 * Abyss
		 */
		else if (destination.equalsIgnoreCase("?reshanta")) {
			PacketSendUtility.sendMessage(player, "Teminon | Primum | Tigraki | Magos | Tokanu | Leibos");
		}
		else if (destination.equalsIgnoreCase("Teminon")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2256, 667, 1527); // 5.3
		}
		else if (destination.equalsIgnoreCase("Primum")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 583, 2535, 1636); // 5.3
		}
		else if (destination.equalsIgnoreCase("Tigraki")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 539, 1100, 2843);
		}
		else if (destination.equalsIgnoreCase("Magos")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 1769, 139, 2938); // 5.3
		}
		else if (destination.equalsIgnoreCase("Tokanu")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 989, 2883, 3033); // 5.3
		}
		else if (destination.equalsIgnoreCase("Leibos")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2136, 1943, 1597);
		}
		/**
		 * Sieges
		 */
		// Abyss
		else if (destination.equalsIgnoreCase("Soufre") || destination.equalsIgnoreCase("1141")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 1379, 1187, 1537);
		}
		else if (destination.equalsIgnoreCase("Siel occi") || destination.equalsIgnoreCase("1131")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2792, 2609, 1504);
		}
		else if (destination.equalsIgnoreCase("Siel ori") || destination.equalsIgnoreCase("1132")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2608, 2853, 1530);
		}
		else if (destination.equalsIgnoreCase("Roah") || destination.equalsIgnoreCase("1211")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2735, 801, 2894);
		}
		else if (destination.equalsIgnoreCase("Asteria") || destination.equalsIgnoreCase("1251")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 722, 2961, 2921);
		}
		else if (destination.equalsIgnoreCase("Krotan") || destination.equalsIgnoreCase("1221")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2057, 1275, 2987);
		}
		else if (destination.equalsIgnoreCase("Kysis") || destination.equalsIgnoreCase("1231")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 2506, 2109, 3074);
		}
		else if (destination.equalsIgnoreCase("Miren") || destination.equalsIgnoreCase("1241")) {
			goTo(player, WorldMapType.RESHANTA.getId(), 1789, 2269, 2951);
		} // Balaurea
		else if (destination.equalsIgnoreCase("Avidite") || destination.equalsIgnoreCase("2011")) {
			goTo(player, WorldMapType.INGGISON.getId(), 887, 1979, 341);
		}
		else if (destination.equalsIgnoreCase("Dragon") || destination.equalsIgnoreCase("2021")) {
			goTo(player, WorldMapType.INGGISON.getId(), 1729, 2236, 329);
		}
		else if (destination.equalsIgnoreCase("Vorgaltem") || destination.equalsIgnoreCase("3011")) {
			goTo(player, WorldMapType.GELKMAROS.getId(), 1198, 806, 314);
		}
		else if (destination.equalsIgnoreCase("Pourpre") || destination.equalsIgnoreCase("3021")) {
			goTo(player, WorldMapType.GELKMAROS.getId(), 1882, 1042, 331);
		}
		/**
		 * Instances
		 */
		else if (destination.equalsIgnoreCase("Haramel")) {
			goTo(player, 300200000, 176, 21, 144);
		}
		else if (destination.equalsIgnoreCase("Nochsana") || destination.equalsIgnoreCase("NTC")) {
			goTo(player, 300030000, 513, 668, 331);
		}
		else if (destination.equalsIgnoreCase("Arcanis") || destination.equalsIgnoreCase("Sky Temple of Arcanis")) {
			goTo(player, 320050000, 177, 229, 536);
		}
		else if (destination.equalsIgnoreCase("Fire Temple") || destination.equalsIgnoreCase("FT")) {
			goTo(player, 320100000, 144, 312, 123);
		}
		else if (destination.equalsIgnoreCase("Kromede") || destination.equalsIgnoreCase("Kromede Trial")) {
			goTo(player, 300230000, 248, 244, 189);
		} // Steel Rake
		else if (destination.equalsIgnoreCase("Steel Rake") || destination.equalsIgnoreCase("SR")) {
			goTo(player, 300100000, 237, 506, 948);
		}
		else if (destination.equalsIgnoreCase("Steel Rake Lower") || destination.equalsIgnoreCase("SR Low")) {
			goTo(player, 300100000, 283, 453, 903);
		}
		else if (destination.equalsIgnoreCase("Steel Rake Middle") || destination.equalsIgnoreCase("SR Mid")) {
			goTo(player, 300100000, 283, 453, 953);
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
		else if (destination.equalsIgnoreCase("Adma") || destination.equalsIgnoreCase("Adma Stronghold")) {
			goTo(player, 320130000, 450, 200, 168);
		}
		else if (destination.equalsIgnoreCase("Alquimia") || destination.equalsIgnoreCase("Alquimia Research Center")) {
			goTo(player, 320110000, 603, 527, 200);
		}
		else if (destination.equalsIgnoreCase("Draupnir") || destination.equalsIgnoreCase("Draupnir Cave")) {
			goTo(player, 320080000, 491, 373, 622);
		}
		else if (destination.equalsIgnoreCase("Theobomos Lab") || destination.equalsIgnoreCase("Theobomos Research Lab")) {
			goTo(player, 310110000, 477, 201, 170);
		}
		else if (destination.equalsIgnoreCase("Dark Poeta") || destination.equalsIgnoreCase("DP")) {
			goTo(player, 300040000, 1214, 412, 140);
		} // Lower Abyss
		else if (destination.equalsIgnoreCase("Sulfur") || destination.equalsIgnoreCase("Sulfur Tree Nest")) {
			goTo(player, 300060000, 462, 345, 163);
		}
		else if (destination.equalsIgnoreCase("Right Wing") || destination.equalsIgnoreCase("Right Wing Chamber")) {
			goTo(player, 300090000, 263, 386, 103);
		}
		else if (destination.equalsIgnoreCase("Left Wing") || destination.equalsIgnoreCase("Left Wing Chamber")) {
			goTo(player, 300080000, 672, 606, 321);
		} // Upper Abyss
		else if (destination.equalsIgnoreCase("Asteria Chamber")) {
			goTo(player, 300050000, 467.95233f, 566.6705f, 201.70262f);
		}
		else if (destination.equalsIgnoreCase("Miren Chamber")) {
			goTo(player, 300130000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Chamber")) {
			goTo(player, 300120000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Chamber")) {
			goTo(player, 300140000, 528, 109, 176);
		}
		else if (destination.equalsIgnoreCase("Roah Chamber")) {
			goTo(player, 300070000, 504, 396, 94);
		}
		else if (destination.equalsIgnoreCase("Miren Barrack")) {
			goTo(player, 301290000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Barrack")) {
			goTo(player, 301280000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Barrack")) {
			goTo(player, 301300000, 528, 109, 176);
		}
		else if (destination.equalsIgnoreCase("Miren Barrack Legion")) {
			goTo(player, 301250000, 527, 120, 176);
		}
		else if (destination.equalsIgnoreCase("Kysis Barrack Legion")) {
			goTo(player, 301240000, 528, 121, 176);
		}
		else if (destination.equalsIgnoreCase("Krotan Barrack Legion")) {
			goTo(player, 301260000, 528, 109, 176);
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
		else if (destination.equalsIgnoreCase("Taloc") || destination.equalsIgnoreCase("Taloc's Hollow")) {
			goTo(player, 300190000, 200, 214, 1099);
		} // Udas
		else if (destination.equalsIgnoreCase("Udas") || destination.equalsIgnoreCase("Udas Temple")) {
			goTo(player, 300150000, 637, 657, 134);
		}
		else if (destination.equalsIgnoreCase("Udas Lower") || destination.equalsIgnoreCase("Udas Lower Temple")) {
			goTo(player, 300160000, 1146, 277, 116);
		}
		else if (destination.equalsIgnoreCase("Beshmundir") || destination.equalsIgnoreCase("BT") || destination.equalsIgnoreCase("Beshmundir Temple")) {
			goTo(player, 300170000, 1477, 237, 243);
		} // Padmaraska Cave
		else if (destination.equalsIgnoreCase("Padmaraska Cave")) {
			goTo(player, 320150000, 385, 506, 66);
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
		 * 2.5 Maps
		 */
		else if (destination.equalsIgnoreCase("Kaisinel Academy")) {
			goTo(player, 110070000, 459, 251, 128);
		}
		else if (destination.equalsIgnoreCase("Marchutan Priory")) {
			goTo(player, 120080000, 577, 250, 94);
		}
		else if (destination.equalsIgnoreCase("Esoterrace")) {
			goTo(player, 300250000, 333, 437, 326);
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
		 * 3.0 Instances
		 */
		else if (destination.equalsIgnoreCase("Steel Rake Cabin") || destination.equalsIgnoreCase("Steel Rake Solo")) {
			goTo(player, 300460000, 248, 244, 189);
		}
		/**
		 * 3.5 Instance
		 */
		else if (destination.equalsIgnoreCase("refuge") || destination.equalsIgnoreCase("Dragon Lord Refuge")) {
			goTo(player, 300520000, 505, 520, 240);
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
		/**
		 * 3.7 Instances
		 */
		else if (destination.equalsIgnoreCase("hexway")) {
			goTo(player, 300700000, 682, 607, 320);
		}
		else if (destination.equalsIgnoreCase("shugotomb")) {
			goTo(player, 300560000, 178, 234, 543);
		}
		else if (destination.equalsIgnoreCase("unity") || destination.equalsIgnoreCase("Unity Training Grounds")) {
			goTo(player, 301100000, 500, 371, 211);
		}
		/**
		 * 4.3 Instances
		 */
		else if (destination.equalsIgnoreCase("mystic") || destination.equalsIgnoreCase("Danuar Mysticarium")) {
			goTo(player, 300480000, 179, 122, 231);
		}
		else if (destination.equalsIgnoreCase("eternal") || destination.equalsIgnoreCase("Steel Wall Bastion")) {
			goTo(player, 300540000, 763, 268, 233);
		}
		else if (destination.equalsIgnoreCase("ophidan") || destination.equalsIgnoreCase("Ophidan Bridge")) {
			goTo(player, 300590000, 760, 561, 572);
		}
		else if (destination.equalsIgnoreCase("infinity") || destination.equalsIgnoreCase("Infinity Shard")) {
			goTo(player, 300800000, 118, 115, 131);
		}
		else if (destination.equalsIgnoreCase("reliquary") || destination.equalsIgnoreCase("Danuar Reliquary")) {
			goTo(player, 301110000, 256, 257, 241);
		}
		else if (destination.equalsIgnoreCase("kamar") || destination.equalsIgnoreCase("Kamar Battlefield")) {
			goTo(player, 301120000, 1374, 1455, 600);
		}
		else if (destination.equalsIgnoreCase("sauro") || destination.equalsIgnoreCase("Sauro Supply Base")) {
			goTo(player, 301130000, 641, 176, 195);
		}
		else if (destination.equalsIgnoreCase("danuar") || destination.equalsIgnoreCase("Danuar Sanctuary")) {
			goTo(player, 301380000, 388.8532f, 1183.4175f, 55.30134f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("circus") || destination.equalsIgnoreCase("Nightmare Circus")) {
			goTo(player, 301160000, 464, 567, 201);
		}
		/**
		 * 4.5 Instances
		 */
		else if (destination.equalsIgnoreCase("jormungand") || destination.equalsIgnoreCase("Jormungand Marching Route")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 301210000, 322, 490, 596);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 301210000, 758, 560, 576);
			}
		}
		else if (destination.equalsIgnoreCase("steelwall") || destination.equalsIgnoreCase("Steel Wall Bastion Battlefield")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 301220000, 400, 166, 432);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 301220000, 570, 166, 432);
			}
		}
		else if (destination.equalsIgnoreCase("Illuminary") || destination.equalsIgnoreCase("Illuminary Obelisk")) {
			goTo(player, 301230000, 321, 323, 405);
		}
		/**
		 * 4.7
		 */
		else if (destination.equalsIgnoreCase("kaldor") || destination.equalsIgnoreCase("Kaldor")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 600090000, 1268, 1333, 194);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 600090000, 397, 1380, 163);
			}
		}
		else if (destination.equalsIgnoreCase("levinshor") || destination.equalsIgnoreCase("Levinshor")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 600100000, 207, 183, 374);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 600100000, 1842, 1782, 305);
			}
		}
		else if (destination.equalsIgnoreCase("pangea") || destination.equalsIgnoreCase("Belus")) { // Belus
			goTo(player, 400020000, 1238, 1232, 1518);
		}
		else if (destination.equalsIgnoreCase("annex") || destination.equalsIgnoreCase("Transidium Annex")) { // Transidium Annex
			goTo(player, 400030000, 509, 513, 675);
		}
		else if (destination.equalsIgnoreCase("pangea2") || destination.equalsIgnoreCase("Aspida")) { // Aspida
			goTo(player, 400040000, 1238, 1232, 1518);
		}
		else if (destination.equalsIgnoreCase("pangea3") || destination.equalsIgnoreCase("Atanatos")) { // Atanatos
			goTo(player, 400050000, 1238, 1232, 1518);
		}
		else if (destination.equalsIgnoreCase("pangea4") || destination.equalsIgnoreCase("Disillon")) { // Disillon
			goTo(player, 400060000, 1238, 1232, 1518);
		}
		else if (destination.equalsIgnoreCase("wisplight") || destination.equalsIgnoreCase("Elyos.WisplightAbbey")) { // Wisplight Abbey (Elyos) - Not logged in for 30-days
			goTo(player, 130090000, 247, 236, 129);
		}
		else if (destination.equalsIgnoreCase("fatebound") || destination.equalsIgnoreCase("Asmod.FateboundAbbey")) { // Fatebound Abbey (Asmodian) - Not logged in for 30-days
			goTo(player, 140010000, 272, 266, 96);
		}
		/**
		 * 4.7 Instances
		 */
		else if (destination.equalsIgnoreCase("shugotomb2") || destination.equalsIgnoreCase("Shugo Emperor's Vault")) { // Shugo Emperor's Vault
			goTo(player, 301400000, 545.5205f, 295.1831f, 400.29556f, (byte) 30);
		}
		else if (destination.equalsIgnoreCase("baruna") || destination.equalsIgnoreCase("LinkgateFoundry")) { // Linkgate Foundry
			goTo(player, 301270000, 289, 216, 311);
		}
		else if (destination.equalsIgnoreCase("runatorium") || destination.equalsIgnoreCase("Runatorium")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 301310000, 270.1437f, 348.6699f, 79.44365f, (byte) 105);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 301310000, 258.5553f, 169.85149f, 79.430855f, (byte) 45);
			}
		}
		/**
		 * 4.8 Maps
		 */
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
		/**
		 * 4.8 Instances
		 */
		else if (destination.equalsIgnoreCase("raksangruins")) { // Raksang Ruins
			goTo(player, 300610000, 829.7167f, 942.56274f, 1207.4312f, (byte) 75);
		}
		else if (destination.equalsIgnoreCase("rentusrefuge")) { // Rentus Base Refuge
			goTo(player, 300620000, 557.61523f, 593.1484f, 154.125f, (byte) 73);
		}
		else if (destination.equalsIgnoreCase("dragonlord")) { // Anguished Dragon Lord�s Refuge
			goTo(player, 300630000, 509.55295f, 518.0558f, 240.26651f);
		}
		else if (destination.equalsIgnoreCase("dragonsphire")) { // Dragonsphire Depths
			goTo(player, 301390000, 321.51364f, 183.10413f, 1687.2552f);
		}
		else if (destination.equalsIgnoreCase("Aturam") || destination.equalsIgnoreCase("Aturam Sky Fortress")) {
			goTo(player, 300240000, 694.3073f, 456.0754f, 655.7797f, (byte) 53);
		}
		else if (destination.equalsIgnoreCase("Rentus") || destination.equalsIgnoreCase("Rentus Base")) {
			goTo(player, 300280000, 557.61523f, 593.1484f, 154.125f, (byte) 73);
		}
		else if (destination.equalsIgnoreCase("seizeddanuar") || destination.equalsIgnoreCase("Seized Danuar Sanctuary")) {
			goTo(player, 301140000, 388.8532f, 1183.4175f, 55.30134f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("idian") || destination.equalsIgnoreCase("Idian Dephts")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 210090000, 671.0f, 646.0f, 514.8738f);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 220100000, 671.0f, 646.0f, 514.8738f);
			}
		}
		/**
		 * 4.8 only for Tests
		 */
		else if (destination.equalsIgnoreCase("lf5")) {
			goTo(player, 210080000, 263.2868f, 194.33182f, 499.01782f);
		}
		else if (destination.equalsIgnoreCase("df5")) {
			goTo(player, 220090000, 253.55469f, 155.04338f, 503.6178f);
		}
		else if (destination.equalsIgnoreCase("reach") || destination.equalsIgnoreCase("Stonespear Reach")) {
			goTo(player, 301500000, 149.65219f, 264.2412f, 97.454155f);
		}
		/**
		 * 4.9 Maps
		 */
		else if (destination.equalsIgnoreCase("manor") || destination.equalsIgnoreCase("Argent Manor")) {
			goTo(player, 301510000, 993.69604f, 1206.2352f, 65.640015f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("dragonsphirequest")) {
			goTo(player, 301520000, 321.51364f, 183.10413f, 1687.2552f);
		}
		/**
		 * 5.0 Maps
		 */
		else if (destination.equalsIgnoreCase("esterra") || destination.equalsIgnoreCase("Esterra")) { // Elyos
			goTo(player, 210100000, 1422.5951f, 1257.2947f, 336.66037f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("nosra") || destination.equalsIgnoreCase("Nosra")) { // Asmodians
			goTo(player, 220110000, 1789.5037f, 1994.3834f, 198.30061f, (byte) 46);
		}
		/**
		 * 5.0 Instances
		 */
		else if (destination.equalsIgnoreCase("library") || destination.equalsIgnoreCase("Library of Knowledge")) {
			goTo(player, 301540000, 750.30023f, 513.66595f, 469.02893f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("library_quest") || destination.equalsIgnoreCase("Library of Knowledge Quest")) {
			goTo(player, 301570000, 683.4529f, 433.97238f, 468.86707f, (byte) 63);
		}
		else if (destination.equalsIgnoreCase("sanctuary_quest") || destination.equalsIgnoreCase("Sanctuary Dungeon Quest")) {
			goTo(player, 301580000, 432.54764f, 492.83694f, 99.59915f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("shugotomb3") || destination.equalsIgnoreCase("Fire Temple of Memory")) {
			goTo(player, 302000000, 147.39536f, 455.12192f, 141.88219f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("admas_fall") || destination.equalsIgnoreCase("Adma Ruins")) {
			goTo(player, 301600000, 492.9464f, 465.5458f, 173.486f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("theobomos_test") || destination.equalsIgnoreCase("Elemental Lord's Laboratory")) {
			goTo(player, 301610000, 215.1005f, 375.422f, 202.84338f, (byte) 100);
		}
		else if (destination.equalsIgnoreCase("drakenseers") || destination.equalsIgnoreCase("Drakenseers_Lair")) {
			goTo(player, 301620000, 272.85535f, 350.14633f, 336.43332f, (byte) 90);
		}
		else if (destination.equalsIgnoreCase("runatorium_ruins") || destination.equalsIgnoreCase("Runatorium Ruins")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 301680000, 258.5553f, 169.85149f, 79.430855f, (byte) 45);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 301680000, 270.1437f, 348.6699f, 79.44365f, (byte) 105);
			}
		}
		/**
		 * 5.1 Instances
		 */
		else if (destination.equalsIgnoreCase("hell_pass") || destination.equalsIgnoreCase("Hell Pass")) {
			goTo(player, 301630000, 229.00113f, 171.8445f, 164.60031f, (byte) 20);
		}
		else if (destination.equalsIgnoreCase("weapon_factory") || destination.equalsIgnoreCase("Mechanerks Weapons Factory")) {
			goTo(player, 301640000, 400.3279f, 290.5061f, 198.64015f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("garden") || destination.equalsIgnoreCase("Garden of Knowledge")) {
			goTo(player, 301550000, 1470.9073f, 766.46826f, 1035.2672f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("ashunatal") || destination.equalsIgnoreCase("Ashunatal Dredgion")) {
			goTo(player, 301650000, 414, 193, 431);
		}
		else if (destination.equalsIgnoreCase("kroban") || destination.equalsIgnoreCase("Kroban Base")) {
			goTo(player, 301660000, 306.62512f, 909.52423f, 105.5561f, (byte) 45);
		}
		else if (destination.equalsIgnoreCase("rift") || destination.equalsIgnoreCase("Rift of Oblivion")) {
			goTo(player, 302100000, 919.4777f, 460.33365f, 352.96f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("marching_route") || destination.equalsIgnoreCase("Balaur Marching Route")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 301670000, 764.24976f, 577.43243f, 578.3397f, (byte) 90);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 301670000, 764.24976f, 577.43243f, 578.3397f, (byte) 90); // TODO
			}
		}
		else if (destination.equalsIgnoreCase("sanctum_battlefield") || destination.equalsIgnoreCase("Sanctum Battlefield")) {
			goTo(player, 302200000, 1391.4268f, 1692.0596f, 573.2861f, (byte) 105);
		}
		else if (destination.equalsIgnoreCase("pandaemonium_battlefield") || destination.equalsIgnoreCase("Pandaemonium Battlefield")) {
			goTo(player, 302300000, 1240.918f, 1177.3456f, 214.66058f, (byte) 15);
		}
		else if (destination.equalsIgnoreCase("kumuki") || destination.equalsIgnoreCase("Kumuki Hideout")) {
			goTo(player, 302330000, 173.6281f, 20.820198f, 144.22491f, (byte) 60);
		}
		/**
		 * 5.3 Golden Arena
		 */
		else if (destination.equalsIgnoreCase("golden_arena") || destination.equalsIgnoreCase("Golden Arena")) {
			if (player.getRace() == Race.ELYOS) {
				goTo(player, 302320000, 383.37f, 224.3448f, 231.12335f, (byte) 30);
			}
			else if (player.getRace() == Race.ASMODIANS) {
				goTo(player, 302320000, 386.1356f, 283.4616f, 231.1234f, (byte) 90);
			}
		}
		/**
		 * 5.3 Aether Mine Elyos (Quest Instance)
		 */
		else if (destination.equalsIgnoreCase("aether_mine") || destination.equalsIgnoreCase("Aether Mine")) {
			goTo(player, 301690000, 323.2161f, 267.80365f, 259.34897f, (byte) 90);
		}
		/**
		 * 5.6 Instances
		 */
		else if (destination.equalsIgnoreCase("museum") || destination.equalsIgnoreCase("Museum of Knowledge")) {
			goTo(player, 301560000, 1244.3882f, 1026.1548f, 761.20197f, (byte) 60);
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
		/**
		 * 5.8 Instances
		 */
		else if (destination.equalsIgnoreCase("treasure") || destination.equalsIgnoreCase("Treasure Island of Courage")) {
			goTo(player, 301700000, 1370.3208f, 1381.8193f, 375.19562f, (byte) 60);
		}
		else if (destination.equalsIgnoreCase("mirash") || destination.equalsIgnoreCase("Mirash Sanctum")) {
			goTo(player, 301720000, 770.9657f, 831.978f, 520.6439f, (byte) 60);
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
