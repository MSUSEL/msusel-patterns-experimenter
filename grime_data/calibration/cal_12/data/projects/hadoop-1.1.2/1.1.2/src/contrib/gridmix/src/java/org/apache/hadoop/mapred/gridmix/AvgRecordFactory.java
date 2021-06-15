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
package org.apache.hadoop.mapred.gridmix;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

/**
 * Given byte and record targets, emit roughly equal-sized records satisfying
 * the contract.
 */
class AvgRecordFactory extends RecordFactory {

  /**
   * Percentage of record for key data.
   */
  public static final String GRIDMIX_KEY_FRC = "gridmix.key.fraction";


  private final long targetBytes;
  private final long targetRecords;
  private final long step;
  private final int avgrec;
  private final int keyLen;
  private long accBytes = 0L;
  private long accRecords = 0L;
  private int unspilledBytes = 0;
  private int minSpilledBytes = 0;

  /**
   * @param targetBytes Expected byte count.
   * @param targetRecords Expected record count.
   * @param conf Used to resolve edge cases @see #GRIDMIX_KEY_FRC
   */
  public AvgRecordFactory(long targetBytes, long targetRecords,
      Configuration conf) {
    this(targetBytes, targetRecords, conf, 0);
  }
  
  /**
   * @param minSpilledBytes Minimum amount of data expected per record
   */
  public AvgRecordFactory(long targetBytes, long targetRecords,
      Configuration conf, int minSpilledBytes) {
    this.targetBytes = targetBytes;
    this.targetRecords = targetRecords <= 0 && this.targetBytes >= 0
      ? Math.max(1,
          this.targetBytes / conf.getInt("gridmix.missing.rec.size", 64 * 1024))
      : targetRecords;
    final long tmp = this.targetBytes / this.targetRecords;
    step = this.targetBytes - this.targetRecords * tmp;
    avgrec = (int) Math.min(Integer.MAX_VALUE, tmp + 1);
    keyLen = Math.max(1,
        (int)(tmp * Math.min(1.0f, conf.getFloat(GRIDMIX_KEY_FRC, 0.1f))));
    this.minSpilledBytes = minSpilledBytes;
  }

  @Override
  public boolean next(GridmixKey key, GridmixRecord val) throws IOException {
    if (accBytes >= targetBytes) {
      return false;
    }
    final int reclen = accRecords++ >= step ? avgrec - 1 : avgrec;
    final int len = (int) Math.min(targetBytes - accBytes, reclen);
    
    unspilledBytes += len;
    
    // len != reclen?
    if (key != null) {
      if (unspilledBytes < minSpilledBytes && accRecords < targetRecords) {
        key.setSize(1);
        val.setSize(1);
        accBytes += key.getSize() + val.getSize();
        unspilledBytes -= (key.getSize() + val.getSize());
      } else {
        key.setSize(keyLen);
        val.setSize(unspilledBytes - key.getSize());
        accBytes += unspilledBytes;
        unspilledBytes = 0;
      }
    } else {
      if (unspilledBytes < minSpilledBytes && accRecords < targetRecords) {
        val.setSize(1);
        accBytes += val.getSize();
        unspilledBytes -= val.getSize();
      } else {
        val.setSize(unspilledBytes);
        accBytes += unspilledBytes;
        unspilledBytes = 0;
      }
    }
    return true;
  }

  @Override
  public float getProgress() throws IOException {
    return Math.min(1.0f, accBytes / ((float)targetBytes));
  }

  @Override
  public void close() throws IOException {
    // noop
  }

}
