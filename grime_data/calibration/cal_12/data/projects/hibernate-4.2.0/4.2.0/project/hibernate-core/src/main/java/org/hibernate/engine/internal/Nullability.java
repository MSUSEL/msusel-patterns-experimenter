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
package org.hibernate.engine.internal;

import java.util.Iterator;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.bytecode.instrumentation.spi.LazyPropertyInitializer;
import org.hibernate.engine.spi.CascadingAction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.Type;

/**
 * Implements the algorithm for validating property values for illegal null values
 * 
 * @author Gavin King
 */
public final class Nullability {
	
	private final SessionImplementor session;
	private final boolean checkNullability;

	public Nullability(SessionImplementor session) {
		this.session = session;
		this.checkNullability = session.getFactory().getSettings().isCheckNullability();
	}
	/**
	 * Check nullability of the class persister properties
	 *
	 * @param values entity properties
	 * @param persister class persister
	 * @param isUpdate whether it is intended to be updated or saved
	 * @throws org.hibernate.PropertyValueException Break the nullability of one property
	 * @throws HibernateException error while getting Component values
	 */
	public void checkNullability(
			final Object[] values,
			final EntityPersister persister,
			final boolean isUpdate) 
	throws PropertyValueException, HibernateException {
		/*
		 * Typically when Bean Validation is on, we don't want to validate null values
		 * at the Hibernate Core level. Hence the checkNullability setting.
		 */
		if ( checkNullability ) {
			/*
			  * Algorithm
			  * Check for any level one nullability breaks
			  * Look at non null components to
			  *   recursively check next level of nullability breaks
			  * Look at Collections contraining component to
			  *   recursively check next level of nullability breaks
			  *
			  *
			  * In the previous implementation, not-null stuffs where checked
			  * filtering by level one only updateable
			  * or insertable columns. So setting a sub component as update="false"
			  * has no effect on not-null check if the main component had good checkeability
			  * In this implementation, we keep this feature.
			  * However, I never see any documentation mentioning that, but it's for
			  * sure a limitation.
			  */

			final boolean[] nullability = persister.getPropertyNullability();
			final boolean[] checkability = isUpdate ?
				persister.getPropertyUpdateability() :
				persister.getPropertyInsertability();
			final Type[] propertyTypes = persister.getPropertyTypes();

			for ( int i = 0; i < values.length; i++ ) {

				if ( checkability[i] && values[i]!= LazyPropertyInitializer.UNFETCHED_PROPERTY ) {
					final Object value = values[i];
					if ( !nullability[i] && value == null ) {

						//check basic level one nullablilty
						throw new PropertyValueException(
								"not-null property references a null or transient value",
								persister.getEntityName(),
								persister.getPropertyNames()[i]
							);

					}
					else if ( value != null ) {

						//values is not null and is checkable, we'll look deeper
						String breakProperties = checkSubElementsNullability( propertyTypes[i], value );
						if ( breakProperties != null ) {
							throw new PropertyValueException(
								"not-null property references a null or transient value",
								persister.getEntityName(),
								buildPropertyPath( persister.getPropertyNames()[i], breakProperties )
							);
						}

					}
				}

			}
		}
	}

	/**
	 * check sub elements-nullability. Returns property path that break
	 * nullability or null if none
	 *
	 * @param propertyType type to check
	 * @param value value to check
	 *
	 * @return property path
	 * @throws HibernateException error while getting subcomponent values
	 */
	private String checkSubElementsNullability(final Type propertyType, final Object value) 
	throws HibernateException {
		//for non null args, check for components and elements containing components
		if ( propertyType.isComponentType() ) {
			return checkComponentNullability( value, (CompositeType) propertyType );
		}
		else if ( propertyType.isCollectionType() ) {

			//persistent collections may have components
			CollectionType collectionType = (CollectionType) propertyType;
			Type collectionElementType = collectionType.getElementType( session.getFactory() );
			if ( collectionElementType.isComponentType() ) {
				//check for all components values in the collection

				CompositeType componentType = (CompositeType) collectionElementType;
				Iterator iter = CascadingAction.getLoadedElementsIterator( session, collectionType, value );
				while ( iter.hasNext() ) {
					Object compValue = iter.next();
					if (compValue != null) {
						return checkComponentNullability(compValue, componentType);
					}
				}
			}
		}
		return null;
	}

	/**
	 * check component nullability. Returns property path that break
	 * nullability or null if none
	 *
	 * @param value component properties
	 * @param compType component not-nullable type
	 *
	 * @return property path
	 * @throws HibernateException error while getting subcomponent values
	 */
	private String checkComponentNullability(final Object value, final CompositeType compType)
	throws HibernateException {
		/* will check current level if some of them are not null
		 * or sublevels if they exist
		 */
		boolean[] nullability = compType.getPropertyNullability();
		if ( nullability!=null ) {
			//do the test
			final Object[] values = compType.getPropertyValues( value, EntityMode.POJO );
			final Type[] propertyTypes = compType.getSubtypes();
			for ( int i=0; i<values.length; i++ ) {
				final Object subvalue = values[i];
				if ( !nullability[i] && subvalue==null ) {
					return compType.getPropertyNames()[i];
				}
				else if ( subvalue != null ) {
					String breakProperties = checkSubElementsNullability( propertyTypes[i], subvalue );
					if ( breakProperties != null ) {
						return buildPropertyPath( compType.getPropertyNames()[i], breakProperties );
					}
	 			}
	 		}
		}
		return null;
	}

	/**
	 * Return a well formed property path.
	 * Basically, it will return parent.child
	 *
	 * @param parent parent in path
	 * @param child child in path
	 * @return parent-child path
	 */
	private static String buildPropertyPath(String parent, String child) {
		return new StringBuilder( parent.length() + child.length() + 1 )
			.append(parent).append('.').append(child).toString();
	}

}
