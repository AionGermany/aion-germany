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

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.nio.ByteOrder;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class OrderedDataOutputStream extends FilterOutputStream implements DataOutput
{
    private ByteOrder _order;
    private byte writeBuffer[] = new byte[8];
    
    /**
     * Default order is Little endian
     * @param out
     */
    public OrderedDataOutputStream(OutputStream out)
    {
        super(out);
        _order = ByteOrder.LITTLE_ENDIAN;
    }
    
    /**
     * 
     * @param out
     * @param order
     */
    public OrderedDataOutputStream(OutputStream out, ByteOrder order)
    {
        super(out);
        _order = order;
    }
    
    public synchronized void write(int b) throws IOException
    {
        out.write(b);
    }
    
    public synchronized void write(byte b[], int off, int len) throws IOException
    {
        out.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException
    {
        out.write(v ? 0x01 : 0x00);
    }

    public void writeByte(int v) throws IOException
    {
        out.write(v);
    }

    public void writeBytes(String s) throws IOException
    {
        int len = s.length();
        for (int i = 0 ; i < len ; i++)
        {
            out.write((byte)s.charAt(i));
        }
    }

    public void writeChar(int v) throws IOException
    {
        if(_order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write((v) & 0xFF);
            out.write((v >>> 8) & 0xFF);
        }
        else
        {
            out.write((v >>> 8) & 0xFF);
            out.write((v) & 0xFF);
        }
    }

    public void writeChars(String s) throws IOException
    {
        int len = s.length();
        for (int i = 0 ; i < len ; i++)
        {
            writeChar(s.charAt(i));
        }
    }

    public void writeDouble(double v) throws IOException
    {
        writeLong(Double.doubleToRawLongBits(v));
    }

    public void writeFloat(float v) throws IOException
    {
        writeInt(Float.floatToIntBits(v));
    }

    public void writeInt(int v) throws IOException
    {
        if(_order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write((v) & 0xFF);
            out.write((v >>>  8) & 0xFF);
            out.write((v >>> 16) & 0xFF);
            out.write((v >>> 24) & 0xFF);
        }
        else
        {
            out.write((v >>> 24) & 0xFF);
            out.write((v >>> 16) & 0xFF);
            out.write((v >>>  8) & 0xFF);
            out.write((v) & 0xFF);
        }
    }

    public void writeLong(long v) throws IOException
    {
        if(_order == ByteOrder.LITTLE_ENDIAN)
        {
            writeBuffer[0] = (byte)(v);
            writeBuffer[1] = (byte)(v >>>  8);
            writeBuffer[2] = (byte)(v >>> 16);
            writeBuffer[3] = (byte)(v >>> 24);
            writeBuffer[4] = (byte)(v >>> 32);
            writeBuffer[5] = (byte)(v >>> 40);
            writeBuffer[6] = (byte)(v >>> 48);
            writeBuffer[7] = (byte)(v >>> 56);
        }
        else
        {
            writeBuffer[0] = (byte)(v >>> 56);
            writeBuffer[1] = (byte)(v >>> 48);
            writeBuffer[2] = (byte)(v >>> 40);
            writeBuffer[3] = (byte)(v >>> 32);
            writeBuffer[4] = (byte)(v >>> 24);
            writeBuffer[5] = (byte)(v >>> 16);
            writeBuffer[6] = (byte)(v >>>  8);
            writeBuffer[7] = (byte)(v);
        }
        out.write(writeBuffer, 0, 8);
    }

    public void writeShort(int v) throws IOException
    {
        if(_order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write((v) & 0xFF);
            out.write((v >>> 8) & 0xFF);
        }
        else
        {
            out.write((v >>> 8) & 0xFF);
            out.write((v) & 0xFF);
        }
    }

    /**
     * not really ordered according to the order, using java's UTF-8 standard
     */
    public void writeUTF(String str) throws IOException
    {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;
     
            /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++)
        {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F))
            {
                utflen++;
            }
            else if (c > 0x07FF)
            {
                utflen += 3;
            }
            else
            {
                utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                    "encoded string too long: " + utflen + " bytes");

        byte[] bytearr = null;
        bytearr = new byte[utflen+2];
         
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen) & 0xFF);
            
        int i=0;
        for (i=0; i<strlen; i++)
        {
           c = str.charAt(i);
           if (!((c >= 0x0001) && (c <= 0x007F))) break;
           bytearr[count++] = (byte) c;
        }
        
        for (;i < strlen; i++)
        {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F))
            {
                bytearr[count++] = (byte) c;
            }
            else if (c > 0x07FF)
            {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c) & 0x3F));
            }
            else
            {
                bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen+2);
    }
    
}