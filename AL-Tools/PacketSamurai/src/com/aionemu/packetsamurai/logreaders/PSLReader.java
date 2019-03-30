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


package com.aionemu.packetsamurai.logreaders;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import javolution.util.FastList;

import com.aionemu.packetsamurai.PacketHandler;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.Session;
import com.aionemu.packetsamurai.utils.collector.Collector;

/**
 * @author kami
 * 
 */
public class PSLReader extends AbstractLogReader
{
	private RandomAccessFile logFile;
	private Session session;
	private int totalPacketCount;
	private FastList<DataPacket> packets;
	private int headerSize;
	private final ByteBuffer bb;
	private final FileChannel fc;

	protected PacketHandler packetHandler;

	public PSLReader(String filename) throws IOException
	{
		super(filename);
		logFile = new RandomAccessFile(filename, "r");
		bb = ByteBuffer.allocate((int)logFile.length());
		fc = logFile.getChannel();
		fc.read(bb);
		packets = new FastList<DataPacket>();
	}

	@Override
	protected String getAditionalName()
	{
		return "psl";
	}

	@Override
	public boolean parseHeader()
	{
		try
		{
			bb.flip();
			headerSize = bb.remaining();
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.get(); // logVersion
			totalPacketCount = bb.getInt();
			bb.get();
			bb.getShort();
			short port = bb.getShort();
			Inet4Address cilentIp = (Inet4Address) Inet4Address.getByAddress(readBytes(bb, 4));
			Inet4Address serverIp = (Inet4Address) Inet4Address.getByAddress(readBytes(bb, 4));
			readString(bb); // protocolName
			String sessionComments = readString(bb);
			String serverType = readString(bb);
			long analyserBitSet = bb.getLong();
			long sessionID = bb.getLong();
			boolean isDecrypted = (bb.get() & 0xFF) == 0x00;
			headerSize -= bb.remaining();
			session = new Session(sessionID, AbstractLogReader.getLogProtocolByPort(port), "live", !isDecrypted);
			session.setAnalyserBitSet(analyserBitSet);
			session.setClientIp(cilentIp);
			session.setComments(sessionComments);
			session.setServerIp(serverIp);
			session.setServerType(serverType);
			session.setShown(true);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean parsePackets(boolean isReload) throws IOException
	{
		short packetSize;
		long timestamp;
		byte[] data;
		packetDirection pd;
		DataPacket packet;

		bb.rewind();
		bb.position(headerSize);
		for (int i = 0; i < totalPacketCount; i++) {
			pd = (bb.get() & 0xFF) == 0x01 ? packetDirection.serverPacket : packetDirection.clientPacket;
			packetSize = bb.getShort();
			timestamp = bb.getLong();
			data = readBytes(bb, packetSize - 2);
			packet = new DataPacket(session.getNewPacketNumber(), data, pd, timestamp, session.getProtocol(), true);
			if (Collector.isEnabled()) {
				session.getCollector().parse(packet, pd);
			}
			packets.add(packet);
		}
		session.setPackets(packets);
		((Main) PacketSamurai.getUserInterface()).showSession(session, true, isReload, super.getFileName());
		return true;
	}

	@Override
	protected void closeFile() throws IOException
	{
		bb.clear();
		fc.close();
		logFile.close();
	}

	@Override
	protected String getFileExtension()
	{
		return "psl";
	}

	@Override
	public boolean supportsHeaderReading()
	{
		return false;
	}

	private String readString(ByteBuffer buf)
	{
		StringBuffer sb = new StringBuffer();
		char ch;
		try
		{
			while ((ch = buf.getChar()) != '\000')
				sb.append(ch);
		}
		catch (Exception e)
		{}
		return sb.toString();
	}

	public final byte[] readBytes(ByteBuffer buf, int length)
	{
		byte[] result = new byte[length];
		try
		{
			buf.get(result);
		}
		catch (Exception e)
		{}
		return result;
	}
}
