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


package com.aionemu.packetsamurai.parser.valuereader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InflateReader implements Reader {

	int _skipBytes = 0;
	int _uncompressedSize = 0;

	@Override
	public boolean loadReaderFromXML(Node n) {
		NamedNodeMap enumAttrs = n.getAttributes();
		Node nameAttr = enumAttrs.getNamedItem("skipbytes");
		if (nameAttr != null)
			_skipBytes = Integer.parseInt(nameAttr.getNodeValue());

		return true;
	}

	@Override
	public boolean saveReaderToXML(Element element, Document doc) {
		return true;
	}

	@Override
	public JComponent readToComponent(ValuePart part) {
		JButton view = new JButton("View");
		view.addActionListener(new ButtonActionListener(this.read(part)));
		view.setActionCommand("clicked");
		return view;
	}

	@Override
	public String read(ValuePart part) {
		byte[] bytes = part.getBytes();
		byte[] realBytes;

		if (_skipBytes > 0)
			realBytes = Arrays.copyOfRange(bytes, _skipBytes - 1, bytes.length - 1);
		else
			realBytes = bytes;

		Inflater decompressor = new Inflater();
		decompressor.setInput(realBytes);

		// Create an expandable byte array to hold the decompressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream(realBytes.length);

		byte[] buf = new byte[1024];
		try {
			while (true) {
				int count = decompressor.inflate(buf);
				if (count > 0) {
					bos.write(buf, 0, count);
				}
				else if (count == 0 && decompressor.finished()) {
					break;
				}
				else {
					throw new RuntimeException("bad zip data, size:" + realBytes.length);
				}
			}
		}
		catch (DataFormatException t) {
			throw new RuntimeException(t);
		}
		finally {
			decompressor.end();
		}
		try {
			bos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		String content = "";
		try {
			content = bos.toString("UTF-16LE");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	class ButtonActionListener implements ActionListener {

		private String _xml;

		public ButtonActionListener(String html) {
			_xml = html;
		}

		public void actionPerformed(ActionEvent e) {
			JDialog dlg = new JDialog(((Main) PacketSamurai.getUserInterface()).getMainFrame(), "Text");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(350, 400);
			dlg.setLocationRelativeTo(((Main) PacketSamurai.getUserInterface()).getMainFrame());

			JEditorPane sourceDisplay = new JEditorPane();
			sourceDisplay.setEditable(false);
			sourceDisplay.setContentType("text/plain");
			sourceDisplay.setText(_xml);

			dlg.add(new JScrollPane(sourceDisplay));
			dlg.setVisible(true);
		}
	}

}
