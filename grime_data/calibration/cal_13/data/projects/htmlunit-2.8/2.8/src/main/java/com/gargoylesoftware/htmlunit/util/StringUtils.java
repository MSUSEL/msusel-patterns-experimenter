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

import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * String utilities class for utility functions not covered by third party libraries.
 *
 * @version $Revision: 5724 $
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Martin Tamme
 */
public final class StringUtils {

    private static final Log LOG = LogFactory.getLog(StringUtils.class);

    /**
     * Disallow instantiation of this class.
     */
    private StringUtils() {
        // Empty.
    }

    /**
     * Escapes the characters '<', '>' and '&' into their XML entity equivalents. Note that
     * sometimes we have to use this method instead of
     * {@link org.apache.commons.lang.StringEscapeUtils#escapeXml(String)} or
     * {@link org.apache.commons.lang.StringEscapeUtils#escapeHtml(String)} because those methods
     * escape some unicode characters as well.
     *
     * @param s the string to escape
     * @return the escaped form of the specified string
     */
    public static String escapeXmlChars(final String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Returns <tt>true</tt> if the specified string contains whitespace, <tt>false</tt> otherwise.
     *
     * @param s the string to check for whitespace
     * @return <tt>true</tt> if the specified string contains whitespace, <tt>false</tt> otherwise
     */
    public static boolean containsWhitespace(final String s) {
        for (final char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index within the specified string of the first occurrence of
     * the specified search character.
     *
     * @param s the string to search
     * @param searchChar the character to search for
     * @param beginIndex the index at which to start the search
     * @param endIndex the index at which to stop the search
     * @return the index of the first occurrence of the character in the string or <tt>-1</tt>
     */
    public static int indexOf(final String s, final char searchChar, final int beginIndex, final int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            if (s.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <tt>true</tt> if the specified string is a valid float, possibly trimming the string before checking.
     *
     * @param s the string to check
     * @param trim whether or not to trim the string before checking
     * @return <tt>true</tt> if the specified string is a valid float, <tt>false</tt> otherwise
     */
    public static boolean isFloat(String s, final boolean trim) {
        if (trim) {
            s = s.trim();
        }

        boolean ok;
        try {
            Float.parseFloat(s);
            ok = true;
        }
        catch (final NumberFormatException e) {
            ok = false;
        }

        return ok;
    }

    /**
     * Returns <tt>true</tt> if the specified collection of strings contains the specified string, ignoring case.
     *
     * @param strings the strings to search
     * @param string the string to search for
     * @return <tt>true</tt> if the specified collection of strings contains the specified string, ignoring case
     */
    public static boolean containsCaseInsensitive(final Collection<String> strings, String string) {
        string = string.toLowerCase();
        for (String s : strings) {
            if (s.toLowerCase().equals(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses the specified date string, assuming that it is formatted according to RFC 1123, RFC 1036 or as an ANSI
     * C HTTP date header. This method returns <tt>null</tt> if the specified string is <tt>null</tt> or unparseable.
     *
     * @param s the string to parse as a date
     * @return the date version of the specified string, or <tt>null</tt>
     */
    public static Date parseHttpDate(final String s) {
        if (s == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(s);
        }
        catch (final DateParseException e) {
            LOG.warn("Unable to parse date: " + s);
            return null;
        }
    }

    /**
     * Formats the specified date according to RFC 1123.
     *
     * @param date the date to format
     * @return the specified date, formatted according to RFC 1123
     */
    public static String formatHttpDate(final Date date) {
        WebAssert.notNull("date", date);
        return DateUtils.formatDate(date);
    }

}
