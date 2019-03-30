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


package com.aionemu.packetsamurai.logrepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javolution.util.FastMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.Main;
import com.aionemu.packetsamurai.gui.logrepo.LogFilesTab;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class LogRepository
{
    private static String REPOSITORY_FILE = "logrepository.xml";
    private File _localLogsDir;
    private Map<String, LogFile> _localLogs = new FastMap<String, LogFile>();
    private Map<String, LogFile> _remoteLogs = new FastMap<String, LogFile>();
    private int _lastId;
    
    private static class SingletonHolder
	{
		private final static LogRepository singleton = new LogRepository();
	}
	
	public static LogRepository getInstance()
	{
		return SingletonHolder.singleton;
	}

    private LogRepository()
    {
        _localLogsDir = new File("./logs");
        this.loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        Document doc;
        File file = new File(REPOSITORY_FILE);
        Node root = null;

        if (file.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setIgnoringComments(true);
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                doc = docBuilder.parse(fis);
                root = doc.getDocumentElement();
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ParserConfigurationException e)
            {
                e.printStackTrace();
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }

            if (root != null)
            {
                for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling())
                {
                    if (n.getNodeName().equalsIgnoreCase("local"))
                    {
                        this.parseLocalLogs(n);
                    }
                    else if (n.getNodeName().equalsIgnoreCase("remote"))
                    {
                        this.parseRemoteLogs(n);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void loadFromDisk()
    {
        this.refreshLocalLogs();
    }

    private void parseLocalLogs(Node node)
    {
        NamedNodeMap attributes;
        int id;
        @SuppressWarnings("unused")
        long filesize;
        String uploader, filename;
        boolean isRemote;
        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equalsIgnoreCase("log"))
            {
                attributes = n.getAttributes();
                filename = attributes.getNamedItem("filename").getNodeValue();
                filesize = Long.parseLong(attributes.getNamedItem("filesize").getNodeValue());
                isRemote = Boolean.parseBoolean( attributes.getNamedItem("isRemote").getNodeValue());
                
                File file = new File(this.getLogsDir()+"/"+filename);
                
                LogFile logFile = new LogFile(file, isRemote);
                if (file.exists())
                {
                    if (isRemote)
                    {
                        id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                        uploader = attributes.getNamedItem("uploader").getNodeValue();
                        logFile.setRemoteUploader(uploader);
                        logFile.setRemoteId(id);
                    }
                    logFile.setComments(attributes.getNamedItem("comments").getNodeValue());
                    this.addLocalLog(logFile);
                }
            }
        }
    }

    private void parseRemoteLogs(Node node)
    {
        NamedNodeMap attributes;
        int id;
        long filesize;
        String uploader, filename, protocol;
        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if (n.getNodeName().equalsIgnoreCase("log"))
            {
                attributes = n.getAttributes();
                id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                filename = attributes.getNamedItem("filename").getNodeValue();
                filesize = Long.parseLong(attributes.getNamedItem("filesize").getNodeValue());
                uploader = attributes.getNamedItem("uploader").getNodeValue();
                LogFile logFile = new LogFile(filename, filesize, false, true);
                
                logFile.setRemoteUploader(uploader);
                logFile.setRemoteId(id);
                logFile.setComments(attributes.getNamedItem("comments").getNodeValue());
                
                
                if (attributes.getNamedItem("protocol") != null)
                {
                    protocol = attributes.getNamedItem("protocol").getNodeValue();
                    logFile.setRemoteProtocolName(protocol);
                    // discard logs from old logger version that didnt saved the protocol (they will get refreshed from remote repo)
                    this.addRemoteLog(logFile);
                }
            }
        }
    }

    public File getLogsDir()
    {
        return _localLogsDir;
    }

    public void refreshLocalLogs()
    {
        if (!_localLogsDir.isDirectory())
            throw new IllegalStateException("The local logs dir must be a directory");

        int size = _localLogs.size();

        for (File log : _localLogsDir.listFiles(new LogFileFilter()))
        {
            if (!_localLogs.containsKey(log.getName()))
            {
                try
                {
                    LogFile logFile = new LogFile(log,false);
                    logFile.loadHeaders();
                    logFile.checkRemote();
                    this.addLocalLog(logFile);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        if (_localLogs.size() > size)
        {
            this.runSaveDatabase();
        }
    }

    public void refreshRemoteLogs()
    {
        int size = _remoteLogs.size();
        RemoteLogRepositoryBackend.getInstance().updateRemoteLogsList(this.getLastId()+1);
        if (_remoteLogs.size() > size)
        {
            this.runSaveDatabase();
        }
    }

    public void addLocalLog(LogFile logFile)
    {
        _localLogs.put(logFile.getName(), logFile);
        if (_remoteLogs.containsKey(logFile.getName()))
        {
            if (!_remoteLogs.get(logFile.getName()).isRemote())
            {
                throw new IllegalStateException("This log was suposed to be remote: "+logFile.getName());
            }
            _remoteLogs.remove(logFile.getName());
        }
        this.compareAndSet(logFile.getRemoteId());
    }

    public void addRemoteLog(LogFile logFile)
    {
        if (_localLogs.containsKey(logFile.getName()))
        {
            LogFile localLog = _localLogs.get(logFile.getName());
            localLog.setRemote(true);
            this.updateLogInfo(logFile, localLog);
        }
        else
        {
            if (!_remoteLogs.containsKey(logFile.getName()))
            {
                LogFilesTab filesTab = ((Main)PacketSamurai.getUserInterface()).getLogRepoTab().getLogFilesTab();
                filesTab.addLogFile(logFile);
            }
            else
            {
                LogFile localLog = _remoteLogs.get(logFile.getName());
                this.updateLogInfo(logFile, localLog);
            }
            _remoteLogs.put(logFile.getName(), logFile);
        }
        this.compareAndSet(logFile.getRemoteId());
    }
    
    public void updateLogInfo(LogFile srcLogFile, LogFile dstLogFile)
    {
        dstLogFile.setRemoteUploader(srcLogFile.getRemoteUploader());
        dstLogFile.setComments(srcLogFile.getComments());
        dstLogFile.setRemoteAnalyserBits(srcLogFile.getAnalyserBitSet());
        dstLogFile.setRemoteId(srcLogFile.getRemoteId());
        dstLogFile.setUploadedTime(srcLogFile.getUploadedTime());
    }

    public Collection<LogFile> getLocalLogs()
    {
        return _localLogs.values();
    }

    public Collection<LogFile> getRemoteLogs()
    {
        return _remoteLogs.values();
    }

    private synchronized void compareAndSet(int id)
    {
        if (_lastId < id)
        {
            _lastId = id;
        }
    }

    public synchronized int getLastId()
    {
        return _lastId;
    }

    private class LogFileFilter implements FilenameFilter
    {

        public boolean accept(File arg0, String name) 
        {
            if (name.endsWith(".cap") || name.endsWith(".pcap"))
                return true;
            return false;
        }
    }

    public void runSaveDatabase()
    {
        Thread save = new Thread
        ( 
                new Runnable()
                {
                    public void run()
                    {
                        LogRepository.this.saveDatabase();
                    }
                }
        );
        save.start();
    }

    private synchronized void saveDatabase()
    {
        try
        {
            TransformerFactory transformFac  = TransformerFactory.newInstance(); 
            
            //trans.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2"); 
            transformFac.setAttribute("indent-number", 4);
            Transformer trans = transformFac.newTransformer(); 
            trans.setOutputProperty(OutputKeys.INDENT, "yes"); 
//             Fill Xml elements
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setIgnoringComments(true);
            DocumentBuilder docBuilder = factory.newDocumentBuilder(); 
            Document doc = docBuilder.newDocument(); 
            //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            //Document doc = docBuilder.newDocument();
            
            /*OutputFormat of = new OutputFormat("XML","UTF-8",true);
            of.setIndent(1);
            of.setIndenting(true);
            of.setLineWidth(0);*/
            
            
            Element root = doc.createElement("repository");

            Element local = doc.createElement("local");
            doc.appendChild(root);
            root.appendChild(local);

            Element log;
            for (LogFile logFile : this.getLocalLogs())
            {
                log = doc.createElement("log");
                log.setAttribute("filename",logFile.getName());
                log.setAttribute("filesize",Long.toString(logFile.getSize()));
                log.setAttribute("isRemote",Boolean.toString(logFile.isRemote()));
                log.setAttribute("comments",logFile.getComments());
                log.setAttribute("protocol",logFile.getProtocolName());
                if (logFile.isRemote())
                {
                    log.setAttribute("uploader",logFile.getRemoteUploader());
                    log.setAttribute("id",Integer.toString(logFile.getRemoteId()));
                }
                local.appendChild(log);
            }

            Element remote = doc.createElement("remote");
            root.appendChild(remote);

            for (LogFile logFile : this.getRemoteLogs())
            {
                log = doc.createElement("log");
                log.setAttribute("filename",logFile.getName());
                log.setAttribute("filesize",Long.toString(logFile.getSize()));
                log.setAttribute("comments",logFile.getComments());
                log.setAttribute("protocol",logFile.getProtocolName());
                if (logFile.isRemote())
                {
                    log.setAttribute("uploader",logFile.getRemoteUploader());
                    log.setAttribute("id",Integer.toString(logFile.getRemoteId()));
                }
                remote.appendChild(log);
            }

            File file = new File(REPOSITORY_FILE);
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            
            DOMSource domSource = new DOMSource(doc); 
            StreamResult output = new StreamResult(new OutputStreamWriter(fos, "UTF-8"));
            trans.transform(domSource, output); 
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}