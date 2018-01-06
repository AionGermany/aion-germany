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
package com.aionemu.gameserver.geoEngine;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.models.GeoMap;
import com.aionemu.gameserver.geoEngine.scene.Geometry;
import com.aionemu.gameserver.geoEngine.scene.Mesh;
import com.aionemu.gameserver.geoEngine.scene.Node;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.geoEngine.scene.VertexBuffer;
import com.aionemu.gameserver.geoEngine.scene.mesh.DoorGeometry;
import com.aionemu.gameserver.model.templates.materials.MaterialTemplate;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.aionemu.gameserver.world.zone.ZoneService;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 * @author Mr. Poke
 */
public class GeoWorldLoader {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GeoWorldLoader.class);
	private static String GEO_DIR = "data/geo/";
	private static boolean DEBUG = false;

	public static void setDebugMod(boolean debug) {
		DEBUG = debug;
	}

	@SuppressWarnings("resource")
	public static Map<String, Spatial> loadMeshs(String fileName) throws IOException {
		Map<String, Spatial> geoms = new HashMap<String, Spatial>();
		File geoFile = new File(fileName);
		FileChannel roChannel = null;
		MappedByteBuffer geo = null;
		roChannel = new RandomAccessFile(geoFile, "r").getChannel();
		int size = (int) roChannel.size();
		geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
		geo.order(ByteOrder.LITTLE_ENDIAN);
		while (geo.hasRemaining()) {
			short namelenght = geo.getShort();
			byte[] nameByte = new byte[namelenght];
			geo.get(nameByte);
			String name = new String(nameByte).intern();
			Node node = new Node(DEBUG ? name : null);
			byte intentions = 0;
			byte singleChildMaterialId = -1;
			int modelCount = geo.getShort();
			for (int c = 0; c < modelCount; c++) {
				Mesh m = new Mesh();

				int vectorCount = (geo.getShort()) * 3;
				ByteBuffer floatBuffer = ByteBuffer.allocateDirect(vectorCount * 4);
				FloatBuffer vertices = floatBuffer.asFloatBuffer();
				for (int x = 0; x < vectorCount; x++) {
					vertices.put(geo.getFloat());
				}

				int triangles = geo.getInt();
				ByteBuffer shortBuffer = ByteBuffer.allocateDirect(triangles * 2);
				ShortBuffer indexes = shortBuffer.asShortBuffer();
				for (int x = 0; x < triangles; x++) {
					indexes.put(geo.getShort());
				}

				Geometry geom = null;
				m.setCollisionFlags(geo.getShort());
				if ((m.getIntentions() & CollisionIntention.MOVEABLE.getId()) != 0) {
					// TODO: skip moveable collisions (ships, shugo boxes), not handled yet
					continue;
				}
				intentions |= m.getIntentions();
				m.setBuffer(VertexBuffer.Type.Position, 3, vertices);
				m.setBuffer(VertexBuffer.Type.Index, 3, indexes);
				m.createCollisionData();

				if ((intentions & CollisionIntention.DOOR.getId()) != 0 && (intentions & CollisionIntention.PHYSICAL.getId()) != 0) {
					if (!GeoDataConfig.GEO_DOORS_ENABLE) {
						continue;
					}
					geom = new DoorGeometry(name, m);
					// what if doors have few models ?
				}
				else {
					MaterialTemplate mtl = DataManager.MATERIAL_DATA.getTemplate(m.getMaterialId());
					geom = new Geometry(null, m);
					if (mtl != null || m.getMaterialId() == 11) {
						node.setName(name);
					}
					if (modelCount == 1) {
						geom.setName(name);
						singleChildMaterialId = geom.getMaterialId();
					}
					else {
						geom.setName(("child" + c + "_" + name).intern());
					}
					node.attachChild(geom);
				}
				geoms.put(geom.getName(), geom);
			}
			node.setCollisionFlags((short) (intentions << 8 | singleChildMaterialId & 0xFF));
			if (!node.getChildren().isEmpty()) {
				geoms.put(name, node);
			}
		}
		destroyDirectByteBuffer(geo);
		return geoms;

	}

	@SuppressWarnings("resource")
	public static boolean loadWorld(int worldId, Map<String, Spatial> models, GeoMap map) throws IOException {
		File geoFile = new File(GEO_DIR + worldId + ".geo");
		FileChannel roChannel = null;
		MappedByteBuffer geo = null;
		roChannel = new RandomAccessFile(geoFile, "r").getChannel();
		geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size()).load();
		geo.order(ByteOrder.LITTLE_ENDIAN);
		if (geo.get() == 0) {
			map.setTerrainData(new short[] { geo.getShort() });
		}
		else {
			int size = geo.getInt();
			short[] terrainData = new short[size];
			for (int i = 0; i < size; i++) {
				terrainData[i] = geo.getShort();
			}
			map.setTerrainData(terrainData);
		}

		while (geo.hasRemaining()) {
			int nameLength = geo.getShort();
			byte[] nameByte = new byte[nameLength];
			geo.get(nameByte);
			String name = new String(nameByte);
			Vector3f loc = new Vector3f(geo.getFloat(), geo.getFloat(), geo.getFloat());
			float[] matrix = new float[9];
			for (int i = 0; i < 9; i++) {
				matrix[i] = geo.getFloat();
			}
			float scale = geo.getFloat();
			Matrix3f matrix3f = new Matrix3f();
			matrix3f.set(matrix);
			Spatial node = models.get(name.toLowerCase().intern());
			try {
				if (node != null) {
					Spatial nodeClone = node;
					if (node instanceof DoorGeometry) {
						try {
							nodeClone = node.clone();
						}
						catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						createDoors(nodeClone, worldId, matrix3f, loc, scale);
						map.attachChild(nodeClone);
					}
					else {
						nodeClone = attachChild(map, node, matrix3f, loc, scale);
						List<Spatial> children = ((Node) node).descendantMatches("child\\d+_" + name.replace("\\", "\\\\"));
						if (children.size() == 0) {
							createZone(nodeClone, worldId, 0);
						}
						else {
							for (int c = 0; c < children.size(); c++) {
								Spatial child = children.get(c);
								nodeClone = attachChild(map, child, matrix3f, loc, scale);
								createZone(nodeClone, worldId, c + 1);
							}
						}
					}
				}
			}
			catch (Throwable t) {
				System.out.println(t);
			}
		}
		destroyDirectByteBuffer(geo);
		map.updateModelBound();
		return true;
	}

	private static Spatial attachChild(GeoMap map, Spatial node, Matrix3f matrix, Vector3f location, float scale) {
		Spatial nodeClone = node;
		try {
			nodeClone = node.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		nodeClone.setTransform(matrix, location, scale);
		nodeClone.updateModelBound();
		map.attachChild(nodeClone);
		return nodeClone;
	}

	private static void createZone(Spatial node, int worldId, int childNumber) {
		if (GeoDataConfig.GEO_MATERIALS_ENABLE && (node.getIntentions() & CollisionIntention.MATERIAL.getId()) != 0) {
			BoundingVolume bv = node.getWorldBound();
			int regionId = getVectorHash(bv.getCenter().x, bv.getCenter().y, bv.getCenter().z);
			int index = node.getName().lastIndexOf('\\');
			int dotIndex = node.getName().lastIndexOf('.');
			String zoneName = node.getName().substring(index + 1, dotIndex).toUpperCase();
			if (childNumber > 0) {
				zoneName += "_CHILD" + childNumber;
			}
			String existingName = zoneName + "_" + regionId + "_" + worldId;
			if (ZoneName.getId(existingName) != ZoneName.getId(ZoneName.NONE)) {
				// for override
				zoneName += "_" + regionId;
				node.setName(zoneName);
				ZoneService.getInstance().createMaterialZoneTemplate(node, worldId, node.getMaterialId(), true);
			}
			else {
				node.setName(zoneName);
				ZoneService.getInstance().createMaterialZoneTemplate(node, regionId, worldId, node.getMaterialId());
			}
		}
	}

	private static void createDoors(Spatial node, int worldId, Matrix3f matrix, Vector3f location, float scale) {
		node.setTransform(matrix, location, scale);
		node.updateModelBound();
		BoundingVolume bv = node.getWorldBound();
		int regionId = getVectorHash(bv.getCenter().x, bv.getCenter().y, bv.getCenter().z);
		int index = node.getName().lastIndexOf('\\');
		String doorName = worldId + "_" + "DOOR" + "_" + regionId + "_" + node.getName().substring(index + 1).toUpperCase();
		node.setName(doorName);
	}

	/**
	 * Hash formula from paper http://www.beosil.com/download/CollisionDetectionHashing_VMV03.pdf Hash table size 50000, the higher value, more precision
	 */
	private static int getVectorHash(float x, float y, float z) {
		long xIntBits = Float.floatToIntBits(x);
		long yIntBits = Float.floatToIntBits(y);
		long zIntBits = Float.floatToIntBits(z);
		return (int) ((xIntBits * 73856093 ^ yIntBits * 19349663 ^ zIntBits * 83492791) % 50000);
	}

	private static void destroyDirectByteBuffer(Buffer toBeDestroyed) {
		Cleaner cleaner = ((DirectBuffer) toBeDestroyed).cleaner();
		if (cleaner != null) {
			cleaner.clean();
		}
	}
}
