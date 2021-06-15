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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;

public class TestCodecFactory extends TestCase {

  private static class BaseCodec implements CompressionCodec {
    private Configuration conf;
    
    public void setConf(Configuration conf) {
      this.conf = conf;
    }
    
    public Configuration getConf() {
      return conf;
    }
    
    public CompressionOutputStream createOutputStream(OutputStream out) 
    throws IOException {
      return null;
    }
    
    public Class<? extends Compressor> getCompressorType() {
      return null;
    }

    public Compressor createCompressor() {
      return null;
    }

    public CompressionInputStream createInputStream(InputStream in, 
                                                    Decompressor decompressor) 
    throws IOException {
      return null;
    }

    public CompressionInputStream createInputStream(InputStream in) 
    throws IOException {
      return null;
    }

    public CompressionOutputStream createOutputStream(OutputStream out, 
                                                      Compressor compressor) 
    throws IOException {
      return null;
    }

    public Class<? extends Decompressor> getDecompressorType() {
      return null;
    }

    public Decompressor createDecompressor() {
      return null;
    }

    public String getDefaultExtension() {
      return ".base";
    }
  }
  
  private static class BarCodec extends BaseCodec {
    public String getDefaultExtension() {
      return "bar";
    }
  }
  
  private static class FooBarCodec extends BaseCodec {
    public String getDefaultExtension() {
      return ".foo.bar";
    }
  }
  
  private static class FooCodec extends BaseCodec {
    public String getDefaultExtension() {
      return ".foo";
    }
  }
  
  /**
   * Returns a factory for a given set of codecs
   * @param classes the codec classes to include
   * @return a new factory
   */
  private static CompressionCodecFactory setClasses(Class[] classes) {
    Configuration conf = new Configuration();
    CompressionCodecFactory.setCodecClasses(conf, Arrays.asList(classes));
    return new CompressionCodecFactory(conf);
  }
  
  private static void checkCodec(String msg, 
                                 Class expected, CompressionCodec actual) {
    assertEquals(msg + " unexpected codec found",
                 expected.getName(),
                 actual.getClass().getName());
  }
  
  public static void testFinding() {
    CompressionCodecFactory factory = 
      new CompressionCodecFactory(new Configuration());
    CompressionCodec codec = factory.getCodec(new Path("/tmp/foo.bar"));
    assertEquals("default factory foo codec", null, codec);
    codec = factory.getCodec(new Path("/tmp/foo.gz"));
    checkCodec("default factory for .gz", GzipCodec.class, codec);
    codec = factory.getCodec(new Path("/tmp/foo.bz2"));
    checkCodec("default factory for .bz2", BZip2Codec.class, codec);
    factory = setClasses(new Class[0]);
    codec = factory.getCodec(new Path("/tmp/foo.bar"));
    assertEquals("empty codec bar codec", null, codec);
    codec = factory.getCodec(new Path("/tmp/foo.gz"));
    assertEquals("empty codec gz codec", null, codec);
    codec = factory.getCodec(new Path("/tmp/foo.bz2"));
    assertEquals("default factory for .bz2", null, codec);
    factory = setClasses(new Class[]{BarCodec.class, FooCodec.class, 
                                     FooBarCodec.class});
    codec = factory.getCodec(new Path("/tmp/.foo.bar.gz"));
    assertEquals("full factory gz codec", null, codec);
    codec = factory.getCodec(new Path("/tmp/foo.bz2"));
    assertEquals("default factory for .bz2", null, codec);
    codec = factory.getCodec(new Path("/tmp/foo.bar"));
    checkCodec("full factory bar codec", BarCodec.class, codec);
    codec = factory.getCodec(new Path("/tmp/foo/baz.foo.bar"));
    checkCodec("full factory foo bar codec", FooBarCodec.class, codec);
    codec = factory.getCodec(new Path("/tmp/foo.foo"));
    checkCodec("full factory foo codec", FooCodec.class, codec);
  }
}
