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
package org.apache.hadoop.net;

import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

/**
 * Implements the {@link DNSToSwitchMapping} via static mappings. Used
 * in testcases that simulate racks.
 *
 */
public class StaticMapping extends Configured implements DNSToSwitchMapping {
  public void setconf(Configuration conf) {
    String[] mappings = conf.getStrings("hadoop.configured.node.mapping");
    if (mappings != null) {
      for (int i = 0; i < mappings.length; i++) {
        String str = mappings[i];
        String host = str.substring(0, str.indexOf('='));
        String rack = str.substring(str.indexOf('=') + 1);
        addNodeToRack(host, rack);
      }
    }
  }
  /* Only one instance per JVM */
  private static Map<String, String> nameToRackMap = new HashMap<String, String>();
  
  static synchronized public void addNodeToRack(String name, String rackId) {
    nameToRackMap.put(name, rackId);
  }
  public List<String> resolve(List<String> names) {
    List<String> m = new ArrayList<String>();
    synchronized (nameToRackMap) {
      for (String name : names) {
        String rackId;
        if ((rackId = nameToRackMap.get(name)) != null) {
          m.add(rackId);
        } else {
          m.add(NetworkTopology.DEFAULT_RACK);
        }
      }
      return m;
    }
  }
}
