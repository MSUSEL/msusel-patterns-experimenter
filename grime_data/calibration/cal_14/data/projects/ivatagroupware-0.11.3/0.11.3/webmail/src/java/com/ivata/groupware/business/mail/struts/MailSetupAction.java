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
package com.ivata.groupware.business.mail.struts;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.struts.HibernateSetupAction;
import com.ivata.groupware.admin.struts.HibernateSetupForm;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * Setup the application to use a database and mail server.
 *
 * @since ivata groupware 0.11 (2005-03-25)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3.2.1 $
 */
public class MailSetupAction extends HibernateSetupAction {
    /**
     * Logger for this class.
     */
    private static final Logger logger =
        Logger.getLogger(MailSetupAction.class);
    /**
     * The text of the prepared statement query used to change settings.
     */
    private static final String SETTINGS_STATEMENT =
        "UPDATE SETTING SET value = ? WHERE name = ? AND person_user IS NULL";
    /**
     * On <strong>Courier IMAP</strong> and <strong>Cyrus IMAP</strong>,
     * all mail folder names are prefixed by the value 'INBOX.' - this
     * setting stores that prefix.
     */
    private String mailFolderNamespace = "";
    /**
     * Constructor.
     *
     * @param securityParam System security object.
     * @param settingsParam System settings object.
     * @param maskFactoryParam Creates input and list masks.
     * @param authenticatorParam Used to check user is auhtorized to see this
     * page.
     */
    public MailSetupAction(final Security securityParam,
            final Settings settingsParam,
            final MaskFactory maskFactoryParam,
            final MaskAuthenticator authenticatorParam) {
        super(securityParam, settingsParam, maskFactoryParam,
                authenticatorParam);
    }
    /**
     * Check a socket on a given host is valid. This is used to check the
     * SMTP and IMAP connections.
     *
     * @param host host we are checking.
     * @param port port we are checking.
     * @param errorMessages if there are any errors, they are appended to this
     * collection.
     * @param errorString the error message to show the user if all is not well.
     * @return a string buffer containing all of the output read from the port.
     */
    private StringBuffer checkSocket(final String host, final int port,
            final ActionErrors errorMessages, final String errorString) {
        Socket socket = null;
        StringBuffer socketOutput = new StringBuffer();
        try {
            socket = new Socket(host, port);
            logger.info("Connected with server "
                    + socket.getInetAddress()
                    + ":"
                    + socket.getPort());
            InputStream socketInput = socket.getInputStream();
            byte [] buffer = new byte[1024];
            int bytesRead = 0;
            do {
                // wait a total of SOCKET_WAIT_NUMBER times, each time for
                // SOCKET_WAIT_INTERVAL milliseconds
                synchronized(this) {
                    for (int waitCount = 0;
                            (waitCount < MailSetupConstants.SOCKET_WAIT_NUMBER)
                                && (socketInput.available() == 0);
                            ++waitCount) {
                        try {
                             wait(MailSetupConstants.SOCKET_WAIT_INTERVAL);
                        } catch (InterruptedException e1) {
                            logger.warn("Socket wait interrupted.", e1);
                            break;
                        }
                    }
                }
                socketOutput.append(new String(buffer, 0, bytesRead));
            } while ((socketInput.available() != 0)
                    && ((bytesRead = socketInput.read(
                    buffer, 0, buffer.length)) != -1));
            if (logger.isDebugEnabled()) {
                logger.debug("Read the following string from "
                        + socket.getInetAddress()
                        + ":"
                        + socket.getPort());
                logger.debug(socketOutput.toString());
            }
        } catch (UnknownHostException e) {
            errorMessages.add(null, new ActionMessage(
                    errorString,
                    e.getClass().getName(),
                    host,
                    e.getMessage()));
        } catch (IOException e) {
            errorMessages.add(null, new ActionMessage(
                    errorString,
                    e.getClass().getName(),
                    host,
                    e.getMessage()));
        } finally {
            if ((socket != null)
                    && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error(e.getClass().getName()
                            + ": closing socket: "
                            + e.getMessage(),
                            e);
                }
            }
        }
        return socketOutput;
    }
    /**
     * Overridden to check that there are mail domains defined on windows.
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
            HttpServletResponse responseParam, HttpSession sessionParam)
            throws SystemException {
        MailSetupForm setupForm = (MailSetupForm) formParam;
        List mailDomains = setupForm.getMailDomains();
        if (setupForm.isWindows()
                && ((mailDomains == null)
                        || mailDomains.isEmpty())) {
            errorsParam.add(null, new ActionMessage(
                    "errors.setup.noMailDomains"));
        }
        return super.execute(mappingParam, errorsParam, formParam,
                requestParam, responseParam, sessionParam);
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
        MailSetupForm setupForm = (MailSetupForm)formParam;

        // check the mail hosts work as expected
        String mailHostIMAP = setupForm.getMailHostIMAP();
        String mailHostSMTP = setupForm.getMailHostSMTP();

        StringBuffer socketOutput = checkSocket(mailHostIMAP,
                MailSetupConstants.IMAP_PORT,
                errorMessages, "errors.setup.hostIMAP");
        // look for courier or cyrus - that means we need to set the folder
        // namespace
        if (socketOutput.indexOf(MailSetupConstants.CYRUS_SIGNATURE) != -1) {
            logger.info("IMAP server identified as Cyrus - setting folder "
                    + "namespace to '"
                    + MailSetupConstants.CYRUS_FOLDER_NAMESPACE
                    + "'.");
            mailFolderNamespace = MailSetupConstants.CYRUS_FOLDER_NAMESPACE;
        } else if (socketOutput.indexOf(MailSetupConstants.COURIER_SIGNATURE) != -1) {
            logger.info("IMAP server identified as Courier - setting folder "
                    + "namespace to '"
                    + MailSetupConstants.COURIER_FOLDER_NAMESPACE
                    + "'.");
            mailFolderNamespace = MailSetupConstants.COURIER_FOLDER_NAMESPACE;
        } else {
            logger.info("IMAP server neither Cyrus nor Courier - using "
                    + "default folder namespace.");
            mailFolderNamespace = "";
        }

        checkSocket(mailHostSMTP, MailSetupConstants.SMTP_PORT,
                errorMessages, "errors.setup.hostSMTP");

        String superReturn =
            super.onConfirm(mappingParam, errorMessages, formParam, request,
                responseParam, session, defaultForwardParam);
        // did we get error messages? if so, return to the input page
        if (!errorMessages.isEmpty()) {
            return null;
        }

        // if the superclass told us to go somewhere, we'd better comply!
        if (superReturn != null) {
            return superReturn;
        }
        // otherwise go to the default place
        return defaultForwardParam;
    }
    /**
     * Helper. Uses JDBC to set a single setting in the new settings table.
     *
     * @param preparedStatement Statement matching {@link SETTINGS_STATEMENT}.
     * @param settingName The name of the setting to be amended.
     * @param settingValue New setting value as a string.
     * @throws SystemException if the setting cannot be set, or more than 1
     * row is returned.
     */
    private void setOneSystemSetting(
            final PreparedStatement preparedStatement,
            final String settingName,
            final String settingValue)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setOneSystemSetting("
                    + "PreparedStatement preparedStatement = "
                    + preparedStatement
                    + ", String settingName = "
                    + settingName
                    + ", String settingValue = "
                    + settingValue + ") - start");
        }
        try {
            preparedStatement.setString(1, settingValue);
        } catch (SQLException e) {
            logger.error ("setOneSystemSetting - Setting setting value to '"
                + settingValue
                + "'", e);
            throw new SystemException(e);
        }
        try {
            preparedStatement.setString(2, settingName);
        } catch (SQLException e) {
            logger.error ("setOneSystemSetting - Setting setting name to '"
                + settingName
                + "'", e);
            throw new SystemException(e);
        }
        int numberOfRows;
        try {
            numberOfRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("setOneSystemSetting - Executing statement '"
                + SETTINGS_STATEMENT
                + "'", e);
            throw new SystemException(e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("setOneSystemSetting -"
                    + "Number of rows returned: "
                    + numberOfRows);
        }
        if (numberOfRows != 1) {
            String message = "setOneSystemSetting - More than 1 row returned "
                + "for system setting '"
                + settingName
                + "'";
            logger.error(message);
            throw new SystemException(message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("setOneSystemSetting - end");
        }
    }
    /**
     * Reset the pico container factory and update all settings in the new
     * factory.
     * @param setupForm the form from the current request.
     * @param securitySession guest security session, used to access settings.
     * @param session current HTTP session.
     * @throws SystemException if any setting cannot be set, or more than
     * one row is returned when setting.
     */
    protected void resetFactoryUpdateSettings(
            final HibernateSetupForm hibernateSetupForm,
            final SecuritySession securitySession,
            final HttpSession session)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("resetFactoryUpdateSettings("
                    + "HibernateSetupForm hibernateSetupForm = "
                    + hibernateSetupForm
                    + ", SecuritySession securitySession = "
                    + securitySession
                    + ", HttpSession session = "
                    + session + ") - start");
        }

        MailSetupForm setupForm = (MailSetupForm) hibernateSetupForm;
        String uRL = hibernateSetupForm.getDatabaseURL();
        String userName = hibernateSetupForm.getDatabaseUserName();
        String password = hibernateSetupForm.getDatabasePassword();

        if (logger.isDebugEnabled()) {
            logger.debug("resetFactoryUpdateSettings - "
                    + "Connecting to new DB with parameters: "
                    + "uRL = " + uRL + ", "
                    + "userName = " + userName + ", "
                    + "password = ******)");
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(uRL, userName,
                password);
        } catch (SQLException e) {
            logger.error("resetFactoryUpdateSettings - "
                    + "Connecting to new DB with paramteters: "
                    + "uRL = " + uRL + ", "
                    + "userName = " + userName + ", "
                    + "password = ******)", e);
            throw new SystemException(e);
        }
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(
                SETTINGS_STATEMENT);
        } catch (SQLException e) {
            logger.error("resetFactoryUpdateSettings - Preparing statement '"
                + SETTINGS_STATEMENT
                + "'", e);
            throw new SystemException(e);
        }
        try {
            setOneSystemSetting(preparedStatement,
                    "emailAddressHost",
                    setupForm.getMailDomain());
            setOneSystemSetting(preparedStatement,
                    "emailFolderNamespace",
                    mailFolderNamespace);
            setOneSystemSetting(preparedStatement,
                    "emailHost",
                    setupForm.getMailHostIMAP());
            setOneSystemSetting(preparedStatement,
                    "emailHostSmtp",
                    setupForm.getMailHostSMTP());
            setOneSystemSetting(preparedStatement,
                    "emailScriptServerEnvironment",
                    "SITE_ID=www\nSUDO_USER=root\nSUDO_PATH="
                        + setupForm.getScriptsPath()
                        + MailSetupConstants.SCRIPT_PATH_EXIM);
            setOneSystemSetting(preparedStatement,
                    "pathScriptMailServer",
                    setupForm.getScriptsPath()
                        + MailSetupConstants.SCRIPT_PATH_SUDO);
            // on windows, we use the COM interface to hMailServer
            if (setupForm.isWindows()) {
                setOneSystemSetting(preparedStatement,
                        "securitySessionServer",
                        "com.ivata.groupware.business.mail.server.HMailServer");
                // hMailServer needs the folder to be called 'INBOX', all caps
                setOneSystemSetting(preparedStatement,
                        "emailFolderInbox",
                        "INBOX");
            } else {
                // other platforms make do with sudo scripts
                setOneSystemSetting(preparedStatement,
                        "securitySessionServer",
                        "com.ivata.groupware.business.mail.server."
                            + "ScriptMailServer");
            }
        } catch (Exception e) {
            logger.error("resetFactoryUpdateSettings - Exception, so rolling "
                + "back transation", e);
            try {
                preparedStatement.close();
            } catch (SQLException e2) {
                logger.error ("resetFactoryUpdateSettings - Errror rolling "
                    + "back statement.", e2);
            }
            throw new SystemException(e);

        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error ("resetFactoryUpdateSettings - Error closing "
                    + "prepared statement.", e);
                throw new SystemException(e);
            }
            // HSQLDB requires us to explicitly shutdown
            if ((connection != null)
                    && (uRL.indexOf("hsqldb:file") != -1)) {
                logger.info("resetFactoryUpdateSettings - shutting down "
                    + "HSQLDB file.");
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    boolean success = statement.execute("SHUTDOWN");
                    if (!success) {
                        logger.error("resetFactoryUpdateSettings - shutting "
                        + "down HSQLDB file returned FALSE.");
                    }
                } catch (SQLException e) {
                    logger.error ("resetFactoryUpdateSettings - Error closing  "
                        + " shutdown statement.", e);
                    throw new SystemException(e);
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {
                            logger.error ("resetFactoryUpdateSettings - Error "
                                + "closing shutdown statement.", e);
                            throw new SystemException(e);
                        }
                    }
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("resetFactoryUpdateSettings() - end");
        }
    }
}
