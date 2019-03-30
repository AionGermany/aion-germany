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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.gui.logrepo.TransferPanel;
import com.aionemu.packetsamurai.logrepo.communication.CompoundXmlRequest;
import com.aionemu.packetsamurai.logrepo.communication.Request;
import com.aionemu.packetsamurai.logrepo.communication.RequestForPart;
import com.aionemu.packetsamurai.logrepo.communication.RequestPart;
import com.aionemu.packetsamurai.session.DataPacket;
import com.aionemu.packetsamurai.session.Session;

/**
 * 
 * @author Gilles Duboscq
 * @author Ulysses R. Ribeiro
 */
public class RemoteLogRepositoryBackend
{
    public static int CONCURENT_HTTP_CONNECTIONS = 5; //not RFC compilant but well....
    public static int CONCURENT_DOWNLOADS = 2;
    public static int CONCURENT_UPLOADS = 2;

    //
    /*private static String REPOSITORY_HOST_ADDRESS = "localhost";
    public static int REPOSITORY_HOST_PORT = 80;*/

    private HttpClient _httpClient;
    private String _repoLocation = "http://localhost/logrepo/";
    private String _repoRequestScript = "index.php";
    private String _repoTicketScript = "ticket.php";
    private String _userName;
    private String _password;
    private DownloadQueue _dlQueue;
    private UploadQueue _upQueue;

    // XXX should be a interface implemented by TranferPanel, not it directly
    public TransferPanel _downloadListener;
    public TransferPanel _uploadListener;
    
    private static class SingletonHolder
	{
		private final static RemoteLogRepositoryBackend singleton = new RemoteLogRepositoryBackend();
	}
	
	public static RemoteLogRepositoryBackend getInstance()
	{
		return SingletonHolder.singleton;
	}

    public static void showOutput(InputStream in)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        try
        {
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private RemoteLogRepositoryBackend()
    {

        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(CONCURENT_HTTP_CONNECTIONS);

        /*for (int i = 0; i < CONCURENT_HTTP_CONNECTIONS; i++)
        {
            HttpConnection httpConn = new HttpConnection(REPOSITORY_HOST_ADDRESS, REPOSITORY_HOST_PORT);
            httpConn.setHttpConnectionManager(connectionManager);
        }*/
        _httpClient = new HttpClient(connectionManager);
        /*HostConfiguration hc = new HostConfiguration();
        hc.setHost(REPOSITORY_HOST_ADDRESS, REPOSITORY_HOST_PORT);
        _httpClient.setHostConfiguration(hc);*/
        _dlQueue = new DownloadQueue(CONCURENT_DOWNLOADS);
        _upQueue = new UploadQueue(CONCURENT_UPLOADS);
    }
    
    public void setProxy(String host, int port)
    {
        HostConfiguration hc = new HostConfiguration(_httpClient.getHostConfiguration());
        hc.setProxy(host, port);
        _httpClient.setHostConfiguration(hc);
    }

    public HttpClient getHttpClient()
    {
        return _httpClient;
    }

    public boolean isConnected()
    {
        return (getUserName() != null && getPassword() != null);
    }

    public void enqueueUpload(LogFile file)
    {
        if(!file.isRemote() &&  isConnected())
            _upQueue.addNewUpLoadTask(file);
    }

    public void enqueueDownload(LogFile file)
    {
        if(!file.isLocal() &&  isConnected())
            _dlQueue.addNewDownLoadTask(file);
    }

    public void updateLogDetails(LogFile logFile)
    {
        CompoundXmlRequest req = new CompoundXmlRequest();
        PostMethod post = new PostMethod(_repoLocation+_repoRequestScript);

        Request updateRequest = req.createRequest("update");
        updateRequest.addPart("logname", logFile.getName());
        updateRequest.addPart("analyzerflags", Long.toString(logFile.getAnalyserBitSet()));
        updateRequest.addPart("comments", logFile.getComments());

        NameValuePair[] data = 
        {
                new NameValuePair("user", getUserName()),
                new NameValuePair("pass", getPassword()),
                new NameValuePair("req",req.toXml())
        };

        post.setRequestBody(data);

        try
        {
            int response = _httpClient.executeMethod(post);
            if (response != HttpStatus.SC_OK)
            {
                
                System.out.println("a\n"+post.getResponseBodyAsString());
            }
            else
            {
                System.out.println(post.getResponseBodyAsString());
            }
        }
        catch (HttpException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public boolean updateRemoteLogsList()
    {
        return this.updateRemoteLogsList(0);
    }


    public boolean updateRemoteLogsList(int id)
    {
        if (!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return false;
        }
        PostMethod post = new PostMethod(_repoLocation+_repoRequestScript);
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request listRequest = req.createRequest("list");
        listRequest.addPart(new RequestPart("startId", Integer.toString(id)));

        NameValuePair[] data = 
        {
                new NameValuePair("user", getUserName()),
                new NameValuePair("pass", getPassword()),
                new NameValuePair("req",req.toXml())
        };

        post.setRequestBody(data);
        try
        {
            int response = _httpClient.executeMethod(post);
            if (response != HttpStatus.SC_OK)
            {
                return false;
            }
            post.getResponseHeaders();
            //System.out.println("========================");
            //System.out.println(post.getResponseBodyAsString());
            //System.out.println("========================");
            this.parseLogsListReply(post.getResponseBodyAsString());
            //System.out.println("HTTP "+response);
        }
        catch (HttpException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed updating remote logs list.\nPlease check your connection and proxy configs.\nReason: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            // TODO Auto-generated catch block
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public void parseLogsListReply(String xmlReply)
    {
        Node replies = this.getReplyRoot(xmlReply);
        System.out.println("number of replies: "+replies.getChildNodes().getLength());
        NamedNodeMap attributes;
        LogFile logFile;

        // XXX we presume that all(one) replies are for the list request
        // as multiple requests are not implemented client side yet
        for (Node reply = replies.getFirstChild(); reply != null; reply = reply.getNextSibling())
        {
            if (reply.getNodeName().equals("reply"))
            {
                attributes = reply.getAttributes();

                if (!attributes.getNamedItem("result").getNodeValue().equals("parsed"))
                    continue;

                for (Node log = reply.getFirstChild(); log != null; log = log.getNextSibling())
                {
                    if (log.getNodeName().equals("log"))
                    {
                        attributes = log.getAttributes();
                        String name = attributes.getNamedItem("filename").getNodeValue();
                        long size = Long.parseLong(attributes.getNamedItem("filesize").getNodeValue());
                        logFile = new LogFile(name, size, false, true);
                        logFile.setRemoteAnalyserBits(Long.parseLong(attributes.getNamedItem("analyzerflags").getNodeValue()));
                        logFile.setComments(attributes.getNamedItem("comment").getNodeValue());
                        logFile.setRemoteProtocolName(attributes.getNamedItem("protocol").getNodeValue());
                        logFile.setRemoteUploader(attributes.getNamedItem("uploader").getNodeValue());
                        logFile.setUploadedTime(Long.parseLong(attributes.getNamedItem("uploaded").getNodeValue())*1000);
                        logFile.setRemoteId(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
                        LogRepository.getInstance().addRemoteLog(logFile);
                    }
                }
            }
        }
    }

    public Node getReplyRoot(String xmlReply)
    {
        Document doc;
        InputSource is = new InputSource(new StringReader(xmlReply));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        DocumentBuilder docBuilder;
        try
        {
            docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.parse(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Node root = doc.getDocumentElement();
        if (!root.getNodeName().equals("replies"))
        {
            PacketSamurai.getUserInterface().log("LogRepository: Error: malformed reply: root node should be called 'replies'.");
            return null;
        }
        return root;
    }

    /**
     * We could have made some extensive xml format for those searches, but i'm lazy :p
     * @param search some search in our own format (for exemple 'author:John' or stuff like 'packets>5000')
     * @return
     */
    public List<LogFile> searchRemoteLogs(String search)
    {
        List<LogFile> logs = new FastList<LogFile>();
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return null;
        }
        PostMethod post = new PostMethod(_repoLocation+"/rpc.php");
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request listReq = req.createRequest("list");
        listReq.addPart(new RequestPart("matchQuery",search));

        post.addParameter("req",req.toXml());
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                //TODO omg
            }
            //TODO handle response
            post.getResponseHeaders();
            post.getResponseBodyAsStream();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return logs;
    }

    public void upLoadFile(LogFile file)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        file.loadHeaders();
        PostMethod post = new PostMethod(_repoLocation+_repoRequestScript);
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request uploadRequest = req.createRequest("upload");
        uploadRequest.addPart(new RequestPart("file",file.getName())); //TODO maybe we'll need some other info
        // i think we should send a list of all the packet ids at some point, tho is it a good idea to do it here?
        // coz if upload fails we wouldnt want this to be saved in db...
        // tho if we dotn do it now, when are w going to do so?
        // maybe we can make the client (us) associate packet id info after with some request.
        // making such a list also requires us to parse the file...
        /*uploadRequest.addPart(new RequestPart("comment",file.getComments()));
        uploadRequest.addPart(new RequestPart("analyserflags",Long.toString(file.getAnalyserBitSet())));
        uploadRequest.addPart(new RequestPart("servertype",file.getServerType()));
        uploadRequest.addPart(new RequestPart("protocol",file.getProtocolName()));
        uploadRequest.addPart(new RequestPart("sessionid",Long.toString(file.getSessionId())));*/
        /*Map<String, Integer> ids = createPacketIDList(file);
        RequestForPart idsPart = new RequestForPart("ids");
        for(Entry<String, Integer> id : ids.entrySet())
        {
            idsPart.addPart(new RequestPart(id.getKey(), id.getValue().toString()));
        }
        uploadRequest.addPart(idsPart);*/

        post.addParameter("req",req.toXml());
        System.out.println(req.toXml());
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                RemoteLogRepositoryBackend.getInstance().getUploadListener().transferFinished(file, false);
            }
            post.getResponseHeaders();
            this.parseUploadLogReply(file, post.getResponseBodyAsString());
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void parseUploadLogReply(LogFile logFile, String xmlReply)
    {
        Node replies = this.getReplyRoot(xmlReply);
        NamedNodeMap attributes;

        // XXX we presume that all(one) replies are for the list request
        // as multiple requests are not implemented client side yet
        for (Node reply = replies.getFirstChild(); reply != null; reply = reply.getNextSibling())
        {
            if (reply.getNodeName().equals("reply"))
            {
                attributes = reply.getAttributes();

                if (!attributes.getNamedItem("result").getNodeValue().equals("parsed"))
                    continue;

                for (Node log = reply.getFirstChild(); log != null; log = log.getNextSibling())
                {
                    if (log.getNodeName().equals("ticket"))
                    {
                        attributes = log.getAttributes();
                        long ticketId = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
                        System.out.println("got ticketid: "+ticketId);
                        this.upLoadFile(logFile, ticketId);
                    }
                }
            }
        }
    }

    public void upLoadFile(LogFile file, long ticketId)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        try
        {
            PostMethod filePost = new PostMethod(_repoLocation+_repoTicketScript);
            Part[] parts = 
            { 
                    new StringPart("user", this.getUserName()),
                    new StringPart("pass", this.getPassword()),
                    new StringPart("id", Long.toString(ticketId)),
                    new StringPart("req[comment]", file.getComments()),
                    new StringPart("req[analyzerflags]", Long.toString(file.getAnalyserBitSet())),
                    new StringPart("req[servertype]", file.getServerType()),
                    new StringPart("req[protocol]", file.getProtocolName()),
                    new LogFilePart(file.getName(), file)
            };
            filePost.setRequestEntity( new MultipartRequestEntity(parts, filePost.getParams()) );
            System.out.println("a");
            int response = _httpClient.executeMethod(filePost);
            System.out.println("b");
            if (response != HttpStatus.SC_OK)
            {
                System.out.println("HTTP "+response);
                System.out.println(filePost.getResponseBodyAsString());
            }
            else
            {
                file.setRemote(true);
                filePost.getResponseHeaders();
                System.out.println("OK");
                System.out.println(filePost.getResponseBodyAsString());

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void downLoadFile(LogFile file)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        PostMethod post = new PostMethod(_repoLocation+_repoRequestScript);
        post.addParameter("user", this.getUserName());
        post.addParameter("pass", this.getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request downloadReq = req.createRequest("download");
        downloadReq.addPart(new RequestPart("file", file.getName()));
        post.addParameter("req",req.toXml());

        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                // TODO omg
            }
            // TODO handle response
            post.getResponseHeaders();
            //System.out.println(post.getResponseBodyAsString());
            this.parseDownloadLogReply(file, post.getResponseBodyAsString());
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setDownloadListener(TransferPanel downloadListener)
    {
        _downloadListener = downloadListener;
    }

    public void setUploadListener(TransferPanel uploadListener)
    {
        _uploadListener = uploadListener;
    }

    public TransferPanel getUploadListener()
    {
        return _uploadListener;
    }

    public void parseDownloadLogReply(LogFile logFile, String xmlReply)
    {
        Node replies = this.getReplyRoot(xmlReply);
        NamedNodeMap attributes;

        // XXX we presume that all(one) replies are for the list request
        // as multiple requests are not implemented client side yet
        for (Node reply = replies.getFirstChild(); reply != null; reply = reply.getNextSibling())
        {
            if (reply.getNodeName().equals("reply"))
            {
                attributes = reply.getAttributes();

                if (!attributes.getNamedItem("result").getNodeValue().equals("parsed"))
                    continue;

                for (Node log = reply.getFirstChild(); log != null; log = log.getNextSibling())
                {
                    if (log.getNodeName().equals("ticket"))
                    {
                        attributes = log.getAttributes();
                        long ticketId = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
                        System.out.println("got ticketid: "+ticketId);
                        this.downloadFile(logFile, ticketId);
                    }
                }
            }
        }
    }

    public void downloadFile(LogFile file, long ticketId)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        PostMethod post = new PostMethod(_repoLocation+_repoTicketScript);
        post.addParameter("user", this.getUserName());
        post.addParameter("pass", this.getPassword());
        post.addParameter("id",Long.toString(ticketId));
        System.out.println("tid: "+ticketId);
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                // TODO omg
            }
            // TODO handle response
            post.getResponseHeaders();
            InputStream is = post.getResponseBodyAsStream();
            File localFile = new File(LogRepository.getInstance().getLogsDir()+"/"+file.getName());
            FileOutputStream fos = new FileOutputStream(localFile);

            byte[] buffer = new byte[4096];
            int len, progress;
            int total = 0;
            while ((len = is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, len);
                total += len;
                progress = (int) ((((double) total)/post.getResponseContentLength())*100);
                //System.out.println("total: "+total+" post rcl: "+post.getResponseContentLength()+ " - progress: "+progress);
                _downloadListener.updateTransferProgress(file, progress);
            }
            fos.flush();
            fos.close();
            file.setFile(localFile);
            file.setLocal(true);
            _downloadListener.transferFinished(file, true);
            //System.out.println(post.getResponseBodyAsString());
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            _downloadListener.transferFinished(file, false);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            _downloadListener.transferFinished(file, false);
            e.printStackTrace();
        }
    }

    public void deleteFile(LogFile file)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        Set<LogFile> files = new FastSet<LogFile>(1);
        files.add(file);
        deleteFiles(files);
    }

    public void deleteFiles(Set<LogFile> files)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return;
        }
        if(files.isEmpty())
            throw new IllegalArgumentException("wtf man? dont gimme an empty file set");
        PostMethod post = new PostMethod(_repoLocation+"/rpc.php");
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request deleteReq = req.createRequest("delete");
        RequestPart subPart = null;
        if(files.size() > 1)
        {
            subPart = new RequestForPart("fileset");
            for(LogFile file : files)
            {
                ((RequestForPart) subPart).addPart(new RequestPart("file", file.getName()));
            }
        }
        else
        {
            LogFile file = files.iterator().next();
            subPart = new RequestPart("file", file.getName());
        }
        deleteReq.addPart(subPart);

        post.addParameter("req",req.toXml());

        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                //omg
            }
            post.getResponseHeaders();
            post.getResponseBodyAsStream();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<String> listUsers()
    {
        List<String> users = new FastList<String>();
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return null;
        }
        PostMethod post = new PostMethod(_repoLocation+"/rpc.php");
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        @SuppressWarnings("unused")
        Request listReq = req.createRequest("listusers");

        post.addParameter("req",req.toXml());
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                //TODO omg
            }
            //TODO handle response
            post.getResponseHeaders();
            post.getResponseBodyAsStream();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }

    public boolean addOrModUser(String name, String pass, int rights)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return false;
        }
        PostMethod post = new PostMethod(_repoLocation+"/rpc.php");
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request addUserReq = req.createRequest("addmoduser");
        addUserReq.addPart(new RequestPart("name",name));
        addUserReq.addPart(new RequestPart("pass",RemoteLogRepositoryBackend.getPasswordHash(pass)));
        addUserReq.addPart(new RequestPart("rights",Integer.toString(rights)));

        post.addParameter("req",req.toXml());
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                //TODO omg
            }
            //TODO handle response
            post.getResponseHeaders();
            post.getResponseBodyAsStream();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteUser(String name)
    {
        if(!isConnected())
        {
            PacketSamurai.getUserInterface().log("You have to be connected to use the Remote Log Repository");
            return false;
        }
        PostMethod post = new PostMethod(_repoLocation+"/rpc.php");
        post.addParameter("user", getUserName());
        post.addParameter("pass", getPassword());
        CompoundXmlRequest req = new CompoundXmlRequest();
        Request deleteUserReq = req.createRequest("deleteuser");
        deleteUserReq.addPart(new RequestPart("name",name));

        post.addParameter("req",req.toXml());
        try
        {
            int response = _httpClient.executeMethod(post);
            if(response != HttpStatus.SC_OK)
            {
                //TODO omg
            }
            //TODO handle response
            post.getResponseHeaders();
            post.getResponseBodyAsStream();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getPasswordHash(String pass)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA");
            BigInteger sha;
            sha = new BigInteger(1,md.digest(pass.getBytes("UTF-8")));
            return sha.toString(16);
        }
        catch (NoSuchAlgorithmException e1)
        {
            e1.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unused")
    private static Map<String,Integer> createPacketIDList(LogFile file)
    {
        boolean mustUnload = false;
        if(!file.isFullyLoaded())
        {
            mustUnload = true;
            file.loadFully();
        }
        Session s = file.getSession();
        Map<String, Integer> packetIDs = new FastMap<String, Integer>();
        for(DataPacket packet : s.getPackets())
        {
            if (packet.getFormat() != null)
            {
                String op = packet.getPacketFormat().getOpcodeStr();
                Integer count = packetIDs.get(op);
                if(count != null)
                {
                    packetIDs.put(op, count+1);
                }
                else
                {
                    packetIDs.put(op, 1);
                }
            }
        }
        if(mustUnload)
            file.unLoadSessionPackets();
        return packetIDs;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName)
    {
        _userName = userName;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return _userName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.setPasswordHashed(password == null ? null : RemoteLogRepositoryBackend.getPasswordHash(password));
    }
    
    /**
     * @param password the password to set
     */
    public void setPasswordHashed(String password)
    {
        _password = password;
    }

    /**
     * @return the password (HASHED)
     */
    public String getPassword()
    {
        return _password;
    }
}