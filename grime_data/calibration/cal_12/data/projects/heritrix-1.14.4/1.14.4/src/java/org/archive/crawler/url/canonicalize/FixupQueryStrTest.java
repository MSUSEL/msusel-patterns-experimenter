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
package org.archive.crawler.url.canonicalize;

import org.apache.commons.httpclient.URIException;
import org.archive.net.UURIFactory;

import junit.framework.TestCase;

/**
 * Test we strip trailing question mark.
 * @author stack
 * @version $Date: 2006-09-01 22:44:50 +0000 (Fri, 01 Sep 2006) $, $Revision: 4591 $
 */
public class FixupQueryStrTest extends TestCase {

    public void testCanonicalize() throws URIException {
        final String url = "http://WWW.aRchive.Org/index.html";
        assertTrue("Mangled " + url,
            url.equals((new FixupQueryStr("test")).
                canonicalize(url, UURIFactory.getInstance(url))));
        assertTrue("Failed to strip '?' " + url,
            url.equals((new FixupQueryStr("test")).
                canonicalize(url + "?", UURIFactory.getInstance(url))));
        assertTrue("Failed to strip '?&' " + url,
            url.equals((new FixupQueryStr("test")).
                canonicalize(url + "?&", UURIFactory.getInstance(url))));
        assertTrue("Failed to strip extraneous '&' " + url,
            (url + "?x=y").equals((new FixupQueryStr("test")).
                canonicalize(url + "?&x=y", UURIFactory.getInstance(url))));
        String tmp = url + "?x=y";
        assertTrue("Mangled x=y " + tmp,
            tmp.equals((new FixupQueryStr("test")).
                canonicalize(tmp, UURIFactory.getInstance(url))));
        String tmp2 = tmp + "&";
        String fixed = new FixupQueryStr("test").
            canonicalize(tmp2, UURIFactory.getInstance(url));
        assertTrue("Mangled " + tmp2, tmp.equals(fixed));
    }
}
