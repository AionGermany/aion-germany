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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;

import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * <code>Node</code> defines an internal node of a scene graph. The internal node maintains a collection of children and handles merging said children into a single bound to allow for very fast
 * culling of multiple nodes. Node allows for any number of children to be attached.
 *
 * @author Mark Powell
 * @author Gregg Patton
 * @author Joshua Slack
 */
public class Node extends Spatial implements Cloneable {

	private static final Logger logger = Logger.getLogger(Node.class.getName());
	/**
	 * This node's children.
	 */
	protected ArrayList<Spatial> children = new ArrayList<Spatial>(1);
	protected short collisionFlags;

	/**
	 * Default constructor.
	 */
	public Node() {
	}

	/**
	 * Constructor instantiates a new <code>Node</code> with a default empty list for containing children.
	 *
	 * @param name
	 *            the name of the scene element. This is required for identification and comparision purposes.
	 */
	public Node(String name) {
		super(name);
		collisionFlags = CollisionIntention.ALL.getId();
	}

	/**
	 * <code>getQuantity</code> returns the number of children this node maintains.
	 *
	 * @return the number of children this node maintains.
	 */
	public int getQuantity() {
		return children.size();
	}

	/**
	 * <code>getTriangleCount</code> returns the number of triangles contained in all sub-branches of this node that contain geometry.
	 *
	 * @return the triangle count of this branch.
	 */
	@Override
	public int getTriangleCount() {
		int count = 0;
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				count += children.get(i).getTriangleCount();
			}
		}

		return count;
	}

	/**
	 * <code>getVertexCount</code> returns the number of vertices contained in all sub-branches of this node that contain geometry.
	 *
	 * @return the vertex count of this branch.
	 */
	@Override
	public int getVertexCount() {
		int count = 0;
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				count += children.get(i).getVertexCount();
			}
		}

		return count;
	}

	/**
	 * <code>attachChild</code> attaches a child to this node. This node becomes the child's parent. The current number of children maintained is returned. <br>
	 * If the child already had a parent it is detached from that former parent.
	 *
	 * @param child
	 *            the child to attach to this node.
	 * @return the number of children maintained by this node.
	 * @throws NullPointerException
	 *             If child is null.
	 */
	public int attachChild(Spatial child) {
		if (child == null) {
			throw new NullPointerException();
		}

		if (child.getParent() != this && child != this) {
			if (child.getParent() != null) {
				child.getParent().detachChild(child);
			}
			child.setParent(this);
			children.add(child);
		}

		return children.size();
	}

	/**
	 * <code>attachChildAt</code> attaches a child to this node at an index. This node becomes the child's parent. The current number of children maintained is returned. <br>
	 * If the child already had a parent it is detached from that former parent.
	 *
	 * @param child
	 *            the child to attach to this node.
	 * @return the number of children maintained by this node.
	 * @throws NullPointerException
	 *             if child is null.
	 */
	public int attachChildAt(Spatial child, int index) {
		if (child == null) {
			throw new NullPointerException();
		}

		if (child.getParent() != this && child != this) {
			if (child.getParent() != null) {
				child.getParent().detachChild(child);
			}
			child.setParent(this);
			children.add(index, child);
		}

		return children.size();
	}

	/**
	 * <code>detachChild</code> removes a given child from the node's list. This child will no longe be maintained.
	 *
	 * @param child
	 *            the child to remove.
	 * @return the index the child was at. -1 if the child was not in the list.
	 */
	public int detachChild(Spatial child) {
		if (child == null) {
			throw new NullPointerException();
		}

		if (child.getParent() == this) {
			int index = children.indexOf(child);
			if (index != -1) {
				detachChildAt(index);
			}
			return index;
		}

		return -1;
	}

	/**
	 * <code>detachChild</code> removes a given child from the node's list. This child will no longe be maintained. Only the first child with a matching name is removed.
	 *
	 * @param childName
	 *            the child to remove.
	 * @return the index the child was at. -1 if the child was not in the list.
	 */
	public int detachChildNamed(String childName) {
		if (childName == null) {
			throw new NullPointerException();
		}

		for (int x = 0, max = children.size(); x < max; x++) {
			Spatial child = children.get(x);
			if (childName.equals(child.getName())) {
				detachChildAt(x);
				return x;
			}
		}
		return -1;
	}

	/**
	 * <code>detachChildAt</code> removes a child at a given index. That child is returned for saving purposes.
	 *
	 * @param index
	 *            the index of the child to be removed.
	 * @return the child at the supplied index.
	 */
	public Spatial detachChildAt(int index) {
		Spatial child = children.remove(index);
		if (child != null) {
			child.setParent(null);
		}
		return child;
	}

	/**
	 * <code>detachAllChildren</code> removes all children attached to this node.
	 */
	public void detachAllChildren() {
		for (int i = children.size() - 1; i >= 0; i--) {
			detachChildAt(i);
		}
		logger.info("All children removed.");
	}

	public int getChildIndex(Spatial sp) {
		return children.indexOf(sp);
	}

	/**
	 * More efficient than e.g detaching and attaching as no updates are needed.
	 *
	 * @param index1
	 * @param index2
	 */
	public void swapChildren(int index1, int index2) {
		Spatial c2 = children.get(index2);
		Spatial c1 = children.remove(index1);
		children.add(index1, c2);
		children.remove(index2);
		children.add(index2, c1);
	}

	/**
	 * <code>getChild</code> returns a child at a given index.
	 *
	 * @param i
	 *            the index to retrieve the child from.
	 * @return the child at a specified index.
	 */
	public Spatial getChild(int i) {
		return children.get(i);
	}

	/**
	 * <code>getChild</code> returns the first child found with exactly the given name (case sensitive.)
	 *
	 * @param name
	 *            the name of the child to retrieve. If null, we'll return null.
	 * @return the child if found, or null.
	 */
	public Spatial getChild(String name) {
		if (name == null) {
			return null;
		}

		for (int x = 0, cSize = getQuantity(); x < cSize; x++) {
			Spatial child = children.get(x);
			if (name.equals(child.getName())) {
				return child;
			}
			else if (child instanceof Node) {
				Spatial out = ((Node) child).getChild(name);
				if (out != null) {
					return out;
				}
			}
		}
		return null;
	}

	/**
	 * determines if the provided Spatial is contained in the children list of this node.
	 *
	 * @param spat
	 *            the child object to look for.
	 * @return true if the object is contained, false otherwise.
	 */
	public boolean hasChild(Spatial spat) {
		if (children.contains(spat)) {
			return true;
		}

		for (int i = 0, max = getQuantity(); i < max; i++) {
			Spatial child = children.get(i);
			if (child instanceof Node && ((Node) child).hasChild(spat)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns all children to this node.
	 *
	 * @return a list containing all children to this node
	 */
	public List<Spatial> getChildren() {
		return children;
	}

	public void childChange(Geometry geometry, int index1, int index2) {
		// just pass to parent
		if (parent != null) {
			parent.childChange(geometry, index1, index2);
		}
	}

	@Override
	public int collideWith(Collidable other, CollisionResults results) {
		if ((getIntentions() & results.getIntentions()) == 0) {
			return 0;
		}

		if (other instanceof Ray) {
			if (worldBound == null || !worldBound.intersects(((Ray) other))) {
				return 0;
			}
		}

		int total = 0;
		for (int i = 0; i < children.size(); i++) {
			Spatial child = children.get(i);
			if (child instanceof Geometry) {

				// not used materialIds do not have collision intention for materials set
				// not all material meshes have physical collisions set
				// TODO: implement event mesh collisions
				if ((child.getIntentions() & results.getIntentions()) == 0 || (child.getIntentions() & CollisionIntention.EVENT.getId()) != 0) {
					continue;
				}
				if ((results.getIntentions() & CollisionIntention.MATERIAL.getId()) != 0 && child.getMaterialId() <= 0) {
					continue;
				}
			}
			total += child.collideWith(other, results);
			if (total > 0 && results.isOnlyFirst()) {
				break;
			}
		}
		return total;
	}

	/**
	 * Returns flat list of Spatials implementing the specified class AND with name matching the specified pattern.
	 * </P>
	 * <p/>
	 * Note that we are <i>matching</i> the pattern, therefore the pattern must match the entire pattern (i.e. it behaves as if it is sandwiched between "^" and "$"). You can set regex modes, like
	 * case insensitivity, by using the (?X) or (?X:Y) constructs.
	 * </P>
	 * <p/>
	 * By design, it is always safe to code loops like: <CODE><PRE>
	 * for (Spatial spatial : node.descendantMatches(AClass.class, "regex"))
	 * </PRE></CODE>
	 * </P>
	 * <p/>
	 * "Descendants" does not include self, per the definition of the word. To test for descendants AND self, you must do a <code>node.matches(aClass, aRegex)</code> +
	 * <code>node.descendantMatches(aClass, aRegex)</code>.
	 * <p/>
	 *
	 * @param spatialSubclass
	 *            Subclass which matching Spatials must implement. Null causes all Spatials to qualify.
	 * @param nameRegex
	 *            Regular expression to match Spatial name against. Null causes all Names to qualify.
	 * @return Non-null, but possibly 0-element, list of matching Spatials (also Instances extending Spatials).
	 * @see java.util.regex.Pattern
	 * @see Spatial#matches(Class<? extends Spatial>, String)
	 */
	@SuppressWarnings("unchecked")
	public <T extends Spatial> List<T> descendantMatches(Class<T> spatialSubclass, String nameRegex) {
		List<T> newList = new ArrayList<T>();
		if (getQuantity() < 1) {
			return newList;
		}
		for (int i = 0; i < children.size(); i++) {
			Spatial child = children.get(i);
			if (child.matches(spatialSubclass, nameRegex)) {
				newList.add((T) child);
			}
			if (child instanceof Node) {
				newList.addAll(((Node) child).descendantMatches(spatialSubclass, nameRegex));
			}
		}
		return newList;
	}

	/**
	 * Convenience wrapper.
	 *
	 * @see #descendantMatches(Class<? extends Spatial>, String)
	 */
	public <T extends Spatial> List<T> descendantMatches(Class<T> spatialSubclass) {
		return descendantMatches(spatialSubclass, null);
	}

	/**
	 * Convenience wrapper.
	 *
	 * @see #descendantMatches(Class<? extends Spatial>, String)
	 */
	public <T extends Spatial> List<T> descendantMatches(String nameRegex) {
		return descendantMatches(null, nameRegex);
	}

	@Override
	public void setModelBound(BoundingVolume modelBound) {
		if (children != null) {
			for (int i = 0, max = children.size(); i < max; i++) {
				children.get(i).setModelBound(modelBound != null ? modelBound.clone(null) : null);
			}
		}
	}

	@Override
	public void updateModelBound() {
		BoundingVolume resultBound = null;
		if (children != null) {
			for (int i = 0, max = children.size(); i < max; i++) {
				Spatial child = children.get(i);
				child.updateModelBound();
				if (resultBound != null) {
					// merge current world bound with child world bound
					resultBound.mergeLocal(child.getWorldBound());
				}
				else {
					// set world bound to first non-null child world bound
					if (child.getWorldBound() != null) {
						resultBound = child.getWorldBound().clone(this.worldBound);
					}
				}
			}
		}
		this.worldBound = resultBound;
	}

	/*
	 * (non-Javadoc)
	 * @see aionjHungary.geoEngine.scene.Spatial#setTransform(aionjHungary.geoEngine.math.Matrix3f, aionjHungary.geoEngine.math.Vector3f)
	 */
	@Override
	public void setTransform(Matrix3f rotation, Vector3f loc, float scale) {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				children.get(i).setTransform(rotation, loc, scale);
			}
		}
	}

	@Override
	public Node clone() throws CloneNotSupportedException {
		Node node = new Node(name);
		node.collisionFlags = collisionFlags;
		for (Spatial spatial : children) {
			if (spatial instanceof Geometry) {
				Geometry geom = new Geometry(spatial.getName(), ((Geometry) spatial).getMesh());
				node.attachChild(geom);
			}
			else if (spatial instanceof Node) {
				node.attachChild(((Node) (spatial)).clone());
			}
			else {
				new UnsupportedDataTypeException();
			}
		}
		return node;
	}

	@Override
	public short getCollisionFlags() {
		return collisionFlags;
	}

	@Override
	public void setCollisionFlags(short flags) {
		collisionFlags = flags;
	}
}
