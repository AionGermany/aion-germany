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
package com.aionemu.gameserver.dataholders.loadingutils;

import static org.apache.commons.io.filefilter.FileFilterUtils.andFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.makeSVNAware;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * <code>XmlMerger</code> is a utility that writes XML document onto an other document with resolving all <code>import</code> elements.
 * </p>
 * <p>
 * Schema:
 * <p/>
 * 
 * <pre>
 * &lt;xs:element name="import"&gt;
 * &lt;xs:annotation&gt;
 * &lt;xs:documentation&gt;&lt;![CDATA[
 *      Attributes:
 *          'file' :
 *              Required attribute.
 *              Specified path to imported file or directory.
 *          'skipRoot' :
 *              Optional attribute.
 *              Default value: 'false'.
 *              If enabled, then root tags of imported files are ignored.
 *          'recirsiveImport':
 *              Optional attribute.
 *              Default value: 'true'.
 *              If enabled and attribute 'file' points to the directory, then all xml files in that
 *              directory ( and deeper - recursively ) will be imported, otherwise only files inside
 *              that directory (without it subdirectories)
 *  ]]&gt;&lt;/xs:documentation&gt;
 * &lt;/xs:annotation&gt;
 * &lt;xs:complexType&gt;
 * &lt;xs:attribute type="xs:string" name="file" use="required"/&gt;
 * &lt;xs:attribute type="xs:boolean" name="skipRoot" use="optional" default="false"/&gt;
 * &lt;xs:attribute type="xs:boolean" name="recursiveImport" use="optional" default="true" /&gt;
 * &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * </pre>
 * <p/>
 * </p>
 * <p/>
 * Created on: 23.07.2009 12:55:14
 *
 * @author Aquanox
 */
public class XmlMerger {

	private static final Logger logger = LoggerFactory.getLogger(XmlMerger.class);
	private final File baseDir;
	private final File sourceFile;
	private final File destFile;
	private final File metaDataFile;
	private XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	private XMLEventFactory eventFactory = XMLEventFactory.newInstance();

	/**
	 * Create new instance of <tt>XmlMerger </tt>. Base directory is set to directory which contains source file.
	 *
	 * @param source
	 *            Source file.
	 * @param target
	 *            Destination file.
	 */
	public XmlMerger(File source, File target) {
		this(source, target, source.getParentFile());
	}

	/**
	 * Create new instance of <tt>XmlMerger </tt>
	 *
	 * @param source
	 *            Source file.
	 * @param target
	 *            Destination file.
	 * @param baseDir
	 *            Root directory.
	 */
	public XmlMerger(File source, File target, File baseDir) {
		this.baseDir = baseDir;

		this.sourceFile = source;
		this.destFile = target;

		this.metaDataFile = new File(target.getParent(), target.getName() + ".properties");
	}

	/**
	 * This method creates a result document if it is missing, or updates existing one if the source file has modification.<br />
	 * If there are no changes - nothing happens.
	 *
	 * @throws FileNotFoundException
	 *             when source file doesn't exists.
	 * @throws XMLStreamException
	 *             when XML processing error was occurred.
	 */
	public void process() throws Exception {
		logger.debug("Processing " + sourceFile + " files into " + destFile);

		if (!sourceFile.exists()) {
			throw new FileNotFoundException("Source file " + sourceFile.getPath() + " not found.");
		}

		boolean needUpdate = false;

		if (!destFile.exists()) {
			logger.debug("Dest file not found - creating new file");
			needUpdate = true;
		}
		else if (!metaDataFile.exists()) {
			logger.debug("Meta file not found - creating new file");
			needUpdate = true;
		}
		else {
			logger.debug("Dest file found - checking file modifications");
			needUpdate = checkFileModifications();
		}

		if (needUpdate) {
			logger.debug("Modifications found. Updating...");
			try {
				doUpdate();
			}
			catch (Exception e) {
				FileUtils.deleteQuietly(destFile);
				FileUtils.deleteQuietly(metaDataFile);
				throw e;
			}
		}
		else {
			logger.debug("Files are up-to-date");
		}
	}

	/**
	 * Check for modifications of included files.
	 *
	 * @return <code>true</code> if at least one of included files has modifications.
	 * @throws IOException
	 *             IO Error.
	 * @throws SAXException
	 *             Document parsing error.
	 * @throws ParserConfigurationException
	 *             if a SAX parser cannot be created which satisfies the requested configuration.
	 */
	private boolean checkFileModifications() throws Exception {
		long destFileTime = destFile.lastModified();

		if (sourceFile.lastModified() > destFileTime) {
			logger.debug("Source file was modified ");
			return true;
		}

		Properties metadata = restoreFileModifications(metaDataFile);

		if (metadata == null) // new file or smth else.
		{
			return true;
		}

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();

		SAXParser parser = parserFactory.newSAXParser();

		TimeCheckerHandler handler = new TimeCheckerHandler(baseDir, metadata);

		parser.parse(sourceFile, handler);

		return handler.isModified();
	}

	/**
	 * This method processes the source file, replacing all of the 'import' tags by the data from the relevant files.
	 *
	 * @throws XMLStreamException
	 *             on event writing error.
	 * @throws IOException
	 *             if the destination file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
	 */
	private void doUpdate() throws XMLStreamException, IOException {
		XMLEventReader reader = null;
		XMLEventWriter writer = null;

		Properties metadata = new Properties();

		try {
			writer = outputFactory.createXMLEventWriter(new BufferedWriter(new FileWriter(destFile, false)));
			reader = inputFactory.createXMLEventReader(new FileReader(sourceFile));

			while (reader.hasNext()) {
				final XMLEvent xmlEvent = reader.nextEvent();

				if (xmlEvent.isStartElement() && isImportQName(xmlEvent.asStartElement().getName())) {
					processImportElement(xmlEvent.asStartElement(), writer, metadata);
					continue;
				}

				if (xmlEvent.isEndElement() && isImportQName(xmlEvent.asEndElement().getName())) {
					continue;
				}

				if (xmlEvent instanceof Comment)// skip comments.
				{
					continue;
				}

				if (xmlEvent.isCharacters())// skip whitespaces.
				{
					if (xmlEvent.asCharacters().isWhiteSpace() || xmlEvent.asCharacters().isIgnorableWhiteSpace())// skip
					// whitespaces.
					{
						continue;
					}
				}

				writer.add(xmlEvent);

				if (xmlEvent.isStartDocument()) {
					writer.add(eventFactory.createComment("\nThis file is machine-generated. DO NOT MODIFY IT!\n"));
				}
			}

			storeFileModifications(metadata, metaDataFile);
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				}
				catch (Exception ignored) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				}
				catch (Exception ignored) {
				}
			}
		}
	}

	private boolean isImportQName(QName name) {
		return "import".equals(name.getLocalPart());
	}

	private static final QName qNameFile = new QName("file");
	private static final QName qNameSkipRoot = new QName("skipRoot");
	/**
	 * If this option is enabled you import the directory, and all its subdirectories. Default is 'true'.
	 */
	private static final QName qNameRecursiveImport = new QName("recursiveImport");

	/**
	 * This method processes the 'import' element, replacing it by the data from the relevant files.
	 *
	 * @throws XMLStreamException
	 *             on event writing error.
	 * @throws FileNotFoundException
	 *             of imported file was not found.
	 */
	private void processImportElement(StartElement element, XMLEventWriter writer, Properties metadata) throws XMLStreamException, IOException {
		File file = new File(baseDir, getAttributeValue(element, qNameFile, null, "Attribute 'file' is missing or empty."));

		if (!file.exists()) {
			throw new FileNotFoundException("Missing file to import:" + file.getPath());
		}

		boolean skipRoot = Boolean.valueOf(getAttributeValue(element, qNameSkipRoot, "false", null));
		boolean recImport = Boolean.valueOf(getAttributeValue(element, qNameRecursiveImport, "true", null));

		if (file.isFile()) {
			importFile(file, skipRoot, writer, metadata);
		}
		else {
			logger.debug("Processing dir " + file);

			Collection<File> files = listFiles(file, recImport);

			for (File childFile : files) {
				importFile(childFile, skipRoot, writer, metadata);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static Collection<File> listFiles(File root, boolean recursive) {
		IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

		return FileUtils.listFiles(root, andFileFilter(andFileFilter(notFileFilter(prefixFileFilter("new")), suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
	}

	/**
	 * Extract an attribute value from a <code>StartElement </code> event.
	 *
	 * @param element
	 *            Event object.
	 * @param name
	 *            Attribute QName
	 * @param def
	 *            Default value.
	 * @param onErrorMessage
	 *            On error message.
	 * @return attribute value
	 * @throws XMLStreamException
	 *             if attribute is missing and there is no default value set.
	 */
	private String getAttributeValue(StartElement element, QName name, String def, String onErrorMessage) throws XMLStreamException {
		Attribute attribute = element.getAttributeByName(name);

		if (attribute == null) {
			if (def == null) {
				throw new XMLStreamException(onErrorMessage, element.getLocation());
			}

			return def;
		}

		return attribute.getValue();
	}

	/**
	 * Read all {@link javax.xml.stream.events.XMLEvent}'s from specified file and write them onto the {@link javax.xml.stream.XMLEventWriter}
	 *
	 * @param file
	 *            File to import
	 * @param skipRoot
	 *            Skip-root flag
	 * @param writer
	 *            Destenation writer
	 * @throws XMLStreamException
	 *             On event reading/writing error.
	 * @throws FileNotFoundException
	 *             if the reading file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
	 */
	private void importFile(File file, boolean skipRoot, XMLEventWriter writer, Properties metadata) throws XMLStreamException, IOException {
		logger.debug("Appending file " + file);
		metadata.setProperty(file.getPath(), makeHash(file));

		XMLEventReader reader = null;

		try {
			reader = inputFactory.createXMLEventReader(new FileReader(file));

			QName firstTagQName = null;

			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();

				// skip start and end of document.
				if (event.isStartDocument() || event.isEndDocument()) {
					continue;
				}
				// skip all comments.
				if (event instanceof Comment) {
					continue;
				}
				// skip white-spaces and all ignoreable white-spaces.
				if (event.isCharacters()) {
					if (event.asCharacters().isWhiteSpace() || event.asCharacters().isIgnorableWhiteSpace()) {
						continue;
					}
				}

				// modify root-tag of imported file.
				if (firstTagQName == null && event.isStartElement()) {
					firstTagQName = event.asStartElement().getName();

					if (skipRoot) {
						continue;
					}
					else {
						StartElement old = event.asStartElement();

						event = eventFactory.createStartElement(old.getName(), old.getAttributes(), null);
					}
				}

				// if root was skipped - skip root end too.
				if (event.isEndElement() && skipRoot && event.asEndElement().getName().equals(firstTagQName)) {
					continue;
				}

				// finally - write tag
				writer.add(event);
			}
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (Exception ignored) {
				}
			}
		}
	}

	private static class TimeCheckerHandler extends DefaultHandler {

		private File basedir;
		private Properties metadata;
		private boolean isModified = false;
		private Locator locator;

		private TimeCheckerHandler(File basedir, Properties metadata) {
			this.basedir = basedir;
			this.metadata = metadata;
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (isModified || !"import".equals(qName)) {
				return;
			}

			String value = attributes.getValue(qNameFile.getLocalPart());

			if (value == null) {
				throw new SAXParseException("Attribute 'file' is missing", locator);
			}

			File file = new File(basedir, value);

			if (!file.exists()) // noinspection ThrowableInstanceNeverThrown
			{
				throw new SAXParseException("Imported file not found. file=" + file.getPath(), locator);
			}

			if (file.isFile() && checkFile(file))// if file - just check it.
			{
				isModified = true;
				return;
			}

			if (file.isDirectory())// otherwise check all files inside
			{
				String rec = attributes.getValue(qNameRecursiveImport.getLocalPart());

				Collection<File> files = listFiles(file, rec == null ? true : Boolean.valueOf(rec));

				for (File childFile : files) {
					if (checkFile(childFile)) {
						isModified = true;
						return;
					}
				}
			}
		}

		private boolean checkFile(File file) {
			String data = metadata.getProperty(file.getPath());

			if (data == null) // file was added.
			{
				return true;
			}

			try {
				String hash = makeHash(file);

				if (!data.equals(hash))// file|dir was changed.
				{
					return true;
				}
			}
			catch (IOException e) {
				logger.warn("File varification error. File: " + file.getPath() + ", location=" + locator.getLineNumber() + ":" + locator.getColumnNumber(), e);
				return true;// was modified.
			}

			return false;
		}

		public boolean isModified() {
			return isModified;
		}
	}

	private Properties restoreFileModifications(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}

		FileReader reader = null;

		try {
			Properties props = new Properties();

			reader = new FileReader(file);

			props.load(reader);

			return props;
		}
		catch (IOException e)// properties
		{
			logger.debug("File modfications restoring error. ", e);
			return null;
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private void storeFileModifications(Properties props, File file) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);
			props.store(writer, " This file is machine-generated. DO NOT EDIT!");
		}
		catch (IOException e) {
			logger.error("Failed to store file modification data.");
			throw e;
		}
		finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * Create a unique identifier of file and it contents.
	 *
	 * @param file
	 *            the file to checksum, must not be <code>null</code>
	 * @return String identifier
	 * @throws IOException
	 *             if an IO error occurs reading the file
	 */
	private static String makeHash(File file) throws IOException {
		return String.valueOf(FileUtils.checksumCRC32(file));
	}
}
