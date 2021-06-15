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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

/**
 * <p>Data Content Handler for...</p>
 * <dl>
 * <dt>MIME type name</dt><dd>message</dd>
 * <dt>MIME subtype name</dt><dd>disposition-notification</dd>
 * </dl>
 */
public class message_disposition_notification
        extends
            AbstractDataContentHandler
{

    /**
     * Default Constructor.
     */
    public message_disposition_notification()
    {
        super();
    }

    /**
     * @see org.apache.james.util.mail.handlers.AbstractDataContentHandler#computeDataFlavor()
     */
    protected ActivationDataFlavor computeDataFlavor()
    {
        return new ActivationDataFlavor(String.class,
                "message/disposition-notification", "Message String");
    }

    /**
     * @see org.apache.james.util.mail.handlers.AbstractDataContentHandler#computeContent(javax.activation.DataSource)
     */
    protected Object computeContent(DataSource aDataSource)
            throws MessagingException
    {
        String encoding = getCharacterSet(aDataSource.getContentType());
        Reader reader = null;
        Writer writer = new StringWriter(2048);
        String content = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(aDataSource
                    .getInputStream(), encoding), 2048);
            while (reader.ready())
                writer.write(reader.read());
            writer.flush();
            content = writer.toString();
        }
        catch (IllegalArgumentException e)
        {
            throw new MessagingException("Encoding = \"" + encoding + "\"", e);
        }
        catch (IOException e)
        {
            throw new MessagingException(
                    "Exception obtaining content from DataSource", e);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException e1)
            {
                // No-op
            }
        }
        return content;
    }

    /**
     * @see javax.activation.DataContentHandler#writeTo(java.lang.Object,
     *      java.lang.String, java.io.OutputStream)
     */
    public void writeTo(Object aPart, String aMimeType, OutputStream aStream)
            throws IOException
    {
        if (!(aPart instanceof String))
            throw new IOException("Type \"" + aPart.getClass().getName()
                    + "\" is not supported.");

        String encoding = getCharacterSet(getDataFlavor().getMimeType());
        Writer writer = null;
        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(aStream,
                    encoding), 2048);
        }
        catch (IllegalArgumentException e)
        {
            throw new UnsupportedEncodingException(encoding);
        }
        writer.write((String) aPart);
        writer.flush();
    }

    protected String getCharacterSet(String aType)
    {
        String characterSet = null;
        try
        {
            characterSet = new ContentType(aType).getParameter("charset");
        }
        catch (ParseException e)
        {
            // no-op
        }
        finally
        {
            if (null == characterSet)
                characterSet = "us-ascii";
        }
        return MimeUtility.javaCharset(characterSet);
    }

}
