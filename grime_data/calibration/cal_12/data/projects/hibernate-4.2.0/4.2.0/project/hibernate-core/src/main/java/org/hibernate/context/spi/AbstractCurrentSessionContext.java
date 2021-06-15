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
package org.hibernate.context.spi;

import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.context.TenantIdentifierMismatchException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.compare.EqualsHelper;

/**
 * Base support for {@link CurrentSessionContext} implementors.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractCurrentSessionContext implements CurrentSessionContext {
	private final SessionFactoryImplementor factory;

	protected AbstractCurrentSessionContext(SessionFactoryImplementor factory) {
		this.factory = factory;
	}

	public SessionFactoryImplementor factory() {
		return factory;
	}

	protected SessionBuilder baseSessionBuilder() {
		final SessionBuilder builder = factory.withOptions();
		final CurrentTenantIdentifierResolver resolver = factory.getCurrentTenantIdentifierResolver();
		if ( resolver != null ) {
			builder.tenantIdentifier( resolver.resolveCurrentTenantIdentifier() );
		}
		return builder;
	}

	protected void validateExistingSession(Session existingSession) {
		final CurrentTenantIdentifierResolver resolver = factory.getCurrentTenantIdentifierResolver();
		if ( resolver != null && resolver.validateExistingCurrentSessions() ) {
			final String current = resolver.resolveCurrentTenantIdentifier();
			if ( ! EqualsHelper.equals( existingSession.getTenantIdentifier(), current ) ) {
				throw new TenantIdentifierMismatchException(
						String.format(
								"Reported current tenant identifier [%s] did not match tenant identifier from " +
										"existing session [%s]",
								current,
								existingSession.getTenantIdentifier()
						)
				);
			}
		}
	}
}
