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
package org.hibernate.service.spi;

import org.jboss.logging.Logger;

import org.hibernate.service.Service;

/**
 * Models a binding for a particular service
 *
 * @author Steve Ebersole
 */
public final class ServiceBinding<R extends Service> {
	private static final Logger log = Logger.getLogger( ServiceBinding.class );

	public static interface ServiceLifecycleOwner {
		public <R extends Service> R initiateService(ServiceInitiator<R> serviceInitiator);

		public <R extends Service> void configureService(ServiceBinding<R> binding);
		public <R extends Service> void injectDependencies(ServiceBinding<R> binding);
		public <R extends Service> void startService(ServiceBinding<R> binding);

		public <R extends Service> void stopService(ServiceBinding<R> binding);
	}

	private final ServiceLifecycleOwner lifecycleOwner;
	private final Class<R> serviceRole;
	private final ServiceInitiator<R> serviceInitiator;
	private R service;

	public ServiceBinding(ServiceLifecycleOwner lifecycleOwner, Class<R> serviceRole, R service) {
		this.lifecycleOwner = lifecycleOwner;
		this.serviceRole = serviceRole;
		this.serviceInitiator = null;
		this.service = service;
	}

	public ServiceBinding(ServiceLifecycleOwner lifecycleOwner, ServiceInitiator<R> serviceInitiator) {
		this.lifecycleOwner = lifecycleOwner;
		this.serviceRole = serviceInitiator.getServiceInitiated();
		this.serviceInitiator = serviceInitiator;
	}

	public ServiceLifecycleOwner getLifecycleOwner() {
		return lifecycleOwner;
	}

	public Class<R> getServiceRole() {
		return serviceRole;
	}

	public ServiceInitiator<R> getServiceInitiator() {
		return serviceInitiator;
	}

	public R getService() {
		return service;
	}

	public void setService(R service) {
		if ( this.service != null ) {
			if ( log.isDebugEnabled() ) {
				log.debug( "Overriding existing service binding [" + serviceRole.getName() + "]" );
			}
		}
		this.service = service;
	}
}
