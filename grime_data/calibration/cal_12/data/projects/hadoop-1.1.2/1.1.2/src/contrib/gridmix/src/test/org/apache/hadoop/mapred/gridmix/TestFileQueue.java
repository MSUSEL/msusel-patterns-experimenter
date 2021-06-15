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
package org.apache.hadoop.mapred.gridmix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class TestFileQueue {

  static final Log LOG = LogFactory.getLog(TestFileQueue.class);
  static final int NFILES = 4;
  static final int BLOCK = 256;
  static final Path[] paths = new Path[NFILES];
  static final String[] loc = new String[NFILES];
  static final long[] start = new long[NFILES];
  static final long[] len = new long[NFILES];

  @BeforeClass
  public static void setup() throws IOException {
    final Configuration conf = new Configuration();
    final FileSystem fs = FileSystem.getLocal(conf).getRaw();
    final Path p = new Path(System.getProperty("test.build.data", "/tmp"),
        "testFileQueue").makeQualified(fs);
    fs.delete(p, true);
    final byte[] b = new byte[BLOCK];
    for (int i = 0; i < NFILES; ++i) {
      Arrays.fill(b, (byte)('A' + i));
      paths[i] = new Path(p, "" + (char)('A' + i));
      OutputStream f = null;
      try {
        f = fs.create(paths[i]);
        f.write(b);
      } finally {
        if (f != null) {
          f.close();
        }
      }
    }
  }

  @AfterClass
  public static void cleanup() throws IOException {
    final Configuration conf = new Configuration();
    final FileSystem fs = FileSystem.getLocal(conf).getRaw();
    final Path p = new Path(System.getProperty("test.build.data", "/tmp"),
        "testFileQueue").makeQualified(fs);
    fs.delete(p, true);
  }

  static ByteArrayOutputStream fillVerif() throws IOException {
    final byte[] b = new byte[BLOCK];
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (int i = 0; i < NFILES; ++i) {
      Arrays.fill(b, (byte)('A' + i));
      out.write(b, 0, (int)len[i]);
    }
    return out;
  }

  @Test
  public void testRepeat() throws Exception {
    final Configuration conf = new Configuration();
    Arrays.fill(loc, "");
    Arrays.fill(start, 0L);
    Arrays.fill(len, BLOCK);

    final ByteArrayOutputStream out = fillVerif();
    final FileQueue q =
      new FileQueue(new CombineFileSplit(paths, start, len, loc), conf);
    final byte[] verif = out.toByteArray();
    final byte[] check = new byte[2 * NFILES * BLOCK];
    q.read(check, 0, NFILES * BLOCK);
    assertArrayEquals(verif, Arrays.copyOf(check, NFILES * BLOCK));

    final byte[] verif2 = new byte[2 * NFILES * BLOCK];
    System.arraycopy(verif, 0, verif2, 0, verif.length);
    System.arraycopy(verif, 0, verif2, verif.length, verif.length);
    q.read(check, 0, 2 * NFILES * BLOCK);
    assertArrayEquals(verif2, check);

  }

  @Test
  public void testUneven() throws Exception {
    final Configuration conf = new Configuration();
    Arrays.fill(loc, "");
    Arrays.fill(start, 0L);
    Arrays.fill(len, BLOCK);

    final int B2 = BLOCK / 2;
    for (int i = 0; i < NFILES; i += 2) {
      start[i] += B2;
      len[i] -= B2;
    }
    final FileQueue q =
      new FileQueue(new CombineFileSplit(paths, start, len, loc), conf);
    final ByteArrayOutputStream out = fillVerif();
    final byte[] verif = out.toByteArray();
    final byte[] check = new byte[NFILES / 2 * BLOCK + NFILES / 2 * B2];
    q.read(check, 0, verif.length);
    assertArrayEquals(verif, Arrays.copyOf(check, verif.length));
    q.read(check, 0, verif.length);
    assertArrayEquals(verif, Arrays.copyOf(check, verif.length));
  }

  @Test
  public void testEmpty() throws Exception {
    final Configuration conf = new Configuration();
    // verify OK if unused
    final FileQueue q = new FileQueue(new CombineFileSplit(
          new Path[0], new long[0], new long[0], new String[0]), conf);
  }

}
