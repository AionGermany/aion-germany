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
package com.aionemu.gameserver.instance.handlers;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.StageList;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.WalkerFormator;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public class GeneralInstanceHandler implements InstanceHandler {

	protected Integer mapId;
	protected int instanceId;
	protected final long creationTime;
	protected WorldMapInstance instance;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		this.instance = instance;
		this.instanceId = instance.getInstanceId();
		this.mapId = instance.getMapId();
	}

	public GeneralInstanceHandler() {
		creationTime = System.currentTimeMillis();
	}

	@Override
	public void onInstanceDestroy() {
	}

	@Override
	public void onPlayerLogin(Player player) {
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
	}

	@Override
	public void onLeaveZone(Player player, ZoneInstance zone) {
	}

	@Override
	public void onPlayerLogOut(Player player) {
	}

	@Override
	public void onOpenDoor(int door) {
	}

	@Override
	public void onPlayMovieEnd(Player player, int movieId) {
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, npcId, x, y, z, heading);
		return SpawnEngine.spawnObject(template, instanceId);
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, int staticId) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, npcId, x, y, z, heading);
		template.setStaticId(staticId);
		return SpawnEngine.spawnObject(template, instanceId);
	}

	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, String walkerId, int walkerIdx) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(this.mapId.intValue(), npcId, x, y, z, heading, walkerId, walkerIdx);
		return SpawnEngine.spawnObject(template, this.instanceId);
	}

	@Override
	public void onEnterInstance(Player player) {
	}

	@Override
	public void onLeaveInstance(Player player) {
	}

	@Override
	public boolean onReviveEvent(Player player) {
		return false;
	}

	public void onCheckAfk(Player player) {
	}

	protected void sendMsg(int msg, int Obj, boolean isShout, int color) {
		sendMsg(msg, Obj, isShout, color, 0);
	}

	protected void sendMsg(int msg, int Obj, boolean isShout, int color, int time) {
		NpcShoutsService.getInstance().sendMsg(instance, msg, Obj, isShout, color, time);
	}

	protected void sendMsg(int msg) {
		sendMsg(msg, 0, false, 25);
	}

	protected void organizeAndSpawn() {
		WalkerFormator.organizeAndSpawn(this.mapId.intValue(), this.instanceId);
	}

	protected void walkerDestroy() {
		WalkerFormator.onInstanceDestroy(this.mapId.intValue(), this.instanceId);
	}

	protected Npc getNpc(int npcId) {
		return instance.getNpc(npcId);
	}

	@Override
	public StageType getStage() {
		return StageType.DEFAULT;
	}

	@Override
	public void onDropRegistered(Npc npc) {
	}

	@Override
	public void onGather(Player player, Gatherable gatherable) {
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return null;
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		return false;
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
	}

	@Override
	public void onExitInstance(Player player) {
	}

	@Override
	public void doReward(Player player) {
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		return false;
	}

	@Override
	public void onStopTraining(Player player) {
	}

	@Override
	public void onDie(Npc npc) {
	}

	@Override
	public void onChangeStage(StageType type) {
	}

	@Override
	public void onChangeStageList(StageList list) {
	}

	@Override
	public boolean isEnemy(Player attacker, Player target) {
		return false;
	}
}
