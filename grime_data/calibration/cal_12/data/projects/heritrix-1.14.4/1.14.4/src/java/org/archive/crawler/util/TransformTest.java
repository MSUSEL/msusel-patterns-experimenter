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
package org.archive.crawler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Tests the {@link dex.misc.Transform} class.
 */
public class TransformTest extends TestCase {

    // Convert integers to strings, eliminating negative numbers    
    private static class PositiveToString 
    implements Transformer<Integer, String> {
        public String transform(Integer i) {
            if (i < 0) {
                return null;
            }
            return i.toString();
        }
    }

    /**
     * Tests using a simple Transformer.  The Transformer changes
     * positive integers into strings.  The test sets up a 
     * list of random integers, remembering which ones are
     * positive.  The Transform is created, and the Transform's
     * contents are compared against the list of remembered positive 
     * integers.
     */
    public void testTransform() {
        Transformer<Integer,String> transformer = new PositiveToString();

        // Transform of an empty collection should be empty.
        List<Integer> empty = new ArrayList<Integer>();
        assertTrue(new Transform<Integer,String>(empty, transformer).isEmpty());

        // Some simple test data.
        Integer[] testData = new Integer[] { -5, 3, 2, -11, 0, 111, -161 };
        String[] expected = new String[] { "3", "2", "0", "111" };
        List<Integer> list = Arrays.asList(testData);
        Transform<Integer,String> c = new Transform<Integer,String>(list,
                transformer);
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(new ArrayList<String>(c), expectedList);

        // Same test as above, with random data
        for (int i = 0; i < 100; i++) {
            randomTest();
        }
    }

    private void randomTest() {
        Transformer<Integer, String> transformer = new PositiveToString();
        Random random = new Random();
        int max = random.nextInt(1024) + 10;
        List<Integer> testData = new ArrayList<Integer>(max);
        List<String> expected = new ArrayList<String>(max);
        for (int i = 0; i < max; i++) {
            int e = random.nextInt();
            testData.add(e);
            if (e >= 0) {
                expected.add(Integer.toString(e));
            }
        }

        Transform<Integer,String> c = new Transform<Integer,String>(testData,
                transformer);
        List<String> results = new ArrayList<String>(c);
        assertEquals(expected, results);
    }

    /**
     * Tests the static subclasses method.  The test sets up a list of
     * Number instances that may contain random Double, Float, Integer
     * or Long values.  The Long values are remembered.  The subclasses
     * method is used to create a Transform containing only the Long
     * values.  The Transform is compared against the list of remembered
     * Long values.
     */
    public void testSubclasses() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int max = random.nextInt(1024) + 10;
            List<Number> testData = new ArrayList<Number>(max);
            List<Long> expected = new ArrayList<Long>(max);
            for (int j = 0; j < max; j++) {
                int v = random.nextInt(4);
                switch (v) {
                case 0:
                    long l = random.nextLong();
                    testData.add(l);
                    expected.add(l);
                    break;
                case 1:
                    testData.add(random.nextInt());
                    break;
                case 2:
                    testData.add(random.nextDouble());
                    break;
                case 3:
                    testData.add(random.nextFloat());
                    break;
                }
            }
            Collection<Long> c = Transform.subclasses(testData, Long.class);
            List<Long> results = new ArrayList<Long>(c);
            assertEquals(expected, results);
        }
    }

    public void testSingleton() {
        Set<Number> set = new HashSet<Number>();
        set.add(3);
        Collection<Integer> c = Transform.subclasses(set, Integer.class);
        for (Integer i : c) {
            System.out.println(i);
        }
    }
}