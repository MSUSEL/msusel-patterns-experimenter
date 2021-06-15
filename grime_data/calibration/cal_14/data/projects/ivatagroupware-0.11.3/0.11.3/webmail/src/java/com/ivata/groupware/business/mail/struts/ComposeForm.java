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
package com.ivata.groupware.business.mail.struts;

import java.util.Iterator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.drive.file.FileDO;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.groupware.business.mail.message.MessageDO;
import com.ivata.mask.Mask;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>Main form used to input/display data in
 * <code>/mail/compose.jsp</code>.</p>
 *
 * @since 2002-11-09
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class ComposeForm extends DialogForm {
    /**
     * <p>Set when attachments were uploaded to the temporary upload
     * directory, contains the fileNames separated by
     * <code>File.SeparatorChar</code>.</p>
     */
    private String attach;
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;
    /**
     * Refer to {@link Logger}.
     */
    private Logger log = Logger.getLogger(ComposeForm.class);
    Mail mail;
    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;

    /**
     * <p>Message data object which contains all of the
     * information in this form.</p>
     */
    private MessageDO message = new MessageDO();
    /**
     * Stores any exception encountered when sending the mail.
     */
    private MessagingException messagingException = null;
    /**
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     */
    public ComposeForm(final Mail mail) {
        this.mail = mail;
    }

    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        attach = null;
    }

    /**
     * <p>Set when attachments were uploaded to the temporary upload
     * directory, contains the fileNames separated by
     * <code>File.SeparatorChar</code>.</p>
     *
     * @return the current value of attach.
     */
    public final String getAttach() {
        return attach;
    }

    /**
     * <p>This map contains names of files to remove from the attachment
     * list</p>
     * @param attachmentId the fileName or attachmentId of the attachment
     * to remove
     * @return the remove status for the specified attachment.
     */
    public final boolean getAttachmentRemove(final String attachmentId) {
        // always return false, a file in the list wants to stay there...
        return false;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }

    /**
     * <p>Get the message data object which contains all of the
     * information in this form.</p>
     *
     * @return message data object which contains all of the
     * information in this form.
     */
    public MessageDO getMessage() {
        return message;
    }

    /**
     * @return Returns the messagingException.
     */
    public MessagingException getMessagingException() {
        return messagingException;
    }

    /**
     * <p>Get recipients of the message.</p>
     *
     * @return recipients of the message, as a <code>String</code>
     * containing all of the instances separated by semicolons (;),  each
     * one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final String getRecipients() {
        return CollectionHandling.convertToLines(message.getRecipients(), ';');
    }

    /**
     * <p>Get "Blind carbon copy" recipients as List of strings. These are
     * additional recipients whose identity is <em>not</em> made known to
     * any
     * other recipients.</p>
     *
     * @return recipients of the message, as a <code>String</code>
     * containing all of the instances separated by semicolons (;),  each
     * one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     *
     */
    public final String getRecipientsBCC() {
        return CollectionHandling.convertToLines(message.getRecipientsBCC(), ';');
    }

    /**
     * <p>Get "Carbon copy" recipients of the message. These are
     * additional
     * recipients whose identity is made known to all other
     * recipients.</p>
     *
     * @return recipients of the message, as a <code>String</code>
     * containing all of the instances separated by semicolons (;),  each
     * one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     *
     */
    public final String getRecipientsCC() {
        return CollectionHandling.convertToLines(message.getRecipientsCC(), ';');
    }

    /**
     * <p>Get senders of the message.</p>
     *
     * @return senders of the message, as a <code>String</code>
     * containing all of the instances separated by semicolons (;),  each
     * one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final String getSenders() {
        return CollectionHandling.convertToLines(message.getSenders(), ';');
    }

    /**
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.<p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        // ok, it's not very memory-efficient, but it's easier to maintain :-)
        super.reset(mapping, request);
        attach = null;
    }

    /**
     * <p>Set when attachments were uploaded to the temporary upload
     * directory, contains the fileNames separated by
     * <code>File.SeparatorChar</code>.</p>
     *
     * @param attach the new value of attach.
     */
    public final void setAttach(final String attach) {
        this.attach = attach;
    }

    /**
     * <p>This map contains names of attachments to remove from the message</p>
     *
     * @param attachmentId the attachment to remove from the message
     * @param remove when <code>true</code>, the attachment should be removed.
     */
    public final void setAttachmentRemove(final String attachmentId,
            final boolean remove) {
        // if there's an attachment marked to remove, remove the corresponding
        // attachment from the list
        if (remove && (message.getAttachments() != null)) {
            int attachmentIdHashCode = Integer.parseInt(attachmentId);

            for (Iterator i = message.getAttachments().iterator(); i.hasNext();) {
                FileDO currentDO = (FileDO) i.next();
                String currentId = currentDO.getName();

                if (currentId.hashCode() == attachmentIdHashCode) {
                    message.getAttachments().remove(currentDO);

                    break;
                }
            }
        }
    }

    /**
     * <p>Set the message data object which contains all of the
     * information in this form. The contents of the form are entirely
     * replaced by the contents of the messge provided.</p>
     *
     * @param message message data object which contains all of
     * the new information in this form.
     */
    public final void setMessage(final MessageDO message) {
        this.message = message;
    }
    /**
     * @param messagingExceptionParam The messagingException to set.
     */
    public void setMessagingException(MessagingException messagingExceptionParam) {
        if (log.isDebugEnabled()) {
            log.debug("setMessagingException before: '" + messagingException
                    + "', after: '" + messagingExceptionParam + "'");
        }

        messagingException = messagingExceptionParam;
    }

    /**
     * <p>Set recipients of the message.</p>
     *
     * @param recipients recipients of the message, as a
     * <code>String</code> containing all of the instances separated by
     * semicolons (;),  each one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final void setRecipients(final String recipients) {
        message.setRecipients(CollectionHandling.convertFromLines(recipients,
                ";"));
    }

    /**
     * <p>Set "Blind carbon copy" recipients as List of strings. These are
     * additional recipients whose identity is <em>not</em> made known to
     * any
     * other recipients.</p>
     *
     * @param recipientsBCC recipients of the message, as a
     * <code>String</code> containing all of the instances separated by
     * semicolons (;),  each one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final void setRecipientsBCC(final String recipientsBCC) {
        message.setRecipientsBCC(CollectionHandling.convertFromLines(
                recipientsBCC, ";"));
    }

    /**
     * <p>Set "Carbon copy" recipients of the message. These are
     * additional
     * recipients whose identity is made known to all other
     * recipients.</p>
     *
     * @param recipientsCC recipients of the message, as a
     * <code>String</code> containing all of the instances separated by
     * semicolons (;),  each one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final void setRecipientsCC(final String recipientsCC) {
        message.setRecipientsCC(CollectionHandling.convertFromLines(
                recipientsCC, ";"));
    }

    /**
     * <p>Set senders of the message.</p>
     *
     * @param senders senders of the message, as a <code>String</code>
     * containing all of the instances separated by semicolons (;),  each
     * one formatted according to <a
     * href='http://www.faqs.org/rfcs/rfc822.HTML'>RFC822</a>.
     */
    public final void setSenders(final String senders) {
        message.setSenders(CollectionHandling.convertFromLines(senders, ";"));
    }

    /**
     * <p>Call the corresponding server-side validation, handle possible
     * exceptions and return any errors generated.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return <code>ActionMessages</code> collection containing all
     * validation errors, or <code>null</code> if there were no errors.
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        // only validate if ok was pressed
        if (StringHandling.isNullOrEmpty(getOk()) &&
                StringHandling.isNullOrEmpty(getApply())) {
            return null;
        }

        if (mail == null) {
            throw new NullPointerException(
                "ERROR in ComposeForm: mail remote instance is null.");
        }

        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        ValidationErrors validationErrors;
        try {
            validationErrors = mail.validate(securitySession,
                message);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
        return validationErrors;
    }
}
