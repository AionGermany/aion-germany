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


package com.aionemu.packetsamurai.logrepo.communication;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javolution.util.FastList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class CompoundXmlRequest
{
    private List<Request> _requests;
    private int _currentId;
    
    public CompoundXmlRequest()
    {
        _requests = new FastList<Request>();
    }
    
    public Request createRequest(String type)
    {
        Request req = new Request(_currentId++, type);
        _requests.add(req);
        return req;
    }

    public String toXml()
    {
        Document document;
        DocumentBuilder docBuilder;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            docBuilder = factory.newDocumentBuilder();
            document = docBuilder.newDocument();
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        DOMImplementationLS implLS = (DOMImplementationLS) docBuilder.getDOMImplementation();
        LSSerializer domWriter = implLS.createLSSerializer();
        LSOutput output = implLS.createLSOutput();
        
        //DOMException: FEATURE_NOT_SUPPORTED: The parameter format-pretty-print is recognized but the requested value cannot be set.
        //domWriter.getDomConfig().setParameter(Constants.DOM_FORMAT_PRETTY_PRINT, Boolean.TRUE);
        
        output.setEncoding("UTF-8");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        output.setByteStream(outStream);
        
        
        Element root = document.createElement("requests");
        for(Request req : _requests)
        {
            Element reqNode = document.createElement("request");
            reqNode.setAttribute("id", Integer.toString(req.getId()));
            reqNode.setAttribute("type", req.getType());
            for(RequestPart part : req.getParts())
            {
                Element partNode = null;
                if(part instanceof RequestForPart)
                {
                    partNode = document.createElement("for");
                    writeForPart(partNode, (RequestForPart) part, document);
                }
                else
                {
                    partNode = document.createElement("part");
                    partNode.setAttribute("name", part.getName());
                    partNode.setAttribute("value", part.getValue());
                }
                reqNode.appendChild(partNode);
            }
            root.appendChild(reqNode);
        }
        
        document.appendChild(root);
        domWriter.write(document, output);
        
        return outStream.toString();
    }
    
    public void writeForPart(Element forNode, RequestForPart forPart, Document doc)
    {
        for(RequestPart part : forPart.getSubParts())
        {
            Element partNode = null;
            if(part instanceof RequestForPart)
            {
                partNode = doc.createElement("for");
                partNode.setAttribute("name", part.getName());
                writeForPart(partNode, (RequestForPart) part, doc);
            }
            else
            {
                partNode = doc.createElement("part");
                partNode.setAttribute("name", part.getName());
                partNode.setAttribute("value", part.getValue());
            }
            forNode.appendChild(partNode);
        }
    }
    
}