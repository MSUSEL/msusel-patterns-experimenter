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
package org.apache.james.imapserver;

import java.io.Serializable;

/**
 * Class for holding the name-value pairs of an RFC822 or MIME header.
 * Like javax.mail.Header but serializable
 *
 * @version 0.1 on 14 Dec 2000
 */
public class MessageHeader implements Serializable {
    public static final String CRLF =  "\r\n";
    public static final String CRLFHTAB =  "\r\n\t";
    public static final String CRLFWS = "\r\n ";

    private final String name;
    private final String value;

    public MessageHeader(String headerLine) {
        int colon = headerLine.indexOf(":");
        name = headerLine.substring(0, colon);
        StringBuffer unwrapped = new StringBuffer(headerLine.length());
        boolean finished = false;
        int pos = colon + 1;
        while (!finished) {
            int crlf = headerLine.indexOf(CRLF, pos);
            if (crlf == -1) {
                unwrapped.append(headerLine.substring(pos));
                finished = true;
            } else {
                unwrapped.append(headerLine.substring(pos, crlf));
                unwrapped.append(" ");
                pos = crlf +3;
            }
        }
        value = unwrapped.toString();
    }

    public MessageHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name, aka field name, of this header.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value, aka field value, of this Header
     *
     * @return String
     */
    public String getValue() {
        return value;
    }
}


