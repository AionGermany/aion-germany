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
package com.aionemu.packetsamurai.gui.logrepo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import javolution.util.FastMap;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.gui.ViewTab;
import com.aionemu.packetsamurai.gui.images.IconsTable;
import com.aionemu.packetsamurai.logrepo.LogFile;
import com.aionemu.packetsamurai.logrepo.LogRepository;

/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
public class FileSystemPanel extends JPanel implements ActionListener
{
    private JTextField _quickFilter = new JTextField("QuickFilter");
    private JButton _refreshLocalButton = new JButton(IconsTable.ICON_REFRESH);
    private JButton _refreshRemoteButton = new JButton(IconsTable.ICON_REFRESH_GREEN);
    private JXTreeTable _fileTree = new JXTreeTable(new SimpleTreeModel(new DefaultMutableTreeTableNode()));
    private JLabel _statsLabel = new JLabel("");
    private Map<String, DefaultMutableTreeTableNode> _categoriesLocal = new FastMap<String, DefaultMutableTreeTableNode>();
    private Map<String, DefaultMutableTreeTableNode> _categoriesRemote = new FastMap<String, DefaultMutableTreeTableNode>();
    private DefaultMutableTreeTableNode _local, _remote;
    
    public FileSystemPanel(String name)
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();

        getFileTree().setTreeCellRenderer(new LogTreeRenderer());
        getFileTree().getColumnModel().getColumn(0).setPreferredWidth(140);
        getFileTree().getColumnModel().getColumn(1).setPreferredWidth(50);
        getFileTree().getColumnModel().getColumn(0).setPreferredWidth(100);
        getFileTree().addMouseListener(new LogTreeMouseAdpater());

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(name),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        cons.anchor = GridBagConstraints.NORTHWEST;
        cons.fill = GridBagConstraints.BOTH;
        cons.insets = new Insets(2,2,2,2);
        cons.weightx = 1.0;
        this.add(_quickFilter, cons);


        cons.fill = GridBagConstraints.NONE;
        
        _refreshLocalButton.setPreferredSize(new Dimension(_refreshLocalButton.getIcon().getIconWidth()+9, _refreshLocalButton.getIcon().getIconHeight()+9));
        _refreshLocalButton.setToolTipText("Refresh Local Logs");
        _refreshLocalButton.setActionCommand("refreshLocal");
        _refreshLocalButton.addActionListener(this);
        this.add(_refreshLocalButton);
        
        _refreshRemoteButton.setPreferredSize(new Dimension(_refreshRemoteButton.getIcon().getIconWidth()+9, _refreshRemoteButton.getIcon().getIconHeight()+9));
        _refreshRemoteButton.setToolTipText("Refresh Remote Logs");
        _refreshRemoteButton.setActionCommand("refreshRemote");
        _refreshRemoteButton.addActionListener(this);
        this.add(_refreshRemoteButton);
        
        cons.fill = GridBagConstraints.BOTH;

        cons.gridwidth = 3;
        cons.weighty = 1.0;
        cons.gridy = 1;
        this.add(new JScrollPane(getFileTree()), cons);
        cons.weighty = 0.0;
        cons.gridy = 2;
        this.add(_statsLabel, cons);
        
        DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) this.getFileTree().getTreeTableModel().getRoot();
        _local = new DefaultMutableTreeTableNode("Local Logs");
        _remote = new DefaultMutableTreeTableNode("Remote Logs");
        DefaultTreeTableModel model = (DefaultTreeTableModel) this.getFileTree().getTreeTableModel();
        model.insertNodeInto(_local, root, root.getChildCount());
        model.insertNodeInto(_remote, root, root.getChildCount());
    }

    public void addLogFile(LogFile logFile)
    {
        DefaultMutableTreeTableNode cat = null;
        
        if (logFile.isLocal())
        {
            if ((cat = _categoriesLocal.get(logFile.getProtocolName())) == null)
            {
                cat = new DefaultMutableTreeTableNode(logFile.getProtocolName());
                _categoriesLocal.put(logFile.getProtocolName(), cat);
                _local.insert(cat, _local.getChildCount());
            }
        }
        else if (logFile.isRemote())
        {
            if ((cat = _categoriesRemote.get(logFile.getProtocolName())) == null)
            {
                cat = new DefaultMutableTreeTableNode(logFile.getProtocolName());
                _categoriesRemote.put(logFile.getProtocolName(), cat);
                _remote.insert(cat, _remote.getChildCount());
            }
        }
        else
        {
            // GO IN PANIC
        	PacketSamurai.getUserInterface().log("Warning : Unable to find proper category for log file "+logFile.getName());
        	return;
        }
        
        cat.insert(new LogFileNode(logFile), cat.getChildCount());
        SwingUtilities.invokeLater
        (
                new Runnable()
                {
                    public void run()
                    {
                        getFileTree().updateUI();
                    }
                }
        );
    }
    
    public boolean removeLogFile(LogFile logFile)
    {
        return this.removeLogFile((DefaultMutableTreeTableNode) getFileTree().getTreeTableModel().getRoot(), logFile);
    }
    
    public boolean removeLogFile(DefaultMutableTreeTableNode parent, LogFile logFile)
    {
        int size = parent.getChildCount();
        int i = 0;
        boolean found = false;
        while (i < size && !found)
        {
            if (parent.getChildAt(i).isLeaf())
            {
                LogFileNode logNode = (LogFileNode) parent.getChildAt(i);
                // there should be only 1 LogFile representing a file thus
                // == can be used
                if (logNode.getLogFile() == logFile)
                {
                    parent.remove(i);
                    found = true;
                }
            }
            else
            {
                if (this.removeLogFile((DefaultMutableTreeTableNode) parent.getChildAt(i), logFile))
                {
                    found = true;
                    break;
                }
            }
            
            i++;
        }

        if (found)
        {
            SwingUtilities.invokeLater
            (
                    new Runnable()
                    {
                        public void run()
                        {
                            getFileTree().updateUI();
                        }
                    }
            );
        }
        
        return found;
    }

    protected void setFileTree(JXTreeTable fileTree)
    {
        _fileTree = fileTree;
    }

    public JXTreeTable getFileTree()
    {
        return _fileTree;
    }

    public JButton getRefreshButton()
    {
        return _refreshLocalButton;
    }

    private static final String[] COLUMN_NAMES =
    {
        "Name",
        "Size",
        "Uploader"
    };

    class SimpleTreeModel extends DefaultTreeTableModel
    {
        public SimpleTreeModel(TreeTableNode root)
        {
            super(root);
        }
        
        @Override
        public boolean isCellEditable(Object arg0, int arg1)
        {
            return false;
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }
        
        @Override
        public String getColumnName(int col)
        {
            return COLUMN_NAMES[col];
        }

        @Override
        public Object getValueAt(Object node, int column)
        {
            if (node instanceof LogFileNode)
            {
                LogFileNode logNode = ((LogFileNode)node);
                switch(column)
                {
                    case 0:
                        return logNode.getLogFile().getName();
                    case 1:
                        long size = logNode.getLogFile().getSize();
                        if (size > 0 && size < 1024)
                        {
                            return "< 1 KB";
                        }
                        return (size/1024)+" KB";
                    case 2:
                        return logNode.getLogFile().getRemoteUploader();
                    default:
                        return "";
                }
            }
            DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
            switch(column)
            {
                case 0:
                    return defNode.toString();
                default:
                    return null;
            }
        }
    }

    class LogFileNode extends DefaultMutableTreeTableNode
    {
        private LogFile _logFile;

        public LogFileNode(LogFile logFile)
        {
            _logFile = logFile;
        }

        public LogFile getLogFile()
        {
            return _logFile;
        }

        @Override
        public boolean isLeaf()
        {
            return true;
        }
    }

    class LogTreeRenderer extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus)
        {
            
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof LogFileNode)
            {
                this.setText(((LogFileNode) value).getLogFile().getName());
                this.setIcon(IconsTable.ICON_FILE);
            }
            return this;
        }
    }

    class LogTreeMouseAdpater extends MouseAdapter implements ActionListener
    {
        private JPopupMenu _popupMenu;
        private LogFileNode _currentNode;

        public LogTreeMouseAdpater()
        {
            _popupMenu = new JPopupMenu();
            
            JMenuItem itemOpenGo = new JMenuItem("Open on Viewer & Go");
            itemOpenGo.setActionCommand("opengo");
            itemOpenGo.addActionListener(this);
            _popupMenu.add(itemOpenGo);
            
            JMenuItem itemOpen = new JMenuItem("Open on Viewer");
            itemOpen.setActionCommand("open");
            itemOpen.addActionListener(this);
            _popupMenu.add(itemOpen);
        }

        public void mousePressed(MouseEvent e)
        {
            //showPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            showPopup(e);
        }

        private void showPopup(MouseEvent e)
        {

            if (e.isPopupTrigger())
            {
                JXTreeTable treeTable = (JXTreeTable) e.getSource();
                TreePath tp = treeTable.getPathForLocation(e.getX(), e.getY());
                if (tp != null && (tp.getLastPathComponent()) instanceof LogFileNode)
                {
                    _currentNode = (LogFileNode) tp.getLastPathComponent();
                    if (_currentNode.getLogFile().isLocal())
                    {
                        _popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
            else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
            {
                JXTreeTable treeTable = (JXTreeTable) e.getSource();
                TreePath tp = treeTable.getPathForLocation(e.getX(), e.getY());
                if (tp != null && (tp.getLastPathComponent()) instanceof LogFileNode)
                {
                    LogFileNode logNode = (LogFileNode) tp.getLastPathComponent();
                    LogFilesTab logFilesTab = ((Main) PacketSamurai.getUserInterface()).getLogRepoTab().getLogFilesTab();
                    if (logNode.getLogFile().isLocal() && !logNode.getLogFile().isRemote())
                    {
                        logFilesTab.addLogUpload(logNode.getLogFile());
                    }
                    else if (logNode.getLogFile().isRemote() && !logNode.getLogFile().isLocal())
                    {
                        logFilesTab.addLogDownload(logNode.getLogFile());
                    }
                }
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            String cmd = e.getActionCommand();
            if (cmd.equals("open"))
            {
                Thread t = new Thread()
                {
                    public void run()
                    {
                        LogFile logFile = _currentNode.getLogFile();
                        if (logFile.isLocal())
                        {
                            ((Main) PacketSamurai.getUserInterface()).openSession(logFile.getFile());
                        }
                    }
                };
                t.start();
            }
            else if (cmd.equals("opengo"))
            {
                Thread t = new Thread()
                {
                    public void run()
                    {
                        LogFile logFile = _currentNode.getLogFile();
                        if (logFile.isLocal())
                        {
                            ((Main) PacketSamurai.getUserInterface()).openSession(logFile.getFile());
                            ViewTab vt = ((Main) PacketSamurai.getUserInterface()).getViewerTab();
                            vt.setSelectedIndex(vt.getComponentCount()-1);
                            ((Main) PacketSamurai.getUserInterface()).getTabPane().setSelectedComponent(vt);
                        }
                    }
                };
                t.start();
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        
        if (cmd.equals("refreshLocal"))
        {
            LogRepository.getInstance().refreshLocalLogs();
        }
        else if (cmd.equals("refreshRemote"))
        {
            LogRepository.getInstance().refreshRemoteLogs();
        }
    }
}
