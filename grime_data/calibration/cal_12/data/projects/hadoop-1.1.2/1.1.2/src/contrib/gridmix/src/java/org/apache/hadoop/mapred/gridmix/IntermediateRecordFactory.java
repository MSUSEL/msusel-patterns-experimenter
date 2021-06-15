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
 * Factory passing reduce specification as its last record.
 */
class IntermediateRecordFactory extends RecordFactory {

  private final GridmixKey.Spec spec;
  private final RecordFactory factory;
  private final int partition;
  private final long targetRecords;
  private boolean done = false;
  private long accRecords = 0L;

  /**
   * @param targetBytes Expected byte count.
   * @param targetRecords Expected record count; will emit spec records after
   *                      this boundary is passed.
   * @param partition Reduce to which records are emitted.
   * @param spec Specification to emit.
   * @param conf Unused.
   */
  public IntermediateRecordFactory(long targetBytes, long targetRecords,
      int partition, GridmixKey.Spec spec, Configuration conf) {
    this(new AvgRecordFactory(targetBytes, targetRecords, conf), partition,
        targetRecords, spec, conf);
  }

  /**
   * @param factory Factory from which byte/record counts are obtained.
   * @param partition Reduce to which records are emitted.
   * @param targetRecords Expected record count; will emit spec records after
   *                      this boundary is passed.
   * @param spec Specification to emit.
   * @param conf Unused.
   */
  public IntermediateRecordFactory(RecordFactory factory, int partition,
      long targetRecords, GridmixKey.Spec spec, Configuration conf) {
    this.spec = spec;
    this.factory = factory;
    this.partition = partition;
    this.targetRecords = targetRecords;
  }

  @Override
  public boolean next(GridmixKey key, GridmixRecord val) throws IOException {
    assert key != null;
    final boolean rslt = factory.next(key, val);
    ++accRecords;
    if (rslt) {
      if (accRecords < targetRecords) {
        key.setType(GridmixKey.DATA);
      } else {
        final int orig = key.getSize();
        key.setType(GridmixKey.REDUCE_SPEC);
        spec.rec_in = accRecords;
        key.setSpec(spec);
        val.setSize(val.getSize() - (key.getSize() - orig));
        // reset counters
        accRecords = 0L;
        spec.bytes_out = 0L;
        spec.rec_out = 0L;
        done = true;
      }
    } else if (!done) {
      // ensure spec emitted
      key.setType(GridmixKey.REDUCE_SPEC);
      key.setPartition(partition);
      key.setSize(0);
      val.setSize(0);
      spec.rec_in = 0L;
      key.setSpec(spec);
      done = true;
      return true;
    }
    key.setPartition(partition);
    return rslt;
  }

  @Override
  public float getProgress() throws IOException {
    return factory.getProgress();
  }

  @Override
  public void close() throws IOException {
    factory.close();
  }
}
