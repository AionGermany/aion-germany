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

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javolution.util.FastList;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DownloadQueue
{
    private ThreadPoolExecutor _threadPool;
    
    public DownloadQueue(int size)
    {
        _threadPool = new ThreadPoolExecutor(size,size,15L,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
    }
    
    public void addNewDownLoadTask(LogFile log)
    {
        _threadPool.execute(new DownloadTask(log));
    }
    
    public List<LogFile> getWaitingLogs()
    {
        List<LogFile> logs = new FastList<LogFile>();
        for (Runnable r :_threadPool.getQueue())
        {
            logs.add(((DownloadTask)r).getLog());
        }
        return logs;
    }
    
    private class DownloadTask implements Runnable
    {
        private LogFile _log;

        public DownloadTask(LogFile log)
        {
            _log = log;
        }
        
        public LogFile getLog()
        {
            return _log;
        }

        public void run()
        {
            RemoteLogRepositoryBackend.getInstance().downLoadFile(_log);
        }
        
    }
}