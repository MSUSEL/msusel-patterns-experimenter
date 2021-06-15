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
package org.apache.hadoop.streaming;

import java.io.*;
import java.net.InetAddress;
import java.util.*;

import org.apache.hadoop.io.IOUtils;

/**
 * This is a class used to get the current environment
 * on the host machines running the map/reduce. This class
 * assumes that setting the environment in streaming is 
 * allowed on windows/ix/linuz/freebsd/sunos/solaris/hp-ux
 */
public class Environment extends Properties {

  public Environment() throws IOException {
    // Extend this code to fit all operating
    // environments that you expect to run in
    // http://lopica.sourceforge.net/os.html
    String command = null;
    String OS = System.getProperty("os.name");
    String lowerOs = OS.toLowerCase();
    if (OS.indexOf("Windows") > -1) {
      command = "cmd /C set";
    } else if (lowerOs.indexOf("ix") > -1 || lowerOs.indexOf("linux") > -1
               || lowerOs.indexOf("freebsd") > -1 || lowerOs.indexOf("sunos") > -1
               || lowerOs.indexOf("solaris") > -1 || lowerOs.indexOf("hp-ux") > -1) {
      command = "env";
    } else if (lowerOs.startsWith("mac os x") || lowerOs.startsWith("darwin")) {
      command = "env";
    } else {
      // Add others here
    }

    if (command == null) {
      throw new RuntimeException("Operating system " + OS + " not supported by this class");
    }

    // Read the environment variables

    Process pid = Runtime.getRuntime().exec(command);
    BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
    try {
      while (true) {
        String line = in.readLine();
        if (line == null)
          break;
        int p = line.indexOf("=");
        if (p != -1) {
          String name = line.substring(0, p);
          String value = line.substring(p + 1);
          setProperty(name, value);
        }
      }
      in.close();
      in = null;
    } finally {
      IOUtils.closeStream(in);
    }
    try {
      pid.waitFor();
    } catch (InterruptedException e) {
      throw new IOException(e.getMessage());
    }
  }

  // to be used with Runtime.exec(String[] cmdarray, String[] envp) 
  String[] toArray() {
    String[] arr = new String[super.size()];
    Enumeration it = super.keys();
    int i = -1;
    while (it.hasMoreElements()) {
      String key = (String) it.nextElement();
      String val = (String) get(key);
      i++;
      arr[i] = key + "=" + val;
    }
    return arr;
  }

  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<String, String>();
    Enumeration<Object> it = super.keys();
    while (it.hasMoreElements()) {
      String key = (String) it.nextElement();
      String val = (String) get(key);
      map.put(key, val);
    }
    return map;
  }
  
  public String getHost() {
    String host = getProperty("HOST");
    if (host == null) {
      // HOST isn't always in the environment
      try {
        host = InetAddress.getLocalHost().getHostName();
      } catch (IOException io) {
        io.printStackTrace();
      }
    }
    return host;
  }

}
