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
package org.apache.hadoop.mapred;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.hdfs.MiniDFSCluster;

public class TestCustomOutputCommitter extends TestCase {
  static final Path input = new Path("/test/input/");
  static final Path output = new Path("/test/output");
  
  public void testCommitter() throws Exception {
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    FileSystem fs = null;
    Path testFile = new Path(input, "testfile");
    try {
      Configuration conf = new Configuration();

      //start the mini mr and dfs cluster.
      dfs = new MiniDFSCluster(conf, 2 , true, null);
      fs = dfs.getFileSystem();
      FSDataOutputStream stream = fs.create(testFile);
      stream.write("teststring".getBytes());
      stream.close();

      mr = new MiniMRCluster(2, fs.getUri().toString(), 1);

      String[] args = new String[6];
      args[0] = "-libjars";
      // the testjob.jar as a temporary jar file 
      // holding custom output committer
      args[1] = "build/test/testjar/testjob.jar";
      args[2] = "-D";
      args[3] = "mapred.output.committer.class=testjar.CustomOutputCommitter";
      args[4] = input.toString();
      args[5] = output.toString();
      JobConf jobConf = mr.createJobConf();
      int ret = ToolRunner.run(jobConf, new WordCount(), args);

      assertTrue("not failed ", ret == 0);
    } finally {
      if (dfs != null) {dfs.shutdown();};
      if (mr != null) {mr.shutdown();};
    }
  }
}
