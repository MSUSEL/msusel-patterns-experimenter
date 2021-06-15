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
package org.hibernate.test.dynamicentity.tuplizer2;
import org.hibernate.EntityNameResolver;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.test.dynamicentity.ProxyHelper;
import org.hibernate.test.dynamicentity.tuplizer.MyEntityInstantiator;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.PojoEntityTuplizer;

/**
 * @author Steve Ebersole
 */
public class MyEntityTuplizer extends PojoEntityTuplizer {

	public MyEntityTuplizer(EntityMetamodel entityMetamodel, PersistentClass mappedEntity) {
		super( entityMetamodel, mappedEntity );
	}

	public MyEntityTuplizer(EntityMetamodel entityMetamodel, EntityBinding mappedEntity) {
		super( entityMetamodel, mappedEntity );
	}

	public EntityNameResolver[] getEntityNameResolvers() {
		return new EntityNameResolver[] { MyEntityNameResolver.INSTANCE };
	}

	protected Instantiator buildInstantiator(PersistentClass persistentClass) {
		return new MyEntityInstantiator( persistentClass.getEntityName() );
	}

	public String determineConcreteSubclassEntityName(Object entityInstance, SessionFactoryImplementor factory) {
		String entityName = ProxyHelper.extractEntityName( entityInstance );
		if ( entityName == null ) {
			entityName = super.determineConcreteSubclassEntityName( entityInstance, factory );
		}
		return entityName;
	}

	protected ProxyFactory buildProxyFactory(PersistentClass persistentClass, Getter idGetter, Setter idSetter) {
		// allows defining a custom proxy factory, which is responsible for
		// generating lazy proxies for a given entity.
		//
		// Here we simply use the default...
		return super.buildProxyFactory( persistentClass, idGetter, idSetter );
	}

	public static class MyEntityNameResolver implements EntityNameResolver {
		public static final MyEntityNameResolver INSTANCE = new MyEntityNameResolver();

		public String resolveEntityName(Object entity) {
			return ProxyHelper.extractEntityName( entity );
		}

		public boolean equals(Object obj) {
			return getClass().equals( obj.getClass() );
		}

		public int hashCode() {
			return getClass().hashCode();
		}
	}
}