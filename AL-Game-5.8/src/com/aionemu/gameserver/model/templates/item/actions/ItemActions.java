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
package com.aionemu.gameserver.model.templates.item.actions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemActions")
public class ItemActions {

	@XmlElements({ 
		@XmlElement(name = "skilllearn", type = SkillLearnAction.class), 
		@XmlElement(name = "extract", type = ExtractAction.class), 
		@XmlElement(name = "skilluse", type = SkillUseAction.class), 
		@XmlElement(name = "enchant", type = EnchantItemAction.class), 
		@XmlElement(name = "queststart", type = QuestStartAction.class), 
		@XmlElement(name = "dye", type = DyeAction.class), 
		@XmlElement(name = "craftlearn", type = CraftLearnAction.class), 
		@XmlElement(name = "toypetspawn", type = ToyPetSpawnAction.class), 
		@XmlElement(name = "decompose", type = DecomposeAction.class), 
		@XmlElement(name = "titleadd", type = TitleAddAction.class), 
		@XmlElement(name = "learnemotion", type = EmotionLearnAction.class), 
		@XmlElement(name = "read", type = ReadAction.class), 
		@XmlElement(name = "fireworkact", type = FireworksUseAction.class), 
		@XmlElement(name = "instancetimeclear", type = InstanceTimeClear.class), 
		@XmlElement(name = "expandinventory", type = ExpandInventoryAction.class), 
		@XmlElement(name = "animation", type = AnimationAddAction.class), 
		@XmlElement(name = "cosmetic", type = CosmeticItemAction.class), 
		@XmlElement(name = "charge", type = ChargeAction.class),
		@XmlElement(name = "ride", type = RideAction.class), 
		@XmlElement(name = "houseobject", type = SummonHouseObjectAction.class), 
		@XmlElement(name = "housedeco", type = DecorateAction.class), 
		@XmlElement(name = "assemble", type = AssemblyItemAction.class), 
		@XmlElement(name = "adoptpet", type = AdoptPetAction.class), 
		@XmlElement(name = "apextract", type = ApExtractAction.class), 
		@XmlElement(name = "remodel", type = RemodelAction.class), 
		@XmlElement(name = "expextract", type = ExpExtractAction.class), 
		@XmlElement(name = "polish", type = PolishAction.class), 
		@XmlElement(name = "composition", type = CompositionAction.class), 
		@XmlElement(name = "tuning", type = TuningAction.class), 
		@XmlElement(name = "pack", type = PackAction.class), 
		@XmlElement(name = "stenchant", type = AuthorizeAction.class), 
		@XmlElement(name = "multi_return_item", type = MultiReturnAction.class), 
		@XmlElement(name = "godsocket", type = GodstoneAction.class), 
		@XmlElement(name = "remove_soul_bind", type = RemoveSoulBindAction.class), 
		@XmlElement(name = "expaction", type = ExpAction.class), 
		@XmlElement(name = "skill_skin", type = SkillAnimationAction.class), 
		@XmlElement(name = "f2p", type = F2pAction.class),
		@XmlElement(name = "reduce_level", type = ItemReduceLevelAction.class), 
		@XmlElement(name = "luna", type = LunaChestAction.class), 
		@XmlElement(name = "skill_enhance", type = SkillEnhanceAction.class),
		@XmlElement(name = "sweep", type = ShugoSweepAction.class),})

	protected List<AbstractItemAction> itemActions;

	/**
	 * Gets the value of the itemActions property. Objects of the following type(s) are allowed in the list {@link SkillLearnAction } {@link SkillUseAction }
	 */
	public List<AbstractItemAction> getItemActions() {
		if (itemActions == null) {
			itemActions = new ArrayList<AbstractItemAction>();
		}
		return this.itemActions;
	}

	public List<ToyPetSpawnAction> getToyPetSpawnActions() {
		List<ToyPetSpawnAction> result = new ArrayList<ToyPetSpawnAction>();
		if (itemActions == null) {
			return result;
		}

		for (AbstractItemAction action : itemActions) {
			if (action instanceof ToyPetSpawnAction) {
				result.add((ToyPetSpawnAction) action);
			}
		}
		return result;
	}

	public EnchantItemAction getEnchantAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof EnchantItemAction) {
				return (EnchantItemAction) action;
			}
		}
		return null;
	}

	public SummonHouseObjectAction getHouseObjectAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof SummonHouseObjectAction) {
				return (SummonHouseObjectAction) action;
			}
		}
		return null;
	}

	public CraftLearnAction getCraftLearnAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof CraftLearnAction) {
				return (CraftLearnAction) action;
			}
		}
		return null;
	}

	public DecorateAction getDecorateAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof DecorateAction) {
				return (DecorateAction) action;
			}
		}
		return null;
	}

	public DyeAction getDyeAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof DyeAction) {
				return (DyeAction) action;
			}
		}
		return null;
	}

	public AdoptPetAction getAdoptPetAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof AdoptPetAction) {
				return (AdoptPetAction) action;
			}
		}
		return null;
	}

	public RemodelAction getRemodelAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof RemodelAction) {
				return (RemodelAction) action;
			}
		}
		return null;
	}

	public PolishAction getPolishAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof PolishAction) {
				return (PolishAction) action;
			}
		}
		return null;
	}

	public TuningAction getTuningAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof TuningAction) {
				return (TuningAction) action;
			}
		}
		return null;
	}

	public AuthorizeAction getAuthorizeAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof AuthorizeAction) {
				return (AuthorizeAction) action;
			}
		}
		return null;
	}

	public RemoveSoulBindAction getRemoveSoulBindAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof RemoveSoulBindAction) {
				return (RemoveSoulBindAction) action;
			}
		}
		return null;
	}

}