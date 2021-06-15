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

/**
 * Plugin class to test resource information reported by TT. Use
 * configuration items {@link #MAXVMEM_TESTING_PROPERTY} and
 * {@link #MAXPMEM_TESTING_PROPERTY} to tell TT the total vmem and the total
 * pmem. Use configuration items {@link #NUM_PROCESSORS},
 * {@link #CPU_FREQUENCY}, {@link #CUMULATIVE_CPU_TIME} and {@link #CPU_USAGE}
 * to tell TT the CPU information.
 */
public class DummyResourceCalculatorPlugin extends ResourceCalculatorPlugin {

  /** max vmem on the TT */
  public static final String MAXVMEM_TESTING_PROPERTY =
      "mapred.tasktracker.maxvmem.testing";
  /** max pmem on the TT */
  public static final String MAXPMEM_TESTING_PROPERTY =
      "mapred.tasktracker.maxpmem.testing";
  /** number of processors for testing */
  public static final String NUM_PROCESSORS =
      "mapred.tasktracker.numprocessors.testing";
  /** CPU frequency for testing */
  public static final String CPU_FREQUENCY =
      "mapred.tasktracker.cpufrequency.testing";
  /** cumulative CPU usage time for testing */
  public static final String CUMULATIVE_CPU_TIME =
      "mapred.tasktracker.cumulativecputime.testing";
  /** CPU usage percentage for testing */
  public static final String CPU_USAGE =
      "mapred.tasktracker.cpuusage.testing";
  /** process cumulative CPU usage time for testing */
  public static final String PROC_CUMULATIVE_CPU_TIME =
      "mapred.tasktracker.proccumulativecputime.testing";
  /** process pmem for testing*/
  public static final String PROC_PMEM_TESTING_PROPERTY =
      "mapred.tasktracker.procpmem.testing";
  /** process vmem for testing*/
  public static final String PROC_VMEM_TESTING_PROPERTY =
      "mapred.tasktracker.procvmem.testing";

  /** {@inheritDoc} */
  @Override
  public long getVirtualMemorySize() {
    return getConf().getLong(MAXVMEM_TESTING_PROPERTY, -1);
  }

  /** {@inheritDoc} */
  @Override
  public long getPhysicalMemorySize() {
    return getConf().getLong(MAXPMEM_TESTING_PROPERTY, -1);
  }

  /** {@inheritDoc} */
  @Override
  public long getAvailableVirtualMemorySize() {
    return getConf().getLong(MAXVMEM_TESTING_PROPERTY, -1);
  }

  /** {@inheritDoc} */
  @Override
  public long getAvailablePhysicalMemorySize() {
    return getConf().getLong(MAXPMEM_TESTING_PROPERTY, -1);
  }

  /** {@inheritDoc} */
  @Override
  public int getNumProcessors() {
    return getConf().getInt(NUM_PROCESSORS, -1);
  }

  /** {@inheritDoc} */
  @Override
  public long getCpuFrequency() {
    return getConf().getLong(CPU_FREQUENCY, -1);
  }

  /** {@inheritDoc} */
  @Override
  public long getCumulativeCpuTime() {
    return getConf().getLong(CUMULATIVE_CPU_TIME, -1);
  }

  /** {@inheritDoc} */
  @Override
  public float getCpuUsage() {
    return getConf().getFloat(CPU_USAGE, -1);
  }

  @Override
  public ProcResourceValues getProcResourceValues() {
    long cpuTime = getConf().getLong(PROC_CUMULATIVE_CPU_TIME, -1);
    long pMem = getConf().getLong(PROC_PMEM_TESTING_PROPERTY, -1);
    long vMem = getConf().getLong(PROC_VMEM_TESTING_PROPERTY, -1);
    return new ProcResourceValues(cpuTime, pMem, vMem);
  }
}
