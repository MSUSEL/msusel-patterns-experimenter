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
package org.hibernate.ejb.util;

import java.util.Map;
import java.util.Properties;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceException;

import org.hibernate.AssertionFailure;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

/**
 * @author Emmanuel Bernard
 */
public abstract class ConfigurationHelper {
	public static void overrideProperties(Properties properties, Map<?,?> overrides) {
		for ( Map.Entry entry : overrides.entrySet() ) {
			if ( entry.getKey() != null && entry.getValue() != null ) {
				properties.put( entry.getKey(), entry.getValue() );
			}
		}
	}

	public static FlushMode getFlushMode(Object value) {
		FlushMode flushMode = null;
		if (value instanceof FlushMode) {
			flushMode = (FlushMode) value;
		}
		else if (value instanceof javax.persistence.FlushModeType) {
			flushMode = ConfigurationHelper.getFlushMode( (javax.persistence.FlushModeType) value);
		}
		else if (value instanceof String) {
			flushMode = ConfigurationHelper.getFlushMode( (String) value);
		}
		if (flushMode == null) {
			throw new PersistenceException("Unable to parse org.hibernate.flushMode: " + value);
		}
		return flushMode;
	}

	private static FlushMode getFlushMode(String flushMode)  {
		if (flushMode == null) {
			return null;
		}
		flushMode = flushMode.toUpperCase();
		return FlushMode.valueOf( flushMode );
	}

	private static FlushMode getFlushMode(FlushModeType flushMode)  {
		switch(flushMode) {
			case AUTO:
				return FlushMode.AUTO;
			case COMMIT:
				return FlushMode.COMMIT;
			default:
				throw new AssertionFailure("Unknown FlushModeType: " + flushMode);
		}
	}

	public static Integer getInteger(Object value) {
		if ( value instanceof Integer ) {
			return (Integer) value;
		}
		else {
			return Integer.valueOf( (String) value );
		}
	}

	public static Boolean getBoolean(Object value) {
		if ( value instanceof Boolean ) {
			return (Boolean) value;
		}
		else {
			return Boolean.valueOf( (String) value );
		}
	}

	public static CacheMode getCacheMode(Object value) {
		if ( value instanceof CacheMode ) {
			return (CacheMode) value;
		}
		else {
			return CacheMode.valueOf( (String) value );
		}
	}
}
