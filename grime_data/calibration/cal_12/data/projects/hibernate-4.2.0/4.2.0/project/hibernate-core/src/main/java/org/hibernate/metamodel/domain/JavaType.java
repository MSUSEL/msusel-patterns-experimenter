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
package org.hibernate.metamodel.domain;

import org.hibernate.internal.util.ValueHolder;
import org.hibernate.service.classloading.spi.ClassLoaderService;

/**
 * Models the naming of a Java type where we may not have access to that type's {@link Class} reference.  Generally
 * speaking this is the case in various hibernate-tools and reverse-engineering use cases.
 *
 * @author Steve Ebersole
 */
public class JavaType {
	private final String name;
	private final ValueHolder<Class<?>> classReference;

	public JavaType(final String name, final ClassLoaderService classLoaderService) {
		this.name = name;
		this.classReference = new ValueHolder<Class<?>>(
				new ValueHolder.DeferredInitializer<Class<?>>() {
					@Override
					public Class<?> initialize() {
						return classLoaderService.classForName( name );
					}
				}
		);
	}

	public JavaType(Class<?> theClass) {
		this.name = theClass.getName();
		this.classReference = new ValueHolder<Class<?>>( theClass );
	}

	public String getName() {
		return name;
	}

	public Class<?> getClassReference() {
		return classReference.getValue();
	}

	@Override
	public String toString() {
		return new StringBuilder( super.toString() )
				.append( "[name=" ).append( name ).append( "]" )
				.toString();
	}
}
