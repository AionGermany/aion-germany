package com.aionemu.packetsamurai.utils;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.objects.player.Player;
import com.aionemu.packetsamurai.utils.collector.objects.player.PlayerAppearance;
import com.aionemu.packetsamurai.utils.collector.objects.player.PlayerItem;
import com.aionemu.packetsamurai.utils.collector.objects.player.PlayerTool;
import com.aionemu.packetsamurai.utils.collector.objects.player.Players;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
public class PlayerAppearanceExporter 
{
	private List<DataPacket> packets;
	public static FastMap<String, Player> playerByName = PlayerTool.playerByName; //new FastMap<String, Player>();
	public static Players players = PlayerTool.players;
	static String fileName = "data/appearances/player_appearances.xml";
	static int newAdded = 0;
	
	public PlayerAppearanceExporter(List<DataPacket> packets, String sessionName) 
	{
		this.packets = packets;
	}

	public void parse()
	{
		//Fill playerByNameMap with entries from Template
/*		for (Player player : players.players)
		{
			if (!playerByName.containsKey(player.name.toLowerCase()))
			{
				playerByName.put(player.name.toLowerCase(), player);
				
				PacketSamurai.getUserInterface().log("Added from Template Player : "+player.name);

			}
		}
*/		
		try
		{			
			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				
				if("SM_CHARACTER_LIST".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					Player player = new Player();
					PlayerAppearance appearance = new PlayerAppearance();
					
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("characterName".equals(partName)) 
							player.name = valuePart.readValue();
						else if("raceId".equals(partName)) player.race = valuePart.readValue();
						else if("genderId".equals(partName)) player.gender = valuePart.readValue();
						else if("classId".equals(partName)) player.playerClass = valuePart.readValue();
						else if("level".equals(partName)) player.level = Integer.parseInt(valuePart.readValue());
						else if("skinRGB".equals(partName)) appearance.skinRGB = ((IntValuePart)valuePart).getIntValue();
						else if("hairRGB".equals(partName)) appearance.hairRGB = ((IntValuePart)valuePart).getIntValue();
						else if("lipRGB".equals(partName)) appearance.lipRGB = ((IntValuePart)valuePart).getIntValue();
						else if("eyeRGB".equals(partName)) appearance.eyeRGB = ((IntValuePart)valuePart).getIntValue();
						else if("face".equals(partName)) appearance.face = Integer.parseInt(valuePart.readValue());
						else if("hairShape".equals(partName)) appearance.hairShape = Integer.parseInt(valuePart.readValue());
						else if("deco".equals(partName)) appearance.deco = Integer.parseInt(valuePart.readValue());
						else if("tattoo".equals(partName)) appearance.tattoo = Integer.parseInt(valuePart.readValue());
						else if("faceContour".equals(partName)) appearance.faceContour = Integer.parseInt(valuePart.readValue());
						else if("expresion".equals(partName)) appearance.expresion = Integer.parseInt(valuePart.readValue());
						else if("unk_0x06".equals(partName)) appearance.unk1 = Integer.parseInt(valuePart.readValue()); //0x06
						else if("jawLine".equals(partName)) appearance.jawLine = Integer.parseInt(valuePart.readValue());
						else if("foreHead".equals(partName)) appearance.foreHead = Integer.parseInt(valuePart.readValue());
						else if("eyeHeight".equals(partName)) appearance.eyeHeight = Integer.parseInt(valuePart.readValue());
						else if("eyeSpace".equals(partName)) appearance.eyeSpace = Integer.parseInt(valuePart.readValue());
						else if("eyeWidth".equals(partName)) appearance.eyeWidth = Integer.parseInt(valuePart.readValue());
						else if("eyeSize".equals(partName)) appearance.eyeSize = Integer.parseInt(valuePart.readValue());
						else if("eyeShape".equals(partName)) appearance.eyeShape = Integer.parseInt(valuePart.readValue());
						else if("eyeAngle".equals(partName)) appearance.eyeAngle = Integer.parseInt(valuePart.readValue());
						else if("browHeight".equals(partName)) appearance.browHeight = Integer.parseInt(valuePart.readValue());
						else if("browAngle".equals(partName)) appearance.browAngle = Integer.parseInt(valuePart.readValue());
						else if("browShape".equals(partName)) appearance.browShape = Integer.parseInt(valuePart.readValue());
						else if("nose".equals(partName)) appearance.nose = Integer.parseInt(valuePart.readValue());
						else if("noseBridge".equals(partName)) appearance.noseBridge = Integer.parseInt(valuePart.readValue());
						else if("noseWidth".equals(partName)) appearance.noseWidth = Integer.parseInt(valuePart.readValue());
						else if("noseTip".equals(partName)) appearance.noseTip = Integer.parseInt(valuePart.readValue());
						else if("cheek".equals(partName)) appearance.cheek = Integer.parseInt(valuePart.readValue());
						else if("lipHeight".equals(partName)) appearance.lipHeight = Integer.parseInt(valuePart.readValue());
						else if("mouthSize".equals(partName)) appearance.mouthSize = Integer.parseInt(valuePart.readValue());
						else if("lipSize".equals(partName)) appearance.lipSize = Integer.parseInt(valuePart.readValue());
						else if("smile".equals(partName)) appearance.smile = Integer.parseInt(valuePart.readValue());
						else if("lipShape".equals(partName)) appearance.lipShape = Integer.parseInt(valuePart.readValue());
						else if("ChinHeight".equals(partName)) appearance.ChinHeight = Integer.parseInt(valuePart.readValue());
						else if("CheckBones".equals(partName)) appearance.CheckBones = Integer.parseInt(valuePart.readValue());
						else if("earShape".equals(partName)) appearance.earShape = Integer.parseInt(valuePart.readValue());
						else if("headSize".equals(partName)) appearance.headSize = Integer.parseInt(valuePart.readValue());
						else if("neck".equals(partName)) appearance.neck = Integer.parseInt(valuePart.readValue());
						else if("neckLength".equals(partName)) appearance.neckLength = Integer.parseInt(valuePart.readValue());
						else if("shoulderSize".equals(partName)) appearance.shoulderSize = Integer.parseInt(valuePart.readValue());
						else if("torso".equals(partName)) appearance.torso = Integer.parseInt(valuePart.readValue());
						else if("chest".equals(partName)) appearance.chest = Integer.parseInt(valuePart.readValue());
						else if("waist".equals(partName)) appearance.waist = Integer.parseInt(valuePart.readValue());
						else if("hips".equals(partName)) appearance.hips = Integer.parseInt(valuePart.readValue());
						else if("armThickness".equals(partName)) appearance.armThickness = Integer.parseInt(valuePart.readValue());
						else if("handSize".equals(partName)) appearance.handSize = Integer.parseInt(valuePart.readValue());
						else if("legThickness".equals(partName)) appearance.legThickness = Integer.parseInt(valuePart.readValue());
						else if("footSize".equals(partName)) appearance.footSize = Integer.parseInt(valuePart.readValue());
						else if("facialRatio".equals(partName)) appearance.facialRatio = Integer.parseInt(valuePart.readValue());
						else if("unk_0x00".equals(partName)) appearance.unk2 = Integer.parseInt(valuePart.readValue());
						else if("armLength".equals(partName)) appearance.armLength = Integer.parseInt(valuePart.readValue());
						else if("legLength".equals(partName)) appearance.legLength = Integer.parseInt(valuePart.readValue());
						else if("shoulders".equals(partName)) appearance.shoulders = Integer.parseInt(valuePart.readValue());
						else if("faceshape".equals(partName)) appearance.faceShape = Integer.parseInt(valuePart.readValue());
						else if("height".equals(partName)) 
						{
							if (!playerByName.containsKey(player.name.toLowerCase()))
							{
								appearance.height = Float.parseFloat(valuePart.readValue());
								player.appearance = appearance;
								players.players.add(player);
								playerByName.put(player.name.toLowerCase(), player);
								player = new Player();
								appearance = new PlayerAppearance();
								newAdded++;
							}							
						}
					}
				}				
				
				if("SM_PLAYER_INFO".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					Player player = new Player();
					PlayerAppearance appearance = new PlayerAppearance();
					FastList<PlayerItem> items = new FastList<PlayerItem>();
					FastList<Integer> itemIds = new FastList<Integer>();
					FastList<Integer> itemColors = new FastList<Integer>();
					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("name".equals(partName))player.name = valuePart.readValue();
						else if("raceId".equals(partName)) player.race = valuePart.readValue();
						else if("genderId".equals(partName)) player.gender = valuePart.readValue();
						else if("classId".equals(partName)) player.playerClass = valuePart.readValue();
						else if("level".equals(partName)) player.level = Integer.parseInt(valuePart.readValue());
						else if("skinRGB".equals(partName)) appearance.skinRGB = ((IntValuePart)valuePart).getIntValue();
						else if("hairRGB".equals(partName)) appearance.hairRGB = ((IntValuePart)valuePart).getIntValue();
						else if("lipRGB".equals(partName)) appearance.lipRGB = ((IntValuePart)valuePart).getIntValue();
						else if("eyeRGB".equals(partName)) appearance.eyeRGB = ((IntValuePart)valuePart).getIntValue();
						else if("face".equals(partName)) appearance.face = Integer.parseInt(valuePart.readValue());
						else if("hairShape".equals(partName)) appearance.hairShape = Integer.parseInt(valuePart.readValue());
						else if("deco".equals(partName)) appearance.deco = Integer.parseInt(valuePart.readValue());
						else if("tattoo".equals(partName)) appearance.tattoo = Integer.parseInt(valuePart.readValue());
						else if("faceContour".equals(partName)) appearance.faceContour = Integer.parseInt(valuePart.readValue());
						else if("expresion".equals(partName)) appearance.expresion = Integer.parseInt(valuePart.readValue());
						else if("unk_0x06".equals(partName)) appearance.unk1 = Integer.parseInt(valuePart.readValue()); //0x06
						else if("jawLine".equals(partName)) appearance.jawLine = Integer.parseInt(valuePart.readValue());
						else if("foreHead".equals(partName)) appearance.foreHead = Integer.parseInt(valuePart.readValue());
						else if("eyeHeight".equals(partName)) appearance.eyeHeight = Integer.parseInt(valuePart.readValue());
						else if("eyeSpace".equals(partName)) appearance.eyeSpace = Integer.parseInt(valuePart.readValue());
						else if("eyeWidth".equals(partName)) appearance.eyeWidth = Integer.parseInt(valuePart.readValue());
						else if("eyeSize".equals(partName)) appearance.eyeSize = Integer.parseInt(valuePart.readValue());
						else if("eyeShape".equals(partName)) appearance.eyeShape = Integer.parseInt(valuePart.readValue());
						else if("eyeAngle".equals(partName)) appearance.eyeAngle = Integer.parseInt(valuePart.readValue());
						else if("browHeight".equals(partName)) appearance.browHeight = Integer.parseInt(valuePart.readValue());
						else if("browAngle".equals(partName)) appearance.browAngle = Integer.parseInt(valuePart.readValue());
						else if("browShape".equals(partName)) appearance.browShape = Integer.parseInt(valuePart.readValue());
						else if("nose".equals(partName)) appearance.nose = Integer.parseInt(valuePart.readValue());
						else if("noseBridge".equals(partName)) appearance.noseBridge = Integer.parseInt(valuePart.readValue());
						else if("noseWidth".equals(partName)) appearance.noseWidth = Integer.parseInt(valuePart.readValue());
						else if("noseTip".equals(partName)) appearance.noseTip = Integer.parseInt(valuePart.readValue());
						else if("cheek".equals(partName)) appearance.cheek = Integer.parseInt(valuePart.readValue());
						else if("lipHeight".equals(partName)) appearance.lipHeight = Integer.parseInt(valuePart.readValue());
						else if("mouthSize".equals(partName)) appearance.mouthSize = Integer.parseInt(valuePart.readValue());
						else if("lipSize".equals(partName)) appearance.lipSize = Integer.parseInt(valuePart.readValue());
						else if("smile".equals(partName)) appearance.smile = Integer.parseInt(valuePart.readValue());
						else if("lipShape".equals(partName)) appearance.lipShape = Integer.parseInt(valuePart.readValue());
						else if("ChinHeight".equals(partName)) appearance.ChinHeight = Integer.parseInt(valuePart.readValue());
						else if("CheckBones".equals(partName)) appearance.CheckBones = Integer.parseInt(valuePart.readValue());
						else if("earShape".equals(partName)) appearance.earShape = Integer.parseInt(valuePart.readValue());
						else if("headSize".equals(partName)) appearance.headSize = Integer.parseInt(valuePart.readValue());
						else if("neck".equals(partName)) appearance.neck = Integer.parseInt(valuePart.readValue());
						else if("neckLength".equals(partName)) appearance.neckLength = Integer.parseInt(valuePart.readValue());
						else if("shoulderSize".equals(partName)) appearance.shoulderSize = Integer.parseInt(valuePart.readValue());
						else if("torso".equals(partName)) appearance.torso = Integer.parseInt(valuePart.readValue());
						else if("chest".equals(partName)) appearance.chest = Integer.parseInt(valuePart.readValue());
						else if("waist".equals(partName)) appearance.waist = Integer.parseInt(valuePart.readValue());
						else if("hips".equals(partName)) appearance.hips = Integer.parseInt(valuePart.readValue());
						else if("armThickness".equals(partName)) appearance.armThickness = Integer.parseInt(valuePart.readValue());
						else if("handSize".equals(partName)) appearance.handSize = Integer.parseInt(valuePart.readValue());
						else if("legThickness".equals(partName)) appearance.legThickness = Integer.parseInt(valuePart.readValue());
						else if("footSize".equals(partName)) appearance.footSize = Integer.parseInt(valuePart.readValue());
						else if("facialRatio".equals(partName)) appearance.facialRatio = Integer.parseInt(valuePart.readValue());
						else if("unk_0x00".equals(partName)) appearance.unk2 = Integer.parseInt(valuePart.readValue());
						else if("armLength".equals(partName)) appearance.armLength = Integer.parseInt(valuePart.readValue());
						else if("legLength".equals(partName)) appearance.legLength = Integer.parseInt(valuePart.readValue());
						else if("shoulders".equals(partName)) appearance.shoulders = Integer.parseInt(valuePart.readValue());
						else if("faceShape".equals(partName)) appearance.faceShape = Integer.parseInt(valuePart.readValue());					
						else if("height".equals(partName)) appearance.height = Float.parseFloat(valuePart.readValue());					
						else if("itemTemplateId".equals(partName)) 
						{	
							int i = Integer.parseInt(valuePart.readValue());
							itemIds.add(i);
						}
						else if("itemColor".equals(partName)) 
						{	
							int i = Integer.parseInt(valuePart.readValue());
							itemColors.add(i);
						}
					}
					if (!playerByName.containsKey(player.name.toLowerCase()))
					{
						int in = 0;
						for (Integer i : itemIds)
						{
							PlayerItem pi = new PlayerItem();
							pi.itemTemplateId = i;
							pi.itemColor = itemColors.get(in);
									
							items.add(pi);
							in++;
						}
						player.id = playerByName.size()+1;
						player.appearance = appearance;
						// Not active now but works.. maybe for later functions :) 
						player.items = items;

						playerByName.put(player.name.toLowerCase(), player);
						players.players.add(player);
						newAdded++;
					}	
				}				
			}
		}
		catch (Exception e)
		{
			PacketSamurai.getUserInterface().log(" Parse Error: "+e.getMessage());
		}
		writeXml();
	}
	
	public static void writeXml()
	{
		if (newAdded > 0)
		{
			try 
			{
		        JAXBContext jaxbContext = JAXBContext.newInstance( Players.class );
		        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		      
		        jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		        new File("./data/appearances/").mkdirs(); 
		        jaxbMarshaller.marshal( players, new File( fileName ) );	        
			} 
			catch (JAXBException e) 
			{
				PacketSamurai.getUserInterface().log("Xml:"+ e.getMessage());
			}
			PacketSamurai.getUserInterface().log("Export [PlayerAppearance] - Written successfully "+newAdded+" new PlayerAppearances - You have now a total of ["+playerByName.size()+"] PlayerAppearances!");
			newAdded = 0;
		}
		else PacketSamurai.getUserInterface().log("Export [PlayerAppearance] - Nothing to Export..");
	}
}
