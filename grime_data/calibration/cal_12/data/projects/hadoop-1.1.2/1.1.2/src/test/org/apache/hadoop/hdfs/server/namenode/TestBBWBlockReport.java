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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.FSConstants.SafeModeAction;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class TestBBWBlockReport {

  private final Path src = new Path(System.getProperty("test.build.data",
      "/tmp"), "testfile");

  private Configuration conf = null;

  private final String fileContent = "PartialBlockReadTest";

  @Before
  public void setUp() {
    conf = new Configuration();
    conf.setInt("ipc.client.connection.maxidletime", 1000);
  }

  @Test(timeout = 60000)
  // timeout is mainly for safe mode
  public void testDNShouldSendBBWReport() throws Exception {
    FileSystem fileSystem = null;
    FSDataOutputStream outStream = null;
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 1, true, null);
    cluster.waitActive();
    try {
      fileSystem = cluster.getFileSystem();
      // Keep open stream
      outStream = writeFileAndSync(fileSystem, src, fileContent);
      // Parameter true will ensure that NN came out of safemode
      cluster.restartNameNode();
      assertEquals(
          "Not able to read the synced block content after NameNode restart (with append support)",
          fileContent, getFileContentFromDFS(fileSystem));
    } finally {
      if (null != fileSystem)
        fileSystem.close();
      if (null != outStream)
        outStream.close();
      cluster.shutdown();
    }
  }

  private String getFileContentFromDFS(FileSystem fs) throws IOException {
    ByteArrayOutputStream bio = new ByteArrayOutputStream();
    IOUtils.copyBytes(fs.open(src), bio, conf, true);
    return new String(bio.toByteArray());
  }

  private FSDataOutputStream writeFileAndSync(FileSystem fs, Path src,
      String fileContent) throws IOException {
    FSDataOutputStream fo = fs.create(src);
    fo.writeBytes(fileContent);
    fo.sync();
    return fo;
  }
}
