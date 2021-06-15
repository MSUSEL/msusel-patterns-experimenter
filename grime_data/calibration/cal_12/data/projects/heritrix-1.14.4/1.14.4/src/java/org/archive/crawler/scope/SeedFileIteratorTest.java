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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.archive.net.UURI;

/**
 * Test {@link SeedFileIterator}.
 * @author gojomo
 * @version $Revision: 4651 $, $Date: 2006-09-25 18:31:13 +0000 (Mon, 25 Sep 2006) $
 */
public class SeedFileIteratorTest extends TestCase {
    public void testHyphenInHost() {
        final String seedFileContent = "http://www.examp-le.com/";
        StringWriter sw = new StringWriter();
        StringReader sr = new StringReader(seedFileContent);
        UURI seed = 
            (UURI)(new SeedFileIterator(new BufferedReader(sr), sw)).next();
        assertEquals("Hyphen is problem", seed.toString(),
            seedFileContent);
    }

    public void testGeneral() throws IOException {
        String seedFile = "# comment\n" + // comment
                "\n" + // blank line
                "www.example.com\n" + // naked host, implied scheme
                "www.example.org/foo\n" + // naked host+path, implied scheme
                "http://www.example.net\n" + // full HTTP URL
                "+http://www.example.us"; // 'directive' (should be ignored)
        StringWriter ignored = new StringWriter();
        SeedFileIterator iter = new SeedFileIterator(new BufferedReader(
                new StringReader(seedFile)), new BufferedWriter(ignored));
        LinkedList<String> seeds = new LinkedList<String>();
        while (iter.hasNext()) {
            UURI n = iter.next();
            if (n instanceof UURI) {
                seeds.add(n.getURI());
            }
        }
        assertTrue("didn't get naked host", seeds
                .contains("http://www.example.com/"));
        assertTrue("didn't get naked host+path", seeds
                .contains("http://www.example.org/foo"));
        assertTrue("didn't get full http URL", seeds
                .contains("http://www.example.net/"));
        assertTrue("got wrong number of URLs", seeds.size() == 3);
        assertTrue("ignored entry not reported", ignored.toString().indexOf(
                "+http://www.example.us") >= 0);
    }
}

