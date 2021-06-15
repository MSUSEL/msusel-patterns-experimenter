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

import junit.framework.TestCase;
import java.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;

/**
 * test for the input truncation bug when mark/reset is used.
 * HADOOP-1489
 */
public class TestTruncatedInputBug extends TestCase {
  private static String TEST_ROOT_DIR =
    new Path(System.getProperty("test.build.data","/tmp"))
    .toString().replace(' ', '+');
  
  private void writeFile(FileSystem fileSys, 
                         Path name, int nBytesToWrite) 
    throws IOException {
    DataOutputStream out = fileSys.create(name);
    for (int i = 0; i < nBytesToWrite; ++i) {
      out.writeByte(0);
    }
    out.close();
  }
  
  /**
   * When mark() is used on BufferedInputStream, the request
   * size on the checksum file system can be small.  However,
   * checksum file system currently depends on the request size
   * >= bytesPerSum to work properly.
   */
  public void testTruncatedInputBug() throws IOException {
    final int ioBufSize = 512;
    final int fileSize = ioBufSize*4;
    int filePos = 0;

    Configuration conf = new Configuration();
    conf.setInt("io.file.buffer.size", ioBufSize);
    FileSystem fileSys = FileSystem.getLocal(conf);

    try {
      // First create a test input file.
      Path testFile = new Path(TEST_ROOT_DIR, "HADOOP-1489");
      writeFile(fileSys, testFile, fileSize);
      assertTrue(fileSys.exists(testFile));
      assertTrue(fileSys.getLength(testFile) == fileSize);

      // Now read the file for ioBufSize bytes
      FSDataInputStream in = fileSys.open(testFile, ioBufSize);
      // seek beyond data buffered by open
      filePos += ioBufSize * 2 + (ioBufSize - 10);  
      in.seek(filePos);

      // read 4 more bytes before marking
      for (int i = 0; i < 4; ++i) {  
        if (in.read() == -1) {
          break;
        }
        ++filePos;
      }

      // Now set mark() to trigger the bug
      // NOTE: in the fixed code, mark() does nothing (not supported) and
      //   hence won't trigger this bug.
      in.mark(1);
      System.out.println("MARKED");
      
      // Try to read the rest
      while (filePos < fileSize) {
        if (in.read() == -1) {
          break;
        }
        ++filePos;
      }
      in.close();

      System.out.println("Read " + filePos + " bytes."
                         + " file size=" + fileSize);
      assertTrue(filePos == fileSize);

    } finally {
      try {
        fileSys.close();
      } catch (Exception e) {
        // noop
      }
    }
  }  // end testTruncatedInputBug
}
