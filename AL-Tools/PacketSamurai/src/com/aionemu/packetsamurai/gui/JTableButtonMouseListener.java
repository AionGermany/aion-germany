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
package com.aionemu.packetsamurai.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 * @author Ulysses R. Ribeiro
 *
 */
public class JTableButtonMouseListener implements MouseListener
{
    private JTable _table;

    private void forwardEvent(MouseEvent e)
    {
        TableColumnModel columnModel = _table.getColumnModel();
        int column = columnModel.getColumnIndexAtX(e.getX());
        int row    = e.getY() / _table.getRowHeight();
        Object value;
        JComponent c;

        if(row >= _table.getRowCount() || row < 0 ||
                column >= _table.getColumnCount() || column < 0)
            return;

        value = _table.getValueAt(row, column);

        if(!(value instanceof JComponent))
        {
            return;
        }
        
        c = (JComponent)value;
        
        if (c instanceof JButton)
        {
            JButton b = (JButton) c;
            if (e.getID() == MouseEvent.MOUSE_PRESSED)
            {
                b.doClick(100);
            }
        }
        c.dispatchEvent(new MouseEvent(c, e.getID(), e.getWhen(), e.getModifiers(), 0, 0, 1, e.isPopupTrigger(), e.getButton()));
    }

    public JTableButtonMouseListener(JTable table)
    {
        _table = table;
    }

    public void mouseEntered(MouseEvent e)
    {
        forwardEvent(e);
    }

    public void mouseExited(MouseEvent e)
    {
        forwardEvent(e);
    }

    public void mousePressed(MouseEvent e)
    {
        forwardEvent(e);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            forwardEvent(e);
        }
    }
    
    public void mouseReleased(MouseEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            forwardEvent(e);
        }
    }
}
