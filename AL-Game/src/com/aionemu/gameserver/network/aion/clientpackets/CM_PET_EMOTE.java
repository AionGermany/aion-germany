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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.PetEmote;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET_EMOTE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 * @author update Ever'
 */
public class CM_PET_EMOTE extends AionClientPacket {

	private PetEmote emote;
	private int emoteId;
	private float x1;
	private float y1;
	private float z1;
	private byte h;
	private float x2;
	private float y2;
	private float z2;
	private int emotionId;
	private int unk2;

	public CM_PET_EMOTE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		emoteId = readC();
		emote = PetEmote.getEmoteById(emoteId);

		if (emote == PetEmote.MOVE_STOP) {
			x1 = readF();
			y1 = readF();
			z1 = readF();
			h = readSC();
		}
		else if (emote == PetEmote.MOVETO) {
			x1 = readF();
			y1 = readF();
			z1 = readF();
			h = readSC();
			x2 = readF();
			y2 = readF();
			z2 = readF();
		}
		else {
			emotionId = readC();
			unk2 = readC();
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		Pet pet = player.getPet();

		if (pet == null) {
			return;
		}

		// sometimes client is crazy enough to send -2.4457384E7 as z coordinate
		// TODO (check retail) either its client bug or packet problem somewhere
		// reproducible by flying randomly and falling from long height with fly resume
		if (x1 < 0 || y1 < 0 || z1 < 0) {
			return;
		}
		// log.info("CM_PET_EMOTE emote {}, unk1 {}, unk2 {}", new Object[] { emoteId, unk1, unk2 });
		// switch (emote) {

		if (emote == PetEmote.ALARM) {
			PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote), true);
		}
		else if (emote == PetEmote.MOVE_STOP) {
			World.getInstance().updatePosition(pet, x1, y1, z1, h);
			PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, x1, y1, z1, h), true);
		}
		else if (emote == PetEmote.MOVETO) {
			World.getInstance().updatePosition(pet, x1, y1, z1, h);
			pet.getMoveController().setNewDirection(x2, y2, z2, h);
			PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, x1, y1, z2, x2, y2, z2, h), true);
		}
		else if (emote == PetEmote.FLY) {
			PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, emotionId, unk2), true);
		}
		else if (emote == PetEmote.MOVE_STOP) {
			World.getInstance().updatePosition(pet, x1, y1, z1, h);
			PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, x1, y1, z1, h), true);
		}
		else {
			if (emotionId > 0) {
				PacketSendUtility.sendPacket(player, new SM_PET_EMOTE(pet, emote, emotionId, unk2));
			}
			else {
				PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, 0, unk2), true);
			}
		}
	}
}
