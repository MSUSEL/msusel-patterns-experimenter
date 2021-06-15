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
package org.hibernate.service.config.spi;

import java.util.Map;

import org.hibernate.service.Service;

/**
 * Provides access to the initial user-provided configuration values
 *
 * @author Steve Ebersole
 */
public interface ConfigurationService extends Service {
	public Map getSettings();

	public <T> T getSetting(String name, Converter<T> converter);
	public <T> T getSetting(String name, Converter<T> converter, T defaultValue);
	public <T> T getSetting(String name, Class<T> expected, T defaultValue);

	/**
	 * Cast <tt>candidate</tt> to the instance of <tt>expected</tt> type.
	 *
	 * @param expected The type of instance expected to return.
	 * @param candidate The candidate object to be casted.
	 * @return The instance of expected type or null if this cast fail.
	 */
	public <T> T cast(Class<T> expected, Object candidate);
	public static interface Converter<T> {
		public T convert(Object value);
	}
}
