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
package org.apache.commons.httpclient.cookie;

import java.util.Collection;
import java.util.SortedMap; // <- IA/HERITRIX CHANGE

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;

/**
 * A cookie spec that does nothing.  Cookies are neither parsed, formatted nor matched.
 * It can be used to effectively disable cookies altogether. 
 * 
 * @since 3.0
 */
public class IgnoreCookiesSpec implements CookieSpec {

    /**
     * 
     */
    public IgnoreCookiesSpec() {
        super();
    }

    /**
     * Returns an empty {@link Cookie cookie} array.  All parameters are ignored.
     */
    public Cookie[] parse(String host, int port, String path, boolean secure, String header)
        throws MalformedCookieException {
        return new Cookie[0];
    }

    /**
     * @return <code>null</code>
     */
    public Collection getValidDateFormats() {
        return null;
    }
    
    /**
     * Does nothing.
     */
    public void setValidDateFormats(Collection datepatterns) {
    }
    
    /**
     * @return <code>null</code>
     */
    public String formatCookie(Cookie cookie) {
        return null;
    }

    /**
     * @return <code>null</code>
     */
    public Header formatCookieHeader(Cookie cookie) throws IllegalArgumentException {
        return null;
    }

    /**
     * @return <code>null</code>
     */
    public Header formatCookieHeader(Cookie[] cookies) throws IllegalArgumentException {
        return null;
    }

    /**
     * @return <code>null</code>
     */
    public String formatCookies(Cookie[] cookies) throws IllegalArgumentException {
        return null;
    }

    /**
     * @return <code>false</code>
     */
    public boolean match(String host, int port, String path, boolean secure, Cookie cookie) {
        return false;
    }

    /**
     * Returns an empty {@link Cookie cookie} array.  All parameters are ignored.
     */
    public Cookie[] match(String host, int port, String path, boolean secure, Cookie[] cookies) {
        return new Cookie[0];
    }

    /**
     * Returns an empty {@link Cookie cookie} array.  All parameters are ignored.
     */
    public Cookie[] parse(String host, int port, String path, boolean secure, Header header)
        throws MalformedCookieException, IllegalArgumentException {
        return new Cookie[0];
    }

    /**
     * Does nothing.
     */
    public void parseAttribute(NameValuePair attribute, Cookie cookie)
        throws MalformedCookieException, IllegalArgumentException {
    }

    /**
     * Does nothing.
     */
    public void validate(String host, int port, String path, boolean secure, Cookie cookie)
        throws MalformedCookieException, IllegalArgumentException {
    }

    /**
     * @return <code>false</code>
     */
    public boolean domainMatch(final String host, final String domain) {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean pathMatch(final String path, final String topmostPath) {
        return false;
    }

// BEGIN IA/HERITRIX ADDITION
    public Cookie[] match(String domain, int port, String path, boolean secure,
        SortedMap cookiesMap) {
        return new Cookie[0];
    }
// END IA/HERITRIX CHANGE
}
