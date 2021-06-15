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
package org.apache.james.util.mail.mdn;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.apache.james.util.mail.MimeMultipartReport;

/**
 * Class <code>MDNFactory</code> creates MimeMultipartReports containing
 * Message Delivery Notifications as specified by RFC 2298.
 */
public class MDNFactory
{

    /**
     * Default Constructor
     */
    private MDNFactory()
    {
        super();
    }
    
    /**
     * Answers a MimeMultipartReport containing a
     * Message Delivery Notification as specified by RFC 2298.
     * 
     * @param humanText
     * @param reporting_UA_name
     * @param reporting_UA_product
     * @param original_recipient
     * @param final_recipient
     * @param original_message_id
     * @param disposition
     * @return MimeMultipartReport
     * @throws MessagingException
     */
    static public MimeMultipartReport create(String humanText,
            String reporting_UA_name,
            String reporting_UA_product,
            String original_recipient,
            String final_recipient,
            String original_message_id,
            Disposition disposition) throws MessagingException
    {
        // Create the message parts. According to RFC 2298, there are two
        // compulsory parts and one optional part...
        MimeMultipartReport multiPart = new MimeMultipartReport();
        multiPart.setReportType("disposition-notification");
        
        // Part 1: The 'human-readable' part
        MimeBodyPart humanPart = new MimeBodyPart();
        humanPart.setText(humanText);
        multiPart.addBodyPart(humanPart);

        // Part 2: MDN Report Part
        // 1) reporting-ua-field
        StringBuffer mdnReport = new StringBuffer(128);
        mdnReport.append("Reporting-UA: ");
        mdnReport.append((reporting_UA_name == null ? "" : reporting_UA_name));
        mdnReport.append("; ");
        mdnReport.append((reporting_UA_product == null ? "" : reporting_UA_product));
        mdnReport.append("\r\n");
        // 2) original-recipient-field
        if (null != original_recipient)
        {
            mdnReport.append("Original-Recipient: ");
            mdnReport.append("rfc822; ");
            mdnReport.append(original_recipient);
            mdnReport.append("\r\n");
        }
        // 3) final-recipient-field
        mdnReport.append("Final-Recepient: ");
        mdnReport.append("rfc822; ");
        mdnReport.append((final_recipient == null ? "" : final_recipient));
        mdnReport.append("\r\n");
        // 4) original-message-id-field
        mdnReport.append("Original-Message-ID: ");
        mdnReport.append((original_message_id == null ? "" : original_message_id));
        mdnReport.append("\r\n");
        // 5) disposition-field
        mdnReport.append(disposition.toString());
        mdnReport.append("\r\n");
        MimeBodyPart mdnPart = new MimeBodyPart();
        mdnPart.setContent(mdnReport.toString(), "message/disposition-notification");
        multiPart.addBodyPart(mdnPart);

        // Part 3: The optional third part, the original message is omitted.
        // We don't want to propogate over-sized, virus infected or
        // other undesirable mail!
        // There is the option of adding a Text/RFC822-Headers part, which
        // includes only the RFC 822 headers of the failed message. This is
        // described in RFC 1892. It would be a useful addition!        
        return multiPart;
    }

}
