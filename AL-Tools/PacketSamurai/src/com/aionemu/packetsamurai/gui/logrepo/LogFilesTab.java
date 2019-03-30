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


package com.aionemu.packetsamurai.gui.logrepo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;


import org.jdesktop.swingx.JXTreeTable;

import com.aionemu.packetsamurai.gui.logrepo.FileSystemPanel.LogFileNode;
import com.aionemu.packetsamurai.logrepo.LogFile;
import com.aionemu.packetsamurai.logrepo.LogRepository;
import com.aionemu.packetsamurai.logrepo.RemoteLogRepositoryBackend;

/**
 * 
 * @author Ulysses R. Ribeiro
 * 
 */
@SuppressWarnings("serial")
public class LogFilesTab extends JPanel implements ComponentListener
{
    private JSplitPane _outerSplitPane;
    
    private FileSystemPanel _localFileSystem = new FileSystemPanel("Logs Filesystem");
    
    private LogDetailsPanel _logDetailsPane = new LogDetailsPanel("Log Details");
    private MiddlePanel _middlePanel = new MiddlePanel();
    
    private boolean _localLoaded, _remoteLoaded;
    
    public LogFilesTab()
    {
        
        _localFileSystem.getFileTree().getSelectionModel().addListSelectionListener(new LogSelectionListener(_localFileSystem.getFileTree()));
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        
        this.addComponentListener(this);
        
        _outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        _outerSplitPane.setLeftComponent(getLocalFileSystemPanel());
        _outerSplitPane.setRightComponent(_middlePanel);
        
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0;
        cons.weighty = 1.0;

        // same weight for each size
        _outerSplitPane.setResizeWeight(0.5);
        
        this.add(_outerSplitPane, cons);
        
        // makes each side with same desired size so that the split happens exactly in the middle
        this.getLocalFileSystemPanel().setPreferredSize(_middlePanel.getPreferredSize());
    }
    
    class MiddlePanel extends JPanel
    {
        private JSplitPane _outSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        private JSplitPane _inSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        private TransferPanel _downloadsPanel = new TransferPanel("Downloads", true);
        private TransferPanel _uploadsPanel = new TransferPanel("Uploads", false);
        
        public MiddlePanel()
        {
            this.setLayout(new GridBagLayout());
            GridBagConstraints cons = new GridBagConstraints();
            
            RemoteLogRepositoryBackend.getInstance().setDownloadListener(_downloadsPanel);
            RemoteLogRepositoryBackend.getInstance().setUploadListener(_uploadsPanel);
            
            _inSplitPane.setTopComponent(_downloadsPanel);
            _inSplitPane.setBottomComponent(_uploadsPanel);
            _inSplitPane.setResizeWeight(0.5);
            
            _outSplitPane.setTopComponent(_logDetailsPane);
            _outSplitPane.setBottomComponent(_inSplitPane);
            _outSplitPane.setResizeWeight(0.6);
            
            cons.fill = GridBagConstraints.BOTH;
            cons.weightx = 1.0;
            cons.weighty = 1.0;
            this.add(_outSplitPane, cons);
        }
    }
    
    public void showLocalLogs()
    {
        for (LogFile logFile : LogRepository.getInstance().getLocalLogs())
        {
            try
            {
                logFile.loadHeaders();
                this.getLocalFileSystemPanel().addLogFile(logFile);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, "Failed Loading/Adding local log ("+logFile.getName()+"), skipped.\nReason: "+e.getLocalizedMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    public void addLogDownload(LogFile logFile)
    {
        if (RemoteLogRepositoryBackend.getInstance().isConnected())
        {
            RemoteLogRepositoryBackend.getInstance().enqueueDownload(logFile);
            JProgressBar jpb = new JProgressBar();
            jpb.setStringPainted(true);
            jpb.setString("Requesting");
            this._middlePanel._downloadsPanel.addRow(new JLabel(logFile.getName()), jpb);
        }
    }
    
    public void addLogUpload(LogFile logFile)
    {
        if (RemoteLogRepositoryBackend.getInstance().isConnected())
        {
            RemoteLogRepositoryBackend.getInstance().enqueueUpload(logFile);
            JProgressBar jpb = new JProgressBar();
            jpb.setStringPainted(true);
            jpb.setString("Requesting");
            this._middlePanel._uploadsPanel.addRow(new JLabel(logFile.getName()), jpb);
        }
    }

    public FileSystemPanel getLocalFileSystemPanel()
    {
        return _localFileSystem;
    }

    public void addLogFile(LogFile logFile)
    {
        _localFileSystem.addLogFile(logFile);
    }
    
    public boolean removeLogFile(LogFile logFile)
    {
        return _localFileSystem.removeLogFile(logFile);
    }
    
    public void componentShown(ComponentEvent e)
    {
        synchronized (this)
        {
            // load
            LogRepository.getInstance();
            
            if (!_localLoaded)
            {
                Thread t = new Thread()
                {
                    public void run()
                    {
                        LogRepository.getInstance().refreshLocalLogs();
                        LogFilesTab.this.showLocalLogs();
                        _localLoaded = true;
                    }
                };
                t.start();
            }
            if (RemoteLogRepositoryBackend.getInstance().isConnected() && !_remoteLoaded)
            {
                Thread t = new Thread()
                {
                    public void run()
                    {
                        LogRepository.getInstance().refreshRemoteLogs();
                        _remoteLoaded = true;
                    }
                };
                t.start();
            }
        }
    }
    
    public void componentResized(ComponentEvent e)
    {
        //nothing
    }
    
    public void componentMoved(ComponentEvent e)
    {
        //nothing
    }
    
    public void componentHidden(ComponentEvent e)
    {
        //nothing
    }

    public class LogSelectionListener implements ListSelectionListener
    {
        private JXTreeTable _table;

        public LogSelectionListener(JXTreeTable table)
        {
            _table = table;
        }
        
        public void valueChanged(ListSelectionEvent e)
        {
            TreePath tp = _table.getPathForRow(_table.getSelectedRow());
            if (tp != null && tp.getLastPathComponent() instanceof LogFileNode)
            {
                _logDetailsPane.showDetails(((LogFileNode) tp.getLastPathComponent()).getLogFile());
            }
        }

    }
    
}