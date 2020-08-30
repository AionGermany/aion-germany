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
package world;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.handlers.GeneralWorldHandler;
import com.aionemu.gameserver.world.handlers.WorldID;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

@WorldID(210040000)
public class Heiron extends GeneralWorldHandler {

	private int heironHunting;

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		case 280416: // Deputy Hanuman Faithful Subordinate
		case 280417: // Deputy Hanuman Faithful Subordinate
		case 280771: // Underling Of Agro
		case 280775: // Power Of Exedil
		case 280780: // Dedrun Fragment
		case 280788: // Medeus Drake
		case 280802: // Thirsting Bloodwing
		case 280803: // Vicious Bloodwing
		case 280804: // Cruel Vampire
		case 280814: // Geschphalt Minion
		case 653085: // Indratu Grunt
			despawnNpc(npc);
			break;
		default:
			if (npc.getTarget() instanceof Player) {
				heironHunting++;
				if (heironHunting == 250) {
					heironHunting = 0;
					switch (Rnd.get(1, 25)) {
					case 1:
						ItemService.addItem(player, 188073315, 1); // Legendary Punishing Weapon Box
						break;
					case 2:
						ItemService.addItem(player, 188073316, 1); // Legendary Unyielding Weapon Box
						break;
					case 3:
						ItemService.addItem(player, 188073317, 1); // Legendary Strident Weapon Box
						break;
					case 4:
						ItemService.addItem(player, 188073318, 1); // Legendary Kromede's Weapon Box
						break;
					case 5:
						ItemService.addItem(player, 188073319, 1); // Legendary Weapon Box Of Wrath
						break;
					case 6:
						ItemService.addItem(player, 188073320, 1); // Legendary Weapon Box Of Conquest
						break;
					case 7:
						ItemService.addItem(player, 188073390, 1); // Legendary Kromede's Armor Box
						break;
					case 8:
						ItemService.addItem(player, 188900063, 5); // Experience Crystal
						break;
					case 9:
						ItemService.addItem(player, 188070376, 2); // Golden Box Of Minion Contracts
						break;
					case 10:
						ItemService.addItem(player, 190120147, 1); // Solar Unicorn - Superior Recovery
						break;
					case 11:
						ItemService.addItem(player, 166023100, 5); // Ancient PvE Enchantment Stone
						break;
					case 12:
						ItemService.addItem(player, 166023101, 5); // Legendary PvE Enchantment Stone
						break;
					case 13:
						ItemService.addItem(player, 166023102, 5); // Ultimate PvE Enchantment Stone
						break;
					case 14:
						ItemService.addItem(player, 186020002, 50); // Gold Ingots
						break;
					case 15:
						ItemService.addItem(player, 166401000, 1000); // Manastone Fastener
						break;
					case 16:
						ItemService.addItem(player, 188071961, 1); // Luna Wooden Box (200)
						break;
					case 17:
						ItemService.addItem(player, 188073017, 1); // Ultimate Transformation Box
						break;
					case 18:
						ItemService.addItem(player, 188070768, 1); // Ancient Daevanion Skill Box
						break;
					case 19:
						ItemService.addItem(player, 188070330, 1); // Legendary Daevanion Skill Box
						break;
					case 20:
						ItemService.addItem(player, 169610397, 1); // [Title Card] Special Daeva 180 Day Pass
						break;
					case 21:
						ItemService.addItem(player, 169020003, 50000); // Shard Bundle
						break;
					case 22:
						ItemService.addItem(player, 188070351, 10); // Stigma Enchantment Stone Bundle
						break;
					case 23:
						ItemService.addItem(player, 188071672, 10); // Lugbug's Cubicle Box
						break;
					case 24:
						ItemService.addItem(player, 190095008, 10); // Legendary Transformation Contract
						break;
					case 25:
						ItemService.addItem(player, 188071059, 1); // Shining Skill Card Bundle
						break;
					}
				}
			}
			break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
		case 730033: // Destroyed Teleport Statue
			destroyedStatue(player, 208.0000f, 2709.0000f, 141.0000f, (byte) 65);
			break;
		case 730040: // Secret Passage Of Indratu Legion
			if (player.getLevel() >= 60) {
				indratuLegionIn(player, 2216.0000f, 2487.0000f, 182.0000f, (byte) 56);
			} else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_DIRECT_PORTAL_LEVEL_LIMIT);
			}
			break;
		}
	}

	protected void destroyedStatue(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, 1, x, y, z, h);
	}

	protected void indratuLegionIn(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, 1, x, y, z, h);
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@SuppressWarnings("unused")
	private void despawnNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			npc.getController().onDelete();
		}
	}

	protected List<Npc> getNpcs(int npcId) {
		List<Npc> npcs = new FastList<Npc>();
		for (Npc npc : this.map.getWorld().getNpcs()) {
			if (npc.getNpcId() == npcId) {
				npcs.add(npc);
			}
		}
		return npcs;
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getWorldId() == map.getMapId() && player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
}
