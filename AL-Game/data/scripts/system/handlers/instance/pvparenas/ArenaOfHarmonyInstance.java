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
package instance.pvparenas;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;

/**
 * @author xTz
 */
@InstanceID(300450000)
public class ArenaOfHarmonyInstance extends HarmonyTrainingCenterInstance {

	@Override
	protected void reward() {
		float totalScoreAP = (9.599f * 3) * 100;
		float totalScoreCourage = (0.1f * 3) * 100;
		int totalPoints = instanceReward.getTotalPoints();
		for (HarmonyGroupReward group : instanceReward.getGroups()) {
			int score = group.getPoints();
			int rank = instanceReward.getRank(score);
			float percent = group.getParticipation();
			float scoreRate = ((float) score / (float) totalPoints);
			int basicAP = 200;
			int rankingAP = 0;
			basicAP *= percent;
			int basicCoI = 0;
			int rankingCoI = 0;
			basicCoI *= percent;
			int scoreAP = (int) (totalScoreAP * scoreRate);
			switch (rank) {
				case 0:
					rankingAP = 4681;
					rankingCoI = 49;
					group.setGloryTicket(1);
					break;
				case 1:
					rankingAP = 1887;
					rankingCoI = 20;
					break;
				case 2:
					rankingAP = 151;
					rankingCoI = 1;
					break;
			}
			rankingAP *= percent;
			rankingCoI *= percent;
			int scoreCoI = (int) (totalScoreCourage * scoreRate);
			group.setBasicAP(basicAP);
			group.setRankingAP(rankingAP);
			group.setScoreAP(scoreAP);
			group.setBasicCourage(basicCoI);
			group.setRankingCourage(rankingCoI);
			group.setScoreCourage(scoreCoI);
		}
		super.reward();
	}
}
