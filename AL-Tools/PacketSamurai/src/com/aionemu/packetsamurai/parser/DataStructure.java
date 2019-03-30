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


package com.aionemu.packetsamurai.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.Util;
import com.aionemu.packetsamurai.parser.datatree.DataForBlock;
import com.aionemu.packetsamurai.parser.datatree.DataForPart;
import com.aionemu.packetsamurai.parser.datatree.DataSwitchBlock;
import com.aionemu.packetsamurai.parser.datatree.DataTreeNode;
import com.aionemu.packetsamurai.parser.datatree.DataTreeNodeContainer;
import com.aionemu.packetsamurai.parser.datatree.IntBSValuePart;
import com.aionemu.packetsamurai.parser.datatree.IntValuePart;
import com.aionemu.packetsamurai.parser.datatree.ValuePart;
import com.aionemu.packetsamurai.parser.formattree.ForPart;
import com.aionemu.packetsamurai.parser.formattree.WhilePart;
import com.aionemu.packetsamurai.parser.formattree.Format;
import com.aionemu.packetsamurai.parser.formattree.Part;
import com.aionemu.packetsamurai.parser.formattree.PartContainer;
import com.aionemu.packetsamurai.parser.formattree.SwitchCaseBlock;
import com.aionemu.packetsamurai.parser.formattree.AbstractSwitchPart;
import com.aionemu.packetsamurai.parser.formattree.SwitchPart;

import javolution.util.FastList;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DataStructure
{
    private ByteOrder _order;
    private ByteBuffer _buf;
    private byte[] _unparsed;
    private Format _format;
    private DataTreeNodeContainer _packetParts;
    private boolean _mustUpdate = true;
    private boolean _isValid;
    protected String _warning;
    protected String _error;
    private DataPacketMode _mode;
    private OrderedDataOutputStream _forgingStream;
    
    public enum DataPacketMode
    {
        PARSING,
        FORGING
    }

    /**
     * Default is Little endian provide a prepared BteBuffer if you want to use an other order
     * @param raw
     * @param dir
     */
    public DataStructure(byte[] raw, Format format)
    {
        this(ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN), format);
    }
    
    public DataStructure(ByteBuffer buf, Format format)
    {
        this.setByteBuffer(buf);
        _format = format;
        _mode = DataPacketMode.PARSING;
        _order = buf.order();
    }
    
    public DataStructure(DataTreeNodeContainer root)
    {
        this(root,null,ByteOrder.LITTLE_ENDIAN);
    }
    
    public DataStructure(DataTreeNodeContainer root, ByteOrder order)
    {
        this(root,null,order);
    }
    
    public DataStructure(DataTreeNodeContainer root, Format format)
    {
        this(root,format,ByteOrder.LITTLE_ENDIAN);
    }
    
    public DataStructure(DataTreeNodeContainer root, Format format, ByteOrder order)
    {
        if(!root.isRoot())
            throw new IllegalArgumentException("The root of a Forging mode packet must be... root :p");
        _packetParts = root;
        _format = format;
        _mode = DataPacketMode.FORGING;
        _order = order;
    }

    public synchronized void parse()
    {
        if(this.getMode() != DataPacketMode.PARSING)
            throw new IllegalStateException("Can not parse a non-parsing mode DataPacket");
        if(!_mustUpdate) // could also be used to invalidate parsing results after protocol change
            return;
        _mustUpdate =  false;
        _packetParts =  new DataTreeNodeContainer();
        this.getByteBuffer().rewind();
        if (this.getFormat() != null)
        {
            this.getFormat().registerFormatChangeListener(this);
            boolean ret = parse(this.getFormat().getMainBlock(),_packetParts);
            this.setValid(ret);
            if(!ret &&PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERBOSE.ordinal())
            {
                if (this.getFormat() != null)
                {
                    System.out.println(this.getFormat().toString());
                }
                dumpParts();
            }
        }
        else
        {
            this.setValid(false);
        }
        if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERY_VERBOSE.ordinal())
        {
            if(this.getFormat() != null)
            {
                System.out.println(this.getFormat().toString());
            }
            dumpParts();
        }
    }
    
    private boolean parse(PartContainer protocolNode, DataTreeNodeContainer dataNode)
    {
        for(Part part : protocolNode.getParts())
        {
        	if(part instanceof WhilePart)
            {
                // find the size of this for in the scope
                ValuePart vp = dataNode.getPacketValuePartById(((ForPart)part).getForId());
                if(vp == null)
                {
                    _error = "Error: could not find valuepart to loop on for (While "+part.getName()+" - id:"+((ForPart)part).getForId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                if(!(vp instanceof IntValuePart))
                {
                    _error = "Error: for id didnt refer to an IntValePart in (While "+part.getName()+" - id:"+((ForPart)part).getForId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                int size;
                if(vp instanceof IntBSValuePart)
                	size = ((IntBSValuePart)vp).getBitCount();
                else
                	size = ((IntValuePart)vp).getIntValue();
                if(size < 0)
                	size =- size;
                //check size here
                if (size > this.getByteBuffer().remaining())
                {
                    _error = "Error size is too big ("+size+") for While (Part Name: "+part.getName()+" - Id: "+((ForPart)part).getForId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                DataForPart forPart = new DataForPart(dataNode, (ForPart) part);
                int reamingStop = getByteBuffer().remaining()-size;
                int i = 0;
                while(getByteBuffer().remaining() > reamingStop)
                {
                    DataForBlock forBlock = new DataForBlock(forPart, ((ForPart)part).getModelBlock(),i++,size);
                    if(!parse(((ForPart)part).getModelBlock(), forBlock))
                        return false;
                }
            }
        	else if (part instanceof ForPart) {
				int size;
				ForPart fp = ((ForPart) part);
				if (fp.getSize() != 0) {
					size = fp.getSize();
				}
				else {
					// find the size of this for in the scope
					ValuePart vp = dataNode.getPacketValuePartById(fp.getForId());
					if (vp == null) {
						_error = "Error: could not find valuepart to loop on for (For " + part.getName() + " - id:" + fp.getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
						return false;
					}
					if (!(vp instanceof IntValuePart)) {
						_error = "Error: for id didnt refer to an IntValePart in (For " + part.getName() + " - id:" + fp.getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
						return false;
					}

					if (vp instanceof IntBSValuePart)
						size = ((IntBSValuePart) vp).getBitCount();
					else
						size = ((IntValuePart) vp).getIntValue();
					if (size < 0)
						size = -size;
				}
				//check size here
				if (fp.getModelBlock().hasConstantLength()) {
					int forBlockSize = fp.getModelBlock().getLength();
					if (size * forBlockSize > this.getByteBuffer().remaining()) {
						_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + fp.getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
						return false;
					}
				}
				else if (size > this.getByteBuffer().remaining()) {
					_error = "Error size is too big (" + size + ") for For (Part Name: " + part.getName() + " - Id: " + fp.getForId() + ") in [" + part.getContainingFormat().getContainingPacketFormat() + "]";
					return false;
				}
				DataForPart forPart = new DataForPart(dataNode, (ForPart) part);
				for (int i = 0; i < size; i++) {
					DataForBlock forBlock = new DataForBlock(forPart, fp.getModelBlock(), i, size);
					if (!parse(fp.getModelBlock(), forBlock))
						return false;
				}
			}
            else if(part instanceof AbstractSwitchPart)
            {
                //find the actual type
                ValuePart vp= dataNode.getPacketValuePartById(((AbstractSwitchPart)part).getSwitchId());
                if (vp == null)
                {
                    _error = "Error: could not find valuepart to switch on for Switch (Part: "+part.getName()+" - id:"+((AbstractSwitchPart)part).getSwitchId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                if (!(vp instanceof IntValuePart))
                {
                    _error = "Error: swicth id didnt refer to an IntValePart in Switch (Part: "+part.getName()+" - id:"+((AbstractSwitchPart)part).getSwitchId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                List<SwitchCaseBlock> caseBlocksFormat = ((AbstractSwitchPart)part).getCases(((IntValuePart)vp).getIntValue());
                if (caseBlocksFormat.isEmpty() && part instanceof SwitchPart)
                {
                    _error = "Error: no such case: "+((IntValuePart)vp).getIntValue()+" for (Switch "+part.getName()+" - id:"+((AbstractSwitchPart)part).getSwitchId()+") in ["+part.getContainingFormat().getContainingPacketFormat()+"]";
                    return false;
                }
                for(SwitchCaseBlock caseBlockFormat : caseBlocksFormat)
                {
                    DataSwitchBlock caseBlock = new DataSwitchBlock(dataNode, caseBlockFormat, vp);
                    if(!parse(caseBlockFormat,caseBlock))
                        return false;
                }
            }
            else if(part instanceof PartContainer)
            {
                _error = "Error: Unparsed new type of PartContainer ("+this.getClass().getSimpleName()+")";
                return false;
            }
            else if(part.getType().isReadableType())
            {
                ValuePart vp = part.getType().getValuePart(dataNode, part);
                vp.parse(this.getByteBuffer());
            }
        }
        return true;
    }
    
    public synchronized boolean forge()
    {
        if(this.getMode() != DataPacketMode.FORGING)
            throw new IllegalStateException("Can not forge a non-forging mode DataPacket");
        if(!isTreeValid())
            throw new IllegalStateException("Tree must be valid before Forging");
        ByteArrayOutputStream forgingBaos = new ByteArrayOutputStream();
        _forgingStream = new OrderedDataOutputStream(forgingBaos,_order);
        boolean ret = this.forge(this.getRootNode());
        ByteBuffer newbuf = ByteBuffer.allocate(forgingBaos.size());
        newbuf.put(forgingBaos.toByteArray());
        newbuf.rewind();
        newbuf.order(_order);
        this.setByteBuffer(newbuf);
        return ret;
    }
    
    private boolean forge(DataTreeNodeContainer node)
    {
        for(DataTreeNode subnode : node.getNodes())
        {
            if(subnode instanceof DataForPart)
            {
                for(DataForBlock block :((DataForPart)subnode).getNodes())
                {
                    forge(block);
                }
            }
            else if(subnode instanceof DataSwitchBlock)
            {
                forge((DataSwitchBlock) subnode);
            }
            else if(subnode instanceof ValuePart)
            {
                try
                {
                    ((ValuePart)subnode).forge(_forgingStream);
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return true;       
    }

    public DataTreeNodeContainer getRootNode()
    {
        if(this.getMode() == DataPacketMode.PARSING)
            this.parse();
        return _packetParts;
    }
    
    public DataPacketMode getMode()
    {
        return _mode;
    }

    public Format getFormat()
    {
        return _format;
    }
    
    public void setFormat(Format f)
    {
        _format = f;
    }
    
    public void dumpParts()
    {
        dumpParts(_packetParts, 0);
    }
    
    public void dumpParts(DataTreeNodeContainer node, int depth)
    {
        for (DataTreeNode n : node.getNodes())
        {
            if(n instanceof ValuePart)
            {
                System.out.println(Util.repeat(" ", depth*2)+"Name: "+((ValuePart)n).getModelPart().getName()+" ("+((ValuePart)n).getType()+") - Part value: "+((ValuePart)n).getValueAsString()+" "+((ValuePart)n).readValue());
            }
            else
            {
                System.out.println(Util.repeat(" ", depth*2)+"Name: "+((DataTreeNodeContainer)n).getModelPart().getName()+" - Part: "+((DataTreeNodeContainer)n).getModelPart().getType());
                dumpParts((DataTreeNodeContainer)n, depth+1);
            }
        }
    }

    // All those read methods shouldnt exist
    public int readC()
    {
        return getByteBuffer().get() & 0xFF;
    }

    public int readH()
    {
        return getByteBuffer().getShort() & 0xFFFF;
    }

    public int readD()
    {
        return getByteBuffer().getInt();
    }

    public double readF()
    {
        return getByteBuffer().getDouble();
    }

    public String readS()
    {
        StringBuffer sb = new StringBuffer();
        char ch;
        while ((ch = getByteBuffer().getChar()) != 0)
            sb.append(ch);
        return sb.toString();
    }

    public byte[] readB( int length)
    {
        byte[] result = new byte[length];
        getByteBuffer().get(result);
        return result;
    }

    public int readIntType(PartType type)
    {
        switch (type.getTypeByteNumber())
        {
            case 1:
                return readC();
            case 2:
                return readH();
            case 4:
                return readD();
        }
        throw new IllegalArgumentException("Type is not an Int :"+type.getName());
    }

    public byte get(int idx)
    {
        return getByteBuffer().get(idx);
    }

    public byte[] getUnparsedData()
    {
        if (_unparsed == null)
        {
            int size = getByteBuffer().remaining();
            _unparsed = new byte[size];
            getByteBuffer().get(_unparsed);
        }
        return _unparsed;
    }
    
    public byte[] getData()
    {
        return getByteBuffer().array();
    }
    
    public int getSize()
    {
        return getByteBuffer().limit();
    }

    /**
     * to be used by UI
     * @return
     */
    public List<ValuePart> getValuePartList()
    {
        return getValuePartList(this.getRootNode());
    }
    
    private List<ValuePart> getValuePartList(DataTreeNodeContainer node)
    {
        FastList<ValuePart> parts = new FastList<ValuePart>();
        for(DataTreeNode n : node.getNodes())
        {
            if(n instanceof ValuePart)
                parts.add((ValuePart) n);
            else if(n instanceof DataTreeNodeContainer)
                parts.addAll(getValuePartList((DataTreeNodeContainer) n));
        }
        return parts;
    }

    protected void setValid(boolean val)
    {
        _isValid = val;
    }
    
    public boolean isValid()
    {
        return _isValid || _mustUpdate;
    }
    
    public void invalidateParsing()
    {
        if(this.getMode() != DataPacketMode.PARSING)
            throw new IllegalStateException("Can not invalidate parsing on a non-parsing mode DataPacket");
        synchronized(this)
        {
            this.setValid(false);
            _mustUpdate = true;
            _packetParts = null;
        }
            
    }
    
    public void invalidateForging()
    {
        if(this.getMode() != DataPacketMode.FORGING)
            throw new IllegalStateException("Can not invalidate forging on a non-forging mode DataPacket");
        synchronized(this)
        {
            this.setValid(false);
            _mustUpdate = true;
        }
    }
    
    public boolean hasWarning()
    {
        return _warning != null;
    }
    
    public boolean hasError()
    {
        return _error != null;
    }
    
    public String getErrorMessage()
    {
        return _error != null ? _error : _warning;
    }
    
    public boolean isTreeValid()
    {
        boolean ret;
        if(this.getFormat() != null)
        {
            // validate against Format
            ret = validateTree(this.getRootNode(), this.getFormat().getMainBlock());
        }
        else
        {
            // just validate the structure
            ret = validateTree(this.getRootNode());
        }
        return ret;
    }
    
    public ByteBuffer getByteBuffer()
    {
        return _buf;
    }

    protected void setByteBuffer(ByteBuffer buf)
    {
        //if(this.getMode() == DataPacketMode.FORGING)
        //    throw new IllegalStateException("Can not force the ByteBuffer for a Forging mode DataPacket");
        _buf = buf;
    }
    
    private boolean validateTree(DataTreeNodeContainer node)
    {
        boolean insideFor = node instanceof DataForPart;
        DataForBlock model = null;
        for(DataTreeNode n : node.getNodes())
        {
            if(n instanceof DataForPart)
            {
                if(insideFor)
                    return false;
                if(!validateTree((DataForPart)n))
                    return false;
            }
            else if(n instanceof DataSwitchBlock)
            {
                if(insideFor)
                    return false;
                if(!validateTree((DataSwitchBlock)n))
                    return false;
            }
            else if(n instanceof DataForBlock)
            {
                if(!insideFor)
                    return false;
                if(model == null)
                {
                    model = (DataForBlock) n;
                }
                else
                {
                    if(!branchesEqual(model, (DataForBlock)n))
                        return false;
                }
                if(!validateTree((DataForBlock)n))
                    return false;
            }
            else if(insideFor)
            {
                return false;
            }
        }
        return true;
    }
    
    private boolean branchesEqual(DataTreeNodeContainer branch1, DataTreeNodeContainer branch2)
    {
        Iterator<? extends DataTreeNode> it1 = branch1.getNodes().iterator();
        Iterator<? extends DataTreeNode> it2 = branch2.getNodes().iterator();
        while(it1.hasNext())
        {
            if(!it2.hasNext())
                return false;
            DataTreeNode node1 = it1.next();
            DataTreeNode node2 = it2.next();
            if(node1.getClass() !=  node2.getClass())
                return false;
            if(node1 instanceof IntValuePart && ((IntValuePart)node1).getType() != ((IntValuePart)node2).getType())
                return false;
            //if(node1 instanceof D)
        }
        if(it2.hasNext())
            return false;
        return true;
    }
    
    private boolean validateTree(DataTreeNodeContainer dataTreeNode, PartContainer protocolNode)
    {
        return true;
    }
}