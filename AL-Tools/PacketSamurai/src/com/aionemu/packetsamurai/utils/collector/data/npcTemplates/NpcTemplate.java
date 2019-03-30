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
package com.aionemu.packetsamurai.utils.collector.data.npcTemplates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="npcTemplate", propOrder={"stats", "equipment", "kiskStats", "boundRadius", "talkInfo", "massiveLooting"})
public class NpcTemplate
{
  @XmlElement(required=true)
  protected NpcStatsTemplate stats;
  protected NpcEquipmentList equipment;
  @XmlElement(name="kisk_stats")
  protected KiskStats kiskStats;
  @XmlElement(name="bound_radius")
  protected BoundRadius boundRadius;
  @XmlElement(name="talk_info")
  protected TalkInfo talkInfo;
  @XmlElement(name="massive_looting")
  private MassiveLooting massiveLooting;
  @XmlAttribute(name="npc_id", required=true)
  protected int npcId;
  @XmlAttribute(name="level", required=true)
  protected int level;
  @XmlAttribute(name="name")
  protected String name;
  @XmlAttribute(name="name_id", required=true)
  protected int nameId;
  @XmlAttribute(name="name_desc")
  protected String nameDesc;
  @XmlAttribute(name="height")
  protected Float height;
  @XmlAttribute(name="title_id")
  protected Integer titleId;
  @XmlAttribute(name="rank")
  protected NpcRank rank;
  @XmlAttribute(name="rating")
  protected NpcRating rating;
  @XmlAttribute(name="race")
  protected Race race;
  @XmlAttribute(name="tribe")
  protected String tribe;
  @XmlAttribute(name="type")
  protected NpcTemplateType type;
  @XmlAttribute(name="ui_type")
  protected NpcUiType uiType;
  @XmlAttribute(name="abyss_type")
  protected AbyssType abyssType;
  @XmlAttribute(name="ai")
  protected String ai;
  @XmlAttribute(name="srange")
  protected Integer srange;
  @XmlAttribute(name="arange")
  protected Integer arange;
  @XmlAttribute(name="adelay")
  protected Integer adelay;
  @XmlAttribute(name="arate")
  protected Integer arate;
  @XmlAttribute(name="hpgauge")
  protected Integer hpgauge;
  @XmlAttribute(name="state")
  protected Integer state;
  @XmlAttribute(name="on_mist")
  protected Boolean onMist;
  @XmlAttribute(name="floatcorpse")
  protected Boolean floatcorpse;
  @XmlAttribute(name="ammo_speed")
  protected Integer ammoSpeed;
  @XmlAttribute(name="stare_range")
  protected Integer stareRange;
  
  public NpcStatsTemplate getStats()
  {
    return this.stats;
  }
  
  public void setStats(NpcStatsTemplate value)
  {
    this.stats = value;
  }
  
  public NpcEquipmentList getEquipment()
  {
    return this.equipment;
  }
  
  public void setEquipment(NpcEquipmentList value)
  {
    this.equipment = value;
  }
  
  public KiskStats getKiskStats()
  {
    return this.kiskStats;
  }
  
  public void setKiskStats(KiskStats value)
  {
    this.kiskStats = value;
  }
  
  public BoundRadius getBoundRadius()
  {
    return this.boundRadius;
  }
  
  public void setBoundRadius(BoundRadius value)
  {
    this.boundRadius = value;
  }
  
  public TalkInfo getTalkInfo()
  {
    return this.talkInfo;
  }
  
  public void setTalkInfo(TalkInfo value)
  {
    this.talkInfo = value;
  }

  public MassiveLooting getMassiveLooting()
  {
    return this.massiveLooting;
  }
  
  public void setMassiveLooting(MassiveLooting value)
  {
    this.massiveLooting = value;
  }
  
  public int getNpcId()
  {
    return this.npcId;
  }
  
  public void setNpcId(int value)
  {
    this.npcId = value;
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public void setLevel(int value)
  {
    this.level = value;
  }
  
  public String getName()
  {
    if (this.name == null) {
      return "";
    }
    return this.name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public int getNameId()
  {
    return this.nameId;
  }
  
  public void setNameId(int value)
  {
    this.nameId = value;
  }
  
  public String getNameDesc()
  {
    if (this.nameDesc == null) {
      return "";
    }
    return this.nameDesc;
  }
  
  public void setNameDesc(String value)
  {
    this.nameDesc = value;
  }
  
  public float getHeight()
  {
    if (this.height == null) {
      return 0.0F;
    }
    return this.height.floatValue();
  }
  
  public void setHeight(Float value)
  {
    this.height = value;
  }
  
  public int getTitleId()
  {
    if (this.titleId == null) {
      return 0;
    }
    return this.titleId.intValue();
  }
  
  public void setTitleId(Integer value)
  {
    this.titleId = value;
  }
  
  public NpcRank getRank()
  {
    return this.rank;
  }
  
  public void setRank(NpcRank value)
  {
    this.rank = value;
  }
  
  public NpcRating getRating()
  {
    return this.rating;
  }
  
  public void setRating(NpcRating value)
  {
    this.rating = value;
  }
  
  public Race getRace()
  {
    return this.race;
  }
  
  public void setRace(Race value)
  {
    this.race = value;
  }
  
  public String getTribe()
  {
    return this.tribe;
  }
  
  public void setTribe(String value)
  {
    this.tribe = value;
  }
  
  public NpcTemplateType getType()
  {
    return this.type;
  }
  
  public void setType(NpcTemplateType value)
  {
    this.type = value;
  }
  
  public AbyssType getAbyssType()
  {
    return this.abyssType;
  }
  
  public void setAbyssType(AbyssType value)
  {
    this.abyssType = value;
  }
  
  public String getAi()
  {
    if (this.ai == null) {
      return "dummy";
    }
    return this.ai;
  }
  
  public void setAi(String value)
  {
    this.ai = value;
  }
  
  public int getSrange()
  {
    if (this.srange == null) {
      return 0;
    }
    return this.srange.intValue();
  }
  
  public void setSrange(Integer value)
  {
    this.srange = value;
  }
  
  public Integer getArange()
  {
    return this.arange;
  }
  
  public void setArange(Integer value)
  {
    this.arange = value;
  }
  
  public int getAdelay()
  {
    if (this.adelay == null) {
      return 2000;
    }
    return this.adelay.intValue();
  }
  
  public void setAdelay(Integer value)
  {
    this.adelay = value;
  }
  
  public Integer getArate()
  {
    return this.arate;
  }
  
  public void setArate(Integer value)
  {
    this.arate = value;
  }
  
  public Integer getHpgauge()
  {
    return this.hpgauge;
  }
  
  public void setHpgauge(Integer value)
  {
    this.hpgauge = value;
  }
  
  public Integer getState()
  {
    return this.state;
  }
  
  public void setState(Integer value)
  {
    this.state = value;
  }
  
  public Boolean isOnMist()
  {
    return this.onMist;
  }
  
  public void setOnMist(Boolean value)
  {
    this.onMist = value;
  }
  
  public boolean isFloatcorpse()
  {
    if (this.floatcorpse == null) {
      return false;
    }
    return this.floatcorpse.booleanValue();
  }
  
  public void setFloatcorpse(Boolean value)
  {
    this.floatcorpse = value;
  }
  
  public Integer getAmmoSpeed()
  {
    return this.ammoSpeed;
  }
  
  public void setAmmoSpeed(Integer value)
  {
    this.ammoSpeed = value;
  }
  
  public int getStareRange()
  {
    if (this.stareRange == null) {
      return 10;
    }
    return this.stareRange.intValue();
  }
  
  public void setStareRange(Integer value)
  {
    this.stareRange = value;
  }
  
  public NpcUiType getUiType()
  {
    return this.uiType;
  }
  
  public void setUiType(NpcUiType value)
  {
    this.uiType = value;
  }
}
