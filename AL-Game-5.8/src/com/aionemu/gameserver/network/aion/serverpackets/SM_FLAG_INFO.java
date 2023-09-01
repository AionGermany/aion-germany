package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by Ace on 14/02/2017. Updated by FrozenKiller 16/02/2017.
 */

public class SM_FLAG_INFO extends AionServerPacket {

	private List<Npc> flags;

	public SM_FLAG_INFO(List<Npc> flags) {
		this.flags = flags;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(flags.size());
		for (Npc flagNpc : flags) {
			writeD(flagNpc.getNpcId());
			writeD(flagNpc.getObjectId());
			writeD(flagNpc.getLifeStats().getMaxHp());
			writeD(flagNpc.getLifeStats().getCurrentHp());
			writeF(flagNpc.getX());// x
			writeF(flagNpc.getY());// y
			writeF(flagNpc.getZ());// z
		}
		flags.clear();
	}
}
