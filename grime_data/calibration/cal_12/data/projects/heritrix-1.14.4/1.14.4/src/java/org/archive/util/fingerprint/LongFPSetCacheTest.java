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
package org.archive.util.fingerprint;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JUnit test suite for LongFPSetCache
 *
 * @author <a href="mailto:me@jamesc.net">James Casey</a>
 * @version $ Id:$
 */
public class LongFPSetCacheTest extends LongFPSetTestCase {
    /**
     * Create a new LongFPSetCacheTest object
     *
     * @param testName the name of the test
     */
    public LongFPSetCacheTest(final String testName) {
        super(testName);
    }

    /**
     * run all the tests for LongFPSetCacheTest
     *
     * @param argv the command line arguments
     */
    public static void main(String argv[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * return the suite of tests for LongFPSetCacheTest
     *
     * @return the suite of test
     */
    public static Test suite() {
        return new TestSuite(LongFPSetCacheTest.class);
    }

    LongFPSet makeLongFPSet() {
        return new LongFPSetCache();
    }

    /**
     *  This is a cache buffer, which does not grow,
     * but chucks out old values.  Therefore it has a different behaviour
     * from all the other LongFPSets.  We do a different test here.
     */

    public void testCount() {
        LongFPSet fpSet = new LongFPSetCache();
        // TODO: for some reason, when run in a debugger, 
        // the cache-item-discard is glacially slow. It's
        // reasonable when executing.) So, reducing the 
        // number of past-saturation tests in order to let
        // full-unit-tests complete more quickly. 
        final int NUM = 800; // was 1000
        final int MAX_ENTRIES = 768;

        assertEquals("empty set to start", 0, fpSet.count());

        for (int i = 1; i < NUM; ++i) {
            fpSet.add((long) i);
            assertEquals("correct num on add",
                    i<MAX_ENTRIES?i:MAX_ENTRIES, fpSet.count());
        }
    }

    // TODO - implement test methods in LongFPSetCacheTest
}

