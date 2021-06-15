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

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.right.LibraryRights;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p><code>Action</code> invoked when you calling topicModify.jsp
 * from topic.jsp.</p>
 * <!--**********************************************************************-->
 *
 * @since 2002-11-22
 * @author Jan Boros <janboros@sourceforge.net>
 * @version $Revision: 1.4 $
 */
public class FindTopicAction extends MaskAction {
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
    public FindTopicAction(Library library, LibraryRights libraryRights,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.library = library;
        this.libraryRights = libraryRights;
    }

    /**
     * <p>Submit or cancel the form in
     * <code>/library/topicModify.jsp</code>.</p>
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
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        try {
            TopicForm libraryTopicForm = new TopicForm(library);

            // request parameter overrides
            String requestId = request.getParameter(
                        "id");
            Integer id;
            if (requestId != null) {
                id = StringHandling.integerValue(requestId);
            } else {
                id = (Integer) PropertyUtils.getSimpleProperty(form, "id");
            }

            TopicDO topic = library.findTopicByPrimaryKey(securitySession, id);

            libraryTopicForm.setTopic(topic);

            // set view rights to ITEM in the form
            java.util.Collection rights = libraryRights.findRightsForItemsInTopic(
                    securitySession,
                    id,
                    RightConstants.ACCESS_VIEW);
            Integer[] tmp = new Integer[rights.size()];
            int a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsViewItem(tmp);

            // set add rights to ITEM in the form
            rights = libraryRights.findRightsForItemsInTopic(securitySession,
                    id,
                    RightConstants.ACCESS_ADD);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsAddItem(tmp);

            // set amend rights to ITEM in the form
            rights = libraryRights.findRightsForItemsInTopic(securitySession,
                    id,
                    RightConstants.ACCESS_AMEND);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsAmendItem(tmp);

            // set delete rights to ITEM in the form
            rights = libraryRights.findRightsForItemsInTopic(securitySession,
                    id,
                    RightConstants.ACCESS_REMOVE);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsRemoveItem(tmp);

            // set view rights to TOPIC in the form
            rights = libraryRights.findRightsForTopic(securitySession,
                    id,
                    RightConstants.ACCESS_VIEW);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsView(tmp);

            // set amend rights to TOPIC in the form
            rights = libraryRights.findRightsForTopic(securitySession,
                    id,
                    RightConstants.ACCESS_AMEND);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsAmend(tmp);

            // set delete rights to TOPIC in the form
            rights = libraryRights.findRightsForTopic(securitySession,
                    id,
                    RightConstants.ACCESS_REMOVE);
            tmp = new Integer[rights.size()];
            a = 0;

            for (java.util.Iterator i = rights.iterator(); i.hasNext();) {
                tmp[a] = (Integer) i.next();
                a++;
            }

            libraryTopicForm.setRightsRemove(tmp);
            libraryTopicForm.setTopicTab_activeTab(new Integer(0));
            session.setAttribute("libraryTopicForm", libraryTopicForm);
            session.removeAttribute("topicTab_activeTab");

            // Forward control to the topicModify.jsp - FORM for add new or modify existing TOPIC
            return "libraryTopic";
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
    }
}
