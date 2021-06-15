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
package org.hibernate.internal.util;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.bytecode.instrumentation.spi.LazyPropertyInitializer;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

/**
 * Renders entities and query parameters to a nicely readable string.
 * @author Gavin King
 */
public final class EntityPrinter {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, EntityPrinter.class.getName());

    private SessionFactoryImplementor factory;

	/**
	 * Renders an entity to a string.
	 *
	 * @param entityName the entity name
	 * @param entity an actual entity object, not a proxy!
	 * @return the entity rendered to a string
	 */
	public String toString(String entityName, Object entity) throws HibernateException {
		EntityPersister entityPersister = factory.getEntityPersister( entityName );

		if ( entityPersister == null ) {
			return entity.getClass().getName();
		}

		Map<String,String> result = new HashMap<String,String>();

		if ( entityPersister.hasIdentifierProperty() ) {
			result.put(
				entityPersister.getIdentifierPropertyName(),
				entityPersister.getIdentifierType().toLoggableString( entityPersister.getIdentifier( entity ), factory )
			);
		}

		Type[] types = entityPersister.getPropertyTypes();
		String[] names = entityPersister.getPropertyNames();
		Object[] values = entityPersister.getPropertyValues( entity );
		for ( int i=0; i<types.length; i++ ) {
			if ( !names[i].startsWith("_") ) {
				String strValue = values[i]==LazyPropertyInitializer.UNFETCHED_PROPERTY ?
					values[i].toString() :
					types[i].toLoggableString( values[i], factory );
				result.put( names[i], strValue );
			}
		}
		return entityName + result.toString();
	}

	public String toString(Type[] types, Object[] values) throws HibernateException {
		StringBuilder buffer = new StringBuilder();
		for ( int i=0; i<types.length; i++ ) {
			if ( types[i]!=null ) {
				buffer.append( types[i].toLoggableString( values[i], factory ) ).append( ", " );
			}
		}
		return buffer.toString();
	}

	public String toString(Map<String,TypedValue> namedTypedValues) throws HibernateException {
		Map<String,String> result = new HashMap<String,String>();
		for ( Map.Entry<String, TypedValue> entry : namedTypedValues.entrySet() ) {
			result.put(
					entry.getKey(), entry.getValue().getType().toLoggableString(
					entry.getValue().getValue(),
					factory
			)
			);
		}
		return result.toString();
	}

	// Cannot use Map as an argument because it clashes with the previous method (due to type erasure)
	public void toString(Iterable<Map.Entry<EntityKey,Object>> entitiesByEntityKey) throws HibernateException {
        if ( ! LOG.isDebugEnabled() || ! entitiesByEntityKey.iterator().hasNext() ) return;
        LOG.debug( "Listing entities:" );
		int i=0;
		for (  Map.Entry<EntityKey,Object> entityKeyAndEntity : entitiesByEntityKey ) {
			if (i++>20) {
                LOG.debug("More......");
				break;
			}
            LOG.debug( toString( entityKeyAndEntity.getKey().getEntityName(), entityKeyAndEntity.getValue() ) );
		}
	}

	public EntityPrinter(SessionFactoryImplementor factory) {
		this.factory = factory;
	}
}
