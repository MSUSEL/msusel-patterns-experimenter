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
package com.ivata.groupware.business.mail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecurityServer;
import com.ivata.groupware.admin.security.server.SecurityServerException;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsDataTypeException;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressConstants;
import com.ivata.groupware.business.addressbook.telecomaddress.TelecomAddressDO;
import com.ivata.groupware.business.drive.file.FileContentDO;
import com.ivata.groupware.business.drive.file.FileDO;
import com.ivata.groupware.business.mail.message.MessageDO;
import com.ivata.groupware.business.mail.message.MessageNotFoundException;
import com.ivata.groupware.business.mail.server.MailServer;
import com.ivata.groupware.business.mail.server.NoMailServerException;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.groupware.web.format.EmailAddressFormatter;
import com.ivata.groupware.web.format.SanitizerFormat;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SerializedByteArray;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.format.CharacterEntityFormat;
import com.ivata.mask.web.format.FormatConstants;
import com.ivata.mask.web.format.HTMLFormatter;
import com.ivata.mask.web.format.LineBreakFormat;
import com.ivata.mask.web.tag.webgui.list.ListColumnComparator;


/**
 * <p>This session bean provides an interface to the mail system. Every mail
 * operation for retrieving deleting and sending messages takes place in this
 * class.</p>
 *
 * @since 2002-07-12
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @author Peter Illes
 * @version $Revision: 1.6.2.1 $
 */
public class MailImpl implements Mail, Serializable {
    /**
     * <p>Used to return both the HTML & plain text parts of  a message in
     * <code>createThreadMessage</code>.</p>
     */
    private class MessageTextParts {
        public MimeBodyPart HTMLPart = null;
        public MimeBodyPart textPart = null;
    }

    /**
     * Logger for this class.
     */
    private static Logger logger = Logger.getLogger(MailImpl.class);
    private AddressBook addressBook;
    private SettingDateFormatter dateFormatter = null;
    private MailServer mailServer;
    MaskFactory maskFactory;
    /**
     * <p>
     * Settings implementation. Used to retrieve the email address host.
     * </p>
     */
    private Settings settings;


    /**
     * <p>
     * Initialize the mail implementation.
     * </p>
     *
     * @param securityServer A valid security server for the current site. If
     * this is not an instance of {@link com.ivata.groupware.business.mail.server.MailServer}
     * the mail implementation will not be usable.
     * @param persistenceManager This is used to save/access data from the
     * persistence store.
     * @param addressBook This is used to read contacts email details.
     * @param settings Contains user defined settings and preferences.
     * @param dateFormatter Used to format mail dates and times.
     * @param idDispenser
     */
    public MailImpl(SecurityServer securityServer,
            AddressBook addressBook,
            Settings settings,
            MaskFactory maskFactory) {
        assert (securityServer != null);
        if (securityServer instanceof MailServer) {
            this.mailServer = (MailServer) securityServer;
        } else {
            logger.warn("Security server class ("
                    + securityServer.getClass().getName()
                    + ") is not a mail server class.");
        }
        this.settings = settings;
        this.addressBook = addressBook;
        this.maskFactory = maskFactory;
    }
    private void checkDateFormatter(SecuritySession securitySession) {
        if (dateFormatter == null) {
            dateFormatter = new SettingDateFormatter(securitySession,
                settings);
        }
    }
    /**
     * <p>Add a composed message to the drafts folder for later sending.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param messageDO data object containing full details of the
     *     message to be added to the drafts.
     * @return new <code>MessageDO</code> with the <code>id</code> set to the
     *     current value in the mail system.
     */
    public MessageDO addMessageToDraftsFolder(final MailSession mailSession,
            final MessageDO messageDO) throws SystemException {
        checkDateFormatter(mailSession);

        Store store = mailServer.connectStore(mailSession);
        try {
            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (java.security.NoSuchProviderException e1) {
                throw new SystemException(e1);
            }

            // get the drafts folder in case we want to copy over an older mail
            Folder draftsFolder = openDraftsFolder(store, mailSession);

            MimeMessage newMessage = setDOToJavaMailMessage(javaMailSession,
                    draftsFolder, messageDO);

            newMessage.setSentDate(Calendar.getInstance().getTime());

            // append the new message to the drafts folder
            Message[] messages = { newMessage };

                draftsFolder.appendMessages(messages);

            // note the new id
            messageDO.setMessageID(((MimeMessage) draftsFolder.getMessage(
                    draftsFolder.getMessageCount())).getMessageID());

            // only now can we delete/expunge the old mail from the drafts folder
            draftsFolder.expunge();
            draftsFolder.close(true);
        } catch (MessagingException e1) {
            throw new SystemException(e1);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return messageDO;
    }

    /**
     * <p>Recursive routine used for building up all attachments in the
     * <code>MessageDO<code> provided.</p>
     *
     * @param messagePart the multipart part or message to process.
     * @param messageDO the data object to add results to.
     * @throws GroupwareException if there is a <code>MailMessagingException</code> or
     *   <code>IOException</code>.
     */
    private void addMultiPart(final Part messagePart,
            final MessageDO messageDO)
            throws SystemException {
        String outputText = "";
        MimeMultipart content;
        MimeBodyPart subPart;

        List messageTextParts = new Vector();

        try {
            content = (MimeMultipart) messagePart.getContent();

            //go through all the subParts
            for (int i = 0; i < content.getCount(); i++) {
                subPart = (MimeBodyPart) content.getBodyPart(i);

                // when multipart/alternative and no text found in parent call,
                // store the text of the parts to select the best one after the loop
                if (messagePart.isMimeType("multipart/alternative") &&
                        subPart.isMimeType("text/*") &&
                        StringHandling.isNullOrEmpty(messageDO.getText())) {
                    messageTextParts.add(subPart);
                } else if (messagePart.isMimeType("multipart/*")) {
                    // other multipart types
                    if (StringHandling.isNullOrEmpty(messageDO.getText()) &&
                            (subPart.getDisposition() == null) &&
                            (subPart.getFileName() == null)) {
                        if (subPart.isMimeType("text/*")) {
                            messageTextParts.add(subPart);
                        } else if (subPart.isMimeType("multipart/*")) {
                            addMultiPart((Part) subPart, messageDO);
                        } else {
                            addPart(subPart, messageDO);
                        }
                    } else {
                        if (subPart.isMimeType("multipart/*")) {
                            addMultiPart((Part) subPart, messageDO);
                        } else {
                            addPart(subPart, messageDO);
                        }
                    }
                }
            }

            // looking for best message text
            if (!messageTextParts.isEmpty()) {
                String HTML = null;
                String text = null;

                // let's choose the best text
                for (Iterator i = messageTextParts.iterator(); i.hasNext();) {
                    subPart = (MimeBodyPart) i.next();

                    if (subPart.isMimeType("text/HTML")) {
                        HTML = (String) subPart.getContent();
                    } else if (subPart.isMimeType("text/plain")) {
                        text = (String) subPart.getContent();
                    }
                     // TODO: we could use text/enriched too
                }

                if (HTML != null) {
                    messageDO.setText(HTML);
                    messageDO.setFormat(FormatConstants.FORMAT_HTML);
                } else if (text != null) {
                    messageDO.setText(text);
                    messageDO.setFormat(FormatConstants.FORMAT_TEXT);
                }
            }
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (java.io.IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Add a part of a multi-part message to the attachments of the message
     * data object.</p>
     *
     * @param part the message part to process.
     * @param messageDO the data object to add results to.
     * @throws GroupwareException if there is a <code>MessagingException</code> or an
     *     <code>IOException</code>.
     */
    private void addPart(final MimePart part,
            final MessageDO messageDO)
        throws SystemException {
        FileDO attachment = new FileDO();

        try {
            attachment.setMimeType(part.getContentType());
            attachment.setSize(new Integer(part.getSize()));

            String contentId = part.getContentID();
            String name = part.getFileName();

            // return with empty hands if no identifier, the attachment will be
            // impossible to locate
            if ((contentId == null) && (name == null)) {
                return;
            }

            // prefer contentId as identifier and name as the display info
            attachment.setName((contentId != null) ? contentId : name);
            attachment.setComment((name != null) ? name : contentId);
        } catch (MessagingException e) {
            throw new SystemException(e);
        }

        messageDO.getAttachments().add(attachment);
    }

    /**
     * <p>Internal helper to add a message to the sent mail folder. This should
     * be called <em><u>after</u></em> sending the mail.</p>
     */
    private void addToSentFolder(final MailSession mailSession,
            final MimeMessage message)
            throws SystemException {
        Folder sentFolder;

        Store store = mailServer.connectStore(mailSession);
        try {
            String sentFolderName = settings.getStringSetting(
                    mailSession,
                    "emailFolderSent",
                    mailSession.getUser());
            sentFolder = mailServer.getFolder(mailSession, store, sentFolderName);

            if (!sentFolder.exists()) {
                try {
                    if (!sentFolder.create(Folder.HOLDS_MESSAGES)) {
                        throw new SystemException(
                            "There was no sent folder for you on this server, "
                            + "and ivata mail could not create one.<br>Please "
                            + "contact your administrator.");
                    }
                } catch (MessagingException e1) {
                    throw new SystemException(e1);
                }
            }

            Message[] messages = { message };

            try {
                sentFolder.appendMessages(messages);
            } catch (MessagingException eAppend) {
                throw new SystemException(
                    "There was an error adding your message to the sent "
                    + "messages folder: " +
                    eAppend.getMessage(),
                    eAppend);
            }
        } catch (MessagingException eNoSent) {
            throw new SystemException("Sent folder not available in store. " +
                eNoSent.getMessage(),
                eNoSent);
        } catch (SettingsDataTypeException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }

    /**
     * <p>
     * Add appropriate user addresses given a list of user aliases.
     * </p>
     *
     * @param securitySession valid security session.
     * @param userName name of the user who owns teh aliases.
     * @param userAliases a <code>Collection</code> of <code>String</code>
     *     instances containing the local part of the different email aliases
     *     this user has. If the user has no aliaes, an empty collection should
     *     be provided.
     * @param telecomAddresess a <code>Collection</code> containing all the
     *     user's existing email addresses, as <code>TelecomAddressDO</code>
     *     instances.
     */
    public void addUserAliasEmailAddresses(final SecuritySession securitySession,
            final String userName,
            final Collection userAliases,
            final Collection telecomAddresses,
            final String emailAddressHost)
            throws SystemException {
        checkDateFormatter(securitySession);
        Iterator telecomAddressIterator = telecomAddresses.iterator();
        List currentAddresses = new Vector();
        while (telecomAddressIterator.hasNext()) {
            TelecomAddressDO thisTelecomAddress = (TelecomAddressDO)
                telecomAddressIterator.next();
            if (!StringHandling.isNullOrEmpty(thisTelecomAddress.getAddress())
                    && (thisTelecomAddress.getType() == TelecomAddressConstants.TYPE_EMAIL)) {
                currentAddresses.add(thisTelecomAddress.getAddress());
            }
        }

        // if the person has no email address, give him/her one
        // check there is at least one alias - the following routine will
        // do the rest
        // we make it conditional because you might not always want to have
        // user@host as your email address - this way, you can specify a
        // different one
        if ((currentAddresses.size() == 0)
                && (userAliases.size() == 0)) {
            userAliases.add(userName);
        }

        // go thro' all aliases and create email addreses from them
        Iterator aliasIterator = userAliases.iterator();
        while(aliasIterator.hasNext()) {
            String alias = (String) aliasIterator.next();
            String aliasAddress = alias + "@" + emailAddressHost;
            // if it is already there, move on...
            if (currentAddresses.contains(aliasAddress)) {
                continue;
            }
            TelecomAddressDO newAddress = new TelecomAddressDO();
            newAddress.setAddress(aliasAddress);
            newAddress.setType(TelecomAddressConstants.TYPE_EMAIL);
            newAddress.setNumber(telecomAddresses.size());
            telecomAddresses.add(newAddress);
        }
    }

    /**
     * <p>Append attachments to a message located in the drafts folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param id the unique identifier of the message to which we want to append
     *     attachments.
     * @param attachments <code>List</code> of <code>String</code>s -
     *     filenames of files waiting in upload directory.
     * @return <code>null</code> when the operation failed, otherwise the new
     *     message id.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public MessageDO appendAttachments(final MailSession mailSession,
            final String id,
            final List attachments) throws SystemException {
        checkDateFormatter(mailSession);
        String newId = null;
        UserDO user = mailSession.getUser();
        Store store = mailServer.connectStore(mailSession);

        try {
            String siteHome = settings.getStringSetting(mailSession,
                "siteHome", user);
            String uploadDirectory = siteHome + "/users/" +
                mailSession.authenticator.getPasswordAuthentication()
                                         .getUserName() + "/upload/files/";

            Session javaMailSession;

            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (SecurityServerException e) {
                throw new SystemException(e);
            } catch (java.security.NoSuchProviderException e) {
                throw new SystemException(e);
            }

            Folder draftsFolder = openDraftsFolder(store, mailSession);
            MimeMessage oldMessage = findJavaMailMessageByFolderMessageId(draftsFolder,
                    id);
            int i;
            MimeBodyPart newPart;
            MimeMultipart newMessageContent = new MimeMultipart();
            MimeMultipart oldMessageContent;
            MimeMessage newMessage = copyJavaMailMessage(javaMailSession,
                    oldMessage);

            // when the message is already multipart/mixed, no probs...
            if (oldMessage.isMimeType("multipart/mixed")) {
                oldMessageContent = (MimeMultipart) oldMessage.getContent();

                for (i = 0; i < oldMessageContent.getCount(); i++) {
                    newPart = (MimeBodyPart) oldMessageContent.getBodyPart(i);
                    newMessageContent.addBodyPart(newPart);
                }
            } else {
                // create the first part from the old message, attachments will be appended
                newPart = new MimeBodyPart();
                newPart.setContent(oldMessage.getContent(),
                    oldMessage.getContentType());
                newPart.setHeader("Content-Type", oldMessage.getContentType());
                newMessageContent.addBodyPart(newPart);
            }

            // add the attachments, passed as fileNames of files in upload dir
            for (Iterator attachmentsIterator = attachments.iterator();
                    attachmentsIterator.hasNext();) {
                File attachment = new File(uploadDirectory,
                        (String) attachmentsIterator.next());

                // process the file in upload directory
                if (attachment.canRead()) {
                    newPart = new MimeBodyPart();
                    newPart.setFileName(attachment.getName());
                    newPart.setDisposition(Part.ATTACHMENT);

                    DataSource dataSource = new FileDataSource(attachment);
                    newPart.setDataHandler(new DataHandler(dataSource));
                    newPart.setHeader("Content-Type",
                        dataSource.getContentType());
                    newPart.setHeader("Content-Transfer-Encoding", "base64");
                    newMessageContent.addBodyPart(newPart);
                }
            }

            newMessage.setContent(newMessageContent);
            newMessage.setHeader("Content-Type",
                newMessageContent.getContentType());

            // first append the new message
            Message[] messages = { newMessage };

            draftsFolder.appendMessages(messages);

            // note the new id
            newId = ((MimeMessage) draftsFolder.getMessage(draftsFolder.getMessageCount())).getMessageID();

            // only now is it safe to delete the old message
            oldMessage.setFlag(Flags.Flag.DELETED, true);
            draftsFolder.expunge();

            // now it's safe to delete the files in upload dir, they're in the new multipart
            for (Iterator attachmentsIterator = attachments.iterator();
                    attachmentsIterator.hasNext();) {
                File attachment = new File(uploadDirectory,
                        (String) attachmentsIterator.next());

                if (attachment.canWrite()) {
                    attachment.delete();
                }
            }

            // MessageDO returnDO = getDOFromJavaMailMessage(newMessage, true);
            MessageDO returnDO = getDOFromJavaMailMessage(
                    findJavaMailMessageByFolderMessageId(
                    draftsFolder, newId), true);
            draftsFolder.close(true);
            return returnDO;
        } catch (MessagingException em) {
            throw new SystemException(em);
        } catch (IOException eio) {
            throw new SystemException(eio);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }

    /**
     * <p>
     * Check we have a valid mail server.
     * </p>
     *
     * @throws NoMailServerException if there is no mail server.
     */
    private void checkMailServer() throws SystemException {
        if (mailServer == null) {
            logger.warn("No mail server found.");
            throw new NoMailServerException();
        }
    }

    /**
     * <p>Helper method. Converts recipients from a collection of
     * <code>PersonDO</code>, <code>UserDO</code> or <code>String<code>
     * instances into an array of email addresses.</p>
     * @param securitySession TODO
     * @param addresses a <code>Collection</code> containing all of the email
     *      addresses to convert. These can either be as <code>String<code>
     *      instances or <code>PersonDO<code> instances, where the default
     *      email address for each person is taken.
     *
     * @return array or <code>InternetAddress</code> instances for each of the
     *      input parameters.
     */
    private InternetAddress[] convertAddresses(final SecuritySession securitySession,
            final Collection addresses)
            throws SystemException {
        InternetAddress[] returnAddresses = new InternetAddress[addresses.size()];

        // prerequisites check we got given something to convert
        if (addresses == null) {
            return returnAddresses;
        }

        int index = 0;

        for (Iterator i = addresses.iterator(); i.hasNext();) {
            Object item = i.next();
            String addressString = null;

            if (PersonDO.class.isInstance(item)) {
                PersonDO person = (PersonDO) item;

                addressString = person.getEmailAddress();
            } else if (UserDO.class.isInstance(item)) {
                UserDO user = (UserDO) item;
                PersonDO person = addressBook.findPersonByUserName(securitySession,
                        user.getName());

                // only set the address for users who are not disabled
                if (user.isEnabled()) {
                    addressString = person.getEmailAddress();
                }
            } else {
                if (!String.class.isInstance(item)) {
                    throw new SystemException("Cannot convert item of class '" +
                        item.getClass() + "' into an email address.");
                }

                addressString = (String) item;
            }

            // ignore empty addresses
            if (!StringHandling.isNullOrEmpty(addressString)) {
                try {
                    returnAddresses[index++] = new InternetAddress(addressString);
                } catch (AddressException eAddress) {
                    throw new SystemException(
                        "ERROR in MailBean: cannot convert internet address '"
                            + addressString
                            + "': "
                            + eAddress.getMessage(),
                            eAddress);
                }
            }
        }

        return returnAddresses;
    }

    /**
     * <p>Copy the fields of an old <code>MimeMessage</code> to a new one.</p>
     *
     * <p><strong>Note:</strong> this method does not copy the content. Both the text
     * and the message attachments (if any) must be set individually.</p>
     *
     * @param javaMailSession valid <em>JavaMail</em> session to which the user
     *     should already be logged in.
     * @param message a valid message filled out with values to copy.
     * @return a valid <em>JavaMail</em> message ready with <code>recipients</code>,
     *     <code>from</code> and <code>subject</code> fields matching
     *     <code>message</code>.
     */
    private MimeMessage copyJavaMailMessage(final Session javaMailSession,
            final MimeMessage message)
            throws SystemException {
        MimeMessage newMessage = new MimeMessage(javaMailSession);

        try {
            newMessage.setRecipients(Message.RecipientType.TO,
                message.getRecipients(Message.RecipientType.TO));
            newMessage.setRecipients(Message.RecipientType.CC,
                message.getRecipients(Message.RecipientType.CC));
            newMessage.setRecipients(Message.RecipientType.BCC,
                message.getRecipients(Message.RecipientType.BCC));
            newMessage.addFrom(message.getFrom());
            newMessage.setSubject(message.getSubject());
        } catch (MessagingException e) {
            throw new SystemException(e);
        }

        return newMessage;
    }

    /**
     * <p>Convert a <em>JavaMail</em> message to an <em>ivata groupware</em> dependent
     * value object.</p>
     *
     * @param message a valid <em>JavaMail</em> message to be converted.
     * @param includeContent <code>true</code> if the <code>text</code> and
     *     attachments of the message should also be set, otherwise
     *     <code>false</code>.
     * @return message data object with the values filled out to match
     *     the <em>JavaMail</em> object.
     */
    private MessageDO createDOFromJavaMailMessage(final MimeMessage message,
            final boolean includeContent)
            throws SystemException {
        // right - we got here, so that means we have a message
        MessageDO messageDO = new MessageDO();
        try {
            //setting the fields of the MessageDO:
            if (message.getFolder() != null) {
                messageDO.setFolderName(message.getFolder().getName());
            }

            if (message.getReceivedDate() != null) {
                GregorianCalendar receivedDate = new GregorianCalendar();

                receivedDate.setTime(message.getReceivedDate());
                messageDO.setReceived(receivedDate);
            }

            if (message.getRecipients(Message.RecipientType.TO) != null) {
                messageDO.setRecipients(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.TO))));
            }

            if (message.getRecipients(Message.RecipientType.CC) != null) {
                messageDO.setRecipientsCC(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.CC))));
            }

            if (message.getRecipients(Message.RecipientType.BCC) != null) {
                messageDO.setRecipientsBCC(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.BCC))));
            }

            if (message.getFrom() != null) {
                messageDO.setSenders(Arrays.asList(toStringArray(
                            message.getFrom())));
            }

            if (message.getSentDate() != null) {
                GregorianCalendar sentDate = new GregorianCalendar();

                sentDate.setTime(message.getSentDate());
                messageDO.setSent(sentDate);
            }

            messageDO.setSize(new Integer(message.getSize()));
            messageDO.setSubject(message.getSubject());

            // message content handling - not always done for efficiency in lists
            if (includeContent) {
                Integer format;
                String text;

                // create new, empty List for our attachments
                messageDO.setAttachments(new Vector());

                // if it is a multipart message (has attachments), pass control to
                // recursive routine to go thro' them all
                if (message.isMimeType("multipart/*")) {
                    addMultiPart(message, messageDO);

                    // here are types with textual content
                } else if (message.isMimeType("text/*") ||
                        message.isMimeType("message/*")) {
                    // if it is not multipart, we're just left with text or HTML
                    if (message.isMimeType("text/HTML")) {
                        // simple message with HTML content
                        messageDO.setFormat(FormatConstants.FORMAT_HTML);
                    } else {
                        // anything else with simple content should have text content
                        messageDO.setFormat(FormatConstants.FORMAT_TEXT);
                    }

                    messageDO.setText((String) message.getContent());

                    // other (not correct?) types, as self-contained attachments...
                } else {
                    messageDO.setFormat(FormatConstants.FORMAT_TEXT);
                    messageDO.setText("");
                    addPart(message, messageDO);
                }
            }
            messageDO.setMessageID(message.getMessageID());
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }

        return messageDO;
    }

    /**
     * <p>Create a new mail folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the full path name of the folder to create.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void createFolder(final MailSession mailSession,
            final String folderName)
            throws SystemException {
        assert (mailSession != null);
        checkDateFormatter(mailSession);

        Session javaMailSession;

        try {
            javaMailSession = mailSession.getJavaMailSession();
        } catch (AuthenticationFailedException e) {
            throw new SystemException(
                "User is no longer authorized to use this server: " +
                e.getMessage(),
                e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (SecurityServerException e) {
            throw new SystemException(e);
        } catch (java.security.NoSuchProviderException e) {
            throw new SystemException(e);
        }

        Store store = mailServer.connectStore(mailSession);
        try {
            if (folderName == null) {
                throw new SystemException(
                    "ERROR in MailBean.createFolder: folderName is null");
            }

            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            if (!folder.create(Folder.HOLDS_MESSAGES)) {
                throw new SystemException(
                    "ERROR in MailBean.createFolder: could not create folder '" +
                    folderName + "'");
            }
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }


    /**
     * <p>Helper method for <code>createThreadMethod</code>.</p>
     *
     * <p>Create a new message in the drafts folder from an existing one,
     * resulting in a forwarded message.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder to copy existing messages from.
     * @param messageIds the unique identifier of the messages to be extended.
     *     Can be <code>null</code> if a new message is requeested. When
     *     forwarding, multiple address identifiers may be specified otherwise
     *     (if editing a draft message or replying) only one message identifier
     *     should be set in the list.
     * @param thread set to one of the constants in {@link MailConstants
     *     MailConstants}.
     * @return populated message data object matching the required
     *     message, and with the <code>id</code> set to the message in the
     *     drafts folder.
     */
    private MimeMessage createForwardedMessage(final MailSession mailSession,
            final Folder folder,
            final List messageIds)
            throws SystemException {
        checkDateFormatter(mailSession);
        try {
            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (java.security.NoSuchProviderException e1) {
                throw new SystemException(e1);
            }
            UserDO user = mailSession.getUser();

            // if this is HTML, we'll need to store multipart data
            MessageTextParts messageTextParts = null;

            // first go thro' all the messages and see if there are _any_ which
            // are multipart
            boolean isMultipart = false;
            int format = FormatConstants.FORMAT_TEXT;

            for (Iterator i = messageIds.iterator(); i.hasNext();) {
                String id = (String) i.next();
                MimeMessage oldMessage = findJavaMailMessageByFolderMessageId(folder,
                        id);

                // is this multipart?
                if (oldMessage.isMimeType("multipart/*")) {
                    isMultipart = true;

                    // try to find an HTML subpart
                    messageTextParts = getMultiPartText(oldMessage);

                    if (messageTextParts.HTMLPart != null) {
                        format = FormatConstants.FORMAT_HTML;

                        // no need to check any further...
                        break;
                    }
                }
            }

            // text header/prefix depends on the format
            String messageHeader;

            if (format == FormatConstants.FORMAT_HTML) {
                messageHeader = settings.getStringSetting(mailSession,
                        "emailHeaderForwardHTML",
                        user);
            } else {
                messageHeader = settings.getStringSetting(mailSession,
                        "emailHeaderForwardText",
                        user);
            }

            MimeMessage newMessage = new MimeMessage(javaMailSession);
            StringBuffer subject = new StringBuffer();
            String subjectPrefix = settings.getStringSetting(mailSession,
                    "emailSubjectForwardPrefix",
                    user);
            String subjectSeperator = settings.getStringSetting(mailSession,
                    "emailSubjectForwardSeperator",
                    user);

            subject.append(subjectPrefix);

            StringBuffer messageText = new StringBuffer();

            // we'll format the reply text, if it is text & this is HTML
            CharacterEntityFormat characterEntities = new CharacterEntityFormat();

            // go thro' all of the old ids again, this time to add the content
            int index = 0;

            for (Iterator i = messageIds.iterator(); i.hasNext(); ++index) {
                String id = (String) i.next();
                MimeMessage oldMessage = findJavaMailMessageByFolderMessageId(folder,
                        id);

                // prepend Re: or Fwd: unless the previous subject already starts like this
                String oldSubject = StringHandling.getNotNull(oldMessage.getSubject(),
                        getNullString());

                // if there is a fwd: prefix check this message doesn't start with
                // that
                if ((subjectPrefix != null) &&
                        (oldSubject.startsWith(subjectPrefix))) {
                    oldSubject = oldSubject.substring(subjectPrefix.length());
                }

                // if there is more than one forwarded message, append separator
                // between the subjects
                if ((index > 0) && !oldSubject.equals("")) {
                    subject.append(subjectSeperator);
                }

                subject.append(oldSubject);

                // locate the multipart in the new message, for multiparts
                String oldMessageText = null;
                int oldFormat = FormatConstants.FORMAT_TEXT;

                if (oldMessage.isMimeType("multipart/*")) {
                    // if there is an HTML text, use that
                    if (messageTextParts.HTMLPart != null) {
                        oldMessageText = (String) messageTextParts.HTMLPart.getContent();
                        oldFormat = FormatConstants.FORMAT_HTML;
                    } else if (messageTextParts.textPart != null) {
                        oldMessageText = (String) messageTextParts.textPart.getContent();
                    }
                } else {
                    oldMessageText = (String) oldMessage.getContent();
                }

                // if we have to build the text with a prefix/header, do that
                if ((oldMessageText != null) && (messageHeader != null)) {
                    messageText.append(formatMessageHeader(messageHeader,
                            oldMessage));
                }

                if (oldMessageText != null) {
                    // we have to watch it if the old format wasn't HTML before
                    if ((format == FormatConstants.FORMAT_HTML) &&
                            !(oldFormat == FormatConstants.FORMAT_HTML)) {
                        messageText.append("<pre>\n");
                        oldMessageText = characterEntities.format(oldMessageText);
                    }

                    messageText.append(oldMessageText);

                    if ((format == FormatConstants.FORMAT_HTML) &&
                            !(oldFormat == FormatConstants.FORMAT_HTML)) {
                        messageText.append("\n</pre>\n");
                    }
                }
            }

            if (format == FormatConstants.FORMAT_HTML) {
                // we want to work with copies of the original texts,
                // create a new HTML content multipart
                MimeMultipart newTextContent = createHTMLContent(messageText.toString());

                // set the multipart/alternative as the content of the base part
                newMessage.setContent(newTextContent);
                newMessage.setHeader("Content-Type",
                    newTextContent.getContentType());

                // plain text replies and forwards are easier
            } else {
                newMessage.setContent(messageText.toString(), "text/plain");
            }

            return newMessage;
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Create text content of a message, if the text is <code>HTML</code>
     * format. This actually creates an alternative multipart type, and loads
     * both the <code>HTML</code> content and the equivalent plain text.</p>
     *
     * @param hTMLText the new HTML text of the body content
     * @exception MessagingException thrown by <code>MimeBodyPart.setContent</code>
     * @return the newx content of the alternative multipart message content.
     */
    private MimeMultipart createHTMLContent(final String hTMLText)
        throws MessagingException {
        // HTML desired, the base part will be multipart/alternative
        MimeMultipart newTextContent = new MimeMultipart("alternative");

        // the richer format the last, so plain text first
        MimeBodyPart mimePart = new MimeBodyPart();
        SanitizerFormat sanitizer = new SanitizerFormat();

        sanitizer.setSourceName("user input");
        sanitizer.setTextOnly(true);
        mimePart.setContent(sanitizer.format(hTMLText), "text/plain");
        mimePart.setHeader("Content-Type", "text/plain");
        newTextContent.addBodyPart(mimePart);

        // TODO: this is not efficient - it parses the HTML tree twice :-(
        // replace with HTMLParser calls directly
        // the HTML part now
        mimePart = new MimeBodyPart();
        sanitizer.setTextOnly(false);
        mimePart.setContent(sanitizer.format(hTMLText), "text/HTML");
        mimePart.setHeader("Content-Type", "text/HTML");
        newTextContent.addBodyPart(mimePart);

        return newTextContent;
    }

    /**
     * <p>Helper method for <code>createThreadMethod</code>.</p>
     *
     * <p>Create a new message in the drafts folder from an existing one,
     * resulting in a message which replies to the original one.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder to copy existing messages from.
     * @param messageIds the unique identifier of the messages to be extended.
     *     Can be <code>null</code> if a new message is requeested. When
     *     forwarding, multiple address identifiers may be specified otherwise
     *     (if editing a draft message or replying) only one message identifier
     *     should be set in the list.
     * @param thread set to one of the congstants in {@link MailConstants
     *     MailConstants}.
     * @return populated message data object matching the required
     *     message, and with the <code>id</code> set to the message in the
     *     drafts folder.
     */
    private MimeMessage createReplyMessage(final MailSession mailSession,
            final Folder folder,
            final String id,
            final Integer thread)
            throws SystemException {
        try {
            UserDO user = mailSession.getUser();
            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (java.security.NoSuchProviderException e) {
                throw new SystemException(e);
            }

            MimeMessage oldMessage = this.findJavaMailMessageByFolderMessageId(folder,
                    id);

            // if this is HTML, we'll need to store multipart data
            MessageTextParts messageTextParts = null;

            // we will build the subject up using a prefix (possibly)
            StringBuffer subject = new StringBuffer();
            String subjectPrefix = null;

            // there may also be a text prefix (= header)
            StringBuffer messageText = new StringBuffer();
            String oldMessageText = null;
            String messageHeader = null;

            // we'll format the reply text
            HTMLFormatter formatter = new HTMLFormatter();

            // mail API methods for reply, replyall
            MimeMessage newMessage = (MimeMessage) oldMessage.reply(MailConstants.THREAD_REPLY_ALL.equals(
                        thread));

            // emailReplyIndent means '> '  (without the quotes)
            // note: HTML formatting might remove this below!!
            LineBreakFormat lineBreaks = new LineBreakFormat();

            lineBreaks.setPrepend(settings.getStringSetting(mailSession,
                    "emailReplyIndent",
                    user));
            formatter.add(lineBreaks);

            // first the subject prefix
            subjectPrefix = settings.getStringSetting(mailSession,
                    "emailSubjectReplyPrefix",
                    user);

            // for the reply we want to make a new multipart, if
            // this is an HTML mail
            if (oldMessage.isMimeType("multipart/*")) {
                messageHeader = settings.getStringSetting(mailSession,
                        "emailHeaderReplyHTML",
                        user);
                messageTextParts = getMultiPartText(oldMessage);
            } else {
                messageHeader = settings.getStringSetting(mailSession,
                        "emailHeaderReplyText",
                        user);
            }

            // prepend Re: unless the previous subject already starts like this
            String oldSubject = StringHandling.getNotNull(oldMessage.getSubject(),
                    getNullString());

            if ((subjectPrefix != null) &&
                    (!oldSubject.startsWith(subjectPrefix))) {
                subject.append(subjectPrefix);
            }

            subject.append(oldSubject);
            newMessage.setSubject(subject.toString());

            // locate the multipart in the new message, for multiparts
            int format = FormatConstants.FORMAT_TEXT;

            if (oldMessage.isMimeType("multipart/*")) {
                // if there is an HTML text, use that
                if (messageTextParts.HTMLPart != null) {
                    oldMessageText = (String) messageTextParts.HTMLPart.getContent();
                    format = FormatConstants.FORMAT_HTML;
                } else if (messageTextParts.textPart != null) {
                    oldMessageText = (String) messageTextParts.textPart.getContent();
                }
            } else {
                oldMessageText = (String) oldMessage.getContent();
            }

            // if we have to build the text with a prefix/header, do that
            if ((oldMessageText != null) && (messageHeader != null)) {
                messageText.append(formatMessageHeader(messageHeader, oldMessage));
            }

            if (oldMessageText != null) {
                messageText.append(oldMessageText);
            }

            if (format == FormatConstants.FORMAT_HTML) {
                // we want to work with copies of the original texts,
                // create a new HTML content multipart
                MimeMultipart newTextContent = createHTMLContent(messageText.toString());

                // set the multipart/alternative as the content of the base part
                newMessage.setContent(newTextContent);
                newMessage.setHeader("Content-Type",
                    newTextContent.getContentType());

                // plain text replies and forwards are easier
            } else {
                newMessage.setContent(messageText.toString(), "text/plain");
            }

            return newMessage;
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Create a new message in the drafts folder from an existing one,
     * based on a 'thread'. The thread indicates that the message is a:<br/>
     * <ul>
     *   <li>reply to all recipients of a previous message</li>
     *   <li>reply to one recipient of a previous message</li>
     *   <li>previous message(s) forwarded to new recipients</li>
     *   <li>an existing (draft) message being altered for resending</li>
     * </ul></p>
     *
     * <p>This new message in the drafts folder can then be used to store
     * attachments or for reviewing before sending.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder to copy existing messages from.
     * @param messageIds the unique identifier of the messages to be extended.
     *     Can be <code>null</code> if a new message is requeested. When
     *     forwarding, multiple address identifiers may be specified otherwise
     *     (if editing a draft message or replying) only one message identifier
     *     should be set in the list.
     * @param thread set to one of the constants in {@link MailConstants
     *     MailConstants}.
     * @return populated message data object matching the required
     *     message, and with the <code>id</code> set to the message in the
     *     drafts folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public MessageDO createThreadMessage(final MailSession mailSession,
            final String folderName,
            final List messageIds,
            final Integer thread)
            throws SystemException {
        checkDateFormatter(mailSession);
        MimeMessage newMessage = null;
        MessageDO messageDO = null;
        Session javaMailSession;

        try {
            javaMailSession = mailSession.getJavaMailSession();
        } catch (AuthenticationFailedException e) {
            throw new SystemException(
                "User is no longer authorized to use this server: " +
                e.getMessage(),
                e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (java.security.NoSuchProviderException e) {
            throw new SystemException(e);
        }

        Store store = mailServer.connectStore(mailSession);
        try {
            // check if everything's set to create based on thread
            if ((folderName == null) || (messageIds == null) ||
                    (messageIds.size() == 0) || (thread == null)) {
                throw new SystemException(
                    "ERROR in MailBean.createThreadMessage: these parameters cannot be empty:" +
                    " folderName (specified: '" + folderName +
                    "') messageIds (specified: '" + messageIds +
                    "') thread (specified: '" + thread + "').");
            }

            // prerequisite - check this is a forward thread if there are > 1 messages
            if ((messageIds.size() > 1) &&
                    !MailConstants.THREAD_FORWARD.equals(thread)) {
                throw new SystemException("ERROR in MailBean: too many messages (" +
                    messageIds.size() +
                    ") in thread. Only 'forward' can have multiple messages.");
            }

            // create a new message appropriate to the thread type requested
            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            folder.open(Folder.READ_ONLY);

            if (MailConstants.THREAD_FORWARD.equals(thread)) {
                newMessage = createForwardedMessage(mailSession, folder,
                        messageIds);
            } else {
                String messageId = (String) messageIds.get(0);

                if (MailConstants.THREAD_DRAFT.equals(thread)) {
                    MimeMessage oldMessage = this.findJavaMailMessageByFolderMessageId(folder,
                            messageId);
                    newMessage = new MimeMessage(oldMessage);
                } else {
                    newMessage = createReplyMessage(mailSession, folder,
                            messageId, thread);
                }
            }

            // now we should have the new message, let's put it to the drafts folder
            Folder draftsFolder = openDraftsFolder(store, mailSession);

            messageDO = getDOFromJavaMailMessage(newMessage, true);

            Message[] messages = { newMessage };

            draftsFolder.appendMessages(messages);
            messageDO.setFolderName(draftsFolder.getName());
            draftsFolder.close(true);
            folder.close(false);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return messageDO;
    }

    /**
     * <p>Delete a list of messages from the trash folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param ids the unique identifiers (<code>String</code> instances) of the
     *     messages to be removed.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void deleteMessagesFromTrash(final MailSession mailSession,
            final List ids)
            throws SystemException {
        checkDateFormatter(mailSession);
        Store store = mailServer.connectStore(mailSession);
        try {
            UserDO user = mailSession.getUser();
            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (java.security.NoSuchProviderException e) {
                throw new SystemException(e);
            }
            Folder trashFolder = mailServer.getFolder(mailSession, store,
                    settings.getStringSetting(
                        mailSession,
                        "emailFolderTrash",
                        user));

            trashFolder.open(Folder.READ_WRITE);

            // now mark all the original messages as deleted
            for (Iterator i = ids.iterator(); i.hasNext();) {
                String id = (String) i.next();
                MimeMessage message = findJavaMailMessageByFolderMessageId(trashFolder,
                        id);

                message.setFlag(Flags.Flag.DELETED, true);
            }

            trashFolder.expunge();
            trashFolder.close(true);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }

    /**
     * <p>Check whether or not a given folder pathname exists.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the full path name of the folder to check.
     * @return <code>true</code> if the folder exists, otherwise <code>false</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public boolean doesFolderExist(final MailSession mailSession,
            final String folderName)
            throws SystemException {
        assert (mailSession != null);
        checkDateFormatter(mailSession);

        Session javaMailSession = null;

        try {
            javaMailSession = mailSession.getJavaMailSession();
        } catch (AuthenticationFailedException e) {
            throw new SystemException(
                "User is no longer authorized to use this server: " +
                e.getMessage(),
                e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (SecurityServerException e) {
            throw new SystemException(e);
        } catch (java.security.NoSuchProviderException e) {
            throw new SystemException(e);
        }

        boolean folderExists = false;

        Store store = mailServer.connectStore(mailSession);
        try {
            if (folderName == null) {
                throw new SystemException(
                    "ERROR in MailBean.doesFolderExist: folderName is null");
            }

            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            folderExists = folder.exists();
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return folderExists;
    }

    /**
     * <p>Locate the message in the folder given with the specified unique
     * identifier.</p>
     *
     * @param folder folder to search for the message in.
     * @param id the unique identifier of the message.
     * @return the message in this folder with the id required, or
     *     <code>null</code> if no such message exists.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     */
    private MimeMessage findJavaMailMessageByFolderMessageId(final Folder folder,
            final String id) throws SystemException {
        MimeMessage message = null;

        // folder name used for errors only
        String folderName = (folder == null) ? "[none]" : folder.getName();

        try {
            // prerequisites, the id cannot be null, and folder must exist and be open
            if (StringHandling.isNullOrEmpty(id)) {
                throw new MessageNotFoundException(folderName, id,
                    "you must specify a valid message id");
            } else if (folder == null) {
                throw new MessageNotFoundException(folderName, id,
                    "you must specify a valid folder");
            } else if (!folder.exists()) {
                throw new MessageNotFoundException(folderName, id,
                    folderName + " does not exist");
            } else if (!folder.isOpen()) {
                throw new MessageNotFoundException(folderName, id,
                    folderName + " is not open");
            }

            // go thro' each one and compare ids - it's slow but it works
            // TODO: - examine ways to cache the mail folders
            for (int messageNumber = 1;
                    messageNumber <= folder.getMessageCount();
                    ++messageNumber) {
                MimeMessage messageTest = (MimeMessage) folder.getMessage(messageNumber);

                if (id.equals(messageTest.getMessageID())) {
                    message = messageTest;

                    break;
                }
            }
        } catch (FolderNotFoundException e) {
            throw new MessageNotFoundException(folderName, id,
                "FolderNotFoundException looking for " + folderName + ": " +
                e.getMessage(),
                e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        }

        return message;
    }

    /**
     * <p>This method retrieves the requested message and sets all the
     * attributes of a MessageDO object for use on client side.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder the message is located in.
     * @param id the unique identifier of the message.
     * @return a MessageDO instance filled up with the messages attributes,
     *     except the contents of the attachments.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public MessageDO findMessageByFolderMessageId(final MailSession mailSession,
            final String folderName,
            final String id) throws SystemException {
        checkDateFormatter(mailSession);
        MessageDO messageDO = null;

        Store store = mailServer.connectStore(mailSession);
        try {
            if (folderName == null) {
                logger.error(
                    "MailBean.findMessageByFolderMessageId(): "
                        + "folderName is null.");
                return null;
            }
            // right, we now have to find the message using the folder name and the message number
            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            // now open the folder
            folder.open(Folder.READ_ONLY);

            MimeMessage message = findJavaMailMessageByFolderMessageId(folder,
                    id);

            // if some other program download the message -> we didn't find it so return null
            if (message != null) {
                messageDO = getDOFromJavaMailMessage(message, true);
            }

            folder.close(false);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return messageDO;
    }

    /**
     * <p>Used in the main folder index page, this method returns the contents
     * of a folder as a <code>List</code> of
     * <code>MessageDO</code> instances.</p>
     *
     * <p><strong>Note:</strong> for efficiency reasons, this method does not fill the
     * text, format or attachment values of the returned <code>MessageDO</code>
     * instance. For that, you must call
     * {@link #findMessageByFolderMessageId findMessageByFolderMessageId}.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder to list.
     * @param sortBy the field to sort the returned list by. Set to one of the
     *     <code>SORT_...</code> constants in {@link MailConstants MailConstants}.
     * @param sortAscending if <code>true</code> then the messages are sorted in
     *     ascending order, otherwise (<code>false</code>) they are descending.
     * @return <code>List</code> of
     *     <code>MessageDO</code> instances.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List findMessagesInFolder(final MailSession mailSession,
            final String folderName,
            final Integer sortBy,
            final boolean sortAscending)
            throws SystemException {
        checkDateFormatter(mailSession);
        UserDO user = mailSession.getUser();
        MessageDO messageDO;
        Message[] messages;
        int i;

        // see if we have a mail session
        Session javaMailSession;

        // make a tree map to sort the items
        ListColumnComparator comparator = new ListColumnComparator(sortAscending);
        TreeMap map = new TreeMap(comparator);

        // some types must be sorted numerically, rather than alphanumerically
        comparator.setSortNumerically(MailConstants.SORT_RECEIVED.equals(sortBy)
                || MailConstants.SORT_SENT.equals(sortBy)
                || MailConstants.SORT_SIZE.equals(sortBy));

        try {
            javaMailSession = mailSession.getJavaMailSession();
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (java.security.NoSuchProviderException e) {
            throw new SystemException(e);
        }

        Store store = mailServer.connectStore(mailSession);
        try {
            Folder folderProcessed =
                mailServer.getFolder(mailSession, store, folderName);

            // go thro and sort the contents
            String sentKey;
            StringBuffer key = new StringBuffer(32);
            GregorianCalendar received;
            GregorianCalendar sent;

            // first go thro' and make sure there is an ivata message id for each
            // message - only necessary for incoming mails where message id
            // might not be unique
            String inboxFolderName = settings.getStringSetting(mailSession,
                    "emailFolderInbox",
                    user);

            if (folderName.equalsIgnoreCase(inboxFolderName)) {
                folderProcessed.open(Folder.READ_WRITE);
                messages = folderProcessed.getMessages();

                int length = messages.length;

                for (i = 0; i < length; i++) {
                    MimeMessage message = (MimeMessage) messages[i];

                    // check there is an ivata message id in the message id string
                    String messageId = message.getMessageID();

                    if ((messageId == null) ||
                            (messageId.indexOf("ivata-") == -1)) {
                        // create a new message dependent value object
                        MessageDO newMessage = createDOFromJavaMailMessage(message, true);
                        Integer newId = newMessage.getId();

                        if (messageId == null) {
                            messageId = "";
                        }

                        messageId += ("ivata-" + newId);

                        MimeMessage ivataMessage = new MimeMessage(message);

                        ivataMessage.setHeader("Message-ID", messageId);
                        message.setFlag(Flags.Flag.DELETED, true);

                        Message[] ivataMessages = { ivataMessage };

                        folderProcessed.appendMessages(ivataMessages);
                    }
                }

                // close the folder to save changes and re-open read only
                folderProcessed.close(true);
            }

            // now we can be sure-ish that message ids are unique -
            // the only way they can't be unique is if this is not the inbox
            // and JavaMail doesn't create a unique id when we make a new mail
            folderProcessed.open(Folder.READ_ONLY);
            messages = folderProcessed.getMessages();

            for (i = 0; i < messages.length; i++) {
                MimeMessage message = (MimeMessage) messages[i];

                messageDO = getDOFromJavaMailMessage(message, false);
                sent = messageDO.getSent();
                sentKey = ((sent == null) ? ""
                                          : new Long(sent.getTime().getTime()).toString());
                key.delete(0, key.capacity());

                // which field sorts is determined by the value of sortBy...
                if (MailConstants.SORT_FOLDER.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(
                            messageDO.getFolderName()));
                } else if (MailConstants.SORT_ID.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(messageDO.getMessageID()));
                } else if (MailConstants.SORT_RECEIVED.equals(sortBy)) {
                    received = messageDO.getReceived();
                    key.append(((received == null) ? ""
                                                   : new Long(
                            received.getTime().getTime()).toString()));
                } else if (MailConstants.SORT_RECIPIENTS.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(
                            messageDO.getRecipients()));
                } else if (MailConstants.SORT_RECIPIENTS_BCC.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(
                            messageDO.getRecipientsBCC()));
                } else if (MailConstants.SORT_RECIPIENTS_CC.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(
                            messageDO.getRecipientsCC()));
                } else if (MailConstants.SORT_SENDERS.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(messageDO.getSenders()));
                } else if (MailConstants.SORT_SIZE.equals(sortBy)) {
                    key.append(StringHandling.getNotNull(messageDO.getSize()));
                } else if (!MailConstants.SORT_SENT.equals(sortBy)) {
                    // default is subject - note sent date is appended below
                    key.append(StringHandling.getNotNull(messageDO.getSubject()));
                }

                // append the sent date to the key for secondary sorting
                key.append(sentKey);
                map.put(key.toString(), messageDO);
            }
        } catch (NoSuchProviderException e) {
            throw new SystemException(e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (SettingsDataTypeException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return new Vector(map.values());
    }

    /**
     * <p>Format a header for a reply, or a forwarded message.</p>
     *
     * @param messageHeader the header to format.
     * @param oldMessage the old message to read data from
     * @return formatted message header.
     */
    private String formatMessageHeader(final String messageHeader,
            final MimeMessage oldMessage)
            throws SystemException {
        Date date;
        String oldSentDate;
        String oldSubject;

        try {
            date = oldMessage.getSentDate();
            oldSubject = StringHandling.getNotNull(oldMessage.getSubject(),
                    getNullString());
        } catch (MessagingException e) {
            throw new SystemException(e);
        }

        if (date == null) {
            oldSentDate = "";
        } else {
            oldSentDate = dateFormatter.format(date);
        }

        EmailAddressFormatter emailFormatter = new EmailAddressFormatter();

        try {
            // the same four arguments are used in the same order
            // for both reply & forward headers in resource files
            String oldSenders = StringHandling.getNotNull(emailFormatter.format(
                        oldMessage.getFrom()), getNullString());
            String oldRecipients = StringHandling.getNotNull(emailFormatter.format(
                        oldMessage.getRecipients(MimeMessage.RecipientType.TO)),
                    getNullString());
            Object[] arguments = {
                oldSenders, oldRecipients, oldSubject, oldSentDate
            };

            return MessageFormat.format(messageHeader, arguments);
        } catch (MessagingException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Retrieve an attachment's content and it's MimeType. This method is
     * used to by the download servlet.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder the message is located in.
     * @param messageId the unique identifier of the message.
     * @param attachmentId the unique identifier of the attachment.
     * @return attachment data object containing attachment content
     *     and mime type.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public final FileContentDO getAttachment(final MailSession mailSession,
            final String folderName,
            final String messageId,
            final String attachmentId)
            throws SystemException {
        checkDateFormatter(mailSession);
        FileContentDO attachmentDO = null;

        Store store = mailServer.connectStore(mailSession);
        try {
            // right, we now have to find the message using the folder name and the message number
            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            // now open the folder
            folder.open(Folder.READ_ONLY);

            Message message = findJavaMailMessageByFolderMessageId(folder,
                    messageId);
            InputStream inStream = null;
            String mimeType = null;

            // prerequisites - the message must be multipart, otherwise it can
            // be only a self-contained attachment
            if (message.isMimeType("multipart/*")) {
                MimeMultipart multiMessage = (MimeMultipart) message.getContent();
                MimeBodyPart subPart = getAttachment(multiMessage, attachmentId);
                inStream = subPart.getInputStream();
                mimeType = subPart.getContentType();
            } else {
                String withOutAngels = attachmentId.substring(1,
                        attachmentId.length() - 1);

                if (((message.getFileName() != null) &
                        attachmentId.equals(message.getFileName())) ||
                        ((message.getFileName() != null) &
                        withOutAngels.equals(message.getFileName()))) {
                    inStream = message.getInputStream();
                    mimeType = message.getContentType();
                }
            }

            if ((inStream != null) && (mimeType != null)) {
                int oneByte;

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                while ((oneByte = inStream.read()) != -1) {
                    outStream.write(oneByte);
                }

                attachmentDO = new FileContentDO(new SerializedByteArray(
                            outStream.toByteArray()), mimeType);
            }

            folder.close(false);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }

        return attachmentDO;
    }

    /**
     * <p>This method goes through multiparts recursively looking for a part
     * with the given content id
     * @param multiPart the <code>MimeMultipart</code> to search
     * @param attachmentId the id of the wanted part
     * @return <code>MimeBodyPart</code> with the wanted id or <code>null</code>
     * when there wasn't such part in the given multipart
     * @throws MessagingException
     * @throws IOException
     */
    private MimeBodyPart getAttachment(final MimeMultipart multiPart,
            final String attachmentId) throws MessagingException, IOException {
        MimeBodyPart attachment = (MimeBodyPart) multiPart.getBodyPart(attachmentId);

        // if the attachment was not found in this level, it's more embedded...
        if (attachment == null) {
            MimeBodyPart embeddedPart;

            for (int i = 0; i < multiPart.getCount(); i++) {
                embeddedPart = (MimeBodyPart) multiPart.getBodyPart(i);

                // go deeper to multiparts
                if (embeddedPart.isMimeType("multipart/*")) {
                    // done when something returned from recursive call
                    if ((attachment = getAttachment(
                                    (MimeMultipart) embeddedPart.getContent(),
                                    attachmentId)) != null) {
                        break;
                    }
                } else if ((embeddedPart.getFileName() != null) &&
                        (attachmentId.indexOf(embeddedPart.getFileName()) != -1)) {
                    attachment = embeddedPart;

                    break;
                }
            }
        }

        return attachment;
    }

    /**
     * <p>Convert a <em>JavaMail</em> message to an <em>ivata groupware</em> dependent
     * value object.</p>
     *
     * @param message a valid <em>JavaMail</em> message to be converted.
     * @param includeContent <code>true</code> if the <code>text</code> and
     *     attachments of the message should also be set, otherwise
     *     <code>false</code>.
     * @return message data object with the values filled out to match
     *     the <em>JavaMail</em> object.
     */
    private MessageDO getDOFromJavaMailMessage(final MimeMessage message,
            final boolean includeContent)
            throws SystemException {
        // TODO: this is almost the same as the createDO... method just now
        // right - we got here, so that means we have a message
        MessageDO messageDO = new MessageDO();
        try {
            //setting the fields of the MessageDO:
            if (message.getFolder() != null) {
                messageDO.setFolderName(message.getFolder().getName());
            }

            if (message.getReceivedDate() != null) {
                GregorianCalendar receivedDate = new GregorianCalendar();

                receivedDate.setTime(message.getReceivedDate());
                messageDO.setReceived(receivedDate);
            }

            if (message.getRecipients(Message.RecipientType.TO) != null) {
                messageDO.setRecipients(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.TO))));
            }

            if (message.getRecipients(Message.RecipientType.CC) != null) {
                messageDO.setRecipientsCC(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.CC))));
            }

            if (message.getRecipients(Message.RecipientType.BCC) != null) {
                messageDO.setRecipientsBCC(Arrays.asList(toStringArray(
                            message.getRecipients(Message.RecipientType.BCC))));
            }

            if (message.getFrom() != null) {
                messageDO.setSenders(Arrays.asList(toStringArray(
                            message.getFrom())));
            }

            if (message.getSentDate() != null) {
                GregorianCalendar sentDate = new GregorianCalendar();

                sentDate.setTime(message.getSentDate());
                messageDO.setSent(sentDate);
            }

            messageDO.setSize(new Integer(message.getSize()));
            messageDO.setSubject(message.getSubject());

            // message content handling - not always done for efficiency in lists
            if (includeContent) {
                Integer format;
                String text;

                // create new, empty List for our attachments
                messageDO.setAttachments(new Vector());

                // if it is a multipart message (has attachments), pass control to
                // recursive routine to go thro' them all
                if (message.isMimeType("multipart/*")) {
                    addMultiPart(message, messageDO);

                    // here are types with textual content
                } else if (message.isMimeType("text/*") ||
                        message.isMimeType("message/*")) {
                    // if it is not multipart, we're just left with text or HTML
                    if (message.isMimeType("text/HTML")) {
                        // simple message with HTML content
                        messageDO.setFormat(FormatConstants.FORMAT_HTML);
                    } else {
                        // anything else with simple content should have text content
                        messageDO.setFormat(FormatConstants.FORMAT_TEXT);
                    }

                    messageDO.setText((String) message.getContent());

                    // other (not correct?) types, as self-contained attachments...
                } else {
                    messageDO.setFormat(FormatConstants.FORMAT_TEXT);
                    messageDO.setText("");
                    addPart(message, messageDO);
                }
            }
            messageDO.setMessageID(message.getMessageID());
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }

        return messageDO;
    }

    /**
     * <p>Helper for <code>createThreadMessage</code>. This method processes a
     * <em>Multipart</em> message or part and returns just the body parts
     * representing text in either plain-text and/or <code>HTML</code> format.</p>
     *
     * @param message or part to be processed.
     * @return the text of the message or part, it's plain text or <code>HTML</code>.
     */
    private MessageTextParts getMultiPartText(final Part message)
            throws SystemException {
        MessageTextParts messageTextParts = new MessageTextParts();
        MimeMultipart content;
        MimeBodyPart subPart;
        int i;

        try {
            content = (MimeMultipart) message.getContent();

            //go through all the subParts
            for (i = 0; i < content.getCount(); i++) {
                subPart = (MimeBodyPart) content.getBodyPart(i);

                //when multipart/alternative, we are close to find what we want...
                if (message.isMimeType("multipart/alternative")) {
                    if (subPart.isMimeType("text/plain") &&
                            (messageTextParts.textPart == null)) {
                        messageTextParts.textPart = subPart;
                    } else if (subPart.isMimeType("text/HTML") &&
                            (messageTextParts.HTMLPart == null)) {
                        messageTextParts.HTMLPart = subPart;

                        // usually the HTML part will come after the plain text
                        // if the plain text is set, we're all finished
                        if (messageTextParts.textPart != null) {
                            break;
                        }
                    }
                } else {
                    // if this is not alternative multipart, recurse to get the text
                    if (subPart.isMimeType("multipart/*")) {
                        messageTextParts = getMultiPartText((Part) subPart);

                        // when we are on a text subpart and text not set yet, this
                        // should be it...
                    } else if (subPart.isMimeType("text/plain") &&
                            (messageTextParts.textPart == null)) {
                        messageTextParts.textPart = subPart;
                    } else if (subPart.isMimeType("text/HTML") &&
                            (messageTextParts.HTMLPart == null)) {
                        messageTextParts.HTMLPart = subPart;

                        break;
                    }
                }
            }
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (java.io.IOException e) {
            throw new SystemException(e);
        }

        return messageTextParts;
    }

    /**
     * <p>Generic string to use when some message text/subject is null.</p>
     *
     * @return replacement text to use when a message element is null.
     */
    private String getNullString() {
        // TODO: i18n
        return "[none]";
    }

    /**
     * <p>Retrieve all of the email aliases for the user provided, on the curent
     * email server. The aliases returned each one containing just the 'user' (or
     * 'local') part of the email address, before the '@' sign.</p>
     *
     * @param userName the name of the user for whom to retrieve the email aliases.
     * @return a <code>Collection</code> of <code>String</code> instances containing
     *     the local part of the different email aliases this user has. If the user
     *     has no aliaes, an empty collection is returned.
     */
    public List getUserAliases(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        checkMailServer();
        checkDateFormatter(securitySession);

        List aliases = null;
        aliases = mailServer.getUserAliases(securitySession, userName);

        return aliases;
    }

    /**
     * <p>Get the vacation message for the user provided.</p>
     *
     * <p>The vacation message is a text the user can set, which will be sent
     * to all emails received at this address while the user is not present.</p>
     *
     * @param userName the name of the user for whom to get the vacation message.
     * @return the vacation message text for this user. If the user has no
     *     vacation message set, <code>null</code> is returned.
     */
    public final String getVacationMessage(final SecuritySession securitySession,
            final String userName)
            throws SystemException {
        checkMailServer();
        checkDateFormatter(securitySession);

        String vacationMessage = null;
        vacationMessage = mailServer.getVacationMessage(securitySession,
                userName);

        return vacationMessage;
    }

    /**
     * <p>Get the time the specified mail folder was last modified as a
     * <code>long</code>. This can then be saved and compared to subsequent
     * calls of this method to see if the folder has changed.</p>
     *
     * @param userName the name of the user for whom to locate the folder.
     * @param folderName the name of the folder to locate.
     * @return operating system specific timestamp indicating when the
     * folder was last changed.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public final boolean hasNewMessages(final SecuritySession securitySession,
            final String userName,
            final String folderName)
            throws SystemException {
        checkMailServer();
        if (userName == null) {
            throw new SystemException(
                "ERROR in MailBean.getFolderModified: userName is null.");
        }

        if (folderName == null) {
            throw new SystemException(
                "ERROR in MailBean.getFolderModified: folderName is null.");
        }

        return mailServer.hasNewMessages(securitySession,
                userName, folderName);
    }

    /**
     * <p>Login to the mail system. This method should be called before any other,
     * to establish the mail session and store.</p>
     *
     * @param userName this user name is used to log into the remote system.
     * @param password the clear-text password to log into the remote system.
     * @throws GroupwareException if the person cannot log in.
     * @return the mail session (class <code>Session</code>) in a
     *    <code>SessionSerializer</code>.
     *
     * @ejb.interface-method
     *      view-type = "local"
     */
    public MailSession login(final UserDO user,
            final String password)
            throws SystemException {
        checkMailServer();
        return (MailSession) mailServer.login(user, password);
    }

    /**
     * <p>Move a list of messages from one folder to another.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the name of the folder the messages are currently located in.
     * @param ids the unique identifiers (<code>String</code> instances) of the
     *     messages to be moved.
     * @param targetFolderName the name of the the folder the message will be moved
     *     to.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void moveMessages(final MailSession mailSession,
            final String folderName,
            final List ids,
            final String targetFolderName) throws SystemException {
        Store store = mailServer.connectStore(mailSession);
        checkDateFormatter(mailSession);
        try {
            Folder folder = mailServer.getFolder(mailSession, store, folderName);

            folder.open(Folder.READ_WRITE);

            Folder targetFolder =
                    mailServer.getFolder(mailSession, store, targetFolderName);

            if (!targetFolder.exists()) {
                targetFolder.create(Folder.HOLDS_MESSAGES);
            }

            targetFolder.open(Folder.READ_WRITE);

            List messages = new Vector();

            for (Iterator i = ids.iterator(); i.hasNext();) {
                String id = (String) i.next();
                MimeMessage message = findJavaMailMessageByFolderMessageId(folder,
                        id);

                if (message == null) {
                    logger.error(
                        "MailBean.moveMessage:I couldn't find a message with id"
                        + id
                        + " in folder '"
                        + folder.getName()
                        + "'");
                } else {
                    messages.add(message);
                }
            }

            if (!messages.isEmpty()) {
                Message[] messageArray = {  };

                messageArray = (Message[]) messages.toArray(messageArray);
                targetFolder.appendMessages(messageArray);
                targetFolder.close(false);

                // now mark all the original messages as deleted
                for (Iterator i = messages.iterator(); i.hasNext();) {
                    Message message = (Message) i.next();

                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }

            folder.expunge();
            folder.close(true);
        } catch (NoSuchProviderException e) {
            throw new SystemException(e);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }

    /**
     * <p>Helper method to get the name of the drafts folder from the settings,
     * and then open the folder in read-write mode.</p>
     *
     * @param store connected store.
     * @param javaMailSession valid <em>JavaMail</em> session to which the user
     *     should already be logged in.
     * @return opened drafts folder.
     * @exception NoSuchProviderException thrown by
     *      <code>Session.getStore</code> if there is no <em>IMAP</em> store
     *      available.
     * @exception MessagingException throw by <code>JavaMail</code> if there is
     *      problem locating or opening the drafts folder.
     * @exception SettingsDataTypeException if there is a problem accessing the
     *      setting "emailFolderDrafts".
     */
    private Folder openDraftsFolder(final Store store,
            final MailSession mailSession)
            throws SystemException {
        assert (store != null);
        assert (store.isConnected());
        assert (mailSession != null);
        checkDateFormatter(mailSession);

        UserDO user = mailSession.getUser();

        try {
            // first get the name of the drafts folder from the settings
            String draftsFolderName = settings.getStringSetting(mailSession,
                    "emailFolderDrafts",
                    user);
            Folder draftsFolder =
                    mailServer.getFolder(mailSession, store, draftsFolderName);
            if (!(draftsFolder.exists() ||
                    draftsFolder.create(Folder.HOLDS_MESSAGES))) {
                throw new SystemException(
                    "ERROR in MailBean.openDraftsFolder: Cannot create drafts folder.");
            }

            draftsFolder.open(Folder.READ_WRITE);
            return draftsFolder;
        } catch (MessagingException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Helper. This method calculates which of an old message's attachments
     * are still required and sets the content of the new message on this
     * basis.</p>
     *
     * <p>This method contains functionality from <code>setDO...</code> which
     * has been split off to avoid a long <code>if...else</code>.</p>
     *
     * @param messageDO data object containing the latest changes from
     *     client-side.
     * @param newBasePart the new base part of the message with content
     *     pre-prepared.
     * @param oldMessage previous message retained from drafts folder.
     * @param newMessage new message being constructed from the draft message and
     *     messageDO changes.
     */
    private void retainAttachments(final MessageDO messageDO,
            final MimeBodyPart newBasePart,
            final MimeMessage oldMessage,
            final MimeMessage newMessage)
            throws SystemException {
        try {
            MimeMultipart newMasterContent = new MimeMultipart();
            MimeMultipart oldContent = (MimeMultipart) oldMessage.getContent();

            // iterate through all the BodyParts (from 1 as it should be the base part)
            for (int j = 1; j < oldContent.getCount(); j++) {
                MimeBodyPart mimePart = new MimeBodyPart();

                mimePart = (MimeBodyPart) oldContent.getBodyPart(j);

                // if there are no attachments to remove, no need to find out if this one has to die
                if (messageDO.getAttachments() != null) {
                    // look if this BodyPart is in the list of attachments to delete
                    for (Iterator i = messageDO.getAttachments().iterator();
                            i.hasNext();) {
                        String id = ((FileDO) i.next()).getName();

                        // if it's an embedded message, try with messageId
                        if ((mimePart.isMimeType("message/*") &&
                                (((MimeMessage) mimePart.getContent()).getMessageID()
                                      .equals(id))) ||
                                ((mimePart.getFileName() != null) &&
                                mimePart.getFileName().equals(id)) ||
                                ((mimePart.getContentID() != null) &&
                                mimePart.getContentID().equals(id))) {
                            newMasterContent.addBodyPart(mimePart);
                        }
                    }
                }
            }

            // if there's at least one attachment left, it's worth to let it be multipart/mixed
            // and put the new base as the first part in this multipart
            if (newMasterContent.getCount() > 0) {
                newMasterContent.addBodyPart(newBasePart, 0);
                newMessage.setContent(newMasterContent);

                //if there are no attachments, only the base part will be there
            } else {
                if (messageDO.getFormat() == FormatConstants.FORMAT_HTML) {
                    newMessage.setContent((MimeMultipart) newBasePart.getContent());
                    newMessage.setHeader("Content-Type",
                        newBasePart.getContentType());
                } else {
                    newMessage.setText(messageDO.getText());
                }
            }
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Send a mime email message that is already composed. If <code>id</code>
     * has been set in <code>messageDO</code> it is assumed to point to a
     * message in the drafts folder. Attachments are copied from the message
     * who match the contents of <code>getAttachmentIds</code>. (All other
     * attachments are discarded.)</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param messageDO data object containing full details of the
     *     message to be sent.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public void send(final MailSession mailSession,
            final MessageDO messageDO)
            throws SystemException {
        checkDateFormatter(mailSession);
        Store store = mailServer.connectStore(mailSession);
        try {
            // if mail is disabled, just get out
            if (!settings.getBooleanSetting(mailSession,
                    "emailEnable", null)
                             .booleanValue()) {
                return;
            }

            Session javaMailSession;
            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (SecurityServerException e1) {
                throw new SystemException(e1);
            } catch (java.security.NoSuchProviderException e1) {
                throw new SystemException(e1);
            }

            // get the drafts folder in case we want to copy over an older mail
            Folder draftsFolder = openDraftsFolder(store, mailSession);
            MimeMessage newMessage = setDOToJavaMailMessage(javaMailSession,
                    draftsFolder, messageDO);

            newMessage.setSentDate(Calendar.getInstance().getTime());

            // this bit actually sends the message
            Transport.send(newMessage);

            // add the message to the sent folder
            addToSentFolder(mailSession, newMessage);

            // only now can we delete/expunge the old mail from the drafts folder
            draftsFolder.expunge();
            draftsFolder.close(true);
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (SettingsDataTypeException e) {
            throw new SystemException(e);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                logger.error("Messaging exception on closing the store", e);
            }
        }
    }

    /**
     * <p>Send an mime email message without using a data object.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param fromAddress the address of the person sending the mail. This must
     *    be formatted according to <a
     *    href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     * @param to recipients, a <code>Collection</code> containing instances of
     *    <code>String</code> or <code>UserDO</code> or
     *    <code>PersonDO</code>. A mixture of these types is allowed. If the
     *    type of an instance is <code>String</code>, then it must be formatted
     *    according to <a href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     *    Otherwise, if the type is <code>PersonDO</code>, the method
     *    <code>getEmailAddress</code> must return a valid address string for
     *    this person.
     * @param cc cc recipients. For format, see <code>to</code> parameter.
     * @param bcc bcc recipients. For format, see <code>to</code> parameter.
     * @param subject clear-text email subject field.
     * @param content any valid email content type, as defined by
     *    <code>MimeMessage</code>.
     * @param contentType mime type for the <code>content</code> field, as
     *    defined by <code>MimeMessage</code>.
     * @param addToSentFolder if set to <code>true</code> then the mail is added
     *    to the sent folder of the current email session.
     *
     * @ejb.interface-method
     *      view-type = "both"
     */
    public void send(final MailSession mailSession,
            final String fromAddress,
            final Collection to,
            final Collection cc,
            final Collection bcc,
            final String subject,
            Object content, String contentType, boolean addToSentFolder)
            throws SystemException {
        checkDateFormatter(mailSession);
        try {
            try {
                // if mail is disabled, just get out
                if (!settings.getBooleanSetting(mailSession, "emailEnable", null)
                                 .booleanValue()) {
                    return;
                }
            } catch (SettingsDataTypeException e) {
                throw new SystemException(e);
            }

            // see if we have a mail session
            Session javaMailSession;

            try {
                javaMailSession = mailSession.getJavaMailSession();
            } catch (AuthenticationFailedException e) {
                throw new SystemException(
                    "User is no longer authorized to use this server: " +
                    e.getMessage(),
                    e);
            } catch (MessagingException e) {
                throw new SystemException(e);
            } catch (SecurityServerException e) {
                throw new SystemException(e);
            } catch (java.security.NoSuchProviderException e) {
                throw new SystemException(e);
            }

            Store store = javaMailSession.getStore("imap");

            if (store == null) {
                throw new SystemException(
                    "ERROR in MailBean: could not access the mail store");
            }

            MimeMessage message = new MimeMessage(javaMailSession);

            try {
                message.setFrom(new InternetAddress(fromAddress));
                message.setSubject(subject);
                message.setRecipients(MimeMessage.RecipientType.TO,
                    convertAddresses(null, to));

                if (cc != null) {
                    message.setRecipients(MimeMessage.RecipientType.CC,
                        convertAddresses(null, cc));
                }

                if (bcc != null) {
                    message.setRecipients(MimeMessage.RecipientType.BCC,
                        convertAddresses(null, bcc));
                }

                message.setContent(content, contentType);
                message.setSentDate(Calendar.getInstance().getTime());
            } catch (MessagingException e) {
                throw new SystemException("Please check your input: " +
                    e.getMessage(),
                    e);
            }

            // send it
            try {
                Transport.send(message);
            } catch (MessagingException e) {
                throw new SystemException("The message could not be sent: " +
                    e.getMessage(),
                    e);
            }

            // add the message to the sent items folder
            if (addToSentFolder) {
                addToSentFolder(mailSession, message);
            }
        } catch (NoSuchProviderException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>Convert an <em>ivata groupware</em> message data object into a
     * new <em>JavaMail</em> message that's ready to be sent.</p>
     *
     * <p><strong>Note:</strong> this method will look for an existing message (in the
     * drafts folder) to use for the attachments. This is only done if the
     * <code>MessageDO</code> has a valid <code>id</code>, and the drafts folder
     * provided is not <code>null</code>. The previous mail is marked for
     * deletion in the drafts folder, and this can be confirmed by expunging that
     * folder and calling <code>close</code> with <code>true</code>. This is
     * not done internally as it may depend on further actions (such as adding
     * another mail successfully to the folder).</p>
     *
     * @param javaMailSession valid <em>JavaMail</em> session to which the user
     *     should already be logged in.
     * @param draftsFolder the opened drafts to retrieve a previous message from.
     *     The previous message is identified by the <code>id</code> attribute
     *     of the <code>messageDO</code>. If this parameter is <code>null</code>
     *     then no previous mail is considered.
     * @param messageDO a valid message data object with the values
     *     filled out to match the <em>JavaMail</em> object which will be sent.
     * @return a valid <em>JavaMail</em> message ready to be sent.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     */
    private MimeMessage setDOToJavaMailMessage(final Session javaMailSession,
            final Folder draftsFolder,
            final MessageDO messageDO)
            throws SystemException {
        MimeMessage newMessage = new MimeMessage(javaMailSession);

        try {
            newMessage.addFrom(convertAddresses(null, messageDO.getSenders()));
            newMessage.setSubject(messageDO.getSubject());
            newMessage.setRecipients(MimeMessage.RecipientType.TO,
                convertAddresses(null, messageDO.getRecipients()));

            if (messageDO.getRecipientsCC() != null) {
                newMessage.setRecipients(MimeMessage.RecipientType.CC,
                    convertAddresses(null, messageDO.getRecipientsCC()));
            }

            if (messageDO.getRecipientsBCC() != null) {
                newMessage.setRecipients(MimeMessage.RecipientType.BCC,
                    convertAddresses(null, messageDO.getRecipientsBCC()));
            }

            newMessage.setSentDate(Calendar.getInstance().getTime());

            // first try to identify any existing message we might be copying
            MimeMessage oldMessage = null;

            if ((messageDO.getMessageID() != null) && (draftsFolder != null)) {
                oldMessage = findJavaMailMessageByFolderMessageId(draftsFolder,
                        messageDO.getMessageID());
            }

            MimeBodyPart newBasePart = new MimeBodyPart();

            // new base part is needed if the format is HTML or if there were
            // previous attachments we want to retain
            if (messageDO.getFormat() == FormatConstants.FORMAT_TEXT) {
                newBasePart.setText(messageDO.getText());
            } else {
                // HTML desired, the base part will be multipart/alternative
                MimeMultipart newTextContent = createHTMLContent(messageDO.getText());

                // set the multipart/alternative as the content of the base part
                newBasePart.setContent(newTextContent);
                newBasePart.setHeader("Content-Type",
                    newTextContent.getContentType());
            }

            // if the message has no id, or if it had no attachments, then this
            // is a fresh new message -
            // no need to copy the contents of another message from the
            // drafts folder
            if ((oldMessage == null) ||
                    !oldMessage.isMimeType("multipart/mixed") ||
                    (messageDO.getAttachments() == null)) {
                // no attachments - simply decide whether we have HTML or just text
                if (messageDO.getFormat() == FormatConstants.FORMAT_HTML) {
                    newMessage.setContent((MimeMultipart) newBasePart.getContent());
                    newMessage.setHeader("Content-Type",
                        newBasePart.getContentType());
                } else {
                    newMessage.setText(messageDO.getText());
                }
            } else {
                // the old message had attachments - we need to examine which
                // of them are still required
                retainAttachments(messageDO, newBasePart, oldMessage, newMessage);

                // mark the old message for deletion
                oldMessage.setFlag(Flags.Flag.DELETED, true);
            }
        } catch (StackOverflowError e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }

        return newMessage;
    }

    /**
     * <p>Helper for <code>createThreadMessage</code>. This method processes a
     * <em>Multipart</em> message or part and sets just the body parts
     * representing text in either plain-text and/or <code>HTML</code> format.</p>
     *
     * @param message or part to be processed.
     * @param messageTextPartsParam the HTML and plain text parts to set.
     */
    private void setMultiPartText(final Part message,
            final MessageTextParts messageTextPartsParam)
            throws SystemException {
        MessageTextParts messageTextParts = messageTextPartsParam;
        MimeMultipart content;
        MimeBodyPart subPart;
        int i;

        try {
            content = (MimeMultipart) message.getContent();

            //go through all the subParts
            for (i = 0; i < content.getCount(); i++) {
                subPart = (MimeBodyPart) content.getBodyPart(i);

                //when multipart/alternative, process the HTML subPart (we prefer HTML)
                if (message.isMimeType("multipart/alternative")) {
                    if (subPart.isMimeType("text/plain") &&
                            (messageTextParts.textPart != null)) {
                        subPart.setContent(messageTextParts.textPart.getContent(),
                            "text/plain");
                    } else if (subPart.isMimeType("text/HTML") &&
                            (messageTextParts.HTMLPart != null)) {
                        subPart.setContent(messageTextParts.HTMLPart.getContent(),
                            "text/HTML");

                        // usually the HTML part will come after the plain text
                        break;
                    }
                }

                // when multipart/related, process the first part as primary...,
                if (message.isMimeType("multipart/related") ||
                        message.isMimeType("multipart/mixed")) {
                    // if this is multipart, recurse to get the text
                    if (subPart.isMimeType("multipart/*")) {
                        messageTextParts = getMultiPartText((Part) subPart);
                    } else if (subPart.isMimeType("text/plain") &&
                            (messageTextParts.textPart == null)) {
                        messageTextParts.textPart = subPart;
                    } else if (subPart.isMimeType("text/HTML") &&
                            (messageTextParts.HTMLPart == null)) {
                        messageTextParts.HTMLPart = subPart;

                        break;
                    }
                }
            }
        } catch (MessagingException e) {
            throw new SystemException(e);
        } catch (java.io.IOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * <p>Set all of the email aliases for the user provided, on the curent
     * email server. Each alias in the collection should contain just the 'user'
     * (or 'local') part of the email address, before the '@' sign.</p>
     *
     * @param userName the name of the user for whom to retrieve the email aliases.
     * @param userAliases a <code>Collection</code> of <code>String</code>
     *     instances containing the local part of the different email aliases
     *     this user has. If the user has no aliaes, an empty collection should
     *     be provided.
     */
    public final void setUserAliases(final SecuritySession securitySession,
            final String userName,
            final Collection userAliases)
            throws SystemException {
        checkMailServer();
        checkDateFormatter(securitySession);

        // for the aliases which have been removed, remove the email addresses
        List currentUserAliases = mailServer.getUserAliases(securitySession,
                userName);
        PersonDO person = addressBook.findPersonByUserName(securitySession,
                userName);
        UserDO user = person.getUser();
        Set telecomAddresses = person.getTelecomAddresses();

        String emailAddressHost =
                settings.getStringSetting(
                    securitySession,
                    "emailAddressHost",
                    user);

        // now go thro' the current aliases and see which ones have been removed
        // to remove the associated email addresses, if any
        Iterator currentUserAliasesIterator = currentUserAliases.iterator();
        List removedEmailAddresses = new Vector();
        List removedEmailTelecomAddresses = new Vector();
        List currentEmailAddresses = new Vector();
        while (currentUserAliasesIterator.hasNext()) {
            String alias = (String) currentUserAliasesIterator.next();
            if (!userAliases.contains(alias)) {
                removedEmailAddresses.add(alias + "@" + emailAddressHost);
            }
        }
        Iterator telecomAddressIterator = telecomAddresses.iterator();
        while (telecomAddressIterator.hasNext()) {
            TelecomAddressDO thisTelecomAddress = (TelecomAddressDO)
                telecomAddressIterator.next();
            if (thisTelecomAddress.getType() == TelecomAddressConstants.TYPE_EMAIL) {
                if (removedEmailAddresses.contains(thisTelecomAddress.getAddress())) {
                    removedEmailTelecomAddresses.add(thisTelecomAddress);
                } else {
                    currentEmailAddresses.add(thisTelecomAddress.getAddress());
                }
            }
        }

        int sizeBefore = telecomAddresses.size();
        if (user.getId() != null) {
            addUserAliasEmailAddresses(securitySession, userName, userAliases,
                    telecomAddresses, emailAddressHost);
        }
        int sizeAfter = telecomAddresses.size();

        // if some email telecom addresses were removed or added, update the
        // person
        if ((removedEmailTelecomAddresses.size() > 0)
                || (sizeAfter != sizeBefore)) {
            Iterator removedEmailTelecomAddressesIterator = removedEmailTelecomAddresses.iterator();
            while (removedEmailTelecomAddressesIterator.hasNext()) {
                telecomAddresses.remove(removedEmailTelecomAddressesIterator.next());
            }
            addressBook.amendPerson(securitySession, person);
        }

        // remove the user name itself from the aliases - if it is included
        userAliases.remove(userName);

        mailServer.setUserAliases(securitySession, userName, userAliases);
    }

    /**
     * <p>Set the vacation message for the user provided.</p>
     *
     * <p>The vacation message is a text the user can set, which will be sent
     * to all emails received at this address while the user is not present.</p>
     *
     * @param userName the name of the user for whom to get the vacation message.
     * @param vacationMessage vacation message text for this user. If the user
     *     has no vacation message set, set to <code>null</code>.
     */
    public final void setVacationMessage(final SecuritySession securitySession,
            final String userName,
            final String vacationMessage)
            throws SystemException {
        checkMailServer();
        checkDateFormatter(securitySession);
        mailServer.setVacationMessage(securitySession, userName, null);
    }

    /**
     * <p>Helper method to convert an array of <code>Address</code> instances to
     * strings.</p>
     *
     * @param addresses non-<code>null</code> array of <code>Address</code>
     *     instances to be converted to strings.
     * @return array of strings representing the <code>Address.toString</code>
     *     values.
     */
    private String[] toStringArray(final Address[] addresses) {
        int i;
        String[] retArray = new String[addresses.length];

        for (i = 0; i < addresses.length; i++) {
            retArray[i] = addresses[i].toString();
        }

        return retArray;
    }

    /**
     * <p>Confirm all of the elements of the message are present and valid,
     * before the message is sent.</p>
     *
     * @param messageDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public ValidationErrors validate(final SecuritySession securitySession,
            final MessageDO messageDO) {
        checkDateFormatter(securitySession);
        ValidationErrors errors = new ValidationErrors();
        Mask mask = maskFactory.getMask(MessageDO.class);

        // recipients is mandatory
        if ((messageDO.getRecipients() == null) ||
                (messageDO.getRecipients().size() == 0)) {
            errors.add(new ValidationError(
                    "compose",
                    Mail.BUNDLE_PATH,
                    mask.getField("recipients"),
                    "errors.required"));
        }

        // senders is mandatory
        if ((messageDO.getSenders() == null) ||
                (messageDO.getSenders().size() == 0)) {
            errors.add(new ValidationError(
                    "compose",
                    Mail.BUNDLE_PATH,
                    mask.getField("senders"),
                    "errors.required"));
        }

        // subject is mandatory for now - see todo above
        if ((messageDO == null) ||
                StringHandling.isNullOrEmpty(messageDO.getSubject())) {
            errors.add(new ValidationError(
                    "compose",
                    Mail.BUNDLE_PATH,
                    mask.getField("subject"),
                    "errors.required"));
        }

        // text is mandatory for now - see todo above
        if ((messageDO == null) ||
                StringHandling.isNullOrEmpty(messageDO.getText())) {
            errors.add(new ValidationError(
                    "compose",
                    Mail.BUNDLE_PATH,
                    mask.getField("text"),
                    "errors.required"));
        }

        // validate email adresses
        if (messageDO.getRecipients() != null) {
            for (Iterator i = messageDO.getRecipients().iterator();
                    i.hasNext();) {
                String currentRecipient = "";

                try {
                    currentRecipient = (String) i.next();

                    InternetAddress emailAddress = new InternetAddress(currentRecipient,
                            true);
                } catch (AddressException e) {
                    errors.add(new ValidationError(
                            "",
                            Mail.BUNDLE_PATH,
                            mask.getField("to"),
                            "errors.mail.wrongEmailAddress",
                            Arrays.asList(new String[] { currentRecipient })));
                }
            }
        }

        if (messageDO.getRecipientsCC() != null) {
            for (Iterator i = messageDO.getRecipientsCC().iterator();
                    i.hasNext();) {
                String currentRecipientCC = "";

                try {
                    currentRecipientCC = (String) i.next();

                    InternetAddress emailAddress = new InternetAddress(currentRecipientCC,
                            true);
                } catch (AddressException e) {
                    errors.add(new ValidationError(
                            "",
                            Mail.BUNDLE_PATH,
                            mask.getField("cc"),
                            "errors.mail.wrongEmailAddress",
                            Arrays.asList(new String[] { currentRecipientCC })));
                }
            }
        }

        if (messageDO.getRecipientsBCC() != null) {
            for (Iterator i = messageDO.getRecipientsBCC().iterator();
                    i.hasNext();) {
                String currentRecipientBCC = "";

                try {
                    currentRecipientBCC = (String) i.next();

                    InternetAddress emailAddress = new InternetAddress(currentRecipientBCC,
                            true);
                } catch (AddressException e) {
                    errors.add(new ValidationError(
                            "",
                            Mail.BUNDLE_PATH,
                            mask.getField("bcc"),
                            "errors.mail.wrongEmailAddress",
                            Arrays.asList(new String[] { currentRecipientBCC })));
                }
            }
        }

        return errors;
    }
}
