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
package org.apache.hadoop.eclipse;

import org.apache.hadoop.eclipse.servers.ServerRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  /**
   * The plug-in ID
   */
  public static final String PLUGIN_ID = "org.apache.hadoop.eclipse";

  /**
   * The shared unique instance (singleton)
   */
  private static Activator plugin;

  /**
   * Constructor
   */
  public Activator() {
    synchronized (Activator.class) {
      if (plugin != null) {
        // Not a singleton!?
        throw new RuntimeException("Activator for " + PLUGIN_ID
            + " is not a singleton");
      }
      plugin = this;
    }
  }

  /* @inheritDoc */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
  }

  /* @inheritDoc */
  @Override
  public void stop(BundleContext context) throws Exception {
    ServerRegistry.getInstance().dispose();
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared unique instance (singleton)
   * 
   * @return the shared unique instance (singleton)
   */
  public static Activator getDefault() {
    return plugin;
  }

}
