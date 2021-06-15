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
package org.hibernate.service.internal;

import java.util.LinkedHashSet;

import org.hibernate.integrator.internal.IntegratorServiceImpl;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.service.BootstrapServiceRegistry;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.ServiceBinding;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * {@link ServiceRegistry} implementation containing specialized "bootstrap" services, specifically:<ul>
 * <li>{@link ClassLoaderService}</li>
 * <li>{@link IntegratorService}</li>
 * </ul>
 *
 * @author Steve Ebersole
 */
public class BootstrapServiceRegistryImpl
		implements ServiceRegistryImplementor, BootstrapServiceRegistry, ServiceBinding.ServiceLifecycleOwner {
	private static final LinkedHashSet<Integrator> NO_INTEGRATORS = new LinkedHashSet<Integrator>();

	private final ServiceBinding<ClassLoaderService> classLoaderServiceBinding;
	private final ServiceBinding<IntegratorService> integratorServiceBinding;

	public BootstrapServiceRegistryImpl() {
		this( new ClassLoaderServiceImpl(), NO_INTEGRATORS );
	}

	public BootstrapServiceRegistryImpl(
			ClassLoaderService classLoaderService,
			IntegratorService integratorService) {
		this.classLoaderServiceBinding = new ServiceBinding<ClassLoaderService>(
				this,
				ClassLoaderService.class,
				classLoaderService
		);

		this.integratorServiceBinding = new ServiceBinding<IntegratorService>(
				this,
				IntegratorService.class,
				integratorService
		);
	}


	public BootstrapServiceRegistryImpl(
			ClassLoaderService classLoaderService,
			LinkedHashSet<Integrator> providedIntegrators) {
		this( classLoaderService, new IntegratorServiceImpl( providedIntegrators, classLoaderService ) );
	}



	@Override
	public <R extends Service> R getService(Class<R> serviceRole) {
		final ServiceBinding<R> binding = locateServiceBinding( serviceRole );
		return binding == null ? null : binding.getService();
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <R extends Service> ServiceBinding<R> locateServiceBinding(Class<R> serviceRole) {
		if ( ClassLoaderService.class.equals( serviceRole ) ) {
			return (ServiceBinding<R>) classLoaderServiceBinding;
		}
		else if ( IntegratorService.class.equals( serviceRole ) ) {
			return (ServiceBinding<R>) integratorServiceBinding;
		}

		return null;
	}

	@Override
	public void destroy() {
	}

	@Override
	public ServiceRegistry getParentServiceRegistry() {
		return null;
	}

	@Override
	public <R extends Service> R initiateService(ServiceInitiator<R> serviceInitiator) {
		throw new ServiceException( "Boot-strap registry should only contain provided services" );
	}

	@Override
	public <R extends Service> void configureService(ServiceBinding<R> binding) {
		throw new ServiceException( "Boot-strap registry should only contain provided services" );
	}

	@Override
	public <R extends Service> void injectDependencies(ServiceBinding<R> binding) {
		throw new ServiceException( "Boot-strap registry should only contain provided services" );
	}

	@Override
	public <R extends Service> void startService(ServiceBinding<R> binding) {
		throw new ServiceException( "Boot-strap registry should only contain provided services" );
	}

	@Override
	public <R extends Service> void stopService(ServiceBinding<R> binding) {
		throw new ServiceException( "Boot-strap registry should only contain provided services" );
	}

}
