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
package ai.siege;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

/**
 * @author Source
 */
@AIName("incarnate")
public class IncarnateAI2 extends AggressiveNpcAI2 {

	Future<?> avatar_scan;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		avatar_scan = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (SiegeConfig.SIEGE_IDA_ENABLED) {
					getOwner().getKnownList().doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player player) {
							if (player.getAbyssRank().getRank().getId() > AbyssRankEnum.STAR4_OFFICER.getId()) {
								boolean inform = false;
								EffectController controller = player.getEffectController();
								for (Effect eff : controller.getAbnormalEffects()) {
									if (eff.isDeityAvatar()) {
										eff.endEffect();
										getOwner().getEffectController().clearEffect(eff);
										inform = true;
									}
								}

								if (inform) {
									String message = "The power of incarnation removes " + player.getName() + " morph state.";
									PacketSendUtility.broadcastPacket(getOwner(), new SM_MESSAGE(getObjectId(), getOwner().getName(), message, ChatType.BRIGHT_YELLOW_CENTER));
								}
							}
						}
					});
				}
			}
		}, 10000, 10000);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		avatar_scan.cancel(true);
	}
}
