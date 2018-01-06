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
package com.aionemu.gameserver.model.siege;

import java.util.Iterator;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * Calculates fortresses as 10 points and artifacts as 1 point each. Need to find retail calculation. (Upper forts worth more...)
 *
 * @author Sarynth
 * @updated Eloann
 */
public class Influence {

	private static final Influence instance = new Influence();
	private float inggison_e = 0;
	private float inggison_a = 0;
	private float inggison_b = 0;
	private float gelkmaros_e = 0;
	private float gelkmaros_a = 0;
	private float gelkmaros_b = 0;
	private float abyss_e = 0;
	private float abyss_a = 0;
	private float abyss_b = 0;
	private float kaldor_e = 0;
	private float kaldor_a = 0;
	private float kaldor_b = 0;
	private float belus_e = 0;
	private float belus_a = 0;
	private float belus_b = 0;
	private float aspida_e = 0;
	private float aspida_a = 0;
	private float aspida_b = 0;
	private float atanatos_e = 0;
	private float atanatos_a = 0;
	private float atanatos_b = 0;
	private float disillon_e = 0;
	private float disillon_a = 0;
	private float disillon_b = 0;
	private float global_e = 0;
	private float global_a = 0;
	private float global_b = 0;

	private Influence() {
		calculateInfluence();
	}

	public static Influence getInstance() {
		return instance;
	}

	/**
	 * Recalculates Influence
	 */
	public void recalculateInfluence() {
		calculateInfluence();
	}

	/**
	 * calculate influence
	 */
	private void calculateInfluence() {
		float balaurea = 0.0039512194f;
		float abyss = 0.006097561f;
		float e_inggison = 0;
		float a_inggison = 0;
		float b_inggison = 0;
		float t_inggison = 0;
		float e_gelkmaros = 0;
		float a_gelkmaros = 0;
		float b_gelkmaros = 0;
		float t_gelkmaros = 0;
		float e_abyss = 0;
		float a_abyss = 0;
		float b_abyss = 0;
		float t_abyss = 0;
		float e_kaldor = 0;
		float a_kaldor = 0;
		float b_kaldor = 0;
		float t_kaldor = 0;
		for (SiegeLocation sLoc : SiegeService.getInstance().getSiegeLocations().values()) {
			switch (sLoc.getWorldId()) {
				case 210050000:
					if (sLoc instanceof FortressLocation) {
						t_inggison += sLoc.getInfluenceValue();
						switch (sLoc.getRace()) {
							case ELYOS:
								e_inggison += sLoc.getInfluenceValue();
								break;
							case ASMODIANS:
								a_inggison += sLoc.getInfluenceValue();
								break;
							case BALAUR:
								b_inggison += sLoc.getInfluenceValue();
								break;
						}
					}
					break;
				case 220070000:
					if (sLoc instanceof FortressLocation) {
						t_gelkmaros += sLoc.getInfluenceValue();
						switch (sLoc.getRace()) {
							case ELYOS:
								e_gelkmaros += sLoc.getInfluenceValue();
								break;
							case ASMODIANS:
								a_gelkmaros += sLoc.getInfluenceValue();
								break;
							case BALAUR:
								b_gelkmaros += sLoc.getInfluenceValue();
								break;
						}
					}
					break;
				case 400010000:
					if (sLoc instanceof FortressLocation) {
						t_abyss += sLoc.getInfluenceValue();
						switch (sLoc.getRace()) {
							case ELYOS:
								e_abyss += sLoc.getInfluenceValue();
								break;
							case ASMODIANS:
								a_abyss += sLoc.getInfluenceValue();
								break;
							case BALAUR:
								b_abyss += sLoc.getInfluenceValue();
								break;
						}
					}
					break;
				case 600090000:
					if (sLoc instanceof FortressLocation) {
						t_kaldor += sLoc.getInfluenceValue();
						switch (sLoc.getRace()) {
							case ELYOS:
								e_kaldor += sLoc.getInfluenceValue();
								break;
							case ASMODIANS:
								a_kaldor += sLoc.getInfluenceValue();
								break;
							case BALAUR:
								b_kaldor += sLoc.getInfluenceValue();
						}
					}
					break;
			}
		}

		inggison_e = e_inggison / t_inggison;
		inggison_a = a_inggison / t_inggison;
		inggison_b = b_inggison / t_inggison;

		gelkmaros_e = e_gelkmaros / t_gelkmaros;
		gelkmaros_a = a_gelkmaros / t_gelkmaros;
		gelkmaros_b = b_gelkmaros / t_gelkmaros;

		abyss_e = e_abyss / t_abyss;
		abyss_a = a_abyss / t_abyss;
		abyss_b = b_abyss / t_abyss;

		kaldor_e = (e_kaldor / t_kaldor);
		kaldor_a = (a_kaldor / t_kaldor);
		kaldor_b = (b_kaldor / t_kaldor);

		// global_e = ((kaldor_e * balaurea + inggison_e * balaurea + gelkmaros_e * balaurea + abyss_e * abyss) * 100f);
		// global_a = ((kaldor_a * balaurea + inggison_a * balaurea + gelkmaros_a * balaurea + abyss_a * abyss) * 100f);
		// global_b = ((kaldor_b * balaurea + inggison_b * balaurea + gelkmaros_b * balaurea + abyss_b * abyss) * 100f);
		// without Inggison and Gelkmaros since 5.3
		global_e = ((kaldor_e * balaurea + abyss_e * abyss) * 100f);
		global_a = ((kaldor_a * balaurea + abyss_a * abyss) * 100f);
		global_b = ((kaldor_b * balaurea + abyss_b * abyss) * 100f);
	}

	/**
	 * Broadcast packet with influence update to all players. - Responsible for the message "The Divine Fortress is now vulnerable."
	 */
	@SuppressWarnings("unused")
	private void broadcastInfluencePacket() {
		SM_INFLUENCE_RATIO pkt = new SM_INFLUENCE_RATIO();

		Player player;
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			player = iter.next();
			PacketSendUtility.sendPacket(player, pkt);
		}
	}

	/**
	 * @return elyos control
	 */
	public float getGlobalElyosInfluence() {
		return this.global_e;
	}

	/**
	 * @return asmos control
	 */
	public float getGlobalAsmodiansInfluence() {
		return this.global_a;
	}

	/**
	 * @return balaur control
	 */
	public float getGlobalBalaursInfluence() {
		return this.global_b;
	}

	/**
	 * @return elyos control
	 */
	public float getInggisonElyosInfluence() {
		return this.inggison_e;
	}

	/**
	 * @return asmos control
	 */
	public float getInggisonAsmodiansInfluence() {
		return this.inggison_a;
	}

	/**
	 * @return balaur control
	 */
	public float getInggisonBalaursInfluence() {
		return this.inggison_b;
	}

	/**
	 * @return elyos control
	 */
	public float getGelkmarosElyosInfluence() {
		return this.gelkmaros_e;
	}

	/**
	 * @return asmos control
	 */
	public float getGelkmarosAsmodiansInfluence() {
		return this.gelkmaros_a;
	}

	/**
	 * @return balaur control
	 */
	public float getGelkmarosBalaursInfluence() {
		return this.gelkmaros_b;
	}

	/**
	 * @return elyos control
	 */
	public float getAbyssElyosInfluence() {
		return this.abyss_e;
	}

	/**
	 * @return asmos control
	 */
	public float getAbyssAsmodiansInfluence() {
		return this.abyss_a;
	}

	/**
	 * @return balaur control
	 */
	public float getAbyssBalaursInfluence() {
		return this.abyss_b;
	}

	/**
	 * @return elyos control
	 */
	public float getKaldorElyosInfluence() {
		return this.kaldor_e;
	}

	/**
	 * @return asmos control
	 */
	public float getKaldorAsmodiansInfluence() {
		return this.kaldor_a;
	}

	/**
	 * @return balaur control
	 */
	public float getKaldorBalaursInfluence() {
		return this.kaldor_b;
	}

	/**
	 * @return elyos control
	 */
	public float getBelusElyosInfluence() {
		return this.belus_e;
	}

	/**
	 * @return asmos control
	 */
	public float getBelusAsmodiansInfluence() {
		return this.belus_a;
	}

	/**
	 * @return balaur control
	 */
	public float getBelusBalaursInfluence() {
		return this.belus_b;
	}

	/**
	 * @return elyos control
	 */
	public float getAspidaElyosInfluence() {
		return this.aspida_e;
	}

	/**
	 * @return asmos control
	 */
	public float getAspidaAsmodiansInfluence() {
		return this.aspida_a;
	}

	/**
	 * @return balaur control
	 */
	public float getAspidaBalaursInfluence() {
		return this.aspida_b;
	}

	/**
	 * @return elyos control
	 */
	public float getAtanatosElyosInfluence() {
		return this.atanatos_e;
	}

	/**
	 * @return asmos control
	 */
	public float getAtanatosAsmodiansInfluence() {
		return this.atanatos_a;
	}

	/**
	 * @return balaur control
	 */
	public float getAtanatosBalaursInfluence() {
		return this.atanatos_b;
	}

	/**
	 * @return elyos control
	 */
	public float getDisillonElyosInfluence() {
		return this.disillon_e;
	}

	/**
	 * @return asmos control
	 */
	public float getDisillonAsmodiansInfluence() {
		return this.disillon_a;
	}

	/**
	 * @return balaur control
	 */
	public float getDisillonBalaursInfluence() {
		return this.disillon_b;
	}

	/**
	 * @return float containing dmg modifier for disadvantaged race
	 */
	public float getPvpRaceBonus(Race attRace) {
		float bonus = 1;
		float elyos = getGlobalElyosInfluence();
		float asmo = getGlobalAsmodiansInfluence();
		switch (attRace) {
			case ASMODIANS:
				if (elyos >= 0.81f && asmo <= 0.10f) {
					bonus = 1.2f;
				}
				else if (elyos >= 0.81f || (elyos >= 0.71f && asmo <= 0.10f)) {
					bonus = 1.15f;
				}
				else if (elyos >= 0.71f) {
					bonus = 1.1f;
				}
				break;
			case ELYOS:
				if (asmo >= 0.81f && elyos <= 0.10f) {
					bonus = 1.2f;
				}
				else if (asmo >= 0.81f || (asmo >= 0.71f && elyos <= 0.10f)) {
					bonus = 1.15f;
				}
				else if (asmo >= 0.71f) {
					bonus = 1.1f;
				}
				break;
			default:
				break;
		}
		return bonus;
	}
}
