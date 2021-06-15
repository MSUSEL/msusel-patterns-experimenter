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
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Utility methods relating to text.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public final class TextUtil {

    /**
     * Default encoding used.
     */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";

    /** Private constructor to prevent instantiation. */
    private TextUtil() { }

    /**
     * Returns true if the string starts with the specified prefix, irrespective of case.
     * @param stringToCheck the string to check
     * @param prefix the prefix
     * @return true if the string starts with the prefix
     */
    public static boolean startsWithIgnoreCase(final String stringToCheck, final String prefix) {
        WebAssert.notNull("stringToCheck", stringToCheck);
        WebAssert.notNull("prefix", prefix);

        if (prefix.length() == 0) {
            throw new IllegalArgumentException("Prefix may not be empty");
        }

        final int prefixLength = prefix.length();
        if (stringToCheck.length() < prefixLength) {
            return false;
        }
        return stringToCheck.substring(0, prefixLength).toLowerCase().equals(prefix.toLowerCase());
    }

    /**
     * Convert a string into an input stream.
     * @param content the string
     * @return the resulting input stream
     */
    public static InputStream toInputStream(final String content) {
        try {
            return toInputStream(content, DEFAULT_CHARSET);
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(
                DEFAULT_CHARSET + " is an unsupported encoding!  You may have a corrupted installation of java.");
        }
    }

    /**
     * Convert a string into an input stream.
     * @param content the string
     * @param encoding the encoding to use when converting the string to a stream
     * @return the resulting input stream
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    public static InputStream toInputStream(
            final String content,
            final String encoding)
        throws
            UnsupportedEncodingException {

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(content.length() * 2);
            final OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, encoding);
            writer.write(content);
            writer.flush();

            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(byteArray);
        }
        catch (final UnsupportedEncodingException e) {
            throw e;
        }
        catch (final IOException e) {
            // Theoretically impossible since all the "IO" is in memory but it's a
            // checked exception so we have to catch it.
            e.printStackTrace();
            throw new IllegalStateException("Exception when converting a string to an input stream: " + e);
        }
    }

    /**
     * Converts a string into a byte array using a default encoding {@link #DEFAULT_CHARSET}.
     *
     * @param content the string to convert, assumed to be {@link #DEFAULT_CHARSET} encoded
     * @return the String as a byte[]; if the default encoding is not supported an empty byte[] will be returned
     */
    public static byte[] stringToByteArray(final String content) {
        return content != null ? stringToByteArray(content, DEFAULT_CHARSET) : null;
    }

    /**
     * Converts a string into a byte array using the specified encoding.
     *
     * @param charset the name of a supported charset
     * @param content the string to convert
     * @return the String as a byte[]; if the specified encoding is not supported an empty byte[] will be returned
     */
    public static byte[] stringToByteArray(final String content, final String charset) {
        byte[] contentBytes;
        try {
            contentBytes = content.getBytes(charset);
        }
        catch (final UnsupportedEncodingException e) {
            contentBytes = new byte[0];
        }
        return contentBytes;
    }
}
