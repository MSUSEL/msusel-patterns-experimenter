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
import java.io.File
import java.io.FileNotFoundException
import java.sql.DriverManager
import net.sf.hibernate.Interceptor
import net.sf.hibernate.SessionFactory
import net.sf.hibernate.cfg.Configuration

import org.apache.log4j.Logger
import org.apache.struts.action.ActionErrors
import org.picocontainer.Parameter
import org.picocontainer.defaults.ComponentParameter
import org.picocontainer.defaults.ConstantParameter
import org.picocontainer.defaults.DefaultPicoContainer

import com.ivata.groupware.admin.script.ExternalScriptExecutor
import com.ivata.groupware.admin.script.ScriptExecutor
import com.ivata.groupware.admin.security.Security
import com.ivata.groupware.admin.security.addressbook.AddressBookSecurity
import com.ivata.groupware.admin.security.addressbook.AddressBookSecurityImpl
import com.ivata.groupware.admin.security.right.SecurityRights
import com.ivata.groupware.admin.security.right.SecurityRightsImpl
import com.ivata.groupware.admin.security.server.PlainTextSecurityServer
import com.ivata.groupware.admin.security.server.PlainTextSecuritySession
import com.ivata.groupware.admin.security.server.SecurityServer
import com.ivata.groupware.admin.security.server.SecuritySession
import com.ivata.groupware.admin.setting.Settings
import com.ivata.groupware.admin.setting.SettingsImpl
import com.ivata.groupware.admin.struts.HibernateSetupAction
import com.ivata.groupware.business.addressbook.AddressBook
import com.ivata.groupware.business.addressbook.AddressBookImpl
import com.ivata.groupware.business.addressbook.person.group.right.GroupRights
import com.ivata.groupware.business.addressbook.person.group.tree.PersonTreeModel
import com.ivata.groupware.business.addressbook.right.AddressBookRights
import com.ivata.groupware.business.addressbook.right.AddressBookRightsImpl
import com.ivata.groupware.business.addressbook.struts.PersonAction
import com.ivata.groupware.business.addressbook.struts.PersonForm
import com.ivata.groupware.business.calendar.Calendar
import com.ivata.groupware.business.calendar.CalendarImpl
import com.ivata.groupware.business.library.Library
import com.ivata.groupware.business.library.LibraryImpl
import com.ivata.groupware.business.library.right.LibraryRights
import com.ivata.groupware.business.library.right.LibraryRightsImpl
import com.ivata.groupware.business.mail.Mail
import com.ivata.groupware.business.mail.MailImpl
import com.ivata.groupware.business.mail.server.HMailServer
import com.ivata.groupware.business.mail.server.MailServer
import com.ivata.groupware.business.mail.server.ScriptMailServer
import com.ivata.groupware.business.mail.struts.MailUserAction
import com.ivata.groupware.business.mail.struts.MailUserForm
import com.ivata.groupware.business.search.SearchEngine
import com.ivata.groupware.business.search.SearchEngineImpl
import com.ivata.groupware.container.PicoContainerFactory
import com.ivata.groupware.container.persistence.QueryPersistenceManager
import com.ivata.groupware.container.persistence.TimestampDOListener
import com.ivata.groupware.container.persistence.hibernate.HibernateInterceptor
import com.ivata.groupware.container.persistence.hibernate.HibernateManager
import com.ivata.groupware.container.persistence.hibernate.HibernateQueryFactory
import com.ivata.groupware.container.persistence.hibernate.HibernateSession
import com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants
import com.ivata.groupware.mask.struts.SecurityMaskAuthenticator
import com.ivata.groupware.navigation.Navigation
import com.ivata.groupware.navigation.NavigationImpl
import com.ivata.groupware.util.SettingDateFormatter
import com.ivata.mask.MaskFactory
import com.ivata.mask.field.DefaultFieldValueConvertorFactory
import com.ivata.mask.field.FieldValueConvertorFactory
import com.ivata.mask.persistence.PersistenceManager
import com.ivata.mask.persistence.PersistenceSession
import com.ivata.mask.persistence.right.PersistenceRights
import com.ivata.mask.util.StringHandling
import com.ivata.mask.web.format.DateFormatter
import com.ivata.mask.web.format.HTMLFormatter
import com.ivata.mask.web.format.URLFormat
import com.ivata.mask.web.struts.MaskAuthenticator

////////////////////////////////////////////////////////////////////////////////
// CREATE THE INSTANCE - can't do anything else without it!
pico = new DefaultPicoContainer(parent)
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// things we commonly need - useful closures, factory, settings and logger
logger = Logger.getLogger(this.class)
// general exception handling closure
logError = {throwable, message |
    logger.error(throwable.class.name + " " + message, throwable)
    throw throwable
}

if (assemblyScope == PicoContainerFactory.NO_SCOPE) {
    logger.debug ("Started reading the NanoContainer Groovy script for scope '" +
        assemblyScope +
        "'.")
} else {
    logger.info ("Started reading the NanoContainer Groovy script for scope '" +
        assemblyScope +
        "'.")
}

// now initialize the factory and settings instances
try {
    logger.debug ("Getting factory settings")
    settings = factory.settings
} catch (Throwable t) {
    logError(t, "initializing the factory and settings instances.")
}

// closure to register a single instance of the class provided
// the class should already have been defined in pico
registerInstance = { theClass |
    theInstance = pico.getComponentInstanceOfType(theClass)
    if (theInstance == null) {
        logger.error("registerInstance - getComponentInstanceOfType returned null for ${theClass}.")
        throw new NullPointerException("registerInstance - No instance found for ${theClass}.")
    }
    pico.unregisterComponent(theClass)
    factory.singletonInstances[theClass] = theInstance
    logger.debug("registerInstance - Registering ${theClass} to ${theInstance}.")
    pico.unregisterComponent(theClass)
    pico.registerComponentInstance(theClass, theInstance)
}
// closure to use the same single instance for two classes
// definedClass should already have been registered using registerInstance
// defineNowClass is the new class to assign the same instance to
registerSameInstance = { definedClass, defineNowClass |
    alreadyDefinedInstance = factory.singletonInstances[definedClass]
    factory.singletonInstances[defineNowClass] = alreadyDefinedInstance
    pico.unregisterComponent(defineNowClass)
    pico.registerComponentInstance(defineNowClass, alreadyDefinedInstance)
}
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// REGISTER SINGLETONS
// by 'singletons' here we mean classes which have exaclty one instance.
// the classes for these instances are stored in the 'singletonClasses'
// container, and all the instances are in the 'singletonInstances' map
try {
    for (eachSingleton in factory.singletonInstances) {
        logger.debug ("Registering singleton instance '" + eachSingleton.key + "' to '" + eachSingleton.value + "'")
        pico.unregisterComponent(eachSingleton.key)
        pico.registerComponentInstance(eachSingleton.key, eachSingleton.value)
    }
} catch (Throwable t) {
    logError(t, "assigning singletons to the container.")
}
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// DYNAMIC COMPONENTS
// Register all the dynamically generated instances - these are created
// for all scopes
// ivata MASKS
try {
    logger.debug ("Registering ivata MASKS")
    pico.unregisterComponent(MaskFactory.class)
    pico.registerComponentInstance(MaskFactory.class, factory.maskFactory)
} catch (Throwable t) {
    logError(t, "registering the mask factory.")
}
// HTML FORMATTER, URL FORMAT & DATE FORMATTER
try {
    logger.debug ("Registering HTML FORMATTER, URL FORMAT & DATE FORMATTER")
    pico.registerComponentImplementation(HTMLFormatter.class)
    pico.registerComponentImplementation(URLFormat.class)
    // date formatter needs a session
    if ((assemblyScope != PicoContainerFactory.APPLICATION_SCOPE)
            && (assemblyScope != PicoContainerFactory.SINGLETON_SCOPE)) {
        pico.registerComponentImplementation(DateFormatter.class,
            SettingDateFormatter.class)
    }
} catch (Throwable t) {
    logError(t, "registering the HTML formatter, formats and date formatter.")
}

// SECURITY SERVER ACTIONS & FORMS - not on application scope
try {
    if ((assemblyScope != PicoContainerFactory.APPLICATION_SCOPE)
            && (assemblyScope != PicoContainerFactory.SINGLETON_SCOPE)) {
        logger.debug ("Registering SECURITY SERVER ACTIONS & FORMS")
        emailHost = settings["emailHost"]
        emailScriptServerEnvironment = settings["emailScriptServerEnvironment"]
        pathScriptMailServer = settings["pathScriptMailServer"]

        // only set up an email security environment it an email host is
        // available
        if (!(StringHandling.isNullOrEmpty(emailHost)
                || StringHandling.isNullOrEmpty(
                        emailScriptServerEnvironment)
                || StringHandling.isNullOrEmpty(
                        pathScriptMailServer))) {
            // the mail system extends the person action class, to add
            // user alias functionality
            pico.registerComponentImplementation(
                    PersonAction.class,
                    MailUserAction.class)

            // likewise, there is an associated extended person form...
            pico.registerComponentImplementation(
                    PersonForm.class,
                    MailUserForm.class)
        }
    }
} catch (Throwable t) {
    logError(t, "registering the security server actions and forms.")
}
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// FINALLY, THE SINGLETON SCOPE
// all of the clases registered in the 'singleton scope' container are of
// classes where we only want one instance for the whole app. these are
// registered in the following section, and then that same instance of each one
// is placed inside each subsequent container (via the REGISTER SINGLETONS
// section above).
if (assemblyScope == PicoContainerFactory.SINGLETON_SCOPE) {
    // first clear the singleton instances, in case we changed something since
    // the last time the factory was initialized
    try {
        factory.singletonInstances.clear()
    } catch (Throwable t) {
        logError(t, "clearing the singleton instances.")
    }

    // HIBERNATE SESSION FACTORY & SETTTINGS
    try {
        logger.info ("Registering HIBERNATE SESSION FACTORY & SETTTINGS")
        hibernateConfiguration = new Configuration()
        sessionFileName = factory
        if (factory.hibernateConfigFileName == null) {
            logger.info("No hibernate config file defined in pico container factory.")
            hibernateConfiguration.configure()
        } else {
            logger.info("Initializing hibernate config from '${factory.hibernateConfigFileName}'.")
            hibernateConfiguration.configure(new File(factory.hibernateConfigFileName))
        }
        pico.unregisterComponent("hibernateProperties")
        pico.registerComponentInstance("hibernateProperties",
            hibernateConfiguration.properties)

        // for some reason, the res URL doesn't work on tomcat 5.0.x
        // this ugly workaround copies the tmp db to your home drive
        if (hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_URL] == HibernateSetupConstants.AUTOMATIC_DATABASE_MEMORY_URL) {
            try {
                Class.forName(hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_DRIVER]);
                DriverManager.getConnection(
                    hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_URL],
                    hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_USER_NAME],
                    hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_PASSWORD]);
            } catch (Throwable t) {
                logger.warn("Exception trying to connect to memory database.", t);
                logger.warn("DEFAULTING TMP DATBASE URL TO '${HibernateSetupConstants.AUTOMATIC_DATABASE_TMP_URL}'")
                hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_URL] = HibernateSetupConstants.AUTOMATIC_DATABASE_TMP_URL
                actionErrors = new ActionErrors()
                copyReturn = HibernateSetupAction.copyStartDB(actionErrors,
                    HibernateSetupConstants.AUTOMATIC_DATABASE_TMP_URL,
                    "true")
                if (!(copyReturn
                        && actionErrors.isEmpty())) {
                    logError("Error copying tmp DB to '${HibernateSetupConstants.AUTOMATIC_DATABASE_TMP_URL}': " + actionErrors)
                }
            }

        }
        logger.info("Hibernate dialect: ${hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_DIALECT]}")
        logger.info("Hibernate driver: ${hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_DRIVER]}")
        logger.info("Hibernate URL: ${hibernateConfiguration.properties[HibernateSetupConstants.HIBERNATE_PROPERTY_DATABASE_URL]}")


        factory.singletonInstances[Configuration.class] = hibernateConfiguration
        sessionFactory = hibernateConfiguration.buildSessionFactory()
        factory.initializeSettingsCache(hibernateConfiguration.properties)
        pico.unregisterComponent(SessionFactory.class)
        pico.registerComponentInstance(SessionFactory.class,
                sessionFactory)
        factory.singletonInstances[SessionFactory.class] = sessionFactory
    } catch (Throwable t) {
        logError(t, "registering the Hibernate session factory and settings.")
    }

    ////////////////////////////////////////////////////////////////////////////
    // PERSISTENCE QUERIES COME NEXT
    // all of the persistence queries have been split into separate sub-scripts
    // the idea behind this is to make it easier to include just part of the
    // project - for example, to get the webmail working as a program in its
    // own right
    // each scrpt is located in a src/groovy directory in the appropriate sub-
    // project
    queryMap = new HashMap()
    queryArgumentsMap = new HashMap()
    scriptVariables = ["queryMap":queryMap, "queryArgumentsMap":queryArgumentsMap]
    // closure to run another script from the class path and handle errors
    // pass the script name and a map of all variables you want to set in the
    // shell
    runClassPathScript = { scriptName |
        try {
            classLoader = this.class.classLoader
            binding = new Binding()
            for (scriptVariable in scriptVariables) {
                binding.setProperty(scriptVariable.key, scriptVariable.value)
            }
            queriesScript = new GroovyShell(binding)
            scriptInputStream = classLoader.getResourceAsStream(scriptName)
            if (scriptInputStream == null) {
                throw new FileNotFoundException("Cannot find script.")
            }
            queriesScript.evaluate(scriptInputStream, scriptName)
        } catch (Throwable t) {
            logError(t, "loading script '${scriptName}' from the class path.")
        }
    }
    // ADDRESSBOOK
    runClassPathScript("/addressBookQueries.groovy")
    // ADMIN
    runClassPathScript("/adminQueries.groovy")
    // CALENDAR
    runClassPathScript("/calendarQueries.groovy")
    // LIBRARY
    runClassPathScript("/libraryQueries.groovy")
    // NAVIGATION
    runClassPathScript("/navigationQueries.groovy")
    // RIGHTS
    runClassPathScript("/rightsQueries.groovy")
    // SEARCH
    runClassPathScript("/searchQueries.groovy")
    // SECURITY
    runClassPathScript("/securityQueries.groovy")
    // DRIVE - TODO: put these in a separate file when we re-instroduce the
    // drive
    queryMap["driveDirectoryByParentIdName"] =
        "FROM " +
        "com.ivata.groupware.business.drive.directory.DirectoryDO directory " +
        "WHERE " +
        "directory.parent.id=:parentId " +
        "AND " +
        "directory.name=:name"
    queryArgumentsMap["driveDirectoryByParentIdName"] = ["parentId", "name"]
    queryMap["driveFileByDirectoryIdName"] =
        "FROM " +
        "com.ivata.groupware.business.drive.file.DriveFileDO driveFile  " +
        "WHERE " +
        "driveFile.directory.id=:directoryId " +
        "AND " +
        "driveFile.name=:name"
    queryArgumentsMap["driveFileByDirectoryIdName"] = ["directoryId", "name"]
    ////////////////////////////////////////////////////////////////////////////

    // HIBERNATE MANAGER
    try {
        logger.info ("Registering HIBERNATE MANAGER")
        // ok - I know this is mad but the standard array constructor
        //         parameters = new Parameter[] {x, y, z}
        // was causing an exception for me
        parameters = new Parameter[2]
        parameters[0] = new ConstantParameter(queryMap)
        parameters[1] = new ConstantParameter(queryArgumentsMap)
        pico.registerComponentImplementation(HibernateQueryFactory.class,
            HibernateQueryFactory.class, parameters)
        registerInstance(HibernateQueryFactory.class)

        // ensure it is instantiated - this will register it with the
        // persistence manager
        pico.registerComponentImplementation(
                PersistenceRights.class, GroupRights.class)
        pico.registerComponentImplementation(
            PersistenceManager.class, HibernateManager.class)
        registerInstance(PersistenceManager.class)
        registerSameInstance(PersistenceManager.class,
            QueryPersistenceManager.class)
logger.info ("Registering HIBERNATE MANAGER 9")
    } catch (Exception e) {
        logError(e, "initializing the persistence manager.")
    }

    // PERSISTENCE LISTENERS
    try {
        logger.info ("Registering PERSISTENCE LISTENERS")
        // the time stamp listener makes sure that the timestamp fields
        // are saved
        pico.registerComponentImplementation(TimestampDOListener.class)
        registerInstance(TimestampDOListener.class)
    } catch (Exception e) {
        logError(e, "initializing the persistence listeners.")
    }

    // SETTINGS
    try {
        logger.info ("Registering SETTINGS")
        pico.registerComponentImplementation(Settings.class,
                SettingsImpl.class)
        registerInstance(Settings.class)
    } catch (Throwable t) {
        logError(t, "initializing the settings.")
    }

    // ADDRESSBOOK
    try {
        logger.info ("Registering ADDRESS BOOK")
        pico.registerComponentImplementation(
                AddressBook.class, AddressBookImpl.class)
        pico.registerComponentImplementation(
                AddressBookRights.class, AddressBookRightsImpl.class)
        registerInstance(AddressBook.class)
        registerInstance(AddressBookRights.class)
    } catch (Throwable t) {
        logError(t, "initializing the address book.")
    }

    // CALENDAR
    try {
        logger.info ("Registering CALENDAR")
        pico.registerComponentImplementation(Calendar.class,
                CalendarImpl.class)
        registerInstance(Calendar.class)
    } catch (Throwable t) {
        logError(t, "initializing the calendar.")
    }

    // NAVIGATION
    try {
        logger.info ("Registering NAVIGATION")
        pico.registerComponentImplementation(Navigation.class,
                NavigationImpl.class)
        registerInstance(Navigation.class)
    } catch (Throwable t) {
        logError(t, "initializing the navigation.")
    }

    // SECURITY SERVER
    try {
        logger.info ("Registering SECURITY MANAGER")
        emailHost = settings["emailHost"]
        emailScriptServerEnvironment = settings["emailScriptServerEnvironment"]
        pathScriptMailServer = settings["pathScriptMailServer"]
        userNamePrefix = settings["userNamePrefix"]

        securitySessionServer = settings["securitySessionServer"]

        pico.registerComponentImplementation(FieldValueConvertorFactory.class,
                DefaultFieldValueConvertorFactory.class)
        registerInstance(FieldValueConvertorFactory.class)
    } catch (Throwable t) {
        logError(t, "initializing the security server.")
    }

    // extra check if we are __supposed__ to use the hMailServer
    // check to see if we have the jcom library
    try {
        if ("com.ivata.groupware.business.mail.server.HMailServer" == securitySessionServer) {
            logger.info("Checking Windows hMailServer.")
            // this should force the static methods to load
            HMailServer.getDomains()
        }
    } catch (Throwable t) {
        logError(t, "Checking for jcom.dll on Windows install.")
    }

    // MAIL SECURITY SERVER
    // only set up an email security environment it an email host is
    // available - otherwise, you get db-based plain text authentication
    try {
        logger.info ("Registering MAIL SECURITY MANAGER")
        if (StringHandling.isNullOrEmpty(emailHost)
                || StringHandling.isNullOrEmpty(securitySessionServer)
                || (ScriptMailServer.class.name == securitySessionServer
                    && (StringHandling.isNullOrEmpty(
                            emailScriptServerEnvironment)
                        || StringHandling.isNullOrEmpty(
                            pathScriptMailServer)))) {
            // if there is no email host, then use plain text authentication
            logger.warn("Warning: no mail environment found. Using plain " +
                    "text authentication. (To use the mail environment, " +
                    "all the following settings must " +
                    "be neither empty nor null.)")
            logger.warn("--> emailHost: '" +
                    emailHost +
                    "'")
            logger.warn("--> emailScriptServerEnvironment: '"+
                    emailScriptServerEnvironment +
                    "'")
            logger.warn("--> pathScriptMailServer: '" +
                    pathScriptMailServer +
                    "'")
            logger.warn("--> securitySessionServer: '" +
                    securitySessionServer +
                    "'")

            pico.registerComponentImplementation(SecurityServer.class,
                PlainTextSecurityServer.class)
            registerInstance(SecurityServer.class)
        } else {
            // script mail server needs extra parameters to get it up
            // and running
            if (ScriptMailServer.class.name
                    == securitySessionServer) {
                logger.info("Using UNIX-style script mail server.")
                // script mail server class is read from the settings
                parameters = new Parameter[3]
                parameters[0] = new ComponentParameter()
                parameters[1] = new ConstantParameter(pathScriptMailServer)
                parameters[2] = new ConstantParameter(emailScriptServerEnvironment)
                pico.registerComponentImplementation(
                        ScriptExecutor.class, ExternalScriptExecutor.class,
                        parameters)
                parameters = new Parameter[4]
                // script executor
                parameters[0] = new ComponentParameter()
                // user name prefix - prepended to system user names
                parameters[1] = new ConstantParameter(userNamePrefix)
                // address book
                parameters[2] = new ComponentParameter()
                // settings
                parameters[3] = new ComponentParameter()
                pico.registerComponentImplementation(
                        SecurityServer.class, ScriptMailServer.class,
                        parameters)

            } else if (HMailServer.class.name == securitySessionServer) {
                logger.info("Using Windows hMailServer.")

                // this is the implementation for MS Windows
                domainName = settings["emailAddressHost"]
                parameters = new Parameter[3]
                // domain name - used to access the
                // hMailServer instance
                parameters[0] = new ConstantParameter(domainName)
                // address book
                parameters[1] = new ComponentParameter()
                // settings
                parameters[2] = new ComponentParameter()
                pico.registerComponentImplementation(
                        SecurityServer.class, HMailServer.class,
                        parameters)
            } else {
                // assume Pico can initialize any other implementation
                pico.registerComponentImplementation(
                        SecurityServer.class, securitySessionServer)
            }
            registerInstance(SecurityServer.class)
            registerSameInstance(SecurityServer.class, MailServer.class)
        }
    } catch (Throwable t) {
        logError(t, "initializing the mail security server.")
    }

    // SECURITY
    try {
        logger.info ("Registering SECURITY")
        authenticatorClass = settings["siteAuthenticatorClass"]
        if (authenticatorClass == null) {
            authenticatorClass = "com.ivata.groupware.mask.struts." +
                "SecurityMaskAuthenticator"
        }
        securityMaskAuthenticatorClass = Class.forName(authenticatorClass)
        pico.registerComponentImplementation(MaskAuthenticator.class,
            SecurityMaskAuthenticator.class)

        parameters = new Parameter[4]
        // query persistence manager
        parameters[0] = new ComponentParameter()
        // security server
        parameters[1] = new ComponentParameter()
        // mask factory
        parameters[2] = new ComponentParameter()
        // demo version - fail safe default value (i.e. demo), hence
        // the double negative - if the setting is missing, it is
        // a demo and you won't be able to add users
        parameters[3] = new ConstantParameter(
                        new Boolean(Boolean.FALSE != settings["demoVersion"]))
        pico.registerComponentImplementation(AddressBookSecurity.class,
            AddressBookSecurityImpl.class, parameters)
        registerInstance(AddressBookSecurity.class)
        // use the same instance for address book security and plain old
        // security
        registerSameInstance(AddressBookSecurity.class, Security.class)
        pico.registerComponentImplementation(SecurityRights.class,
                SecurityRightsImpl.class)

        registerInstance(MaskAuthenticator.class)
        registerInstance(SecurityRights.class)
    } catch (Throwable t) {
        logError(t, "initializing the security system.")
    }

    // SEARCH ENGINE
    try {
        logger.info ("Registering SEARCH ENGINE")
        pico.registerComponentImplementation(SearchEngine.class,
            SearchEngineImpl.class)
        registerInstance(SearchEngine.class)
    } catch (Throwable t) {
        logError(t, "initializing the search engine.")
    }

    // MAIL
    try {
        logger.info ("Registering MAIL")
        pico.registerComponentImplementation(Mail.class, MailImpl.class)
        registerInstance(Mail.class)
    } catch (Throwable t) {
        logError(t, "initializing the mail system.")
    }

    // LIBRARY
    try {
        logger.info ("Registering LIBRARY")
        pico.registerComponentImplementation(Library.class, LibraryImpl.class)
        pico.registerComponentImplementation(LibraryRights.class,
            LibraryRightsImpl.class)
        registerInstance(Library.class)
        registerInstance(LibraryRights.class)
    } catch (Throwable t) {
        logError(t, "initializing the library system.")
    }
}
////////////////////////////////////////////////////////////////////////////////
if (assemblyScope == PicoContainerFactory.NO_SCOPE) {
    logger.debug ("Finished reading the NanoContainer Groovy script.")
} else {
    logger.info ("Finished reading the NanoContainer Groovy script.")
}
////////////////////////////////////////////////////////////////////////////////

