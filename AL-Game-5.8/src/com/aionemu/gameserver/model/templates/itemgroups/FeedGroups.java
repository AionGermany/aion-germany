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
package com.aionemu.gameserver.model.templates.itemgroups;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
public final class FeedGroups {

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedFluidGroup")
	public static class FeedFluidGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedArmorGroup")
	public static class FeedArmorGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedThornGroup")
	public static class FeedThornGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedBalaurGroup")
	public static class FeedBalaurGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedBoneGroup")
	public static class FeedBoneGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedSoulGroup")
	public static class FeedSoulGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "FeedExcludeGroup")
	public static class FeedExcludeGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "StinkingJunkGroup")
	public static class StinkingJunkGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "HealthyFoodAllGroup")
	public static class HealthyFoodAllGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "HealthyFoodSpicyGroup")
	public static class HealthyFoodSpicyGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AetherPowderBiscuitGroup")
	public static class AetherPowderBiscuitGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AetherCrystalBiscuitGroup")
	public static class AetherCrystalBiscuitGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AetherGemBiscuitGroup")
	public static class AetherGemBiscuitGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "PoppySnackGroup")
	public static class PoppySnackGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "PoppySnackTastyGroup")
	public static class PoppySnackTastyGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "PoppySnackNutritiousGroup")
	public static class PoppySnackNutritiousGroup extends FeedItemGroup {
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "ShugoEventCoinGroup")
	public static class ShugoEventCoinGroup extends FeedItemGroup {
	}
}
