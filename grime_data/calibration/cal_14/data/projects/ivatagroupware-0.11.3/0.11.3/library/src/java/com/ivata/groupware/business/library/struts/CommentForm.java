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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.validation.ValidationErrors;


/**
 * <p>Contains details of a comment which is being changed.</p>
 *
 * @since 2003-02-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class CommentForm extends LibraryForm {
    /**
     * <p>Contains the comment currently being edited.</p>
     */
    private CommentDO comment;
    /**
     * <p>
     * Library implementation.
     * </p>
     */
    private Library library;
    /**
     *<p>it <code>true</code> that refresh list of openComments, otherwise refres display item page.</p>
     */
    private String list;
    /**
     *
     * @param library
     * @param maskParam
     *            Refer to {@link #getMask}.
     * @param baseClassParam
     *            Refer to {@link #getBaseClass}.
     */
    public CommentForm(final Library library) {
        this.library = library;
        clear();
    }

    /**
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        comment = new CommentDO();
        list = null;
    }

    /**
     * <p>Contains the comment currently being edited.</p>
     *
     * @return the current value of comment.
     */
    public final CommentDO getComment() {
        return comment;
    }

    /**
     *<p>it <code>true</code> that refresh list of openComments, otherwise refres display item page.</p>
     *
     * @return the current value of list.
     */
    public final String getList() {
        return this.list;
    }

    /**/
    public void reset(final HttpServletRequest request,
            final HttpSession session) {
        comment = new CommentDO();
    }

    /**
     * <p>Contains the comment currently being edited.</p>
     *
     * @param comment the new value of comment.
     */
    public final void setComment(final CommentDO comment) {
        this.comment = comment;
    }

    /**
     *<p>it <code>true</code> that refresh list of openComments, otherwise refres display item page.</p>
     *
     * @param list the new value of list.
     */
    public final void setList(final String list) {
        this.list = list;
    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionMessages</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found,
     * return <code>null</code> or an <code>ActionMessages</code> object with
     * no recorded error messages.
     * <p>
     * The default ipmlementation performs no validation and returns
     * <code>null</code>.  Subclasses must override this method to provide
     * any validation they wish to perform.
     *
     * @param request The servlet request we are processing.
     * @param session The HTTP session we are processing.
     * @see com.ivata.mask.web.struts.MaskForm#validate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession)
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        // if there is no ok, just get out
        if (StringHandling.isNullOrEmpty(getOk())) {
            return null;
        }

        // if it gets here - ok was pressed. validate on the server side
        ActionErrors errors;
        ValidationErrors validationErrors;

        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        validationErrors = library.validate(securitySession, comment);

        return validationErrors;
    }
}
