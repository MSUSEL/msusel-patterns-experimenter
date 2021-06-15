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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * Captures the resource usage metrics.
 */
public class ResourceUsageMetrics implements Writable, DeepCompare  {
  private long cumulativeCpuUsage;
  private long virtualMemoryUsage;
  private long physicalMemoryUsage;
  private long heapUsage;
  
  public ResourceUsageMetrics() {
  }
  
  /**
   * Get the cumulative CPU usage.
   */
  public long getCumulativeCpuUsage() {
    return cumulativeCpuUsage;
  }
  
  /**
   * Set the cumulative CPU usage.
   */
  public void setCumulativeCpuUsage(long usage) {
    cumulativeCpuUsage = usage;
  }
  
  /**
   * Get the virtual memory usage.
   */
  public long getVirtualMemoryUsage() {
    return virtualMemoryUsage;
  }
  
  /**
   * Set the virtual memory usage.
   */
  public void setVirtualMemoryUsage(long usage) {
    virtualMemoryUsage = usage;
  }
  
  /**
   * Get the physical memory usage.
   */
  public long getPhysicalMemoryUsage() {
    return physicalMemoryUsage;
  }
  
  /**
   * Set the physical memory usage.
   */
  public void setPhysicalMemoryUsage(long usage) {
    physicalMemoryUsage = usage;
  }
  
  /**
   * Get the total heap usage.
   */
  public long getHeapUsage() {
    return heapUsage;
  }
  
  /**
   * Set the total heap usage.
   */
  public void setHeapUsage(long usage) {
    heapUsage = usage;
  }
  
  /**
   * Returns the size of the serialized data
   */
  public int size() {
    int size = 0;
    size += WritableUtils.getVIntSize(cumulativeCpuUsage);   // long #1
    size += WritableUtils.getVIntSize(virtualMemoryUsage);   // long #2
    size += WritableUtils.getVIntSize(physicalMemoryUsage);  // long #3
    size += WritableUtils.getVIntSize(heapUsage);            // long #4
    return size;
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    cumulativeCpuUsage = WritableUtils.readVLong(in);  // long #1
    virtualMemoryUsage = WritableUtils.readVLong(in);  // long #2
    physicalMemoryUsage = WritableUtils.readVLong(in); // long #3
    heapUsage = WritableUtils.readVLong(in);           // long #4
  }
  
  @Override
  public void write(DataOutput out) throws IOException {
    //TODO Write resources version no too
    WritableUtils.writeVLong(out, cumulativeCpuUsage);  // long #1
    WritableUtils.writeVLong(out, virtualMemoryUsage);  // long #2
    WritableUtils.writeVLong(out, physicalMemoryUsage); // long #3
    WritableUtils.writeVLong(out, heapUsage);           // long #4
  }

  private static void compareMetric(long m1, long m2, TreePath loc) 
  throws DeepInequalityException {
    if (m1 != m2) {
      throw new DeepInequalityException("Value miscompared:" + loc.toString(), 
                                        loc);
    }
  }
  
  private static void compareSize(ResourceUsageMetrics m1, 
                                  ResourceUsageMetrics m2, TreePath loc) 
  throws DeepInequalityException {
    if (m1.size() != m2.size()) {
      throw new DeepInequalityException("Size miscompared: " + loc.toString(), 
                                        loc);
    }
  }
  
  @Override
  public void deepCompare(DeepCompare other, TreePath loc)
      throws DeepInequalityException {
    if (!(other instanceof ResourceUsageMetrics)) {
      throw new DeepInequalityException("Comparand has wrong type", loc);
    }

    ResourceUsageMetrics metrics2 = (ResourceUsageMetrics) other;
    compareMetric(getCumulativeCpuUsage(), metrics2.getCumulativeCpuUsage(), 
                  new TreePath(loc, "cumulativeCpu"));
    compareMetric(getVirtualMemoryUsage(), metrics2.getVirtualMemoryUsage(), 
                  new TreePath(loc, "virtualMemory"));
    compareMetric(getPhysicalMemoryUsage(), metrics2.getPhysicalMemoryUsage(), 
                  new TreePath(loc, "physicalMemory"));
    compareMetric(getHeapUsage(), metrics2.getHeapUsage(), 
                  new TreePath(loc, "heapUsage"));
    compareSize(this, metrics2, new TreePath(loc, "size"));
  }
}

