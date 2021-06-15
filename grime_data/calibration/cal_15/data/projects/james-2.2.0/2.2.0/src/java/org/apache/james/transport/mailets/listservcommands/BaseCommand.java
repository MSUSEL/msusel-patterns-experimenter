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

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.james.services.UsersRepository;
import org.apache.james.transport.mailets.ICommandListservManager;
import org.apache.james.util.RFC2822Headers;
import org.apache.james.util.XMLResources;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.MailetContext;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * BaseCommand is a convience base class for any class that wishes to implement {@link IListServCommand}.
 * It provides some functions like:
 * <ul>
 *  <li>{@link #log}
 *  <li>{@link #sendStandardReply}
 *  <li>{@link #generateMail}
 * </ul>
 *
 * <br />
 * <br />
 *
 * @version CVS $Revision: 1.1.2.4 $ $Date: 2004/04/16 20:59:53 $
 * @since 2.2.0
 * @see org.apache.james.transport.mailets.CommandListservManager
 */
public abstract class BaseCommand implements IListServCommand {

    protected Configuration configuration;
    protected ICommandListservManager commandListservManager;
    protected String commandName;
    protected MailetContext mailetContext;

    /**
     * Perform any required initialization
     * @param configuration
     * @throws ConfigurationException
     */
    public void init(ICommandListservManager commandListservManager, Configuration configuration) throws ConfigurationException {
        this.commandListservManager = commandListservManager;
        this.configuration = configuration;
        commandName = configuration.getAttribute("name");
        mailetContext = this.commandListservManager.getMailetConfig().getMailetContext();
        log("Initialized listserv command: [" + commandName + ", " + getClass().getName() + "]");
    }

    /**
     * The name of this command
     * @see IListServCommand#getCommandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @see Configuration
     */
    protected Configuration getConfiguration() {
        return configuration;
    }

    /**
     * The list serv manager
     * @return {@link ICommandListservManager}
     */
    protected ICommandListservManager getCommandListservManager() {
        return commandListservManager;
    }

    /**
     * The current mailet context
     * @return {@link MailetContext}
     */
    protected MailetContext getMailetContext() {
        return mailetContext;
    }

    /**
     * @see ICommandListservManager#getUsersRepository
     */
    protected UsersRepository getUsersRepository() {
        return commandListservManager.getUsersRepository();
    }

    /**
     * Writes the specified message to a mailet log file, prepended by
     * the mailet's name.
     *
     * @param message - a String specifying the message to be written to the log file
     */
    protected void log(String message) {
        StringBuffer logBuffer =
                new StringBuffer(256)
                .append(getCommandName())
                .append(": ")
                .append(message);
        mailetContext.log(logBuffer.toString());
    }

    /**
     * Writes an explanatory message and a stack trace for a given Throwable
     * exception to the mailet log file, prepended by the mailet's name.
     *
     * @param message - a String that describes the error or exception
     * @param t - the java.lang.Throwable error or exception
     */
    protected void log(String message, Throwable t) {
        StringBuffer logBuffer =
                new StringBuffer(256)
                .append(getCommandName())
                .append(": ")
                .append(message);
        mailetContext.log(logBuffer.toString(), t);
    }

    /**
     * Produces a standard response replyAddress to the sender
     * @param origMail
     * @param subject
     * @param message
     * @param replyAddress an optional custom replyAddress address
     * @throws MessagingException
     *
     * @see #generateMail
     * @see MailetContext#sendMail
     */
    protected void sendStandardReply(Mail origMail, String subject, String message, String replyAddress) throws MessagingException {
        MailAddress senderAddress = origMail.getSender();
        try {
            MimeMessage mimeMessage = generateMail(senderAddress.toString(),
                    senderAddress.getUser(),
                    getCommandListservManager().getListOwner(),
                    getCommandListservManager().getListName(true),
                    subject,
                    message);
            if (replyAddress != null) {
                mimeMessage.setHeader(RFC2822Headers.REPLY_TO, replyAddress);
            }

            getMailetContext().sendMail(mimeMessage);
        } catch (Exception e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    /**
     * Use this to get standard properties for future calls to {@link org.apache.james.util.XMLResources}
     * @return properties with the "LIST_NAME" and the "DOMAIN_NAME" properties
     */
    protected Properties getStandardProperties() {
        return commandListservManager.getStandardProperties();
    }

    /**
     * Send mail
     *
     * @param destEmailAddr the destination email addr: user@server.com
     * @param destDisplayName the display name
     * @param fromEmailAddr
     * @param fromDisplayName
     * @param emailSubject
     * @param emailPlainText
     * @throws Exception
     */
    protected MimeMessage generateMail(String destEmailAddr,
                                       String destDisplayName,
                                       String fromEmailAddr,
                                       String fromDisplayName,
                                       String emailSubject,
                                       String emailPlainText) throws Exception {
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(System.getProperties(), null));

        InternetAddress[] toAddrs = InternetAddress.parse(destEmailAddr, false);
        toAddrs[0].setPersonal(destDisplayName);
        InternetAddress from = new InternetAddress(fromEmailAddr);
        from.setPersonal(fromDisplayName);

        message.setRecipients(Message.RecipientType.TO, toAddrs);
        message.setFrom(from);
        message.setSubject(emailSubject);
        message.setSentDate(new java.util.Date());

        MimeMultipart msgbody = new MimeMultipart();
        MimeBodyPart html = new MimeBodyPart();
        html.setDataHandler(new DataHandler(new MailDataSource(emailPlainText, "text/plain")));
        msgbody.addBodyPart(html);
        message.setContent(msgbody);
        return message;
    }

    protected XMLResources[] initXMLResources(String[] names) throws ConfigurationException {
        return commandListservManager.initXMLResources(names);
    }
}
