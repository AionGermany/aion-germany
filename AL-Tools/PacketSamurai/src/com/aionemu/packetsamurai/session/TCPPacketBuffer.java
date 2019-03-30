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


/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.packetsamurai.session;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;
import jpcap.packet.TCPPacket;

/**
 * Buffer for sequencing packets, since they are captured out of sequence
 * @author Ulysses R. Ribeiro
 */
public class TCPPacketBuffer 
{
    public static final boolean DEBUG_RAW_TRAFFIC = false;
    private static FileOutputStream _bao;
    private static final long MODULO = 4294967296L;
    private Map<Long, SeqHolder> _waitingPrevious = new FastMap<Long, SeqHolder>();
    private List<TCPPacket> _sequenced = new FastList<TCPPacket>();
    private long _lastAck;

    public TCPPacketBuffer()
    {
        if (DEBUG_RAW_TRAFFIC)
        {
            try
            {
                _bao = new FileOutputStream("lol.bin");
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void add(TCPPacket p)
    {
        for (SeqHolder sh : _waitingPrevious.values())
        {
            TCPPacket old = sh.getPacket();
            if (sh.getPacket().sequence == p.sequence)
            {
                if (old.data.length < p.data.length)
                {
                    int diff = p.data.length - old.data.length;
                    //System.err.printf("DIFF %d = %d - %d \n",diff, p.data.length, old.data.length);
                    long seq = (old.sequence + old.data.length)%MODULO;
                    //System.err.println("ADJUSTED TO SEQ: "+seq+"\nPACKET: "+p);
                    byte[] data = new byte[diff];
                    System.arraycopy(p.data, p.data.length - diff, data, 0, data.length);
                    p.data = data;
                    p.sequence = seq;
                    //System.err.println(Util.hexDump(data));
                }
                else if (old.data.length == p.data.length)
                {
                    // packet retransmitted
                    // dont add, else the data will be duped (acked again)
                    return;
                }
            }
        }
        
        long nextSeq = (p.sequence + p.data.length)%MODULO;
        
        
        _waitingPrevious.put(nextSeq, new SeqHolder(nextSeq, p));
        this.processAck(_lastAck);
    }

    public void ack(TCPPacket p)
    {
        _lastAck = p.ack_num;

        processAck(p.ack_num);

        byte[] options = p.option;
        if (options != null)
        {
            long start = -1;
            long end = -1;

            for (int i = 0; i < options.length;)
            {
                // 0 end of opts
                // 1 nop
                if (options[i] == 0 || options[i] == 1)
                {
                    i++;
                    continue;
                }

                int type = options[i++] & 0xff;
                int size = options[i++] & 0xff;
                switch (type)
                {
                    case 5: //SACK
                        if (size-2 == 8)
                        {
                            //System.err.println("SACK no pacote: "+p);
                            //System.exit(0);
                            start = getUInt(options, i);
                            i += 4;
                            end = getUInt(options, i);
                        }
                        else
                        {
                            throw new RuntimeException("No support for multiple S-ACK");
                        }
                        break;
                    default: //unsupported operation
                        break;
                }

                i += size - 2;
            }

            while (end != -1 && start != -1)
            {
                SeqHolder sh = null;
                for (SeqHolder seqHolder : _waitingPrevious.values())
                {
                    if (seqHolder.getPacket().sequence == start)
                    {
                        long nextSeq = (seqHolder.getPacket().sequence + seqHolder.getPacket().data.length)%MODULO;
                        
                        // ignore if SACK covers whole packet
                        if (end != nextSeq)
                        {
                            sh = seqHolder;
                            break;
                        }
                    }
                }
                
                // if not already ack'ed (duplicate ack recvd)
                if (sh != null)
                {
                    //System.err.println("NULL ACK: "+_lastAck+" -> "+p.toString());
                    //System.err.println("END: "+end+" START: "+start);
                    int diff = (int) ((end - start)%MODULO);
                    TCPPacket packet = sh.getPacket();
                    
                    // check if sack doesnt overlap to next packet
                    if (diff > packet.data.length)
                    {
                        // this one covers whole packet so just ignore
                        start =  (start + packet.data.length)%MODULO;
                        continue;
                    }
                    start = -1;
                    end = -1;
                    byte[] data = new byte[diff];
                    
                    //System.err.println("sh SEQ: "+sh.getPacket().sequence+" LEN: "+packet.data.length+" DIFF: "+diff);
                    byte[] data2 = new byte[packet.data.length - diff];

                    System.arraycopy(sh.getPacket().data, 0, data, 0, data.length);
                    System.arraycopy(sh.getPacket().data, diff, data2, 0, data2.length);
                    //System.err.println("### SACK ("+data2.length+")\nCAUSED BY: "+p+"\nON: "+sh.getPacket());

                    TCPPacket sackPacket = new TCPPacket(packet.src_port, packet.dst_port, packet.sequence, packet.ack_num, packet.urg, packet.ack, packet.psh, packet.rst, packet.syn, packet.fin, packet.rsv1, packet.rsv2, packet.window, packet.urgent_pointer);
                    sackPacket.data = data;
                    sh.getPacket().data = data2;

                    this.addSequenced(sackPacket);
                    //System.err.println(Util.printData(sackPacket.data));

                    //long prevSeq = (p.ack_num - sh.getPacket().data.length)%MODULO;
                    //long prevSeq = sh.getPacket().sequence;
                    //processAck(p.ack_num);
                    //this.addSequenced(_lastAck);
                }
                else
                {
                    end = -1;
                    start = -1;
                }
            }
        }
        else
        {
            //processAck(p.ack_num);
        }
    }

    private static long getUInt(byte[] array, int offset)
    {
        if (array.length < offset + 4)
        {
            throw new IllegalArgumentException("Invalid offset for size");
        }

        long ret = ((array[offset] & 0xffl) << 24);
        ret |= ((array[offset+1] & 0xffl) << 16);
        ret |= ((array[offset+2] & 0xffl) << 8);
        ret |= (array[offset+3] & 0xffl);

        return ret;
    }

    public void processAck(long ack)
    {
        SeqHolder sh = _waitingPrevious.get(ack);
        if (sh != null && !sh.isAcked())
        {
            //long previousSeq = (ack - sh.getPacket().data.length)%MODULO;
            long previousSeq = sh.getPacket().sequence;
            processAck(previousSeq);
            this.addSequenced(ack);
        }
    }

    private void addSequenced(long ack)
    {
        SeqHolder seqHolder = _waitingPrevious.get(ack);
        seqHolder.ack();
        TCPPacket packet = seqHolder.getPacket();

        this.addSequenced(packet);
        /*if (packet.src_port == 7777)
        {
            System.err.println("ACKED: "+ack);
        }*/
    }

    private void addSequenced(TCPPacket packet)
    {
        if (DEBUG_RAW_TRAFFIC)
        {
            try
            {
                if (packet.src_port == 7777)
                {
                    System.err.println("WROTE");
                    _bao.write(packet.data);
                    _bao.flush();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        _sequenced.add(packet);
    }

    public List<TCPPacket> getSequencedPackets()
    {
        return _sequenced;
    }

    public int getPendingSequencePackets()
    {
        return _waitingPrevious.size();
    }

    public void flush()
    {
        _sequenced.clear();
    }

    public boolean hasSequencedPacket()
    {
        return (!_sequenced.isEmpty());
    }

    static class SeqHolder
    {
        private final long _nextSeq;

        private final TCPPacket _packet;
        
        private boolean _acked;

        public SeqHolder(long nextSeq, TCPPacket packet)
        {
            _nextSeq = nextSeq;
            _packet = packet;
        }

        /**
         * @return the nextSeq
         */
        public long getNextSeq()
        {
            return _nextSeq;
        }

        /**
         * @return the packet
         */
        public TCPPacket getPacket()
        {
            return _packet;
        }
        
        public void ack()
        {
            _acked = true;
        }
        
        public boolean isAcked()
        {
            return _acked;
        }

    }
}
