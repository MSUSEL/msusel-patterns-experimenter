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
package org.apache.hadoop.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

/**
 * Plugin to calculate virtual and physical memories on the system.
 * 
 * @deprecated Use
 *             {@link org.apache.hadoop.util.ResourceCalculatorPlugin}
 *             instead
 */
@Deprecated
public abstract class MemoryCalculatorPlugin extends Configured {

  /**
   * Obtain the total size of the virtual memory present in the system.
   * 
   * @return virtual memory size in bytes.
   */
  public abstract long getVirtualMemorySize();

  /**
   * Obtain the total size of the physical memory present in the system.
   * 
   * @return physical memory size bytes.
   */
  public abstract long getPhysicalMemorySize();

  /**
   * Get the MemoryCalculatorPlugin from the class name and configure it. If
   * class name is null, this method will try and return a memory calculator
   * plugin available for this system.
   * 
   * @param clazz class-name
   * @param conf configure the plugin with this.
   * @return MemoryCalculatorPlugin
   */
  public static MemoryCalculatorPlugin getMemoryCalculatorPlugin(
      Class<? extends MemoryCalculatorPlugin> clazz, Configuration conf) {

    if (clazz != null) {
      return ReflectionUtils.newInstance(clazz, conf);
    }

    // No class given, try a os specific class
    try {
      String osName = System.getProperty("os.name");
      if (osName.startsWith("Linux")) {
        return new LinuxMemoryCalculatorPlugin();
      }
    } catch (SecurityException se) {
      // Failed to get Operating System name.
      return null;
    }

    // Not supported on this system.
    return null;
  }
}