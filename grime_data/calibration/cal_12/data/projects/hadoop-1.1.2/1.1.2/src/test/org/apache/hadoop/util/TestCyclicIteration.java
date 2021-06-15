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
package org.apache.hadoop.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TestCyclicIteration extends junit.framework.TestCase {
  public void testCyclicIteration() throws Exception {
    for(int n = 0; n < 5; n++) {
      checkCyclicIteration(n);
    }
  }

  private static void checkCyclicIteration(int numOfElements) {
    //create a tree map
    final NavigableMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
    final Integer[] integers = new Integer[numOfElements];
    for(int i = 0; i < integers.length; i++) {
      integers[i] = 2*i;
      map.put(integers[i], integers[i]);
    }
    System.out.println("\n\nintegers=" + Arrays.asList(integers));
    System.out.println("map=" + map);

    //try starting everywhere
    for(int start = -1; start <= 2*integers.length - 1; start++) {
      //get a cyclic iteration
      final List<Integer> iteration = new ArrayList<Integer>(); 
      for(Map.Entry<Integer, Integer> e : new CyclicIteration<Integer, Integer>(map, start)) {
        iteration.add(e.getKey());
      }
      System.out.println("start=" + start + ", iteration=" + iteration);
      
      //verify results
      for(int i = 0; i < integers.length; i++) {
        final int j = ((start+2)/2 + i)%integers.length;
        assertEquals("i=" + i + ", j=" + j, iteration.get(i), integers[j]);
      }
    }
  }
}
