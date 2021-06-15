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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

/**
 * A helper to load the native hadoop code i.e. libhadoop.so.
 * This handles the fallback to either the bundled libhadoop-Linux-i386-32.so
 * or the default java implementations where appropriate.
 *  
 */
public class NativeCodeLoader {

  private static final Log LOG =
    LogFactory.getLog(NativeCodeLoader.class);
  
  private static boolean nativeCodeLoaded = false;
  
  static {
    // Try to load native hadoop library and set fallback flag appropriately
    LOG.debug("Trying to load the custom-built native-hadoop library...");
    try {
      System.loadLibrary("hadoop");
      LOG.info("Loaded the native-hadoop library");
      nativeCodeLoaded = true;
    } catch (Throwable t) {
      // Ignore failure to load
      LOG.debug("Failed to load native-hadoop with error: " + t);
      LOG.debug("java.library.path=" + System.getProperty("java.library.path"));
    }
    
    if (!nativeCodeLoaded) {
      LOG.warn("Unable to load native-hadoop library for your platform... " +
               "using builtin-java classes where applicable");
    }
  }

  /**
   * Check if native-hadoop code is loaded for this platform.
   * 
   * @return <code>true</code> if native-hadoop is loaded, 
   *         else <code>false</code>
   */
  public static boolean isNativeCodeLoaded() {
    return nativeCodeLoaded;
  }

  /**
   * Return if native hadoop libraries, if present, can be used for this job.
   * @param conf configuration
   * 
   * @return <code>true</code> if native hadoop libraries, if present, can be 
   *         used for this job; <code>false</code> otherwise.
   */
  public boolean getLoadNativeLibraries(Configuration conf) {
    return conf.getBoolean("hadoop.native.lib", true);
  }
  
  /**
   * Set if native hadoop libraries, if present, can be used for this job.
   * 
   * @param conf configuration
   * @param loadNativeLibraries can native hadoop libraries be loaded
   */
  public void setLoadNativeLibraries(Configuration conf, 
                                     boolean loadNativeLibraries) {
    conf.setBoolean("hadoop.native.lib", loadNativeLibraries);
  }

}
