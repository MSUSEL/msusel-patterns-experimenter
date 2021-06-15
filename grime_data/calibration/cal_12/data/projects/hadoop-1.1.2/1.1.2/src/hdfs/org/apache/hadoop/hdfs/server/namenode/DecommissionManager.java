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
package org.apache.hadoop.hdfs.server.namenode;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.CyclicIteration;

/**
 * Manage node decommissioning.
 */
class DecommissionManager {
  static final Log LOG = LogFactory.getLog(DecommissionManager.class);

  private final FSNamesystem fsnamesystem;

  DecommissionManager(FSNamesystem namesystem) {
    this.fsnamesystem = namesystem;
  }

  /** Periodically check decommission status. */
  class Monitor implements Runnable {
    /** recheckInterval is how often namenode checks
     *  if a node has finished decommission
     */
    private final long recheckInterval;
    /** The number of decommission nodes to check for each interval */
    private final int numNodesPerCheck;
    /** firstkey can be initialized to anything. */
    private String firstkey = "";

    Monitor(int recheckIntervalInSecond, int numNodesPerCheck) {
      this.recheckInterval = recheckIntervalInSecond * 1000L;
      this.numNodesPerCheck = numNodesPerCheck;
    }

    /**
     * Check decommission status of numNodesPerCheck nodes
     * for every recheckInterval milliseconds.
     */
    public void run() {
      for(; fsnamesystem.isRunning(); ) {
        synchronized(fsnamesystem) {
          check();
        }
  
        try {
          Thread.sleep(recheckInterval);
        } catch (InterruptedException ie) {
          LOG.info("Interrupted " + this.getClass().getSimpleName(), ie);
        }
      }
    }
    
    private void check() {
      int count = 0;
      for(Map.Entry<String, DatanodeDescriptor> entry
          : new CyclicIteration<String, DatanodeDescriptor>(
              fsnamesystem.datanodeMap, firstkey)) {
        final DatanodeDescriptor d = entry.getValue();
        firstkey = entry.getKey();

        if (d.isDecommissionInProgress()) {
          try {
            fsnamesystem.checkDecommissionStateInternal(d);
          } catch(Exception e) {
            LOG.warn("entry=" + entry, e);
          }
          if (++count == numNodesPerCheck) {
            return;
          }
        }
      }
    }
  }
}