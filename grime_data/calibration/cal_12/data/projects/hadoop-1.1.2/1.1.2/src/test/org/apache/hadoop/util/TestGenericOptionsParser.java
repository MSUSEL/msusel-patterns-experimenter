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
package org.apache.hadoop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class TestGenericOptionsParser extends TestCase {
  File testDir;
  Configuration conf;
  FileSystem localFs;
      
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    conf = new Configuration();
    localFs = FileSystem.getLocal(conf);
    testDir = new File(System.getProperty("test.build.data", "/tmp"), "generic");
    if(testDir.exists())
      localFs.delete(new Path(testDir.toString()), true);
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    if(testDir.exists()) {
      localFs.delete(new Path(testDir.toString()), true);
    }
  }

  /**
   * testing -fileCache option
   * @throws IOException
   */
  public void testTokenCacheOption() throws IOException {
    FileSystem localFs = FileSystem.getLocal(conf);
    
    File tmpFile = new File(testDir, "tokenCacheFile");
    if(tmpFile.exists()) {
      tmpFile.delete();
    }
    String[] args = new String[2];
    // pass a files option 
    args[0] = "-tokenCacheFile";
    args[1] = tmpFile.toString();
    
    // test non existing file
    Throwable th = null;
    try {
      new GenericOptionsParser(conf, args);
    } catch (Exception e) {
      th = e;
    }
    assertNotNull(th);
    
    // create file
    Path tmpPath = new Path(tmpFile.toString());
    localFs.create(tmpPath);
    new GenericOptionsParser(conf, args);
    String fileName = conf.get("mapreduce.job.credentials.json");
    assertNotNull("files is null", fileName);
    assertEquals("files option does not match",
      localFs.makeQualified(tmpPath).toString(), fileName);
    
    localFs.delete(new Path(testDir.getAbsolutePath()), true);
  }
  
  public void testFilesOption() throws Exception {
    File tmpFile = new File(testDir, "tmpfile");
    Path tmpPath = new Path(tmpFile.toString());
    localFs.create(tmpPath);
    String[] args = new String[2];
    // pass a files option 
    args[0] = "-files";
    args[1] = tmpFile.toString();
    new GenericOptionsParser(conf, args);
    String files = conf.get("tmpfiles");
    assertNotNull("files is null", files);
    assertEquals("files option does not match",
      localFs.makeQualified(tmpPath).toString(), files);
    
    // pass file as uri
    Configuration conf1 = new Configuration();
    URI tmpURI = new URI(tmpFile.toString() + "#link");
    args[0] = "-files";
    args[1] = tmpURI.toString();
    new GenericOptionsParser(conf1, args);
    files = conf1.get("tmpfiles");
    assertNotNull("files is null", files);
    assertEquals("files option does not match", 
      localFs.makeQualified(new Path(tmpURI)).toString(), files);
   
    // pass a file that does not exist.
    // GenericOptionParser should throw exception
    Configuration conf2 = new Configuration();
    args[0] = "-files";
    args[1] = "file:///xyz.txt";
    Throwable th = null;
    try {
      new GenericOptionsParser(conf2, args);
    } catch (Exception e) {
      th = e;
    }
    assertNotNull("throwable is null", th);
    assertTrue("FileNotFoundException is not thrown",
      th instanceof FileNotFoundException);
    files = conf2.get("tmpfiles");
    assertNull("files is not null", files);
  }

}
