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
package org.apache.hadoop.io.compress;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * A factory that will find the correct codec for a given filename.
 */
public class CompressionCodecFactory {

  public static final Log LOG =
    LogFactory.getLog(CompressionCodecFactory.class.getName());

  /**
   * A map from the reversed filename suffixes to the codecs.
   * This is probably overkill, because the maps should be small, but it 
   * automatically supports finding the longest matching suffix. 
   */
  private SortedMap<String, CompressionCodec> codecs = null;
  
  private void addCodec(CompressionCodec codec) {
    String suffix = codec.getDefaultExtension();
    codecs.put(new StringBuffer(suffix).reverse().toString(), codec);
  }
  
  /**
   * Print the extension map out as a string.
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    Iterator<Map.Entry<String, CompressionCodec>> itr = 
      codecs.entrySet().iterator();
    buf.append("{ ");
    if (itr.hasNext()) {
      Map.Entry<String, CompressionCodec> entry = itr.next();
      buf.append(entry.getKey());
      buf.append(": ");
      buf.append(entry.getValue().getClass().getName());
      while (itr.hasNext()) {
        entry = itr.next();
        buf.append(", ");
        buf.append(entry.getKey());
        buf.append(": ");
        buf.append(entry.getValue().getClass().getName());
      }
    }
    buf.append(" }");
    return buf.toString();
  }

  /**
   * Get the list of codecs listed in the configuration
   * @param conf the configuration to look in
   * @return a list of the Configuration classes or null if the attribute
   *         was not set
   */
  public static List<Class<? extends CompressionCodec>> getCodecClasses(Configuration conf) {
    String codecsString = conf.get("io.compression.codecs");
    if (codecsString != null) {
      List<Class<? extends CompressionCodec>> result
        = new ArrayList<Class<? extends CompressionCodec>>();
      StringTokenizer codecSplit = new StringTokenizer(codecsString, ",");
      while (codecSplit.hasMoreElements()) {
        String codecSubstring = codecSplit.nextToken();
        if (codecSubstring.length() != 0) {
          try {
            Class<?> cls = conf.getClassByName(codecSubstring);
            if (!CompressionCodec.class.isAssignableFrom(cls)) {
              throw new IllegalArgumentException("Class " + codecSubstring +
                                                 " is not a CompressionCodec");
            }
            result.add(cls.asSubclass(CompressionCodec.class));
          } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Compression codec " + 
                                               codecSubstring + " not found.",
                                               ex);
          }
        }
      }
      return result;
    } else {
      return null;
    }
  }
  
  /**
   * Sets a list of codec classes in the configuration.
   * @param conf the configuration to modify
   * @param classes the list of classes to set
   */
  public static void setCodecClasses(Configuration conf,
                                     List<Class> classes) {
    StringBuffer buf = new StringBuffer();
    Iterator<Class> itr = classes.iterator();
    if (itr.hasNext()) {
      Class cls = itr.next();
      buf.append(cls.getName());
      while(itr.hasNext()) {
        buf.append(',');
        buf.append(itr.next().getName());
      }
    }
    conf.set("io.compression.codecs", buf.toString());   
  }
  
  /**
   * Find the codecs specified in the config value io.compression.codecs 
   * and register them. Defaults to gzip and zip.
   */
  public CompressionCodecFactory(Configuration conf) {
    codecs = new TreeMap<String, CompressionCodec>();
    List<Class<? extends CompressionCodec>> codecClasses = getCodecClasses(conf);
    if (codecClasses == null) {
      addCodec(new GzipCodec());
      addCodec(new DefaultCodec());      
    } else {
      Iterator<Class<? extends CompressionCodec>> itr = codecClasses.iterator();
      while (itr.hasNext()) {
        CompressionCodec codec = ReflectionUtils.newInstance(itr.next(), conf);
        addCodec(codec);     
      }
    }
  }
  
  /**
   * Find the relevant compression codec for the given file based on its
   * filename suffix.
   * @param file the filename to check
   * @return the codec object
   */
  public CompressionCodec getCodec(Path file) {
    CompressionCodec result = null;
    if (codecs != null) {
      String filename = file.getName();
      String reversedFilename = new StringBuffer(filename).reverse().toString();
      SortedMap<String, CompressionCodec> subMap = 
        codecs.headMap(reversedFilename);
      if (!subMap.isEmpty()) {
        String potentialSuffix = subMap.lastKey();
        if (reversedFilename.startsWith(potentialSuffix)) {
          result = codecs.get(potentialSuffix);
        }
      }
    }
    return result;
  }
  
  /**
   * Removes a suffix from a filename, if it has it.
   * @param filename the filename to strip
   * @param suffix the suffix to remove
   * @return the shortened filename
   */
  public static String removeSuffix(String filename, String suffix) {
    if (filename.endsWith(suffix)) {
      return filename.substring(0, filename.length() - suffix.length());
    }
    return filename;
  }
  
  /**
   * A little test program.
   * @param args
   */
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    CompressionCodecFactory factory = new CompressionCodecFactory(conf);
    boolean encode = false;
    for(int i=0; i < args.length; ++i) {
      if ("-in".equals(args[i])) {
        encode = true;
      } else if ("-out".equals(args[i])) {
        encode = false;
      } else {
        CompressionCodec codec = factory.getCodec(new Path(args[i]));
        if (codec == null) {
          System.out.println("Codec for " + args[i] + " not found.");
        } else { 
          if (encode) {
            CompressionOutputStream out = 
              codec.createOutputStream(new java.io.FileOutputStream(args[i]));
            byte[] buffer = new byte[100];
            String inFilename = removeSuffix(args[i], 
                                             codec.getDefaultExtension());
            java.io.InputStream in = new java.io.FileInputStream(inFilename);
            int len = in.read(buffer);
            while (len > 0) {
              out.write(buffer, 0, len);
              len = in.read(buffer);
            }
            in.close();
            out.close();
          } else {
            CompressionInputStream in = 
              codec.createInputStream(new java.io.FileInputStream(args[i]));
            byte[] buffer = new byte[100];
            int len = in.read(buffer);
            while (len > 0) {
              System.out.write(buffer, 0, len);
              len = in.read(buffer);
            }
            in.close();
          }
        }
      }
    }
  }
}
