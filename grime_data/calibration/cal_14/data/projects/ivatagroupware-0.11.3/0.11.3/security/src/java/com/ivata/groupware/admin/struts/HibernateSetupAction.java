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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.util.DTDEntityResolver;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.struts.LoginGuestAction;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants;
import com.ivata.groupware.struts.SetupAction;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.util.ThrowableHandling;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * Setup the application to use a database.
 *
 * @since ivata groupware 0.11 (2005-03-25)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 */
public class HibernateSetupAction extends LoginGuestAction
        implements SetupAction {
    /**
     * Logger for this class.
     */
    private static final Logger logger =
        Logger.getLogger(HibernateSetupAction.class);
    /**
     * Helper to get the appropriate XPath to locate a Hibernate property
     * element in the config.
     *
     * @param propertyName name of the hibernate property.
     * @return XPath expression for this property.
     */
    private static String getHibernateXPath(final String propertyName) {
        return "/hibernate-configuration/session-factory/property[@name='"
            + propertyName
            + "']";
    }
    /**
     * Stores the actions in the request processor, so they can be reset.
     */
    private Map actions;
    /**
     * Constructor.
     *
     * @param securityParam System security object.
     * @param settingsParam System settings object.
     * @param maskFactoryParam Creates input and list masks.
     * @param authenticatorParam Used to check user is auhtorized to see this
     * page.
     */
    public HibernateSetupAction(final Security securityParam,
            final Settings settingsParam,
            final MaskFactory maskFactoryParam,
            final MaskAuthenticator authenticatorParam) {
        super(securityParam, settingsParam, maskFactoryParam,
                authenticatorParam);
    }
    /**
     * Check the database connection specified in the form, and log any errors.
     * @param setupForm the form from the current request, containing the
     * database settings to check.
     * @param errorMessages collection to append error messages to.
     */
    private void checkConnection(final HibernateSetupForm setupForm,
            final ActionErrors errorMessages) {
        String databaseDriver = setupForm.getDatabaseDriver();
        String databasePassword = setupForm.getDatabasePassword();
        String databaseURL = setupForm.getDatabaseURL();
        String databaseUserName = setupForm.getDatabaseUserName();
        // first check we can connect to the chosen db
        // does the database driver exist?
        try {
            Class.forName(databaseDriver);
            try {
                Connection connection = DriverManager.getConnection(
                        databaseURL,
                        databaseUserName,
                        databasePassword);
                Statement statement = connection.createStatement();
                // NOTE: this is about the only SQL in the whole app.
                ResultSet allSettings = statement.executeQuery(
                        "select name, value, type from setting where "
                        + "person_user is null");
                if (!allSettings.next()) {
                    errorMessages.add(null, new ActionMessage(
                            "errors.setup.databaseSettings",
                            databaseURL,
                            databaseUserName));
                }
            } catch (Exception e) {
                // if we get in here it means we have a driver but could
                // not connect
                logger.error(e.getClass().getName()
                        + ": connecting to URL '"
                        + setupForm.getDatabaseURL()
                        + "'",
                        e);
                Throwable cause = ThrowableHandling.getCause(e);
                errorMessages.add(null, new ActionMessage(
                        "errors.setup.databaseConnection",
                        databaseURL,
                        databaseUserName,
                        cause.getClass().getName(),
                        StringHandling.getNotNull(cause.getMessage())));
            }
        } catch (ClassNotFoundException e) {
            // if we get in here it means the database driver was not
            // found
            logger.error(e.getClass().getName()
                    + ": looking for driver '"
                    + setupForm.getDatabaseDriver()
                    + "'",
                    e);
            errorMessages.add(null, new ActionMessage(
                    "errors.setup.databaseDriver",
                    databaseDriver));
        }
    }
    /**
     * Check the form is valid and, if not, return the action forward we should
     * go to, to sort it out.
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param formParam optional ActionForm bean for this request (if any).
     * @return <code>null</code> if the action should continue, otherwise the
     * name of a forward to pass control to.
     */
    protected String checkForm(final ActionMapping mappingParam,
            final ActionForm formParam) {
        if (!"setupForm".equals(mappingParam.getName())) {
            return "setupAction";
        }
        return null;
    }
    /**
     * Split off from <code>onConfirm</code> to create a new start database.
     *
     * @param setupForm the current request form.
     * @param errorMessages used to append messages to if there is any error
     * creating the database.
     * @param dbFile the file representing the path to the automatically
     * generated <strong>HypersonicSQL</strong> database.
     * @param propertiesFile file representing the path to the properties file
     * for the automatically generated <strong>HypersonicSQL</strong> database.
     * @param lockFile file representing path to the database lock file
     * for the automatically generated <strong>HypersonicSQL</strong> database.
     * @param scriptFile file representing the path to the script file
     * for the automatically generated <strong>HypersonicSQL</strong> database.
     */
    private static void copyStartDB(final ActionErrors errorMessages,
            final File dbFile,
            final File propertiesFile,
            final File scriptFile,
            final File lockFile) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating database.");
        }

        File dbDirectory = dbFile.getParentFile();
        // try to create the directory, if it doesn't exist
        if ((dbDirectory != null)
                && !dbDirectory.exists()) {
            if (!dbDirectory.mkdirs()) {
                logger.error("Could not create directory '"
                        + dbDirectory.getAbsolutePath()
                        + "'.");
                errorMessages.add(null, new ActionMessage(
                        "errors.setup.databaseDirectory",
                        dbDirectory.getAbsolutePath()));
            }
        }
        // check the previous step didn't fail.
        if (dbDirectory.exists()) {
            // see if there is a previous lock file and remove it if so
            if (lockFile.exists()) {
                logger.warn("Removing previous lock file '"
                        + lockFile.getAbsolutePath()
                        + "'.");
                if(!lockFile.delete()) {
                    logger.error("COULD NOT REMOVE previous lock file '"
                            + lockFile.getAbsolutePath()
                            + "'.");
                }
            }

            // now we have the directory, and we know the user is cool with
            // our writing the files out, so next try to extract the start
            // db into the chosen path
            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = Hibernate.class
                    .getResourceAsStream(HibernateSetupConstants.START_DB
                            + HibernateSetupConstants.HSQLDB_PROPERTIES_SUFFIX);
                if (inputStream == null) {
                    throw new FileNotFoundException("Could not "
                            + "locate HypersonicSQL properties file '"
                            + HibernateSetupConstants.START_DB
                            + HibernateSetupConstants.HSQLDB_PROPERTIES_SUFFIX
                            + "'");
                }

                OutputStream outputStream = new FileOutputStream(
                        propertiesFile);
                while ((bytesRead = inputStream.read(buffer, 0,
                        buffer.length)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream = Hibernate.class
                    .getResourceAsStream(HibernateSetupConstants.START_DB
                            + HibernateSetupConstants.HSQLDB_SCRIPT_SUFFIX);
                if (inputStream == null) {
                    throw new FileNotFoundException("Could not "
                            + "locate HypersonicSQL script file '"
                            + HibernateSetupConstants.START_DB
                            + HibernateSetupConstants.HSQLDB_SCRIPT_SUFFIX
                            + "'");
                }
                outputStream = new FileOutputStream(scriptFile);
                while ((bytesRead = inputStream.read(buffer, 0,
                        buffer.length)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                logger.error("Could not copy database resources.", e);
                errorMessages.add(null, new ActionMessage(
                        "errors.setup.databaseResources",
                        e.getClass().getName(),
                        e.getMessage()));
            }
        }
    }
    /**
     * Create a <strong>HypersonicSQL</strong> database on the local file system.
     * This is a copy of the start (memory) database shipped with <strong>ivata
     * groupware</strong>. If the form exists already and the user has confirmed
     * she doesn't want to overwrite, then the existing form is left as is.
     *
     * @param setupForm current form from the request.
     * @param request current servlet request we are processing.
     * @param errorMessages if there are any errors, they will be appended to
     * this collection.
     * @return <code>true</code> if all is well and the database has been crated
     * or an existing one can be used, othwerise <code>false</code> if user
     * confirmation is required before using/overwriting an existing DB.
     */
    private static boolean copyStartDB(
            final ActionErrors errorMessages,
            final String databaseURL,
            final String confirmOverwriteString) {
        String dbPath = databaseURL.substring(
                HibernateSetupConstants.AUTOMATIC_DATABASE_URL_START.length());
        File dbFile = new File(dbPath);
        if (logger.isDebugEnabled()) {
            logger.debug("Database path is '"
                    + dbPath
                    + "', for URL '"
                    + databaseURL
                    + "'");
        }
        File propertiesFile = new File(dbPath
                + HibernateSetupConstants.HSQLDB_PROPERTIES_SUFFIX);
        File scriptFile = new File(dbPath
                + HibernateSetupConstants.HSQLDB_SCRIPT_SUFFIX);
        File lockFile = new File(dbPath
                + HibernateSetupConstants.HSQLDB_LOCK_SUFFIX);
        // if either file exists, check the user confirmed we can
        // overwrite them
        boolean databaseExists = (propertiesFile.exists()
                || scriptFile.exists());
        boolean confirmOverwrite = "true".equals(confirmOverwriteString);
        // if the database exists, and we have not asked the user for
        // confirmation, then go to the user confirmation page
        if ((confirmOverwriteString == null)
                && databaseExists) {
            return false;
        }
        if ((!databaseExists)
                || confirmOverwrite) {
            copyStartDB(errorMessages, dbFile,
                    propertiesFile, scriptFile, lockFile);
        }
        return true;
    }

    /**
     * Prepare the form for the first use of the setup page.
     *
     * @param mappingParam Refer to {@link MaskAction#execute}.
     * @param errorsParam Refer to {@link MaskAction#execute}.
     * @param formParam Refer to {@link MaskAction#execute}.
     * @param requestParam Refer to {@link MaskAction#execute}.
     * @param responseParam Refer to {@link MaskAction#execute}.
     * @param sessionParam Refer to {@link MaskAction#execute}.
     * @return Refer to {@link MaskAction#execute}.
     * @throws SystemException Refer to {@link MaskAction#execute}.
     */
    public String execute(ActionMapping mappingParam, ActionErrors errorsParam,
            ActionForm formParam, HttpServletRequest requestParam,
            HttpServletResponse responseParam, HttpSession session)
            throws SystemException {
        // if there is no security session
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        if (securitySession == null) {
            super.execute(mappingParam, errorsParam, formParam, requestParam,
                    responseParam, session);
            securitySession = (SecuritySession)
                session.getAttribute("securitySession");
        }
        if (formParam == null) {
            return "setup";
        }
        HibernateSetupForm setupForm = (HibernateSetupForm) formParam;
        // by default create database automatically, if we are using the memory
        // db already
        ActionErrors errorMessages = new ActionErrors();
        Document document = readHibernateConfig(errorMessages);
        String uRL = HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL;
        if (errorMessages.isEmpty()) {
            // note here - if there were errors, we will just default to create
            // a db automatically below
            Element element = (Element)
                document.selectSingleNode(getHibernateXPath(
                        HibernateSetupConstants
                            .HIBERNATE_PROPERTY_DATABASE_URL));
            if (element != null) {
                uRL = element.getTextTrim();
            }
        }
        // if you are logged on as guest and the default (memory) URL is not
        // used (i.e. this is a real, not test system), you need to log in first
        if (securitySession.isGuest()
                && !HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL
                    .equals(uRL)) {
            return "setupSecurity";
        }
        // by deafult, create the database automatically for the user, if she
        // is still using the memory db
        if (HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL.equals(uRL)) {
            setupForm.setCreateDatabaseAutomatically(true);
        } else {
            // default the form values from the hibernate config
            setupForm.setCreateDatabaseAutomatically(false);
            setupForm.setDatabaseURL(uRL);
            Element element = (Element)
                document.selectSingleNode(getHibernateXPath(
                    HibernateSetupConstants
                        .HIBERNATE_PROPERTY_DATABASE_DIALECT));
            setupForm.setDatabaseDialect(element.getTextTrim());
            element = (Element)
                document.selectSingleNode(getHibernateXPath(
                    HibernateSetupConstants
                        .HIBERNATE_PROPERTY_DATABASE_DRIVER));
            setupForm.setDatabaseDriver(element.getTextTrim());
            element = (Element)
            document.selectSingleNode(getHibernateXPath(
                HibernateSetupConstants
                    .HIBERNATE_PROPERTY_DATABASE_PASSWORD));
            setupForm.setDatabasePassword(element.getTextTrim());
            element = (Element)
            document.selectSingleNode(getHibernateXPath(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_USER_NAME));
            setupForm.setDatabaseUserName(element.getTextTrim());
        }
        return null;
    }
    /**
     * Called when the user clicks the 'OK' button to continue with the chosen
     * setup options. If you chose hibernate settings, this will write them
     * out.
     *
     * @param mappingParam Refer to {@link MaskAction#onConfirm}.
     * @param errorsParam Refer to {@link MaskAction#onConfirm}.
     * @param formParam Refer to {@link MaskAction#onConfirm}.
     * @param requestParam Refer to {@link MaskAction#onConfirm}.
     * @param responseParam Refer to {@link MaskAction#onConfirm}.
     * @param sessionParam Refer to {@link MaskAction#onConfirm}.
     * @param defaultForwardParam Refer to {@link MaskAction#onConfirm}.
     * @return Refer to {@link MaskAction#onConfirm}.
     * @throws SystemException Refer to {@link MaskAction#onConfirm}.
     */
    public String onConfirm(ActionMapping mappingParam,
            ActionErrors errorMessages, ActionForm formParam,
            HttpServletRequest request, HttpServletResponse responseParam,
            HttpSession session, String defaultForwardParam)
            throws SystemException {
        HibernateSetupForm setupForm = (HibernateSetupForm)formParam;
        if (formParam == null) {
            return "setup";
        }
        // if we should create the database automatically, do that first
        if (setupForm.isCreateDatabaseAutomatically()) {
            // the method fails if it needs user confirmation
            String confirmOverwriteString =
                request.getParameter("confirmOverwrite");
            if (!copyStartDB(
                    errorMessages, setupForm.getDatabaseURL(),
                    confirmOverwriteString)) {
                return "setupConfirm";
            }
        }

        // if there was no error till this point, that means any automatic
        // process went smoothly. in that case, perform validation checks
        // on the db
        if (errorMessages.isEmpty()) {
            checkConnection(setupForm, errorMessages);
        }
        // if there are no errors thus far, try writing out the new hibernate
        // configuration
        // first read in the existing hibernate config
        Document document = null;
        if (errorMessages.isEmpty()) {
            document = readHibernateConfig(errorMessages);
        }
        // if that went well, update the settings in the config
        if ((document != null)
                && errorMessages.isEmpty()) {
            updateHibernateConfig(document, setupForm, errorMessages);
        }
        // finally, write out the changed Hibernate config
        if ((document != null)
                && errorMessages.isEmpty()) {
            writeHibernateConfig(document, errorMessages);
        }

        // now, if all is well, reset the pico container factory to use
        // the new settings database. then update the mail settings to the
        // values shown
        if (errorMessages.isEmpty()) {
            // login as guest to force a re-login after this process
            SecuritySession securitySession = (SecuritySession)
                session.getAttribute("securitySession");
            if ((securitySession == null)
                    || !securitySession.isGuest()) {
                super.execute(mappingParam, errorMessages, formParam, request,
                        responseParam, session);
            }
            // FIXME: this is dirty. I need a nicer way to trigger a reset of
            // all cached actions in the request processor at this point
            assert (actions != null);
            actions.clear();
            resetFactoryUpdateSettings(setupForm, (SecuritySession)
                    session.getAttribute("securitySession"), session);
        }

        // did we get error messages? if not, continue as normal to the results
        // page
        if (errorMessages.isEmpty()) {
            // confirm that the application is set-up, in case the memory
            // database is still used. Note, in this case, the user will have to
            // go thru' the setup again, the next time the app is restarted -
            // "that's not a bug, it's a feature" ;-)
            servlet.getServletContext().setAttribute(
                    HibernateSetupConstants.CONFIRM_ATTRIBUTE,
                    Boolean.TRUE);
            return defaultForwardParam;
        }

        // if there were errors, return to the setup input page
        request.setAttribute(Globals.ERROR_KEY, errorMessages);
        return null;
    }
    /**
     * Read in the current hibernate config so we can update the changed
     * <strong>Hibernate</strong> configuration.
     * @param errorMessages any error will be added to this collection.
     * @return <strong>DOM4J</strong> document containing the
     * <strong>Hibernate</strong> configuration.
     */
    private Document readHibernateConfig(final ActionErrors errorMessages) {
        String hibernateConfigPath =
            servlet.getServletContext().getRealPath(
                    HibernateSetupConstants.HIBERNATE_CONFIG);
        SAXReader reader = new SAXReader();
        Document document = null;
        // first read in the existing Hibernate config
        try {
            // use an entity resolver so the DTD is found even if you have
            // no internet
            reader.setEntityResolver(new DTDEntityResolver());
            // without the FileReader, it fails on resin
            document = reader.read(new FileReader(hibernateConfigPath));
        } catch (FileNotFoundException e) {
            logger.error(e.getClass().getName()
                    + ": reading the hibernate config '"
                    + HibernateSetupConstants.HIBERNATE_CONFIG
                    + "', real path '"
                    + hibernateConfigPath
                    + "'",
                    e);
            errorMessages.add(null, new ActionMessage(
                    "errors.setup.hibernateConfigRead",
                    e.getClass().getName(),
                    HibernateSetupConstants.HIBERNATE_CONFIG,
                    hibernateConfigPath,
                    e.getMessage()));
        } catch (DocumentException e) {
            logger.error(e.getClass().getName()
                    + ": reading the hibernate config '"
                    + HibernateSetupConstants.HIBERNATE_CONFIG
                    + "', real path '"
                    + hibernateConfigPath
                    + "'",
                    e);
            errorMessages.add(null, new ActionMessage(
                    "errors.setup.hibernateConfigRead",
                    e.getClass().getName(),
                    HibernateSetupConstants.HIBERNATE_CONFIG,
                    hibernateConfigPath,
                    e.getMessage()));
        }
        return document;
    }
    /**
     * Reset the pico container factory. Override this method toupdate settings
     * in the new factory.
     * @param setupForm the form from the current request.
     * @param securitySession guest security session, used to access settings.
     * @param session current HTTP session.
     * @throws SystemException if any setting cannot be set.
     */
    protected void resetFactoryUpdateSettings(
            final HibernateSetupForm hibernateSetupForm,
            final SecuritySession securitySession,
            final HttpSession session)
            throws SystemException {
        PicoContainerFactory factory = PicoContainerFactory.getInstance();
        factory.reset();
        factory.initialize();
    }
    /**
     * This method should only ever be called by the request processor,
     * to set the actions so they can be reset at the appropriate time.
     *
     * @param actionsParam map of all action instances.
     */
    public void setActions(Map actionsParam) {
        this.actions = actionsParam;
    }
    /**
     * Helper to select a single property node from the hibernate config
     * file, set its text and handle any error.
     * @param propertyName name of the property to search for.
     * @param text new value of the text for this element, if found.
     * @param document hibernate config document.
     * @param errorMessages any error will be added to this collection.
     */
    private void setHibernateProperty(final String propertyName,
            final String text, final Document document,
            final ActionErrors errorMessages) {
        // change the config to match the new settings
        String xPath = getHibernateXPath(propertyName);
        Element element = (Element)
            document.selectSingleNode(
                    xPath);
        if (element == null) {
            errorMessages.add(null, new ActionMessage(
                    "errors.setup.hibernateConfigElement",
                    HibernateSetupConstants.HIBERNATE_CONFIG,
                    xPath));
        } else {
            element.setText(text);
        }
    }
    /**
     * Update the entries in the <strong>DOM4J</strong> document to represent
     * the new <strong>Hibernate</strong> config values.
     *
     * @param document <strong>DOM4J</strong> document representing the
     * <strong>Hibernate</strong> config values.
     * @param setupForm current form we are processing from the request.
     * @param errorMessages any error will be added to this collection.
     */
    private void updateHibernateConfig(final Document document,
            final HibernateSetupForm setupForm,
            final ActionErrors errorMessages) {
        setHibernateProperty(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_DRIVER,
                setupForm.getDatabaseDriver(),
                document,
                errorMessages);
        setHibernateProperty(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_URL,
                setupForm.getDatabaseURL(),
                document,
                errorMessages);
        setHibernateProperty(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_DIALECT,
                setupForm.getDatabaseDialect(),
                document,
                errorMessages);
        setHibernateProperty(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_USER_NAME,
                setupForm.getDatabaseUserName(),
                document,
                errorMessages);
        setHibernateProperty(
                HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_PASSWORD,
                setupForm.getDatabasePassword(),
                document,
                errorMessages);
    }
    /**
     * Write out the hibernate config to the file.
     * @param document the current <strong>DOM4J</strong>, with updated
     * settings.
     * @param errorMessages if the file cannot be written, a message is logged
     * here.
     */
    private void writeHibernateConfig(final Document document,
            final ActionErrors errorMessages) throws SystemException {
        // store the filename so we read the same config in next time
        String hibernateConfigPath =
            servlet.getServletContext().getRealPath(
                    HibernateSetupConstants.HIBERNATE_CONFIG);
        PicoContainerFactory.getInstance().setHibernateConfigFileName(
                hibernateConfigPath);
        SAXReader reader = new SAXReader();
        // now this is the bit where we write the config out
        try {
            XMLWriter writer = new XMLWriter(
                    new FileWriter(hibernateConfigPath),
                    new OutputFormat("  ", true));
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            logger.error(e.getClass().getName()
                    + ": writing the hibernate config '"
                    + HibernateSetupConstants.HIBERNATE_CONFIG
                    + "', real path '"
                    + hibernateConfigPath
                    + "'",
                    e);
            errorMessages.add(null, new ActionMessage(
                    "errors.setup.hibernateConfigRead",
                    e.getClass().getName(),
                    HibernateSetupConstants.HIBERNATE_CONFIG,
                    hibernateConfigPath,
                    e.getMessage()));
        }
    }
}
