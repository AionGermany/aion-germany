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

package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * This interface defines a factory for connection implementations.<br>
 * It is used by the class {@link com.aionemu.commons.network.Acceptor Acceptor}
 * to create actual connection implementations.<br>
 * 
 * @author -Nemesiss-
 * @see com.aionemu.commons.network.Acceptor
 */
public interface ConnectionFactory {

	/**
	 * Create a new {@link com.aionemu.commons.network.AConnection AConnection}
	 * instance.<br>
	 * 
	 * @param socket
	 *            that new {@link com.aionemu.commons.network.AConnection
	 *            AConnection} instance will represent.<br>
	 * @param dispatcher
	 *            to wich new connection will be registered.<br>
	 * @return a new instance of {@link com.aionemu.commons.network.AConnection
	 *         AConnection}<br>
	 * @throws IOException
	 * @see com.aionemu.commons.network.AConnection
	 * @see com.aionemu.commons.network.Dispatcher
	 */
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException;
}
