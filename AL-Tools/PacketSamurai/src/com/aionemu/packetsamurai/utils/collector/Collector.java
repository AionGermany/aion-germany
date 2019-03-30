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


package com.aionemu.packetsamurai.utils.collector;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.utils.collector.data.houses.CollectedHouseDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.houses.HouseDecorData;
import com.aionemu.packetsamurai.utils.collector.data.houses.HouseSpawnInfo;
import com.aionemu.packetsamurai.utils.collector.data.npcData.CollectedNpcDataLoader;
import com.aionemu.packetsamurai.utils.collector.data.npcMoves.NpcMoveDataSaver;
import com.aionemu.packetsamurai.utils.collector.data.npcTemplates.NpcsTool;
import com.aionemu.packetsamurai.utils.collector.data.npcskills.NpcSkillTemplate;
import com.aionemu.packetsamurai.utils.collector.data.npcskills.NpcSkillsTool;
import com.aionemu.packetsamurai.utils.collector.data.towns.TownSpawnsTool;
import com.aionemu.packetsamurai.utils.collector.data.windstreams.WindstreamsTool;
import com.aionemu.packetsamurai.utils.collector.objects.GatherSpawn;
import com.aionemu.packetsamurai.utils.collector.objects.Npc;
import com.aionemu.packetsamurai.utils.collector.objects.NpcInfo;
import com.aionemu.packetsamurai.utils.collector.objects.VisibleObject;

public class Collector {

	private static boolean enabled = true;

	private KnowList knowList = new KnowList();

	private int worldId;
	@SuppressWarnings("unused")
	private int ownerId;
	private Map<Integer, NpcInfo> npcInfoList = new FastMap<Integer, NpcInfo>();
	private Map<Integer, GatherSpawn> gatherInfoList = new FastMap<Integer, GatherSpawn>();

	/**
	 * @return the enabled
	 */
	public static boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *          the enabled to set
	 */
	public static void setEnabled(boolean enabled) {
		Collector.enabled = enabled;
	}

	public void parse(DataPacket dp, packetDirection direction) {
		String packetName = dp.getName();
		if (packetName == null)
			return;
		switch (direction) {
			case serverPacket:
				if (packetName.equals("SM_DELETE")) {
					int objectId = Integer.parseInt(dp.getValuePartList().get(0).readValue());
					knowList.remove(objectId);
				}
				else if ("SM_PLAYER_SPAWN".equals(packetName)) {
					worldId = Integer.parseInt(dp.getValuePartList().get(1).readValue());
				}
				else if ("SM_MOVE".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					float x = 0, y = 0, z = 0;
					byte moveType = 0;
					int objectId = 0;
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName().trim();
						if ("objId".equals(partName)) {
							objectId = Integer.parseInt(valuePart.readValue());
						}
						else if ("x".equals(partName)) {
							x = Float.parseFloat(valuePart.readValue());
						}
						else if ("y".equals(partName)) {
							y = Float.parseFloat(valuePart.readValue());
						}
						else if ("z".equals(partName)) {
							z = Float.parseFloat(valuePart.readValue());
						}
						else if ("movementTypeId".equals(partName)) {
							moveType = Byte.parseByte(valuePart.readValue());
						}
					}
					if (npcInfoList.containsKey(objectId)) {
						NpcInfo npc = npcInfoList.get(objectId);
						NpcMoveDataSaver.add(npc.npcId, npc.name, x, y, z, moveType, dp.getTimeStamp());
					}
				}
				else if ("SM_NPC_INFO".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					NpcInfo npc = new NpcInfo();
					int objectId = 0;
					int creatorId = 0;
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName().trim();
						if ("objectId".equals(partName)) {
							objectId = Integer.parseInt(valuePart.readValue());
						}
						else if ("npcId".equals(partName)) {
							npc.npcId = Integer.parseInt(valuePart.readValue());
						}
						else if ("static_id".equals(partName)) {
							npc.static_id = Integer.parseInt(valuePart.readValue());
						}
						else if ("npcTemplateNameId".equals(partName)) {
							try {
								npc.npcTemplateNameId = Integer.parseInt(valuePart.getValueAsString());
								npc.name = valuePart.getModelPart().getReader().read(valuePart);
							}
							catch (Exception e) {
								PacketSamurai.getUserInterface().log("Unknown npc nameId: " + valuePart.getValueAsString());
							}
						}
						else if ("npcTemplateTitleId".equalsIgnoreCase(partName)) {
							try {
								npc.npcTemplateTitleId = Integer.parseInt(valuePart.getValueAsString());
							}
							catch (Exception e) {
								PacketSamurai.getUserInterface().log("Unknown npc nameId: " + valuePart.getValueAsString());
							}
						}
						else if ("npcState".equals(partName)) {
							npc.npcMode = Integer.parseInt(valuePart.readValue());
						}
						else if ("moveType".equals(partName)) {
							npc.moveType = Integer.parseInt(valuePart.readValue());
						}
						else if ("x".equals(partName)) {
							npc.x = Float.parseFloat(valuePart.readValue());
						}
						else if ("y".equals(partName)) {
							npc.y = Float.parseFloat(valuePart.readValue());
						}
						else if ("z".equals(partName)) {
							npc.z = Float.parseFloat(valuePart.readValue());
						}
						else if ("npcHeading".equals(partName)) {
							npc.npcHeading = Integer.parseInt(valuePart.readValue());
						}
						else if ("maxHp".equalsIgnoreCase(partName)) {
							npc.hp = Integer.parseInt(valuePart.readValue());
						}
						else if ("level".equalsIgnoreCase(partName)) {
							npc.level = Byte.parseByte(valuePart.readValue());
						}
						else if ("creatorId".equalsIgnoreCase(partName)) {
							creatorId = Integer.parseInt(valuePart.readValue());
						}
						else if ("npcType".equals(partName)) {
							npc.npcType = Integer.parseInt(valuePart.readValue());
						}
						else if ("townId".equals(partName)) {
							npc.townId = Integer.parseInt(valuePart.readValue());
						}
					}
					CollectedNpcDataLoader.add(npc.npcId, npc.level, npc.hp, npc.npcTemplateTitleId, npc.npcType);
            		NpcsTool.update(npc.npcId, npc.level, npc.hp, npc.npcTemplateTitleId, npc.npcType, npc.npcTemplateNameId);
					npc.worldId = worldId;
					Npc n = new Npc(objectId, npc.npcId, npc.static_id);
					knowList.add(n);
					npcInfoList.put(objectId, npc);
					if (creatorId > 0 && CollectedHouseDataLoader.isHouseAddress(creatorId)) {
						HouseSpawnInfo spawnInfo = new HouseSpawnInfo(creatorId, npc.npcTemplateNameId);
						spawnInfo.setCoords(npc.x, npc.y, npc.z, npc.npcHeading);
						CollectedHouseDataLoader.add(spawnInfo);
					}
					if (npc.npcTemplateTitleId == 462877 || npc.npcTemplateTitleId == 462877) {
						TownSpawnsTool.add(npc.townId, npc.worldId, npc.npcId, npc.x, npc.y, npc.z, npc.npcHeading);
					}
				}
				else if ("SM_GATHERABLE_INFO".equals(packetName)) {
					GatherSpawn spawn = new GatherSpawn();
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(dp.getValuePartList());
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("npcId".equals(partName))
							spawn.npcId = Integer.parseInt(valuePart.readValue());
						else if ("StaticId".equals(partName))
							spawn.staticid = Integer.parseInt(valuePart.readValue());
						else if ("ObjectId".equals(partName))
							spawn.objectId = Integer.parseInt(valuePart.readValue());
						else if ("heading".equals(partName))
							spawn.heading = Byte.parseByte(valuePart.readValue());
						spawn.worldId = worldId;
					}
					gatherInfoList.put(spawn.objectId, spawn);
				}
				else if ("SM_ATTACK".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					int attackerId = 0;
					int attackCount = 0;
					short hpAtUse = 0;
					VisibleObject object = null;
					NpcSkillTemplate template = null;

					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("attacker".equals(partName)) {
							attackerId = Integer.parseInt(valuePart.readValue());
							object = knowList.get(attackerId);
							if (object != null && object instanceof Npc) {
								template = new NpcSkillTemplate();
							}
						}
						else if ("attackNo".equals(partName)) {
							attackCount = Integer.parseInt(valuePart.readValue());
						}
						else if ("attacker %hp".equals(partName)) {
							hpAtUse = Short.parseShort(valuePart.readValue());
						}
						if (attackerId != 0 && hpAtUse != 0)
							break;
					}
					if (template != null && object != null) {
						template.setSkillid(0);
						template.getStats().maxHp = hpAtUse;
						template.getStats().minHp = hpAtUse;
						if (attackCount < 0)
							template.getStats().useCount = attackCount & 0xFF;
						else
							template.getStats().useCount = attackCount;
						NpcSkillsTool.addSkill(object.getTemplateId(), template);
					}
				}
				else if ("SM_CASTSPELL_END".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					int effectorId = 0;
					int skillId = 0;
					int level = 0;
					short hpAtUse = 0;
					NpcSkillTemplate template = null;
					VisibleObject object = null;

					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("effectorId".equals(partName)) {
							effectorId = Integer.parseInt(valuePart.readValue());
							object = knowList.get(effectorId);
							if (object != null && object instanceof Npc) {
								template = new NpcSkillTemplate();
							}
						}
						else if ("spellId".equals(partName)) {
							skillId = Integer.parseInt(valuePart.getValueAsString());
						}
						else if ("level".equals(partName)) {
							level = Short.parseShort(valuePart.readValue());
						}
						else if ("attackerHp".equals(partName)) {
							hpAtUse = Short.parseShort(valuePart.readValue());
						}
						if (effectorId != 0 && skillId != 0 && level != 0 && hpAtUse != 0)
							break;
					}
					if (template != null && object != null) {
						template.setSkillid(skillId);
						template.setSkilllevel(level);
						template.getStats().maxHp = hpAtUse;
						template.getStats().minHp = hpAtUse;
						PacketSamurai.getUserInterface().log("Add new npc skill: " + template.toString());
						if (NpcSkillsTool.addSkill(object.getTemplateId(), template)) {
							PacketSamurai.getUserInterface().log("Add new npc skill: " + template.toString());
						}
					}
				}
				else if ("SM_HOUSE_RENDER".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					HouseDecorData data = new HouseDecorData();
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("playerId".equals(partName)) {
							int playerId = Integer.parseInt(valuePart.readValue());
							if (playerId != 0)
								break;
						}
						else if ("address".equals(partName)) {
							data.address = Integer.parseInt(valuePart.readValue());
						}
						else if ("buildingId".equals(partName)) {
							data.building = Integer.parseInt(valuePart.readValue());
						}
						else if ("roof".equals(partName)) {
							data.roof = Integer.parseInt(valuePart.readValue());
						}
						else if ("outwall".equals(partName)) {
							data.outwall = Integer.parseInt(valuePart.readValue());
						}
						else if ("frame".equals(partName)) {
							data.frame = Integer.parseInt(valuePart.readValue());
						}
						else if ("door".equals(partName)) {
							data.door = Integer.parseInt(valuePart.readValue());
						}
						else if ("garden".equals(partName)) {
							data.garden = Integer.parseInt(valuePart.readValue());
						}
						else if ("fence".equals(partName)) {
							data.fence = Integer.parseInt(valuePart.readValue());
						}
						else if ("inwall".equals(partName) && data.inwall == 0) {
							data.inwall = Integer.parseInt(valuePart.readValue());
						}
						else if ("infloor".equals(partName) && data.infloor == 0) {
							data.infloor = Integer.parseInt(valuePart.readValue());
						}
						else if ("addon".equals(partName) && data.addon == 0) {
							data.addon = Integer.parseInt(valuePart.readValue());
						}
					}
					CollectedHouseDataLoader.add(data);
				}
				else if ("SM_WINDSTREAM_ANNOUNCE".equals(packetName)) {
					List<ValuePart> valuePartList = dp.getValuePartList();
					int mapId = 0;
					int streamId = 0;
					int bidirectional = 0 ;
					int boost = 0;
					//FastList<ValuePart> valuePartList = new FastList<ValuePart>(dp.getValuePartList());
					for (ValuePart valuePart : valuePartList) {
						String partName = valuePart.getModelPart().getName();
						if ("mapId".equalsIgnoreCase(partName))
							mapId = Integer.parseInt(valuePart.readValue());
						else if ("bidirectional".equalsIgnoreCase(partName))
							bidirectional = Integer.parseInt(valuePart.readValue());
						else if ("streamId".equalsIgnoreCase(partName))
							streamId = Integer.parseInt(valuePart.readValue());
						else if ("boost".equalsIgnoreCase(partName))
							boost = Integer.parseInt(valuePart.readValue());
					}
					WindstreamsTool.Update(mapId, streamId, boost, bidirectional);
				}

				break;
			case clientPacket:
				if ("CM_MOVE".equals(packetName)) {

				}
				break;
		}
	}
}
