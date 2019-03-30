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


/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.packetsamurai.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.net.Inet4Address;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.gui.images.IconsTable;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.protocol.PacketId.IdPart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.GameSessionViewer;
import com.aionemu.packetsamurai.session.Session;

/**
 * @author Ulysses R. Ribeiro
 */
@SuppressWarnings({ "serial", "unused" })
public class ViewPane extends JPanel {

	private GridBagLayout _layout = new GridBagLayout();

	// Details
	private SessionDetailPanel _detailsPanel;

	// Packet Table
	private PacketTableModel _packetTableModel;
	private JTable _packetTable;
	private int _currentSelectedPacket = -1;

	// Hex Dump
	private JTextPane _hexDumpArea;
	private DefaultStyledDocument _hexStyledDoc;

	// Format Entry
	private JTextField _packetFormat;

	// Packet View Table
	private PacketViewTableModel _packetViewTableModel;
	private JXTreeTable _packetViewTable;
	private DataPartNode _currentSelectedPart = null;

	private GameSessionViewer _gameSessionViewer;
	private FilterDlg _filterDlg;
	private String _fileName;
	private JCheckBox _autoScroll;

	public ViewPane(GameSessionViewer gsv) {
		_gameSessionViewer = gsv;

		this.setLayout(_layout);

		GridBagConstraints cons = new GridBagConstraints();
		cons.insets = new Insets(5, 5, 5, 5);
		cons.weightx = 0.5;
		cons.gridy = 0;
		cons.gridheight = 5;
		cons.weighty = 0.95;
		cons.fill = GridBagConstraints.BOTH;
		_packetTableModel = new PacketTableModel();
		_packetTable = new JTable(_packetTableModel);
		_packetTable.addMouseListener(new PacketTableMouseListener());
		_packetTable.setDefaultRenderer(Object.class, new PacketTableRenderer(_packetTableModel));
		_packetTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_packetTable.getSelectionModel().addListSelectionListener(new PacketSelectionListener());
		_packetTable.getColumnModel().getColumn(0).setMaxWidth(35);
		_packetTable.getColumnModel().getColumn(1).setMaxWidth(50);
		_packetTable.getColumnModel().getColumn(2).setMaxWidth(90);
		_packetTable.getColumnModel().getColumn(3).setMaxWidth(45);
		_packetTable.scrollRectToVisible(_packetTable.getCellRect(_packetTable.getRowCount()-1, 0, true));
		JScrollPane scrollPane = new JScrollPane(_packetTable);
		
		// Autoscroll
			scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
				public void adjustmentValueChanged(AdjustmentEvent e) {  
					if(_autoScroll.isSelected())
						e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
				}
			});
				
			this.add(scrollPane, cons);
		
			cons.fill = GridBagConstraints.BOTH;
			cons.weightx = 0.5;
			cons.weighty = 0.5;
			cons.gridx = 1;
			cons.gridy = 1;
			cons.gridheight = 1;
			_hexDumpArea = new JTextPane();
			_hexDumpArea.setPreferredSize(new Dimension(100, 317));
			_hexStyledDoc = (DefaultStyledDocument) _hexDumpArea.getStyledDocument();
			addStylesToHexDump(_hexStyledDoc);
			JScrollPane hexDumpScrollPane = new JScrollPane(_hexDumpArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.add(hexDumpScrollPane, cons);

			JPanel smallPane = new JPanel();
			smallPane.setLayout(_layout);
			cons.fill = GridBagConstraints.NONE;
			cons.weightx = 0.05;
			cons.weighty = 0.1;
			cons.gridx = 0;
			cons.gridy = 2;
			cons.gridheight = 1;
			_autoScroll = new JCheckBox("Auto Scroll");
			
			_autoScroll.setMnemonic(KeyEvent.VK_A); 
			_autoScroll.setSelected(true);
			cons.anchor = GridBagConstraints.WEST;

			_packetFormat = new JTextField();
			_packetFormat.setEditable(false);
			cons.anchor = GridBagConstraints.WEST;
			smallPane.add(_autoScroll, cons);
			cons.gridx = 1;
			cons.weightx = 0.95;
			cons.fill = GridBagConstraints.HORIZONTAL;
			smallPane.add(_packetFormat, cons);
			cons.weightx = 0.5;
			this.add(smallPane, cons);

			
			cons.fill = GridBagConstraints.BOTH;
			cons.weightx = 0.5;
			cons.weighty = 0.4;
			cons.gridx = 1;
			cons.gridy = 4;
			cons.gridheight = 1;
			_packetViewTableModel = new PacketViewTableModel(null);
			_packetViewTable = new JXTreeTable(_packetViewTableModel);
			_packetViewTable.setSelectionModel(new DefaultListSelectionModel());
			_packetViewTable.getSelectionModel().addListSelectionListener(new PartSelectionListener(_packetViewTable));
			_packetViewTable.setDefaultRenderer(Object.class, new IconTableRenderer());
			_packetViewTable.addMouseListener(new JTableButtonMouseListener(_packetViewTable));
			_packetViewTable.setTreeCellRenderer(new PacketViewTreeRenderer());
			JScrollPane packetViewScrollPane = new JScrollPane(_packetViewTable);
			this.add(packetViewScrollPane, cons);

			cons.fill = GridBagConstraints.BOTH;
			cons.anchor = GridBagConstraints.NORTH;
			cons.gridx = 1;
			cons.gridy = 0;
			cons.weighty = 0.1;
			cons.gridwidth = 1;
			_detailsPanel = new SessionDetailPanel(_gameSessionViewer.getSession());
			this.add(_detailsPanel, cons);

			this.displaySession();
	}

	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	public String getFileName() {
		return this._fileName;
	}

	public void setSelectedPacket(int startIndex, int endIndex) {
		this.getPacketTable().setAutoscrolls(true);
		this.getPacketTable().getSelectionModel().setSelectionInterval(startIndex, endIndex);
		this.getPacketTable().scrollRectToVisible(this.getPacketTable().getCellRect(startIndex, 0, true));
	}

	private void addStylesToHexDump(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style base = doc.addStyle("base", def);
		StyleConstants.setFontFamily(base, "Monospaced");

		Style regular = doc.addStyle("regular", base);
		//StyleConstants.setUnderline(regular , true);

		Style s = doc.addStyle("d", regular);
		StyleConstants.setBackground(s, new Color(72, 164, 255));

		s = doc.addStyle("dbs", regular);
		StyleConstants.setBackground(s, new Color(72, 164, 255));

		s = doc.addStyle("Q", regular);
		StyleConstants.setBackground(s, new Color(255, 255, 128));

		s = doc.addStyle("h", regular);
		StyleConstants.setBackground(s, Color.ORANGE);

		s = doc.addStyle("hbs", regular);
		StyleConstants.setBackground(s, Color.ORANGE);

		s = doc.addStyle("s", regular);
		StyleConstants.setBackground(s, new Color(156, 220, 156));

		s = doc.addStyle("S", regular);
		StyleConstants.setBackground(s, new Color(156, 220, 156));

		s = doc.addStyle("c", regular);
		StyleConstants.setBackground(s, Color.PINK);

		s = doc.addStyle("cbs", regular);
		StyleConstants.setBackground(s, Color.PINK);

		s = doc.addStyle("f", regular);
		StyleConstants.setBackground(s, Color.LIGHT_GRAY);

		Color bxColor = new Color(255, 234, 213);
		s = doc.addStyle("b", regular);
		StyleConstants.setBackground(s, bxColor);
		s = doc.addStyle("x", regular);
		StyleConstants.setBackground(s, bxColor);

		s = doc.addStyle("op", regular);
		StyleConstants.setBackground(s, Color.YELLOW);

		s = doc.addStyle("selected", regular);
		StyleConstants.setBackground(s, Color.BLUE);

		s = doc.addStyle("chk", regular);
		StyleConstants.setBackground(s, Color.GREEN);
	}

	public void addStyledText(String text, String style) {
		this.addStyledText(text, style, false);
	}

	public void addStyledText(String text, String style, boolean index) {
		Style s = _hexStyledDoc.getStyle(style);
		if (s == null) {
			PacketSamurai.getUserInterface().log("WARNING: Missing style for partType: " + style);
			style = "base";
		}

		try {
			_hexStyledDoc.insertString(_hexStyledDoc.getLength(), text, _hexStyledDoc.getStyle(style));
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void highlightSelectedPart(DataPartNode node) {
		Style style = _hexStyledDoc.getStyle("selected");
		StyleConstants.setForeground(style, Color.WHITE);
		
		int offset = node.getOffset();
		offset += offset/(16*3)*19;
		try {
			String text = _hexStyledDoc.getText(offset, node.getLength());
			_hexStyledDoc.replace(offset, node.getLength(), text, style);
			_hexDumpArea.setCaretPosition(offset);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public FilterDlg getFilterDialog() {
		if (_filterDlg == null)
			_filterDlg = new FilterDlg(((Main) PacketSamurai.getUserInterface()).getMainFrame(), this);
		return _filterDlg;
	}

	public void displaySession() {

		int size = this.getGameSessionViewer().getAllPackets().size();
		DataPacket gp;

		System.out.println("Size of Packet list: " + size);
		PacketTableModel packetTableModel = this.getPacketTableModel();
		packetTableModel.reinit(size);
		if (size > 0) {
			long startTime = this.getGameSessionViewer().getAllPackets().get(0).getTimeStamp();
			long teste = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				gp = this.getGameSessionViewer().getPacket(i);

				// filtering
				if (this.getFilterDialog().isFilterEnabledFor(gp))
					continue;

				packetTableModel.addRow(gp, startTime);
			}
			PacketSamurai.getUserInterface().log("done in: " + ((System.currentTimeMillis() - teste)) + " ms");
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				ViewPane.this.getPacketTable().updateUI();
			}

		});

	}

	public JTable getPacketTable() {
		// OMG that's hacky! here to update packet count as we usualy do a getPacketTable() to update the ui after adding
		// packets
		_detailsPanel.setPacketCount(_packetTableModel.getRowCount());
		return _packetTable;
	}

	public PacketTableModel getPacketTableModel() {
		return _packetTableModel;
	}

	public JTable getPacketViewTable() {
		return _packetViewTable;
	}

	public PacketViewTableModel getPacketViewTableModel() {
		return _packetViewTableModel;
	}

	public GameSessionViewer getGameSessionViewer() {
		return _gameSessionViewer;
	}

	public void updateCurrentPacket() {
		this.updateCurrentPacket(false);
	}

	public void updateCurrentPacket(boolean forced) {
		if (!forced && _currentSelectedPacket == _packetTable.getSelectedRow())
			return;
		/*
		 * try { DataStructure gp = _gameSessionViewer.getPacket(_currentSelectedPacket); if(gp!= null) gp.setViewed(false);
		 * } catch (IndexOutOfBoundsException e) { }
		 */
		_currentSelectedPacket = _packetTable.getSelectedRow();

		// reset the selected part as we selected another packet
		_currentSelectedPart = null;

		DataPacket gp = _packetTableModel.getPacket(_currentSelectedPacket);
		// gp.setViewed(true);
		// System.out.println("Selected row/packet: "+_table.getSelectedRow());

		_hexDumpArea.setText("");
		if (gp.getPacketFormat() != null && gp.isValid()) {
			_packetFormat.setText(gp.getPacketFormat().getPartsStr());

			// colorfull hex dump :P
			// opcode
			int offset = 0;
			if (gp.getPacketId() != null) {
				for (IdPart part : gp.getPacketId().getParts()) {
					int bytes = part.getType().getTypeByteNumber();
					for (int i2 = 0; i2 < bytes; i2++) {
						addStyledText(Util.zeropad(Long.toHexString(part.getValue() << (8 * i2) & 0xff), 2).toUpperCase(), "op");
						if (i2 < bytes - 1)
							addStyledText(" ", "op");

						offset++;
					}
					addStyledText(" ", "base");
				}
			}

			// chksum
			if (gp.getProtocol().getChecksumSize() > 0) {
				int size = gp.getProtocol().getChecksumSize() + offset;
				for (int i = offset; i < size; i++) {
					addStyledText(Util.zeropad(Integer.toHexString(gp.getIdData()[i] & 0xff), 2).toUpperCase(), "chk");
					if (i < size - 1)
						addStyledText(" ", "chk");
				}
				addStyledText(" ", "base");
			}

			// parsed packet parts
			for (ValuePart part : gp.getValuePartList()) {
				addStyledText(Util.rawHexDump(part.getBytes()), part.getType().getName(), true);
				addStyledText(" ", "base");
			}

			// unparsed data (if any)
			addStyledText(Util.rawHexDump(gp.getUnparsedData()), "base");

			this.addLineBreaksToHexDump(gp.getIdData(), gp.getByteBuffer().array());

			int opSize = 0;
			for (PartType pt : gp.getPacketFormat().getIdParts()) {
				opSize += pt.getTypeByteNumber() * 3;
			}
			DataPartNode root = new DataPartNode(gp.getRootNode(), opSize + gp.getProtocol().getChecksumSize() * 3);
			_packetViewTableModel.setRoot(root);
			_packetViewTable.revalidate();
			_packetViewTable.expandAll();
		}
		else {
			// clear the format
			_packetFormat.setText("");

			// dump everything
			_hexDumpArea.setText("");

			// if we know the opcode but the format was invalid still show the opcode
			if (gp.getFormat() != null) {
				for (IdPart part : gp.getPacketId().getParts()) {
					int bytes = part.getType().getTypeByteNumber();
					for (int i2 = 0; i2 < bytes; i2++) {
						addStyledText(Util.zeropad(Long.toHexString(part.getValue() << (8 * i2) & 0xff), 2).toUpperCase(), "op");
						if (i2 < bytes - 1)
							addStyledText(" ", "op");
					}
					addStyledText(" ", "base");
				}
			}

			ViewPane.this.addStyledText(Util.rawHexDump(gp.getData()), "base");

			this.addLineBreaksToHexDump(gp.getIdData(), gp.getByteBuffer().array());

			// no part for u
			_packetViewTableModel.setRoot(null);
		}
	}

	private void addLineBreaksToHexDump(byte[] id, byte[] data) {
		// add linefeeds to the dump
		int lnCount = _hexDumpArea.getText().length() / 48;
		int rest = _hexDumpArea.getText().length() % 48;
		for (int i = 1; i <= lnCount; i++) {
			int pos = i * 67 - 20;
			try {
				int idx = i - 1;
				String ansci = idx == 0 ? (Util.toAnsci(id, 0, id.length) + Util.toAnsci(data, 0, 16 - id.length)) : Util
					.toAnsci(data, idx * 16 - id.length, idx * 16 + 16 - id.length);
				_hexStyledDoc.replace(pos, 1, "   " + ansci + "\n", _hexStyledDoc.getStyle("base"));
				// _hexStyledDoc.remove(pos,1);
				// _hexStyledDoc.insertString(pos, "\n", _hexStyledDoc.getStyle("base"));
			}
			catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// rest
		if (rest != 0) {
			try {
				int pos = lnCount * 67 + rest;
				String space = "";
				int spaceCount = 48 - rest;
				while (spaceCount-- > 0)
					space += " ";
				String ansci = lnCount == 0 ? (Util.toAnsci(id, 0, id.length) + Util.toAnsci(data, 0, data.length - id.length))
					: Util.toAnsci(data, lnCount * 16 - id.length, data.length - id.length);
				_hexStyledDoc.insertString(pos, space + "  " + ansci, _hexStyledDoc.getStyle("base"));
			}
			catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public int getSelectedPacketindex() {
		return _packetTable.getSelectedRow();
	}

	private class SessionDetailPanel extends JPanel {

		private JLabel _port;
		private JLabel _protocol;
		private JLabel _serverType;
		private JLabel _packetCount;
		private JLabel _clientIP;
		private JLabel _serverIP;

		public SessionDetailPanel(Session s) {
			this.setLayout(new GridLayout(3, 2));
			_port = new JLabel("Port: " + s.getProtocol().getPort());
			_protocol = new JLabel("Protocol: " + s.getProtocol().getName());
			_serverType = new JLabel("Server Type: " + s.getServerType());
			_packetCount = new JLabel("Packets: " + s.getPackets().size());
			_clientIP = new JLabel("Client: " + (s.getClientIp() == null ? "N/A" : s.getClientIp().getHostAddress()));
			_serverIP = new JLabel("Server: " + (s.getServerIp() == null ? "N/A" : s.getServerIp().getHostAddress()));
			this.add(_port);
			this.add(_clientIP);
			this.add(_protocol);
			this.add(_serverIP);
			this.add(_serverType);
			this.add(_packetCount);
		}

		public void setPort(int port) {
			_port.setText("Port: " + port);
		}

		public void setProtocol(String protocol) {
			_protocol.setText("Protocol: " + protocol);
		}

		public void setServerType(String type) {
			_serverType.setText("Server Type: " + type);
		}

		public void setPacketCount(int count) {
			_packetCount.setText("Packets: " + count);
		}

		public void setClientIP(Inet4Address addr) {
			_clientIP.setText("Client: " + addr.toString());
		}

		public void setServerIP(Inet4Address addr) {
			_serverIP.setText("Server: " + addr.toString());
		}
	}

	public class PartSelectionListener implements ListSelectionListener {

		JXTreeTable _table;

		// It is necessary to keep the table since it is not possible
		// to determine the table from the event's source
		PartSelectionListener(JXTreeTable table) {
			_table = table;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == _table.getSelectionModel()) {
				_table.getPathForRow(_table.getSelectedRow());

				// is there already a highlighted part?
				if (_currentSelectedPart != null) {
					int offset = _currentSelectedPart.getOffset();
					offset += offset/(16*3)*19;
					// unhighlight previous selected text
					// restoring its style
					try {
						String text = _hexStyledDoc.getText(offset, _currentSelectedPart.getLength());
						_hexStyledDoc.remove(offset, _currentSelectedPart.getLength());
						_hexStyledDoc.insertString(offset, text,
							_hexStyledDoc.getStyle(_currentSelectedPart.getPacketNode().getModelPart().getType().getName()));
					}
					catch (BadLocationException e1) {
						e1.printStackTrace();
					}

				}
				TreePath tp = _table.getPathForRow(_table.getSelectedRow());
				if (tp != null && ((DataPartNode) tp.getLastPathComponent()).isLeaf()) {
					_currentSelectedPart = (DataPartNode) tp.getLastPathComponent();
					ViewPane.this.highlightSelectedPart(_currentSelectedPart);
				}
			}
		}
	}

	public class PacketSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			ViewPane view = ViewPane.this;
			// If cell selection is enabled, both row and column change events are fired
			if (e.getSource() == view.getPacketTable().getSelectionModel()) {
				view.updateCurrentPacket();
			}
			else if (e.getSource() == view.getPacketTable().getColumnModel().getSelectionModel()
				&& view.getPacketTable().getColumnSelectionAllowed()) {
			}
		}

	}

	public class PacketViewTreeRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DataPartNode && leaf) {
				// System.out.println(((DataPartNode) value).getPacketNode().getModelPart());
				this.setText(((DataPartNode) value).getPacketNode().treeString());
				this.setIcon(IconsTable.getInstance().getIconForPartType(
					((DataPartNode) value).getPacketNode().getModelPart().getType()));
			}
			return this;
		}
	}

	@Override
	public void finalize() {
		System.out.println("Finalized: " + this.getClass().getSimpleName());
	}
}
