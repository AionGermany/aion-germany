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
package com.aionemu.gameserver.world.handlers;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.handlers.WorldHandler;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public class GeneralWorldHandler implements WorldHandler {

    protected WorldMap map;
    protected Integer mapId;

    @Override
    public void onWorldCreate(WorldMap map) {
        this.map = map;
        this.mapId = map.getMapId();
        this.generateDrop();
    }

    @Override
    public void onOpenDoor(Player player, int door) {
    }

    @Override
    public void onEnterZone(Player player, ZoneInstance zone) {
    }

    @Override
    public void onLeaveZone(Player player, ZoneInstance zone) {
    }

    @Override
    public void onPlayMovieEnd(Player player, int movieId) {
    }

    @Override
    public void onSkillUse(Player player, SkillTemplate template) {
    }

    protected VisibleObject spawn(int worldId, int npcId, float x, float y, float z, byte heading) {
        SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
        return SpawnEngine.spawnObject(template, 1);
    }

    protected VisibleObject spawn(int worldId, int npcId, float x, float y, float z, byte heading, int staticId) {
        SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
        template.setStaticId(staticId);
        return SpawnEngine.spawnObject(template, 1);
    }

    protected Npc getNpc(int npcId) {
        return (Npc)World.getInstance().findVisibleObject(npcId);
    }

    protected void sendMsg(int msg, int Obj, boolean isShout, int color) {
        this.sendMsg(msg, Obj, isShout, color, 0, 0);
    }

    protected void sendMsg(int msg, int Obj, boolean isShout, int color, int time, int unk) {
        NpcShoutsService.getInstance().sendMsg(this.map, msg, Obj, isShout, color, time, 0);
    }

    protected void sendMsg(int msg) {
        this.sendMsg(msg, 0, false, 26);
    }

    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
        return false;
    }

    @Override
    public void onDie(Npc npc) {
    }

    @Override
    public void onDropRegistered(Npc npc) {
    }

    @Override
    public void onWorldDropRegistered(Npc npc) {
    }

    @Override
    public void onGather(Player player, Gatherable gatherable) {
    }

    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
    }

    @Override
    public void generateDrop() {
    }

    @Override
    public void checkPlayTime() {
    }
}
