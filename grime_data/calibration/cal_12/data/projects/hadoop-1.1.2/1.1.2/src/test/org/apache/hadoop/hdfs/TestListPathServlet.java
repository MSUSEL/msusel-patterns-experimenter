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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.server.namenode.ListPathsServlet;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for {@link ListPathsServlet} that serves the URL
 * http://<namenodeaddress:httpport?/listPaths
 * 
 * This test does not use the servlet directly. Instead it is based on
 * {@link HftpFileSystem}, which uses this servlet to implement
 * {@link HftpFileSystem#listStatus(Path)} method.
 */
public class TestListPathServlet {
  private static final Configuration CONF = new Configuration();
  private static MiniDFSCluster cluster;
  private static FileSystem fs;
  private static URI hftpURI;
  private static HftpFileSystem hftpFs;
  private Random r = new Random();
  private List<String> filelist = new ArrayList<String>();

  @BeforeClass
  public static void setup() throws Exception {
    // start a cluster with single datanode
    cluster = new MiniDFSCluster(CONF, 1, true, null);
    cluster.waitActive();
    fs = cluster.getFileSystem();

    final String str = "hftp://"
        + CONF.get("dfs.http.address");
    hftpURI = new URI(str);
    hftpFs = (HftpFileSystem) FileSystem.get(hftpURI, CONF);
  }

  @AfterClass
  public static void teardown() {
    cluster.shutdown();
  }

  /** create a file with a length of <code>fileLen</code> */
  private void createFile(String fileName, long fileLen) throws IOException {
    filelist.add(hftpURI + fileName);
    final Path filePath = new Path(fileName);
    DFSTestUtil.createFile(fs, filePath, fileLen, (short) 1, r.nextLong());
  }

  private void mkdirs(String dirName) throws IOException {
    filelist.add(hftpURI + dirName);
    fs.mkdirs(new Path(dirName));
  }

  @Test
  public void testListStatus() throws Exception {
    // Empty root directory
    checkStatus("/");

    // Root directory with files and directories
    createFile("/a", 1);
    createFile("/b", 1);
    mkdirs("/dir");

    checkFile(new Path("/a"));
    checkFile(new Path("/b"));
    checkStatus("/");

    // A directory with files and directories
    createFile("/dir/.a.crc", 1);
    createFile("/dir/b", 1);
    mkdirs("/dir/dir1");
    
    checkFile(new Path("/dir/.a.crc"));
    checkFile(new Path("/dir/b"));
    checkStatus("/dir");

    // Non existent path
    checkStatus("/nonexistent");
    checkStatus("/nonexistent/a");

    final String username = UserGroupInformation.getCurrentUser().getShortUserName() + "1";
    final HftpFileSystem hftp2 = cluster.getHftpFileSystemAs(username, CONF, "somegroup");
    { //test file not found on hftp 
      final Path nonexistent = new Path("/nonexistent");
      try {
        hftp2.getFileStatus(nonexistent);
        Assert.fail();
      } catch(IOException ioe) {
        FileSystem.LOG.info("GOOD: getting an exception", ioe);
      }
    }

    { //test permission error on hftp
      final Path dir = new Path("/dir");
      fs.setPermission(dir, new FsPermission((short)0));
      try {
        hftp2.getFileStatus(new Path(dir, "a"));
        Assert.fail();
      } catch(IOException ioe) {
        FileSystem.LOG.info("GOOD: getting an exception", ioe);
      }
    }
  }

  private void checkStatus(String listdir) throws IOException {
    final Path listpath = hftpFs.makeQualified(new Path(listdir));
    listdir = listpath.toString();
    final FileStatus[] statuslist = hftpFs.listStatus(listpath);
    for (String directory : filelist) {
      System.out.println("dir:" + directory);
    }
    for (String file : filelist) {
      System.out.println("file:" + file);
    }
    for (FileStatus status : statuslist) {
      System.out.println("status:" + status.getPath().toString() + " type "
          + (status.isDir() ? "directory" : "file"));
    }
    for (String file : filelist) {
      boolean found = false;
      // Consider only file under the list path
      if (!file.startsWith(listpath.toString()) ||
          file.equals(listpath.toString())) {
        continue;
      }
      for (FileStatus status : statuslist) {
        if (status.getPath().toString().equals(file)) {
          found = true;
          break;
        }
      }
      Assert.assertTrue("Directory/file not returned in list status " + file,
          found);
    }
  }

  private void checkFile(final Path f) throws IOException {
    final Path hdfspath = fs.makeQualified(f);
    final FileStatus hdfsstatus = fs.getFileStatus(hdfspath);
    FileSystem.LOG.info("hdfspath=" + hdfspath);

    final Path hftppath = hftpFs.makeQualified(f);
    final FileStatus hftpstatus = hftpFs.getFileStatus(hftppath);
    FileSystem.LOG.info("hftppath=" + hftppath);
    
    Assert.assertEquals(hdfspath.toUri().getPath(),
        hdfsstatus.getPath().toUri().getPath());
    checkFileStatus(hdfsstatus, hftpstatus);
  }

  private static void checkFileStatus(final FileStatus expected,
      final FileStatus computed) {
    Assert.assertEquals(expected.getPath().toUri().getPath(),
        computed.getPath().toUri().getPath());

// TODO: test will fail if the following is un-commented. 
//    Assert.assertEquals(expected.getAccessTime(), computed.getAccessTime());
//    Assert.assertEquals(expected.getModificationTime(),
//        computed.getModificationTime());

    Assert.assertEquals(expected.getBlockSize(), computed.getBlockSize());
    Assert.assertEquals(expected.getGroup(), computed.getGroup());
    Assert.assertEquals(expected.getLen(), computed.getLen());
    Assert.assertEquals(expected.getOwner(), computed.getOwner());
    Assert.assertEquals(expected.getPermission(), computed.getPermission());
    Assert.assertEquals(expected.getReplication(), computed.getReplication());
  }
}
