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
package org.apache.hadoop.fs;

import org.apache.hadoop.conf.Configuration;
import java.io.*;

import junit.framework.*;

/**
 * This class tests the local file system via the FileSystem abstraction.
 */
public class TestLocalFileSystem extends TestCase {
  private static String TEST_ROOT_DIR
    = System.getProperty("test.build.data","build/test/data/work-dir/localfs");


  static void writeFile(FileSystem fs, Path name) throws IOException {
    FSDataOutputStream stm = fs.create(name);
    stm.writeBytes("42\n");
    stm.close();
  }
  
  static String readFile(FileSystem fs, Path name) throws IOException {
    byte[] b = new byte[1024];
    int offset = 0;
    FSDataInputStream in = fs.open(name);
    for(int remaining, n;
        (remaining = b.length - offset) > 0 && (n = in.read(b, offset, remaining)) != -1;
        offset += n); 
    in.close();

    String s = new String(b, 0, offset);
    System.out.println("s=" + s);
    return s;
  }

  private void cleanupFile(FileSystem fs, Path name) throws IOException {
    assertTrue(fs.exists(name));
    fs.delete(name, true);
    assertTrue(!fs.exists(name));
  }
  
  /**
   * Test the capability of setting the working directory.
   */
  public void testWorkingDirectory() throws IOException {
    Configuration conf = new Configuration();
    FileSystem fileSys = FileSystem.getLocal(conf);
    Path origDir = fileSys.getWorkingDirectory();
    Path subdir = new Path(TEST_ROOT_DIR, "new");
    try {
      // make sure it doesn't already exist
      assertTrue(!fileSys.exists(subdir));
      // make it and check for it
      assertTrue(fileSys.mkdirs(subdir));
      assertTrue(fileSys.isDirectory(subdir));
      
      fileSys.setWorkingDirectory(subdir);
      
      // create a directory and check for it
      Path dir1 = new Path("dir1");
      assertTrue(fileSys.mkdirs(dir1));
      assertTrue(fileSys.isDirectory(dir1));
      
      // delete the directory and make sure it went away
      fileSys.delete(dir1, true);
      assertTrue(!fileSys.exists(dir1));
      
      // create files and manipulate them.
      Path file1 = new Path("file1");
      Path file2 = new Path("sub/file2");
      writeFile(fileSys, file1);
      fileSys.copyFromLocalFile(file1, file2);
      assertTrue(fileSys.exists(file1));
      assertTrue(fileSys.isFile(file1));
      cleanupFile(fileSys, file2);
      fileSys.copyToLocalFile(file1, file2);
      cleanupFile(fileSys, file2);
      
      // try a rename
      fileSys.rename(file1, file2);
      assertTrue(!fileSys.exists(file1));
      assertTrue(fileSys.exists(file2));
      fileSys.rename(file2, file1);
      
      // try reading a file
      InputStream stm = fileSys.open(file1);
      byte[] buffer = new byte[3];
      int bytesRead = stm.read(buffer, 0, 3);
      assertEquals("42\n", new String(buffer, 0, bytesRead));
      stm.close();
    } finally {
      fileSys.setWorkingDirectory(origDir);
      fileSys.delete(subdir, true);
    }
  }

  public void testCopy() throws IOException {
    Configuration conf = new Configuration();
    LocalFileSystem fs = FileSystem.getLocal(conf);
    Path src = new Path(TEST_ROOT_DIR, "dingo");
    Path dst = new Path(TEST_ROOT_DIR, "yak");
    writeFile(fs, src);
    assertTrue(FileUtil.copy(fs, src, fs, dst, true, false, conf));
    assertTrue(!fs.exists(src) && fs.exists(dst));
    assertTrue(FileUtil.copy(fs, dst, fs, src, false, false, conf));
    assertTrue(fs.exists(src) && fs.exists(dst));
    assertTrue(FileUtil.copy(fs, src, fs, dst, true, true, conf));
    assertTrue(!fs.exists(src) && fs.exists(dst));
    fs.mkdirs(src);
    assertTrue(FileUtil.copy(fs, dst, fs, src, false, false, conf));
    Path tmp = new Path(src, dst.getName());
    assertTrue(fs.exists(tmp) && fs.exists(dst));
    assertTrue(FileUtil.copy(fs, dst, fs, src, false, true, conf));
    assertTrue(fs.delete(tmp, true));
    fs.mkdirs(tmp);
    try {
      FileUtil.copy(fs, dst, fs, src, true, true, conf);
      fail("Failed to detect existing dir");
    } catch (IOException e) { }
  }

  public void testHomeDirectory() throws IOException {
    Configuration conf = new Configuration();
    FileSystem fileSys = FileSystem.getLocal(conf);
    Path home = new Path(System.getProperty("user.home"))
      .makeQualified(fileSys);
    Path fsHome = fileSys.getHomeDirectory();
    assertEquals(home, fsHome);
  }

  public void testPathEscapes() throws IOException {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.getLocal(conf);
    Path path = new Path(TEST_ROOT_DIR, "foo%bar");
    writeFile(fs, path);
    FileStatus status = fs.getFileStatus(path);
    assertEquals(path.makeQualified(fs), status.getPath());
    cleanupFile(fs, path);
  }
  
  public void testGetCanonicalServiceName() throws IOException {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.getLocal(conf);
    assertNull(fs.getCanonicalServiceName());
  }
}
