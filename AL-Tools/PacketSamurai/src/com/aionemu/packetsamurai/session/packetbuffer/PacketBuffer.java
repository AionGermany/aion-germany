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


package com.aionemu.packetsamurai.session.packetbuffer;

import com.aionemu.packetsamurai.protocol.Protocol;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public interface PacketBuffer
{
    /**
     * provides the PacketBuffer with a ref to the protocol with which it is used
     * @param p
     */
    public void setProtocol(Protocol p);
    
    /**
     * @return the size in bytes of the data of a wholly available packet, 0 if no packet is completly avaialable
     */
    public int nextAvaliablePacket();
    
    /**
     * Fills the byte arrays providen with the data of a packet.
     * Once this is called, the packet returned must be consumed
     * @param header
     * @param data
     */
    public void getNextPacket(byte[] header, byte[] data);
    
    /**
     * Provides raw data from the stream to the PacketBuffer
     * @param data
     */
    public void addData(byte[] data);
}