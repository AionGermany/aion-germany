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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

/**
 * @author Falke_34
 */
public class TransformationCommonData extends VisibleObjectTemplate implements IExpirable {

	private int transformationId;
	private final int transformationObjId;
	private int masterObjectId;
	private String transformationGrade;
	private String name;
	private int count;

	public TransformationCommonData(int transformationId, int masterObjectId, String name, String grade, int count) {
		this.transformationObjId = IDFactory.getInstance().nextId();
		this.transformationId = transformationId;
		this.masterObjectId = masterObjectId;
		this.name = name;
		this.transformationGrade = grade;
		this.count = count;
	}

	public int getObjectId() {
		return transformationObjId;
	}

	public int getMasterObjectId() {
		return masterObjectId;
	}

	public int getTransformationId() {
		return transformationId;
	}

	public int setTransformationId(int transformationId) {
		return this.transformationId = transformationId;
	}

	public String getTransformationGrade() {
		return transformationGrade;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGrade(String grade) {
		this.transformationGrade = grade;
	}

	@Override
	public int getTemplateId() {
		return transformationId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNameId() {
		return 0;
	}

	public String getGrade() {
		return transformationGrade;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@Override
	public int getExpireTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void expireEnd(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canExpireNow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void expireMessage(Player player, int time) {
		// TODO Auto-generated method stub
		
	}
}
