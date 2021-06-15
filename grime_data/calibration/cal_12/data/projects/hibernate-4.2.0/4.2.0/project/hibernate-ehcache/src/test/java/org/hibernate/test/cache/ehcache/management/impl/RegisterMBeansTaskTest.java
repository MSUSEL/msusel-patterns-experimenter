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
package org.hibernate.test.cache.ehcache.management.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.TimerTask;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cache.ehcache.management.impl.EhcacheHibernate;
import org.hibernate.cache.ehcache.management.impl.EhcacheHibernateMBeanRegistrationImpl;
import org.hibernate.cache.ehcache.management.impl.ProviderMBeanRegistrationHelper;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mateus Pimenta
 */
public class RegisterMBeansTaskTest {
	
	private Field field;

	@Test
	public void testJMXRegistration() throws Exception {
		SessionFactory sessionFactory = null;
		System.setProperty( "derby.system.home", "target/derby" );
		Configuration config = new Configuration().configure( "/hibernate-config/hibernate.cfg.xml" );
		config.setProperty( "hibernate.hbm2ddl.auto", "create" );
		try {
			sessionFactory = config.buildSessionFactory();
		}
		catch ( HibernateException ex ) {
			System.err.println( "Initial SessionFactory creation failed." + ex );
			throw new ExceptionInInitializerError( ex );
		}		
		
		try {
			sessionFactory.getStatistics().setStatisticsEnabled( true );

			EhcacheHibernateMBeanRegistrationImpl ehcacheHibernateMBeanRegistration = new EhcacheHibernateMBeanRegistrationImpl();

	        TimerTask instance = configureTimeTask(ehcacheHibernateMBeanRegistration, config.getProperties());

	        // Run the task
	        instance.run();
	        
	        // Checks if the ehcache hibernate was successfully started
	        field = ehcacheHibernateMBeanRegistration.getClass().getDeclaredField("ehcacheHibernate");
	        field.setAccessible(true);
	        EhcacheHibernate ehcacheHibernate = (EhcacheHibernate) field.get(ehcacheHibernateMBeanRegistration);
	        Assert.assertTrue(ehcacheHibernate.isHibernateStatisticsSupported());
		} finally {
			sessionFactory.close();
		}
	}

	private TimerTask configureTimeTask(EhcacheHibernateMBeanRegistrationImpl ehcacheHibernateMBeanRegistration, Properties properties) throws Exception {
		net.sf.ehcache.config.Configuration configuration = ConfigurationFactory.parseConfiguration();
		CacheManager manager = new CacheManager( configuration );
		
		
		TimerTask instance = null;
		Class<?>[] classes = ProviderMBeanRegistrationHelper.class.getDeclaredClasses();
		for (Class<?> clazz : classes) {
			if ("org.hibernate.cache.ehcache.management.impl.ProviderMBeanRegistrationHelper.RegisterMBeansTask".equals(clazz.getCanonicalName())) {
		        Constructor<?> constructor = clazz.getConstructor(EhcacheHibernateMBeanRegistrationImpl.class, CacheManager.class, Properties.class);
				constructor.setAccessible(true);
				instance = (TimerTask) constructor.newInstance(ehcacheHibernateMBeanRegistration, manager, properties);
			}
			
		}
		return instance;
	}
	
}
