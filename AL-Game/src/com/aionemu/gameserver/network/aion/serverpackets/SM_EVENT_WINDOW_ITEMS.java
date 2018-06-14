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

import java.sql.Timestamp;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.templates.event.EventsWindow;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SM_EVENT_WINDOW_ITEMS extends AionServerPacket {

    private static final Logger log = LoggerFactory.getLogger(SM_EVENT_WINDOW_ITEMS.class);
    private Collection<EventsWindow> active_events_packet;
    @SuppressWarnings("unused")
    private int remainTime;

    public SM_EVENT_WINDOW_ITEMS(Collection<EventsWindow> collection) {
        active_events_packet = collection;
    }

    @Override
    protected void writeImpl(AionConnection aionConnection) {
        writeC(1);
        writeH(active_events_packet.size());
        for (EventsWindow eventsWindow : active_events_packet) {
            log.info("event id " + eventsWindow.getId() + " remain " + eventsWindow.getRemainingTime() + " start-time " + new Timestamp(eventsWindow.getPeriodStart().getMillis()).getTime() / 1000 + " end-time " + new Timestamp(eventsWindow.getPeriodEnd().getMillis()).getTime() / 1000 + " total size " + active_events_packet.size());

            //writeC(1); // Always 1
            //writeC(1); // ShowIcon 0 = False 1 = True

            writeD(eventsWindow.getId()); // Id

            writeB(new byte[33]); // Byte 33 GF 5.8?

            writeD(eventsWindow.getRemainingTime()); // Remaining Time
            writeD(eventsWindow.getItemId());  // ItemId
            writeQ(eventsWindow.getCount()); // ItemCount

            writeD(10950);

            writeQ(new Timestamp(eventsWindow.getPeriodStart().getMillis()).getTime() / 1000); // Period Start TimeStamp
            writeQ(new Timestamp(eventsWindow.getPeriodEnd().getMillis()).getTime() / 1000); // Period End TimeSTamp

            writeD(0);
            writeD(0);

            writeD(1088063744);

            writeD(eventsWindow.getMinLevel()); // StartLevel
            writeD(eventsWindow.getMaxLevel()); // EndLevel

            
            writeB(new byte[92]); // Byte 92 GF 5.8?

            writeD(0);
            writeD(0);
            writeD(1);

            writeD(8);
            writeD(1);

            writeC(0);

            writeD(1);
            writeH(0);

            writeD(-1);
            writeD(0);
        }
    }
}
