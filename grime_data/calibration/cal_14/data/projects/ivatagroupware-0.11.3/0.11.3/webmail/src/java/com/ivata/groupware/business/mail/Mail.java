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

import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.drive.file.FileContentDO;
import com.ivata.groupware.business.mail.message.MessageDO;
import com.ivata.groupware.business.mail.message.MessageNotFoundException;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;


/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 27, 2004
 * @version $Revision: 1.3 $
 */
public interface Mail {

    public final static String BUNDLE_PATH = "mail";

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
    MessageDO addMessageToDraftsFolder(final MailSession mailSession,
            final MessageDO messageDO) throws SystemException;
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
    void addUserAliasEmailAddresses(final SecuritySession securitySession,
            final String userName,
            final Collection userAliases,
            final Collection telecomAddresses,
            final String emailAddressHost)
            throws SystemException;

    /**
     * <p>Append attachments to a message located in the drafts folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param id the unique identifier of the message to which we want to append
     *     attachments.
     * @param attachments <code>Vector</code> of <code>String</code>s -
     *     filenames of files waiting in upload directory.
     * @return <code>null</code> when the operation failed, otherwise the new
     *     message id.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     */
    MessageDO appendAttachments(final MailSession mailSession,
            final String id,
            final List attachments) throws SystemException;

    /**
     * <p>Create a new mail folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param folderName the full path name of the folder to create.
     */
    void createFolder(final MailSession mailSession,
            final String folderName)
        throws SystemException;

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
     */
    MessageDO createThreadMessage(final MailSession mailSession,
            final String folderName,
            final List messageIds,
            final Integer thread)
        throws SystemException;

    /**
     * <p>Delete a list of messages from the trash folder.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param ids the unique identifiers (<code>String</code> instances) of the
     *     messages to be removed.
     * @exception MessageNotFoundException if the folder doesn't exist, or there
     *     is no matching mail in this folder.
     */
    void deleteMessagesFromTrash(final MailSession mailSession,
            final List ids)
        throws SystemException;

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
        throws SystemException;

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
     */
    MessageDO findMessageByFolderMessageId(final MailSession mailSession,
            final String folderName,
            final String id) throws SystemException;

    /**
     * <p>Used in the main folder index page, this method returns the contents
     * of a folder as a <code>Vector</code> of
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
     * @return <code>Vector</code> of
     *     <code>MessageDO</code> instances.
     */
    List findMessagesInFolder(final MailSession mailSession,
            final String folderName,
            final Integer sortBy,
            final boolean sortAscending)
        throws SystemException;

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
     */
    FileContentDO getAttachment(final MailSession mailSession,
            final String folderName,
            final String messageId,
            final String attachmentId)
        throws SystemException;

    /**
     * <p>Get the time the specified mail folder was last modified as a
     * <code>long</code>. This can then be saved and compared to subsequent
     * calls of this method to see if the folder has changed.</p>
     *
     * @param userName the name of the user for whom to locate the folder.
     * @param folderName the name of the folder to locate.
     * @return operating system specific timestamp indicating when the
     * folder was last changed.
     */
    boolean hasNewMessages(final SecuritySession securitySession,
            final String userName,
            final String folderName) throws SystemException;

    /**
     * <p>Retrieve all of the email aliases for the user provided, on the curent
     * email server. The aliases returned each one containing just the 'user' (or
     * 'local') part of the email address, before the '@' sign.</p>
     *
     * @param userName the name of the user for whom to retrieve the email aliases.
     * @return a <code>Collection</code> of <code>String</code> instances containing
     *     the local part of the different email aliases this user has. If the user
     *     has no aliaes, an empty collection is returned.
     * @throws MandatoryFieldException if the user name is <code>null</code>.
     *
     * @ejb.interface-method
     *      view-type = "remote"
     */
    public List getUserAliases(final SecuritySession securitySession,
            final String userName) throws SystemException;

    /**
     * <p>Get the vacation message for the user provided.</p>
     *
     * <p>The vacation message is a text the user can set, which will be sent
     * to all emails received at this address while the user is not present.</p>
     *
     * @param userName the name of the user for whom to get the vacation message.
     * @return the vacation message text for this user. If the user has no
     *     vacation message set, <code>null</code> is returned.
     * @throws MandatoryFieldException if the user name is <code>null</code>.
     */
    String getVacationMessage(final SecuritySession securitySession,
            final String userName) throws SystemException;
    /**
     * <p>Login to the mail system. This method should be called before any other,
     * to establish the mail session and store.</p>
     *
     * @param userName this user name is used to log into the remote system.
     * @param password the clear-text password to log into the remote system.
     * @throws EJBException if the person cannot log in.
     * @return the mail session (class <code>Session</code>) in a
     *    <code>SessionSerializer</code>.
     */
    MailSession login(final UserDO user,
            final String password)
        throws SystemException;

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
     */
    void moveMessages(final MailSession mailSession,
            final String folderName,
            final List ids,
            final String targetFolderName) throws SystemException;

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
     */
    void send(final MailSession mailSession,
            final MessageDO messageDO)
        throws SystemException;

    /**
     * <p>Send an mime email message without using a data object.</p>
     *
     * @param mailSession valid mail session to which the user should already be
     *     logged in.
     * @param fromAddress the address of the person sending the mail. This must
     *    be formatted according to <a
     *    href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     * @param to recipients, a <code>Collection</code> containing instances of
     *    <code>String</code> or <code>UserLocal</code> or
     *    <code>PersonLocal</code>. A mixture of these types is allowed. If the
     *    type of an instance is <code>String</code>, then it must be formatted
     *    according to <a href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     *    Otherwise, if the type is <code>PersonLocal</code>, the method
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
     */
    void send(final MailSession mailSession,
            final String fromAddress,
            final Collection to,
            final Collection cc,
            final Collection bcc,
            final String subject,
        Object content, String contentType, boolean addToSentFolder)
        throws SystemException;

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
     * @throws MandatoryFieldException if either input is <code>null</code>.
     */
    void setUserAliases(final SecuritySession securitySession,
            final String userName,
            final Collection userAliases) throws SystemException;

    /**
     * <p>Set the vacation message for the user provided.</p>
     *
     * <p>The vacation message is a text the user can set, which will be sent
     * to all emails received at this address while the user is not present.</p>
     *
     * @param userName the name of the user for whom to get the vacation message.
     * @param vacationMessage vacation message text for this user. If the user
     *     has no vacation message set, set to <code>null</code>.
     * @throws MandatoryFieldException if the user name is <code>null</code>.
     */
    void setVacationMessage(final SecuritySession securitySession,
            final String userName,
            final String vacationMessage) throws SystemException;

    /**
     * <p>Confirm all of the elements of the message are present and valid,
     * before the message is sent.</p>
     *
     * @param messageDO data object to check for consistency and
     *     completeness.
     * @return a collection of validation errors if any of the
     *     mandatory fields are missing, or if fields contain invalid values.
     */
    ValidationErrors validate(final SecuritySession securitySession,
            final MessageDO messageDO) throws SystemException;
}
