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
package org.apache.hadoop.hdfs.server.namenode;

import java.util.*;

/** Manage name-to-serial-number maps for users and groups. */
class SerialNumberManager {
  /** This is the only instance of {@link SerialNumberManager}.*/
  static final SerialNumberManager INSTANCE = new SerialNumberManager();

  private SerialNumberMap<String> usermap = new SerialNumberMap<String>();
  private SerialNumberMap<String> groupmap = new SerialNumberMap<String>();

  private SerialNumberManager() {}

  int getUserSerialNumber(String u) {return usermap.get(u);}
  int getGroupSerialNumber(String g) {return groupmap.get(g);}
  String getUser(int n) {return usermap.get(n);}
  String getGroup(int n) {return groupmap.get(n);}

  {
    getUserSerialNumber(null);
    getGroupSerialNumber(null);
  }

  private static class SerialNumberMap<T> {
    private int max = 0;
    private int nextSerialNumber() {return max++;}

    private Map<T, Integer> t2i = new HashMap<T, Integer>();
    private Map<Integer, T> i2t = new HashMap<Integer, T>();

    synchronized int get(T t) {
      Integer sn = t2i.get(t);
      if (sn == null) {
        sn = nextSerialNumber();
        t2i.put(t, sn);
        i2t.put(sn, t);
      }
      return sn;
    }

    synchronized T get(int i) {
      if (!i2t.containsKey(i)) {
        throw new IllegalStateException("!i2t.containsKey(" + i
            + "), this=" + this);
      }
      return i2t.get(i);
    }

    /** {@inheritDoc} */
    public String toString() {
      return "max=" + max + ",\n  t2i=" + t2i + ",\n  i2t=" + i2t;
    }
  }
}
