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

import junit.framework.TestCase;
import java.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * This class tests the DFS class via the FileSystem interface in a single node
 * mini-cluster.
 */
public class TestLocalDFS extends TestCase {

  private void writeFile(FileSystem fileSys, Path name) throws IOException {
    DataOutputStream stm = fileSys.create(name);
    stm.writeBytes("oom");
    stm.close();
  }
  
  private void readFile(FileSystem fileSys, Path name) throws IOException {
    DataInputStream stm = fileSys.open(name);
    byte[] buffer = new byte[4];
    int bytesRead = stm.read(buffer, 0 , 4);
    assertEquals("oom", new String(buffer, 0 , bytesRead));
    stm.close();
  }
  
  private void cleanupFile(FileSystem fileSys, Path name) throws IOException {
    assertTrue(fileSys.exists(name));
    fileSys.delete(name, true);
    assertTrue(!fileSys.exists(name));
  }

  static String getUserName(FileSystem fs) {
    if (fs instanceof DistributedFileSystem) {
      return ((DistributedFileSystem)fs).dfs.ugi.getShortUserName();
    }
    return System.getProperty("user.name");
  }

  /**
   * Tests get/set working directory in DFS.
   */
  public void testWorkingDirectory() throws IOException {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    FileSystem fileSys = cluster.getFileSystem();
    try {
      Path orig_path = fileSys.getWorkingDirectory();
      assertTrue(orig_path.isAbsolute());
      Path file1 = new Path("somewhat/random.txt");
      writeFile(fileSys, file1);
      assertTrue(fileSys.exists(new Path(orig_path, file1.toString())));
      fileSys.delete(file1, true);
      Path subdir1 = new Path("/somewhere");
      fileSys.setWorkingDirectory(subdir1);
      writeFile(fileSys, file1);
      cleanupFile(fileSys, new Path(subdir1, file1.toString()));
      Path subdir2 = new Path("else");
      fileSys.setWorkingDirectory(subdir2);
      writeFile(fileSys, file1);
      readFile(fileSys, file1);
      cleanupFile(fileSys, new Path(new Path(subdir1, subdir2.toString()),
                                    file1.toString()));

      // test home directory
      Path home = new Path("/user/" + getUserName(fileSys))
        .makeQualified(fileSys);
      Path fsHome = fileSys.getHomeDirectory();
      assertEquals(home, fsHome);

    } finally {
      fileSys.close();
      cluster.shutdown();
    }
  }
}
