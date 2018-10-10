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

package com.aionemu.commons.scripting.url;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.aionemu.commons.scripting.ScriptClassLoader;

/**
 * This class represents URL Stream handler that accepts
 * {@value #HANDLER_PROTOCOL} protocol
 * 
 * @author SoulKeeper
 */
public class VirtualClassURLStreamHandler extends URLStreamHandler {

	/**
	 * Script Handler protocol for classes compiled from source
	 */
	public static final String HANDLER_PROTOCOL = "aescript://";

	/**
	 * Script class loader that loaded those classes
	 */
	private final ScriptClassLoader cl;

	/**
	 * Creates new instance of url stream handler with given classloader
	 * 
	 * @param cl
	 *            ScriptClassLoaderImpl that was used to load compiled class
	 */
	public VirtualClassURLStreamHandler(ScriptClassLoader cl) {
		this.cl = cl;
	}

	/**
	 * Opens new URL connection for URL
	 * 
	 * @param u
	 *            url
	 * @return Opened connection
	 * @throws IOException
	 *             never thrown
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return new VirtualClassURLConnection(u, cl);
	}
}
