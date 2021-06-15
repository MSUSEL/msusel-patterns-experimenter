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
package org.apache.hadoop.tools.rumen;

public class TaskInfo {
  private final long bytesIn;
  private final int recsIn;
  private final long bytesOut;
  private final int recsOut;
  private final long maxMemory;
  private final ResourceUsageMetrics metrics;

  public TaskInfo(long bytesIn, int recsIn, long bytesOut, int recsOut,
      long maxMemory) {
    this(bytesIn, recsIn, bytesOut, recsOut, maxMemory, 
         new ResourceUsageMetrics());
  }
  
  public TaskInfo(long bytesIn, int recsIn, long bytesOut, int recsOut,
                  long maxMemory, ResourceUsageMetrics metrics) {
    this.bytesIn = bytesIn;
    this.recsIn = recsIn;
    this.bytesOut = bytesOut;
    this.recsOut = recsOut;
    this.maxMemory = maxMemory;
    this.metrics = metrics;
  }

  /**
   * @return Raw bytes read from the FileSystem into the task. Note that this
   *         may not always match the input bytes to the task.
   */
  public long getInputBytes() {
    return bytesIn;
  }

  /**
   * @return Number of records input to this task.
   */
  public int getInputRecords() {
    return recsIn;
  }

  /**
   * @return Raw bytes written to the destination FileSystem. Note that this may
   *         not match output bytes.
   */
  public long getOutputBytes() {
    return bytesOut;
  }

  /**
   * @return Number of records output from this task.
   */
  public int getOutputRecords() {
    return recsOut;
  }

  /**
   * @return Memory used by the task leq the heap size.
   */
  public long getTaskMemory() {
    return maxMemory;
  }

  /**
   * @return Resource usage metrics
   */
  public ResourceUsageMetrics getResourceUsageMetrics() {
    return metrics;
  }
}
