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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceBuff;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * This packet is notify client what map should be loaded.
 *
 * @author -Nemesiss-
 */
public class SM_PLAYER_SPAWN extends AionServerPacket {

	/**
	 * Player that is entering game.
	 */
	private final Player player;
	private static InstanceBuff instanceBuff;

	/**
	 * Constructor.
	 *
	 * @param player
	 */
	public SM_PLAYER_SPAWN(Player player) {
		super();
		this.player = player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getWorldId());
		writeD(player.getWorldId()); // world + chnl
		writeD(0x00); // unk
		writeC(WorldMapType.getWorld(player.getWorldId()).isPersonal() ? 1 : 0);
		writeF(player.getX()); // x
		writeF(player.getY()); // y
		writeF(player.getZ()); // z
		writeC(player.getHeading()); // heading
		writeD(0);
		writeD(0); // TODO => can take some value but dunno what this info is atm
		if ((player.getWorldId() == 210020000 || player.getWorldId() == 210040000 || player.getWorldId() == 210050000 || player.getWorldId() == 210060000 || player.getWorldId() == 210070000 || player.getWorldId() == 210100000 || player.getWorldId() == 220020000 || player.getWorldId() == 220040000 || player.getWorldId() == 220050000 || player.getWorldId() == 220070000 || player.getWorldId() == 220080000 || player.getWorldId() == 220110000 || player.getWorldId() == 400010000 || player.getWorldId() == 400020000 || player.getWorldId() == 400040000 || player.getWorldId() == 400050000 || player.getWorldId() == 400060000 || player.getWorldId() == 600010000 || player.getWorldId() == 600090000 || player.getWorldId() == 600100000)) {
			// This game area is a free PK area. Abide by the rules and you will have a great gaming experience.
			// PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSGBOX_AKS_ENTER_PK_SERVER, 0, 0));
			writeD(1);
			instanceBuff = new InstanceBuff(1);
			instanceBuff.applyPledge(player, 1);
			// TODO CHECK this
		}
		if ((player.getWorldId() == 300200000 || player.getWorldId() == 320100000 || player.getWorldId() == 320050000 || player.getWorldId() == 300030000)) {
			writeD(2);
			instanceBuff = new InstanceBuff(2);
			instanceBuff.applyPledge(player, 2);
		}
		else if ((player.getWorldId() == 300080000 || player.getWorldId() == 300090000 || player.getWorldId() == 300060000 || player.getWorldId() == 301160000 || player.getWorldId() == 310050000 || player.getWorldId() == 320110000 || player.getWorldId() == 310100000 || player.getWorldId() == 300230000 || player.getWorldId() == 320080000)) {
			writeD(3);
			instanceBuff = new InstanceBuff(3);
			instanceBuff.applyPledge(player, 3);
		}
		else if ((player.getWorldId() == 300050000 || player.getWorldId() == 300070000 || player.getWorldId() == 300110000 || player.getWorldId() == 300140000 || player.getWorldId() == 300120000 || player.getWorldId() == 300130000 || player.getWorldId() == 300100000 || player.getWorldId() == 300460000 || player.getWorldId() == 310090000 || player.getWorldId() == 320130000 || player.getWorldId() == 310110000 || player.getWorldId() == 300040000)) {
			writeD(4);
			instanceBuff = new InstanceBuff(4);
			instanceBuff.applyPledge(player, 4);
		}
		else if ((player.getWorldId() == 300210000 || player.getWorldId() == 300300000 || player.getWorldId() == 300320000 || player.getWorldId() == 300250000 || player.getWorldId() == 300160000 || player.getWorldId() == 300560000 || player.getWorldId() == 300190000 || player.getWorldId() == 300150000 || player.getWorldId() == 300170000 || player.getWorldId() == 300240000 || player.getWorldId() == 320150000 || player.getWorldId() == 300700000 || player.getWorldId() == 300280000 || player.getWorldId() == 300440000)) {
			writeD(5);
			instanceBuff = new InstanceBuff(5);
			instanceBuff.applyPledge(player, 5);
		}
		else if ((player.getWorldId() == 300510000 || player.getWorldId() == 300520000 || player.getWorldId() == 300600000 || player.getWorldId() == 300480000 || player.getWorldId() == 301110000 || player.getWorldId() == 301140000 || player.getWorldId() == 300800000 || player.getWorldId() == 300590000 || player.getWorldId() == 301130000 || player.getWorldId() == 300540000 || player.getWorldId() == 301230000 || player.getWorldId() == 301240000 || player.getWorldId() == 301250000 || player.getWorldId() == 301260000 || player.getWorldId() == 301270000 || player.getWorldId() == 301280000 || player.getWorldId() == 301290000 || player.getWorldId() == 301300000 || player.getWorldId() == 301360000 || player.getWorldId() == 301370000 || player.getWorldId() == 300610000 || player.getWorldId() == 300620000 || player.getWorldId() == 300630000 || player.getWorldId() == 301380000 || player.getWorldId() == 301390000 || player.getWorldId() == 301510000 || player.getWorldId() == 301540000 || player.getWorldId() == 301550000 || player.getWorldId() == 301600000 || player.getWorldId() == 301610000 || player.getWorldId() == 301620000 || player.getWorldId() == 301650000 || player.getWorldId() == 301660000 || player.getWorldId() == 302100000|| this.player.getWorldId() == 302200000 || this.player.getWorldId() == 302300000 || this.player.getWorldId() == 302340000 || this.player.getWorldId() == 302400000)) {
			writeD(6);
			instanceBuff = new InstanceBuff(6);
			instanceBuff.applyPledge(player, 6);
		}
		else if ((player.getWorldId() == 300350000 || player.getWorldId() == 300360000 || player.getWorldId() == 300420000 || player.getWorldId() == 300430000 || player.getWorldId() == 300450000 || player.getWorldId() == 300550000 || player.getWorldId() == 300570000 || player.getWorldId() == 301100000)) {
			writeD(9);
			instanceBuff = new InstanceBuff(9);
			instanceBuff.applyPledge(player, 9);
		}
		else if ((player.getWorldId() == 301320000 || player.getWorldId() == 301330000 || player.getWorldId() == 301400000 || player.getWorldId() == 301590000 || player.getWorldId() == 301630000 || player.getWorldId() == 301640000 || player.getWorldId() == 302000000)) {
			writeD(14);
			instanceBuff = new InstanceBuff(14);
			instanceBuff.applyPledge(player, 14);
		}
        else if (this.player.getWorldId() == 302350000) {
            this.writeD(17);
            instanceBuff = new InstanceBuff(17);
            instanceBuff.applyPledge(player, 17);
        }
		else {
			writeD(0);
			if (player.getBonusId() > 0) {
				instanceBuff.endPledge(player);
			}
		}
		// Fast Track Server
		writeC(World.getInstance().getWorldMap(player.getWorldId()).getTemplate().getBeginnerTwinCount() > 0 && FastTrackConfig.FASTTRACK_ENABLE ? 1 : 0);
		writeD(0); // 4.0 protocol changed
		writeC(0); // 4.7 new part
	}
}
