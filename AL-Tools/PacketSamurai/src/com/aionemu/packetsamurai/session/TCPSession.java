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


package com.aionemu.packetsamurai.session;

import jpcap.packet.TCPPacket;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.session.packetbuffer.DefaultPacketBuffer;
import com.aionemu.packetsamurai.session.packetbuffer.PacketBuffer;

/**
 * @author Ulysses R. Ribeiro
 */
public class TCPSession extends Session {

	private PacketBuffer _csPacketBuffer;
	private PacketBuffer _scPacketBuffer;

	private TCPPacketBuffer _scTCPPacketBuffer = new TCPPacketBuffer();
	private TCPPacketBuffer _csTCPPacketBuffer = new TCPPacketBuffer();

	public TCPSession(long sessionId, Protocol protocol, String prefix, boolean crypted) {
		this(sessionId, protocol, prefix, crypted, true);
	}

	public TCPSession(long sessionId, Protocol protocol, String prefix, boolean crypted, boolean decrypt) {
		super(sessionId, protocol, prefix, crypted, decrypt);
		this.initBuffers();
	}

	private void initBuffers() {
		String pBufType = this.getProtocol().getPacketBufferType();
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.aionemu.packetsamurai.session.packetbuffer." + pBufType);
			if (clazz == null) {
				PacketSamurai.getUserInterface().log(
					"Wrong PacketBuffer type in " + this.getProtocol().getFileName() + " defaulting to DefaultPacketBuffer");
				_csPacketBuffer = new DefaultPacketBuffer();
				_scPacketBuffer = new DefaultPacketBuffer();
				return;
			}
			_csPacketBuffer = (PacketBuffer) clazz.newInstance();
			_scPacketBuffer = (PacketBuffer) clazz.newInstance();
		}
		catch (Exception e) {
			PacketSamurai.getUserInterface().log(
				"Wrong PacketBuffer type in " + this.getProtocol().getFileName() + " defaulting to DefaultPacketBuffer");
			_csPacketBuffer = new DefaultPacketBuffer();
			_scPacketBuffer = new DefaultPacketBuffer();
		}
		_csPacketBuffer.setProtocol(this.getProtocol());
		_scPacketBuffer.setProtocol(this.getProtocol());
	}

	public synchronized void receivePacket(TCPPacket p, boolean fromServer, long time) {
		int size;
		if (fromServer) {
			// sequence packets from server
			if (p.data.length > 0) {
				_scTCPPacketBuffer.add(p);
			}

			// ack on the other side
			_csTCPPacketBuffer.ack(p);
			// process pos ack
			for (TCPPacket packet : _csTCPPacketBuffer.getSequencedPackets()) {
				_csPacketBuffer.addData(packet.data);
				while ((size = _csPacketBuffer.nextAvaliablePacket()) > 0) {
					byte[] header = new byte[2];
					byte[] packetData = new byte[size];
					_csPacketBuffer.getNextPacket(header, packetData);
					// System.out.println("TCPSession : receivePacket : New Full Packet (ClientPacket) size="+size+" time="+time);
					this.addPacket(packetData, !fromServer, time);
				}
			}
			_csTCPPacketBuffer.flush();
		}
		else // from client
		{
			// sequence packets from server
			if (p.data.length > 0) {
				_csTCPPacketBuffer.add(p);
			}
			@SuppressWarnings("unused")
			int countA = 0, countB = 0;
			// ack on the other side
			_scTCPPacketBuffer.ack(p);
			// process pos ack
			for (TCPPacket packet : _scTCPPacketBuffer.getSequencedPackets()) {
				if (packet.data.length > 0) {
					_scPacketBuffer.addData(packet.data);
					countB = 0;
					while ((size = _scPacketBuffer.nextAvaliablePacket()) > 0) {
						byte[] header = new byte[2];
						byte[] packetData = new byte[size];
						_scPacketBuffer.getNextPacket(header, packetData);
						// System.out.println("TCPSession : receivePacket : New Full Packet (ServerPacket) size="+size+" time="+time);
						this.addPacket(packetData, !fromServer, time);
					}
				}

				// Connection end?
				if (packet.fin || packet.rst) {
					PacketSamurai.getUserInterface().log("PACKETS:: " + countA);
					PacketSamurai.getUserInterface().log(
						"[S -> C] TCP Sequencing Buffer Report: Pending: " + _scTCPPacketBuffer.getPendingSequencePackets());
					PacketSamurai.getUserInterface().log(
						"[S -> C] TCP Data Buffer Report: Pending: " + _scPacketBuffer.nextAvaliablePacket());
					PacketSamurai.getUserInterface().log(
						"[C -> S] TCP Sequencing Buffer Report: Pending: " + _csTCPPacketBuffer.getPendingSequencePackets());
					PacketSamurai.getUserInterface().log(
						"[C -> S] TCP Data Buffer Report: Pending: " + _csPacketBuffer.nextAvaliablePacket());
					PacketSamurai.getUserInterface().log("Connection ended. (FIN: " + packet.fin + " - RST: " + packet.rst + ")");
					this.saveSession();
					this.close();
					return;
				}

			}
			_scTCPPacketBuffer.flush();

		}
	}

	public void receivePacket(TCPPacket p, boolean fromServer) {
		this.receivePacket(p, fromServer, (p.sec * 1000) + (p.usec / 1000));
	}

	public void close() {
		GameSessionTable.getInstance().removeGameSession(this.getSessionId());
	}
}
