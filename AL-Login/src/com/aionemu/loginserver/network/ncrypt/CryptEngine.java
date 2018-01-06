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


package com.aionemu.loginserver.network.ncrypt;

import com.aionemu.commons.utils.Rnd;

/**
 * Crypto engine for ecnrypting/decrypting packets, error handling and verifying
 * checksum
 *
 * @author EvilSpirit
 */
public class CryptEngine {

    /**
     * A key
     */
    private byte[] key = {(byte) 0x6b, (byte) 0x60, (byte) 0xcb, (byte) 0x5b,
        (byte) 0x82, (byte) 0xce, (byte) 0x90, (byte) 0xb1, (byte) 0xcc,
        (byte) 0x2b, (byte) 0x6c, (byte) 0x55, (byte) 0x6c, (byte) 0x6c,
        (byte) 0x6c, (byte) 0x6c};
    /**
     * Tells you whether the key is updated or not
     */
    private boolean updatedKey = false;
    /**
     * A secret blowfish cipher
     */
    private BlowfishCipher cipher;

    /**
     * Default constructor. Initialize the Blowfish Cipher with an initial
     * static key to encrypt the first packet sent to the client
     */
    public CryptEngine() {
        cipher = new BlowfishCipher(key);
    }

    /**
     * Update the key for packet encryption/decryption with the Blowfish Cipher
     *
     * @param newKey new Blowfish Key
     */
    public void updateKey(byte[] newKey) {
        this.key = newKey;
    }

    /**
     * Decrypt given data
     *
     * @param data byte array to be decrypted
     * @param offset byte array offset
     * @param length byte array length
     * @return true, if decrypted packet has valid checksum, false overwise
     */
    public boolean decrypt(byte[] data, int offset, int length) {
        cipher.decipher(data, offset, length);

        return verifyChecksum(data, offset, length);
    }

    /**
     * Encrypt given data
     *
     * @param data byte array to be encrypted
     * @param offset byte array offset
     * @param length byte array length
     * @return length of encrypted byte array
     */
    public int encrypt(byte[] data, int offset, int length) {
        length += 4;

        // the key is not updated, so the first packet should be encrypted with
        // initial key
        if (!updatedKey) {
            length += 4;
            length += 8 - length % 8;
            encXORPass(data, offset, length, Rnd.nextInt());
            cipher.cipher(data, offset, length);
            cipher.updateKey(key);
            updatedKey = true;
        } else {
            length += 8 - length % 8;
            appendChecksum(data, offset, length);
            cipher.cipher(data, offset, length);
        }

        return length;
    }

    /**
     * Verify checksum in a packet
     *
     * @param data byte array - encrypted packet
     * @param offset byte array offset
     * @param length byte array size
     * @return true, if checksum is ok, false overwise
     */
    private boolean verifyChecksum(byte[] data, int offset, int length) {
        if ((length & 3) != 0 || (length <= 4)) {
            return false;
        }

        long chksum = 0;
        int count = length - 4;
        long check;
        int i;

        for (i = offset; i < count; i += 4) {
            check = data[i] & 0xff;
            check |= data[i + 1] << 8 & 0xff00;
            check |= data[i + 2] << 0x10 & 0xff0000;
            check |= data[i + 3] << 0x18 & 0xff000000;
            chksum ^= check;
        }

        check = data[i] & 0xff;
        check |= data[i + 1] << 8 & 0xff00;
        check |= data[i + 2] << 0x10 & 0xff0000;
        check |= data[i + 3] << 0x18 & 0xff000000;
        check = data[i] & 0xff;
        check |= data[i + 1] << 8 & 0xff00;
        check |= data[i + 2] << 0x10 & 0xff0000;
        check |= data[i + 3] << 0x18 & 0xff000000;

        return 0 == chksum;
    }

    /**
     * add checksum to the end of the packet
     *
     * @param raw byte array - encrypted packet
     * @param offset byte array offset
     * @param length byte array size
     */
    private void appendChecksum(byte[] raw, int offset, int length) {
        long chksum = 0;
        int count = length - 4;
        long ecx;
        int i;

        for (i = offset; i < count; i += 4) {
            ecx = raw[i] & 0xff;
            ecx |= raw[i + 1] << 8 & 0xff00;
            ecx |= raw[i + 2] << 0x10 & 0xff0000;
            ecx |= raw[i + 3] << 0x18 & 0xff000000;
            chksum ^= ecx;
        }

        ecx = raw[i] & 0xff;
        ecx |= raw[i + 1] << 8 & 0xff00;
        ecx |= raw[i + 2] << 0x10 & 0xff0000;
        ecx |= raw[i + 3] << 0x18 & 0xff000000;
        raw[i] = (byte) (chksum & 0xff);
        raw[i + 1] = (byte) (chksum >> 0x08 & 0xff);
        raw[i + 2] = (byte) (chksum >> 0x10 & 0xff);
        raw[i + 3] = (byte) (chksum >> 0x18 & 0xff);
    }

    /**
     * First packet encryption with XOR key (integer - 4 bytes)
     *
     * @param data byte array to be encrypted
     * @param offset byte array offset
     * @param length byte array length
     * @param key integer value as key
     */
    private void encXORPass(byte[] data, int offset, int length, int key) {
        int stop = length - 8;
        int pos = 4 + offset;
        int edx;
        int ecx = key;

        while (pos < stop) {
            edx = (data[pos] & 0xFF);
            edx |= (data[pos + 1] & 0xFF) << 8;
            edx |= (data[pos + 2] & 0xFF) << 16;
            edx |= (data[pos + 3] & 0xFF) << 24;
            ecx += edx;
            edx ^= ecx;
            data[pos++] = (byte) (edx & 0xFF);
            data[pos++] = (byte) (edx >> 8 & 0xFF);
            data[pos++] = (byte) (edx >> 16 & 0xFF);
            data[pos++] = (byte) (edx >> 24 & 0xFF);
        }

        data[pos++] = (byte) (ecx & 0xFF);
        data[pos++] = (byte) (ecx >> 8 & 0xFF);
        data[pos++] = (byte) (ecx >> 16 & 0xFF);
        data[pos] = (byte) (ecx >> 24 & 0xFF);
    }
}
