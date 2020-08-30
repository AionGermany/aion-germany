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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public interface WorldHandler {

    public void onWorldCreate(WorldMap map);

    public void onOpenDoor(Player player, int door);

    public void onEnterZone(Player player, ZoneInstance zone);

    public void onLeaveZone(Player player, ZoneInstance zone);

    public void onPlayMovieEnd(Player player, int movieId);

    public void onSkillUse(Player player, SkillTemplate template);

    public boolean onDie(Player player, Creature lastAttacker);

    public void onDie(Npc npc);

    public void onDropRegistered(Npc npc);

    public void onWorldDropRegistered(Npc npc);

    public void onGather(Player player, Gatherable gatherable);

    public void handleUseItemFinish(Player player, Npc npc);

    public void generateDrop();

    public void checkPlayTime();
}
