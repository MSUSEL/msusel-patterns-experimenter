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
package org.apache.hadoop.mapred.gridmix;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.sun.tools.javac.code.Attribute.Array;

public class TestRandomAlgorithm {
  private static final int[][] parameters = new int[][] {
    {5, 1, 1}, 
    {10, 1, 2},
    {10, 2, 2},
    {20, 1, 3},
    {20, 2, 3},
    {20, 3, 3},
    {100, 3, 10},
    {100, 3, 100},
    {100, 3, 1000},
    {100, 3, 10000},
    {100, 3, 100000},
    {100, 3, 1000000}
  };
  
  private List<Integer> convertIntArray(int[] from) {
    List<Integer> ret = new ArrayList<Integer>(from.length);
    for (int v : from) {
      ret.add(v);
    }
    return ret;
  }
  
  private void testRandomSelectSelector(int niter, int m, int n) {
    RandomAlgorithms.Selector selector = new RandomAlgorithms.Selector(n,
        (double) m / n, new Random());
    Map<List<Integer>, Integer> results = new HashMap<List<Integer>, Integer>(
        niter);
    for (int i = 0; i < niter; ++i, selector.reset()) {
      int[] result = new int[m];
      for (int j = 0; j < m; ++j) {
        int v = selector.next();
        if (v < 0)
          break;
        result[j]=v;
      }
      Arrays.sort(result);
      List<Integer> resultAsList = convertIntArray(result);
      Integer count = results.get(resultAsList);
      if (count == null) {
        results.put(resultAsList, 1);
      } else {
        results.put(resultAsList, ++count);
      }
    }

    verifyResults(results, m, n);
  }

  private void testRandomSelect(int niter, int m, int n) {
    Random random = new Random();
    Map<List<Integer>, Integer> results = new HashMap<List<Integer>, Integer>(
        niter);
    for (int i = 0; i < niter; ++i) {
      int[] result = RandomAlgorithms.select(m, n, random);
      Arrays.sort(result);
      List<Integer> resultAsList = convertIntArray(result);
      Integer count = results.get(resultAsList);
      if (count == null) {
        results.put(resultAsList, 1);
      } else {
        results.put(resultAsList, ++count);
      }
    }

    verifyResults(results, m, n);
  }

  private void verifyResults(Map<List<Integer>, Integer> results, int m, int n) {
    if (n>=10) {
      assertTrue(results.size() >= Math.min(m, 2));
    }
    for (List<Integer> result : results.keySet()) {
      assertEquals(m, result.size());
      Set<Integer> seen = new HashSet<Integer>();
      for (int v : result) {
        System.out.printf("%d ", v);
        assertTrue((v >= 0) && (v < n));
        assertTrue(seen.add(v));
      }
      System.out.printf(" ==> %d\n", results.get(result));
    }
    System.out.println("====");
  }
  
  @Test
  public void testRandomSelect() {
    for (int[] param : parameters) {
    testRandomSelect(param[0], param[1], param[2]);
    }
  }
  
  @Test
  public void testRandomSelectSelector() {
    for (int[] param : parameters) {
      testRandomSelectSelector(param[0], param[1], param[2]);
      }
  }
}
