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

/*******************************
 * Some handy constants
 * 
 *******************************/
interface MRConstants {
  //
  // Timeouts, constants
  //
  public static final int HEARTBEAT_INTERVAL_MIN = 300;
  
  public static final long COUNTER_UPDATE_INTERVAL = 60 * 1000;

  /**
   * How often TaskTracker needs to check the health of its disks, if not
   * configured using mapred.disk.healthChecker.interval
   */
  public static final long DEFAULT_DISK_HEALTH_CHECK_INTERVAL = 60 * 1000;

  //
  // Result codes
  //
  public static int SUCCESS = 0;
  public static int FILE_NOT_FOUND = -1;
  
  /**
   * The custom http header used for the map output length.
   */
  public static final String MAP_OUTPUT_LENGTH = "Map-Output-Length";

  /**
   * The custom http header used for the "raw" map output length.
   */
  public static final String RAW_MAP_OUTPUT_LENGTH = "Raw-Map-Output-Length";

  /**
   * The map task from which the map output data is being transferred
   */
  public static final String FROM_MAP_TASK = "from-map-task";
  
  /**
   * The reduce task number for which this map output is being transferred
   */
  public static final String FOR_REDUCE_TASK = "for-reduce-task";
  
  public static final String WORKDIR = "work";
}
