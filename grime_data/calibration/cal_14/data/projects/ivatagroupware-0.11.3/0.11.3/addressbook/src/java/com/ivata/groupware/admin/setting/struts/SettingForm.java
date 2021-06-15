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
package com.ivata.groupware.admin.setting.struts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.SettingConstants;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.mask.Mask;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>The form used to input/display data in
 * preferences (setting/index.jsp) </p>
 *
 * @since 2003-01-29
 * @author Peter Illes
 * @version $Revision: 1.3 $
 */
public class SettingForm extends DialogForm {
    /**
     * <p>when not null or non-empty string, the current user can change
     * system settings</p>
     */
    private String administrator;
    /**
     * <p>holds the values of the visible fields in tabs of
     * <code>/setting/index.jsp</code>, but it's only used because <strong>HTML
     * tags</strong> have the <code>property<code> property mandatory</p>
     */
    private Map setting;
    /**
     * <p>The area where the settings change takes place: "user" or
     * "system"</p>
     */
    private String settingArea;
    /**
     * <p>this map holds the override information for  for all settings
     * stored  in <code>settingUser</code> and <code>settingSystem</code>
     * as
     * boolean, it's only used when a user with rights to change system
     * settings is the current user. An element with the key setting name
     * has value <code>true</code> if users can override it....</p>
     */
    private Map settingOverride;
    /**
     * <p>
     * ivata settings implementation - used in validation.
     * </p>
     */
    private Settings settings;
    /**
     * <p>holds the values of the system settings</p>
     */
    private Map settingSystem;

    /**
     * <p>stores the active tab</p>
     */
    private Integer settingTab_activeTab;
    /**
     * <p>stores the names of system settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     */
    private List settingTabSystem;
    /**
     * <p>stores the names of user settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     */
    private List settingTabUser;
    /**
     * <p>this map holds the types for all settings stored  in
     * <code>settingUser</code> and  <code>settingSystem</code></p>
     */
    private Map settingType;
    /**
     * <p>holds the values of the user settings</p>
     */
    private Map settingUser;
    /**
     * <p>holds the list of tab names, the list of tabs shown varies
     * because of user rights</p>
     */
    private List tabName;
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;
    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;

    /**
     * <p>
     * Construct a valid setting form.
     * </p>
     *
     * @param maskParam Refer to {@link DialogForm#DialogForm}.
     * @param baseClassParam Refer to {@link DialogForm#DialogForm}.
     * @param settings ivata settings implementation - used in validation.
     */
    public SettingForm(final Mask maskParam,
            final Class baseClassParam,
            final Settings settings) {
        this.settings = settings;
        clear();
    }

    /**
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        administrator = null;
        setting = new HashMap();
        settingArea = "user";
        settingOverride = new HashMap();
        settingSystem = new HashMap();
        settingTab_activeTab = null;
        settingTabSystem = null;
        settingTabUser = null;
        settingType = new HashMap();
        settingUser = new HashMap();
        tabName = null;
    }

    /**
     * <p>gets the administrator flag, that is when not null or non-empty
     * string, the current user can change system settings</p>
     * @return  when not null or non-empty string, the current user can
     * change system settings
     */
    public final String getAdministrator() {
        return administrator;
    }

    /**
     * <p>always returns empty string</p>
     * @param key the name of the setting
     * @return empty string
     */
    public final String getSetting(final String key) {
        return "";
    }

    /**
     * <p>Gets the area where the settings change takes place: "user" or
     * "system"</p>
     * @return "system" or "user"
     */
    public final String getSettingArea() {
        return settingArea;
    }

    /**
     * <p>returns the names of all  settings from the setting form as a
     * <code>Set</code>. The set may vary because of user rights and the
     * fact that some settings can be disabled (locked).</p>
     * @return names of settings
     */
    public final Set getSettingName() {
        return settingType.keySet();
    }

    /**
     * <p>returns the override status for a setting, only used when the
     * current user has rights to change system settings</p>
     * @param key the name of the setting
     * @return <code>true</code> when this setting can be overriden by
     * users,   <code>false</code> otherwise
     */
    public final boolean getSettingOverride(final String key) {
        return ((Boolean) settingOverride.get(key)).booleanValue();
    }

    /**
     * <p>returns a  value of the system settings</p>
     * @param key the name of the setting
     * @return the value of the setting
     */
    public final Object getSettingSystem(final String key) {
        return settingSystem.get(key);
    }

    /**
     * <p>getter for  the active tab field</p>
     * @return the id of the active tab as <code>Integer</code>
     */
    public final Integer getSettingTab_activeTab() {
        return settingTab_activeTab;
    }

    /**
     * <p>stores the names of system settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     *
     * @return the current value of settingTabSystem.
     */
    public List getSettingTabSystem() {
        return settingTabSystem;
    }

    /**
     * <p>returns the system setting names for the current  tab as comma
     * separated string with quotes (used for javascript array
     * definition)</p>
     * @return comma separated string with quotes (used for javascript
     * array definition)
     */
    public final String getSettingTabSystemFields() {
        String returnString = "";

        for (Iterator i =
                ((List) settingTabSystem.get(settingTab_activeTab.intValue())).iterator();
            i.hasNext();) {
            returnString += ("'" + (String) i.next() + "'");
            if (i.hasNext()) {
                returnString += ",";
            }
        }
        return returnString;
    }

    /**
     * <p>stores the names of user settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     *
     * @return the current value of settingTabUser.
     */
    public List getSettingTabUser() {
        return settingTabUser;
    }

    /**
     * <p>returns the user setting names for the current  tab as comma
     * separated string with quotes (used for javascript array
     * definition)</p>
     * @return comma separated string with quotes (used for javascript
     * array definition)
     */
    public final String getSettingTabUserFields() {
        String returnString = "";

        for (Iterator i =
                ((List) settingTabUser.get(settingTab_activeTab.intValue())).iterator();
            i.hasNext();) {
            returnString += ("'" + (String) i.next() + "'");
            if (i.hasNext()) {
                returnString += ",";
            }
        }
        return returnString;
    }

    /**
     * <p>returns the type for one of  the settings</p>
     * @param key the name of the setting
     * @return the type of the setting as <code>SettingConstants</code>
     */
    public final Integer getSettingType(final String key) {
        return (Integer) settingType.get(key);
    }

    /**
     * <p>returns a  value of the user settings</p>
     * @param key the name of the setting
     * @return the value of the setting
     */
    public final Object getSettingUser(final String key) {
        return settingUser.get(key);
    }

    /**
     * <p>returns the names of the displayed tabs</p>
     * @return  the names of displayed tabs as <code>List</code> of
     * <code>String</code>s
     */
    public List getTabName() {
        return tabName;
    }

    /**
     * <p>Reset appopriate bean properties to their default state.  This
     * method
     * is called before the properties are repopulated by the controller
     * servlet.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     *
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {

        if (settingTab_activeTab!=null) {

            // reset the override checkboxes from the last tab to <code>false</code>
            // because they're specific, only the 'succesful' ones are set
            int activeTab = settingTab_activeTab.intValue();
            for (Iterator i = ((List) getSettingTabSystem().get(activeTab)).iterator();
                    i.hasNext();) {
                String currentKey = (String) i.next();
                settingOverride.put(currentKey, new Boolean(false));
            }
            for (Iterator i = ((List) getSettingTabUser().get(activeTab)).iterator();
                    i.hasNext();) {
                String currentKey = (String) i.next();
                settingOverride.put(currentKey, new Boolean(false));
            }
            // reset the checkboxes on email tab
            if (((String) tabName.get(activeTab)).equals("email")) {
                if (settingType.containsKey("emailSignatureForward")) {
                   settingUser.put("emailSignatureForward","false");
                   settingSystem.put("emailSignatureForward","false");
                }
                if (settingType.containsKey("emailSignatureReply")) {
                   settingUser.put("emailSignatureReply","false");
                   settingSystem.put("emailSignatureReply","false");
                }
            }
        }

        // buttons
        setApply(null);
        setOk(null);

    }

    /**
     * <p>sets the administrator flag, that is when not null or non-empty
     * string, the current user can change system settings</p>
     * @param administrator  when not null or non-empty string, the
     * current user can change system settings
     */
    public final void setAdministrator(final String administrator) {
        this.administrator = administrator;
    }

    /**
     * <p>sets the last value from the visible field of a setting, but
     * this value is not used, {@see getSetting(String) </p>
     * @param key the name of the setting
     * @param the value of the visible field of a setting
     */
    public final void setSetting(final String key,
            final String value) {
    }

    /**
     * <p>Sets the area where the settings change takes place: "user" or
     * "system"</p>
     * @param settingArea
     */
    public final void setSettingArea(final String settingArea) {
        this.settingArea = settingArea;
    }

    /**
     * <p>sets the override status for a setting, only used when the
     * current user has rights to change system settings</p>
     * @param key the name of the setting
     * @param overridable  <code>true</code> when this setting can be
     * overriden by users,   <code>false</code> otherwise
     */
    public final void setSettingOverride(final String key,
            final boolean overridable) {
        settingOverride.put(key, new Boolean(overridable));
    }

    /**
     * <p>sets a  value of the system settings</p>
     * @param key the name of the setting
     * @param value the value of the setting
     */
    public final void setSettingSystem(final String key,
            final Object value) {
        if ((key.equals("emailSignatureReply") || key.equals("emailSignatureForward"))
                && !(value instanceof Boolean)
                && StringHandling.isNullOrEmpty((String) value)) {
            return;
        } else {
            settingSystem.put(key, value);
        }
    }

    /**
     * <p>setter for  the active tab field</p>
     * @param settingTab_activeTab  the id of the active tab as
     * <code>Integer</code>
     */
    public final void setSettingTab_activeTab(final Integer settingTab_activeTab) {
        this.settingTab_activeTab = settingTab_activeTab;
    }

    /**
     * <p>stores the names of system settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     *
     * @param settingTabSystem the new value of settingTabSystem.
     */
    public final void setSettingTabSystem(final List settingTabSystem) {
        this.settingTabSystem = settingTabSystem;
    }

    /**
     * <p>stores the names of user settings for individual tabs as
     * <code>List</code>s of <code>String</code>s.</p>
     *
     * @param settingTabUser the new value of settingTabUser.
     */
    public final void setSettingTabUser(final List settingTabUser) {
        this.settingTabUser = settingTabUser;
    }

    /**
     * <p>sets the type of a settings</p>
     * @param key the name of the setting
     * @param type the type of the setting
     */
    public final void setSettingType(final String key,
            final Integer type) {
        settingType.put(key, type);
    }

    /**
     * <p>sets a  value of the user settings</p>
     * @param key the name of the setting
     * @param value the value of the setting
     */
    public final void setSettingUser(final String key,
            final Object value) {
        if ((key.equals("emailSignatureReply") || key.equals("emailSignatureForward"))
                && !(value instanceof Boolean)
                && StringHandling.isNullOrEmpty((String) value)) {
            return;
        } else {
            settingUser.put(key, value);
        }
    }

    /**
     * <p>sets the names of the tabs to display</p>
     * @param tabName <code> List of tab names
     */
    public final void setTabName(final List tabName) {
        this.tabName = tabName;
    }

    /**
     * Validate the settings for correct input,
     * and return an <code>ActionMessages</code> object that encapsulates
     * any
     * validation errors that have been found.  If no errors are found,
     * return <code>null</code>
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     *
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
        ValidationErrors validationErrors = new ValidationErrors();
        SecuritySession securitySession = (SecuritySession)
            request.getSession().getAttribute("securitySession");


        // only do the validation when ok or apply was pressed
        if (!(getOk() == null && getApply() == null)) {
            // we need these for server-side validation
            // these maps will contain only the settings that were ok here
            Map settingUserValidation = new HashMap(this.settingUser);
            Map settingSystemValidation = new HashMap(this.settingSystem);

            // setting area identification (user or system) for error messages
            ResourceBundle adminBundle =
                ResourceBundle.getBundle("com.ivata.groupware.admin.ApplicationResources",
                    (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));

            String settingSystemArea = adminBundle.getString("setting.system");
            String settingUserArea = adminBundle.getString("setting.user");

            // first go through both the user and the system map and check the types
            for (Iterator i = settingType.keySet().iterator(); i.hasNext();) {
                String currentKey = (String) i.next();
                String currentFieldName = adminBundle.getString("setting.field." + currentKey);
                Integer currentType = (Integer) settingType.get(currentKey);
                // the values are strings when they were set from request or
                // the correct types when the user haven't submitted the appropriate
                // tab yet
                String currentUserValue = settingUser.get(currentKey).toString();
                String currentSystemValue = settingSystem.get(currentKey).toString();

                switch (currentType.intValue()) {
                    // check and conversion to Boolean
                    case SettingConstants.DATA_TYPE_BOOLEAN:
                        if (!(currentUserValue.equalsIgnoreCase("true") ||
                                currentUserValue.equalsIgnoreCase("false"))) {
                            validationErrors.add(
                                    new ValidationError(
                                            "errors.setting.boolean",
                                    Arrays.asList(new String[]{
                                            settingUserArea, currentFieldName
                                            })));
                            settingUserValidation.remove(currentKey);
                        } else {
                            settingUser.put(currentKey,
                                new Boolean(currentUserValue));
                            settingUserValidation.put(currentKey,
                                new Boolean(currentUserValue));
                        }
                        if (!(currentSystemValue.equalsIgnoreCase("true") ||
                                currentSystemValue.equalsIgnoreCase("false"))) {
                            validationErrors.add(
                                    new ValidationError(
                                            "errors.setting.boolean",
                                    Arrays.asList(new String[]{
                                            settingSystemArea, currentFieldName
                                            })));
                            settingSystemValidation.remove(currentKey);
                        } else {
                            settingSystem.put(currentKey,
                                new Boolean(currentSystemValue));
                            settingSystemValidation.put(currentKey,
                                new Boolean(currentSystemValue));
                        }
                        break;

                    //check and conversion to Integer
                    case SettingConstants.DATA_TYPE_INTEGER:
                        // check whether the string holds number representation
                        try {
                            Integer.parseInt(currentUserValue);
                            // when we're here it's ok
                            settingUser.put(currentKey, new Integer(currentUserValue));
                            settingUserValidation.put(currentKey,
                                new Integer(currentUserValue));
                        } catch (NumberFormatException e) {
                            // we catched this so the string didn't contain a number
                            validationErrors.add(
                                    new ValidationError(
                                            "errors.setting.integer",
                                    Arrays.asList(new String[]{
                                            settingUserArea, currentFieldName
                                            })));
                            settingUserValidation.remove(currentKey);
                        }
                        try {
                            Integer.parseInt(currentSystemValue);
                            // when we're here it's ok
                            settingSystem.put(currentKey, new Integer(currentSystemValue));
                            settingSystemValidation.put(currentKey,
                                new Integer(currentSystemValue));
                        } catch (NumberFormatException e) {
                            // we catched this so the string didn't contain a number
                            validationErrors.add(
                                    new ValidationError(
                                            "errors.setting.integer",
                                    Arrays.asList(new String[]{
                                            settingSystemArea, currentFieldName
                                            })));
                            settingSystemValidation.remove(currentKey);
                        }
                        break;

                        // the only other type is string so far, no special validation here
                    case SettingConstants.DATA_TYPE_STRING:

                        break;
                }
            }
            // we checked the types here, the real validation takes place on the
            // server, worth to do so when there are some settings with correct
            // types left
            if (!(settingUserValidation.isEmpty() && settingSystemValidation.isEmpty())) {
                Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
                ValidationErrors userErrors;
                try {
                    userErrors = settings.validate(securitySession,
                        settingUserValidation, locale, SettingConstants.SETTING_USER);
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                }
                validationErrors.addAll(userErrors);
                ValidationErrors systemErrors;
                try {
                    systemErrors = settings.validate(securitySession,
                        settingSystemValidation, locale, SettingConstants.SETTING_SYSTEM);
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                }
                validationErrors.addAll(systemErrors);
            }
        }
        return validationErrors;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }
}
