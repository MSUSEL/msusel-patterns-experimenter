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
package org.archive.util;

import org.apache.commons.httpclient.URIException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for SURT
 * 
 * @author gojomo
 * @version $ Id$
 */
public class SURTTest extends TestCase {
    /**
     * Create a new MemQueueTest object
     * 
     * @param testName
     *            the name of the test
     */
    public SURTTest(final String testName) {
        super(testName);
    }

    /**
     * run all the tests for MemQueueTest
     * 
     * @param argv
     *            the command line arguments
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
        return new TestSuite(SURTTest.class);
    }

    public void testMisc() throws URIException {
        assertEquals("", 
                "http://(org,archive,www,)",
                SURT.fromURI("http://www.archive.org"));

        assertEquals("", 
                "http://(org,archive,www,)/movies/movies.php",
                SURT.fromURI("http://www.archive.org/movies/movies.php"));

        assertEquals("", 
                "http://(org,archive,www,:8080)/movies/movies.php",
                SURT.fromURI("http://www.archive.org:8080/movies/movies.php"));

        assertEquals("", 
                "http://(org,archive,www,@user:pass)/movies/movies.php",
                SURT.fromURI("http://user:pass@www.archive.org/movies/movies.php"));

        assertEquals("", 
                "http://(org,archive,www,:8080@user:pass)/movies/movies.php",
                SURT.fromURI("http://user:pass@www.archive.org:8080/movies/movies.php"));
        
        assertEquals("", 
                "http://(org,archive,www,)/movies/movies.php#top",
                SURT.fromURI("http://www.archive.org/movies/movies.php#top"));
    }
    
    public void testAtSymbolInPath() throws URIException {
        assertEquals("@ in path",
                "http://(com,example,www,)/foo@bar",
                SURT.fromURI("http://www.example.com/foo@bar"));  
    }
    
    /**
     * Verify that dotted-quad numeric IP address is unreversed as per change
     * requested in: [ 1572391 ] SURTs for IP-address URIs unhelpful
     * 
     * @throws URIException
     */
    public void testDottedQuadAuthority() throws URIException {
        assertEquals("dotted-quad IP authority",
                "http://(127.2.34.5)/foo",
                SURT.fromURI("http://127.2.34.5/foo"));  
    }
}

