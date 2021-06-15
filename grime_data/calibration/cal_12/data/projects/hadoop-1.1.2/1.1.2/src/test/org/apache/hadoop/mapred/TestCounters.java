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
package org.apache.hadoop.mapred;

import junit.framework.TestCase;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;

import org.apache.hadoop.mapred.Counters.Counter;

/**
 * TestCounters checks the sanity and recoverability of {@code Counters}
 */
public class TestCounters extends TestCase {
  enum myCounters {TEST1, TEST2};
  private static final long MAX_VALUE = 10;
  
  // Generates enum based counters
  private Counters getEnumCounters(Enum[] keys) {
    Counters counters = new Counters();
    for (Enum key : keys) {
      for (long i = 0; i < MAX_VALUE; ++i) {
        counters.incrCounter(key, i);
      }
    }
    return counters;
  }
  
  // Generate string based counters
  private Counters getEnumCounters(String[] gNames, String[] cNames) {
    Counters counters = new Counters();
    for (String gName : gNames) {
      for (String cName : cNames) {
        for (long i = 0; i < MAX_VALUE; ++i) {
          counters.incrCounter(gName, cName, i);
        }
      }
    }
    return counters;
  }
  
  /**
   * Test counter recovery
   */
  private void testCounter(Counters counter) throws ParseException {
    String compactEscapedString = counter.makeEscapedCompactString();
    
    Counters recoveredCounter = 
      Counters.fromEscapedCompactString(compactEscapedString);
    // Check for recovery from string
    assertEquals("Recovered counter does not match on content", 
                 counter, recoveredCounter);
    assertEquals("recovered counter has wrong hash code",
                 counter.hashCode(), recoveredCounter.hashCode());
  }
  
  public void testCounters() throws IOException {
    Enum[] keysWithResource = {Task.Counter.MAP_INPUT_BYTES, 
                               Task.Counter.MAP_OUTPUT_BYTES};
    
    Enum[] keysWithoutResource = {myCounters.TEST1, myCounters.TEST2};
    
    String[] groups = {"group1", "group2", "group{}()[]"};
    String[] counters = {"counter1", "counter2", "counter{}()[]"};
    
    try {
      // I. Check enum counters that have resource bundler
      testCounter(getEnumCounters(keysWithResource));

      // II. Check enum counters that dont have resource bundler
      testCounter(getEnumCounters(keysWithoutResource));

      // III. Check string counters
      testCounter(getEnumCounters(groups, counters));
    } catch (ParseException pe) {
      throw new IOException(pe);
    }
  }
  
  /**
   * Verify counter value works
   */
  public void testCounterValue() {
    final int NUMBER_TESTS = 100;
    final int NUMBER_INC = 10;
    final Random rand = new Random();
    for (int i = 0; i < NUMBER_TESTS; i++) {
      long initValue = rand.nextInt();
      long expectedValue = initValue;
      Counter counter = new Counter("foo", "bar", expectedValue);
      assertEquals("Counter value is not initialized correctly",
                   expectedValue, counter.getValue());
      for (int j = 0; j < NUMBER_INC; j++) {
        int incValue = rand.nextInt();
        counter.increment(incValue);
        expectedValue += incValue;
        assertEquals("Counter value is not incremented correctly",
                     expectedValue, counter.getValue());
      }
      expectedValue = rand.nextInt();
      counter.setValue(expectedValue);
      assertEquals("Counter value is not set correctly",
                   expectedValue, counter.getValue());
    }
  }
  
  public static void main(String[] args) throws IOException {
    new TestCounters().testCounters();
  }
}
