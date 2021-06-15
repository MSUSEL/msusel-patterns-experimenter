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
package org.apache.hadoop.hdfs.server.datanode.metrics;

import java.io.IOException;

/**
 * 
 * This Interface defines the methods to get the status of a the FSDataset of
 * a data node.
 * It is also used for publishing via JMX (hence we follow the JMX naming
 * convention.) 
 *  * Note we have not used the MetricsDynamicMBeanBase to implement this
 * because the interface for the FSDatasetMBean is stable and should
 * be published as an interface.
 * 
 * <p>
 * Data Node runtime statistic  info is report in another MBean
 * @see org.apache.hadoop.hdfs.server.datanode.metrics.DataNodeStatisticsMBean
 *
 */
public interface FSDatasetMBean {
  
  /**
   * Returns the total space (in bytes) used by dfs datanode
   * @return  the total space used by dfs datanode
   * @throws IOException
   */  
  public long getDfsUsed() throws IOException;
    
  /**
   * Returns total capacity (in bytes) of storage (used and unused)
   * @return  total capacity of storage (used and unused)
   * @throws IOException
   */
  public long getCapacity() throws IOException;

  /**
   * Returns the amount of free storage space (in bytes)
   * @return The amount of free storage space
   * @throws IOException
   */
  public long getRemaining() throws IOException;
  
  /**
   * Returns the storage id of the underlying storage
   */
  public String getStorageInfo();

}
