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
package org.apache.hadoop.io;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.io.nativeio.NativeIO;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assume.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestSecureIOUtils {
  private static String realOwner; 
  private static final File testFilePath =
      new File(System.getProperty("test.build.data"), "TestSecureIOContext");

  @BeforeClass
  public static void makeTestFile() throws Exception {
    FileOutputStream fos = new FileOutputStream(testFilePath);
    fos.write("hello".getBytes("UTF-8"));
    fos.close();

    Configuration conf = new Configuration();
    FileSystem rawFS = FileSystem.getLocal(conf).getRaw();
    FileStatus stat = rawFS.getFileStatus(
      new Path(testFilePath.toString()));
    realOwner = stat.getOwner();
  }

  @Test
  public void testReadUnrestricted() throws IOException {
    SecureIOUtils.openForRead(testFilePath, null).close();
  }

  @Test
  public void testReadCorrectlyRestrictedWithSecurity() throws IOException {
    SecureIOUtils
      .openForRead(testFilePath, realOwner).close();
  }

  @Test
  public void testReadIncorrectlyRestrictedWithSecurity() throws IOException {
    try {
      SecureIOUtils
        .openForRead(testFilePath, "invalidUser").close();
      fail("Didn't throw expection for wrong ownership!");
    } catch (IOException ioe) {
      // expected
    }
  }

  @Test
  public void testCreateForWrite() throws IOException {
    try {
      SecureIOUtils.createForWrite(testFilePath, 0777);
      fail("Was able to create file at " + testFilePath);
    } catch (SecureIOUtils.AlreadyExistsException aee) {
      // expected
    }
  }
}
