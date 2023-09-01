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
package instance;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Falke_34
 */
@InstanceID(301690000)
public class AetherMineQuestInstance extends GeneralInstanceHandler {

	// TODO - Quest
	// after use the portal all NPCs despawn
	// and Helnjor + Downcast Dezabo spawns
	// now kill Helnjor
	// Dezabo spawns
	// now you speak with Dezabo and you will teleport to the Town

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		switch (player.getRace()) {
			case ELYOS:
				// Warrior of Darkness
				spawn(244111, 259.7762f, 195.5483f, 235.58435f, (byte) 0);
				spawn(244111, 288.3309f, 218.39922f, 244.47997f, (byte) 0);
				spawn(244111, 322.9853f, 223.45547f, 252.28363f, (byte) 0);
				spawn(244111, 304.2836f, 218.1335f, 246.8944f, (byte) 0);
				spawn(244111, 237.9888f, 183.5066f, 233.90103f, (byte) 7);
				spawn(244111, 206.911f, 179.34335f, 229.21446f, (byte) 47);
				spawn(244111, 180.63031f, 168.75336f, 228.08234f, (byte) 18);
				spawn(244111, 172.48128f, 149.92288f, 230.46252f, (byte) 10);
				// Battle Mage of Darkness
				spawn(244112, 277.151f, 202.81923f, 241.4017f, (byte) 0);
				spawn(244112, 311.01392f, 238.29396f, 252.25574f, (byte) 0);
				spawn(244112, 290.26578f, 202.9856f, 243.84018f, (byte) 0);
				spawn(244112, 338.76392f, 238.66896f, 256.06082f, (byte) 0);
				spawn(244112, 214.44975f, 168.16321f, 229.43385f, (byte) 65);
				spawn(244112, 201.54176f, 169.01022f, 228.5797f, (byte) 7);
				spawn(244112, 187.36093f, 151.86436f, 228.2209f, (byte) 10);
				spawn(244112, 168.99156f, 158.07268f, 230.7956f, (byte) 10);
				// Helnjor
				spawn(244113, 172.97343f, 156.89346f, 230.44385f, (byte) 93);
				// Asmodian Portal Rift
				spawn(703317, 164.67667f, 150.92424f, 232.88237f, (byte) 0, 39);
				// Esterra Protection Artefact
				spawn(731709, 248.59483f, 189.85466f, 236.011f, (byte) 0, 21);
				// Dezabo
				spawn(806293, 321.3939f, 260.16995f, 256.4484f, (byte) 15);
				// Downcast Dezabo
				spawn(806294, 173.752f, 153.86577f, 230.34056f, (byte) 33);
			case ASMODIANS:
				// Warrior of Light
				spawn(244127, 259.7762f, 195.5483f, 235.58435f, (byte) 0);
				spawn(244127, 288.3309f, 218.39922f, 244.47997f, (byte) 0);
				spawn(244127, 322.9853f, 223.45547f, 252.28363f, (byte) 0);
				spawn(244127, 304.2836f, 218.1335f, 246.8944f, (byte) 0);
				spawn(244127, 237.9888f, 183.5066f, 233.90103f, (byte) 7);
				spawn(244127, 206.911f, 179.34335f, 229.21446f, (byte) 47);
				spawn(244127, 180.63031f, 168.75336f, 228.08234f, (byte) 18);
				spawn(244127, 172.48128f, 149.92288f, 230.46252f, (byte) 10);
				// Battle Mage of Light
				spawn(244128, 277.151f, 202.81923f, 241.4017f, (byte) 0);
				spawn(244128, 311.01392f, 238.29396f, 252.25574f, (byte) 0);
				spawn(244128, 290.26578f, 202.9856f, 243.84018f, (byte) 0);
				spawn(244128, 338.76392f, 238.66896f, 256.06082f, (byte) 0);
				spawn(244128, 214.44975f, 168.16321f, 229.43385f, (byte) 65);
				spawn(244128, 201.54176f, 169.01022f, 228.5797f, (byte) 7);
				spawn(244128, 187.36093f, 151.86436f, 228.2209f, (byte) 10);
				spawn(244128, 168.99156f, 158.07268f, 230.7956f, (byte) 10);
				// Yunius
				spawn(244129, 172.97343f, 156.89346f, 230.44385f, (byte) 93);
				// Elyos Portal Rift
				spawn(703325, 164.67667f, 150.92424f, 232.88237f, (byte) 0, 39);
				// Nosra Protection Artefact
				spawn(731715, 248.59483f, 189.85466f, 236.011f, (byte) 0, 21);
				// Wizabo
				spawn(806298, 321.3939f, 260.16995f, 256.4484f, (byte) 15);
				// Downcast Wizabo
				spawn(806299, 173.752f, 153.86577f, 230.34056f, (byte) 33);
				break;
			default:
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
	}

	@Override
	public void onLeaveInstance(Player player) {
	}

	@Override
	public void onInstanceDestroy() {
	}

	@Override
	public void onPlayerLogOut(Player player) {
	}

	@Override
	public void onExitInstance(Player player) {
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
