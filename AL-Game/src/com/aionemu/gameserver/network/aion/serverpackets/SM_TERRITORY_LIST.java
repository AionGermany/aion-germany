/**
 * 
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import com.aionemu.gameserver.model.team.legion.LegionTerritory;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author CoolyT
 */
public class SM_TERRITORY_LIST extends AionServerPacket {

	int size = 6;
	Collection<LegionTerritory> territoryList;

	public SM_TERRITORY_LIST(Collection<LegionTerritory> territoryList) {
		this.territoryList = territoryList;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(territoryList.size());
		for (LegionTerritory territory : territoryList) {
			writeD(territory.getId()); // TerretoryId
			writeD(territory.getLegionId());
			writeH(territory.getLegionId() > 0 ? 0x8009 : 0);
			writeH(territory.getLegionId() > 0 ? 255 : 0);
			writeH(0);
			writeS(territory.getLegionName());
		}
	}
}
