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
package org.hibernate.bytecode.spi;

import java.util.Set;

import org.hibernate.bytecode.instrumentation.spi.FieldInterceptor;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Encapsulates bytecode instrumentation information about a particular entity.
 *
 * @author Steve Ebersole
 */
public interface EntityInstrumentationMetadata {
	/**
	 * The name of the entity to which this metadata applies.
	 *
	 * @return The entity name
	 */
	public String getEntityName();

	/**
     * Has the entity class been bytecode instrumented?
	 *
	 * @return {@code true} indicates the entity class is instrumented for Hibernate use; {@code false}
	 * indicates it is not
	 */
    public boolean isInstrumented();

    /**
     * Build and inject a field interceptor instance into the instrumented entity.
	 *
	 * @param entity The entity into which built interceptor should be injected
	 * @param entityName The name of the entity
	 * @param uninitializedFieldNames The name of fields marked as lazy
	 * @param session The session to which the entity instance belongs.
	 *
	 * @return The built and injected interceptor
	 *
	 * @throws NotInstrumentedException Thrown if {@link #isInstrumented()} returns {@code false}
     */
    public FieldInterceptor injectInterceptor(
            Object entity,
            String entityName,
            Set uninitializedFieldNames,
            SessionImplementor session) throws NotInstrumentedException;

    /**
     * Extract the field interceptor instance from the instrumented entity.
	 *
	 * @param entity The entity from which to extract the interceptor
	 *
	 * @return The extracted interceptor
	 *
	 * @throws NotInstrumentedException Thrown if {@link #isInstrumented()} returns {@code false}
	 */
    public FieldInterceptor extractInterceptor(Object entity) throws NotInstrumentedException;
}
