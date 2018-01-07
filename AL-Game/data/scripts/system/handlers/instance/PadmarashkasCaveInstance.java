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

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Ritsu, Luzien
 * @see http://gameguide.na.aiononline.com/aion/Padmarashka%27s+Cave+Walkthrough
 */
@InstanceID(320150000)
public class PadmarashkasCaveInstance extends GeneralInstanceHandler {

	boolean moviePlayed = false;
	private int killedPadmarashkaProtector = 0;
	private int killedEggs = 0;

	@Override
	public void onDie(Npc npc) {
		final int npcId = npc.getNpcId();
		switch (npcId) {
			case 218670:
			case 218671:
			case 218673:
			case 218674:
				if (++killedPadmarashkaProtector == 4) {
					killedPadmarashkaProtector = 0;
					final Npc padmarashka = getNpc(218756);
					if (padmarashka != null && !padmarashka.getLifeStats().isAlreadyDead()) {
						padmarashka.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
						padmarashka.getEffectController().broadCastEffectsImp();
						SkillEngine.getInstance().getSkill(padmarashka, 19187, 55, padmarashka).useNoAnimationSkill();
						padmarashka.getEffectController().removeEffect(19186); // skill should handle this TODO: fix
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								padmarashka.getAi2().onCreatureEvent(AIEventType.CREATURE_AGGRO, instance.getPlayersInside().get(0));
							}
						}, 1000);
					}
				}
				break;
			case 282613:
			case 282614:
				if (++killedEggs == 20) { // TODO: find value
					final Npc padmarashka = getNpc(218756);
					if (padmarashka != null && !padmarashka.getLifeStats().isAlreadyDead()) {
						SkillEngine.getInstance().applyEffectDirectly(20101, padmarashka, padmarashka, 0);
					}
				}
				break;
		}
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("PADMARASHKAS_NEST_320150000")) {
			if (!moviePlayed) {
				sendMovie();
			}
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	private void sendMovie() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 488));
				moviePlayed = true;
			}
		});
	}

	@Override
	public void onInstanceDestroy() {
		moviePlayed = false;
		killedPadmarashkaProtector = 0;
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
