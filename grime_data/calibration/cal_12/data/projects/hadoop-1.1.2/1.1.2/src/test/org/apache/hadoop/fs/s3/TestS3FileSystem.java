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
package org.apache.hadoop.fs.s3;

import java.io.IOException;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;

public class TestS3FileSystem extends TestCase {

  public void testInitialization() throws IOException {
    initializationTest("s3://a:b@c", "s3://a:b@c");
    initializationTest("s3://a:b@c/", "s3://a:b@c");
    initializationTest("s3://a:b@c/path", "s3://a:b@c");
    initializationTest("s3://a@c", "s3://a@c");
    initializationTest("s3://a@c/", "s3://a@c");
    initializationTest("s3://a@c/path", "s3://a@c");
    initializationTest("s3://c", "s3://c");
    initializationTest("s3://c/", "s3://c");
    initializationTest("s3://c/path", "s3://c");
  }
  
  private void initializationTest(String initializationUri, String expectedUri)
    throws IOException {
    
    S3FileSystem fs = new S3FileSystem(new InMemoryFileSystemStore());
    fs.initialize(URI.create(initializationUri), new Configuration());
    assertEquals(URI.create(expectedUri), fs.getUri());
  }

}
