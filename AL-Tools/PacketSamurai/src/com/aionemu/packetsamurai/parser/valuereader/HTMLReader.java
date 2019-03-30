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
package com.aionemu.packetsamurai.parser.valuereader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.parser.datatree.StringValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;

/**
 * @author Ulysses R. Ribeiro, modified Rolandas
 *
 */
public class HTMLReader implements Reader
{

    public boolean loadReaderFromXML(Node n)
    {
        return true;
    }

    public String read(ValuePart part)
    {
        if (part instanceof StringValuePart)
        {
            return ((StringValuePart)part).getStringValue();
        }
        PacketSamurai.getUserInterface().log("ERROR: HTML Reader set on a non String part: "+part.getModelPart().getName());
        return "";
    }

    public JComponent readToComponent(ValuePart part)
    {
        JButton view = new JButton("View");
        view.addActionListener(new ButtonActionListener(this.read(part)));
        view.setActionCommand("clicked");
        return view;
    }

    public boolean saveReaderToXML(Element element, Document doc)
    {
        return true;
    }

    class ButtonActionListener implements ActionListener
    {
        private String _html;
        
        public ButtonActionListener(String html)
        {
            _html = html;
        }
        
        public void actionPerformed(ActionEvent e)
        {
            JDialog dlg = new JDialog(((Main) PacketSamurai.getUserInterface()).getMainFrame(),"HTML");
            dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dlg.setSize(350, 400);
            dlg.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getMainFrame());
            
            JTabbedPane tabPane = new JTabbedPane();
            
            // HTML
            JEditorPane htmlDisplay = new JEditorPane();
            htmlDisplay.setEditable(false);
            htmlDisplay.setContentType("text/html");
            htmlDisplay.setText(_html);
            
            // Source
            JEditorPane sourceDisplay = new JEditorPane();
            sourceDisplay.setEditable(false);
            sourceDisplay.setContentType("text/plain");
            sourceDisplay.setText(_html);
            
            tabPane.add(new JScrollPane(htmlDisplay), "HTML");
            tabPane.add(new JScrollPane(sourceDisplay), "Source");
            
            dlg.add(tabPane);
            dlg.setVisible(true);
        }
    }
}
