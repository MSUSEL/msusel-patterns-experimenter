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
package org.hibernate.property;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Represents a "back-reference" to the id of a collection owner.  A "back-reference" is pertinent in mapping scenarios
 * where we have a uni-directional one-to-many association in which only the many side is mapped.  In this case it is
 * the collection itself which is responsible for the FK value.
 * <p/>
 * In this scenario, the one side has no inherent knowledge of its "owner".  So we introduce a synthetic property into
 * the one side to represent the association; a so-called back-reference.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BackrefPropertyAccessor implements PropertyAccessor {

	private final String propertyName;
	private final String entityName;

	// cache these since they are stateless
	private final BackrefSetter setter; // this one could even be static...
	private final BackrefGetter getter;

	/**
	 * A placeholder for a property value, indicating that
	 * we don't know the value of the back reference
	 */
	public static final Serializable UNKNOWN = new Serializable() {
		public String toString() {
			return "<unknown>";
		}

		public Object readResolve() {
			return UNKNOWN;
		}
	};

	/**
	 * Constructs a new instance of BackrefPropertyAccessor.
	 *
	 * @param collectionRole The collection role which this back ref references.
	 * @param entityName The owner's entity name.
	 */
	public BackrefPropertyAccessor(String collectionRole, String entityName) {
		this.propertyName = collectionRole.substring( entityName.length() + 1 );
		this.entityName = entityName;

		this.setter = new BackrefSetter();
		this.getter = new BackrefGetter();
	}

	/**
	 * {@inheritDoc}
	 */
	public Setter getSetter(Class theClass, String propertyName) {
		return setter;
	}

	/**
	 * {@inheritDoc}
	 */
	public Getter getGetter(Class theClass, String propertyName) {
		return getter;
	}


	/**
	 * Internal implementation of a property setter specific to these back-ref properties.
	 */
	public static final class BackrefSetter implements Setter {

		/**
		 * {@inheritDoc}
		 */
		public Method getMethod() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getMethodName() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void set(Object target, Object value, SessionFactoryImplementor factory) {
			// this page intentionally left blank :)
		}

	}


	/**
	 * Internal implementation of a property getter specific to these back-ref properties.
	 */
	public class BackrefGetter implements Getter {

		/**
		 * {@inheritDoc}
		 */
		public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) {
			if ( session == null ) {
				return UNKNOWN;
			}
			else {
				return session.getPersistenceContext().getOwnerId( entityName, propertyName, target, mergeMap );
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public Member getMember() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object get(Object target) {
			return UNKNOWN;
		}

		/**
		 * {@inheritDoc}
		 */
		public Method getMethod() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getMethodName() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Class getReturnType() {
			return Object.class;
		}
	}
}

