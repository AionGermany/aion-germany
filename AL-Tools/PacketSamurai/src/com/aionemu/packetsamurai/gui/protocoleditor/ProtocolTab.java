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


package com.aionemu.packetsamurai.gui.protocoleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import javolution.util.FastList;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.IconComboBoxRenderer;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.gui.images.IconsTable;
import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.PartTypeManager;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFormat;
import com.aionemu.packetsamurai.protocol.protocoltree.ProtocolNode;

@SuppressWarnings("serial")
/**
 * This class is a JPanel that is used to represent a root packet family (like client packets or server packets)
 * it is used in the ProtocolEditor
 * @author Gilles Duboscq
 * 
 */
public class ProtocolTab extends JPanel {

	private JXTreeTable _partsTreeTable;
	private PacketFamilly _packetFamilly;
	private JXTree _protocolTree;
	private JScrollPane _scrollPanePacketFormat;

	public final static ImageIcon packetIcon = IconsTable.ICON_PACKET;
	public final static ImageIcon packetGroupIcon = IconsTable.ICON_PACKETGRP;
	public final static ImageIcon caseIcon = IconsTable.ICON_CASE;
	private JComboBox<?> _partTypeCombo;

	public ProtocolTab(PacketFamilly familly) {
		this();
		changeFamilly(familly);
	}

	public ProtocolTab() {
		setLayout(new GridLayout(1, 2));

		_scrollPanePacketFormat = new JScrollPane(createTreeTable(null));

		_protocolTree = new JXTree();
		_protocolTree.setCellRenderer(new PacketTreeRenderer());
		_protocolTree.getSelectionModel().addTreeSelectionListener(
			new ProtocolTreeSelectionListener(_scrollPanePacketFormat));

		JScrollPane scrollPaneProtocolTree = new JScrollPane(_protocolTree);

		Container partsContainer = new Container();
		GridBagConstraints partsCons = new GridBagConstraints();
		GridBagLayout partsLayout = new GridBagLayout();
		partsContainer.setLayout(partsLayout);

		Container packetsContainer = new Container();
		GridBagConstraints packetsCons = new GridBagConstraints();
		GridBagLayout packetsLayout = new GridBagLayout();
		packetsContainer.setLayout(packetsLayout);

		Container partsToolBar = new Container();
		Container packetsToolBar = new Container();

		_partTypeCombo = new JComboBox<Object>(IconComboBoxRenderer.types);
		_partTypeCombo.setRenderer(new IconComboBoxRenderer());
		JButton addPartButton = new JButton(IconsTable.ICON_PLUS);
		addPartButton.addActionListener(new PacketEditorActionListener());
		addPartButton.setActionCommand("+");
		JButton removePartButton = new JButton(IconsTable.ICON_MINUS);
		removePartButton.addActionListener(new PacketEditorActionListener());
		removePartButton.setActionCommand("-");
		partsToolBar.add(_partTypeCombo);
		partsToolBar.add(addPartButton);
		partsToolBar.add(removePartButton);
		FlowLayout partsFlowLayout = new FlowLayout();
		partsFlowLayout.setAlignment(FlowLayout.CENTER);
		partsToolBar.setLayout(partsFlowLayout);

		JButton addPacketButton = new JButton(IconsTable.ICON_PLUS);
		addPacketButton.addActionListener(new PacketEditorActionListener());
		addPacketButton.setActionCommand("+p");
		JButton removePacketButton = new JButton(IconsTable.ICON_MINUS);
		removePacketButton.addActionListener(new PacketEditorActionListener());
		removePacketButton.setActionCommand("-p");
		packetsToolBar.add(addPacketButton);
		packetsToolBar.add(removePacketButton);
		FlowLayout packetsFlowLayout = new FlowLayout();
		packetsFlowLayout.setAlignment(FlowLayout.CENTER);
		packetsToolBar.setLayout(packetsFlowLayout);

		packetsCons.weightx = 0.5;
		packetsCons.fill = GridBagConstraints.BOTH;
		packetsCons.gridy = 0;
		packetsCons.gridx = 0;
		packetsContainer.add(packetsToolBar, packetsCons);
		packetsCons.gridy = 1;
		packetsCons.weighty = 0.5;
		packetsContainer.add(scrollPaneProtocolTree, packetsCons);

		partsCons.weightx = 0.5;
		partsCons.fill = GridBagConstraints.BOTH;
		partsCons.gridy = 0;
		partsCons.gridx = 0;
		partsContainer.add(partsToolBar, partsCons);
		partsCons.gridy = 1;
		partsCons.weighty = 0.5;
		partsContainer.add(_scrollPanePacketFormat, partsCons);

		add(packetsContainer);
		add(partsContainer);
	}

	public void changeFamilly(PacketFamilly f) {
		_packetFamilly = f;
		if (f == null) {
			setName("-! No Protocol !-");
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("-");
			_protocolTree.setModel(new DefaultTreeModel(root));
		}
		else {
			setName(f.getName());

			DefaultMutableTreeNode root = new DefaultMutableTreeNode(_packetFamilly);
			buildTree(_packetFamilly, root);
			_protocolTree.setModel(new DefaultTreeModel(root));
		}
	}

	private void buildTree(PacketFamilly root, DefaultMutableTreeNode currentNode) {
		FastList<ProtocolNode> nodes = new FastList<ProtocolNode>(root.getNodes().values());
		Collections.sort(nodes, new Comparator<ProtocolNode>() {

			public int compare(ProtocolNode o1, ProtocolNode o2) {
				return o1.getID() - o2.getID();
			}
		});
		for (ProtocolNode pNode : nodes) {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(pNode);
			if (pNode instanceof PacketFormat) {
				currentNode.add(newNode);
			}
			else if (pNode instanceof PacketFamilly) {
				currentNode.add(newNode);
				buildTree((PacketFamilly) pNode, newNode);
			}
		}
	}

	private JXTreeTable createTreeTable(PacketFormat format) {
		JXTreeTable partsTreeTable = new JXTreeTable(new PacketPartsTreeTableModel(format));
		partsTreeTable.setDefaultEditor(TreeTableModel.class, new TreeTableComboBoxCellEditor(partsTreeTable));
		partsTreeTable.setDefaultEditor(String.class, new TreeTableTextCellEditor(partsTreeTable));
		partsTreeTable.setDefaultEditor(Integer.class, new TreeTableTextCellEditor(partsTreeTable));
		partsTreeTable.setTreeCellRenderer(new PacketPartsTreeRenderer(partsTreeTable, format));
		partsTreeTable.setRootVisible(false);
		partsTreeTable.addTreeExpansionListener(new PacketPartsTreeExpensionListener(partsTreeTable));
		// partsTreeTable.getColumnModel().getColumn(2).setMaxWidth(25);
		// partsTreeTable.getColumnModel().getColumn(3).setMaxWidth(115);
		// partsTreeTable.getColumnModel().getColumn(3).setPreferredWidth(105);
		resizeTreeColumn(partsTreeTable);
		TreeTableComboBoxCellEditor editor = (TreeTableComboBoxCellEditor) partsTreeTable
			.getDefaultEditor(TreeTableModel.class);
		JComboBox<?> combo = editor.getComboBox();
		combo.setRenderer(new IconComboBoxRenderer());
		_partsTreeTable = partsTreeTable;
		return partsTreeTable;
	}

	private static void resizeTreeColumn(JXTreeTable treeTable) {
		/*
		 * TableCellRenderer headerRenderer = treeTable.getTableHeader().getDefaultRenderer(); TableColumn column =
		 * treeTable.getColumnModel().getColumn(0); Component comp = headerRenderer.getTableCellRendererComponent( null,
		 * column.getHeaderValue(), false, false, 0, 0); int headerWidth = comp.getPreferredSize().width; int cellWidth =
		 * treeTable.getMaximumSize().width; cellWidth += 40; //offset for the comboBox int newWith = Math.max(headerWidth,
		 * cellWidth); column.setPreferredWidth(newWith); column.setMinWidth(newWith); column.setMaxWidth(newWith);
		 */
		treeTable.sizeColumnsToFit(0);
	}

	private class PacketTreeRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				value = ((DefaultMutableTreeNode) value).getUserObject();
			}
			if (value instanceof ProtocolNode) {
				if (value instanceof PacketFormat) {
					setIcon(packetIcon);
				}
				else if (value instanceof PacketFamilly) {
					setIcon(packetGroupIcon);
				}
			}
			return this;
		}
	}

	private class PacketPartsTreeRenderer extends DefaultTreeCellRenderer {

		public PacketPartsTreeRenderer(JXTreeTable partsTreeTable, PacketFormat format) {
			addMouseMotionListener(new PacketPartMouseMotionListener(partsTreeTable, format));
			addMouseListener(new PacketPartMouseMotionListener(partsTreeTable, format));
		}

		@SuppressWarnings("unused")
		public PacketPartsTreeRenderer() {

		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof Part) {
				String com = ((Part) value).getComment();
				if (com != null && !com.equals(""))
					setToolTipText(com);
				if (value instanceof SwitchCaseBlock) {
					setIcon(caseIcon);
				}
				else {
					ImageIcon ii = IconsTable.getInstance().getIconForPartType(((Part) value).getType());
					if (ii != null)
						setIcon(ii);
				}
			}
			return this;
		}
	}

	private class PacketPartsTreeExpensionListener implements TreeExpansionListener {

		private JXTreeTable _treeTable;

		public PacketPartsTreeExpensionListener(JXTreeTable partsTreeTable) {
			_treeTable = partsTreeTable;
		}

		public void treeCollapsed(TreeExpansionEvent event) {
			resizeTreeColumn(_treeTable);
		}

		public void treeExpanded(TreeExpansionEvent event) {
			resizeTreeColumn(_treeTable);
		}

	}

	private class ProtocolTreeSelectionListener implements TreeSelectionListener {

		private JScrollPane _scrollPanePacketFormat;

		public ProtocolTreeSelectionListener(JScrollPane pane) {
			_scrollPanePacketFormat = pane;
		}

		public void valueChanged(TreeSelectionEvent e) {
			Object obj = ((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getUserObject();
			if (obj instanceof PacketFormat) {

				_scrollPanePacketFormat.setViewportView(ProtocolTab.this.createTreeTable(((PacketFormat) obj)));
			}
			else {
				_scrollPanePacketFormat.setViewportView(ProtocolTab.this.createTreeTable(null));
			}
		}
	}

	private class PacketEditorActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (_packetFamilly == null)
				return;
			if (e.getActionCommand().equals("+")) {
				PacketFormat format = (PacketFormat) _partsTreeTable.getTreeTableModel().getRoot();
				if (format == null)
					return;
				PartType type = PartTypeManager.getInstance().getType((String) _partTypeCombo.getSelectedItem());
				Part part = new Part(type, -1, "", "", "");
				if (type == PartType.forBlock)
					part = new ForPart(-2);
				Object afterObject = null;
				if (_partsTreeTable.getSelectedRowCount() > 0) {
					TreePath path = _partsTreeTable.getPathForRow(_partsTreeTable.getSelectedRow());
					if (path != null)
						afterObject = path.getLastPathComponent();
				}
				if (afterObject instanceof SwitchCaseBlock) {
					((SwitchCaseBlock) afterObject).addPart(part);
				}
				else {
					Part afterPart = null;
					afterPart = (Part) afterObject;
					if (afterPart == null)
						format.getDataFormat().getMainBlock().addPart(part);
					else
						format.getDataFormat().getMainBlock().addPartAfter(part, afterPart);
				}
				_partsTreeTable.updateUI();
			}
			else if (e.getActionCommand().equals("-")) {
				PacketFormat format = (PacketFormat) _partsTreeTable.getTreeTableModel().getRoot();
				if (format == null || _partsTreeTable.getSelectedRowCount() < 1)
					return;
				for (int row : _partsTreeTable.getSelectedRows()) {
					TreePath path = _partsTreeTable.getPathForRow(row);
					if (path == null)
						continue;
					if (path.getLastPathComponent() instanceof SwitchCaseBlock) {
						SwitchCaseBlock sCase = (SwitchCaseBlock) path.getLastPathComponent();
						sCase.getContainingSwitch().removeCase(sCase);
						continue;
					}
					Part part = (Part) path.getLastPathComponent();
					if (part == null)
						continue;
					format.getDataFormat().getMainBlock().removePart(part);
				}
				_partsTreeTable.updateUI();
			}
			else if (e.getActionCommand().equals("+p")) {
				final JDialog createDialog = new JDialog(((Main) PacketSamurai.getUserInterface()).getProtocolEditor(),
					"Create new Packet");
				JPanel createPanel = new JPanel();
				JButton createButton = new JButton("Create");
				createButton.setActionCommand("create");
				final JTextField createField = new JTextField("(c)", 30);
				createButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						if ("create".equals(e.getActionCommand())) {
							int idNumber = 0;
							String pString = createField.getText();
							if (pString == null) {
								createDialog.setVisible(false);
								createDialog.dispose();
								return;
							}
							List<PartType> ids = PacketFormat.getIdPartsInString(pString);
							if (ids == null) {
								createDialog.dispose();
								return;
							}
							JDialog idDialog = new JDialog(((Main) PacketSamurai.getUserInterface()).getProtocolEditor(),
								"Create new Packet");
							PartsPanel idPanel = new PartsPanel();
							for (PartType p : ids) {
								idPanel.addRow(new PartRowPanel(p));
								idNumber++;
							}
							idPanel.updateRows();
							JPanel idDialPanel = new JPanel();
							idDialPanel.setLayout(new BoxLayout(idDialPanel, BoxLayout.PAGE_AXIS));
							JButton finishButton = new JButton("Finish");
							finishButton.setActionCommand("finish");
							finishButton.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub

								}

							});
							idDialPanel.add(idPanel);
							idDialPanel.add(finishButton);
							idDialog.setSize(450, 75 + idNumber * 30);
							idDialog.setResizable(false);
							idDialog.setContentPane(idDialPanel);
							idDialog.setLocationRelativeTo(createDialog);
							createDialog.setVisible(false);
							idDialog.setVisible(true);

						}
					}

				});
				createPanel.add(createField);
				createPanel.add(createButton);
				createDialog.setSize(450, 75);
				createDialog.setResizable(false);
				createDialog.setContentPane(createPanel);
				createDialog.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getProtocolEditor());
				createDialog.setVisible(true);
			}
			else if (e.getActionCommand().equals("-p")) {
				TreePath[] paths = _protocolTree.getSelectionPaths();
				if (paths == null)
					return;
				for (TreePath path : paths) {
					if (path == null)
						continue;
					ProtocolNode node = (ProtocolNode) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
					if (node == null)
						continue;
					_packetFamilly.remove(node);
				}
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) _protocolTree.getModel().getRoot();
				root.removeAllChildren();
				buildTree(_packetFamilly, root);
				_protocolTree.updateUI();
			}
		}

	}

	private class PacketPartMouseMotionListener implements MouseInputListener {

		@SuppressWarnings("unused")
		private JXTreeTable _treeTable;
		private Part _dragedObject;
		private MouseEvent _firstMouseEvent;
		private PacketFormat _format;

		public PacketPartMouseMotionListener(JXTreeTable partsTreeTable, PacketFormat format) {
			_treeTable = partsTreeTable;
			_format = format;
		}

		public void mousePressed(MouseEvent e) {
			if (_format == null)
				return;
			if (_dragedObject != null && (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
				// TODO: cancel
				System.out.println("Cancel");
				return;
			}
			_firstMouseEvent = e;
			_dragedObject = getObjectAt(e.getX(), e.getY());
		}

		public void mouseReleased(MouseEvent e) {
			if (_format == null)
				return;
			// TODO: finish movement
			System.out.println("Finish move");
			_dragedObject = null;

		}

		public void mouseDragged(MouseEvent e) {
			if (_format == null)
				return;
			int dx = e.getX() - _firstMouseEvent.getX();
			int dy = e.getY() - _firstMouseEvent.getY();
			// TODO: move part
			System.out.println("dragging: x=" + e.getX() + " y=" + e.getY() + " dx=" + dx + "dy=" + dy);
		}

		private Part getObjectAt(int x, int y) {
			return null;
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	private class PartsPanel extends JPanel {

		private List<PartRowPanel> _idRows;

		public PartsPanel() {
			_idRows = new FastList<PartRowPanel>();
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		}

		public void addRow(PartRowPanel row) {
			_idRows.add(row);
		}

		@SuppressWarnings("unused")
		public void addRow(PartRowPanel row, int i) {
			_idRows.add(i, row);
		}

		@SuppressWarnings("unused")
		public void removeRow(PartRowPanel row) {
			_idRows.remove(row);
		}

		public void updateRows() {
			this.removeAll();
			for (PartRowPanel row : _idRows) {
				this.add(row);
			}
		}
	}

	private class PartRowPanel extends JPanel {

		private PartType _type;
		private PartTypeComboBox _typeCombo;

		public PartRowPanel(PartType type) {
			_type = type;
			_typeCombo = new PartTypeComboBox();
			_typeCombo.setSelectedItem(_type.getName());
			JButton addButton = new JButton(IconsTable.ICON_PLUS);
			addButton.setActionCommand("+id");
			addButton.addActionListener(new PacketEditorActionListener());
			JButton delButton = new JButton(IconsTable.ICON_MINUS);
			delButton.setActionCommand("-id");
			delButton.addActionListener(new PacketEditorActionListener());
			this.add(_typeCombo);
			this.add(addButton);
			this.add(delButton);
			this.setBackground(Color.CYAN);
			this.setBorder(new LineBorder(Color.BLACK));
		}
	}
}
