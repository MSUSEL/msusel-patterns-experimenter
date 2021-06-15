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
package com.ivata.groupware.admin.setting;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.business.BusinessLogic;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.mask.persistence.FinderException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.validation.ValidationException;


/**
 * <p>This class controls/sets the global preferences.</p>
 *
 * @since 2001-09-06
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class SettingsImpl extends BusinessLogic implements Settings {
    /**
     * Persistence manger used to store/retrieve data objects, or retrieve a
     * new persistence session.
     */
    private QueryPersistenceManager persistenceManager;

    /**
     * Construct a new address book.
     *
     * @param persistenceManager used to store objects in db.
     */
    public SettingsImpl(final QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    /**
     * <p>
     * Change the value of an existing setting.
     * </p>
     *
     * @param name the name of the setting to set
     * @param value new value to be set.
     * @param user if not <code>null</code>, then the setting for this user
     * is set, otherwise the general setting is changed.
     */
    public void amendSetting(final SecuritySession securitySession,
            final String name,
            final Object value,
            final UserDO user)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            // type must be the same as the existing one
            SettingDO generalSetting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                "adminSettingByName", new Object[] {name});

            if ((generalSetting.getType() == SettingConstants.DATA_TYPE_INTEGER)
                    && !(value instanceof Integer)) {
                throw new ValidationException(
                        new ValidationError(
                                "errors.setting.type.integer",
                                Arrays.asList(new Object[] {
                                        value.getClass().getName(),
                                        value
                                        })));
            } else if ((generalSetting.getType() == SettingConstants.DATA_TYPE_BOOLEAN)
                    && !(value instanceof Boolean)) {
                new ValidationError(
                        "errors.setting.type.boolean",
                        Arrays.asList(new Object[] {
                                value.getClass().getName(),
                                value
                                }));
            } else if (!(value instanceof String)) {
                String className;
                if (value == null) {
                    className = "[None]";
                } else {
                    className = value.getClass().getName();
                }
                new ValidationError(
                        "errors.setting.type.string",
                        Arrays.asList(new Object[] {
                                className,
                                value
                                }));
            }

            // if there is no user, change the general setting, otherwise find
            // the user setting and change that
            if (user == null) {
                generalSetting.setValue(value.toString());
                persistenceManager.amend(persistenceSession, generalSetting);
            } else {
                try {
                    SettingDO setting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                        "adminSettingByNameUserName", new Object[] {name, user.getName()});
                    setting.setValue(StringHandling.getNotNull(value, null));
                } catch (FinderException e) {
                    // create a new setting
                    SettingDO setting = new SettingDO();
                    setting.setDescription(generalSetting.getDescription());
                    setting.setEnabled(generalSetting.isEnabled());
                    setting.setName(name);
                    setting.setType(generalSetting.getType());
                    setting.setUser(user);
                    setting.setValue(value.toString());
                    persistenceManager.add(persistenceSession, setting);
                }

            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * Find a setting with a given name for the user.
     *
     * @param name the name of the setting to find
     * @param user the user who owns this setting, or <code>null</code> if the
     *    setting is shared by all users.
     * @param value object of class String, Integer or Boolean
     */
    private SettingDO findSetting(final SecuritySession securitySession,
            final String name,
            final UserDO user)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {

            SettingDO setting = null;

            // first try to find the user setting - if a user was specified
            if ((user != null)
                    && (user.getName() != null)) {
                try {
                    setting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                        "adminSettingByNameUserName", new Object[] {name, user.getName()});
                } catch (FinderException e) {
                    // ok - we will try to get the general setting
                }
            }

            // if the setting is still null, try to find the general setting
            if (setting == null) {
                setting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                    "adminSettingByName", new Object[] {name});
            }
            return setting;
        } catch (Exception e) {
            persistenceSession.cancel();
            if (e instanceof SystemException) {
                throw (SystemException) e;
            }
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Get a setting of class <code>Boolean</code>.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Boolean
     * @return a setting of class <code>Boolean</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @throws SettingNullException if this setting does not exist.
     */
    public final Boolean getBooleanSetting(final SecuritySession session,
            final String name,
            final UserDO user) throws SystemException {
        return (Boolean) getSettingOfType(session, name, user, Boolean.class);
    }
    /**
     * <p>Get a setting of class <code>Integer</code>.</p>
     *
     * @exception SettingsDataTypeException if the setting has any class other
     *     than Integer.
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for.
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting of class <code>Integer</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    public final Integer getIntegerSetting(final SecuritySession session,
            final String name,
            final UserDO user) throws SystemException {
        return (Integer) getSettingOfType(session, name, user, Integer.class);
    }

    /**
     * <p>Get a setting for a given user. The class of the returned object will
     * depend on the <code>type</code> field of the EJB with this name and can
     * be one of:</br/>
     * <ul>
     * <li><code>Integer</code></li>
     * <li><code>String</code></li>
     * <li><code>Boolean</code></li></p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @return a setting with the setting name provided. The type of the
     *     returned object depends on the <code>type</code> field of the
     *     setting.
     * @see #getIntegerSetting
     * @see #getStringSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    public final Object getSetting(final SecuritySession securitySession,
            final String name,
            final UserDO userDO) throws SystemException {
        SettingDO setting = findSetting(securitySession, name, userDO);
        Object returnObject;
        // set the type on the basis of the class of the parameter
        switch (setting.getType()) {
        case SettingConstants.DATA_TYPE_INTEGER:
            returnObject = new Integer(setting.getValue());
            break;

        case SettingConstants.DATA_TYPE_BOOLEAN:
            returnObject = new Boolean(setting.getValue());
            break;

            // make the default a string
        default:
            returnObject = setting.getValue();
            break;
        }
        return returnObject;
    }

    /**
     * Private helper. Get a setting and check it has the type specified.
     *
     * @throws SettingsDataTypeException if the type of the setting is not what
     * we expected.
     * @throws SystemException see {@link #getSetting getSetting}.
     */
    private Object getSettingOfType(final SecuritySession session,
            final String name,
            final UserDO user,
            final Class settingType)
            throws SettingsDataTypeException,
            SystemException {
        Object oTest = getSetting(session, name, user);
        if ((oTest != null) &&
                (!oTest.getClass().getName().equals(settingType.getName()))) {
                throw new  SettingsDataTypeException("Setting '"
                        + name
                        + "' has class '"
                        + oTest.getClass().getName()
                        + "', expected '"
                        + settingType.getName()
                        + "'.");
        }
        return oTest;
    }

    /**
     * <p>Get the type of a setting</p>
     * @param name the name of the setting
     * @return one of the static fields of <code>SettingConstants</code>
     * @throws SettingNullException if this setting does not exist.
     */
    public final int getSettingType(final SecuritySession securitySession,
            final String name) throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            return ((SettingDO) persistenceManager.findInstance(persistenceSession, "adminSettingName",
                new Object[] {name})).getType();
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Get a setting of class String.</p>
     *
     * @return the setting if set, or null if not
     * @param name the name of the setting to return the value for
     * @param userName the user to search for. If null is specified, the
     *     default setting is searched for and returned if found.
     * @exception SettingsDataTypeException if the setting has any class other
     *     than String
     * @return a setting of class <code>String</code> for the setting
     *      name provided.
     * @see #getSetting
     * @see #getIntegerSetting
     * @see #getBooleanSetting
     * @throws SettingNullException if this setting does not exist.
     */
    public final String getStringSetting(final SecuritySession session,
            final String name,
            final UserDO user) throws SystemException {
        return (String) getSettingOfType(session, name, user, String.class);
    }
    /**
     * <p>
     * Find out whether or not a setting is enabled.
     * </p>
     *
     * @param securitySession valid security session.
     * @param name name of the setting to check.
     * @return <code>true</code> if the setting exists and is enabled.
     * @throws SystemException if it don't work out in some way :-)
     */
    public boolean isSettingEnabled(final SecuritySession securitySession,
            final String
            name)
            throws SystemException {
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            SettingDO setting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                    "adminSettingByName", new Object[] {name});
            return setting.isEnabled();
        } catch (FinderException e) {
            return false;
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>
     * Revert a user setting back to the general value.
     * </p>
     *
     * @param name the name of the setting to revert
     * @param user the setting for this user is reverted
     */
    public void revertSetting(final SecuritySession securitySession,
            final String name,
            final UserDO user)
            throws SystemException {
        assert(user != null);

        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            SettingDO setting = (SettingDO) persistenceManager.findInstance(persistenceSession,
                "adminSettingByNameUserName", new Object[] {name, user.getName()});
            persistenceManager.remove(persistenceSession, setting);
        } catch (FinderException e) {
            // that's ok - the default value will already be used
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>Confirm all of the settings passed are correct.</p>
     *
     * @param settings a <code>Map</code> with setting names as keys and setting
     *  values as values
     * @param the <code>Locale</code> to get localised error messages
     * @param settingType one of the constants in <code>SettingConstants</code>:
     *  <code>SETTING_USER</code> or  <code>SETTING_SYSTEM</code>
     * @return a collection of validation errors if any of the settings contains
     *  invalid value.
     *
     * @ejb.interface-method
     *      view-type="both"
     */
    public ValidationErrors validate(final SecuritySession session,
            final Map settings,
            final Locale locale,
            final int settingType) throws SystemException {
        ResourceBundle resources = ResourceBundle.getBundle("com.ivata.groupware.admin.ApplicationResources", locale);
        ValidationErrors errors = new ValidationErrors();

        String settingAreaString = "";
        if (settingType == SettingConstants.SETTING_SYSTEM) {
            settingAreaString = resources.getString("setting.system");
        } else if (settingType == SettingConstants.SETTING_USER) {
            settingAreaString = resources.getString("setting.user");
        }

        // go through all the passed settings and check the ones that need to be
        // validated
        String currentName = "";
        Integer valueInteger;
        String valueString;
        Boolean valueBoolean;

        for (Iterator i = settings.keySet().iterator(); i.hasNext(); ) {
            currentName = (String) i.next();

            // required email string settings
            if (currentName.equals("emailHost") ||
                currentName.equals("emailHostSmtp") ||
                currentName.equals("emailFolderInbox") ||
                currentName.equals("emailFolderSent") ||
                currentName.equals("emailFolderTrash") ||
                currentName.equals("emailFolderDrafts")) {

                if (StringHandling.isNullOrEmpty((String) settings.get(currentName))) {
                    errors.add(new ValidationError(
                            "errors.setting.required",
                            Arrays.asList(new String[] {settingAreaString,
                                    resources.getString("setting.field."
                                            + currentName)})));
                }

            // email port
            } else if (currentName.equals("emailPort")) {
                valueInteger = (Integer) settings.get(currentName);
                if (valueInteger.intValue() < 1 || valueInteger.intValue() > 65535) {
                    errors.add(new ValidationError(
                            "errors.setting.range",
                            Arrays.asList(new String[] {settingAreaString,
                                    resources.getString("setting.field."
                                            + currentName),
                                            "1",
                                            "65535"})));

                }

            // library integer settings
            } else if (currentName.equals("libraryHome") ||
                currentName.equals("libraryRecent") ||
                currentName.equals("libraryCommentSpacer")) {

                valueInteger = (Integer) settings.get(currentName);
                if (valueInteger.intValue() < 1) {
                    errors.add(new ValidationError(
                            "errors.setting.greaterThan",
                            Arrays.asList(new String[] {settingAreaString,
                                resources.getString("setting.field."
                                        + currentName),
                                        "0"})));

                }


            // date formats
            } else if (currentName.equals("i18nDateInputDisplay") ||
                        currentName.equals("i18nDateLong") ||
                        currentName.equals("i18nDateLongDay") ||
                        currentName.equals("i18nDateLongYear") ||
                        currentName.equals("i18nDateShort") ||
                        currentName.equals("i18nDateShortYear") ||
                        currentName.equals("i18nDateWeekDay") ||
                        currentName.equals("i18nDateShortToday") ||
                        currentName.equals("i18nDateShortYesterday") ||
                        currentName.equals("i18nDateThisAfternoon") ||
                        currentName.equals("i18nDateThisMorning") ||
                        currentName.equals("i18nDateThisEvening") ||
                        currentName.equals("i18nDateYesterdayMorning") ||
                        currentName.equals("i18nDateYesterdayAfternoon") ||
                        currentName.equals("i18nDateYesterdayEvening")) {

                // the validation of date formats
                try {
                    SimpleDateFormat testDateFormat = new SimpleDateFormat((String) settings.get(currentName));
                } catch (IllegalArgumentException e) {
                    errors.add(new ValidationError(
                            "errors.setting.date",
                            Arrays.asList(new String[] {settingAreaString,
                                    resources.getString("setting.field."
                                            + currentName)})));
                }

            //time formats
            }  else if (currentName.equals("i18nTimeInputDisplay") ||
                        currentName.equals("i18nTimeLong") ||
                        currentName.equals("i18nTimeShort")) {

                // the validation of time formats
                try {
                    SimpleDateFormat testDateFormat = new SimpleDateFormat((String) settings.get(currentName));
                } catch (IllegalArgumentException e) {
                    errors.add(new ValidationError(
                            "errors.setting.time",
                            Arrays.asList(new String[] {settingAreaString,
                                    resources.getString("setting.field."
                                            + currentName)})));
                }


            }

        }
        return errors;
    }
}
