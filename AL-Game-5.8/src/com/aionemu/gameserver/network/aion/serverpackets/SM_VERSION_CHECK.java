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
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.IPRange;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.NetworkController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.ChatService;

/**
 * @author -Nemesiss- CC fix
 * @modified by Novo, cura
 * @author GiGatR00n, NewLives
 */

public class SM_VERSION_CHECK extends AionServerPacket {

	private static final Logger log = LoggerFactory.getLogger(SM_VERSION_CHECK.class);
	/**
	 * Aion Client version
	 */
	private int version;
	/**
	 * Number of characters can be created
	 */
	private int characterLimitCount;
	/**
	 * Related to the character creation mode
	 */
	private final int characterFactionsMode;
	private final int characterCreateMode;

	/**
	 * @param chatService
	 */
	public SM_VERSION_CHECK(int version) {
		this.version = version;

		if (MembershipConfig.CHARACTER_ADDITIONAL_ENABLE != 10 && MembershipConfig.CHARACTER_ADDITIONAL_COUNT > GSConfig.CHARACTER_LIMIT_COUNT) {
			characterLimitCount = MembershipConfig.CHARACTER_ADDITIONAL_COUNT;
		}
		else {
			characterLimitCount = GSConfig.CHARACTER_LIMIT_COUNT;
		}
		characterLimitCount *= NetworkController.getInstance().getServerCount();

		if (GSConfig.CHARACTER_CREATION_MODE < 0 || GSConfig.CHARACTER_CREATION_MODE > 2) {
			characterFactionsMode = 0;
		}
		else {
			characterFactionsMode = GSConfig.CHARACTER_CREATION_MODE;
		}

		if (GSConfig.CHARACTER_FACTION_LIMITATION_MODE < 0 || GSConfig.CHARACTER_FACTION_LIMITATION_MODE > 3) {
			characterCreateMode = 0;
		}
		else {
			characterCreateMode = GSConfig.CHARACTER_FACTION_LIMITATION_MODE * 0x04;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		// aion 3.0 = 194
		// aion 3.5 = 196
		// aion 4.0 = 201
		// aion 4.5 = 203
		// aion 4.7 = 204
		// aion 4.7.0.7 = 205
		// aion 4.7.5.x = 206
		// aion 5.1.x.x = 212
		if (version < 213) {
			// Send wrong client version
			writeC(0x02);
			return;
		}
		if (version == 213) {
			log.info("Authentication with Client Version 5.8");
		}
		else if (version < 213) {
			log.info("Authentication with Client Version lower than 5.8");
		}
		writeC(0x00);
		writeC(NetworkConfig.GAMESERVER_ID);
		writeD(180205);// start year month day
		writeD(171201);// start year month day
		writeD(0x00);// spacing
		writeD(180205);// year month day
		writeD((int) (Calendar.getInstance().getTimeInMillis() / 1000)); // Start Server Time in Seconds Unit (Need to Implements in Config Files)
		writeC(0x00);// unk
		writeC(GSConfig.SERVER_COUNTRY_CODE);// country code;
		int serverMode = (characterLimitCount * 0x10) | characterFactionsMode;
		writeC(serverMode | characterCreateMode);
		writeD((int) (Calendar.getInstance().getTimeInMillis() / 1000));
		writeD(-3600);// 5.8 (-3600 = +1 Std, 0 = -1Std)
		writeD(40014200);
		// MOVED TO PACKET 168 
//		writeC(GSConfig.CHARACTER_REENTRY_TIME);
//		writeC(EventsConfig.ENABLE_DECOR);
//		writeC(EventService.getInstance().getEventType().getId());
		// MOVED TO PACKET 168
		writeD(0);
		writeD(68536);
		writeB(new byte[20]);
		for (int i = 0; i < 11; i++) {
			writeD(1000);
		}
		writeH(25600);
		writeH(0);
		writeC(0);
		writeD(1000);
		writeH(1);
		writeC(0);
		// for... chat servers?
		{

			// if the correct ip is not sent it will not work
			byte[] addr = IPConfig.getDefaultAddress();
			for (IPRange range : IPConfig.getRanges()) {
				if (range.isInRange(con.getIP())) {
					addr = range.getAddress();
					break;
				}
			}
			writeB(addr);
			writeH(ChatService.getPort());
		}
	}
}
