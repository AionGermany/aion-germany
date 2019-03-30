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


package com.aionemu.packetsamurai.parser.formattree;

import java.lang.ref.WeakReference;


import com.aionemu.packetsamurai.parser.DataStructure;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFormat;

import javolution.util.FastList;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class Format
{
    private final PacketFormat _containingPacketFormat;
    private final PartContainer _mainBlock;
    //  uses WeakReferance to avoid leaking the DataPackets
    private final FastList<WeakReference<DataStructure>> _formatChangeListeners;
    
    public Format(PacketFormat container)
    {
        _formatChangeListeners = new FastList<WeakReference<DataStructure>>();
        _mainBlock = new PartContainer(PartType.block, true);
        _mainBlock.setContainingFormat(this);
        _containingPacketFormat = container;
    }
    
    public PartContainer getMainBlock()
    {
        return _mainBlock;
    }
    
    public void triggerFormatChanged()
    {
        for(WeakReference<DataStructure> ref :_formatChangeListeners)
        {
            DataStructure dp = ref.get();
            if(dp != null)
                dp.invalidateParsing();
        }
    }

    public void registerFormatChangeListener(DataStructure packet)
    {
        _formatChangeListeners.add(new WeakReference<DataStructure>(packet));
    }

    /**
     * @return the containingPacketFormat
     */
    public PacketFormat getContainingPacketFormat()
    {
        return _containingPacketFormat;
    }
    
    @Override 
    public String toString()
    {
    	return _containingPacketFormat.toString();
    }
}