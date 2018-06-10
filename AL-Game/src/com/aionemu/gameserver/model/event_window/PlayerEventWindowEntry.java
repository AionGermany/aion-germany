/*
 * Decompiled with CFR 0_123.
 */
package com.aionemu.gameserver.model.event_window;

import com.aionemu.gameserver.model.event_window.EventWindowEntry;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import java.sql.Timestamp;

public class PlayerEventWindowEntry
extends EventWindowEntry {
    private PersistentState persistentState;

    public PlayerEventWindowEntry(int n2, Timestamp timestamp, int n3, PersistentState persistentState) {
        super(n2, timestamp, n3);
        this.persistentState = persistentState;
    }

    public PersistentState getPersistentState() {
        return this.persistentState;
    }

    public void setPersistentState(PersistentState persistentState) {
        switch (persistentState) {
            case DELETED: {
                if (this.persistentState == PersistentState.NEW) {
                    this.persistentState = PersistentState.NOACTION;
                    break;
                }
                this.persistentState = PersistentState.DELETED;
                break;
            }
            case UPDATE_REQUIRED: {
                if (this.persistentState == PersistentState.NEW) break;
                this.persistentState = PersistentState.UPDATE_REQUIRED;
                break;
            }
            case NOACTION: {
                break;
            }
            default: {
                this.persistentState = persistentState;
            }
        }
    }

}

