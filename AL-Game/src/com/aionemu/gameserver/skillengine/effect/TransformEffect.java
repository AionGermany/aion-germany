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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerTransformationDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Sweetkr, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformEffect")
public abstract class TransformEffect extends EffectTemplate {

	@XmlAttribute
	protected int model;

	@XmlAttribute
	protected TransformType type = TransformType.NONE;

	@XmlAttribute
	protected int panelid;

	@XmlAttribute
	protected int itemId = 0;

    @XmlAttribute
    protected int skillId;

	@XmlAttribute
	protected AbnormalState state = AbnormalState.BUFF;
    private int modelId;
	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
		if (state != null) {
			effect.getEffected().getEffectController().setAbnormal(state.getId());
			effect.setAbnormal(state.getId());
		}
	}
    
    public void endEffect(Effect effect, AbnormalState state) {
        final Creature effected = effect.getEffected();
        int newModel = 0;
        int oldPanelid = 0;
        int oldEquipment = 0;
        int oldSkillId = 0;
        TransformType transformType = TransformType.PC;
        if (this.state != null) {
            effected.getEffectController().unsetAbnormal(this.state.getId());
        }
        if (effected instanceof Player) {
            for (final Effect tmp : effected.getEffectController().getAbnormalEffects()) {
                for (final EffectTemplate template : tmp.getEffectTemplates()) {
                    if (template instanceof TransformEffect && ((TransformEffect)template).getTransformId() != this.model) {
                        newModel = ((TransformEffect)template).getTransformId();
                        transformType = ((TransformEffect)template).getTransformType();
                        oldPanelid = ((TransformEffect)template).getPanelId();
                        oldEquipment = ((TransformEffect)template).getItemId();
                        oldSkillId = ((TransformEffect)template).getSkillId();
                        break;
                    }
                }
            }
            effected.getTransformModel().setModelId(newModel);
            effected.getTransformModel().setPanelId(oldPanelid);
            effected.getTransformModel().setItemId(oldEquipment);
            effected.getTransformModel().setTransformType(transformType);
            effected.getTransformModel().setSkillId(oldSkillId);
            effected.getTransformModel().setTransformId(0);
            effected.getEffectController().removeEffect(4769);
            ((Player)effected).setInvisibleTransform(false);
            DAOManager.getDAO(PlayerTransformationDAO.class).deletePlTransfo(effected.getObjectId());
            PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected, oldPanelid, false));
            if (effected instanceof Player && (transformType == TransformType.PC || transformType == TransformType.NONE || transformType == TransformType.FORM1 || transformType == TransformType.FORM2 || transformType == TransformType.FORM3 || transformType == TransformType.FORM4 || transformType == TransformType.FORM5 || transformType == TransformType.AVATAR)) {
                ((Player)effected).setTransformed(false);
            }
        }
        else if (effected instanceof Summon) {
            effected.getTransformModel().setModelId(0);
            PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected, 0, false));
        }
        else if (effected instanceof Npc) {
            effected.getTransformModel().setModelId(effected.getObjectTemplate().getTemplateId());
            PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected, 0, false));
        }
    }
    
    public void startEffect(Effect effect, AbnormalState effectId) {
        Creature effected = effect.getEffected();
        if (isSpecialTransform(effect.getSkillId())) {
            modelId = 0;
            effected.getTransformModel().setSkillId(effect.getSkillId());
        }
        else {
            this.modelId = this.model;
            effected.getTransformModel().setSkillId(skillId);
        }
        effected.getTransformModel().setModelId(modelId);
        effected.getTransformModel().setPanelId(panelid);
        effected.getTransformModel().setItemId(itemId);
        effected.getTransformModel().setSkillId(effect.getSkillId());
        effected.getTransformModel().setTransformType(effect.getTransformType());
        effected.getTransformModel().setTransformId(DataManager.TRANSFORM_BOOK_DATA.getTransformId(skillId));
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected, model, true));
        if (effected instanceof Player) {
            ((Player)effected).setTransformed(true);
        }
        if (effected instanceof Player) {
            ((Player)effected).setTransformed(true);
            ((Player)effected).setTransformedModelId(modelId);
            ((Player)effected).setTransformedPanelId(panelid);
            ((Player)effected).setTransformedItemId(itemId);
            DAOManager.getDAO(PlayerTransformationDAO.class).storePlTransfo(effected.getObjectId(), panelid, itemId);
            ((Player)effected).setInvisibleTransform(false);
        }
        super.startEffect(effect);
    }
    
    public TransformType getTransformType() {
        return this.type;
    }
    
    public int getTransformId() {
        return this.model;
    }
    
    public int getPanelId() {
        return this.panelid;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    private boolean isSpecialTransform(int skillId) {
        switch (skillId) {
            case 3370:
            case 3371:
            case 4875:
            case 4878:
            case 4879:
            case 4881:
            case 5053:
            case 5065:
            case 5066:
            case 5069:
            case 5070:
            case 5071:
            case 5072:
            case 5073:
            case 5074:
            case 5075:
            case 5076:
            case 5077:
            case 5078:
            case 5079:
            case 5080:
            case 5300:
            case 5301:
            case 5302:
            case 5303:
            case 5304:
            case 5305:
            case 5306:
            case 5607:
            case 5608:
            case 5609:
            case 5610:
            case 5611:
            case 5612:
            case 5613:
            case 5614:
            case 5615:
            case 5616:
            case 5617:
            case 5618:
            case 5619:
            case 5620:
            case 5621:
            case 5622:
            case 5623:
            case 5624:
            case 5625:
            case 5626:
            case 5627:
            case 5628:
            case 5629:
            case 5630:
            case 5631:
            case 5632:
            case 5633:
            case 5634:
            case 5635:
            case 5636:
            case 5637:
            case 5638:
            case 5639:
            case 5640:
            case 5641:
            case 5642:
            case 5643:
            case 5644:
            case 5645:
            case 5646:
            case 5647:
            case 5648:
            case 5649:
            case 5650:
            case 5651:
            case 5652:
            case 5653:
            case 5654:
            case 5655:
            case 5656:
            case 5657:
            case 5678:
            case 5687:
            case 5688:
            case 5689:
            case 5690:
            case 5691:
            case 5692:
            case 5693:
            case 5866:
            case 5916:
            case 5917:
            case 5981:
            case 5982:
            case 5983:
            case 5984:
            case 5985:
            case 5986:
            case 5987:
            case 5988: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
