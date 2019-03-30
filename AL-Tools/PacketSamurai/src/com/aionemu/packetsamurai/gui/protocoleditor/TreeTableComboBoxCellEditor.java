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


/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.packetsamurai.gui.protocoleditor;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.JXTree.DelegatingRenderer;


@SuppressWarnings("serial")
public class TreeTableComboBoxCellEditor extends DefaultCellEditor
{
	/**
	 * 
	 */
	private final JXTreeTable table;

	public TreeTableComboBoxCellEditor(JXTreeTable table)
	{
		super(new PartTypeComboBox());
		this.table = table;
	}
	
	public JComboBox<?> getComboBox()
	{
		return (JComboBox<?>) getComponent();
	}

	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int r, int c)
	{
		Component component = super.getTableCellEditorComponent(table, value, isSelected, r, c);
		//boolean rv = t.isRootVisible();
		//int offsetRow = rv ? r : r - 1;
		int offsetRow = r;
		Rectangle bounds = this.table.getCellRect(offsetRow, 0, false);
		int offset = bounds.x;
		TreeCellRenderer tcr = this.table.getTreeCellRenderer();
		System.out.println("Renderer is a : "+tcr);
		if(tcr instanceof DelegatingRenderer)
			tcr = ((DelegatingRenderer)tcr).getDelegateRenderer();
		System.out.println("After un-delegataion, renderer is a : "+tcr);
		if (tcr instanceof DefaultTreeCellRenderer)
		{
			System.out.println("Renderer is a DefaultTreeCellRenderer");
			Object node = this.table.getPathForRow(offsetRow).getLastPathComponent();
			int depth = this.table.getPathForRow(offsetRow).getPathCount();
			System.out.println("Node's depth is "+depth);
			offset += (depth - 1) * 20;
			Icon icon;
			if (this.table.getTreeTableModel().isLeaf(node))
				icon = ((DefaultTreeCellRenderer) tcr).getLeafIcon();
			else if (this.table.isExpanded(offsetRow))
				icon = ((DefaultTreeCellRenderer) tcr).getOpenIcon();
			else
				icon = ((DefaultTreeCellRenderer) tcr).getClosedIcon();
			if (icon != null)
			{
				offset += ((DefaultTreeCellRenderer) tcr).getIconTextGap()
						+ icon.getIconWidth();
			}
		}
		((PartTypeComboBox) getComponent()).offset = offset;
		((PartTypeComboBox) getComponent()).setSelectedItem(value);
		return component;
	}

	public boolean isCellEditable(EventObject e)
	{
		if (e instanceof MouseEvent)
		{
			MouseEvent me = (MouseEvent)e;
			if (me.getClickCount() >= 2)
			{
				return true;
			}
			return false;
		}
		if (e == null)
		{
			return true;
		}
		return false;
	}
}