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

import com.aionemu.gameserver.geoEngine.math.FastMath;
import com.aionemu.gameserver.geoEngine.utils.BufferUtils;

public class VertexBuffer extends GLObject implements Cloneable {

	/**
	 * Type of buffer. Specifies the actual attribute it defines.
	 */
	public static enum Type {

		/**
		 * Position of the vertex (3 floats)
		 */
		Position,
		/**
		 * The size of the point when using point buffers.
		 */
		Size,
		/**
		 * Normal vector, normalized.
		 */
		Normal,
		/**
		 * Texture coordinate
		 */
		TexCoord,
		/**
		 * Color and Alpha (4 floats)
		 */
		Color,
		/**
		 * Tangent vector, normalized.
		 */
		Tangent,
		/**
		 * Binormal vector, normalized.
		 */
		Binormal,
		/**
		 * Specifies the source data for various vertex buffers when interleaving is used.
		 */
		InterleavedData,
		/**
		 * Do not use.
		 */
		@Deprecated
		MiscAttrib,
		/**
		 * Specifies the index buffer, must contain integer data.
		 */
		Index,
		/**
		 * Inital vertex position, used with animation
		 */
		BindPosePosition,
		/**
		 * Inital vertex normals, used with animation
		 */
		BindPoseNormal,
		/**
		 * Bone weights, used with animation
		 */
		BoneWeight,
		/**
		 * Bone indices, used with animation
		 */
		BoneIndex,
		/**
		 * Texture coordinate #2
		 */
		TexCoord2;
	}

	/**
	 * The usage of the VertexBuffer, specifies how often the buffer is used. This can determine if a vertex buffer is placed in VRAM or held in video memory, but no garantees are made- it's only a
	 * hint.
	 */
	public static enum Usage {

		/**
		 * Mesh data is sent once and very rarely updated.
		 */
		Static,
		/**
		 * Mesh data is updated occasionally (once per frame or less).
		 */
		Dynamic,
		/**
		 * Mesh data is updated every frame.
		 */
		Stream,
		/**
		 * Mesh data is not sent to GPU at all. It is only used by the CPU.
		 */
		CpuOnly;
	}

	public static enum Format {
		// Floating point formats

		Half(2),
		Float(4),
		Double(8),
		// Integer formats
		Byte(1),
		UnsignedByte(1),
		Short(2),
		UnsignedShort(2),
		Int(4),
		UnsignedInt(4);

		private int componentSize = 0;

		Format(int componentSize) {
			this.componentSize = componentSize;
		}

		/**
		 * @return Size in bytes of this data type.
		 */
		public int getComponentSize() {
			return componentSize;
		}
	}

	protected int offset = 0;
	protected int stride = 0;
	protected int components = 0;
	/**
	 * derived from components * format.getComponentSize()
	 */
	protected transient int componentsLength = 0;
	protected Buffer data = null;
	protected transient ByteBuffer mappedData;
	protected Usage usage;
	protected Type bufType;
	protected Format format;
	protected boolean normalized = false;
	protected transient boolean dataSizeChanged = false;

	/**
	 * Creates an empty, uninitialized buffer. Must call setupData() to initialize.
	 */
	public VertexBuffer(Type type) {
		super(GLObject.Type.VertexBuffer);
		this.bufType = type;
	}

	/**
	 * Do not use this constructor. Serialization purposes only.
	 */
	public VertexBuffer() {
		super(GLObject.Type.VertexBuffer);
	}

	protected VertexBuffer(int id) {
		super(GLObject.Type.VertexBuffer, id);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public Buffer getData() {
		return data;
	}

	public ByteBuffer getMappedData() {
		return mappedData;
	}

	public void setMappedData(ByteBuffer mappedData) {
		this.mappedData = mappedData;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		// if (id != -1)
		// throw new UnsupportedOperationException("Data has already been sent. Cannot set usage.");

		this.usage = usage;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public Type getBufferType() {
		return bufType;
	}

	public Format getFormat() {
		return format;
	}

	public int getNumComponents() {
		return components;
	}

	public int getNumElements() {
		int elements = data.capacity() / components;
		if (format == Format.Half) {
			elements /= 2;
		}
		return elements;
	}

	public void setupData(Usage usage, int components, Format format, Buffer data) {
		if (id != -1) {
			throw new UnsupportedOperationException("Data has already been sent. Cannot setupData again.");
		}

		this.data = data;
		this.components = components;
		this.usage = usage;
		this.format = format;
		this.componentsLength = components * format.getComponentSize();
		setUpdateNeeded();
	}

	public void updateData(Buffer data) {
		if (id != -1) {
			// request to update data is okay
		}

		// will force renderer to call glBufferData again
		if (this.data.capacity() != data.capacity()) {
			dataSizeChanged = true;
		}
		this.data = data;
		setUpdateNeeded();
	}

	public boolean hasDataSizeChanged() {
		return dataSizeChanged;
	}

	@Override
	public void clearUpdateNeeded() {
		super.clearUpdateNeeded();
		dataSizeChanged = false;
	}

	public void convertToHalf() {
		if (id != -1) {
			throw new UnsupportedOperationException("Data has already been sent.");
		}

		if (format != Format.Float) {
			throw new IllegalStateException("Format must be float!");
		}

		int numElements = data.capacity() / components;
		format = Format.Half;
		this.componentsLength = components * format.getComponentSize();

		ByteBuffer halfData = BufferUtils.createByteBuffer(componentsLength * numElements);
		halfData.rewind();

		FloatBuffer floatData = (FloatBuffer) data;
		floatData.rewind();

		for (int i = 0; i < floatData.capacity(); i++) {
			float f = floatData.get(i);
			short half = FastMath.convertFloatToHalf(f);
			halfData.putShort(half);
		}
		this.data = halfData;
		setUpdateNeeded();
		dataSizeChanged = true;
	}

	public void compact(int numElements) {
		int total = components * numElements;
		data.clear();
		switch (format) {
			case Byte:
			case UnsignedByte:
			case Half:
				ByteBuffer bbuf = (ByteBuffer) data;
				bbuf.limit(total);
				ByteBuffer bnewBuf = BufferUtils.createByteBuffer(total);
				bnewBuf.put(bbuf);
				data = bnewBuf;
				break;
			case Short:
			case UnsignedShort:
				ShortBuffer sbuf = (ShortBuffer) data;
				sbuf.limit(total);
				ShortBuffer snewBuf = BufferUtils.createShortBuffer(total);
				snewBuf.put(sbuf);
				data = snewBuf;
				break;
			case Int:
			case UnsignedInt:
				IntBuffer ibuf = (IntBuffer) data;
				ibuf.limit(total);
				IntBuffer inewBuf = BufferUtils.createIntBuffer(total);
				inewBuf.put(ibuf);
				data = inewBuf;
				break;
			case Float:
				FloatBuffer fbuf = (FloatBuffer) data;
				fbuf.limit(total);
				FloatBuffer fnewBuf = BufferUtils.createFloatBuffer(total);
				fnewBuf.put(fbuf);
				data = fnewBuf;
				break;
			default:
				throw new UnsupportedOperationException("Unrecognized buffer format: " + format);
		}
		data.clear();
		setUpdateNeeded();
		dataSizeChanged = true;
	}

	public void copyElement(int inIndex, VertexBuffer outVb, int outIndex) {
		if (outVb.format != format || outVb.components != components) {
			throw new IllegalArgumentException("Buffer format mismatch. Cannot copy");
		}

		int inPos = inIndex * components;
		int outPos = outIndex * components;
		int elementSz = components;
		if (format == Format.Half) {
			// because half is stored as bytebuf but its 2 bytes long
			inPos *= 2;
			outPos *= 2;
			elementSz *= 2;
		}

		data.clear();
		outVb.data.clear();

		switch (format) {
			case Byte:
			case UnsignedByte:
			case Half:
				ByteBuffer bin = (ByteBuffer) data;
				ByteBuffer bout = (ByteBuffer) outVb.data;
				bin.position(inPos).limit(inPos + elementSz);
				bout.position(outPos).limit(outPos + elementSz);
				bout.put(bin);
				break;
			case Short:
			case UnsignedShort:
				ShortBuffer sin = (ShortBuffer) data;
				ShortBuffer sout = (ShortBuffer) outVb.data;
				sin.position(inPos).limit(inPos + elementSz);
				sout.position(outPos).limit(outPos + elementSz);
				sout.put(sin);
				break;
			case Int:
			case UnsignedInt:
				IntBuffer iin = (IntBuffer) data;
				IntBuffer iout = (IntBuffer) outVb.data;
				iin.position(inPos).limit(inPos + elementSz);
				iout.position(outPos).limit(outPos + elementSz);
				iout.put(iin);
				break;
			case Float:
				FloatBuffer fin = (FloatBuffer) data;
				FloatBuffer fout = (FloatBuffer) outVb.data;
				fin.position(inPos).limit(inPos + elementSz);
				fout.position(outPos).limit(outPos + elementSz);
				fout.put(fin);
				break;
			default:
				throw new UnsupportedOperationException("Unrecognized buffer format: " + format);
		}

		data.clear();
		outVb.data.clear();
	}

	public static final Buffer createBuffer(Format format, int components, int numElements) {
		if (components < 1 || components > 4) {
			throw new IllegalArgumentException("Num components must be between 1 and 4");
		}

		int total = numElements * components;

		switch (format) {
			case Byte:
			case UnsignedByte:
				return BufferUtils.createByteBuffer(total);
			case Half:
				return BufferUtils.createByteBuffer(total * 2);
			case Short:
			case UnsignedShort:
				return BufferUtils.createShortBuffer(total);
			case Int:
			case UnsignedInt:
				return BufferUtils.createIntBuffer(total);
			case Float:
				return BufferUtils.createFloatBuffer(total);
			case Double:
				return BufferUtils.createDoubleBuffer(total);
			default:
				throw new UnsupportedOperationException("Unrecoginized buffer format: " + format);
		}
	}

	@Override
	public VertexBuffer clone() {
		// NOTE: Superclass GLObject automatically creates shallow clone
		// e.g re-use ID.
		VertexBuffer vb = (VertexBuffer) super.clone();
		if (data != null) {
			vb.updateData(BufferUtils.clone(data));
		}

		return vb;
	}

	public VertexBuffer clone(Type overrideType) {
		VertexBuffer vb = new VertexBuffer(overrideType);
		vb.components = components;
		vb.componentsLength = componentsLength;
		vb.data = BufferUtils.clone(data);
		vb.format = format;
		vb.handleRef = new Object();
		vb.id = -1;
		vb.normalized = normalized;
		vb.offset = offset;
		vb.stride = stride;
		vb.updateNeeded = true;
		vb.usage = usage;
		return vb;
	}

	@Override
	public String toString() {
		String dataTxt = null;
		if (data != null) {
			dataTxt = ", elements=" + data.capacity();
		}
		return getClass().getSimpleName() + "[fmt=" + format.name() + ", type=" + bufType.name() + ", usage=" + usage.name() + dataTxt + "]";
	}

	@Override
	public void resetObject() {
		// assert this.id != -1;
		this.id = -1;
		setUpdateNeeded();
	}

	@Override
	public GLObject createDestructableClone() {
		return new VertexBuffer(id);
	}
}
