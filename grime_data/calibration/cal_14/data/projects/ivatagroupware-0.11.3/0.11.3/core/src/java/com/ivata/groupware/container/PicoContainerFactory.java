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
package com.ivata.groupware.container;

import groovy.lang.Binding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.hibernate.SessionFactory;

import org.apache.log4j.Logger;
import org.nanocontainer.reflection.DefaultNanoPicoContainer;
import org.nanocontainer.script.ScriptedContainerBuilder;
import org.nanocontainer.script.groovy.GroovyContainerBuilder;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.ObjectReference;
import org.picocontainer.defaults.SimpleReference;

import com.ivata.groupware.container.persistence.hibernate.HibernateSetupConstants;
import com.ivata.mask.DefaultMaskFactory;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.field.DefaultFieldValueConvertorFactory;
import com.ivata.mask.util.SystemException;

/**
 * This factory class calls <strong>Groovy</strong> scripts to create
 * <strong>PicoContainer</code> instances.
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since ivata groupware v0.10 (2004-03-22)
 * @version $Revision: 1.5.2.1 $
 */
public final class PicoContainerFactory implements Serializable {
    /**
     * Scope string for the application level container.
     */
    public static final String APPLICATION_SCOPE = "IGW_APPLICATION_SCOPE";
    /**
     * The only instance of this class.
     */
    private static PicoContainerFactory instance = new PicoContainerFactory();

    /**
     * <p>This log provides tracing and debugging information.</p>
     */
    private static Logger logger = Logger.getLogger(PicoContainerFactory.class);
    /**
     * Scope string used when the container is request or session scope (we
     * make no distinciton).
     */
    public static final String NO_SCOPE = "IGW_NO_SCOPE";
    /**
     * Scope string for the internal 'singleton' container. This contains
     * instances of all classes which should only be created once.
     */
    public static final String SINGLETON_SCOPE = "IGW_SINGLETON_SCOPE";

    /**
     * Private helpser to intialize a class and trap any error.
     */
    private static Class classForName(final String name) throws Exception {
        try {
            return Class.forName(name);
        } catch(Exception e) {
            logger.error(e.getClass().getName()
                    + " thrown looking for class called '"
                    + name
                    + "'", e);
            throw e;
        }
    }
    /**
     * Get a single instance of the factory.
     *
     * @throws SystemException if an instance of this class cannot be
     * initialized.
     */
    public static PicoContainerFactory getInstance()
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("getInstance() - start");
        }

        synchronized (instance) {
            if (!instance.isInitialized()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getInstance() - intiializing.");
                }
                instance.initialize();
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getInstance() - end - return value = "
                    + instance);
        }
        return instance;
    }
    /**
     * This object is responsible for calling <strong>Groovy</strong> to create
     * the containers.
     */
    private ScriptedContainerBuilder builder = null;
    /**
     * The main container.
     */
    private PicoContainer globalContainer;
    /**
     * Refer to {@link #getHibernateConfigFileName}.
     */
    private String hibernateConfigFileName = null;
    /**
     * <p>
     * Stores the mask factory used throughout this application.
     * </p>
     */
    private MaskFactory maskFactory = new DefaultMaskFactory(
            "imInputMaskAction", "imListAction",
            new DefaultFieldValueConvertorFactory());
    /**
     * Filename of the nano container script used to initialize the container.
     * Will be loaded from the classpath.
     */
    private String nanoContainerScript = "/nanoContainer.groovy";
    /**
     * <strong>Hibernate</strong> session factory used throughout the
     * application.
     */
    private SessionFactory sessionFactory = null;


    /**
     * This cache is used (for efficiency reasons) to read in and store all
     * of the settings. You must have a table called <code>setting</code>, with
     * columns <code>name</code>, <code>type</code>, <code>value</code> and
     * <code>user</code>. Only those settings where <code>user</code> is
     * <code>null</code> will be read.
     */
    private Map settings = new HashMap();

    /**
     * Used to store the instances of 'singleton' classes, i.e. those where
     * we only want exactly 1 instance. Indexed by class name.
     */
    private Map singletonInstances = new HashMap();
    /**
     * Public constructor - should only be called by the application server.
     * See {@link #getInstance getInstance} to get an instance of this class
     * for any other purpose.
     */
    public PicoContainerFactory() {
    }
    /**
     *
     * @param scope
     * @return
     * @throws SystemException
     */
    public PicoContainer getContainer(final String scope,
            final PicoContainer parentContainer)
            throws SystemException {
        assert (builder != null);
        ObjectReference containerRef = new SimpleReference();
        ObjectReference parentContainerRef = new SimpleReference();
        parentContainerRef.set(parentContainer);
        builder.buildContainer(containerRef, parentContainerRef,
                scope, true);
        return (PicoContainer) containerRef.get();
    }

    /**
     * Get the root PicoContainer, and initialize it if it was not already.
     *
     * @return root container.
     */
    public PicoContainer getGlobalContainer() {
        assert (globalContainer != null);
        return globalContainer;
    }
    /**
     * Stores the full path to the filename - it seems if we use the resource
     * the configuration doesn't get reloaded after we've written it out.
     * Setting the filename as here after a setup is a workaround.
     * @return Returns the hibernate config file name.
     */
    public String getHibernateConfigFileName() {
        return hibernateConfigFileName;
    }
    /**
     * <p>
     * Get the mask factory used throughout the ivata groupware application.
     * </p>
     *
     * @return Returns the maskFactory.
     */
    public MaskFactory getMaskFactory() {
        return maskFactory;
    }
    /**
     * Return a map of all settings as they were when the container was started.
     * @return Returns the settings.
     */
    public Map getSettings() {
        return settings;
    }
    /**
     * This map contains a single instance for each class for which we want just
     * one instance, keyed by the class name. This method should only be called
     * by the <strong>Groovy</strong> initialization script.
     *
     * @return Returns the singletonInstances.
     */
    public Map getSingletonInstances() {
        return singletonInstances;
    }
    /**
     * This method is called to re-initalize the whole system. It is called
     * auto-matically from {@link #getInstance getInstance} when
     * there is no global container set up yet.
     *
     * @throws SystemException when the
     * <a href='http://www.nanocontainer.org'>NanoContainer</a> cannot be
     * initialized.
     */
    public void initialize() throws SystemException {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        PicoContainer parentContainer =
            new DefaultNanoPicoContainer(classLoader);
        // now create the builder which is used to build all subsequent
        // containers
        InputStreamReader scriptReader;
        try {
            InputStream inputStream = classLoader.getResourceAsStream(
                    nanoContainerScript);
            if (inputStream == null) {
                throw new FileNotFoundException("Could not find '"
                        + nanoContainerScript
                        + "' on the current class path.");
            }
            scriptReader = new InputStreamReader(inputStream);
        } catch (Exception e) {
            logger.error(e.getClass().getName()
                    + " thrown loading nano container script '"
                    + nanoContainerScript
                    + "'", e);
            throw new SystemException(e);
        }
        builder = new GroovyContainerBuilder(
                scriptReader,
                classLoader) {
            /**
             * Overridden to add in a factory object.
             * @param bindingParam The currently bound variables
             * @see org.nanocontainer.script.groovy
             * .GroovyContainerBuilder#handleBinding(groovy.lang.Binding)
             */
            protected void handleBinding(final Binding binding) {
                if (logger.isDebugEnabled()) {
                    logger.debug("handleBinding(Binding binding = " + binding
                            + ") - start");
                }

                binding.setProperty("factory", instance);
                super.handleBinding(binding);

                if (logger.isDebugEnabled()) {
                    logger.debug("handleBinding(Binding) - end");
                }
            }
        };

        // first register the 'singletons'
        try {
            parentContainer = getContainer(SINGLETON_SCOPE,
                    parentContainer);
        } catch (Exception e) {
            logger.error ("initialize - "
                + e.getClass().getName()
                + " initializing the SINGLETON_SCOPE.", e);
            throw new SystemException(e);
        }
        // then use that as the parent of the application scope
        try {
            globalContainer = getContainer(APPLICATION_SCOPE,
                    parentContainer);
        } catch (Exception e) {
            logger.error ("initialize - "
                + e.getClass().getName()
                + " initializing the APPLICATION_SCOPE.", e);
            throw new SystemException(e);
        }
    }

    /**
     * <p>
     * Initialize the cache with all the settings. This can then be used
     * throughout the initialization of the other objects.
     * </p>
     * <p>
     * <b>Note</b> these settings are static and represeng the value of the
     * settings when the program was started. The values are not updated
     * when the settings change, unless the factory is reset and re-initialized.
     * </p>
     * <p>
     * This method should only ever be called from
     * the <strong>Groovy</strong> intialization script.
     * </p>
     *
     * @param configuration This is a valid <strong>Hibernate</strong>
     * configuration object. It must have these properties set:
     * <code>hibernate.connection.driver_class</code>,
     * <code>hibernate.connection.url</code>,
     * <code>hibernate.connection.username</code>,
     * <code>hibernate.connection.password</code>.
     */
    public void initializeSettingsCache(Properties
            hibernateProperties) throws Exception {
        settings.clear();
        String driverClass = hibernateProperties.getProperty(
                        HibernateSetupConstants
                            .HIBERNATE_PROPERTY_DATABASE_DRIVER);
        assert(driverClass != null);
        classForName(driverClass);
        String uRL = hibernateProperties.getProperty(
                HibernateSetupConstants
                .HIBERNATE_PROPERTY_DATABASE_URL);
        assert(uRL != null);
        String userName =hibernateProperties.getProperty(
                HibernateSetupConstants
                .HIBERNATE_PROPERTY_DATABASE_USER_NAME);
        assert(userName != null);
        String password = hibernateProperties.getProperty(
                HibernateSetupConstants
                .HIBERNATE_PROPERTY_DATABASE_PASSWORD);
        Connection connection = DriverManager.getConnection(uRL, userName,
                password);
        Statement statement = connection.createStatement();
        // NOTE: this is about the only SQL in the whole app.
        ResultSet allSettings = statement.executeQuery(
                "select name, value, type from setting where person_user is "
                + "null");
        while(allSettings.next()) {
            String name = allSettings.getString(1);
            String stringValue = allSettings.getString(2);
            int type = allSettings.getInt(3);
            Object value;
            if (type == 0) {
                value = new BigDecimal(stringValue);
            } else if (type == 2) {
                value = new Boolean(stringValue);
            } else {
                value = stringValue;
            }
            settings.put(name, value);
        }
        statement.close();
    }
    /**
     * <p>
     * Create an instance of the class provided. If the container cannot
     * instantiate, a temporary container is created to specify this class.
     * </p>
     *
     * @param container The container to use to instantiate the class.
     * @param theClassName The class to instantiate.
     * @return valid instance.
     */
    public Object instantiateOrOverride(
            Class theClass)
            throws SystemException {
        return instantiateOrOverride(globalContainer, theClass);
    }
    public Object instantiateOrOverride(PicoContainer container,
            Class theClass)
            throws SystemException {
        Object instance = container.getComponentInstance(theClass);
        if (instance != null) {
            return instance;
        }

        // now try the PicoContainer override way
        MutablePicoContainer tempContainer =
            new DefaultPicoContainer(container);
        tempContainer.registerComponentImplementation(theClass);
        instance = tempContainer.getComponentInstance(theClass);
        if (instance != null) {
            return instance;
        }

        // ok, really override everything
        tempContainer = override(container);
        tempContainer.registerComponentImplementation(theClass);
        return tempContainer.getComponentInstance(theClass);
    }

    /**
     * <p>
     * Create an instance of the class provided. If the container cannot
     * instantiate, a temporary container is created to specify this class.
     * </p>
     *
     * @param container The container to use to instantiate the class.
     * @param className The name of the class to instantiate.
     * @return valid instance.
     */
    public Object instantiateOrOverride(PicoContainer container,
            String className)
            throws SystemException  {
        assert (className != null);
        Class theClass;
        try {
            theClass = classForName(className);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return instantiateOrOverride(container, theClass);
    }
    /**
     * Find out if the factory has been intialized or not.
     *
     * @return <code>true</code> if the factory has been initialized
     * (<code>init</code> method was called successfully).
     */
    public boolean isInitialized() {
        return (!singletonInstances.isEmpty());
    }

    /**
     * Override the container provided. This creates a child container of the
     * parent provided and registers all standard components on the child
     * container.
     *
     * @param parent parent container to be overridden
     */
    public MutablePicoContainer override(PicoContainer parent)
            throws SystemException {
        return (MutablePicoContainer) getContainer(NO_SCOPE, parent);
    }
    /**
     * Clear the contents of the factory and mark it as not initialized. After
     * calling this method, you should call <code>intialize</code>.
     */
    public void reset() {
        singletonInstances.clear();
        globalContainer = null;
        sessionFactory = null;
    }
    /**
     * Refer to {@link #getHibernateConfigFileName}.
     * @param hibernateConfigFileNameParam Refer to
     * {@link #getHibernateConfigFileName}.
     */
    public void setHibernateConfigFileName(String hibernateConfigFileNameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting hibernateConfigFileName. Before '"
                    + hibernateConfigFileName + "', after '"
                    + hibernateConfigFileNameParam + "'");
        }
        hibernateConfigFileName = hibernateConfigFileNameParam;
    }
    /**
     * Refer to {@link #getSettings}.
     * @param settingsParam Refer to {@link #getSettings}.
     */
    public void setSettings(Map settingsParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting settings. Before '" + settings + "', after '"
                    + settingsParam + "'");
        }
        settings = settingsParam;
    }
    /**
     * Refer to {@link #getSingletonInstances}.
     * @param singletonInstancesParam Refer to {@link #getSingletonInstances}.
     */
    public void setSingletonInstances(Map singletonInstancesParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting singletonInstances. Before '"
                    + singletonInstances + "', after '"
                    + singletonInstancesParam + "'");
        }
        singletonInstances = singletonInstancesParam;
    }
}
