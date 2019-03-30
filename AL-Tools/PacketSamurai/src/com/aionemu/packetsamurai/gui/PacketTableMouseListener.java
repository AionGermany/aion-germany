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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * @author Ulysses R. Ribeiro
 *
 */
public class PacketTableMouseListener implements MouseListener, ActionListener
{
    private JPopupMenu _popupMenu;
    
    public PacketTableMouseListener()
    {
        _popupMenu = new JPopupMenu();
        
        JMenuItem itemOpenGo = new JMenuItem("Toggle Mark");
        itemOpenGo.setActionCommand("mark");
        itemOpenGo.addActionListener(this);
        _popupMenu.add(itemOpenGo);
        
        JMenuItem itemOpen = new JMenuItem("Open on Viewer");
        itemOpen.setActionCommand("open");
        itemOpen.addActionListener(this);
        _popupMenu.add(itemOpen);
    }
    
    

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            this.showPopupMenu(e);
        }
    }
    
    public void showPopupMenu(MouseEvent e)
    {
        JTable table = (JTable) e.getSource();
        int row = table.rowAtPoint(e.getPoint());
        //int col = table.columnAtPoint(e.getPoint());
        boolean val = !((PacketTableModel) table.getModel()).getIsMarked(row);
        ((PacketTableModel) table.getModel()).setIsMarked(row, val);
        table.repaint();
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e)
    {
        
    }



   
}
