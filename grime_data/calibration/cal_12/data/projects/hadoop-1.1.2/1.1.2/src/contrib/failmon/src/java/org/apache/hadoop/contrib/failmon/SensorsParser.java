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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************************************************
 * Objects of this class parse the output of the lm-sensors utility 
 * to gather information about fan speed, temperatures for cpus
 * and motherboard etc.
 *
 **********************************************************/

public class SensorsParser extends ShellParser {

  /**
   * Reads and parses the output of the 'sensors' command 
   * and creates an appropriate EventRecord that holds 
   * the desirable information.
   * 
   * @param s unused parameter
   * 
   * @return the EventRecord created
   */
  public EventRecord query(String s) throws Exception {
    StringBuffer sb;

    //sb = Environment.runCommand("sensors -A");
     sb = Environment.runCommand("cat sensors.out");

    EventRecord retval = new EventRecord(InetAddress.getLocalHost()
        .getCanonicalHostName(), InetAddress.getAllByName(InetAddress.getLocalHost()
        .getHostName()), Calendar.getInstance(), "lm-sensors", "Unknown",
        "sensors -A", "-");
    readGroup(retval, sb, "fan");
    readGroup(retval, sb, "in");
    readGroup(retval, sb, "temp");
    readGroup(retval, sb, "Core");

    return retval;
  }

  /**
   * Reads and parses lines that provide the output
   * of a group of sensors with the same functionality.
   * 
   * @param er the EventRecord to which the new attributes are added
   * @param sb the text to parse
   * @param prefix a String prefix specifying the common prefix of the
   * sensors' names in the group (e.g. "fan", "in", "temp"
   * 
   * @return the EventRecord created
   */
  private EventRecord readGroup(EventRecord er, StringBuffer sb, String prefix) {

    Pattern pattern = Pattern.compile(".*(" + prefix
        + "\\s*\\d*)\\s*:\\s*(\\+?\\d+)", Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(sb);

    while (matcher.find())
      er.set(matcher.group(1), matcher.group(2));

    return er;
  }

  /**
   * Invokes query() to do the parsing and handles parsing errors. 
   * 
   * @return an array of EventRecords that holds one element that represents
   * the current state of the hardware sensors
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
    return ("lm-sensors parser");
  }

}
