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

import java.io.IOException;
import java.io.InputStream;

/**
 * This defines a reusable datasource that can supply an input stream with
 * MimeMessage data.  This allows a MimeMessageWrapper or other classes to
 * grab the underlying data.
 *
 * @see MimeMessageWrapper
 */
public abstract class MimeMessageSource {
    /**
     * Returns a unique String ID that represents the location from where 
     * this file is loaded.  This will be used to identify where the data 
     * is, primarily to avoid situations where this data would get overwritten.
     *
     * @return the String ID
     */
    public abstract String getSourceId();

    /**
     * Get an input stream to retrieve the data stored in the datasource
     *
     * @return a <code>InputStream</code> containing the data
     *
     * @throws IOException if an error occurs while generating the
     *                     InputStream
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Return the size of all the data.
     * Default implementation... others can override to do this much faster
     *
     * @return the size of the data represented by this source
     * @throws IOException if an error is encountered while computing the message size
     */
    public long getMessageSize() throws IOException {
        int size = 0;
        InputStream in = null;
        try {
            in = getInputStream();
            int read = 0;
            byte[] data = new byte[1024];
            while ((read = in.read(data)) > 0) {
                size += read;
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                // Exception ignored because logging is
                // unavailable
            }
        }
        return size;
    }

}
