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
package com.ivata.groupware.business.library.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsDataTypeException;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Invoked when the user edits, displays or enters a new library
 * comment.</p>
 *
 * @since 2003-02-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 *
 */
public class CommentAction extends MaskAction {
    private Library library;
    private Settings settings;
    /**
     *  TODO
     * @param library
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public CommentAction(Library library, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.library = library;
        this.settings = settings;
    }

    /**
     * <p>Called when the clear button is pressed, or after an ok or
     * delete button, where the session should be restored to its default
     * state.</p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     */
    public void clear(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        // clear the comment form
        session.removeAttribute("libraryCommentForm");
    }

    /**
     * <p>Overridden to TODO:.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName current user name from session. .
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     *
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        CommentForm commentForm = (CommentForm) form;

        ItemForm itemForm = (ItemForm) session.getAttribute("libraryItemForm");

        if (StringHandling.isNullOrEmpty(commentForm.getList())) {
            commentForm.setList("false");
        }

        // if there is no item form, just get out
        // but only if we are not in list of openComments
        if (itemForm == null && commentForm.getList().equals("false")) {
            return "utilClosePopUp";
        }

        LibraryItemDO item = null;

        if (commentForm.getList().equals("false")) {
            item = itemForm.getItem();
        }

        Integer requestParentId = StringHandling.integerValue(
                request.getParameter("parentId"));
        Integer requestId = StringHandling.integerValue(
                request.getParameter("id"));
        CommentDO comment = commentForm.getComment();
        CommentDO parent = null;
        if (comment != null) {
            parent = comment.getParent();
        }
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        comment.setCreatedBy(securitySession.getUser());

        // reply to an existing comment
        if (requestParentId != null) {
            commentForm.setComment(comment = new CommentDO());
            comment.setParent(parent = library.findCommentByPrimaryKey(
                        securitySession, requestParentId));


            // edit an existing comment
        } else if (requestId != null) {
            commentForm.setComment(comment = library.findCommentByPrimaryKey(
                    securitySession, requestId));
        }

        if (comment.getItem() == null) {
            if (item != null) {
                comment.setItem(item);
            } else {
                comment.setItem(parent.getItem());
            }
        }

        if (comment.getCreatedBy() == null) {
            comment.setCreatedBy(securitySession.getUser());
        }

        commentForm.setBundle("library");
        commentForm.setDeleteKey("submitComment.alert.delete");

        // set default values here
        if (StringHandling.isNullOrEmpty(comment.getSubject())) {
            try {
                comment.setSubject(settings.getStringSetting(securitySession,
                        "emailSubjectReplyPrefix",
                        securitySession.getUser()) +
                    (((parent == null) ||
                    StringHandling.isNullOrEmpty(parent.getSubject()))
                    ? ((item != null) ? item.getTitle() : comment.getItem().getTitle())
                    : parent.getSubject()));
            } catch (SettingsDataTypeException e) {
                throw new SystemException(e);
            }
        }
        return null;
    }

    /**
     * <p>This method is called if the ok or apply buttons are
     * pressed.</p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @param ok <code>true</code> if the ok button was pressed, otherwise
     * <code>false</code> if the apply button was pressed.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {

        // new comment ?
        CommentDO comment = ((CommentForm) form).getComment();
        CommentDO parent = comment.getParent();
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        StringBuffer openerPage = new StringBuffer();

        if (((CommentForm) form).getList().equals("true")) {
            openerPage.append("/library/openComments.jsp");
        } else {
            openerPage.append("/library/display.action?id=");
            openerPage.append(comment.getItem().getId());
        }

        if (comment.getId() == null) {
            comment.setCreatedBy(securitySession.getUser());

            Integer parentId = (parent == null) ? null : parent.getId();

            comment = library.addComment(securitySession, comment);
        } else {
            // amend an existing comment
            library.amendComment(securitySession, comment);
        }

        // flush the comment tree for this item from jsp cache
        //flushCache("itemCommentTree_" + comment.getItemId().toString(),PageContext.APPLICATION_SCOPE, request);

        // if it was 'ok' we won't know the id till now
        if (!((CommentForm) form).getList().equals("true")) {
            openerPage.append("#comment");
            openerPage.append(comment.getId());
        }

        request.setAttribute("openerPage", openerPage.toString());

        return defaultForward;
    }

    /**
     * <p>This method is called if the delete (confirm, not warn) button
     * is pressed.</p>
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param log valid logging object to write messages to.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     *
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String onDelete(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session, final String defaultForward) throws SystemException {
        return null;
    }
}
