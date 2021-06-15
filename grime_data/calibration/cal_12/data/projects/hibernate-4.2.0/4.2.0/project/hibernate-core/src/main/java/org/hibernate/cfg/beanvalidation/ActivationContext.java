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
package org.hibernate.cfg.beanvalidation;

import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * Defines the context needed to call the {@link TypeSafeActivator}
 *
 * @author Steve Ebersole
 */
public interface ActivationContext {
	/**
	 * Access the requested validation mode(s).
	 * <p/>
	 * IMPL NOTE : the legacy code allowed multiple mode values to be specified, so that is why it is multi-valued here.
	 * However, I cannot find any good reasoning why it was defined that way and even JPA states it should be a single
	 * value.  For 4.1 (in maintenance) I think it makes the most sense to not mess with it.  Discuss for
	 * 4.2 and beyond.
	 *
	 * @return The requested validation modes
	 */
	public Set<ValidationMode> getValidationModes();

	/**
	 * Access the Configuration
	 *
	 * @return The Hibernate Configuration object
	 */
	public Configuration getConfiguration();

	/**
	 * Access the SessionFactory being built to trigger this BV activation
	 *
	 * @return The SessionFactory being built
	 */
	public SessionFactoryImplementor getSessionFactory();

	/**
	 * Access the ServiceRegistry specific to the SessionFactory being built.
	 *
	 * @return The SessionFactoryServiceRegistry
	 */
	public SessionFactoryServiceRegistry getServiceRegistry();
}
