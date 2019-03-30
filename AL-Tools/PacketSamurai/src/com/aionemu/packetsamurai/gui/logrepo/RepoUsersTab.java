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

import java.awt.Component;
import java.util.EventObject;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.aionemu.packetsamurai.logrepo.RepoUser;


@SuppressWarnings("serial")
public class RepoUsersTab extends JPanel
{
    private JTable _userTable;
    private UserTableModel _userTableModel;
    
    public RepoUsersTab()
    {
        _userTableModel = new UserTableModel();
        _userTableModel.addColumn(new TableColumn(0,75,null,new UserTableCellEditor()));
        _userTable = new JTable();
        _userTable.setColumnModel(_userTableModel);
        //this.add(_userTable);
    }
    
    public void addUsers(List<RepoUser> users)
    {
        // TODO...
    }
    
    public void addUser(RepoUser user)
    {
        // TODO...
    }
    
    public void removeUser(RepoUser user)
    {
        // TODO
    }
    
    private class UserTableModel extends DefaultTableColumnModel
    {
        
    }
    
    private class UserTableCellEditor implements TableCellEditor
    {

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
        {
            // TODO Auto-generated method stub
            return null;
        }

        public void addCellEditorListener(CellEditorListener l)
        {
            // TODO Auto-generated method stub
            
        }

        public void cancelCellEditing()
        {
            // TODO Auto-generated method stub
            
        }

        public Object getCellEditorValue()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isCellEditable(EventObject anEvent)
        {
            // TODO Auto-generated method stub
            return false;
        }

        public void removeCellEditorListener(CellEditorListener l)
        {
            // TODO Auto-generated method stub
            
        }

        public boolean shouldSelectCell(EventObject anEvent)
        {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean stopCellEditing()
        {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
}