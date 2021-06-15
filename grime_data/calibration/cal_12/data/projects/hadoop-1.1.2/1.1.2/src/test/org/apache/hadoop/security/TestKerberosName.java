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
package org.apache.hadoop.security;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestKerberosName {

  @Before
  public void setUp() throws Exception {
    Configuration conf = new Configuration();
    conf.set("hadoop.security.auth_to_local", 
             ("RULE:[1:$1@$0](.*@YAHOO\\.COM)s/@.*//\n" +
              "RULE:[2:$1](johndoe)s/^.*$/guest/\n" +
              "RULE:[2:$1;$2](^.*;admin$)s/;admin$//\n" +
              "RULE:[2:$2](root)\n" +
              "DEFAULT"));
    conf.set("hadoop.security.authentication", "kerberos");
    KerberosName.setConfiguration(conf);
    KerberosName.printRules();
  }

  private void checkTranslation(String from, String to) throws Exception {
    System.out.println("Translate " + from);
    KerberosName nm = new KerberosName(from);
    String simple = nm.getShortName();
    System.out.println("to " + simple);
    assertEquals("short name incorrect", to, simple);
  }

  @Test
  public void testRules() throws Exception {
    checkTranslation("omalley@APACHE.ORG", "omalley");
    checkTranslation("hdfs/10.0.0.1@APACHE.ORG", "hdfs");
    checkTranslation("oom@YAHOO.COM", "oom");
    checkTranslation("johndoe/zoo@FOO.COM", "guest");
    checkTranslation("joe/admin@FOO.COM", "joe");
    checkTranslation("joe/root@FOO.COM", "root");
  }
  
  private void checkBadName(String name) {
    System.out.println("Checking " + name + " to ensure it is bad.");
    try {
      new KerberosName(name);
      fail("didn't get exception for " + name);
    } catch (IllegalArgumentException iae) {
      // PASS
    }
  }
  
  private void checkBadTranslation(String from) {
    System.out.println("Checking bad translation for " + from);
    KerberosName nm = new KerberosName(from);
    try {
      nm.getShortName();
      fail("didn't get exception for " + from);
    } catch (IOException ie) {
      // PASS
    }
  }
  
  @Test
  public void testAntiPatterns() throws Exception {
    checkBadName("owen/owen/owen@FOO.COM");
    checkBadName("owen@foo/bar.com");
    checkBadTranslation("foo@ACME.COM");
    checkBadTranslation("root/joe@FOO.COM");
  }
}
