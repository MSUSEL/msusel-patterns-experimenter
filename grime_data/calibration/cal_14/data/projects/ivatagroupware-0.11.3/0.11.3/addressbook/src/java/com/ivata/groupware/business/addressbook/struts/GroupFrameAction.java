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
package com.ivata.groupware.business.addressbook.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>
 * This action tells the JSP page <code>groupFrame.jsp</code> what to display,
 * and how to display it.
 * </p>
 *
 * @since ivata groupware 0.10 (2004-11-03)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class GroupFrameAction extends MaskAction {
    /**
     * <p>
     * Constructor. Called by <strong>PicoContainer.</strong>.
     * </p>
     *
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public GroupFrameAction(MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
    }

    /**
     * <p>
     * This method does all the hard work in preparing data for
     * <code>frameIndex.jsp</code>.
     * </p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.

     * @see com.ivata.mask.web.struts.MaskAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionMessages, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.http.HttpSession)
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session)
            throws SystemException {
        String mode = request.getParameter("mode");
        if (StringHandling.isNullOrEmpty(mode)) {
            throw new SystemException("No mode specified in GroupFrameAction");
        }

        // this frame can handle 3 different types of content:
        // groups, address books and user groups
        String menuPage, contentFrameName;
        // content will be set via JavaScript on the menu pane
        String contentPage = "/util/loading.jsp";

        if ("group".equals(mode)) {
            menuPage = "/addressBook/groupTree.action?mode=group";
            contentFrameName = "ivataGroup";
        } else if ("addressBook".equals(mode)) {
            menuPage = "/addressBook/groupList.action?mode=addressBook";
            contentFrameName = "ivataAddressBook";
        } else if ("userGroup".equals(mode)) {
            menuPage = "/addressBook/groupList.action?mode=userGroup";
            contentFrameName = "ivataUserGroup";
        } else {
            throw new SystemException("ERROR in GroupFrameAction: unidentified mode '"
                    + mode
                    + "'");
        }

        try {
            PropertyUtils.setProperty(form, "contentFrameName", contentFrameName);
            PropertyUtils.setProperty(form, "contentPage", contentPage);
            PropertyUtils.setProperty(form, "menuFrameName",
                    contentFrameName + "List");
            PropertyUtils.setProperty(form, "menuPage", menuPage);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        }
        return null;
    }
}
