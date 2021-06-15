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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**********************************************************
 * Objects of this class represent metrics collected for 
 * a specific hardware source. Each EventRecord contains a HashMap of 
 * (key, value) pairs, each of which represents a property of
 * the metered value. For instance, when parsing a log file, an
 * EventRecord is created for each log entry, which contains 
 * the hostname and the ip addresses of the node, timestamp of
 * the log entry, the actual message etc. Each and every EventRecord
 * contains the hostname of the machine on which it was collected,
 * its IP address and the time of collection.
 * 
 * The main purpose of this class is to provide a uniform format
 * for records collected from various system compontents (logs,
 * ifconfig, smartmontools, lm-sensors etc). All metric values are 
 * converted into this format after they are collected by a
 * Monitored object.
 *
 **********************************************************/

public class EventRecord {

  HashMap<String, Object> fields;

  /**
   * Create the EventRecord given the most common properties
   * among different metric types.
   */
  public EventRecord(String _hostname, Object [] _ips, Calendar _timestamp,
      String _type, String _logLevel, String _source, String _message) {
    fields = new HashMap<String, Object>();
    fields.clear();
    set("hostname", _hostname);
    set("ips", _ips);
    set("timestamp", _timestamp);
    set("type", _type);
    set("logLevel", _logLevel);
    set("source", _source);
    set("message", _message);
  }

  /**
   * Create the EventRecord with no fields other than "invalid" as
   * the hostname. This is only used as a dummy.
   */
  public EventRecord() {
    // creates an invalid record
    fields = new HashMap<String, Object>();
    fields.clear();
    set("hostname", "invalid");
  }

  /**
   * Return the HashMap of properties of the EventRecord.
   * 
   * @return a HashMap that contains all properties of the record.
   */
  public final HashMap<String, Object> getMap() {
    return fields;
  }

  /**
   * Set the value of a property of the EventRecord.
   * 
   * @param fieldName the name of the property to set
   * @param fieldValue the value of the property to set
   * 
   */
  public void set(String fieldName, Object fieldValue) {
    if (fieldValue != null)
      fields.put(fieldName, fieldValue);
  }

  /**
   * Get the value of a property of the EventRecord.
   * If the property with the specific key is not found,
   * null is returned.
   * 
   * @param fieldName the name of the property to get.
   */
  public Object get(String fieldName) {
    return fields.get(fieldName);
  }

  /**
   * Check if the EventRecord is a valid one, i.e., whether
   * it represents meaningful metric values.
   * 
   * @return true if the EventRecord is a valid one, false otherwise.
   */
  public boolean isValid() {
    return !("invalid".equalsIgnoreCase((String) fields.get("hostname")));
  }

  /**
   * Creates and returns a string representation of the object.
   * 
   * @return a String representation of the object
   */

  public String toString() {
    String retval = "";
    ArrayList<String> keys = new ArrayList<String>(fields.keySet());
    Collections.sort(keys);

    for (int i = 0; i < keys.size(); i++) {
      Object value = fields.get(keys.get(i));
      if (value == null)
        retval += keys.get(i) + ":\tnull\n";
      else if (value instanceof String)
        retval += keys.get(i) + ":\t" + value + "\n";
      else if (value instanceof Calendar)
        retval += keys.get(i) + ":\t" + ((Calendar) value).getTime() + "\n";
      else if (value instanceof InetAddress[] || value instanceof String []) {
        retval += "Known IPs:\t";
        for (InetAddress ip : ((InetAddress[]) value))
          retval += ip.getHostAddress() + " ";
        retval += "\n";
      } else {
        retval += keys.get(i) + ":\t" + value.toString() + "\n";
      }
    }
    return retval;
  }

}
