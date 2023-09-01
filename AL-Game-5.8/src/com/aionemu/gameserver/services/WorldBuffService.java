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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.WorldBuff;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.worldbuff.WorldBuffTemplate;
import com.aionemu.gameserver.model.templates.worldbuff.WorldBuffType;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Steve
 */
public class WorldBuffService {

	public FastMap<Integer, FastList<WorldBuffTemplate>> worldBuff = FastMap.newInstance();

	public WorldBuffService() {
		worldBuff = DataManager.WORLD_BUFF_DATA.getWorldbuffs();
	}

	public void onReturnHome(Npc npc) {
		FastList<WorldBuffTemplate> buff = worldBuff.get(npc.getWorldId());
		if (buff == null) {
			return;
		}
		for (FastList.Node<WorldBuffTemplate> n = buff.head(), end = buff.tail(); (n = n.getNext()) != end;) {
			WorldBuffTemplate bf = n.getValue();
			if (bf.getType().equals(WorldBuffType.PC)) {
				continue;
			}
			for (FastList.Node<TribeClass> t = bf.getTribe().head(), endt = bf.getTribe().tail(); (t = t.getNext()) != endt;) {
				if (npc.getTribe().equals(t.getValue())) {
					buff(bf, npc);
					break;
				}
			}
			if (!bf.getNpcIds().isEmpty() && bf.getNpcIds().contains(npc.getNpcId())) {
				buff(bf, npc);
			}
		}
	}

	public void onAfterSpawn(Creature creature) {
		FastList<WorldBuffTemplate> buff = worldBuff.get(creature.getWorldId());
		if (buff == null) {
			return;
		}

		for (FastList.Node<WorldBuffTemplate> n = buff.head(), end = buff.tail(); (n = n.getNext()) != end;) {
			WorldBuffTemplate bf = n.getValue();
			if (creature instanceof Player) {
				Player player = (Player) creature;
				if (bf.getType().equals(WorldBuffType.NPC)) {
					continue;
				}
				if (player.getWorldBuffList() != null) {
					FastList<WorldBuff> buffList = player.getWorldBuffList();
					for (FastList.Node<WorldBuff> b = buffList.head(), endb = buffList.tail(); (b = b.getNext()) != endb;) {
						if (player.getWorldId() == b.getValue().getWorldId()) {
							buff(bf, creature);
							break;
						}
					}
				}
				else {
					player.addWorldBuff(new WorldBuff(bf.getSkillIds(), player.getWorldId()));
					buff(bf, creature);
				}
			}
			if (creature instanceof Npc) {
				Npc npc = (Npc) creature;
				if (bf.getType().equals(WorldBuffType.PC)) {
					continue;
				}
				for (FastList.Node<TribeClass> t = bf.getTribe().head(), endt = bf.getTribe().tail(); (t = t.getNext()) != endt;) {
					if (npc.getTribe().equals(t.getValue())) {
						buff(bf, npc);
						break;
					}
				}
				if (!bf.getNpcIds().isEmpty() && bf.getNpcIds().contains(npc.getNpcId())) {
					buff(bf, npc);
				}
			}
		}
	}

	public void onEnterWorld(Player player) {
		if (player.getWorldBuffList() == null) {
			onAfterSpawn(player);
			return;
		}
		FastList<WorldBuff> buff = player.getWorldBuffList();
		for (FastList.Node<WorldBuff> n = buff.head(), end = buff.tail(); (n = n.getNext()) != end;) {
			if (player.getWorldId() != n.getValue().getWorldId()) {
				for (FastList.Node<Integer> s = n.getValue().getSkillIds().head(), ends = n.getValue().getSkillIds().tail(); (s = s.getNext()) != ends;) {
					player.getEffectController().removeEffect(s.getValue());
				}
				player.getWorldBuffList().remove(n.getValue());
			}
		}
	}

	public void onLogOut(Player player) {
		if (player.getWorldBuffList() == null) {
			return;
		}
		FastList<WorldBuff> buff = player.getWorldBuffList();
		for (FastList.Node<WorldBuff> n = buff.head(), end = buff.tail(); (n = n.getNext()) != end;) {
			if (player.getWorldId() != n.getValue().getWorldId()) {
				for (FastList.Node<Integer> s = n.getValue().getSkillIds().head(), ends = n.getValue().getSkillIds().tail(); (s = s.getNext()) != ends;) {
					player.getEffectController().removeEffect(s.getValue());
				}
				player.getWorldBuffList().remove(n.getValue());
			}
		}
	}

	private void buff(WorldBuffTemplate buff, Creature creature) {
		for (FastList.Node<Integer> n = buff.getSkillIds().head(), end = buff.getSkillIds().tail(); (n = n.getNext()) != end;) {
			if (creature.getEffectController().hasAbnormalEffect(n.getValue())) {
				return;
			}
			SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(n.getValue());
			if (template == null) {
				return;
			}
			SkillEngine.getInstance().applyEffectDirectly(template.getSkillId(), creature, creature, buff.getDuration() > 0 ? buff.getDuration() : template.getDuration(), buff.isNoRemoveAtDie());
		}
	}

	public static final WorldBuffService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final WorldBuffService instance = new WorldBuffService();
	}
}
