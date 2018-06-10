package com.aionemu.gameserver.model.event_window;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerEventsWindowDAO;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.event_window.EventWindowList;
import com.aionemu.gameserver.model.event_window.PlayerEventWindowEntry;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerEventWindowList
implements EventWindowList<Player> {
    private final Map<Integer, PlayerEventWindowEntry> entry = new HashMap<Integer, PlayerEventWindowEntry>(0);
    private int RemaininG;

    public PlayerEventWindowList() {
    }

    public PlayerEventWindowList(List<PlayerEventWindowEntry> list) {
        this();
        for (PlayerEventWindowEntry playerEventWindowEntry : list) {
            entry.put(playerEventWindowEntry.getId(), playerEventWindowEntry);
        }
    }

    public PlayerEventWindowEntry[] getAll() {
        ArrayList<PlayerEventWindowEntry> arrayList = new ArrayList();
        arrayList.addAll(entry.values());
        return arrayList.toArray(new PlayerEventWindowEntry[arrayList.size()]);
    }

    public PlayerEventWindowEntry[] getBasic() {
        return entry.values().toArray(new PlayerEventWindowEntry[entry.size()]);
    }

    private synchronized boolean add(Player player, int RemaininG, Timestamp timestamp, int Time, PersistentState persistentState) {
        entry.put(RemaininG, new PlayerEventWindowEntry(RemaininG, timestamp, Time, persistentState));
        (DAOManager.getDAO(PlayerEventsWindowDAO.class)).store(player.getPlayerAccount().getId(), RemaininG, timestamp, Time);
        return true;
    }

    @Override
    public boolean add(Player player, int RemaininG, Timestamp timestamp, int Time) {
        return add(player, RemaininG, timestamp, Time, PersistentState.NEW);
    }

    @Override
    public synchronized boolean remove(Player player, int RemaininG) {
        PlayerEventWindowEntry playerEventWindowEntry = entry.get(RemaininG);
        if (playerEventWindowEntry != null) {
            playerEventWindowEntry.setPersistentState(PersistentState.DELETED);
            entry.remove(RemaininG);
            (DAOManager.getDAO(PlayerEventsWindowDAO.class)).delete(player.getPlayerAccount().getId(), RemaininG);
        }
        return true;
    }

    @Override
    public int size() {
        return entry.size();
    }

    public void setRemaining(int RemaininG) {
        this.RemaininG = RemaininG;
    }

    public int getRenaming() {
        return RemaininG;
    }
}

