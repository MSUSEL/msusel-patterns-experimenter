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
 * Test stripping of session ids.
 * @author stack
 * @version $Date: 2006-09-01 22:44:50 +0000 (Fri, 01 Sep 2006) $, $Revision: 4591 $
 */
public class StripSessionIDsTest extends TestCase {
    private static final String  BASE = "http://www.archive.org/index.html";
    public void testCanonicalize() throws URIException {
        String str32id = "0123456789abcdefghijklemopqrstuv";
        String url = BASE + "?jsessionid=" + str32id;
        String expectedResult = BASE + "?";
        String result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test that we don't strip if not 32 chars only.
        url = BASE + "?jsessionid=" + str32id + '0';
        expectedResult = url;
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test what happens when followed by another key/value pair.
        url = BASE + "?jsessionid=" + str32id + "&x=y";
        expectedResult = BASE + "?x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test what happens when followed by another key/value pair and
        // prefixed by a key/value pair.
        url = BASE + "?one=two&jsessionid=" + str32id + "&x=y";
        expectedResult = BASE + "?one=two&x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test what happens when prefixed by a key/value pair.
        url = BASE + "?one=two&jsessionid=" + str32id;
        expectedResult = BASE + "?one=two&";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test aspsession.
        url = BASE + "?aspsessionidABCDEFGH=" + "ABCDEFGHIJKLMNOPQRSTUVWX"
            + "&x=y";
        expectedResult = BASE + "?x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test archive phpsession.
        url = BASE + "?phpsessid=" + str32id + "&x=y";
        expectedResult = BASE + "?x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // With prefix too.
        url = BASE + "?one=two&phpsessid=" + str32id + "&x=y";
        expectedResult = BASE + "?one=two&x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // With only prefix
        url = BASE + "?one=two&phpsessid=" + str32id;
        expectedResult = BASE + "?one=two&";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));
        
        // Test sid.
        url = BASE + "?" + "sid=9682993c8daa2c5497996114facdc805" + "&x=y";
        expectedResult = BASE + "?x=y";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));	
        
        // Igor test.
        url = BASE + "?" + "sid=9682993c8daa2c5497996114facdc805" + "&" +
            "jsessionid=" + str32id;
        expectedResult = BASE + "?";
        result = (new StripSessionIDs("test")).
            canonicalize(url, UURIFactory.getInstance(url));
        assertTrue("Failed " + result, expectedResult.equals(result));  
    }
}
