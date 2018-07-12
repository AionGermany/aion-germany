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
import java.util.Calendar;
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

    public SM_EVENT_WINDOW_ITEMS(Collection<EventsWindow> active_events_packet) {
        this.active_events_packet = active_events_packet;
    }

    @Override
    protected void writeImpl(AionConnection aionConnection) {
        writeC(1); // Do not Change !!!
        writeH(active_events_packet.size());
        for (EventsWindow eventsWindow : active_events_packet) {
            log.info("event id " + eventsWindow.getId() + " remain " + eventsWindow.getRemainingTime() + " start-time " + new Timestamp(eventsWindow.getPeriodStart().getMillis()).getTime() / 1000 + " end-time " + new Timestamp(eventsWindow.getPeriodEnd().getMillis()).getTime() / 1000 + " total size " + active_events_packet.size());
            writeD(eventsWindow.getId()); // Id
            writeD(0); // TODO reward recived count
            writeD(eventsWindow.getRemainingTime() * 60); //Displayed Remaining Time
            writeD(0); // Do not Change !!!
            writeD(0);// TODO reward recived count
            writeD((int) (Calendar.getInstance().getTimeInMillis() / 1000)); // PlayerLoginTime
            writeC(1); // Do not Change !!!
            writeD(5); // Do not Change !!!
            writeD(1); // Do not Change !!!
            writeC(-104); // Do not Change !!!
            writeC(98); // Do not Change !!!
            writeC(21); // Do not Change !!!
            writeC(0); // Do not Change !!!
            writeD(eventsWindow.getRemainingTime()); // Remaining Time
            writeD(eventsWindow.getItemId());  // ItemId
            writeQ(eventsWindow.getCount()); // ItemCount
            writeD(126); // Do not Change !!! This ist the Max Count of Day !!
            writeQ(new Timestamp(eventsWindow.getPeriodStart().getMillis()).getTime() / 1000); // Period Start TimeStamp
            writeQ(new Timestamp(eventsWindow.getPeriodEnd().getMillis()).getTime() / 1000); // Period End TimeSTamp
            writeD(0);//Does something
            writeD(0); // If player has this Item already in inventory it's ItemId
            writeD(1090157056); // Do not Change !!!
            writeD(eventsWindow.getMinLevel()); // StartLevel
            writeD(eventsWindow.getMaxLevel()); // EndLevel
            writeD(-1);// Do not Change !!!
            writeB(new byte[84]); // Do not Change !!!
            writeD(-1);// Do not Change !!!
            writeB(new byte[8]);// Do not Change !!!
            writeD(1);// Do not Change !!!
            writeD(9);// Do not Change !!!
            writeD(1);// Do not Change !!! //How many time you can get this reward
            writeB(new byte[7]);// Do not Change !!!
            writeD(-1);// Do not Change !!!
            writeD(0);// Do not Change !!!
        }
    }
}
