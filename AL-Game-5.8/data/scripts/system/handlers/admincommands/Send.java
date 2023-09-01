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
package admincommands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET.PacketElementType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * This admin command is used for sending custom packets from server to client.
 * <p/>
 * Sends packets based on xml mappings in folder "./data/packets".<br />
 * Command details: "//send [1]<br />
 * * 1 - packet mappings name.<br />
 * * - 'demo' for file './data/packets/demo.xml'<br />
 * * - 'test' for file './data/packets/test.xml'<br />
 * * Reciever is a targetted by admin player. If target is 'null' or not a Player - sends to admin.<br />
 * <p/>
 * Created on: 14.07.2009 13:54:46
 *
 * @author Aquanox
 */
public class Send extends AdminCommand {

	public Send() {
		super("send");
		// init unmrshaller once.
		try {
			unmarshaller = JAXBContext.newInstance(Packets.class, Packet.class, Part.class).createUnmarshaller();
		}
		catch (Exception e) {
			throw new GameServerError("Failed to initialize unmarshaller.", e);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(Send.class);
	private static final File FOLDER = new File("./data/packets");
	private Unmarshaller unmarshaller;

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 1) {
			PacketSendUtility.sendMessage(admin, "Example: //send [file] ");
			return;
		}

		final String mappingName = params[0];
		final Player target = getTargetPlayer(admin);

		// logger.debug("Mapping: " + mappingName);
		// logger.debug("Target: " + target);
		File packetsData = new File(FOLDER, mappingName + ".xml");

		if (!packetsData.exists()) {
			PacketSendUtility.sendMessage(admin, "Mapping with name " + mappingName + " not found");
			return;
		}

		final Packets packetsTemplate;

		try {
			packetsTemplate = (Packets) unmarshaller.unmarshal(packetsData);
		}
		catch (JAXBException e) {
			logger.error("Unmarshalling error", e);
			return;
		}

		if (packetsTemplate.getPackets().isEmpty()) {
			PacketSendUtility.sendMessage(admin, "No packets to send.");
			return;
		}

		send(admin, target, packetsTemplate);
	}

	private void send(Player sender, final Player target, Packets packets) {
		final String senderObjectId = String.valueOf(sender.getObjectId());
		final String targetObjectId = String.valueOf(target.getObjectId());

		long delay = 0;
		for (final Packet packetTemplate : packets) {
			// logger.debug("Processing: " + packetTemplate);

			final SM_CUSTOM_PACKET packet = new SM_CUSTOM_PACKET(packetTemplate.getOpcode());

			for (Part part : packetTemplate.getParts()) {
				PacketElementType byCode = PacketElementType.getByCode(part.getType());

				String value = part.getValue();

				if (value.indexOf("${objectId}") != -1) {
					value = value.replace("${objectId}", targetObjectId);
				}
				if (value.indexOf("${senderObjectId}") != -1) {
					value = value.replace("${senderObjectId}", senderObjectId);
				}
				if (value.indexOf("${targetObjectId}") != -1) {
					value = value.replace("${targetObjectId}", targetObjectId);
				}

				if (part.getRepeatCount() == 1) // skip loop
				{
					packet.addElement(byCode, value);
				}
				else {
					for (int i = 0; i < part.getRepeatCount(); i++) {
						packet.addElement(byCode, value);
					}
				}
			}

			delay += packetTemplate.getDelay();

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// logger.debug("Sending: " + packetTemplate);
					PacketSendUtility.sendPacket(target, packet);
				}
			}, delay);

			delay += packets.getDelay();
		}
	}

	private Player getTargetPlayer(Player admin) {
		if (admin.getTarget() instanceof Player) {
			return (Player) admin.getTarget();
		}
		return admin;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "packets")
	private static class Packets implements Iterable<Packet> {

		@XmlElement(name = "packet")
		private List<Packet> packets = new ArrayList<Packet>();
		@XmlAttribute(name = "delay")
		private long delay = -1;

		public long getDelay() {
			return delay;
		}

		public List<Packet> getPackets() {
			return packets;
		}

		@SuppressWarnings("unused")
		public boolean add(Packet packet) {
			return packets.add(packet);
		}

		@Override
		public Iterator<Packet> iterator() {
			return packets.iterator();
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Packets");
			sb.append("{delay=").append(delay);
			sb.append(", packets=").append(packets);
			sb.append('}');
			return sb.toString();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "packet")
	private static class Packet {

		@XmlElement(name = "part")
		private Collection<Part> parts = new ArrayList<Part>();
		@XmlAttribute(name = "opcode")
		private String opcode = "-1";
		@XmlAttribute(name = "delay")
		private long delay = 0;

		public int getOpcode() {
			return Integer.decode(opcode);
		}

		public Collection<Part> getParts() {
			return parts;
		}

		public long getDelay() {
			return delay;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Packet");
			sb.append("{opcode=").append(opcode);
			sb.append(", parts=").append(parts);
			sb.append('}');
			return sb.toString();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "part")
	private static class Part {

		@XmlAttribute(name = "type", required = true)
		private String type = null;
		@XmlAttribute(name = "value", required = true)
		private String value = null;
		@XmlAttribute(name = "repeat", required = true)
		private int repeatCount = 1;

		public char getType() {
			return type.charAt(0);
		}

		public String getValue() {
			return value;
		}

		public int getRepeatCount() {
			return repeatCount;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Part");
			sb.append("{type='").append(type).append('\'');
			sb.append(", value='").append(value).append('\'');
			sb.append(", repeatCount=").append(repeatCount);
			sb.append('}');
			return sb.toString();
		}
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}
