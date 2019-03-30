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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.logrepo.RemoteLogRepositoryBackend;


/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
public class MainRepoTab extends JPanel implements ActionListener
{
    // account
    private JLabel _userLabel = new JLabel("User: ");
    private JTextField _userField = new JTextField();
    private JLabel _passLabel = new JLabel("Pass: ");
    private JPasswordField _passField = new JPasswordField();
    private JCheckBox _saveCheck = new JCheckBox("Save");
    private JButton _enableButton = new JButton("Enable");

    // proxy
    private JLabel _hostLabel = new JLabel("Host: ");
    private JTextField _hostField = new JTextField();
    private JLabel _portLabel = new JLabel("Port: ");
    private JTextField _portField = new JTextField();
    private JCheckBox _proxyEnableCheck = new JCheckBox("Enable");
    
    public MainRepoTab()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridwidth = 1;
        
        _enableButton.addActionListener(this);
        _enableButton.setActionCommand("enable");
        
        JPanel accPanel = new JPanel();
        accPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Account"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        
        
        accPanel.setLayout(new GridLayout(0,2));
        cons.anchor = GridBagConstraints.NORTHWEST;
        accPanel.add(_userLabel);
        accPanel.add(_userField);
        accPanel.add(_passLabel);
        accPanel.add(_passField);
        accPanel.add(_saveCheck);
        accPanel.add(_enableButton);
        
        //this.add(accPanel, cons);
        
        
        //cons.gridy = 1;
        _proxyEnableCheck.setActionCommand("enableProxy");
        _proxyEnableCheck.addActionListener(this);
        
        
        JPanel proxyPanel = new JPanel();
        proxyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("HTTP Proxy"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        proxyPanel.setLayout(new GridLayout(0,2));
        
        proxyPanel.add(_hostLabel);
        _hostField.setPreferredSize(new Dimension(120,_hostField.getHeight()));
        proxyPanel.add(_hostField);
        proxyPanel.add(_portLabel);
        proxyPanel.add(_portField);
        proxyPanel.add(_proxyEnableCheck);
        this.add(accPanel, cons);
        cons.gridx = 1;
        cons.gridwidth = 2;
        cons.weightx = 0.5;
        this.add(proxyPanel, cons);
        
        
        // omg XXX
        
        cons.gridx = 0;
        cons.ipadx = 60;
        cons.weightx = 0.5;
        cons.weighty = 1.0;
        cons.gridy = 1;
        cons.gridwidth = 4;
        this.add(new JLabel(""), cons);
        
        this.enableSavedAccount();
        this.enableSavedProxy();
    }

    private String getUserName()
    {
        return _userField.getText();
    }

    private String getPassword()
    {
        return String.valueOf(_passField.getPassword());
    }
    
    public boolean enableSavedAccount()
    {
        String repoUser = PacketSamurai.getConfigProperty("RepositoryUser");
        String repoPass = PacketSamurai.getConfigProperty("RepositoryPass");
        if (repoUser.length() > 0 && repoPass.length() > 0)
        {
            RemoteLogRepositoryBackend.getInstance().setUserName(repoUser);
            _userField.setText(repoUser);
            RemoteLogRepositoryBackend.getInstance().setPasswordHashed(repoPass);
            _passField.setText("12345678"); // let it look as filled
            _saveCheck.setSelected(true);
            this.enableGUI();
            return true;
        }
        return false;
    }
    
    public void enableSavedProxy()
    {
        String repoProxyHost = PacketSamurai.getConfigProperty("RepositoryProxyHost", "");
        String repoProxyPort = PacketSamurai.getConfigProperty("RepositoryProxyPort", "");
        String repoProxyActive = PacketSamurai.getConfigProperty("RepositoryProxyActive", "False");
        boolean proxyActive = Boolean.parseBoolean(repoProxyActive);
        if (repoProxyHost.length() > 0 && repoProxyPort.length() > 0)
        {
            _hostField.setText(repoProxyHost);
            _portField.setText(repoProxyPort);
            if (proxyActive)
            {
                this.enableProxy();
            }
        }
    }
    
    public void enableGUI()
    {
        MainRepoTab.this._userField.setEditable(false);
        MainRepoTab.this._passField.setEditable(false);
        _saveCheck.setEnabled(false);
        MainRepoTab.this._enableButton.setText("Disable");
        MainRepoTab.this._enableButton.setActionCommand("disable");
    }
    
    public void setProxyEditable(boolean val)
    {
        _hostField.setEditable(val);
        _portField.setEditable(val);
    }

    public void enableProxy()
    {
        try
        {
            String host = MainRepoTab.this._hostField.getText();
            int port = Integer.parseInt(MainRepoTab.this._portField.getText());
            this.setProxyEditable(false);
            RemoteLogRepositoryBackend.getInstance().setProxy(host, port);
            _proxyEnableCheck.setActionCommand("disableProxy");
            _proxyEnableCheck.setSelected(true);
            PacketSamurai.setConfigProperty("RepositoryProxyHost", host);
            PacketSamurai.setConfigProperty("RepositoryProxyPort", port);
            PacketSamurai.setConfigProperty("RepositoryProxyActive", "True");
        }
        catch (NumberFormatException e1)
        {
            JOptionPane.showMessageDialog(MainRepoTab.this, "Invalid port", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void disableProxy()
    {
        this.setProxyEditable(true);
        _proxyEnableCheck.setActionCommand("enableProxy");
        RemoteLogRepositoryBackend.getInstance().setProxy("", -1);
        PacketSamurai.setConfigProperty("RepositoryProxyActive", "False");
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("enable"))
        {
            if (!enableSavedAccount())
            {
                RemoteLogRepositoryBackend.getInstance().setUserName(MainRepoTab.this.getUserName());
                RemoteLogRepositoryBackend.getInstance().setPassword(MainRepoTab.this.getPassword());

                if (_saveCheck.isSelected())
                {
                    this.enableGUI();

                    PacketSamurai.setConfigProperty("RepositoryUser", MainRepoTab.this.getUserName());
                    PacketSamurai.setConfigProperty("RepositoryPass", RemoteLogRepositoryBackend.getInstance().getPassword());
                }
                else
                {
                    PacketSamurai.setConfigProperty("RepositoryUser", "");
                    PacketSamurai.setConfigProperty("RepositoryPass", "");
                }
            }
        }
        else if (cmd.equals("disable"))
        {
            RemoteLogRepositoryBackend.getInstance().setUserName(null);
            RemoteLogRepositoryBackend.getInstance().setPassword(null);
            MainRepoTab.this._userField.setEditable(true);
            MainRepoTab.this._passField.setEditable(true);
            _saveCheck.setEnabled(true);
            MainRepoTab.this._enableButton.setText("Enable");
            MainRepoTab.this._enableButton.setActionCommand("enable");
        }
        else if (cmd.equals("save"))
        {
            if (!_saveCheck.isSelected())
            {
                PacketSamurai.setConfigProperty("RepositoryUser", "");
                PacketSamurai.setConfigProperty("RepositoryPass", "");
            }
        }
        else if (cmd.equals("enableProxy"))
        {
            MainRepoTab.this.enableProxy();
        }
        else if (cmd.equals("disableProxy"))
        {
            MainRepoTab.this.disableProxy();
        }
    }
}
