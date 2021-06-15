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
import java.util.HashMap;
import java.text.DateFormat;

/**********************************************************
 * Objects of this class hold the serialized representations
 * of EventRecords. A SerializedRecord is essentially an EventRecord
 * with all its property values converted to strings. It also provides 
 * some convenience methods for printing the property fields in a 
 * more readable way.
 *
 **********************************************************/

public class SerializedRecord {

  HashMap<String, String> fields;
  private static DateFormat dateFormatter =
    DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);;

  /**
   * Create the SerializedRecord given an EventRecord.
   */
  
  public SerializedRecord(EventRecord source) {
    fields = new HashMap<String, String>();
    fields.clear();

    for (String k : source.getMap().keySet()) {
      ArrayList<String> strs = getStrings(source.getMap().get(k));
      if (strs.size() == 1)
        fields.put(k, strs.get(0));
      else
        for (int i = 0; i < strs.size(); i++)
          fields.put(k + "#" + i, strs.get(i));
    }

  }

  /**
   * Extract String representations from an Object.
   * 
   * @param o the input object
   * 
   * @return an ArrayList that contains Strings found in o
   */
  private ArrayList<String> getStrings(Object o) {
    ArrayList<String> retval = new ArrayList<String>();
    retval.clear();
    if (o == null)
      retval.add("null");
    else if (o instanceof String)
      retval.add((String) o);
    else if (o instanceof Calendar)
      retval.add(dateFormatter.format(((Calendar) o).getTime()));
    else if (o instanceof InetAddress[])
      for (InetAddress ip : ((InetAddress[]) o))
        retval.add(ip.getHostAddress());
    else if (o instanceof String[])
      for (String s : (String []) o)
        retval.add(s);
    else
      retval.add(o.toString());

    return retval;
  }

  /**
   * Set the value of a property of the EventRecord.
   * 
   * @param fieldName the name of the property to set
   * @param fieldValue the value of the property to set
   * 
   */
  public void set(String fieldName, String fieldValue) {
    fields.put(fieldName, fieldValue);
  }

  /**
   * Get the value of a property of the EventRecord.
   * If the property with the specific key is not found,
   * null is returned.
   * 
   * @param fieldName the name of the property to get.
   */
  public String get(String fieldName) {
    return fields.get(fieldName);
  }

  /**
   * Arrange the keys to provide a more readable printing order:
   * first goes the timestamp, then the hostname and then the type, followed
   * by all other keys found.
   * 
   * @param keys The input ArrayList of keys to re-arrange.
   */
  public static void arrangeKeys(ArrayList<String> keys) {
    move(keys, "timestamp", 0);
    move(keys, "hostname", 1);
    move(keys, "type", 2);
  }

  private static void move(ArrayList<String> keys, String key, int position) {
    int cur = keys.indexOf(key);
    if (cur == -1)
      return;
    keys.set(cur, keys.get(position));
    keys.set(position, key);
  }

  /**
   * Check if the SerializedRecord is a valid one, i.e., whether
   * it represents meaningful metric values.
   * 
   * @return true if the EventRecord is a valid one, false otherwise.
   */
  public boolean isValid() {
    return !("invalid".equalsIgnoreCase(fields.get("hostname")));
  }

  
  /**
   * Creates and returns a string reperssentation of the object
   * 
   * @return a String representing the object
   */

  public String toString() {
    String retval = "";
    ArrayList<String> keys = new ArrayList<String>(fields.keySet());
    arrangeKeys(keys);

    for (int i = 0; i < keys.size(); i++) {
      String value = fields.get(keys.get(i));
      if (value == null)
        retval += keys.get(i) + ":\tnull\n";
      else
        retval += keys.get(i) + ":\t" + value + "\n";
    }
    return retval;
  }
}
