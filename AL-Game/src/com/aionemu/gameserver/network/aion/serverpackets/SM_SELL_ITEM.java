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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author orz, Sarynth, modified by Artur
 */
public class SM_SELL_ITEM extends AionServerPacket {

	private int targetObjectId;
    private int sellPercentage;
	private TradeListTemplate tradeListTemplate;
	@SuppressWarnings("unused")
	private byte action = 0x01; // ?! WTF

	public SM_SELL_ITEM(int targetObjectId, int sellPercentage) {
		this.sellPercentage = sellPercentage;
		this.targetObjectId = targetObjectId;
	}

	public SM_SELL_ITEM(int targetObjectId, TradeListTemplate tradeListTemplate) {
		this.targetObjectId = targetObjectId;
		this.tradeListTemplate = tradeListTemplate;
		this.sellPercentage = tradeListTemplate.getBuyPriceRate();
		if (tradeListTemplate.getTradeNpcType() == TradeNpcType.ABYSS) {
			this.action = 0x02;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		if ((this.tradeListTemplate != null) && (this.tradeListTemplate.getNpcId() != 0) && (this.tradeListTemplate.getCount() != 0)) {
			writeD(this.targetObjectId);
			writeC(this.tradeListTemplate.getTradeNpcType().index());
			writeD(this.sellPercentage);
			writeH(256);
			writeH(this.tradeListTemplate.getCount());
			for (TradeListTemplate.TradeTab tradeTabl : this.tradeListTemplate.getTradeTablist())
				writeD(tradeTabl.getId());
		}
		else {
			writeD(this.targetObjectId);
			writeD(5121);
			writeD(65792);
			writeC(0);
		}
	}
}
