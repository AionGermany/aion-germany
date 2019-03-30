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
import java.util.Arrays;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;

public class AionLoginCrypter implements ProtocolCrypter
{
	private Protocol protocol;
    private static byte[] STATIC_BLOWFISH_KEY = 
    {
        (byte) 0x6b, (byte) 0x60, (byte) 0xcb, (byte) 0x5b,
        (byte) 0x82, (byte) 0xce, (byte) 0x90, (byte) 0xb1,
        (byte) 0xcc, (byte) 0x2b, (byte) 0x6c, (byte) 0x55,
        (byte) 0x6c, (byte) 0x6c, (byte) 0x6c, (byte) 0x6c
    };
    
    private NewCrypt _crypt;
    private NewCrypt _initcrypt = new NewCrypt(STATIC_BLOWFISH_KEY);
	
	public boolean decrypt(byte[] raw, packetDirection dir)
	{
        try
        {
            if(_crypt == null)
            {
                byte[] potentialInit = Arrays.copyOf(raw, raw.length);
                _initcrypt.decrypt(potentialInit);
                NewCrypt.decXORPass(potentialInit);

                DataPacket packet = new DataPacket(0, Arrays.copyOf(potentialInit, potentialInit.length), dir, 0, protocol);
                if(dir == packetDirection.serverPacket && "SM_INIT".equals(packet.getName()))
                {
                    ValuePart part = (ValuePart) packet.getRootNode().getPartByName("Blowfish key");
                    if(part == null)
                    {
                    	PacketSamurai.getUserInterface().log("Check your protocol there is no part called 'Blowfish key' which is required in packet 0x00 of the LS protocol.");
                        return false;
                    }
                    _crypt = new NewCrypt(part.getBytes());
                    System.arraycopy(potentialInit, 0, raw, 0, raw.length);
                    return true; // no checksum here
                }
               	PacketSamurai.getUserInterface().log("No key was ready to read Packet, there should have been an Init packet before");
                return false;
            }
            if(dir == packetDirection.serverPacket)
            {
                _crypt.decrypt(raw);
                if(!_crypt.checksum(raw))
                	PacketSamurai.getUserInterface().log("LoginCrypter : Wrong checksum (packet id: "+raw[0]+", dir:"+dir+")");
                return true;
            }
            _crypt.decrypt(raw);
            if(!_crypt.checksum(raw))
            	PacketSamurai.getUserInterface().log("LoginCrypter : Wrong checksum (packet id: "+raw[0]+", dir:"+dir+")");
            return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void setProtocol(Protocol protocol)
	{
		this.protocol = protocol;
	}
}
