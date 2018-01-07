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
package ai.instance.museumKnowledge;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Rinzler
 */
@AIName("IDEternity_03_Def_Boss_Energy") //246421, 246422, 246423, 246424
public class IDEternity_03_Def_Boss_EnergyAI2 extends NpcAI2 {

	@Override
	public void think() {
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 17746, 60, getOwner()).useNoAnimationSkill();
	}

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 246421: // IDEternity_03_Def_Boss_Energy_01.
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(246421, 408.66196f, 1013.1304f, 711.93115f, (byte) 0, 177); // IDEternity_03_Def_Boss_Energy_01.
					}
				}, 120000);
				break;
			case 246422: // IDEternity_03_Def_Boss_Energy_02.
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(246422, 408.77426f, 1037.3873f, 711.90881f, (byte) 0, 179); // IDEternity_03_Def_Boss_Energy_02.
					}
				}, 120000);
				break;
			case 246423: // IDEternity_03_Def_Boss_Energy_03.
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(246423, 386.68903f, 1037.3842f, 711.95770f, (byte) 0, 181); // IDEternity_03_Def_Boss_Energy_03.
					}
				}, 120000);
				break;
			case 246424: // IDEternity_03_Def_Boss_Energy_04.
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(246424, 386.68146f, 1013.2744f, 711.93091f, (byte) 0, 183); // IDEternity_03_Def_Boss_Energy_04.
					}
				}, 120000);
				break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
