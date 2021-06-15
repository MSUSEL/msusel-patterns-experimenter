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

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.security.authentication.util.KerberosUtil;
import org.junit.Test;

public class TestKerberosUtil {
  public static final Log LOG = LogFactory.getLog(TestKerberosUtil.class);

  @Test
  public void testGetServerPrincipal() throws IOException {
    String service = "TestKerberosUtil";
    String localHostname = SecurityUtil.getLocalHostName();
    String testHost = "FooBar";
    
    // send null hostname
    assertEquals("When no hostname is sent",
        service + "/" + localHostname.toLowerCase(),
        KerberosUtil.getServicePrincipal(service, null));
    // send empty hostname
    assertEquals("When empty hostname is sent",
        service + "/" + localHostname.toLowerCase(),
        KerberosUtil.getServicePrincipal(service, ""));
    // send 0.0.0.0 hostname
    assertEquals("When 0.0.0.0 hostname is sent",
        service + "/" + localHostname.toLowerCase(),
        KerberosUtil.getServicePrincipal(service, "0.0.0.0"));
    // send uppercase hostname
    assertEquals("When uppercase hostname is sent",
        service + "/" + testHost.toLowerCase(),
        KerberosUtil.getServicePrincipal(service, testHost));
    // send lowercase hostname
    assertEquals("When lowercase hostname is sent",
        service + "/" + testHost.toLowerCase(),
        KerberosUtil.getServicePrincipal(service, testHost.toLowerCase()));
  }
}