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
package org.apache.hadoop.contrib.failmon;

import java.net.InetAddress;
import java.util.Calendar;

/**********************************************************
 * Objects of this class parse the /proc/cpuinfo file to 
 * gather information about present processors in the system.
 *
 **********************************************************/


public class CPUParser extends ShellParser {

 /**
  * Constructs a CPUParser
  */
  public CPUParser() {
    super();
  }

  /**
   * Reads and parses /proc/cpuinfo and creates an appropriate 
   * EventRecord that holds the desirable information.
   * 
   * @param s unused parameter
   * 
   * @return the EventRecord created
   */
  public EventRecord query(String s) throws Exception {
    StringBuffer sb = Environment.runCommand("cat /proc/cpuinfo");
    EventRecord retval = new EventRecord(InetAddress.getLocalHost()
        .getCanonicalHostName(), InetAddress.getAllByName(InetAddress.getLocalHost()
        .getHostName()), Calendar.getInstance(), "CPU", "Unknown", "CPU", "-");

    retval.set("processors", findAll("\\s*processor\\s*:\\s*(\\d+)", sb
        .toString(), 1, ", "));

    retval.set("model name", findPattern("\\s*model name\\s*:\\s*(.+)", sb
        .toString(), 1));

    retval.set("frequency", findAll("\\s*cpu\\s*MHz\\s*:\\s*(\\d+)", sb
        .toString(), 1, ", "));

    retval.set("physical id", findAll("\\s*physical\\s*id\\s*:\\s*(\\d+)", sb
        .toString(), 1, ", "));

    retval.set("core id", findAll("\\s*core\\s*id\\s*:\\s*(\\d+)", sb
        .toString(), 1, ", "));

    return retval;
  }

  /**
   * Invokes query() to do the parsing and handles parsing errors. 
   * 
   * @return an array of EventRecords that holds one element that represents
   * the current state of /proc/cpuinfo
   */
  
  public EventRecord[] monitor() {

    EventRecord[] recs = new EventRecord[1];

    try {
      recs[0] = query(null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return recs;
  }
  
  /**
   * Return a String with information about this class
   * 
   * @return A String describing this class
   */
  public String getInfo() {
    return ("CPU Info parser");
  }

}
