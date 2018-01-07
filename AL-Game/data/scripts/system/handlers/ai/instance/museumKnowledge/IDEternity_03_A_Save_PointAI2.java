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
package ai.instance.museumKnowledge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler
 */
@AIName("IDEternity_03_A_Save_Point") //835392
public class IDEternity_03_A_Save_PointAI2 extends NpcAI2 {

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(this, creature);
	}

	private void checkDistance(NpcAI2 ai, Creature creature) {
		if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
			if (MathUtil.isIn3dRange(getOwner(), creature, 15)) {
				announceIDEternity03A();
				IDEternity03ASavePoint();
			}
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
	}

	private void IDEternity03ASavePoint() {
		AI2Actions.deleteOwner(IDEternity_03_A_Save_PointAI2.this);
		spawn(281446, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
		spawn(731731, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
	}

	private void announceIDEternity03A() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					// 침하된 회랑 이동석을 이용할 수 있습니다.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_03_teleporter_1);
				}
			}
		});
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
