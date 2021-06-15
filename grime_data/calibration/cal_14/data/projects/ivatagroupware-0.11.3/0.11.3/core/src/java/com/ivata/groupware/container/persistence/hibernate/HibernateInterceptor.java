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
package com.ivata.groupware.container.persistence.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.type.Type;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.container.persistence.listener.AddPersistenceListener;
import com.ivata.groupware.container.persistence.listener.AmendPersistenceListener;
import com.ivata.groupware.container.persistence.listener.RemovePersistenceListener;
import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.valueobject.ValueObject;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   May 31, 2004
 * @version $Revision: 1.4 $
 */
public class HibernateInterceptor implements Interceptor, Serializable {
    private static Map addListeners = new HashMap();
    private static Map amendListeners = new HashMap();
    private static Logger log = Logger.getLogger(HibernateInterceptor.class);
    private static Map removeListeners = new HashMap();
    /**
     * Refer to {@link }.
     *
     * @param dOClass
     * @param listener
     * @see com.ivata.groupware.container.persistence.QueryPersistenceManager#addAddListener(java.lang.Class, com.ivata.groupware.container.persistence.listener.AddPersistenceListener)
     */
    public static synchronized void addAddListener(Class dOClass, AddPersistenceListener listener) {
        if (log.isDebugEnabled()) {
            log.debug("addAddListener: "
                    + listener);
        }
        List addListenersThisClass = (List)addListeners.get(dOClass.getName());
        if(addListenersThisClass == null) {
            addListeners.put(dOClass.getName(),
                    addListenersThisClass = new Vector());
        }
        addListenersThisClass.add(listener);
    }


    /**
     * Refer to {@link }.
     *
     * @param dOClass
     * @param listener
     * @see com.ivata.groupware.container.persistence.QueryPersistenceManager#addAmendListener(java.lang.Class, com.ivata.groupware.container.persistence.listener.AddPersistenceListener)
     */
    public static synchronized void addAmendListener(Class dOClass, AmendPersistenceListener listener) {
        if (log.isDebugEnabled()) {
            log.debug("addAmendListener: "
                    + listener);
        }
        List amendListenersThisClass = (List)amendListeners.get(dOClass.getName());
        if(amendListenersThisClass == null) {
            amendListeners.put(dOClass.getName(),
                    amendListenersThisClass = new Vector());
        }
        amendListenersThisClass.add(listener);
    }


    /**
     * Refer to {@link }.
     *
     * @param dOClass
     * @param listener
     * @see com.ivata.groupware.container.persistence.QueryPersistenceManager#addRemoveListener(java.lang.Class, com.ivata.groupware.container.persistence.listener.AddPersistenceListener)
     */
    public static synchronized void addRemoveListener(Class dOClass, RemovePersistenceListener listener) {
        if (log.isDebugEnabled()) {
            log.debug("addRemoveListener: "
                    + listener);
        }
        List removeListenersThisClass = (List)removeListeners.get(dOClass.getName());
        if(removeListenersThisClass == null) {
            removeListeners.put(dOClass.getName(),
                    removeListenersThisClass = new Vector());
        }
        removeListenersThisClass.add(listener);
    }
    HibernateManager hibernateManager;
    HibernateSession hibernateSession;
    SessionFactory sessionFactory;
    public HibernateInterceptor(HibernateManager hibernateManager,
            SessionFactory sessionFactory) {
        this.hibernateManager = hibernateManager;
        this.sessionFactory = sessionFactory;
    }

    /**
     * @see net.sf.hibernate.Interceptor#findDirty(Object, java.io.Serializable, Object[], Object[], String[], net.sf.hibernate.type.Type[])
     */
    public int[] findDirty(final Object object,
            final Serializable arg1,
            final Object[] arg2,
            final Object[] arg3,
            final String[] arg4,
            final Type[] arg5) {
        if (log.isDebugEnabled()) {
            log.debug("findDirty: "
                    + object);
        }
        return null;
    }

    private String getIdProperty(final Class clazz) throws CallbackException {
        try {
            return sessionFactory.getClassMetadata(clazz)
                                 .getIdentifierPropertyName();
        } catch (HibernateException e) {
            throw new CallbackException(
                "Error getting identifier property for class " + clazz, e);
        }
    }
    /**
     * Get a list of listener class names which are superclasses of the data
     * object class provided.
     *
     * @param listeners all listeners of the type (add/amend/remove) to be got.
     * @param dOClass the subclass to look for.
     * @return a <code>List</code> of <code>String</code> instances
     */
    private List getListenerClasses(Map listeners, Class dOClass)
            throws PersistenceException {
        if (log.isDebugEnabled()) {
            log.debug("getListenerClasses: getting for "
                    + dOClass);
        }
        List listenerClasses = new Vector();
        Set allClasses = listeners.keySet();
        for (Iterator iterator = allClasses.iterator(); iterator.hasNext();) {
            String className = (String) iterator.next();
            Class listenerClass;
            try {
                listenerClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new PersistenceException(e);
            }
            if (listenerClass.isAssignableFrom(dOClass)) {
                listenerClasses.add(className);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getListenerClasses: found "
                    + listenerClasses.size()
                    + " classes.");
        }
        return listenerClasses;
    }
    public Object instantiate(final Class dOClass,
            final Serializable key)
            throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("instantiate: looking for instance of "
                    + dOClass);
        }
        PicoContainer globalContainer;
        try {
            globalContainer = PicoContainerFactory.getInstance()
                .getGlobalContainer();
        } catch (SystemException e) {
            throw new CallbackException(e);
        }
        MutablePicoContainer tempContainer = new DefaultPicoContainer(globalContainer);
        tempContainer.registerComponentImplementation(dOClass);

        Object instance = tempContainer.getComponentInstance(dOClass);
        try {
            Integer id = null;
            if (key != null) {
                id = new Integer(key.toString());
            }
            PropertyUtils.setProperty(instance, getIdProperty(dOClass), id);
        } catch (CallbackException e) {
            log.error(e);
            throw new CallbackException(
                "Error setting property for key '"
                + key
                +"'", e);
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new CallbackException(
                "Error setting property for key '"
                + key
                +"'", e);
        } catch (InvocationTargetException e) {
            log.error(e);
            throw new CallbackException(
                "Error setting property for key '"
                + key
                +"'", e);
        } catch (NoSuchMethodException e) {
            log.error(e);
            throw new CallbackException(
                "Error setting property for key '"
                + key
                +"'", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("instantiate: returning "
                    + instance);
        }
        return instance;
    }

    /**
     * @see net.sf.hibernate.Interceptor#isUnsaved(Object)
     */
    public Boolean isUnsaved(final Object object) {
        // if it is a value object, whether or not it is saved depends on the
        // state of the id field
        if (object instanceof ValueObject) {
            ValueObject valueObject = (ValueObject) object;
            boolean isUnsaved = (valueObject.getIdString() == null);
            if (log.isDebugEnabled()) {
                log.debug("isUnsaved: returning "
                        + isUnsaved
                        + ".");
            }
            return new Boolean(isUnsaved);
        }
        if (log.isDebugEnabled()) {
            log.debug("isUnsaved: returning null.");
        }
        return null;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onDelete(Object, java.io.Serializable, Object[], String[], net.sf.hibernate.type.Type[])
     */
    public void onDelete(Object object, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("In onDelete: "
                    + object.toString());
        }
        // only interested in our value objects!
        if (!(object instanceof ValueObject)) {
            if (log.isDebugEnabled()) {
                log.debug("In onDelete: object is not a value object.");
            }
            return;
        }
        ValueObject valueObject = (ValueObject)object;
        Class valueObjectClass = valueObject.getClass();
        String className = valueObjectClass.getName();
        List listenerClasses;
        try {
            listenerClasses = getListenerClasses(removeListeners,
                    valueObjectClass);
        } catch (PersistenceException e) {
            throw new CallbackException(e);
        }
        for (Iterator classNameIterator = listenerClasses.iterator();
                classNameIterator.hasNext();) {
            String superclassName = (String) classNameIterator.next();
            List listeners = (List) removeListeners.get(superclassName);
            if (log.isDebugEnabled()) {
                log.debug("In onDelete: processing "
                        + listeners.size()
                        + " listener(s) of superclass "
                        + superclassName);
            }
            for (Iterator listenerIterator = listeners.iterator();
                    listenerIterator.hasNext();) {
                RemovePersistenceListener listener =
                    (RemovePersistenceListener) listenerIterator.next();
                try {
                    listener.onRemove(hibernateSession, valueObject);
                } catch (PersistenceException e) {
                    throw new CallbackException(e);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Leaving onDelete.");
        }
    }

    /**
     * @see net.sf.hibernate.Interceptor#onFlushDirty(Object, java.io.Serializable, Object[], Object[], String[], net.sf.hibernate.type.Type[])
     */
    public boolean onFlushDirty(Object object, Serializable id,
            Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("In onFlushDirty: "
                    + object.toString());
        }
        // only interested in our value objects!
        if (!(object instanceof ValueObject)) {
            if (log.isDebugEnabled()) {
                log.debug("Leaving onFlushDirty: object is not a value object.");
            }
            return false;
        }
        ValueObject valueObject = (ValueObject)object;
        String className = valueObject.getClass().getName();
        List listenerClasses;
        try {
            listenerClasses = getListenerClasses(amendListeners,
                    valueObject.getClass());
        } catch (PersistenceException e) {
            throw new CallbackException(e);
        }
        if (listenerClasses.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Leaving onFlushDirty: no listeners found for class "
                        + valueObject.getClass());
            }
            return false;
        }
        for (Iterator classNameIterator = listenerClasses.iterator();
                classNameIterator.hasNext();) {
            String superclassName = (String) classNameIterator.next();
            List listeners = (List) amendListeners.get(superclassName);
            if (log.isDebugEnabled()) {
                log.debug("In onFlushDirty: processing "
                        + listeners.size()
                        + " listener(s) of superclass "
                        + superclassName);
            }
            for (Iterator listenerIterator = listeners.iterator();
                    listenerIterator.hasNext();) {
                AmendPersistenceListener listener =
                    (AmendPersistenceListener) listenerIterator.next();
                try {
                    listener.onAmend(hibernateSession, valueObject);
                } catch (PersistenceException e) {
                    throw new CallbackException(e);
                }
                // no idea why this is necessary! why not let me modify the
                // object and let Hibernate worry about the state?!
                // FIXME: not working - throws ClassCastException on property
                // type if collection
                // updateState("onFlushDirty", valueObject, currentState,
                //        propertyNames);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Leaving onFlushDirty: returning true.");
        }
        return true;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onLoad(Object, java.io.Serializable, Object[], String[], net.sf.hibernate.type.Type[])
     */
    public boolean onLoad(final Object arg0,
            final Serializable arg1,
            final Object[] arg2,
            final String[] arg3,
            final Type[] arg4) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("onLoad: returning false.");
        }
        return false;
    }


    /**
     * @see net.sf.hibernate.Interceptor#onSave(Object, java.io.Serializable, Object[], String[], net.sf.hibernate.type.Type[])
     */
    public boolean onSave(Object object, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("In onSave: "
                    + object.toString());
        }
        // only interested in our value objects!
        if (!(object instanceof ValueObject)) {
            if (log.isDebugEnabled()) {
                log.debug("Leaving onSave: object is not a value object.");
            }
            return false;
        }
        ValueObject valueObject = (ValueObject)object;
        String className = valueObject.getClass().getName();
        List listenerClasses;
        try {
            listenerClasses = getListenerClasses(addListeners,
                    valueObject.getClass());
        } catch (PersistenceException e) {
            throw new CallbackException(e);
        }
        if (listenerClasses.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Leaving onSave: no listeners found for class "
                        + valueObject.getClass());
            }
            return false;
        }
        for (Iterator classNameIterator = listenerClasses.iterator();
                classNameIterator.hasNext();) {
            String superclassName = (String) classNameIterator.next();
            List listeners = (List) addListeners.get(superclassName);
            if (log.isDebugEnabled()) {
                log.debug("In onSave: processing "
                        + listeners.size()
                        + " listener(s) of superclass "
                        + superclassName);
            }
            for (Iterator listenerIterator = listeners.iterator();
                    listenerIterator.hasNext();) {
                AddPersistenceListener listener =
                    (AddPersistenceListener) listenerIterator.next();
                try {
                    listener.onAdd(hibernateSession, valueObject);
                } catch (PersistenceException e) {
                    throw new CallbackException(e);
                }
                // no idea why this is necessary! why not let me modify the
                // object and let Hibernate worry about the state?!
                updateState("onSave", valueObject, state, propertyNames);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Leaving onSave: returning true.");
        }
        return true;
    }
    /**
     * @see net.sf.hibernate.Interceptor#postFlush(java.util.Iterator)
     */
    public void postFlush(final Iterator arg0) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("postFlush - doing nothing");
        }
    }

    /**
     * @see net.sf.hibernate.Interceptor#preFlush(java.util.Iterator)
     */
    public void preFlush(final Iterator arg0) throws CallbackException {
        if (log.isDebugEnabled()) {
            log.debug("preFlush - doing nothing");
        }
    }
    /**
     * Chicken/egg situation - can't put this in the constructor.
     * @param hibernateSession
     */
    public void setHibernateSession(HibernateSession hibernateSession) {
        if (log.isDebugEnabled()) {
            log.debug("setHibernateSession: "
                    + hibernateSession);
        }
        this.hibernateSession = hibernateSession;
    }

    private void updateState(String methodName, ValueObject valueObject,
            Object[] state, String[] propertyNames)
            throws CallbackException {
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            try {
                Object value = PropertyUtils.getProperty(valueObject,
                        propertyName);
                if (log.isDebugEnabled()) {
                    log.debug(methodName
                            + ": property '" + propertyName
                            + "' was '"
                            + state[i]
                            + "', now '"
                            + value
                            + "'");
                }
                state[i] = value;
            } catch (IllegalAccessException e1) {
                throw new CallbackException(e1);
            } catch (InvocationTargetException e1) {
                throw new CallbackException(e1);
            } catch (NoSuchMethodException e1) {
                throw new CallbackException(e1);
            }
        }

    }
}
