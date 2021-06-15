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
 * Test stripping of userinfo from an url.
 * @author stack
 * @version $Date: 2005-07-18 17:30:21 +0000 (Mon, 18 Jul 2005) $, $Revision: 3704 $
 */
public class StripUserinfoRuleTest extends TestCase {
    public void testCanonicalize() throws URIException {
        String url = "http://WWW.aRchive.Org/index.html";
        final String expectedResult = url;
        String result = (new StripUserinfoRule("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Mangled no userinfo " + result,
            url.equals(result));
        url = "http://stack:password@WWW.aRchive.Org/index.html";
        result = (new StripUserinfoRule("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Didn't strip userinfo " + result,
            expectedResult.equals(result));
        url = "http://stack:pass@@@@@@word@WWW.aRchive.Org/index.html";
        result = (new StripUserinfoRule("test")).
            canonicalize(url, 
                UURIFactory.getInstance("http://archive.org"));
        assertTrue("Didn't get to last @ " + result,
            expectedResult.equals(result));
        url = "ftp://stack:pass@@@@@@word@archive.org/index.html";
        result = (new StripUserinfoRule("test")).
            canonicalize(url,
                UURIFactory.getInstance("http://archive.org"));
        assertTrue("Didn't get to last @ " + result,
            "ftp://archive.org/index.html".equals(result));
    }
}
