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

import java.io.*;
import junit.framework.TestCase;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.MRCaching.TestResult;

/**
 * A JUnit test to test caching with DFS
 * 
 */
public class TestMiniMRDFSCaching extends TestCase {

  public void testWithDFS() throws IOException {
    MiniMRCluster mr = null;
    MiniDFSCluster dfs = null;
    FileSystem fileSys = null;
    try {
      JobConf conf = new JobConf();
      conf.set("fs.hdfs.impl",
               "org.apache.hadoop.hdfs.ChecksumDistributedFileSystem");      
      dfs = new MiniDFSCluster(conf, 1, true, null);
      fileSys = dfs.getFileSystem();
      mr = new MiniMRCluster(2, fileSys.getName(), 4);
      MRCaching.setupCache("/cachedir", fileSys);
      // run the wordcount example with caching
      TestResult ret = MRCaching.launchMRCache("/testing/wc/input",
                                            "/testing/wc/output",
                                            "/cachedir",
                                            mr.createJobConf(),
                                            "The quick brown fox\nhas many silly\n"
                                            + "red fox sox\n", false);
      assertTrue("Archives not matching", ret.isOutputOk);
      // launch MR cache with symlinks
      ret = MRCaching.launchMRCache("/testing/wc/input",
                                    "/testing/wc/output",
                                    "/cachedir",
                                    mr.createJobConf(),
                                    "The quick brown fox\nhas many silly\n"
                                    + "red fox sox\n", true);
      assertTrue("Archives not matching", ret.isOutputOk);
    } finally {
      if (fileSys != null) {
        fileSys.close();
      }
      if (dfs != null) {
        dfs.shutdown();
      }
      if (mr != null) {
        mr.shutdown();
      }
    }
  }

  public static void main(String[] argv) throws Exception {
    TestMiniMRDFSCaching td = new TestMiniMRDFSCaching();
    td.testWithDFS();
  }
}
