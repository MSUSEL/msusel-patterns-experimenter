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
package com.ivata.groupware.business.mail.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.addressbook.AddressBook;
import com.ivata.mask.util.SystemException;

/**
 * <p>
 * On <strong>Microsoft Windows</strong> platforms, we recommend you use the
 * open source <a href='http://www.hmailserver.com/'>hMailServer</a>. This class
 * provides an interface to that system.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> this code indirectly uses
 * <a href='http://www.microsoft.com/com/default.mspx'>Microsoft COM</a>
 * and will only work on <strong>Microsoft Windows</strong>. For UNIX Systems,
 * use the {@link com.ivata.groupware.business.mail.server.ScriptMailServer
 * ScriptMailServer}.
 * </p>
 *
 * <p>
 * This version of the code has been tested to work with <strong>hMailServer
 * versions 3.x and 4.x</strong>.
 * </p>
 *
 * @since ivata groupware 0.10 (2005-02-21)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.3.2.1 $
 */
public class HMailServer extends JavaMailServer implements MailServer {
    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;
    /**
     * Name of the COM object we'll be using for <strong>hMailServer
     * v3.x</strong>.
     */
    private static final String V3_BASE_APP_NAME = "HCOM.BaseApp";
    /**
     * Name of the Domains COM object we'll be using for <strong>hMailServer
     * v4.x</strong>.
     */
    private static final String V4_DOMAINS_NAME = "hMailServer.Domains";

    /**
     * Stores the only instance of the base app - through which we communicate
     * to hMailServer. <strong>This is only used for hMailServer v3.x.</strong>.
     */
    private static IDispatch baseApp = null;
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(HMailServer.class);
    /**
     * JCom release object. See <a
     * href='http://sourceforge.net/project/showfiles.php?group_id=12841'>jcom
     * docs</a>.
     */
    private static ReleaseManager releaseManager;
    static {
        try {
            releaseManager = new ReleaseManager();
        } catch (Exception e) {
            logger.error(e.getClass().getName()
                    + " trying to create JCom ReleaseManager.");
            throw new RuntimeException (e);
        }
        IDispatch domains;
        try {
            domains = new IDispatch(releaseManager, V4_DOMAINS_NAME);
        } catch (Exception e) {
            logger.error(e.getClass().getName()
                    + " trying to retrieve hMailServer domains from COM "
                    + "object '"
                    + V4_DOMAINS_NAME
                    + "'.", e);
            // null case handled below
            domains = null;
        }
        if (domains != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Identified hMailServer v4.x domains.");
            }
        } else {
            try {
                baseApp = new IDispatch(releaseManager, V3_BASE_APP_NAME);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not find hMailServer v3.x COM object '"
                            + V3_BASE_APP_NAME
                            + "'. Trying v4.x", e);
                }
                baseApp = null;
            }
            if (baseApp == null) {
                String message = "No hMailServer v3.x Base App found "
                    + "with COM NAME '"
                    + V3_BASE_APP_NAME
                    + "', and no hMailServer 4.x Domains object found "
                    + "with COM NAME '"
                    + V4_DOMAINS_NAME
                    + "'";
                logger.error(message);
                throw new NullPointerException(message);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Identified hMailServer v3.x base app.");
            }
            try {
                logger.info("Using hMailServer version "
                        + baseApp.get("Version"));
            } catch (Exception e) {
                logger.error(e.getClass().getName()
                        + " trying to retrieve hMailServer version.", e);
            }
        }
    }
    /**
     * Get all the domains of the mail server.
     *
     * @return Returns the mail server domains.
     * @throws SystemException If the domains cannot be retrieved for any
     * reason.
     */
    public static IDispatch getDomains() throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getDomains() - start");
        }
        IDispatch domains;

        // if we're using v3, just get it from the baseApp
        if (baseApp != null) {
            try {
                domains = (IDispatch) baseApp.get("Domains");
            } catch (JComException e) {
                logger.error("Retrieving domains from hMailServer v3.x",
                        e);
                throw new SystemException(e);
            }
        } else {
            try {
                domains = new IDispatch(releaseManager, V4_DOMAINS_NAME);
            } catch (JComException e) {
                logger.error("Retrieving domains from hMailServer v4.x, COM "
                        + "name '"
                        + V4_DOMAINS_NAME
                        + "'.",
                        e);
                throw new SystemException(e);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getDomains() - end - return value = " + domains);
        }
        return domains;
    }
   /**
    * Simple test routine, to check this class is working on
    * <strong>Windows</strong>.
    *
    * @param args Program arguments - not used, in this program.
    */
   public static void main(final String[] args) {
    if (logger.isDebugEnabled()) {
        logger.debug("main(String[] args = " + args + ") - start");
    }

       HMailServer mailTest = null;
       try {
           mailTest = new HMailServer("win.ivata.com", null, null);
       } catch (Exception e) {
           logger.error("Cannot instantiate HMailServer", e);
           System.exit(-1);
       }

       try {
           mailTest.removeUser(null, "test");
       } catch (Exception e) {
           logger.warn("No previous test user", e);
       }
       try {
           mailTest.addUser(null, "test", "test name");
           mailTest.setPassword(null, "test", "mytest");
           mailTest.checkPassword(null, "test", "mytest");
           mailTest.setVacationMessage(null, "test",
                   "This is a vacation message.");
           logger.info("Vacation message is "
                   + mailTest.getVacationMessage(null, "test"));
       } catch (Exception e) {
           logger.error("TEST ERROR", e);
           System.exit(-1);
       }
       System.exit(0);

    if (logger.isDebugEnabled()) {
        logger.debug("main(String[]) - end");
    }
   }
   /**
    * <copyDoc>Refer to {@link HMailServer()}.</copyDoc>
    */
   private String domainName;
   /**
    * Constructor. Create a connection to <strong>hMailServer</strong>.
    *
    * @param domainNameParam hMailServer domain name.
    * @param addressBook {@inheritDoc}
    * @param settings {@inheritDoc}
    */
   public HMailServer(final String domainNameParam,
                    final AddressBook addressBook,
                    final Settings settings) {
        super(addressBook, settings);
        domainName = domainNameParam;
    }
    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param fullNameParam {@inheritDoc}
     * @throws SystemException If the user already exists.
     */
    public void addUser(final SecuritySession securitySession,
            final String userNameParam, final String fullNameParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("addUser(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ", String fullNameParam = "
                    + fullNameParam + ") - start");
        }

        try {
            IDispatch accounts = getDomainAccounts();
            String address = getSystemUserName(securitySession, userNameParam);
            // first check this is a unique address
            if (isUser(securitySession, userNameParam)) {
                throw new SystemException("User address must be unique: "
                        + "address '"
                        + address
                        + "' already exists.");
            }

            IDispatch newAccount = (IDispatch) accounts.method("Add",
                    new Object[] {});
            newAccount.put("Address", getSystemUserName(securitySession,
                    userNameParam));
            newAccount.put("Active", Boolean.TRUE);
            newAccount.method("Save", new Object[] {});
        } catch (SystemException e) {
            logger.error("addUser(SecuritySession, String, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("addUser(SecuritySession, String, String)", e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("addUser(SecuritySession, String, String) - end");
        }
    }

    /**
     * <copyDoc>Refer to {@link }.</copyDoc>
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param passwordParam {@inheritDoc}
     * @throws SystemException If the password doesn't match, or there is
     * a technical problem communicating with hMailServer.
     */
    public void checkPassword(final SecuritySession securitySession,
            final String userNameParam,
            final String passwordParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("checkPassword(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ", String passwordParam = "
                    + passwordParam + ") - start");
        }

        try {
            String address = getSystemUserName(securitySession,
                    userNameParam);
            IDispatch accounts = getDomainAccounts();
            IDispatch account = (IDispatch)
                accounts.get("ItemByAddress", new Object[] {
                    address
            });
            // if we're using v3, use the utilities from the baseApp
            if (baseApp != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Checking the password for account '"
                            + userNameParam
                            + "' via hMailServer v3.x.");
                }
                IDispatch utilities;
                try {
                    utilities = (IDispatch) baseApp.get("Utilities");
                } catch (JComException e) {
                    logger.error("Getting the utilities object from "
                            + "hMailServer v3.x",
                            e);
                    throw new SystemException(e);
                }
                String passwordCheck = (String) utilities.method("MD5",
                        new Object[] {
                    passwordParam
                });
                if (!passwordCheck.equals(account.get("Password"))) {
                    throw new SystemException("Passwords did not match");
                }
            } else {
                // v4.x uses the account object
                if (logger.isDebugEnabled()) {
                    logger.debug("Checking the password for account '"
                            + userNameParam
                            + "' via hMailServer v4.x.");
                }
                Boolean validatePassword = (Boolean)
                    account.method("ValidatePassword", new Object[] {
                            passwordParam});
                if (!Boolean.TRUE.equals(validatePassword)) {
                    throw new SystemException("Passwords did not match");
                }
            }
        } catch (SystemException e) {
            logger.error("checkPassword(SecuritySession, String, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("checkPassword(SecuritySession, String, String)", e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("checkPassword - end");
        }
    }

    /**
     * Private helper. Get the with the given name (in the constructor). Throw
     * an exception if the  domain doesn't exist.
     *
     *  @return the domain with the name given.
     *  @throws JComException Thrown if the domains collection cannot be
     *  retreived from hMailServer.
     *  @throws SystemException If there is no domain with the name
     *  provided.
     */
    private IDispatch getDomain()
            throws JComException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getDomain() - start");
        }

        IDispatch domain = null;
        IDispatch domains = getDomains();
        try {
            domain = (IDispatch)
                domains.method("ItemByName", new Object[] {domainName});
        } catch (JComException e) {
            logger.error("getDomain()", e);

            throw new SystemException("Could not find hMailServer domain "
                    + "called '"
                    + domainName
                    + "'",
                    e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getDomain() - end - return value = " + domain);
        }
        return domain;
    }
    /**
     * Private helper. Get the accounts collection for the domain with the given
     * name. Throw an exception if the domain doesn't exist.
     *
     * @return Domain accounts object.
     * @throws JComException if the domain cannot be retrieved, or there is no
     * accounts object.
     * @throws SystemException <copyDoc>Refer to {@link #getDomain}.</copyDoc>
     */
    private IDispatch getDomainAccounts()
            throws JComException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getDomainAccounts() - start");
        }

        IDispatch domain = getDomain();
        IDispatch accounts = (IDispatch) domain.get("Accounts");

        if (logger.isDebugEnabled()) {
            logger.debug("getDomainAccounts() - end - return value = "
                    + accounts);
        }
        return accounts;
    }
    /**
     * Private helper. Get the aliases collection for the domain with the given
     * name. Throw an exception if the domain doesn't exist.
     *
     * @return Domain aliases object.
     * @throws SystemException <copyDoc>Refer to {@link #getDomain}.</copyDoc>
     * @throws JComException If the aliases cannot be retrieved from the
     * domain.
     */
    private IDispatch getDomainAliases()
            throws JComException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getDomainAliases() - start");
        }

        IDispatch domain = getDomain();
        IDispatch aliases = (IDispatch) domain.get("Aliases");

        if (logger.isDebugEnabled()) {
            logger
                    .debug("getDomainAliases() - end - return value = "
                            + aliases);
        }
        return aliases;
    }

    /**
     * On <strong>hMailServer</strong>, the system user name is the user
     * followed by the 'at sign' and the domain name.
     *
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @return always returns <code>userNameParam</code> followed by the at
     * sign and the domain name.
     */
    public String getSystemUserName(final SecuritySession securitySession,
            final String userNameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("getSystemUserName(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        String systemUserName = userNameParam + "@" + domainName;
        if (logger.isDebugEnabled()) {
            logger.debug("Returning '"
                    + systemUserName
                    + "' as system user name for plain user name '"
                    + userNameParam
                    + "'");
        }
        return systemUserName;
    }
    ////////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public List getUserAliases(final SecuritySession securitySession,
            final String userNameParam)
        throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserAliases(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        List userAliases = new Vector();
        try {
            IDispatch aliases = getDomainAliases();
            int count = ((Integer) aliases.get("Count")).intValue();
            String address = getSystemUserName(securitySession,
                    userNameParam);
            String atDomain = "@" + domainName;

            for (int i = 0; i < count; ++i) {
                IDispatch alias = (IDispatch) aliases.get("Item", new Object[] {
                        new Integer(i)
                });
                String value = (String) alias.get("Value");
                // we're only interested in aliases for this one user
                if (!address.equals(value)) {
                    continue;
                }
                // we only want aliases to this domain
                String name = (String) alias.get("Name");
                int pos = name.indexOf(atDomain);
                if (pos == -1) {
                    continue;
                }
                userAliases.add(name.substring(0, pos - 1));
            }
        } catch (SystemException e) {
            logger.error("getUserAliases(SecuritySession, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("getUserAliases(SecuritySession, String)", e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getUserAliases - end - return value = "
                            + userAliases);
        }
        return userAliases;
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @return Never returns. This method has not been implemeneted and only
     * ever throws an exception.
     * @throws SystemException Always thrown as this method is not implemented
     * for hMailServer.
     */
    public String getUserForwarding(final SecuritySession securitySession,
            final String userNameParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserForwarding(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        throw new SystemException(
                "getUserForwarding not implemented for "
                + "HMailServer");
    }

    /**
     * Returns the username, the part before the 'at sign' in the system
     * user name.
     *
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param systemUserNameParam {@inheritDoc}
     * @return always returns <code>systemUserNameParam</code> before 'at sign'.
     */
    public String getUserNameFromSystemUserName(
            final SecuritySession securitySession,
            final String systemUserNameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserNameFromSystemUserName = "
                            + securitySession
                            + ", String systemUserNameParam = "
                            + systemUserNameParam + ") - start");
        }

        int atPos = systemUserNameParam.indexOf('@');
        if (atPos != -1) {
            String returnString = systemUserNameParam.substring(0, atPos);
            if (logger.isDebugEnabled()) {
                logger.debug("getUserNameFromSystemUserName - end - "
                        + "return value = "
                        + returnString);
            }
            return returnString;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getUserNameFromSystemUserName - end - return value = "
                            + systemUserNameParam);
        }
        return systemUserNameParam;
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @return The vacation string for this user.
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public String getVacationMessage(final SecuritySession securitySession,
            final String userNameParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getVacationMessage(SecuritySession securitySession = "
                    + securitySession
                    + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        try {
            String address = getSystemUserName(securitySession,
                    userNameParam);
            IDispatch accounts = getDomainAccounts();
            IDispatch account = (IDispatch)
                accounts.get("ItemByAddress", new Object[] {
                    address
            });
            String returnString = (String) account.get("VacationMessage");
            if (logger.isDebugEnabled()) {
                logger.debug("getVacationMessage - end - return value = "
                                + returnString);
            }
            return returnString;
        } catch (SystemException e) {
            logger.error("getVacationMessage(SecuritySession, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("getVacationMessage(SecuritySession, String)", e);

            throw new SystemException(e);
        }
    }
    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @return <code>true</code> if the user exists.
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public boolean isUser(final SecuritySession securitySession,
            final String userNameParam) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("isUser(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        try {
            boolean exists = false;
            String address = getSystemUserName(securitySession,
                    userNameParam);
            IDispatch accounts = getDomainAccounts();
            int count = ((Integer) accounts.get("Count")).intValue();
            for (int i = 0; i < count; ++i) {
                IDispatch account = (IDispatch)
                    accounts.get("Item", new Object[] {new Integer(i)});
                if (address.equals(account.get("Address"))) {
                    exists = true;
                    break;
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("isUser - end - return value = "
                                + exists);
            }
            return exists;
        } catch (SystemException e) {
            logger.error("isUser(SecuritySession, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("isUser(SecuritySession, String)", e);

            throw new SystemException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param nameParam {@inheritDoc}
     * @throws SystemException Always thrown as this method is not implemented
     * for hMailServer.
     */
    public void removeList(final SecuritySession securitySession,
            final String nameParam) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("removeList(SecuritySession securitySession = "
                    + securitySession + ", String nameParam = " + nameParam
                    + ") - start");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("removeList(SecuritySession, String) - end");
        }
        throw new SystemException(
                "removeList not implemented for "
                + "HMailServer");
    }


    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public void removeUser(final SecuritySession securitySession,
            final String userNameParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("removeUser(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ") - start");
        }

        try {
            IDispatch accounts = getDomainAccounts();
            IDispatch account = null;
            String address = getSystemUserName(securitySession,
                    userNameParam);
            try {
                account = (IDispatch) accounts.get("ItemByAddress",
                        new Object[] {address});
            } catch (JComException e) {
                logger.error("removeUser(SecuritySession, String)", e);

                throw new SystemException("No user found with address '"
                        + address
                        + "'", e);
            }
            accounts.method("DeleteByDBID", new Object[] {account.get("ID")});
        } catch (SystemException e) {
            logger.error("removeUser(SecuritySession, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("removeUser(SecuritySession, String)", e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("removeUser(SecuritySession, String) - end");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param nameParam {@inheritDoc}
     * @param usersParam {@inheritDoc}
     * @throws SystemException Always thrown as this method is not implemented
     * for hMailServer.
     */
    public void setList(final SecuritySession securitySession,
            final String nameParam, final Collection usersParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setList(SecuritySession securitySession = "
                    + securitySession + ", String nameParam = " + nameParam
                    + ", Collection usersParam = " + usersParam + ") - start");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("setList(SecuritySession, String, Collection) - end");
        }
        throw new SystemException(
                "setList not implemented for "
                + "HMailServer");
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param passwordParam {@inheritDoc}
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public void setPassword(final SecuritySession securitySession,
            final String userNameParam,
            final String passwordParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setPassword(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ", String passwordParam = "
                    + passwordParam + ") - start");
        }

        try {
            String address = getSystemUserName(securitySession,
                    userNameParam);
            IDispatch accounts = getDomainAccounts();
            IDispatch account = (IDispatch)
                accounts.get("ItemByAddress", new Object[] {
                    address
            });
            account.put("Password", passwordParam);
            account.method("Save", new Object[] {});
        } catch (SystemException e) {
            logger.error("setPassword(SecuritySession, String, String)", e);

            throw e;
        } catch (Exception e) {
            logger.error("setPassword(SecuritySession, String, String)", e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("setPassword(SecuritySession, String, String) - end");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param aliasesParam {@inheritDoc}
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public void setUserAliases(final SecuritySession securitySession,
            final String userNameParam,
            final Collection aliasesParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setUserAliases(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ", Collection aliasesParam = "
                    + aliasesParam + ") - start");
        }

        try {
            IDispatch aliases = getDomainAliases();
            int count = ((Integer) aliases.get("Count")).intValue();
            String address = getSystemUserName(securitySession,
                    userNameParam);
            String atDomain = "@" + domainName;

            // first get all existing aliases in one handy sized map
            Map allAliases = new HashMap();
            for (int i = 0; i < count; ++i) {
                IDispatch alias = (IDispatch) aliases.get("Item", new Object[] {
                        new Integer(i)
                });
                allAliases.put(alias.get("Name"), alias.get("Value"));
            }

            // ok, this is a bit sloppy but for now, if the alias is already
            // taken by someone else, it is just changed to this guy or gal
            Iterator aliasIterator = aliasesParam.iterator();
            while (aliasIterator.hasNext()) {
                String fullAlias = aliasIterator.next() + atDomain;
                String existing = (String) allAliases.get(fullAlias);
                IDispatch alias;
                if (existing != null) {
                    // if it is already set to this user, safe to ignore
                    // otherwise, we'll just quietly take it over
                    if (address.equals(existing)) {
                        continue;
                    }
                    alias = (IDispatch) aliases.get("ItemByName",
                                new Object[] {
                            fullAlias
                    });
                } else {
                    // new alias here
                    alias = (IDispatch)
                            aliases.method("Add", new Object[] {});
                    alias.put("Name", fullAlias);
                }
                alias.put("Value", address);
                alias.put("Active", Boolean.TRUE);
                alias.method("Save", new Object[] {});
            }
        } catch (SystemException e) {
            logger.error("setUserAliases(SecuritySession, String, Collection)",
                    e);
            throw e;
        } catch (Exception e) {
            logger.error("setUserAliases(SecuritySession, String, Collection)",
                    e);
            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("setUserAliases - end");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param addressParam {@inheritDoc}
     * @throws SystemException Always thrown as this method is not implemented
     * for hMailServer.
     */
    public void setUserForwarding(final SecuritySession securitySession,
            final String userNameParam, final String addressParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setUserForwarding(SecuritySession securitySession = "
                    + securitySession + ", String userNameParam = "
                    + userNameParam + ", String addressParam = " + addressParam
                    + ") - start");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("setUserForwarding - end");
        }
        throw new SystemException(
                "setUserForwarding not implemented for "
                + "HMailServer");
    }

    /**
     * {@inheritDoc}
     *
     * @param securitySession {@inheritDoc}
     * @param userNameParam {@inheritDoc}
     * @param messageParam {@inheritDoc}
     * @throws SystemException If there is a technical problem or the domain
     * aliases cannot be retrieved.
     */
    public void setVacationMessage(final SecuritySession securitySession,
            final String userNameParam,
            final String messageParam)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setVacationMessage(SecuritySession securitySession = "
                    + securitySession
                    + ", String userNameParam = "
                    + userNameParam
                    + ", String messageParam = "
                    + messageParam + ") - start");
        }

        try {
            String address = getSystemUserName(securitySession,
                    userNameParam);
            IDispatch accounts = getDomainAccounts();
            IDispatch account = (IDispatch)
                accounts.get("ItemByAddress", new Object[] {
                    address
            });
            account.put("VacationMessage", messageParam);
            account.method("Save", new Object[] {});
        } catch (SystemException e) {
            logger.error("setVacationMessage",
                    e);

            throw e;
        } catch (Exception e) {
            logger.error("setVacationMessage",
                    e);

            throw new SystemException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("setVacationMessage - end");
        }
    }
}
