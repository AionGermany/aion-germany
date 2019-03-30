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


import com.aionemu.packetsamurai.parser.datatree.DataTreeNodeContainer;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.parttypes.BlockPartType;
import com.aionemu.packetsamurai.parser.parttypes.DoublePartType;
import com.aionemu.packetsamurai.parser.parttypes.FloatPartType;
import com.aionemu.packetsamurai.parser.parttypes.IntBSPartType;
import com.aionemu.packetsamurai.parser.parttypes.IntPartType;
import com.aionemu.packetsamurai.parser.parttypes.LongPartType;
import com.aionemu.packetsamurai.parser.parttypes.RawPartType;
import com.aionemu.packetsamurai.parser.parttypes.StringPartType;
import com.aionemu.packetsamurai.parser.parttypes.BlockPartType.blockType;
import com.aionemu.packetsamurai.parser.parttypes.IntPartType.intType;
import com.aionemu.packetsamurai.parser.parttypes.StringPartType.stringType;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public abstract class PartType
{
    private String _name;
    public static final PartType c = new IntPartType("c",intType.c);
    public static final PartType h = new IntPartType("h",intType.h);
    public static final PartType d = new IntPartType("d",intType.d);
    public static final PartType cbs = new IntBSPartType("cbs",intType.c);
    public static final PartType hbs = new IntBSPartType("hbs",intType.h);
    public static final PartType dbs = new IntBSPartType("dbs",intType.d);
    public static final PartType f = new FloatPartType("f");
    public static final PartType df = new DoublePartType("df");
    public static final PartType Q = new LongPartType("Q");
    public static final PartType S = new StringPartType("S",stringType.S);
    public static final PartType s = new StringPartType("s",stringType.s);
    public static final PartType b = new RawPartType("b");
    public static final PartType x = new RawPartType("x");
    public static final PartType Ss = new StringPartType("Ss",stringType.Ss);
    public static final PartType Sn = new StringPartType("Sn",stringType.Sn);
    public static final PartType forBlock = new BlockPartType("forblock", blockType.forblock);
    public static final PartType swicthBlock = new BlockPartType("switchblock", blockType.switchblock);
    public static final PartType block = new BlockPartType("block", blockType.block);

    
    public PartType(String name)
    {
        _name = name;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public abstract ValuePart getValuePart(DataTreeNodeContainer parent, Part part);
    
    public abstract boolean isReadableType();
    
    public abstract boolean isBlockType();
    
    public abstract int getTypeByteNumber();
    
    @Override
    public String toString()
    {
        return _name;
    }
}