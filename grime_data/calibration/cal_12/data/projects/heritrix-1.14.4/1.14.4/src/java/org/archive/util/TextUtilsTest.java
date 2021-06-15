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

import java.util.regex.Matcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for TextUtils
 * 
 * @author gojomo
 * @version $ Id$
 */
public class TextUtilsTest extends TestCase {
    /**
     * Create a new TextUtilsTest object
     * 
     * @param testName
     *            the name of the test
     */
    public TextUtilsTest(final String testName) {
        super(testName);
    }

    /**
     * run all the tests for TextUtilsTest
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
        return new TestSuite(TextUtilsTest.class);
    }

    public void testMatcherRecycling() {
        String pattern = "f.*";
        Matcher m1 = TextUtils.getMatcher(pattern,"foo");
        assertTrue("matcher against 'foo' problem", m1.matches());
        TextUtils.recycleMatcher(m1);
        Matcher m2 = TextUtils.getMatcher(pattern,"");
        assertFalse("matcher against '' problem", m2.matches());
        assertTrue("matcher not recycled",m1==m2);
        // now verify proper behavior without recycling
        Matcher m3 = TextUtils.getMatcher(pattern,"fuggedaboutit");
        assertTrue("matcher against 'fuggedaboutit' problem",m3.matches());
        assertFalse("matcher was recycled",m3==m2);
    }
    
    public void testGetFirstWord() {
        final String firstWord = "one";
        String tmpStr = TextUtils.getFirstWord(firstWord + " two three");
        assertTrue("Failed to get first word 1 " + tmpStr,
            tmpStr.equals(firstWord));
        tmpStr = TextUtils.getFirstWord(firstWord);
        assertTrue("Failed to get first word 2 " + tmpStr,
            tmpStr.equals(firstWord));       
    }
    
    public void testUnescapeHtml() {
        final String abc = "abc";
        CharSequence cs = TextUtils.unescapeHtml("abc");
        assertEquals(cs, abc);
        final String backwards = "aaa;lt&aaa";
        cs = TextUtils.unescapeHtml(backwards);
        assertEquals(cs, backwards);
        final String ampersand = "aaa&aaa";
        cs = TextUtils.unescapeHtml(ampersand);
        assertEquals(cs, ampersand);
        final String encodedAmpersand = "aaa&amp;aaa";
        cs = TextUtils.unescapeHtml(encodedAmpersand);
        assertEquals(cs, ampersand);
        final String encodedQuote = "aaa&#39;aaa";
        cs = TextUtils.unescapeHtml(encodedQuote);
        assertEquals(cs, "aaa'aaa");
        final String entityQuote = "aaa&quot;aaa";
        cs = TextUtils.unescapeHtml(entityQuote);
        assertEquals(cs, "aaa\"aaa");
        final String hexencoded = "aaa&#x000A;aaa";
        cs = TextUtils.unescapeHtml(hexencoded);
        assertEquals(cs, "aaa\naaa");
        final String zeroPos = "&amp;aaa";
        cs = TextUtils.unescapeHtml(zeroPos);
        assertEquals(cs, "&aaa");
    }
    
    public void testUnescapeHtmlWithDanglingAmpersand() {
        final String mixedEncodedAmpersand1 = "aaa&aaa&amp;aaa";
        CharSequence cs = TextUtils.unescapeHtml(mixedEncodedAmpersand1);
        assertEquals(cs,"aaa&aaa&aaa");
        final String mixedEncodedAmpersand2 = "aaa&aaa&amp;aaa&amp;aaa";
        cs = TextUtils.unescapeHtml(mixedEncodedAmpersand2);
        assertEquals(cs,"aaa&aaa&aaa&aaa");
    } 
}

