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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import com.aionemu.packetsamurai.protocol.Protocol;
import com.aionemu.packetsamurai.protocol.ProtocolManager;


@SuppressWarnings("serial")
/**
 * 
 * @author Gilles Duboscq
 * 
 */
public class ProtocolEditor extends JDialog 
{
	private Protocol _currentProtocol;
	private ProtocolTab _clientTab;
	private ProtocolTab _serverTab;
    private JMenu _protocolMenu;
    private ProtocolEditorListener _pel;

    public ProtocolEditor(JFrame frame)
	{
		super(frame);
		setTitle("Packet Samurai - Protocol Editor");
		setSize(800, 600);
		setLayout(new BorderLayout());
        
        _clientTab = new ProtocolTab();
        _serverTab = new ProtocolTab();
        
		//menus
		JMenuBar menuBar = new JMenuBar();
		//action listener
        _pel = new ProtocolEditorListener(this);
		
		// * File menu
		JMenu fileMenu = new JMenu("File");
        
        //   * Reload Button
        JMenuItem reloadButton = new JMenuItem("Reload");
        reloadButton.setActionCommand("reload");
        reloadButton.addActionListener(_pel);
        //   * Save Button
		JMenuItem saveButton = new JMenuItem("Save");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(_pel);
        
        fileMenu.add(reloadButton);
		fileMenu.add(saveButton);
		
		// * Protocol menu
		_protocolMenu = new JMenu("Chose Protocol");

        loadProtocols();
        
		JMenu editMenu = new JMenu("Edit");
        JMenuItem protoPorpertyButton = new JMenuItem("Protocol Properties");
        protoPorpertyButton.setActionCommand("properties");
        protoPorpertyButton.addActionListener(_pel);
        
        editMenu.add(protoPorpertyButton);
		
		menuBar.add(fileMenu);
		menuBar.add(_protocolMenu);
		menuBar.add(editMenu );
		
		
		setJMenuBar(menuBar);
		
		// tabs
		JTabbedPane tabPane  = new JTabbedPane();

   		tabPane.add(_clientTab);
   		tabPane.add(_serverTab);
		add(tabPane);
	}
    
    public Protocol getCurrentProtocol()
    {
        return _currentProtocol;
    }
    
    public void loadProtocols()
    {
        _protocolMenu.removeAll();
        for(Protocol proto : ProtocolManager.getInstance().getProtocols())
        {
            JMenuItem protoButton = new JMenuItem(proto.getName()+" ("+proto.getPort()+")");
            protoButton.setActionCommand("p:"+proto.getName());
            protoButton.addActionListener(_pel);
            _protocolMenu.add(protoButton);
        }
        JMenuItem newProtoButton = new JMenuItem("New Protocol...");
        newProtoButton.setActionCommand("new");
        newProtoButton.addActionListener(_pel);
        _protocolMenu.addSeparator();
        _protocolMenu.add(newProtoButton);
        if(!ProtocolManager.getInstance().getProtocols().isEmpty())
            switchToProtocol(ProtocolManager.getInstance().getProtocols().iterator().next());
        else
        	switchToProtocol(null);
    }
    
    public void switchToProtocol(Protocol p)
    {
    	if(p == null)
    	{
    		_currentProtocol = null;
    		_clientTab.changeFamilly(null);
	        _serverTab.changeFamilly(null);
    	}
    	else
    	{
	        _currentProtocol = p;
	        _clientTab.changeFamilly(p.getClientPacketsFamilly());
	        _serverTab.changeFamilly(p.getServerPacketsFamilly());
    	}
    }
	
	private static class ProtocolEditorListener implements ActionListener
	{
        private ProtocolEditor _pEditor;
		
		public ProtocolEditorListener(ProtocolEditor pe)
		{
            _pEditor = pe;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String cmd = e.getActionCommand();
            System.out.println(cmd);
			if(cmd.startsWith("p:"))
			{
                String pName = cmd.substring(2);
			    _pEditor.switchToProtocol(ProtocolManager.getInstance().getProtocolByName(pName));
			}
            else if(cmd.equals("new"))
            {
                // dialog
            }
            else if(cmd.equals("properties"))
            {
                // dialog
            }
			else if(cmd.equals("save"))
			{
				Protocol currentProtocol = _pEditor.getCurrentProtocol();
				if(currentProtocol != null)
					currentProtocol.saveProtocol();
			}
            else if(cmd.equals("reload"))
            {
                ProtocolManager.getInstance().loadProtocols();
                _pEditor.loadProtocols();
            }
		}
	}
}