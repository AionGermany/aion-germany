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
package playercommands;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestross
 */
public class cmd_start extends PlayerCommand {

	public cmd_start() {
		super("start");
	}

	@Override
	public void execute(final Player player, String... params) {
		if (player.getPlayerClass() == PlayerClass.ASSASSIN) {
			ItemService.addItem(player, 110300795, 1); //
			ItemService.addItem(player, 113300778, 1); //
			ItemService.addItem(player, 114300793, 1); //
			ItemService.addItem(player, 112300712, 1); //
			ItemService.addItem(player, 111300753, 1); //
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 125001715, 1); // Archon Tribunus's Hat
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 100200807, 2); // Stormwing's Dagger
			ItemService.addItem(player, 100001080, 1); // Stormwing Scale Sword
			ItemService.addItem(player, 100000618, 1); // Archon Tribunus's Sword
			ItemService.addItem(player, 100200589, 2); // Archon Tribunus's Dagger
		}
		else if (player.getPlayerClass() == PlayerClass.CHANTER) {
			ItemService.addItem(player, 114500759, 1); //
			ItemService.addItem(player, 110500771, 1); //
			ItemService.addItem(player, 111500740, 1); //
			ItemService.addItem(player, 113500751, 1); //
			ItemService.addItem(player, 112500700, 1); //
			ItemService.addItem(player, 125001716, 1); // Archon Tribunus's Chain Helm
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 101500698, 1); // Stormwing's Staff
			ItemService.addItem(player, 115000967, 1); // Stormwing Scale Shield
			ItemService.addItem(player, 100100676, 1); // Stormwing's Warhammer
			ItemService.addItem(player, 100100482, 1); // Archon Tribunus's Warhammer
			ItemService.addItem(player, 115000765, 1); // Archon Tribunus's Shield
			ItemService.addItem(player, 101500481, 1); // Archon Tribunus's Staff
		}
		else if (player.getPlayerClass() == PlayerClass.CLERIC) {
			ItemService.addItem(player, 114500759, 1); //
			ItemService.addItem(player, 110500771, 1); //
			ItemService.addItem(player, 111500740, 1); //
			ItemService.addItem(player, 113500751, 1); //
			ItemService.addItem(player, 112500700, 1); //
			ItemService.addItem(player, 125001716, 1); // Archon Tribunus's Chain Helm
			ItemService.addItem(player, 122000852, 2); // Archon Tribunus's Sapphire Ring
			ItemService.addItem(player, 120000803, 2); // Archon Tribunus's Sapphire Earrings
			ItemService.addItem(player, 121000732, 1); // Archon Tribunus's Sapphire Necklace
			ItemService.addItem(player, 123000845, 1); // Archon Tribunus's Belt
			ItemService.addItem(player, 101500698, 1); // Stormwing's Staff
			ItemService.addItem(player, 115000967, 1); // Stormwing Scale Shield
			ItemService.addItem(player, 100100676, 1); // Stormwing's Warhammer
			ItemService.addItem(player, 100100482, 1); // Archon Tribunus's Warhammer
			ItemService.addItem(player, 115000765, 1); // Archon Tribunus's Shield
			ItemService.addItem(player, 101500481, 1); // Archon Tribunus's Staff
		}
		else if (player.getPlayerClass() == PlayerClass.GLADIATOR) {
			ItemService.addItem(player, 114600719, 1); //
			ItemService.addItem(player, 111600732, 1); //
			ItemService.addItem(player, 113600725, 1); //
			ItemService.addItem(player, 110600759, 1); //
			ItemService.addItem(player, 112600708, 1); //
			ItemService.addItem(player, 125001717, 1); // Archon Tribunus's Helm
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 115000966, 1); // Stormwing's Shield
			ItemService.addItem(player, 100000893, 2); // Stormwing's Azure Scale Sword
			ItemService.addItem(player, 101300655, 1); // Stormwing's Spear
			ItemService.addItem(player, 100900684, 1); // Stormwing's Greatsword
			ItemService.addItem(player, 115000765, 1); // Archon Tribunus's Shield
			ItemService.addItem(player, 100000618, 2); // Archon Tribunus's Sword
			ItemService.addItem(player, 100900475, 1); // Archon Tribunus's Greatsword
			ItemService.addItem(player, 101300464, 1); // Archon Tribunus's Spear
		}
		else if (player.getPlayerClass() == PlayerClass.RANGER) {
			ItemService.addItem(player, 110300795, 1); //
			ItemService.addItem(player, 113300778, 1); //
			ItemService.addItem(player, 114300793, 1); //
			ItemService.addItem(player, 112300712, 1); //
			ItemService.addItem(player, 111300753, 1); //
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 125001715, 1); // Archon Tribunus's Hat
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 100200807, 2); // Stormwing's Dagger
			ItemService.addItem(player, 101700722, 1); // Stormwing's Longbow
			ItemService.addItem(player, 101700494, 1); // Archon Tribunus's Longbow
			ItemService.addItem(player, 100200589, 2); // Archon Tribunus's Dagger
		}
		else if (player.getPlayerClass() == PlayerClass.SORCERER) {
			ItemService.addItem(player, 114100780, 1); //
			ItemService.addItem(player, 111100751, 1); //
			ItemService.addItem(player, 113100761, 1); //
			ItemService.addItem(player, 112100712, 1); //
			ItemService.addItem(player, 110100843, 1); //
			ItemService.addItem(player, 125001714, 1); // Archon Tribunus's Headband
			ItemService.addItem(player, 122000852, 2); // Archon Tribunus's Sapphire Ring
			ItemService.addItem(player, 120000803, 2); // Archon Tribunus's Sapphire Earrings
			ItemService.addItem(player, 121000732, 1); // Archon Tribunus's Sapphire Necklace
			ItemService.addItem(player, 123000845, 1); // Archon Tribunus's Belt
			ItemService.addItem(player, 100500698, 1); // Stormwing's Jewel
			ItemService.addItem(player, 100600755, 1); // Stormwing's Tome
			ItemService.addItem(player, 100600512, 1); // Archon Tribunus's Tome
			ItemService.addItem(player, 100500479, 1); // Archon Tribunus's Jewel
		}
		else if (player.getPlayerClass() == PlayerClass.SPIRIT_MASTER) {
			ItemService.addItem(player, 114100780, 1); //
			ItemService.addItem(player, 111100751, 1); //
			ItemService.addItem(player, 113100761, 1); //
			ItemService.addItem(player, 112100712, 1); //
			ItemService.addItem(player, 110100843, 1); //
			ItemService.addItem(player, 125001714, 1); // Archon Tribunus's Headband
			ItemService.addItem(player, 122000852, 2); // Archon Tribunus's Sapphire Ring
			ItemService.addItem(player, 120000803, 2); // Archon Tribunus's Sapphire Earrings
			ItemService.addItem(player, 121000732, 1); // Archon Tribunus's Sapphire Necklace
			ItemService.addItem(player, 123000845, 1); // Archon Tribunus's Belt
			ItemService.addItem(player, 100500698, 1); // Stormwing's Jewel
			ItemService.addItem(player, 100600755, 1); // Stormwing's Tome
			ItemService.addItem(player, 100600512, 1); // Archon Tribunus's Tome
			ItemService.addItem(player, 100500479, 1); // Archon Tribunus's Jewel
		}
		else if (player.getPlayerClass() == PlayerClass.TEMPLAR) {
			ItemService.addItem(player, 114600719, 1); //
			ItemService.addItem(player, 111600732, 1); //
			ItemService.addItem(player, 113600725, 1); //
			ItemService.addItem(player, 110600759, 1); //
			ItemService.addItem(player, 112600708, 1); //
			ItemService.addItem(player, 125001717, 1); // Archon Tribunus's Helm
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 115000967, 1); // Stormwing Scale Shield
			ItemService.addItem(player, 100000893, 1); // Stormwing's Azure Scale Sword
			ItemService.addItem(player, 100900684, 1); // Stormwing's Greatsword
			ItemService.addItem(player, 115000765, 1); // Archon Tribunus's Shield
			ItemService.addItem(player, 100000618, 1); // Archon Tribunus's Sword
			ItemService.addItem(player, 100900475, 1); // Archon Tribunus's Greatsword
		}
		else if (player.getPlayerClass() == PlayerClass.BARD) {
			ItemService.addItem(player, 114100780, 1); //
			ItemService.addItem(player, 111100751, 1); //
			ItemService.addItem(player, 113100761, 1); //
			ItemService.addItem(player, 112100712, 1); //
			ItemService.addItem(player, 110100843, 1); //
			ItemService.addItem(player, 125001714, 1); // Archon Tribunus's Headband
			ItemService.addItem(player, 122000852, 2); // Archon Tribunus's Sapphire Ring
			ItemService.addItem(player, 120000803, 2); // Archon Tribunus's Sapphire Earrings
			ItemService.addItem(player, 121000732, 1); // Archon Tribunus's Sapphire Necklace
			ItemService.addItem(player, 123000845, 1); // Archon Tribunus's Belt
			ItemService.addItem(player, 102000662, 1); // Stormwing's Harp
			ItemService.addItem(player, 102000521, 1); // Archon Tribunus's Harp
		}
		else if (player.getPlayerClass() == PlayerClass.GUNNER) {
			ItemService.addItem(player, 110300795, 1); //
			ItemService.addItem(player, 113300778, 1); //
			ItemService.addItem(player, 114300793, 1); //
			ItemService.addItem(player, 112300712, 1); //
			ItemService.addItem(player, 111300753, 1); //
			ItemService.addItem(player, 122000851, 2); // Archon Tribunus's Ruby Ring
			ItemService.addItem(player, 120000802, 2); // Archon Tribunus's Ruby Earrings
			ItemService.addItem(player, 121000731, 1); // Archon Tribunus's Ruby Necklace
			ItemService.addItem(player, 125001715, 1); // Archon Tribunus's Hat
			ItemService.addItem(player, 123000844, 1); // Archon Tribunus's Leather Belt
			ItemService.addItem(player, 101800867, 2); // Stormwing's Pistol
			ItemService.addItem(player, 101900627, 1); // Stormwing's Aethercannon
			ItemService.addItem(player, 101900491, 1); // Archon Tribunus's Aethercannon
			ItemService.addItem(player, 101800504, 2); // Archon Tribunus's Pistol
		}
		else if (player.getPlayerClass() == PlayerClass.RIDER) {
			ItemService.addItem(player, 114500759, 1); //
			ItemService.addItem(player, 110500771, 1); //
			ItemService.addItem(player, 111500740, 1); //
			ItemService.addItem(player, 113500751, 1); //
			ItemService.addItem(player, 112500700, 1); //
			ItemService.addItem(player, 125001716, 1); // Archon Tribunus's Chain Helm
			ItemService.addItem(player, 122000852, 2); // Archon Tribunus's Sapphire Ring
			ItemService.addItem(player, 120000803, 2); // Archon Tribunus's Sapphire Earrings
			ItemService.addItem(player, 121000732, 1); // Archon Tribunus's Sapphire Necklace
			ItemService.addItem(player, 123000845, 1); // Archon Tribunus's Belt
			ItemService.addItem(player, 102100756, 1); // Stormwing's Cipher-Blade
			ItemService.addItem(player, 102100487, 1); // Archon Tribunus's Cipher-Blade
		}
		player.setCommandUsed(true);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				player.setCommandUsed(false);
			}
		}, 500 * 500 * 100000);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.DEFAULT_FINISH_MESSAGE));
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: .start ");
	}
}
