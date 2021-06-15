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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for UriUtils. 
 * 
 * Several of the tests for the 'legacy' (H1 through at least 1.14.4) 
 * heuristics are disabled by renaming, because those heuristics have known 
 * failures; however, until more experience with the new heuristics is 
 * collected, H1 still uses them for consistency. 
 * 
 * @contributor gojomo
 * @version $Id: ArchiveUtilsTest.java 5052 2007-04-10 02:26:52Z gojomo $
 */
public class UriUtilsTest extends TestCase {

    public UriUtilsTest(final String testName) {
        super(testName);
    }

    /**
     * run all the tests for ArchiveUtilsTest
     * 
     * @param argv
     *            the command line arguments
     */
    public static void main(String argv[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(UriUtilsTest.class);
    }

    /** image URIs that should be considered likely URIs **/
    static String[] urisRelativeImages = { 
        "photo.jpg", 
        "./photo.jpg",
        "../photo.jpg", 
        "images/photo.jpg", 
        "../../images/photo.jpg" };

    /** check that plausible relative image URIs return true with legacy tests */
    public void xestLegacySimpleImageRelatives() {
        legacyTryAll(urisRelativeImages, true);
    }
    
    /** check that plausible relative image URIs return true with new tests */
    public void testNewSimpleImageRelatives() {
        tryAll(urisRelativeImages,true); 
    }

    /** absolute URIs that should be considered likely URIs **/
    static String[] urisAbsolute = { 
        "http://example.com",
        "http://example.com/", "http://www.example.com",
        "http://www.example.com/", "http://www.example.com/about",
        "http://www.example.com/about/",
        "http://www.example.com/about/index.html", "https://example.com",
        "https://example.com/", "https://www.example.com",
        "https://www.example.com/", "https://www.example.com/about",
        "https://www.example.com/about/",
        "https://www.example.com/about/index.html",
        "ftp://example.com/public/report.pdf",
    // TODO: other schemes? mailto?

    };

    /** check that absolute URIs return true with legacy tests */
    public void testLegacyAbsolutes() {
        legacyTryAll(urisAbsolute,true);
    }
    
    /** check that absolute URIs return true with new tests */
    public void testAbsolutes() {
        tryAll(urisAbsolute,true);
    }

    /** path-absolute images URIs that should be considered likely URIs **/
    static String[] urisPathAbsoluteImages = { 
        "/photo.jpg", 
        "/images/photo.jpg", 
    };
    
    /** check that path-absolute image URIs return true with legacy tests*/
    public void testLegacySimpleImagePathAbsolutes() {
        legacyTryAll(urisPathAbsoluteImages, true); 
    }
    
    /** check that path-absolute image URIs return true with new tests*/
    public void testSimpleImagePathAbsolutes() {
        tryAll(urisPathAbsoluteImages, true); 
    }
    
    /** URI-like strings risking false positives that should NOT be likely URIs **/
    static String[] notUrisNaiveFalsePositives = {
        "0.99",
        "3.14157",
        "text/javascript"
    };
    
    /** check that typical false-positives of the naive test are not deemed URIs */
    public void xestLegacyNaiveFalsePositives() {
        legacyTryAll(notUrisNaiveFalsePositives, false); 
    }
    
    /** check that typical false-positives of the naive test are not deemed URIs */
    public void testNaiveFalsePositives() {
        tryAll(notUrisNaiveFalsePositives, false); 
    }
    
    /** strings that should not be considered likely URIs **/
    static String[] notUrisNaive = {
        "foo bar",
        "<script>foo=bar</script>",
        "item\t$0.99\tred",
    };
    
    /** check that strings that fail naive test are not deemed URIs legacy tests*/
    public void testLegacyNaiveNotUris() {
        legacyTryAll(notUrisNaive, false); 
    }
    
    /** check that strings that fail naive test are not deemed URIs new tests*/
    public void testNaiveNotUris() {
        tryAll(notUrisNaive, false); 
    }
    
    
    /**
     * Test that all supplied candidates give the expected result, for each of 
     * the 'legacy' (H1) likely-URI-tests
     * 
     * @param candidates String[] to test
     * @param expected desired answer
     */
    protected void legacyTryAll(String[] candidates, boolean expected) {
        for (String candidate : candidates) {
            assertEquals("javascript context: " + candidate, 
                    expected, 
                    UriUtils.isLikelyUriJavascriptContextLegacy(candidate));
            assertEquals("html context: " + candidate, 
                    expected, 
                    UriUtils.isLikelyUriHtmlContextLegacy(candidate));
        }
    }
    

    
    /**
     * Test that all supplied candidates give the expected results, for 
     * the 'new' heuristics now in this class. 
     * @param candidates String[] to test
     * @param expected desired answer
     */
    protected void tryAll(String[] candidates, boolean expected) {
        for (String candidate : candidates) {
            assertEquals("new: " + candidate, 
                    expected, 
                    UriUtils.isLikelyUri(candidate));
            assertEquals("html context: " + candidate, 
                    expected, 
                    UriUtils.isLikelyUri(candidate));
        }
    }
}
