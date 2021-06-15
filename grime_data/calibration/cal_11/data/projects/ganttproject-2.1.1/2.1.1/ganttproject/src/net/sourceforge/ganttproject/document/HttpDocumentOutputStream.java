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
/*
 * Created on 18.08.2003
 *
 */
package net.sourceforge.ganttproject.document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.webdav.lib.WebdavResource;

/**
 * This class implements an OutputStream for documents on
 * WebDAV-enabled-servers. It is a helper class for HttpDocument.
 * 
 * @see HttpDocument
 * @author Michael Haeusler (michael at akatose.de)
 */
class HttpDocumentOutputStream extends ByteArrayOutputStream {

    private WebdavResource webdavResource;

    public HttpDocumentOutputStream(WebdavResource webdavResource) {
        super();
        this.webdavResource = webdavResource;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            try {
                webdavResource.putMethod(toByteArray());
            } catch (HttpException e) {
                throw new IOException(e.getMessage() + "(" + e.getReasonCode()
                        + ")");
            }
        }
    }

}
