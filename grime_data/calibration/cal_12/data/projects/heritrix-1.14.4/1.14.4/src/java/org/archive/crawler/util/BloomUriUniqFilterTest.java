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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.UriUniqFilter;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;


/**
 * Test BloomUriUniqFilter.
 * @author gojomo
 */
public class BloomUriUniqFilterTest extends TestCase
implements UriUniqFilter.HasUriReceiver {
    private Logger logger =
        Logger.getLogger(BloomUriUniqFilterTest.class.getName());

    private BloomUriUniqFilter filter = null;

    /**
     * Set to true if we visited received.
     */
    private boolean received = false;

    protected void setUp() throws Exception {
        super.setUp();
        this.filter = new BloomUriUniqFilter(2000,24);
        this.filter.setDestination(this);
    }

    public void testAdding() throws URIException {
        this.filter.add(this.getUri(),
            new CandidateURI(UURIFactory.getInstance(this.getUri())));
        this.filter.addNow(this.getUri(),
            new CandidateURI(UURIFactory.getInstance(this.getUri())));
        this.filter.addForce(this.getUri(),
            new CandidateURI(UURIFactory.getInstance(this.getUri())));
        // Should only have add 'this' once.
        assertTrue("Count is off", this.filter.count() == 1);
    }

    /**
     * Test inserting.
     * @throws URIException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testWriting() throws URIException {
        long start = System.currentTimeMillis();
        ArrayList<UURI> list = new ArrayList<UURI>(1000);
        int count = 0;
        final int MAX_COUNT = 1000;
        for (; count < MAX_COUNT; count++) {
            assertEquals("count off",count,filter.count());
            UURI u = UURIFactory.getInstance("http://www" +
                    count + ".archive.org/" + count + "/index.html");
            assertFalse("already contained "+u.toString(),filter.bloom.contains(u.toString()));
            logger.fine("adding "+u.toString());
            filter.add(u.toString(), new CandidateURI(u));
            assertTrue("not in bloom",filter.bloom.contains(u.toString()));
            if (count > 0 && ((count % 100) == 0)) {
                list.add(u);
            }
        }
        logger.fine("Added " + count + " in " +
                (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (Iterator i = list.iterator(); i.hasNext();) {
            UURI uuri = (UURI)i.next();
            filter.add(uuri.toString(), new CandidateURI(uuri));
        }
        logger.fine("Readded subset " + list.size() + " in " +
                (System.currentTimeMillis() - start));

        assertTrue("Count is off: " + filter.count(),
            filter.count() == MAX_COUNT);
    }

    public void testNote() {
        filter.note(this.getUri());
        assertFalse("Receiver was called", this.received);
    }

// FORGET CURRENTLY UNSUPPORTED IN BloomUriUniqFilter
//    public void testForget() throws URIException {
//        this.filter.forget(this.getUri(),
//                new CandidateURI(UURIFactory.getInstance(this.getUri())));
//        assertTrue("Didn't forget", this.filter.count() == 0);
//    }

    public void receive(CandidateURI item) {
        this.received = true;
    }

    public String getUri() {
        return "http://www.archive.org";
    }
}
