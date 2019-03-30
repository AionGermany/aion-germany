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
package com.aionemu.packetsamurai.crypt;

import java.io.IOException;
import java.util.logging.Logger;

public class NewCrypt
{
	protected static Logger _log = Logger.getLogger(NewCrypt.class.getName());
	BlowfishEngine _decrypt;
	
	public NewCrypt(String key)
	{
		byte[] keybytes = key.getBytes();
		_decrypt = new BlowfishEngine();
		_decrypt.init(keybytes);
	}
	
	/**
	 * @param blowfishKey
	 */
	public NewCrypt(byte[] blowfishKey)
	{
		_decrypt = new BlowfishEngine();
		_decrypt.init(blowfishKey);
	}

	public boolean checksum(byte[] raw)
	{
		long chksum = 0;
		int count = raw.length-2;
		long ecx = -1; //avoids ecs beeing == chksum if an error occured in the try
		int i =0;
		try
        {
			for (i=0; i<count; i+=4)
			{
				ecx = raw[i] &0xff;
				ecx |= raw[i+1] << 8 &0xff00;
				ecx |= raw[i+2] << 0x10 &0xff0000;
				ecx |= raw[i+3] << 0x18 &0xff000000;
				
				chksum ^= ecx;
			}
	
			ecx = raw[i] &0xff;
			ecx |= raw[i+1] << 8 &0xff00;
			ecx |= raw[i+2] << 0x10 &0xff0000;
			ecx |= raw[i+3] << 0x18 &0xff000000;

        }
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
			//Looks like this will only happen on incoming packets as outgoing ones are padded
			//and the error can only happen in last raw[i] =, raw [i+1] = ... and it doesnt really matters for incomming packets
		}

		return ecx == chksum;	
	}
	

	public void decrypt(byte[] raw) throws IOException
	{
		int count = raw.length /8;

		for (int i=0; i<count;i++)
		{
			_decrypt.processBlock(raw,i*8);
		}
	}
    
    /**
     * Packet is first XOR encoded with <code>key</code>
     * Then, the last 4 bytes are overwritten with the the XOR "key".
     * Thus this assume that there is enough room for the key to fit without overwriting data.
     * @param raw The raw bytes to be encrypted
     * @param key The 4 bytes (int) XOR key
     */
    public static void encXORPass(byte[] raw, int key)
    {
        int stop = raw.length-8;
        int pos = 4;
        int edx;
        int ecx = key; // Initial xor key
        
        while (pos < stop)
        {
            edx = (raw[pos] & 0xFF);
            edx |= (raw[pos+1] & 0xFF) << 8;
            edx |= (raw[pos+2] & 0xFF) << 16;
            edx |=  (raw[pos+3] & 0xFF) << 24;

            ecx += edx;
            
            edx ^= ecx;
            
            raw[pos++] = (byte) (edx & 0xFF);
            raw[pos++] = (byte) (edx >> 8 & 0xFF);
            raw[pos++] = (byte) (edx >> 16 & 0xFF);
            raw[pos++] = (byte) (edx >> 24 & 0xFF);
        }
        
        raw[pos++] = (byte) (ecx & 0xFF);
        raw[pos++] = (byte) (ecx >> 8 & 0xFF);
        raw[pos++] = (byte) (ecx >> 16 & 0xFF);
        raw[pos++] = (byte) (ecx >> 24 & 0xFF);
    }
    
    /**
     * 
     */
    public static void decXORPass(byte[] raw)
    {
        int count = raw.length/4;
        int pos = (count - 1)*4;
        int ecx;
        
        ecx =  (raw[--pos] & 0xFF) << 24;
        ecx |= (raw[--pos] & 0xFF) << 16;
        ecx |= (raw[--pos] & 0xFF) << 8;
        ecx |= (raw[--pos] & 0xFF);
        
        int val;
        while (pos > 4)
        {
            raw[--pos] ^= (ecx >> 24);
            
            val = (raw[pos] & 0xFF) << 24;
            raw[--pos] ^= (ecx >> 16);
            val += (raw[pos] & 0xFF) << 16;
            raw[--pos] ^= (ecx >> 8);
            val += (raw[pos] & 0xFF) << 8;
            raw[--pos] ^= ecx;
            val += (raw[pos] & 0xFF);
            
            ecx = ecx - val;
        }
    }
}
