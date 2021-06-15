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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/** This test makes sure that "DU" does not get to run on each call to getUsed */ 
public class TestDU extends TestCase {
  final static private File DU_DIR = new File(
      System.getProperty("test.build.data","/tmp"), "dutmp");

  public void setUp() throws IOException {
      FileUtil.fullyDelete(DU_DIR);
      assertTrue(DU_DIR.mkdirs());
  }

  public void tearDown() throws IOException {
      FileUtil.fullyDelete(DU_DIR);
  }
    
  private void createFile(File newFile, int size) throws IOException {
    // write random data so that filesystems with compression enabled (e.g., ZFS)
    // can't compress the file
    Random random = new Random();
    byte[] data = new byte[size];
    random.nextBytes(data);

    newFile.createNewFile();
    RandomAccessFile file = new RandomAccessFile(newFile, "rws");

    file.write(data);
      
    file.getFD().sync();
    file.close();
  }

  /**
   * Verify that du returns expected used space for a file.
   * We assume here that if a file system crates a file of size 
   * that is a multiple of the block size in this file system,
   * then the used size for the file will be exactly that size.
   * This is true for most file systems.
   * 
   * @throws IOException
   * @throws InterruptedException
   */
  public void testDU() throws IOException, InterruptedException {
    final int writtenSize = 32*1024;   // writing 32K
    // Allow for extra 4K on-disk slack for local file systems
    // that may store additional file metadata (eg ext attrs).
    final int slack = 4*1024;
    File file = new File(DU_DIR, "data");
    createFile(file, writtenSize);

    Thread.sleep(5000); // let the metadata updater catch up
    
    DU du = new DU(file, 10000);
    du.start();
    long duSize = du.getUsed();
    du.shutdown();

    assertTrue("Invalid on-disk size",
        duSize >= writtenSize &&
        writtenSize <= (duSize + slack));
    
    //test with 0 interval, will not launch thread 
    du = new DU(file, 0);
    du.start();
    duSize = du.getUsed();
    du.shutdown();
    
    assertTrue("Invalid on-disk size",
        duSize >= writtenSize &&
        writtenSize <= (duSize + slack));
    
    //test without launching thread 
    du = new DU(file, 10000);
    duSize = du.getUsed();

    assertTrue("Invalid on-disk size",
        duSize >= writtenSize &&
        writtenSize <= (duSize + slack));
  }
}
