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

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.server.namenode.NameNode;

/** Test NameNode port defaulting code. */
public class TestDefaultNameNodePort {
  @Test
  public void testGetAddressFromString() throws Exception {
    assertEquals(NameNode.getAddress("foo").getPort(),
                 NameNode.DEFAULT_PORT);
    assertEquals(NameNode.getAddress("hdfs://foo/").getPort(),
                 NameNode.DEFAULT_PORT);
    assertEquals(NameNode.getAddress("hdfs://foo:555").getPort(),
                 555);
    assertEquals(NameNode.getAddress("foo:555").getPort(),
                 555);
  }

  @Test
  public void testGetAddressFromConf() throws Exception {
    Configuration conf = new Configuration();
    FileSystem.setDefaultUri(conf, "hdfs://foo/");
    assertEquals(NameNode.getAddress(conf).getPort(), NameNode.DEFAULT_PORT);
    FileSystem.setDefaultUri(conf, "hdfs://foo:555/");
    assertEquals(NameNode.getAddress(conf).getPort(), 555);
    FileSystem.setDefaultUri(conf, "foo");
    assertEquals(NameNode.getAddress(conf).getPort(), NameNode.DEFAULT_PORT);
  }

  public void testGetUri() {
    assertEquals(NameNode.getUri(new InetSocketAddress("foo", 555)),
                 URI.create("hdfs://foo:555"));
    assertEquals(NameNode.getUri(new InetSocketAddress("foo",
                                                       NameNode.DEFAULT_PORT)),
                 URI.create("hdfs://foo"));
  }

  @Test
  public void testSlashAddress() throws Exception {
    verifyBadAuthAddress("/junk");
  } 

  @Test
  public void testSlashSlashAddress() throws Exception {
    verifyBadAuthAddress("//junk");
  } 

  @Test
  public void testNoAuthAddress() throws Exception {
    // this is actually the default value if the default fs isn't configured!
    verifyBadAuthAddress("file:///");
  } 

  public void verifyBadAuthAddress(String noAuth) throws Exception {
    Configuration conf = new Configuration();
    FileSystem.setDefaultUri(conf, noAuth);
    try {
      InetSocketAddress addr = NameNode.getAddress(conf);
      // this will show what we got back in case the test fails
      assertEquals(null, addr);
    } catch (IllegalArgumentException e) {
      assertEquals(
          "Does not contain a valid host:port authority: " + noAuth,
          e.getMessage());
    }
  } 


}
