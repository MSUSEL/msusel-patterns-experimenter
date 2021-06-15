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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.struts.HibernateSetupForm;
import com.ivata.groupware.business.mail.server.HMailServer;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;

/**
 * Contains all of the values you need to setup a basic <strong>ivata
 * groupware</strong> installation.
 *
 * @since ivata groupware 0.11 (2005-03-27)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2.2.1 $
 */
public class MailSetupForm extends HibernateSetupForm {
    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(MailSetupForm.class);
    /**
     * <copyDoc>Refer to {@link #getMailDomain}.</copyDoc>
     */
    private String mailDomain;
    /**
     * <copyDoc>Refer to {@link #getMailDomains}.</copyDoc>
     */
    private List mailDomains = null;
    /**
     * <copyDoc>Refer to {@link #getMailHostIMAP}.</copyDoc>
     */
    private String mailHostIMAP;
    /**
     * <copyDoc>Refer to {@link #getMailHostSMTP}.</copyDoc>
     */
    private String mailHostSMTP;
    /**
     * <copyDoc>Refer to {@link #getMailServerScriptsPath}.</copyDoc>
     */
    private String scriptsPath;
    /**
     * <copyDoc>Refer to {@link #isWindows}.</copyDoc>
     */
    private boolean windows = false;
    /**
     * Prepare the form.
     *
     * @see com.ivata.mask.web.struts.DialogForm#clear()
     */
    protected void clear() {
        if (logger.isDebugEnabled()) {
            logger.debug("clear() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("clear() - end");
        }
    }
    /**
     * <p>
     * Get the domain name of the email address for users of this system.
     * The domain name is that part which comes afer the 'at symbol' (@) in
     * their email addresses.
     * </p>
     * <p>
     * On Windows, this must match the domain name set up in
     * <strong>hMailServer</strong>.
     * </p>
     * @return Returns the mail domain.
     */
    public String getMailDomain() {
        if (logger.isDebugEnabled()) {
            logger.debug("getMailDomain() - start");
        }

        if (logger.isDebugEnabled()) {
            logger
                    .debug("getMailDomain() - end - return value = "
                            + mailDomain);
        }
        return mailDomain;
    }
    /**
     * On <strong>Windows</strong>, this returns a list of all email domains
     * as strings. On other platforms, it just returns <code>null</code>.
     *
     * @return Returns the mail domains for <strong>hMailServer</strong> on
     * <strong>Windows</strong>
     */
    public List getMailDomains() {
        if (logger.isDebugEnabled()) {
            logger.debug("getMailDomains() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getMailDomains() - end - return value = "
                    + mailDomains);
        }
        return mailDomains;
    }
    /**
     * Name or IP address of the host used to receive <strong>IMAP</strong>
     * messages.
     *
     * @return Returns the mail host used for IMAP.
     */
    public String getMailHostIMAP() {
        if (logger.isDebugEnabled()) {
            logger.debug("getMailHostIMAP() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getMailHostIMAP() - end - return value = "
                    + mailHostIMAP);
        }
        return mailHostIMAP;
    }
    /**
     * Name or IP address of the host used to send mail messages.
     *
     * @return Returns the mail host used for SMTP.
     */
    public String getMailHostSMTP() {
        if (logger.isDebugEnabled()) {
            logger.debug("getMailHostSMTP() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getMailHostSMTP() - end - return value = "
                    + mailHostSMTP);
        }
        return mailHostSMTP;
    }
    /**
     * On <strong>UNIX-style</strong> systems, this is the path to scripts which
     * are used to add/remove users from the mail system. (The implementation
     * for <strong>Windows</strong> uses <strong>COM</strong> to communicate
     * with <strong>hMailServer</strong> directly.
     *
     * @return Returns the mail server scripts path.
     */
    public String getScriptsPath() {
        if (logger.isDebugEnabled()) {
            logger.debug("getScriptsPath() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getScriptsPath() - end - return value = "
                    + scriptsPath);
        }
        return scriptsPath;
    }
    /**
     * Returns <code>true</code> if this is a <strong>Microsoft Windows</strong>
     * installation.
     * @return Returns <code>true</code> if this is a <strong>Microsoft
     * Windows</strong> installation.
     */
    public boolean isWindows() {
        if (logger.isDebugEnabled()) {
            logger.debug("isWindows() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("isWindows() - end - return value = " + windows);
        }
        return windows;
    }

    /**
     * {@inheritDoc}
     *
     * @param mappingParam {@inheritDoc}
     * @param requestParam {@inheritDoc}
     */
    public void reset(
            final ActionMapping mappingParam,
            final HttpServletRequest requestParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("reset(ActionMapping mappingParam = " + mappingParam
                    + ", HttpServletRequest requestParam = " + requestParam
                    + ") - start");
        }

        super.reset(mappingParam, requestParam);
        SecuritySession securitySession = (SecuritySession)
            requestParam.getSession().getAttribute("securitySession");
        // don't use constructor injection as the factory may reset
        PicoContainerFactory factory;
        try {
            factory = PicoContainerFactory.getInstance();
        } catch (SystemException e) {
            logger.error("reset(ActionMapping, HttpServletRequest)", e);

            throw new RuntimeException(e);
        }
        assert (factory != null);
        PicoContainer container = factory.getGlobalContainer();
        assert (container != null);
        Settings settings = (Settings)
            container.getComponentInstance(Settings.class);
        assert (settings != null);
        windows = System.getProperty("os.name")
            .toLowerCase().startsWith("windows");

        try {
            mailDomain =
                    settings.getStringSetting(securitySession,
                            "emailAddressHost",
                            null);
            // on windows, get all the domains from hMailServer
            if (windows) {
                mailDomains = new Vector();
                try {
                    IDispatch domains = HMailServer.getDomains();
                    int count;
                    if (domains == null) {
                        logger.error("Null domains object returned from"
                                + " HMailServer. Could not connect to "
                                + "hMailServer via COM.");
                        count = 0;
                    } else {
                        count = ((Integer) domains.get("Count")).intValue();
                    }
                    for (int i = 0; i < count; ++i) {
                        IDispatch domain = (IDispatch) domains.get("Item",
                                new Object[] {
                                    new Integer(i)
                        });
                        mailDomains.add(domain.get("Name"));
                    }
                } catch (JComException e) {
                    logger.error(e.getClass().getName()
                            + ": getting Windows domains.",
                            e);
                    throw new RuntimeException(e);
                }
            } else if (StringHandling.isNullOrEmpty(mailDomain)) {
                // only default the mail domain in UNIX; on windows it will
                // default to the first domain found in hMailServer
                mailDomain = MailSetupConstants.DEFAULT_MAIL_DOMAIN;
                mailDomains = null;
            }
        } catch (SystemException e) {
            logger.error("reset(ActionMapping, HttpServletRequest)", e);

            throw new RuntimeException(e);
        }
        try {
            mailHostIMAP =
                    settings.getStringSetting(securitySession, "emailHost",
                            null);
        } catch (SystemException e) {
            logger.error("reset(ActionMapping, HttpServletRequest)", e);

            throw new RuntimeException(e);
        }
        try {
            mailHostSMTP =
                    settings.getStringSetting(securitySession, "emailHostSmtp",
                            null);
        } catch (SystemException e) {
            logger.error("reset(ActionMapping, HttpServletRequest)", e);

            throw new RuntimeException(e);
        }
        // script mail server is not for windows - only UNIX style machines
        try {
            String pathScriptMailServer =
                    settings.getStringSetting(securitySession,
                            "pathScriptMailServer",
                            null);
            int pos;
            // normall the script mail server path contains the sudo subpath
            // if not, or the setting is empty, default
            if (StringHandling.isNullOrEmpty(pathScriptMailServer)
                    || ((pos = pathScriptMailServer
                            .indexOf(MailSetupConstants
                                    .SCRIPT_PATH_SUDO)) == -1)) {
                scriptsPath = MailSetupConstants.DEFAULT_SCRIPTS_PATH;
            } else {
                scriptsPath = pathScriptMailServer.substring(0, pos);
            }
        } catch (SystemException e) {
            logger.error("reset(ActionMapping, HttpServletRequest)", e);

            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("reset(ActionMapping, HttpServletRequest) - end");
        }
    }

    /**
     * <copyDoc>Refer to {@link #getMailDomain}.</copyDoc>
     * @param mailDomainParam
     * <copyDoc>Refer to {@link #getMailDomain}.</copyDoc>
     */
    public void setMailDomain(final String mailDomainParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting mailDomain. Before '" + mailDomain
                    + "', after '" + mailDomainParam + "'");
        }
        mailDomain = mailDomainParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setMailDomain(String) - end");
        }
    }
    /**
     * <copyDoc>Refer to {@link #getMailDomains}.</copyDoc>
     * @param mailDomainsParam
     * <copyDoc>Refer to {@link #getMailDomains}.</copyDoc>
     */
    public void setMailDomains(final List mailDomainsParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting mailDomains. Before '" + mailDomains
                    + "', after '" + mailDomainsParam + "'");
        }
        mailDomains = mailDomainsParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setMailDomains(List) - end");
        }
    }
    /**
     * <copyDoc>Refer to {@link #getMailHostIMAP}.</copyDoc>
     * @param mailHostIMAPParam
     * <copyDoc>Refer to {@link #getMailHostIMAP}.</copyDoc>
     */
    public void setMailHostIMAP(final String mailHostIMAPParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting mailHostIMAP. Before '" + mailHostIMAP
                    + "', after '" + mailHostIMAPParam + "'");
        }
        mailHostIMAP = mailHostIMAPParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setMailHostIMAP(String) - end");
        }
    }
    /**
     * <copyDoc>Refer to {@link #getMailHostSMTP}.</copyDoc>
     * @param mailHostSMTPParam
     * <copyDoc>Refer to {@link #getMailHostSMTP}.</copyDoc>
     */
    public void setMailHostSMTP(final String mailHostSMTPParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting mailHostSMTP. Before '" + mailHostSMTP
                    + "', after '" + mailHostSMTPParam + "'");
        }
        mailHostSMTP = mailHostSMTPParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setMailHostSMTP(String) - end");
        }
    }

    /**
     * <copyDoc>Refer to {@link #getMailServerScriptsPath}.</copyDoc>
     * @param mailServerScriptsPathParam <copyDoc>Refer to
     * {@link #getMailServerScriptsPath}.</copyDoc>
     */
    public void setScriptsPath(final String mailServerScriptsPathParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting mailServerScriptsPath. Before '"
                    + scriptsPath + "', after '"
                    + mailServerScriptsPathParam + "'");
        }
        scriptsPath = mailServerScriptsPathParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setScriptsPath(String) - end");
        }
    }
    /**
     * <copyDoc>Refer to {@link #isWindows}.</copyDoc>
     * @param windowsParam <copyDoc>Refer to {@link #isWindows}.</copyDoc>
     */
    public void setWindows(final boolean windowsParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting windows. Before '" + windows + "', after '"
                    + windowsParam + "'");
        }
        windows = windowsParam;

        if (logger.isDebugEnabled()) {
            logger.debug("setWindows(boolean) - end");
        }
    }
    /**
     * This method has been implemented to check that the mail server fields
     * are either all blank, or all preasent.
     * @param requestParam current request we are processing.
     * @param sessionParam current HTTP session.
     * @return a validation errors collection containing any errors which
     * would prevent the form from being successfully processed.
     */
    public ValidationErrors validate(
            final HttpServletRequest requestParam,
            final HttpSession sessionParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("validate(HttpServletRequest requestParam = "
                    + requestParam + ", HttpSession sessionParam = "
                    + sessionParam + ") - start");
        }

        ValidationErrors errors = super.validate(requestParam, sessionParam);

        boolean mailDomainEmpty = StringHandling.isNullOrEmpty(mailDomain);
        boolean mailHostSMTPEmpty = StringHandling.isNullOrEmpty(mailHostSMTP);
        boolean mailHostIMAPEmpty = StringHandling.isNullOrEmpty(mailHostIMAP);
        if (!(mailDomainEmpty
                    && mailHostIMAPEmpty
                    && mailHostSMTPEmpty)
                && (mailDomainEmpty
                    || mailHostIMAPEmpty
                    || mailHostSMTPEmpty)) {
            errors.add(
                    new ValidationError("errors.setup.mailParameters",
                            Arrays.asList(new String[] {})));
        }
        // now check the scripts path, if a scripts path was specified
        if (!(StringHandling.isNullOrEmpty(scriptsPath)
                || isWindows())) {
            // strip off trailing slash
            if (scriptsPath.endsWith("/")) {
                scriptsPath = scriptsPath.substring(0,
                        scriptsPath.length() - 1);
            }
            File sudoScriptMailServerDir = new File(scriptsPath
                    + MailSetupConstants.SCRIPT_PATH_SUDO);
            File eximScriptMailServerDir = new File(scriptsPath
                    + MailSetupConstants.SCRIPT_PATH_EXIM);
            if (!sudoScriptMailServerDir.exists()) {
                errors.add(
                        new ValidationError("errors.setup.sudoPath",
                                Arrays.asList(new String[] {
                                        MailSetupConstants.SCRIPT_PATH_SUDO,
                                        scriptsPath
                                })));
            } else if (!sudoScriptMailServerDir.isDirectory()) {
                errors.add(
                        new ValidationError("errors.setup.sudoPathDirectory",
                                Arrays.asList(new String[] {
                                        MailSetupConstants.SCRIPT_PATH_SUDO,
                                        scriptsPath
                                })));
            } else  if (!eximScriptMailServerDir.exists()) {
                // note - if one is missing they likely both are - I used an
                // else here so only one rror would show
                errors.add(
                        new ValidationError("errors.setup.eximPath",
                                Arrays.asList(new String[] {
                                        MailSetupConstants.SCRIPT_PATH_EXIM,
                                        scriptsPath
                                })));
            } else if (!eximScriptMailServerDir.isDirectory()) {
                errors.add(
                        new ValidationError("errors.setup.eximPathDirectory",
                                Arrays.asList(new String[] {
                                        MailSetupConstants.SCRIPT_PATH_EXIM,
                                        scriptsPath
                                })));
            }
        }


        if (logger.isDebugEnabled()) {
            logger.debug("validate - end - return value = "
                            + errors);
        }
        return errors;
    }
}
