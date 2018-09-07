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
package com.aionemu.gameserver.geoEngine.scene;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.aionemu.gameserver.geoEngine.bounding.BoundingBox;
import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.collision.bih.BIHTree;
import com.aionemu.gameserver.geoEngine.math.Matrix4f;
import com.aionemu.gameserver.geoEngine.math.Triangle;
import com.aionemu.gameserver.geoEngine.math.Vector2f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.VertexBuffer.Format;
import com.aionemu.gameserver.geoEngine.scene.VertexBuffer.Type;
import com.aionemu.gameserver.geoEngine.scene.VertexBuffer.Usage;
import com.aionemu.gameserver.geoEngine.scene.mesh.IndexBuffer;
import com.aionemu.gameserver.geoEngine.scene.mesh.IndexByteBuffer;
import com.aionemu.gameserver.geoEngine.scene.mesh.IndexIntBuffer;
import com.aionemu.gameserver.geoEngine.scene.mesh.IndexShortBuffer;
import com.aionemu.gameserver.geoEngine.utils.BufferUtils;
import com.aionemu.gameserver.geoEngine.utils.IntMap;
import com.aionemu.gameserver.geoEngine.utils.IntMap.Entry;

public class Mesh {

	// TODO: Document this enum
	public enum Mode {

		Points,
		Lines,
		LineLoop,
		LineStrip,
		Triangles,
		TriangleStrip,
		TriangleFan,
		Hybrid
	}

	// private static final int BUFFERS_SIZE = VertexBuffer.Type.BoneIndex.ordinal() + 1;
	/**
	 * The bounding volume that contains the mesh entirely. By default a BoundingBox (AABB).
	 */
	private BoundingVolume meshBound = new BoundingBox();
	private CollisionData collisionTree = null;
	// private EnumMap<VertexBuffer.Type, VertexBuffer> buffers = new EnumMap<Type,
	// VertexBuffer>(VertexBuffer.Type.class);
	// private VertexBuffer[] buffers = new VertexBuffer[BUFFERS_SIZE];
	private IntMap<VertexBuffer> buffers = new IntMap<VertexBuffer>();
	private float pointSize = 1;
	private float lineWidth = 1;
	private transient int vertexArrayID = -1;
	private int vertCount = -1;
	private int elementCount = -1;
	private int maxNumWeights = -1; // only if using skeletal animation
	private int[] modeStart;
	private Mode mode = Mode.Triangles;
	private short collisionFlags = -1;

	public Mesh() {
	}

	public int[] getModeStart() {
		return modeStart;
	}

	public void setModeStart(int[] modeStart) {
		this.modeStart = modeStart;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		updateCounts();
	}

	public int getMaxNumWeights() {
		return maxNumWeights;
	}

	public void setMaxNumWeights(int maxNumWeights) {
		this.maxNumWeights = maxNumWeights;
	}

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Locks the mesh so it cannot be modified anymore, thus optimizing its data.
	 */
	@SuppressWarnings("unchecked")
	public void setStatic() {
		for (Entry<VertexBuffer> entry : buffers) {
			entry.getValue().setUsage(Usage.Static);
		}
	}

	@SuppressWarnings("unchecked")
	public void setStreamed() {
		for (Entry<VertexBuffer> entry : buffers) {
			entry.getValue().setUsage(Usage.Stream);
		}
	}

	@SuppressWarnings("unchecked")
	public void setInterleaved() {
		ArrayList<VertexBuffer> vbs = new ArrayList<VertexBuffer>();
		for (Entry<VertexBuffer> entry : buffers) {
			vbs.add(entry.getValue());
		}
		// ArrayList<VertexBuffer> vbs = new ArrayList<VertexBuffer>(buffers.values());
		// index buffer not included when interleaving
		vbs.remove(getBuffer(Type.Index));

		int stride = 0; // aka bytes per vertex
		for (int i = 0; i < vbs.size(); i++) {
			VertexBuffer vb = vbs.get(i);
			// if (vb.getFormat() != Format.Float){
			// throw new UnsupportedOperationException("Cannot interleave vertex buffer.\n" +
			// "Contains not-float data.");
			// }
			stride += vb.componentsLength;
			vb.getData().clear(); // reset position & limit (used later)
		}

		VertexBuffer allData = new VertexBuffer(Type.InterleavedData);
		ByteBuffer dataBuf = BufferUtils.createByteBuffer(stride * getVertexCount());
		allData.setupData(Usage.Static, -1, Format.UnsignedByte, dataBuf);
		setBuffer(allData);

		for (int vert = 0; vert < getVertexCount(); vert++) {
			for (int i = 0; i < vbs.size(); i++) {
				VertexBuffer vb = vbs.get(i);
				switch (vb.getFormat()) {
					case Float:
						FloatBuffer fb = (FloatBuffer) vb.getData();
						for (int comp = 0; comp < vb.components; comp++) {
							dataBuf.putFloat(fb.get());
						}
						break;
					case Byte:
					case UnsignedByte:
						ByteBuffer bb = (ByteBuffer) vb.getData();
						for (int comp = 0; comp < vb.components; comp++) {
							dataBuf.put(bb.get());
						}
						break;
					case Half:
					case Short:
					case UnsignedShort:
						ShortBuffer sb = (ShortBuffer) vb.getData();
						for (int comp = 0; comp < vb.components; comp++) {
							dataBuf.putShort(sb.get());
						}
						break;
					case Int:
					case UnsignedInt:
						IntBuffer ib = (IntBuffer) vb.getData();
						for (int comp = 0; comp < vb.components; comp++) {
							dataBuf.putInt(ib.get());
						}
						break;
					default:
						break;
				}
			}
		}

		int offset = 0;
		for (VertexBuffer vb : vbs) {
			vb.setOffset(offset);
			vb.setStride(stride);

			// discard old buffer
			vb.setupData(vb.usage, vb.components, vb.format, null);
			offset += vb.componentsLength;
		}
	}

	private int computeNumElements(int bufSize) {
		switch (mode) {
			case Triangles:
				return bufSize / 3;
			case TriangleFan:
			case TriangleStrip:
				return bufSize - 2;
			case Points:
				return bufSize;
			case Lines:
				return bufSize / 2;
			case LineLoop:
				return bufSize;
			case LineStrip:
				return bufSize - 1;
			default:
				throw new UnsupportedOperationException();
		}
	}

	public void updateCounts() {
		if (getBuffer(Type.InterleavedData) != null) {
			throw new IllegalStateException("Should update counts before interleave");
		}

		VertexBuffer pb = getBuffer(Type.Position);
		VertexBuffer ib = getBuffer(Type.Index);
		if (pb != null) {
			vertCount = pb.getData().capacity() / pb.getNumComponents();
		}
		if (ib != null) {
			elementCount = computeNumElements(ib.getData().capacity());
		}
		else {
			elementCount = computeNumElements(vertCount);
		}
	}

	public int getTriangleCount(int lod) {
		return elementCount;
	}

	public int getTriangleCount() {
		return elementCount;
	}

	public int getVertexCount() {
		return vertCount;
	}

	public void setTriangleCount(int count) {
		this.elementCount = count;
	}

	public void setVertexCount(int count) {
		this.vertCount = count;
	}

	public void getTriangle(int index, Vector3f v1, Vector3f v2, Vector3f v3) {
		VertexBuffer pb = getBuffer(Type.Position);
		VertexBuffer ib = getBuffer(Type.Index);

		if (pb.getFormat() == Format.Float) {
			FloatBuffer fpb = (FloatBuffer) pb.getData();

			if (ib.getFormat() == Format.UnsignedShort) {
				// accepted format for buffers
				ShortBuffer sib = (ShortBuffer) ib.getData();

				// aquire triangle's vertex indices
				int vertIndex = index * 3;
				int vert1 = sib.get(vertIndex);
				int vert2 = sib.get(vertIndex + 1);
				int vert3 = sib.get(vertIndex + 2);

				BufferUtils.populateFromBuffer(v1, fpb, vert1);
				BufferUtils.populateFromBuffer(v2, fpb, vert2);
				BufferUtils.populateFromBuffer(v3, fpb, vert3);
			}
		}
	}

	public void getTriangle(int index, Triangle tri) {
		getTriangle(index, tri.get1(), tri.get2(), tri.get3());
		tri.setIndex(index);
	}

	public void getTriangle(int index, int[] indices) {
		VertexBuffer ib = getBuffer(Type.Index);
		if (ib.getFormat() == Format.UnsignedShort) {
			// accepted format for buffers
			ShortBuffer sib = (ShortBuffer) ib.getData();

			// aquire triangle's vertex indices
			int vertIndex = index * 3;
			indices[0] = sib.get(vertIndex);
			indices[1] = sib.get(vertIndex + 1);
			indices[2] = sib.get(vertIndex + 2);
		}
	}

	public int getId() {
		return vertexArrayID;
	}

	public void setId(int id) {
		if (vertexArrayID != -1) {
			throw new IllegalStateException("ID has already been set.");
		}

		vertexArrayID = id;
	}

	public void createCollisionData() {
		if (collisionTree != null) {
			return;
		}
		BIHTree tree = new BIHTree(this);
		tree.construct();
		collisionTree = tree;
	}

	public int collideWith(Collidable other, Matrix4f worldMatrix, BoundingVolume worldBound, CollisionResults results) {

		if (collisionTree == null) {
			createCollisionData();
		}

		return collisionTree.collideWith(other, worldMatrix, worldBound, results);
	}

	public void setBuffer(Type type, int components, FloatBuffer buf) {
		VertexBuffer vb = buffers.get(type.ordinal());
		if (vb == null) {
			if (buf == null) {
				return;
			}

			vb = new VertexBuffer(type);
			vb.setupData(Usage.Dynamic, components, Format.Float, buf);
			// buffers.put(type, vb);
			buffers.put(type.ordinal(), vb);
		}
		else {
			vb.setupData(Usage.Dynamic, components, Format.Float, buf);
		}
		updateCounts();
	}

	public void setBuffer(Type type, int components, float[] buf) {
		setBuffer(type, components, BufferUtils.createFloatBuffer(buf));
	}

	public void setBuffer(Type type, int components, IntBuffer buf) {
		VertexBuffer vb = buffers.get(type.ordinal());
		if (vb == null) {
			vb = new VertexBuffer(type);
			vb.setupData(Usage.Dynamic, components, Format.UnsignedInt, buf);
			buffers.put(type.ordinal(), vb);
			updateCounts();
		}
	}

	public void setBuffer(Type type, int components, int[] buf) {
		setBuffer(type, components, BufferUtils.createIntBuffer(buf));
	}

	public void setBuffer(Type type, int components, ShortBuffer buf) {
		VertexBuffer vb = buffers.get(type.ordinal());
		if (vb == null) {
			vb = new VertexBuffer(type);
			vb.setupData(Usage.Dynamic, components, Format.UnsignedShort, buf);
			buffers.put(type.ordinal(), vb);
			updateCounts();
		}
	}

	public void setBuffer(Type type, int components, byte[] buf) {
		setBuffer(type, components, BufferUtils.createByteBuffer(buf));
	}

	public void setBuffer(Type type, int components, ByteBuffer buf) {
		VertexBuffer vb = buffers.get(type.ordinal());
		if (vb == null) {
			vb = new VertexBuffer(type);
			vb.setupData(Usage.Dynamic, components, Format.UnsignedByte, buf);
			buffers.put(type.ordinal(), vb);
			updateCounts();
		}
	}

	public void setBuffer(VertexBuffer vb) {
		if (buffers.containsKey(vb.getBufferType().ordinal())) {
			throw new IllegalArgumentException("Buffer type already set: " + vb.getBufferType());
		}

		buffers.put(vb.getBufferType().ordinal(), vb);
	}

	public void clearBuffer(VertexBuffer.Type type) {
		buffers.remove(type.ordinal());
	}

	public void setBuffer(Type type, int components, short[] buf) {
		setBuffer(type, components, BufferUtils.createShortBuffer(buf));
	}

	public VertexBuffer getBuffer(Type type) {
		return buffers.get(type.ordinal());
	}

	public FloatBuffer getFloatBuffer(Type type) {
		VertexBuffer vb = getBuffer(type);
		if (vb == null) {
			return null;
		}

		return (FloatBuffer) vb.getData();
	}

	public ShortBuffer getShortBuffer(Type type) {
		VertexBuffer vb = getBuffer(type);
		if (vb == null) {
			return null;
		}

		return (ShortBuffer) vb.getData();
	}

	public IndexBuffer getIndexBuffer() {
		VertexBuffer vb = getBuffer(Type.Index);
		if (vb == null) {
			return null;
		}

		Buffer buf = vb.getData();
		if (buf instanceof ByteBuffer) {
			return new IndexByteBuffer((ByteBuffer) buf);
		}
		else if (buf instanceof ShortBuffer) {
			return new IndexShortBuffer((ShortBuffer) buf);
		}
		else if (buf instanceof IntBuffer) {
			return new IndexIntBuffer((IntBuffer) buf);
		}
		else {
			throw new UnsupportedOperationException("Index buffer type unsupported: " + buf.getClass());
		}
	}

	public void scaleTextureCoordinates(Vector2f scaleFactor) {
		VertexBuffer tc = getBuffer(Type.TexCoord);
		if (tc == null) {
			throw new IllegalStateException("The mesh has no texture coordinates");
		}

		if (tc.getFormat() != VertexBuffer.Format.Float) {
			throw new UnsupportedOperationException("Only float texture coord format is supported");
		}

		if (tc.getNumComponents() != 2) {
			throw new UnsupportedOperationException("Only 2D texture coords are supported");
		}

		FloatBuffer fb = (FloatBuffer) tc.getData();
		fb.clear();
		for (int i = 0; i < fb.capacity() / 2; i++) {
			float x = fb.get();
			float y = fb.get();
			fb.position(fb.position() - 2);
			x *= scaleFactor.getX();
			y *= scaleFactor.getY();
			fb.put(x).put(y);
		}
		fb.clear();
	}

	public void updateBound() {
		VertexBuffer posBuf = getBuffer(VertexBuffer.Type.Position);
		if (meshBound == null) {
			meshBound = new BoundingBox();
		}
		if (posBuf != null) {
			meshBound.computeFromPoints((FloatBuffer) posBuf.getData());
		}
	}

	public BoundingVolume getBound() {
		return meshBound;
	}

	public void setBound(BoundingVolume modelBound) {
		meshBound = modelBound;
	}

	public IntMap<VertexBuffer> getBuffers() {
		return buffers;
	}

	public short getCollisionFlags() {
		return collisionFlags;
	}

	public void setCollisionFlags(short collisionFlags) {
		this.collisionFlags = collisionFlags;
	}

	public byte getMaterialId() {
		return (byte) (collisionFlags & 0xFF);
	}

	public byte getIntentions() {
		return (byte) (collisionFlags >> 8);
	}
}
