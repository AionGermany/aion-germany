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
package com.aionemu.packetsamurai.gui;

import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.GameSessionViewer;
import com.aionemu.packetsamurai.session.Session;


/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
public class ViewTab extends JTabbedPane
{
	
	public void showSession(final Session s, final boolean notify, boolean isReload, String fileName)
	{
		final GameSessionViewer gsv = new GameSessionViewer(s);

		final ViewPane viewPane = new ViewPane(gsv);

		viewPane.setFileName(fileName);

		//add the new tab
		this.addTab("Session "+s.getSessionId(), viewPane);

	     if (isReload)
	     {
	       remove(getSelectedComponent());
	       setSelectedComponent(viewPane);
	     }

		if (notify)
		{
		    s.setNewPacketNotification(new Runnable()
		    {
		        public void run()
		        {
		            List<DataPacket> sessionPackets = s.getPackets();
		            List<DataPacket> viewPackets = viewPane.getGameSessionViewer().getAllPackets();
		            int size = viewPackets.size();
		            int newSize = sessionPackets.size();
		            if(size >= newSize)
		                return;
		            for(int i = size; i < newSize; i++)
		            {
		                viewPackets.add(sessionPackets.get(i));
		            }
		            long startTime = gsv.getAllPackets().get(0).getTimeStamp();
                    DataPacket gp;
		            for(int i = size; i < newSize; i++)
		            {
		                gp = gsv.getPacket(i);

		                viewPane.getPacketTableModel().addRow(gp, startTime);
		            }
                    
                    SwingUtilities.invokeLater
                    (
                            new Runnable()
                            {

                                public void run()
                                {
                                    viewPane.getPacketTable().updateUI();
                                }
                            }
                    );
                    
		        }

		    });
		}
		s.setShown(true);
	}
	
	public ViewPane getCurrentViewPane()
	{
		return (ViewPane)getSelectedComponent();
	}
	
	public void removeAll()
	{
        int size = this.getComponentCount();
        ViewPane vp;
		for (int i = 0; i < size; i++)
		{
            vp = (ViewPane)this.getComponent(i);
            vp.getGameSessionViewer().getSession().setShown(false);
		}
		super.removeAll();
	}
}
