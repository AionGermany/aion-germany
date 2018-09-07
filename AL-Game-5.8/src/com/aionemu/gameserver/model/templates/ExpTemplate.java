package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Antraxx
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "exp")
public class ExpTemplate {

	@XmlAttribute(name = "level", required = true)
	private int level;

	@XmlAttribute(name = "exp", required = true)
	private long exp;

	@XmlAttribute(name = "cp", required = false)
	private int cp;

	@XmlAttribute(name = "cp_lvlup", required = false)
	private int cpLevelUp;

	@XmlAttribute(name = "feverpoint_boost", required = false)
	private long feverPointBoost;

	@XmlAttribute(name = "feverpoint_max", required = false)
	private long feverPointMax;

	public int getLevel() {
		return level;
	}

	public long getExp() {
		return exp;
	}

	public int getCp() {
		return cp;
	}

	public int getCpLevelUp() {
		return cpLevelUp;
	}

	public long getFeverPointBoost() {
		return feverPointBoost;
	}

	public long getFeverPointMax() {
		return feverPointMax;
	}

}
