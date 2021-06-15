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

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.test.dynamicentity.Address;
import org.hibernate.test.dynamicentity.Company;
import org.hibernate.test.dynamicentity.Customer;
import org.hibernate.test.dynamicentity.Person;
import org.hibernate.test.dynamicentity.ProxyHelper;
import org.hibernate.tuple.Instantiator;

/**
 * @author Steve Ebersole
 */
public class MyEntityInstantiator implements Instantiator {
	private final String entityName;

	public MyEntityInstantiator(String entityName) {
		this.entityName = entityName;
	}

	public Object instantiate(Serializable id) {
		if ( Person.class.getName().equals( entityName ) ) {
			return ProxyHelper.newPersonProxy( id );
		}
		if ( Customer.class.getName().equals( entityName ) ) {
			return ProxyHelper.newCustomerProxy( id );
		}
		else if ( Company.class.getName().equals( entityName ) ) {
			return ProxyHelper.newCompanyProxy( id );
		}
		else if ( Address.class.getName().equals( entityName ) ) {
			return ProxyHelper.newAddressProxy( id );
		}
		else {
			throw new IllegalArgumentException( "unknown entity for instantiation [" + entityName + "]" );
		}
	}

	public Object instantiate() {
		return instantiate( null );
	}

	public boolean isInstance(Object object) {
		try {
			return ReflectHelper.classForName( entityName ).isInstance( object );
		}
		catch( Throwable t ) {
			throw new HibernateException( "could not get handle to entity-name as interface : " + t );
		}
	}
}