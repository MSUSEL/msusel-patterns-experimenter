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
// Source file: h:/cvslocal/ivata groupware/src/com/ivata/intranet/admin/setting/struts/SettingAction.java

/*
 * Copyright (c) 2001 - 2005 ivata limited.
 * All rights reserved.
 * -----------------------------------------------------------------------------
 * ivata groupware may be redistributed under the GNU General Public
 * License as published by the Free Software Foundation;
 * version 2 of the License.
 *
 * These programs are free software; you can redistribute them and/or
 * modify them under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2 of the License.
 *
 * These programs are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License in the file LICENSE.txt for more
 * details.
 *
 * If you would like a copy of the GNU General Public License write to
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA.
 *
 *
 * To arrange commercial support and licensing, contact ivata at
 *                  http://www.ivata.com/contact.jsp
 * -----------------------------------------------------------------------------
 * $Log: SettingAction.java,v $
 * Revision 1.3  2005/04/10 18:47:34  colinmacleod
 * Changed i tag to em and b tag to strong.
 *
 * Revision 1.2  2005/04/09 17:19:05  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:50:44  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.10  2004/12/31 18:27:42  colinmacleod
 * Added MaskFactory to constructor of MaskAction.
 *
 * Revision 1.9  2004/12/23 21:01:25  colinmacleod
 * Updated Struts to v1.2.4.
 * Changed base classes to use ivata masks.
 *
 * Revision 1.8  2004/11/12 18:19:13  colinmacleod
 * Change action and form classes to extend MaskAction, MaskForm respectively.
 *
 * Revision 1.7  2004/11/12 15:56:46  colinmacleod
 * Removed dependencies on SSLEXT.
 * Moved Persistence classes to ivata masks.
 *
 * Revision 1.6  2004/11/03 15:31:49  colinmacleod
 * Change method interfaces to remove log.
 *
 * Revision 1.5  2004/07/13 19:41:12  colinmacleod
 * Moved project to POJOs from EJBs.
 * Applied PicoContainer to services layer (replacing session EJBs).
 * Applied Hibernate to persistence layer (replacing entity EJBs).
 *
 * Revision 1.4  2004/03/21 21:16:05  colinmacleod
 * Shortened name to ivata op.
 *
 * Revision 1.3  2004/02/10 19:57:21  colinmacleod
 * Changed email address.
 *
 * Revision 1.2  2004/02/01 22:00:32  colinmacleod
 * Added full names to author tags
 *
 * Revision 1.1.1.1  2004/01/27 20:57:47  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.3  2003/10/17 12:36:12  jano
 * fixing problems with building
 * converting intranet -> portal
 * Eclipse building
 *
 * Revision 1.2  2003/10/15 13:04:21  colin
 * fixing for XDoclet
 *
 * Revision 1.18  2003/03/26 16:52:52  peter
 * when there was no user setting before and only the system one was changed, new user setting not created
 *
 * Revision 1.17  2003/03/04 14:19:35  colin
 * added emailDefaultFormat
 *
 * Revision 1.16  2003/03/03 16:57:08  colin
 * converted localization to automatic paths
 * added labels
 * added mandatory fieldName attribute
 *
 * Revision 1.15  2003/02/25 17:00:07  peter
 * added parent refreshing on close
 *
 * Revision 1.14  2003/02/24 18:56:15  colin
 * added to admin
 *
 * Revision 1.13  2003/02/19 18:20:46  peter
 * fixed validation and input format doubling
 *
 * Revision 1.10  2003/02/13 17:24:18  peter
 * added new tabs: controls, notification
 *
 * Revision 1.9  2003/02/12 17:25:34  peter
 * added library and notification tab
 *
 * Revision 1.8  2003/02/12 14:12:50  peter
 * rights do work
 *
 * Revision 1.5  2003/02/05 18:15:07  peter
 * works on validation, and changed to Map backed form
 * -----------------------------------------------------------------------------
 */
package com.ivata.groupware.admin.setting.struts;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.right.SettingsRights;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p><code>Action</code> invoked whenever
 * <code>/setting.jsp</code> is submitted.</p>
 *
 * @since 2003-01-27
 * @author Peter Illes
 * @version $Revision: 1.3 $
 */
public class SettingAction extends MaskAction {
    private Settings settings;
    private SettingsRights settingsRights;
    /**
     * <p>
     * TODO
     * </p>
     *
     * @param settings
     * @param settingsRights
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public SettingAction(Settings settings, SettingsRights settingsRights,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.settings = settings;
        this.settingsRights = settingsRights;
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
        session.removeAttribute("settingTab_activeTab");
        session.removeAttribute("settingForm");
    }

    /**
     * <p>Called from the other <code>execute</code> method, this can
     * be overridden by each subclass to provide the <em>ivata</em>-specific
     * processing required.</p>
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
        SettingForm settingForm = (SettingForm) form;
        List tabName = settingForm.getTabName();
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");

        Integer activeTab = settingForm.getSettingTab_activeTab();

        // if the tab is null, it should be the first call, INITIALISE:
        if (settingForm.getSettingTab_activeTab() == null) {
            // these two Lists will contain setting names for individual tabs
            Vector settingTabUser = new Vector();
            Vector settingTabSystem = new Vector();

            //very important, the administrator rights:
            if (settingsRights.canAmendSystemSettings(securitySession)) {
                settingForm.setAdministrator("true");
            }

            // the jsp will start in user mode
            settingForm.setSettingArea("user");
            // the default jsp tab
            activeTab = new Integer(0);
            settingForm.setSettingTab_activeTab(activeTab);
            // create the vector of tab names, only add tabs with at least
            // one setting enabled (or allowed to change)
            tabName = new Vector();
            // these two work Lists will hold the names of succesful settings
            List settingNameUser;
            List settingNameSystem;

            // --- the <strong>general</strong> tab
            settingNameUser = initialiseSettings(securitySession,
                        new String[]
                        {"siteDefaultForward"},
                        settingForm);
            settingNameSystem = initialiseSettings(securitySession,
                        new String[]
                        {"siteTitle", "siteName", "siteWelcomeText"},
                        settingForm);
            if (!(settingNameUser.isEmpty() && settingNameSystem.isEmpty())) {
                tabName.add("general");
                settingTabUser.add(settingNameUser);
                settingTabSystem.add(settingNameSystem);
            }

            // --- the <strong>controls</strong> tab
            settingNameUser = initialiseSettings(securitySession,
                        new String[]
                        {"guiShortcutStyle", "guiShortcutPause"},
                        settingForm);
            if (!settingNameUser.isEmpty()) {
                tabName.add("controls");
                settingTabUser.add(settingNameUser);
                settingTabSystem.add(new Vector());
            }

            // --- the <strong>email</strong> tab
            settingNameUser = initialiseSettings(securitySession,
                    new String[] {
                "emailDefaultFormat",
                "emailReplyIndent",  "emailFolderInbox", "emailFolderSent",
                "emailFolderTrash", "emailFolderDrafts", "emailSignatureHTML",
                "emailSignatureText", "emailSignatureForward", "emailSignatureReply"},
                        settingForm);
            settingNameSystem = initialiseSettings(securitySession,
                    new String[] {
                "emailHost", "emailPort", "emailHostSmtp"},
                        settingForm);
            if (!(settingNameUser.isEmpty() && settingNameSystem.isEmpty())) {
                tabName.add("email");
                settingTabUser.add(settingNameUser);
                settingTabSystem.add(settingNameSystem);
            }

            // --- the <strong>library</strong> tab
            settingNameUser = initialiseSettings(securitySession,
                    new String[] {
                "libraryHome",  "libraryRecent", "libraryCommentSpacer"},
                        settingForm);
            if (!settingNameUser.isEmpty()) {
                tabName.add("library");
                settingTabUser.add(settingNameUser);
                settingTabSystem.add(new Vector());
            }

            // --- the <strong>notification</strong> tab
            settingNameSystem = initialiseSettings(securitySession,
                    new String[] {
                "libraryNotificationContentComment",
                "libraryNotificationContentCommentAmend",
                "libraryNotificationContentItem",
                "libraryNotificationContentItemAmend",
                "libraryNotificationContentItemReply",
                "libraryNotificationContentItemReplyAmend"},
                        settingForm);
            if (!settingNameSystem.isEmpty()) {
                tabName.add("notification");
                settingTabUser.add(new Vector());
                settingTabSystem.add(settingNameSystem);
            }

            // --- the <strong>dateTime</strong> tab
            settingNameUser = initialiseSettings(securitySession,
                    new String[] {
                "i18nTimeZone","calendarDefaultView","calendarFirstWeekDay",

                "i18nDateInputDisplay", "i18nTimeInputDisplay",

                "i18nDateLong", "i18nDateLongDay", "i18nDateLongYear",
                "i18nDateShort","i18nDateShortYear", "i18nDateWeekDay",
                "i18nTimeLong", "i18nTimeShort",

                "i18nDateShortToday", "i18nDateShortYesterday",
                "i18nDateThisAfternoon", "i18nDateThisMorning",
                "i18nDateThisEvening", "i18nDateYesterdayMorning",
                "i18nDateYesterdayAfternoon", "i18nDateYesterdayEvening"},
                settingForm);
            if (!settingNameUser.isEmpty()) {
                tabName.add("dateTime");
                settingTabUser.add(settingNameUser);
                settingTabSystem.add(new Vector());
            }

            // when there's no tab with settings to show, go to page with
            // this info and a close button
            if (tabName.isEmpty()) {
                clear(mapping, errors, form, request, response, session);
                return "noSettings";
            }

            // set visible tab info to the form
            settingForm.setTabName(tabName);
            settingForm.setSettingTabUser(settingTabUser);
            settingForm.setSettingTabSystem(settingTabSystem);
        }
        return null;
    }

    /**
     * <p>helper method that sets the initial values of  setting to the
     * <code>settingForm</code>
     * @param name the names of the settings
     * @param form the <code>SettingForm</code> to fill
     * @param userName the current user
     * @param settings the <code>SettingsRemote</code>
     * @param rights the <code>SettingsRightsRemote</code>
     * @return List of settings from the input array <code>name</code>,
     * that are enabled or allowed to update
     */
    private List initialiseSettings(final SecuritySession securitySession,
            final String[] names,
            final SettingForm form)
            throws SystemException {
        Vector returnVector = new Vector();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];

            // first check if this setting is enabled right now
            if (settings.isSettingEnabled(securitySession, name)) {
                // now check if this user can change this setting on system
                // or user level
                if (settingsRights.canAmendSystemSettings(securitySession) ||
                        settingsRights.canAmendSetting(securitySession, name)) {
                    form.setSettingUser(name, settings.getSetting(securitySession,
                        name, securitySession.getUser()));
                    form.setSettingSystem(name, settings.getSetting(securitySession,
                        name, null));
                    form.setSettingType(name, new Integer(settings.getSettingType(securitySession, name)));
                    form.setSettingOverride(name, settingsRights.canAmendSetting(securitySession,
                        name));

                    returnVector.add(name);
                }
            }
        }
        return returnVector;
    }

    /**
     * <p>This method is called if the ok or apply buttons are pressed.</p>
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
     * @param ok <code>true</code> if the ok button was pressed, otherwise
     * <code>false</code> if the apply button was pressed.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {
        SettingForm settingForm = (SettingForm) form;

        // store the settings, also manage revert-to-system cases
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        saveSettings(securitySession, settingForm);

        request.setAttribute("refreshOpener", "1");

        return defaultForward;
    }


    /**
     * <p>the method handles saving all settings in
     * <code>settingForm</code> , removes the user
     * setting when the values of the system and user settings are the
     * same</p>
     * @param form the <code>SettingForm</code> with the request parameter
     * values
     * @param settings the <code>SettingsRemote</code>
     */
    private void saveSettings(final SecuritySession securitySession,
            final SettingForm form)
            throws SystemException {
        UserDO user = securitySession.getUser();

        boolean administrator = (form.getAdministrator()!=null);
        for (Iterator i = form.getSettingName().iterator(); i.hasNext();) {
            String name = (String) i.next();
            Object userValue = form.getSettingUser(name);
            Object systemValue = form.getSettingSystem(name);

            Object oldSystemValue = settings.getSetting(securitySession, name, null);

            // when the user can change system settings and override status
            // for settings
            if(administrator) {
                settings.amendSetting(securitySession, name, systemValue, null);

                // these two settings have to be treated in a special way,
                // as they have their simpliefied couples (without 'Display')
                // to allow less strict user input
                if (name.equals("i18nDateInputDisplay") ||
                        name.equals("i18nTimeInputDisplay")) {
                    settings.amendSetting(securitySession, name, userValue, user);
                }

                // override checkbox for this setting
                if (form.getSettingOverride(name)) {
                    settingsRights.addAmendRightForSetting(securitySession, name);
                } else {
                    settingsRights.removeAmendRightForSetting(securitySession, name);
                }
            }

            // when the user value copies the system one (old or new), remove it
            if (userValue.equals(systemValue) || userValue.equals(oldSystemValue)) {
                settings.revertSetting(securitySession, name, user);
                // these two settings have to be treated in a special way,
                // as they have their simpliefied couples (without 'Display')
                // to allow less strict user input, remove them also
                if (name.equals("i18nDateInputDisplay") ||
                        name.equals("i18nTimeInputDisplay")) {
                    String displaySettingName = name.substring(0,name.indexOf("Display"));
                    settings.revertSetting(securitySession, displaySettingName, user);
                }
                // set the user setting as a copy of the new system one to
                // the form, as this could be an apply only
                form.setSettingUser(name, systemValue);

                // otherwise save the new value of the user setting
            } else {
                settings.amendSetting(securitySession, name, userValue, user);
                // these two settings have to be treated in a special way,
                // as they have their simpliefied couples (without 'Display')
                // to allow less strict user input
                if (name.equals("i18nDateInputDisplay") ||
                        name.equals("i18nTimeInputDisplay")) {
                    String displaySettingName = name.substring(0,name.indexOf("Display"));
                    settings.amendSetting(securitySession, displaySettingName, userValue, user);
                }

            }
        }
    }

    /**
     * <p>The method takes a date / time input format and converts it to a
     * less strict one</p>
     * @param inputFormat the <code>String</code> with the date/time
     * format pattern
     * @return a less strict form of the provided format
     */
    private String simplifyInputFormat(final String inputFormat) {
        String returnFormat = inputFormat;
        String[] patterns = new String[] {"MM","dd","hh","KK","HH","kk","mm","ss"};

        for (Iterator i = Arrays.asList(patterns).iterator(); i.hasNext(); ) {
            String currentPattern = (String) i.next();

            // find the pattern
            int patternPosition = returnFormat.indexOf(currentPattern);

            if (patternPosition != -1) {
                int longPatternPosition = returnFormat.indexOf(currentPattern, patternPosition + 1);
                if (!(currentPattern.equals("MM") && longPatternPosition == (patternPosition + 1))) {
                    // replace the double pattern with a single char of it
                    returnFormat = returnFormat.substring(0, patternPosition) +
                        currentPattern.substring(1) +
                        returnFormat.substring(patternPosition + 2);

                }
            }
        }
        return returnFormat;
    }
}
