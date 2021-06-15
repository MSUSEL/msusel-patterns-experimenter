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
package org.apache.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.OutputStream;
import org.junit.Test;

/**
 * Regression test for HDFS-1542, a deadlock between the main thread
 * and the DFSOutputStream.DataStreamer thread caused because
 * Configuration.writeXML holds a lock on itself while writing to DFS.
 */
public class TestWriteConfigurationToDFS {
  @Test(timeout=60000)
  public void testWriteConf() throws Exception {
    Configuration conf = new Configuration();
    conf.setInt(DFSConfigKeys.DFS_BLOCK_SIZE_KEY, 4096);
    System.out.println("Setting conf in: " + System.identityHashCode(conf));
    
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null, null);
    cluster.waitActive();
    try {
      FileSystem fs = cluster.getFileSystem();
      Path filePath = new Path("/testWriteConf.xml");
      OutputStream os = fs.create(filePath);
      StringBuilder longString = new StringBuilder();
      for (int i = 0; i < 100000; i++) {
        longString.append("hello");
      } // 500KB
      conf.set("foobar", longString.toString());
      conf.writeXml(os);
      os.close();
    } finally {
      cluster.shutdown();
    }
  }
}