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
package com.gargoylesoftware.htmlunit.util;

import java.net.URL;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link UrlUtils}.
 *
 * @version $Revision: 5644 $
 * @author Daniel Gredler
 * @author Martin Tamme
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 */
public class UrlUtilsTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewProtocol() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewProtocol(a, "ftp");
        assertEquals("ftp://my.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewHost() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewHost(a, "your.home.com");
        assertEquals("http://your.home.com/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewPort() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPort(a, 8080);
        assertEquals("http://my.home.com:8080/index.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewPath() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewPath(a, "/es/indice.html");
        assertEquals("http://my.home.com/es/indice.html?query#ref", b.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewRef() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewRef(a, "abc");
        assertEquals("http://my.home.com/index.html?query#abc", b.toExternalForm());

        final URL c = new URL("http://my.home.com/#ref");
        final URL d = UrlUtils.getUrlWithNewRef(c, "xyz");
        assertEquals("http://my.home.com/#xyz", d.toExternalForm());

        final URL e = new URL("http://my.home.com#ref");
        final URL f = UrlUtils.getUrlWithNewRef(e, "xyz");
        assertEquals("http://my.home.com#xyz", f.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getUrlWithNewQuery() throws Exception {
        final URL a = new URL("http://my.home.com/index.html?query#ref");
        final URL b = UrlUtils.getUrlWithNewQuery(a, "xyz");
        assertEquals("http://my.home.com/index.html?xyz#ref", b.toExternalForm());
    }

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with the normal examples taken from
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a> Section 5.1.
     */
    @Test
    public void resolveUrlWithNormalExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("g:h",                  UrlUtils.resolveUrl(baseUrl, "g:h"));
        assertEquals("http://a/b/c/g",       UrlUtils.resolveUrl(baseUrl, "g"));
        assertEquals("http://a/b/c/g",       UrlUtils.resolveUrl(baseUrl, "./g"));
        assertEquals("http://a/b/c/g/",      UrlUtils.resolveUrl(baseUrl, "g/"));
        assertEquals("http://a/g",           UrlUtils.resolveUrl(baseUrl, "/g"));
        assertEquals("http://g",             UrlUtils.resolveUrl(baseUrl, "//g"));
        assertEquals("http://a/b/c/d;p?y",   UrlUtils.resolveUrl(baseUrl, "?y"));
        assertEquals("http://a/b/c/g?y",     UrlUtils.resolveUrl(baseUrl, "g?y"));
        assertEquals("http://a/b/c/g?y/./x", UrlUtils.resolveUrl(baseUrl, "g?y/./x"));
        assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));
        assertEquals("http://a/b/c/g#s",     UrlUtils.resolveUrl(baseUrl, "g#s"));
        assertEquals("http://a/b/c/g#s/./x", UrlUtils.resolveUrl(baseUrl, "g#s/./x"));
        assertEquals("http://a/b/c/g?y#s",   UrlUtils.resolveUrl(baseUrl, "g?y#s"));
        assertEquals("http://a/b/c/d;x",     UrlUtils.resolveUrl(baseUrl, ";x"));
        assertEquals("http://a/b/c/g;x",     UrlUtils.resolveUrl(baseUrl, "g;x"));
        assertEquals("http://a/b/c/g;x?y#s", UrlUtils.resolveUrl(baseUrl, "g;x?y#s"));
        assertEquals("http://a/b/c/",        UrlUtils.resolveUrl(baseUrl, "."));
        assertEquals("http://a/b/c/",        UrlUtils.resolveUrl(baseUrl, "./"));
        assertEquals("http://a/b/",          UrlUtils.resolveUrl(baseUrl, ".."));
        assertEquals("http://a/b/",          UrlUtils.resolveUrl(baseUrl, "../"));
        assertEquals("http://a/b/g",         UrlUtils.resolveUrl(baseUrl, "../g"));
        assertEquals("http://a/",            UrlUtils.resolveUrl(baseUrl, "../.."));
        assertEquals("http://a/",            UrlUtils.resolveUrl(baseUrl, "../../"));
        assertEquals("http://a/g",           UrlUtils.resolveUrl(baseUrl, "../../g"));

        //Following two cases were failing when original implementation was modified to handle
        //the cases given in RFC 1808. Lots of other test cases failed because of that.
        assertEquals(URL_FIRST + "foo.xml", UrlUtils.resolveUrl(URL_FIRST, "/foo.xml"));
        assertEquals(URL_FIRST + "foo.xml", UrlUtils.resolveUrl(URL_FIRST, "foo.xml"));
    }

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with the abnormal examples taken from
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a> Section 5.2.
     */
    @Test
    public void resolveUrlWithAbnormalExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("http://a/b/c/d;p?q#f", UrlUtils.resolveUrl(baseUrl, ""));

// These examples are fine... but it's not what browsers do (see below)
//        assertEquals("http://a/../g",        UrlUtils.resolveUrl(baseUrl, "../../../g"));
//        assertEquals("http://a/../../g",     UrlUtils.resolveUrl(baseUrl, "../../../../g"));
//        assertEquals("http://a/./g",         UrlUtils.resolveUrl(baseUrl, "/./g"));
//        assertEquals("http://a/../g",        UrlUtils.resolveUrl(baseUrl, "/../g"));
        assertEquals("http://a/g",        UrlUtils.resolveUrl(baseUrl, "../../../g"));
        assertEquals("http://a/g",     UrlUtils.resolveUrl(baseUrl, "../../../../g"));
        assertEquals("http://a/./g",         UrlUtils.resolveUrl(baseUrl, "/./g"));
        assertEquals("http://a/g",        UrlUtils.resolveUrl(baseUrl, "/../g"));

        assertEquals("http://a/b/c/g.",      UrlUtils.resolveUrl(baseUrl, "g."));
        assertEquals("http://a/b/c/.g",      UrlUtils.resolveUrl(baseUrl, ".g"));
        assertEquals("http://a/b/c/g..",     UrlUtils.resolveUrl(baseUrl, "g.."));
        assertEquals("http://a/b/c/..g",     UrlUtils.resolveUrl(baseUrl, "..g"));
        assertEquals("http://a/b/g",         UrlUtils.resolveUrl(baseUrl, "./../g"));
        assertEquals("http://a/b/c/g/",      UrlUtils.resolveUrl(baseUrl, "./g/."));
        assertEquals("http://a/b/c/g/h",     UrlUtils.resolveUrl(baseUrl, "g/./h"));
        assertEquals("http://a/b/c/h",       UrlUtils.resolveUrl(baseUrl, "g/../h"));
        assertEquals("http:g",               UrlUtils.resolveUrl(baseUrl, "http:g"));
        assertEquals("http:",                UrlUtils.resolveUrl(baseUrl, "http:"));
    }

    /**
     * Test {@link UrlUtils#resolveUrl(String, String)} with extra examples.
     */
    @Test
    public void resolveUrlWithExtraExamples() {
        final String baseUrl = "http://a/b/c/d;p?q#f";

        assertEquals("http://a/b/c/d;",      UrlUtils.resolveUrl(baseUrl, ";"));
        assertEquals("http://a/b/c/d;p?",    UrlUtils.resolveUrl(baseUrl, "?"));
        assertEquals("http://a/b/c/d;p?q#",  UrlUtils.resolveUrl(baseUrl, "#"));
        assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));

        assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../f.html"));
        assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../../f.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void percent() throws Exception {
        final URL url = new URL("http://localhost/bug%21.html");
        assertEquals("http://localhost/bug%21.html", UrlUtils.encodeUrl(url, false).toExternalForm());
    }
}
