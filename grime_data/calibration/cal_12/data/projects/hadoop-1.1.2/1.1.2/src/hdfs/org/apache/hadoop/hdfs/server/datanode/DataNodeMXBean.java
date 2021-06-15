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
 * 
 * This is the JMX management interface for data node information
 */
public interface DataNodeMXBean {
  
  /**
   * @return the host name
   */
  public String getHostName();
  
  /**
   * Gets the version of Hadoop.
   * 
   * @return the version of Hadoop
   */
  public String getVersion();
  
  /**
   * Gets the rpc port.
   * 
   * @return the rpc port
   */
  public String getRpcPort();
  
  /**
   * Gets the http port.
   * 
   * @return the http port
   */
  public String getHttpPort();
  
  /**
   * Gets the namenode IP address.
   * 
   * @return the namenode IP address
   */
  public String getNamenodeAddress();
  
  /**
   * Gets the information of each volume on the Datanode. Please
   * see the implementation for the format of returned information.
   * 
   * @return the volume info
   */
  public String getVolumeInfo();
}
