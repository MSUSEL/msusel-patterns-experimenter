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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.tools.rumen.ResourceUsageMetrics;

class LoadSplit extends CombineFileSplit {
  private int id;
  private int nSpec;
  private int maps;
  private int reduces;
  private long inputRecords;
  private long outputBytes;
  private long outputRecords;
  private long maxMemory;
  private double[] reduceBytes = new double[0];
  private double[] reduceRecords = new double[0];

  // Spec for reduces id mod this
  private long[] reduceOutputBytes = new long[0];
  private long[] reduceOutputRecords = new long[0];

  private ResourceUsageMetrics mapMetrics;
  private ResourceUsageMetrics[] reduceMetrics;

  LoadSplit() {
    super();
  }

  public LoadSplit(CombineFileSplit cfsplit, int maps, int id,
      long inputBytes, long inputRecords, long outputBytes,
      long outputRecords, double[] reduceBytes, double[] reduceRecords,
      long[] reduceOutputBytes, long[] reduceOutputRecords,
      ResourceUsageMetrics metrics,
      ResourceUsageMetrics[] rMetrics)
      throws IOException {
    super(cfsplit);
    this.id = id;
    this.maps = maps;
    reduces = reduceBytes.length;
    this.inputRecords = inputRecords;
    this.outputBytes = outputBytes;
    this.outputRecords = outputRecords;
    this.reduceBytes = reduceBytes;
    this.reduceRecords = reduceRecords;
    nSpec = reduceOutputBytes.length;
    this.reduceOutputBytes = reduceOutputBytes;
    this.reduceOutputRecords = reduceOutputRecords;
    this.mapMetrics = metrics;
    this.reduceMetrics = rMetrics;
  }

  public int getId() {
    return id;
  }
  public int getMapCount() {
    return maps;
  }
  public long getInputRecords() {
    return inputRecords;
  }
  public long[] getOutputBytes() {
    if (0 == reduces) {
      return new long[] { outputBytes };
    }
    final long[] ret = new long[reduces];
    for (int i = 0; i < reduces; ++i) {
      ret[i] = Math.round(outputBytes * reduceBytes[i]);
    }
    return ret;
  }
  public long[] getOutputRecords() {
    if (0 == reduces) {
      return new long[] { outputRecords };
    }
    final long[] ret = new long[reduces];
    for (int i = 0; i < reduces; ++i) {
      ret[i] = Math.round(outputRecords * reduceRecords[i]);
    }
    return ret;
  }
  public long getReduceBytes(int i) {
    return reduceOutputBytes[i];
  }
  public long getReduceRecords(int i) {
    return reduceOutputRecords[i];
  }
  
  public ResourceUsageMetrics getMapResourceUsageMetrics() {
    return mapMetrics;
  }
  
  public ResourceUsageMetrics getReduceResourceUsageMetrics(int i) {
    return reduceMetrics[i];
  }
  
  @Override
  public void write(DataOutput out) throws IOException {
    super.write(out);
    WritableUtils.writeVInt(out, id);
    WritableUtils.writeVInt(out, maps);
    WritableUtils.writeVLong(out, inputRecords);
    WritableUtils.writeVLong(out, outputBytes);
    WritableUtils.writeVLong(out, outputRecords);
    WritableUtils.writeVLong(out, maxMemory);
    WritableUtils.writeVInt(out, reduces);
    for (int i = 0; i < reduces; ++i) {
      out.writeDouble(reduceBytes[i]);
      out.writeDouble(reduceRecords[i]);
    }
    WritableUtils.writeVInt(out, nSpec);
    for (int i = 0; i < nSpec; ++i) {
      WritableUtils.writeVLong(out, reduceOutputBytes[i]);
      WritableUtils.writeVLong(out, reduceOutputRecords[i]);
    }
    mapMetrics.write(out);
    int numReduceMetrics = (reduceMetrics == null) ? 0 : reduceMetrics.length;
    WritableUtils.writeVInt(out, numReduceMetrics);
    for (int i = 0; i < numReduceMetrics; ++i) {
      reduceMetrics[i].write(out);
    }
  }
  @Override
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
    id = WritableUtils.readVInt(in);
    maps = WritableUtils.readVInt(in);
    inputRecords = WritableUtils.readVLong(in);
    outputBytes = WritableUtils.readVLong(in);
    outputRecords = WritableUtils.readVLong(in);
    maxMemory = WritableUtils.readVLong(in);
    reduces = WritableUtils.readVInt(in);
    if (reduceBytes.length < reduces) {
      reduceBytes = new double[reduces];
      reduceRecords = new double[reduces];
    }
    for (int i = 0; i < reduces; ++i) {
      reduceBytes[i] = in.readDouble();
      reduceRecords[i] = in.readDouble();
    }
    nSpec = WritableUtils.readVInt(in);
    if (reduceOutputBytes.length < nSpec) {
      reduceOutputBytes = new long[nSpec];
      reduceOutputRecords = new long[nSpec];
    }
    for (int i = 0; i < nSpec; ++i) {
      reduceOutputBytes[i] = WritableUtils.readVLong(in);
      reduceOutputRecords[i] = WritableUtils.readVLong(in);
    }
    mapMetrics = new ResourceUsageMetrics();
    mapMetrics.readFields(in);
    int numReduceMetrics = WritableUtils.readVInt(in);
    reduceMetrics = new ResourceUsageMetrics[numReduceMetrics];
    for (int i = 0; i < numReduceMetrics; ++i) {
      reduceMetrics[i] = new ResourceUsageMetrics();
      reduceMetrics[i].readFields(in);
    }
  }
}
