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

package com.aionemu.commons.utils.xml;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Unfortunately JAXP seems to be working wrong in multithreaded enviroment.<br>
 * Even in case of creating new document builder factory per document.<br>
 * Using aggressive synchronization here :(
 */
public abstract class XmlUtils {

	private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private static final TransformerFactory tf = TransformerFactory.newInstance();

	static {
		dbf.setNamespaceAware(true);
	}

	public static Document getDocument(String xmlSource) {
		synchronized (XmlUtils.class) {
			Document document = null;
			if (xmlSource != null) {
				try {
					final Reader stream = new StringReader(xmlSource);
					DocumentBuilder db = dbf.newDocumentBuilder();
					document = db.parse(new InputSource(stream));
				} catch (Exception e) {
					throw new RuntimeException("Error converting string to document", e);
				}
			}
			return document;
		}
	}

	public static String getString(Document document) {
		synchronized (XmlUtils.class) {
			try {
				DOMSource domSource = new DOMSource(document);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				return writer.toString();
			} catch (TransformerException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Schema getSchema(String schemaString) {
		Schema schema = null;
		try {
			if (schemaString != null) {
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				StreamSource ss = new StreamSource();
				ss.setReader(new StringReader(schemaString));
				schema = sf.newSchema(ss);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to create schema from string: " + schemaString, e);
		}
		return schema;
	}

	public static Schema getSchema(URL schemaURL) {
		Schema schema = null;
		try {
			if (schemaURL != null) {
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				schema = sf.newSchema(schemaURL);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to create schema from URL " + schemaURL, e);
		}
		return schema;
	}

	public static void validate(Schema schema, Document document) {

		Validator validator = schema.newValidator();
		try {
			validator.validate(new DOMSource(document));
		} catch (Exception e) {
			throw new RuntimeException("Failed to validate document", e);
		}
	}
}