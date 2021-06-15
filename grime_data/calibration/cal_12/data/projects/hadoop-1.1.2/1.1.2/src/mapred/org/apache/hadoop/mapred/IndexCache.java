/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.hadoop.mapred;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;

class IndexCache {

  private final JobConf conf;
  private final int totalMemoryAllowed;
  private AtomicInteger totalMemoryUsed = new AtomicInteger();
  private static final Log LOG = LogFactory.getLog(IndexCache.class);

  private final ConcurrentHashMap<String,IndexInformation> cache =
    new ConcurrentHashMap<String,IndexInformation>();
  
  private final LinkedBlockingQueue<String> queue = 
    new LinkedBlockingQueue<String>();

  public IndexCache(JobConf conf) {
    this.conf = conf;
    totalMemoryAllowed =
      conf.getInt("mapred.tasktracker.indexcache.mb", 10) * 1024 * 1024;
    LOG.info("IndexCache created with max memory = " + totalMemoryAllowed);
  }

  /**
   * This method gets the index information for the given mapId and reduce.
   * It reads the index file into cache if it is not already present.
   * @param mapId
   * @param reduce
   * @param fileName The file to read the index information from if it is not
   *                 already present in the cache
   * @param expectedIndexOwner The expected owner of the index file
   * @return The Index Information
   * @throws IOException
   */
  public IndexRecord getIndexInformation(String mapId, int reduce,
      Path fileName, String expectedIndexOwner) throws IOException {

    IndexInformation info = cache.get(mapId);

    if (info == null) {
      info = readIndexFileToCache(fileName, mapId, expectedIndexOwner);
    } else {
      synchronized (info) {
        while (null == info.mapSpillRecord) {
          try {
            info.wait();
          } catch (InterruptedException e) {
            throw new IOException("Interrupted waiting for construction", e);
          }
        }
      }
      LOG.debug("IndexCache HIT: MapId " + mapId + " found");
    }

    if (info.mapSpillRecord.size() == 0 ||
        info.mapSpillRecord.size() < reduce) {
      throw new IOException("Invalid request " +
        " Map Id = " + mapId + " Reducer = " + reduce +
        " Index Info Length = " + info.mapSpillRecord.size());
    }
    return info.mapSpillRecord.getIndex(reduce);
  }

  private IndexInformation readIndexFileToCache(Path indexFileName,
      String mapId, String expectedIndexOwner) throws IOException {
    IndexInformation info;
    IndexInformation newInd = new IndexInformation();
    if ((info = cache.putIfAbsent(mapId, newInd)) != null) {
      synchronized (info) {
        while (null == info.mapSpillRecord) {
          try {
            info.wait();
          } catch (InterruptedException e) {
            throw new IOException("Interrupted waiting for construction", e);
          }
        }
      }
      LOG.debug("IndexCache HIT: MapId " + mapId + " found");
      return info;
    }
    LOG.debug("IndexCache MISS: MapId " + mapId + " not found") ;
    SpillRecord tmp = null;
    try { 
      tmp = new SpillRecord(indexFileName, conf, expectedIndexOwner);
    } catch (Throwable e) { 
      tmp = new SpillRecord(0);
      cache.remove(mapId);
      throw new IOException("Error Reading IndexFile", e);
    } finally { 
      synchronized (newInd) { 
        newInd.mapSpillRecord = tmp;
        newInd.notifyAll();
      } 
    } 
    queue.add(mapId);
    
    if (totalMemoryUsed.addAndGet(newInd.getSize()) > totalMemoryAllowed) {
      freeIndexInformation();
    }
    return newInd;
  }

  /**
   * This method removes the map from the cache. It should be called when
   * a map output on this tracker is discarded.
   * @param mapId The taskID of this map.
   */
  public void removeMap(String mapId) {
    IndexInformation info = cache.remove(mapId);
    if (info != null) {
      totalMemoryUsed.addAndGet(-info.getSize());
      if (!queue.remove(mapId)) {
        LOG.warn("Map ID" + mapId + " not found in queue!!");
      }
    } else {
      LOG.info("Map ID " + mapId + " not found in cache");
    }
  }

  /**
   * Bring memory usage below totalMemoryAllowed.
   */
  private synchronized void freeIndexInformation() {
    while (totalMemoryUsed.get() > totalMemoryAllowed) {
      String s = queue.remove();
      IndexInformation info = cache.remove(s);
      if (info != null) {
        totalMemoryUsed.addAndGet(-info.getSize());
      }
    }
  }

  private static class IndexInformation {
    SpillRecord mapSpillRecord;

    int getSize() {
      return mapSpillRecord == null
        ? 0
        : mapSpillRecord.size() * MapTask.MAP_OUTPUT_INDEX_RECORD_LENGTH;
    }
  }
}
