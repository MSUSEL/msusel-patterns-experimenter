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
package org.archive.crawler.extractor;

import java.util.List;

import org.archive.net.UURI;

import junit.framework.TestCase;

/**
 * Test ExtractorURI
 * 
 * @author gojomo
 */
public class ExtractorURITest extends TestCase {
    
    public void testFullQuery() {
        String queryStringUri = "http://www.example2.com";
        innerTestQueryString(queryStringUri,queryStringUri);
    }

    public void testFullQueryEncoded() {
        String queryStringUri = "http%3A//www.example2.com/";
        String expectedUri = "http://www.example2.com/";
        innerTestQueryString(queryStringUri,expectedUri);
    }
    
    public void testFullQueryEncodedComplex() {
        String queryStringUri = "http%3A//www.example2.com/foo%3Fbar%3Dbz%26red%3Dblue";
        String expectedUri = "http://www.example2.com/foo?bar=bz&red=blue";
        innerTestQueryString(queryStringUri,expectedUri);
    }
    
    private void innerTestQueryString(String queryStringUri, String expectedUri) {
        UURI uuri = UURI.from(
                "http://www.example.com/foo?"+queryStringUri);
        innerTestForPresence(uuri, expectedUri);
    }

    private void innerTestForPresence(UURI uuri, String expectedUri) {
        List<String> results = ExtractorURI.extractQueryStringLinks(uuri);
        assertTrue(
                "URI not found: "+expectedUri,
                results.contains(expectedUri));
    }
    
    public void testParameterComplex() {
        String parameterUri = "http%3A//www.example2.com/foo%3Fbar%3Dbz%26red%3Dblue";
        String expectedUri = "http://www.example2.com/foo?bar=bz&red=blue";
        UURI uuri = UURI.from(
                "http://www.example.com/foo?uri="+parameterUri+"&foo=bar");
        innerTestForPresence(uuri,expectedUri);
    }
}
