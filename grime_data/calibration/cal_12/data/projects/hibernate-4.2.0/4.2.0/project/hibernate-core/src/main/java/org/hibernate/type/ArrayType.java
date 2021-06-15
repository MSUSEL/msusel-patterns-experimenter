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
package org.hibernate.type;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.collection.internal.PersistentArrayHolder;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * A type for persistent arrays.
 * @author Gavin King
 */
public class ArrayType extends CollectionType {

	private final Class elementClass;
	private final Class arrayClass;

	/**
	 * @deprecated Use {@link #ArrayType(TypeFactory.TypeScope, String, String, Class )} instead.
	 * See Jira issue: <a href="https://hibernate.onjira.com/browse/HHH-7771">HHH-7771</a>
	 */
	@Deprecated
	public ArrayType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Class elementClass, boolean isEmbeddedInXML) {
		super( typeScope, role, propertyRef, isEmbeddedInXML );
		this.elementClass = elementClass;
		arrayClass = Array.newInstance(elementClass, 0).getClass();
	}

	public ArrayType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Class elementClass) {
		super( typeScope, role, propertyRef );
		this.elementClass = elementClass;
		arrayClass = Array.newInstance(elementClass, 0).getClass();
	}

	public Class getReturnedClass() {
		return arrayClass;
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key)
	throws HibernateException {
		return new PersistentArrayHolder(session, persister);
	}

	/**
	 * Not defined for collections of primitive type
	 */
	public Iterator getElementsIterator(Object collection) {
		return Arrays.asList( (Object[]) collection ).iterator();
	}

	public PersistentCollection wrap(SessionImplementor session, Object array) {
		return new PersistentArrayHolder(session, array);
	}

	public boolean isArrayType() {
		return true;
	}

	public String toLoggableString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if ( value == null ) {
			return "null";
		}
		int length = Array.getLength(value);
		List list = new ArrayList(length);
		Type elemType = getElementType(factory);
		for ( int i=0; i<length; i++ ) {
			list.add( elemType.toLoggableString( Array.get(value, i), factory ) );
		}
		return list.toString();
	}
	
	public Object instantiateResult(Object original) {
		return Array.newInstance( elementClass, Array.getLength(original) );
	}

	public Object replaceElements(
		Object original,
		Object target,
		Object owner, 
		Map copyCache, 
		SessionImplementor session)
	throws HibernateException {
		
		int length = Array.getLength(original);
		if ( length!=Array.getLength(target) ) {
			//note: this affects the return value!
			target=instantiateResult(original);
		}
		
		Type elemType = getElementType( session.getFactory() );
		for ( int i=0; i<length; i++ ) {
			Array.set( target, i, elemType.replace( Array.get(original, i), null, session, owner, copyCache ) );
		}
		
		return target;
	
	}

	public Object instantiate(int anticipatedSize) {
		throw new UnsupportedOperationException();
	}

	public Object indexOf(Object array, Object element) {
		int length = Array.getLength(array);
		for ( int i=0; i<length; i++ ) {
			//TODO: proxies!
			if ( Array.get(array, i)==element ) return i;
		}
		return null;
	}

	@Override
	protected boolean initializeImmediately() {
		return true;
	}

	@Override
	public boolean hasHolder() {
		return true;
	}

}
