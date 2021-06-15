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
package org.apache.james.transport.matchers;

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import java.util.Collection;

/**
 * Checks whether this message has an attachment
 *
 * @version CVS $Revision: 1.2.4.4 $ $Date: 2004/03/15 03:54:21 $
 */
public class HasAttachment extends GenericMatcher {

    /** 
     * Either every recipient is matching or neither of them.
     * @throws MessagingException if no attachment is found and at least one exception was thrown
     */
    public Collection match(Mail mail) throws MessagingException {
        
        Exception anException = null;
        
        try {
            MimeMessage message = mail.getMessage();
            Object content;
            
            /**
             * if there is an attachment and no inline text,
             * the content type can be anything
             */
            if (message.getContentType() == null) {
                return null;
            }
            
            content = message.getContent();
            if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    try {
                        Part part = multipart.getBodyPart(i);
                        String fileName = part.getFileName();
                        if (fileName != null) {
                            return mail.getRecipients(); // file found
                        }
                    } catch (MessagingException e) {
                        anException = e;
                    } // ignore any messaging exception and process next bodypart
                }
            } else {
                String fileName = message.getFileName();
                if (fileName != null) {
                    return mail.getRecipients(); // file found
                }
            }
        } catch (Exception e) {
            anException = e;
        }
        
        // if no attachment was found and at least one exception was catched rethrow it up
        if (anException != null) {
            throw new MessagingException("Malformed message", anException);
        }
        
        return null; // no attachment found
    }
}
