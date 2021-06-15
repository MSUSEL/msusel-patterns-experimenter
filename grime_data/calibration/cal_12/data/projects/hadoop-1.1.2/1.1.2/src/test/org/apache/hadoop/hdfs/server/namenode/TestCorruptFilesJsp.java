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

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.TestDatanodeBlockScanner;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem.CorruptFileBlockInfo;
import org.junit.Test;

/** A JUnit test for corrupt_files.jsp */
public class TestCorruptFilesJsp  {

  @Test
  public void testCorruptFilesJsp() throws Exception {
    MiniDFSCluster cluster = null;
    try {
      final int FILE_SIZE = 512;
      Path[] filepaths = { new Path("/audiobook"), new Path("/audio/audio1"),
          new Path("/audio/audio2"), new Path("/audio/audio") };

      Configuration conf = new Configuration();
      // DataNode scans directories
      conf.setInt(DFSConfigKeys.DFS_DATANODE_DIRECTORYSCAN_INTERVAL_KEY, 1);
      // DataNode sends block reports
      conf.setInt(DFSConfigKeys.DFS_BLOCKREPORT_INTERVAL_MSEC_KEY, 3 * 1000);
      cluster = new MiniDFSCluster(conf, 1, true, null);
      cluster.waitActive();

      FileSystem fs = cluster.getFileSystem();

      // create files
      for (Path filepath : filepaths) {
        DFSTestUtil.createFile(fs, filepath, FILE_SIZE, (short) 1, 0L);
        DFSTestUtil.waitReplication(fs, filepath, (short) 1);
      }

      // verify there are not corrupt files
      Collection<CorruptFileBlockInfo> badFiles = cluster.getNameNode()
          .getNamesystem().listCorruptFileBlocks();
      assertTrue("There are " + badFiles.size()
          + " corrupt files, but expecting none", badFiles.size() == 0);

      String nnUrl = cluster.getNameNode().getHttpAddress().getHostName() + ":"
          + cluster.getNameNode().getHttpAddress().getPort(); 
      URL url = new URL("http://" + nnUrl + "/corrupt_files.jsp");
      String corruptFilesPage = DFSTestUtil.urlGet(url);
      assertTrue("Corrupt files page is not showing a healthy filesystem",
          corruptFilesPage.contains("No missing blocks found at the moment."));

      // Now corrupt all the files except for the last one
      for (int idx = 0; idx < filepaths.length - 1; idx++) {
        String blockName = DFSTestUtil.getFirstBlock(fs, filepaths[idx])
            .getBlockName();
        TestDatanodeBlockScanner.corruptReplica(blockName, 0);

        // read the file so that the corrupt block is reported to NN
        FSDataInputStream in = fs.open(filepaths[idx]);
        try {
          in.readFully(new byte[FILE_SIZE]);
        } catch (ChecksumException ignored) { // checksum error is expected.
        }
        in.close();
      }

      // verify if all corrupt files were reported to NN
      badFiles = cluster.getNameNode().getNamesystem().listCorruptFileBlocks();
      assertTrue("Expecting 3 corrupt files, but got " + badFiles.size(),
          badFiles.size() == 3);

      corruptFilesPage = DFSTestUtil.urlGet(url);
      assertTrue("'/audiobook' should be corrupt", corruptFilesPage
          .contains("/audiobook"));
      assertTrue("'/audio/audio1' should be corrupt", corruptFilesPage
          .contains("/audio/audio1"));
      assertTrue("'/audio/audio2' should be corrupt", corruptFilesPage
          .contains("/audio/audio2"));
      assertTrue("Summary message shall report 3 corrupt files",
          corruptFilesPage.contains("At least 3 corrupt file(s)"));

      // clean up
      for (Path filepath : filepaths) {
        fs.delete(filepath, false);
      }
    } finally {
      if (cluster != null) {
        cluster.shutdown();
      }
    }
  }

}
