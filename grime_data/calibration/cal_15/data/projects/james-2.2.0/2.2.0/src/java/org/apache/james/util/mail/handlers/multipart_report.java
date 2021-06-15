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
package org.apache.james.util.mail.handlers;

import java.io.IOException;
import java.io.OutputStream;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataSource;
import javax.mail.MessagingException;

import org.apache.james.util.mail.MimeMultipartReport;

/**
 * <p>Data Content Handler for...</p>
 * <dl>
 * <dt>MIME type name</dt><dd>multipart</dd>
 * <dt>MIME subtype name</dt><dd>report</dd>
 * </dl>
 */
public class multipart_report extends AbstractDataContentHandler
{
    /**
     * Default constructor.
     */
    public multipart_report()
    {
        super();
    }

    /**
     * @see org.apache.james.util.mail.handlers.AbstractDataContentHandler#computeDataFlavor()
     */
    protected ActivationDataFlavor computeDataFlavor()
    {
        return new ActivationDataFlavor(MimeMultipartReport.class,
                "multipart/report", "Multipart Report");
    }

    /**
     * @see javax.activation.DataContentHandler#writeTo(java.lang.Object,
     *      java.lang.String, java.io.OutputStream)
     */
    public void writeTo(Object aPart, String aMimeType, OutputStream aStream)
            throws IOException
    {
        if (!(aPart instanceof MimeMultipartReport))
            throw new IOException("Type \"" + aPart.getClass().getName()
                    + "\" is not supported.");
        try
        {
            ((MimeMultipartReport) aPart).writeTo(aStream);
        }
        catch (MessagingException e)
        {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * @see org.apache.james.util.mail.handlers.AbstractDataContentHandler#computeContent(javax.activation.DataSource)
     */
    protected Object computeContent(DataSource aDataSource)
            throws MessagingException
    {
        return new MimeMultipartReport(aDataSource);
    }
}
