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
package org.apache.james.smtpserver;

import java.io.IOException;
import java.io.InputStream;

/** 
  * Wraps an underlying input stream, limiting the allowable size
  * of incoming data. The size limit is configured in the conf file,
  * and when the limit is reached, a MessageSizeException is thrown.
  */
public class SizeLimitedInputStream extends InputStream {
    /**
     * Maximum number of bytes to read.
     */
    private long maxmessagesize = 0;
    /**
     * Running total of bytes read from wrapped stream.
     */
    private long bytesread = 0;

    /**
     * InputStream that will be wrapped.
     */
    private InputStream in = null;

    /**
     * Constructor for the stream. Wraps an underlying stream.
     * @param in InputStream to use as basis for new Stream.
     * @param maxmessagesize Message size limit, in Kilobytes
     */
    public SizeLimitedInputStream(InputStream in, long maxmessagesize) {
        this.in = in;
        this.maxmessagesize = maxmessagesize;
    }

    /**
     * Overrides the read method of InputStream to call the read() method of the
     * wrapped input stream.
     * @throws IOException Throws a MessageSizeException, which is a sub-type of IOException
     * @return Returns the number of bytes read.
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int l = in.read(b, off, len);

        bytesread += l;

        if (maxmessagesize > 0 && bytesread > maxmessagesize) {
            throw new MessageSizeException();
        }

        return l;
    }

    /**
     * Overrides the read method of InputStream to call the read() method of the
     * wrapped input stream.
     * @throws IOException Throws a MessageSizeException, which is a sub-type of IOException.
     * @return Returns the int character value of the byte read.
     */
    public int read() throws IOException {
        if (maxmessagesize > 0 && bytesread <= maxmessagesize) {
            bytesread++;
            return in.read();
        } else {
            throw new MessageSizeException();
        }
    }
}
