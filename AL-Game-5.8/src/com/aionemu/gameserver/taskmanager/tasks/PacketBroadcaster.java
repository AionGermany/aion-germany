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
package com.aionemu.gameserver.taskmanager.tasks;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.AbstractFIFOPeriodicTaskManager;

/**
 * @author lord_rex and MrPoke
 */
public final class PacketBroadcaster extends AbstractFIFOPeriodicTaskManager<Creature> {

	private static final class SingletonHolder {

		private static final PacketBroadcaster INSTANCE = new PacketBroadcaster();
	}

	public static PacketBroadcaster getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private PacketBroadcaster() {
		super(200);
		log.debug("[PacketBroadCaster] Init Packet Broadcaster...");
	}

	public static enum BroadcastMode {

		UPDATE_STATS {

			@Override
			public void sendPacket(Creature creature) {
				creature.getGameStats().updateStatInfo();
			}
		},
		UPDATE_SPEED {

			@Override
			public void sendPacket(Creature creature) {
				creature.getGameStats().updateSpeedInfo();
			}
		},
		UPDATE_PLAYER_HP_STAT {

			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendHpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_MP_STAT {

			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendMpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_EFFECT_ICONS {

			@Override
			public void sendPacket(Creature creature) {
				creature.getEffectController().updatePlayerEffectIconsImpl();
			}
		},
		UPDATE_PLAYER_FLY_TIME {

			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendFpPacketUpdateImpl();
			}
		},
		BROAD_CAST_EFFECTS {

			@Override
			public void sendPacket(Creature creature) {
				creature.getEffectController().broadCastEffectsImp();
			}
		};

		private final byte MASK;

		private BroadcastMode() {
			MASK = (byte) (1 << ordinal());
		}

		public byte mask() {
			return MASK;
		}

		protected abstract void sendPacket(Creature creature);

		protected final void trySendPacket(final Creature creature, byte mask) {
			if ((mask & mask()) == mask()) {
				sendPacket(creature);
				creature.removePacketBroadcastMask(this);
			}
		}
	}

	private static final BroadcastMode[] VALUES = BroadcastMode.values();

	@Override
	protected void callTask(Creature creature) {
		for (byte mask; (mask = creature.getPacketBroadcastMask()) != 0;) {
			for (BroadcastMode mode : VALUES) {
				mode.trySendPacket(creature, mask);
			}
		}
	}

	@Override
	protected String getCalledMethodName() {
		return "packetBroadcast()";
	}
}
