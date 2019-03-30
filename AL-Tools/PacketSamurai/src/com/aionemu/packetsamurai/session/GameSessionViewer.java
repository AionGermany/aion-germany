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


/**
 * 
 */
package com.aionemu.packetsamurai.session;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ulysses R. Ribeiro
 *
 */
public class GameSessionViewer
{
	private List<DataPacket> _logPackets;
	private Session _session;
	
	public GameSessionViewer(Session s)
	{
		_session = s;
		_logPackets = new ArrayList<DataPacket>(s.getPackets().size());
		_logPackets.addAll(s.getPackets());
	}

	public List<DataPacket> getAllPackets()
	{
		return _logPackets;
	}

	public DataPacket getPacket(int index)
	{
		return _logPackets.get(index);
	}

	public Session getSession()
	{
		return _session;
	}
}
