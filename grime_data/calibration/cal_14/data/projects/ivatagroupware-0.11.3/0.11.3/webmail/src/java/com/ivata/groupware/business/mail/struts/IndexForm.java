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

import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.business.mail.MailConstants;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>This form is used to identify a message folder to list and the
 * selected messages on that folder.</p>
 *
 * @since 2002-11-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 *
 */
public class IndexForm extends DialogForm {
    /**
     * <p>
     * Date formatter, used in the JSP page.
     * </p>
     */
    private SettingDateFormatter dateFormatter;

    /**
     * <p>If the current folder (see {@link #getFolderName getFolderName})
     * is the trash folder, setting this <code>true</code> specifies the
     * selected messages should be permanently deleted.</p>
     */
    private boolean deleteTrash;

    /**
     * <p>The name of the folder to locate messages which will be
     * forwarded, replied to or used as a draft template.</p>
     */
    private String folderName;

    /**
     * <p>Determines whether or not the selected messages should be
     * forwarded.</p>
     */
    private boolean forwardMessages;

    /**
     * <p>Automatically set by the <code>ListBodyTag</code>, this
     * attribute sets the first row in the page.</p>
     */
    private int listRowFrom;

    /**
     * <p>Column of the display list which corresponds to
     * <code>sortBy</code>,</p>
     */
    private int listSortColumn;

    /**
     * <p>Contains all message ids being displayed (to compare with the
     * selected ones).</p>
     */
    private String[] messageIds;

    /**
     * <p>Indicates a folder name to which the currently selected messages
     * should be moved to.</p>
     */
    private String moveTo;

    /**
     * <p>Determines whether or not previously selected ids should be
     * retained.</p>
     */
    private boolean retainPrevious;

    /**
     * <p>Indicates all messages should be marked as selected. This
     * selects all ids not shown on the current page - JavaScript selects
     * the ones on the current page.</p>
     */
    private boolean selectAll;

    /**
     * <p>Contains all message ids the user selected.</p>
     *
     */
    private String[] selectedMessageIds;

    /**
     * <p>If <code>true</code> then the messages are sorted in ascending
     * order, otherwise (<code>false</code>) they are descending.</p>
     */
    private boolean sortAscending;

    /**
     * <p>The field to sort the returned list by. Set to one of the
     * <code>SORT_...</code> constants in {@link
     * com.ivata.groupware.business.mail.MailConstants MailConstants}.
     */
    private Integer sortBy;

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
     * Construct an index form. This is created by the request processor.
     * </p>
     *
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     * @param dateFormatter used in the JSP page.
     */
    public IndexForm(final SettingDateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }
    /**
     * <p>
     * Return all form state to initial values.
     * </p>
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        deleteTrash = false;
        folderName = null;
        forwardMessages = false;
        listRowFrom = 0;
        listSortColumn = 4;
        messageIds = null;
        moveTo = null;
        retainPrevious = false;
        selectAll = false;
        selectedMessageIds = null;
        sortAscending = true;
        sortBy = MailConstants.SORT_SENT;
    }
    /**
     * <p>
     * Date formatter, used in the JSP page.
     * </p>
     *
     * @return used in the JSP page.
     */
    public final SettingDateFormatter getDateFormatter() {
        return dateFormatter;
    }

    /**
     * <p>If the current folder (see {@link #getFolderName getFolderName})
     * is the trash folder, setting this <code>true</code> specifies the
     * selected messages should be permanently deleted.</p>
     *
     * @return <code>true</code> if the selected messages will be deleted,
     * otherwise <code>false</code>.
     */
    public final boolean getDeleteTrash() {
        return deleteTrash;
    }

    /**
     * <p>Get the name of the folder to locate messages which will be
     * forwarded, replied to or used as a draft template.</p>
     *
     * @return the current name of the folder where the messages are
     * located.
     */
    public final String getFolderName() {
        return folderName;
    }

    /**
     * <p>Find out if the selected messages should be forwarded as a new
     * message.</p>
     *
     * @return <code>true</code> to specify that the selected messages
     * should be forwarded as a new message, otherwise <code>false</code>.
     */
    public final boolean getForwardMessages() {
        return forwardMessages;
    }

    /**
     * <p>Automatically set by the <code>ListBodyTag</code>, this
     * attribute sets the first row in the page.</p>
     *
     * @return the first row to be shown in the list control
     */
    public final int getListRowFrom() {
        return listRowFrom;
    }

    /**
     * <p>Column of the display list which corresponds to
     * <code>sortBy</code>,</p>
     *
     * @return the current value of listSortColumn.
     */
    public final int getListSortColumn() {
        return listSortColumn;
    }

    /**
     * <p>Contains all message ids being displayed (to compare with the
     * selected ones).</p>
     *
     * @return the current value of messageIds.
     */
    public final String[] getMessageIds() {
        return messageIds;
    }

    /**
     * <p>Get the folder name to which the currently selected messages
     * should be moved.</p>
     *
     * @return the name of the folder to move the currently selected
     * messages to, or <code>null</code> if no move action was selected..
     */
    public final String getMoveTo() {
        return moveTo;
    }

    /**
     * <p>Get whether or not previously selected ids should be
     * retained.</p>
     *
     * @return <code>true</code> if previously selected message ids should
     * be retained, otherwise <code>false</code>.
     */
    public final boolean getRetainPrevious() {
        return retainPrevious;
    }

    /**
     * <p>Get whether all messages should be marked as selected. This
     * selects all ids not shown on the current page - JavaScript selects
     * the ones on the current page.</p>
     *
     * @return <code>true</code> if all messages not shown should be
     * selected, otherwise <code>false</code>
     */
    public final boolean getSelectAll() {
        return selectAll;
    }

    /**
     * <p>Contains all message ids the user selected.</p>
     *
     * @return current value of the message ids the user selected
     */
    public final String[] getSelectedMessageIds() {
        return selectedMessageIds;
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
     * <p>Reset all bean properties to their default state.  This method
     * is called before the properties are repopulated by the controller
     * servlet.<p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing.
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        this.folderName = null;
        this.listRowFrom = 0;
        this.messageIds = new String[0];
        this.moveTo = null;
        this.retainPrevious = false;
        this.selectedMessageIds = new String[0];
        this.selectAll = false;
        this.sortAscending = false;
        this.sortBy = MailConstants.SORT_SENT;
        this.forwardMessages = false;
        this.deleteTrash = false;
    }

    /**
     * <p>
     * Date formatter, used in the JSP page.
     * </p>
     *
     * @param formatter used in the JSP page.
     */
    public final void setDateFormatter(final SettingDateFormatter formatter) {
        dateFormatter = formatter;
    }

    /**
     * <p>If the current folder (see {@link #getFolderName getFolderName})
     * is the trash folder, setting this <code>true</code> specifies the
     * selected messages should be permanently deleted.</p>
     *
     * @param deleteTrash <code>true</code> if the selected messages will
     * be deleted, otherwise <code>false</code>.
     */
    public final void setDeleteTrash(final boolean deleteTrash) {
        this.deleteTrash = deleteTrash;
    }

    /**
     * <p>Set the name of the folder to locate messages which will be
     * forwarded, replied to or used as a draft template.</p>
     *
     * @param folderName the current name of the folder where the messages
     * are located.
     */
    public final void setFolderName(final String folderName) {
        this.folderName = folderName;
    }

    /**
     * <p>Specify that the selected messages should be forwarded as a new
     * message.</p>
     *
     * @param forwardMessages <code>true</code> to specify that the
     * selected messages should be forwarded as a new message, otherwise
     * <code>false</code>.
     */
    public final void setForwardMessages(final boolean forwardMessages) {
        this.forwardMessages = forwardMessages;
    }

    /**
     * <p>Automatically set by the <code>ListBodyTag</code>, this
     * attribute sets the first row in the page.</p>
     *
     * @param listRowFrom the first row to be displayed in the list
     * control.
     */
    public final void setListRowFrom(final int listRowFrom) {
        this.listRowFrom = listRowFrom;
    }

    /**
     * <p>Column of the display list which corresponds to
     * <code>sortBy</code>,</p>
     *
     * @param listSortColumn the new value of listSortColumn.
     */
    public final void setListSortColumn(final int listSortColumn) {
        this.listSortColumn = listSortColumn;
    }

    /**
     * <p>Contains all message ids being displayed (to compare with the
     * selected ones).</p>
     *
     * @param messageIds the new value of messageIds.
     */
    public final void setMessageIds(final String[] messageIds) {
        this.messageIds = messageIds;
    }

    /**
     * <p>Set the folder name to which the currently selected messages
     * should be moved.</p>
     *
     * @param folderName the name of the folder to move the currently
     * selected messages to, or <code>null</code> if no move action was
     * selected..
     */
    public final void setMoveTo(final String moveTo) {
        this.moveTo = moveTo;
    }

    /**
     * <p>Set whether or not previously selected ids should be
     * retained.</p>
     *
     * @param retainPrevious set to <code>true</code> if previously
     * selected message ids should be retained, otherwise
     * <code>false</code>.
     */
    public final void setRetainPrevious(final boolean retainPrevious) {
        this.retainPrevious = retainPrevious;
    }

    /**
     * <p>Set whether all messages should be marked as selected. This
     * selects all ids not shown on the current page - JavaScript selects
     * the ones on the current page.</p>
     *
     * @param selectAll <code>true</code> if all messages not shown should
     * be selected, otherwise <code>false</code>
     */
    public final void setSelectAll(final boolean selectAll) {
        this.selectAll = selectAll;
    }

    /**
     * <p>Contains all message ids the user selected.</p>
     *
     * @param selectedMessageIds the new value of the message ids the user
     * selected
     */
    public final void setSelectedMessageIds(final String[] selectedMessageIds) {
        this.selectedMessageIds = selectedMessageIds;
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
