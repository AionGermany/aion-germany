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



package com.aionemu.packetsamurai.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import javolution.util.FastList;

import com.aionemu.packetsamurai.Captor;
import com.aionemu.packetsamurai.IUserInterface;
import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.logrepo.LogRepoTab;
import com.aionemu.packetsamurai.gui.protocoleditor.ProtocolEditor;
import com.aionemu.packetsamurai.logreaders.AbstractLogReader;
import com.aionemu.packetsamurai.protocol.ProtocolManager;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.Session;
import com.aionemu.packetsamurai.utils.ConquestNpcExporter;
import com.aionemu.packetsamurai.utils.ConquestPortalExporter;
import com.aionemu.packetsamurai.utils.NpcFlagExporter;
import com.aionemu.packetsamurai.utils.NpcGatherExporter;
import com.aionemu.packetsamurai.utils.NpcInfoExporter;
import com.aionemu.packetsamurai.utils.NpcLootExporter;
import com.aionemu.packetsamurai.utils.NpcObjectIdExporter;
import com.aionemu.packetsamurai.utils.NpcSkillExporter;
import com.aionemu.packetsamurai.utils.NpcSpawnExporter;
import com.aionemu.packetsamurai.utils.NpcStaticExporter;
import com.aionemu.packetsamurai.utils.NpcTestExporter;
import com.aionemu.packetsamurai.utils.NpcTitleExporter;
import com.aionemu.packetsamurai.utils.NpcWalkExporter;
import com.aionemu.packetsamurai.utils.PlayerAppearanceExporter;
import com.aionemu.packetsamurai.utils.collector.Collector;
import com.aionemu.packetsamurai.utils.collector.DataManager;

/**
 * @author Ulysses R. Ribeiro
 */
public class Main implements IUserInterface {
	protected JFrame _frame;

	private JMenuBar _menuBar = new JMenuBar();

	private JMenu _fileMenu = new JMenu("File");
	private JMenu _editMenu = new JMenu("Edit");
	private JMenu _utilsMenu = new JMenu("Utils");
	private JMenu _toolsMenu = new JMenu("Tools");
	private JMenu _helpMenu = new JMenu("Help");

	private JMenuItem _itemFlush;
	private JMenuItem _itemClose;
	private JMenuItem _itemCloseAll;

	private JMenuItem _itemSearch;
	private JMenuItem _itemSearchNext;
	private JMenuItem _itemGoto;
	private JMenuItem _itemFilter;
	private JCheckBoxMenuItem ignoreNpc;
	private JCheckBoxMenuItem ignoreBaseNpc;
	private JCheckBoxMenuItem ignoreSiegeNpc;
	private JCheckBoxMenuItem ignoreConquestNpc;
	private JCheckBoxMenuItem dataCollector;
	private JCheckBoxMenuItem _itemRecording;
	private JCheckBoxMenuItem gameserverData;

	private JCheckBoxMenuItem packetSkip;

	private ProtocolEditor _pEditor;

	private boolean _writeLog = true;

	private ActionListener _menuListener = new MenuActionListener();

	private JTabbedPane _tabPane = new JTabbedPane();

	private ConsoleTab _consoleTab;

	private ViewTab _viewerTab;

	private JDialog _selectInterfaceWindow;

	private SearchDlg _searchDlg;

	private JList<String> _interfaceList;

	private LogRepoTab _logRepo;
	
	private final List<String> _loggedStrings = new ArrayList<String>();

	private final String _timeId;

	public Main() {
		_timeId = String.valueOf((Calendar.getInstance().getTimeInMillis()/1000));
	}

	public void init() {
		
		_frame = new JFrame("Packet Samurai [Aion-Lightning - 7.x EU Edition] reworked for AionGer");
		_frame.setLayout(new BorderLayout());
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Menu Bar Itens
		// File Menu
		JMenuItem itemOpen = new JMenuItem("Open");
		itemOpen.setActionCommand("Open");
		itemOpen.addActionListener(_menuListener);

		_itemFlush = new JMenuItem("Flush");
		_itemFlush.setEnabled(false);
		_itemFlush.setActionCommand("Flush");
		_itemFlush.addActionListener(_menuListener);

		_itemClose = new JMenuItem("Close");
		_itemClose.setEnabled(false);
		_itemClose.setActionCommand("Close");
		_itemClose.addActionListener(_menuListener);

		_itemCloseAll = new JMenuItem("Close All");
		_itemCloseAll.setEnabled(false);
		_itemCloseAll.setActionCommand("CloseAll");
		_itemCloseAll.addActionListener(_menuListener);

		JMenuItem itemExit = new JMenuItem("Exit");
		itemExit.setActionCommand("Exit");
		itemExit.addActionListener(_menuListener);

		_fileMenu.add(itemOpen);
		_fileMenu.add(_itemFlush);
		_fileMenu.add(_itemClose);
		_fileMenu.add(_itemCloseAll);
		_fileMenu.add(itemExit);

		// Edit
		_itemSearch = new JMenuItem("Search");
		_itemSearch.setEnabled(false);
		_itemSearch.setActionCommand("Search");
		_itemSearch.setMnemonic(KeyEvent.VK_F);
		_itemSearch.addActionListener(_menuListener);
		_editMenu.add(_itemSearch);

		_itemSearchNext = new JMenuItem("Search Next");
		_itemSearchNext.setEnabled(false);
		_itemSearchNext.setActionCommand("SearchNext");
		_itemSearchNext.addActionListener(_menuListener);
		_editMenu.add(_itemSearchNext);

		_itemGoto = new JMenuItem("Go to...");
		_itemGoto.setEnabled(false);
		_itemGoto.setActionCommand("GoTo");
		_itemGoto.addActionListener(_menuListener);
		_editMenu.add(_itemGoto);

		_itemFilter = new JMenuItem("Filter");
		_itemFilter.setEnabled(false);
		_itemFilter.setActionCommand("Filter");
		_itemFilter.addActionListener(_menuListener);
		_editMenu.add(_itemFilter);

		// Utils
		dataCollector = new JCheckBoxMenuItem("Data collector");
		dataCollector.setActionCommand("collector");
		dataCollector.addActionListener(_menuListener);
		dataCollector.setState(Collector.isEnabled());
		_utilsMenu.add(dataCollector);
		

		_utilsMenu.addSeparator();
		JMenuItem save = new JMenuItem("Save data");
		save.setActionCommand("save");
		save.addActionListener(_menuListener);
		_utilsMenu.add(save);
		_utilsMenu.addSeparator();
		
		// Skip Npc Type Part
		ignoreConquestNpc = new JCheckBoxMenuItem("Skip ConquestNpcs Export");
		ignoreConquestNpc.setState(false);
		_utilsMenu.add(this.ignoreConquestNpc);
		
		ignoreBaseNpc = new JCheckBoxMenuItem("Skip BaseNpcs Export");
		ignoreBaseNpc.setState(false);
		_utilsMenu.add(this.ignoreBaseNpc);
		
		ignoreSiegeNpc = new JCheckBoxMenuItem("Skip SiegeNpcs Export");
		ignoreSiegeNpc.setState(false);
		_utilsMenu.add(this.ignoreSiegeNpc);
		
		ignoreNpc = new JCheckBoxMenuItem("Skip normal Npcs Export");
		ignoreNpc.setState(false);
		_utilsMenu.add(this.ignoreNpc);
		_utilsMenu.addSeparator();
		// Export Part
		
		// AllExporter
		JMenuItem exportAll = new JMenuItem("Export ALL");
		exportAll.setActionCommand("ExportAll");
		exportAll.addActionListener(_menuListener);
		_utilsMenu.add(exportAll);
		_utilsMenu.addSeparator();
		
		// NpcConquestExporter
		JMenuItem exportConquestNpc = new JMenuItem("Update ConquestNpc Spawns");
		exportConquestNpc.setActionCommand("ExportConquestNpcSpawns");
		exportConquestNpc.addActionListener(_menuListener);
		_utilsMenu.add(exportConquestNpc);
		// ConquestPortalExporter
		JMenuItem exportConquestPortal = new JMenuItem("Update Conquest Portals");
		exportConquestPortal.setActionCommand("ExportConquestPortals");
		exportConquestPortal.addActionListener(_menuListener);
		_utilsMenu.add(exportConquestPortal);
		// NpcGatherExporter
		JMenuItem exportGather = new JMenuItem("Update Gather Spawns");
		exportGather.setActionCommand("ExportGatherSpawns");
		exportGather.addActionListener(_menuListener);
		_utilsMenu.add(exportGather);
		_utilsMenu.addSeparator();
		// NpcLootExporter
		JMenuItem exportLoot = new JMenuItem("Export Loot List");
		exportLoot.setActionCommand("ExportLootSpawns");
		exportLoot.addActionListener(_menuListener);
		_utilsMenu.add(exportLoot);
		//NpcInfoExporter
		JMenuItem exportInfos = new JMenuItem("Export Npc Infos");
		exportInfos.setActionCommand("ExportNpcInfo");
		exportInfos.addActionListener(_menuListener);
		_utilsMenu.add(exportInfos);
		//NpcSpawnExporter
		JMenuItem exportSpawns = new JMenuItem("Export Npc Spawns");
		exportSpawns.setActionCommand("ExportNpcSpawns");
		exportSpawns.addActionListener(_menuListener);
		_utilsMenu.add(exportSpawns);
		//NpcFlagExporter
		JMenuItem exportFlags = new JMenuItem("Export Npc Flags");
		exportFlags.setActionCommand("ExportNpcFlags");
		exportFlags.addActionListener(_menuListener);
		_utilsMenu.add(exportFlags);
		//NpcSkillExporter
		JMenuItem exportSkills = new JMenuItem("Export NPC Skills");
		exportSkills.setActionCommand("ExportNpcSkill");
		exportSkills.addActionListener(_menuListener);
		_utilsMenu.add(exportSkills);				
		//NpcTestExporter
		JMenuItem exportTest = new JMenuItem("Export Test");
		exportTest.setActionCommand("ExportNpcTest");
		exportTest.addActionListener(_menuListener);
		_utilsMenu.add(exportTest);
		//NpcStaticExporter
		JMenuItem exportStatic = new JMenuItem("Export Npc StaticId");
		exportStatic.setActionCommand("ExportNpcStatic");
		exportStatic.addActionListener(_menuListener);
		_utilsMenu.add(exportStatic);
		//NpcTitleExporter
		JMenuItem exportTitles = new JMenuItem("Export Npc Title");
		exportTitles.setActionCommand("ExportNpcTitle");
		exportTitles.addActionListener(_menuListener);
		_utilsMenu.add(exportTitles);
		//NpcWalkExporter
		JMenuItem exportWalk = new JMenuItem("Export Npc Walk");
		exportWalk.setActionCommand("ExportNpcWalks");
		exportWalk.addActionListener(_menuListener);
		_utilsMenu.add(exportWalk);
		//NpcObjectIdExporter
		JMenuItem exportObjectId = new JMenuItem("Export Npc ObjectId");
		exportObjectId.setActionCommand("ExportNpcObjectId");
		exportObjectId.addActionListener(_menuListener);
		_utilsMenu.add(exportObjectId);
		_utilsMenu.addSeparator();
		//PlayerAppearanceExporter
		JMenuItem exportPlayerAppearance = new JMenuItem("Export Player Appearances");
		exportPlayerAppearance.setActionCommand("ExportPlayerAppearance");
		exportPlayerAppearance.addActionListener(_menuListener);
		_utilsMenu.add(exportPlayerAppearance);

		// Tools

		_itemRecording = new JCheckBoxMenuItem("Record Stream");
		_itemRecording.setState(true);
		_toolsMenu.add(this._itemRecording);
		
		gameserverData = new JCheckBoxMenuItem("Get Data from GameServer");
		gameserverData.setState(PacketSamurai.configGetDataFromGameServer());
		gameserverData.setActionCommand("getDataFromGameServer");
		gameserverData.addActionListener(_menuListener);
		_toolsMenu.add(gameserverData);

		packetSkip = new JCheckBoxMenuItem("Skip packets");
		packetSkip.setState(false);
		_toolsMenu.add(this.packetSkip);

        JMenuItem itemDump = new JMenuItem("Dump Selected Packet");
        itemDump.setActionCommand("Dump");
        itemDump.addActionListener(_menuListener);
        _toolsMenu.add(itemDump);

		JMenuItem itemSelectInterface = new JMenuItem("Select Interface");
		itemSelectInterface.setActionCommand("SelectInterface");
		itemSelectInterface.addActionListener(_menuListener);
		_toolsMenu.add(itemSelectInterface);

		JMenuItem itemEditProtocol = new JMenuItem("Edit Protocol");
		itemEditProtocol.setActionCommand("EditProtocol");
		itemEditProtocol.addActionListener(_menuListener);
		_toolsMenu.add(itemEditProtocol);

		JMenuItem itemSetActiveProtocols = new JMenuItem("Set Active Protocols");
		itemSetActiveProtocols.setActionCommand("SetActiveProtocols");
		itemSetActiveProtocols.addActionListener(_menuListener);
		_toolsMenu.add(itemSetActiveProtocols);

		JMenuItem protocolsReloadProtocols = new JMenuItem("Protocols Reload");
		protocolsReloadProtocols.setActionCommand("ProtocolsReload");
		protocolsReloadProtocols.addActionListener(this._menuListener);
		this._toolsMenu.add(protocolsReloadProtocols);

		// Help
		JMenuItem itemAbout = new JMenuItem("About");
		itemAbout.setActionCommand("About");
		itemAbout.addActionListener(_menuListener);
		_helpMenu.add(itemAbout);

		_menuBar.add(_fileMenu);
		_menuBar.add(_editMenu);
		_menuBar.add(_utilsMenu);
		_menuBar.add(_toolsMenu);
		_menuBar.add(_helpMenu);
		_frame.setJMenuBar(_menuBar);

		// Console Tab
		_consoleTab = new ConsoleTab();

		// Viewer Tab
		_viewerTab = new ViewTab();

		//_logRepo = new LogRepoTab();

		// hotkeys
		_tabPane.registerKeyboardAction(_menuListener, "Search", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		_tabPane.registerKeyboardAction(_menuListener, "SearchNext", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		_tabPane.registerKeyboardAction(_menuListener, "Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		_tabPane.registerKeyboardAction(_menuListener, "EditProtocol", KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		_tabPane.registerKeyboardAction(_menuListener, "Filter", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		_tabPane.add("Console", _consoleTab);
		_tabPane.add("Packet Viewer", _viewerTab);
		//_tabPane.add("Log Repository", _logRepo);

		// build the frame
		_frame.add(_tabPane, BorderLayout.CENTER);

		// add the window listeners
		addListeners();

		_frame.setMinimumSize(new Dimension(850, 600));
		_frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
		_consoleTab.setBackground(Color.BLUE);
		_frame.setAlwaysOnTop(false);
		_frame.setVisible(true);
	}

	public JTabbedPane getTabPane() {
		return _tabPane;
	}

	public LogRepoTab getLogRepoTab() {
		return _logRepo;
	}

	public void log(String text)
	{
		log(text, true);
	}
	
	@Override
	public synchronized void log(String text, boolean display) 
	{
		if (display) {
			_consoleTab.addText(text);
			System.err.println(text);
		}

		if (_writeLog) {
			try {
				new File("logs/txt/").mkdirs();
				File outFile = new File("logs/txt/log-"+_timeId+".txt");
				if (!outFile.exists())
					outFile.createNewFile();
				
				// output         
				FileOutputStream fos = new FileOutputStream(outFile);
				PrintWriter out = new PrintWriter(fos);
				
				String totalText = "";
				for (String str : _loggedStrings) {
					totalText += str + "\n";
				}
				
				totalText += text;
				out.write(totalText);
				
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		_loggedStrings.add(text);
	}

	private void addListeners() {
		// Window Closing
		/*
		 * _frame.addWindowListener(new WindowAdapter() { public void
		 * windowClosing(WindowEvent event) { close(); } });
		 */
	}

	public void showInterfaceSelector(String[] interfaceNames) {
		_selectInterfaceWindow = new JDialog(_frame);
		_selectInterfaceWindow.setTitle("Double-Click to Select the Interface");
		_selectInterfaceWindow.setLocationRelativeTo(_frame);
		_interfaceList = new JList<String>(interfaceNames);
		_interfaceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(_interfaceList);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = _interfaceList.locationToIndex(e.getPoint());
					if (Captor.getInstance().getCurrentDeviceId() == index) {
						_selectInterfaceWindow.dispose();
						return;
					}
					Captor.getInstance().openDevice(index);
					PacketSamurai.setConfigProperty("NetworkInterface", Integer.toString(index));
					_selectInterfaceWindow.dispose();
				}
			}
		};

		_interfaceList.addMouseListener(mouseListener);

		_selectInterfaceWindow.add(scrollPane);
		_selectInterfaceWindow.setSize(400, 350);
		_selectInterfaceWindow.setVisible(true);

		JFrame frame = new JFrame("Title");
		frame.setSize(200,200);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	public void showAboutDialog() {
		JOptionPane.showMessageDialog(this.getMainFrame(), "Packet Samurai Aion-Lightning\n7.x EU Edition\n\nGilles Duboscq\nUlysses R. Ribeiro\n\nUpdated by AionGer:\nFalke34\nFrozenKiller\nKev\nCooly\n", "About Packet Samurai 7.x EU Edition", JOptionPane.PLAIN_MESSAGE);
	}

	public void close() {
		PacketSamurai.saveConfig();
	}

	public void toggleSearchDialog() {
		if (getViewerTab().getComponentCount() > 0) {
			if (_searchDlg == null) {
				_searchDlg = new SearchDlg(_frame);
				_searchDlg.setVisible(true);
			} else {
				// toggle display
				_searchDlg.setVisible(!_searchDlg.isVisible());
			}
		}
	}

	public void toggleFilterDialog() {
		if (getViewerTab().getComponentCount() > 0) {
			FilterDlg filterDlg = getViewerTab().getCurrentViewPane().getFilterDialog();

			// toggle display
			filterDlg.setVisible(!filterDlg.isVisible());
		}
	}

	public void searchNext() {
		if (getViewerTab().getComponentCount() > 0) {
			if (_searchDlg != null) {
				ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
				if (pane != null) {
					int index = _searchDlg.search(pane.getSelectedPacketindex() + 1);
					if (index >= 0) {
						JTable pt = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane().getPacketTable();
						pt.setAutoscrolls(true);
						pt.getSelectionModel().setSelectionInterval(index, index);
						pt.scrollRectToVisible(pt.getCellRect(index, 0, true));
						_searchDlg.setCurrentSearchIndex(index + 1);
					}
				}
			} else {
				toggleSearchDialog();
			}
		}
	}

	public void toggleProtocolEditor() {
		if (_pEditor == null) {
			ProtocolEditor pe = new ProtocolEditor(_frame);
			_pEditor = pe;
			_pEditor.setLocationRelativeTo(_frame); // will make PE spawn
			// centered relatively to
			// the main frame
		}

		// toggle display
		_pEditor.setVisible(!_pEditor.isVisible());
	}
		
	private void exportAll()
	{
		exportConquestNpcSpawns();
		exportGatherSpawns();
		exportLootSpawns();
		exportNpcInfo();
		exportNpcSpawns();
		exportNpcFlags();
		exportNpcSkill();
		exportNpcTest();
		exportNpcStatic();
		exportNpcWalks();
		exportNpcObjectId();
		exportNpcTitle();
		
	}
	
	//PlayerAppearanceExporter
	private void exportPlayerAppearance() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new PlayerAppearanceExporter(packets, sessionName).parse();
			}
		}
	}
	// ConquestPortalExporter
	private void exportConquestPortals() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new ConquestPortalExporter(packets, sessionName).parse();
			}
		}
	}
	// ConquestNpcExporter
	private void exportConquestNpcSpawns() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new ConquestNpcExporter(packets, sessionName).parse();
			}
		}
	}
	// NpcGatherExporter
	private void exportGatherSpawns() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcGatherExporter(packets, sessionName).parse();
			}
		}
	}
	// NpcLootExporter
	private void exportLootSpawns() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcLootExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcInfoExporter
	private void exportNpcInfo() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcInfoExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcSpawnExporter
	private void exportNpcSpawns() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcSpawnExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcFlagExporter
	private void exportNpcFlags() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcFlagExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcSkillExporter
	private void exportNpcSkill() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcSkillExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcTestExporter
	private void exportNpcTest() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcTestExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcStaticExporter
	private void exportNpcStatic() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcStaticExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcWalkExporter
	private void exportNpcWalks() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcWalkExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcObjectIdExporter
	private void exportNpcObjectId() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcObjectIdExporter(packets, sessionName).parse();
			}
		}
	}
	//NpcTitleExporter
	private void exportNpcTitle() {
		if (getViewerTab().getComponentCount() > 0) {

			ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
			if (pane != null) {
				String sessionName = pane.getGameSessionViewer().getSession().getSessionName();
				FastList<DataPacket> packets = pane.getGameSessionViewer().getSession().getPackets();
				new NpcTitleExporter(packets, sessionName).parse();
			}
		}
	}

	private void protocolsReload() {
		Thread threadSwitch = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ProtocolManager.getInstance().loadProtocols();

					ViewPane pane = ((Main) PacketSamurai.getUserInterface()).getViewerTab().getCurrentViewPane();
					if (pane != null) {
						int index = pane.getSelectedPacketindex();

						System.out.println(pane.getFileName());
						AbstractLogReader reader = AbstractLogReader.getLogReaderForFile(pane.getFileName());
						if (reader == null)
							return;
						reader.reloadParse();

						pane.setSelectedPacket(index, index);
					}

					PacketSamurai.getUserInterface().log("reloaded protocols!.");
				} catch (Exception e) {
					PacketSamurai.getUserInterface().log("something went wrong on reloading protocols...");
					e.printStackTrace();
				}
			}
		});
		threadSwitch.start();
	}

	public boolean isRecording() {
		return _itemRecording.isSelected();
	}
	
	public boolean getDataFromGameServer() {
		return gameserverData.isSelected();
	}

	public boolean isSkippingPackets() {
		return packetSkip.isSelected();
	}
	
	//Skip Npc Export Switches
	public boolean isSkippingNpcs() {
		return ignoreNpc.isSelected();
	}
	public boolean isSkippingConquestNpcs() {
		return ignoreConquestNpc.isSelected();
	}
	public boolean isSkippingBaseNpcs() {
		return ignoreBaseNpc.isSelected();
	}
	public boolean isSkippingSiegeNpcs() {
		return ignoreSiegeNpc.isSelected();
	}

	// MenuActions
	public class MenuActionListener implements ActionListener {

		public void actionPerformed(ActionEvent ev) {
			String actionCmd = ev.getActionCommand();
			if (actionCmd.equals("Open")) {
				final JFileChooser chooser = new JFileChooser(PacketSamurai.getConfigProperty("lastLogDir", ".\\logs\\"));
				chooser.setFileFilter(new FileFilter() {

					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.isFile() && (f.getName().endsWith(".pcap") || f.getName().endsWith(".cap") || f.getName().endsWith(".bin") || f.getName().endsWith(".psl"));
					}

					@Override
					public String getDescription() {
						return "All supported formats (.cap .pcap .bin .psl)";
					}

				});
				final int returnVal = chooser.showOpenDialog(_frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Thread t = new Thread() {
						public void run() {
							((Main) PacketSamurai.getUserInterface()).openSession(chooser.getSelectedFile());
						}
					};
					t.start();
				}
			}else if (actionCmd.equals("Flush"))
			{
				Main.this.getViewerTab().getCurrentViewPane().getGameSessionViewer().getAllPackets().clear();
				Main.this.getViewerTab().getCurrentViewPane().getGameSessionViewer().getSession().flush();
				Main.this.getViewerTab().getCurrentViewPane().displaySession();
			}
			else if (actionCmd.equals("Close")) {
				Main.this.closeSessionTab(Main.this.getViewerTab().getCurrentViewPane());
			} else if (actionCmd.equals("CloseAll")) {
				getViewerTab().removeAll();
				Main.this.toggleSessionItems(false);
			} else if (actionCmd.equals("Search")) {
				toggleSearchDialog();
			} else if (actionCmd.equals("SearchNext")) {
				searchNext();
			} else if (actionCmd.equals("GoTo")) {
				String ret = JOptionPane.showInputDialog(null, "Enter the packet number", "Go to packet...", JOptionPane.INFORMATION_MESSAGE);
				if (ret != null) {
					try {
						int pn = Integer.parseInt(ret);

						if (pn >= 0 && pn < Main.this.getViewerTab().getCurrentViewPane().getPacketTable().getRowCount()) {
							Main.this.getViewerTab().getCurrentViewPane().setSelectedPacket(pn, pn);
						} else {
							JOptionPane.showMessageDialog(null, "Invalid value for packet number.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid value for packet number.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (actionCmd.equals("Filter")) {
				toggleFilterDialog();
			} else if (actionCmd.equals("SelectInterface")) {
				// must run asynchronously from Main UI
				new Thread(new Runnable() {

					public void run() {
						Captor.getInstance().selectNetWorkInterface();

					}
				}).start();
			} else if (actionCmd.equals("SetActiveProtocols")) {
				new Thread(new Runnable() {

					public void run() {
						Captor.showSetActiveProtocols();
					}
				}).start();
			} else if (actionCmd.equals("Exit")) {
				System.exit(0);
			} else if (actionCmd.equals("About")) {
				showAboutDialog();
			} else if (actionCmd.equals("EditProtocol")) {
				toggleProtocolEditor();
			} else if (actionCmd.equals("ExportAll")){
				exportAll();
			} else if (actionCmd.equals("ExportPlayerAppearance")){
				exportPlayerAppearance();
			} else if (actionCmd.equals("ExportGatherSpawns")){
				exportGatherSpawns();
			} else if (actionCmd.equals("ExportConquestNpcSpawns")){
				exportConquestNpcSpawns();
			} else if (actionCmd.equals("ExportConquestPortals")){
				exportConquestPortals();
			} else if (actionCmd.equals("ExportLootSpawns")){
				exportLootSpawns();
			} else if (actionCmd.equals("ExportNpcInfo")){
				exportNpcInfo();
			} else if (actionCmd.equals("ExportNpcSpawns")){
				exportNpcSpawns();
			} else if (actionCmd.equals("ExportNpcFlags")){
				exportNpcFlags();
			} else if (actionCmd.equals("ExportNpcSkill")){
				exportNpcSkill();			
			} else if (actionCmd.equals("ExportNpcTest")){
				exportNpcTest();
			} else if (actionCmd.equals("ExportNpcStatic")){
				exportNpcStatic();
			} else if (actionCmd.equals("ExportNpcWalks")){
				exportNpcWalks();
			} else if (actionCmd.equals("ExportNpcTitle")){
				exportNpcTitle();
			} else if (actionCmd.equals("ExportNpcObjectId")){
				exportNpcObjectId();
			} else if (actionCmd.equals("ProtocolsReload")) {
				protocolsReload();
			} else if(actionCmd.equals("Dump")) {
				DumpPacket();
			} else if(actionCmd.equals("getDataFromGameServer")) {
				DataFromGameServer();
			} else if (actionCmd.equals("collector")) {
				collector();
			} else if (actionCmd.equals("save")) {
				DataManager.save();
			}
		}

	}

	private void collector(){
		Collector.setEnabled(!Collector.isEnabled());
		dataCollector.setState(Collector.isEnabled());
	}
	public ProtocolEditor getProtocolEditor() {
		return _pEditor;
	}

	public ViewTab getViewerTab() {
		return _viewerTab;
	}

	public void openSession(File file) {
		Set<Session> sessions = null;
		try {
			sessions = Main.getSessionsFromFile(file);
		} catch (FileNotFoundException fnfe) {
			PacketSamurai.getUserInterface().log("ERROR: Opening (" + file.getAbsolutePath() + "), file was not found.");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			PacketSamurai.getUserInterface().log("ERROR: Opening (" + file.getAbsolutePath() + "), I/O error.");
		} catch (BufferUnderflowException e) {
			PacketSamurai.getUserInterface().log("ERROR: Opening (" + file.getAbsolutePath() + "), file format error.");
			e.printStackTrace();
		}

		if (sessions != null) {
			PacketSamurai.setConfigProperty("lastLogDir", file.getParent());
			for (Session s : sessions) {
				this.showSession(s, false, false, file.getAbsolutePath());
			}
		}
	}

	/**
	 * 
	 * @param file
	 *            The file to be opened
	 * @return A set containing all session(s) stored on the file, or null if
	 *         there is no support for the file extension.
	 * @throws IOException
	 *             if there was an I/O error.
	 * @throws BufferUnderflowException
	 *             if there was insufficient data or a file format error.
	 */
	public static Set<Session> getSessionsFromFile(File file) throws IOException {
		Set<Session> sessions = null;

		String filename = file.getAbsolutePath();

		// for now its hardcoded, maybe registering later
		AbstractLogReader reader = AbstractLogReader.getLogReaderForFile(filename);
		if (reader == null)
			return null;
		reader.parse();
		sessions = reader.getSessions();

		return sessions;
	}

	public JFrame getMainFrame() {
		return _frame;
	}

	public void showSession(Session s, boolean notify, boolean isReload, String fileName) {
		this.getViewerTab().showSession(s, notify, isReload, fileName);
		this.toggleSessionItems(true);
	}

	private void DataFromGameServer()
	{
		PacketSamurai.setConfigProperty("getDataFromGameServer", getDataFromGameServer() ? "True" : "False");
		log("DataSource & UpdateDestination is now :" +(getDataFromGameServer() ? " GameServer" : " local PS-Data"));
		DataManager.load();
	}
	
	public void DumpPacket()
	{
		ViewTab vt = this.getViewerTab();
			if(vt == null) return;

		ViewPane vp = vt.getCurrentViewPane();
		if(vp == null) return;

		JTable table = vp.getPacketTable();
		if(table == null) return;

		int id = table.getSelectedRow();
		if(id != -1)
		{
			DataPacket packet = vt.getCurrentViewPane().getGameSessionViewer().getPacket(id);
			if(packet != null)
			{
				String fname = packet.getName();

				if(fname == null || fname.isEmpty())
				{
					fname = String.format("{0}_unk", packet.getPacketId());
				}
				File file = new File(fname + ".bin");
				try {
					RandomAccessFile raf = new RandomAccessFile(file, "rw");
					raf.write(packet.getIdData());
					raf.write(packet.getData());
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void toggleSessionItems(boolean enabled) {
		_itemClose.setEnabled(enabled);
		_itemCloseAll.setEnabled(enabled);
		_itemSearch.setEnabled(enabled);
		_itemSearchNext.setEnabled(enabled);
		_itemGoto.setEnabled(enabled);
		_itemFilter.setEnabled(enabled);
	}

	public void closeSessionTab(ViewPane vp) {
		Session s = vp.getGameSessionViewer().getSession();
		if (s != null) {
			s.setShown(false);
		}
		getViewerTab().remove(vp);
		if (getViewerTab().getComponentCount() == 0) {
			this.toggleSessionItems(false);
		}
	}

	public void setFileLogging(boolean b) {
		System.out.println("Writelog: " + b);
		_writeLog = b;
		
	}
}
