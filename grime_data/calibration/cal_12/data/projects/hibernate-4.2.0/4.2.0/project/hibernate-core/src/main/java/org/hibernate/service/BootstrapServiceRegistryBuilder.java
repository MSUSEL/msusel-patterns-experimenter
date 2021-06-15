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
package org.hibernate.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.integrator.internal.IntegratorServiceImpl;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.internal.BootstrapServiceRegistryImpl;

/**
 * Builder for bootstrap {@link ServiceRegistry} instances.
 *
 * @author Steve Ebersole
 *
 * @see BootstrapServiceRegistryImpl
 * @see ServiceRegistryBuilder#ServiceRegistryBuilder(BootstrapServiceRegistry)
 */
public class BootstrapServiceRegistryBuilder {
	private final LinkedHashSet<Integrator> providedIntegrators = new LinkedHashSet<Integrator>();
	private List<ClassLoader> providedClassLoaders;
	private ClassLoaderService providedClassLoaderService;

	/**
	 * Add an {@link Integrator} to be applied to the bootstrap registry.
	 *
	 * @param integrator The integrator to add.
	 * @return {@code this}, for method chaining
	 */
	public BootstrapServiceRegistryBuilder with(Integrator integrator) {
		providedIntegrators.add( integrator );
		return this;
	}

	/**
	 * Applies the specified {@link ClassLoader} as the application class loader for the bootstrap registry
	 *
	 * @param classLoader The class loader to use
	 * @return {@code this}, for method chaining
	 *
	 * @deprecated Use {@link #with(ClassLoader)} instead
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	@Deprecated
	public BootstrapServiceRegistryBuilder withApplicationClassLoader(ClassLoader classLoader) {
		return with( classLoader );
	}

	/**
	 * Adds a provided {@link ClassLoader} for use in class-loading and resource-lookup
	 *
	 * @param classLoader The class loader to use
	 *
	 * @return {@code this}, for method chaining
	 */
	public BootstrapServiceRegistryBuilder with(ClassLoader classLoader) {
		if ( providedClassLoaders == null ) {
			providedClassLoaders = new ArrayList<ClassLoader>();
		}
		providedClassLoaders.add( classLoader );
		return this;
	}


	/**
	 * Adds a provided {@link ClassLoaderService} for use in class-loading and resource-lookup
	 *
	 * @param classLoaderService The class loader to use
	 *
	 * @return {@code this}, for method chaining
	 */
	public BootstrapServiceRegistryBuilder with(ClassLoaderService classLoaderService) {
		providedClassLoaderService = classLoaderService;
		return this;
	}


	/**
	 * Applies the specified {@link ClassLoader} as the resource class loader for the bootstrap registry
	 *
	 * @param classLoader The class loader to use
	 * @return {@code this}, for method chaining
	 *
	 * @deprecated Use {@link #with(ClassLoader)} instead
	 */
	@Deprecated
	@SuppressWarnings( {"UnusedDeclaration"})
	public BootstrapServiceRegistryBuilder withResourceClassLoader(ClassLoader classLoader) {
		return with( classLoader );
	}

	/**
	 * Applies the specified {@link ClassLoader} as the Hibernate class loader for the bootstrap registry
	 *
	 * @param classLoader The class loader to use
	 * @return {@code this}, for method chaining
	 *
	 * @deprecated Use {@link #with(ClassLoader)} instead
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	@Deprecated
	public BootstrapServiceRegistryBuilder withHibernateClassLoader(ClassLoader classLoader) {
		return with( classLoader );
	}

	/**
	 * Applies the specified {@link ClassLoader} as the environment (or system) class loader for the bootstrap registry
	 *
	 * @param classLoader The class loader to use
	 * @return {@code this}, for method chaining
	 *
	 * @deprecated Use {@link #with(ClassLoader)} instead
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	@Deprecated
	public BootstrapServiceRegistryBuilder withEnvironmentClassLoader(ClassLoader classLoader) {
		return with( classLoader );
	}

	/**
	 * Build the bootstrap registry.
	 *
	 * @return The built bootstrap registry
	 */
	public BootstrapServiceRegistry build() {
		final ClassLoaderService classLoaderService;
		if ( providedClassLoaderService == null ) {
			classLoaderService = new ClassLoaderServiceImpl( providedClassLoaders );
		} else {
			classLoaderService = providedClassLoaderService;
		}

		final IntegratorServiceImpl integratorService = new IntegratorServiceImpl(
				providedIntegrators,
				classLoaderService
		);

		return new BootstrapServiceRegistryImpl( classLoaderService, integratorService );
	}
}
