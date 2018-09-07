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
package instance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author nightm
 * @reworked Himiko
 */
@InstanceID(300800000)
public class InfinityShardInstance extends GeneralInstanceHandler {

	private int ShieldGenerator = 0;
	private int Protectors1 = 0;
	private int Protectors2 = 0;
	private int Protectors3 = 0;
	private int Protectors4 = 0;
	private int buff = 0;

	private Future<?> spIda;
	private Future<?> Cast;
	private Future<?> CastBuff1;
	private Future<?> CastBuff2;
	private Future<?> CastBuff3;
	private Future<?> CastBuff4;
	private Future<?> CheckBuff;
	private Future<?> CheckID;
	private Future<?> Leave;
	private Future<?> Destroy;
	private WorldMapInstance InfinityShardInstance;

	private Map<Integer, Integer> spawnsNpc = new HashMap<Integer, Integer>();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		performSkillToTarget(231073, 231073, 21371);
		performSkillToTarget(231073, 231073, 20984);
		performSkillToTarget(231074, 231074, 21371);
		performSkillToTarget(231074, 231074, 20984);
		performSkillToTarget(231078, 231078, 21371);
		performSkillToTarget(231078, 231078, 20984);
		performSkillToTarget(231082, 231082, 21371);
		performSkillToTarget(231082, 231082, 20984);
		performSkillToTarget(231086, 231086, 21371);
		performSkillToTarget(231086, 231086, 20984);
		spawnsNpc.put(1, 231092);
		spawnsNpc.put(2, 231093);
		spawnsNpc.put(3, 231094);
		spawnsNpc.put(4, 231095);
		InfinityShardInstance = instance;
	}

	private void startSp() {
		if (spIda == null) {
			spIda = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					spawnIda();
				}
			}, 30 * 1000 * 1, 30 * 1000 * 1);
		}
	}

	private void startCheck() {
		if (CheckBuff == null) {
			CheckBuff = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					Check();
				}
			}, 60 * 1000 * 1, 60 * 1000 * 1);
		}
	}

	private void startCheckID() {
		if (CheckID == null) {
			CheckID = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					CheckIDBuff();
				}
			}, 60 * 1000 * 1, 60 * 1000 * 1);
		}
	}

	private void Check() {
		Npc id1 = getNpc(231092);
		Npc id2 = getNpc(231093);
		Npc id3 = getNpc(231094);
		Npc id4 = getNpc(231095);
		Npc hyperion = getNpc(231073);
		if ((id1 != null && id1.getEffectController().hasAbnormalEffect(21371)) && (id2 != null && id2.getEffectController().hasAbnormalEffect(21371)) && (id3 != null && id3.getEffectController().hasAbnormalEffect(21371)) && (id4 != null && id4.getEffectController().hasAbnormalEffect(21371))) {
			SkillEngine.getInstance().getSkill(hyperion, 20983, 65, hyperion).useNoAnimationSkill();

			// Delete Hyperion
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					Npc hyperion = getNpc(231073);
					despawnNpc(hyperion);
					if (CheckBuff != null) {
						CheckBuff.cancel(false);
					}
				}
			}, 5 * 1000);
		}
	}

	private void CheckIDBuff() {
		Npc id1 = getNpc(231092);
		Npc id2 = getNpc(231093);
		Npc id3 = getNpc(231094);
		Npc id4 = getNpc(231095);
		Npc hyperion = getNpc(231073);
		if ((id1 != null && id1.getEffectController().hasAbnormalEffect(21371) || hyperion != null && hyperion.getEffectController().hasAbnormalEffect(21382))) {
			performSkillToTarget(231073, 231073, 21382);
		}
		if ((id2 != null && id1.getEffectController().hasAbnormalEffect(21371) || hyperion != null && hyperion.getEffectController().hasAbnormalEffect(21384))) {
			performSkillToTarget(231073, 231073, 21384);
		}
		if ((id3 != null && id1.getEffectController().hasAbnormalEffect(21371) || hyperion != null && hyperion.getEffectController().hasAbnormalEffect(21416))) {
			performSkillToTarget(231073, 231073, 21416);
		}
		if ((id4 != null && id1.getEffectController().hasAbnormalEffect(21371) || hyperion != null && hyperion.getEffectController().hasAbnormalEffect(21382))) {
			performSkillToTarget(231073, 231073, 21382);
		}
	}

	private void DestroyInst() {
		if (Destroy == null) {
			Destroy = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					InstanceService.destroyInstance(InfinityShardInstance);
				}
			}, 120 * 1000 * 1); // 10 seconds changed to 2 minutes
		}
	}

	private void Cast(int Nm) {
		final int number = Nm;
		sendMessage(1401790);
		Cast = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				CargeCast(number);
			}
		}, 25 * 1000 * 1);

	}

	private void CastBuffHyperion(int Nm) {
		final int number = Nm;
		switch (number) {
			case 1:
				CastBuff1 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						CastBuff(number);
					}
				}, 2 * 1000 * 1);
				break;
			case 2:
				CastBuff2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						CastBuff(number);
					}
				}, 2 * 1000 * 1);
				break;
			case 3:
				CastBuff3 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						CastBuff(number);
					}
				}, 2 * 1000 * 1);
				break;
			case 4:
				CastBuff4 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						CastBuff(number);
					}
				}, 2 * 1000 * 1);
				break;
		}
	}

	private void spawnIda() {
		int i = 0;

		for (int objId : spawnsNpc.values()) {
			if (i == 1)
				break;
			if (instance.getNpc(objId) == null) {

				switch (objId) {
					case 231092:

						spawn(231092, 147.95657f, 133.9761f, 128.65849f, (byte) 58);
						performSkillToTarget(231092, 231073, 21257);
						Cast(1);
						i++;
						break;
					case 231093:

						spawn(231093, 109.58858f, 141.61421f, 128.6585f, (byte) 89);
						performSkillToTarget(231093, 231073, 21381);
						Cast(2);
						i++;
						break;
					case 231094:

						spawn(231094, 134.70184f, 159.27972f, 134.62769f, (byte) 84);
						performSkillToTarget(231094, 231073, 21383);
						Cast(3);
						i++;
						break;
					case 231095:

						spawn(231095, 118.58971f, 115.9688f, 134.85f, (byte) 27);
						Cast(4);
						i++;
						break;
				}
			}
		}
	}

	@Override
	public void onDie(Npc npc) {
		Npc shield1 = instance.getNpc(231104);
		Npc shield2 = instance.getNpc(284437);
		final int npcId = npc.getNpcId();
		switch (npcId) {
			case 231075:
			case 231076:
			case 231077:
				Protectors1++;
				if (Protectors1 >= 3) {
					final Npc generator1 = getNpc(231074);
					remove(generator1);
				}
				despawnNpc(npc);
				break;
			case 231079:
			case 231080:
			case 231081:
				Protectors2++;
				if (Protectors2 >= 3) {
					final Npc generator2 = getNpc(231078);
					remove(generator2);
				}
				despawnNpc(npc);
				break;
			case 231083:
			case 231084:
			case 231085:
				Protectors3++;
				if (Protectors3 >= 3) {
					final Npc generator3 = getNpc(231082);
					remove(generator3);
				}
				despawnNpc(npc);
				break;
			case 231087:
			case 231088:
			case 231089:
				Protectors4++;
				if (Protectors4 >= 3) {
					final Npc generator4 = getNpc(231086);
					remove(generator4);
				}
				despawnNpc(npc);
				break;
			case 231074:
			case 231078:
			case 231082:
			case 231086:
				sendMessage(1401795);
				ShieldGenerator++;
				if (ShieldGenerator >= 4) {
					final Npc hyperion = getNpc(231073);
					despawnNpc(shield1);
					sendMessage(1401796);
					despawnNpc(shield2);
					remove(hyperion);
					startSp();
					startCheck();
					startCheckID();
				}
				despawnNpc(npc);
				break;
			case 231073:
				spawn(730842, 143.37f, 135.30f, 112.17f, (byte) 55);
				stopTask();
				DestroyInst();
				break;
			case 231090:
			case 231091:
			case 231092:
			case 231093:
			case 231094:
			case 231095:
			case 231096:
			case 231097:
			case 233298:
			case 233299:
			case 231102:
			case 231103:
				despawnNpc(npc);
				break;
		}
	}

	private void CargeCast(int Nm) {
		switch (Nm) {
			case 1:
				if (instance.getNpc(231092) != null) {
					performSkillToTarget(231092, 231073, 21258);
					buff++;
					sendChargeMessage(buff);
					CastBuffHyperion(1);
				}
				break;
			case 2:
				if (instance.getNpc(231093) != null) {
					performSkillToTarget(231093, 231073, 21382);
					buff++;
					sendChargeMessage(buff);
					CastBuffHyperion(2);
				}
				break;
			case 3:
				if (instance.getNpc(231094) != null) {
					performSkillToTarget(231094, 231073, 21384);
					buff++;
					sendChargeMessage(buff);
					CastBuffHyperion(3);
				}
				break;
			case 4:
				if (instance.getNpc(231095) != null) {
					performSkillToTarget(231095, 231073, 21257);
					buff++;
					sendChargeMessage(buff);
					CastBuffHyperion(4);
				}
				break;

		}
	}

	private void CastBuff(int Nm) {

		switch (Nm) {
			case 1:
				if (instance.getNpc(231092) != null)
					performSkillToTarget(231092, 231092, 21371);
				performSkillToTarget(231092, 231092, 20984);
				break;
			case 2:
				if (instance.getNpc(231093) != null)
					performSkillToTarget(231093, 231093, 21371);
				performSkillToTarget(231093, 231093, 20984);
				break;
			case 3:
				if (instance.getNpc(231094) != null)
					performSkillToTarget(231094, 231094, 21371);
				performSkillToTarget(231094, 231094, 20984);
				break;
			case 4:
				if (instance.getNpc(231095) != null)
					performSkillToTarget(231095, 231095, 21371);
				performSkillToTarget(231095, 231095, 20984);
				break;

		}
	}

	private void sendChargeMessage(int Act) {

		switch (Act) {
			case 1:
				sendMessage(1401791);
				break;
			case 2:
				sendMessage(1401792);
				break;
			case 3:
				sendMessage(1401793);
				break;
			case 4:
				sendMessage(1401794);
				break;
		}

	}

	private void sendMessage(final int msg) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
			}
		});
	}

	private void performSkillToTarget(int npcId, int targetId, int skillId) {
		final Npc npc = instance.getNpc(npcId);
		final Npc target = instance.getNpc(targetId);
		SkillEngine.getInstance().getSkill(npc, skillId, 60, target).useSkill();
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void remove(Npc npc) {
		npc.getEffectController().removeEffect(21371);
		npc.getEffectController().removeEffect(20984);
	}

	private void stopTask() {
		if (spIda != null) {
			spIda.cancel(false);
		}
		if (Cast != null) {
			Cast.cancel(false);
		}
		if (CastBuff1 != null) {
			CastBuff1.cancel(false);
		}
		if (CastBuff2 != null) {
			CastBuff2.cancel(false);
		}
		if (CastBuff3 != null) {
			CastBuff3.cancel(false);
		}
		if (CastBuff4 != null) {
			CastBuff4.cancel(false);
		}
		if (CheckBuff != null) {
			CheckBuff.cancel(false);
		}
		if (Leave != null) {
			Leave.cancel(false);
		}
		if (CheckID != null) {
			CheckID.cancel(false);
		}
	}

	@Override
	public void onInstanceDestroy() {
		ShieldGenerator = 0;
		Protectors1 = 0;
		Protectors2 = 0;
		Protectors3 = 0;
		Protectors4 = 0;
		stopTask();
		if (Destroy != null) {
			Destroy.cancel(false);
		}
	}
}
