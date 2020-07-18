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

import java.util.ArrayList;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.EnchantDaevanionSkillService;

/**
 * @author Falke_34
 */
public class CM_DAEVANION_SKILL_FUSION extends AionClientPacket {

    private ArrayList<Integer> sacrificeBook = new ArrayList<Integer>();
    private int count;

	public CM_DAEVANION_SKILL_FUSION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		readD();
		count = readH();
        for (int i = 0; i < count; ++i) {
            sacrificeBook.add(readD());
        }
    }

    protected void runImpl() {
    	EnchantDaevanionSkillService.combineDaevanionBook(getConnection().getActivePlayer(), sacrificeBook);
    }
}
