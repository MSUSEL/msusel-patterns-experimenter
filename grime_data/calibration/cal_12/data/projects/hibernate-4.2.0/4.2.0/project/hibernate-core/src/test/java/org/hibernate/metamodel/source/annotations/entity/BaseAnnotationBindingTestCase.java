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
package org.hibernate.metamodel.source.annotations.entity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.source.internal.MetadataImpl;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * @author Hardy Ferentschik
 */
public abstract class BaseAnnotationBindingTestCase extends BaseUnitTestCase {
	protected MetadataSources sources;
	protected MetadataImpl meta;

	@Rule
	public MethodRule buildMetaData = new MethodRule() {
		@Override
		public Statement apply(final Statement statement, FrameworkMethod frameworkMethod, Object o) {
			return new KeepSetupFailureStatement( statement, frameworkMethod );
		}
	};

	@After
	public void tearDown() {
		sources = null;
		meta = null;
	}

	public EntityBinding getEntityBinding(Class<?> clazz) {
		return meta.getEntityBinding( clazz.getName() );
	}

	public EntityBinding getRootEntityBinding(Class<?> clazz) {
		return meta.getRootEntityBinding( clazz.getName() );
	}

	class KeepSetupFailureStatement extends Statement {
		private final Statement origStatement;
		private final FrameworkMethod origFrameworkMethod;
		private Throwable setupError;
		private boolean expectedException;

		KeepSetupFailureStatement(Statement statement, FrameworkMethod frameworkMethod) {
			this.origStatement = statement;
			this.origFrameworkMethod = frameworkMethod;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				createBindings();
				origStatement.evaluate();
				if ( setupError != null ) {
					throw setupError;
				}
			}
			catch ( Throwable t ) {
				if ( setupError == null ) {
					throw t;
				}
				else {
					if ( !expectedException ) {
						throw setupError;
					}
				}
			}
		}

		private void createBindings() {
			try {
				sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
				Resources resourcesAnnotation = origFrameworkMethod.getAnnotation( Resources.class );
				if ( resourcesAnnotation != null ) {
					sources.getMetadataBuilder().with( resourcesAnnotation.cacheMode() );

					for ( Class<?> annotatedClass : resourcesAnnotation.annotatedClasses() ) {
						sources.addAnnotatedClass( annotatedClass );
					}
					if ( !resourcesAnnotation.ormXmlPath().isEmpty() ) {
						sources.addResource( resourcesAnnotation.ormXmlPath() );
					}
				}
				meta = (MetadataImpl) sources.buildMetadata();
			}
			catch ( final Throwable t ) {
				setupError = t;
				Test testAnnotation = origFrameworkMethod.getAnnotation( Test.class );
				Class<?> expected = testAnnotation.expected();
				if ( t.getClass().equals( expected ) ) {
					expectedException = true;
				}
			}
		}
	}
}


