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

import java.util.Properties;

import net.sf.ehcache.CacheManager;

import org.hibernate.SessionFactory;

/**
 * Interface for helping registering mbeans for ehcache backed hibernate second-level cache
 * <p/>
 * <p/>
 *
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 */
public interface EhcacheHibernateMBeanRegistration {

	/**
	 * Registers MBean for the input manager and session factory properties.
	 * <p/>
	 * MBeans will be registered based on the input session factory name. If the input name is null or blank, the name of the cache-manager
	 * is used
	 *
	 * @param manager
	 * @param properties
	 *
	 * @throws Exception
	 */
	public void registerMBeanForCacheManager(CacheManager manager, Properties properties) throws Exception;

	/**
	 * Enable hibernate statistics in the mbean.
	 *
	 * @param sessionFactory
	 */
	public void enableHibernateStatisticsSupport(SessionFactory sessionFactory);

}
