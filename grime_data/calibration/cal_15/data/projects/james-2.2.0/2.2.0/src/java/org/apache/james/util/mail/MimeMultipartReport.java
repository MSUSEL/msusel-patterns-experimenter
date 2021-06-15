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
package org.apache.james.util.mail;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;

/**
 * Class <code>MimeMultipartReport</code> implements JavaMail support
 * for a MIME type of MimeMultipart with a subtype of report.
 */
public class MimeMultipartReport extends MimeMultipart
{

    /**
     * Default constructor
     */
    public MimeMultipartReport()
    {
        this("report");
    }

    /**
     * Constructs a MimeMultipartReport of the given subtype.
     * @param subtype
     */
    public MimeMultipartReport(String subtype)
    {
        super(subtype);
    }

    /**
     * Constructs a MimeMultipartReport from the passed DataSource.
     * @param aDataSource
     * @throws javax.mail.MessagingException
     */
    public MimeMultipartReport(DataSource aDataSource) throws MessagingException
    {
        super(aDataSource);
    }
    
    /**
     * Sets the type of report.
     * @param reportType
     * @throws MessagingException
     */
    public void setReportType(String reportType) throws MessagingException
    {
        ContentType contentType = new ContentType(getContentType());
        contentType.setParameter("report-type", reportType);
        setContentType(contentType);
    }
    
    /**
     * Sets the content type
     * @param aContentType
     */
    protected void setContentType(ContentType aContentType)
    {
        contentType = aContentType.toString();
    }

}
