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


/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.packetsamurai.gui.images;

import java.util.HashMap;

import javax.swing.ImageIcon;

import com.aionemu.packetsamurai.parser.PartType;


/**
 * Images singletons
 * 
 * @author Ulysses R. Ribeiro
 *
 */
public class IconsTable
{
    public static final ImageIcon ICON_MINUS = new ImageIcon(IconsTable.class.getResource("-.png"));
    public static final ImageIcon ICON_PLUS = new ImageIcon(IconsTable.class.getResource("+.png"));
    
    public static final ImageIcon ICON_PACKET = new ImageIcon(IconsTable.class.getResource("packet.png"));
    public static final ImageIcon ICON_PACKETGRP = new ImageIcon(IconsTable.class.getResource("packetgrp.png"));
    
    public static final ImageIcon ICON_SWITCH = new ImageIcon(IconsTable.class.getResource("switch.png"));
    public static final ImageIcon ICON_CASE = new ImageIcon(IconsTable.class.getResource("case.png"));
    public static final ImageIcon ICON_FOR = new ImageIcon(IconsTable.class.getResource("for.png"));
    
    public static final ImageIcon ICON_C = new ImageIcon(IconsTable.class.getResource("c.png"));
    public static final ImageIcon ICON_H = new ImageIcon(IconsTable.class.getResource("h.png"));
    public static final ImageIcon ICON_D = new ImageIcon(IconsTable.class.getResource("d.png"));
    public static final ImageIcon ICON_F = new ImageIcon(IconsTable.class.getResource("f.png"));
    public static final ImageIcon ICON_DF = new ImageIcon(IconsTable.class.getResource("df.png"));
    public static final ImageIcon ICON_Q = new ImageIcon(IconsTable.class.getResource("Q.png"));
    public static final ImageIcon ICON_S = new ImageIcon(IconsTable.class.getResource("S.png"));
    public static final ImageIcon ICON_S_SMALL = new ImageIcon(IconsTable.class.getResource("smallS.png"));
    public static final ImageIcon ICON_B = new ImageIcon(IconsTable.class.getResource("b.png"));
    public static final ImageIcon ICON_X = new ImageIcon(IconsTable.class.getResource("x.png"));
    
    public static final ImageIcon ICON_FROM_SERVER = new ImageIcon(IconsTable.class.getResource("fromServer.png"));
    public static final ImageIcon ICON_FROM_CLIENT = new ImageIcon(IconsTable.class.getResource("fromClient.png"));
    public static final ImageIcon ICON_FROM_SERVER_ERROR = new ImageIcon(IconsTable.class.getResource("fromServerErr.png"));
    public static final ImageIcon ICON_FROM_CLIENT_ERROR = new ImageIcon(IconsTable.class.getResource("fromClientErr.png"));
    public static final ImageIcon ICON_FROM_SERVER_WARNING = new ImageIcon(IconsTable.class.getResource("fromServerWar.png"));
    public static final ImageIcon ICON_FROM_CLIENT_WARNING = new ImageIcon(IconsTable.class.getResource("fromClientWar.png"));
    
    public static final ImageIcon ICON_FILE = new ImageIcon(IconsTable.class.getResource("file.png"));
    
    public static final ImageIcon ICON_REFRESH = new ImageIcon(IconsTable.class.getResource("refresh.png"));
    public static final ImageIcon ICON_REFRESH_GREEN = new ImageIcon(IconsTable.class.getResource("refreshgreen.png"));
    
    private HashMap<PartType, ImageIcon> _iconMap;
    
    private static class SingletonHolder
	{
		private final static IconsTable singleton = new IconsTable();
	}
	
	public static IconsTable getInstance()
	{
		return SingletonHolder.singleton;
	}
    
    private IconsTable()
    {
        _iconMap = new HashMap<PartType, ImageIcon>();
    }
    
    public void registerIcon(PartType type, ImageIcon icon)
    {
        _iconMap.put(type, icon);
    }
    
    public ImageIcon getIconForPartType(PartType type)
    {
        return _iconMap.get(type);
    }
}
