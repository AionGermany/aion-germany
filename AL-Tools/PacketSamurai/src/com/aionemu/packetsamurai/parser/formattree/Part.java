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


import com.aionemu.packetsamurai.parser.PartType;
import com.aionemu.packetsamurai.parser.valuereader.Reader;

/**
 * Used to represent the structure, it does not contain data
 * 
 * @author Ulysses R. Ribeiro, Gilles Duboscq
 */
public class Part
{
    private PartType _type;
    private int _id;
    private String _name;
    private String _comment;
    private String _lookUpType;
    private String _analyzerName;
    private PartContainer _parentContainer;
    private Format _containingFormat;
    private int _bxSizeId;
    private int _bxSize;
    private boolean _dynamicBSize = false;
    private boolean _isRoot;
    private boolean _isRealPart;
    private Reader _reader;
    
    public Part(PartType type, int id, String name, String comment, String lookUpType)
    {
        this(type, id, name, comment, lookUpType, "");
    }
    
    public Part(PartType type, int id, String name, String comment, String lookUpType, String analyzerName)
    {
        this.setType(type);
        this.setId(id);
        this.setName(name);
        this.setComment(comment);
        this.setLookUpType(lookUpType);
        this.setAnalyzerName(analyzerName);
        _isRealPart = true;
    }
    
    public Part(PartType type)
    {
        this(type, false);
    }
    
    public Part(boolean isRoot)
    {
        this(PartType.block, isRoot);
    }
    
    public Part(PartType type, boolean isRoot)
    {
        this.setType(type);
        _isRoot = isRoot;
        _isRealPart = false;
    }
    
    public void setType(PartType type)
    {
        _type = type;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }

    public PartType getType()
    {
        return _type;
    }
    
    public int getId() 
    {
        return _id;
    }

    public void setId(int id)
    {
        _id = id;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public String getLookUpType()
    {
        return _lookUpType;
    }

    public void setLookUpType(String lookUp)
    {
        _lookUpType = lookUp;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }

    public String getComment()
    {
        return _comment;
    }

    public void setComment(String comment)
    {
        _comment = comment;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }

    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public void setAnalyzerName(String analyzerName)
    {
        _analyzerName = analyzerName;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public String getAnalyzerName()
    {
        return _analyzerName;
    }
    
    /**
     * @return The parent container or null if this is the top level part.
     */
    public PartContainer getParentContainer()
    {
        return _parentContainer;
    }
    
    public void setParentContainer(PartContainer pc)
    {
        _parentContainer = pc;
    }
    
    public void setContainingFormat(Format format)
    {
        _containingFormat = format;
    }
    
    public Format getContainingFormat()
    {
        return _containingFormat;
    }
    
    public boolean isInteger()
    {
        return this.getType() == PartType.c ||
            this.getType() == PartType.h ||
            this.getType() == PartType.d;
    }
    
    public boolean isLongInteger()
    {
        return this.getType() == PartType.Q;
    }
    
    public boolean isString()
    {
        return this.getType() == PartType.S ||
            this.getType() == PartType.s;
    }
    
    public boolean isByteArray()
    {
        return this.getType() == PartType.x ||
            this.getType() == PartType.b;
    }
    
    public String toString()
    {
        return this.getType().getName();
    }
    
    public void setBSizeId(int sizeid)
    {
        _bxSizeId  = sizeid;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }

    public int getBSizeId()
    {
        return _bxSizeId;
    }
    
    public boolean isDynamicBSize()
    {
        return _dynamicBSize;
    }
    
    public void setDynamicBSize(boolean b)
    {
        _dynamicBSize = b;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public void setBSize(int size)
    {
        _bxSize = size;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public int getBSize()
    {
        return _bxSize;
    }
    
    public boolean isRoot()
    {
        return _isRoot;
    }
    
    public boolean isRealPart()
    {
        return _isRealPart;
    }
    
    public void setReader(Reader r)
    {
        _reader = r;
        if(this.getContainingFormat() != null)
            this.getContainingFormat().triggerFormatChanged();
    }
    
    public Reader getReader()
    {
        return _reader;
    }
}
