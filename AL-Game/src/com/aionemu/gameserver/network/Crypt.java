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

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;

/**
 * Crypt will encrypt server packet and decrypt client packet.
 *
 * @author hack99
 * @author kao
 * @author -Nemesiss-
 */
public class Crypt {

	private final static Logger log = LoggerFactory.getLogger(Crypt.class);
	/**
	 * Second byte of server packet must be equal to this
	 */
	public final static byte staticServerPacketCode = 0x54; // 4.9 => 0x56 || 4.7 | 4.6 | 4.5 => 0x44 | 4.3 => 0x46
	/**
	 * Crypt is enabled after first server packet was send.
	 */
	private boolean isEnabled;
	private EncryptionKeyPair packetKey = null;

	/**
	 * Enable crypt key - generate random key that will be used to encrypt second server packet [first one is unencrypted] and decrypt client packets. This method is called from SM_KEY server packet,
	 * that packet sends key to aion client.
	 *
	 * @return "false key" that should by used by aion client to encrypt/decrypt packets.
	 */
	public final int enableKey() {
		if (packetKey != null) {
			throw new KeyAlreadySetException();
		}

		/**
		 * rnd key - this will be used to encrypt/decrypt packet
		 */
		int key = Rnd.nextInt();

		packetKey = new EncryptionKeyPair(key);

		log.debug("new encrypt key: " + packetKey);

		/**
		 * false key that will be sent to aion client in SM_KEY packet
		 */
		return (key ^ 0xCD92E4D7) + 0x3FF2CCD7; // 6.x
	}

	/**
	 * Decrypt client packet from this ByteBuffer.
	 *
	 * @param buf
	 * @return true if decryption was successful.
	 */
	public final boolean decrypt(ByteBuffer buf) {
		if (!isEnabled) {
			log.debug("if encryption wasn't enabled, then maybe it's client reconnection, so skip packet");
			return true;
		}

		return packetKey.decrypt(buf);
	}

	/**
	 * Encrypt server packet from this ByteBuffer.
	 *
	 * @param buf
	 */
	public final void encrypt(ByteBuffer buf) {
		if (!isEnabled) {
			/**
			 * first packet is not encrypted
			 */
			isEnabled = true;
			log.debug("packet is not encrypted... send in SM_KEY");
			return;
		}

		packetKey.encrypt(buf);
	}

	/**
	 * Server packet opcodec obfuscation.
	 *
	 * @param op
	 * @return obfuscated opcodec
	 */
	public static final int encodeOpcodec(int op) {
		return (op + 0xD7) ^ 0xD7; // 6.x
	}
}
