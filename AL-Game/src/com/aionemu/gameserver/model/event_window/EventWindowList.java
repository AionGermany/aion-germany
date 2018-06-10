/*
 * Decompiled with CFR 0_123.
 */
package com.aionemu.gameserver.model.event_window;

import com.aionemu.gameserver.model.gameobjects.Creature;
import java.sql.Timestamp;

/**
 * @author Ghostfur (Aion-Unique)
 */

public interface EventWindowList<T extends Creature> {
    public boolean add(T var1, int var2, Timestamp var3, int var4);

    public boolean remove(T var1, int var2);

    public int size();
}

