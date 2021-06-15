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
package org.apache.hadoop.hdfs.server.datanode;

/** 
 * a class to throttle the block transfers.
 * This class is thread safe. It can be shared by multiple threads.
 * The parameter bandwidthPerSec specifies the total bandwidth shared by
 * threads.
 */
class BlockTransferThrottler {
  private long period;          // period over which bw is imposed
  private long periodExtension; // Max period over which bw accumulates.
  private long bytesPerPeriod; // total number of bytes can be sent in each period
  private long curPeriodStart; // current period starting time
  private long curReserve;     // remaining bytes can be sent in the period
  private long bytesAlreadyUsed;

  /** Constructor 
   * @param bandwidthPerSec bandwidth allowed in bytes per second. 
   */
  BlockTransferThrottler(long bandwidthPerSec) {
    this(500, bandwidthPerSec);  // by default throttling period is 500ms 
  }

  /**
   * Constructor
   * @param period in milliseconds. Bandwidth is enforced over this
   *        period.
   * @param bandwidthPerSec bandwidth allowed in bytes per second. 
   */
  BlockTransferThrottler(long period, long bandwidthPerSec) {
    this.curPeriodStart = System.currentTimeMillis();
    this.period = period;
    this.curReserve = this.bytesPerPeriod = bandwidthPerSec*period/1000;
    this.periodExtension = period*3;
  }

  /**
   * @return current throttle bandwidth in bytes per second.
   */
  synchronized long getBandwidth() {
    return bytesPerPeriod*1000/period;
  }
  
  /**
   * Sets throttle bandwidth. This takes affect latest by the end of current
   * period.
   * 
   * @param bytesPerSecond 
   */
  synchronized void setBandwidth(long bytesPerSecond) {
    if ( bytesPerSecond <= 0 ) {
      throw new IllegalArgumentException("" + bytesPerSecond);
    }
    bytesPerPeriod = bytesPerSecond*period/1000;
  }
  
  /** Given the numOfBytes sent/received since last time throttle was called,
   * make the current thread sleep if I/O rate is too fast
   * compared to the given bandwidth.
   *
   * @param numOfBytes
   *     number of bytes sent/received since last time throttle was called
   */
  synchronized void throttle(long numOfBytes) {
    if ( numOfBytes <= 0 ) {
      return;
    }

    curReserve -= numOfBytes;
    bytesAlreadyUsed += numOfBytes;

    while (curReserve <= 0) {
      long now = System.currentTimeMillis();
      long curPeriodEnd = curPeriodStart + period;

      if ( now < curPeriodEnd ) {
        // Wait for next period so that curReserve can be increased.
        try {
          wait( curPeriodEnd - now );
        } catch (InterruptedException ignored) {}
      } else if ( now <  (curPeriodStart + periodExtension)) {
        curPeriodStart = curPeriodEnd;
        curReserve += bytesPerPeriod;
      } else {
        // discard the prev period. Throttler might not have
        // been used for a long time.
        curPeriodStart = now;
        curReserve = bytesPerPeriod - bytesAlreadyUsed;
      }
    }

    bytesAlreadyUsed -= numOfBytes;
  }
}
