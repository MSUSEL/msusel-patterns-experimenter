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
package org.apache.hadoop.io.compress.zlib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionLevel;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionStrategy;
import org.apache.hadoop.util.NativeCodeLoader;

/**
 * A collection of factories to create the right 
 * zlib/gzip compressor/decompressor instances.
 * 
 */
public class ZlibFactory {
  private static final Log LOG =
    LogFactory.getLog(ZlibFactory.class);

  private static boolean nativeZlibLoaded = false;
  
  static {
    if (NativeCodeLoader.isNativeCodeLoaded()) {
      nativeZlibLoaded = ZlibCompressor.isNativeZlibLoaded() &&
        ZlibDecompressor.isNativeZlibLoaded();
      
      if (nativeZlibLoaded) {
        LOG.info("Successfully loaded & initialized native-zlib library");
      } else {
        LOG.warn("Failed to load/initialize native-zlib library");
      }
    }
  }
  
  /**
   * Check if native-zlib code is loaded & initialized correctly and 
   * can be loaded for this job.
   * 
   * @param conf configuration
   * @return <code>true</code> if native-zlib is loaded & initialized 
   *         and can be loaded for this job, else <code>false</code>
   */
  public static boolean isNativeZlibLoaded(Configuration conf) {
    return nativeZlibLoaded && conf.getBoolean("hadoop.native.lib", true); 
  }
  
  /**
   * Return the appropriate type of the zlib compressor. 
   * 
   * @param conf configuration
   * @return the appropriate type of the zlib compressor.
   */
  public static Class<? extends Compressor> 
  getZlibCompressorType(Configuration conf) {
    return (isNativeZlibLoaded(conf)) ? 
            ZlibCompressor.class : BuiltInZlibDeflater.class;
  }
  
  /**
   * Return the appropriate implementation of the zlib compressor. 
   * 
   * @param conf configuration
   * @return the appropriate implementation of the zlib compressor.
   */
  public static Compressor getZlibCompressor(Configuration conf) {
    return (isNativeZlibLoaded(conf)) ? 
      new ZlibCompressor(conf) :
      new BuiltInZlibDeflater(conf);
  }

  /**
   * Return the appropriate type of the zlib decompressor. 
   * 
   * @param conf configuration
   * @return the appropriate type of the zlib decompressor.
   */
  public static Class<? extends Decompressor> 
  getZlibDecompressorType(Configuration conf) {
    return (isNativeZlibLoaded(conf)) ? 
            ZlibDecompressor.class : BuiltInZlibInflater.class;
  }
  
  /**
   * Return the appropriate implementation of the zlib decompressor. 
   * 
   * @param conf configuration
   * @return the appropriate implementation of the zlib decompressor.
   */
  public static Decompressor getZlibDecompressor(Configuration conf) {
    return (isNativeZlibLoaded(conf)) ? 
      new ZlibDecompressor() : new BuiltInZlibInflater(); 
  }

  public static void setCompressionStrategy(Configuration conf,
      CompressionStrategy strategy) {
    conf.setEnum("zlib.compress.strategy", strategy);
  }

  public static CompressionStrategy getCompressionStrategy(Configuration conf) {
    return conf.getEnum("zlib.compress.strategy",
        CompressionStrategy.DEFAULT_STRATEGY);
  }

  public static void setCompressionLevel(Configuration conf,
      CompressionLevel level) {
    conf.setEnum("zlib.compress.level", level);
  }

  public static CompressionLevel getCompressionLevel(Configuration conf) {
    return conf.getEnum("zlib.compress.level",
        CompressionLevel.DEFAULT_COMPRESSION);
  }

}
