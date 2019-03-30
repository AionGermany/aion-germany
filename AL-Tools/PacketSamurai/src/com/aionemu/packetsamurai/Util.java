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


package com.aionemu.packetsamurai;

import java.util.Iterator;
import java.util.List;

import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;
import com.aionemu.packetsamurai.utils.collector.data.spawns.SpawnSpot;

import javolution.text.TextBuilder;


/**
 * @author Ulysses R. Ribeiro
 * @edit CoolyT
 */
public class Util 
{
	/** 
	 * Returns the unsigned value of a byte
	 * @param the byte witch u want to convert
	 */
	public static int byteToUInt(byte b)
	{
		return b & 0xFF;
	}

	
	public static String zeropad(String number, int size)
	{
		if (number.length() >= size)
			return number;
		return repeat("0",size - number.length())+number;
	}
	
	/**
	 * Returns the hex dump of the given byte array
	 * @param b the byte array
	 * @return A string with the hex dump
	 */
	public static String rawHexDump(byte[] b)
	{
		if (b == null)
			return "";
		
		int size = b.length;
		if (size == 0)
			return "";
		
		StringBuffer buf = new StringBuffer();
		for (byte aB : b) {
			buf.append(zeropad(Integer.toHexString(byteToUInt(aB)).toUpperCase(), 2));
			buf.append(" ");
		}
		buf.delete(buf.length()-1,buf.length());
		return buf.toString();
	}
	
	/**
	 * Returns the hex dump of the given byte array
	 * as 16 bytes per line
	 * @param b the byte array
	 * @return A string with the hex dump
	 */
	public static String hexDump(byte[] b)
	{
		if (b == null)
			return "";
		StringBuffer buf = new StringBuffer();
		int size = b.length;
		for (int i=0; i < size; i++)
		{
			if ((i+1)%16 == 0)
			{
				buf.append(zeropad(Integer.toHexString(byteToUInt(b[i])).toUpperCase(),2));
				buf.append("\n");
			}
			else
			{
				buf.append(zeropad(Integer.toHexString(byteToUInt(b[i])).toUpperCase(),2));
				buf.append(" ");
			}
		}
		return buf.toString();
	}
	
	public static String repeat(String str, int repeat)
	{
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i < repeat; i++)
			buf.append(str);

		return buf.toString();
	}
	
	public static int parseNumber(byte[] data)
	{
		int size = data.length;
		int output = 0;
		int i = 0;
		while (byteToUInt(data[i]) == 0)
		{
			size--;
		}
		for (; i < size; i++)
		{
			output = output + byteToUInt(data[i])*256^i;
		}
		return output;
	}
	
	public static String printData(byte[] data, int len)
	{ 
        TextBuilder result = new TextBuilder();
		
		int counter = 0;
		
		for (int i=0;i< len;i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i, 4)).append(": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2)).append(" ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i-15;
				for (int a=0; a<16;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0 )
		{
			for (int i=0; i<17-rest;i++ )
			{
				result.append("   ");
			}

			int charpoint = data.length-rest;
			for (int a=0; a<rest;a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char)t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}

		
		return result.toString();
	}
	
	public static String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i=number.length(); i< digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}

	public static String toAnsci(byte[] data, int from, int to)
	{
		StringBuilder result = new StringBuilder();

		for(int i = from; i < to; i++)
		{
			int t1 = data[i];
			if (t1 > 0x1f && t1 < 0x80)
			{
				result.append((char)t1);
			}
			else
			{
				result.append('.');
			}
		}
		return result.toString();
	}
	public static String makeFormatStringParts(List<Part> parts)
	{
		TextBuilder tb = new TextBuilder();
		for (Part part : parts)
		{
			if (part instanceof ForPart)
			{
				tb.append("[");
				tb.append(makeFormatStringParts(((ForPart)part).getModelBlock().getParts()));
				tb.append("]");
			}
			else if (part instanceof AbstractSwitchPart)
			{
				tb.append("[");
				Iterator<SwitchCaseBlock> i = ((AbstractSwitchPart)part).getCases(true).iterator();
				while (i.hasNext())
				{
					SwitchCaseBlock entry = i.next();
					if(entry.isDefault())
						tb.append("default:");
					else
						tb.append(Integer.toString(entry.getSwitchCase())).append(":");
					tb.append(makeFormatStringParts(entry.getParts()));
					if(i.hasNext())
						tb.append("|");
				}
				tb.append("]");
			}
			else
			{
				tb.append(part.getType().getName());
			}
		}
		return tb.toString();
	}


	public static CharSequence makeFormatStringTypes(List<PartType> idParts)
	{
		TextBuilder tb = new TextBuilder();
		for (PartType part : idParts)
		{
			tb.append(part.getName());
		}
		return tb.toString();
	}
    
    public static String getReaderName(Class<?> c)
    {
        String className = c.getName();
        className = className.substring(className.lastIndexOf('.')+1);
        return className.substring(0, className.length() - "Reader".length());
    }


    public static String printData(byte[] blop)
    {
        return printData(blop, blop.length);        
    }
    
	public static void drawTitle(String text)
	{
		StringBuilder sb = new StringBuilder();
		int totalLength = 97;
		int textLength = ("[ "+text+" ]").length();
		int bars = (totalLength - textLength) / 2;
		
		if (text.isEmpty())
		{
			for (int i = 0; i <= totalLength+1;i++)
			{
				sb.append("-");
			}
			PacketSamurai.getUserInterface().log(sb.toString());
			return;
		}
		
		
		for (int i = 0; i <= bars; i++)
		{
			sb.append("-");
		}
		String filler = (bars*2)+textLength == totalLength ? "" : " ";
		sb.append("[ "+text+filler+" ]");
		for (int i = 0; i <= bars; i++)
		{
			sb.append("-");
		}
		PacketSamurai.getUserInterface().log(sb.toString());
	}
	
    public static boolean isInRange(SpawnSpot object1, SpawnSpot object2, float range) 
    {
    	float dx = (object2.x - object1.x);
        float dy = (object2.y - object1.y);
        return dx * dx + dy * dy < range * range;
    }
}
