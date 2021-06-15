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
package org.hibernate.transform;
import java.lang.reflect.Constructor;
import java.util.List;

import org.hibernate.QueryException;

/**
 * Wraps the tuples in a constructor call.
 *
 * todo : why Alias* in the name???
 */
public class AliasToBeanConstructorResultTransformer implements ResultTransformer {

	private final Constructor constructor;

	/**
	 * Instantiates a AliasToBeanConstructorResultTransformer.
	 *
	 * @param constructor The contructor in which to wrap the tuples.
	 */
	public AliasToBeanConstructorResultTransformer(Constructor constructor) {
		this.constructor = constructor;
	}
	
	/**
	 * Wrap the incoming tuples in a call to our configured constructor.
	 */
	public Object transformTuple(Object[] tuple, String[] aliases) {
		try {
			return constructor.newInstance( tuple );
		}
		catch ( Exception e ) {
			throw new QueryException( 
					"could not instantiate class [" + constructor.getDeclaringClass().getName() + "] from tuple",
					e
			);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List transformList(List collection) {
		return collection;
	}

	/**
	 * Define our hashCode by our defined constructor's hasCode.
	 *
	 * @return Our defined ctor hashCode
	 */
	public int hashCode() {
		return constructor.hashCode();
	}

	/**
	 * 2 AliasToBeanConstructorResultTransformer are considered equal if they have the same
	 * defined constructor.
	 *
	 * @param other The other instance to check for equality.
	 * @return True if both have the same defined constuctor; false otherwise.
	 */
	public boolean equals(Object other) {
		return other instanceof AliasToBeanConstructorResultTransformer
				&& constructor.equals( ( ( AliasToBeanConstructorResultTransformer ) other ).constructor );
	}
}
