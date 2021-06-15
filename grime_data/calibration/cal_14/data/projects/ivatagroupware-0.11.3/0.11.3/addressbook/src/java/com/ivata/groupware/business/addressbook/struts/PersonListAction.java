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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

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
 * <p>Retrieves a list of people for the main index page of the
 * address book.</p>
 *
 * @since 2003-01-26
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class PersonListAction extends MaskAction {
    private AddressBook addressBook;
    /**
     * <p>TODO</p>
     *
     * @param addressBook
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public PersonListAction(AddressBook addressBook,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.addressBook = addressBook;
    }


    /**
     * <p>Retrieve a list of people to show in the main index view of the
     * address book.</p>
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
        // group is from the tree combo selector (frmPersonList), index from tabs
        Integer comboAddressBook = null;
        Integer comboGroup = null;
        String index = null;
        try {
            comboAddressBook = (Integer) PropertyUtils.getSimpleProperty(form, "comboAddressBook");
            comboGroup = (Integer) PropertyUtils.getSimpleProperty(form, "comboGroup");
            index = (String) PropertyUtils.getSimpleProperty(form, "index");
            // request parameter overrides the form
            String requestIndex = request.getParameter("index");
            if (!StringHandling.isNullOrEmpty(requestIndex)) {
                PropertyUtils.setSimpleProperty(form, "index", index = requestIndex);
            }
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }
        // request overrides form state
        if (request.getParameter("index") != null) {
            index = request.getParameter("index");
        }
        if (request.getParameter("comboAddressBook") != null) {
            comboAddressBook = StringHandling.integerValue(
                    request.getParameter("comboAddressBook"));
        }
        if (request.getParameter("comboGroup") != null) {
            comboGroup = StringHandling.integerValue(
                    request.getParameter("comboGroup"));
        }

        // have to assign the results to something
        List results;

        // need to do this conversion, because 4 - the address_book group doesn't contain
        // people, and the search by group isn't recursive... all the people
        // will be found this way - with no group membership lookup
        if ((comboAddressBook == null)
                || comboAddressBook.equals("4")) {
            comboAddressBook = new Integer(0);
            comboGroup = new Integer(0);
        }

        // default the index to everyone
        if (StringHandling.isNullOrEmpty(index)) {
            index = "all";
        }
        String initialLetter = "all".equals(index) ? null : index;
        try {
            Integer groupId;

            // if there is a specific group, go for that
            if (!"".equals(comboGroup)) {
                groupId = comboGroup;

            // otherwise see if a general address book was specified
            } else if (!"".equals(comboAddressBook)) {
                groupId = comboAddressBook;
            } else {
                groupId = GroupConstants.ADDRESS_BOOK;
            }
            GroupDO parentGroup = addressBook.findGroupByPrimaryKey(securitySession,
                groupId);
            results = addressBook.findAllPeopleInGroup(securitySession,
                parentGroup, initialLetter);
        } catch(SystemException e) {
            throw new SystemException(e);
        }

        List addressBooks = addressBook.findAddressBooks(securitySession, true);
        List addressBookNames = new Vector();
        Iterator addressBookIterator = addressBooks.iterator();
        while(addressBookIterator.hasNext()) {
            GroupDO addressBook = (GroupDO) addressBookIterator.next();
            addressBookNames.add(addressBook.getName());
        }

        // now store the results we want to show
        try {
            PropertyUtils.setSimpleProperty(form, "comboAddressBook", comboAddressBook);
            PropertyUtils.setSimpleProperty(form, "comboGroup", comboGroup);
            PropertyUtils.setSimpleProperty(form, "index", index);
            PropertyUtils.setSimpleProperty(form, "addressBooks", addressBookNames);
            PropertyUtils.setSimpleProperty(form, "results", results);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }

        // this list always goes to the same page
        return null;
    }
}
