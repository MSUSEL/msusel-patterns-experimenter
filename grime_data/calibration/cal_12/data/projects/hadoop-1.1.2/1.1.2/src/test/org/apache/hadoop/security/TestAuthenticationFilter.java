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


import junit.framework.TestCase;
import org.apache.hadoop.security.authentication.server.AuthenticationFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.http.FilterContainer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

public class TestAuthenticationFilter extends TestCase {

  @SuppressWarnings("unchecked")
  public void testConfiguration() throws Exception {
    Configuration conf = new Configuration();
    conf.set("hadoop.http.authentication.foo", "bar");

    File testDir = new File(System.getProperty("test.build.data", "/tmp"));
    testDir.mkdirs();
    File secretFile = new File(testDir, "http-secret.txt");
    Writer writer = new FileWriter(new File(testDir, "http-secret.txt"));
    writer.write("hadoop");
    writer.close();
    conf.set(AuthenticationFilterInitializer.PREFIX +
             AuthenticationFilterInitializer.SIGNATURE_SECRET_FILE,
             secretFile.getAbsolutePath());

    FilterContainer container = Mockito.mock(FilterContainer.class);
    Mockito.doAnswer(
      new Answer() {
        public Object answer(InvocationOnMock invocationOnMock)
          throws Throwable {
          Object[] args = invocationOnMock.getArguments();

          assertEquals("authentication", args[0]);

          assertEquals(AuthenticationFilter.class.getName(), args[1]);

          Map<String, String> conf = (Map<String, String>) args[2];
          assertEquals("/", conf.get("cookie.path"));

          assertEquals("simple", conf.get("type"));
          assertEquals("36000", conf.get("token.validity"));
          assertEquals("hadoop", conf.get("signature.secret"));
          assertNull(conf.get("cookie.domain"));
          assertEquals("true", conf.get("simple.anonymous.allowed"));
          assertEquals("HTTP/localhost@LOCALHOST",
                       conf.get("kerberos.principal"));
          assertEquals(System.getProperty("user.home") +
                       "/hadoop.keytab", conf.get("kerberos.keytab"));
          assertEquals("bar", conf.get("foo"));

          return null;
        }
      }
    ).when(container).addFilter(Mockito.<String>anyObject(),
                                Mockito.<String>anyObject(),
                                Mockito.<Map<String, String>>anyObject());

    new AuthenticationFilterInitializer().initFilter(container, conf);
  }

}
