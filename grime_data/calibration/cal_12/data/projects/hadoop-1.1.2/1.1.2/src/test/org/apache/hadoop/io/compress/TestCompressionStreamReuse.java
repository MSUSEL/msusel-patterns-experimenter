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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.RandomDatum;
import org.apache.hadoop.io.compress.zlib.ZlibFactory;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionLevel;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionStrategy;
import org.apache.hadoop.util.ReflectionUtils;

import junit.framework.TestCase;

public class TestCompressionStreamReuse extends TestCase {
  private static final Log LOG = LogFactory
      .getLog(TestCompressionStreamReuse.class);

  private Configuration conf = new Configuration();
  private int count = 10000;
  private int seed = new Random().nextInt();

  public void testBZip2Codec() throws IOException {
    resetStateTest(conf, seed, count,
        "org.apache.hadoop.io.compress.BZip2Codec");
  }

  public void testGzipCompressStreamReuse() throws IOException {
    resetStateTest(conf, seed, count,
        "org.apache.hadoop.io.compress.GzipCodec");
  }

  public void testGzipCompressStreamReuseWithParam() throws IOException {
    Configuration conf = new Configuration(this.conf);
    ZlibFactory
        .setCompressionLevel(conf, CompressionLevel.BEST_COMPRESSION);
    ZlibFactory.setCompressionStrategy(conf,
        CompressionStrategy.HUFFMAN_ONLY);
    resetStateTest(conf, seed, count,
        "org.apache.hadoop.io.compress.GzipCodec");
  }

  private static void resetStateTest(Configuration conf, int seed, int count,
      String codecClass) throws IOException {
    // Create the codec
    CompressionCodec codec = null;
    try {
      codec = (CompressionCodec) ReflectionUtils.newInstance(conf
          .getClassByName(codecClass), conf);
    } catch (ClassNotFoundException cnfe) {
      throw new IOException("Illegal codec!");
    }
    LOG.info("Created a Codec object of type: " + codecClass);

    // Generate data
    DataOutputBuffer data = new DataOutputBuffer();
    RandomDatum.Generator generator = new RandomDatum.Generator(seed);
    for (int i = 0; i < count; ++i) {
      generator.next();
      RandomDatum key = generator.getKey();
      RandomDatum value = generator.getValue();

      key.write(data);
      value.write(data);
    }
    LOG.info("Generated " + count + " records");

    // Compress data
    DataOutputBuffer compressedDataBuffer = new DataOutputBuffer();
    DataOutputStream deflateOut = new DataOutputStream(
        new BufferedOutputStream(compressedDataBuffer));
    CompressionOutputStream deflateFilter = codec
        .createOutputStream(deflateOut);
    deflateFilter.write(data.getData(), 0, data.getLength());
    deflateFilter.finish();
    deflateFilter.flush();
    LOG.info("Finished compressing data");

    // reset deflator
    deflateFilter.resetState();
    LOG.info("Finished reseting deflator");

    // re-generate data
    data.reset();
    generator = new RandomDatum.Generator(seed);
    for (int i = 0; i < count; ++i) {
      generator.next();
      RandomDatum key = generator.getKey();
      RandomDatum value = generator.getValue();

      key.write(data);
      value.write(data);
    }
    DataInputBuffer originalData = new DataInputBuffer();
    DataInputStream originalIn = new DataInputStream(
        new BufferedInputStream(originalData));
    originalData.reset(data.getData(), 0, data.getLength());

    // re-compress data
    compressedDataBuffer.reset();
    deflateOut = new DataOutputStream(new BufferedOutputStream(
        compressedDataBuffer));
    deflateFilter = codec.createOutputStream(deflateOut);

    deflateFilter.write(data.getData(), 0, data.getLength());
    deflateFilter.finish();
    deflateFilter.flush();
    LOG.info("Finished re-compressing data");

    // De-compress data
    DataInputBuffer deCompressedDataBuffer = new DataInputBuffer();
    deCompressedDataBuffer.reset(compressedDataBuffer.getData(), 0,
        compressedDataBuffer.getLength());
    CompressionInputStream inflateFilter = codec
        .createInputStream(deCompressedDataBuffer);
    DataInputStream inflateIn = new DataInputStream(
        new BufferedInputStream(inflateFilter));

    // Check
    for (int i = 0; i < count; ++i) {
      RandomDatum k1 = new RandomDatum();
      RandomDatum v1 = new RandomDatum();
      k1.readFields(originalIn);
      v1.readFields(originalIn);

      RandomDatum k2 = new RandomDatum();
      RandomDatum v2 = new RandomDatum();
      k2.readFields(inflateIn);
      v2.readFields(inflateIn);
      assertTrue(
          "original and compressed-then-decompressed-output not equal",
          k1.equals(k2) && v1.equals(v2));
    }
    LOG.info("SUCCESS! Completed checking " + count + " records");
  }
}
