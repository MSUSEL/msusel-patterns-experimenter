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
package org.apache.james.transport.mailets;

import javax.mail.internet.MimeMessage ;

import org.apache.james.core.MailImpl ;
import org.apache.mailet.GenericMailet ;
import org.apache.mailet.Mail ;

/**
 * Adds a specified header and value to the message.
 *
 * Sample configuration:
 *
 * <mailet match="All" class="AddHeader">
 *   <name>X-MailetHeader</name>
 *   <value>TheHeaderValue</value>
 * </mailet>
 *
 * @version 1.0.0, 2002-09-11
 */
public class AddHeader
       extends GenericMailet {

    /**
     * The name of the header to be added.
     */
    private String headerName;

    /**
     * The value to be set for the header.
     */
    private String headerValue;

    /**
     * Initialize the mailet.
     */
    public void init() {
        headerName = getInitParameter("name");
        headerValue = getInitParameter("value");
    }

    /**
     * Takes the message and adds a header to it.
     *
     * @param mail the mail being processed
     *
     * @throws MessagingException if an error arises during message processing
     */
    public void service(Mail mail) {
        try {
            MimeMessage message = mail.getMessage () ;

            //Set the header name and value (supplied at init time).
            message.setHeader(headerName, headerValue);
            message.saveChanges();
        } catch (javax.mail.MessagingException me) {
            log (me.getMessage());
        }
    }

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "AddHeader Mailet" ;
    }

}

