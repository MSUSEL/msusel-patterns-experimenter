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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.groupware.business.addressbook.person.group.right.RightConstants;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.HTMLFormatter;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>
 * Retrieves a list of recent library items.
 * </p>
 *
 * @since 2004-06-27
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class IndexAction extends MaskAction {
    private Library library;
    private Settings settings;
    /**
     * TODO
     * @param securitySession
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public IndexAction(SecuritySession securitySession, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        PicoContainer container = securitySession.getContainer();
        Object object = container.getComponentInstance(PersistenceManager.class);
        object = container.getComponentInstance(AddressBook.class);
        object = container.getComponentInstance(Mail.class);
        object = container.getComponentInstance(Settings.class);
        object = container.getComponentInstance(HTMLFormatter.class);
        this.library = (Library) container.getComponentInstance(Library.class);
//        this.library = library;
        this.settings = settings;
    }


    /**
     * <p>
     * Retrieves a list of recent library items.
     * </p>
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
            Integer libraryHome = settings.getIntegerSetting(securitySession,
                    "libraryHome", securitySession.getUser());
            Integer libraryRecent = settings.getIntegerSetting(securitySession,
                    "libraryRecent", securitySession.getUser());
            int numberOfItems = libraryHome.intValue();
            int numberOfRecent = libraryRecent.intValue();
            int numberToRead = (numberOfRecent > numberOfItems) ? numberOfRecent : numberOfItems;

            Collection items = library.findRecentItems(securitySession, new Integer(numberToRead),
                    RightConstants.ACCESS_VIEW, null);
            PropertyUtils.setProperty(form, "items", items);

            Iterator itemIterator = items.iterator();

            Map commentsForItem = new HashMap();
            while(itemIterator.hasNext()) {
                LibraryItemDO item = (LibraryItemDO) itemIterator.next();
                int commentCount = library.countCommentsForItem(securitySession,
                        item.getId());
                commentsForItem.put(item.getId(), new Integer(commentCount));
            }
            PropertyUtils.setProperty(form, "commentsForItem", commentsForItem);

            Collection unacknowledgedComments = library.findUnacknowledgedComments(securitySession, new Integer(10));
            PropertyUtils.setProperty(form, "unacknowledgedComments", unacknowledgedComments);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        }

        // this list always goes to the same page
        return null;
    }
}
