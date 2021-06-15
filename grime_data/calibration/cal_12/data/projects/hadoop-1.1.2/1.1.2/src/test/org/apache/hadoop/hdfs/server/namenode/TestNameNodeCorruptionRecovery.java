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
package org.apache.hadoop.hdfs.server.namenode;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSImage.NameNodeDirType;
import org.apache.hadoop.hdfs.server.namenode.FSImage.NameNodeFile;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Test;

/**
 * Test the name node's ability to recover from partially corrupted storage
 * directories.
 */
public class TestNameNodeCorruptionRecovery {

  private static final Log LOG = LogFactory.getLog(
    TestNameNodeCorruptionRecovery.class);
  
  private MiniDFSCluster cluster;
  
  @After
  public void tearDownCluster() {
    if (cluster != null) {
      cluster.shutdown();
    }
  }

  /**
   * Test that a corrupted fstime file in a single storage directory does not
   * prevent the NN from starting up.
   */
  @Test
  public void testFsTimeFileCorrupt() throws IOException, InterruptedException {
    cluster = new MiniDFSCluster(new Configuration(), 0, true, null);
    cluster.waitActive();
    assertEquals(cluster.getNameDirs().size(), 2);
    // Get the first fstime file and truncate it.
    truncateStorageDirFile(cluster, NameNodeFile.TIME, 0);
    // Make sure we can start up despite the fact the fstime file is corrupted.
    cluster.restartNameNode();
  }

  /**
   * Tests that a cluster's image is not damaged if checkpoint fails after
   * writing checkpoint time to the image directory but before writing checkpoint
   * time to the edits directory.  This is a very rare failure scenario that can
   * only occur if the namenode is configured with separate directories for image
   * and edits.  This test simulates the failure by forcing the fstime file for
   * edits to contain 0, so that it appears the checkpoint time for edits is less
   * than the checkpoint time for image.
   */
  @Test
  public void testEditsFsTimeLessThanImageFsTime() throws Exception {
    // Create a cluster with separate directories for image and edits.
    Configuration conf = new Configuration();
    File testDir = new File(System.getProperty("test.build.data",
      "build/test/data"), "dfs/");
    conf.set("dfs.name.dir", new File(testDir, "name").getPath());
    conf.set("dfs.name.edits.dir", new File(testDir, "edits").getPath());
    cluster = new MiniDFSCluster(0, conf, 1, true, false, true, null, null, null,
      null);
    cluster.waitActive();

    // Create several files to generate some edits.
    createFile("one");
    createFile("two");
    createFile("three");
    assertTrue(checkFileExists("one"));
    assertTrue(checkFileExists("two"));
    assertTrue(checkFileExists("three"));

    // Restart to force a checkpoint.
    cluster.restartNameNode();

    // Shutdown so that we can safely modify the fstime file.
    File[] editsFsTime = cluster.getNameNode().getFSImage().getFileNames(
      NameNodeFile.TIME, NameNodeDirType.EDITS);
    assertTrue("expected exactly one edits directory containing fstime file",
      editsFsTime.length == 1);
    cluster.shutdown();

    // Write 0 into the fstime file for the edits directory.
    FileOutputStream fos = null;
    DataOutputStream dos = null;
    try {
      fos = new FileOutputStream(editsFsTime[0]);
      dos = new DataOutputStream(fos);
      dos.writeLong(0);
    } finally {
      IOUtils.cleanup(LOG, dos, fos);
    }

    // Restart to force another checkpoint, which should discard the old edits.
    cluster = new MiniDFSCluster(0, conf, 1, false, false, true, null, null,
      null, null);
    cluster.waitActive();

    // Restart one more time.  If all of the prior checkpoints worked correctly,
    // then we expect to load the image successfully and find the files.
    cluster.restartNameNode();
    assertTrue(checkFileExists("one"));
    assertTrue(checkFileExists("two"));
    assertTrue(checkFileExists("three"));
  }

  /**
   * Checks that a file exists in the cluster.
   * 
   * @param file String name of file to check
   * @return boolean true if file exists
   * @throws IOException thrown if there is an I/O error
   */
  private boolean checkFileExists(String file) throws IOException {
    return cluster.getFileSystem().exists(new Path(file));
  }

  /**
   * Creates a new, empty file in the cluster.
   * 
   * @param file String name of file to create
   * @throws IOException thrown if there is an I/O error
   */
  private void createFile(String file) throws IOException {
    cluster.getFileSystem().create(new Path(file)).close();
  }

  private static void truncateStorageDirFile(MiniDFSCluster cluster,
      NameNodeFile f, int storageDirIndex) throws IOException {
    File currentDir = cluster.getNameNode().getFSImage()
        .getStorageDir(storageDirIndex).getCurrentDir();
    File nameNodeFile = new File(currentDir, f.getName());
    assertTrue(nameNodeFile.isFile());
    assertTrue(nameNodeFile.delete());
    assertTrue(nameNodeFile.createNewFile());
  }
}
