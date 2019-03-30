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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.logrepo.LogFile;

import javolution.util.FastMap;

/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
public class TransferPanel extends JPanel 
{
    private static final String[] COLUMN_NAMES =
    {
        "File",
        "Progress",
    };

    private TranferTableModel _transferTableModel = new TranferTableModel();
    private JTable _transferTable = new JTable(_transferTableModel);
    private boolean _isDownloads;

    public TransferPanel(String name, boolean isDownloads)
    {
        _isDownloads = isDownloads;
        _transferTable.setDefaultRenderer(Object.class, _transferTableModel);

        this.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(name),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        this.add(new JScrollPane(_transferTable), cons);
    }

    public JTable getTranferTable()
    {
        return _transferTable;
    }

    public void updateTransferProgressText(LogFile logFile, String newText)
    {
        _transferTableModel.getJProgressBar(logFile.getName()).setString(newText);
        SwingUtilities.invokeLater
        (
                new Runnable(){

                    public void run()
                    {
                        TransferPanel.this.getTranferTable().updateUI();
                    }
                }
        );
    }

    public void updateTransferProgress(LogFile logFile, int progress)
    {
        JProgressBar jpb = _transferTableModel.getJProgressBar(logFile.getName());
        jpb.setValue(progress);
        jpb.setString(progress+"%");
        SwingUtilities.invokeLater
        (
                new Runnable(){

                    public void run()
                    {
                        TransferPanel.this.getTranferTable().updateUI();
                    }
                }
        );
    }

    public void transferFinished(LogFile logFile, boolean success)
    {
        _transferTableModel.removeRow(logFile.getName());
        
        if (success)
        {
            LogFilesTab logFilesTab = ((Main)PacketSamurai.getUserInterface()).getLogRepoTab().getLogFilesTab();

            if (_isDownloads)
            {
                logFilesTab.removeLogFile(logFile);
                logFile.setLocal(true);
                logFilesTab.addLogFile(logFile);
            }
        }
        
        SwingUtilities.invokeLater
        (
                new Runnable(){

                    public void run()
                    {
                        TransferPanel.this.getTranferTable().updateUI();
                    }
                }
        );
    }

    public void addRow(JLabel filename, JProgressBar progress)
    {
        ((DefaultTableModel)_transferTable.getModel()).addRow(new Object[]{filename, progress});
    }

    class TranferTableModel extends DefaultTableModel implements TableCellRenderer
    {
        private Map<String, Integer> _tranfers = new FastMap<String, Integer>();

        protected int getRowByFileName(String filename)
        {
            return _tranfers.get(filename);
        }

        public JProgressBar getJProgressBar(String filename)
        {
            JProgressBar jpg = (JProgressBar) this.getValueAt(this.getRowByFileName(filename), 1);
            return jpg;
        }

        @Override
        public void addRow(Object[] rowData)
        {
            super.addRow(rowData);
            _tranfers.put(((JLabel) rowData[0]).getText(), this.getRowCount()-1);
        }
        
        public void removeRow(String filename)
        {
            int row = _transferTableModel.getRowByFileName(filename);
            _tranfers.remove(filename);
            super.removeRow(row);
        }

        @Override
        public String getColumnName(int col)
        {
            return COLUMN_NAMES[col];
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (value instanceof JComponent)
            {
                JComponent c = (JComponent) value;
                /*if (isSelected)
                {
                    c.setForeground(table.getSelectionForeground());
                    c.setBackground(table.getSelectionBackground());
                }*/
                return c;
            }
            return null;
        }
    }
}
