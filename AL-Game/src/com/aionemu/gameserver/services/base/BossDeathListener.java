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
package com.aionemu.gameserver.services.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.eventcallback.OnDieEventCallback;
import com.aionemu.gameserver.configs.main.BaseConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Source
 */
@SuppressWarnings("rawtypes")
public class BossDeathListener extends OnDieEventCallback {

	private static final Logger log = LoggerFactory.getLogger(BossDeathListener.class);

	private final Base<?> base;

	public BossDeathListener(Base base) {
		this.base = base;
	}

	@Override
	public void onBeforeDie(AbstractAI obj) {
		AionObject winner = base.getBoss().getAggroList().getMostDamage();
		Npc boss = base.getBoss();
		Race race = null;

		if (winner instanceof Creature) {
			final Creature kill = (Creature) winner;
			applyBaseBuff();
            if (kill.getRace().isPlayerRace()) {
                base.setRace(kill.getRace());
                race = kill.getRace();

                if (BaseConfig.ENABLE_BASE_REWARDS) {
                    if (kill instanceof Player) {
                        giveBaseRewardsToPlayers((Player) kill);
                    }
                }
			}
			announceCapture(null, kill);
		}
		else if (winner instanceof TemporaryPlayerTeam) {
			final TemporaryPlayerTeam team = (TemporaryPlayerTeam) winner;
			applyBaseBuff();
			if (team.getRace().isPlayerRace()) {
				base.setRace(team.getRace());
				race = team.getRace();
			}
			announceCapture(team, null);
		}
		else {
			base.setRace(Race.NPC);
		}
		if (base.getBaseLocation().getWorldId() == 400010000) {
			if (race == Race.ASMODIANS && boss.getRace() == Race.ELYOS) {
				AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
			}
			if (race == Race.ELYOS && boss.getRace() == Race.ASMODIANS) {
				AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
			}
			landingWinBase(race);
		}
		BaseService.getInstance().capture(base.getId(), base.getRace());
		log.info("Legat kill ! BOSS: " + boss + " in BaseId: " + base.getBaseLocation().getId() + " killed by RACE: " + race);
	}

	public void announceCapture(final TemporaryPlayerTeam team, final Creature kill) {
		final String baseName = base.getBaseLocation().getName();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (team != null && kill == null) {
					// %0 succeeded in conquering %1
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, team.getRace().getRaceDescriptionId(), baseName));
				}
				else {
					// %0 succeeded in conquering %1
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, kill.getRace().getRaceDescriptionId(), baseName));
				}
				// Abyss Landing 4.9.1
				switch (player.getWorldId()) {
					case 400010000: // Reshanta.
						if (team != null && kill == null) {
							// %0 has occupied %1 Base and the Landing is now enhanced
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403186, team.getRace().getRaceDescriptionId(), baseName));
						}
						else {
							// %0 has occupied %1 Base and the Landing is now enhanced
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403186, kill.getRace().getRaceDescriptionId(), baseName));
						}
						break;
				}
			}
		});
	}

	public void applyBaseBuff() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
					SkillEngine.getInstance().applyEffectDirectly(12115, player, player, 0); // Kaisinel's Bane
					// The power of Kaisinel's Protection surrounds you
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_LIGHT_GAIN, 10000);
				}
				else if (player.getCommonData().getRace() == Race.ASMODIANS) {
					SkillEngine.getInstance().applyEffectDirectly(12117, player, player, 0); // Marchutan's Bane
					// The power of Marchutan's Protection surrounds you
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_DARK_GAIN, 10000);
				}
			}
		});
	}

	protected void giveBaseRewardsToPlayers(Player player) {
		switch (player.getWorldId()) {
			case 210020000: // Eltnen
			case 210040000: // Heiron
			case 220020000: // Morheim
			case 220040000: // Beluslan
				HTMLService.sendGuideHtml(player, "adventurers_base1");
				break;
			case 600090000: // Kaldor
			case 600100000: // Levinshor
				HTMLService.sendGuideHtml(player, "adventurers_base2");
				break;
			case 400020000: // Belus
			case 400040000: // Aspida
			case 400050000: // Atanatos
			case 400060000: // Disillon
				HTMLService.sendGuideHtml(player, "adventurers_base3");
				break;
		}
	}

	public void landingWinBase(Race race) {
		if (race == Race.ASMODIANS) {
			AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
		}
		if (race == Race.ELYOS) {
			AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
		}
	}

	@Override
	public void onAfterDie(AbstractAI obj) {
	}

}
