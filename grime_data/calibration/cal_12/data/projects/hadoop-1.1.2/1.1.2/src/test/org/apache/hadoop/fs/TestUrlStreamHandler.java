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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;

/**
 * Test of the URL stream handler factory.
 */
public class TestUrlStreamHandler extends TestCase {

  /**
   * Test opening and reading from an InputStream through a hdfs:// URL.
   * <p>
   * First generate a file with some content through the FileSystem API, then
   * try to open and read the file through the URL stream API.
   * 
   * @throws IOException
   */
  public void testDfsUrls() throws IOException {

    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 2, true, null);
    FileSystem fs = cluster.getFileSystem();

    // Setup our own factory
    // setURLSteramHandlerFactor is can be set at most once in the JVM
    // the new URLStreamHandler is valid for all tests cases 
    // in TestStreamHandler
    FsUrlStreamHandlerFactory factory =
        new org.apache.hadoop.fs.FsUrlStreamHandlerFactory();
    java.net.URL.setURLStreamHandlerFactory(factory);

    Path filePath = new Path("/thefile");

    try {
      byte[] fileContent = new byte[1024];
      for (int i = 0; i < fileContent.length; ++i)
        fileContent[i] = (byte) i;

      // First create the file through the FileSystem API
      OutputStream os = fs.create(filePath);
      os.write(fileContent);
      os.close();

      // Second, open and read the file content through the URL API
      URI uri = fs.getUri();
      URL fileURL =
          new URL(uri.getScheme(), uri.getHost(), uri.getPort(), filePath
              .toString());

      InputStream is = fileURL.openStream();
      assertNotNull(is);

      byte[] bytes = new byte[4096];
      assertEquals(1024, is.read(bytes));
      is.close();

      for (int i = 0; i < fileContent.length; ++i)
        assertEquals(fileContent[i], bytes[i]);

      // Cleanup: delete the file
      fs.delete(filePath, false);

    } finally {
      fs.close();
      cluster.shutdown();
    }

  }

  /**
   * Test opening and reading from an InputStream through a file:// URL.
   * 
   * @throws IOException
   * @throws URISyntaxException
   */
  public void testFileUrls() throws IOException, URISyntaxException {
    // URLStreamHandler is already set in JVM by testDfsUrls() 
    Configuration conf = new Configuration();

    // Locate the test temporary directory.
    File tmpDir = new File(conf.get("hadoop.tmp.dir"));
    if (!tmpDir.exists()) {
      if (!tmpDir.mkdirs())
        throw new IOException("Cannot create temporary directory: " + tmpDir);
    }

    File tmpFile = new File(tmpDir, "thefile");
    URI uri = tmpFile.toURI();

    FileSystem fs = FileSystem.get(uri, conf);

    try {
      byte[] fileContent = new byte[1024];
      for (int i = 0; i < fileContent.length; ++i)
        fileContent[i] = (byte) i;

      // First create the file through the FileSystem API
      OutputStream os = fs.create(new Path(uri.getPath()));
      os.write(fileContent);
      os.close();

      // Second, open and read the file content through the URL API.
      URL fileURL = uri.toURL();

      InputStream is = fileURL.openStream();
      assertNotNull(is);

      byte[] bytes = new byte[4096];
      assertEquals(1024, is.read(bytes));
      is.close();

      for (int i = 0; i < fileContent.length; ++i)
        assertEquals(fileContent[i], bytes[i]);

      // Cleanup: delete the file
      fs.delete(new Path(uri.getPath()), false);

    } finally {
      fs.close();
    }

  }

}
