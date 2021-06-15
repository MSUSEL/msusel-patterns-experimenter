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
package org.hibernate.bytecode.internal.javassist;

import java.io.Serializable;

import org.hibernate.PropertyAccessException;
import org.hibernate.bytecode.spi.ReflectionOptimizer;

/**
 * The {@link org.hibernate.bytecode.spi.ReflectionOptimizer.AccessOptimizer} implementation for Javassist
 * which simply acts as an adapter to the {@link BulkAccessor} class.
 *
 * @author Steve Ebersole
 */
public class AccessOptimizerAdapter implements ReflectionOptimizer.AccessOptimizer, Serializable {

	public static final String PROPERTY_GET_EXCEPTION =
		"exception getting property value with Javassist (set hibernate.bytecode.use_reflection_optimizer=false for more info)";

	public static final String PROPERTY_SET_EXCEPTION =
		"exception setting property value with Javassist (set hibernate.bytecode.use_reflection_optimizer=false for more info)";

	private final BulkAccessor bulkAccessor;
	private final Class mappedClass;

	public AccessOptimizerAdapter(BulkAccessor bulkAccessor, Class mappedClass) {
		this.bulkAccessor = bulkAccessor;
		this.mappedClass = mappedClass;
	}

	public String[] getPropertyNames() {
		return bulkAccessor.getGetters();
	}

	public Object[] getPropertyValues(Object object) {
		try {
			return bulkAccessor.getPropertyValues( object );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException(
					t,
			        PROPERTY_GET_EXCEPTION,
			        false,
			        mappedClass,
			        getterName( t, bulkAccessor )
				);
		}
	}

	public void setPropertyValues(Object object, Object[] values) {
		try {
			bulkAccessor.setPropertyValues( object, values );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException(
					t,
			        PROPERTY_SET_EXCEPTION,
			        true,
			        mappedClass,
			        setterName( t, bulkAccessor )
			);
		}
	}

	private static String setterName(Throwable t, BulkAccessor accessor) {
		if (t instanceof BulkAccessorException ) {
			return accessor.getSetters()[ ( (BulkAccessorException) t ).getIndex() ];
		}
		else {
			return "?";
		}
	}

	private static String getterName(Throwable t, BulkAccessor accessor) {
		if (t instanceof BulkAccessorException ) {
			return accessor.getGetters()[ ( (BulkAccessorException) t ).getIndex() ];
		}
		else {
			return "?";
		}
	}
}
