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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for {@link NameCache} class
 */
public class TestNameCache {
  @Test
  public void testDictionary() throws Exception {
    // Create dictionary with useThreshold 2
    NameCache<String> cache = 
      new NameCache<String>(2);
    String[] matching = {"part1", "part10000000", "fileabc", "abc", "filepart"};
    String[] notMatching = {"spart1", "apart", "abcd", "def"};

    for (String s : matching) {
      // Add useThreshold times so the names are promoted to dictionary
      cache.put(s);
      assertTrue(s == cache.put(s));
    }
    for (String s : notMatching) {
      // Add < useThreshold times so the names are not promoted to dictionary
      cache.put(s);
    }
    
    // Mark dictionary as initialized
    cache.initialized();
    
    for (String s : matching) {
      verifyNameReuse(cache, s, true);
    }
    // Check dictionary size
    assertEquals(matching.length, cache.size());
    
    for (String s : notMatching) {
      verifyNameReuse(cache, s, false);
    }
  }

  private void verifyNameReuse(NameCache<String> cache, String s, boolean reused) {
    cache.put(s);
    int lookupCount = cache.getLookupCount();
    if (reused) {
      // Dictionary returns non null internal value
      assertNotNull(cache.put(s));
      // Successful lookup increments lookup count
      assertEquals(lookupCount + 1, cache.getLookupCount());
    } else {
      // Dictionary returns null - since name is not in the dictionary
      assertNull(cache.put(s));
      // Lookup count remains the same
      assertEquals(lookupCount, cache.getLookupCount());
    }
  }
}
