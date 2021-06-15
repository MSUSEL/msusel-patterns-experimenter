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
 * Test stripping 'www' if present.
 * @author stack
 * @version $Date: 2006-09-18 20:32:47 +0000 (Mon, 18 Sep 2006) $, $Revision: 4634 $
 */
public class StripWWWNRuleTest extends TestCase {

    public void testCanonicalize() throws URIException {
        String url = "http://WWW.aRchive.Org/index.html";
        String expectedResult = "http://aRchive.Org/index.html";
        String result = (new StripWWWNRule("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        url = "http://www001.aRchive.Org/index.html";
        result = (new StripWWWNRule("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        url = "http://www3.aRchive.Org/index.html";
        result = (new StripWWWNRule("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
    }
}