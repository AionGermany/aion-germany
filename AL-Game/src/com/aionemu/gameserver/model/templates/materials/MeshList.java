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
package com.aionemu.gameserver.model.templates.materials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeshList", propOrder = { "meshMaterials" })
public class MeshList {

	@XmlElement(name = "mesh", required = true)
	protected List<MeshMaterial> meshMaterials;
	@XmlAttribute(name = "world_id", required = true)
	protected int worldId;
	@XmlTransient
	Map<String, Integer> materialIdsByPath = new HashMap<String, Integer>();
	@XmlTransient
	Map<Integer, String> pathZones = new HashMap<Integer, String>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (meshMaterials == null) {
			return;
		}

		for (MeshMaterial meshMaterial : meshMaterials) {
			materialIdsByPath.put(meshMaterial.path, meshMaterial.materialId);
			pathZones.put(meshMaterial.path.hashCode(), meshMaterial.getZoneName());
			meshMaterial.path = null;
		}

		meshMaterials.clear();
		meshMaterials = null;
	}

	public int getWorldId() {
		return worldId;
	}

	/**
	 * Find material ID for the specific mesh
	 *
	 * @param meshPath
	 *            Mesh geo path
	 * @return 0 if not found
	 */
	public int getMeshMaterialId(String meshPath) {
		Integer materialId = materialIdsByPath.get(meshPath);
		if (materialId == null) {
			return 0;
		}
		return materialId;
	}

	public Set<String> getMeshPaths() {
		return materialIdsByPath.keySet();
	}

	public String getZoneName(String meshPath) {
		return pathZones.get(meshPath.hashCode());
	}

	public int size() {
		return materialIdsByPath.size();
	}
}
