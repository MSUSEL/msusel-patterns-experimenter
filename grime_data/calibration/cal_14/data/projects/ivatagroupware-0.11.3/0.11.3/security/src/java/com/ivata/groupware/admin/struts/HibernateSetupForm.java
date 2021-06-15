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
package com.ivata.groupware.admin.struts;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;

/**
 * Contains all of the values you need to setup a basic <strong>ivata
 * groupware</strong> installation.
 *
 * @since ivata groupware 0.11 (2005-03-27)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.1 $
 */
public class HibernateSetupForm extends DialogForm {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(HibernateSetupForm.class);
    /**
     * Refer to {@link #isCreateDatabaseAutomatically}.
     */
    private boolean createDatabaseAutomatically;
    /**
     * Refer to {@link #getDatabaseDialect}.
     */
    private String databaseDialect;
    /**
     * Refer to {@link #getDatabaseDialects}.
     */
    private List databaseDialects;
    /**
     * Refer to {@link #getDatabaseDriver}.
     */
    private String databaseDriver;
    /**
     * Refer to {@link #getDatabaseDrivers}.
     */
    private List databaseDrivers;
    /**
     * Refer to {@link #getDatabasePassword}.
     */
    private String databasePassword;
    /**
     * Refer to {@link #getDatabaseTypes}.
     */
    private List databaseTypes;
    /**
     * Refer to {@link #getDatabaseURL}.
     */
    private String databaseURL;
    /**
     * Refer to {@link #getDatabaseURLs}.
     */
    private List databaseURLs;
    /**
     * Refer to {@link #getDatabaseUserName}.
     */
    private String databaseUserName;
    /**
     * Prepare the form.
     *
     * @see com.ivata.mask.web.struts.DialogForm#clear()
     */
    protected void clear() {
    }
    /**
     * The <strong>Hibernate</strong> dialect of the database we will use.
     *
     * @return Returns the database dialect.
     */
    public String getDatabaseDialect() {
        return databaseDialect;
    }
    /**
     * A list of all the string names of the <strong>Hibernate</strong>
     * dialects available.
     * @return Returns the database dialects.
     */
    public List getDatabaseDialects() {
        return databaseDialects;
    }
    /**
     * The name of the <strong>JDBC</strong> database driver class we will use.
     * @return Returns the database driver.
     */
    public String getDatabaseDriver() {
        return databaseDriver;
    }
    /**
     * A list of all available <strong>JDBC</strong> database driver classes.
     *
     * @return Returns the database drivers.
     */
    public List getDatabaseDrivers() {
        return databaseDrivers;
    }
    /**
     * Password used within the database system.
     * @return Returns the database password.
     */
    public String getDatabasePassword() {
        return databasePassword;
    }
    /**
     * This list should correspond to {@link #getDatabaseDialects} and is a
     * list of the database names for each dialect.
     * @return Returns the database types.
     */
    public List getDatabaseTypes() {
        return databaseTypes;
    }
    /**
     * The <strong>JDBC</strong> URL of the database we will use.
     *
     * @return Returns the database URL.
     */
    public String getDatabaseURL() {
        return databaseURL;
    }
    /**
     * This list should correspond to {@link #getDatabaseDialects} and is a
     * list of the default URLs for each dialect.
     *
     * @return Returns the default database URLs.
     */
    public List getDatabaseURLs() {
        return databaseURLs;
    }
    /**
     * The user name used to login to the database system.
     * @return Returns the database user name.
     */
    public String getDatabaseUserName() {
        return databaseUserName;
    }
    /**
     * Determines whether or not setup should create a simple
     * <strong>HSQLDB</strong> database automaticlaly.
     *
     * @return <code>true</code> if the database should be created
     * automatically.
     */
    public boolean isCreateDatabaseAutomatically() {
        return createDatabaseAutomatically;
    }
    /**
     * Refer to {@link }.
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     * @param mappingParam
     * @param requestParam
     */
    public void reset(ActionMapping mappingParam,
            HttpServletRequest requestParam) {
        SecuritySession securitySession = (SecuritySession)
            requestParam.getSession().getAttribute("securitySession");
        // the following are the settings we'll use to make an automatic
        // database (constructed using hsql)
        createDatabaseAutomatically = false;

        databaseDialect = "";
        databaseDialects = Arrays.asList(HibernateSetupConstants.DATABASE_DIALECTS);
        databaseDriver = "";
        databaseDrivers = Arrays.asList(HibernateSetupConstants.DATABASE_DRIVERS);
        databaseTypes = Arrays.asList(HibernateSetupConstants.DATABASE_TYPES);
        databaseURL = "";
        databaseURLs = Arrays.asList(HibernateSetupConstants.DATABASE_URLS);
    }
    /**
     * Refer to {@link #isCreateDatabaseAutomatically}.
     * @param createDatabaseAutomaticallyParam Refer to
     * {@link #isCreateDatabaseAutomatically}.
     */
    public void setCreateDatabaseAutomatically(
            boolean createDatabaseAutomaticallyParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting createDatabaseAutomatically. Before '"
                    + createDatabaseAutomatically + "', after '"
                    + createDatabaseAutomaticallyParam + "'");
        }
        createDatabaseAutomatically = createDatabaseAutomaticallyParam;
    }
    /**
     * Refer to {@link #getDatabaseDialect}.
     * @param databaseDialectParam Refer to {@link #getDatabaseDialect}.
     */
    public void setDatabaseDialect(String databaseDialectParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseDialect. Before '" + databaseDialect
                    + "', after '" + databaseDialectParam + "'");
        }
        databaseDialect = databaseDialectParam;
    }
    /**
     * Refer to {@link #getDatabaseDialects}.
     * @param databaseDialectsParam Refer to {@link #getDatabaseDialects}.
     */
    public void setDatabaseDialects(List databaseDialectsParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseDialects. Before '"
                    + databaseDialects + "', after '" + databaseDialectsParam
                    + "'");
        }
        databaseDialects = databaseDialectsParam;
    }
    /**
     * Refer to {@link #getDatabaseDriver}.
     * @param databaseDriverParam Refer to {@link #getDatabaseDriver}.
     */
    public void setDatabaseDriver(String databaseDriverParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseDriver. Before '" + databaseDriver
                    + "', after '" + databaseDriverParam + "'");
        }
        databaseDriver = databaseDriverParam;
    }
    /**
     * Refer to {@link #getDatabaseDrivers}.
     * @param databaseDriversParam Refer to {@link #getDatabaseDrivers}.
     */
    public void setDatabaseDrivers(List databaseDriversParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseDrivers. Before '" + databaseDrivers
                    + "', after '" + databaseDriversParam + "'");
        }
        databaseDrivers = databaseDriversParam;
    }
    /**
     * Refer to {@link #getDatabasePassword}.
     * @param databasePasswordParam Refer to {@link #getDatabasePassword}.
     */
    public void setDatabasePassword(String databasePasswordParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databasePassword. Before '"
                    + databasePassword + "', after '" + databasePasswordParam
                    + "'");
        }
        databasePassword = databasePasswordParam;
    }
    /**
     * Refer to {@link #getDatabaseTypes}.
     * @param databaseTypesParam Refer to {@link #getDatabaseTypes}.
     */
    public void setDatabaseTypes(List databaseTypesParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseTypes. Before '" + databaseTypes
                    + "', after '" + databaseTypesParam + "'");
        }
        databaseTypes = databaseTypesParam;
    }
    /**
     * Refer to {@link #getDatabaseURL}.
     * @param databaseURLParam Refer to {@link #getDatabaseURL}.
     */
    public void setDatabaseURL(String databaseURLParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseURL. Before '" + databaseURL
                    + "', after '" + databaseURLParam + "'");
        }
        databaseURL = databaseURLParam;
    }
    /**
     * Refer to {@link #getDatabaseURLs}.
     * @param databaseURLsParam Refer to {@link #getDatabaseURLs}.
     */
    public void setDatabaseURLs(List databaseURLsParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseURLs. Before '" + databaseURLs
                    + "', after '" + databaseURLsParam + "'");
        }
        databaseURLs = databaseURLsParam;
    }
    /**
     * Refer to {@link #getDatabaseUserName}.
     * @param databaseUserNameParam Refer to {@link #getDatabaseUserName}.
     */
    public void setDatabaseUserName(String databaseUserNameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting databaseUserName. Before '"
                    + databaseUserName + "', after '" + databaseUserNameParam
                    + "'");
        }
        databaseUserName = databaseUserNameParam;
    }
    /**
     * Check all of the input is valid.
     * @param requestParam
     * Refer to {@link com.ivata.mask.web.struts.DialogForm#validate}.
     * @param sessionParam
     * Refer to {@link com.ivata.mask.web.struts.DialogForm#validate}.
     * @return
     * Refer to {@link com.ivata.mask.web.struts.DialogForm#validate}.
     */
    public ValidationErrors validate(final HttpServletRequest requestParam,
            final HttpSession sessionParam) {
        ValidationErrors errors = super.validate(requestParam, sessionParam);
        // if we should create the database automatically, default the values
        // (as the disabled fields will not be submitted)
        if (createDatabaseAutomatically) {
            databaseDriver = HibernateSetupConstants.AUTOMATIC_DATABASE_DRIVER;
            databaseDialect = HibernateSetupConstants.AUTOMATIC_DATABASE_DIALECT;
            databasePassword = HibernateSetupConstants.AUTOMATIC_DATABASE_PASSWORD;
            databaseUserName = HibernateSetupConstants.AUTOMATIC_DATABASE_USER_NAME;
            // check the URL starts with the hypersonic file URL
            if (!databaseURL
                    .startsWith(HibernateSetupConstants.AUTOMATIC_DATABASE_URL_START)) {
                errors.add(
                        new ValidationError("errors.setup.automaticDatabaseURL",
                                Arrays.asList(new String[] {
                                        HibernateSetupConstants.AUTOMATIC_DATABASE_URL_START,
                                        databaseURL})));
            }
        } else {
            // if you don't specify any of the database fields, default back
            // to the memory database
            if (StringHandling.isNullOrEmpty(databaseDriver)
                        && StringHandling.isNullOrEmpty(databasePassword)
                        && StringHandling.isNullOrEmpty(databaseUserName)
                        && StringHandling.isNullOrEmpty(databaseURL)) {
                databaseDriver = HibernateSetupConstants.AUTOMATIC_DATABASE_DRIVER;
                databaseDialect = HibernateSetupConstants.AUTOMATIC_DATABASE_DIALECT;
                databasePassword = HibernateSetupConstants.AUTOMATIC_DATABASE_PASSWORD;
                databaseUserName = HibernateSetupConstants.AUTOMATIC_DATABASE_USER_NAME;
                databaseURL = HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL;

            // otherwise, if you specify any of the DB fields, you need at least
            // dialect driver and URL.
            } else if (StringHandling.isNullOrEmpty(databaseDialect)
                            || StringHandling.isNullOrEmpty(databaseDriver)
                            || StringHandling.isNullOrEmpty(databaseURL)) {
                errors.add(
                        new ValidationError("errors.setup.database",
                                Arrays.asList(new String[] {})));
            }
        }

        return errors;
    }
}
