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


package com.aionemu.packetsamurai.parser.valuereader;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

public class TimeMillisReader extends TimeReader 
{
    @Override
    public String read(ValuePart part)  
    {
        if (!(part instanceof IntValuePart))
            return "";

        long result = ((IntValuePart) part).getIntValue();
        Duration duration = null;
		try 
		{
			duration = DatatypeFactory.newInstance().newDuration(result);
			SimpleDateFormat formatter =  new SimpleDateFormat("HH:mm:ss");
			Date date = formatter.parse(duration.getHours() + ":" + duration.getMinutes() + ":" + duration.getSeconds());
			return formatter.format(date);
		} 
		catch (Exception e) 
		{
		}
		return "";
    }
}
