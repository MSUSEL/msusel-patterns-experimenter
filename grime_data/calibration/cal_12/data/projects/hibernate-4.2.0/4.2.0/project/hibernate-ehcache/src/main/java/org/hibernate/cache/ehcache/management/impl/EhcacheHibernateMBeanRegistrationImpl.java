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
package org.hibernate.cache.ehcache.management.impl;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheManagerEventListener;
import org.jboss.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cfg.Environment;

/**
 * Implementation of {@link EhcacheHibernateMBeanRegistration}.
 * Also implements {@link net.sf.ehcache.event.CacheManagerEventListener}. Deregisters mbeans when the associated cachemanager is shutdown.
 * <p/>
 * <p/>
 *
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 */
public class EhcacheHibernateMBeanRegistrationImpl
        implements EhcacheHibernateMBeanRegistration, CacheManagerEventListener {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            EhcacheHibernateMBeanRegistrationImpl.class.getName()
    );
    private static final int MAX_MBEAN_REGISTRATION_RETRIES = 50;
    private String cacheManagerClusterUUID;
    private String registeredCacheManagerName;
    private Status status = Status.STATUS_UNINITIALISED;
    private volatile EhcacheHibernate ehcacheHibernate;
    private volatile ObjectName cacheManagerObjectName;

    /**
     * {@inheritDoc}
     */
    public synchronized void registerMBeanForCacheManager(final CacheManager manager, final Properties properties)
            throws Exception {
        String sessionFactoryName = properties.getProperty( Environment.SESSION_FACTORY_NAME );
        String name = null;
        if ( sessionFactoryName == null ) {
            name = manager.getName();
        }
        else {
            name = "".equals( sessionFactoryName.trim() ) ? manager.getName() : sessionFactoryName;
        }
        registerBean( name, manager );
    }

    private void registerBean(String name, CacheManager manager) throws Exception {
        ehcacheHibernate = new EhcacheHibernate( manager );
        int tries = 0;
        boolean success = false;
        Exception exception = null;
        cacheManagerClusterUUID = manager.getClusterUUID();
        do {
            this.registeredCacheManagerName = name;
            if ( tries != 0 ) {
                registeredCacheManagerName += "_" + tries;
            }
            try {
                // register the CacheManager MBean
                MBeanServer mBeanServer = getMBeanServer();
                cacheManagerObjectName = EhcacheHibernateMbeanNames.getCacheManagerObjectName(
                        cacheManagerClusterUUID,
                        registeredCacheManagerName
                );
                mBeanServer.registerMBean( ehcacheHibernate, cacheManagerObjectName );
                success = true;
                break;
            }
            catch ( InstanceAlreadyExistsException e ) {
                success = false;
                exception = e;
            }
            tries++;
        } while ( tries < MAX_MBEAN_REGISTRATION_RETRIES );
        if ( !success ) {
            throw new Exception(
                    "Cannot register mbean for CacheManager with name" + manager.getName() + " after "
                            + MAX_MBEAN_REGISTRATION_RETRIES + " retries. Last tried name=" + registeredCacheManagerName,
                    exception
            );
        }
        status = status.STATUS_ALIVE;
    }

    private MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * {@inheritDoc}
     */
    public void enableHibernateStatisticsSupport(SessionFactory sessionFactory) {
        ehcacheHibernate.enableHibernateStatistics( sessionFactory );
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void dispose() throws CacheException {
        if ( status == Status.STATUS_SHUTDOWN ) {
            return;
        }

        try {
            getMBeanServer().unregisterMBean( cacheManagerObjectName );
        }
        catch ( Exception e ) {
            LOG.warn(
                    "Error unregistering object instance " + cacheManagerObjectName + " . Error was " + e.getMessage(),
                    e
            );
        }
        ehcacheHibernate = null;
        cacheManagerObjectName = null;
        status = Status.STATUS_SHUTDOWN;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Status getStatus() {
        return status;
    }

    /**
     * No-op in this case
     */
    public void init() throws CacheException {
        // no-op
    }

    /**
     * No-op in this case
     */
    public void notifyCacheAdded(String cacheName) {
        // no-op
    }

    /**
     * No-op in this case
     */
    public void notifyCacheRemoved(String cacheName) {
        // no-op
    }

}
