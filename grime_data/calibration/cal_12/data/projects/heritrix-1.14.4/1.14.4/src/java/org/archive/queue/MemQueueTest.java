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
package org.archive.queue;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JUnit test suite for MemQueue
 *
 * @author <a href="mailto:me@jamesc.net">James Casey</a>
 * @version $ Id$
 */
public class MemQueueTest extends QueueTestBase {
    /**
     * Create a new MemQueueTest object
     *
     * @param testName the name of the test
     */
    public MemQueueTest(final String testName) {
        super(testName);
    }

    /**
     * run all the tests for MemQueueTest
     *
     * @param argv the command line arguments
     */
    public static void main(String argv[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * return the suite of tests for MemQueueTest
     *
     * @return the suite of test
     */
    public static Test suite() {
        return new TestSuite(MemQueueTest.class);
    }

    /*
     * test methods
     */
    protected Queue<Object> makeQueue() {
        return new MemQueue<Object>();
    }

    // TODO - implement test methods in MemQueueTest
}

