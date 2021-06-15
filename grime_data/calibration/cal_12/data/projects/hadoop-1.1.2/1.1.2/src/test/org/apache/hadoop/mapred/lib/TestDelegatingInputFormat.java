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
package org.apache.hadoop.mapred.lib;

import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;

public class TestDelegatingInputFormat extends TestCase {

  public void testSplitting() throws Exception {
    JobConf conf = new JobConf();
    conf.set("fs.hdfs.impl",
       "org.apache.hadoop.hdfs.ChecksumDistributedFileSystem");
    MiniDFSCluster dfs = null;
    try {
      dfs = new MiniDFSCluster(conf, 4, true, new String[] { "/rack0",
         "/rack0", "/rack1", "/rack1" }, new String[] { "host0", "host1",
         "host2", "host3" });
      FileSystem fs = dfs.getFileSystem();

      Path path = getPath("/foo/bar", fs);
      Path path2 = getPath("/foo/baz", fs);
      Path path3 = getPath("/bar/bar", fs);
      Path path4 = getPath("/bar/baz", fs);

      final int numSplits = 100;

      MultipleInputs.addInputPath(conf, path, TextInputFormat.class,
         MapClass.class);
      MultipleInputs.addInputPath(conf, path2, TextInputFormat.class,
         MapClass2.class);
      MultipleInputs.addInputPath(conf, path3, KeyValueTextInputFormat.class,
         MapClass.class);
      MultipleInputs.addInputPath(conf, path4, TextInputFormat.class,
         MapClass2.class);
      DelegatingInputFormat inFormat = new DelegatingInputFormat();
      InputSplit[] splits = inFormat.getSplits(conf, numSplits);

      int[] bins = new int[3];
      for (InputSplit split : splits) {
       assertTrue(split instanceof TaggedInputSplit);
       final TaggedInputSplit tis = (TaggedInputSplit) split;
       int index = -1;

       if (tis.getInputFormatClass().equals(KeyValueTextInputFormat.class)) {
         // path3
         index = 0;
       } else if (tis.getMapperClass().equals(MapClass.class)) {
         // path
         index = 1;
       } else {
         // path2 and path4
         index = 2;
       }

       bins[index]++;
      }

      // Each bin is a unique combination of a Mapper and InputFormat, and
      // DelegatingInputFormat should split each bin into numSplits splits,
      // regardless of the number of paths that use that Mapper/InputFormat
      for (int count : bins) {
       assertEquals(numSplits, count);
      }

      assertTrue(true);
    } finally {
      if (dfs != null) {
       dfs.shutdown();
      }
    }
  }

  static Path getPath(final String location, final FileSystem fs)
      throws IOException {
    Path path = new Path(location);

    // create a multi-block file on hdfs
    DataOutputStream out = fs.create(path, true, 4096, (short) 2, 512, null);
    for (int i = 0; i < 1000; ++i) {
      out.writeChars("Hello\n");
    }
    out.close();

    return path;
  }

  static class MapClass implements Mapper<String, String, String, String> {

    public void map(String key, String value,
       OutputCollector<String, String> output, Reporter reporter)
       throws IOException {
    }

    public void configure(JobConf job) {
    }

    public void close() throws IOException {
    }
  }

  static class MapClass2 extends MapClass {
  }

}
