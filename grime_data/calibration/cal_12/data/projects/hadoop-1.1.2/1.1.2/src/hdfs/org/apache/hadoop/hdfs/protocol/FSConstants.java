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
package org.apache.hadoop.hdfs.protocol;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;

/************************************
 * Some handy constants
 *
 ************************************/
public interface FSConstants {
  public static int MIN_BLOCKS_FOR_WRITE = 5;

  // Long that indicates "leave current quota unchanged"
  public static final long QUOTA_DONT_SET = Long.MAX_VALUE;
  public static final long QUOTA_RESET = -1L;
  
  //
  // Timeouts, constants
  //
  public static long HEARTBEAT_INTERVAL = 3;
  public static long BLOCKREPORT_INTERVAL = 60 * 60 * 1000;
  public static long BLOCKREPORT_INITIAL_DELAY = 0;
  public static final long LEASE_SOFTLIMIT_PERIOD = 60 * 1000;
  public static final long LEASE_HARDLIMIT_PERIOD = 60 * LEASE_SOFTLIMIT_PERIOD;
  public static final long LEASE_RECOVER_PERIOD = 10 * 1000; //in ms
  
  // We need to limit the length and depth of a path in the filesystem.  HADOOP-438
  // Currently we set the maximum length to 8k characters and the maximum depth to 1k.  
  public static int MAX_PATH_LENGTH = 8000;
  public static int MAX_PATH_DEPTH = 1000;
    
  public static final int BUFFER_SIZE = new Configuration().getInt("io.file.buffer.size", 4096);
  //Used for writing header etc.
  public static final int SMALL_BUFFER_SIZE = Math.min(BUFFER_SIZE/2, 512);
  //TODO mb@media-style.com: should be conf injected?
  public static final long DEFAULT_BLOCK_SIZE = DFSConfigKeys.DFS_BLOCK_SIZE_DEFAULT;
  public static final int DEFAULT_DATA_SOCKET_SIZE = 128 * 1024;

  public static final int SIZE_OF_INTEGER = Integer.SIZE / Byte.SIZE;

  // SafeMode actions
  public enum SafeModeAction{ SAFEMODE_LEAVE, SAFEMODE_ENTER, SAFEMODE_GET; }

  // type of the datanode report
  public static enum DatanodeReportType {ALL, LIVE, DEAD }

  /**
   * Distributed upgrade actions:
   * 
   * 1. Get upgrade status.
   * 2. Get detailed upgrade status.
   * 3. Proceed with the upgrade if it is stuck, no matter what the status is.
   */
  public static enum UpgradeAction {
    GET_STATUS,
    DETAILED_STATUS,
    FORCE_PROCEED;
  }

  // Version is reflected in the dfs image and edit log files.
  // Version is reflected in the data storage file.
  // Versions are negative.
  // Decrement LAYOUT_VERSION to define a new version.
  public static final int LAYOUT_VERSION = -32;
  // Current version: 
  // -32: to handle editlog opcode conflicts with 0.20.203 during upgrades and
  // to disallow upgrade to release 0.21.
}
