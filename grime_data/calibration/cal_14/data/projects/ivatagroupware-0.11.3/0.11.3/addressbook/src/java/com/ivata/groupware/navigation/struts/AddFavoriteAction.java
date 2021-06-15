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
package com.ivata.groupware.navigation.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.navigation.menu.MenuConstants;
import com.ivata.groupware.navigation.menu.MenuDO;
import com.ivata.groupware.navigation.menu.item.MenuItemDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.RewriteHandling;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>This action is called when the user clicks on the
 * 'add to favorites' heart in the title frame.</p>
 *
 * @since 2003-03-04
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class AddFavoriteAction extends MaskAction {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(AddFavoriteAction.class);
    /**
     * <p>
     * Used to retrieve favorites menu and add new items to it.
     * </p>
     */
    private PersistenceManager persistenceManager;

    /**
     * <p>
     * Settings implementation.
     * </p>
     */
    private Settings settings;

    /**
     * Construct the action.
     *
     * @param persisitenceManager used to retrieve favorites menu, and add new
     * items to it.
     * @param settings settings implementation.
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public AddFavoriteAction(final PersistenceManager persistenceManagerParam,
            final Settings settings,
            final MaskFactory maskFactory,
            final MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.persistenceManager = persistenceManagerParam;
        this.settings = settings;
    }

    /**
     * <p>Add the title and URL in request paramters to the favorites
     * and forward to the left frame refreshed.</p>
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
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        if (log.isDebugEnabled()) {
            log.debug("In AddFavoriteAction.execute.");
        }
        // TODO: might want to make favorite id a setting somewhere
        // - for now, it is hard-coded to 0 :-)

        // store the favorite, only store the path without the contextPath and
        // strip the jsessionid
        String favorite, uRL;
        try {
            favorite = (String) PropertyUtils.getSimpleProperty(form, "favorite");;
            uRL = (String) PropertyUtils.getSimpleProperty(form, "uRL");;
            if (log.isDebugEnabled()) {
                log.debug("Favorite title is '"
                        + favorite
                        + "', URL is '"
                        + uRL
                        + "'");
            }
        } catch (NoSuchMethodException e) {
            throw new SystemException(e);
        } catch (InvocationTargetException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        }

        // do nothing when URL or title empty
        if (StringHandling.isNullOrEmpty(favorite)
                || StringHandling.isNullOrEmpty(uRL) ) {
            log.warn("Either favorite title ("
                    + favorite
                    + ") or URL ("
                    + uRL
                    + ") is null --> returning null and not adding favorite");
            return null;
        }
        SecuritySession securitySession =
            (SecuritySession) session.getAttribute("securitySession");

        // remove the context path - we want relative links
        uRL = uRL.substring(uRL.lastIndexOf(
                RewriteHandling.getContextPath(request)));


        // remove the " - sitename" from the title
        StringBuffer standardEnding = new StringBuffer("- ");
        standardEnding.append(settings.getStringSetting(securitySession,
                "siteTitle", securitySession.getUser()));
        if (favorite.endsWith(standardEnding.toString())) {
            favorite = favorite.substring(0, favorite.length()
                    - standardEnding.length()).trim();
        }

        // URL rewriting case, the jsessionid part has to be stripped out
        if (uRL.indexOf("jsessionid")!=-1) {
            int jsessionStart = uRL.indexOf(";");
            int jsessionEnd = uRL.lastIndexOf("?");
            if (jsessionStart < jsessionEnd) {
                uRL = uRL.substring(0, jsessionStart) +
                                              uRL.substring(jsessionEnd);
            } else {
                uRL = uRL.substring(0,jsessionStart);
            }
        }
        // get the favorites menu
        PersistenceSession persistenceSession = persistenceManager
            .openSession(securitySession);
        try {
            MenuDO favoritesMenu = (MenuDO)persistenceManager.findByPrimaryKey(
                    persistenceSession,
                    MenuDO.class,
                    MenuConstants.ID_FAVORITES
                    );

            MenuItemDO menuItem = new MenuItemDO();

            menuItem.setMenu(favoritesMenu);
            menuItem.setUser(securitySession.getUser());
            menuItem.setImage(null);
            menuItem.setText(favorite);
            menuItem.setURL(uRL);
            // use the inverse count to ensure the new item always appears at
            // the top!
            menuItem.setPriority(new Integer(0
                    - favoritesMenu.getItems().size()));
            persistenceManager.add(persistenceSession, menuItem);
        } finally {
            persistenceSession.close();
        }

        // this should always go to left.jsp
        return null;
    }
}
