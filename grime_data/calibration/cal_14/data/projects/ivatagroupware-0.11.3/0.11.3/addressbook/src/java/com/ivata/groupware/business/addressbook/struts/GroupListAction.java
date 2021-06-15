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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.TagUtils;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.group.GroupConstants;
import com.ivata.groupware.business.addressbook.person.group.GroupDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>
 * This action tells the JSP page <code>groupList.jsp</code> what to display,
 * and how to display it.
 * </p>
 *
 * @since ivata groupware 0.9 (2004-11-03)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */

public class GroupListAction extends MaskAction {
    /**
     * <p>
     * Address book implementation, used to retrieve address books/user groups.
     * </p>
     */
    private AddressBook addressBook;

    /**
     * <p>
     * Constructor. This is called by <code>PicoContainer</code>.
     * </p>
     *
     * @param addressBook Address book implementation, used to retrieve address
     *     books/user groups.
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public GroupListAction(final AddressBook addressBook,
            final MaskFactory maskFactory,
            MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.addressBook = addressBook;
    }

    /**
     * <p>Clear all bean properties to their default state.The difference
     * between this and <code>reset</code> is that all properties are changed,
     * regardless of current request state.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @see com.ivata.mask.web.struts.DialogForm#clear()
     */
    protected void clear() {
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
            throw new SystemException("No mode specified in GroupListAction");
        }
        Integer selectedId = StringHandling.integerValue(request
                .getParameter("selectedId"));

        // this page can handle 2 different types of content:
        // address books and user groups
        String contentFrameName, menuFrameName;
        List groups;
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        String inputMask, forward, menuFrameURI, resourceFieldPath, deleteKey;
        Boolean empty = null;
        if ("addressBook".equals(mode)) {
            contentFrameName = "ivataAddressBook";
            deleteKey = "addressBook.alert.delete";
            groups = addressBook.findAddressBooks(securitySession, true);
            inputMask = "imAddressBookInputMaskAction";
            forward ="addressBookGroupList";
            menuFrameURI = "/addressBook/groupList.action?mode=addressBook";
            resourceFieldPath = "addressBook";
        } else if ("userGroup".equals(mode)) {
            contentFrameName = "ivataUserGroup";
            deleteKey = "userGroup.alert.delete";
            groups = addressBook.findUserGroups(securitySession, false);
            inputMask = "imUserGroupInputMaskAction";
            forward ="addressBookGroupList";
            menuFrameURI = "/addressBook/groupList.action?mode=userGroup";
            resourceFieldPath = "userGroup";
        } else if ("group".equals(mode)) {
            contentFrameName = "ivataGroup";
            deleteKey = "group.alert.delete";
            groups = addressBook.findAddressBooks(securitySession, true);
            inputMask = "imGroupInputMaskAction";
            forward ="addressBookGroupTree";
            resourceFieldPath = "group";
            menuFrameURI = "/addressBook/groupTree.action"
                + "?inputMaskInvoked=true&mode=group";

            // indicates the requests did came from the group tree page
            boolean idIsAddressBook =
                "true".equals(request.getParameter("idIsAddressBook"));

            Integer selectedIdString = null;
            // if there is no request parameter, default back to the form
            // value
            if (selectedId == null) {
                try {
                    selectedId = (Integer)PropertyUtils.getProperty(form,
                            "selectedId");
                    // if we got the id from the form, that's an address
                    // book, AlRighty!
                    if (selectedId != null) {
                        idIsAddressBook = true;
                    }
                } catch (IllegalAccessException e) {
                    throw new SystemException(e);
                } catch (InvocationTargetException e) {
                    throw new SystemException(e);
                } catch (NoSuchMethodException e) {
                    throw new SystemException(e);
                }
            }
            if (selectedId == null) {
                // default to the system-wide address book
                selectedIdString = GroupConstants.ADDRESS_BOOK_DEFAULT;
                idIsAddressBook = true;
            } else {
                selectedIdString = selectedId;
            }
            if (!idIsAddressBook)  {
                // note in this case the selected id comes from the input
                // mask, which refers to the group which
                // is appearing in the right pane - we need to translate
                // that into the address book
                GroupDO group = addressBook.findGroupByPrimaryKey(
                        securitySession,
                        selectedIdString);
                assert (group != null);
                selectedIdString = group.getAddressBook().getId();
            }
            // see if the tree is empty or not
            List children = addressBook.findGroupsByParent(securitySession,
                    selectedIdString);
            empty = new Boolean(children.size() <= 0);
            selectedId = selectedIdString;
        } else {
            throw new SystemException("ERROR in GroupFrameAction: unidentified mode '"
                    + mode
                    + "'");
        }

        // we just want a map - the ids to the group names
        Map groupMap = new HashMap();
        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            GroupDO group = (GroupDO) iterator.next();
            // ids are converted to strings ... easier to compare
            groupMap.put(group.getId().toString(), group.getName());
        }

        try {
            TagUtils tagUtils = TagUtils.getInstance();

            PropertyUtils.setProperty(form, "contentFrameName", contentFrameName);
            PropertyUtils.setProperty(form, "deleteKey", deleteKey);
            PropertyUtils.setProperty(form, "groupMap", groupMap);
            PropertyUtils.setProperty(form, "inputMask", inputMask);
            PropertyUtils.setProperty(form, "menuFrameURI", menuFrameURI);
            PropertyUtils.setProperty(form, "resourceFieldPath", resourceFieldPath);
            PropertyUtils.setProperty(form, "selectedId", selectedId);

            // only the tree form has empty...
            if (empty != null) {
                PropertyUtils.setProperty(form, "empty", empty);
            }
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        }

        return forward;
    }
}
