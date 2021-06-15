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
package org.hibernate.bytecode.instrumentation.internal;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.bytecode.instrumentation.internal.javassist.JavassistHelper;
import org.hibernate.bytecode.instrumentation.spi.FieldInterceptor;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Helper class for dealing with enhanced entity classes.
 *
 * These operations are expensive.  They are only meant to be used when code does not have access to a
 * SessionFactory (namely from the instrumentation tasks).  When code has access to a SessionFactory,
 * {@link org.hibernate.bytecode.spi.EntityInstrumentationMetadata} should be used instead to query the
 * instrumentation state.  EntityInstrumentationMetadata is accessed from the
 * {@link org.hibernate.persister.entity.EntityPersister} via the
 * {@link org.hibernate.persister.entity.EntityPersister#getInstrumentationMetadata()} method.
 *
 * @author Steve Ebersole
 */
public class FieldInterceptionHelper {
	private static final Set<Delegate> INSTRUMENTATION_DELEGATES = buildInstrumentationDelegates();

	private static Set<Delegate> buildInstrumentationDelegates() {
		HashSet<Delegate> delegates = new HashSet<Delegate>();
		delegates.add( JavassistDelegate.INSTANCE );
		return delegates;
	}

	private FieldInterceptionHelper() {
	}

	public static boolean isInstrumented(Class entityClass) {
		for ( Delegate delegate : INSTRUMENTATION_DELEGATES ) {
			if ( delegate.isInstrumented( entityClass ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInstrumented(Object entity) {
		return entity != null && isInstrumented( entity.getClass() );
	}

	public static FieldInterceptor extractFieldInterceptor(Object entity) {
		if ( entity == null ) {
			return null;
		}
		FieldInterceptor interceptor = null;
		for ( Delegate delegate : INSTRUMENTATION_DELEGATES ) {
			interceptor = delegate.extractInterceptor( entity );
			if ( interceptor != null ) {
				break;
			}
		}
		return interceptor;
	}


	public static FieldInterceptor injectFieldInterceptor(
			Object entity,
	        String entityName,
	        Set uninitializedFieldNames,
	        SessionImplementor session) {
		if ( entity == null ) {
			return null;
		}
		FieldInterceptor interceptor = null;
		for ( Delegate delegate : INSTRUMENTATION_DELEGATES ) {
			interceptor = delegate.injectInterceptor( entity, entityName, uninitializedFieldNames, session );
			if ( interceptor != null ) {
				break;
			}
		}
		return interceptor;
	}

	private static interface Delegate {
		public boolean isInstrumented(Class classToCheck);
		public FieldInterceptor extractInterceptor(Object entity);
		public FieldInterceptor injectInterceptor(Object entity, String entityName, Set uninitializedFieldNames, SessionImplementor session);
	}

	private static class JavassistDelegate implements Delegate {
		public static final JavassistDelegate INSTANCE = new JavassistDelegate();
		public static final String MARKER = "org.hibernate.bytecode.internal.javassist.FieldHandled";

		@Override
		public boolean isInstrumented(Class classToCheck) {
			for ( Class definedInterface : classToCheck.getInterfaces() ) {
				if ( MARKER.equals( definedInterface.getName() ) ) {
					return true;
				}
			}
			return false;
		}

		@Override
		public FieldInterceptor extractInterceptor(Object entity) {
			for ( Class definedInterface : entity.getClass().getInterfaces() ) {
				if ( MARKER.equals( definedInterface.getName() ) ) {
					return JavassistHelper.extractFieldInterceptor( entity );
				}
			}
			return null;
		}

		@Override
		public FieldInterceptor injectInterceptor(
				Object entity,
				String entityName,
				Set uninitializedFieldNames,
				SessionImplementor session) {
			for ( Class definedInterface : entity.getClass().getInterfaces() ) {
				if ( MARKER.equals( definedInterface.getName() ) ) {
					return JavassistHelper.injectFieldInterceptor( entity, entityName, uninitializedFieldNames, session );
				}
			}
			return null;
		}
	}
}
