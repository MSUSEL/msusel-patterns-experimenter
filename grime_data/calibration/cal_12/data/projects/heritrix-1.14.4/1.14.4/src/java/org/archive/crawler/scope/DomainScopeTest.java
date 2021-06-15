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
package org.archive.crawler.scope;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.httpclient.URIException;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;

/**
 * Test the domain scope focus filter.
 *
 * @author Igor Ranitovic
 */
public class DomainScopeTest extends TestCase {

    private ArrayList<UURI> testSeeds;
    private ArrayList<UURI> urlsInScope;
    private ArrayList<UURI> urlsOutOfScope;

    private TestUnitDomainScope dc;

    /**
     * Since testing only focus filter overwrite all other filter to return
     * false.
     *
     * Also override seedsIterator so the test seeds are used.
     */
    @SuppressWarnings("deprecation")
    private class TestUnitDomainScope extends DomainScope {

        private static final long serialVersionUID = 2509499903112690451L;

        public TestUnitDomainScope(String name) {
            super(name);
        }
        
        /* Force test seeds to be used. 
         * @see org.archive.crawler.framework.CrawlScope#seedsIterator()
         */
        public Iterator<UURI> seedsIterator() {
            return testSeeds.iterator();
        }
        
        protected boolean additionalFocusAccepts(Object o) {
            return false;
        }

        protected boolean transitiveAccepts(Object o) {
            return false;
        }

        protected boolean excludeAccepts(Object o) {
            return false;
        }
    }

    public void setUp() throws URIException {
        testSeeds = new ArrayList<UURI>();
        urlsInScope = new ArrayList<UURI>();
        urlsOutOfScope = new ArrayList<UURI>();
        dc = new TestUnitDomainScope("TESTCASE");

        // Add seeds
        addURL(testSeeds, "http://www.a.com/");
        addURL(testSeeds, "http://b.com/");
        addURL(testSeeds, "http://www11.c.com");
        addURL(testSeeds, "http://www.x.y.z.com/index.html");
        addURL(testSeeds, "http://www.1.com/index.html");
        addURL(testSeeds, "http://www.a_b.com/index.html");


        // Add urls in domain scope
        addURL(urlsInScope, "http://www.a.com/");
        addURL(urlsInScope, "http://www1.a.com/");
        addURL(urlsInScope, "http://a.com/");
        addURL(urlsInScope, "http://a.a.com/");

        addURL(urlsInScope, "http://www.b.com/");
        addURL(urlsInScope, "http://www1.b.com/");
        addURL(urlsInScope, "http://b.com/");
        addURL(urlsInScope, "http://b.b.com/");

        addURL(urlsInScope, "http://www.c.com/");
        addURL(urlsInScope, "http://www1.c.com/");
        addURL(urlsInScope, "http://c.com/");
        addURL(urlsInScope, "http://c.c.com/");

        addURL(urlsInScope, "http://www.x.y.z.com/");
        addURL(urlsInScope, "http://www1.x.y.z.com/");
        addURL(urlsInScope, "http://x.y.z.com/");
        addURL(urlsInScope, "http://xyz.x.y.z.com/");
        addURL(urlsInScope, "http://1.com/index.html");
        addURL(urlsInScope, "http://a_b.com/index.html");

        // Add urls out of scope
        addURL(urlsOutOfScope, "http://a.co");
        addURL(urlsOutOfScope, "http://a.comm");
        addURL(urlsOutOfScope, "http://aa.com");
        addURL(urlsOutOfScope, "http://z.com");
        addURL(urlsOutOfScope, "http://y.z.com");
    }

    public void addURL(ArrayList<UURI> list, String url) throws URIException {
        list.add(UURIFactory.getInstance(url));
    }

    public void testInScope() throws URIException {
        for (Iterator i = this.urlsInScope.iterator(); i.hasNext();) {
            Object url = i.next();
            assertTrue("Should be in domain scope: " + url, dc.accepts(url));
        }
    }

    public void testOutOfScope() throws URIException {
        for (Iterator i = this.urlsOutOfScope.iterator(); i.hasNext();) {
            Object url = i.next();
            assertFalse(
                "Should not be in domain scope: " + url,
                dc.accepts(url));
        }
    }
}
