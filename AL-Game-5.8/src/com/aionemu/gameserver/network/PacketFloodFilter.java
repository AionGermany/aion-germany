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
package com.aionemu.gameserver.network;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.PropertiesUtils;
import com.aionemu.gameserver.configs.main.SecurityConfig;

/**
 * @author KID
 */
public class PacketFloodFilter {

	private static PacketFloodFilter pff = new PacketFloodFilter();
	private final Logger log = LoggerFactory.getLogger(PacketFloodFilter.class);

	public static PacketFloodFilter getInstance() {
		return pff;
	}

	private int[] packets;
	private short maxClientRequest = 0x2ff;

	public PacketFloodFilter() {
		if (SecurityConfig.PFF_ENABLE) {
			int cnt = 0;
			packets = new int[maxClientRequest];
			try {
				java.util.Properties props = PropertiesUtils.load("config/administration/pff.properties");
				for (Object key : props.keySet()) {
					String str = (String) key;
					packets[Integer.decode(str)] = Integer.valueOf(props.getProperty(str).trim());
					cnt++;
				}
			}
			catch (IOException e) {
				log.error("Can't read pff.properties", e);
			}
			log.info("PacketFloodFilter initialized with " + cnt + " packets.");
		}
		else {
			log.info("PacketFloodFilter disabled.");
		}
	}

	public final int[] getPackets() {
		return this.packets;
	}
}
