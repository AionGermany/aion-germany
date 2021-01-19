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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.TransformType;

/**
 * @author Sweetkr, xTz
 */
public class SM_TRANSFORM extends AionServerPacket {

	private Creature creature;
	private int state;
	@SuppressWarnings("unused")
	private int modelId;
	private boolean applyEffect;
	@SuppressWarnings("unused")
	private int panelId;
	@SuppressWarnings("unused")
	private int itemId;
	@SuppressWarnings("unused")
	private int skillId = 0;
	@SuppressWarnings("unused")
	private int transformId = 0;

    public SM_TRANSFORM(Creature creature, boolean applyEffect) {
        modelId = 0;
        panelId = 0;
        itemId = 0;
        skillId = 0;
        transformId = 0;
        this.creature = creature;
        this.state = creature.getState();
        this.modelId = creature.getTransformModel().getModelId();
        this.applyEffect = applyEffect;
    }

	public SM_TRANSFORM(Creature creature, int panelId, boolean applyEffect) {
		modelId = 0;
		panelId = 0;
		itemId = 0;
		skillId = 0;
		transformId = 0;
		this.creature = creature;
		state = creature.getState();
		modelId = creature.getTransformModel().getModelId();
		this.panelId = panelId;
		this.applyEffect = applyEffect;
	}

	public SM_TRANSFORM(Creature creature, int panelId, boolean applyEffect, int itemId) {
		this.modelId = 0;
		this.panelId = 0;
		this.itemId = 0;
		this.skillId = 0;
		this.transformId = 0;
		this.creature = creature;
		this.state = creature.getState();
		this.modelId = creature.getTransformModel().getModelId();
		this.panelId = panelId;
		this.applyEffect = applyEffect;
		this.itemId = itemId;
	}

	public SM_TRANSFORM(Creature creature, int panelId, boolean applyEffect, int itemId, int skillId) {
		this.modelId = 0;
		this.panelId = 0;
		this.itemId = 0;
		this.skillId = 0;
		this.transformId = 0;
		this.creature = creature;
		this.state = creature.getState();
		this.modelId = creature.getTransformModel().getModelId();
		this.panelId = panelId;
		this.applyEffect = applyEffect;
		this.itemId = itemId;
		this.skillId = skillId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(creature.getObjectId());
		writeD(creature.getTransformModel().getModelId());
		writeH(state);
		writeF(0.25f);
		writeF(2.0f);
		writeC(applyEffect && creature.getTransformModel().getType() == TransformType.NONE ? 1 : 0);
		writeD(creature.getTransformModel().getType().getId());
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(creature.getTransformModel().getPanelId());
		writeD(creature.getTransformModel().getItemId());
		writeC(1); // 1 = normal, 0 = transparent
		writeH(creature.getTransformModel().getSkillId());
		writeD(creature.getTransformModel().getTransformId());
	}
}
