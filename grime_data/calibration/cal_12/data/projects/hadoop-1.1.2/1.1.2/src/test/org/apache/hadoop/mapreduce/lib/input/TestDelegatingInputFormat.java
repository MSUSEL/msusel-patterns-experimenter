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
package org.apache.hadoop.mapreduce.lib.input;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

public class TestDelegatingInputFormat extends TestCase {

  @SuppressWarnings("unchecked")
  public void testSplitting() throws Exception {
    Job job = new Job();
    MiniDFSCluster dfs = null;
    try {
      dfs = new MiniDFSCluster(job.getConfiguration(), 4, true, new String[] { "/rack0",
         "/rack0", "/rack1", "/rack1" }, new String[] { "host0", "host1",
         "host2", "host3" });
      FileSystem fs = dfs.getFileSystem();

      Path path = getPath("/foo/bar", fs);
      Path path2 = getPath("/foo/baz", fs);
      Path path3 = getPath("/bar/bar", fs);
      Path path4 = getPath("/bar/baz", fs);

      final int numSplits = 100;

      FileInputFormat.setMaxInputSplitSize(job, 
              fs.getFileStatus(path).getLen() / numSplits);
      MultipleInputs.addInputPath(job, path, TextInputFormat.class,
         MapClass.class);
      MultipleInputs.addInputPath(job, path2, TextInputFormat.class,
         MapClass2.class);
      MultipleInputs.addInputPath(job, path3, KeyValueTextInputFormat.class,
         MapClass.class);
      MultipleInputs.addInputPath(job, path4, TextInputFormat.class,
         MapClass2.class);
      DelegatingInputFormat inFormat = new DelegatingInputFormat();

      int[] bins = new int[3];
      for (InputSplit split : (List<InputSplit>)inFormat.getSplits(job)) {
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

      assertEquals("count is not equal to num splits", numSplits, bins[0]);
      assertEquals("count is not equal to num splits", numSplits, bins[1]);
      assertEquals("count is not equal to 2 * num splits",
        numSplits * 2, bins[2]);
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

  static class MapClass extends Mapper<String, String, String, String> {
  }

  static class MapClass2 extends MapClass {
  }

}
