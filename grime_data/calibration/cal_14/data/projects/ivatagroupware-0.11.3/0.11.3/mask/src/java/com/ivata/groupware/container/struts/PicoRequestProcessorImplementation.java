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
package com.ivata.groupware.container.struts;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.Mask;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.field.FieldValueConvertorFactory;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.valueobject.ValueObject;
import com.ivata.mask.web.browser.Browser;
import com.ivata.mask.web.field.DefaultFieldWriterFactory;
import com.ivata.mask.web.field.FieldWriterFactory;
import com.ivata.mask.web.struts.InputMaskForm;
import com.ivata.mask.web.struts.MaskRequestProcessorImplementation;


/**
 * <p>
 * Overridden to extend the create action and create action form methods.
 * </p>
 *
 * @since ivata groupware 0.10 (11-Mar-2005)
 * @author Colin MacLeod <colin.macleod@ivata.com>
 * @version $Revision: 1.1 $
 */

public class PicoRequestProcessorImplementation extends
        MaskRequestProcessorImplementation {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger
            .getLogger(PicoRequestProcessorImplementation.class);

    /**
     * <p>
     * Specifies the  path to the mask config file, relative to the webapp root.
     * </p>
     */
    public static final String MASKS_FILE_NAME = "/WEB-INF/ivataMasks.xml";
    private Security security;
    /**
     * <p>
     * Get a valid persistence manager for the super class constructor.
     * </p>
     *
     * @return valid hibernate persistence manager from the default pico
     * container.
     */
    static PersistenceManager getPersistenceManager() throws SystemException {
        PicoContainer container = PicoContainerFactory.getInstance()
            .getGlobalContainer();
        PersistenceManager persistenceManager =
            (PersistenceManager) container
                .getComponentInstance(PersistenceManager.class);
        assert(persistenceManager != null);
        return persistenceManager;
    }
    /**
     * <p>
     * Get a request processor implementation.
     * </p>
     *
     * @return valid hibernate persistence manager from the default pico
     * container.
     */
    static MaskRequestProcessorImplementation
            getRequestProcessorImplementation() {
        try {
            MaskRequestProcessorImplementation implementation =
                (MaskRequestProcessorImplementation)
                PicoContainerFactory.getInstance().instantiateOrOverride(
                        PicoRequestProcessorImplementation.class);
            assert(implementation != null);
            return implementation;
        } catch (Exception e) {
            logger.error("Cannot intantiate pico request processor "
                    + "implementation", e);
            throw new RuntimeException(e);
        }
    }
    /**
     * <p>
     * Initializes the mask factory, the value object locator and the the
     * standard field value convertors.
     * </p>
     *
     * @param maskFactoryParam
     *            needed to access the masks and groups of masks.
     * @param persistenceManagerParam
     *            used to locate the value objects by their shared base class.
     */
    public PicoRequestProcessorImplementation(final Security security,
            final MaskFactory maskFactoryParam,
            final PersistenceManager persistenceManagerParam,
            final FieldValueConvertorFactory fieldValueConvertorFactory) {
        super (maskFactoryParam, persistenceManagerParam,
                fieldValueConvertorFactory);
        this.security = security;
    }
    /**
     * <p>
     * Create an action. This method re-implemented to create an action in a
     * <strong>PicoContainer</strong> friendly way.
     * </p>
     *
     * @param classNameParam full path and name of the action class to be created.
     * @param request request for which we are creating an action.
     * @param response response we are sending.
     * @param mapping <strong>Struts</strong> mapping.
     * @return newly created action, or <code>null</code> if we can't create one.
     * @throws IOException if the action cannot be created.
     */
    protected Action createAction(final
           String classNameParam,
           final HttpServletRequest request,
           final HttpServletResponse response,
           final ActionMapping mapping,
           final Map actions,
           final ActionServlet servlet)
           throws IOException, SystemException {
        String className = classNameParam;
        // Acquire the Action instance we will be using (if there is one)
        Action instance = null;

        // Return any existing Action instance of this class
        instance = (Action) actions.get(className);

        if (instance != null) {
            return (instance);
        }

        // initialize the mask factory, if necessary
        MaskFactory maskFactory = PicoContainerFactory.getInstance()
            .getMaskFactory();
        synchronized (maskFactory) {
            // if the mask factory is not configured, create the field writer
            // and initialize the mask factory...
            if (!maskFactory.isConfigured()) {
                ServletContext context = servlet.getServletContext();
                FieldWriterFactory fieldWriterFactory = new
                        DefaultFieldWriterFactory(getPersistenceManager(),
                                "/mask/find.action");
                context.setAttribute(FieldWriterFactory.APPLICATION_ATTRIBUTE,
                        fieldWriterFactory);
                maskFactory.readConfiguration(context.getResourceAsStream(
                        MASKS_FILE_NAME));
            }
        }

        HttpSession session = request.getSession();
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        PicoContainer container;
        // if there is a session available, use the container from that
        if (securitySession != null) {
            container = securitySession.getContainer();
        } else {
            // we timed out? login as guest
            securitySession = security.loginGuest();
            session.setAttribute("securitySession", securitySession);
            container = securitySession.getContainer();
            // for now, create a browser with no javascript support
            Browser browser = new Browser(request.getHeader("User-Agent"), null);
            session.setAttribute("browser", browser);
        }
        instance = (Action)
            PicoContainerFactory.getInstance()
                .instantiateOrOverride(container, className);
        if (instance == null) {
            throw new SystemException("Could not instantiate this class");
        }
        actions.put(className, instance);

        return instance;
    }

    /**
     * <p>
     * Get the appropriate action form for the parameters.
     * </p>
     *
     * @param request the servlet request being processed.
     * @param mapping the action mapping for the current request.
     * @param moduleConfig the module configuration.
     * @param servlet the action servlet.
     * @return ActionForm valid action form instance, or <code>null</code> if
     *     none is appropriate.
     */
    protected ActionForm createActionForm(final
            FormBeanConfig config,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final ActionMapping mapping)
            throws SystemException {
        ActionForm instance = null;
        HttpSession session = request.getSession();
        String attribute = mapping.getAttribute();
        String name = mapping.getName();

        if (attribute == null) {
            return null;
        }
        if (config == null) {
            return null;
        }

        // see if there is an existing instance we can reuse
        if ("request".equals(mapping.getScope())) {
            instance = (ActionForm) request.getAttribute(attribute);
        } else {
            instance = (ActionForm) session.getAttribute(attribute);
        }

        if (instance != null) {
            return instance;
        }

        // if it gets down here, we need to create a new instance
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        // watch for session timeout
        String type;
        if (securitySession == null) {
            return null;
        }
        PicoContainer container = securitySession.getContainer();
        // use the value object class/idstring, mask and base class to make an
        // input mask, if we have them - note you need either the id or the
        // class of the value object - you don't need both
        String valueObjectClassName = request.getParameter("valueObject.class.name");
        String valueObjectIdString = request.getParameter("valueObject.idString");
        String baseClassName = request.getParameter("baseClass.name");
        String maskName = request.getParameter("mask.name");

        if ("com.ivata.mask.web.struts.InputMaskForm"
                .equals(config.getType())) {
            assert (!(StringHandling.isNullOrEmpty(valueObjectClassName)
                        && StringHandling.isNullOrEmpty(valueObjectIdString)));
            assert (!StringHandling.isNullOrEmpty(baseClassName));
            assert (!StringHandling.isNullOrEmpty(maskName));

            try {
                Class valueObjectClass = Class.forName(valueObjectClassName);
                Class baseClass = Class.forName(baseClassName);
                MaskFactory maskFactory = PicoContainerFactory.getInstance()
                    .getMaskFactory();
                Mask mask = maskFactory.getMask(valueObjectClass, maskName);
                ValueObject valueObject;
                // if there is no id, make a new value object
                if (StringHandling.isNullOrEmpty(valueObjectIdString)) {
                    valueObject = (ValueObject)
                        PicoContainerFactory.getInstance()
                            .instantiateOrOverride(container,
                                valueObjectClassName);

                    // otherwise use hibernate to find an existing instance
                } else {
                    PersistenceManager persistenceManager =
                        getPersistenceManager();
                    PersistenceSession persistenceSession =
                        persistenceManager.openSession(securitySession);
                    try {
                        valueObject = persistenceManager.findByPrimaryKey(
                                persistenceSession, valueObjectClass,
                                valueObjectIdString);
                    } finally {
                        persistenceSession.close();
                    }
                }
                instance = new InputMaskForm(valueObject, mask, baseClass);
            } catch (ClassNotFoundException e1) {
                throw new SystemException(e1);
            }
        } else {
            // this is for non-ivata-masks forms. they are constructed via
            // pico directly
            try {
                instance = (ActionForm)
                    PicoContainerFactory.getInstance()
                        .instantiateOrOverride(container,
                            config.getType());
            } catch (SystemException e) {
                return null;
            }
        }
        return (instance);
    }
    /**
     * Refer to {@link }.
     *
     * @param servletParam
     * @param moduleConfigParam
     * @throws javax.servlet.ServletException
     * @see org.apache.struts.action.RequestProcessor#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet servletParam, ModuleConfig moduleConfigParam)
            throws ServletException {
    }

}
