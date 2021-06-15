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
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.right.LibraryRights;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p><code>Action</code> invoked whenever
 * <code>/library/topicModify.jsp</code> is submitted.</p>
 *
 *
 * @since 2002-11-22
 * @author Jan Boros <janboros@sourceforge.net>
 * @version $Revision: 1.3 $
 */
public class TopicAction extends MaskAction {
    Library library;
    LibraryRights libraryRights;

    /**
     * TODO
     * @param library
     * @param libraryRights
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public TopicAction(Library library, LibraryRights libraryRights,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.library = library;
        this.libraryRights = libraryRights;
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
        TopicForm topicForm = (TopicForm) form;
        TopicDO topic = topicForm.getTopic();
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");

        // if I have not ID , so it's new TOPIC
        if (topic.getId() == null) {
            topic = library.addTopic(securitySession, topic);
        } else {
            topic = library.amendTopic(securitySession, topic);
        }

        // amend RIGHTS for TOPIC
        /* TODO
        try {
            libraryRights.amendViewRightsForTopic(securitySession, topic.getId(),
                new java.util.Vector(java.util.Arrays.asList(
                        topicForm.getRightsView())));
            libraryRights.amendAmendRightsForTopic(securitySession, topic.getId(),
                new java.util.Vector(java.util.Arrays.asList(
                        topicForm.getRightsAmend())));
            libraryRights.amendRemoveRightsForTopic(securitySession, topic.getId(),
                new java.util.Vector(java.util.Arrays.asList(
                        topicForm.getRightsRemove())));
        } catch (SystemException e) {
            throw new SystemException(e);
        }
        */
        request.setAttribute("openerPage",
            "/library/topicItems.jsp?topicId=" + topic.getId());

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
        TopicForm topicForm = (TopicForm) form;
        TopicDO topic = topicForm.getTopic();
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");

        library.removeTopic(securitySession, topic);
        request.setAttribute("openerPage", "/library/topic.jsp");

        return null;
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
            final HttpSession session)
            throws SystemException {
        session.removeAttribute("topicTab_activeTab");
        session.removeAttribute("libraryTopicForm");
    }
}
