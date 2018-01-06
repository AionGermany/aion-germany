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
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer
 */
public class AdminConfig {

	/**
	 * Admin properties
	 */
	@Property(key = "gameserver.administration.gmlevel", defaultValue = "3")
	public static int GM_LEVEL;
	@Property(key = "gameserver.administration.gmpanel", defaultValue = "3")
	public static int GM_PANEL;
	@Property(key = "gameserver.administration.baseshield", defaultValue = "3")
	public static int COMMAND_BASESHIELD;
	@Property(key = "gameserver.administration.flight.freefly", defaultValue = "3")
	public static int GM_FLIGHT_FREE;
	@Property(key = "gameserver.administration.flight.unlimited", defaultValue = "3")
	public static int GM_FLIGHT_UNLIMITED;
	@Property(key = "gameserver.administration.doors.opening", defaultValue = "3")
	public static int DOORS_OPEN;
	@Property(key = "gameserver.administration.auto.res", defaultValue = "3")
	public static int ADMIN_AUTO_RES;
	@Property(key = "gameserver.administration.instancereq", defaultValue = "3")
	public static int INSTANCE_REQ;
	@Property(key = "gameserver.administration.view.player", defaultValue = "3")
	public static int ADMIN_VIEW_DETAILS;
	@Property(key = "gameserver.administration.instant.enchant", defaultValue = "false")
	public static boolean INSTANT_ENCHANT_SUCCESS;
	/**
	 * Admin options
	 */
	@Property(key = "gameserver.administration.gm.mode.connection", defaultValue = "false")
	public static boolean GM_MODE_CONNECTION;
	@Property(key = "gameserver.administration.invis.gm.connection", defaultValue = "false")
	public static boolean INVISIBLE_GM_CONNECTION;
	@Property(key = "gameserver.administration.enemity.gm.connection", defaultValue = "Normal")
	public static String ENEMITY_MODE_GM_CONNECTION;
	@Property(key = "gameserver.administration.invul.gm.connection", defaultValue = "false")
	public static boolean INVULNERABLE_GM_CONNECTION;
	@Property(key = "gameserver.administration.vision.gm.connection", defaultValue = "false")
	public static boolean VISION_GM_CONNECTION;
	@Property(key = "gameserver.administration.whisper.gm.connection", defaultValue = "false")
	public static boolean WHISPER_GM_CONNECTION;
	@Property(key = "gameserver.administration.quest.dialog.log", defaultValue = "false")
	public static boolean QUEST_DIALOG_LOG;
	@Property(key = "gameserver.administration.trade.item.restriction", defaultValue = "false")
	public static boolean ENABLE_TRADEITEM_RESTRICTION;
	/**
	 * Custom TAG based on access level
	 */
	@Property(key = "gameserver.customtag.enable", defaultValue = "true")
	public static boolean CUSTOMTAG_ENABLE;
	@Property(key = "gameserver.customtag.access1", defaultValue = "\uE06F Supporter \uE06F %s")
	public static String CUSTOMTAG_ACCESS1;
	@Property(key = "gameserver.customtag.access2", defaultValue = "\uE04E Junior-GM \uE04E %s")
	public static String CUSTOMTAG_ACCESS2;
	@Property(key = "gameserver.customtag.access3", defaultValue = "\uE050 Senior-GM \uE050 %s")
	public static String CUSTOMTAG_ACCESS3;
	@Property(key = "gameserver.customtag.access4", defaultValue = "\uE04A Head-GM \uE04A %s")
	public static String CUSTOMTAG_ACCESS4;
	@Property(key = "gameserver.customtag.access5", defaultValue = "\uE0BD Admin \uE0BD %s")
	public static String CUSTOMTAG_ACCESS5;
	@Property(key = "gameserver.customtag.access6", defaultValue = "\uE0BD Developer \uE0BD %s")
	public static String CUSTOMTAG_ACCESS6;
	@Property(key = "gameserver.customtag.access7", defaultValue = "\uE0BD Server-CoAdmin \uE0BD %s")
	public static String CUSTOMTAG_ACCESS7;
	@Property(key = "gameserver.customtag.access8", defaultValue = "\uE0BD Server-Admin \uE0BD %s")
	public static String CUSTOMTAG_ACCESS8;
	@Property(key = "gameserver.customtag.access9", defaultValue = "\uE0BD Server-CoOwner \uE0BD %s")
	public static String CUSTOMTAG_ACCESS9;
	@Property(key = "gameserver.customtag.access10", defaultValue = "\uE0BD Server-Owner \uE0BD %s")
	public static String CUSTOMTAG_ACCESS10;
	@Property(key = "gameserver.admin.announce.levels", defaultValue = "*")
	public static String ANNOUNCE_LEVEL_LIST;
	/**
	 * GM special skill
	 */
	@Property(key = "administration.command.special.skill", defaultValue = "9")
	public static int COMMAND_SPECIAL_SKILL;

	@Property(key = "gameserver.not-open.twoclienwindow", defaultValue = "false")
	public static boolean NO_OPEN_NEW_WINDOW;
}
