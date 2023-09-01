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
package com.aionemu.gameserver.geoEngine.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;
import com.aionemu.gameserver.geoEngine.math.Vector2f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * <code>BufferUtils</code> is a helper class for generating nio buffers from jME data classes such as Vectors and ColorRGBA.
 *
 * @author Joshua Slack
 * @version $Id: BufferUtils.java,v 1.16 2007/10/29 16:56:18 nca Exp $
 */
public final class BufferUtils {

	//// -- TEMP DATA OBJECTS -- ////
	// private static final Vector2f _tempVec2 = new Vector2f();
	// private static final Vector3f _tempVec3 = new Vector3f();
	// private static final ColorRGBA _tempColor = new ColorRGBA();
	//// -- TRACKER HASH -- ////
	private static final Map<Buffer, Object> trackingHash = PlatformDependent.newConcurrentHashMap(new WeakHashMap<Buffer, Object>());
	private static final Object ref = new Object();
	private static final boolean trackDirectMemory = false;

	//// -- GENERIC CLONE -- ////
	public static Buffer clone(Buffer buf) {
		if (buf instanceof FloatBuffer) {
			return clone((FloatBuffer) buf);
		}
		else if (buf instanceof ShortBuffer) {
			return clone((ShortBuffer) buf);
		}
		else if (buf instanceof ByteBuffer) {
			return clone((ByteBuffer) buf);
		}
		else if (buf instanceof IntBuffer) {
			return clone((IntBuffer) buf);
		}
		else if (buf instanceof DoubleBuffer) {
			return clone((DoubleBuffer) buf);
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	//// -- VECTOR3F METHODS -- ////

	/**
	 * Generate a new FloatBuffer using the given array of Vector3f objects. The FloatBuffer will be 3 * data.length long and contain the vector data as data[0].x, data[0].y, data[0].z, data[1].x...
	 * etc.
	 *
	 * @param data
	 *            array of Vector3f objects to place into a new FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(Vector3f... data) {
		if (data == null) {
			return null;
		}
		FloatBuffer buff = createFloatBuffer(3 * data.length);
		for (int x = 0; x < data.length; x++) {
			if (data[x] != null) {
				buff.put(data[x].x).put(data[x].y).put(data[x].z);
			}
			else {
				buff.put(0).put(0).put(0);
			}
		}
		buff.flip();
		return buff;
	}

	/**
	 * Generate a new FloatBuffer using the given array of float primitives.
	 *
	 * @param data
	 *            array of float primitives to place into a new FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(float... data) {
		if (data == null) {
			return null;
		}
		FloatBuffer buff = createFloatBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	/**
	 * Create a new FloatBuffer of an appropriate size to hold the specified number of Vector3f object data.
	 *
	 * @param vertices
	 *            number of vertices that need to be held by the newly created buffer
	 * @return the requested new FloatBuffer
	 */
	public static FloatBuffer createVector3Buffer(int vertices) {
		FloatBuffer vBuff = createFloatBuffer(3 * vertices);
		return vBuff;
	}

	/**
	 * Create a new FloatBuffer of an appropriate size to hold the specified number of Vector3f object data only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param vertices
	 *            number of vertices that need to be held by the newly created buffer
	 * @return the requested new FloatBuffer
	 */
	public static FloatBuffer createVector3Buffer(FloatBuffer buf, int vertices) {
		if (buf != null && buf.limit() == 3 * vertices) {
			buf.rewind();
			return buf;
		}

		return createFloatBuffer(3 * vertices);
	}

	/**
	 * Sets the data contained in the given color into the FloatBuffer at the specified index.
	 *
	 * @param color
	 *            the data to insert
	 * @param buf
	 *            the buffer to insert into
	 * @param index
	 *            the postion to place the data; in terms of colors not floats
	 */
	/*
	 * public static void setInBuffer(ColorRGBA color, FloatBuffer buf, int index) { buf.position(index*4); buf.put(color.r); buf.put(color.g); buf.put(color.b); buf.put(color.a); }
	 */

	/**
	 * Sets the data contained in the given Vector3F into the FloatBuffer at the specified index.
	 *
	 * @param vector
	 *            the data to insert
	 * @param buf
	 *            the buffer to insert into
	 * @param index
	 *            the postion to place the data; in terms of vectors not floats
	 */
	public static void setInBuffer(Vector3f vector, FloatBuffer buf, int index) {
		if (buf == null) {
			return;
		}
		if (vector == null) {
			buf.put(index * 3, 0);
			buf.put((index * 3) + 1, 0);
			buf.put((index * 3) + 2, 0);
		}
		else {
			buf.put(index * 3, vector.x);
			buf.put((index * 3) + 1, vector.y);
			buf.put((index * 3) + 2, vector.z);
		}
	}

	/**
	 * Updates the values of the given vector from the specified buffer at the index provided.
	 *
	 * @param vector
	 *            the vector to set data on
	 * @param buf
	 *            the buffer to read from
	 * @param index
	 *            the position (in terms of vectors, not floats) to read from the buf
	 */
	public static void populateFromBuffer(Vector3f vector, FloatBuffer buf, int index) {
		vector.x = buf.get(index * 3);
		vector.y = buf.get(index * 3 + 1);
		vector.z = buf.get(index * 3 + 2);
	}

	/**
	 * Generates a Vector3f array from the given FloatBuffer.
	 *
	 * @param buff
	 *            the FloatBuffer to read from
	 * @return a newly generated array of Vector3f objects
	 */
	public static Vector3f[] getVector3Array(FloatBuffer buff) {
		buff.clear();
		Vector3f[] verts = new Vector3f[buff.limit() / 3];
		for (int x = 0; x < verts.length; x++) {
			Vector3f v = new Vector3f(buff.get(), buff.get(), buff.get());
			verts[x] = v;
		}
		return verts;
	}

	/**
	 * Copies a Vector3f from one position in the buffer to another. The index values are in terms of vector number (eg, vector number 0 is postions 0-2 in the FloatBuffer.)
	 *
	 * @param buf
	 *            the buffer to copy from/to
	 * @param fromPos
	 *            the index of the vector to copy
	 * @param toPos
	 *            the index to copy the vector to
	 */
	public static void copyInternalVector3(FloatBuffer buf, int fromPos, int toPos) {
		copyInternal(buf, fromPos * 3, toPos * 3, 3);
	}

	/**
	 * Normalize a Vector3f in-buffer.
	 *
	 * @param buf
	 *            the buffer to find the Vector3f within
	 * @param index
	 *            the position (in terms of vectors, not floats) of the vector to normalize
	 */
	public static void normalizeVector3(FloatBuffer buf, int index) {
		Vector3f tempVec3 = Vector3f.newInstance();
		populateFromBuffer(tempVec3, buf, index);
		tempVec3.normalizeLocal();
		setInBuffer(tempVec3, buf, index);
		Vector3f.recycle(tempVec3);
	}

	/**
	 * Add to a Vector3f in-buffer.
	 *
	 * @param toAdd
	 *            the vector to add from
	 * @param buf
	 *            the buffer to find the Vector3f within
	 * @param index
	 *            the position (in terms of vectors, not floats) of the vector to add to
	 */
	public static void addInBuffer(Vector3f toAdd, FloatBuffer buf, int index) {
		Vector3f tempVec3 = Vector3f.newInstance();
		populateFromBuffer(tempVec3, buf, index);
		tempVec3.addLocal(toAdd);
		setInBuffer(tempVec3, buf, index);
		Vector3f.recycle(tempVec3);
	}

	/**
	 * Multiply and store a Vector3f in-buffer.
	 *
	 * @param toMult
	 *            the vector to multiply against
	 * @param buf
	 *            the buffer to find the Vector3f within
	 * @param index
	 *            the position (in terms of vectors, not floats) of the vector to multiply
	 */
	public static void multInBuffer(Vector3f toMult, FloatBuffer buf, int index) {
		Vector3f tempVec3 = Vector3f.newInstance();
		populateFromBuffer(tempVec3, buf, index);
		tempVec3.multLocal(toMult);
		setInBuffer(tempVec3, buf, index);
		Vector3f.recycle(tempVec3);
	}

	/**
	 * Checks to see if the given Vector3f is equals to the data stored in the buffer at the given data index.
	 *
	 * @param check
	 *            the vector to check against - null will return false.
	 * @param buf
	 *            the buffer to compare data with
	 * @param index
	 *            the position (in terms of vectors, not floats) of the vector in the buffer to check against
	 * @return
	 */
	public static boolean equals(Vector3f check, FloatBuffer buf, int index) {
		Vector3f tempVec3 = Vector3f.newInstance();
		populateFromBuffer(tempVec3, buf, index);
		boolean eq = tempVec3.equals(check);
		Vector3f.recycle(tempVec3);
		return eq;
	}

	// // -- VECTOR2F METHODS -- ////

	/**
	 * Generate a new FloatBuffer using the given array of Vector2f objects. The FloatBuffer will be 2 * data.length long and contain the vector data as data[0].x, data[0].y, data[1].x... etc.
	 *
	 * @param data
	 *            array of Vector2f objects to place into a new FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(Vector2f... data) {
		if (data == null) {
			return null;
		}
		FloatBuffer buff = createFloatBuffer(2 * data.length);
		for (int x = 0; x < data.length; x++) {
			if (data[x] != null) {
				buff.put(data[x].x).put(data[x].y);
			}
			else {
				buff.put(0).put(0);
			}
		}
		buff.flip();
		return buff;
	}

	/**
	 * Create a new FloatBuffer of an appropriate size to hold the specified number of Vector2f object data.
	 *
	 * @param vertices
	 *            number of vertices that need to be held by the newly created buffer
	 * @return the requested new FloatBuffer
	 */
	public static FloatBuffer createVector2Buffer(int vertices) {
		FloatBuffer vBuff = createFloatBuffer(2 * vertices);
		return vBuff;
	}

	/**
	 * Create a new FloatBuffer of an appropriate size to hold the specified number of Vector2f object data only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param vertices
	 *            number of vertices that need to be held by the newly created buffer
	 * @return the requested new FloatBuffer
	 */
	public static FloatBuffer createVector2Buffer(FloatBuffer buf, int vertices) {
		if (buf != null && buf.limit() == 2 * vertices) {
			buf.rewind();
			return buf;
		}

		return createFloatBuffer(2 * vertices);
	}

	//// -- INT METHODS -- ////

	/**
	 * Generate a new IntBuffer using the given array of ints. The IntBuffer will be data.length long and contain the int data as data[0], data[1]... etc.
	 *
	 * @param data
	 *            array of ints to place into a new IntBuffer
	 */
	public static IntBuffer createIntBuffer(int... data) {
		if (data == null) {
			return null;
		}
		IntBuffer buff = createIntBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	/**
	 * Create a new int[] array and populate it with the given IntBuffer's contents.
	 *
	 * @param buff
	 *            the IntBuffer to read from
	 * @return a new int array populated from the IntBuffer
	 */
	public static int[] getIntArray(IntBuffer buff) {
		if (buff == null) {
			return null;
		}
		buff.clear();
		int[] inds = new int[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	/**
	 * Create a new float[] array and populate it with the given FloatBuffer's contents.
	 *
	 * @param buff
	 *            the FloatBuffer to read from
	 * @return a new float array populated from the FloatBuffer
	 */
	public static float[] getFloatArray(FloatBuffer buff) {
		if (buff == null) {
			return null;
		}
		buff.clear();
		float[] inds = new float[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	//// -- GENERAL DOUBLE ROUTINES -- ////

	/**
	 * Create a new DoubleBuffer of the specified size.
	 *
	 * @param size
	 *            required number of double to store.
	 * @return the new DoubleBuffer
	 */
	public static DoubleBuffer createDoubleBuffer(int size) {
		DoubleBuffer buf = ByteBuffer.allocateDirect(8 * size).order(ByteOrder.nativeOrder()).asDoubleBuffer();
		buf.clear();
		if (trackDirectMemory) {
			trackingHash.put(buf, ref);
		}
		return buf;
	}

	/**
	 * Create a new DoubleBuffer of an appropriate size to hold the specified number of doubles only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param size
	 *            number of doubles that need to be held by the newly created buffer
	 * @return the requested new DoubleBuffer
	 */
	public static DoubleBuffer createDoubleBuffer(DoubleBuffer buf, int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createDoubleBuffer(size);
		return buf;
	}

	/**
	 * Creates a new DoubleBuffer with the same contents as the given DoubleBuffer. The new DoubleBuffer is seperate from the old one and changes are not reflected across. If you want to reflect
	 * changes, consider using Buffer.duplicate().
	 *
	 * @param buf
	 *            the DoubleBuffer to copy
	 * @return the copy
	 */
	public static DoubleBuffer clone(DoubleBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		DoubleBuffer copy;
		if (buf.isDirect()) {
			copy = createDoubleBuffer(buf.limit());
		}
		else {
			copy = DoubleBuffer.allocate(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	//// -- GENERAL FLOAT ROUTINES -- ////

	/**
	 * Create a new FloatBuffer of the specified size.
	 *
	 * @param size
	 *            required number of floats to store.
	 * @return the new FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		FloatBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buf.clear();
		if (trackDirectMemory) {
			trackingHash.put(buf, ref);
		}
		return buf;
	}

	/**
	 * Copies floats from one position in the buffer to another.
	 *
	 * @param buf
	 *            the buffer to copy from/to
	 * @param fromPos
	 *            the starting point to copy from
	 * @param toPos
	 *            the starting point to copy to
	 * @param length
	 *            the number of floats to copy
	 */
	public static void copyInternal(FloatBuffer buf, int fromPos, int toPos, int length) {
		float[] data = new float[length];
		buf.position(fromPos);
		buf.get(data);
		buf.position(toPos);
		buf.put(data);
	}

	/**
	 * Creates a new FloatBuffer with the same contents as the given FloatBuffer. The new FloatBuffer is seperate from the old one and changes are not reflected across. If you want to reflect changes,
	 * consider using Buffer.duplicate().
	 *
	 * @param buf
	 *            the FloatBuffer to copy
	 * @return the copy
	 */
	public static FloatBuffer clone(FloatBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		FloatBuffer copy;
		if (buf.isDirect()) {
			copy = createFloatBuffer(buf.limit());
		}
		else {
			copy = FloatBuffer.allocate(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	//// -- GENERAL INT ROUTINES -- ////

	/**
	 * Create a new IntBuffer of the specified size.
	 *
	 * @param size
	 *            required number of ints to store.
	 * @return the new IntBuffer
	 */
	public static IntBuffer createIntBuffer(int size) {
		IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
		buf.clear();
		if (trackDirectMemory) {
			trackingHash.put(buf, ref);
		}
		return buf;
	}

	/**
	 * Create a new IntBuffer of an appropriate size to hold the specified number of ints only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param size
	 *            number of ints that need to be held by the newly created buffer
	 * @return the requested new IntBuffer
	 */
	public static IntBuffer createIntBuffer(IntBuffer buf, int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createIntBuffer(size);
		return buf;
	}

	/**
	 * Creates a new IntBuffer with the same contents as the given IntBuffer. The new IntBuffer is seperate from the old one and changes are not reflected across. If you want to reflect changes,
	 * consider using Buffer.duplicate().
	 *
	 * @param buf
	 *            the IntBuffer to copy
	 * @return the copy
	 */
	public static IntBuffer clone(IntBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		IntBuffer copy;
		if (buf.isDirect()) {
			copy = createIntBuffer(buf.limit());
		}
		else {
			copy = IntBuffer.allocate(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	//// -- GENERAL BYTE ROUTINES -- ////

	/**
	 * Create a new ByteBuffer of the specified size.
	 *
	 * @param size
	 *            required number of ints to store.
	 * @return the new IntBuffer
	 */
	public static ByteBuffer createByteBuffer(int size) {
		ByteBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
		buf.clear();
		if (trackDirectMemory) {
			trackingHash.put(buf, ref);
		}
		return buf;
	}

	/**
	 * Create a new ByteBuffer of an appropriate size to hold the specified number of ints only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param size
	 *            number of bytes that need to be held by the newly created buffer
	 * @return the requested new IntBuffer
	 */
	public static ByteBuffer createByteBuffer(ByteBuffer buf, int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createByteBuffer(size);
		return buf;
	}

	public static ByteBuffer createByteBuffer(byte... data) {
		ByteBuffer bb = createByteBuffer(data.length);
		bb.put(data);
		bb.flip();
		return bb;
	}

	public static ByteBuffer createByteBuffer(String data) {
		byte[] bytes = data.getBytes();
		ByteBuffer bb = createByteBuffer(bytes.length);
		bb.put(bytes);
		bb.flip();
		return bb;
	}

	/**
	 * Creates a new ByteBuffer with the same contents as the given ByteBuffer. The new ByteBuffer is seperate from the old one and changes are not reflected across. If you want to reflect changes,
	 * consider using Buffer.duplicate().
	 *
	 * @param buf
	 *            the ByteBuffer to copy
	 * @return the copy
	 */
	public static ByteBuffer clone(ByteBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		ByteBuffer copy;
		if (buf.isDirect()) {
			copy = createByteBuffer(buf.limit());
		}
		else {
			copy = ByteBuffer.allocate(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	//// -- GENERAL SHORT ROUTINES -- ////

	/**
	 * Create a new ShortBuffer of the specified size.
	 *
	 * @param size
	 *            required number of shorts to store.
	 * @return the new ShortBuffer
	 */
	public static ShortBuffer createShortBuffer(int size) {
		ShortBuffer buf = ByteBuffer.allocateDirect(2 * size).order(ByteOrder.nativeOrder()).asShortBuffer();
		buf.clear();
		if (trackDirectMemory) {
			trackingHash.put(buf, ref);
		}
		return buf;
	}

	/**
	 * Create a new ShortBuffer of an appropriate size to hold the specified number of shorts only if the given buffer if not already the right size.
	 *
	 * @param buf
	 *            the buffer to first check and rewind
	 * @param size
	 *            number of shorts that need to be held by the newly created buffer
	 * @return the requested new ShortBuffer
	 */
	public static ShortBuffer createShortBuffer(ShortBuffer buf, int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createShortBuffer(size);
		return buf;
	}

	public static ShortBuffer createShortBuffer(short... data) {
		if (data == null) {
			return null;
		}
		ShortBuffer buff = createShortBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	/**
	 * Creates a new ShortBuffer with the same contents as the given ShortBuffer. The new ShortBuffer is seperate from the old one and changes are not reflected across. If you want to reflect changes,
	 * consider using Buffer.duplicate().
	 *
	 * @param buf
	 *            the ShortBuffer to copy
	 * @return the copy
	 */
	public static ShortBuffer clone(ShortBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		ShortBuffer copy;
		if (buf.isDirect()) {
			copy = createShortBuffer(buf.limit());
		}
		else {
			copy = ShortBuffer.allocate(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	/**
	 * Ensures there is at least the <code>required</code> number of entries left after the current position of the buffer. If the buffer is too small a larger one is created and the old one copied to
	 * the new buffer.
	 *
	 * @param buffer
	 *            buffer that should be checked/copied (may be null)
	 * @param required
	 *            minimum number of elements that should be remaining in the returned buffer
	 * @return a buffer large enough to receive at least * * the <code>required</code> number of entries, same position as the input buffer, not null
	 */
	public static FloatBuffer ensureLargeEnough(FloatBuffer buffer, int required) {
		if (buffer == null || (buffer.remaining() < required)) {
			int position = (buffer != null ? buffer.position() : 0);
			FloatBuffer newVerts = createFloatBuffer(position + required);
			if (buffer != null) {
				buffer.rewind();
				newVerts.put(buffer);
				newVerts.position(position);
			}
			buffer = newVerts;
		}
		return buffer;
	}

	public static ShortBuffer ensureLargeEnough(ShortBuffer buffer, int required) {
		if (buffer == null || (buffer.remaining() < required)) {
			int position = (buffer != null ? buffer.position() : 0);
			ShortBuffer newVerts = createShortBuffer(position + required);
			if (buffer != null) {
				buffer.rewind();
				newVerts.put(buffer);
				newVerts.position(position);
			}
			buffer = newVerts;
		}
		return buffer;
	}

	public static ByteBuffer ensureLargeEnough(ByteBuffer buffer, int required) {
		if (buffer == null || (buffer.remaining() < required)) {
			int position = (buffer != null ? buffer.position() : 0);
			ByteBuffer newVerts = createByteBuffer(position + required);
			if (buffer != null) {
				buffer.rewind();
				newVerts.put(buffer);
				newVerts.position(position);
			}
			buffer = newVerts;
		}
		return buffer;
	}

	public static void printCurrentDirectMemory(StringBuilder store) {
		long totalHeld = 0;
		// make a new set to hold the keys to prevent concurrency issues.
		ArrayList<Buffer> bufs = new ArrayList<Buffer>(trackingHash.keySet());
		int fBufs = 0, bBufs = 0, iBufs = 0, sBufs = 0, dBufs = 0;
		int fBufsM = 0, bBufsM = 0, iBufsM = 0, sBufsM = 0, dBufsM = 0;
		for (Buffer b : bufs) {
			if (b instanceof ByteBuffer) {
				totalHeld += b.capacity();
				bBufsM += b.capacity();
				bBufs++;
			}
			else if (b instanceof FloatBuffer) {
				totalHeld += b.capacity() * 4;
				fBufsM += b.capacity() * 4;
				fBufs++;
			}
			else if (b instanceof IntBuffer) {
				totalHeld += b.capacity() * 4;
				iBufsM += b.capacity() * 4;
				iBufs++;
			}
			else if (b instanceof ShortBuffer) {
				totalHeld += b.capacity() * 2;
				sBufsM += b.capacity() * 2;
				sBufs++;
			}
			else if (b instanceof DoubleBuffer) {
				totalHeld += b.capacity() * 8;
				dBufsM += b.capacity() * 8;
				dBufs++;
			}
		}
		long heapMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		boolean printStout = store == null;
		if (store == null) {
			store = new StringBuilder();
		}
		store.append("Existing buffers: ").append(bufs.size()).append("\n");
		store.append("(b: ").append(bBufs).append("  f: ").append(fBufs).append("  i: ").append(iBufs).append("  s: ").append(sBufs).append("  d: ").append(dBufs).append(")").append("\n");
		store.append("Total   heap memory held: ").append(heapMem / 1024).append("kb\n");
		store.append("Total direct memory held: ").append(totalHeld / 1024).append("kb\n");
		store.append("(b: ").append(bBufsM / 1024).append("kb  f: ").append(fBufsM / 1024).append("kb  i: ").append(iBufsM / 1024).append("kb  s: ").append(sBufsM / 1024).append("kb  d: ").append(dBufsM / 1024).append("kb)").append("\n");
		if (printStout) {
			System.out.println(store.toString());
		}
	}
}
