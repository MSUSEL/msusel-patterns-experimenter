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
package org.hibernate.tuple.entity;

import java.util.Set;

import org.hibernate.bytecode.instrumentation.spi.FieldInterceptor;
import org.hibernate.bytecode.spi.EntityInstrumentationMetadata;
import org.hibernate.bytecode.spi.NotInstrumentedException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * @author Steve Ebersole
 */
public class NonPojoInstrumentationMetadata implements EntityInstrumentationMetadata {
	private final String entityName;
	private final String errorMsg;

	public NonPojoInstrumentationMetadata(String entityName) {
		this.entityName = entityName;
		this.errorMsg = "Entity [" + entityName + "] is non-pojo, and therefore not instrumented";
	}

	@Override
	public String getEntityName() {
		return entityName;
	}

	@Override
	public boolean isInstrumented() {
		return false;
	}

	@Override
	public FieldInterceptor extractInterceptor(Object entity) throws NotInstrumentedException {
		throw new NotInstrumentedException( errorMsg );
	}

	@Override
	public FieldInterceptor injectInterceptor(
			Object entity, String entityName, Set uninitializedFieldNames, SessionImplementor session)
			throws NotInstrumentedException {
		throw new NotInstrumentedException( errorMsg );
	}
}
