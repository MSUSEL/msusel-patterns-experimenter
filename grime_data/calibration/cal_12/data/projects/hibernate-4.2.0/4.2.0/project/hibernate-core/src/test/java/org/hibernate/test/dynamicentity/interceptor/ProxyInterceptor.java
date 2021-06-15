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
package org.hibernate.test.dynamicentity.interceptor;
import java.io.Serializable;
import java.lang.reflect.Proxy;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.test.dynamicentity.Company;
import org.hibernate.test.dynamicentity.Customer;
import org.hibernate.test.dynamicentity.ProxyHelper;

/**
 * Our custom {@link org.hibernate.Interceptor} impl which performs the
 * interpretation of entity-name -> proxy instance and vice-versa.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class ProxyInterceptor extends EmptyInterceptor {

	/**
	 * The callback from Hibernate to determine the entity name given
	 * a presumed entity instance.
	 *
	 * @param object The presumed entity instance.
	 * @return The entity name (pointing to the proper entity mapping).
	 */
	public String getEntityName(Object object) {
		String entityName = ProxyHelper.extractEntityName( object );
		if ( entityName == null ) {
			entityName = super.getEntityName( object );
		}
		return entityName;
	}

	/**
	 * The callback from Hibernate in order to build an instance of the
	 * entity represented by the given entity name.  Here, we build a
	 * {@link Proxy} representing the entity.
	 *
	 * @param entityName The entity name for which to create an instance.  In our setup,
	 * this is the interface name.
	 * @param entityMode The entity mode in which to create an instance.  Here, we are only
	 * interestes in custom behavior for the POJO entity mode.
	 * @param id The identifier value for the given entity.
	 * @return The instantiated instance.
	 */
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
		if ( entityMode == EntityMode.POJO ) {
			if ( Customer.class.getName().equals( entityName ) ) {
				return ProxyHelper.newCustomerProxy( id );
			}
			else if ( Company.class.getName().equals( entityName ) ) {
				return ProxyHelper.newCompanyProxy( id );
			}
		}
		return super.instantiate( entityName, entityMode, id );
	}

}
