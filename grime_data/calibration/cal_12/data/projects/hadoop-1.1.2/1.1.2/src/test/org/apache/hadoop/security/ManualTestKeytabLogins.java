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

import org.apache.hadoop.security.UserGroupInformation;
import static org.junit.Assert.assertTrue;

/**
 * Regression test for HADOOP-6947 which can be run manually in
 * a kerberos environment.
 *
 * To run this test, set up two keytabs, each with a different principal.
 * Then run something like:
 *  <code>
 *  HADOOP_CLASSPATH=build/test/classes bin/hadoop \
 *     org.apache.hadoop.security.ManualTestKeytabLogins \
 *     usera/test@REALM  /path/to/usera-keytab \
 *     userb/test@REALM  /path/to/userb-keytab
 *  </code>
 */
public class ManualTestKeytabLogins {

  public static void main(String []args) throws Exception {
    if (args.length != 4) {
      System.err.println(
        "usage: ManualTestKeytabLogins <principal 1> <keytab 1> <principal 2> <keytab 2>");
      System.exit(1);
    }

    UserGroupInformation ugi1 =
      UserGroupInformation.loginUserFromKeytabAndReturnUGI(
        args[0], args[1]);
    System.out.println("UGI 1 = " + ugi1);
    assertTrue(ugi1.getUserName().equals(args[0]));
    
    UserGroupInformation ugi2 =
      UserGroupInformation.loginUserFromKeytabAndReturnUGI(
        args[2], args[3]);
    System.out.println("UGI 2 = " + ugi2);
    assertTrue(ugi2.getUserName().equals(args[2]));
  }
}
