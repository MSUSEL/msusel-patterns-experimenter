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
package org.apache.james.core;

import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import java.io.*;
import java.util.Enumeration;

import org.apache.james.util.RFC2822Headers;

/**
 * This interface defines a container for mail headers. Each header must use
 * MIME format: <pre>name: value</pre>.
 *
 */
public class MailHeaders extends InternetHeaders implements Serializable, Cloneable {

    /**
     * No argument constructor
     *
     * @throws MessagingException if the super class cannot be properly instantiated
     */
    public MailHeaders() throws MessagingException {
        super();
    }

    /**
     * Constructor that takes an InputStream containing the contents
     * of the set of mail headers.
     *
     * @param in the InputStream containing the header data
     *
     * @throws MessagingException if the super class cannot be properly instantiated
     *                            based on the stream
     */
    public MailHeaders(InputStream in) throws MessagingException {
        super(in);
    }

    /**
     * Write the headers to an output stream
     *
     * @param writer the stream to which to write the headers
     */
    public void writeTo(OutputStream out) {
        PrintStream pout;
        if (out instanceof PrintStream) {
            pout = (PrintStream)out;
        } else {
            pout = new PrintStream(out);
        }
        for (Enumeration e = super.getAllHeaderLines(); e.hasMoreElements(); ) {
            pout.print((String) e.nextElement());
            pout.print("\r\n");
        }
        // Print trailing CRLF
        pout.print("\r\n");
    }

    /**
     * Generate a representation of the headers as a series of bytes.
     *
     * @return the byte array containing the headers
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream headersBytes = new ByteArrayOutputStream();
        writeTo(headersBytes);
        return headersBytes.toByteArray();
    }

    /**
     * Check if a particular header is present.
     *
     * @return true if the header is present, false otherwise
     */
    public boolean isSet(String name) {
        String[] value = super.getHeader(name);
        return (value != null && value.length != 0);
    }

    /**
     * Check if all REQUIRED headers fields as specified in RFC 822
     * are present.
     *
     * @return true if the headers are present, false otherwise
     */
    public boolean isValid() {
        return (isSet(RFC2822Headers.DATE) && isSet(RFC2822Headers.TO) && isSet(RFC2822Headers.FROM));
    }
}
