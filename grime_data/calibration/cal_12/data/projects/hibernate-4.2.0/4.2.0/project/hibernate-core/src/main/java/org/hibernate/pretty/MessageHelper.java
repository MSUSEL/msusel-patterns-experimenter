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
package org.hibernate.pretty;
import java.io.Serializable;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

/**
 * MessageHelper methods for rendering log messages relating to managed
 * entities and collections typically used in log statements and exception
 * messages.
 *
 * @author Max Andersen, Gavin King
 */
public final class MessageHelper {

	private MessageHelper() {
	}


	// entities ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Generate an info message string relating to a particular entity,
	 * based on the given entityName and id.
	 *
	 * @param entityName The defined entity name.
	 * @param id The entity id value.
	 * @return An info string, in the form [FooBar#1].
	 */
	public static String infoString(String entityName, Serializable id) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if( entityName == null ) {
			s.append( "<null entity name>" );
		}
		else {
			s.append( entityName );
		}
		s.append( '#' );

		if ( id == null ) {
			s.append( "<null>" );
		}
		else {
			s.append( id );
		}
		s.append( ']' );

		return s.toString();
	}

	/**
	 * Generate an info message string relating to a particular entity.
	 *
	 * @param persister The persister for the entity
	 * @param id The entity id value
	 * @param factory The session factory
	 * @return An info string, in the form [FooBar#1]
	 */
	public static String infoString(
			EntityPersister persister, 
			Object id, 
			SessionFactoryImplementor factory) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		Type idType;
		if( persister == null ) {
			s.append( "<null EntityPersister>" );
			idType = null;
		}
		else {
			s.append( persister.getEntityName() );
			idType = persister.getIdentifierType();
		}
		s.append( '#' );

		if ( id == null ) {
			s.append( "<null>" );
		}
		else {
			if ( idType == null ) {
				s.append( id );
			}
			else {
				s.append( idType.toLoggableString( id, factory ) );
			}
		}
		s.append( ']' );

		return s.toString();

	}

	/**
	 * Generate an info message string relating to a particular entity,.
	 *
	 * @param persister The persister for the entity
	 * @param id The entity id value
	 * @param identifierType The entity identifier type mapping
	 * @param factory The session factory
	 * @return An info string, in the form [FooBar#1]
	 */
	public static String infoString(
			EntityPersister persister, 
			Object id, 
			Type identifierType,
			SessionFactoryImplementor factory) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if( persister == null ) {
			s.append( "<null EntityPersister>" );
		}
		else {
			s.append( persister.getEntityName() );
		}
		s.append( '#' );

		if ( id == null ) {
			s.append( "<null>" );
		}
		else {
			s.append( identifierType.toLoggableString( id, factory ) );
		}
		s.append( ']' );

		return s.toString();
	}

	/**
	 * Generate an info message string relating to a series of entities.
	 *
	 * @param persister The persister for the entities
	 * @param ids The entity id values
	 * @param factory The session factory
	 * @return An info string, in the form [FooBar#<1,2,3>]
	 */
	public static String infoString(
			EntityPersister persister, 
			Serializable[] ids, 
			SessionFactoryImplementor factory) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if( persister == null ) {
			s.append( "<null EntityPersister>" );
		}
		else {
			s.append( persister.getEntityName() );
			s.append( "#<" );
			for ( int i=0; i<ids.length; i++ ) {
				s.append( persister.getIdentifierType().toLoggableString( ids[i], factory ) );
				if ( i < ids.length-1 ) {
					s.append( ", " );
				}
			}
			s.append( '>' );
		}
		s.append( ']' );

		return s.toString();

	}

	/**
	 * Generate an info message string relating to given entity persister.
	 *
	 * @param persister The persister.
	 * @return An info string, in the form [FooBar]
	 */
	public static String infoString(EntityPersister persister) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if ( persister == null ) {
			s.append( "<null EntityPersister>" );
		}
		else {
			s.append( persister.getEntityName() );
		}
		s.append( ']' );
		return s.toString();
	}

	/**
	 * Generate an info message string relating to a given property value
	 * for an entity.
	 *
	 * @param entityName The entity name
	 * @param propertyName The name of the property
	 * @param key The property value.
	 * @return An info string, in the form [Foo.bars#1]
	 */
	public static String infoString(String entityName, String propertyName, Object key) {
		StringBuilder s = new StringBuilder()
				.append( '[' )
				.append( entityName )
				.append( '.' )
				.append( propertyName )
				.append( '#' );

		if ( key == null ) {
			s.append( "<null>" );
		}
		else {
			s.append( key );
		}
		s.append( ']' );
		return s.toString();
	}


	// collections ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Generate an info message string relating to a particular managed
	 * collection.  Attempts to intelligently handle property-refs issues
	 * where the collection key is not the same as the owner key.
	 *
	 * @param persister The persister for the collection
	 * @param collection The collection itself
	 * @param collectionKey The collection key
	 * @param session The session
	 * @return An info string, in the form [Foo.bars#1]
	 */
	public static String collectionInfoString( 
			CollectionPersister persister,
			PersistentCollection collection,
			Serializable collectionKey,
			SessionImplementor session ) {
		
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if ( persister == null ) {
			s.append( "<unreferenced>" );
		}
		else {
			s.append( persister.getRole() );
			s.append( '#' );
			
			Type ownerIdentifierType = persister.getOwnerEntityPersister()
					.getIdentifierType();
			Serializable ownerKey;
			// TODO: Is it redundant to attempt to use the collectionKey,
			// or is always using the owner id sufficient?
			if ( collectionKey.getClass().isAssignableFrom( 
					ownerIdentifierType.getReturnedClass() ) ) {
				ownerKey = collectionKey;
			} else {
				ownerKey = session.getPersistenceContext()
						.getEntry( collection.getOwner() ).getId();
			}
			s.append( ownerIdentifierType.toLoggableString( 
					ownerKey, session.getFactory() ) );
		}
		s.append( ']' );

		return s.toString();
	}

	/**
	 * Generate an info message string relating to a series of managed
	 * collections.
	 *
	 * @param persister The persister for the collections
	 * @param ids The id values of the owners
	 * @param factory The session factory
	 * @return An info string, in the form [Foo.bars#<1,2,3>]
	 */
	public static String collectionInfoString(
			CollectionPersister persister, 
			Serializable[] ids, 
			SessionFactoryImplementor factory) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if ( persister == null ) {
			s.append( "<unreferenced>" );
		}
		else {
			s.append( persister.getRole() );
			s.append( "#<" );
			for ( int i = 0; i < ids.length; i++ ) {
				addIdToCollectionInfoString( persister, ids[i], factory, s );
				if ( i < ids.length-1 ) {
					s.append( ", " );
				}
			}
			s.append( '>' );
		}
		s.append( ']' );
		return s.toString();
	}

	/**
	 * Generate an info message string relating to a particular managed
	 * collection.
	 *
	 * @param persister The persister for the collection
	 * @param id The id value of the owner
	 * @param factory The session factory
	 * @return An info string, in the form [Foo.bars#1]
	 */
	public static String collectionInfoString(
			CollectionPersister persister, 
			Serializable id, 
			SessionFactoryImplementor factory) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if ( persister == null ) {
			s.append( "<unreferenced>" );
		}
		else {
			s.append( persister.getRole() );
			s.append( '#' );

			if ( id == null ) {
				s.append( "<null>" );
			}
			else {
				addIdToCollectionInfoString( persister, id, factory, s );
			}
		}
		s.append( ']' );

		return s.toString();
	}
	
	private static void addIdToCollectionInfoString(
			CollectionPersister persister,
			Serializable id,
			SessionFactoryImplementor factory,
			StringBuilder s ) {
		// Need to use the identifier type of the collection owner
		// since the incoming is value is actually the owner's id.
		// Using the collection's key type causes problems with
		// property-ref keys.
		// Also need to check that the expected identifier type matches
		// the given ID.  Due to property-ref keys, the collection key
		// may not be the owner key.
		Type ownerIdentifierType = persister.getOwnerEntityPersister()
				.getIdentifierType();
		if ( id.getClass().isAssignableFrom( 
				ownerIdentifierType.getReturnedClass() ) ) {
			s.append( ownerIdentifierType.toLoggableString( id, factory ) );
		} else {
			// TODO: This is a crappy backup if a property-ref is used.
			// If the reference is an object w/o toString(), this isn't going to work.
			s.append( id.toString() );
		}
	}

	/**
	 * Generate an info message string relating to a particular managed
	 * collection.
	 *
	 * @param role The role-name of the collection
	 * @param id The id value of the owner
	 * @return An info string, in the form [Foo.bars#1]
	 */
	public static String collectionInfoString(String role, Serializable id) {
		StringBuilder s = new StringBuilder();
		s.append( '[' );
		if( role == null ) {
			s.append( "<unreferenced>" );
		}
		else {
			s.append( role );
			s.append( '#' );

			if ( id == null ) {
				s.append( "<null>" );
			}
			else {
				s.append( id );
			}
		}
		s.append( ']' );
		return s.toString();
	}

}
