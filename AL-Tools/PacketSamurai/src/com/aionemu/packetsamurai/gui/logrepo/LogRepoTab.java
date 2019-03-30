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

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class LogRepoTab extends JTabbedPane
{
    private MainRepoTab _mainRepoTab;
    private LogFilesTab _logFilesTab;
    private RepoUsersTab _repoUsersTab;

    public LogRepoTab()
    {
        _mainRepoTab = new MainRepoTab();
        _logFilesTab = new LogFilesTab();
        _repoUsersTab = new RepoUsersTab();
        
        this.add("Main",_mainRepoTab);
        this.add("Logs",_logFilesTab);
        this.add("Users",_repoUsersTab);
    }
    
    public LogFilesTab getLogFilesTab()
    {
        return _logFilesTab;
    }
    
    public RepoUsersTab getRepoUserTab()
    {
        return _repoUsersTab;
    }
}