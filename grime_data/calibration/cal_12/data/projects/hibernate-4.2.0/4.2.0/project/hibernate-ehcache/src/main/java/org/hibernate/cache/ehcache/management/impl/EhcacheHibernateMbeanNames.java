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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Utility class used for getting {@link javax.management.ObjectName}'s for ehcache hibernate MBeans
 * <p/>
 * <p/>
 *
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 * @since 1.7
 */
public abstract class EhcacheHibernateMbeanNames {

	/**
	 * Group id for all sampled mbeans registered
	 */
	public static final String GROUP_ID = "net.sf.ehcache.hibernate";

	/**
	 * Type for the ehcache backed hibernate second level cache statistics mbean
	 */
	public static final String EHCACHE_HIBERNATE_TYPE = "EhcacheHibernateStats";

	/**
	 * Filter out invalid ObjectName characters from s.
	 *
	 * @param s
	 *
	 * @return A valid JMX ObjectName attribute value.
	 */
	public static String mbeanSafe(String s) {
		return s == null ? "" : s.replaceAll( ":|=|\n", "." );
	}

	/**
	 * Returns an ObjectName for the passed name
	 *
	 * @param name
	 *
	 * @return An {@link javax.management.ObjectName} using the input name of cache manager
	 *
	 * @throws javax.management.MalformedObjectNameException
	 */
	public static ObjectName getCacheManagerObjectName(String cacheManagerClusterUUID, String name)
			throws MalformedObjectNameException {
		ObjectName objectName = new ObjectName(
				GROUP_ID + ":type=" + EHCACHE_HIBERNATE_TYPE + ",name=" + mbeanSafe( name )
						+ getBeanNameSuffix( cacheManagerClusterUUID )
		);
		return objectName;
	}

	private static String getBeanNameSuffix(String cacheManagerClusterUUID) {
		String suffix = "";
		if ( !isBlank( cacheManagerClusterUUID ) ) {
			suffix = ",node=" + cacheManagerClusterUUID;
		}
		return suffix;
	}

	private static boolean isBlank(String param) {
		return param == null || "".equals( param.trim() );
	}
}
