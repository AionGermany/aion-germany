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


package com.aionemu.packetsamurai.parser.formattree;

import java.util.List;


import com.aionemu.packetsamurai.PacketSamurai;
import com.aionemu.packetsamurai.parser.PartType;


/**
 * 
 * @author Ulysses R. Ribeiro
 */
public class ForPart extends Part
{
    private PartContainer _modelBlock;
    private int _forId = -1;
	private int size;

	public ForPart(int id)
	{
		this(id, "");
	}
    public ForPart(int id, int size, String analyzerName) {
		this(id, analyzerName);
		this.size = size;
	}

    public ForPart(int id, String analyzerName)
    {
        super(PartType.forBlock);
        this.setAnalyzerName(analyzerName);
        this.setForId(id);
        _modelBlock = new PartContainer(PartType.block);
    }

	public int getSize()
    {
        return size;
    }

    public PartContainer getModelBlock()
    {
        return _modelBlock;
    }
    
	public void addPart(Part part)
	{
        this.getModelBlock().addPart(part);
        part.setContainingFormat(this.getContainingFormat());
        part.setParentContainer(this.getParentContainer()); // this can NOT be root
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
	}

	public void addParts(List<Part> parts)
	{
        this.getModelBlock().addParts(parts);
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
	}
	
    public Part getPartById(int id)
    {
        return _modelBlock.getPacketPartById(id);
    }
	
	public String treeString()
	{
        Part pp;
		if ((pp = this.getParentContainer().getPacketPartByIdInScope(this.getForId(),this)) != null)
		{
			return "For.. : '"+pp.getName()+"'";
		}
        PacketSamurai.getUserInterface().log("ForSize Part id "+this.getForId()+" not found");
	    return "For..";
	}
    
    public int getForId()
    {
        return _forId;
    }
    
	public void setForId(int id)
	{
		_forId = id;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
	}

	public boolean addPartAfter(Part part, Part afterPart)
	{
		return _modelBlock.addPartAfter(part, afterPart);
	}

	public boolean removePart(Part part)
	{
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
		return _modelBlock.removePart(part);
	}
    
    /**
     * for can not have an id
     */
    public int getId()
    {
        return -1;
    }
    
    @Override
    public void setParentContainer(PartContainer pc)
    {
        super.setParentContainer(pc);
        _modelBlock.setParentContainer(pc);
    }
    
    @Override
    public void setContainingFormat(Format format)
    {
        super.setContainingFormat(format);
        _modelBlock.setContainingFormat(format);
    }
}