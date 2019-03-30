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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.StringValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

public class ClientStringReader implements Reader {
  private static Map<Integer, String> stringsById = new HashMap<Integer, String>();
  
  public static void load()
  {
	//PacketSamurai.getUserInterface().log("Loading Client strings... Please wait.");
    Util.drawTitle("Client Strings");
    File stringsFolder = new File("./data/client_strings");
    if (!stringsFolder.exists()) {
      stringsFolder.mkdir();
    }
    try
    {
      File[] files = stringsFolder.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File sFile = arrayOfFile1[i];
        File xml = new File(sFile.getPath());
        
        String stringFile = FileUtils.readFileToString(xml);
        String[] strings = StringUtils.substringsBetween(stringFile, "<string>", "</string>");
        if (strings != null)
        {
          String[] arrayOfString1;
          int m = (arrayOfString1 = strings).length;
          for (int k = 0; k < m; k++)
          {
            String string = arrayOfString1[k];
            int stringId = Integer.parseInt(StringUtils.substringBetween(string, "<id>", "</id>"));
            String stringBody = StringUtils.substringBetween(string, "<body>", "</body>");
            stringsById.put(Integer.valueOf(stringId), stringBody);
          }
        }
      }
      PacketSamurai.getUserInterface().log("Strings [Client] - Loaded " + stringsById.size() + " strings from "+files.length+" Files");
    }
    catch (IOException e)
    {
      PacketSamurai.getUserInterface().log("ERROR: Failed to load client_strings.xsd: " + e.toString());
      e.printStackTrace();
    }
  }
  
  boolean _real = true;
  
  public boolean loadReaderFromXML(Node n)
  {
    NamedNodeMap enumAttrs = n.getAttributes();
    Node nameAttr = enumAttrs.getNamedItem("real");
    if (nameAttr != null) {
      this._real = Boolean.parseBoolean(nameAttr.getNodeValue());
    }
    return true;
  }
  
  public boolean saveReaderToXML(Element element, Document doc)
  {
    return true;
  }
  
  public JComponent readToComponent(ValuePart part)
  {
    return new JLabel(read(part));
  }
  
  public String read(ValuePart part)
  {
    int id = 0;
    if (!(part instanceof IntValuePart))
    {
      if (!(part instanceof StringValuePart)) {
        return "";
      }
      try
      {
        id = Integer.parseInt(((StringValuePart)part).getStringValue());
      }
      catch (NumberFormatException e)
      {
        return ((StringValuePart)part).getStringValue();
      }
    }
    else
    {
      id = ((IntValuePart)part).getIntValue();
    }
    if ((!this._real) && (id % 2 == 1)) {
      id = (id - 1) / 2;
    }
    if (stringsById == null) {
      return Integer.toString(id);
    }
    return getStringById(id);
  }
  
  public static String getStringById(int nameId)
  {
    return stringsById.get(nameId) != null ? (String)stringsById.get(nameId) : "None";
  }
}
