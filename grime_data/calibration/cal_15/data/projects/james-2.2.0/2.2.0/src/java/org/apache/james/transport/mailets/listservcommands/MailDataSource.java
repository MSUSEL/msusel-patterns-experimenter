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
package org.apache.james.transport.mailets.listservcommands;


import javax.activation.DataSource;
import java.io.*;

/**
 * MailDataSource implements a typed DataSource from :
 *  an InputStream, a byte array, and a string
 *
 * This is used from {@link BaseCommand#generateMail}
 *
 * @version CVS $Revision: 1.1.2.4 $ $Date: 2004/03/15 03:54:20 $
 * @since 2.2.0
 */
public class MailDataSource implements DataSource {

    protected static final int DEFAULT_BUF_SIZE = 0x2000;

    protected static final String DEFAULT_ENCODING = "iso-8859-1";
    protected static final String DEFAULT_NAME = "HtmlMailDataSource";

    protected byte[] data; // data
    protected String contentType; // content-type

    /**
     * Create a datasource from an input stream
     */
    public MailDataSource(InputStream inputStream, String contentType) throws IOException {
        this.contentType = contentType;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyStream(inputStream, baos);
        data = baos.toByteArray();
    }

    /**
     * Create a datasource from a byte array
     */
    public MailDataSource(byte[] data, String contentType) {
        this.contentType = contentType;
        this.data = data;
    }

    /**
     * Create a datasource from a String
     */
    public MailDataSource(String data, String contentType) throws UnsupportedEncodingException {
        this.contentType = contentType;
        this.data = data.getBytes(DEFAULT_ENCODING);
    }

    /**
     * returns the inputStream
     */
    public InputStream getInputStream() throws IOException {
        if (data == null)
            throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }

    /**
     * Not implemented
     */
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("getOutputStream() isn't implemented");
    }

    /**
     * returns the contentType for this data source
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * returns a static moniker
     */
    public String getName() {
        return DEFAULT_NAME;
    }

    protected static int copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        inputStream = new BufferedInputStream(inputStream);
        outputStream = new BufferedOutputStream(outputStream);

        byte[] bbuf = new byte[DEFAULT_BUF_SIZE];
        int len;
        int totalBytes = 0;
        while ((len = inputStream.read(bbuf)) != -1) {
            outputStream.write(bbuf, 0, len);
            totalBytes += len;
        }
        outputStream.flush();
        return totalBytes;
    }
}

