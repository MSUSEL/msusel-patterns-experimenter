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
package org.apache.hadoop.test.system;

import java.util.Map;

import org.apache.hadoop.io.Writable;

/**
 * Daemon system level process information.
 */
public interface ProcessInfo extends Writable {
  /**
   * Get the current time in the millisecond.<br/>
   * 
   * @return current time on daemon clock in millisecond.
   */
  public long currentTimeMillis();

  /**
   * Get the environment that was used to start the Daemon process.<br/>
   * 
   * @return the environment variable list.
   */
  public Map<String,String> getEnv();

  /**
   * Get the System properties of the Daemon process.<br/>
   * 
   * @return the properties list.
   */
  public Map<String,String> getSystemProperties();

  /**
   * Get the number of active threads in Daemon VM.<br/>
   * 
   * @return number of active threads in Daemon VM.
   */
  public int activeThreadCount();

  /**
   * Get the maximum heap size that is configured for the Daemon VM. <br/>
   * 
   * @return maximum heap size.
   */
  public long maxMemory();

  /**
   * Get the free memory in Daemon VM.<br/>
   * 
   * @return free memory.
   */
  public long freeMemory();

  /**
   * Get the total used memory in Demon VM. <br/>
   * 
   * @return total used memory.
   */
  public long totalMemory();
}