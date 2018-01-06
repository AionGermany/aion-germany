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
package com.aionemu.gameserver.model.templates.cosmeticitems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CosmeticItemTemplate")
public class CosmeticItemTemplate {

	@XmlAttribute(name = "type")
	private String type;
	@XmlAttribute(name = "cosmetic_name")
	private String cosmeticName;
	@XmlAttribute(name = "id")
	private int id;
	@XmlAttribute(name = "race")
	private Race race;
	@XmlAttribute(name = "gender_permitted")
	private String genderPermitted;
	@XmlElement(name = "preset")
	private Preset preset;

	public String getType() {
		return type;
	}

	public String getCosmeticName() {
		return cosmeticName;
	}

	public int getId() {
		return id;
	}

	public Race getRace() {
		return race;
	}

	public String getGenderPermitted() {
		return genderPermitted;
	}

	public Preset getPreset() {
		return preset;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "Preset")
	public static class Preset {

		@XmlElement(name = "scale")
		private float scale;
		@XmlElement(name = "hair_type")
		private int hairType;
		@XmlElement(name = "face_type")
		private int faceType;
		@XmlElement(name = "hair_color")
		private int hairColor;
		@XmlElement(name = "lip_color")
		private int lipColor;
		@XmlElement(name = "eye_color")
		private int eyeColor;
		@XmlElement(name = "eye_color2")
		private int eyeColor2;
		@XmlElement(name = "skin_color")
		private int skinColor;

		public float getScale() {
			return scale;
		}

		public int getHairType() {
			return hairType;
		}

		public int getFaceType() {
			return faceType;
		}

		public int getHairColor() {
			return hairColor;
		}

		public int getLipColor() {
			return lipColor;
		}

		public int getEyeColor() {
			return eyeColor;
		}

		public int getEyeColor2() {
			return eyeColor2;
		}

		public int getSkinColor() {
			return skinColor;
		}
	}
}
