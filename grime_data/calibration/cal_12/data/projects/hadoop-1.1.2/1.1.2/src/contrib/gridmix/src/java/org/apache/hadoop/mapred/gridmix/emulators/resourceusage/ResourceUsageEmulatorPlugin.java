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
package org.apache.hadoop.mapred.gridmix.emulators.resourceusage;

import java.io.IOException;

import org.apache.hadoop.mapred.gridmix.Progressive;
import org.apache.hadoop.util.ResourceCalculatorPlugin;
import org.apache.hadoop.tools.rumen.ResourceUsageMetrics;
import org.apache.hadoop.conf.Configuration;

/**
 * <p>Each resource to be emulated should have a corresponding implementation 
 * class that implements {@link ResourceUsageEmulatorPlugin}.</p>
 * <br><br>
 * {@link ResourceUsageEmulatorPlugin} will be configured using the 
 * {@link #initialize(Configuration, ResourceUsageMetrics, 
 *                    ResourceCalculatorPlugin, Progressive)} call.
 * Every 
 * {@link ResourceUsageEmulatorPlugin} is also configured with a feedback module
 * i.e a {@link ResourceCalculatorPlugin}, to monitor the current resource 
 * usage. {@link ResourceUsageMetrics} decides the final resource usage value to
 * emulate. {@link Progressive} keeps track of the task's progress.</p>
 * 
 * <br><br>
 * 
 * For configuring GridMix to load and and use a resource usage emulator, 
 * see {@link ResourceUsageMatcher}. 
 */
public interface ResourceUsageEmulatorPlugin extends Progressive {
  /**
   * Initialize the plugin. This might involve
   *   - initializing the variables
   *   - calibrating the plugin
   */
  void initialize(Configuration conf, ResourceUsageMetrics metrics, 
                  ResourceCalculatorPlugin monitor,
                  Progressive progress);

  /**
   * Emulate the resource usage to match the usage target. The plugin can use
   * the given {@link ResourceCalculatorPlugin} to query for the current 
   * resource usage.
   * @throws IOException
   * @throws InterruptedException
   */
  void emulate() throws IOException, InterruptedException;
}
