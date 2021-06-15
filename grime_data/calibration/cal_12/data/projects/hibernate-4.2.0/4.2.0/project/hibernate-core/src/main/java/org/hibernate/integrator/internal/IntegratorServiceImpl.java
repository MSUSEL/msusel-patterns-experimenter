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
package org.hibernate.integrator.internal;

import java.util.LinkedHashSet;

import org.jboss.logging.Logger;

import org.hibernate.cfg.beanvalidation.BeanValidationIntegrator;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.service.classloading.spi.ClassLoaderService;

/**
 * @author Steve Ebersole
 */
public class IntegratorServiceImpl implements IntegratorService {
	private static final Logger LOG = Logger.getLogger( IntegratorServiceImpl.class.getName() );

	private final LinkedHashSet<Integrator> integrators = new LinkedHashSet<Integrator>();

	public IntegratorServiceImpl(LinkedHashSet<Integrator> providedIntegrators, ClassLoaderService classLoaderService) {
		// register standard integrators.  Envers and JPA, for example, need to be handled by discovery because in
		// separate project/jars.
		addIntegrator( new BeanValidationIntegrator() );

		// register provided integrators
		for ( Integrator integrator : providedIntegrators ) {
			addIntegrator( integrator );
		}

		for ( Integrator integrator : classLoaderService.loadJavaServices( Integrator.class ) ) {
			addIntegrator( integrator );
		}
	}

	private void addIntegrator(Integrator integrator) {
		LOG.debugf( "Adding Integrator [%s].", integrator.getClass().getName() );
		integrators.add( integrator );
	}

	@Override
	public Iterable<Integrator> getIntegrators() {
		return integrators;
	}
}
