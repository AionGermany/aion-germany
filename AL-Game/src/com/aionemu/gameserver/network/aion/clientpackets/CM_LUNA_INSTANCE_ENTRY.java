package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.InstanceEntryCostEnum;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.InstanceEntryService;

public class CM_LUNA_INSTANCE_ENTRY extends AionClientPacket {

    private int syncId;
    private InstanceEntryCostEnum type;
    
    public CM_LUNA_INSTANCE_ENTRY(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
    
    protected void readImpl() {
        syncId = readD();
        readD();
        type = InstanceEntryCostEnum.getCotstId(readC());
    }
    
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        int worldId = DataManager.INSTANCE_COOLTIME_DATA.getSyncId(syncId);
        InstanceEntryService.getInstance().onResetInstanceEntry(player, worldId, type);
    }
}
