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

import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * <code>Spatial</code> defines the base class for scene graph nodes. It maintains a link to a parent, it's local transforms and the world's transforms. All other nodes, such as <code>Node</code> and
 * <code>Geometry</code> are subclasses of <code>Spatial</code>.
 *
 * @author Mark Powell
 * @author Joshua Slack
 * @author Rolandas - added materials
 * @version $Revision: 4075 $, $Data$
 */
public abstract class Spatial implements Collidable, Cloneable {

	public enum CullHint {

		/**
		 * Do whatever our parent does. If no parent, we'll default to dynamic.
		 */
		Inherit,
		/**
		 * Do not draw if we are not at least partially within the view frustum of the renderer's camera.
		 */
		Dynamic,
		/**
		 * Always cull this from view.
		 */
		Always,
		/**
		 * Never cull this from view. Note that we will still get culled if our parent is culled.
		 */
		Never;
	}

	/**
	 * Spatial's bounding volume relative to the world.
	 */
	protected BoundingVolume worldBound;
	/**
	 * This spatial's name.
	 */
	protected String name;
	/**
	 * Spatial's parent, or null if it has none.
	 */
	protected transient Node parent;

	/**
	 * Default Constructor.
	 */
	public Spatial() {
	}

	/**
	 * Constructor instantiates a new <code>Spatial</code> object setting the rotation, translation and scale value to defaults.
	 *
	 * @param name
	 *            the name of the scene element. This is required for identification and comparision purposes.
	 */
	public Spatial(String name) {
		this();
		if (name != null) {
			this.name = name.intern();
		}
	}

	/**
	 * Sets the name of this spatial.
	 *
	 * @param name
	 *            The spatial's new name.
	 */
	public void setName(String name) {
		if (name != null) {
			this.name = name.intern();
		}
	}

	/**
	 * Returns the name of this spatial.
	 *
	 * @return This spatial's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <code>getParent</code> retrieve's this node's parent. If the parent is null this is the root node.
	 *
	 * @return the parent of this node.
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Called by {@link Node#attachChild(Spatial)} and {@link Node#detachChild(Spatial)} - don't call directly. <code>setParent</code> sets the parent of this node.
	 *
	 * @param parent
	 *            the parent of this node.
	 */
	protected void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * <code>removeFromParent</code> removes this Spatial from it's parent.
	 *
	 * @return true if it has a parent and performed the remove.
	 */
	public boolean removeFromParent() {
		if (parent != null) {
			parent.detachChild(this);
			return true;
		}
		return false;
	}

	/**
	 * determines if the provided Node is the parent, or parent's parent, etc. of this Spatial.
	 *
	 * @param ancestor
	 *            the ancestor object to look for.
	 * @return true if the ancestor is found, false otherwise.
	 */
	public boolean hasAncestor(Node ancestor) {
		if (parent == null) {
			return false;
		}
		else if (parent.equals(ancestor)) {
			return true;
		}
		else {
			return parent.hasAncestor(ancestor);
		}
	}

	/**
	 * <code>updateModelBound</code> recalculates the bounding object for this Spatial.
	 */
	public abstract void updateModelBound();

	/**
	 * <code>setModelBound</code> sets the bounding object for this Spatial.
	 *
	 * @param modelBound
	 *            the bounding object for this spatial.
	 */
	public abstract void setModelBound(BoundingVolume modelBound);

	/**
	 * @return The sum of all verticies under this Spatial.
	 */
	public abstract int getVertexCount();

	/**
	 * @return The sum of all triangles under this Spatial.
	 */
	public abstract int getTriangleCount();

	public byte getMaterialId() {
		return (byte) (getCollisionFlags() & 0xFF);
	}

	public byte getIntentions() {
		return (byte) (getCollisionFlags() >> 8);
	}

	public abstract short getCollisionFlags();

	public abstract void setCollisionFlags(short flags);

	/**
	 * Note that we are <i>matching</i> the pattern, therefore the pattern must match the entire pattern (i.e. it behaves as if it is sandwiched between "^" and "$"). You can set regex modes, like
	 * case insensitivity, by using the (?X) or (?X:Y) constructs.
	 *
	 * @param spatialSubclass
	 *            Subclass which this must implement. Null causes all Spatials to qualify.
	 * @param nameRegex
	 *            Regular expression to match this name against. Null causes all Names to qualify.
	 * @return true if this implements the specified class and this's name matches the specified pattern.
	 * @see java.util.regex.Pattern
	 */
	public boolean matches(Class<? extends Spatial> spatialSubclass, String nameRegex) {
		if (spatialSubclass != null && !spatialSubclass.isInstance(this)) {
			return false;
		}

		if (nameRegex != null && (name == null || !name.matches(nameRegex))) {
			return false;
		}

		return true;
	}

	/**
	 * <code>getWorldBound</code> retrieves the world bound at this node level.
	 *
	 * @return the world bound at this level.
	 */
	public BoundingVolume getWorldBound() {
		return worldBound;
	}

	/**
	 * Returns the Spatial's name followed by the class of the spatial <br>
	 * Example: "MyNode (com.jme3.scene.Spatial)
	 *
	 * @return Spatial's name followed by the class of the Spatial
	 */
	@Override
	public String toString() {
		return name + " (" + this.getClass().getSimpleName() + ") use " + CollisionIntention.toString(getIntentions());
	}

	public abstract void setTransform(Matrix3f rotation, Vector3f loc, float scale);

	@Override
	public Spatial clone() throws CloneNotSupportedException {
		return (Spatial) super.clone();
	}
}
