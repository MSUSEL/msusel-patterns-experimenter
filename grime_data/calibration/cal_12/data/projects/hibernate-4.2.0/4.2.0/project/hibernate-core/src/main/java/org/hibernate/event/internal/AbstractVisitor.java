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
package org.hibernate.event.internal;

import org.hibernate.HibernateException;
import org.hibernate.bytecode.instrumentation.spi.LazyPropertyInitializer;
import org.hibernate.event.spi.EventSource;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

/**
 * Abstract superclass of algorithms that walk
 * a tree of property values of an entity, and
 * perform specific functionality for collections,
 * components and associated entities.
 *
 * @author Gavin King
 */
public abstract class AbstractVisitor {

	private final EventSource session;

	AbstractVisitor(EventSource session) {
		this.session = session;
	}

	/**
	 * Dispatch each property value to processValue().
	 *
	 * @param values
	 * @param types
	 * @throws HibernateException
	 */
	void processValues(Object[] values, Type[] types) throws HibernateException {
		for ( int i=0; i<types.length; i++ ) {
			if ( includeProperty(values, i) ) {
				processValue( i, values, types );
			}
		}
	}
	
	/**
	 * Dispatch each property value to processValue().
	 *
	 * @param values
	 * @param types
	 * @throws HibernateException
	 */
	public void processEntityPropertyValues(Object[] values, Type[] types) throws HibernateException {
		for ( int i=0; i<types.length; i++ ) {
			if ( includeEntityProperty(values, i) ) {
				processValue( i, values, types );
			}
		}
	}
	
	void processValue(int i, Object[] values, Type[] types) {
		processValue( values[i], types[i] );
	}
	
	boolean includeEntityProperty(Object[] values, int i) {
		return includeProperty(values, i);
	}
	
	boolean includeProperty(Object[] values, int i) {
		return values[i]!= LazyPropertyInitializer.UNFETCHED_PROPERTY;
	}

	/**
	 * Visit a component. Dispatch each property
	 * to processValue().
	 * @param component
	 * @param componentType
	 * @throws HibernateException
	 */
	Object processComponent(Object component, CompositeType componentType) throws HibernateException {
		if (component!=null) {
			processValues(
				componentType.getPropertyValues(component, session),
				componentType.getSubtypes()
			);
		}
		return null;
	}

	/**
	 * Visit a property value. Dispatch to the
	 * correct handler for the property type.
	 * @param value
	 * @param type
	 * @throws HibernateException
	 */
	final Object processValue(Object value, Type type) throws HibernateException {

		if ( type.isCollectionType() ) {
			//even process null collections
			return processCollection( value, (CollectionType) type );
		}
		else if ( type.isEntityType() ) {
			return processEntity( value, (EntityType) type );
		}
		else if ( type.isComponentType() ) {
			return processComponent( value, (CompositeType) type );
		}
		else {
			return null;
		}
	}

	/**
	 * Walk the tree starting from the given entity.
	 *
	 * @param object
	 * @param persister
	 * @throws HibernateException
	 */
	void process(Object object, EntityPersister persister)
	throws HibernateException {
		processEntityPropertyValues(
			persister.getPropertyValues( object ),
			persister.getPropertyTypes()
		);
	}

	/**
	 * Visit a collection. Default superclass
	 * implementation is a no-op.
	 * @param collection
	 * @param type
	 * @throws HibernateException
	 */
	Object processCollection(Object collection, CollectionType type)
	throws HibernateException {
		return null;
	}

	/**
	 * Visit a many-to-one or one-to-one associated
	 * entity. Default superclass implementation is
	 * a no-op.
	 * @param value
	 * @param entityType
	 * @throws HibernateException
	 */
	Object processEntity(Object value, EntityType entityType)
	throws HibernateException {
		return null;
	}

	final EventSource getSession() {
		return session;
	}
}
