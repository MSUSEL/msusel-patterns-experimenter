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

import org.apache.james.transport.matchers.HasHabeasWarrantMark;
import org.apache.mailet.GenericMailet ;
import org.apache.mailet.Mail ;

/*
 * This matcher adds the Hebeas Warrant Mark to a message.
 * For details see: http://www.hebeas.com
 *
 * Usage: <mailet match="<suitable-matcher>" class="AddHabeasWarrantMark" />
 *
 * NOTE: Although this mailet is covered by the Apache Software License,
 * the Habeas Warrant Mark is copyright.  A separate license from Habeas
 * is required in order to legally attach the Habeas Warrant Mark to
 * e-mail messages.  Each James Administrator is responsible for
 * ensuring that James is configured to attach the Habeas Warrant Mark
 * only to e-mail covered by a suitable license received from Habeas.
 * 
 * Because the Habeas Warrant Mark is copyright material, I have asked
 * for and received the following explicit statement from Habeas:
 *
 * -----------------------------------
 * From: Lindsey Pettit [mailto:support@habeas.com]
 * Sent: Sunday, September 29, 2002 5:51
 * To: Noel J. Bergman
 * Subject: RE: Habeas and Apache James
 *
 * Dear Noel,
 * 
 * > FURTHERMORE, if James is to be capable of sending Habeas SWE, I need
 * > to write a Mailet that attaches the headers.  As with any MTA, it
 * > would be up to the administrator to properly configure James and make
 * > sure that licenses are acquired.  Since the Habeas Warrant Mark is
 * > copyright, I believe that I require authorization from you for that
 * > Mailet, especially since it attaches the Habeas Warrant Mark.  For my
 * > own protection, please show me why such authorization is unnecessary,
 * > send me a digitally signed e-mail, or FAX a signed authorization
 * 
 * You do not yourself need the authorization to build the functionality 
 * into the [mailet];  what one needs authorization, in the form of a 
 * license, for, is to use the mark *in headers*, in outgoing email.
 * However, please let me know if you would like something more 
 * formal, and I can try to have something faxed to you.
 * 
 * > The Mailet docs would reference the Habeas website, and inform
 * > administrators that in order to USE the mailet, they need to ensure
 * > that they have whatever licenses are required from you as appropriate
 * > to your licensing terms.
 * 
 * That's absolutely perfect!
 * -----------------------------------
 *
 */

public class AddHabeasWarrantMark extends GenericMailet
{
    /**
     * Called by the mailet container to allow the mailet to process to
     * a message message.
     *
     * This method adds the Habeas Warrant Mark headers to the message,
     * saves the changes, and then allows the message to fall through
     * in the pipeline.
     *
     * @param mail - the Mail object that contains the message and routing information
     * @throws javax.mail.MessagingException - if an message or address parsing exception occurs or
     *      an exception that interferes with the mailet's normal operation
     */
    public void service(Mail mail) throws javax.mail.MessagingException
    {
        try
        {
            javax.mail.internet.MimeMessage message = mail.getMessage();

            for(int i = 0 ; i < HasHabeasWarrantMark.warrantMark.length ; i++)
            {
                message.setHeader(HasHabeasWarrantMark.warrantMark[i][0], HasHabeasWarrantMark.warrantMark[i][1]);
            }

            message.saveChanges();
        }
        catch (javax.mail.MessagingException me)
        {
            log(me.getMessage());
        }
    }

    /*
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo()
    {
        return "Add Habeas Warrant Mark.  Must be used in accordance with a license from Habeas (see http://www.habeas.com for details).";
    }
}
