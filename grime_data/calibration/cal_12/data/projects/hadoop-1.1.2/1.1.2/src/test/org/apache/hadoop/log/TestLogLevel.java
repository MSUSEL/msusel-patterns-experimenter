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
/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.hadoop.log;

import java.io.*;
import java.net.*;

import org.apache.hadoop.http.HttpServer;

import junit.framework.TestCase;
import org.apache.commons.logging.*;
import org.apache.commons.logging.impl.*;
import org.apache.log4j.*;

public class TestLogLevel extends TestCase {
  static final PrintStream out = System.out;

  public void testDynamicLogLevel() throws Exception {
    String logName = TestLogLevel.class.getName();
    Log testlog = LogFactory.getLog(logName);

    //only test Log4JLogger
    if (testlog instanceof Log4JLogger) {
      Logger log = ((Log4JLogger)testlog).getLogger();
      log.debug("log.debug1");
      log.info("log.info1");
      log.error("log.error1");
      assertTrue(!Level.ERROR.equals(log.getEffectiveLevel()));

      HttpServer server = new HttpServer("..", "localhost", 22222, true);
      server.start();
      int port = server.getPort();

      //servlet
      URL url = new URL("http://localhost:" + port
          + "/logLevel?log=" + logName + "&level=" + Level.ERROR);
      out.println("*** Connecting to " + url);
      URLConnection connection = url.openConnection();
      connection.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(
          connection.getInputStream()));
      for(String line; (line = in.readLine()) != null; out.println(line));
      in.close();

      log.debug("log.debug2");
      log.info("log.info2");
      log.error("log.error2");
      assertTrue(Level.ERROR.equals(log.getEffectiveLevel()));

      //command line
      String[] args = {"-setlevel", "localhost:"+port, logName,""+Level.DEBUG};
      LogLevel.main(args);
      log.debug("log.debug3");
      log.info("log.info3");
      log.error("log.error3");
      assertTrue(Level.DEBUG.equals(log.getEffectiveLevel()));
    }
    else {
      out.println(testlog.getClass() + " not tested.");
    }
  }
}
