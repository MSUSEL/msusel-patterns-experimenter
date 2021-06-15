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

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.james.Constants;
import org.apache.james.core.MailImpl;
import org.apache.james.services.MailRepository;
import org.apache.james.services.MailStore;
import org.apache.james.util.RFC2822Headers;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.MailAddress;
import org.apache.mailet.Mail;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import java.util.Iterator;

/**
 * Re-spools Mail found in the specified Repository.
 *
 * &lt;mailet match="RecipientIs=respool@localhost" class="FromRepository"&gt;
 *    &lt;repositoryPath&gt; <i>repository path</i> &lt;/repositoryPath&gt;
 *    &lt;processor&gt; <i>target processor</i> &lt;/repositoryPath&gt;
 *    &lt;delete&t; [true|<b>false</b>] &lt;/delete&gt;
 * &lt;/mailet&gt;
 *
 * @version This is $Revision: 1.1.2.4 $
 */
public class FromRepository extends GenericMailet {

    /**
     * The repository from where this mailet spools mail.
     */
    private MailRepository repository;

    /**
     * Whether this mailet should delete messages after being spooled
     */
    private boolean delete = false;

    /**
     * The path to the repository
     */
    private String repositoryPath;

    /**
     * The processor that will handle the re-spooled message(s)
     */
    private String processor;

    /**
     * Initialize the mailet, loading configuration information.
     */
    public void init() {
        repositoryPath = getInitParameter("repositoryPath");
        processor = (getInitParameter("processor") == null) ? Mail.DEFAULT : getInitParameter("processor");

        try {
            delete = (getInitParameter("delete") == null) ? false : new Boolean(getInitParameter("delete")).booleanValue();
        } catch (Exception e) {
            // Ignore exception, default to false
        }

        ComponentManager compMgr = (ComponentManager)getMailetContext().getAttribute(Constants.AVALON_COMPONENT_MANAGER);
        try {
            MailStore mailstore = (MailStore) compMgr.lookup("org.apache.james.services.MailStore");
            DefaultConfiguration mailConf
                = new DefaultConfiguration("repository", "generated:ToRepository");
            mailConf.setAttribute("destinationURL", repositoryPath);
            mailConf.setAttribute("type", "MAIL");
            repository = (MailRepository) mailstore.select(mailConf);
        } catch (ComponentException cnfe) {
            log("Failed to retrieve Store component:" + cnfe.getMessage());
        } catch (Exception e) {
            log("Failed to retrieve Store component:" + e.getMessage());
        }
    }

    /**
     * Spool mail from a particular repository.
     *
     * @param triggering e-mail (eventually parameterize via the
     * trigger message)
     */
    public void service(Mail trigger) throws MessagingException {
        trigger.setState(Mail.GHOST);
        java.util.Collection processed = new java.util.ArrayList();
        Iterator list = repository.list();
        while (list.hasNext()) {
            String key = (String) list.next();
            try {
                MailImpl mail =  repository.retrieve(key);
                if (mail != null && mail.getRecipients() != null) {
                    log((new StringBuffer(160).append("Spooling mail ").append(mail.getName()).append(" from ").append(repositoryPath)).toString());

                    /*
                    log("Return-Path: " + mail.getMessage().getHeader(RFC2822Headers.RETURN_PATH, ", "));
                    log("Sender: " + mail.getSender());
                    log("To: " + mail.getMessage().getHeader(RFC2822Headers.TO, ", "));
                    log("Recipients: ");
                    for (Iterator i = mail.getRecipients().iterator(); i.hasNext(); ) {
                        log("    " + ((MailAddress)i.next()).toString());
                    };
                    */

                    mail.setAttribute("FromRepository", Boolean.TRUE);
                    mail.setState(processor);
                    getMailetContext().sendMail(mail);
                    if (delete) processed.add(key);
                }
            } catch (MessagingException e) {
                log((new StringBuffer(160).append("Unable to re-spool mail ").append(key).append(" from ").append(repositoryPath)).toString(), e);
            }
        }
        if (delete) repository.remove(processed);
    }

    /**
     * Return a string describing this mailet.
     *
     * @return a string describing this mailet
     */
    public String getMailetInfo() {
        return "FromRepository Mailet";
    }
}
