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
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.gridmix.Progressive;
import org.apache.hadoop.util.ResourceCalculatorPlugin;
import org.apache.hadoop.tools.rumen.ResourceUsageMetrics;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * <p>This is the driver class for managing all the resource usage emulators.
 * {@link ResourceUsageMatcher} expects a comma separated list of 
 * {@link ResourceUsageEmulatorPlugin} implementations specified using 
 * {@link #RESOURCE_USAGE_EMULATION_PLUGINS} as the configuration parameter.</p>
 * 
 * <p>Note that the order in which the emulators are invoked is same as the 
 * order in which they are configured.
 */
public class ResourceUsageMatcher implements Progressive {
  /**
   * Configuration key to set resource usage emulators.
   */
  public static final String RESOURCE_USAGE_EMULATION_PLUGINS =
    "gridmix.emulators.resource-usage.plugins";
  
  private List<ResourceUsageEmulatorPlugin> emulationPlugins = 
    new ArrayList<ResourceUsageEmulatorPlugin>();
  
  /**
   * Configure the {@link ResourceUsageMatcher} to load the configured plugins
   * and initialize them.
   */
  @SuppressWarnings("unchecked")
  public void configure(Configuration conf, ResourceCalculatorPlugin monitor, 
                        ResourceUsageMetrics metrics, Progressive progress) {
    Class[] plugins = conf.getClasses(RESOURCE_USAGE_EMULATION_PLUGINS);
//, null, ResourceUsageEmulatorPlugin.class);
    if (plugins == null) {
      System.out.println("No resource usage emulator plugins configured.");
    } else {
      for (Class<? extends ResourceUsageEmulatorPlugin> plugin : plugins) {
        if (plugin != null) {
          emulationPlugins.add(ReflectionUtils.newInstance(plugin, conf));
        }
      }
    }

    // initialize the emulators once all the configured emulator plugins are
    // loaded
    for (ResourceUsageEmulatorPlugin emulator : emulationPlugins) {
      emulator.initialize(conf, metrics, monitor, progress);
    }
  }
  
  public void matchResourceUsage() throws IOException, InterruptedException {
    for (ResourceUsageEmulatorPlugin emulator : emulationPlugins) {
      // match the resource usage
      emulator.emulate();
    }
  }
  
  /**
   * Returns the average progress.
   */
  @Override
  public float getProgress() {
    if (emulationPlugins.size() > 0) {
      // return the average progress
      float progress = 0f;
      for (ResourceUsageEmulatorPlugin emulator : emulationPlugins) {
        // consider weighted progress of each emulator
        progress += emulator.getProgress();
      }

      return progress / emulationPlugins.size();
    }
    
    // if no emulators are configured then return 1
    return 1f;
    
  }
}
