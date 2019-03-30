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


package com.aionemu.packetsamurai.parser;

import java.util.HashMap;
import java.util.Map;

import com.aionemu.packetsamurai.gui.images.IconsTable;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public class PartTypeManager
{
    private Map<String, PartType> _map;
    
    private static class SingletonHolder
	{
		private final static PartTypeManager singleton = new PartTypeManager();
	}
	
	public static PartTypeManager getInstance()
	{
		return SingletonHolder.singleton;
	}
    
    private PartTypeManager()
    {
        _map = new HashMap<String, PartType>();
    }
    
    public PartType getType(String name)
    {
    	PartType t = _map.get(name);
    	if(t == null)
    		throw new IllegalArgumentException("There are no PartType: "+name);
        return t;
    }
    
    public void registerPartType(PartType type)
    {
        _map.put(type.getName(), type);
    }
    
    public static void initBaseTypes()
    {
        PartTypeManager.getInstance().registerPartType(PartType.c);
        PartTypeManager.getInstance().registerPartType(PartType.h);
        PartTypeManager.getInstance().registerPartType(PartType.d);
        PartTypeManager.getInstance().registerPartType(PartType.cbs);
        PartTypeManager.getInstance().registerPartType(PartType.hbs);
        PartTypeManager.getInstance().registerPartType(PartType.dbs);
        PartTypeManager.getInstance().registerPartType(PartType.f);
        PartTypeManager.getInstance().registerPartType(PartType.df);
        PartTypeManager.getInstance().registerPartType(PartType.Q);
        PartTypeManager.getInstance().registerPartType(PartType.S);
        PartTypeManager.getInstance().registerPartType(PartType.Ss);
        PartTypeManager.getInstance().registerPartType(PartType.Sn);
        PartTypeManager.getInstance().registerPartType(PartType.s);
        PartTypeManager.getInstance().registerPartType(PartType.b);
        PartTypeManager.getInstance().registerPartType(PartType.x);
        PartTypeManager.getInstance().registerPartType(PartType.forBlock);
        PartTypeManager.getInstance().registerPartType(PartType.swicthBlock);
        PartTypeManager.getInstance().registerPartType(PartType.block);
        
        IconsTable.getInstance().registerIcon(PartType.c, IconsTable.ICON_C);
        IconsTable.getInstance().registerIcon(PartType.h, IconsTable.ICON_H);
        IconsTable.getInstance().registerIcon(PartType.d, IconsTable.ICON_D);
        IconsTable.getInstance().registerIcon(PartType.cbs, IconsTable.ICON_C);
        IconsTable.getInstance().registerIcon(PartType.hbs, IconsTable.ICON_H);
        IconsTable.getInstance().registerIcon(PartType.dbs, IconsTable.ICON_D);
        IconsTable.getInstance().registerIcon(PartType.f, IconsTable.ICON_F);
        IconsTable.getInstance().registerIcon(PartType.df, IconsTable.ICON_DF);
        IconsTable.getInstance().registerIcon(PartType.Q, IconsTable.ICON_Q);
        IconsTable.getInstance().registerIcon(PartType.S, IconsTable.ICON_S);
        IconsTable.getInstance().registerIcon(PartType.Ss, IconsTable.ICON_S);
        IconsTable.getInstance().registerIcon(PartType.Sn, IconsTable.ICON_S);
        IconsTable.getInstance().registerIcon(PartType.s, IconsTable.ICON_S_SMALL);
        IconsTable.getInstance().registerIcon(PartType.b, IconsTable.ICON_B);
        IconsTable.getInstance().registerIcon(PartType.x, IconsTable.ICON_X);
        IconsTable.getInstance().registerIcon(PartType.forBlock, IconsTable.ICON_FOR);
        IconsTable.getInstance().registerIcon(PartType.swicthBlock, IconsTable.ICON_SWITCH);
        IconsTable.getInstance().registerIcon(PartType.block, IconsTable.ICON_PACKETGRP);
    }
}