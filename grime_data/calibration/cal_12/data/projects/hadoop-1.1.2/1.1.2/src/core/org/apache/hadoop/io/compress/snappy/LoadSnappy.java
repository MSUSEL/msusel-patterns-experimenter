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
package org.apache.hadoop.io.compress.snappy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.NativeCodeLoader;

/**
 * Determines if Snappy native library is available and loads it if available.
 */
public class LoadSnappy {
  private static final Log LOG = LogFactory.getLog(LoadSnappy.class.getName());

  private static boolean AVAILABLE = false;
  private static boolean LOADED = false;

  static {
    try {
      System.loadLibrary("snappy");
      LOG.warn("Snappy native library is available");
      AVAILABLE = true;
    } catch (UnsatisfiedLinkError ex) {
      //NOP
    }
    boolean hadoopNativeAvailable = NativeCodeLoader.isNativeCodeLoaded();
    LOADED = AVAILABLE && hadoopNativeAvailable;
    if (LOADED) {
      LOG.info("Snappy native library loaded");
    } else {
      LOG.warn("Snappy native library not loaded");
    }
  }

  /**
   * Returns if Snappy native library is loaded.
   *
   * @return <code>true</code> if Snappy native library is loaded,
   * <code>false</code> if not.
   */
  public static boolean isAvailable() {
    return AVAILABLE;
  }

  /**
   * Returns if Snappy native library is loaded.
   *
   * @return <code>true</code> if Snappy native library is loaded,
   * <code>false</code> if not.
   */
  public static boolean isLoaded() {
    return LOADED;
  }

}
