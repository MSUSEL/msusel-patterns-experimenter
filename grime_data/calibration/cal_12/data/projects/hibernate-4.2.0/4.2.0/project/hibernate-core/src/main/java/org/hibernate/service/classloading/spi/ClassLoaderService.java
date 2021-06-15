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
package org.hibernate.service.classloading.spi;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.service.Service;

/**
 * A service for interacting with class loaders
 *
 * @author Steve Ebersole
 */
public interface ClassLoaderService extends Service {
	/**
	 * Locate a class by name
	 *
	 * @param className The name of the class to locate
	 *
	 * @return The class reference
	 *
	 * @throws ClassLoadingException Indicates the class could not be found
	 */
	public <T> Class<T> classForName(String className);

	/**
	 * Locate a resource by name (classpath lookup)
	 *
	 * @param name The resource name.
	 *
	 * @return The located URL; may return {@code null} to indicate the resource was not found
	 */
	public URL locateResource(String name);

	/**
	 * Locate a resource by name (classpath lookup) and gets its stream
	 *
	 * @param name The resource name.
	 *
	 * @return The stream of the located resource; may return {@code null} to indicate the resource was not found
	 */
	public InputStream locateResourceStream(String name);

	/**
	 * Locate a series of resource by name (classpath lookup)
	 *
	 * @param name The resource name.
	 *
	 * @return The list of URL matching; may return {@code null} to indicate the resource was not found
	 */
	public List<URL> locateResources(String name);

	/**
	 * Discovers and instantiates implementations of the named service contract.
	 * <p/>
	 * NOTE : the terms service here is used differently than {@link Service}.  Instead here we are talking about
	 * services as defined by {@link java.util.ServiceLoader}.
	 *
	 * @param serviceContract The java type defining the service contract
	 * @param <S> The type of the service contract
	 *     
	 * @return The ordered set of discovered services.
	 */
	public <S> LinkedHashSet<S> loadJavaServices(Class<S> serviceContract);
}
