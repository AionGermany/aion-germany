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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aionemu.packetsamurai.logrepo.LogFile;
import com.aionemu.packetsamurai.logrepo.LogRepository;
import com.aionemu.packetsamurai.logrepo.RemoteLogRepositoryBackend;


/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
class LogDetailsPanel extends JPanel
{
    private JLabel _fileNameLabel = new JLabel("Filename: ");
    private JLabel _fileStatusLabel = new JLabel("Local: ");
    private JLabel _fileInfoLabel = new JLabel("");
    
    private JButton _saveButton = new JButton("Save");
    private JEditorPane _commentEditor = new JEditorPane();
    
    private LogFile _logFile;
    
    public LogDetailsPanel(String name)
    {
        _saveButton.setEnabled(false);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(name),
                BorderFactory.createEmptyBorder(2,2,2,2)));
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        
        _saveButton.addActionListener(new SaveButtonListener());
        _saveButton.setActionCommand("save");
        
        cons.insets = new Insets(2,2,2,2);
        cons.anchor = GridBagConstraints.NORTHWEST;
        this.add(_fileNameLabel, cons);
        cons.gridx = 1;
        cons.anchor = GridBagConstraints.NORTHEAST;
        this.add(_saveButton, cons);
        cons.fill = GridBagConstraints.BOTH;
        cons.anchor = GridBagConstraints.NORTHWEST;
        cons.gridx = 0;
        cons.gridy = 1;
        this.add(_fileStatusLabel, cons);
        cons.gridy = 2;
        this.add(_fileInfoLabel, cons);
        
        cons.weighty = 0.5;
        cons.gridy = 3;
        cons.gridheight = 5;
        this.add(new JScrollPane(_commentEditor), cons);
    }
    
    public void showDetails(LogFile logFile)
    {
        _saveButton.setEnabled(true);
        _logFile = logFile;
        this.setFileName(logFile.getName());
        this.setStatus(logFile.isLocal(), logFile.isRemote());
        this.setFileInfo(logFile);
        this.setCommentText(logFile.getComments());
    }
    
    protected void setFileName(String name)
    {
        _fileNameLabel.setText("Filename: "+name);
    }
    
    protected void setStatus(boolean isLocal, boolean isRemote)
    {
        _fileStatusLabel.setText("<html>Local: <font color="+(isLocal ? "green" : "red")+">"+(isLocal ? "YES" : "NO")+"</font> Remote: <font color="+(isRemote ? "green" : "red")+">"+(isRemote ? "YES" : "NO")+"</font></html>");
    }
    
    protected void setFileInfo(LogFile logFile)
    {
        String lastModified;
        String uploaded;
        if (logFile.isLocal())
        {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTimeInMillis(logFile.getFile().lastModified());
            lastModified = DateFormat.getInstance().format(calendar.getTime());
            if (logFile.isRemote())
            {
                calendar.setTimeInMillis(logFile.getUploadedTime());
                uploaded = DateFormat.getInstance().format(calendar.getTime());
            }
            else
            {
                uploaded = "N/A";
            }
        }
        else
        {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTimeInMillis(logFile.getUploadedTime());
            uploaded = DateFormat.getInstance().format(calendar.getTime());
            lastModified = "N/A";
        }
        _fileInfoLabel.setText("<html><font color=blue>Filesize:</font> "+logFile.getSize()+" bytes&nbsp<font color=blue>Last-Modified:</font> "+lastModified+"&nbsp<font color=blue>Uploaded:</font> "+uploaded+"</html>");
    }
    
    protected void setCommentText(String text)
    {
        _commentEditor.setText(text);
    }
    
    class SaveButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String cmd = e.getActionCommand();
            if (cmd.equals("save"))
            {
                _logFile.setComments(_commentEditor.getText());
            }
            Thread update = new Thread
            ( 
                    new Runnable()
                    {

                        public void run()
                        {
                            RemoteLogRepositoryBackend.getInstance().updateLogDetails(_logFile);
                        }

                    }
            );
            update.setName("LogFile Details Updater Thread");
            update.start();
            LogRepository.getInstance().runSaveDatabase();
        }
    }
}
