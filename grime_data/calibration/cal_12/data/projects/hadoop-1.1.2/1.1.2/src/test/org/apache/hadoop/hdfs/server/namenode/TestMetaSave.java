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

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.Random;
import static org.junit.Assert.assertTrue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.fs.FSDataOutputStream;

/**
 * This class tests the creation and validation of metasave
 */
public class TestMetaSave {
  static final int NUM_DATA_NODES = 2;
  static final long seed = 0xDEADBEEFL;
  static final int blockSize = 8192;
  private static MiniDFSCluster cluster = null;
  private static FileSystem fileSys = null;

  private void createFile(FileSystem fileSys, Path name) throws IOException {
    FSDataOutputStream stm = fileSys.create(name, true, fileSys.getConf()
        .getInt("io.file.buffer.size", 4096), (short) 2, (long) blockSize);
    byte[] buffer = new byte[1024];
    Random rand = new Random(seed);
    rand.nextBytes(buffer);
    stm.write(buffer);
    stm.close();
  }

  @BeforeClass
  public static void setUp() throws IOException {
    // start a cluster
    Configuration conf = new Configuration();

    // High value of replication interval
    // so that blocks remain under-replicated
    conf.setInt("dfs.replication.interval", 1000);
    conf.setLong("dfs.heartbeat.interval", 1L);
    conf.setLong("heartbeat.recheck.interval", 1L);
    cluster = new MiniDFSCluster(conf, NUM_DATA_NODES, true, null);
    cluster.waitActive();
    fileSys = cluster.getFileSystem();
  }

  /**
   * Tests metasave
   */
  @Test
  public void testMetaSave() throws IOException, InterruptedException {

    final FSNamesystem namesystem = cluster.getNameNode().getNamesystem();

    for (int i = 0; i < 2; i++) {
      Path file = new Path("/filestatus" + i);
      createFile(fileSys, file);
    }

    cluster.stopDataNode(1);
    // wait for namenode to discover that a datanode is dead
    Thread.sleep(15000);
    namesystem.setReplication("/filestatus0", (short) 4);

    namesystem.metaSave("metasave.out.txt");

    // Verification
    String logFile = System.getProperty("hadoop.log.dir") + "/"
        + "metasave.out.txt";
    FileInputStream fstream = new FileInputStream(logFile);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = reader.readLine();
    assertTrue(line.equals("3 files and directories, 2 blocks = 5 total"));
    line = reader.readLine();
    assertTrue(line.equals("Live Datanodes: 1"));
    line = reader.readLine();
    assertTrue(line.equals("Dead Datanodes: 1"));
    line = reader.readLine();
    line = reader.readLine();
    assertTrue(line.matches("^/filestatus[01]:.*"));
  }

  @AfterClass
  public static void tearDown() throws IOException {
    if (fileSys != null)
      fileSys.close();
    if (cluster != null)
      cluster.shutdown();
  }
}
