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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ivata.groupware.business.mail.MailConstants;
import com.ivata.groupware.business.mail.message.MessageDO;
import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>This form is used to identify an existing message to display, or
 * from which we want to create a new
 * one. Depending on the value of the <code>thread</code> attribute,
 * the newmessage can:<br/>
 * <ul>
 * <li>Reply to the main recipient of the previous message</li>
 * <li>Reply to all visible (i.e. not <em>BCC</em>) recipients of the
 * previous message</li>
 * <li>Forward existing message, or messages</li>
 * <li>Edit an existing message as a temple for a new message.</li>
 * </ul></p>
 *
 * @since 2002-11-11
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class DisplayForm extends DialogForm {

    /**
     * <p>Indicates this message should be moved to the trash folder.
     * After the message has been moved, the next message in the folder
     * will
     * be displayed or (if there are no more messages) the folder
     * index.</p>
     */
    private boolean delete;

    /**
     * <p>Indicates this message should be permanently erased from the
     * trash folder. After the message has been deleted, the next message
     * in
     * the trash folder will be displayed or (if there are no more
     * messages)
     * the trash folder index.</p>
     */
    private boolean deleteTrash;

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the next message in the current folder
     * will be displayed.</p>
     */
    private String displayNext;

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the previous message in the current
     * folder will be displayed.</p>
     */
    private String displayPrevious;

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder after this one.</p>
     */
    private boolean hasNext;

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder before this one.</p>
     */
    private boolean hasPrevious;

    /**
     * <p>Stores the data of the currently displayed message.</p>
     */
    private MessageDO message = new MessageDO();
    private boolean sortAscending;
    private Integer sortBy;
    /**
     * <p>Set to one of the constants in {@link
     * com.ivata.groupware.business.mail.MailConstants MailConstants}, this
     * indicator
     * tells us that the message is a:<br/>
     * <ul>
     * <li>reply to all recipients of a previous message</li>
     * <li>reply to one recipient of a previous message</li>
     * <li>previous message(s) forwarded to new recipients</li>
     * <li>an existing (draft) message being altered for resending</li>
     * </ul></p>
     */
    private Integer thread;

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;
    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        delete = false;
        deleteTrash = false;
        displayNext = null;
        displayPrevious = null;
        hasNext = false;
        hasPrevious = false;
        message = new MessageDO();
        sortAscending = true;
        sortBy = MailConstants.SORT_SENT;
        thread = null;
    }

    /**
     * <p>Indicates this message should be moved to the trash folder.
     * After the message has been moved, the next message in the folder
     * will
     * be displayed or (if there are no more messages) the folder
     * index.</p>
     *
     * @return the current value of delete.
     */
    public final boolean getDelete() {
        return delete;
    }

    /**
     * <p>Indicates this message should be permanently erased from the
     * trash folder. After the message has been deleted, the next message
     * in
     * the trash folder will be displayed or (if there are no more
     * messages)
     * the trash folder index.</p>
     *
     * @return the current value of deleteTrash.
     */
    public final boolean getDeleteTrash() {
        return deleteTrash;
    }

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the next message in the current folder
     * will be displayed.</p>
     *
     * @return the current value of displayNext.
     */
    public final String getDisplayNext() {
        return displayNext;
    }

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the previous message in the current
     * folder will be displayed.</p>
     *
     * @return the current value of displayPrevious.
     */
    public final String getDisplayPrevious() {
        return displayPrevious;
    }

    /**
     * <p>Get the name of the folder to locate messages which will be
     * forwarded, replied to or used as a draft template.</p>
     *
     * @return the current name of the folder where the messages are
     * located.
     */
    public final String getFolderName() {
        return message.getFolderName();
    }

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder after this one.</p>
     *
     * @return the current value of hasNext.
     */
    public final boolean getHasNext() {
        return hasNext;
    }

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder before this one.</p>
     *
     * @return the current value of hasPrevious.
     */
    public final boolean getHasPrevious() {
        return hasPrevious;
    }

    /**
     * <p>Get the unique identifier of the message to be displayed.</p>
     *
     * @return the current value of the message identifier.
     */
    public final String getId() {
        return message.getMessageID();
    }

    /**
     * <p>Get the message data object which contains the main
     * information in this form.</p>
     *
     * @return message data object which contains all of the
     * information in this form.
     */
    public MessageDO getMessage() {
        return message;
    }

    /**
     * <p>Get whether the messages are sorted in ascending or descending
     * order.</p>
     *
     * @return <code>true</code> if the messages are sorted in ascending
     * order, otherwise (<code>false</code>) indicates they are
     * descending.
     */
    public final boolean getSortAscending() {
        return sortAscending;
    }

    /**
     * <p>Get the field to sort the returned list by, one of the
     * <code>SORT_...</code> constants in {@link
     * com.ivata.groupware.business.mail.MailConstants MailConstants}.
     *
     * @return the current value indicating which column to sort by.
     */
    public final Integer getSortBy() {
        return sortBy;
    }

    /**
     * <p>Get the setting of <code>thread</code>. This indicator tells us
     * that the message is a:<br/>
     * <ul>
     * <li>reply to all recipients of a previous message</li>
     * <li>reply to one recipient of a previous message</li>
     * <li>previous message(s) forwarded to new recipients</li>
     * <li>an existing (draft) message being altered for resending</li>
     * </ul></p>
     *
     * @return the current value of thread, one of the constants in {@link
     * com.ivata.groupware.business.mail.MailConstants MailConstants},
     */
    public final Integer getThread() {
        return thread;
    }

    /**
     * <p>Indicates this message should be moved to the trash folder.
     * After the message has been moved, the next message in the folder
     * will
     * be displayed or (if there are no more messages) the folder
     * index.</p>
     *
     * @param delete the new value of delete.
     */
    public final void setDelete(final boolean delete) {
        this.delete = delete;
    }

    /**
     * <p>Indicates this message should be permanently erased from the
     * trash folder. After the message has been deleted, the next message
     * in
     * the trash folder will be displayed or (if there are no more
     * messages)
     * the trash folder index.</p>
     *
     * @param deleteTrash the new value of deleteTrash.
     */
    public final void setDeleteTrash(final boolean deleteTrash) {
        this.deleteTrash = deleteTrash;
    }

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the next message in the current folder
     * will be displayed.</p>
     *
     * @param displayNext the new value of displayNext.
     */
    public final void setDisplayNext(final String displayNext) {
        this.displayNext = displayNext;
    }

    /**
     * <p>Used to store the result from a submit button, if this is
     * non-<code>null</code>, then the previous message in the current
     * folder will be displayed.</p>
     *
     * @param displayPrevious the new value of displayPrevious.
     */
    public final void setDisplayPrevious(final String displayPrevious) {
        this.displayPrevious = displayPrevious;
    }

    /**
     * <p>Set the name of the folder to locate messages which will be
     * forwarded, replied to or used as a draft template.</p>
     *
     * @param folderName the current name of the folder where the messages
     * are located.
     */
    public final void setFolderName(final String folderName) {
        message.setFolderName(folderName);
    }

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder after this one.</p>
     *
     * @param hasNext the new value of hasNext.
     */
    public final void setHasNext(final boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * <p>Set by the action to indicate whether there are more messages in
     * the current folder before this one.</p>
     *
     * @param hasPrevious the new value of hasPrevious.
     */
    public final void setHasPrevious(final boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    /**
     * <p>Set the unique identifier of the message to be displayed.</p>
     *
     * @param messageId new value of the message identifier.
     */
    public final void setId(final String messageId) {
        this.message.setMessageID(messageId);
    }

    /**
     * <p>Set the message data object which contains the main
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
     * <p>Set whether the messages are sorted in ascending or descending
     * order.</p>
     *
     * @param sortAscending set to <code>true</code> if the messages are
     * sorted in ascending order, otherwise (<code>false</code>) indicates
     * they are descending.
     */
    public final void setSortAscending(final boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    /**
     * <p>Set the field to sort the returned list by, one of the
     * <code>SORT_...</code> constants in {@link
     * com.ivata.groupware.business.mail.MailConstants MailConstants}.
     *
     * @param sortBy the new value indicating which column to sort by.
     */
    public final void setSortBy(final Integer sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * <p>Set the setting of <code>thread</code>. This indicator tells us
     * that the message is a:<br/>
     * <ul>
     * <li>reply to all recipients of a previous message</li>
     * <li>reply to one recipient of a previous message</li>
     * <li>previous message(s) forwarded to new recipients</li>
     * <li>an existing (draft) message being altered for resending</li>
     * </ul></p>
     *
     * @param thread the new value of thread, one of the constants in
     * {@link com.ivata.groupware.business.mail.MailConstants MailConstants},
     */
    public final void setThread(final Integer thread) {
        this.thread = thread;
    }
    /**
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#validate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession)
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        // TODO Auto-generated method stub
        return null;
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
}
