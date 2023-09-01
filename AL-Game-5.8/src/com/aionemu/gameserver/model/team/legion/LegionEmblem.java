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
package com.aionemu.gameserver.model.team.legion;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author Simple modified cura
 */
public class LegionEmblem {

	private int emblemId = 0x00;
	private int color_r = 0x00;
	private int color_g = 0x00;
	private int color_b = 0x00;
	private boolean defaultEmblem = true;
	private LegionEmblemType emblemType = LegionEmblemType.DEFAULT;
	private PersistentState persistentState;
	private boolean isUploading = false;
	private int uploadSize = 0;
	private int uploadedSize = 0;
	private byte[] uploadData;
	private byte[] customEmblemData;

	/**
	 * @return the customEmblemData
	 */
	public byte[] getCustomEmblemData() {
		return customEmblemData;
	}

	/**
	 * @param customEmblemData
	 *            the customEmblemData to set
	 */
	public void setCustomEmblemData(byte[] customEmblemData) {
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		this.customEmblemData = customEmblemData;
		this.emblemType = LegionEmblemType.CUSTOM;
	}

	public LegionEmblem() {
		setPersistentState(PersistentState.NEW);
	}

	/**
	 * @param emblemId
	 *            the emblemId to set
	 * @param color_r
	 *            the color_r to set
	 * @param color_g
	 *            the color_g to set
	 * @param color_b
	 *            the color_b to set
	 * @param emblemType
	 *            the emblemType to set
	 * @param emblem_data
	 */
	public void setEmblem(int emblemId, int color_r, int color_g, int color_b, LegionEmblemType emblemType, byte[] emblem_data) {
		this.emblemId = emblemId;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		this.emblemType = emblemType;
		this.customEmblemData = emblem_data;
		if (this.emblemType.equals(LegionEmblemType.CUSTOM) && customEmblemData == null) {
			this.emblemId = 0;
			this.emblemType = LegionEmblemType.DEFAULT;
		}

		setPersistentState(PersistentState.UPDATE_REQUIRED);
		this.defaultEmblem = false;
	}

	/**
	 * @return the emblemId
	 */
	public int getEmblemId() {
		return emblemId;
	}

	/**
	 * @return the color_r
	 */
	public int getColor_r() {
		return color_r;
	}

	/**
	 * @return the color_g
	 */
	public int getColor_g() {
		return color_g;
	}

	/**
	 * @return the color_b
	 */
	public int getColor_b() {
		return color_b;
	}

	/**
	 * @return the defaultEmblem
	 */
	public boolean isDefaultEmblem() {
		return defaultEmblem;
	}

	/**
	 * @param isUploading
	 *            the isUploading to set
	 */
	public void setUploading(boolean isUploading) {
		this.isUploading = isUploading;
	}

	/**
	 * @return the isUploading
	 */
	public boolean isUploading() {
		return isUploading;
	}

	/**
	 * @param emblemSize
	 *            the emblemSize to set
	 */
	public void setUploadSize(int emblemSize) {
		this.uploadSize = emblemSize;
	}

	/**
	 * @return the emblemSize
	 */
	public int getUploadSize() {
		return uploadSize;
	}

	/**
	 * @param uploadData
	 *            the uploadData to set
	 */
	public void addUploadData(byte[] data) {
		byte[] newData = new byte[uploadedSize];
		int i = 0;
		if (uploadData != null && uploadData.length > 0) {
			for (byte dataByte : uploadData) {
				newData[i] = dataByte;
				i++;
			}
		}
		for (byte dataByte : data) {
			newData[i] = dataByte;
			i++;
		}
		this.uploadData = newData;
	}

	/**
	 * @return the uploadData
	 */
	public byte[] getUploadData() {
		return this.uploadData;
	}

	/**
	 * @param uploadedSize
	 *            the uploadedSize to set
	 */
	public void addUploadedSize(int uploadedSize) {
		this.uploadedSize += uploadedSize;
	}

	/**
	 * @return the uploadedSize
	 */
	public int getUploadedSize() {
		return uploadedSize;
	}

	/**
	 * @param emblemType
	 *            the emblemType to set
	 */
	public void setEmblemType(LegionEmblemType emblemType) {
		this.emblemType = emblemType;
	}

	/**
	 * @return the emblemType
	 */
	public LegionEmblemType getEmblemType() {
		return emblemType;
	}

	/**
	 * This method will clear out all upload data
	 */
	public void resetUploadSettings() {
		this.isUploading = false;
		this.uploadedSize = 0;
		this.uploadData = null;
	}

	/**
	 * @param persistentState
	 */
	public void setPersistentState(PersistentState persistentState) {
		switch (persistentState) {
			case UPDATE_REQUIRED:
				if (this.persistentState == PersistentState.NEW) {
					break;
				}
			default:
				this.persistentState = persistentState;
		}
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState() {
		return persistentState;
	}
}
