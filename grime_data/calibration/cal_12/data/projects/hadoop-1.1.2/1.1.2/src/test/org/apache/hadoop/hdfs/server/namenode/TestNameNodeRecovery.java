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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.List;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.common.HdfsConstants.StartupOption;
import org.apache.hadoop.hdfs.server.namenode.FSImage.NameNodeFile;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

/**
 * This tests data recovery mode for the NameNode.
 */
public class TestNameNodeRecovery {
  private static final Log LOG = LogFactory.getLog(TestNameNodeRecovery.class);
  private static StartupOption recoverStartOpt = StartupOption.RECOVER;

  static {
    recoverStartOpt.setForce(MetaRecoveryContext.FORCE_ALL);
  }

  /** Test that we can successfully recover from a situation where the last
   * entry in the edit log has been truncated. */
  @Test(timeout=180000)
  public void testRecoverTruncatedEditLog() throws IOException {
    final String TEST_PATH = "/test/path/dir";
    final String TEST_PATH2 = "/alt/test/path";

    // Start up the mini dfs cluster
    Configuration conf = new Configuration();
    MiniDFSCluster cluster;
    cluster = new MiniDFSCluster(0, conf, 0, true, true, false,
        StartupOption.FORMAT, null, null, null);
    cluster.waitActive();
    FileSystem fileSys = cluster.getFileSystem();
    fileSys.mkdirs(new Path(TEST_PATH));
    fileSys.mkdirs(new Path(TEST_PATH2));

    List<File> nameEditsDirs =
        (List<File>)FSNamesystem.getNamespaceEditsDirs(conf);
    cluster.shutdown();

    File dir = nameEditsDirs.get(0); //has only one
    File editFile = new File(new File(dir, "current"),
        NameNodeFile.EDITS.getName());
    assertTrue("Should exist: " + editFile, editFile.exists());

    // Corrupt the last edit
    long fileLen = editFile.length();
    RandomAccessFile rwf = new RandomAccessFile(editFile, "rw");
    rwf.setLength(fileLen - 1);
    rwf.close();

    // Make sure that we can't start the cluster normally before recovery
    try {
      LOG.debug("trying to start normally (this should fail)...");
      cluster = new MiniDFSCluster(0, conf, 0, false, true, false,
          StartupOption.REGULAR, null, null, null);
      cluster.waitActive();
      fail("expected the truncated edit log to prevent normal startup");
    } catch (IOException e) {
    } finally {
      cluster.shutdown();
    }

    // Perform recovery
    try {
      LOG.debug("running recovery...");
      cluster = new MiniDFSCluster(0, conf, 0, false, true, false,
          StartupOption.RECOVER, null, null, null);
      cluster.waitActive();
    } catch (IOException e) {
      fail("caught IOException while trying to recover. " +
          "message was " + e.getMessage() +
          "\nstack trace\n" + StringUtils.stringifyException(e));
    } finally {
      cluster.shutdown();
    }

    // Make sure that we can start the cluster normally after recovery
    try {
      cluster = new MiniDFSCluster(0, conf, 0, false, true, false,
          StartupOption.REGULAR, null, null, null);
      cluster.waitActive();
      assertTrue(cluster.getFileSystem().exists(new Path(TEST_PATH)));
    } catch (IOException e) {
      fail("failed to recover.  Error message: " + e.getMessage());
    } finally {
      cluster.shutdown();
    }
    LOG.debug("testRecoverTruncatedEditLog: successfully recovered the " +
        "truncated edit log");
  }
}
